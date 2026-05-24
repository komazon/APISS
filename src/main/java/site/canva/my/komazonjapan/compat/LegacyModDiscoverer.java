package site.canva.my.komazonjapan.compat;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.canva.my.komazonjapan.compat.loader.LegacyClassLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 互換レイヤー: 1.12.2 Mod を Windowsでは AppData Roaming の
 * `\.minecraft\mods\modsapi\` から検出し、LegacyClassLoader で読み込んで
 * 既存のライフサイクル／レジストリブリッジに登録する。
 */
public class LegacyModDiscoverer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyModDiscoverer.class);
    private static final String DEFAULT_LEGACY_MOD_DIR = ".minecraft/mods/modsapi";
    private static final String LEGACY_MOD_PATH_PROPERTY = "legacy.mods.path";

    private final LegacyModLifecycleBridge lifecycleBridge;
    private final LegacyRegistryBridge registryBridge;
    private final Path modsApiPath;
    private final List<LegacyClassLoader> activeLoaders = new ArrayList<>();

    public LegacyModDiscoverer(LegacyModLifecycleBridge lifecycleBridge,
                               LegacyRegistryBridge registryBridge) {
        this.lifecycleBridge = lifecycleBridge;
        this.registryBridge = registryBridge;
        this.modsApiPath = resolveLegacyModsPath();
    }

    private Path resolveLegacyModsPath() {
        String override = System.getProperty(LEGACY_MOD_PATH_PROPERTY);
        if (override != null && !override.isBlank()) {
            return Path.of(override);
        }

        String appData = System.getenv("APPDATA");
        if (appData != null && !appData.isBlank()) {
            Path windowsPath = Path.of(appData, ".minecraft", "mods", "modsapi");
            if (Files.exists(windowsPath)) {
                return windowsPath;
            }
        }

        return Path.of(System.getProperty("user.home"), DEFAULT_LEGACY_MOD_DIR.split("/"));
    }

    public void discoverLegacyMods() {
        if (!Files.exists(modsApiPath) || !Files.isDirectory(modsApiPath)) {
            LOGGER.warn("[互換レイヤー] 旧Modフォルダーが見つかりません: {}", modsApiPath);
            return;
        }

        LOGGER.info("[互換レイヤー] 古い1.12.2 Mod を検出: {}", modsApiPath);
        try {
            List<Path> jarPaths = new ArrayList<>();
            Files.list(modsApiPath)
                    .filter(path -> path.toString().toLowerCase().endsWith(".jar"))
                    .forEach(jarPaths::add);

            if (jarPaths.isEmpty()) {
                LOGGER.warn("[互換レイヤー] 旧Mod JAR が見つかりませんでした: {}", modsApiPath);
                return;
            }

            LegacyClassLoader classLoader = new LegacyClassLoader(jarPaths, getClass().getClassLoader());
            activeLoaders.add(classLoader);

            List<LegacyModDescriptor> descriptors = new ArrayList<>();
            for (Path jarPath : jarPaths) {
                loadLegacyModJar(classLoader, jarPath, descriptors);
            }

            for (LegacyModDescriptor descriptor : sortLegacyMods(descriptors)) {
                registerLegacyModDescriptor(descriptor);
            }
        } catch (IOException e) {
            LOGGER.error("[互換レイヤー] Legacy mod ディレクトリのスキャンに失敗しました", e);
        }
    }

    private void loadLegacyModJar(LegacyClassLoader classLoader, Path jarPath, List<LegacyModDescriptor> descriptors) {
        LOGGER.info("[互換レイヤー] 旧Mod JAR をロード: {}", jarPath.getFileName());
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            List<String> modClasses = findLegacyModClasses(jarFile);
            if (modClasses.isEmpty()) {
                LOGGER.warn("[互換レイヤー] 1.12.2 Modクラスが見つかりませんでした: {}", jarPath);
                return;
            }

            for (String className : modClasses) {
                collectLegacyModDescriptor(classLoader, jarFile, className, descriptors);
            }
        } catch (IOException e) {
            LOGGER.error("[互換レイヤー] 旧Mod JAR の読み込みに失敗: {}", jarPath, e);
        }
    }

    private void collectLegacyModDescriptor(LegacyClassLoader classLoader, JarFile jarFile,
                                            String className, List<LegacyModDescriptor> descriptors) {
        try {
            Class<?> modClass = classLoader.loadClass(className);
            Mod modAnnotation = modClass.getAnnotation(Mod.class);
            if (modAnnotation == null) {
                LOGGER.warn("[互換レイヤー] {} は @Mod アノテーションが見つからないためスキップします。", className);
                return;
            }

            String modId = modAnnotation.modid();
            Object instance = instantiateLegacyMod(modClass);
            if (instance == null) {
                LOGGER.warn("[互換レイヤー] {} のインスタンス生成に失敗しました。", className);
                return;
            }

            FMLPreInitializationEvent.ModMetadataStub metadata = new FMLPreInitializationEvent.ModMetadataStub(modId);
            metadata.name = modAnnotation.name().isEmpty() ? modId : modAnnotation.name();
            metadata.version = modAnnotation.version();
            metadata.dependencies = modAnnotation.dependencies();
            metadata.requiredMods = modAnnotation.requiredMods();
            metadata.description = "";
            parseMcmodInfo(jarFile, metadata);

            injectModInstanceField(modClass, instance);

            List<String> dependencies = parseModDependencies(modAnnotation.dependencies(), modAnnotation.requiredMods());
            descriptors.add(new LegacyModDescriptor(modId, dependencies, modClass, instance, metadata));
            LOGGER.info("[互換レイヤー] 旧Mod を検出・解析: {} (dependencies={})", modId, dependencies);
        } catch (ClassNotFoundException e) {
            LOGGER.error("[互換レイヤー] 旧Modクラスのロードに失敗: {}", className, e);
        }
    }

    private List<LegacyModDescriptor> sortLegacyMods(List<LegacyModDescriptor> descriptors) {
        Map<String, LegacyModDescriptor> descriptorById = new HashMap<>();
        for (LegacyModDescriptor descriptor : descriptors) {
            descriptorById.put(descriptor.modId(), descriptor);
        }

        List<LegacyModDescriptor> sorted = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> inStack = new HashSet<>();

        for (LegacyModDescriptor descriptor : descriptors) {
            sortDescriptor(descriptor, descriptorById, sorted, visited, inStack);
        }

        return sorted;
    }

    private void sortDescriptor(LegacyModDescriptor descriptor,
                                Map<String, LegacyModDescriptor> descriptorById,
                                List<LegacyModDescriptor> sorted,
                                Set<String> visited,
                                Set<String> inStack) {
        if (visited.contains(descriptor.modId())) {
            return;
        }
        if (inStack.contains(descriptor.modId())) {
            LOGGER.warn("[互換レイヤー] 依存関係ループを検出: {}", descriptor.modId());
            return;
        }

        inStack.add(descriptor.modId());
        for (String dependency : descriptor.dependencies()) {
            LegacyModDescriptor dep = descriptorById.get(dependency);
            if (dep == null) {
                LOGGER.debug("[互換レイヤー] 未検出の依存Mod: {} を {} が参照", dependency, descriptor.modId());
                continue;
            }
            sortDescriptor(dep, descriptorById, sorted, visited, inStack);
        }
        inStack.remove(descriptor.modId());
        visited.add(descriptor.modId());
        sorted.add(descriptor);
    }

    private List<String> parseModDependencies(String dependencies, String requiredMods) {
        List<String> parsed = new ArrayList<>();
        for (String raw : List.of(dependencies, requiredMods)) {
            if (raw == null || raw.isBlank()) {
                continue;
            }
            for (String token : raw.split(";")) {
                token = token.strip();
                if (token.isEmpty()) {
                    continue;
                }
                int colonIndex = token.indexOf(':');
                if (colonIndex >= 0) {
                    token = token.substring(colonIndex + 1);
                }
                int atIndex = token.indexOf('@');
                if (atIndex >= 0) {
                    token = token.substring(0, atIndex);
                }
                token = token.strip();
                if (!token.isEmpty()) {
                    parsed.add(token);
                }
            }
        }
        return parsed;
    }

    private void parseMcmodInfo(JarFile jarFile, FMLPreInitializationEvent.ModMetadataStub metadata) {
        try {
            JarEntry entry = jarFile.getJarEntry("mcmod.info");
            if (entry == null) {
                return;
            }
            try (InputStream stream = jarFile.getInputStream(entry)) {
                String json = new String(stream.readAllBytes());
                FMLPreInitializationEvent.ModMetadataStub parsed = FMLPreInitializationEvent.ModMetadataStub.fromJson(json, metadata.modId);
                metadata.name = parsed.name;
                if (!parsed.version.isEmpty()) {
                    metadata.version = parsed.version;
                }
                if (!parsed.description.isEmpty()) {
                    metadata.description = parsed.description;
                }
                if (!parsed.dependencies.isEmpty()) {
                    metadata.dependencies = parsed.dependencies;
                }
                if (!parsed.requiredMods.isEmpty()) {
                    metadata.requiredMods = parsed.requiredMods;
                }
            }
        } catch (IOException e) {
            LOGGER.warn("[互換レイヤー] mcmod.info の読み取りに失敗しました: {}", e.getMessage());
        }
    }

    private void registerLegacyModDescriptor(LegacyModDescriptor descriptor) {
        LOGGER.info("[互換レイヤー] 旧Mod を互換レイヤーに登録: {}", descriptor.modId());
        lifecycleBridge.registerLegacyMod(descriptor.modClass(), descriptor.modInstance(), descriptor.metadata());
        registryBridge.registerLegacyMod(descriptor.modClass(), descriptor.modInstance());
        new net.minecraftforge.fml.common.ModContainer(
                descriptor.modId(), descriptor.metadata().name, descriptor.metadata().version);
    }

    private record LegacyModDescriptor(
            String modId,
            List<String> dependencies,
            Class<?> modClass,
            Object modInstance,
            FMLPreInitializationEvent.ModMetadataStub metadata
    ) {}

    private List<String> findLegacyModClasses(JarFile jarFile) throws IOException {
        List<String> modClassNames = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                continue;
            }
            try (InputStream stream = jarFile.getInputStream(entry)) {
                if (isLegacyModClass(stream)) {
                    String className = entry.getName().replace('/', '.').replaceAll("\\.class$", "");
                    modClassNames.add(className);
                    LOGGER.info("[互換レイヤー] @Mod クラスを検出: {}", className);
                }
            }
        }
        return modClassNames;
    }

    private boolean isLegacyModClass(InputStream inputStream) throws IOException {
        ClassReader reader = new ClassReader(inputStream);
        LegacyModAnnotationVisitor visitor = new LegacyModAnnotationVisitor();
        reader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
        return visitor.isLegacyMod();
    }

    private Object instantiateLegacyMod(Class<?> modClass) {
        try {
            Constructor<?> constructor = modClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            LOGGER.error("[互換レイヤー] 旧Modクラスのインスタンス生成に失敗: {}", modClass.getName(), e);
            return null;
        }
    }

    private void injectModInstanceField(Class<?> modClass, Object instance) {
        for (Field field : modClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(net.minecraftforge.fml.common.Mod.Instance.class)) {
                try {
                    field.setAccessible(true);
                    field.set(null, instance);
                    LOGGER.debug("[互換レイヤー] @Mod.Instance を注入: {}#{}", modClass.getName(), field.getName());
                } catch (IllegalAccessException e) {
                    LOGGER.warn("[互換レイヤー] @Mod.Instance の注入に失敗: {}#{}", modClass.getName(), field.getName(), e);
                }
            }
        }
    }

    private static class LegacyModAnnotationVisitor extends ClassVisitor {
        private boolean legacyMod;

        public LegacyModAnnotationVisitor() {
            super(Opcodes.ASM9);
        }

        public boolean isLegacyMod() {
            return legacyMod;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if ("Lnet/minecraftforge/fml/common/Mod;".equals(descriptor)) {
                legacyMod = true;
            }
            return null;
        }
    }
}

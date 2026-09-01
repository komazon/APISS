package site.canva.my.komazonjapan.compat.loader;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import site.canva.my.komazonjapan.compat.asm.LegacyClassTransformer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 互換レイヤー: 1.12.2 Mod JAR を読み込み、クラス定義時に ASM で互換性変換を適用する ClassLoader。
 */
public class LegacyClassLoader extends URLClassLoader {

    private final Map<String, byte[]> classBytes = new HashMap<>();

    public LegacyClassLoader(Path jarPath, ClassLoader parent) throws IOException {
        this(List.of(jarPath), parent);
    }

    public LegacyClassLoader(List<Path> jarPaths, ClassLoader parent) throws IOException {
        super(jarPaths.stream().map(path -> {
            try {
                return path.toUri().toURL();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toArray(URL[]::new), parent);
        loadJarContents(jarPaths);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = classBytes.get(name);
        if (bytes != null) {
            try {
                byte[] transformed = LegacyClassTransformer.transform(bytes, this);
                return defineClass(name, transformed, 0, transformed.length);
            } catch (IOException e) {
                throw new ClassNotFoundException("Failed to transform legacy class: " + name, e);
            }
        }

        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            if (isCompatStubCandidate(name)) {
                byte[] stubBytes = generateCompatStub(name);
                return defineClass(name, stubBytes, 0, stubBytes.length);
            }
            throw e;
        }
    }

    private static boolean isCompatStubCandidate(String className) {
        return className.startsWith("net.minecraftforge.compat.") || 
               className.startsWith("net.minecraftforge.common.") || 
               className.startsWith("net.minecraftforge.fml.");
    }

    private static byte[] generateCompatStub(String className) {
        String internalName = className.replace('.', '/');
        int access = Opcodes.ACC_PUBLIC;
        boolean interfaceStub = isInterfaceName(className);
        if (interfaceStub) {
            access |= Opcodes.ACC_ABSTRACT | Opcodes.ACC_INTERFACE;
        }

        String superName = "java/lang/Object";
        if (!interfaceStub) {
            superName = determineSuperClass(internalName);
        }

        ClassWriter writer = new ClassWriter(0);
        writer.visit(Opcodes.V1_8, access, internalName, superName, 
                interfaceStub ? null : "java/lang/Object", interfaceStub ? null : new String[0]);
        writer.visitEnd();
        return writer.toByteArray();
    }

    private static String determineSuperClass(String internalName) {
        if (internalName.contains("EntityMob")) return "net/minecraftforge/compat/entity/EntityCreature";
        if (internalName.contains("EntityCreature")) return "net/minecraftforge/compat/entity/LivingEntity";
        if (internalName.contains("LivingEntity")) return "net/minecraftforge/compat/entity/Entity";
        if (internalName.contains("Entity")) return "java/lang/Object";
        return "java/lang/Object";
    }

    private static boolean isInterfaceName(String className) {
        String simpleName = className.substring(className.lastIndexOf('.') + 1);
        return simpleName.length() > 1 && simpleName.charAt(0) == 'I' && Character.isUpperCase(simpleName.charAt(1));
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> loaded = findLoadedClass(name);
            if (loaded != null) {
                return loaded;
            }

            if (classBytes.containsKey(name)) {
                try {
                    Class<?> clazz = findClass(name);
                    if (resolve) {
                        resolveClass(clazz);
                    }
                    return clazz;
                } catch (ClassNotFoundException ignored) {
                    // Fall through to parent delegation if transformation/load fails.
                }
            }

            Class<?> parentClass = super.loadClass(name, false);
            if (resolve) {
                resolveClass(parentClass);
            }
            return parentClass;
        }
    }

    @Override
    public URL getResource(String name) {
        URL resource = super.getResource(name);
        if (resource != null) {
            return resource;
        }
        return super.findResource(name);
    }

    private void loadJarContents(List<Path> jarPaths) throws IOException {
        for (Path jarPath : jarPaths) {
            try (JarFile jarFile = new JarFile(jarPath.toFile())) {
                for (JarEntry entry : java.util.Collections.list(jarFile.entries())) {
                    if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                        continue;
                    }
                    String className = entry.getName().replace('/', '.').replaceAll("\\.class$", "");
                    try (InputStream stream = jarFile.getInputStream(entry)) {
                        byte[] bytes = stream.readAllBytes();
                        classBytes.put(className, bytes);
                    }
                }
            }
        }
    }
}

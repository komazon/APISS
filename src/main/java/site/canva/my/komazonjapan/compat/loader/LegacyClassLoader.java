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
        if (bytes == null) {
            return super.findClass(name);
        }

        try {
            byte[] transformed = LegacyClassTransformer.transform(bytes);
            return defineClass(name, transformed, 0, transformed.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Failed to transform legacy class: " + name, e);
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

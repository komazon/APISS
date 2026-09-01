package site.canva.my.komazonjapan.compat.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;

import java.io.IOException;

/**
 * 互換レイヤー: 1.12.2 クラスのバイトコードを ASM でリマップする。
 */
public class LegacyClassTransformer {

    public static byte[] transform(byte[] original, ClassLoader loader) throws IOException {
        ClassReader reader = new ClassReader(original);
        ClassWriter writer = new LegacyClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS, loader);
        ClassRemapper remapper = new ClassRemapper(writer, new LegacyRemapper());
        reader.accept(remapper, ClassReader.EXPAND_FRAMES);
        return writer.toByteArray();
    }

    private static final class LegacyClassWriter extends ClassWriter {
        private final ClassLoader loader;

        private LegacyClassWriter(ClassReader reader, int flags, ClassLoader loader) {
            super(reader, flags);
            this.loader = loader;
        }

        @Override
        protected String getCommonSuperClass(String type1, String type2) {
            try {
                Class<?> class1 = Class.forName(type1.replace('/', '.'), false, loader);
                Class<?> class2 = Class.forName(type2.replace('/', '.'), false, loader);
                if (class1.isAssignableFrom(class2)) {
                    return type1;
                }
                if (class2.isAssignableFrom(class1)) {
                    return type2;
                }
                if (class1.isInterface() || class2.isInterface()) {
                    return "java/lang/Object";
                }
                while (!class1.isAssignableFrom(class2)) {
                    class1 = class1.getSuperclass();
                }
                return class1.getName().replace('.', '/');
            } catch (ClassNotFoundException e) {
                return "java/lang/Object";
            }
        }
    }
}

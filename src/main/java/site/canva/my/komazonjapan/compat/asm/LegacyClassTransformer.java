package site.canva.my.komazonjapan.compat.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;

import java.io.IOException;

/**
 * 互換レイヤー: 1.12.2 クラスのバイトコードを ASM でリマップする。
 */
public class LegacyClassTransformer {

    public static byte[] transform(byte[] original) throws IOException {
        ClassReader reader = new ClassReader(original);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        ClassRemapper remapper = new ClassRemapper(writer, new LegacyRemapper());
        reader.accept(remapper, ClassReader.EXPAND_FRAMES);
        return writer.toByteArray();
    }
}

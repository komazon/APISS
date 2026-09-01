package net.minecraftforge.compat.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 1.12.2 の NonNullList をエミュレートする互換クラス。
 * 現代の NeoForge では ArrayList や他のコレクションが使用されますが、
 * レガシーModが期待するインターフェースを提供します。
 */
public class NonNullList<E> extends ArrayList<E> {
    
    public NonNullList() {
        super();
    }

    public NonNullList(int initialCapacity) {
        super(initialCapacity);
    }

    public NonNullList(Supplier<E> defaultSupplier) {
        super();
        // 実際にはデフォルトサプライアによる埋め合わせが必要ですが、
        // 最小限の互換性のために空のリストとして初期化します。
    }

    /**
     * 1.12.2 の func_191196_a() に相当するメソッド。
     * LegacyRemapper によってこのメソッドへの呼び出しは 'create' にリマップされます。
     */
    public static <T> NonNullList<T> create() {
        return new NonNullList<>();
    }

    @Override
    public E set(int index, E element) {
        if (element == null) {
            throw new IllegalArgumentException("NonNullList cannot contain null elements");
        }
        return super.set(index, element);
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new IllegalArgumentException("NonNullList cannot contain null elements");
        }
        return super.add(e);
    }
}

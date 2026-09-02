package net.minecraftforge.compat.block.state;

/**
 * 互換レイヤー：1.12.2 の BlockStateContainer のダミー実装。
 * 
 * 1.12.2 Mod は Block.registerBlockState() などで
 * BlockStateContainer を使用してブロックの状態を定義する。
 * 
 * このスタブは実際の状態管理を行わず、1.12.2 Mod がクラスロード時に
 * NoClassDefFoundError を起こさないために存在する。
 */
public class BlockStateContainer {
    
    private final Object block;
    private final Object[] properties;
    
    public BlockStateContainer(Object block, Object... properties) {
        this.block = block;
        this.properties = properties;
    }
    
    public Object getBlock() {
        return block;
    }
    
    public Object[] getProperties() {
        return properties;
    }
}

package net.minecraftforge.compat.item;

import net.minecraft.world.item.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2の Item クラスのブリッジ実装。
 *
 * 1.12.2では:
 *   new Item()
 *   item.setRegistryName("mymod", "my_item")
 *   item.setUnlocalizedName("my_item")
 *   item.setMaxStackSize(1)
 *   item.setMaxDamage(250)
 *
 * このクラスが旧スタイルのメソッドチェーンを受け取り、
 * 内部で現代の Item として動作するよう変換する。
 */
public class LegacyItem extends Item {

    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyItem.class);

    /** IForgeRegistryが登録名を取り出すために参照するフィールド */
    String _legacyRegistryName = null;

    String _unlocalizedName = null;

    // ─── 1.12.2スタイルのコンストラクタ ───

    /**
     * 1.12.2の new Item() に相当するデフォルトコンストラクタ。
     * 現代の Item.Properties() を内部で生成する。
     */
    public LegacyItem() {
        super(new Item.Properties());
        LOGGER.debug("[互換レイヤー] LegacyItem 生成");
    }

    /**
     * 現代のPropertiesを直接受け取るコンストラクタ（サブクラス用）。
     */
    protected LegacyItem(Item.Properties properties) {
        super(properties);
    }

    // ─── 1.12.2のメソッドチェーン ───

    public LegacyItem setRegistryName(String domain, String path) {
        this._legacyRegistryName = domain + ":" + path;
        LOGGER.debug("[互換レイヤー] Item RegistryName 設定: {}", this._legacyRegistryName);
        return this;
    }

    public LegacyItem setRegistryName(String registryName) {
        this._legacyRegistryName = registryName;
        return this;
    }

    public LegacyItem setUnlocalizedName(String name) {
        this._unlocalizedName = name;
        return this;
    }

    /**
     * 1.12.2の setMaxStackSize() — 現代では Properties で設定するが、
     * このメソッドはスタブとして受け取るだけ（後続のPhaseで対応）。
     */
    public LegacyItem setMaxStackSize(int maxStackSize) {
        LOGGER.debug("[互換レイヤー] setMaxStackSize({}) は現在のPhaseでは無視されます", maxStackSize);
        return this;
    }

    /**
     * 1.12.2の setMaxDamage() — 耐久値設定。スタブ。
     */
    public LegacyItem setMaxDamage(int maxDamage) {
        LOGGER.debug("[互換レイヤー] setMaxDamage({}) は現在のPhaseでは無視されます", maxDamage);
        return this;
    }

    public String getRegistryName() {
        return _legacyRegistryName;
    }
}

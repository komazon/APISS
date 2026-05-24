package net.minecraftforge.registries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 互換レイヤー: 1.12.2の IForgeRegistry<T> のダミー実装。
 *
 * 1.12.2のModは RegistryEvent.Register<Block> などのイベントで
 * event.getRegistry().register(myBlock) を呼び出して登録する。
 *
 * このダミー実装は register() を呼ばれた時点では何もせず、
 * 内部リスト(pendingEntries)にオブジェクトを溜め込む。
 * 現代の RegisterEvent が発火した時に LegacyRegistryBridge が
 * この内部リストを読み出して現代のレジストリに流し込む。
 *
 * 設計書「実装手順 3: レジストリ・ブリッジ」に相当。
 */
public class IForgeRegistry<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IForgeRegistry.class);

    /** 登録待ちエントリ。ResourceLocation文字列 → オブジェクト の順序保証マップ */
    private final Map<String, T> pendingEntries = new LinkedHashMap<>();

    private final String registryName;

    public IForgeRegistry(String registryName) {
        this.registryName = registryName;
    }

    /**
     * 1.12.2 Modが呼び出す登録メソッド。
     * オブジェクトのRegistryNameを読み取り、pendingEntriesに追加するだけ。
     * 実際のゲームへの登録は LegacyRegistryBridge が行う。
     *
     * @param entry setRegistryName() で名前が設定済みのオブジェクト
     */
    public void register(T entry) {
        String regName = getRegistryName(entry);
        if (regName == null) {
            LOGGER.error("[互換レイヤー] register() されたオブジェクトに RegistryName がありません: {}",
                    entry.getClass().getName());
            return;
        }
        pendingEntries.put(regName, entry);
        LOGGER.debug("[互換レイヤー] {} レジストリに追加 (保留中): {}", registryName, regName);
    }

    /**
     * 複数のエントリを一括登録する。
     * 1.12.2の GameRegistry.registerAll() の模倣。
     */
    @SafeVarargs
    public final void registerAll(T... entries) {
        for (T entry : entries) {
            register(entry);
        }
    }

    /** 登録待ちエントリを返す。LegacyRegistryBridge が呼び出す。 */
    public Map<String, T> getPendingEntries() {
        return pendingEntries;
    }

    public String getRegistryName() {
        return registryName;
    }

    /**
     * オブジェクトから RegistryName を取り出す。
     * 現代のBlock/Itemはbuilt-inレジストリで管理されるため、
     * 1.12.2の setRegistryName() 相当のフィールドをリフレクションで探す。
     */
    private String getRegistryName(T entry) {
        try {
            // LegacyBlock / LegacyItem が持つ内部フィールドを参照
            var field = entry.getClass().getDeclaredField("_legacyRegistryName");
            field.setAccessible(true);
            return (String) field.get(entry);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // フィールドがない場合はクラス名から推測（フォールバック）
            LOGGER.warn("[互換レイヤー] _legacyRegistryName フィールドが見つかりません: {}。クラス名で代替します。",
                    entry.getClass().getSimpleName());
            return entry.getClass().getSimpleName().toLowerCase();
        }
    }
}

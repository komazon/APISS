package net.minecraftforge.fml.common.event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 互換レイヤー: 1.12.2の FMLPreInitializationEvent のダミー実装。
 *
 * 1.12.2 Modは @EventHandler の付いたメソッドでこのイベントを受け取り、
 * レジストリ登録の準備やコンフィグ読み込みを行う。
 *
 * 互換レイヤーの LegacyModLifecycleBridge が現代の FMLCommonSetupEvent 発火時に
 * このインスタンスを生成し、1.12.2Modのメソッドへ渡す。
 */
public class FMLPreInitializationEvent extends FMLStateEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(FMLPreInitializationEvent.class);

    // 1.12.2ではModの設定ファイルディレクトリを提供していた
    private final java.io.File configDir;

    private final ModMetadataStub metadata;

    public FMLPreInitializationEvent(String modId, java.io.File configDir, ModMetadataStub metadata) {
        super(modId);
        this.configDir = configDir;
        this.metadata = metadata != null ? metadata : new ModMetadataStub(modId);
        LOGGER.debug("[互換レイヤー] FMLPreInitializationEvent を生成: modId={}", modId);
    }

    /**
     * 1.12.2 Modがコンフィグファイルのディレクトリを取得するために呼ぶ。
     * 現代では config/ フォルダへのパスを返す。
     */
    public java.io.File getModConfigurationDirectory() {
        return configDir;
    }

    /**
     * 1.12.2 Modが自分のmodidを確認するために呼ぶことがある。
     */
    public String getModId() {
        return super.getModId();
    }

    /**
     * 1.12.2 Modが自分のModMetadataを取得するために呼ぶ。
     * 互換レイヤーでは最低限の情報だけ返すスタブを返す。
     */
    public ModMetadataStub getModMetadata() {
        return metadata;
    }

    // ─── 1.12.2でよく使われたが互換レイヤーではスタブとして処理 ───

    /**
     * ModMetadataの最低限のスタブ。
     * 1.12.2の mcmod.info から読まれる情報を模倣する。
     */
    public static class ModMetadataStub {
        public final String modId;
        public String name = "";
        public String version = "";
        public String description = "";
        public String dependencies = "";
        public String requiredMods = "";

        public ModMetadataStub(String modId) {
            this.modId = modId;
        }

        public static ModMetadataStub fromJson(String json, String modId) {
            ModMetadataStub metadata = new ModMetadataStub(modId);
            if (json == null || json.isBlank()) {
                return metadata;
            }
            String normalized = json.replace("\r", "").replace("\n", " ");
            metadata.name = findJsonString(normalized, "name", metadata.name);
            metadata.version = findJsonString(normalized, "version", metadata.version);
            metadata.description = findJsonString(normalized, "description", metadata.description);
            metadata.dependencies = findJsonString(normalized, "dependencies", metadata.dependencies);
            metadata.requiredMods = findJsonString(normalized, "requiredMods", metadata.requiredMods);
            return metadata;
        }

        private static String findJsonString(String json, String key, String fallback) {
            String pattern = "\"" + key + "\"\\s*:\\s*\"";
            int start = json.indexOf(pattern);
            if (start < 0) {
                return fallback;
            }
            start += pattern.length();
            int end = json.indexOf('"', start);
            if (end < 0) {
                return fallback;
            }
            return json.substring(start, end);
        }
    }
}

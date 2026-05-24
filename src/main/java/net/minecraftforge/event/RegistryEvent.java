package net.minecraftforge.event;

import net.minecraftforge.registries.IForgeRegistry;

/**
 * 互換レイヤー: 1.12.2の RegistryEvent のダミー実装。
 *
 * 1.12.2のModは以下のように登録を行う:
 *
 *   @SubscribeEvent
 *   public void registerBlocks(RegistryEvent.Register<Block> event) {
 *       event.getRegistry().register(myBlock);
 *   }
 *
 * このダミークラスを同じパッケージ・クラス名で用意することで
 * 1.12.2Modが NoClassDefFoundError を起こさずにロードできる。
 *
 * 設計書「実装手順 3: レジストリ・ブリッジ」の「ダミーのRegistryEvent.Register<T>」に相当。
 */
public class RegistryEvent {

    /**
     * ブロック・アイテム等の登録イベント。
     * 1.12.2Modの @SubscribeEvent メソッドがこの型を引数にとる。
     */
    public static class Register<T> {

        private final IForgeRegistry<T> registry;

        public Register(IForgeRegistry<T> registry) {
            this.registry = registry;
        }

        /**
         * 1.12.2Modが登録先レジストリを取得するために呼ぶ。
         * 返されるIForgeRegistryのregister()はキャッシュに溜め込む偽実装。
         */
        public IForgeRegistry<T> getRegistry() {
            return registry;
        }
    }

    /**
     * 1.12.2の RegistryEvent.MissingMappings イベントのダミー実装。
     *
     * このイベントはマップされない ID を扱うため、レガシーモッドの解析時に
     * 型解決が必要になります。
     */
    public static class MissingMappings {

        private final String modId;

        public MissingMappings(String modId) {
            this.modId = modId;
        }

        public String getModId() {
            return modId;
        }

        public java.util.List<Mapping> getMappings() {
            return java.util.Collections.emptyList();
        }

        public static class Mapping {

            private final String name;
            private boolean ignored;
            private Object target;

            public Mapping(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public Object getTarget() {
                return target;
            }

            public void ignore() {
                this.ignored = true;
            }

            public boolean isIgnored() {
                return ignored;
            }
        }
    }

    /**
     * 1.12.2の RegistryEvent.NewRegistry イベントのダミー実装。
     */
    public static class NewRegistry<T> {

        private final String name;

        public NewRegistry(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 1.12.2の RegistryEvent.RegistryConstructed イベントのダミー実装。
     */
    public static class RegistryConstructed<T> {

        private final IForgeRegistry<T> registry;

        public RegistryConstructed(IForgeRegistry<T> registry) {
            this.registry = registry;
        }

        public IForgeRegistry<T> getRegistry() {
            return registry;
        }
    }
}

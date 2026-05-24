package site.canva.my.komazonjapan;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import site.canva.my.komazonjapan.compat.LegacyModLifecycleBridge;
import site.canva.my.komazonjapan.compat.LegacyModDiscoverer;
import site.canva.my.komazonjapan.compat.LegacyRegistryBridge;
import site.canva.my.komazonjapan.compat.test.DummyLegacyMod;
import site.canva.my.komazonjapan.compat.test.DummyLegacyModWithBlock;

@Mod(McModAPIs.MODID)
public class McModAPIs {

    public static final String MODID = "mcmodapis";
    public static final Logger LOGGER = LogUtils.getLogger();

    // ─── 現代のNeoForge DeferredRegister ───
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<Block> EXAMPLE_BLOCK =
            BLOCKS.registerSimpleBlock("example_block", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);
    public static final DeferredItem<Item> EXAMPLE_ITEM =
            ITEMS.registerSimpleItem("example_item", p -> p.food(
                    new FoodProperties.Builder()
                            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB =
            CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mcmodapis"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> output.accept(EXAMPLE_ITEM.get()))
                    .build());

    // ─── 互換レイヤー Phase 1 & 2 ───
    public static final LegacyModLifecycleBridge LEGACY_LIFECYCLE = new LegacyModLifecycleBridge();
    public static final LegacyRegistryBridge    LEGACY_REGISTRY   = new LegacyRegistryBridge();

    public McModAPIs(IEventBus modEventBus, ModContainer modContainer) {
        // 現代NeoForgeの初期化
        modEventBus.addListener(this::commonSetup);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(Config::onLoad);

        // ─── Phase 1: ライフサイクルブリッジ ───
        DummyLegacyMod phase1Mod = new DummyLegacyMod();
        LEGACY_LIFECYCLE.registerLegacyMod(DummyLegacyMod.class, phase1Mod);
        modEventBus.addListener(LEGACY_LIFECYCLE::onCommonSetup);

        // ─── Phase 2: レジストリブリッジ ───
        DummyLegacyModWithBlock phase2Mod = new DummyLegacyModWithBlock();
        LEGACY_LIFECYCLE.registerLegacyMod(DummyLegacyModWithBlock.class, phase2Mod);
        LEGACY_REGISTRY.registerLegacyMod(DummyLegacyModWithBlock.class, phase2Mod);
        // RegisterEvent のタイミングでキャッシュを現代レジストリに流し込む
        modEventBus.addListener(LEGACY_REGISTRY::onRegister);

        // ─── 追加: 旧Mod の自動検出とロード ───
        LegacyModDiscoverer legacyModDiscoverer = new LegacyModDiscoverer(LEGACY_LIFECYCLE, LEGACY_REGISTRY);
        legacyModDiscoverer.discoverLegacyMods();

        LOGGER.info("[McModAPIs] 互換レイヤー Phase 1 & 2: 初期化完了");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }
        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());
        Config.ITEM_STRINGS.get().forEach(item -> LOGGER.info("ITEM >> {}", item));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}

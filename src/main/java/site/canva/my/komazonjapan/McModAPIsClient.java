package site.canva.my.komazonjapan;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// BUG FIX: クラス名を "121APIClient" → "McModAPIsClient" に変更。
@Mod(value = McModAPIs.MODID, dist = Dist.CLIENT)
public class McModAPIsClient {

    public McModAPIsClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        container.getEventBus().register(this);
    }

    @SubscribeEvent
    void onClientSetup(FMLClientSetupEvent event) {
        McModAPIs.LOGGER.info("HELLO FROM CLIENT SETUP");
        McModAPIs.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}

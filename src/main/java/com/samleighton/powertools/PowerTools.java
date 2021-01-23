package com.samleighton.powertools;

import com.samleighton.powertools.init.Items;
import com.samleighton.powertools.items.PowerPick;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Mod("power_tools")
public class PowerTools {

    public static PowerTools instance;
    //private static final Logger logger = LogManager.getLogManager().getLogger("power_tools");

    public PowerTools(){

        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistration);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        //logger.info("setup method registration");
    }

    private void clientRegistration(final FMLClientSetupEvent event) {
        //logger.info("clientRegistries method registered");
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    Items.power_pick = new PowerPick(ItemTier.WOOD, 2, 5.0f, new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(new ResourceLocation("power_tools", "power_pick"))
            );
        }
    }
}

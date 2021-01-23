package com.samleighton.powertools;

import com.samleighton.powertools.init.PowerToolItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PowerTools.MOD_ID)
public class PowerTools {

    public static final String MOD_ID = "power_tools";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public PowerTools() {
        // Obtain Mod Event Bus
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Add the setup listener to event bus
        modEventBus.addListener(this::setup);
        // Register all DeferredRegisters of Item type with the event bus
        PowerToolItems.DEFERRED_ITEMS.register(modEventBus);
        // Register our mod with the MinecraftForge event bus
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Logging setup");
    }

}

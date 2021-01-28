package com.samleighton.powertools.init;

import com.samleighton.powertools.PowerTools;
import com.samleighton.powertools.tools.PowerToolTier;
import com.samleighton.powertools.tools.items.PowerChop;
import com.samleighton.powertools.tools.items.PowerPick;
import com.samleighton.powertools.tools.items.PowerScoop;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemsRegistry {

    // Create the deferred register for items
    public static final DeferredRegister<Item> DEFERRED_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PowerTools.MOD_ID);

    // Create the registry object for the power pick item
    public static final RegistryObject<Item> POWER_PICK = DEFERRED_ITEMS.register("power_pick",
            () -> new PowerPick(PowerToolTier.POWER, 0, 0f, new Item.Properties().group(ItemGroup.TOOLS)));

    // Create the registry object for the power scoop item
    public static final RegistryObject<Item> POWER_SCOOP = DEFERRED_ITEMS.register("power_scoop",
            () -> new PowerScoop(PowerToolTier.POWER, 0, 0f, new Item.Properties().group(ItemGroup.TOOLS)));

    // Create the registry object for the power chop item
    public static final RegistryObject<Item> POWER_CHOP = DEFERRED_ITEMS.register("power_chop",
            () -> new PowerChop(PowerToolTier.POWER, 0, 0f, new Item.Properties().group(ItemGroup.TOOLS)));

}

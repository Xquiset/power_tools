package com.samleighton.powertools.init;

import com.samleighton.powertools.PowerTools;
import com.samleighton.powertools.items.PowerPick;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PowerToolItems {

    // Create the deferred register for items
    public static final DeferredRegister<Item> DEFERRED_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PowerTools.MOD_ID);

    // Create the registry object for the power pick item
    public static final RegistryObject<Item> POWER_PICK = DEFERRED_ITEMS.register("power_pick", () -> new PowerPick(PowerToolMaterial.POWER, 0, 0f, new Item.Properties().group(ItemGroup.TOOLS)));
}

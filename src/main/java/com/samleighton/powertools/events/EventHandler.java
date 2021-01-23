package com.samleighton.powertools.events;

import com.samleighton.powertools.init.Items;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class EventHandler {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        UUID pUUID = player.getUniqueID();
        player.sendMessage(new StringTextComponent("Power Tools Loaded"), pUUID);

        // Get the power pick item from Items class
        Item powerPick = Items.POWER_PICK.get();
        // Create the item stack
        ItemStack powerPickStack = new ItemStack(powerPick);
        // Add the stack to the inventory
        player.addItemStackToInventory(powerPickStack);
        // Tell the player about the new pick
        player.sendMessage(new StringTextComponent("Have a power pick fool!"), pUUID);
    }
}

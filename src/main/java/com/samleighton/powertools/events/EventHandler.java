package com.samleighton.powertools.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class EventHandler {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        player.sendMessage(new StringTextComponent("Power Tools Loaded"), null);

        Item.Properties powerPickProperties = new Item.Properties();

        PickaxeItem powerPick = new PickaxeItem(ItemTier.DIAMOND, (int)ItemTier.DIAMOND.getAttackDamage(), ItemTier.DIAMOND.getEfficiency(), powerPickProperties);
        ItemStack powerPickStack = new ItemStack(powerPick);
        player.addItemStackToInventory(powerPickStack);
    }
}

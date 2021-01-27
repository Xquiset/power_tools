package com.samleighton.powertools.events;

import com.samleighton.powertools.tools.items.PowerPick;
import com.samleighton.powertools.tools.items.PowerScoop;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
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
    }

    @SubscribeEvent
    public static void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
        // Get the player
        PlayerEntity player = event.getPlayer();
        // Get the item in the players hand
        Item itemInHand = player.getHeldItemMainhand().getItem();
        // Get position of block that just broke
        BlockPos blockBrokePos = event.getPos();

        if (itemInHand instanceof PowerPick) {
            PowerPick pick = (PowerPick) itemInHand;
            pick.destroyAOEBlocks(player, blockBrokePos);
        } else if (itemInHand instanceof PowerScoop) {
            PowerScoop scoop = (PowerScoop) itemInHand;
            scoop.destroyAOEBlocks(player, blockBrokePos);
        }
    }
}

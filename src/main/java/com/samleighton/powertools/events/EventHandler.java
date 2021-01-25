package com.samleighton.powertools.events;

import com.samleighton.powertools.PowerTools;
import com.samleighton.powertools.init.PowerToolItems;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
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
        // Get the BlockState of the broken block from event
        BlockState blockState = event.getState();
        // Get the RayTraceResult objectMouseOver from Minecraft instance
        RayTraceResult lookingAt = Minecraft.getInstance().objectMouseOver;
        // Get the block ray trace result from the item
        BlockRayTraceResult blockRayTraceResult = PowerToolItems.blockRayTraceResult(Minecraft.getInstance().world, player, RayTraceContext.FluidMode.ANY);
        // Get the face the block was broken from as a string
        String blockFaceBroken = blockRayTraceResult.getFace().getString();

        // Check if the player is looking at an actual block, null if not a block
        if (lookingAt != null) {
            // Get position of block that just broke
            BlockPos blockBrokePos = new BlockPos(lookingAt.getHitVec());

            // Check if the player is holding a power pick and if the block requires a tool to be broken
            if (itemInHand.equals(PowerToolItems.POWER_PICK.get()) && blockState.getRequiresTool()) {
                // Cancel event
                //event.setCanceled(true);
                // Check if the power pick can harvest the block broken or if the block even requires a tool
                if (blockState.getHarvestTool() == ToolType.PICKAXE) {
                    // Create BlockState array of all surrounding blocks
                    ArrayList<BlockPos> surroundingBlocks = new ArrayList<>();

                    if (blockFaceBroken.equalsIgnoreCase("east") || blockFaceBroken.equalsIgnoreCase("west")) {
                        player.sendMessage(new StringTextComponent("Broke block on " + blockFaceBroken), player.getUniqueID());
                        surroundingBlocks = calcEastWestBlocks(blockBrokePos);
                    }

                    // Separate log list per event call
                    PowerTools.LOGGER.info("-------------------------------------------------------------------------------------------------------------------------------------------------");
                    // Loop over our surrounding blocks array
                    for (BlockPos pos : surroundingBlocks) {
                        // Grab the BlockState from the world using the BlockPos
                        BlockState state = event.getWorld().getBlockState(pos);
                        // Check if the block in block state is an AirBlock
                        if (!(state.getBlock() instanceof AirBlock)) {
                            // Log the BlockPos and Block
                            PowerTools.LOGGER.info(pos.toString() + " : " + state.getBlock().toString());
                            // Destroy the block and leave a drop if a pickaxe can harvest the block
                            event.getWorld().destroyBlock(pos, state.getHarvestTool() == ToolType.PICKAXE);
                        }
                    }
                }
            }
        }
    }

    private static ArrayList<BlockPos> calcEastWestBlocks(BlockPos pos) {
        PowerTools.LOGGER.info(pos.toString());
        ArrayList<BlockPos> temp = new ArrayList<>();

        int x = pos.getX();

        for (int y = -1; y <= 1; y++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos tempPos = new BlockPos(x, pos.getY() + y, pos.getZ() + z);
                temp.add(tempPos);
            }
        }

        return temp;
    }
}

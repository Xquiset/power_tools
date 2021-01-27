package com.samleighton.powertools.events;

import com.samleighton.powertools.PowerTools;
import com.samleighton.powertools.tools.items.PowerPick;
import com.samleighton.powertools.tools.items.PowerScoop;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
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
        // Get position of block that just broke
        BlockPos blockBrokePos = event.getPos();
        // Check if the item in the players hand is a PowerPick
        if (itemInHand instanceof PowerPick) {
            // Cast item in hand to power pick for ray tracing
            PowerPick powerPick = (PowerPick) itemInHand;
            // Ray trace from item in hand to block
            BlockRayTraceResult blockRayTraceResult = powerPick.blockRayTrace(event.getPlayer().getEntityWorld(), player, RayTraceContext.FluidMode.NONE);
            // Get the block face from ray trace result
            Direction blockFace = blockRayTraceResult.getFace();

            // Check if the player is holding a power pick and if the block requires a tool to be broken
            if (blockState.getRequiresTool() && blockState.getHarvestTool() == ToolType.PICKAXE) {
                // Create BlockPos array of all surrounding blocks
                ArrayList<BlockPos> surroundingBlocks = new ArrayList<>();

                // Determine which blocks to calculate for destruction
                switch (blockFace) {
                    case UP:
                    case DOWN:
                        surroundingBlocks = calcUpDownBlocks(blockBrokePos);
                        break;
                    case NORTH:
                    case SOUTH:
                        surroundingBlocks = calcNorthSouthBlocks(blockBrokePos);
                        break;
                    case EAST:
                    case WEST:
                        surroundingBlocks = calcEastWestBlocks(blockBrokePos);
                        break;
                }

                // Separate log list per event call
                PowerTools.LOGGER.info("-------------------------------------------------------------------------------------------------------------------------------------------------");
                // Loop over our surrounding blocks array
                for (BlockPos pos : surroundingBlocks) {
                    // Grab the BlockState from the world using the BlockPos
                    BlockState state = event.getWorld().getBlockState(pos);
                    // Check if the block in block state is an AirBlock
                    if (!(state.getBlock() instanceof AirBlock) && state.getHarvestTool() == ToolType.PICKAXE) {
                        // Log the BlockPos and Block
                        PowerTools.LOGGER.info(pos.toString() + " : " + state.getBlock().toString());
                        // Destroy the block and leave a drop if a pickaxe can harvest the block
                        event.getWorld().destroyBlock(pos, true);
                    }
                }
            }
        }
        // Check if the item in the players hand is a PowerPick
        else if (itemInHand instanceof PowerScoop) {
            // Cast item in hand to power pick for ray tracing
            PowerScoop powerScoop = (PowerScoop) itemInHand;
            // Ray trace from item in hand to block
            BlockRayTraceResult blockRayTraceResult = powerScoop.blockRayTrace(event.getPlayer().getEntityWorld(), player, RayTraceContext.FluidMode.NONE);
            // Get the block face from ray trace result
            Direction blockFace = blockRayTraceResult.getFace();

            // Check if the player is holding a power pick and if the block requires a tool to be broken
            if (blockState.getHarvestTool() == ToolType.SHOVEL) {
                // Create BlockPos array of all surrounding blocks
                ArrayList<BlockPos> surroundingBlocks = new ArrayList<>();

                // Determine which blocks to calculate for destruction
                switch (blockFace) {
                    case UP:
                    case DOWN:
                        surroundingBlocks = calcUpDownBlocks(blockBrokePos);
                        break;
                    case NORTH:
                    case SOUTH:
                        surroundingBlocks = calcNorthSouthBlocks(blockBrokePos);
                        break;
                    case EAST:
                    case WEST:
                        surroundingBlocks = calcEastWestBlocks(blockBrokePos);
                        break;
                }

                // Separate log list per event call
                PowerTools.LOGGER.info("-------------------------------------------------------------------------------------------------------------------------------------------------");
                // Loop over our surrounding blocks array
                for (BlockPos pos : surroundingBlocks) {
                    // Grab the BlockState from the world using the BlockPos
                    BlockState state = event.getWorld().getBlockState(pos);
                    // Check if the block in block state is an AirBlock
                    if (!(state.getBlock() instanceof AirBlock) && state.getHarvestTool() == ToolType.SHOVEL) {
                        // Log the BlockPos and Block
                        PowerTools.LOGGER.info(pos.toString() + " : " + state.getBlock().toString());
                        // Destroy the block and leave a drop if a shovel can harvest the block
                        event.getWorld().destroyBlock(pos, true);
                    }
                }
            }
        }
    }
}

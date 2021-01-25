package com.samleighton.powertools.events;

import com.samleighton.powertools.PowerTools;
import com.samleighton.powertools.items.PowerPick;
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

        // Check if the item in the players hand is a PowerPick
        if (itemInHand instanceof PowerPick) {
            // Get the BlockState of the broken block from event
            BlockState blockState = event.getState();
            // Get position of block that just broke
            BlockPos blockBrokePos = event.getPos();
            // Cast item in hand to power pick for ray tracing
            PowerPick powerPick = (PowerPick) itemInHand;
            // Ray trace from item in hand to block
            BlockRayTraceResult blockRayTraceResult = powerPick.blockRayTrace(event.getPlayer().getEntityWorld(), player, RayTraceContext.FluidMode.NONE);
            // Get the block face from ray trace result
            Direction blockFace = blockRayTraceResult.getFace();

            // Check if the player is holding a power pick and if the block requires a tool to be broken
            if (blockState.getRequiresTool() && blockState.getHarvestTool() == ToolType.PICKAXE) {
                // Create BlockState array of all surrounding blocks
                ArrayList<BlockPos> surroundingBlocks = new ArrayList<>();

                player.sendMessage(new StringTextComponent("Direction = " + blockFace.toString()), player.getUniqueID());
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
    }

    // EAST & WEST X is constant
    private static ArrayList<BlockPos> calcEastWestBlocks(BlockPos pos) {
        ArrayList<BlockPos> blocks = new ArrayList<>();
        int x = pos.getX();

        for (int y = -1; y <= 1; y++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos tempPos = new BlockPos(x, pos.getY() + y, pos.getZ() + z);
                blocks.add(tempPos);
            }
        }

        return blocks;
    }

    // NORTH & SOUTH Z is constant
    private static ArrayList<BlockPos> calcNorthSouthBlocks(BlockPos pos) {
        ArrayList<BlockPos> blocks = new ArrayList<>();
        int z = pos.getZ();

        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                BlockPos tempPos = new BlockPos(pos.getX() + x, pos.getY() + y, z);
                blocks.add(tempPos);
            }
        }

        return blocks;
    }

    // UP & DOWN Y is constant
    private static ArrayList<BlockPos> calcUpDownBlocks(BlockPos pos) {
        ArrayList<BlockPos> blocks = new ArrayList<>();
        int y = pos.getY();

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos tempPos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
                blocks.add(tempPos);
            }
        }

        return blocks;
    }
}

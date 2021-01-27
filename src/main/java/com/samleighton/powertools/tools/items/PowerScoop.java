package com.samleighton.powertools.tools.items;

import com.google.common.collect.Sets;
import com.samleighton.powertools.PowerTools;
import com.samleighton.powertools.tools.PowerToolsItem;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.Set;

public class PowerScoop extends PowerToolsItem {
    private static final Set<Block> POWERSCOOP_EFFECTIVE = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.FARMLAND, Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.RED_SAND, Blocks.SNOW_BLOCK, Blocks.SNOW, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER, Blocks.SOUL_SOIL);

    public PowerScoop(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(attackDamageIn, attackSpeedIn, tier, POWERSCOOP_EFFECTIVE, builder.addToolType(ToolType.SHOVEL, tier.getHarvestLevel()));
    }

    public boolean canHarvestBlock(BlockState blockIn) {
        return (blockIn.isIn(Blocks.SNOW) || blockIn.isIn(Blocks.SNOW_BLOCK)) || !blockIn.getRequiresTool();
    }

    public void destroyAOEBlocks(PlayerEntity player, BlockPos blockPos) {
        ArrayList<BlockPos> surroundingBlocks = new ArrayList<>();

        // Ray trace from item in hand to block
        BlockRayTraceResult blockRayTraceResult = blockRayTrace(player.getEntityWorld(), player);
        // Get the block face from ray trace result
        Direction blockFace = blockRayTraceResult.getFace();

        // Determine which blocks to calculate for destruction
        switch (blockFace) {
            case UP:
            case DOWN:
                surroundingBlocks = calcUpDownBlocks(blockPos);
                break;
            case NORTH:
            case SOUTH:
                surroundingBlocks = calcNorthSouthBlocks(blockPos);
                break;
            case EAST:
            case WEST:
                surroundingBlocks = calcEastWestBlocks(blockPos);
                break;
        }

        //TODO: Refactor more
        boolean test = surroundingBlocks.removeIf(pos -> !canHarvestBlock(player.getEntityWorld().getBlockState(pos)));

        destroyBlocksInWorld(player.getEntityWorld(), surroundingBlocks);
    }

    private void destroyBlocksInWorld(IWorld world, ArrayList<BlockPos> blocksToDestroy) {
        // Separate log list per event call
        PowerTools.LOGGER.info("-------------------------------------------------------------------------------------------------------------------------------------------------");
        // Loop over our surrounding blocks array
        for (BlockPos pos : blocksToDestroy) {
            // Grab the BlockState from the world using the BlockPos
            BlockState state = world.getBlockState(pos);
            // Check if the block in block state is an AirBlock
            if (!(state.getBlock() instanceof AirBlock)) {
                // Log the BlockPos and Block
                PowerTools.LOGGER.info(pos.toString() + " : " + state.getBlock().toString());
                // Destroy the block and leave a drop if a pickaxe can harvest the block
                world.destroyBlock(pos, true);
            }
        }
    }
}
package com.samleighton.powertools.tools.items;

import com.google.common.collect.ImmutableSet;
import com.samleighton.powertools.PowerTools;
import com.samleighton.powertools.tools.PowerToolsItem;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.Set;

public class PowerPick extends PowerToolsItem {
    private static final Set<Block> POWERPICK_EFFECTIVE = ImmutableSet.of(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.POWERED_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.NETHER_GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.GRANITE, Blocks.POLISHED_GRANITE, Blocks.DIORITE, Blocks.POLISHED_DIORITE, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE, Blocks.STONE_SLAB, Blocks.SMOOTH_STONE_SLAB, Blocks.SANDSTONE_SLAB, Blocks.PETRIFIED_OAK_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.BRICK_SLAB, Blocks.STONE_BRICK_SLAB, Blocks.NETHER_BRICK_SLAB, Blocks.QUARTZ_SLAB, Blocks.RED_SANDSTONE_SLAB, Blocks.PURPUR_SLAB, Blocks.SMOOTH_QUARTZ, Blocks.SMOOTH_RED_SANDSTONE, Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_STONE, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE, Blocks.POLISHED_GRANITE_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_DIORITE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.END_STONE_BRICK_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB, Blocks.SMOOTH_QUARTZ_SLAB, Blocks.GRANITE_SLAB, Blocks.ANDESITE_SLAB, Blocks.RED_NETHER_BRICK_SLAB, Blocks.POLISHED_ANDESITE_SLAB, Blocks.DIORITE_SLAB, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.PISTON_HEAD);

    public PowerPick(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(attackDamageIn, attackSpeedIn, tier, POWERPICK_EFFECTIVE, builder.addToolType(ToolType.PICKAXE, tier.getHarvestLevel()));
    }

    public boolean canHarvestBlock(BlockState blockIn) {
        int i = this.getTier().getHarvestLevel();
        if (blockIn.getHarvestTool() == ToolType.PICKAXE) {
            return i >= blockIn.getHarvestLevel();
        }
        Material material = blockIn.getMaterial();
        return material == Material.ROCK || material == Material.IRON || material == Material.ANVIL;
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

package com.samleighton.powertools.tools;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Set;

public class PowerToolsItem extends ToolItem {

    protected ArrayList<BlockPos> aoeBlocks = new ArrayList<>();

    public PowerToolsItem(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Item.Properties builderIn) {
        super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builderIn);
    }

    /**
     * Used to ray trace from an item to a block
     * Mostly used for determining the face of a block
     *
     * @param worldIn, The world the block is in
     * @param player,  The player to trace from
     * @return BlockRayTraceResult
     */
    protected BlockRayTraceResult blockRayTrace(World worldIn, PlayerEntity player) {
        return Item.rayTrace(worldIn, player, RayTraceContext.FluidMode.NONE);
    }

    /**
     * Used to set and calculate AOE blocks for a 3x3 power tool
     *
     * @param player,   The player destroying the block
     * @param blockPos, The initial block destroyed
     */
    protected void setAoeBlocks(PlayerEntity player, BlockPos blockPos) {
        // Ray trace from item in hand to block
        BlockRayTraceResult blockRayTraceResult = blockRayTrace(player.getEntityWorld(), player);
        // Get the block face from ray trace result
        Direction blockFace = blockRayTraceResult.getFace();

        // Determine which blocks to calculate for destruction
        switch (blockFace) {
            case UP:
            case DOWN:
                this.aoeBlocks = calcUpDownBlocks(blockPos);
                break;
            case NORTH:
            case SOUTH:
                this.aoeBlocks = calcNorthSouthBlocks(blockPos);
                break;
            case EAST:
            case WEST:
                this.aoeBlocks = calcEastWestBlocks(blockPos);
                break;
        }
    }

    /**
     * Used to obtain the Array of AOE blocks to break
     */
    protected ArrayList<BlockPos> getAoeBlocks() {
        return this.aoeBlocks;
    }

    /**
     * Destroys an Array of AOE blocks
     *
     * @param world, The world to destroy the blocks in
     */
    protected void destroyBlocksInWorld(IWorld world, PlayerEntity player) {
        ItemStack itemInHand = player.getHeldItemMainhand();
        // Separate log list per event call
        //PowerTools.LOGGER.info("-------------------------------------------------------------------------------------------------------------------------------------------------");
        // Loop over our surrounding blocks array
        for (BlockPos pos : getAoeBlocks()) {
            // Grab the BlockState from the world using the BlockPos
            BlockState state = world.getBlockState(pos);
            // Check if the block in block state is an AirBlock
            if (!(state.getBlock() instanceof AirBlock)) {
                // Log the BlockPos and Block
                //PowerTools.LOGGER.info(pos.toString() + " : " + state.getBlock().toString());
                // Destroy the block and leave a drop if a pickaxe can harvest the block
                world.destroyBlock(pos, true, player);
                itemInHand.damageItem(1, player, playerEntity -> playerEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
            }
        }
    }

    /**
     * Calculates the blocks to destroy when block was destroyed
     * on East or West face
     *
     * @param pos The position of the block destroyed
     * @return The array of blocks to destroy
     */
    protected ArrayList<BlockPos> calcEastWestBlocks(BlockPos pos) {
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

    /**
     * Calculates the blocks to destroy when block was destroyed
     * on North or South face
     *
     * @param pos The position of the block destroyed
     * @return The array of blocks to destroy
     */
    protected ArrayList<BlockPos> calcNorthSouthBlocks(BlockPos pos) {
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

    /**
     * Calculates the blocks to destroy when block was destroyed
     * on Up or Down face
     *
     * @param pos The position of the block destroyed
     * @return The array of blocks to destroy
     */
    protected ArrayList<BlockPos> calcUpDownBlocks(BlockPos pos) {
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

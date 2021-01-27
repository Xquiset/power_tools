package com.samleighton.powertools.tools;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Set;

public class PowerToolsItem extends ToolItem {

    public PowerToolsItem(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Item.Properties builderIn) {
        super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builderIn);
    }

    protected BlockRayTraceResult blockRayTrace(World worldIn, PlayerEntity player) {
        return Item.rayTrace(worldIn, player, RayTraceContext.FluidMode.NONE);
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

package com.samleighton.powertools.tools.items;

import com.google.common.collect.Sets;
import com.samleighton.powertools.tools.PowerToolsItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ToolType;

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

        setAoeBlocks(player, blockPos);

        getAoeBlocks().removeIf(pos -> !canHarvestBlock(player.getEntityWorld().getBlockState(pos)) || pos.equals(blockPos));

        destroyBlocksInWorld(player.getEntityWorld(), player);
    }
}
package com.samleighton.powertools.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
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

public class PowerToolsHub extends ToolItem {
    private final Set<Block> effectiveBlocks;
    protected final float efficiency;
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> toolAttributes;

    public PowerToolsHub(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Item.Properties builderIn){
        super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builderIn);
        this.effectiveBlocks = effectiveBlocksIn;
        this.efficiency = tier.getEfficiency();
        this.attackDamage = attackDamageIn + tier.getAttackDamage();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double)attackSpeedIn, AttributeModifier.Operation.ADDITION));
        this.toolAttributes = builder.build();
    }

    public BlockRayTraceResult blockRayTrace(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode) {
        return Item.rayTrace(worldIn, player, fluidMode);
    }

    /**
     * Calculates the blocks to destroy when block was destroyed
     * on East or West face
     *
     * @param pos The position of the block destroyed
     * @return The array of blocks to destroy
     */
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

    /**
     * Calculates the blocks to destroy when block was destroyed
     * on North or South face
     *
     * @param pos The position of the block destroyed
     * @return The array of blocks to destroy
     */
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

    /**
     * Calculates the blocks to destroy when block was destroyed
     * on Up or Down face
     *
     * @param pos The position of the block destroyed
     * @return The array of blocks to destroy
     */
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

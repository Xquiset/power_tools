package com.samleighton.powertools.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;

public class PowerPick extends PickaxeItem {

    public PowerPick(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    /**
     * Used to get the face of the block the pickaxe has broken
     *
     * @param worldIn   The world the player is in
     * @param player    The player entity breaking the block
     * @param fluidMode The fluid mode of the block breaking
     * @return
     */
    public BlockRayTraceResult blockRayTrace(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode) {
        return Item.rayTrace(worldIn, player, fluidMode);
    }
}

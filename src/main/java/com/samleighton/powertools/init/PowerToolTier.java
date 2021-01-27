package com.samleighton.powertools.init;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum PowerToolTier implements IItemTier {

    // Create the POWER tier
    POWER(10.0f, 12.0f, 1500, 4, 25, () -> {
        return Ingredient.fromItems(Items.DIAMOND_BLOCK);
    });

    private final float attackDamage, efficiency;
    private final int durability, harvestLevel, enchantability;
    private final LazyValue<Ingredient> repairMaterial;

    PowerToolTier(float attackDamage, float efficiency, int durability, int harvestLevel, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.attackDamage = attackDamage;
        this.efficiency = efficiency;
        this.durability = durability;
        this.harvestLevel = harvestLevel;
        this.enchantability = enchantability;
        this.repairMaterial = new LazyValue<>(repairMaterial);
    }

    @Override
    public int getMaxUses() {
        return this.durability;
    }

    @Override
    public float getEfficiency() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }
}

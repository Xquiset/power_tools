package com.samleighton.powertools.items;

import net.minecraft.item.IItemTier;
import net.minecraft.item.PickaxeItem;

public class PowerPick extends PickaxeItem {

    public PowerPick(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder){
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }
}

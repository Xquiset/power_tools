package com.samleighton.powertools.init;

import com.samleighton.powertools.PowerTools;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PowerTools.MOD_ID);

    public static final RegistryObject<Block> CITRINE_ORE = BLOCKS.register("citrine_ore",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON)
                .hardnessAndResistance(10f, 20f)
                .harvestTool(ToolType.PICKAXE).harvestLevel(2)
                .sound(SoundType.METAL).setRequiresTool()));
}

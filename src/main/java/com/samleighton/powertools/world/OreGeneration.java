package com.samleighton.powertools.world;

import com.samleighton.powertools.PowerTools;
import com.samleighton.powertools.init.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.swing.text.StringContent;
import java.util.ArrayList;


public class OreGeneration {

    public static void generateOres(final BiomeLoadingEvent event){
        //if we want ore to spawn everywhere except the end and the nether
        if(!(event.getCategory().equals(Biome.Category.THEEND) && event.getCategory().equals(Biome.Category.NETHER))){
            generateOre(event.getGeneration(), OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlockRegistry.CITRINE_ORE.get().getDefaultState(), 3, 3, 16, 1);
        }
    }

    private static void generateOre(BiomeGenerationSettingsBuilder settings, RuleTest fillerType, BlockState state, int veinSize, int minHeight, int maxHeight, int maxPerChunk){
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "ore_citrine",
                Feature.ORE.withConfiguration(new OreFeatureConfig(fillerType, state, veinSize))
                        .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                        .square().func_242731_b(maxPerChunk));
        PowerTools.LOGGER.info(new StringTextComponent(state.getBlock().toString()));
    }
}

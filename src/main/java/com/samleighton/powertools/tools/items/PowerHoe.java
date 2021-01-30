package com.samleighton.powertools.tools.items;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.samleighton.powertools.tools.PowerToolsItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public class PowerHoe extends PowerToolsItem {
    private static final Set<Block> POWERHOE_EFFECTIVE = ImmutableSet.of(
            Blocks.NETHER_WART_BLOCK,
            Blocks.WARPED_WART_BLOCK,
            Blocks.HAY_BLOCK,
            Blocks.DRIED_KELP_BLOCK,
            Blocks.TARGET,
            Blocks.SHROOMLIGHT,
            Blocks.SPONGE,
            Blocks.WET_SPONGE,
            Blocks.JUNGLE_LEAVES,
            Blocks.OAK_LEAVES,
            Blocks.SPRUCE_LEAVES,
            Blocks.DARK_OAK_LEAVES,
            Blocks.ACACIA_LEAVES,
            Blocks.BIRCH_LEAVES);
    protected static final Map<Block, BlockState> POWERHOE_LOOKUP = Maps.newHashMap(ImmutableMap.of(
            Blocks.GRASS_BLOCK,
            Blocks.FARMLAND.getDefaultState(),
            Blocks.GRASS_PATH,
            Blocks.FARMLAND.getDefaultState(),
            Blocks.DIRT,
            Blocks.FARMLAND.getDefaultState(),
            Blocks.COARSE_DIRT,
            Blocks.DIRT.getDefaultState()));

    public PowerHoe(IItemTier tier, int attackDamageIn, float attackSpeedIn, Item.Properties builder) {
        super(attackDamageIn, attackSpeedIn, tier, POWERHOE_EFFECTIVE, builder.addToolType(ToolType.HOE, tier.getHarvestLevel()));
    }

    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getItem();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Direction direction = context.getFace();
        Hand hand = context.getHand();

        if (player == null) {
            return ActionResultType.FAIL;
        }

/*        if (!this.tryTill(stack, player, world, pos, direction, hand)) {
            return ActionResultType.FAIL;
        }*/
        if (!player.isCrouching()){
            playTillSound(world, player, pos, direction);
            BlockPos.getAllInBox(pos.add(-1,0,-1), pos.add(1,0,1)).forEach(aoePos -> { this.tryTill(stack, player, world, aoePos, direction, hand); });
        } else {
            playTillSound(world, player, pos, direction);
            this.tryTill(stack, player, world, pos, direction, hand);
        }

        // PASS is a lie
        // ActionResultType must be success for immediate till
        if (direction != Direction.DOWN && world.isAirBlock(pos.up())) {
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    public boolean tryTill(ItemStack stack, PlayerEntity player, World world, BlockPos pos, Direction direction, Hand hand) {
        if (direction != Direction.DOWN && world.isAirBlock(pos.up())) {
            BlockState state = POWERHOE_LOOKUP.get(world.getBlockState(pos).getBlock());
            if (state != null) {
                if (!world.isRemote) {
                    world.setBlockState(pos, state, 11);
                    if (player != null) {
                        stack.damageItem(1, player, (entity) -> {
                            entity.sendBreakAnimation(hand);
                        });
                    }
                }

                return true;
            }
        }

        return false;
    }

    public static void playTillSound(World world, PlayerEntity player, BlockPos pos, Direction direction){
        if (direction != Direction.DOWN && world.isAirBlock(pos.up())) {
            world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    public static BlockState getHoeTillingState(BlockState originalState) {
        return POWERHOE_LOOKUP.get(originalState.getBlock());
    }
}
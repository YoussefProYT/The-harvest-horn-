package com.yousef.harvesthorn.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class HarvestHornItem extends Item {

    private final int size;
    private final boolean isHarver;

    public HarvestHornItem(Properties properties, int size) {
        this(properties, size, false);
    }

    public HarvestHornItem(Properties properties, int size, boolean isHarver) {
        super(properties);
        this.size = size;
        this.isHarver = isHarver;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return use(context.getLevel(), context.getPlayer(), context.getHand());
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        if (player == null) {
            return InteractionResult.PASS;
        }

        ItemStack stack = player.getItemInHand(hand);
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        player.getCooldowns().addCooldown(stack, 60);

        ServerLevel serverWorld = (ServerLevel) world;
        ServerPlayer serverPlayer = (ServerPlayer) player;
        BlockPos center = player.blockPosition();

        processArea(serverWorld, serverPlayer, center, size);
        serverWorld.playSound(null, center, SoundEvents.ITEM_GOAT_HORN_SOUND_0, SoundSource.PLAYERS, 1.0F, 1.0F);
        spawnParticles(serverWorld, center, size);

        stack.hurtAndBreak(1, player, hand);
        return InteractionResult.SUCCESS;
    }

    private void processArea(ServerLevel world, ServerPlayer player, BlockPos center, int size) {
        int start = -(size / 2);
        int end = start + size - 1;

        for (int x = start; x <= end; x++) {
            for (int z = start; z <= end; z++) {
                for (int y = -1; y <= 1; y++) {
                    processBlock(world, player, center.offset(x, y, z));
                }
            }
        }
    }

    private void processBlock(ServerLevel world, ServerPlayer player, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof CropBlock cropBlock) {
            if (cropBlock.isMaxAge(state)) {
                harvestCropAndReplant(world, player, pos, state, cropBlock);
            } else if (isHarver && cropBlock.isValidBonemealTarget(world, pos, state)) {
                cropBlock.performBonemeal(world, world.getRandom(), pos, state);
            }
            return;
        }

        if (block instanceof NetherWartBlock) {
            int age = state.getValue(NetherWartBlock.AGE);
            if (age >= NetherWartBlock.MAX_AGE) {
                harvestNetherWart(world, player, pos, state);
            }
        }
    }

    private void harvestCropAndReplant(ServerLevel world, ServerPlayer player,
                                       BlockPos pos, BlockState state, CropBlock cropBlock) {
        List<ItemStack> drops = Block.getDrops(state, world, pos,
                world.getBlockEntity(pos), player, player.getMainHandItem());

        for (ItemStack drop : drops) {
            giveOrDrop(player, world, pos, drop);
        }

        world.setBlock(pos, cropBlock.getStateForAge(0), Block.UPDATE_ALL);
    }

    private void harvestNetherWart(ServerLevel world, ServerPlayer player, BlockPos pos, BlockState state) {
        List<ItemStack> drops = Block.getDrops(state, world, pos,
                world.getBlockEntity(pos), player, player.getMainHandItem());

        for (ItemStack drop : drops) {
            giveOrDrop(player, world, pos, drop);
        }

        world.setBlock(pos, state.setValue(NetherWartBlock.AGE, 0), Block.UPDATE_ALL);
    }

    private void giveOrDrop(ServerPlayer player, ServerLevel world, BlockPos pos, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        if (player.getInventory().add(stack)) {
            return;
        }

        ItemEntity itemEntity = new ItemEntity(world,
                pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, stack);
        world.addFreshEntity(itemEntity);
    }

    private void spawnParticles(ServerLevel world, BlockPos center, int size) {
        double x = center.getX() + 0.5;
        double y = center.getY() + 0.5;
        double z = center.getZ() + 0.5;

        world.addParticle(ParticleTypes.HAPPY_VILLAGER,
                x, y, z,
                0.0D, 0.0D, 0.0D);
    }
}

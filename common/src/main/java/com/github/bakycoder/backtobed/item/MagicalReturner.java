package com.github.bakycoder.backtobed.item;

import com.github.bakycoder.backtobed.utils.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The MagicalReturner class represents an item that allows players to teleport to their respawn point.
 */
public class MagicalReturner extends Item {
    /**
     * Representing the name of the item.
     */
    public static final String ITEM_NAME = "magical_returner";
    /**
     * Representing the duration of item usage in ticks.
     */
    private static final int ITEM_DURATION_USAGE_TICKS = 200;
    /**
     * Representing the cooldown duration of the item in ticks.
     */
    private static final int ITEM_COOLDOWN_TICKS = 36000;
    /**
     * The executor service for scheduling asynchronous tasks.
     */
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    /**
     * Constructs a MagicalReturner item with specified properties.
     */
    public MagicalReturner() {
        super(new Properties()
                .stacksTo(1)
        );
    }

    /**
     * Appends additional information to the item tooltip when hovering over it.
     *
     * @param itemStack   The ItemStack representing the item.
     * @param level       The current Level.
     * @param list        The list of tooltip components.
     * @param tooltipFlag The tooltip flag indicating advanced information.
     */

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);

        // Add information about the item's behavior to the tooltip
        list.addAll(TooltipUtils.formatComponent("item.backtobed.magical_returner.tooltip.behavior", ChatFormatting.GRAY, true));

        // Add further details to the tooltip if the SHIFT key is held
        if (Screen.hasShiftDown())
            list.addAll(TooltipUtils.formatComponent("item.backtobed.magical_returner.tooltip.further", ChatFormatting.GREEN));
        else
            list.add(TooltipUtils.highlightComponentArg("backtobed.tooltip.key_hold", "SHIFT", ChatFormatting.YELLOW, ChatFormatting.GOLD));
    }

    /**
     * Returns the total duration of item usage.
     *
     * @param stack The ItemStack representing the item.
     * @return The total duration of item usage in seconds.
     */
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    /**
     * Called every tick during item usage to perform additional actions.
     *
     * @param level        The current Level.
     * @param livingEntity The LivingEntity using the item.
     * @param itemStack    The ItemStack representing the item.
     * @param i            The remaining item use duration.
     */
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        super.onUseTick(level, livingEntity, itemStack, i);

        // Check if the entity is a server player and the world is not client-side
        if (!(livingEntity instanceof ServerPlayer player) || level.isClientSide()) return;

        // Get the remaining duration of item usage
        int duration = this.getUseDuration(itemStack) - i;
        if (duration < ITEM_DURATION_USAGE_TICKS) return;

        // Check if the player is in the overworld dimension
        if (player.getCommandSenderWorld().dimension() != Level.OVERWORLD) {
            player.displayClientMessage(Component.translatable("item.backtobed.magical_returner.condition.teleport_for_overworld"), true);

            // Stop item usage and add cooldown
            player.stopUsingItem();
            player.getCooldowns().addCooldown(this, ITEM_COOLDOWN_TICKS);
        }

        // Get the player's respawn position
        BlockPos respawnPos = player.getRespawnPosition();

        // Check if the respawn position is not set
        if (respawnPos == null) {
            player.displayClientMessage(Component.translatable("item.backtobed.magical_returner.condition.no_respawn_point"), true);

            player.stopUsingItem();
            player.getCooldowns().addCooldown(this, ITEM_COOLDOWN_TICKS);
            return;
        }

        // Check if the block at the respawn position is a bed
        if (level.getBlockState(respawnPos).getBlock() instanceof BedBlock) {
            double destinationX = respawnPos.getX() + 0.5;
            double destinationY = respawnPos.getY() + 0.6D;
            double destinationZ = respawnPos.getZ() + 0.5;

            // Check if the player is riding an entity
            if (player.getVehicle() != null) {
                // Get the entity instance being ridden
                Entity riddenEntity = player.getVehicle();

                // Stop the player from riding the entity
                player.stopRiding();

                // Delayed teleportation of the ridden entity
                executor.schedule(() -> {
                    assert riddenEntity != null;
                    riddenEntity.teleportTo(destinationX, destinationY, destinationZ);
                }, 100, TimeUnit.MILLISECONDS);

                player.teleportTo(destinationX, destinationY, destinationZ);
            } else {
                player.teleportTo(destinationX, destinationY, destinationZ);
            }

            // Play a teleportation sound at the respawn position
            level.playSound(null, respawnPos, SoundEvents.AMETHYST_CLUSTER_HIT, SoundSource.PLAYERS, 1.0F, 1.0F);

            // Define dust color transition options for particle effects
            DustColorTransitionOptions dustColorTransitionOptions = new DustColorTransitionOptions(
                    new Vector3f(0.0f, 1.0f, 1.0f),
                    new Vector3f(1.0f, 0.0f, 1.0f),
                    1.0f
            );

            // Convert the Level to ServerLevel for particle spawning
            ServerLevel serverLevel = (ServerLevel) level;

            // Spawn teleportation particles at the respawn position
            serverLevel.sendParticles(player, dustColorTransitionOptions, true, respawnPos.getX() + 0.5, respawnPos.getY() + 0.6D, respawnPos.getZ() + 0.5, 85, 0.85D, 0.75D, .85D, 0.005D);
        } else {
            // Display a message indicating no access to the bed
            player.displayClientMessage(Component.translatable("item.backtobed.magical_returner.condition.no_access_to_bed"), true);
        }

        // Stop item usage and add cooldown
        player.stopUsingItem();
        player.getCooldowns().addCooldown(this, ITEM_COOLDOWN_TICKS);
    }

    /**
     * Gets the animation type for item usage.
     *
     * @param stack The ItemStack representing the item.
     * @return The UseAnim value representing the animation type.
     */
    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    /**
     * Handles the player's interaction with the item.
     *
     * @param worldIn  The current Level.
     * @param playerIn The player interacting with the item.
     * @param handIn   The hand used for interaction.
     * @return An InteractionResultHolder containing the result of the interaction.
     */
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        playerIn.startUsingItem(handIn);

        return InteractionResultHolder.consume(playerIn.getItemInHand(handIn));
    }
}

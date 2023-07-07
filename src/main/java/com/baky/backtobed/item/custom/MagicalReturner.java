package com.baky.backtobed.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagicalReturner extends Item {

    public MagicalReturner(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);

        // Add item tooltip information
        tooltipComponents.add(Component.translatable("magical_returner.info1").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("magical_returner.info2").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal(""));

        // Check if shift is pressed to show additional information
        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("magical_returner.pet_teleport1").withStyle(ChatFormatting.GREEN));
            tooltipComponents.add(Component.translatable("magical_returner.pet_teleport2").withStyle(ChatFormatting.GREEN));
        } else {
            tooltipComponents.add(Component.translatable("magical_returner.shift_press").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW; // This makes the player perform the bow drawing animation while holding right-click
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        playerIn.startUsingItem(handIn);
        return InteractionResultHolder.consume(playerIn.getItemInHand(handIn));
    }
}

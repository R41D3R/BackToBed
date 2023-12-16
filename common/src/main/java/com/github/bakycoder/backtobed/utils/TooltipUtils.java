package com.github.bakycoder.backtobed.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for formatting and manipulating tooltips.
 */
public class TooltipUtils {
    /**
     * The maximum length of a text line in the tooltip.
     */
    private static final int MAX_TEXT_LINE_LENGTH = 28;

    /**
     * Formats a localized string from the language file and breaks it into lines with a specified style.
     *
     * @param key          The translation key to retrieve from the language file.
     * @param style        The ChatFormatting style to apply to the entire tooltip.
     * @param newLineAtEnd Whether to add an empty line at the end of the tooltip.
     * @return A list of formatted tooltip components.
     */
    public static List<Component> formatComponent(String key, ChatFormatting style, boolean newLineAtEnd) {
        List<Component> tooltipLines = new ArrayList<>();

        String text = I18n.get(key);

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        if (!currentLine.isEmpty()) {
            tooltipLines.add(Component.literal(currentLine.toString()).withStyle(style));
        }

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= MAX_TEXT_LINE_LENGTH) {
                if (!currentLine.isEmpty()) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                tooltipLines.add(Component.literal(currentLine.toString()).withStyle(style));
                currentLine = new StringBuilder(word);
            }
        }

        if (!currentLine.isEmpty()) {
            tooltipLines.add(Component.literal(currentLine.toString()).withStyle(style));
        }

        if (newLineAtEnd) {
            tooltipLines.add(Component.literal(""));
        }

        return tooltipLines;
    }

    /**
     * Formats a localized string from the language file and breaks it into lines with a specified style.
     * Does not add an empty line at the end.
     *
     * @param key   The translation key to retrieve from the language file.
     * @param style The ChatFormatting style to apply to the entire tooltip.
     * @return A list of formatted tooltip components.
     */
    public static List<Component> formatComponent(String key, ChatFormatting style) {
        return formatComponent(key, style, false);
    }

    /**
     * Creates a mutable tooltip component with a highlighted argument.
     *
     * @param key      The translation key to retrieve from the language file.
     * @param arg      The argument to be highlighted.
     * @param keyStyle The ChatFormatting style to apply to the non-argument part of the tooltip.
     * @param argStyle The ChatFormatting style to apply to the argument part of the tooltip.
     * @return The mutable tooltip component with the highlighted argument.
     */
    public static MutableComponent highlightComponentArg(String key, String arg, ChatFormatting keyStyle, ChatFormatting argStyle) {
        String text = I18n.get(key);

        MutableComponent textComponent = Component.literal(String.format(text, arg));

        int argStart = textComponent.getString().indexOf(arg);

        MutableComponent argComponent = Component.literal(arg).withStyle(argStyle);

        MutableComponent textBeforeArg = Component.literal(textComponent.getString().substring(0, argStart)).withStyle(keyStyle);
        MutableComponent textAfterArg = Component.literal(textComponent.getString().substring(argStart + arg.length())).withStyle(keyStyle);

        return textBeforeArg.append(argComponent).append(textAfterArg);
    }
}

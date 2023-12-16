package com.github.bakycoder.backtobed.fabric.item;

import com.github.bakycoder.backtobed.BackToBed;
import com.github.bakycoder.backtobed.fabric.BackToBedFabric;
import com.github.bakycoder.backtobed.item.MagicalReturner;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

/**
 * The utility class for registering items associated with the BackToBed mod. (Fabric version)
 * It provides methods to register individual items and is responsible for handling item registration.
 *
 * @see BackToBed
 * @see Item
 * @see ResourceLocation
 * @see Registry
 */
public class ModItems {
    /**
     * Registers a single item with the specified name and item instance.
     *
     * @param itemName The name of the item to register.
     * @param item     The item instance to register.
     */
    private static Item registerItem(String itemName, Item item) {
        ResourceLocation resourceLocation = new ResourceLocation(BackToBed.MOD_ID, itemName);
        return Registry.register(BuiltInRegistries.ITEM, resourceLocation, item);
    }

    /**
     * Registers an item and associates it with a Creative Mode item group.
     *
     * @param itemName  The name of the item to register.
     * @param item      The item instance to register.
     * @param itemGroup The Creative Mode item group to which the item belongs.
     */
    private static void registerItemAndAcceptToGroup(String itemName, Item item, ResourceKey<CreativeModeTab> itemGroup) {
        Item registeredItem = registerItem(itemName, item);
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> entries.accept(registeredItem));
    }

    /**
     * Registers all items associated with the BackToBed mod.
     * This method is called during mod initialization.
     *
     * @see BackToBedFabric
     */
    public static void registerItems() {
        BackToBed.LOGGER.info("Registering items...");

        // Register the MagicalReturner item and associate it with the TOOLS_AND_UTILITIES item group
        registerItemAndAcceptToGroup(MagicalReturner.ITEM_NAME, new MagicalReturner(), CreativeModeTabs.TOOLS_AND_UTILITIES);

        BackToBed.LOGGER.info("Items registered successfully!");
    }
}

package com.github.bakycoder.backtobed.forge.item;

import com.github.bakycoder.backtobed.BackToBed;
import com.github.bakycoder.backtobed.item.MagicalReturner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * The {@code ModItems} class handles the registration of items for the BackToBed mod. (Forge version)
 * It provides methods for registering and organizing custom items within the mod.
 */
public class ModItems {
    /**
     * The deferred register for items, associated with the mod ID.
     * This register is responsible for the deferred registration of items during mod initialization.
     */
    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BackToBed.MOD_ID);
    /**
     * A mapping of registered items to their corresponding Creative Mode tabs.
     */
    private static final HashMap<RegistryObject<Item>, ResourceKey<CreativeModeTab>> ITEM_TAB_MAP = new HashMap<>();

    /**
     * Registers an item and associates it with a Creative Mode tab.
     *
     * @param itemName The name of the item to be registered.
     * @param item     A supplier for creating the item instance.
     * @param tab      The Creative Mode tab to which the item belongs.
     */
    private static void registerItemAndAcceptToTab(String itemName, Supplier<Item> item, ResourceKey<CreativeModeTab> tab) {
        RegistryObject<Item> registeredItem = ITEMS.register(itemName, item);
        ITEM_TAB_MAP.put(registeredItem, tab);
    }

    /**
     * Handles the acceptance of items to Creative Mode tabs during the BuildContents event.
     *
     * @param event The BuildCreativeModeTabContentsEvent event.
     */
    public static void acceptItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        ITEM_TAB_MAP.entrySet().removeIf(entry -> entry.getKey() == null || entry.getValue() == null);

        ITEM_TAB_MAP.forEach((item, tab) -> {
            if (event.getTabKey() == tab) {
                event.accept(item);
            }
        });
    }

    /**
     * Registers items for the BackToBed mod.
     * This method should be called during mod initialization to register all custom items.
     *
     * @param eventBus The mod event bus used for item registration.
     * @see MagicalReturner
     */
    public static void registerItems(IEventBus eventBus) {
        BackToBed.LOGGER.info("Registering items...");

        // Register the MagicalReturner item and associate it with the TOOLS_AND_UTILITIES tab
        registerItemAndAcceptToTab(MagicalReturner.ITEM_NAME, MagicalReturner::new, CreativeModeTabs.TOOLS_AND_UTILITIES);

        ITEMS.register(eventBus);

        BackToBed.LOGGER.info("Items registered successfully!");
    }
}

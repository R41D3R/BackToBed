package com.github.bakycoder.backtobed.fabric;

import com.github.bakycoder.backtobed.BackToBed;
import com.github.bakycoder.backtobed.fabric.item.ModItems;
import net.fabricmc.api.ModInitializer;

/**
 * The main class for the BackToBed mod in the Fabric modding environment.
 * Implements the {@code ModInitializer} interface, providing a method for mod initialization.
 *
 * @see ModInitializer
 * @see ModItems
 */
public class BackToBedFabric implements ModInitializer {
    /**
     * Called on mod initialization. Registers items and performs other necessary setup.
     */
    @Override
    public void onInitialize() {
        BackToBed.LOGGER.info("Initializing mod...");

        try {
            ModItems.registerItems();

            BackToBed.LOGGER.info("Mod initialized successfully!");
        } catch (Exception e) {
            BackToBed.LOGGER.error("Failed to initialize mod", e);
        }
    }
}

package com.github.bakycoder.backtobed.forge;

import com.github.bakycoder.backtobed.BackToBed;
import com.github.bakycoder.backtobed.forge.item.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * The main class for the BackToBed mod, responsible for mod initialization.
 * This class is annotated with {@code @Mod} to mark it as the main mod class.
 * It handles the registration of items and event listeners during mod initialization.
 *
 * @see ModItems
 */
@Mod(BackToBed.MOD_ID)
public class BackToBedForge {
    private static final IEventBus MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

    /**
     * Initializes and registers mod components during mod initialization.
     */
    public BackToBedForge() {
        BackToBed.LOGGER.info("Initializing mod...");

        try {
            ModItems.registerItems(MOD_EVENT_BUS);

            // Registering this class as an event listener with MinecraftForge.
            MinecraftForge.EVENT_BUS.register(this);

            // Register the method for handling the CreativeModeTabEvent.BuildContents event
            MOD_EVENT_BUS.addListener(ModItems::acceptItemsToTabs);

            BackToBed.LOGGER.info("Mod initialized successfully!");
        } catch (Exception e) {
            BackToBed.LOGGER.error("Failed to initialize mod", e);
        }
    }
}

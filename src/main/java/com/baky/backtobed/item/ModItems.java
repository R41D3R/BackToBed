package com.baky.backtobed.item;

import com.baky.backtobed.BackToBed;
import com.baky.backtobed.item.custom.MagicalReturner;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BackToBed.MOD_ID);

    public static final RegistryObject<Item> MAGICAL_RETURNER = ITEMS.register("magical_returner",
            () -> new MagicalReturner(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

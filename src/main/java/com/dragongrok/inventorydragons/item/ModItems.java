package com.dragongrok.inventorydragons.item;

import com.dragongrok.inventorydragons.InventoryDragons;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item INVENTORY_DRAGON_EGG = registerItem("inventory_dragon_egg",
            new Item(new Item.Settings().maxCount(16)));

    public static final Item BABY_INVENTORY_DRAGON = registerItem("baby_inventory_dragon",
            new Item(new Item.Settings().maxCount(1)));

    public static final Item FIRE_INVENTORY_DRAGON = registerItem("fire_inventory_dragon",
            new Item(new Item.Settings().maxCount(1)));

    public static final Item ICE_INVENTORY_DRAGON = registerItem("ice_inventory_dragon",
            new Item(new Item.Settings().maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(InventoryDragons.MOD_ID, name), item);
    }

    public static void registerModItems() {
        InventoryDragons.LOGGER.info("Registering Inventory Dragons items...");
    }
}

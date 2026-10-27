package com.dragongrok.inventorydragons.item;

import com.dragongrok.inventorydragons.InventoryDragons;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    // Eggs (right-click to hatch)
    public static final Item INVENTORY_DRAGON_EGG = registerItem("inventory_dragon_egg",
            new DragonEggItem(new Item.Settings().maxCount(16)));

    public static final Item FIRE_DRAGON_EGG = registerItem("fire_dragon_egg",
            new DragonEggItem(new Item.Settings().maxCount(16)));

    public static final Item ICE_DRAGON_EGG = registerItem("ice_dragon_egg",
            new DragonEggItem(new Item.Settings().maxCount(16)));

    public static final Item NATURE_DRAGON_EGG = registerItem("nature_dragon_egg",
            new DragonEggItem(new Item.Settings().maxCount(16)));

    public static final Item WIND_DRAGON_EGG = registerItem("wind_dragon_egg",
            new DragonEggItem(new Item.Settings().maxCount(16)));

    // Dragons with NBT level/XP
    public static final Item BABY_INVENTORY_DRAGON = registerItem("baby_inventory_dragon",
            new InventoryDragonItem(new Item.Settings().maxCount(1), "Baby"));

    public static final Item FIRE_INVENTORY_DRAGON = registerItem("fire_inventory_dragon",
            new InventoryDragonItem(new Item.Settings().maxCount(1), "Fire"));

    public static final Item ICE_INVENTORY_DRAGON = registerItem("ice_inventory_dragon",
            new InventoryDragonItem(new Item.Settings().maxCount(1), "Ice"));

    public static final Item NATURE_INVENTORY_DRAGON = registerItem("nature_inventory_dragon",
            new InventoryDragonItem(new Item.Settings().maxCount(1), "Nature"));

    public static final Item SHADOW_INVENTORY_DRAGON = registerItem("shadow_inventory_dragon",
            new InventoryDragonItem(new Item.Settings().maxCount(1), "Shadow"));

    public static final Item LIGHT_INVENTORY_DRAGON = registerItem("light_inventory_dragon",
            new InventoryDragonItem(new Item.Settings().maxCount(1), "Light"));

    public static final Item WIND_INVENTORY_DRAGON = registerItem("wind_inventory_dragon",
            new InventoryDragonItem(new Item.Settings().maxCount(1), "Wind"));

    // Special items
    public static final Item DRAGON_TREAT = registerItem("dragon_treat",
            new Item(new Item.Settings().maxCount(64)));

    public static final Item DRAGON_WHISTLE = registerItem("dragon_whistle",
            new Item(new Item.Settings().maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(InventoryDragons.MOD_ID, name), item);
    }

    public static void registerModItems() {
        InventoryDragons.LOGGER.info("Registering Inventory Dragons items with hatching & NBT support...");
    }
}

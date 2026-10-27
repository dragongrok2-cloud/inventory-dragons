package com.dragongrok.inventorydragons.system;

import com.dragongrok.inventorydragons.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Handles hatching of Inventory Dragon Eggs into baby or typed dragons.
 */
public class HatchingSystem {

    private static final Map<Item, Item> EGG_TO_DRAGON = new HashMap<>();
    private static final Random RANDOM = new Random();

    static {
        EGG_TO_DRAGON.put(ModItems.FIRE_DRAGON_EGG, ModItems.FIRE_INVENTORY_DRAGON);
        EGG_TO_DRAGON.put(ModItems.ICE_DRAGON_EGG, ModItems.ICE_INVENTORY_DRAGON);
        EGG_TO_DRAGON.put(ModItems.NATURE_DRAGON_EGG, ModItems.NATURE_INVENTORY_DRAGON);
        EGG_TO_DRAGON.put(ModItems.WIND_DRAGON_EGG, ModItems.WIND_INVENTORY_DRAGON);
        // Generic egg can hatch into any type or baby
    }

    /**
     * Attempts to hatch an egg.
     * Returns the resulting dragon ItemStack, or empty if not an egg.
     */
    public static ItemStack tryHatch(ItemStack eggStack) {
        if (eggStack.isEmpty()) return ItemStack.EMPTY;

        Item egg = eggStack.getItem();

        if (EGG_TO_DRAGON.containsKey(egg)) {
            Item dragon = EGG_TO_DRAGON.get(egg);
            return new ItemStack(dragon);
        }

        // Generic Inventory Dragon Egg → random typed dragon or baby
        if (egg == ModItems.INVENTORY_DRAGON_EGG) {
            Item[] possible = {
                    ModItems.BABY_INVENTORY_DRAGON,
                    ModItems.FIRE_INVENTORY_DRAGON,
                    ModItems.ICE_INVENTORY_DRAGON,
                    ModItems.NATURE_INVENTORY_DRAGON,
                    ModItems.WIND_INVENTORY_DRAGON,
                    ModItems.SHADOW_INVENTORY_DRAGON,
                    ModItems.LIGHT_INVENTORY_DRAGON
            };
            Item result = possible[RANDOM.nextInt(possible.length)];
            return new ItemStack(result);
        }

        return ItemStack.EMPTY;
    }

    public static boolean isDragonEgg(Item item) {
        return item == ModItems.INVENTORY_DRAGON_EGG
                || item == ModItems.FIRE_DRAGON_EGG
                || item == ModItems.ICE_DRAGON_EGG
                || item == ModItems.NATURE_DRAGON_EGG
                || item == ModItems.WIND_DRAGON_EGG;
    }
}

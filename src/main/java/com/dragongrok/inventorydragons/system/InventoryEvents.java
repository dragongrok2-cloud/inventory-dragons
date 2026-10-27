package com.dragongrok.inventorydragons.system;

import com.dragongrok.inventorydragons.InventoryDragons;
import com.dragongrok.inventorydragons.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

/**
 * Handles inventory-related events for Inventory Dragons.
 * - Passive buffs while dragons are in inventory
 * - Feeding with treats
 * - Protection logic (placeholder)
 */
public class InventoryEvents {

    /**
     * Called when a player uses a Dragon Treat on a dragon item in hand.
     */
    public static boolean tryFeedDragon(PlayerEntity player, ItemStack treatStack, ItemStack dragonStack) {
        if (treatStack.getItem() != ModItems.DRAGON_TREAT) return false;
        if (!isInventoryDragon(dragonStack.getItem())) return false;

        // Consume treat
        treatStack.decrement(1);

        // TODO: Increase XP / level on the dragon (will need NBT later)
        player.sendMessage(net.minecraft.text.Text.literal("The dragon happily eats the treat! (+XP)"), true);

        return true;
    }

    public static boolean isInventoryDragon(Item item) {
        return item == ModItems.BABY_INVENTORY_DRAGON
                || item == ModItems.FIRE_INVENTORY_DRAGON
                || item == ModItems.ICE_INVENTORY_DRAGON
                || item == ModItems.NATURE_INVENTORY_DRAGON
                || item == ModItems.SHADOW_INVENTORY_DRAGON
                || item == ModItems.LIGHT_INVENTORY_DRAGON
                || item == ModItems.WIND_INVENTORY_DRAGON;
    }

    /**
     * Placeholder for passive effects while dragons are in the player's inventory.
     * Will be called from a tick event later.
     */
    public static void applyPassiveBuffs(PlayerEntity player) {
        // TODO: Check inventory for dragons and apply status effects based on type & level
        // Fire → Fire Resistance
        // Ice → Frost Resistance
        // Wind → Speed / Slow Falling
        // etc.
    }

    /**
     * Placeholder: protect items from despawn or death if a dragon is present.
     */
    public static boolean shouldProtectInventory(PlayerEntity player) {
        // TODO: return true if player has at least one inventory dragon
        return false;
    }
}

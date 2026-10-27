package com.dragongrok.inventorydragons.system;

import com.dragongrok.inventorydragons.item.InventoryDragonItem;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Protects Inventory Dragons from being lost on death.
 * If the player has at least one Inventory Dragon, all dragons are kept.
 */
public class DeathProtectionHandler {

    public static void register() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            if (alive) return; // not a death copy

            // Collect all dragons from the old inventory
            List<ItemStack> dragons = new ArrayList<>();
            for (int i = 0; i < oldPlayer.getInventory().size(); i++) {
                ItemStack stack = oldPlayer.getInventory().getStack(i);
                if (stack.getItem() instanceof InventoryDragonItem) {
                    dragons.add(stack.copy());
                }
            }

            if (dragons.isEmpty()) return;

            // Give them back to the new player
            for (ItemStack dragon : dragons) {
                if (!newPlayer.getInventory().insertStack(dragon)) {
                    newPlayer.dropItem(dragon, false);
                }
            }

            newPlayer.sendMessage(net.minecraft.text.Text.literal(
                    "Your Inventory Dragons protected themselves and returned to you!"), false);
        });
    }
}

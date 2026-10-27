package com.dragongrok.inventorydragons.system;

import com.dragongrok.inventorydragons.item.InventoryDragonItem;
import com.dragongrok.inventorydragons.item.ModItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Handles inventory tick events for passive dragon buffs.
 * Runs every second (20 ticks) for performance.
 */
public class InventoryTickHandler {

    private static int tickCounter = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;
            if (tickCounter < 20) return; // every second
            tickCounter = 0;

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                applyPassiveBuffs(player);
            }
        });
    }

    private static void applyPassiveBuffs(PlayerEntity player) {
        boolean hasFire = false;
        boolean hasIce = false;
        boolean hasNature = false;
        boolean hasWind = false;
        boolean hasShadow = false;
        boolean hasLight = false;
        int maxLevel = 1;

        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            Item item = stack.getItem();

            if (!(item instanceof InventoryDragonItem dragonItem)) continue;

            int level = InventoryDragonItem.getLevel(stack);
            maxLevel = Math.max(maxLevel, level);

            switch (dragonItem.getDragonType()) {
                case "Fire" -> hasFire = true;
                case "Ice" -> hasIce = true;
                case "Nature" -> hasNature = true;
                case "Wind" -> hasWind = true;
                case "Shadow" -> hasShadow = true;
                case "Light" -> hasLight = true;
            }
        }

        // Apply buffs (duration slightly over 1 second so they don't flicker)
        int duration = 30;
        int amplifier = Math.min(maxLevel / 3, 2); // stronger with higher levels

        if (hasFire) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, duration, 0, true, false, true));
        }
        if (hasIce) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, duration, 0, true, false, true));
        }
        if (hasNature) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, duration, amplifier > 0 ? 0 : 0, true, false, true));
        }
        if (hasWind) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, duration, amplifier, true, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, duration, 0, true, false, true));
        }
        if (hasShadow) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, duration + 200, 0, true, false, true));
        }
        if (hasLight) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, duration, 0, true, false, true));
        }
    }
}

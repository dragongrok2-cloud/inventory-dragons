package com.dragongrok.inventorydragons.sound;

import com.dragongrok.inventorydragons.InventoryDragons;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Sound events for Inventory Dragons.
 * Actual .ogg files will be added later in assets/inventory-dragons/sounds/
 */
public class ModSounds {

    public static final SoundEvent DRAGON_HATCH = register("dragon_hatch");
    public static final SoundEvent DRAGON_EAT = register("dragon_eat");
    public static final SoundEvent DRAGON_ROAR = register("dragon_roar");
    public static final SoundEvent DRAGON_PURR = register("dragon_purr");
    public static final SoundEvent DRAGON_WINGS = register("dragon_wings");
    public static final SoundEvent DRAGON_WHISTLE = register("dragon_whistle");

    private static SoundEvent register(String name) {
        Identifier id = Identifier.of(InventoryDragons.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerModSounds() {
        InventoryDragons.LOGGER.info("Registering Inventory Dragons sounds...");
    }
}

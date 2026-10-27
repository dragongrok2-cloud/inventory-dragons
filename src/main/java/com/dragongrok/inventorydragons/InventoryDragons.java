package com.dragongrok.inventorydragons;

import com.dragongrok.inventorydragons.entity.ModEntities;
import com.dragongrok.inventorydragons.item.ModItems;
import com.dragongrok.inventorydragons.sound.ModSounds;
import com.dragongrok.inventorydragons.system.DeathProtectionHandler;
import com.dragongrok.inventorydragons.system.InventoryTickHandler;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryDragons implements ModInitializer {
    public static final String MOD_ID = "inventory-dragons";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Rrrrr... Inventory Dragons fully loaded! Hatch, feed, summon, protect!");

        ModItems.registerModItems();
        ModEntities.registerModEntities();
        ModSounds.registerModSounds();

        // Passive buffs while dragons are in inventory
        InventoryTickHandler.register();

        // Dragons survive player death
        DeathProtectionHandler.register();
    }
}

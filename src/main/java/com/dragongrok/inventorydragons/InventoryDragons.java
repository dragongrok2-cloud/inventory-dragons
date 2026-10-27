package com.dragongrok.inventorydragons;

import com.dragongrok.inventorydragons.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryDragons implements ModInitializer {
    public static final String MOD_ID = "inventory-dragons";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Rrrrr... Inventory Dragons loaded! Dragons are now living in your pockets!");

        ModItems.registerModItems();

        // TODO: Register inventory dragon entities, eggs, abilities, sounds...
    }
}

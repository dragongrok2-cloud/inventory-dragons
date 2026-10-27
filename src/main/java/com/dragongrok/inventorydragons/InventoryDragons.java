package com.dragongrok.inventorydragons;

import com.dragongrok.inventorydragons.entity.ModEntities;
import com.dragongrok.inventorydragons.item.ModItems;
import com.dragongrok.inventorydragons.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryDragons implements ModInitializer {
    public static final String MOD_ID = "inventory-dragons";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Rrrrr... Inventory Dragons loaded! Dragons (including Wind) are now living in your pockets!");

        ModItems.registerModItems();
        ModEntities.registerModEntities();
        ModSounds.registerModSounds();

        // Systems ready: HatchingSystem, InventoryEvents, DragonLeveling
    }
}

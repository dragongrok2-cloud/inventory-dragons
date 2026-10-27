package com.dragongrok.inventorydragons.item;

import com.dragongrok.inventorydragons.sound.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * Dragon Treat - right-click while holding a dragon in the other hand (or offhand)
 * to feed it and give XP.
 */
public class DragonTreatItem extends Item {

    public DragonTreatItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack treatStack = user.getStackInHand(hand);
        Hand otherHand = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack otherStack = user.getStackInHand(otherHand);

        if (otherStack.getItem() instanceof InventoryDragonItem) {
            if (!world.isClient) {
                // Feed the dragon
                InventoryDragonItem.addXp(otherStack, 25);

                treatStack.decrement(1);

                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        ModSounds.DRAGON_EAT, SoundCategory.PLAYERS, 1.0f, 1.2f);

                int newLevel = InventoryDragonItem.getLevel(otherStack);
                user.sendMessage(net.minecraft.text.Text.literal(
                        "The dragon enjoys the treat! (Level " + newLevel + ")"), true);
            }
            return TypedActionResult.success(treatStack, world.isClient());
        }

        return TypedActionResult.pass(treatStack);
    }
}

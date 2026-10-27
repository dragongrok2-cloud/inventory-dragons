package com.dragongrok.inventorydragons.item;

import com.dragongrok.inventorydragons.sound.ModSounds;
import com.dragongrok.inventorydragons.system.HatchingSystem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * Custom item for all Inventory Dragon Eggs.
 * Right-click to hatch into a dragon.
 */
public class DragonEggItem extends Item {

    public DragonEggItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack eggStack = user.getStackInHand(hand);

        if (!world.isClient) {
            ItemStack hatched = HatchingSystem.tryHatch(eggStack);

            if (!hatched.isEmpty()) {
                // Consume one egg
                eggStack.decrement(1);

                // Give the dragon to the player
                if (!user.getInventory().insertStack(hatched)) {
                    user.dropItem(hatched, false);
                }

                // Play hatch sound
                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        ModSounds.DRAGON_HATCH, SoundCategory.PLAYERS, 1.0f, 1.0f);

                user.sendMessage(net.minecraft.text.Text.literal("The egg hatches! A new Inventory Dragon is born!"), true);

                return TypedActionResult.success(eggStack, false);
            }
        }

        return TypedActionResult.pass(eggStack);
    }
}

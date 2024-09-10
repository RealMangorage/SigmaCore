package org.mangorage.sigmacore;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.mangorage.sigmacoremixins.services.ServiceId;

import java.util.ArrayList;
import java.util.List;

public class CreeperInventory implements CustomInventory {
    public static final ServiceId<CustomInventory> CUSTOM_INV = ServiceId.create(CustomInventory.class, "creeper_inventory");

    private final List<ItemStack> stacks = new ArrayList<>();


    @Override
    public void add(ItemStack stack) {
        stacks.add(stack);
    }

    @Override
    public void drop(Location location) {
        stacks.forEach(a -> {
            location.getWorld().dropItem(location, a);
        });
    }
}

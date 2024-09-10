package org.mangorage.sigmacore;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public interface CustomInventory {
    void add(ItemStack stack);
    void drop(Location location);
}

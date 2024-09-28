package org.mangorage.sigmacore.services.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ExitMenu extends Menu {

    protected ExitMenu() {
        super(Component.text("Confirm Exit?"), 27);

        var stack = new ItemStack(Material.GREEN_CONCRETE);
        stack.editMeta(itemMeta -> itemMeta.itemName(Component.text("Yes")));
        setItem(0, stack);

        addSlotListener(0, e -> {
            e.setCancelled(true);
            close((Player) e.getView().getPlayer());
        });
    }
}

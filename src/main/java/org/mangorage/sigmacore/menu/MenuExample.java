package org.mangorage.sigmacore.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mangorage.sigmacore.SigmaCore;

public class MenuExample extends Menu implements Listener {
    private final Inventory INV;

    public MenuExample() {
        this.INV = Bukkit.createInventory(null, 27);
        Bukkit.getPluginManager().registerEvents(this, SigmaCore.getPlugin());
        INV.setItem(0, new ItemStack(Material.COBBLESTONE));
        INV.setItem(9, new ItemStack(Material.COBBLESTONE));
    }

    public void open(Player player) {
        player.openInventory(INV);
    }

    @EventHandler
    public void onInv(InventoryClickEvent event) {
        System.out.println("1091");
        if (event.getView().getTopInventory() == INV) {
            event.setCancelled(true);
        }
    }
}

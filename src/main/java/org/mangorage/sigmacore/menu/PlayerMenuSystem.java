package org.mangorage.sigmacore.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.mangorage.sigmacore.SigmaCore;
import org.mangorage.sigmacoremixins.services.ServiceId;

public class PlayerMenuSystem implements MenuSystem, Listener {
    public static final ServiceId<MenuSystem> MENU_SYSTEM = ServiceId.create(MenuSystem.class, "player_menu_system");

    private final Player player;
    private Menu menu;

    public PlayerMenuSystem(Player player) {
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, SigmaCore.getPlugin());
    }

    @Override
    public void setMenu(Menu menu) {
        if (this.menu != null)
            menu.close(player);
        this.menu = menu;
        menu.open(player);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        onEvent(event);
    }

    @Override
    public void onEvent(Event event) {
        menu.onEvent(event);
    }
}

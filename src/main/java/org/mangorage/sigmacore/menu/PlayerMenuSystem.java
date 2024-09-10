package org.mangorage.sigmacore.menu;

import org.bukkit.entity.Player;
import org.mangorage.sigmacoremixins.services.ServiceId;

public class PlayerMenuSystem implements MenuSystem {
    public static final ServiceId<MenuSystem> MENU_SYSTEM = ServiceId.create(MenuSystem.class, "player_menu_system");

    private final Player player;

    public PlayerMenuSystem(Player player) {
        this.player = player;
    }

    @Override
    public void openMenu(Menu menu) {
        menu.open(player);
    }
}

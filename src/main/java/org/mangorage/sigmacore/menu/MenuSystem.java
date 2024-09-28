package org.mangorage.sigmacore.menu;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryEvent;

public interface MenuSystem {
    void setMenu(Menu menu);
    void onEvent(Event event);
}

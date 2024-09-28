package org.mangorage.sigmacore.services.menu;

import org.bukkit.event.Event;

public interface MenuSystem {
    void setMenu(Menu menu);
    void onEvent(Event event);
}

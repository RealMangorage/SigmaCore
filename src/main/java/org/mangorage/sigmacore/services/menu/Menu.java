package org.mangorage.sigmacore.services.menu;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Menu {
    private final Int2ObjectMap<Consumer<InventoryClickEvent>> slots = new Int2ObjectOpenHashMap<>();
    private final Inventory inventory;
    private Player player = null;

    protected Menu(Component component, int slots) {
        this.inventory = Bukkit.createInventory(null, slots, component);
    }

    protected void setItem(int slot, ItemStack stack) {
        inventory.setItem(slot, stack);
    }

    protected void addSlotListener(int slot, Consumer<InventoryClickEvent> eventConsumer) {
        slots.put(slot, eventConsumer);
    }

    protected Inventory getInventory() {
        return inventory;
    }

    protected Player getPlayer() {
        return player;
    }

    protected void open(Player player) {
        if (this.player != null) return;
        this.player = player;
        player.openInventory(inventory);
    }

    protected void close(Player player) {
        if (this.player != player) return;
        this.player = null;
        player.closeInventory();
    }

    protected void onEvent(Event event) {
        if (event instanceof InventoryClickEvent event1) {
            if (event1.getClickedInventory() != inventory) return;
            var m = slots.get(event1.getSlot());
            if (m != null) m.accept(event1);
        }
    }
}

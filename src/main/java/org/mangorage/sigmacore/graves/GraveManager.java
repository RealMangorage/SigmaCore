package org.mangorage.sigmacore.graves;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GraveManager implements Listener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Map<UUID, Grave> GRAVES = new ConcurrentHashMap<>();

    private final Plugin plugin;
    private final String id;

    public GraveManager(Plugin plugin, String id) {
        this.plugin = plugin;
        this.id = id;

        var file = Path.of("plugins/" + id);
        try {
            Files.createDirectories(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!event.getPlayer().hasPermission("sigmacore.graves.use")) return;

        var list = Arrays.stream(event.getDrops().toArray(ItemStack[]::new)).map(ItemStack::clone).toList();
        event.getDrops().clear();
        if (list.isEmpty()) return;
        addGrave(event.getPlayer().getLocation(), event.getPlayer(), list);
    }

    @EventHandler
    public void onPlayerClickEntity(PlayerInteractAtEntityEvent event) {
        GRAVES.forEach((k, g) -> {
            if (g.click(event.getRightClicked(), event.getPlayer())) {
                GRAVES.remove(g.getId()); // REMOVED
                save();
            }
        });
    }

    public void addGrave(Location location, Player player, List<ItemStack> items) {
        var grave = new Grave(UUID.randomUUID(), location, player, items);
        GRAVES.put(grave.getId(), grave);
        save();
    }

    record data(List<Grave.SaveData> data) {}


    // (UUID uuid, List<UUID> entities, UUID graveID, UUID playerID, List<ItemStack> items
    public void load() throws FileNotFoundException {
        var file = Path.of("plugins/%s/data.json".formatted(id));
        if (Files.exists(file)) {
            var list = GSON.fromJson(new FileReader(file.toFile()), data.class).data;
            list.forEach(sd -> {
                Grave grave = new Grave(
                        sd.uuid(),
                        sd.entities(),
                        Location.deserialize(sd.location()),
                        sd.graveID(),
                        sd.playerId(),
                        sd.itemStacks().stream().map(ItemStack::deserialize).toList()
                );
                GRAVES.put(grave.getId(), grave);
            });
        }
    }

    public void save() {
        List<Grave.SaveData> list = new ArrayList<>();
        GRAVES.values().forEach(g -> list.add(g.getSaveData()));

        try (var file = new FileWriter(Path.of("plugins/%s/data.json".formatted(id)).toFile())) {
            file.write(GSON.toJson(new data(list)));
            file.flush();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void unload() {
        GRAVES.clear();
    }
}

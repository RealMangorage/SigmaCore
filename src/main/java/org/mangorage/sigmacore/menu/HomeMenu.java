package org.mangorage.sigmacore.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mangorage.sigmacoremixins.services.IServiceHolder;

import java.util.Random;

public class HomeMenu extends Menu {
    private static final ItemStack[] STACKS = new ItemStack[]{
            Material.COAL.asItemType().createItemStack(),
            Material.ACACIA_DOOR.asItemType().createItemStack(),
            Material.BEDROCK.asItemType().createItemStack()
    };
    private static final Random random = new Random();


    public HomeMenu() {
        super(Component.text("Home Menu"), 27);

        var stack = new ItemStack(Material.BARRIER);
        stack.editMeta(m -> m.itemName(Component.text("Exit")));
        setItem(0, stack);

        addSlotListener(0, e -> {
            e.setCancelled(true);
            if (getPlayer() instanceof IServiceHolder serviceHolder) {
                serviceHolder.getServiceOptional(PlayerMenuSystem.MENU_SYSTEM).ifPresent(ms -> ms.setMenu(new ExitMenu()));
            }
        });
    }
}

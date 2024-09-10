package org.mangorage.sigmacore.graves;


import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Grave {
    private static final Random RANDOM = new Random();
    private static final Material[] SLABS = new Material[]{Material.OAK_SLAB, Material.STONE_SLAB, Material.COBBLESTONE_SLAB, Material.MOSSY_COBBLESTONE_SLAB};
    private static final Material[] SIGNS = new Material[]{Material.OAK_SIGN, Material.DARK_OAK_SIGN, Material.BAMBOO_SIGN, Material.CHERRY_SIGN};

    private static Material getSlab() {
        return SLABS[RANDOM.nextInt(0, SLABS.length)];
    }

    private static Material getSign() {
        return SIGNS[RANDOM.nextInt(0, SIGNS.length)];
    }

    public record SaveData(
            UUID uuid,
            List<UUID> entities,
            UUID graveID,
            UUID playerId,
            Map<String, Object> location,
            List<Map<String, Object>> itemStacks
    ){}

    private final UUID uuid;
    private final UUID playerID;
    private final UUID graveID;
    private final Location location;
    private final ArrayList<UUID> entities = new ArrayList<>();
    private final List<ItemStack> items = new ArrayList<>();

    // SPAWN FROM DISK
    public Grave(UUID uuid, List<UUID> entities, Location location, UUID graveID, UUID playerID, List<ItemStack> items) {
        this.uuid = uuid;
        this.entities.addAll(entities);
        this.graveID = graveID;
        this.playerID = playerID;
        this.items.addAll(items);
        this.location = location.clone();
    }

    // SPAWN NEW
    public Grave(UUID uuid, Location location, Player Owner, List<ItemStack> items) {
        this.uuid = uuid;
        this.playerID = Owner.getUniqueId();
        this.graveID = place(Owner, location.clone());
        this.items.addAll(items);
        this.location = location.toBlockLocation().clone();
    }

    public boolean canOpen(Player player) {
        return player.getUniqueId().equals(playerID);
    }

    public boolean click(Entity entity, Player player) {
        if (entity.getUniqueId().equals(graveID)) {
            if (!canOpen(player)) {
                player.sendMessage(Component.text().content("[Graves] Cannot open this Grave."));
                return false;
            }
            entities.forEach(e -> {
                var entity2 = player.getWorld().getEntity(e);
                if (entity2 != null)
                    entity2.remove();

            });

            for (ItemStack item : items) {
                player.getWorld().spawnEntity(player.getLocation(), EntityType.ITEM, CreatureSpawnEvent.SpawnReason.CUSTOM, e2 -> {
                    if (e2 instanceof Item item1)
                        item1.setItemStack(item);
                });
            }

            return true;
        }
        return false;
    }

    private UUID place(Player player, Location location) {
        var world = location.getWorld();
        location = location.getBlock().getLocation();
        location.setPitch(0);
        location.setYaw(0);

        var BD = (BlockDisplay) world.createEntity(location, BlockDisplay.class);
        if (getSlab().createBlockData() instanceof Slab slab) {
            slab.setType(Slab.Type.BOTTOM);
            BD.setBlock(slab);
            BD.setGravity(false);
            BD.setNoPhysics(false);
        }

        var ID = (ItemDisplay) world.createEntity(location.subtract(-0.5, -0.7, -0.1), ItemDisplay.class);
        ID.setItemStack(
                ItemStack.of(getSign(), 1)
        );

        var TD = (TextDisplay) world.createEntity(location.add(-0.05, 0, 0.03), TextDisplay.class);
        TD.text(
                Component.text()
                        .append(
                                Component.text()
                                        .content("Here Lies:")
                                        .build()
                        )
                        .appendNewline()
                        .append(
                                Component.text()
                                        .content(player.getName())
                                        .build()
                        )
                        .build()
        );

        TD.setViewRange(100);
        TD.setAlignment(TextDisplay.TextAlignment.CENTER);
        TD.setGravity(true);
        TD.setVisibleByDefault(true);

        TD.setTransformation(
                new Transformation(
                        new Vector3f(),
                        new AxisAngle4f(),
                        new Vector3f(0.5f, 0.5f, 0f),
                        new AxisAngle4f()
                )
        );

        var clicker = (ArmorStand) world.createEntity(location.add(0, -0.6, 0.4), ArmorStand.class);
        clicker.setVisible(false);
        clicker.setGravity(false);
        clicker.setInvulnerable(true);

        entities.add(TD.getUniqueId());
        entities.add(BD.getUniqueId());
        entities.add(ID.getUniqueId());
        entities.add(clicker.getUniqueId());

        world.addEntity(BD);
        world.addEntity(TD);
        world.addEntity(ID);
        world.addEntity(clicker);

        return clicker.getUniqueId();
    }

    public SaveData getSaveData() {
        return new SaveData(
                uuid,
                entities,
                graveID,
                playerID,
                location.serialize(),
                items.stream().map(ItemStack::serialize).toList()
        );
    }

    public UUID getId() {
        return uuid;
    }
}

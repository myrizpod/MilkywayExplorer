package fr.myriapod.milkywayexplorer.surface.listeners;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.surface.machinery.Conveyor;
import fr.myriapod.milkywayexplorer.surface.machinery.Machinery;
import fr.myriapod.milkywayexplorer.surface.machinery.Producter;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.ConveyorType;
import fr.myriapod.milkywayexplorer.tools.SaveFile;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.joml.Vector3i;

import java.util.*;

public class ConveyorManager {


    //TODO remove all if /stop
    private static Map<UUID, List<Block>> actualConveyorPoseByPlayer = new HashMap<>();
    private static Map<UUID, Machinery> actualMachineryByPlayer = new HashMap<>();

    private final String CONVEYOR_PART_TAG = "conveyor_part";



    //TODO REWORK CONVEYOR CASE WITH SNAKE TECHNIC
    public void interactWithEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        Location entityLoc = entity.getLocation();
        Planet planet = Game.getPlayerPlanet(player);

        Entity interaction = event.getRightClicked();
        Set<String> tags = interaction.getScoreboardTags();

        if(tags.isEmpty()) return;

        if(planet == null) return;

        if(tags.contains(CONVEYOR_PART_TAG)) {
            tags.remove(CONVEYOR_PART_TAG);

            World world = planet.getSurfacePlanet().getWorld();
            Block block = world.getBlockAt(entityLoc);

            actualConveyorPoseByPlayer.putIfAbsent(player.getUniqueId(), new ArrayList<>());
            List<Block> conveyors = actualConveyorPoseByPlayer.get(player.getUniqueId());
            conveyors.add(block);
            actualConveyorPoseByPlayer.put(player.getUniqueId(), conveyors);

            block.setType(Material.STONE_SLAB);
            entity.remove();


            BoundingBox blockHitbox = BoundingBox.of(block);
            blockHitbox.expand(player.getFacing().getOppositeFace(), 1);

            Collection<Entity> nearbyEntities = world.getNearbyEntities(blockHitbox);
            List<Machinery> nearbyMachinery = new ArrayList<>();

            for(Entity e : nearbyEntities) {
                Machinery m = planet.getMachinery(e.getUniqueId());
                if(m == null) continue;

                nearbyMachinery.add(m);
            }

            if(nearbyMachinery.size() == 1) {
                if(actualMachineryByPlayer.get(player.getUniqueId()).equals(nearbyMachinery.get(0))) {
                    player.sendMessage(ChatColor.RED + "Vous ne pouvez pas relier de machine à elle-meme !");
                    event.setCancelled(true);
                    return;
                }

                List<Block> conveyorConstructor = actualConveyorPoseByPlayer.get(player.getUniqueId());
                Block middle = conveyorConstructor.get((int) conveyorConstructor.size()/2);
                Vector3i pos = new Vector3i(middle.getX(), middle.getY(), middle.getZ());

                middle.setType(Material.SMOOTH_STONE_SLAB);

                Interaction hitbox = (Interaction) world.spawnEntity(new Location(world, pos.x + 0.5, pos.y, pos.z + 0.5), EntityType.INTERACTION);
                hitbox.setInteractionHeight(2f);
                hitbox.addScoreboardTag("conveyor");

                Machinery m = new Conveyor(ConveyorType.BASIC, pos, (Producter) actualMachineryByPlayer.get(player.getUniqueId()), (Producter) nearbyMachinery.get(0));

                planet.addMachinery(hitbox.getUniqueId(), m);

                player.sendMessage(ChatColor.GREEN + "Vous avez relié deux machines avec des tapis roulants !");

                actualConveyorPoseByPlayer.put(player.getUniqueId(), new ArrayList<>());
                actualMachineryByPlayer.put(player.getUniqueId(), null);

                return;
            }

            createInteraction(player.getFacing().getOppositeFace(), block, world);

        }

    }



    public void playerInteract(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Planet planet = Game.getPlayerPlanet(player);
        Block block = event.getBlockPlaced();

        if(planet == null) {
            event.setCancelled(true);
            return;
        }
        if(! ConveyorType.BASIC.isItemEqual(event.getItemInHand())) {
            player.sendMessage("PAS UN CONVEYOR");
            event.setCancelled(true);
            return;
        }

        World world = planet.getSurfacePlanet().getWorld();
        BoundingBox blockHitbox = BoundingBox.of(block);
        blockHitbox.expand(player.getFacing(), 1);

        Collection<Entity> nearbyEntities = world.getNearbyEntities(blockHitbox);
        List<Machinery> nearbyMachinery = new ArrayList<>();

        for(Entity e : nearbyEntities) {
            Machinery m = planet.getMachinery(e.getUniqueId());
            if(m == null) continue;

            nearbyMachinery.add(m);
        }

        if(nearbyMachinery.size() != 1) {
            player.sendMessage(ChatColor.RED + "Il n'y a pas la place pour un tapis roulant ici !");
            event.setCancelled(true);
            return;
        }

        actualConveyorPoseByPlayer.putIfAbsent(player.getUniqueId(), new ArrayList<>());
        List<Block> conveyors = actualConveyorPoseByPlayer.get(player.getUniqueId());

        if(! conveyors.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Vous etes deja en train de placer un tapis roulant !");
            event.setCancelled(true);
            return;
        }


        conveyors.add(block);
        actualConveyorPoseByPlayer.put(player.getUniqueId(), conveyors);
        actualMachineryByPlayer.put(player.getUniqueId(), nearbyMachinery.get(0));

        createInteraction(player.getFacing().getOppositeFace(), block, world);
    }


    private void createInteraction(BlockFace face, Block block, World world) {
        double x = 0;
        double z = 0;
        if(face.equals(BlockFace.NORTH)) {
            x = 0.5;
            z = -0.5;
        } else if(face.equals(BlockFace.SOUTH)) {
            x = 0.5;
            z = 1.5;
        } else if (face.equals(BlockFace.EAST)) {
            x = 1.5;
            z = 0.5;
        } else if (face.equals(BlockFace.WEST)) {
            x = -0.5;
            z = 0.5;
        }

        Location createdHitbox = block.getLocation().add(new Vector(x, 0, z));
        Interaction interaction = (Interaction) world.spawnEntity(createdHitbox, EntityType.INTERACTION);
        interaction.setInteractionHeight(0.7f);
        interaction.addScoreboardTag(CONVEYOR_PART_TAG);
        interaction.addScoreboardTag(SaveFile.formatVectorAsString(new Vector3i(block.getX(), block.getY(), block.getZ())));

    }


}

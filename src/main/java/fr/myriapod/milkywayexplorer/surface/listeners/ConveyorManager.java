package fr.myriapod.milkywayexplorer.surface.listeners;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.surface.machinery.*;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.ConveyorType;
import fr.myriapod.milkywayexplorer.tools.SaveFile;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.joml.Vector3i;

import java.util.*;

public class ConveyorManager {


    private static final Map<UUID, List<Block>> actualConveyorPoseByPlayer = new HashMap<>();
    private static final Map<UUID, Machinery> actualMachineryByPlayer = new HashMap<>();

    private static final String CONVEYOR_PART_TAG = "conveyor_part";


    public void interactWithEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        Location entityLoc = entity.getLocation();
        Planet planet = Game.getPlayerPlanet(player);
        ItemStack itemInHand = player.getItemInHand();
        UUID uuid = player.getUniqueId();
        BlockFace facing = player.getFacing();

        Entity interaction = event.getRightClicked();
        Set<String> tags = interaction.getScoreboardTags();

        if(tags.isEmpty()) return;

        if(planet == null) return;

        if(ConveyorType.isItemAConveyor(itemInHand) == null) return;

        if(tags.contains(CONVEYOR_PART_TAG)) {
            World world = planet.getSurfacePlanet().getWorld();
            Block block = world.getBlockAt(entityLoc);

            BoundingBox blockHitbox = BoundingBox.of(block);
            blockHitbox.expand(facing.getOppositeFace(), 1);

            Collection<Entity> nearbyEntities = world.getNearbyEntities(blockHitbox);
            List<Machinery> nearbyMachinery = new ArrayList<>();

            for(Entity e : nearbyEntities) {
                Machinery m = planet.getMachinery(e.getUniqueId());
                if(m == null) continue;

                nearbyMachinery.add(m);
            }

            if(nearbyMachinery.size() == 1) {
                if (! (nearbyMachinery.get(0) instanceof Input)) {
                    player.sendMessage(ChatColor.RED + "Cette machine n'accepte pas de tapis roulant en entré !");
                    event.setCancelled(true);
                    return;
                }


                if (actualMachineryByPlayer.get(uuid).equals(nearbyMachinery.get(0))) {
                    player.sendMessage(ChatColor.RED + "Vous ne pouvez pas relier de machine à elle-meme !");
                    event.setCancelled(true);
                    return;
                }
            }

            actualConveyorPoseByPlayer.putIfAbsent(uuid, new ArrayList<>());
            List<Block> conveyors = actualConveyorPoseByPlayer.get(uuid);
            conveyors.add(block);
            actualConveyorPoseByPlayer.put(uuid, conveyors);

            if(nearbyMachinery.size() == 1) {
                List<Block> conveyorConstructor = actualConveyorPoseByPlayer.get(uuid);
                Block middle = conveyorConstructor.get(conveyorConstructor.size() / 2);
                Vector3i pos = new Vector3i(middle.getX(), middle.getY(), middle.getZ());

                middle.setType(Material.SMOOTH_STONE_SLAB);

                Interaction hitbox = (Interaction) world.spawnEntity(new Location(world, pos.x + 0.5, pos.y, pos.z + 0.5), EntityType.INTERACTION);
                hitbox.setInteractionHeight(2f);
                hitbox.addScoreboardTag("conveyor");

                Machinery m = new Conveyor(ConveyorType.isItemAConveyor(itemInHand), pos, (Output) actualMachineryByPlayer.get(uuid), (Input) nearbyMachinery.get(0));

                planet.addMachinery(hitbox.getUniqueId(), m);

                player.sendMessage(ChatColor.GREEN + "Vous avez relié deux machines avec des tapis roulants !");

                actualConveyorPoseByPlayer.put(uuid, new ArrayList<>());
                actualMachineryByPlayer.put(uuid, null);
            }


            Block blockInFront = block.getLocation().add(facing.getOppositeFace().getDirection()).getBlock();
            Bukkit.getLogger().info("block in front: " + blockInFront.getBoundingBox());

            if(actualConveyorPoseByPlayer.get(uuid).contains(blockInFront)) {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas mettre un tapis roulant ici !");
                event.setCancelled(true);
                return;
            }

            if(blockHitbox.overlaps(blockInFront.getBoundingBox())) {
                //put upward conveyor
                if(blockInFront.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.AIR)) {
                    createInteraction(facing.getOppositeFace(), block, world, 1);
                } else {
                    createInteraction(facing.getOppositeFace(), world.getBlockAt(block.getLocation().add(new Vector(facing.getModX(), 1, facing.getModZ()))), world, 0);
                }


            } else {
                block.setType(itemInHand.getType());

                if(tags.contains("down")) {
                    block.setType(Material.LADDER);
                }

                if(! blockInFront.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.AIR)) {
                    //put normal conveyor
                    createInteraction(facing.getOppositeFace(), block, world, 0);

                } else {
                    //put downward conveyor
                    Interaction down;
                    if(block.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.AIR)) {
                        down = createInteraction(facing.getOppositeFace(), world.getBlockAt(block.getLocation().add(new Vector(facing.getModX(), -1, facing.getModZ()))), world, 0);
                    } else {
                        down = createInteraction(facing.getOppositeFace(), block, world, -1);
                    }
                    down.addScoreboardTag("down");
                }

            }

            BlockState state = block.getState();
            Directional dir = (Directional) state.getBlockData();
            dir.setFacing(facing);
            state.setBlockData(dir);
            state.update();

            entity.remove();

        }

    }


    public void playerBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Planet planet = Game.getPlayerPlanet(player);
        Block block = event.getBlockPlaced();
        ItemStack itemInHand = player.getItemInHand();
        UUID uuid = player.getUniqueId();
        BlockFace facing = player.getFacing();

        if(planet == null) {
            event.setCancelled(true);
            return;
        }

        if(ConveyorType.isItemAConveyor(itemInHand) == null) return;

        World world = planet.getSurfacePlanet().getWorld();
        BoundingBox blockHitbox = BoundingBox.of(block);
        blockHitbox.expand(facing, 1);

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

        if(! (nearbyMachinery.get(0) instanceof Output)) {
            player.sendMessage(ChatColor.RED + "Cette machine n'accepte pas de tapis roulant en sortie !");
            event.setCancelled(true);
            return;
        }

        actualConveyorPoseByPlayer.putIfAbsent(uuid, new ArrayList<>());
        List<Block> conveyors = actualConveyorPoseByPlayer.get(uuid);

        if(! conveyors.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Vous etes deja en train de placer un tapis roulant !");
            event.setCancelled(true);
            return;
        }


        conveyors.add(block);
        actualConveyorPoseByPlayer.put(uuid, conveyors);
        actualMachineryByPlayer.put(uuid, nearbyMachinery.get(0));

        createInteraction(facing.getOppositeFace(), block, world, 0);
    }


    private Interaction createInteraction(BlockFace face, Block block, World world, int flat) {
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

        Location createdHitbox = block.getLocation().add(new Vector(x, flat, z));
        Interaction interaction = (Interaction) world.spawnEntity(createdHitbox, EntityType.INTERACTION);
        interaction.setInteractionHeight(0.7f);
        interaction.addScoreboardTag(CONVEYOR_PART_TAG);
        interaction.addScoreboardTag(SaveFile.formatVectorAsString(new Vector3i(block.getX(), block.getY(), block.getZ())));

        return interaction;
    }




    public static void resetAllPlacings() {
        actualMachineryByPlayer.clear();
        for(List<Block> allBlocks : actualConveyorPoseByPlayer.values()) {
            World world = allBlocks.get(0).getWorld();
            for(Entity e : world.getEntities()) {
                if(e.getScoreboardTags().contains(CONVEYOR_PART_TAG)) {
                    e.remove();
                }
            }

            for(Block b : allBlocks) {
                b.setType(Material.AIR);
            }

        }
    }


}

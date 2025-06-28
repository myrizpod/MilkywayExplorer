package fr.myriapod.milkywayexplorer.surface.listeners;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.surface.SurfacePlanet;
import fr.myriapod.milkywayexplorer.surface.machinery.Conveyor;
import fr.myriapod.milkywayexplorer.surface.machinery.Input;
import fr.myriapod.milkywayexplorer.surface.machinery.Output;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.ConveyorType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3i;

import java.util.*;

public class ConveyorManager {

    private static final Map<SurfacePlanet, Set<Conveyor>> allConveyorsPerPlanet = new HashMap<>();

    public void playerPlaceConveyorEvent(PlayerInteractEvent event, ConveyorType m) {
        //TODO SEE IF INTERACT_EVENT CREATES ERROR THEN MAYBE REPLACE WITH BLOCK_PLACE_EVENT
        poseLogic(event, m);
        event.setCancelled(true);
    }


    private void poseLogic(PlayerInteractEvent event, ConveyorType m) {
        Player player = event.getPlayer();
        final Block block = event.getClickedBlock();
        if(block == null) {
            event.setCancelled(true);
            return;
        }
        Block newBlock = event.getClickedBlock().getRelative(event.getBlockFace());

        BlockFace facing = player.getFacing().getOppositeFace();

        if(!newBlock.getType().equals(Material.AIR)) {
            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas mettre un tapis roulant ici !");
            event.setCancelled(true);
            return;
        }

        newBlock.setType(m.getMaterial());
        BlockState state = newBlock.getState();
        TrapDoor trapDoor = (TrapDoor) state.getBlockData();

        //if clicking on conveyor
        if(ConveyorType.getAllConveyorMaterials().contains(block.getType())) {
            BlockState blockState = block.getState();
            //if clicking on vertical conveyor and player is in air copy it one block above
            if(blockState.getBlockData() instanceof TrapDoor blockTrapDoor) {
                if (blockTrapDoor.isOpen()) {
                    if (!player.isOnGround()) {
                        newBlock.setType(Material.AIR);
                        newBlock = block.getLocation().clone().add(0, 1, 0).getBlock();
                        newBlock.setBlockData(blockTrapDoor);
                    } else {
                        trapDoor.setHalf(Bisected.Half.TOP);
                        if (player.isSneaking()) {
                            facing = facing.getOppositeFace();
                        }
                    }

                } else {
                    //If clicking on closed trapdoor, on top face
                    if(event.getBlockFace().equals(BlockFace.UP)) {
                        trapDoor.setOpen(true);

                        if (player.isSneaking()) {
                            trapDoor.setHalf(Bisected.Half.TOP);
                        }

                    } else {
                        trapDoor.setHalf(Bisected.Half.TOP);
                        if (player.isSneaking()) {
                            facing = facing.getOppositeFace();
                        }
                    }

                }
            }

//            } else {
//                trapDoor.setOpen(true);
//
//                if (player.isSneaking()) {
//                    trapDoor.setHalf(Bisected.Half.TOP);
//                }
//                facing = facing.getOppositeFace();
//            }

        } else {
            trapDoor.setHalf(Bisected.Half.TOP);
            if (player.isSneaking()) {
                facing = facing.getOppositeFace();
            }
        }

        if(block.getType().equals(Material.DISPENSER) || block.getType().equals(Material.DROPPER)) {
            newBlock.setType(Material.AIR);
            newBlock = newBlock.getLocation().clone().subtract(0,1,0).getBlock();
            if (! newBlock.getType().equals(Material.AIR)) {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas mettre un tapis roulant ici !");
                event.setCancelled(true);
                return;
            }
            newBlock.setType(m.getMaterial());
            state = newBlock.getState();
            trapDoor = (TrapDoor) state.getBlockData();
            trapDoor.setHalf(Bisected.Half.TOP);
            if(block.getType().equals(Material.DROPPER)) {
                facing = facing.getOppositeFace();
            }
        }


        trapDoor.setFacing(facing);
        state.setBlockData(trapDoor);
        state.update();



        //Remove item from inventory
        ItemStack itemToRemove = m.getAsItem();
        itemToRemove.setAmount(1);
        player.getInventory().removeItem(itemToRemove);



        //Check for input/output
        SurfacePlanet planet = Game.getPlayerPlanet(player).getSurfacePlanet();

        Input input = null;
        Output output = null;

        if(block.getType().equals(Material.DISPENSER)) {
            for(UUID uuid : planet.getAllMachineries().keySet()) {
                if(planet.getAllMachineries().get(uuid) instanceof Output o) {
                    if (block.getBoundingBox().overlaps(Bukkit.getEntity(uuid).getBoundingBox())) {
                        output = o;
                        break;
                    }
                }
            }
        }
        if(block.getType().equals(Material.DROPPER)) {
            for(UUID uuid : planet.getAllMachineries().keySet()) {
                if(planet.getAllMachineries().get(uuid) instanceof Input i) {
                    if (block.getBoundingBox().overlaps(Bukkit.getEntity(uuid).getBoundingBox())) {
                        input = i;
                        break;
                    }
                }
            }
        }

        Vector3i vPos = new Vector3i(newBlock.getX(), newBlock.getY(), newBlock.getZ());
        Conveyor c = new Conveyor(m, vPos, newBlock, input, output);
        allConveyorsPerPlanet.putIfAbsent(planet, new HashSet<>());
        Set<Conveyor> convs = allConveyorsPerPlanet.get(planet);
        convs.add(c);
        allConveyorsPerPlanet.put(planet, convs);

        for(Conveyor conv : convs) {
            conv.sendForwardItems();
        }

    }

    public void removeConveyor(SurfacePlanet surfacePlanet, Block block) {
        allConveyorsPerPlanet.putIfAbsent(surfacePlanet, new HashSet<>());
        Set<Conveyor> convs = allConveyorsPerPlanet.get(surfacePlanet);
        for(Conveyor c : convs) {
            if(c.isThisMe(block)) {
                convs.remove(c);
                //c.destroy(); TODO DESTROY METHOD IN MACHINERY
                break;
            }
        }
        allConveyorsPerPlanet.put(surfacePlanet, convs);
    }

    public Conveyor getConveyor(Block block) {
        SurfacePlanet planet = Game.getPlanetByWorld(block.getWorld()).getSurfacePlanet();
        for(Conveyor c : allConveyorsPerPlanet.get(planet)) {
            if(c.isThisMe(block)) {
                return c;
            }
        }
        return null;
    }
}

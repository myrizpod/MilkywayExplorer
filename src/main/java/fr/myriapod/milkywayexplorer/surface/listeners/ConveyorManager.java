package fr.myriapod.milkywayexplorer.surface.listeners;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.surface.SurfacePlanet;
import fr.myriapod.milkywayexplorer.surface.machinery.Conveyor;
import fr.myriapod.milkywayexplorer.surface.machinery.Input;
import fr.myriapod.milkywayexplorer.surface.machinery.Output;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.ConveyorType;
import org.bukkit.*;
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

    Map<SurfacePlanet, Set<Conveyor>> allConveyorsPerPlanet = new HashMap<>();

    public void playerPlaceConveyorEvent(PlayerInteractEvent event, ConveyorType m) {
        poseLogic(event, m);
        event.setCancelled(true);
    }


    private void poseLogic(PlayerInteractEvent event, ConveyorType m) {
        Player player = event.getPlayer();

        BlockFace facing = player.getFacing().getOppositeFace();

        Location loc = event.getClickedBlock().getLocation();
        loc.add(0,1,0);
        Block newBlock = loc.getBlock();


        if(!newBlock.getType().equals(Material.AIR)) {
            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas mettre un tapis roulant ici !");
            event.setCancelled(true);
            return;
        }

        newBlock.setType(m.getMaterial());
        BlockState state = newBlock.getState();
        TrapDoor trapDoor = (TrapDoor) state.getBlockData();


        //if clicking on conveyor, set vertical
        if(ConveyorType.getAllConveyorMaterials().contains(event.getClickedBlock().getType())) {
            trapDoor.setOpen(true);

            if(player.isSneaking()) {
                trapDoor.setHalf(Bisected.Half.TOP);
            }
            facing = facing.getOppositeFace();

        } else {
            trapDoor.setHalf(Bisected.Half.TOP);
            if (player.isSneaking()) {
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

        Block matBehind = loc.clone().add(0,1,0).subtract(facing.getDirection()).getBlock();
        Block matFront = loc.clone().add(0,1,0).add(facing.getDirection()).getBlock();
        if(matBehind.getType().equals(Material.DROPPER)) {
            for(UUID uuid : planet.getAllMachineries().keySet()) {
                if(planet.getAllMachineries().get(uuid) instanceof Output o) {
                    if (matBehind.getBoundingBox().contains(Bukkit.getEntity(uuid).getBoundingBox())) {
                        output = o;
                    }
                }
            }
        }
        if(matFront.getType().equals(Material.DISPENSER)) {
            for(UUID uuid : planet.getAllMachineries().keySet()) {
                if(planet.getAllMachineries().get(uuid) instanceof Input i) {
                    if (matFront.getBoundingBox().contains(Bukkit.getEntity(uuid).getBoundingBox())) {
                        input = i;
                    }
                }
            }
        }

        Conveyor c = new Conveyor(m, new Vector3i(newBlock.getX(), newBlock.getY(), newBlock.getZ()), input, output);
        Set<Conveyor> convs = allConveyorsPerPlanet.get(planet);
        if(convs == null) {
            convs = new HashSet<>();
        }
        convs.add(c);
        allConveyorsPerPlanet.put(planet, convs);

    }

}

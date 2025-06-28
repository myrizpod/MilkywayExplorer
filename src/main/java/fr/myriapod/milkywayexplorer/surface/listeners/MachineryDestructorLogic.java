package fr.myriapod.milkywayexplorer.surface.listeners;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.spaceexplorer.Univers;
import fr.myriapod.milkywayexplorer.surface.SurfacePlanet;
import fr.myriapod.milkywayexplorer.surface.machinery.Machinery;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.ConveyorType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MachineryDestructorLogic {
    public void playerInteractWithDestructorEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if(clickedBlock == null) {
            return;
        }

        for(ConveyorType ct : ConveyorType.values()) {
            if(ct.getMaterial().equals(clickedBlock.getType())) {
                new ConveyorManager().removeConveyor(Game.getPlayerPlanet(player).getSurfacePlanet(), clickedBlock);
                clickedBlock.setType(Material.AIR);
                player.getInventory().addItem(ct.getAsItem());
            }
        }

        event.setCancelled(true);
    }

    public void playerInteractWithDestructorEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity clickedEntity = event.getRightClicked();
        SurfacePlanet planet = Game.getPlayerPlanet(player).getSurfacePlanet();

        Machinery m = planet.getMachinery(clickedEntity.getUniqueId());

        if(m != null) {
            //m.destroy();
            player.getInventory().addItem(m.getType().getAsItem());
        }

        event.setCancelled(true);
    }


}

package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.mytools.PasteSchem;
import fr.myriapod.milkywayexplorer.techtree.TechtreeInventories;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class SurfaceListener implements Listener {

    @EventHandler
    public void interactWithEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        Entity interaction = event.getRightClicked();
        Set<String> tags = interaction.getScoreboardTags();

        if(interaction.getScoreboardTags().isEmpty()) return;

        String tag = (String) tags.toArray()[0];
        switch (tag) {
            case "techtree":
                player.openInventory(TechtreeInventories.getDefaultInventory());
                break;

            case "ship":
                Planet planet = Game.getPlayerPlanet(player);

                Bukkit.getLogger().info(String.valueOf(planet));

                if(planet == null) return;

                planet.teleportPlayerToSpace(player);

            }



    }





    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item == null) return;

        //TODO check for material and model data of item and either print schem or I dont know...

        if(SurfaceObject.BASIC_FURNACE.isItemEqual(item)) {

        } else if (SurfaceObject.BASIC_DRILL.isItemEqual(item)) {
            SurfaceObject so = SurfaceObject.BASIC_DRILL;

            new PasteSchem().generate(player.getTargetBlockExact(20).getLocation(), so.getModel());

        }


    }







    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(! player.getWorld().equals(Game.getUniversWorld())) {
            Planet planet = Game.getPlayerPlanet(player);
            Location loc = player.getLocation();

            int area = planet.getSurfacePlanet().getSide();

            if(loc.getX() > area) {
                loc.setX(-loc.getX()+5);
                player.teleport(loc);

            } else if (loc.getX() < -area) {
                loc.setX(-(loc.getX()+5));
                player.teleport(loc);

            } else if (loc.getZ() > area) {
                loc.setZ(-loc.getZ()+5);
                player.teleport(loc);

            } else if (loc.getZ() < -area) {
                loc.setZ(-(loc.getZ()+5));
                player.teleport(loc);

            }
        }


    }


}

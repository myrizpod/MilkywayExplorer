package fr.myriapod.milkywayexplorer.surface.listeners;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.Planet;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class LoopOnPlanet {

    public void loopOnPlanet(PlayerMoveEvent event) {
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

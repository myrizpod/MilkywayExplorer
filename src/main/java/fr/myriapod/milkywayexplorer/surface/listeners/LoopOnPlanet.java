package fr.myriapod.milkywayexplorer.surface.listeners;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.Planet;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class LoopOnPlanet {

    /* TODO FIX
        Could not pass event PlayerMoveEvent to Planet v1.0-SNAPSHOT
        org.bukkit.event.EventException: null
        Caused by: java.lang.NullPointerException: Cannot invoke "fr.myriapod.milkywayexplorer.Planet.getSurfacePlanet()" because "planet" is null
            at fr.myriapod.milkywayexplorer.surface.listeners.LoopOnPlanet.loopOnPlanet(LoopOnPlanet.java:18) ~[?:?]
     */
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

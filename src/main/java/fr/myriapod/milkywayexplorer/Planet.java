package fr.myriapod.milkywayexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.SpacePlanet;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import fr.myriapod.milkywayexplorer.surface.SurfacePlanet;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.joml.Vector3d;

public class Planet {

    private final SpacePlanet spacePlanet;
    private final SurfacePlanet surfacePlanet;

    public Planet(Vector3d pos, double radius, Vector3d starPos, int seed) {

        SpacePlanet sp = new SpacePlanet(pos, radius, starPos, seed);
        SurfacePlanet surP = new SurfacePlanet((int) radius, seed);

        spacePlanet = sp;
        surfacePlanet = surP;

    }


    public SpacePlanet getSpacePlanet() {
        return spacePlanet;
    }

    public SurfacePlanet getSurfacePlanet() {
        return surfacePlanet;
    }


    public void teleportPlayerToSurface(Player player) {
        player.teleport(new Location(surfacePlanet.getWorld(), 0 ,surfacePlanet.getWorld().getHighestBlockYAt(0, 0) + 1.5, 0));
    }

    public void teleportPlayerToSpace(Player player) {
        player.teleport(new Location(Game.getUniversWorld(), 0 ,100, 0));
        Ship ship = new Ship(player);
        Game.shipEnters(0, ship);
    }


}

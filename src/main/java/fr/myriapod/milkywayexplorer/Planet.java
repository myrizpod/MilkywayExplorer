package fr.myriapod.milkywayexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.SpacePlanet;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import fr.myriapod.milkywayexplorer.surface.SurfacePlanet;
import fr.myriapod.milkywayexplorer.surface.machinery.Machinery;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.joml.Vector3d;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Planet {

    private final SpacePlanet spacePlanet;
    private SurfacePlanet surfacePlanet = null;

    public Planet(Vector3d pos, double radius, Vector3d starPos, int seed,boolean isStar) {

        spacePlanet = new SpacePlanet(pos, radius, starPos, seed,isStar);
        if (!isStar) {
            surfacePlanet = new SurfacePlanet((int) Math.sqrt(radius), seed);
        }

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
        player.teleport(new Location(Game.getUniversWorld(), 0.5 ,100, 0.5));
        Ship ship = new Ship(player);
        Vector3d exitpos = spacePlanet.getPos().add(new Vector3d(0, spacePlanet.getRadius(),0));
        ship.setShipPos(exitpos);
        Game.shipEnters(0, ship);
//        player.getScoreboardTags().add("onShip");
    }


    public void addMachinery(UUID uuid, Machinery machinery) {
        surfacePlanet.addMachinery(uuid, machinery);

    }

    public Machinery getMachinery(UUID uuid) {
        return surfacePlanet.getMachinery(uuid);
    }

    public Map<UUID, Machinery> getAllMachines() {
        return surfacePlanet.getAllMachineries();
    }
}

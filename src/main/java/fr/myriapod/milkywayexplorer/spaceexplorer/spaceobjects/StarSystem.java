package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import fr.myriapod.milkywayexplorer.surface.SurfacePlanet;
import org.bukkit.Bukkit;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarSystem {

    private final Planet star;
    private final List<Planet> allPlanets = new ArrayList<>();


    public StarSystem(Vector3d center, int seed) {
        int planetCount = 1;
        Random generator = new Random(seed);

        for(int i = 0; i < planetCount; i++) {
            Planet planet = new Planet(
                    new Vector3d(generator.nextInt(400,1500), 0, 0),
                    generator.nextInt(25,100),
                    center,
                    generator.nextInt()
                    );

            allPlanets.add(planet);
        }
        star = new Planet(
                new Vector3d(0, 0, 0),
                generator.nextInt(100,200),
                center,
                generator.nextInt()
        );

    }

    public void shipEnters(Ship ship) {
        for(Planet p : allPlanets) {
            SpacePlanet sp = p.getSpacePlanet();
            sp.setShip(ship);
        }
        SpacePlanet s = star.getSpacePlanet();
        s.setShip(ship);

        ship.movementLoop();
    }

    public Planet getPlanet(int id) {
        return allPlanets.get(id);
    }


    public void loadSystem() {
        for(Planet p : allPlanets) {
            SpacePlanet sp = p.getSpacePlanet();
            sp.create();
            sp.updatePlanet();

            SurfacePlanet spl = p.getSurfacePlanet();
            spl.generate();
        }
        SpacePlanet s = star.getSpacePlanet();
        s.create();
        s.updatePlanet();
    }

    public List<Planet> getAllPlanets() {
        return allPlanets;
    }

    public Planet getStar() {
        return star;
    }
}


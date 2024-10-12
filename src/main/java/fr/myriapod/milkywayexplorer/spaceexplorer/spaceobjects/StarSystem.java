package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarSystem {


    private final List<Planet> allPlanets = new ArrayList<>();


    public StarSystem(Vector3d center, Random seed) {

        for(int i = 0; i < seed.nextInt(3,7); i++) {
            Planet planet = new Planet(
                    new Vector3d(1000, 0, 0),
                    400,
                    center,
                    seed.nextInt()
                    );

            allPlanets.add(planet);
        }

    }

    public void shipEnters(Ship ship) {
        for(Planet p : allPlanets) {
            SpacePlanet sp = p.getSpacePlanet();
            sp.create();
            sp.updatePlanet();
            sp.setShip(ship);
        }
    }

    public Planet getPlanet(int id) {
        return allPlanets.get(0);
    }


}

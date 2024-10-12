package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarSystem {

    private final double MIN_ROTATION_SPEED = 0.01;
    private final double MAX_ROTATION_SPEED = 0.05;
    private final double MIN_RESOLVE_SPEED = 0.05;
    private final double MAX_RESOLVE_SPEED = 0.1;

    private List<SpacePlanet> allPlanets = new ArrayList<>();


    public StarSystem(Vector3d center, Random seed) {

        for(int i = 0; i < seed.nextInt(3,5); i++) {
            SpacePlanet spacePlanet = new SpacePlanet(
                    new Vector3d(1000, 0, 0),
                    400,
                    200,
                    seed.nextInt(),
                    center,
                    seed.nextDouble(MIN_ROTATION_SPEED, MAX_ROTATION_SPEED),
                    seed.nextDouble(MIN_RESOLVE_SPEED, MAX_RESOLVE_SPEED)
                    );

            allPlanets.add(spacePlanet);
        }

    }

    public void shipEnters(Ship ship) {
        for(SpacePlanet sp : allPlanets) {
            sp.create();
            sp.updatePlanet();
            sp.setShip(ship);
        }
    }


}

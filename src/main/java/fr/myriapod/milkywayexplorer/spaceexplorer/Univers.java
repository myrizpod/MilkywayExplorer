package fr.myriapod.milkywayexplorer.spaceexplorer;

import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.StarSystem;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Univers {


    List<StarSystem> allLoadedSystems = new ArrayList<>();


    public Univers() {
        StarSystem s = new StarSystem(new Vector3d(0 , 0, 0), new Random());

        allLoadedSystems.add(s);
    }


    public void shipEnters(Ship ship) {
        for(StarSystem s : allLoadedSystems) {
            s.shipEnters(ship);
        }
    }
}

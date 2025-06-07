package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import fr.myriapod.milkywayexplorer.Planet;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import fr.myriapod.milkywayexplorer.surface.SurfacePlanet;
import fr.myriapod.milkywayexplorer.tools.SaveFile;
import org.joml.Vector3d;

import java.util.*;

public class StarSystem {

    //TODO should make it so those values actually depend on other valuest like:
    //-planet sizes and amount should depend on star size;
    //-distance between planets should depend on planet sizes (and maybe distance with the star)
    public static final long MAX_SCALE = (long) (3*Math.pow(10,16)); //all values are in m

    public static final long MIN_PLANET_SIZE = (long) (4.88*Math.pow(10,7)); //mercury's size
    public static final long MAX_PLANET_SIZE = (long) (1.4*Math.pow(10,9)); //jupyter's size

    public static final long MIN_STAR_SIZE = (long) (2.2*Math.pow(10,8)); //quite arbitrary, prolly extremely small
    public static final long MAX_STAR_SIZE = (long) (1.2 * Math.pow(10,10)); //prolly too big

    public static final long MIN_PLANET_DISTANCE = (long) (1.4*Math.pow(10,11)); //dist with mercury
    public static final long MAX_PLANET_DISTANCE = (long) Math.pow(10,12);

    private final Planet star;
    private final List<Planet> allPlanets = new ArrayList<>();
    private Vector3d center;


    public StarSystem(Vector3d center, int seed) {
        //int planetCount = 5;
        int planetCount = 1;
        Random generator = new Random(seed);
        this.center = center;

        long starRadius = generator.nextLong(MIN_STAR_SIZE,MAX_STAR_SIZE);

        star = new Planet(
                new Vector3d(0, 0, 0),
                starRadius,
                center,
                generator.nextInt(),
                true
        );

        long currentPlanetDistance = starRadius;

        for(int i = 0; i < planetCount; i++) {
            long currentPlanetSize = generator.nextLong(MIN_PLANET_SIZE,MAX_PLANET_SIZE);
            currentPlanetDistance += generator.nextLong(MIN_PLANET_DISTANCE,MAX_PLANET_DISTANCE) + currentPlanetSize;
            Planet planet = new Planet(
                    new Vector3d(currentPlanetDistance, 0, 0),
                    currentPlanetSize,
                    center,
                    generator.nextInt(),
                    false
            );

            allPlanets.add(planet);
        }


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

    public void preloadSystem() {
        for(Planet p : allPlanets) {
            SpacePlanet sp = p.getSpacePlanet();
            sp.create();
            sp.updatePlanet();

            SurfacePlanet spl = p.getSurfacePlanet();
            spl.generate();
            spl.loadMachineryChunks(new SaveFile().getAllMachineries(center, allPlanets.indexOf(p)));
        }

        SpacePlanet s = star.getSpacePlanet();
        s.create();
        s.updatePlanet();
    }

    public void loadSystem() {
        for(Planet p : allPlanets) {
            SurfacePlanet spl = p.getSurfacePlanet();
            SaveFile saveFile = new SaveFile();
            if (saveFile.doPlanetHasMachinery(center, allPlanets.indexOf(p))) {
                spl.setAllMachineries(saveFile.getAllMachineries(center, allPlanets.indexOf(p)));
            }
        }


    }

    public List<Planet> getAllPlanets() {
        return allPlanets;
    }

    public Planet getStar() {
        return star;
    }

    public Vector3d getCenter() {
        return center;
    }

}


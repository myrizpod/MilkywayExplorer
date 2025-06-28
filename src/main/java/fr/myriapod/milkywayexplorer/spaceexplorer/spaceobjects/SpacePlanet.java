package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.pipeline.JNoise;
import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.tools.Gradients;
import fr.myriapod.milkywayexplorer.tools.Maths;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import org.bukkit.Bukkit;
import org.joml.Vector3d;
import org.joml.Vector3i;
import org.joml.Vector4d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpacePlanet {


    private final double MIN_ROTATION_SPEED = 0.01;
    private final double MAX_ROTATION_SPEED = 0.05;
    private final double MIN_RESOLVE_SPEED = -0.01;
    private final double MAX_RESOLVE_SPEED = 0.01;
    private final double RAPPORT_NB_POINT_SURFACE = 0.0032;


    public final int MAX_PLANET_POINTS = 2000;


    private ArrayList<SpacePixel> pixelComponents;
    private Vector3d pos;
    private int pixelAmount;
    private final double radius;
    private final int seed;
    private Vector3d starPos;
    private final Random generator;
    private double rotSpeed;
    private double revolveSpeed;
    private Ship ship;
    private Vector3d renderPos;
    private double renderScale;
    private boolean isStar;
    private ArrayList<Vector3i> colorlist;


    public SpacePlanet(Vector3d pos, int pixelAmount, double radius, int seed, Vector3d starPos, double rotSpeed, double revolveSpeed) {
        this.pos = pos;
        this.pixelAmount = pixelAmount;
        this.radius = radius;
        this.seed = seed; //seed defines color and planet pattern
        this.starPos = starPos;
        this.generator = new Random(seed);
        this.revolveSpeed = revolveSpeed; //rotation on itself
        this.rotSpeed = rotSpeed; //rotation around star
    }


    public SpacePlanet(Vector3d pos, double radius, Vector3d starPos, int seed,boolean isStar) {
        this.pos = pos;
        //this.pixelAmount = (int) (Maths.getSphereArea(radius) * RAPPORT_NB_POINT_SURFACE);
        this.pixelAmount = 500;
        this.radius = radius;
        this.seed = seed; //seed defines color and planet pattern
        this.starPos = starPos;
        this.generator = new Random(seed);
        this.revolveSpeed = generator.nextDouble(MIN_RESOLVE_SPEED, MAX_RESOLVE_SPEED); //rotation on itself
        this.rotSpeed = generator.nextDouble(MIN_ROTATION_SPEED, MAX_ROTATION_SPEED); //rotation around star
        this.isStar = isStar;
    }


    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Vector3d getPos(){
        return pos;
    }

    public double getRadius(){
        return radius;
    }


    public void create() {

        //The list of all pixels
        pixelComponents = new ArrayList<>();

        //the position used to manage pixels as they should only know their render pos
        renderPos = new Vector3d(pos.x/Ship.MAX_VIEW_DISTANCE*Ship.SKYBOX_SIZE,pos.y/Ship.MAX_VIEW_DISTANCE*Ship.SKYBOX_SIZE,pos.z/Ship.MAX_VIEW_DISTANCE*Ship.SKYBOX_SIZE);


        colorlist = new ArrayList<>();
        int colorAmount = (Math.abs(seed) % 2) + 2; //choose the color amount: randint(2,4)
        if (isStar) {
            colorlist.add(new Vector3i(255, 0, 0));
            colorlist.add(new Vector3i(255, 255, 0));
        }
        else {
            for (int eachCol = 0; eachCol < colorAmount; eachCol++) {
                colorlist.add(new Vector3i(Math.abs(generator.nextInt()) % 255, Math.abs(generator.nextInt()) % 255, Math.abs(generator.nextInt()) % 255));
            }
        }

        setPointAmount(500);
        calculatePointColorsAndPos();


    }

    public void setPointAmount(int points) {
        pixelAmount = points;
        if (pixelComponents.size()>pixelAmount) {
            while (pixelComponents.size()!=pixelAmount) {
                pixelComponents.getFirst().delete();
                pixelComponents.removeFirst();
            }
        }
        else if (pixelComponents.size()<pixelAmount) {
            while (pixelComponents.size()!=pixelAmount) {
                pixelComponents.add(new SpacePixel(renderPos,new Vector3d(0,0,0), "#000000",1, 0));
            }
        }
    }


    private void calculatePointColorsAndPos() {

        JNoise noisePipeline = JNoise.newBuilder().perlin(seed, Interpolation.COSINE, FadeFunction.QUINTIC_POLY).addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).build(); // ^ this is funny noise pattern using complicated library


        //All pixel positions
        List<Vector4d> points = Maths.fibonacciSphere(pixelAmount); //4th dimension is angle in radians

        int i = 0;
        for (Vector4d eachPoint : points) {
            double bwMap = noisePipeline.evaluateNoise(eachPoint.x + 1,eachPoint.y + 1,eachPoint.z + 1);
            double angle = eachPoint.w;

            String color = Gradients.getGradientColor(colorlist,bwMap);

            SpacePixel currentPixel =pixelComponents.get(i);
            currentPixel.setRenderPos(new Vector3d(eachPoint.x + renderPos.x, eachPoint.y + renderPos.y, eachPoint.z + renderPos.z));
            currentPixel.setSpherePos(new Vector3d(eachPoint.x, eachPoint.y, eachPoint.z));
            currentPixel.setColor(color);
            currentPixel.setSize(0.2);
            currentPixel.setAngle(angle);

            i++;
        }

    }




    public double getShipDistance(){
        Vector3d shipPos = ship.getPos();
        return Math.sqrt(Math.pow(pos.x-shipPos.x,2)+Math.pow(pos.y-shipPos.y,2)+Math.pow(pos.z-shipPos.z,2))-radius;
    }

    public void updatePlanet() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            private double currentAngle = 0;
            public void run() {
                rotate(rotSpeed);
                rotateOnItself(revolveSpeed);
                //recalculates the render position of the planet
                if (ship!=null) {
                    // WTF apparement changer la renderpos change la pos
                    // OK BAH LE SHIP BOUGE SEUL NAN SANS BLAGUE YA UN GOLEM QUI SAIT PAS DEV
                    renderPos = new Vector3d(Ship.SKYBOX_SIZE * (pos.x - ship.getPos().x) / Ship.MAX_VIEW_DISTANCE, Ship.SKYBOX_SIZE * (pos.y - ship.getPos().y) / Ship.MAX_VIEW_DISTANCE, Ship.SKYBOX_SIZE * (pos.z - ship.getPos().z) / Ship.MAX_VIEW_DISTANCE);
                    if(getShipDistance()!=0) {
                        setScale(radius * Ship.SKYBOX_SIZE / getShipDistance());
                        calculatePointColorsAndPos();
                    }

                    //offsetting renderpos to avoid clipping in ship
                    renderPos.add(new Vector3d(pos.x - ship.getPos().x,pos.y - ship.getPos().y,pos.z - ship.getPos().z).normalize((double) Ship.SKYBOX_SIZE /5));

                    if(getShipDistance()!=0) {
                        double distanceInRender = Math.sqrt(Math.pow(renderPos.x,2)+Math.pow(renderPos.y,2)+Math.pow(renderPos.z,2));
                        setPointAmount((int) Math.min(Math.max(1,Math.pow(10,0) * Ship.SKYBOX_SIZE / Math.pow(distanceInRender,2)),MAX_PLANET_POINTS));
                    }


                }

                updateAllPoints();


            }

            private void rotate(double angle){
                currentAngle += angle;
                double rad = Math.sqrt(Math.pow(pos.x - starPos.x,2)+Math.pow(pos.z - starPos.z,2));
                double x = rad * Math.cos(Math.PI * 2 * Math.toRadians(currentAngle));
                double z = rad * Math.sin(Math.PI * 2 * Math.toRadians(currentAngle));

                pos.x = x;
                pos.z = z;
            }
            private void rotateOnItself(double speed){
                for (SpacePixel eachPixel : pixelComponents) {
                    eachPixel.rotate(speed);
                }
            }

            private void setScale(double scale){
                renderScale = scale;
                for (SpacePixel eachPixel : pixelComponents) {
                    eachPixel.setDistanceWithCenter(scale);
                }
            }

            private void updateAllPoints() {
                if(ship == null) return;

                for (SpacePixel eachPixel : pixelComponents) {
                    eachPixel.renderToShip(ship, renderPos);
                }


            }

        }, 20, 1);
    }




}
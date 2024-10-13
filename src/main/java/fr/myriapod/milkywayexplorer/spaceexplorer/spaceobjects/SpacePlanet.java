package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.pipeline.JNoise;
import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.mytools.Gradients;
import fr.myriapod.milkywayexplorer.mytools.Maths;
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
    private final double MIN_RESOLVE_SPEED = 0.05;
    private final double MAX_RESOLVE_SPEED = 0.1;


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


    public SpacePlanet(Vector3d pos, double radius, Vector3d starPos, int seed) {
        this.pos = pos;
        this.pixelAmount = 400;
        this.radius = radius;
        this.seed = seed; //seed defines color and planet pattern
        this.starPos = starPos;
        this.generator = new Random(seed);
        this.revolveSpeed = generator.nextDouble(MIN_RESOLVE_SPEED, MAX_RESOLVE_SPEED); //rotation on itself
        this.rotSpeed = generator.nextDouble(MIN_ROTATION_SPEED, MAX_ROTATION_SPEED); //rotation around star
    }


    public void setShip(Ship ship) {
        this.ship = ship;
    }


    public void create() {

        JNoise noisePipeline = JNoise.newBuilder().perlin(seed, Interpolation.COSINE, FadeFunction.QUINTIC_POLY).addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).build(); // ^ this is funny noise pattern using complicated library TODO make some of its parameters depend on seed
        List<Vector4d> points = Maths.fibonacciSphere(pixelAmount, radius); //4th dimension is angle in radians
        pixelComponents = new ArrayList<>();

        ArrayList<Vector3i> colorlist = new ArrayList<>();
        int colorAmount = (Math.abs(seed) % 2) + 2; //choose the color amount: randint(2,4)
        for (int eachCol = 0; eachCol < colorAmount; eachCol++) {
            colorlist.add(new Vector3i(Math.abs(generator.nextInt()) % 255, Math.abs(generator.nextInt()) % 255, Math.abs(generator.nextInt()) % 255));
        }

        for (Vector4d eachPoint : points) {
            double bwMap = noisePipeline.evaluateNoise(eachPoint.x / radius + 1,eachPoint.y / radius + 1,eachPoint.z / radius + 1);
            double angle = eachPoint.w;

            String color = Gradients.getGradientColor(colorlist,bwMap);
            pixelComponents.add(new SpacePixel(new Vector3d(eachPoint.x + pos.x, eachPoint.y + pos.y, eachPoint.z + pos.z),new Vector3d(eachPoint.x + pos.x, eachPoint.y + pos.y, eachPoint.z + pos.z), color,3, angle));

        }

    }

    public void updatePlanet() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            private double currentAngle = 0;
            public void run() {
                rotateOnItself(revolveSpeed);
                rotate(rotSpeed);
                updateAllPoints();

            }

            private void rotate(double angle){
                currentAngle += angle;
                double rad = Math.sqrt(Math.pow(pos.x - starPos.x,2)+Math.pow(pos.z - starPos.z,2));
                double x = rad * Math.cos(Math.PI * 2 * Math.toRadians(currentAngle));
                double z = rad * Math.sin(Math.PI * 2 * Math.toRadians(currentAngle));

                for (SpacePixel eachPixel : pixelComponents){
                    Vector3d pixelPos = eachPixel.getPos();
                    Vector3d newPos = new Vector3d(x + pixelPos.x - pos.x , pixelPos.y, z + pixelPos.z - pos.z );
                    eachPixel.tpTo(newPos);
                }
                pos.x = x;
                pos.z = z;
            }
            private void rotateOnItself(double speed){
                for (SpacePixel eachPixel : pixelComponents) {
                    eachPixel.rotate(pos,speed);
                }

            }

            private void updateAllPoints() {
                if(ship == null) return;

                for (SpacePixel eachPixel : pixelComponents) {
                    eachPixel.renderToShip(ship);
                }


            }

        }, 20, 1);
    }




}


package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator;
import de.articdive.jnoise.pipeline.JNoise;
import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.mytools.Gradients;
import fr.myriapod.milkywayexplorer.mytools.Maths;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.joml.Vector3d;
import org.joml.Vector3i;
import org.joml.Vector4d;

import java.util.ArrayList;
import java.util.List;

public class SpacePlanet {
    Main plugin;

    private ArrayList<SpacePixel> pixelComponnents;
    private Vector3d pos;
    private int pixelAmount;
    private double radius;
    private int seed;
    private Vector3d starPos;

    public SpacePlanet(Vector3d pos, int pixelAmount, double radius, Main main, int seed, Vector3d starPos) {
        this.plugin = main;
        this.pos = pos;
        this.pixelAmount = pixelAmount;
        this.radius = radius;
        this.seed = seed;
        this.starPos = starPos;
    }

    public void create() {

        JNoise noisePipeline=JNoise.newBuilder().perlin(seed,Interpolation.COSINE, FadeFunction.QUINTIC_POLY).addModifier(v -> (v + 1) / 2.0).clamp(0.0, 1.0).build();
        List<Vector4d> points = Maths.fibonacciSphere(pixelAmount,radius);
        pixelComponnents = new ArrayList<>();

        for (Vector4d eachPoint : points) {
            double ccolor = noisePipeline.evaluateNoise(eachPoint.x / radius + 1,eachPoint.y / radius + 1,eachPoint.z / radius + 1);
            double angle = eachPoint.w;

            /*
            double rcolor = Math.abs(eachPoint.x / radius);
            double gcolor = Math.abs(eachPoint.y / radius);
            double bcolor = Math.abs(eachPoint.z / radius);

            String rscolor = String.format("%02x",(Long) Math.round(ccolor * rcolor * 255));
            String gscolor = String.format("%02x",(Long) Math.round(ccolor * gcolor * 255));
            String bscolor = String.format("%02x",(Long) Math.round(ccolor * bcolor * 255));
            String color = "#" + rscolor + gscolor + bscolor;
            */
            String color = Gradients.gradFromTwoColors(new Vector3i(255,0,0), new Vector3i(0,0,255), ccolor);
            pixelComponnents.add(new SpacePixel(new Vector3d(eachPoint.x + pos.x, eachPoint.y + pos.y, eachPoint.z + pos.z),new Vector3d(eachPoint.x + pos.x, eachPoint.y + pos.y, eachPoint.z + pos.z), color,3, angle));

        }

    }

    public void rotatePlanet() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            private double currentAngle = 0;
            public void run() {
                rotateOnItself(0.1);
                //rotate(0.1);

            }

            private void rotate(double angle){
                currentAngle += angle;
                double rad = Math.sqrt(Math.pow(pos.x - starPos.x,2)+Math.pow(pos.z - starPos.z,2));
                double x = rad * Math.cos(Math.PI * 2 * Math.toRadians(currentAngle));
                double z = rad * Math.sin(Math.PI * 2 * Math.toRadians(currentAngle));

                for (SpacePixel eachPixel : pixelComponnents){
                    Vector3d pixelPos = eachPixel.getPos();
                    Vector3d newPos = new Vector3d(x + pixelPos.x - pos.x , pixelPos.y, z + pixelPos.z - pos.z );
                    eachPixel.tpTo(newPos);
                }
                pos.x = x;
                pos.z = z;
            }
            private void rotateOnItself(double speed){
                for (SpacePixel eachPixel : pixelComponnents) {
                    eachPixel.rotate(pos,speed);

                }

            }

        }, 20, 1);
    }




}


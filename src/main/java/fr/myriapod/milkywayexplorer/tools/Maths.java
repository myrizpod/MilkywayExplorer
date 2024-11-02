package fr.myriapod.milkywayexplorer.tools;

import org.joml.Vector4d;

import java.util.ArrayList;
import java.util.List;

public class Maths {

    public static List<Vector4d> fibonacciSphere(int samples, double radius) {
        List<Vector4d> pointsPos = new ArrayList<>();

        double phi = Math.PI * (Math.sqrt(5.0) - 1.0); // golden angle in radians

        for (int i = 0; i < samples; i++) {
            double y = (double) i / (double) samples * 2 - 1;  // y goes from 1 to -1
            double thisRadius = Math.sqrt(1 - (y * y));  // radius at y

            double theta = phi * i;  // golden angle increment
            double x = Math.cos(theta) * thisRadius * radius;
            double z = Math.sin(theta) * thisRadius * radius;
            y *= radius;
            pointsPos.add(new Vector4d(x, y, z, theta)); //theta is angle


        }
        return pointsPos;
    }

    public static double getSphereArea(double radius){
        return 4 * Math.PI * radius * radius ;
    }

    public static double getSpherePointSize(int samples, double radius){
        return Math.sqrt((4 * Math.PI * radius * radius) / samples);
    }


}

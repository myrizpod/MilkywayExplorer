package fr.myriapod.milkywayexplorer.tools;

import org.joml.Matrix3d;
import org.joml.Vector3d;
import org.joml.Vector4d;

import java.util.ArrayList;
import java.util.List;

public class Maths {

    public static List<Vector4d> fibonacciSphere(int samples) {
        List<Vector4d> pointsPos = new ArrayList<>();

        double phi = Math.PI * (Math.sqrt(5.0) - 1.0); // golden angle in radians

        for (int i = 0; i < samples; i++) {
            double y = (double) i / (double) samples * 2 - 1;  // y goes from 1 to -1
            double thisRadius = Math.sqrt(1 - (y * y));  // radius at y

            double theta = phi * i;  // golden angle increment
            double x = Math.cos(theta) * thisRadius;
            double z = Math.sin(theta) * thisRadius;
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

//    public Vector3d angleToVector(Vector3d angle) {
//        Vector3d vec = new Vector3d(0, -1, 0);
//        Matrix3d m = new Matrix3d(1,0,0,0,1,0,0,0,1); //IDENTITY
//
//        m.mul(getRotationMatrixX(angle.x));
//        m.mul(getRotationMatrixY(Math.toRadians(angle.y))); //
//        m.mul(getRotationMatrixZ(angle.z));
//        return m.mul(vec);
//    }
//
//    private Matrix3d getRotationMatrixX(double pitch) {
//        return new Matrix3d(Math.cos(pitch),0,Math.sin(pitch),0,1,0,-Math.sin(pitch),0,Math.cos(pitch));
//    }
//
//    private Matrix3d getRotationMatrixY(double yaw) {
//        return new Matrix3d(Math.cos(yaw), );
//    }
//
//    private Matrix3d getRotationMatrixZ(double roll) {
//        return new Matrix3d(Math.cos(pitch),0,Math.sin(pitch),0,1,0,-Math.sin(pitch),0,Math.cos(pitch));
//    }


}

package fr.myriapod.milkywayexplorer.mytools;

import org.joml.Vector3d;
import org.joml.Vector3i;

import java.util.ArrayList;

public class Gradients {

    public static String getGradientColor(ArrayList<Vector3i> colorList, double pos){
        double sectionSize = 1.0 / colorList.size();
        String hex = "";



        return hex;
    }

    public static String gradFromTwoColors(Vector3i color1, Vector3i color2, double pos) {
        double pos2 = 1 - pos;

        int r = (int) (color1.x * pos + color2.x * pos2);
        int g = (int) (color1.y * pos + color2.y * pos2);
        int b = (int) (color1.z * pos + color2.z * pos2);
        Vector3i color = new Vector3i(r,g,b);
        String hex = "#" + String.format("%02x",r) + String.format("%02x",g) + String.format("%02x",b);
        return hex;
    }
}

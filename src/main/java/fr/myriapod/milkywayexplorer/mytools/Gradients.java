package fr.myriapod.milkywayexplorer.mytools;

import org.joml.Vector3i;

import java.util.List;

public class Gradients {

    public static String getGradientColor(List<Vector3i> colorList, double pos){
        double precisePos = (colorList.size() - 1) * pos;
        precisePos = Math.clamp(precisePos,0,colorList.size() - 1.001);
        int firstPos = (int) Math.floor(precisePos);
        double miniPos = precisePos - firstPos;

        return gradFromTwoColors(colorList.get(firstPos+1) , colorList.get(firstPos) , miniPos);
    }

    public static String gradFromTwoColors(Vector3i color1, Vector3i color2, double pos) {
        double pos2 = 1 - pos;

        int r = (int) (color1.x * pos + color2.x * pos2);
        int g = (int) (color1.y * pos + color2.y * pos2);
        int b = (int) (color1.z * pos + color2.z * pos2);

        return "#" + String.format("%02x",r) + String.format("%02x",g) + String.format("%02x",b);
    }
}

package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3d;

public class SpacePixel {

    private TextDisplay pixel;
    private String color;
    private Vector3d pos;
    private double size;
    private Vector3d trigSpherePos;
    private double currentAngle;

    public SpacePixel(Vector3d pos,Vector3d trigSpherePos, String color, double size,double angle){
        this.pos = pos;
        this.size = size;
        this.color = color;
        this.trigSpherePos = trigSpherePos;
        this.currentAngle = Math.toDegrees(angle);

        pixel = Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), pos.x, pos.y, pos.z), TextDisplay.class);
        pixel.setText(net.md_5.bungee.api.ChatColor.of(color) + "\u2b24");
        pixel.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        Transformation transformation = pixel.getTransformation();
        pixel.setBillboard(Display.Billboard.CENTER);
        transformation.getScale().set(3);
        pixel.setTransformation(transformation);
        pixel.setTeleportDuration(5);
    }

    public void move(Vector3d shift){
        pos.x += shift.x;
        pos.y += shift.y;
        pos.z += shift.z;
        pixel.teleport(new Location(Bukkit.getWorld("world"), pos.x, pos.y, pos.z));
    }

    public void tpTo(Vector3d newPos) {
        pos.x = newPos.x;
        pos.y = newPos.y;
        pos.z = newPos.z;
        pixel.teleport(new Location(Bukkit.getWorld("world"), pos.x, pos.y, pos.z));
    }
    public Vector3d getPos() {

        return pos;
    }

    public Vector3d getTrigSpherePos() {

        return trigSpherePos;
    }

    public void rotate(Vector3d center,double angle) {
        currentAngle += angle;
        double rad = Math.sqrt(Math.pow(pos.x - center.x,2)+Math.pow(pos.z - center.z,2));
        double x = rad * Math.cos(Math.PI * 2 * Math.toRadians(currentAngle));
        double z = rad * Math.sin(Math.PI * 2 * Math.toRadians(currentAngle));
        this.tpTo(new Vector3d(x + center.x ,pos.y,z + center.z));

    }
}

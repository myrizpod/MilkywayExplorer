package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
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
    private Vector3d renderPos;

    public SpacePixel(Vector3d pos,Vector3d trigSpherePos, String color, double size,double angle){
        this.pos = pos;
        this.size = size;
        this.color = color; //hex color code
        this.trigSpherePos = trigSpherePos; //position on the trig sphere: [-1;1] kinda usleless
        this.currentAngle = angle; // angle of the pixel around the vertical axis of the planet in degrees


        pixel = Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), pos.x, pos.y, pos.z), TextDisplay.class);
        pixel.setText(net.md_5.bungee.api.ChatColor.of(color) + "⬤");
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
    }

    public void tpTo(Vector3d newPos) {
        pos.x = newPos.x;
        pos.y = newPos.y;
        pos.z = newPos.z;
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
        double x = Math.cos(currentAngle) * rad;
        double z = Math.sin(currentAngle) * rad;
        this.tpTo(new Vector3d(x + center.x ,pos.y,z + center.z));

    }

    public void renderToShip(Ship ship) {
        //pixel.teleport(new Location(Bukkit.getWorld("world"), pos.x, pos.y, pos.z)); //old way to render
        renderPos = new Vector3d((pos.x - ship.getPos().x) / ship.getSpaceScale() + ship.getWorldCenter().x , (pos.y - ship.getPos().y) / ship.getSpaceScale() + ship.getWorldCenter().y , (pos.z - ship.getPos().z) / ship.getSpaceScale() + ship.getWorldCenter().z);
        //pos.sub(ship.getPos()).add(ship.getWorldCenter()).div(ship.getSpaceScale())
        pixel.teleport(new Location(Bukkit.getWorld("world"), renderPos.x , renderPos.y , renderPos.z));
    }
}

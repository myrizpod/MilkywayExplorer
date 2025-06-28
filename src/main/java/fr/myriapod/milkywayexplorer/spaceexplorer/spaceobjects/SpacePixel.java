package fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceship.Ship;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3d;

public class SpacePixel {

    private final Color BACKGROUND_COLOR = Color.fromARGB(0, 0, 0, 0);
    private final int TP_DURATION = 5;

    private double scale;
    private TextDisplay pixel;
    private String color;
    private Vector3d spherePos;
    private Vector3d relativePos;
    private double currentAngle;
    private Vector3d renderPos;

    public SpacePixel(Vector3d pos,Vector3d spherePos, String color, double size,double angle){
        this.renderPos = pos;
        this.scale = size;
        this.scale = 3;
        this.color = color; //hex color code
        this.spherePos = spherePos; //position on the trig sphere: [-1;1] kinda usleless
        relativePos = spherePos;
        this.currentAngle = angle; // angle of the pixel around the vertical axis of the planet in degrees


        pixel = Game.getUniversWorld().spawn(new Location(Game.getUniversWorld(), 0, 0, 0), TextDisplay.class);
        setColor(color);
        pixel.setBackgroundColor(BACKGROUND_COLOR);
        Transformation transformation = pixel.getTransformation();
        pixel.setBillboard(Display.Billboard.CENTER);
        transformation.getScale().set(scale);
        pixel.setTransformation(transformation);
        pixel.setTeleportDuration(TP_DURATION);
    }

    public void tpTo(Vector3d newPos) {
        renderPos = newPos;
    }

    public void setRelativePos(Vector3d newPos) { relativePos = newPos; }

    public void setSpherePos(Vector3d newPos) { spherePos = newPos; }

    public void rotate(double angle) {
        currentAngle += angle;
        double rad = Math.sqrt(Math.pow(spherePos.x,2)+Math.pow(spherePos.z,2));
        double x = Math.cos(currentAngle) * rad;
        double z = Math.sin(currentAngle) * rad;
        setSpherePos(new Vector3d(x,spherePos.y,z));

    }

    public void setDistanceWithCenter(double dist){
        setRelativePos(new Vector3d(spherePos.x * dist , spherePos.y * dist , spherePos.z * dist ));
    }

    public void renderToShip(Ship ship, Vector3d planetpos) {

        this.tpTo(new Vector3d(planetpos).add(relativePos));

        pixel.teleport(new Location(Game.getUniversWorld(), renderPos.x + ship.getWorldCenter().x, renderPos.y + ship.getWorldCenter().y, renderPos.z + ship.getWorldCenter().z));
    }

    public void setColor(String color) {
        this.color = color;
        pixel.setText(net.md_5.bungee.api.ChatColor.of(color) + "⬤");
    }

    public void setAngle(double currentAngle) {
        this.currentAngle = currentAngle;
    }

    public void setSize(double scale) {
        this.scale = scale;
    }

    public void setRenderPos(Vector3d renderPos) {
        this.renderPos = renderPos;
    }

    public void delete() {
        pixel.remove();
    }
}
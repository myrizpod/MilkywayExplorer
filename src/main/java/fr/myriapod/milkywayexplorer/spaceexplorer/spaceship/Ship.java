package fr.myriapod.milkywayexplorer.spaceexplorer.spaceship;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.tools.Skull;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3d;

public class Ship {

    private final double THRUST_POWER = 3*Math.pow(10,9);

    //private final Horse seat;
    private final Player player;
    private Vector3d shipPos; //pos of the ship in space
    private Vector3d shipRot; // rotation of the ship (pitch[-90;+90] yaw[-180;+180] roll[-180;+180])
    private Vector3d shipRotMomentum; //the rotation momentum of the ship
    private Vector3d shipMomentum; //a vector of the current speed of the ship. is added to pos every X time
    private final Vector3d SHIP_CENTER = new Vector3d(0.5,101,0.5); //actual center of the ship in the world NOT ITS POS IN SPACE
    public static int SKYBOX_SIZE = 50;
    public static double MAX_VIEW_DISTANCE = Math.pow(10,14);


    public Ship(Player player) {
        World world = player.getWorld();

        Bukkit.getLogger().info("Thrust power:"+ THRUST_POWER);

        Game.addShip(this);

        this.player = player;
        this.shipPos = new Vector3d(Math.pow(10,11),0,Math.pow(10,11));
        this.shipRot = new Vector3d(0,0,0);
        this.shipMomentum = new Vector3d(0,0,0);
        this.shipRotMomentum = new Vector3d(0,0,0);

        //create a skybox using a black #000000 head item display with reversed size
//        int skyBoxSize = player.getClientViewDistance()*16; CHECK RENDER DISTANCE *16 ?? //TODO make sure stuff like planets are rendered too based on this
        ItemDisplay skyBox = world.spawn(new Location(world, SHIP_CENTER.x, SHIP_CENTER.y, SHIP_CENTER.z + 2), ItemDisplay.class);
        Transformation boxTransformation = skyBox.getTransformation();
        boxTransformation.getScale().set(-SKYBOX_SIZE*4);
        boxTransformation.getTranslation().set(0, (float) -SKYBOX_SIZE, 0);
        skyBox.setTransformation(boxTransformation);


        skyBox.setItemStack(Skull.getSpaceSkull());

        ArmorStand seat = world.spawn(new Location(world, SHIP_CENTER.x, SHIP_CENTER.y-0.5, SHIP_CENTER.z), ArmorStand.class);
        seat.setGravity(false);
        seat.setInvulnerable(true);
        seat.addScoreboardTag("ship");
        seat.setInvisible(true);
        seat.setMarker(true);
        seat.addPassenger(player);

        TextDisplay controlCircle = world.spawn(new Location(world, SHIP_CENTER.x, SHIP_CENTER.y, SHIP_CENTER.z + 2), TextDisplay.class);
        controlCircle.setText(ChatColor.GREEN + "◯");
        controlCircle.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        Transformation transformation = controlCircle.getTransformation();
        controlCircle.setBillboard(Display.Billboard.CENTER); //prob wrong type of billboard
        transformation.getScale().set(3);
        controlCircle.setTransformation(transformation);
    }

    public Vector3d getPos() {
        return shipPos;
    }

    public void setMomentum(Vector3d momentum) {
        shipMomentum = momentum;
    }


    public Vector3d getWorldCenter(){
        return SHIP_CENTER;
    }

    public void moveShip(Vector3d movement, Vector3d rotMovement){
        shipPos.add(movement);
        shipRot.add(rotMovement);

    }

    public Player getPlayer() {
        return player;
    }

    public void rotFriction() { //make it so when u rotate the ship the rot slows down (not realistic but feels nice)
        shipMomentum = shipRotMomentum.div(1.5);
    }

    public void setShipPos(Vector3d pos){
        //shipPos = pos; //TODO bad
    }


    public void movementLoop() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            public void run() {
                moveShip(shipMomentum,shipRotMomentum);

            }
        }, 20, 1);
    }

    public void movementInput(float forward, float sideways) {

        Bukkit.getLogger().info("Forward: " + forward + "Sideway: " + sideways);

        if(forward>0.5) {
            shipMomentum.add(0, 0, THRUST_POWER);
        } else if (forward<-0.5) {
            shipMomentum.add(0, 0, -THRUST_POWER);
        }


        //TODO should not be able to strafe with ship, use Q and D for something else idk
        if (sideways>0.5) {
            shipMomentum.add(THRUST_POWER, 0, 0);
        } else if (sideways<-0.5) {
            shipMomentum.add(-THRUST_POWER, 0, 0);
        }

    }
}
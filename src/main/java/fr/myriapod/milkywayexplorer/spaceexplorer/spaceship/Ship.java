package fr.myriapod.milkywayexplorer.spaceexplorer.spaceship;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.mytools.Skull;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.util.Transformation;
import org.joml.Vector2d;
import org.joml.Vector3d;

public class Ship {
    private final Horse seat;
    private final Player player;
    private final TextDisplay controlCircle;
    private final ItemDisplay skyBox;
    private Vector3d shipPos; //pos of the ship in space
    private Vector2d shipRot; // rotation of the ship (pitch[-90;+90] yaw[-180;+180])
    private Vector2d shipRotMomentum; //the rotation momentum of the ship
    private Vector3d shipMomentum; //a vector of the current speed of the ship. is added to pos every X time
    private static final Vector3d shipCenter = new Vector3d(0.5,101,0.5); //actual center of the ship in the world NOT ITS POS IN SPACE
    private static final int spaceScale = 50; //This is the scale ratio between space and world: world pos is (objPos-shipPos)/spaceScale + shipCenter


    public Ship(Player player) {
        World world = player.getWorld();

        Game.addShip(this);

        this.player = player;
        this.shipPos = new Vector3d(0,0,0);
        this.shipRot = new Vector2d(0,0);
        this.shipMomentum = new Vector3d(0,0,0);
        this.shipRotMomentum = new Vector2d(0,0);

        //create a skybox using a black #000000 head item display with reversed size
        int skyBoxSize = 150; //TODO make skybox size depend on renderdistance
//        int skyBoxSize = player.getClientViewDistance()*16; CHECK RENDER DISTANCE *16 ?? //TODO make sure stuff like planets are rendered too based on this
        skyBox = world.spawn(new Location(world, shipCenter.x, shipCenter.y, shipCenter.z + 2), ItemDisplay.class);
        Transformation boxTransformation = skyBox.getTransformation();
        boxTransformation.getScale().set(-skyBoxSize);
        boxTransformation.getTranslation().set(0, (float) -skyBoxSize /4, 0);
        skyBox.setTransformation(boxTransformation);


        skyBox.setItemStack(Skull.getSpaceSkull());

        seat = world.spawn(new Location(world, shipCenter.x, shipCenter.y - 2, shipCenter.z), Horse.class);
        seat.setGravity(false);
        seat.setInvulnerable(true);
        seat.addScoreboardTag("ship");
        seat.setInvisible(true);
        seat.addPassenger(player); //TODO TEST make player unable to dismount horse as well as make it invisible

        controlCircle = world.spawn(new Location(world, shipCenter.x, shipCenter.y, shipCenter.z + 2), TextDisplay.class);
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

    public int getSpaceScale(){
        return spaceScale;
    }

    public Vector3d getWorldCenter(){
        return shipCenter;
    }

    public void moveShip(Vector3d movement, Vector2d rotMovement){
        shipPos.add(movement);
        shipRot.add(rotMovement);
    }

    public Player getPlayer() {
        return player;
    }


    public void movementLoop() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            public void run() {
                moveShip(shipMomentum,shipRotMomentum);

            }
        }, 20, 1);
    }
}


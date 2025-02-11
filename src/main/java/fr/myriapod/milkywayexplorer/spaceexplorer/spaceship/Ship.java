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
import org.bukkit.util.Transformation;
import org.joml.Vector3d;

public class Ship {

    private final double THRUST_POWER = 0.5;

    //private final Horse seat;
    private final Player player;
    private Vector3d shipPos; //pos of the ship in space
    private Vector3d shipRot; // rotation of the ship (pitch[-90;+90] yaw[-180;+180] roll[-180;+180])
    private Vector3d shipRotMomentum; //the rotation momentum of the ship
    private Vector3d shipMomentum; //a vector of the current speed of the ship. is added to pos every X time
    private final Vector3d SHIP_CENTER = new Vector3d(0.5,101,0.5); //actual center of the ship in the world NOT ITS POS IN SPACE
    private final int SPACE_SCALE = 50; //This is the scale ratio between space and world: world pos is (objPos-shipPos)/spaceScale + shipCenter


    public Ship(Player player) {
        World world = player.getWorld();

        Game.addShip(this);

        this.player = player;
        this.shipPos = new Vector3d(200,0,100);
        this.shipRot = new Vector3d(0,0,0);
        this.shipMomentum = new Vector3d(0,0,0);
        this.shipRotMomentum = new Vector3d(0,0,0);

        //create a skybox using a black #000000 head item display with reversed size
        int skyBoxSize = 150; //TODO make skybox size depend on renderdistance
//        int skyBoxSize = player.getClientViewDistance()*16; CHECK RENDER DISTANCE *16 ?? //TODO make sure stuff like planets are rendered too based on this
        ItemDisplay skyBox = world.spawn(new Location(world, SHIP_CENTER.x, SHIP_CENTER.y, SHIP_CENTER.z + 2), ItemDisplay.class);
        Transformation boxTransformation = skyBox.getTransformation();
        boxTransformation.getScale().set(-skyBoxSize);
        boxTransformation.getTranslation().set(0, (float) -skyBoxSize /4, 0);
        skyBox.setTransformation(boxTransformation);


        skyBox.setItemStack(Skull.getSpaceSkull());

//        seat = world.spawn(new Location(world, SHIP_CENTER.x, SHIP_CENTER.y - 2, SHIP_CENTER.z), Horse.class);
//        seat.setGravity(false);
//        seat.setInvulnerable(true);
//        seat.addScoreboardTag("ship");
//        seat.setInvisible(true);
        //seat.addPassenger(player);

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

    public int getSpaceScale(){
        return SPACE_SCALE;
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
        shipPos = pos;
    }


    public void movementLoop() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            public void run() {
                getMovement();
                moveShip(shipMomentum,shipRotMomentum);

            }

            Vector3d lastPos;

            private void getMovement() {
                if(player.getScoreboardTags().contains("onShip")) {
                    Location loc = player.getLocation();
                    Vector3d pos = new Vector3d(loc.getX(), loc.getY(), loc.getZ());

                    if(lastPos == null) lastPos = pos;

                    player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.001);
                    player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH).setBaseValue(0.1);
                    player.getAttribute(Attribute.PLAYER_SNEAKING_SPEED).setBaseValue(0.001);

                    if(pos.equals(lastPos)) return;

                    //player.sendMessage("Pos: " + pos.toString());
                    //player.sendMessage("LastPos: " + lastPos.toString());

                    double Xdiff = Math.abs(pos.x-lastPos.x);
                    double Zdiff = Math.abs(pos.z-lastPos.z);

                    if (Xdiff > Zdiff){
                        if(pos.x < lastPos.x) {
                            shipMomentum.add(-THRUST_POWER, 0, 0);
                        } else if (pos.x > lastPos.x) {
                            shipMomentum.add(THRUST_POWER, 0, 0);
                        }

                    } else {
                        if (pos.z < lastPos.z) {
                            shipMomentum.add(0, 0, -THRUST_POWER);
                        } else if (pos.z > lastPos.z) {
                            shipMomentum.add(0, 0, THRUST_POWER);
                        }
                    }




                    lastPos = pos;

                    player.sendMessage("Momentum: " + shipMomentum.toString());

                } else {
                    player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.10000000149011612f);
                    player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH).setBaseValue(0.42f);
                    player.getAttribute(Attribute.PLAYER_SNEAKING_SPEED).setBaseValue(0.3f);
                }

            }
        }, 20, 1);
    }
}
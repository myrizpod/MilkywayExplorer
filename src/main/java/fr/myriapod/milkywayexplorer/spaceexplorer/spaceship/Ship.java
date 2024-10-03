package fr.myriapod.milkywayexplorer.spaceexplorer.spaceship;

import fr.myriapod.milkywayexplorer.Game;
import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.spaceexplorer.spaceobjects.SpacePixel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Transformation;
import org.joml.Vector3d;
import org.joml.Vector3i;

public class Ship {
    private ArmorStand seat;
    private Player player;
    private TextDisplay controlCircle;
    private ItemDisplay skyBox;
    private static final Vector3d shipCenter = new Vector3d(0.5,101,0.5); //actual center of the ship in the world NOT ITS POS IN SPACE
    private Vector3d shipPos; //pos of the ship in space
    private Vector3d shipMomentum; //a vector of the current speed of the ship. is added to pos every X time
    private static final int spaceScale = 50; //This is the scale ratio between space and world: world pos is (objPos-shipPos)/spaceScale + shipCenter


    public Ship(Player player) {

        Game.addShip(this);

        this.player = player;
        this.shipPos = new Vector3d(0,0,0);
        this.shipMomentum = new Vector3d(0,0,0);

        //create a skybox using a black #000000 head item display with reversed size
        //TODO make it work cuz we dont get right head rn
        int skyBoxSize = 150; //TODO make skybox size depend on renderdistance
        skyBox = Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), 0.5, shipCenter.y, 2.5), ItemDisplay.class);
        Transformation boxTransformation = skyBox.getTransformation();
        boxTransformation.getScale().set(-150);
        skyBox.setTransformation(boxTransformation);
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer("Adamchelou")); //Adamchelou is a player with a mostly black btw its temporary tho
        skull.setItemMeta(skullMeta);
        skyBox.setItemStack(skull);

        seat = Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), 0.5, 98.5, 0.5), ArmorStand.class);
        seat.setGravity(false);
        seat.setInvulnerable(true);
        seat.setPassenger(player); //TODO make player unable to dismount armorstand as well as make it invisible

        controlCircle = Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), 0.5, 101, 2.5), TextDisplay.class);
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

    public void moveShip(Vector3d movement){
        shipPos.add(movement);
    }

    public Player getPlayer() {
        return player;
    }


    public void movementLoop() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            public void run() {
                moveShip(shipMomentum);

            }
        }, 20, 1);
    }
}


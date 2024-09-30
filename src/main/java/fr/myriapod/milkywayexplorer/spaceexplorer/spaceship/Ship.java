package fr.myriapod.milkywayexplorer.spaceexplorer.spaceship;

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

public class Ship {
    private ArmorStand seat;
    private TextDisplay controlCircle;
    private ItemDisplay skyBox;
    private static final Vector3d shipCenter = new Vector3d(0.5,101,0.5);

    public Ship(Player player) {

        //create a skybox using a black #000000 head item display with reversed size
        int skyBoxSize = 150;
        skyBox = Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), 0.5, shipCenter.y, 2.5), ItemDisplay.class);
        Transformation boxTransformation = skyBox.getTransformation();
        boxTransformation.getScale().set(-150);
        skyBox.setTransformation(boxTransformation);
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer("Adamchelou"));
        skull.setItemMeta(skullMeta);
        skyBox.setItemStack(skull);

        seat = Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), 0.5, 98.5, 0.5), ArmorStand.class);
        seat.setGravity(false);
        seat.setInvulnerable(true);
        seat.setPassenger(player);

        controlCircle = Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), 0.5, 101, 2.5), TextDisplay.class);
        controlCircle.setText(ChatColor.GREEN + "\u25ef");
        controlCircle.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        Transformation transformation = controlCircle.getTransformation();
        controlCircle.setBillboard(Display.Billboard.CENTER);
        transformation.getScale().set(3);
        controlCircle.setTransformation(transformation);


    }
}

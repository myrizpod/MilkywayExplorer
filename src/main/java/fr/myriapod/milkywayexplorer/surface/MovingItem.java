package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;

public class MovingItem {

    private final Ressource ressource;
    private Location position;
    private ItemDisplay itemDisplay;

    public MovingItem(Ressource ressource, Location position) {
        this.ressource = ressource;
        this.position = position;

        itemDisplay = position.getWorld().spawn(position, ItemDisplay.class);
        itemDisplay.setCustomName(ressource.getName());
        itemDisplay.setItemStack(ressource.getAsItem(1));
        itemDisplay.setGravity(false);
    }


    public Ressource getRessource() {
        return ressource;
    }

    public Location getPosition() {
        return position;
    }
}

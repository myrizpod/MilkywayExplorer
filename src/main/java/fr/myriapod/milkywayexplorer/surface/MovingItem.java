package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.surface.listeners.ConveyorManager;
import fr.myriapod.milkywayexplorer.surface.machinery.Conveyor;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.Collection;

public class MovingItem {

    private final Ressource ressource;
    private ItemDisplay itemDisplay;
    private int number;
    private boolean inMovement;

    public MovingItem(Ressource ressource, int nb, Location position) {
        this.ressource = ressource;
        this.number = nb;

        itemDisplay = position.getWorld().spawn(position, ItemDisplay.class);
        itemDisplay.setCustomName(ressource.getName());
        itemDisplay.setItemStack(ressource.getAsItem(1));
        itemDisplay.setGravity(false);
        Transformation t = itemDisplay.getTransformation();
        t.getScale().set(0.5);
        itemDisplay.setTransformation(t);
    }


    public Ressource getRessource() {
        return ressource;
    }

    public Location getPosition() {
        return itemDisplay.getLocation();
    }

    public int getNumber() {
        return number;
    }

    public void addVelocity(Vector direction, double rate) {
        if(! isThereItemDisplay(direction)) {
            inMovement = true;
            itemDisplay.setTeleportDuration((int) (20 / (rate * 10)));
            itemDisplay.teleport(itemDisplay.getLocation().add(direction));
        }

    }

    private boolean isThereItemDisplay(Vector direction) {
        Collection<ItemDisplay> allItemDisplay = itemDisplay.getWorld().getEntitiesByClass(ItemDisplay.class);
        for(ItemDisplay i : allItemDisplay) {
            if(i.getLocation().distance(itemDisplay.getLocation().add(direction)) < 0.1) {
                return true;
            }
        }
        return false;
    }

    public void changeConveyor() {
        Location itemLoc = itemDisplay.getLocation();

        //Upward/Downward conveyor case
        Block actualBlock = itemLoc.getBlock();
        Conveyor cActual = new ConveyorManager().getConveyor(actualBlock);
        if(cActual != null) {
            cActual.addItem(this);
            return;
        }

        //Horizontal conveyor case
        Block frontBlock = itemLoc.clone().subtract(0,1,0).getBlock();
        Conveyor cFront = new ConveyorManager().getConveyor(frontBlock);
        if(cFront != null) {
            cFront.addItem(this);
        }
    }

    public void stopMoving() {
        inMovement = false;
    }
}

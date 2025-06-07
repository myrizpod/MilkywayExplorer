package fr.myriapod.milkywayexplorer.surface.machinery;


import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.MachineryType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3i;

public abstract class Machinery {

    protected Vector3i pos;
    protected String id;
    protected MachineryType type;


    Machinery(MachineryType type, Vector3i pos) {
        this.type = type;
        this.pos = pos;
        this.id = type.getID();
    }

    public String getID() {
        return id;
    }

    public Vector3i getLocation() {
        return pos;
    }

    public MachineryType getType() {return type;}


    public static MachineryType getAsMachinery(ItemStack item) {
        if(item == null) {
            return null;
        }
        if(item.getType().equals(Material.AIR)) {
            return null;
        }
        for(MachineryType m : MachineryType.getAllTypes()) {
            if(m.isItemEqual(item)) {
                return m;
            }
        }
        return null;
    }


}

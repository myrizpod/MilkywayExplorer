package fr.myriapod.milkywayexplorer.surface.machinery;


import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@MachineryAnnotation
public abstract class Machinery {

    //PROBLEMMMM COMMENT FAIRE ITEM_EQUALS SI Y'A PAS D'INSTANCE...

    String name;
    Material material;
    Tech prerequis;
    String model;
    int modelData;

    Machinery() {}


    Machinery(String name, Material mat, Tech prerequis, String model, int modelData) {
        this.name = name;
        material = mat;
        this.prerequis = prerequis;
        this.model = model;
        this.modelData = modelData;
    }

    public String getModel() {
        return model;
    }


    public boolean isItemEqual(ItemStack item) {
        if(item == null) {
            return false;
        }
        if(! item.getItemMeta().hasCustomModelData()) {
            return false;
        }

        return item.getType().equals(material) && item.getItemMeta().getCustomModelData() == modelData;

    }

}

package fr.myriapod.milkywayexplorer.surface;

import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum SurfaceObject {

    BASIC_FURNACE("Four Basique", Material.FURNACE, Tech.SMELTING, "basic_furnace", 1001),
    CRAFTER("Crafter Basique", Material.CRAFTING_TABLE, Tech.ADVANCED_CRAFTNG, "basic_crafter", 1001),
    BASIC_DRILL("Foreuse Basique", Material.LIGHTNING_ROD, Tech.AUTOMATISATION_ESSENTIALS, "basic_drill", 1001);



    private String name;
    private Material material;
    private Tech prerequis;
    private String model;
    private int modelData;

    SurfaceObject(String name, Material mat, Tech prerequis, String model, int modelData) {
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
        if(! item.getItemMeta().hasCustomModelData()) {
            return false;
        }

        return item.getType().equals(material) && item.getItemMeta().getCustomModelData() == modelData;

    }

}

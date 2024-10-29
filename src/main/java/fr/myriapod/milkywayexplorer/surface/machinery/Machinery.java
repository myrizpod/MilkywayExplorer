package fr.myriapod.milkywayexplorer.surface.machinery;


import fr.myriapod.milkywayexplorer.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MachineryAnnotation
public abstract class Machinery {


    protected Vector3i pos;
    protected String name;
    protected Material material;
    protected Tech prerequis;
    protected String model;
    protected int modelData;
    protected List<String> description = new ArrayList<>();
    protected Map<Ressource, Integer> price = new HashMap<>();

    Machinery() {}


    Machinery(String name, Material mat, Tech prerequis, String model, int modelData, Map<Ressource, Integer> price) {
        this.name = name;
        material = mat;
        this.prerequis = prerequis;
        this.model = model;
        this.modelData = modelData;
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public Vector3i getLocation() {
        return pos;
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

    abstract void setupInfo();

    public ItemStack getAsItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setCustomModelData(modelData);
        meta.setLore(description);
        item.setItemMeta(meta);

        return item;
    }

    public String getName() {
        return name;
    }

    public Map<Ressource, Integer> getPrice() {
        return price;
    }
}

package fr.myriapod.milkywayexplorer.surface.machinery;


import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.MachineryType;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Machinery {


    protected Vector3i pos;
    protected String name;
    protected Material material;
    protected Tech prerequis;
    protected String id;
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

    abstract void setupInfo();

    public String getModel() {
        return model;
    }

    public String getID() {
        return id;
    }

    public Vector3i getLocation() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public Map<Ressource, Integer> getPrice() {
        return price;
    }


    public ItemStack getAsItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setCustomModelData(modelData);
        meta.setLore(description);
        item.setItemMeta(meta);

        return item;
    }


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

package fr.myriapod.milkywayexplorer.surface.machinery.machinerytype;

import fr.myriapod.milkywayexplorer.surface.ressource.Iron;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import fr.myriapod.milkywayexplorer.tools.DirectList;
import fr.myriapod.milkywayexplorer.tools.Tuple;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public enum DrillType implements MachineryType {
    BASIC("Foreuse Basique", Material.LIGHTNING_ROD, Tech.AUTOMATISATION_ESSENTIALS, "basic_drill", "basic_drill", 1001,
            new DirectList<>("Peut etre posé sur un sol exploitable"), new DirectList<>(new Tuple<>(new Iron().getNormalized(), 20)), 0.1);



    private String name;
    private Material material;
    private Tech prerequis;
    private String id;
    private String model;
    private int modelData;
    private List<String> description = new ArrayList<>();
    private Map<Ressource, Integer> price = new HashMap<>();
    private double productionTime;


    DrillType(String name, Material mat, Tech prerequis, String id, String model, int modelData, DirectList<String> description, DirectList<Tuple<Ressource, Integer>> price, double prodTime) {
        this.name = name;
        this.material = mat;
        this.prerequis = prerequis;
        this.id = id;
        this.model = model;
        this.modelData = modelData;
        Iterator<String> it = description.getIterator();
        while (it.hasNext()) {
            String s = it.next();
            this.description.add(s);
        }
        Iterator<Tuple<Ressource, Integer>> itt = price.getIterator();
        while (itt.hasNext()) {
            Tuple<Ressource, Integer> t = itt.next();
            this.price.put(t.getA(), t.getB());
        }
        this.productionTime = prodTime;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Tech getPrerequis() {
        return prerequis;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public int getModelData() {
        return modelData;
    }

    @Override
    public List<String> getDescription() {
        return description;
    }

    @Override
    public Map<Ressource, Integer> getPrice() {
        return price;
    }

    @Override
    public ItemStack getAsItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setCustomModelData(modelData);
        meta.setLore(description);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean isItemEqual(ItemStack item) {
        if(item == null) {
            return false;
        }
        if(! item.getItemMeta().hasCustomModelData()) {
            return false;
        }

        return item.getType().equals(material) && item.getItemMeta().getCustomModelData() == modelData;
    }

    public double getProductionTime() {return productionTime;}

}

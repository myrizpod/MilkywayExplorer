package fr.myriapod.milkywayexplorer.surface.machinery.machinerytype;

import fr.myriapod.milkywayexplorer.surface.ressource.Iron;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import fr.myriapod.milkywayexplorer.tools.DirectList;
import fr.myriapod.milkywayexplorer.tools.Tuple;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public enum CrafterType implements MachineryType {
    BASIC("Crafteur Basique", Material.CRAFTING_TABLE, Tech.AUTOMATISATION_ESSENTIALS, "crafter", null, 1001,
            new DirectList<>(ChatColor.RESET + "Permet de creer les Machines de base"), new DirectList<>(new Tuple<>(new Iron().getNormalized(), 10)), Tech.AUTOMATISATION_ESSENTIALS);


    private String name;
    private Material material;
    private Tech prerequis;
    private String id;
    private String model;
    private int modelData;
    private List<String> description;
    private Map<Ressource, Integer> price;
    private Tech products;


    CrafterType(String name, Material mat, Tech prerequis, String id, String model, int modelData, DirectList<String> description, DirectList<Tuple<Ressource, Integer>> price, Tech products) {
        this.name = name;
        this.material = mat;
        this.prerequis = prerequis;
        this.id = id;
        this.model = model;
        this.modelData = modelData;
        while (description.hasNext()) {
            String s = description.next();
            this.description.add(s);
        }
        while (price.hasNext()) {
            Tuple<Ressource, Integer> t = price.next();
            this.price.put(t.getA(), t.getB());
        }
        this.products = products;

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
}

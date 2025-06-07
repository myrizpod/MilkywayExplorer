package fr.myriapod.milkywayexplorer.surface.machinery.machinerytype;

import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import fr.myriapod.milkywayexplorer.tools.DirectList;
import fr.myriapod.milkywayexplorer.tools.Tuple;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Vector3i;

import java.util.*;

public enum CrafterType implements MachineryType {
    BASIC("Crafteur Basique", Material.CRAFTING_TABLE, Tech.AUTOMATISATION_ESSENTIALS, "basic_crafter", null, 1001,
            new DirectList<>(ChatColor.RESET + "Permet de creer les Machines de base"),
            new DirectList<>(new Tuple<>(Ressource.IRON, 10)),
            MachineryType.SCHEMATIC_SETTING_ONE_BLOCK, Tech.AUTOMATISATION_ESSENTIALS),
    NORMAL("Crafteur Normal", Material.CRAFTER, Tech.AUTOMATISATION_ESSENTIALS, "normal_crafter", "crafter", 1002,
            new DirectList<>(ChatColor.RESET + "Permet de creer les Machines de base"),
            new DirectList<>(new Tuple<>(Ressource.IRON, 2)),
            new SchematicSetting(4.1f, 3.1f), Tech.ADVANCED_CRAFTNG)
    ;


    private final String name;
    private final Material material;
    private final Tech prerequis;
    private final String id;
    private final String model;
    private final int modelData;
    private final List<String> description = new ArrayList<>();
    private final Map<Ressource, Integer> price = new HashMap<>();
    private final SchematicSetting schematicSetting;
    private Tech products;


    CrafterType(String name, Material mat, Tech prerequis, String id, String model, int modelData, DirectList<String> description, DirectList<Tuple<Ressource, Integer>> price, SchematicSetting schematicSetting, Tech products) {
        this.name = name;
        this.material = mat;
        this.prerequis = prerequis;
        this.id = id;
        this.model = model;
        this.modelData = modelData;
        this.schematicSetting = schematicSetting;
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
    public SchematicSetting getSchematicSetting() {
        return schematicSetting;
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

    public Tech getProducts() {
        return products;
    }

}

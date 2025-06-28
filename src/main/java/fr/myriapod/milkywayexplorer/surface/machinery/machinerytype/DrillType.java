package fr.myriapod.milkywayexplorer.surface.machinery.machinerytype;

import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import fr.myriapod.milkywayexplorer.tools.DirectList;
import fr.myriapod.milkywayexplorer.tools.Tuple;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public enum DrillType implements MachineryType {
    MINI("Foreuse Basique", Material.LIGHTNING_ROD, Tech.AUTOMATISATION_ESSENTIALS, "mini_drill", "mini_drill", 1001,
            new DirectList<>("Peut etre posé sur un sol exploitable"),
            new DirectList<>(new Tuple<>(Ressource.IRON, 20)),
            new SchematicSetting(3f,3f), 0.05),
    PNEUMATIC("Foreuse Basique", Material.LIGHTNING_ROD, Tech.AUTOMATISATION_ESSENTIALS, "pneumatic_drill", "pneumatic_drill", 1002,
            new DirectList<>("Peut etre posé sur un sol exploitable"),
            new DirectList<>(new Tuple<>(Ressource.IRON, 20)),
            new SchematicSetting(3f,3f), 0.1),
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
    private final double productionTime;


    DrillType(String name, Material mat, Tech prerequis, String id, String model, int modelData, DirectList<String> description, DirectList<Tuple<Ressource, Integer>> price, SchematicSetting schematicSetting, double prodTime) {
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

    public double getProductionTime() {return productionTime;}

}

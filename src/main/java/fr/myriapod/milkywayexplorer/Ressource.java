package fr.myriapod.milkywayexplorer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public enum Ressource {

    WOOD("Bois", Material.OAK_WOOD, 100, new GenParameters(0.25, null)),
    IRON("Fer", Material.IRON_INGOT, 101, new GenParameters(0.25, "iron_ore")),
    COPPER("Cuivre", Material.COPPER_INGOT, 102, new GenParameters(0.25, null)),
    SULFUR("Sulfur", Material.YELLOW_DYE, 103, new GenParameters(0.20, null)),
    GOLD("Or", Material.GOLD_INGOT, 104, new GenParameters(0.20, null)),
    TITANIUM("Titane", Material.BLUE_DYE, 105, new GenParameters(0.15, null));


    private String name;
    private Material material;
    private int modelData;
    private GenParameters parameters;

    Ressource(String name, Material mat, int modelData, GenParameters parameters) {
        this.name = name;
        this.material = mat;
        this.modelData = modelData;
        this.parameters = parameters;
    }


    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getModelData() {
        return modelData;
    }

    public double getRarity() {
        return parameters.rarity;
    }

    public String getModelName() {
        return parameters.model_name;
    }


    public boolean isEqual(ItemStack item) {
        if(item == null) {
            return false;
        }

        if(item.getItemMeta().hasCustomModelData() && item.getItemMeta().hasDisplayName()) {
            return item.getType().equals(material) && item.getItemMeta().getCustomModelData() == modelData;
        }
        return false;
    }

    public static Ressource nameToRessource(String string) {
        for(Ressource r : Ressource.values()) {
            if(r.name.toLowerCase().equals(string)) {
                return r;
            }
        }
        return null;
    }


    public static Map<Ressource, Integer> inventoryToRessources(Inventory inventory) {
        Map<Ressource, Integer> ressources = new HashMap<>();

        for(ItemStack item : inventory.getContents()) {
            if(item == null) {
                continue;
            }
            if(! item.getItemMeta().hasCustomModelData()) {
                continue;
            }

            for (Ressource r : Ressource.values()) {
                if (r.isEqual(item)) {
                    if(ressources.containsKey(r)) {
                        ressources.put(r, ressources.get(r) + item.getAmount());
                    } else {
                        ressources.put(r, item.getAmount());
                    }
                }
            }
        }

        return ressources;
    }


    /**
     * @Return Retourne une map vide si les ressources dans ressources sont plus grandes que celles demandées dans price sinon retourne la map des materiaux manquants
     *
     */
    public static Map<Ressource, Integer> ressourcesManquantes(Map<Ressource, Integer> ressources, Map<Ressource, Integer> price) {
        Map<Ressource, Integer> retourne = new HashMap<>();

        if(ressources.isEmpty()) {
            return price;
        } if(price.isEmpty()) {
            return retourne;
        }

        for(Ressource r : price.keySet()) {
            if(ressources.containsKey(r)) {
                if(ressources.get(r) < price.get(r)) {
                    retourne.put(r, price.get(r) - ressources.get(r));
                }
            } else {
                retourne.put(r, price.get(r));
            }
        }

        return retourne;
    }

    public ItemStack getAsItem(Integer integer) {
        ItemStack item = new ItemStack(material, integer);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(modelData);
        meta.setDisplayName(ChatColor.RESET + name);
        item.setItemMeta(meta);
        return item;
    }


    static class GenParameters {

        private double rarity;
        private String model_name;

        GenParameters(double rarity, String model) {
            this.rarity = rarity;
            this.model_name = model;
        }

    }


}

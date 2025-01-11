package fr.myriapod.milkywayexplorer.surface.ressource;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public enum Ressource {

    COAL("Charbon", Material.COAL, 100),
    IRON("Fer", Material.IRON_INGOT, 101),
    COPPER("Cuivre", Material.COPPER_INGOT, 102),
    IRON_BAR("Bar de Fer", Material.IRON_BARS, 110);


    final String name;
    final Material material;
    final int modelData;

    Ressource(String name, Material material, int modelData) {
        this.name = name;
        this.material = material;
        this.modelData = modelData;
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



    public boolean isEqual(ItemStack item) {
        if(item == null) {
            return false;
        }
        if(! item.hasItemMeta()) {
            return false;
        }

        if(item.getItemMeta().hasCustomModelData() && item.getItemMeta().hasDisplayName()) {
            return item.getType().equals(material) && item.getItemMeta().getCustomModelData() == modelData;
        }
        return false;
    }


    public ItemStack getAsItem(Integer integer) {
        ItemStack item = new ItemStack(material, integer);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setCustomModelData(modelData);
        meta.setDisplayName(ChatColor.RESET + name);
        item.setItemMeta(meta);
        return item;
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
            if(! item.hasItemMeta()) {
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
        for(Ressource pr : price.keySet()) {
            for (Ressource rr : ressources.keySet()) {
                if (rr.equals(pr)) {
                    if (ressources.get(rr) < price.get(pr)) {
                        retourne.put(pr, price.get(pr) - ressources.get(rr));
                    }
                } else {
                    retourne.put(pr, price.get(pr));
                }
            }
        }
        return retourne;
    }


}

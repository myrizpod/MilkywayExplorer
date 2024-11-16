package fr.myriapod.milkywayexplorer.surface.ressource;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

@RessourceAnnotation
public abstract class Ressource {

    abstract void setupInfo();

    String name;
    Material material;
    int modelData;

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getModelData() {
        return modelData;
    }


    public boolean isEqual(Ressource r) {
        return this.getClass().equals(r.getClass());
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


    public ItemStack getAsItem(Integer integer) {
        ItemStack item = new ItemStack(material, integer);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(modelData);
        meta.setDisplayName(ChatColor.RESET + name);
        item.setItemMeta(meta);
        return item;
    }


    public static Ressource nameToRessource(String string) {
        for(Ressource r : new RessourceAnnotationProcessor().getIterator()) {
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

            for (Ressource r : new RessourceAnnotationProcessor().getIterator()) {
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
                if (rr.isEqual(pr)) {
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

package fr.myriapod.milkywayexplorer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public enum Ressource {

    WOOD("Bois", Material.OAK_WOOD, 100),
    IRON("Fer", Material.IRON_INGOT, 101),
    COPPER("Cuivre", Material.COPPER_INGOT, 102),
    SULFUR("Sulfur", Material.YELLOW_DYE, 103),
    GOLD("Or", Material.GOLD_INGOT, 104),
    TITANIUM("Titane", Material.BLUE_DYE, 105);


    private String name;
    private Material material;
    private int modelData;

    Ressource(String name, Material mat, int modelData) {
        this.name = name;
        this.material = mat;
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
                if (item.getItemMeta().getCustomModelData() == r.modelData && item.getType().equals(r.material)) {
                    ressources.put(r, item.getAmount());
                }
            }
        }

        return ressources;
    }


    /**
     * @Return Retourne null si les ressources dans ressources sont plus grandes que celles demandées dans price sinon retourne la map des materiaux manquants
     *
     */
    public static Map<Ressource, Integer> ressourcesManquantes(Map<Ressource, Integer> ressources, Map<Ressource, Integer> price) {
        Map<Ressource, Integer> retourne = null;

        if(ressources.isEmpty()) {
            retourne = price;
        } if(price.isEmpty()) {
            retourne = null;
        }

        for(Ressource r : price.keySet()) {
            if(ressources.get(r) != null) {
                if(ressources.get(r) < price.get(r)) {
                    if(retourne == null) retourne = new HashMap<>();

                    retourne.put(r, price.get(r) - ressources.get(r));
                }
            }
        }

        return retourne;
    }

}

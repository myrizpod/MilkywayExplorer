package fr.myriapod.milkywayexplorer;

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


    public static Map<Ressource, Integer> inventoryToRessources(Inventory inventory) {
        Map<Ressource, Integer> ressources = new HashMap<>();

        for(ItemStack item : inventory.getContents()) {
            for(Ressource r : Ressource.values()) {
                if (item.getItemMeta().getCustomModelData() == r.modelData && item.getType().equals(r.material)) {
                    ressources.put(r, item.getAmount());
                }
            }
        }

        return ressources;
    }
}

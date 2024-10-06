package fr.myriapod.milkywayexplorer;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public enum Ressource {

    WOOD("Bois", Material.OAK_WOOD),
    IRON("Fer", Material.IRON_INGOT),
    COPPER("Cuivre", Material.COPPER_INGOT) ,
    SULFUR("Sulfur", Material.YELLOW_DYE),
    GOLD("Or", Material.GOLD_INGOT),
    TITANIUM("Titane", Material.BLUE_DYE);


    private String name;
    private Material material;

    Ressource(String name, Material mat) {
        this.name = name;
        this.material = mat;
    }


    public static Map<Ressource, Integer> inventoryToRessources(Inventory inventory) {
        Map<Ressource, Integer> ressources = new HashMap<>();

        for(ItemStack item : inventory.getContents()) {
            for(Ressource r : Ressource.values()) {
                if (item.getItemMeta().getDisplayName().equals(r.name) && item.getType().equals(r.material)) {
                    ressources.put(r, item.getAmount());
                }
            }
        }

        return ressources;
    }
}

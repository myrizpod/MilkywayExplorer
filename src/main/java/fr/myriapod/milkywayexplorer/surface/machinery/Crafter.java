package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.CrafterType;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.MachineryType;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.techtree.Tech;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Crafter extends Machinery {

    Set<MachineryType> products = new HashSet<>();
    CrafterType crafterType;

    public Crafter(CrafterType crafterType, Vector3i pos) {
        this.crafterType = crafterType;
        this.pos = pos;
        setupInfo();
    }

    @Override
    void setupInfo() {
        name = crafterType.getName();
        material = crafterType.getMaterial();
        prerequis = crafterType.getPrerequis();
        id = crafterType.getID();
        model = crafterType.getModel();
        modelData = crafterType.getModelData();
        description.addAll(crafterType.getDescription());
        price.putAll(crafterType.getPrice());

        for(MachineryType m : MachineryType.getAllTypes()) {
            if(m.getPrerequis().equals(Tech.AUTOMATISATION_ESSENTIALS)) {
                products.add(m);
            }
        }
    }


    public Inventory getCrafterInventory() {
        setupInfo();
        Inventory inv = Bukkit.createInventory(null, 9*5, ChatColor.GOLD + name);

        for(MachineryType m : products) {
            ItemStack item = m.getAsItem();
            List<String> lore = new ArrayList<>(m.getDescription());

            for(Ressource r : m.getPrice().keySet()) {
                lore.add(ChatColor.RESET + "" + ChatColor.GOLD + r.getName() + " " + ChatColor.AQUA + m.getPrice().get(r));
            }
            ItemMeta meta = item.getItemMeta();
            meta.setLore(lore);
            item.setItemMeta(meta);

            inv.addItem(item);
        }

        return inv;
    }

}

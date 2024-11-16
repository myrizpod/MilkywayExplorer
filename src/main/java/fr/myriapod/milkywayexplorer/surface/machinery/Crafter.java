package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Crafter extends Machinery {

    Set<Machinery> products = new HashSet<>();

    public Inventory getCrafterInventory() {
        setupInfo();
        Inventory inv = Bukkit.createInventory(null, 9*5, ChatColor.GOLD + name);

        for(Machinery m : products) {
            ItemStack item = m.getAsItem();
            List<String> lore = new ArrayList<>(m.description);

            for(Ressource r : m.price.keySet()) {
                lore.add(ChatColor.RESET + "" + ChatColor.GOLD + r.getName() + " " + ChatColor.AQUA + m.price.get(r));
            }
            ItemMeta meta = item.getItemMeta();
            meta.setLore(lore);
            item.setItemMeta(meta);

            inv.addItem(item);
        }

        return inv;
    }
}

package fr.myriapod.milkywayexplorer.techtree;

import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TechtreeInventories {


    public static final String RETOUR = ChatColor.RESET + "" + ChatColor.GOLD + "Retour";

    public static Inventory getDefaultInventory() {
        List<Tech> major = Tech.getMajorBranches();
        Inventory inv = Bukkit.createInventory(null, 6*9, Techtree.INVENTORY_NAME);

        int i = 0;
        for(Tech tech : major) {
            ItemStack item = new ItemStack(tech.getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RESET + tech.getName());
            item.setItemMeta(meta);

            inv.setItem(i, item);

            i++;

        }

        return inv;
    }


    public static Inventory getTree(Tech t) {
        List<Tech> sons = t.getArborescenceUnlocked();
        Inventory inv = Bukkit.createInventory(null, 6*9, t.getName());

        int i = 0;
        for(Tech son : sons) {

            List<String> lore = new ArrayList<>();
            ItemStack item = new ItemStack(son.getMaterial());
            ItemMeta meta = item.getItemMeta();

            lore.add(ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "Branche Pere: " + son.getFather().getName());
            lore.add(ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "Price: ");

            for(Ressource r : son.getPrice().keySet()) {
                lore.add(ChatColor.RESET + "" + ChatColor.GOLD + r.getName() + " " + ChatColor.AQUA + son.getPrice().get(r));
            }

            meta.setLore(lore);
            meta.setDisplayName(ChatColor.RESET +  son.getName());
            item.setItemMeta(meta);

            inv.setItem(i, item);

            i++;

        }

        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(RETOUR);
        item.setItemMeta(meta);

        inv.setItem(5*9 + 4, item);


        return inv;
    }


}

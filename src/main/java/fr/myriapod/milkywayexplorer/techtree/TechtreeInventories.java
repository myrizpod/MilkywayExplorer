package fr.myriapod.milkywayexplorer.techtree;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TechtreeInventories {


    public static Inventory getDefaultInventory() {
        List<Tech> major = Tech.getMajorBranches();
        Inventory inv = Bukkit.createInventory(null, 6*9, Techtree.INVENTORY_NAME);

        int i = 0;
        for(Tech tech : major) {
            ItemStack item = new ItemStack(tech.getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(tech.getName());
            item.setItemMeta(meta);

            inv.setItem(i, item);

            i++; //TODO faire meilleur placement

        }

        return inv;
    }


    public static Inventory getTree(Tech t) {
        List<Tech> sons = t.getSons();
        Inventory inv = Bukkit.createInventory(null, 6*9, Techtree.INVENTORY_NAME + t.getName()); //CHANGE TO MAKE PAS BO

        int i = 0;
        for(Tech son : sons) {
            ItemStack item = new ItemStack(son.getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(son.getName());
            item.setItemMeta(meta);

            inv.setItem(i, item);

            i++; //TODO faire meilleur placement

        }

        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + "Retour");


        return inv;
    }


}

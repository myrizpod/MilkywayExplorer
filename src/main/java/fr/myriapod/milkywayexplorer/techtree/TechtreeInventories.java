package fr.myriapod.milkywayexplorer.techtree;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
            meta.setDisplayName(tech.getName());
            item.setItemMeta(meta);

            inv.setItem(i, item);

            i++; //TODO faire meilleur placement

        }

        return inv;
    }


    public static Inventory getTree(Tech t) {
        List<Tech> sons = t.getSons();
        Inventory inv = Bukkit.createInventory(null, 6*9, Techtree.INVENTORY_NAME + t.getName()); //TODO Correct name affiching

        int i = 0;
        for(Tech son : sons) { //TODO get sons of sons si tech is unlocked
            ItemStack item = new ItemStack(son.getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(son.getName());
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

package fr.myriapod.milkywayexplorer.techtree;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TechtreeInventories {


    public static Inventory getTree(Tech t) {
        List<Tech> sons = t.getSons();
        Inventory inv = Bukkit.createInventory(null, sons.size()*3 + 9, Techtree.INVENTORY_NAME + t.getName());

        int i = 0;
        int place = 2;
        for(Tech son : sons) {
            ItemStack item = new ItemStack(son.getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(son.getName());
            item.setItemMeta(meta);

            inv.setItem(place * i, item);

            place += 3;

            if(place + 3 >= 9) {
                i++;
                place = 2;
            }


        }


        return inv;
    }


}

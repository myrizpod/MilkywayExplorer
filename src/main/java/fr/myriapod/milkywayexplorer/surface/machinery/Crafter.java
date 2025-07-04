package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.CrafterType;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.MachineryType;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.tools.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Vector3i;

import java.util.*;

public class Crafter extends Machinery implements Producter, Input {

    Set<MachineryType> products = new HashSet<>();

    public Crafter(CrafterType crafterType, Vector3i pos) {
        super(crafterType, pos);

        for(MachineryType m : MachineryType.getAllTypes()) {
            if(m.getPrerequis().equals(crafterType.getProducts())) {
                products.add(m);
            }
        }
    }


    public Inventory getCrafterInventory() {
        Inventory inv = Bukkit.createInventory(null, 9*5, ChatColor.GOLD + type.getName());

        for(MachineryType m : products) {
            ItemStack item = m.getAsItem();
            List<String> lore = new ArrayList<>(m.getDescription());

            for(Ressource r : m.getPrice().keySet()) {
                lore.add(ChatColor.RESET + "" + ChatColor.GOLD + r.getName() + " " + ChatColor.AQUA + m.getPrice().get(r));
            }
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setLore(lore);
            item.setItemMeta(meta);

            inv.addItem(item);
        }

        return inv;
    }


    @Override
    public void addIncomes(Map<Ressource, Integer> prod) {

    }

    @Override
    public void startProduction() {

    }

    @Override
    public void stopProduction() {

    }

    @Override
    public Map<Ressource, Integer> getProducted() {
        return null;
    }

    @Override
    public Tuple<Ressource, Integer> getProducted(Ressource r, int nb) {
        return null;
    }

    @Override
    public void productionLoop() {

    }
}

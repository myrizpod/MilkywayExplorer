package fr.myriapod.milkywayexplorer.surface;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public enum Tools {

    MANUAL_DRILL("Manual Drill", "Permet de miner un filon", Material.DIAMOND_PICKAXE, 1001, 5 * 20),
    MACHINERY_DESTRUCTOR("Machinery Destructor", "Permet de detruire et recuperer des machines posées", Material.MACE, 1001, 2 * 20);



    final String name;
    final List<String> description;
    final Material material;
    final int modelData;
    final int cooldown;

    Tools(String name, String desc, Material mat, int customModelData, int cooldown) {
        this.name = name;
        this.description = Collections.singletonList(desc);
        this.material = mat;
        this.modelData = customModelData;
        this.cooldown = cooldown;
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



    public boolean isEqual(ItemStack item) {
        if(item == null) {
            return false;
        }
        if(! item.hasItemMeta()) {
            return false;
        }

        if(item.getItemMeta().hasCustomModelData() && item.getItemMeta().hasDisplayName()) {
            return item.getType().equals(material) && item.getItemMeta().getCustomModelData() == modelData;
        }
        return false;
    }


    public ItemStack getAsItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setLore(description);
        meta.setCustomModelData(modelData);
        meta.setDisplayName(ChatColor.RESET + name);
        item.setItemMeta(meta);
        return item;
    }


    public boolean canBeUsedByPlayer(Player p) {
        return ! p.hasCooldown(material);
    }

    public void setCooldownForPlayer(Player p) {
        p.setCooldown(material, cooldown);
    }

}

package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.Ressource;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Drill extends Machinery {

    protected final Map<Ressource, Double> production = new HashMap<>();
    protected final Map<Ressource, Integer> producted = new HashMap<>();
    protected boolean isProducting = false;
    protected double prod;


    public void startProduction() {
        isProducting = true;
    }

    public void stopProduction() {
        isProducting = false;
    }

    public void setProduction(Ressource r) {
        production.put(r, this.prod);
        Bukkit.getLogger().info("production " + production);
    }

    public Set<Ressource> getRessources() {
        return production.keySet();
    }

    public Map<Ressource, Integer> getProducted() {
        Map<Ressource, Integer> prod = new HashMap<>(producted);
        producted.keySet().forEach(r -> producted.put(r, 0));
        return prod;
    }


    protected void productionLoop() {

        Bukkit.getScheduler().runTaskTimer(Main.plugin, () -> {
            if (isProducting) {
                for(Ressource r : production.keySet()) {
                    producted.computeIfAbsent(r, ressource -> 0);

                    int value = producted.get(r);
                    value++;

                    producted.put(r, value);
                }


            }
        }, 1, (long) (prod*100) * 20);

    }

}

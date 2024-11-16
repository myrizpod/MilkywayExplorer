package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.surface.ressource.Generable;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Drill extends Machinery implements Producter {

    protected final Map<Generable, Double> production = new HashMap<>();
    protected final Map<Generable, Integer> producted = new HashMap<>();
    protected boolean isProducting = false;
    protected double prod;


    public void addIncomes(Map<Ressource, Integer> incomes) {}

    public void startProduction() {
        isProducting = true;
    }

    public void stopProduction() {
        isProducting = false;
    }

    public void setProduction(Generable r) {
        production.put(r, this.prod);
        Bukkit.getLogger().info("production " + production);
    }

    public Set<Generable> getRessources() {
        return production.keySet();
    }

    public Map<Ressource, Integer> getProducted() {
        Map<Ressource, Integer> prod = new HashMap<>(producted);
        producted.keySet().forEach(r -> producted.put(r, 0));
        return prod;
    }


    public void productionLoop() {

        Bukkit.getScheduler().runTaskTimer(Main.plugin, () -> {
            if (isProducting) {
                for(Generable r : production.keySet()) {
                    producted.computeIfAbsent(r, ressource -> 0);

                    int value = producted.get(r);
                    value++;

                    producted.put(r, value);
                }


            }
        }, 1, (long) (prod*100) * 20);

    }

}

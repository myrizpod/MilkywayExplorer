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


    public void startProduction() {
        isProducting = true;
    }

    public void stopProduction() {
        isProducting = false;
    }

    public void setProduction(Ressource r, double prod) {
        production.put(r, prod);
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
        Map<Ressource, Double> productedD = new HashMap<>(production);
        production.keySet().forEach(r -> producted.computeIfAbsent(r, ressource -> 0));

        Bukkit.getScheduler().runTaskTimer(Main.plugin, () -> {

            if(isProducting) {
                for(Ressource r : production.keySet()) {
                    Double value = productedD.get(r);
                    value += production.get(r);

                    if(value > 1) {
                        producted.put(r, (int) (producted.get(r) + value));
                        value--;
                    }

                    productedD.put(r, value);

                }
            }
        }, 1, 20);

    }

}

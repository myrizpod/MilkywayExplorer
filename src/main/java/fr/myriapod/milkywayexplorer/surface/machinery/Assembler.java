package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Assembler extends Machinery implements Producter {

    protected final Map<Ressource , Map<Ressource, Integer>> recipes = new HashMap<>(); //la clé est la ressource créée et la value son craft
    protected Map<Ressource, Integer> incomes = new HashMap<>();
    protected final Map<Ressource, Integer> producted = new HashMap<>();
    protected boolean isProducting = false;
    protected double prod;

    public void addIncomes(Map<Ressource, Integer> incomes) {
        for(Ressource r : incomes.keySet()) {
            for(Ressource tr : this.incomes.keySet()) {
                if(r.isEqual(tr)) {
                    int n = this.incomes.get(tr);
                    n += incomes.get(r);
                    this.incomes.put(tr, n);
                }
            }
        }
    }

    public void startProduction() {
        isProducting = true;
    }

    public void stopProduction() {
        isProducting = false;
    }


    public Set<Ressource> getRessources() {
        return recipes.keySet();
    }

    public Map<Ressource, Integer> getProducted() {
        Map<Ressource, Integer> prod = new HashMap<>(producted);
        producted.keySet().forEach(r -> producted.put(r, 0));
        return prod;
    }


    public void productionLoop() {

        Bukkit.getScheduler().runTaskTimer(Main.plugin, () -> {
            if (isProducting) {
                for(Ressource craft : recipes.keySet()) {
                    producted.computeIfAbsent(craft, ressource -> 0);
                    if(Ressource.ressourcesManquantes(incomes, recipes.get(craft)).isEmpty()) {
                        int value = producted.get(craft);
                        value++;

                        producted.put(craft, value);

                        //removes ressources used for the craft
                        for(Ressource r : recipes.get(craft).keySet()) {
                            for(Ressource ir : incomes.keySet()) {
                                if(r.isEqual(ir)) {
                                    int n = incomes.get(ir);
                                    n -= recipes.get(craft).get(r);
                                    incomes.put(ir, n);
                                }
                            }
                        }
                    }

                }

            }
        }, 1, (long) (prod*100) * 20);

    }

}

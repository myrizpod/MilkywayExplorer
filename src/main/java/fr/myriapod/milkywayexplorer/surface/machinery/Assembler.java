package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.AssemblerType;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import org.bukkit.Bukkit;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Assembler extends Machinery implements Producter, Input, Output {

    protected Map<Ressource, Map<Ressource, Integer>> recipes = new HashMap<>(); //la clé est la ressource créée et la value son craft
    protected Map<Ressource, Integer> incomes = new HashMap<>();
    protected final Map<Ressource, Integer> producted = new HashMap<>();
    protected boolean isProducting = false;
    protected double prod;

    public Assembler(AssemblerType assembler, Vector3i v) {
        super(assembler, v);
        recipes.putAll(assembler.getProduction().getRecipes());
        prod = assembler.getProductionTime();
        startProduction();
        productionLoop();
    }


    public void addIncomes(Map<Ressource, Integer> incomes) {
        for(Ressource r : incomes.keySet()) {
            this.incomes.putIfAbsent(r, 0);
            for(Ressource tr : this.incomes.keySet()) {
                if(r.equals(tr)) {
                    Bukkit.getLogger().info("working ???");
                    int n = this.incomes.get(tr);
                    n += incomes.get(r);
                    this.incomes.put(tr, n);
                }
            }
        }
        Bukkit.getLogger().info("ass " + this.incomes);
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
        //TODO WTF THINGS HAPPENNING HERE I FKG DONT KNOW
        Bukkit.getScheduler().runTaskTimer(Main.plugin, () -> {
            if (isProducting) {
//                Bukkit.getLogger().info("m " + this);
                for(Ressource craft : recipes.keySet()) {
                    producted.computeIfAbsent(craft, ressource -> 0);
//                    Bukkit.getLogger().info("rm " + Ressource.ressourcesManquantes(incomes, recipes.get(craft)));
                    if(Ressource.ressourcesManquantes(incomes, recipes.get(craft)).isEmpty()) {
                        int value = producted.get(craft);
                        value++;

                        producted.put(craft, value);

                        //removes ressources used for the craft
                        for(Ressource r : recipes.get(craft).keySet()) {
                            for(Ressource ir : incomes.keySet()) {
                                if(r.equals(ir)) {
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

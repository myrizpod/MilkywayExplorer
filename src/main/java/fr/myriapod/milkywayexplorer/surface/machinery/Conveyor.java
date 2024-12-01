package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import org.bukkit.Bukkit;

import java.util.Map;

public abstract class Conveyor extends Machinery {

    Producter input;
    Producter output;
    double rate;

    public Conveyor() {}

    public Conveyor(Producter input, Producter output) {
        this.input = input;
        this.output = output;
    }


    void conveyorLoop() {

        Bukkit.getScheduler().runTaskTimer(Main.plugin, () -> {
            Map<Ressource, Integer> prod = input.getProducted();
            Bukkit.getLogger().info("conv " + prod);
            output.addIncomes(prod);
        }, 1, (long) (rate*100) * 20);
    }


    public boolean hasInput() {
        return input != null;
    }

    public Producter getInput() {
        return input;
    }

    public void setInput(Producter input) {
        this.input = input;
    }
}

package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.Main;
import org.bukkit.Bukkit;

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
            output.addIncomes(input.getProducted());
        }, 1, (long) (rate*100) * 20);
    }


}

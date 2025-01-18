package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.ConveyorType;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import org.bukkit.Bukkit;
import org.joml.Vector3i;

import java.util.Map;

public class Conveyor extends Machinery {

    Producter input;
    Producter output;
    double rate;

    public Conveyor(ConveyorType type, Vector3i pos, Producter input, Producter output) {
        super(type, pos);
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

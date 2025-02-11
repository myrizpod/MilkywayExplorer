package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.ConveyorType;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import org.bukkit.Bukkit;
import org.joml.Vector3i;

import java.util.Map;

public class Conveyor extends Machinery {

    Input input;
    Output output;
    double rate;

    public Conveyor(ConveyorType type, Vector3i pos, Output output, Input input) {
        super(type, pos);
        this.input = input;
        this.output = output;
        this.rate = type.getSpeed();
        conveyorLoop();
    }


    void conveyorLoop() {
        Bukkit.getScheduler().runTaskTimer(Main.plugin, () -> {
            Map<Ressource, Integer> prod = output.getProducted();
            Bukkit.getLogger().info("conv " + prod);
            input.addIncomes(prod);
        }, 1, (long) (rate*100) * 20);
    }

}

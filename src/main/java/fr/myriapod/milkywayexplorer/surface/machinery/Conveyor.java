package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.surface.MovingItem;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.ConveyorType;
import org.bukkit.Bukkit;
import org.joml.Vector3i;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Conveyor extends Machinery {

    Input input;
    Output output;
    double rate;
    List<MovingItem> allItems = new ArrayList<>();

    public Conveyor(ConveyorType type, Vector3i pos, @Nullable Input input, @Nullable Output output) {
        super(type, pos);
        this.rate = type.getSpeed();

        if(input != null) {
            Bukkit.getLogger().info(((Machinery) input).getID());
        }
        if(output != null) {
            Bukkit.getLogger().info(((Machinery) output).getID());
        }
//        conveyorLoop();
    }

//
//    void conveyorLoop() {
//        Bukkit.getScheduler().runTaskTimer(Main.plugin, () -> {
//            Map<Ressource, Integer> prod = output.getProducted();
//            Bukkit.getLogger().info("conv " + prod);
//            input.addIncomes(prod);
//        }, 1, (long) (rate*100) * 20);
//    }

}

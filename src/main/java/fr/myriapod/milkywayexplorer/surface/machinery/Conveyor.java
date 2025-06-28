package fr.myriapod.milkywayexplorer.surface.machinery;

import fr.myriapod.milkywayexplorer.Main;
import fr.myriapod.milkywayexplorer.surface.MovingItem;
import fr.myriapod.milkywayexplorer.surface.machinery.machinerytype.ConveyorType;
import fr.myriapod.milkywayexplorer.surface.ressource.Ressource;
import fr.myriapod.milkywayexplorer.tools.DelayedTask;
import fr.myriapod.milkywayexplorer.tools.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.util.Vector;
import org.joml.Vector3i;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Conveyor extends Machinery {

    Input input;
    Output output;
    double rate;
    int nbItemOnConveyor;
    int idScheduler;
    Block block;
    List<MovingItem> allItems = new ArrayList<>();

    //Map <Tuple<Output, Input>, ConveyorRate> to stock all conveyors on planet quit

    public Conveyor(ConveyorType type, Vector3i vPos, Block block, @Nullable Input input, @Nullable Output output) {
        super(type, vPos);
        this.rate = type.getSpeed();
        this.nbItemOnConveyor = 1;
        this.block = block;

        if(input != null) {
            Bukkit.getLogger().info("fouuuund input: " + ((Machinery) input).getID());
        }
        if(output != null) {
            Bukkit.getLogger().info("fouuuund output: " + ((Machinery) output).getID());
        }
        this.output = output;
        this.input = input;

        Bukkit.getLogger().info("START POS: " + getStartPos().toString());
        Bukkit.getLogger().info("END POS:   " + getEndPos().toString());

        conveyorLoop();
    }


    private void conveyorLoop() {
        idScheduler = Bukkit.getScheduler().runTaskTimer(Main.plugin, () -> {
            //TODO moving logic for item
            //DEBUG
            if(allItems.size() <= 1) {
                if (output != null) {
                    Tuple<Ressource, Integer> prod = output.getProducted(Ressource.IRON, nbItemOnConveyor);
                    if(prod.getB() > 0) {
                        addItem(new MovingItem(prod.getA(), prod.getB(), getStartPos()));
                    }
                }
            }

            if(input != null) {
                for(MovingItem m : allItems) {
                    if(m.getPosition().distance(getEndPos()) <= 0.2 + 0.5) {
                        allItems.remove(m);
                        //input.addIncomes(new Tuple<Ressource, Integer>(m.getRessource(), m.getNumber()));
                    }
                }
            }

        }, 1, 1).getTaskId(); //(long) (20/(rate*10))
    }

    public void sendForwardItems() {
        //allItems.forEach(i -> Bukkit.getLogger().info(i.getRessource().getName()));
        BlockState state = block.getState();
        if(state.getBlockData() instanceof TrapDoor trapDoor) {
            for (int i = allItems.size()-1; i >= 0; i--) { //classic loop to not have ConcurrentModificationException error
                MovingItem m = allItems.get(i);

                if(trapDoor.isOpen()) {
                    Vector v = new Vector();
                    //upward conveyor case
                    if(trapDoor.getHalf().equals(Bisected.Half.BOTTOM)) {
                        v.setY(1);
                    } else {
                        v.setY(-1);
                    }
                    m.addVelocity(v, rate);
                } else {
                    m.addVelocity(trapDoor.getFacing().getDirection(), rate);
                }

                m.changeConveyor();


                new DelayedTask(() -> {
                    Bukkit.getLogger().info("aaaaah");
                    if(input != null) {

                    }
                    m.stopMoving();
                }, (long) (20 / (rate * 10)));

                allItems.remove(i);

            }
        }
    }


    private Location getStartPos() {
        BlockState state = block.getState();
        TrapDoor trapDoor = (TrapDoor) state.getBlockData();
        BlockFace face = trapDoor.getFacing();
        Location blockLoc = block.getLocation();

        return blockLoc.clone().subtract(face.getDirection()).add(0.5, 1.3, 0.5);
    }

    private Location getEndPos() {
        BlockState state = block.getState();
        TrapDoor trapDoor = (TrapDoor) state.getBlockData();
        BlockFace face = trapDoor.getFacing();
        Location blockLoc = block.getLocation();

        return blockLoc.clone().add(face.getDirection()).add(0, 1.3, 0);
    }

    public boolean isThisMe(Block block) {
        return this.block.equals(block);
    }

    public void addItem(MovingItem movingItem) {
        allItems.add(movingItem);
        sendForwardItems();
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

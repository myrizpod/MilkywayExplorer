package fr.myriapod.milkywayexplorer.spaceexplorer.spaceship;

import fr.myriapod.milkywayexplorer.Game;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Field;

public class SpaceShipListener implements Listener {

    @EventHandler
    public void quitPassenger(EntityDismountEvent event) {
        Entity dismounter = event.getEntity();
        Entity dismounted = event.getDismounted();

        if(dismounter instanceof Player) {
            if(dismounted.getScoreboardTags().contains("ship") && ! dismounter.isOp()) {
                event.setCancelled(true);
            }
        }


    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws NoSuchFieldException, IllegalAccessException {
        Player player = event.getPlayer();

        //Craft Player (NMS varient of player)
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

        //Get connection
        ServerCommonPacketListenerImpl connection = nmsPlayer.c;

        //Get Network Manager field (Protected)
        Field f = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
        f.setAccessible(true);

        NetworkManager networkManager = (NetworkManager) f.get(connection);

        //Get Netty Channel field (Private)
        f = networkManager.getClass().getDeclaredField("n");
        f.setAccessible(true);

        Channel channel = (Channel) f.get(networkManager);

        //Add listener
        ChannelDuplexHandler duplexHandler = new ChannelDuplexHandler(){
            @Override
            public void channelRead(ChannelHandlerContext context, Object object)throws Exception {
                if(object instanceof PacketPlayInSteerVehicle packet){
                    Field sidewayField = packet.getClass().getDeclaredField("d");
                    sidewayField.setAccessible(true);
                    Field forwardField = packet.getClass().getDeclaredField("e");
                    forwardField.setAccessible(true);

                    float forwardSteer = (float) forwardField.get(packet);
                    float sidewaySteer = (float) sidewayField.get(packet);

                    Ship ship = Game.getShipByPlayer(player);
                    ship.movementInput(forwardSteer,sidewaySteer);
                }

                super.channelRead(context, object);
            }
        };

        channel.pipeline().addBefore("packet_handler",player.getUniqueId().toString(), duplexHandler);
    }


}

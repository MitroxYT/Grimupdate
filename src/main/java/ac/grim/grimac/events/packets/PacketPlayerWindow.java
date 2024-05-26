package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus.Action;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;

public class PacketPlayerWindow extends PacketListenerAbstract {

    public PacketPlayerWindow() {
        super(PacketListenerPriority.LOW);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        // Client Status is sent in 1.7-1.8
        if (event.getPacketType() == PacketType.Play.Client.CLIENT_STATUS) {
            WrapperPlayClientClientStatus wrapper = new WrapperPlayClientClientStatus(event);

            if (wrapper.getAction() == Action.OPEN_INVENTORY_ACHIEVEMENT) {
                GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
                if (player == null) return;

                player.hasInventoryOpen = true;
            }
        }

        // We need to do this due to 1.9 not sending anymore the inventory action in the Client Status
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) return;

            if (player.getClientVersion().isNewerThan(ClientVersion.V_1_8)) {
                player.hasInventoryOpen = true;
            }
        }

        if (event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) return;

            player.hasInventoryOpen = false;
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) return;

            player.sendTransaction();

            player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(),
                    () -> player.hasInventoryOpen = false);
        } else if (event.getPacketType() == PacketType.Play.Server.OPEN_WINDOW) {
            WrapperPlayServerOpenWindow wrapper = new WrapperPlayServerOpenWindow(event);

            // Beacon X button causes desync
            if (wrapper.getLegacyType() != null && wrapper.getLegacyType().equals("minecraft:beacon") ||
                    wrapper.getType() == 8) {
                return;
            }

            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) return;

            player.sendTransaction();

            player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(),
                    () -> player.hasInventoryOpen = true);
        } else if (event.getPacketType() == PacketType.Play.Server.OPEN_HORSE_WINDOW) {
            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) return;

            player.sendTransaction();

            player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(),
                    () -> player.hasInventoryOpen = true);
        } else if (event.getPacketType() == PacketType.Play.Server.CLOSE_WINDOW) {
            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) return;

            player.sendTransaction();

            player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(),
                    () -> player.hasInventoryOpen = false);
        }
    }
}
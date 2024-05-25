package ac.grim.grimac.utils.anticheat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRotation;

import java.util.HashMap;
import java.util.UUID;

public class NoDamageUtil extends Check implements PacketCheck {
    private int ckickaura;
    public static HashMap<UUID, Integer> killaurausers = new HashMap<>();
    public static HashMap<UUID, Integer> noslowusers = new HashMap<>();
    public static HashMap<UUID, Integer> noslow = new HashMap<>();

    public NoDamageUtil(GrimPlayer player) {
        super(player);
    }

    public void rotate(User p, int count) {
        User packetuser = p;
        for (int i = 0; i < count; i++) {
            float slotId = (float) (Math.random() * 180);
            float yaw = (float) (Math.random() * 180);
            WrapperPlayServerEntityRotation rotpacket = new WrapperPlayServerEntityRotation(p.getEntityId(),slotId,yaw,true);
            packetuser.sendPacket(rotpacket);
        }
    }
    public void onPacketReceive(PacketReceiveEvent e) {
        UUID playerId = e.getUser().getUUID();
        if (e.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            if (killaurausers.containsKey(playerId)) {
                e.setCancelled(true);
                if (++ckickaura >= 7) {
                    killaurausers.remove(playerId);
                    ckickaura = 0;
                }
            }
        }
        if (e.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            WrapperPlayClientPlayerBlockPlacement packet = new WrapperPlayClientPlayerBlockPlacement(e);
            if (noslowusers.containsKey(playerId)) {
                e.setCancelled(true);
                noslowusers.remove(playerId);
            }
        }
        if(e.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {

        }
        if (e.getPacketType() == PacketType.Play.Client.USE_ITEM) {
            WrapperPlayClientUseItem packet = new WrapperPlayClientUseItem(e);

            if (noslowusers.containsKey(playerId)) {
                e.setCancelled(true);
                noslowusers.remove(playerId);
            }
        }
    }
}

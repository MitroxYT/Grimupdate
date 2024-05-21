package ac.grim.grimac.utils.anticheat;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerHeldItemChange;

public class KnockbackUtil extends PacketListenerCommon implements PacketListener {
    public void onPacketSending(PacketEvent e) {
        int slotId = (int) (Math.random() * 9);
    }

    public void velocity(User p, int count) {
        if (count <= 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            int slotId = (int) (Math.random() * 9);
            Vector3d velocity = new Vector3d(1, 1, 1);
            WrapperPlayServerEntityVelocity velpacet = new WrapperPlayServerEntityVelocity(p.getEntityId(), velocity);
            WrapperPlayServerHeldItemChange packet = new WrapperPlayServerHeldItemChange(slotId);
            p.sendPacket(velpacet);
        }
    }
}

package ac.grim.grimac.utils.anticheat;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;

public class KnockbackUtil extends PacketListenerCommon implements PacketListener {


    public void velocity(User p, int count) {
        if (count <= 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            Vector3d velocity = new Vector3d(1, 1, 1);
            WrapperPlayServerEntityVelocity velpacet = new WrapperPlayServerEntityVelocity(p.getEntityId(), velocity);
            p.sendPacket(velpacet);
        }
    }
}

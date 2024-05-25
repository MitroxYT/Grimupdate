package ac.grim.grimac.utils.anticheat;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRotation;

import java.util.UUID;

public class Rotutil extends PacketListenerCommon implements PacketListener {
    public void rotate(User p, int count) {
        if (count <= 0) {
            return;
        }
        User packetuser = p;
        for (int i = 0; i < count; i++) {
            float slotId = (float) (Math.random() * 180);
            float yaw = (float) (Math.random() * 90);
            WrapperPlayServerEntityRotation rotpacket = new WrapperPlayServerEntityRotation(p.getEntityId(),slotId,yaw,true);
            packetuser.sendPacket(rotpacket);
        }
    }
}

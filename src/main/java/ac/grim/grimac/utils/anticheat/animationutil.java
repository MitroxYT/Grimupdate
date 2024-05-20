package ac.grim.grimac.utils.anticheat;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.*;

public class animationutil extends PacketListenerCommon implements PacketListener {
    public void rotate(User p, int count) {
        if (count <= 0) {
            return;
        }
        User packetuser = p;
        for (int i = 0; i < count; i++) {
            float slotId = (float) (Math.random() * 80);
            //float yaw = (float) (Math.random() * 90);
            WrapperPlayServerHurtAnimation animation = new WrapperPlayServerHurtAnimation(p.getEntityId(), slotId);
            WrapperPlayServerSetTitleText textg = new WrapperPlayServerSetTitleText("&cЧитер");
            packetuser.sendPacket(textg);
            packetuser.sendPacket(animation);
            packetuser.sendPacket(animation);
            packetuser.sendPacket(animation);
        }
    }
}

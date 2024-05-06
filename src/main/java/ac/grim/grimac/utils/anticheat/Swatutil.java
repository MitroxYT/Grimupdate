package ac.grim.grimac.utils.anticheat;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerHeldItemChange;

public class Swatutil extends PacketListenerCommon implements PacketListener {
    public void onPacketSending(PacketEvent e) {
        int slotId = (int) (Math.random() * 9);

    }
    public void swap(User p, int count) {
        if (count <= 0) {
            return;
        }
        User packetuser = p;
        for (int i = 0; i < count; i++) {
            int slotId = (int) (Math.random() * 9);
            WrapperPlayServerHeldItemChange packet = new WrapperPlayServerHeldItemChange(slotId);
            packetuser.sendPacket(packet);
        }
    }

}

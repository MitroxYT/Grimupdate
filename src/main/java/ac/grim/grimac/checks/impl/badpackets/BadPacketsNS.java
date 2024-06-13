package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.NoDamageUtil;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;

import java.util.HashMap;
import java.util.UUID;
@CheckData(name = "NoslowG")
public class BadPacketsNS extends Check implements PacketCheck {
    public BadPacketsNS(GrimPlayer player) {
        super(player);
    }
    private int buffer;
    public void onPacketReceive(PacketReceiveEvent e) {
        UUID playerId = e.getUser().getUUID();
        if(e.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
            if(player.packetStateData.slowedByUsingItem) {
                if (++buffer >= 5) {
                    NoDamageUtil.noslowusers.put(playerId, 1);
                    flagAndAlert();
                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();
                }
                if(++buffer== 10) {
                    buffer=0;
                }
            }
        }
    }
}

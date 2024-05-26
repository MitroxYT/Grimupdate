package ac.grim.grimac.checks.impl.combat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

import java.util.Objects;

import static ac.grim.grimac.utils.anticheat.Version.IS_bypassss;
/*
Author: NurcaDev9
Date: 12.05.24
 */
@CheckData(name = "KillauraA")
public class KillauraA extends Check implements PacketCheck {
    public KillauraA(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            if (IS_bypassss) return; //1.8.8 fix
            if (Objects.requireNonNull(player.bukkitPlayer).isHandRaised()) {
                flagWithSetback();
                flagrotateandswap();
                flagAndAlert("use: " + player.bukkitPlayer.isHandRaised());
                event.setCancelled(true);
                player.onPacketCancel();
            }
        }
    }
}

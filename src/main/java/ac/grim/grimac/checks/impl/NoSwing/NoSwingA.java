package ac.grim.grimac.checks.impl.NoSwing;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
@CheckData(name = "NoSwingA", experimental = false)
public class NoSwingA extends Check implements PacketCheck {
    public NoSwingA(final GrimPlayer player) {
        super(player);
    }
    private boolean sentAnimation;
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.ANIMATION) {
            sentAnimation = true;
        } else if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
            if (packet.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) return;
            if (!sentAnimation && flagAndAlert()) {
                event.setCancelled(true);
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
            }

            sentAnimation = false;
        } else if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            if (!sentAnimation) {
                flagAndAlert();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                event.setCancelled(true);
            }
        }
    }
}

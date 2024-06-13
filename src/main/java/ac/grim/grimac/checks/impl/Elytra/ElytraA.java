package ac.grim.grimac.checks.impl.Elytra;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.nmsutil.WatchableIndexUtil;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
//Basen on vulcan ElytraF
@CheckData(name = "ElytraA", experimental = false)
public class ElytraA extends Check implements PacketCheck {
    public ElytraA(final GrimPlayer player) {
        super(player);
    }
    private long last;
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
            WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(event);

            if (packet.getAction() == WrapperPlayClientEntityAction.Action.START_FLYING_WITH_ELYTRA) {
                final long delay = System.currentTimeMillis() - this.last;
                if (delay < 110L) {
                    flagAndAlert("Delay: " + delay);
                    event.setCancelled(true);
                }
                this.last = System.currentTimeMillis();
            }
        }
    }
}

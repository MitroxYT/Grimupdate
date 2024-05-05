package ac.grim.grimac.checks.impl.butterfly;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

// This check is based off of the Emu Anticheat check of the same name (double click)
@CheckData(name = "ButterflyA", decay = 0.025, experimental = true)
public class ButterflyA extends Check implements PacketCheck {
    private int attacks;

    public ButterflyA(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);

            if(wrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                this.attacks++;
            }
        }

        if (event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION || event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION || event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION || event.getPacketType() == PacketType.Play.Client.PLAYER_FLYING) {
            if (this.attacks > 1) {
                flagAndAlert();
            } else {
                reward();
            }
            this.attacks = 0;
        }
    }
}

package ac.grim.grimac.checks.impl.misc;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;


@CheckData(name = "ProtocolA", experimental = false)
public class BedBreakA extends Check implements PacketCheck {// Define the maximum reach distance
    private int avarage, spoof;
    public static boolean velocity;
    public BedBreakA(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent e) {
        if (velocity) {
        if (e.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging digging = new WrapperPlayClientPlayerDigging(e);
            if (digging.getAction()== DiggingAction.START_DIGGING) {
                //System.out.println(player.getName() + " " + digging.getAction());
                flagAndAlert();
                avarage = 0;
                velocity = false;
            }
        }
        }
        if (e.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {

        }
        if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            velocity=false;
        }
    }
    @Override
    public void reload() {
        super.reload();
        velocity = false;
        avarage = 0;
        //getConfig().getDoubleElse("NoSlowA.threshold", 0.001);
        //this.flagclick = getConfig().getInt("AutoClicker.maxcps");
    }
}

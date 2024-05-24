package ac.grim.grimac.checks.impl.autoclicker;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckData(name = "KillauraE")
public class AutoClickerA extends Check implements PacketCheck {
    private int ticks, cps, flagclick;

    public AutoClickerA(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            if (++ticks >= 20) {
                if (cps > 15 && !(event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) && !(event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT)) {
                    flagAndAlert("Check Avarage Combat=" + cps);
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                    flagrotateandswap();
                }
                ticks = cps = 0;
            }
        } else if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity action = new WrapperPlayClientInteractEntity(event);
            if (action.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                ++cps;
            }
        }
    }
    @Override
    public void reload() {
        super.reload();
        //getConfig().getDoubleElse("NoSlowA.threshold", 0.001);
        this.flagclick = getConfig().getInt("AutoClicker.maxcps");
    }
}

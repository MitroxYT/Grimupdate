package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.nmsutil.Materials;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.util.Vector3i;
@CheckData(name = "ScaffoldM")
public class ScaffoldM extends BlockPlaceCheck implements PacketCheck {
    private int ticks, bypassdist, tickflag , resetticks;
    private boolean flags;
    public ScaffoldM(GrimPlayer player) {
        super(player);
    }
    @Override
    public void onBlockPlace(final BlockPlace place) {
        if (player.fallDistance >= 3) return;
        if (player.gamemode == GameMode.CREATIVE) return;
        Vector3i blockPos = place.getPlacedAgainstBlockLocation();
        StateType placeAgainst = player.compensatedWorld.getStateTypeAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (blockPos.getY() <= bypassdist) return;
        if (placeAgainst.isAir() || Materials.isNoPlaceLiquid(placeAgainst) || player.onGround || !player.isSneaking) {
            if (++ticks >= 15) {
                if(flags) {
                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetbackandswap();
                    flagWithSetback();
                    place.resync();
                    place.resync();
                    place.resync();
                    place.resync();
                    place.resync();
                    place.resync();
                    flagAndAlert();
                }
                else {
                    flagrotateandswap();
                    flagAndAlert();
                    flagrotateandswap();
                }
                if (ticks >= 29) {
                    ticks = 0;
                }
            }
        }
    }
    @Override
    public void reload() {
        super.reload();
        //this.ticks = getConfig().getInt("Scaffold.B.flagticks");
        //this.resetticks = getConfig().getInt("Scaffold.B.flagresetticks");
        this.flags = getConfig().getBoolean("Scaffold.B.Cancel-build");
        this.bypassdist = getConfig().getInt("Scaffold.B.mindistance");
        ticks = 0;
    }
}

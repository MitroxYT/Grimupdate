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
@CheckData(name = "ScaffoldG")
public class ScaffoldG extends BlockPlaceCheck implements PacketCheck {
    private int ticks, countsblock, bypassdist;
    private boolean flags;
    public ScaffoldG(GrimPlayer player) {
        super(player);
    }
    @Override
    public void onBlockPlace(final BlockPlace place) {
        if (player.fallDistance >= 3) return;


        if (player.gamemode == GameMode.CREATIVE) return;
        Vector3i blockPos = place.getPlacedAgainstBlockLocation();
        StateType placeAgainst = player.compensatedWorld.getStateTypeAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (blockPos.getY() <= bypassdist) return;
        if (placeAgainst.isAir() || Materials.isNoPlaceLiquid(placeAgainst) || player.isSprinting) {
            ++countsblock;
        }

        if (placeAgainst.isAir() || Materials.isNoPlaceLiquid(placeAgainst) || player.isSprinting) {
            if (++ticks >= 21) {
                if (countsblock >= 5) {
                    if (flags) {
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
                        flagAndAlert("blocks: " + countsblock);
                        place.resync();
                        place.resync();
                        place.resync();
                        place.resync();
                        place.resync();
                        place.resync();
                    }
                    else {
                        flagrotateandswap();
                        flag();
                        flagAndAlert("blocks: " + countsblock);
                        flagrotateandswap();
                    }
                    if (countsblock >= 12) {
                        countsblock = 0;
                        ticks = 0;
                    }
                }
            }
        }
    }
    @Override
    public void reload() {
        super.reload();
        this.flags = getConfig().getBoolean("Scaffold.G.Cancel-build");
        this.bypassdist = getConfig().getInt("Scaffold.G.mindistance");

        countsblock = 0;
        ticks = 0;
    }
}

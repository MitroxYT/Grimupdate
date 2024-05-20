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
@CheckData(name = "ScaffoldE")
public class ScaffoldE extends BlockPlaceCheck implements PacketCheck {
    private int ticks, countsblock, bypassdist;
    private boolean flags;
    private long lastSprintTime;

    public ScaffoldE(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(final BlockPlace place) {
        if (player.fallDistance >= 3) return;

        if (player.gamemode == GameMode.CREATIVE) return;
        Vector3i blockPos = place.getPlacedAgainstBlockLocation();
        StateType placeAgainst = player.compensatedWorld.getStateTypeAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (blockPos.getY() <= bypassdist) return;

        if (placeAgainst.isAir() || Materials.isNoPlaceLiquid(placeAgainst) || player.isSneaking) {
                if (countsblock >= 5) {
                    if (flags) {
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
                        flagWithSetback();
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
                    } else {
                        flag();
                        flagAndAlert("blocks: " + countsblock  + "time: " + System.currentTimeMillis());
                    }
                    if (countsblock >= 10) {
                        countsblock = 0;
                        ticks = 0;
                    }
                }
        }

        if (player.isSneaking) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSprintTime < 5000) {
                ++countsblock;
            } else {
                countsblock = 0;
                lastSprintTime = currentTime;
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
        lastSprintTime = System.currentTimeMillis();
    }
}

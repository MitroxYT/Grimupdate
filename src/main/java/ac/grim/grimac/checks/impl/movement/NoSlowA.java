package ac.grim.grimac.checks.impl.movement;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.impl.combat.Reach;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.Swatutil;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import org.bukkit.entity.Player;

@CheckData(name = "NoSlowA", configName = "NoSlowA", setback = 1)
public class NoSlowA extends Check implements PostPredictionCheck {
    double offsetToFlag;
    double bestOffset = 1;
    // The player sends that they switched items the next tick if they switch from an item that can be used
    // to another item that can be used.  What the fuck mojang.  Affects 1.8 (and most likely 1.7) clients.
    public boolean didSlotChangeLastTick = false;
    public boolean flaggedLastTick = false;

    public NoSlowA(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPredictionComplete(final PredictionComplete predictionComplete) {
        if (!predictionComplete.isChecked()) return;

        // If the player was using an item for certain, and their predicted velocity had a flipped item
        if (player.packetStateData.slowedByUsingItem) {
            // 1.8 users are not slowed the first tick they use an item, strangely
            if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8) && didSlotChangeLastTick) {
                didSlotChangeLastTick = false;
                flaggedLastTick = false;
            }

            if (bestOffset > offsetToFlag) {
                if (flaggedLastTick) {
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
                    flagWithSetback();
                    if (shouldModifyPackets()) {
                        player.onPacketCancel();
                    }
                    player.onPacketCancel();
                    Swatutil swapUtil = new Swatutil();
                    swapUtil.swap(player.user, 1);
                    alert(" tickflag: " + flaggedLastTick);

                }
                flaggedLastTick = true;
            } else {
                reward();
                flaggedLastTick = false;
            }
        }
        bestOffset = 1;
    }

    public void handlePredictionAnalysis(double offset) {
        bestOffset = Math.min(bestOffset, offset);
    }

    @Override
    public void reload() {
        super.reload();
        offsetToFlag = getConfig().getDoubleElse("NoSlowA.threshold", 0.001);
    }
}

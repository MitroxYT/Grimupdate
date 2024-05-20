package ac.grim.grimac.checks.impl.misc;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
@CheckData(name = "BedBreakA", experimental = false)
public class BedBreakA extends Check implements PacketCheck {
    public BedBreakA(GrimPlayer player) {
        super(player);
    }
    @Override
    public void onPacketReceive(PacketReceiveEvent e) {
        if (e.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging digging = new WrapperPlayClientPlayerDigging(e);
            final Vector3i blockPosition = digging.getBlockPosition();

            if (digging.getAction() == DiggingAction.START_DIGGING) {
                WrappedBlockState block = player.compensatedWorld.getWrappedBlockStateAt(blockPosition);
                if (WrappedBlockState.getDefaultState(player.getClientVersion(), block.getType()).getType() == StateTypes.AIR) {
                    return;
                }
                final Vector3i playerPosition = (Vector3i) player.getPossibleEyeHeights();
                final double playerlocation = playerPosition.getX() + playerPosition.getX() + playerPosition.getZ();
                final double blockloacationlocation = blockPosition.getX() + blockPosition.getX() + blockPosition.getZ();
                if (playerlocation != blockloacationlocation) {
                    flagAndAlert();
                    e.setCancelled(true);
                    player.onPacketCancel();
                }

            }
        }
    }
}

/* ac.grim.grimac.checks.impl.misc;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;

import java.util.LinkedList;
import java.util.Queue;


@CheckData(name = "BacktrackA", experimental = true, setback = 5)
public class BackTrackA extends Check implements PacketCheck {
    private static final int MAX_HISTORY_SIZE = 20;
    private final Queue<PlayerPosition> positionHistory = new LinkedList<>();

    public BackTrackA(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity action = new WrapperPlayClientInteractEntity(event);
            if (action.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                checkForBackTrack();
            }
        } else if (event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            WrapperPlayClientPlayerPositionAndRotation position = new WrapperPlayClientPlayerPositionAndRotation(event);
            recordPosition(position);
        }
    }

    private void checkForBackTrack() {
        long currentTime = System.currentTimeMillis();
        PlayerPosition currentPosition = new PlayerPosition(player.bukkitPlayer.getLocation().getX(), player.bukkitPlayer.getLocation().getY(), player.bukkitPlayer.getLocation().getZ(), currentTime);

        for (PlayerPosition pastPosition : positionHistory) {
            long timeDifference = currentTime - pastPosition.timestamp();
            if (isSuspiciousBackTrack(currentPosition, pastPosition, timeDifference)) {
                if (flagWithSetback()) {
                    alert("positionHistory=" + positionHistory.size() + " timeDifference=" + timeDifference + "ms");
                    positionHistory.clear();
                }
                break;
            } else reward();
        }
    }

    private boolean isSuspiciousBackTrack(PlayerPosition currentPosition, PlayerPosition pastPosition, long timeDifference) {
        double distance = currentPosition.distanceTo(pastPosition);
        return distance < 0.1 && timeDifference > 50;
    }

    private void recordPosition(WrapperPlayClientPlayerPositionAndRotation position) {
        PlayerPosition newPosition = new PlayerPosition(position.getPosition().getX(), position.getPosition().getY(), position.getPosition().getZ(), System.currentTimeMillis());
        if (positionHistory.size() >= MAX_HISTORY_SIZE) {
            positionHistory.poll(); // Remove the oldest position if history exceeds the maximum size
        }
        positionHistory.add(newPosition);
    }

    private record PlayerPosition(double x, double y, double z, long timestamp) {

        double distanceTo(PlayerPosition other) {
            return Math.sqrt(Math.pow(this.x - other.x, 2) +
                    Math.pow(this.y - other.y, 2) +
                    Math.pow(this.z - other.z, 2));
        }
    }
}*/
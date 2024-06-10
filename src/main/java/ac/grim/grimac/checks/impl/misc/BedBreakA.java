package ac.grim.grimac.checks.impl.misc;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

@CheckData(name = "BedBreakA", experimental = false)
public class BedBreakA extends Check implements PacketCheck {

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
                if (digging.getAction() == DiggingAction.START_DIGGING) {
                    Location blockLocation = new Location(player.bukkitPlayer.getWorld(), digging.getBlockPosition().getX(), digging.getBlockPosition().getY(), digging.getBlockPosition().getZ());
                    Block block = blockLocation.getBlock();
                    if (block.getType() == Material.RED_BED && isBedSurrounded(block)) {
                        e.setCancelled(true);
                        player.onPacketCancel();
                        flagAndAlert();
                        avarage = 0;
                        velocity = false;
                    }
                }
            }
        }
        if (e.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
        }
        if (e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            velocity = false;
        }
    }

    private boolean isBedSurrounded(Block bedBlock) {
        BlockFace[] faces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
        for (BlockFace face : faces) {
            if (bedBlock.getRelative(face).getType() == Material.AIR) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void reload() {
        super.reload();
        velocity = false;
        avarage = 0;
    }
}
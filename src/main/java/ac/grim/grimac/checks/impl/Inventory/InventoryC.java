package ac.grim.grimac.checks.impl.Inventory;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@CheckData(name = "InventoryC")
public class InventoryC extends Check implements PacketCheck {

    public InventoryC(GrimPlayer player) {
        super(player);
    }
    private Map<UUID, Long> clickTimestamps = new HashMap<>();;
    private Map<String, Integer> clickCounts = new HashMap<>();;
    private boolean cancel;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            UUID playerId = player.getUniqueId();

            long currentTime = System.currentTimeMillis();

            if (!clickTimestamps.containsKey(playerId)) {
                clickTimestamps.put(playerId, currentTime);
                return;
            }

            long lastClickTime = clickTimestamps.get(playerId);

            if (currentTime - lastClickTime < 1000) {
                clickTimestamps.put(playerId, currentTime);

                int clickCount = clickCounts.getOrDefault(playerId.toString(), 0) + 1;
                clickCounts.put(playerId.toString(), clickCount);
                if (clickCount > 90 && shouldModifyPackets()) {
                    close();
                    flagWithSetback();
                   // event.setCancelled(true);
                    flagAndAlert();

                }
            } else {
                clickCounts.put(playerId.toString(), 1);
                clickTimestamps.put(playerId, currentTime);
            }
        }
    }
}

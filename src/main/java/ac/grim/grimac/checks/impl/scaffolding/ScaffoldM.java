package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ScaffoldM extends Check implements PacketCheck {
    private final Map<UUID, Long> lastSlotChangeTimeStamps = new HashMap<>();
    private final Set<UUID> itemUseInProgress = new HashSet<>();
    public static HashMap<UUID, Integer> dubugCMD2 = new HashMap<>();
    private Plugin ScaffoldM;

    public ScaffoldM(GrimPlayer player) {
        super(player);
    }
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (event.getPacketType() == PacketType.Play.Client.USE_ITEM) {
            //dubugCMD2.get(playerId) == 0;
            dubugCMD2.put(playerId, 1);
            itemUseInProgress.add(playerId);
            //Bukkit.getScheduler().runTaskLater(ScaffoldM, () -> itemUseInProgress.remove(playerId), 5);
            //checkForSuspiciousActivity(player, currentTime);
        }
        if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
            if (dubugCMD2.getOrDefault(playerId, 0) == 1) {
                if (itemUseInProgress.contains(playerId)) {
                    if (lastSlotChangeTimeStamps.containsKey(playerId)) {
                        long lastChangeTime = lastSlotChangeTimeStamps.get(playerId);

                        if ((currentTime - lastChangeTime) < 100) {
                            itemUseInProgress.remove(playerId);
                            flagAndAlert("");
                            flagWithSetback();
                            flagWithSetback();
                            flagWithSetback();
                            flagWithSetback();
                            flagWithSetback();
                            flagWithSetback();
                            flagWithSetback();
                        }
                        dubugCMD2.put(playerId, 0);
                    }
                }
            }
            if (dubugCMD2.get(playerId) == 1) {
                dubugCMD2.put(playerId, 0);
            }
            lastSlotChangeTimeStamps.put(playerId, currentTime);
            dubugCMD2.put(playerId, 0);
        }

    }
}

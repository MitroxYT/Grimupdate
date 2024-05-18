package ac.grim.grimac.checks.impl.movement;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CheckData(name = "NoSlowC")
public class NoslowC extends Check implements PacketCheck {
    public static Map<String, Integer> vlnoslowb = new HashMap<>();

    private final Map<UUID, Integer> slotChangeCount = new HashMap<>();
    private final Map<UUID, Long> lastSlotChangeTime = new HashMap<>();
    public static HashMap<UUID, Integer> dubugCMD2 = new HashMap<>();
    private int use;
    public NoslowC(GrimPlayer player) {
        super(player);
    }
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        UUID playerId = player.getUniqueId();
        if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {

            if(dubugCMD2.getOrDefault(playerId, 0)== 1) {
                slotChangeCount.put(playerId, slotChangeCount.getOrDefault(playerId, 0) + 1);
                long currentTime = System.currentTimeMillis();
                Long lastChangeTime = lastSlotChangeTime.getOrDefault(playerId, currentTime);

                if ((currentTime - lastChangeTime) > 2000) {
                    lastSlotChangeTime.put(playerId, currentTime);
                    slotChangeCount.put(playerId, 1);
                    dubugCMD2.put(playerId, 0);
                } else {
                    if (slotChangeCount.get(playerId) > 40) {
                        flagAndAlert();

                    }

                }
                slotChangeCount.put(playerId, 0);
            }
            if(event.getPacketType() == PacketType.Play.Client.USE_ITEM) {
                dubugCMD2.put(playerId, 1);
            }
        }
    }
}

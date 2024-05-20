package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.GrimAC;
import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.impl.combat.Reach;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@CheckData(name = "BadPacketsV", experimental = true)
public class BadPacketsV extends Check implements PacketCheck {
    

    public BadPacketsV(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity interactEntity = new WrapperPlayClientInteractEntity(event);
            if (interactEntity.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) return;
            if (!player.packetStateData.slowedByUsingItem) return;
            ItemStack itemInUse = player.getInventory().getItemInHand(player.packetStateData.eatingHand);
            if (flagAndAlert("UseItem=" + itemInUse.getType().getName().getKey()) && shouldModifyPackets()) {
                Player p = (Player) event.getPlayer();
                event.isCancelled();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
<<<<<<< Updated upstream
=======
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
>>>>>>> Stashed changes
                event.setCancelled(true);
                player.onPacketCancel();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
                flagWithSetback();
            }
        }
    }
}

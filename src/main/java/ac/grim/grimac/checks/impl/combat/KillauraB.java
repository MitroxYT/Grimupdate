package ac.grim.grimac.checks.impl.combat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityTrackXRot;
import ac.grim.grimac.utils.nmsutil.Ray;
import ac.grim.grimac.utils.nmsutil.Ray.*;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

@CheckData(name = "KillauraB")
public class KillauraB extends Check implements PacketCheck {
    public KillauraB(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity action = new WrapperPlayClientInteractEntity(event);
            if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {}
            if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
                WrapperPlayClientPlayerDigging digging = new WrapperPlayClientPlayerDigging(event);
                final Vector3i blockPosition = digging.getBlockPosition();
            PacketEntity entity = player.compensatedEntities.entityMap.get(action.getEntityId());
            final SimpleCollisionBox entitydamege = entity.getPossibleCollisionBoxes();
                EntityType Player;
            //RayTraceResult result = p.getWorld().rayTrace(p.getEyeLocation(), p.getLocation().getDirection().multiply(2.0), 100, FluidCollisionMode.NEVER, true, 0.9, entity1 -> entity1.equals(entity));
            //норм
            }
        }
    }
}

package ac.grim.grimac.checks.impl.misc;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import org.bukkit.scoreboard.Criteria;

import java.util.List;

@CheckData(name = "MetaDataHider", description = "Hide metadata for nametags hacks.")
public class MetadataHider extends Check implements PacketCheck {
    public MetadataHider(GrimPlayer player) {
        super(player);
    }

    boolean enable, healthHider, xpHider, oxygenHider, absorptionHider, onlyForPlayers;

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if(player.disableGrim || player.noModifyPacketPermission) {
            return;
        }

        if (!enable || event.getPacketType() != PacketType.Play.Server.ENTITY_METADATA) {
            return;
        }

        final WrapperPlayServerEntityMetadata wrapper = new WrapperPlayServerEntityMetadata(event);
        final int entityId = wrapper.getEntityId();

        // Player can get their own metadata.
        if (event.getUser().getEntityId() == entityId) {
            return;
        }

        // This is automatically cached.
        final PacketEntity packetEntity = player.compensatedEntities.getEntity(entityId);

        // Lookup failed, so the entity is not a living entity.
        if (packetEntity == null || !packetEntity.isLivingEntity()) {
            return;
        }


        // Bossbar problems
        // Cannot use Boss interface as that doesn't exist on 1.8.8

        List<EntityData> entityMetaData = wrapper.getEntityMetadata();

        boolean shouldPush = false;

        for (EntityData data : entityMetaData) {
            // Search for health.

            if (healthHider && data.getIndex() == MetadataIndex.HEALTH) {
                float health = Float.parseFloat(String.valueOf(data.getValue()));
                // Only modify alive entities (health > 0).
                if (health > 0) {
                    data.setValue(Float.NaN);
                    shouldPush = true;
                }
            }
            else if (oxygenHider && data.getIndex() == MetadataIndex.AIR_TICKS) {
                setDynamicValue(data, 1);
                shouldPush = true;
            }
            else if (xpHider && data.getIndex() == MetadataIndex.XP) {
                setDynamicValue(data, 0);
                shouldPush = true;
            }
            else if (absorptionHider && data.getIndex() == MetadataIndex.ABSORPTION) {
                setDynamicValue(data, 0);
                shouldPush = true;
            }
        }

        if(shouldPush) {
            push(event, wrapper.getEntityId(), entityMetaData);
        }
    }

    void push(PacketSendEvent event, int entityId, List<EntityData> dataList) {
        event.setCancelled(true);
        WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata(entityId, dataList);
        ChannelHelper.runInEventLoop(player.user.getChannel(), () -> player.user.sendPacketSilently(metadata));
    }

    private void setDynamicValue(EntityData obj, int spoofValue) {
        Object value = obj.getValue();
        if (value instanceof Integer) {
            obj.setValue(spoofValue);
        } else if (value instanceof Short) {
            obj.setValue((short) spoofValue);
        } else if (value instanceof Byte) {
            obj.setValue((byte) spoofValue);
        } else if (value instanceof Long) {
            obj.setValue(spoofValue);
        } else if (value instanceof Float) {
            obj.setValue((float) spoofValue);
        } else if (value instanceof Double) {
            obj.setValue(spoofValue);
        }
    }

    @Override
    public void reload() {
        onlyForPlayers = getConfig().getBooleanElse("visual.metadata-hider.onlyForPlayers", true);

        healthHider = getConfig().getBooleanElse("visual.metadata-hider.health", true);
        xpHider = getConfig().getBooleanElse("visual.metadata-hider.xp", true);
        oxygenHider = getConfig().getBooleanElse("visual.metadata-hider.oxygen", true);
        absorptionHider = getConfig().getBooleanElse("visual.metadata-hider.absorption", true);

        enable = healthHider || xpHider || oxygenHider || absorptionHider;
    }
}

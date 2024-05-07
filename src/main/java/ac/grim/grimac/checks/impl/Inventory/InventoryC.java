package ac.grim.grimac.checks.impl.Inventory;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;


@CheckData(name = "InventoryC")
public class InventoryC extends Check implements PacketCheck {
    private org.bukkit.event.player.PlayerMoveEvent p;

    public InventoryC(GrimPlayer player) {
        super(player);
    }

    private boolean cancel;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            WrapperPlayClientClickWindow wrapper = new WrapperPlayClientClickWindow(event);


        }
    }
}

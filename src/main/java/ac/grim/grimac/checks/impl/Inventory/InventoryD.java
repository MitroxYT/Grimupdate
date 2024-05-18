package ac.grim.grimac.checks.impl.Inventory;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;

@CheckData(name = "InventoryD")
public class InventoryD extends Check implements PacketCheck {
    public InventoryD(GrimPlayer player) {
        super(player);
    }
    private boolean cancel;
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            WrapperPlayClientClickWindow wrapper = new WrapperPlayClientClickWindow(event);
            if (player.isSwimming) {
                if (cancel) {
                    flagAndAlert("sw: " + player.isSwimming);
                    flagrotateandswap();
                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();
                    event.setCancelled(true);
                    player.onPacketCancel();
                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();
                    flagWithSetback();
                    event.setCancelled(true);
                    player.onPacketCancel();
                }
                else {
                    flagAndAlert("");
                }
            }
        }
    }
    @Override
    public void reload() {
        super.reload();
        this.cancel = getConfig().getBooleanElse("InventoryB.cancel-click", false);
    }
}

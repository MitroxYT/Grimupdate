package ac.grim.grimac;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrimAC extends JavaPlugin {

    @Override
    public void onLoad() {
        GrimAPI.INSTANCE.load(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("GrimAC has been disabled!");
        GrimAPI.INSTANCE.stop(this);
    }

    @Override
    public void onEnable() {
        GrimAPI.INSTANCE.start(this);
    }
}

package ac.grim.grimac.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.impl.aim.processor.AimProcessor;
import ac.grim.grimac.checks.impl.misc.ClientBrand;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.anticheat.MultiLibUtil;
import ac.grim.grimac.utils.anticheat.Rotutil;

import ac.grim.grimac.utils.math.GrimMath;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("grim|grimac")
public class GrimRoteblo extends BaseCommand {
    @Subcommand("rot")
    @CommandPermission("grim.rot")
    @CommandCompletion("@players")
    public void onConsoleDebug(CommandSender sender, OnlinePlayer target) {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;

        // Short circuit due to minimum java requirements for MultiLib
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18) && MultiLibUtil.isExternalPlayer(target.getPlayer())) {
            String alertString = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("player-not-this-server", "%prefix% &cPlayer isn't on this server!");
            sender.sendMessage(MessageUtil.format(alertString));
            return;
        }

        GrimPlayer grimPlayer = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(target.getPlayer());
        if (grimPlayer == null) {
            String message = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("player-not-found", "%prefix% &cPlayer is exempt or offline!");
            sender.sendMessage(MessageUtil.format(message));
            return;
        }

        else {
            UUID uuid = grimPlayer.getUniqueId();
            String message = "&cуспешно изменил ротацию игрока " + target.getPlayer().getName();
            sender.sendMessage(MessageUtil.format(message));
            Rotutil rotut = new Rotutil();
            rotut.rotate(grimPlayer.user, 1);
            rotut.rotate(grimPlayer.user, 1);
            rotut.rotate(grimPlayer.user, 1);
        }
    }
}

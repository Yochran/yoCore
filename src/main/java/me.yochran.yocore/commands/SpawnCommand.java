package me.yochran.yocore.commands;

import me.yochran.yocore.management.PlayerManagement;
import me.yochran.yocore.player.yoPlayer;
import me.yochran.yocore.utils.Utils;
import me.yochran.yocore.yoCore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    private final yoCore plugin;

    private final PlayerManagement playerManagement = new PlayerManagement();

    public SpawnCommand() {
        this.plugin = yoCore.getPlugin(yoCore.class);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.translate(this.plugin.getConfig().getString("Spawn.MustBePlayer")));
            return true;
        }

        if (args.length > 1) {
            sender.sendMessage(Utils.translate(this.plugin.getConfig().getString("Spawn.IncorrectUsage")));
            return true;
        }

        if (args.length == 0) {
            this.playerManagement.sendToSpawn((Player)sender);
            sender.sendMessage(Utils.translate(this.plugin.getConfig().getString("Spawn.TargetMessage")));
        } else {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(Utils.translate(this.plugin.getConfig().getString("Spawn.InvalidPlayer")));
                return true;
            }

            this.playerManagement.sendToSpawn(target);

            sender.sendMessage(Utils.translate(this.plugin.getConfig().getString("Spawn.ExecutorMessage")
                    .replace("%target%", yoPlayer.getYoPlayer((OfflinePlayer)target).getDisplayName())));

            target.sendMessage(Utils.translate(this.plugin.getConfig().getString("Spawn.TargetMessage")));
        }

        return true;
    }
}

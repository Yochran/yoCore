package me.yochran.yocore.commands.stats.staff;

import me.yochran.yocore.management.StatsManagement;
import me.yochran.yocore.player.yoPlayer;
import me.yochran.yocore.utils.Utils;
import me.yochran.yocore.yoCore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetStatsCommand implements CommandExecutor {

    private final yoCore plugin;
    private final StatsManagement statsManagement = new StatsManagement();

    public ResetStatsCommand() {
        plugin = yoCore.getPlugin(yoCore.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("ResetStats.MustBePlayer")));
            return true;
        }

        if (!statsManagement.statsAreEnabled()) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Stats.NotEnabledMessage")));
            return true;
        }

        if (!sender.hasPermission("yocore.resetstats")) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("ResetStats.NoPermission")));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("ResetStats.IncorrectUsage")));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        yoPlayer yoTarget = new yoPlayer(target);

        if (!statsManagement.isInitialized(target)) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("ResetStats.InvalidPlayer")));
            return true;
        }

        statsManagement.resetPlayer(target);

        sender.sendMessage(Utils.translate(plugin.getConfig().getString("ResetStats.Format")
                .replace("%target%", yoTarget.getDisplayName())));

        return true;
    }
}
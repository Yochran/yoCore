package me.yochran.yocore.commands.economy.staff;

import me.yochran.yocore.management.EconomyManagement;
import me.yochran.yocore.player.yoPlayer;
import me.yochran.yocore.utils.Utils;
import me.yochran.yocore.yoCore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnbountyCommand implements CommandExecutor {

    private final yoCore plugin;

    private final EconomyManagement economyManagement = new EconomyManagement();

    public UnbountyCommand() {
        plugin = yoCore.getPlugin(yoCore.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Unbounty.MustBePlayer")));
            return true;
        }

        if (!economyManagement.economyIsEnabled()) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.NotEnabledMessage")));
            return true;
        }

        if (!sender.hasPermission("yocore.unbounty")) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Unbounty.NoPermission")));
            return true;
        }

        if (!economyManagement.bountyIsEnabled()) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Bounty.NotEnabledMessage")));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Unbounty.IncorrectUsage")));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        yoPlayer yoTarget = new yoPlayer(target);

        if (!economyManagement.isInitialized(target)) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Unbounty.InvalidPlayer")));
            return true;
        }

        if (!economyManagement.isBountied(target)) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Unbounty.PlayerNotBountied")));
            return true;
        }

        OfflinePlayer executor = Bukkit.getOfflinePlayer(UUID.fromString(economyManagement.getBountyExecutor(target)));

        economyManagement.addMoney(executor, economyManagement.getBountyAmount(target));
        economyManagement.removeBounty(target);

        sender.sendMessage(Utils.translate(plugin.getConfig().getString("Unbounty.Format")
                .replace("%target%", yoTarget.getDisplayName())));

        return true;
    }
}
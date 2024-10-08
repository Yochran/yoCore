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

import java.text.DecimalFormat;

public class EconomyCommand implements CommandExecutor {

    private final yoCore plugin;

    private final EconomyManagement economyManagement = new EconomyManagement();

    public EconomyCommand() {
        plugin = yoCore.getPlugin(yoCore.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.MustBePlayer")));
            return true;
        }

        if (!economyManagement.economyIsEnabled()) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.NotEnabledMessage")));
            return true;
        }

        if (!sender.hasPermission("yocore.economycommand")) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.NoPermission")));
            return true;
        }

        if (args.length < 2 || args.length > 3) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.IncorrectUsage")));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        yoPlayer yoTarget = new yoPlayer(target);

        if (!economyManagement.isInitialized(target)) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.InvalidPlayer")));
            return true;
        }

        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###.##");

        switch (args.length) {
            case 2:
                if (!args[0].equalsIgnoreCase("reset")) {
                    sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.IncorrectUsage")));
                    return true;
                }

                economyManagement.resetPlayer(target);

                sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.Format.Reset")
                        .replace("%target%", yoTarget.getDisplayName())
                        .replace("%amount%", df.format(economyManagement.getMoney(target)))));

                if (target.isOnline()) {
                    Bukkit.getPlayer(target.getName()).sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.FormatTarget")
                            .replace("%balance%", df.format(economyManagement.getMoney(target)))));
                }

                break;
            case 3:
                try { Integer.parseInt(args[2]); } catch (NumberFormatException exception) {
                    sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.InvalidAmount")
                            .replace("%maximum%", df.format(plugin.getConfig().getDouble("Economy.MaximumAmount")))));
                    return true;
                }

                if (economyManagement.isOverMaximum(Double.parseDouble(args[2]))) {
                    sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.InvalidAmount")
                            .replace("%maximum%", df.format(plugin.getConfig().getDouble("Economy.MaximumAmount")))));
                    return true;
                }

                if (args[0].equalsIgnoreCase("set")) {
                    plugin.economyData.config.set(target.getUniqueId().toString() + ".Balance", Double.parseDouble(args[2]));
                    plugin.economyData.saveData();

                    sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.Format.Set")
                            .replace("%target%", yoTarget.getDisplayName())
                            .replace("%amount%", df.format(economyManagement.getMoney(target)))));

                    if (target.isOnline()) {
                        Bukkit.getPlayer(target.getName()).sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.FormatTarget")
                                .replace("%balance%", df.format(economyManagement.getMoney(target)))));
                    }
                } else if (args[0].equalsIgnoreCase("give")) {
                    try { Integer.parseInt(args[2]); } catch (NumberFormatException exception) {
                        sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.InvalidAmount")
                                .replace("%maximum%", df.format(plugin.getConfig().getDouble("Economy.MaximumAmount")))));
                        return true;
                    }

                    if (economyManagement.isOverMaximum(Double.parseDouble(args[2]) + economyManagement.getMoney(target))) {
                        sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.InvalidAmount")
                                .replace("%maximum%", df.format(plugin.getConfig().getDouble("Economy.MaximumAmount")))));
                        return true;
                    }
                    economyManagement.addMoney(target, Double.parseDouble(args[2]));

                    sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.Format.Give")
                            .replace("%target%", yoTarget.getDisplayName())
                            .replace("%amount%", df.format(Double.parseDouble(args[2])))));

                    if (target.isOnline()) {
                        Bukkit.getPlayer(target.getName()).sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.FormatTarget")
                                .replace("%balance%", df.format(economyManagement.getMoney(target)))));
                    }
                } else if (args[0].equalsIgnoreCase("take")) {
                    try { Integer.parseInt(args[2]); } catch (NumberFormatException exception) {
                        sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.InvalidAmount")
                                .replace("%maximum%", df.format(plugin.getConfig().getDouble("Economy.MaximumAmount")))));
                        return true;
                    }

                    economyManagement.removeMoney(target, Double.parseDouble(args[2]));

                    sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.Format.Take")
                            .replace("%target%", yoTarget.getDisplayName())
                            .replace("%amount%", df.format(Double.parseDouble(args[2])))));

                    if (target.isOnline()) {
                        Bukkit.getPlayer(target.getName()).sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.FormatTarget")
                                .replace("%balance%", df.format(economyManagement.getMoney(target)))));
                    }
                } else {
                    sender.sendMessage(Utils.translate(plugin.getConfig().getString("Economy.Command.IncorrectUsage")));
                    return true;
                }

                break;
        }

        return true;
    }
}
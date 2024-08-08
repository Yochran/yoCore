package me.yochran.yocore.commands.staff;

import me.yochran.yocore.utils.Utils;
import me.yochran.yocore.yoCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderChestCommand implements CommandExecutor {

    private final yoCore plugin;

    public EnderChestCommand() {
        plugin = yoCore.getPlugin(yoCore.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("EnderChest.MustBePlayer")));
            return true;
        }

        if (!sender.hasPermission("yocore.echest")) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("EnderChest.NoPermission")));
            return true;
        }

        if (args.length > 1) {
            sender.sendMessage(Utils.translate(plugin.getConfig().getString("EnderChest.IncorrectUsage")));
            return true;
        }

        if (args.length == 0) {
            ((Player) sender).openInventory(((Player) sender).getEnderChest());
        } else {
            if (!sender.hasPermission("yocore.echest.others")) {
                sender.sendMessage(Utils.translate(plugin.getConfig().getString("EnderChest.NoPermission")));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Utils.translate(plugin.getConfig().getString("EnderChest.InvalidPlayer")));
                return true;
            }

            ((Player) sender).openInventory(target.getEnderChest());
        }

        return true;
    }
}

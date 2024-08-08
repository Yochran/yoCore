package me.yochran.yocore.commands;

import me.yochran.yocore.utils.Utils;
import me.yochran.yocore.yoCore;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    private final yoCore plugin;

    public SetSpawnCommand() {
        this.plugin = yoCore.getPlugin(yoCore.class);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.translate(this.plugin.getConfig().getString("Spawn.MustBePlayer")));
            return true;
        }

        Location location = ((Player) sender).getLocation();

        plugin.getConfig().set("Spawn.World", location.getWorld().getName());
        plugin.getConfig().set("Spawn.X", location.getX());
        plugin.getConfig().set("Spawn.Y", location.getY());
        plugin.getConfig().set("Spawn.Z", location.getZ());
        plugin.getConfig().set("Spawn.Yaw", location.getYaw());
        plugin.getConfig().set("Spawn.Pitch", location.getPitch());
        plugin.saveConfig();

        sender.sendMessage(Utils.translate(plugin.getConfig().getString("Spawn.SetSpawnMessage")));

        return true;
    }
}

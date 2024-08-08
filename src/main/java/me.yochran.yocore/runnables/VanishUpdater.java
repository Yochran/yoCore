package me.yochran.yocore.runnables;

import me.yochran.yocore.yoCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class VanishUpdater extends BukkitRunnable {

    private final yoCore plugin;

    public VanishUpdater() {
        plugin = yoCore.getPlugin(yoCore.class);
    }

    @Override
    public void run() {
        for (Player staff : Bukkit.getOnlinePlayers()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (plugin.vanished_players.contains(staff.getUniqueId())) {
                    if (!player.hasPermission("yocore.vanish"))
                        player.hidePlayer(staff);
                }

                else player.showPlayer(staff);
            }
        }
    }
}

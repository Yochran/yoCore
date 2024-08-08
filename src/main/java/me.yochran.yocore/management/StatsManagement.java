package me.yochran.yocore.management;

import me.yochran.yocore.yoCore;
import org.bukkit.OfflinePlayer;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class StatsManagement {

    private final yoCore plugin;

    public StatsManagement() {
        plugin = yoCore.getPlugin(yoCore.class);
    }

    public void setupPlayer(OfflinePlayer target) {
        plugin.statsData.config.set(target.getUniqueId().toString() + ".Name", target.getName());
        plugin.statsData.config.set(target.getUniqueId().toString() + ".Kills", 0);
        plugin.statsData.config.set(target.getUniqueId().toString() + ".Deaths", 0);
        plugin.statsData.config.set(target.getUniqueId().toString() + ".KDR", 0);
        plugin.statsData.config.set(target.getUniqueId().toString() + ".Streak", 0);
        plugin.statsData.saveData();
    }

    public boolean isInitialized(OfflinePlayer target) {
        return plugin.statsData.config.contains(target.getUniqueId().toString());
    }

    public boolean statsAreEnabled() {
        return plugin.getConfig().getBoolean("Stats.Enabled");
    }

    public int getKills(OfflinePlayer target) {
        return plugin.statsData.config.getInt(target.getUniqueId().toString() + ".Kills");
    }

    public int getDeaths(OfflinePlayer target) {
        return plugin.statsData.config.getInt(target.getUniqueId().toString() + ".Deaths");
    }

    public double getKDR(OfflinePlayer target) {
        return plugin.statsData.config.getDouble(target.getUniqueId().toString() + ".KDR");
    }

    public int getStreak(OfflinePlayer target) {
        return plugin.statsData.config.getInt(target.getUniqueId().toString() + ".Streak");
    }

    public Map<String, String> getAllStats(OfflinePlayer target) {
        DecimalFormat df = new DecimalFormat("###,###.##");
        Map<String, String> stats = new HashMap<>();

        String kills = df.format(getKills(target));
        String deaths = df.format(getDeaths(target));
        String kdr = df.format(getKDR(target));
        String streak = df.format(getStreak(target));

        stats.put("Kills", kills);
        stats.put("Deaths", deaths);
        stats.put("KDR", kdr);
        stats.put("Streak", streak);

        return stats;
    }

    public void addKill(OfflinePlayer target) {
        plugin.statsData.config.set(target.getUniqueId().toString() + ".Kills", getKills(target) + 1);
        updateKDR(target);
        plugin.statsData.saveData();
    }

    public void addDeath(OfflinePlayer target) {
        plugin.statsData.config.set(target.getUniqueId().toString() + ".Deaths", getDeaths(target) + 1);
        updateKDR(target);
        plugin.statsData.saveData();
    }

    public void updateKDR(OfflinePlayer target) {
        int kills = getKills(target);
        int deaths = getDeaths(target);

        int alternateKills;
        int alternateDeaths;

        if (deaths == 0) {
            alternateDeaths = 1;
        } else {
            alternateDeaths = deaths;
        }

        alternateKills = kills;

        double kdr = (double) alternateKills / (double) alternateDeaths;

        plugin.statsData.config.set(target.getUniqueId().toString() + ".KDR", kdr);
        plugin.statsData.saveData();
    }

    public void addToStreak(OfflinePlayer target) {
        plugin.statsData.config.set(target.getUniqueId().toString() + ".Streak", getStreak(target) + 1);
        plugin.statsData.saveData();
    }

    public void endStreak(OfflinePlayer target) {
        plugin.statsData.config.set(target.getUniqueId().toString() + ".Streak", 0);
        plugin.statsData.saveData();
    }

    public boolean hasStreak(OfflinePlayer target) {
        if (getStreak(target) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean streakShouldBeAnnounced(int streak) {
        if (streak >= plugin.getConfig().getInt("Stats.MinimumStreakEndBroadcast")) {
            return true;
        } else {
            return false;
        }
    }

    public void resetPlayer(OfflinePlayer target) {
        plugin.statsData.config.set(target.getUniqueId().toString() + ".Kills", 0);
        plugin.statsData.config.set(target.getUniqueId().toString() + ".Deaths", 0);
        plugin.statsData.config.set(target.getUniqueId().toString() + ".KDR", 0.0);
        plugin.statsData.config.set(target.getUniqueId().toString() + ".Streak", 0);
        plugin.statsData.saveData();
    }
}

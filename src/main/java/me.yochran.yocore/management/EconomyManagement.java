package me.yochran.yocore.management;

import me.yochran.yocore.yoCore;
import org.bukkit.OfflinePlayer;

import java.util.Map;

public class EconomyManagement {

    private final yoCore plugin;

    public EconomyManagement() {
        plugin = yoCore.getPlugin(yoCore.class);
    }

    public void setupPlayer(OfflinePlayer target) {
        plugin.economyData.config.set(target.getUniqueId().toString() + ".Name", target.getName());
        plugin.economyData.config.set(target.getUniqueId().toString() + ".Balance", plugin.getConfig().getDouble("Economy.StartingAmount"));
        plugin.economyData.config.set(target.getUniqueId().toString() + ".Bountied", false);
        plugin.economyData.config.set(target.getUniqueId().toString() + ".Bounty", null);
        plugin.economyData.saveData();
    }

    public boolean isInitialized(OfflinePlayer target) {
        return plugin.economyData.config.contains(target.getUniqueId().toString());
    }

    public void resetPlayer(OfflinePlayer target) {
        plugin.economyData.config.set(target.getUniqueId().toString() + ".Balance", plugin.getConfig().getDouble("Economy.StartingAmount"));
        plugin.economyData.config.set(target.getUniqueId().toString() + ".Bountied", false);
        plugin.economyData.config.set(target.getUniqueId().toString() + ".Bounty", null);
        plugin.economyData.saveData();
    }

    public boolean economyIsEnabled() {
        return plugin.getConfig().getBoolean("Economy.Enabled");
    }

    public double getMoney(OfflinePlayer target) {
        return plugin.economyData.config.getDouble(target.getUniqueId().toString() + ".Balance");
    }

    public void addMoney(OfflinePlayer target, double amount) {
        plugin.economyData.config.set(target.getUniqueId().toString() + ".Balance", getMoney(target) + amount);
        plugin.economyData.saveData();
    }

    public void removeMoney(OfflinePlayer target, double amount) {
        if (getMoney(target) - amount < 0) {
            plugin.economyData.config.set(target.getUniqueId().toString() + ".Balance", plugin.getConfig().getString("Economy.StartingAmount"));
            plugin.economyData.saveData();
            return;
        }

        plugin.economyData.config.set(target.getUniqueId().toString() + ".Balance", getMoney(target) - amount);
        plugin.economyData.saveData();
    }

    public boolean hasEnoughMoney(OfflinePlayer target, double amount) {
        return getMoney(target) >= amount;
    }

    public boolean isBountied(OfflinePlayer target) {
        return plugin.economyData.config.getBoolean(target.getUniqueId().toString() + ".Bountied");
    }

    public void setBounty(OfflinePlayer executor, OfflinePlayer target, double amount) {
        removeMoney(executor, amount);

        if (!isBountied(target)) {
            removeBounty(target);
        }

        plugin.economyData.config.set(target.getUniqueId().toString() + ".Bountied", true);
        plugin.economyData.config.set(target.getUniqueId().toString() + ".Bounty.Amount", amount);
        plugin.economyData.config.set(target.getUniqueId().toString() + ".Bounty.Executor", executor.getUniqueId().toString());
        plugin.economyData.saveData();
    }

    public void increaseBounty(OfflinePlayer executor, OfflinePlayer target, double amount) {
        removeMoney(executor, amount);

        plugin.economyData.config.set(target.getUniqueId().toString() + ".Bounty.Amount", amount + getBountyAmount(target));
        plugin.economyData.saveData();
    }

    public void removeBounty(OfflinePlayer target) {
        plugin.economyData.config.set(target.getUniqueId().toString() + ".Bountied", false);
        plugin.economyData.config.set(target.getUniqueId().toString() + ".Bounty", null);
        plugin.economyData.saveData();
    }

    public void claimBounty(OfflinePlayer target, OfflinePlayer claimer, double amount) {
        addMoney(claimer, amount);
        removeBounty(target);
    }

    public double getBountyAmount(OfflinePlayer target) {
        return plugin.economyData.config.getDouble(target.getUniqueId().toString() + ".Bounty.Amount");
    }

    public String getBountyExecutor(OfflinePlayer target) {
        return plugin.economyData.config.getString(target.getUniqueId().toString() + ".Bounty.Executor");
    }

    public boolean isOverMaximum(double amount) {
        return amount > plugin.getConfig().getDouble("Economy.MaximumAmount");
    }

    public boolean isUnderPayMinimum(double amount) {
        return amount < plugin.getConfig().getDouble("Pay.MinimumAmount");
    }

    public boolean isUnderBountyMinimum(double amount) {
        return amount < plugin.getConfig().getDouble("Bounty.MinimumAmount");
    }

    public boolean bountyIsEnabled() {
        return plugin.getConfig().getBoolean("Bounty.Enabled");
    }

    public boolean moneyPerKillEnabled() {
        return plugin.getConfig().getBoolean("Economy.MoneyPerKill.Enabled");
    }

    public double getMoneyPerKill() {
        double highest = plugin.getConfig().getDouble("Economy.MoneyPerKill.Highest");
        double lowest = plugin.getConfig().getDouble("Economy.MoneyPerKill.Lowest");

        double range = (highest - lowest) + 1;

        return (Math.random() * range) + lowest;
    }
}

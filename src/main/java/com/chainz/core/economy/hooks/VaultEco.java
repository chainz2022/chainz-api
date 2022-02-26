package com.chainz.core.economy.hooks;

import com.chainz.core.ChainZAPI;
import com.chainz.core.Core;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

import java.util.List;

public class VaultEco implements Economy {
    public static void register() {
        Bukkit.getServer().getServicesManager().register(Economy.class, new VaultEco(), Core.core, ServicePriority.Highest);
    }

    public static boolean isActive() {
        Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
        return vault != null;
    }

    public boolean isEnabled() {
        Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
        return vault != null && vault.isEnabled();
    }

    public String getName() {
        return "ChainZAPI";
    }

    public boolean hasBankSupport() {
        return false;
    }

    public int fractionalDigits() {
        return 2;
    }

    public String format(double amount) {
        return amount + "";
    }

    public String currencyNamePlural() {
        return "Gems";
    }

    public String currencyNameSingular() {
        return "Gem";
    }

    public boolean hasAccount(String s) {
        return true;
    }

    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return true;
    }

    public boolean hasAccount(String s, String s1) {
        return true;
    }

    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return true;
    }

    public double getBalance(String name) {
        return ChainZAPI.getEconomyManager().getCoins(Bukkit.getPlayer(name).getUniqueId());
    }

    public double getBalance(OfflinePlayer offlinePlayer) {
        return ChainZAPI.getEconomyManager().getCoins(offlinePlayer.getUniqueId());
    }

    public double getBalance(String name, String world) {
        return getBalance(name);
    }

    public double getBalance(OfflinePlayer offlinePlayer, String world) {
        return getBalance(offlinePlayer);
    }

    public boolean has(String playerName, double amount) {
        if (amount < 0.0D)
            throw new IllegalArgumentException("Amount can not be less than 0.");
        double balance = getBalance(playerName);
        return (amount >= balance);
    }

    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        if (amount < 0.0D)
            throw new IllegalArgumentException("Amount can not be less than 0.");
        double balance = getBalance(offlinePlayer);
        return (amount >= balance);
    }

    public boolean has(String playerName, String world, double amount) {
        return has(playerName, amount);
    }

    public boolean has(OfflinePlayer offlinePlayer, String world, double amount) {
        return has(offlinePlayer, amount);
    }

    public EconomyResponse withdrawPlayer(String name, double amount) {
        if (amount < 0.0D)
            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds!");

        Double actual = ChainZAPI.getEconomyManager().getCoins(Bukkit.getPlayer(name).getUniqueId());
        if (actual >= amount)
            return new EconomyResponse(0.0D, actual, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");

        ChainZAPI.getEconomyManager().removeCoins(Bukkit.getPlayer(name).getUniqueId(), amount);
        return new EconomyResponse(amount, (actual - amount), EconomyResponse.ResponseType.SUCCESS, null);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer offpl, double amount) {
        if (amount < 0.0D)
            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds!");

        Double actual = ChainZAPI.getEconomyManager().getCoins(offpl.getUniqueId());
        if (actual >= amount)
            return new EconomyResponse(0.0D, actual, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");

        ChainZAPI.getEconomyManager().removeCoins(offpl.getUniqueId(), amount);
        return new EconomyResponse(amount, (actual - amount), EconomyResponse.ResponseType.SUCCESS, null);
    }

    public EconomyResponse withdrawPlayer(String player, String world, double amount) {
        return withdrawPlayer(player, amount);
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String world, double amount) {
        return withdrawPlayer(offlinePlayer, amount);
    }

    public EconomyResponse depositPlayer(String name, double amount) {
        if (amount < 0.0D)
            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot desposit negative funds!");

        Double actual = ChainZAPI.getEconomyManager().getCoins(Bukkit.getPlayer(name).getUniqueId());
        ChainZAPI.getEconomyManager().addCoins(Bukkit.getPlayer(name).getUniqueId(), amount, true);
        return new EconomyResponse(amount, (actual + (int) amount), EconomyResponse.ResponseType.SUCCESS, null);
    }

    public EconomyResponse depositPlayer(OfflinePlayer offpl, double amount) {
        if (amount < 0.0D)
            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot desposit negative funds!");

        Double actual = ChainZAPI.getEconomyManager().getCoins(offpl.getUniqueId());
        ChainZAPI.getEconomyManager().addCoins(offpl.getUniqueId(), amount, true);
        return new EconomyResponse(amount, (actual + (int) amount), EconomyResponse.ResponseType.SUCCESS, null);
    }

    public EconomyResponse depositPlayer(String player, String world, double amount) {
        return depositPlayer(player, amount);
    }

    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    public List<String> getBanks() {
        return null;
    }

    public boolean createPlayerAccount(String s) {
        return true;
    }

    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return true;
    }

    public boolean createPlayerAccount(String s, String s1) {
        return true;
    }

    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return true;
    }

    public EconomyResponse depositPlayer(OfflinePlayer arg0, String arg1, double arg2) {
        return null;
    }
}

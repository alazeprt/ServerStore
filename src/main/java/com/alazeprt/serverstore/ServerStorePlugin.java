package com.alazeprt.serverstore;

import com.alazeprt.serverstore.commands.MainCommand;
import com.alazeprt.serverstore.events.ServerStoreEvent;
import com.alazeprt.serverstore.events.StoreEvent;
import com.alazeprt.serverstore.papi.ServerStoreExpansion;
import com.alazeprt.serverstore.utils.DataUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServerStorePlugin extends JavaPlugin {

    public static BigDecimal money;

    public static FileConfiguration store;

    public static FileConfiguration config;

    public static FileConfiguration data;

    public static Economy economy;

    public static final List<StoreEvent> eventList = new ArrayList<>();

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        getLogger().info("Enabling ServerStore");
        getLogger().info("Setting up economy system...");
        if (!setupEconomy() ) {
            getLogger().severe("Vault dependency not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Setting up data & configuration file");
        File configFile = new File(getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            saveResource("config.yml", false);
        }
        File dataFile = new File(getDataFolder(), "data.yml");
        if(!dataFile.exists()) {
            saveResource("data.yml", false);
        }
        File storeFile = new File(getDataFolder(), "store.yml");
        if(!storeFile.exists()) {
            saveResource("store.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        store = YamlConfiguration.loadConfiguration(storeFile);
        data = YamlConfiguration.loadConfiguration(dataFile);
        if(data.getString("money") == null) {
            data.set("money", new BigDecimal(config.getString("initial")).intValue());
            try {
                data.save(dataFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        money = new BigDecimal(data.getString("money"));
        getLogger().info("Setting up command");
        Objects.requireNonNull(getCommand("store")).setExecutor(new MainCommand());
        getLogger().info("Starting thread for data reset");
        Thread thread = new Thread(() -> {
            Bukkit.getScheduler().runTaskTimer(this, () -> {
                data.set("time", data.getInt("time") + 1);
                if(store.getBoolean("sell.reset_limit") && (data.getInt("time") / 60.0) % store.getInt("sell.reset_time") == 0) {
                    DataUtils.resetSellData();
                    Bukkit.getScheduler().runTask(this, () -> getServer().broadcastMessage("§a[ServerStore] 重置物品收购数量!"));
                }
                if(store.getBoolean("buy.reset_limit") && (data.getInt("time") / 60.0) % store.getInt("buy.reset_time") == 0) {
                    DataUtils.resetBuyData();
                    Bukkit.getScheduler().runTask(this, () -> getServer().broadcastMessage("§a[ServerStore] 重置物品购买数量!"));
                }
            }, 0, 1200);
        });
        thread.start();
        getLogger().info("Enabling events");
        addEvent(new ServerStoreEvent());
        eventList.forEach(StoreEvent::onEnable);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new ServerStoreExpansion().register();
        }
        getLogger().info("ServerStore is ready! (" + (System.currentTimeMillis() - start) + " ms)");
    }

    @Override
    public void onDisable() {
        long start = System.currentTimeMillis();
        getLogger().info("Disabling ServerStore");
        try {
            data.set("money", money.doubleValue());
            data.save(new File(getDataFolder(), "data.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getLogger().info("Disabling events");
        eventList.forEach(StoreEvent::onDisable);
        getLogger().info("ServerStore is disabled! (" + (System.currentTimeMillis() - start) + " ms)");
    }

    public static void addEvent(StoreEvent event) {
        eventList.add(event);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }
}

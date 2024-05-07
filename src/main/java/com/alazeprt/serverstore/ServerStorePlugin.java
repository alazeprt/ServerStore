package com.alazeprt.serverstore;

import com.alazeprt.serverstore.commands.MainCommand;
import com.alazeprt.serverstore.events.ServerStoreEvent;
import com.alazeprt.serverstore.events.StoreEvent;
import net.milkbowl.vault.economy.Economy;
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
        if (!setupEconomy() ) {
            getLogger().severe("Vault dependency not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
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
        Objects.requireNonNull(getCommand("store")).setExecutor(new MainCommand());
        addEvent(new ServerStoreEvent());
        eventList.forEach(StoreEvent::onEnable);
    }

    @Override
    public void onDisable() {
        try {
            data.set("money", money.doubleValue());
            data.save(new File(getDataFolder(), "data.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        eventList.forEach(StoreEvent::onDisable);
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

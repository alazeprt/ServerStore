package com.alazeprt.serverstore.events;

import java.math.BigDecimal;

import static com.alazeprt.serverstore.ServerStorePlugin.config;
import static com.alazeprt.serverstore.ServerStorePlugin.money;
import static com.alazeprt.serverstore.utils.StatusUtils.status;
import static org.bukkit.Bukkit.getServer;

public class ServerStoreEvent implements StoreEvent {

    @Override
    public void onEnable() {
        if(money.compareTo(new BigDecimal(config.getString("stop.money"))) < 0) {
            onStop();
        }
    }

    @Override
    public void onDisable() {}

    @Override
    public void onStop() {
        if(config.getBoolean("stop.enable")) {
            status = false;
            if(config.getBoolean("stop.broadcast.enable")) {
                getServer().broadcastMessage(config.getString("stop.broadcast.message").replace("&", "§"));
            }
        }
    }

    @Override
    public void onRestore() {
        if(config.getBoolean("restore.enable")) {
           status = true;
           if(config.getBoolean("restore.broadcast.enable")) {
               getServer().broadcastMessage(config.getString("restore.broadcast.message").replace("&", "§"));
           }
        }
    }

    @Override
    public void onSell() {
        System.out.println("Sell Event: " + money.doubleValue() + " ? " + config.getString("stop.money"));
        if(money.compareTo(new BigDecimal(config.getString("stop.money"))) < 0) {
            onStop();
        }
    }

    @Override
    public void onBuy() {
        if(!status && money.compareTo(new BigDecimal(config.getString("restore.money"))) > 0) {
            onRestore();
        }
    }
}

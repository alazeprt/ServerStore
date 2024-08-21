package com.alazeprt.serverstore.papi;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import static com.alazeprt.serverstore.ServerStorePlugin.data;
import static com.alazeprt.serverstore.ServerStorePlugin.store;

public class ServerStoreExpansion extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "serverstore";
    }

    @Override
    public String getAuthor() {
        return "alazeprt";
    }

    @Override
    public String getVersion() {
        return "1.2";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.startsWith("buy.") && store.contains("buy." + params)) {
            return data.getString("buy." + params + ".players." + player.getName()) == null ?
                    "0" : data.getString("buy." + params + ".players." + player.getName());
        } else if (params.startsWith("sell.") && store.contains("sell." + params)) {
            return data.getString("sell." + params + ".players." + player.getName()) == null ?
                    "0" : data.getString("sell." + params + ".players." + player.getName());
        }
        return null;
    }
}

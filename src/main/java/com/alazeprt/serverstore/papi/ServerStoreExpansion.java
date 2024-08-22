package com.alazeprt.serverstore.papi;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import java.math.BigDecimal;

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
        if (params.startsWith("buy_") && params.endsWith("_amount") && store.contains("buy." + params.substring(4, params.length() - 7))) {
            String project = params.substring(4, params.length() - 7);
            return data.getString("buy." + project + ".players." + player.getName()) == null ?
                    "0" : data.getString("buy." + project + ".players." + player.getName());
        } else if (params.startsWith("sell_") && params.endsWith("_amount") && store.contains("sell." + params.substring(5, params.length() - 7))) {
            String project = params.substring(5, params.length() - 7);
            return data.getString("sell." + project + ".players." + player.getName()) == null ?
                    "0" : data.getString("sell." + project + ".players." + player.getName());
        } else if (params.startsWith("buy_") && params.endsWith("_player_limit") && store.contains("buy." + params.substring(4, params.length() - 13))) {
            String project = params.substring(4, params.length() - 13);
            return store.getString("buy." + project + ".player_limit");
        } else if (params.startsWith("sell_") && params.endsWith("_player_limit") && store.contains("sell." + params.substring(5, params.length() - 13))) {
            String project = params.substring(5, params.length() - 13);
            return store.getString("sell." + project + ".player_limit");
        } else if (params.startsWith("buy_") && params.endsWith("_total_limit") && store.contains("buy." + params.substring(4, params.length() - 12))) {
            String project = params.substring(4, params.length() - 12);
            return store.getString("buy." + project + ".total_limit") == null ?
                    "无限" : store.getString("buy." + project + ".total_limit");
        } else if (params.startsWith("sell_") && params.endsWith("_total_limit") && store.contains("sell." + params.substring(5, params.length() - 12))) {
            String project = params.substring(5, params.length() - 12);
            return store.getString("sell." + project + ".total_limit") == null ?
                    "无限" : store.getString("sell." + project + ".total_limit");
        } else if (params.startsWith("buy_") && params.endsWith("_player_buy") && store.contains("buy." + params.substring(4, params.length() - 11))) {
            String project = params.substring(4, params.length() - 11);
            return data.getString("buy." + project + ".players." + player.getName()) == null ?
                    "0" : data.getString("buy." + project + ".players." + player.getName());
        } else if (params.startsWith("sell_") && params.endsWith("_player_sell") && store.contains("sell." + params.substring(5, params.length() - 12))) {
            String project = params.substring(5, params.length() - 12);
            return data.getString("sell." + project + ".players." + player.getName()) == null ?
                    "0" : data.getString("sell." + project + ".players." + player.getName());
        } else if (params.startsWith("buy_") && params.endsWith("_total_buy") && store.contains("buy." + params.substring(4, params.length() - 10))) {
            String project = params.substring(4, params.length() - 10);
            BigDecimal bigDecimal = new BigDecimal(0);
            if(data.getConfigurationSection("buy." + project + ".players") == null) {
                return "0";
            }
            for(String key : data.getConfigurationSection("buy." + project + ".players").getKeys(false)) {
                bigDecimal = bigDecimal.add(new BigDecimal(data.getString("buy." + project + ".players." + key)));
            }
            return bigDecimal.toString();
        } else if (params.startsWith("sell_") && params.endsWith("_total_sell") && store.contains("sell." + params.substring(5, params.length() - 11))) {
            String project = params.substring(5, params.length() - 11);
            BigDecimal bigDecimal = new BigDecimal(0);
            if(data.getConfigurationSection("sell." + project + ".players") == null) {
                return "0";
            }
            for(String key : data.getConfigurationSection("sell." + project + ".players").getKeys(false)) {
                bigDecimal = bigDecimal.add(new BigDecimal(data.getString("sell." + project + ".players." + key)));
            }
            return bigDecimal.toString();
        }
        return null;
    }
}

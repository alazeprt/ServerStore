package com.alazeprt.serverstore.utils;

import java.math.BigDecimal;

import static com.alazeprt.serverstore.ServerStorePlugin.data;
import static com.alazeprt.serverstore.ServerStorePlugin.store;

public class DataUtils {
    public static void addSellAmount(String project, String player, BigDecimal bigDecimal) {
        BigDecimal player_amount = new BigDecimal(data.getString("sell." + project + ".players." + player) == null ?
                "0" : data.getString("sell." + project + ".players." + player));
        data.set("sell." + project + ".players." + player, bigDecimal.add(player_amount).intValue());
    }

    public static void addBuyAmount(String project, String player, BigDecimal bigDecimal) {
        BigDecimal player_amount = new BigDecimal(data.getString("buy." + project + ".players." + player) == null ?
                "0" : data.getString("buy." + project + ".players." + player));
        data.set("buy." + project + ".players." + player, bigDecimal.add(player_amount).intValue());
    }

    public static boolean sellPerPlayerLimit(String project, String player, BigDecimal addAmount) {
        if(!store.getBoolean("sell.player_limit")) {
            return true;
        }
        BigDecimal player_amount = new BigDecimal(data.getString("sell." + project + ".players." + player) == null ?
                "0" : data.getString("sell." + project + ".players." + player));
        player_amount = player_amount.add(addAmount);
        System.out.println("Player Sell Origin: " + data.getString("sell." + project + ".players." + player));
        System.out.println("Sell: " + player_amount.doubleValue() + " + " + addAmount.doubleValue() + " ? " + store.getString("sell." + project + ".player_limit"));
        return player_amount.compareTo(new BigDecimal(store.getString("sell." + project + ".player_limit"))) <= 0;
    }

    public static boolean buyPerPlayerLimit(String project, String player, BigDecimal addAmount) {
        if(!store.getBoolean("buy.player_limit")) {
            return true;
        }
        BigDecimal player_amount = new BigDecimal(data.getString("buy." + project + ".players." + player) == null ?
                "0" : data.getString("buy." + project + ".players." + player));
        System.out.println(addAmount.doubleValue());
        player_amount = player_amount.add(addAmount);
        System.out.println("Buy: " + player_amount.doubleValue() + " + " + addAmount.doubleValue() + " ? " + store.getString("buy." + project + ".player_limit"));
        return player_amount.compareTo(new BigDecimal(store.getString("buy." + project + ".player_limit"))) <= 0;
    }

    public static boolean sellTotalLimit(String project, BigDecimal addAmount) {
        if(!store.getBoolean("sell.total_limit")) {
            return true;
        }
        if(data.getConfigurationSection("sell." + project + ".players") == null) {
            return true;
        }
        for(String key : data.getConfigurationSection("sell." + project + ".players").getKeys(false)) {
            addAmount = addAmount.add(new BigDecimal(data.getString("sell." + project + ".players." + key)));
        }
        return addAmount.compareTo(new BigDecimal(store.getString("sell." + project + ".total_limit"))) <= 0;
    }

    public static boolean buyTotalLimit(String project, BigDecimal addAmount) {
        if(!store.getBoolean("buy.total_limit")) {
            return true;
        }
        if(data.getConfigurationSection("buy." + project + ".players") == null) {
            return true;
        }
        for(String key : data.getConfigurationSection("buy." + project + ".players").getKeys(false)) {
            addAmount = addAmount.add(new BigDecimal(data.getString("buy." + project + ".players." + key)));
        }
        return addAmount.compareTo(new BigDecimal(store.getString("buy." + project + ".total_limit"))) <= 0;
    }
}

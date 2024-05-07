package com.alazeprt.serverstore.utils;

import static com.alazeprt.serverstore.ServerStorePlugin.config;

public class StatusUtils {

    public static boolean status = true;

    public static boolean sellStatus() {
        if(!status && (config.getString("stop.type").equalsIgnoreCase("STOP_SELL") ||
                config.getString("stop.type").equalsIgnoreCase("STOP_BOTH") ||
                config.getString("stop.type").equalsIgnoreCase("STOP_ALL"))) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean buyStatus() {
        if(!status && (config.getString("stop.type").equalsIgnoreCase("STOP_BUY") ||
                config.getString("stop.type").equalsIgnoreCase("STOP_BOTH") ||
                config.getString("stop.type").equalsIgnoreCase("STOP_ALL"))) {
            return false;
        } else {
            return true;
        }
    }
}

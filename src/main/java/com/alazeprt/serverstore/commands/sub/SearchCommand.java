package com.alazeprt.serverstore.commands.sub;

import com.alazeprt.serverstore.commands.SubCommand;
import org.bukkit.command.CommandSender;

import static com.alazeprt.serverstore.ServerStorePlugin.store;

public class SearchCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("serverstore.search")) {
            new NoPermissionCommand().execute(sender, null);
            return;
        }
        sender.sendMessage("§9收购物品搜索结果:");
        for(String key : store.getConfigurationSection("sell").getKeys(false)) {
            if(!key.contains(args[1]) && !store.getString("sell." + key + ".item").contains(args[1])) {
                continue;
            }
            if(key.equalsIgnoreCase("player_limit") || key.equalsIgnoreCase("total_limit")) {
                continue;
            }
            sender.sendMessage("§9" + key + ": ");
            sender.sendMessage("  §7物品: " + store.getString("sell." + key + ".item"));
            sender.sendMessage("  §7数量: " + store.getString("sell." + key + ".amount"));
            sender.sendMessage("  §7价格: " + store.getString("sell." + key + ".price"));
            if(store.getBoolean("sell.player_limit")) {
                sender.sendMessage("  §7单个玩家限购: " + store.getString("sell." + key + ".player_limit"));
            }
            if(store.getBoolean("sell.total_limit")) {
                sender.sendMessage("  §7服务器总限购: " + store.getString("sell." + key + ".total_limit"));
            }
        }
        sender.sendMessage("§c购买物品搜索结果:");
        for(String key : store.getConfigurationSection("buy").getKeys(false)) {
            if(!key.contains(args[1]) && !store.getString("buy." + key + ".item").contains(args[1])) {
                continue;
            }
            if(key.equalsIgnoreCase("player_limit") || key.equalsIgnoreCase("total_limit")) {
                continue;
            }
            sender.sendMessage("§c" + key + ": ");
            sender.sendMessage("  §7物品: " + store.getString("buy." + key + ".item"));
            sender.sendMessage("  §7数量: " + store.getString("buy." + key + ".amount"));
            sender.sendMessage("  §7价格: " + store.getString("buy." + key + ".price"));
            if(store.getBoolean("buy.player_limit")) {
                sender.sendMessage("  §7单个玩家限购: " + store.getString("buy." + key + ".player_limit"));
            }
            if(store.getBoolean("buy.total_limit")) {
                sender.sendMessage("  §7服务器总限购: " + store.getString("buy." + key + ".total_limit"));
            }
        }
    }
}

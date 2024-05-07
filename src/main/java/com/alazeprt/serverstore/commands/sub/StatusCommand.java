package com.alazeprt.serverstore.commands.sub;

import com.alazeprt.serverstore.commands.SubCommand;
import com.alazeprt.serverstore.utils.StatusUtils;
import org.bukkit.command.CommandSender;

import static com.alazeprt.serverstore.ServerStorePlugin.*;

public class StatusCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("serverstore.status")) {
            new NoPermissionCommand().execute(sender, null);
            return;
        }
        sender.sendMessage("§aServerStore 状态");
        sender.sendMessage("§e收购商店: " + (StatusUtils.sellStatus() ? "§a开启" : "§c关闭"));
        sender.sendMessage("§e出售商店: " + (StatusUtils.buyStatus() ? "§a开启" : "§c关闭"));
        if(config.getBoolean("status.money")) {
            sender.sendMessage("§e商店存款: §6" + money);
        }
        if(config.getBoolean("status.stop")) {
            if(config.getBoolean("stop.enable")) {
                sender.sendMessage("§e停止收购/出售所需的存款: §6" + config.getString("stop.money"));
            } else {
                sender.sendMessage("§e停止收购/出售所需的存款: §c永久不会停止");
            }
        }
        if(config.getBoolean("status.restore")) {
            if(config.getBoolean("restore.enable")) {
                sender.sendMessage("§e恢复收购/出售所需的存款: §6" + config.getString("restore.money"));
            } else {
                sender.sendMessage("§e恢复收购/出售所需的存款: §c永久不会恢复");
            }
        }
    }
}

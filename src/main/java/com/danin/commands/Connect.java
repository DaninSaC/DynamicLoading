package com.danin.commands;

import com.danin.utils.PluginMessage;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Connect implements CommandExecutor{

    private PluginMessage pluginMessage = new PluginMessage();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 1){
                pluginMessage.getServerIP(player, args[0]);
            }else{
                player.sendMessage(ChatColor.RED+"Usage: /connect <server>");
            }
            
        }
        return false;
    }
}

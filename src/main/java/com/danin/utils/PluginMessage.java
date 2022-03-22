package com.danin.utils;

import com.danin.main.Main;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginMessage implements PluginMessageListener {

    private Main plugin = Main.getPlugin();

    //private static String[] serverList;

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!channel.equals("BungeeCord"))return;
        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String subchannel = input.readUTF();
        
        if(subchannel.equals("GetServers")){
            //serverList = input.readUTF().split(",");
        }

        if(subchannel.equals("ServerIP")){
            String servername = input.readUTF();
            String serverip = input.readUTF();
            int serverport = input.readUnsignedShort();

            player.sendMessage(ChatColor.GREEN+"Sending to server...");

            new Thread(new HandleConnection(player, servername, serverip, serverport)).start();
        }

        
    }

    public void getServerIP(Player player, String server){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("ServerIP");
        output.writeUTF(server);

        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

    public void connectPlayer(Player player, String server){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(server);

        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

    public void getServer(Player player){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("GetServers");

        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }
    
}

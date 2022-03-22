package com.danin.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HandleConnection implements Runnable{
    private volatile Player player;
    private volatile String servername;
    private volatile String serverip;
    private volatile int serverport;

    private PluginMessage pluginMessage = new PluginMessage();

    public HandleConnection(Player player, String servername, String serverip, int serverport){
        this.player = player;
        this.servername = servername;
        this.serverip = serverip;
        this.serverport = serverport;
    }

    public void run(){
        ServerStatus serverStatus = new ServerStatus();
        
        switch(serverStatus.getStatus(serverip, serverport)){
            case 0: break;
            case -1: player.sendMessage(ChatColor.RED+"Failed to send you to the server"); Bukkit.getLogger().warning("Server timed out"); return;
            case -2: player.sendMessage(ChatColor.RED+"Failed to send you to the server"); Bukkit.getLogger().warning("An IOEXception occured getting server status"); return;
            default: pluginMessage.connectPlayer(player, servername); return;
        }
        
        switch(connectToDLS()){
            case 0: break;
            case 1: player.sendMessage(ChatColor.RED+"Failed to send you to the server"); Bukkit.getLogger().warning("DLS: Failed validating server info"); return;
            case 2: player.sendMessage(ChatColor.RED+"Failed to send you to the server"); Bukkit.getLogger().warning("DLS: Went out of memory to use for the servers"); return;
            case 3: player.sendMessage(ChatColor.RED+"Failed to send you to the server"); Bukkit.getLogger().warning("DLS: An IOException occured"); return;
            default: player.sendMessage(ChatColor.RED+"Failed to send you to the server"); return;
        }

        try {
            for(int i = 0; i < 30; i++){
                switch(serverStatus.getStatus(serverip, serverport)){
                    case 0: break;
                    case -1: player.sendMessage(ChatColor.RED+"Failed to send you to the server"); Bukkit.getLogger().warning("Server timed out"); return;
                    case -2: player.sendMessage(ChatColor.RED+"Failed to send you to the server"); Bukkit.getLogger().warning("An IOEXception occured getting server status"); return;
                    default: pluginMessage.connectPlayer(player, servername); return;
                }
                Thread.sleep(1000);
            }
            player.sendMessage(ChatColor.RED+"Failed to send you to the server");
            Bukkit.getLogger().warning("Server doesn't respond.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED+"Failed to send you to the server");
            Bukkit.getLogger().warning("Thread interrupted");
        }
    }

    private int connectToDLS(){
        int status = 10;
        try{
            Socket socket = new Socket("localhost", 59827);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            
            output.writeUTF(servername);
            output.flush();
            status = Integer.valueOf(input.readUTF());
            
            output.close();
            input.close();
            socket.close();

        }catch(IOException e){
            e.printStackTrace();
            player.sendMessage(ChatColor.RED+"Failed to send you to the server");
            Bukkit.getLogger().warning("IOException trying connecting to DLS");
        }

        return status;
        
    }
}

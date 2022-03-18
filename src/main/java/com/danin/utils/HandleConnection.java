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
            case -1: player.sendMessage(ChatColor.RED+"The server are"); return;
            case -2: player.sendMessage(ChatColor.RED+"A IOException occured"); return;
            default: pluginMessage.connectPlayer(player, servername); return;
        }
        
        switch(connectToDLS()){
            case 1: player.sendMessage("Error on DLS. Please contact an admin."); return;
            case 2: player.sendMessage("Error on DLS. Please contact an admin."); return;
            case 3: player.sendMessage("Error on DLS. Please contact an admin."); return;
            case 4: player.sendMessage("Error on DLS. Please contact an admin."); return;
            default: break;
        }

        try {
            for(int i = 0; i < 30; i++){
                switch(serverStatus.getStatus(serverip, serverport)){
                    case 0: break;
                    case -1: player.sendMessage(ChatColor.RED+"Failed to send you to the server. Please contact an admin about this."); return;
                    case -2: player.sendMessage(ChatColor.RED+"Failed to send you to the server. Please contact an admin about this."); return;
                    default: pluginMessage.connectPlayer(player, servername); return;
                }
                Thread.sleep(1000);
            }
            player.sendMessage(ChatColor.RED+"Failed to send you to the server. Please contact an admin about this.");
            Bukkit.getLogger().warning("Server doesn't respond.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED+"Failed to send you to the server. Please contact an admin about this.");
        }
    }

    private int connectToDLS(){
        int status = 4;
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
            player.sendMessage(ChatColor.RED+"Failed to send you to the server. Please contact an admin about this.");
        }

        return status;
        
    }
}

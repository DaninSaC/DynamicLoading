package com.danin.main;

import com.danin.commands.Connect;
import com.danin.utils.PluginMessage;

import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    private static Main plugin;

    public void onEnable(){
        plugin = this;

        getCommand("connect").setExecutor(new Connect());

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessage());    
    }

    public static Main getPlugin(){
        return plugin;
    }

}

package com.danin.events;

import com.danin.utils.PluginMessage;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerSignHitEvent implements Listener {
    
    private PluginMessage pluginMessage = new PluginMessage();

    @EventHandler
    public void onPlayerSignHit(PlayerInteractEvent event){
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if(block != null && block.getState() instanceof Sign){
            Sign sign = (Sign)block.getState();
            if(sign.getLine(0).equalsIgnoreCase("[join]")){
                pluginMessage.getServerIP(player, sign.getLine(1));
            }
        }
    }
}

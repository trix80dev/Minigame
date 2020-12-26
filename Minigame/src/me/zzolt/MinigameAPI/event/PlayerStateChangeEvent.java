package me.zzolt.MinigameAPI.event;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import me.zzolt.MinigameAPI.team.MinigamePlayer;
import me.zzolt.MinigameAPI.team.MinigamePlayer.PlayerState;

public class PlayerStateChangeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }
    
    private MinigamePlayer player;
    private PlayerState state;
    
    public PlayerStateChangeEvent(MinigamePlayer player, PlayerState state) {
    	this.player = player;
    	this.state = state;
    }
    
    public MinigamePlayer getPlayer() {
    	return player;
    }
    
    public PlayerState getState() {
    	return state;
    }

}

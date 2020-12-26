package me.zzolt.MinigameAPI.event;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import me.zzolt.MinigameAPI.minigame.Minigame;
import me.zzolt.MinigameAPI.minigame.Minigame.MinigameState;

public class MinigameStateChangeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }
    
    private Minigame game;
    private MinigameState state;
    
    public MinigameStateChangeEvent(Minigame game, MinigameState state) {
    	this.game = game;
    	this.state = state;
    }
    
    public Minigame getPlayer() {
    	return game;
    }
    
    public MinigameState getState() {
    	return state;
    }
	
}

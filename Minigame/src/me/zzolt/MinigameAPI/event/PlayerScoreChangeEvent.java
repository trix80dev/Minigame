package me.zzolt.MinigameAPI.event;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import me.zzolt.MinigameAPI.team.MinigamePlayer;

public class PlayerScoreChangeEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }
    
    private MinigamePlayer player;
    private int score;
    
    public PlayerScoreChangeEvent(MinigamePlayer player, int score) {
    	this.player = player;
    	this.score = score;
    }
    
    public MinigamePlayer getPlayer() {
    	return player;
    }
    
    public int getScore() {
    	return score;
    }

}

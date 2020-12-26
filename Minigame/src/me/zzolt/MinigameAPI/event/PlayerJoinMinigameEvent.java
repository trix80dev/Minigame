package me.zzolt.MinigameAPI.event;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import me.zzolt.MinigameAPI.minigame.Minigame;
import me.zzolt.MinigameAPI.team.MinigamePlayer;

public class PlayerJoinMinigameEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }
    
    private Minigame minigame;
    private MinigamePlayer player;
	
	public PlayerJoinMinigameEvent(MinigamePlayer player, Minigame minigame) {
		this.player = player;
		this.minigame = minigame;
	}
	
	public MinigamePlayer getPlayer() {
		return player;
	}
	
	public Minigame getMinigame() {
		return minigame;
	}

}

package me.zzolt.MinigameAPI.util;

import cn.nukkit.AdventureSettings;
import cn.nukkit.AdventureSettings.Type;
import cn.nukkit.Player;
import me.zzolt.MinigameAPI.team.MinigamePlayer;

public class PlayerUtil {
	
	public static MinigamePlayer castPlayer(Player player) {
		if(player instanceof MinigamePlayer)
			return (MinigamePlayer) player;
		else
			System.out.println("Player is not Minigame Player!");
			return (MinigamePlayer) player;
	}
	
	public static AdventureSettings spectatorAdventureSettings(MinigamePlayer player) {
		AdventureSettings as = new AdventureSettings(player);
		as.set(Type.ALLOW_FLIGHT, true);
		as.set(Type.FLYING, true);
		as.set(Type.ATTACK_MOBS, false);
		as.set(Type.ATTACK_PLAYERS, false);
		as.set(Type.BUILD_AND_MINE, false);
		as.set(Type.DOORS_AND_SWITCHED, false);
		as.set(Type.OPEN_CONTAINERS, false);
		
		return as;
	}
	
	public static AdventureSettings playerAdventureSettings(MinigamePlayer player) {
		AdventureSettings as = new AdventureSettings(player);
		as.set(Type.ALLOW_FLIGHT, false);
		as.set(Type.FLYING, false);
		as.set(Type.ATTACK_MOBS, true);
		as.set(Type.ATTACK_PLAYERS, true);
		as.set(Type.BUILD_AND_MINE, true);
		as.set(Type.DOORS_AND_SWITCHED, true);
		as.set(Type.OPEN_CONTAINERS, true);
		
		return as;
	}

}

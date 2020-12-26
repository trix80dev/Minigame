package me.zzolt.MinigameAPI.listener;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerCreationEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import me.zzolt.MinigameAPI.MinigameAPI;
import me.zzolt.MinigameAPI.event.PlayerJoinMinigameEvent;
import me.zzolt.MinigameAPI.event.PlayerScoreChangeEvent;
import me.zzolt.MinigameAPI.minigame.Minigame;
import me.zzolt.MinigameAPI.team.MinigamePlayer;
import me.zzolt.MinigameAPI.team.Team;
import me.zzolt.MinigameAPI.util.PlayerUtil;

public class PlayerEventListener implements Listener {
	
	@EventHandler (ignoreCancelled = true)
	public void onPlayerCreation(PlayerCreationEvent event)  {
		event.setPlayerClass(MinigamePlayer.class);
	}
	
	@EventHandler (ignoreCancelled = true)
	public void onPlayerJoinMinigame(PlayerJoinMinigameEvent event) {
		if(event.getMinigame().getPlayers().size() >= event.getMinigame().getMaxPlayers())
			event.setCancelled();
		
		event.getMinigame().addPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof MinigamePlayer)) return;
		MinigamePlayer player = PlayerUtil.castPlayer((Player) event.getEntity());
		if(event.getFinalDamage() >= player.getHealth()) {
			event.setCancelled();
			player.setHealth(20);
			player.die();
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		MinigamePlayer player = PlayerUtil.castPlayer(event.getPlayer());
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR) {
			if(player.getInventory().getItemInHand().getId() == Item.NETHER_STAR) {
				ArrayList<Team> teams = new ArrayList<Team>();
				teams.add(new Team("", 1));
				teams.add(new Team("", 1));
				Minigame m = new Minigame(null, 1, 2, 10, false, teams);
				for(Player p : MinigameAPI.getInstance().getServer().getOnlinePlayers().values()) {
					MinigamePlayer p1 = (MinigamePlayer) p;
					m.addPlayer(p1);
				}
			}  else if (player.getInventory().getItemInHand().getId() == Item.NETHER_BRICK) {
				player.setScore(player.getScore() + 1);
			}
		}
	}
	
	@EventHandler
	public void onPlayerScoreChange(PlayerScoreChangeEvent event) {
		MinigamePlayer player = event.getPlayer();
		player.sendMessage(TextFormat.GREEN + "You scored! You now have " + event.getScore() + " score!");
		if(event.getScore() >= player.getMinigame().getScoreToWin() && player.getMinigame().getScoreToWin() > 0) {
			player.getMinigame().endGame(player.getTeam());
		}
	}

}

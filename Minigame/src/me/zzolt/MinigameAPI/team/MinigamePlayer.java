package me.zzolt.MinigameAPI.team;

import java.net.InetSocketAddress;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.TextFormat;
import me.zzolt.MinigameAPI.MinigameAPI;
import me.zzolt.MinigameAPI.event.PlayerScoreChangeEvent;
import me.zzolt.MinigameAPI.event.PlayerStateChangeEvent;
import me.zzolt.MinigameAPI.minigame.Minigame;
import me.zzolt.MinigameAPI.util.PlayerUtil;

public class MinigamePlayer extends Player {

	public MinigamePlayer(SourceInterface interfaz, Long clientID, InetSocketAddress socketAddress) {
		super(interfaz, clientID, socketAddress);
	}
	
	private Minigame minigame;
	private boolean isInGame;
	private Team team;
	private PlayerState state;
	
	private Vector3 firstSpawnLocation;
	private Vector3 respawnLocation;
	
	private int score;
	private int lives = 2;
	private int respawnTime = 5;
	
	private int kills;
	private int deaths;
	
	public Minigame getMinigame() {
		return minigame;
	}
	
	public void setMinigame(Minigame game) {
		minigame = game;
	}
	
	public boolean isInGame() {
		return isInGame;
	}
	
	public void setInGame(boolean isInGame) {
		this.isInGame = isInGame;
	}
	
	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}

	public void die() {
		if(lives > 0) {
			lives--;
			
			if(lives > 0) {
				startRespawning();
			} else {
				startSpectating();
				getTeam().getAlivePlayers().remove(this);
				if(getTeam().getAlivePlayers().size() == 0 && getMinigame().isLastTeamStanding()) {
					getMinigame().getAliveTeams().remove(getTeam());
					if(getMinigame().getAliveTeams().size() == 1) {
						getMinigame().endGame(getMinigame().getAliveTeams().get(0));
					}
				}
				//player.startSpectating();
			}
			
		} else {
			startRespawning();
		}
		
	}
	
	public void startRespawning() {
		this.setState(PlayerState.RESPAWNING);
		this.setAdventureSettings(PlayerUtil.spectatorAdventureSettings(this));
		this.sendActionBar(TextFormat.YELLOW + "Respawning in " + TextFormat.AQUA + getRespawnTime() + TextFormat.YELLOW + " seconds...");
		this.invisible = true;
		new NukkitRunnable() {
			int seconds = getRespawnTime();

			@Override
			public void run() {
				seconds--;
				if(seconds == 0) {
					minigameRespawn();
					this.cancel();
				}
					
				sendActionBar(TextFormat.YELLOW + "Respawning in " + TextFormat.AQUA + seconds + TextFormat.YELLOW + " seconds...");
				
			}
			
		}.runTaskTimer(MinigameAPI.getInstance(), 20, 20);
	}
	
	public void startSpectating() {
		this.setState(PlayerState.SPECTATING);
		this.setAdventureSettings(PlayerUtil.spectatorAdventureSettings(this));
		this.sendActionBar(TextFormat.YELLOW + "Respawning in " + TextFormat.AQUA + getRespawnTime() + TextFormat.YELLOW + " seconds...");
		this.invisible = true;
	}
	
	public void minigameRespawn() {
		this.setState(PlayerState.PLAYING);
		//setPosition(respawnLocation);
		this.setAdventureSettings(PlayerUtil.playerAdventureSettings(this));
		this.invisible = false;
	}
	
	public PlayerState getState() {
		return state;
	}
	
	private void setState(PlayerState state) {
		this.state = state;
		PlayerStateChangeEvent event = new PlayerStateChangeEvent(this, state);
		MinigameAPI.getInstance().getServer().getPluginManager().callEvent(event);
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		if(isInGame) {
			this.score = score;
			PlayerScoreChangeEvent event = new PlayerScoreChangeEvent(this, score);
			MinigameAPI.getInstance().getServer().getPluginManager().callEvent(event);
		}
	}
	
	public int getLives() {
		return lives;
	}
	
	public void setLives(int lives) {
		this.lives = lives;
	}
	
	public int getRespawnTime() {
		return respawnTime;
	}
	
	public void setRespawnTime(int seconds) {
		this.respawnTime = seconds;
	}
	
	public enum PlayerState {
		IMMOBILE,
		INVULNERABLE,
		PLAYING,
		RESPAWNING,
		SPECTATING
	}
 
}

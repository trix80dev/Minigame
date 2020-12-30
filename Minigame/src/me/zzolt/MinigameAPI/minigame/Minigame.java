package me.zzolt.MinigameAPI.minigame;

import java.util.ArrayList;
import java.util.HashMap;

import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.TextFormat;
import me.zzolt.MinigameAPI.MinigameAPI;
import me.zzolt.MinigameAPI.map.EntitySpawnPosition;
import me.zzolt.MinigameAPI.map.Map;
import me.zzolt.MinigameAPI.map.SpawnPosition;
import me.zzolt.MinigameAPI.team.MinigamePlayer;
import me.zzolt.MinigameAPI.team.Team;
import me.zzolt.MinigameAPI.util.MinigameCountdown;

public class Minigame {
	
	private MinigameState minigameState;
	private ArrayList<MinigamePlayer> players = new ArrayList<MinigamePlayer>();
	private ArrayList<Team> teams = new ArrayList<Team>();
	private ArrayList<Team> aliveTeams = new ArrayList<Team>();
	private ArrayList<Map> maps;
	private Map chosenMap;
	private Level lobby;
	
	private int scoreToWin;
	private boolean lastTeamStanding;
	
	private int countdownPlayers;
	private int maxPlayers;
	
	public Minigame(ArrayList<Map> maps, Level lobby, int countdownPlayers, int maxPlayers, int scoreToWin, boolean lastTeamStanding, ArrayList<Team> teams) {
		this.maps = maps;
		this.lobby = lobby;
		this.countdownPlayers = countdownPlayers;
		this.maxPlayers = maxPlayers;
		this.scoreToWin = scoreToWin;
		this.lastTeamStanding = lastTeamStanding;
		this.teams = teams;
		this.aliveTeams = teams;
		setState(MinigameState.LOBBY);
	}
	
	public MinigameState getState() {
		return minigameState;
	}
	
	public void setState(MinigameState state) {
		minigameState = state;
	}
	
	public ArrayList<MinigamePlayer> getPlayers() {
		return players;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public void addPlayer(MinigamePlayer player) {
		players.add(player);
		player.setInGame(true);
		player.setMinigame(this);
		player.teleport(lobby.getSpawnLocation());
		if(players.size() >= countdownPlayers && getState() == MinigameState.LOBBY) {
			startCountdown();
		}
		for(MinigamePlayer p : players) {
			p.sendMessage(TextFormat.GRAY + player.getName() + "  joined. " + TextFormat.DARK_GRAY + "[" + players.size() + "/" + maxPlayers + "]");
		}
	}
	
	public void removePlayer(MinigamePlayer player) {
		if(players.contains(player)) {
			players.remove(player);
			player.setInGame(false);
		}
		else
			System.out.println("Couldn't Find Player to Remove!");
	}
	
	public ArrayList<Team> getTeams() {
		return teams;
	}
	
	public ArrayList<Team> getAliveTeams() {
		return aliveTeams;
	}
	
	public void setTeams(boolean isRandom) {
		for (int i = 0; i < players.size(); i++) {
			teams.get(i % teams.size()).addPlayer(players.get(i));
		}
	}
	
	public Level getLobby() {
		return lobby;
	}
	
	public ArrayList<Map> getMaps() {
		return maps;
	}
	
	public void addVote(MinigamePlayer player, String map) {
		if(player.getVotedMap() != null) {
			removeVote(player.getVotedMap());
		}
		for(Map m : maps) {
			if(m.getName() == map) {
				player.setVotedMap(m);
				m.setVotes(m.getVotes() + 1);
			}
		}
	}
	
	public void removeVote(Map map) {
		map.setVotes(map.getVotes() - 1);
	}
	
	public int getScoreToWin() {
		return scoreToWin;
	}
	
	public void setScoreToWin(int score) {
		this.scoreToWin = score;
	}
	
	public boolean isLastTeamStanding() {
		return lastTeamStanding;
	}
	
	public void setLastTeamStanding(boolean lastTeamStanding) {
		this.lastTeamStanding = lastTeamStanding;
	}
	
	public void chooseMap() {
		Map voted = null;
		for(Map map : maps) {
			if(voted == null) voted = map;
			if(voted.getVotes() < map.getVotes()) voted = map;
		}
		chosenMap = voted;
	}
	
	public void startCountdown() {
		setState(MinigameState.COUNTDOWN);
		new MinigameCountdown(20) {
			@Override
			public void onTick(int seconds) {
				if(players.size() < countdownPlayers) {
					for(MinigamePlayer player : players) {
						player.sendMessage(TextFormat.GRAY + "Countdown has been cancelled!");
					}
					this.cancel();
				}
				if(seconds == 3) {
					chooseMap();
					for(MinigamePlayer player : players) {
						player.sendMessage(chosenMap.getName() + " won with " + chosenMap.getVotes() + "votes");
						player.setVotedMap(null);
					}
					for(Map map : maps) {
						map.setVotes(0);
					}
				}
				for (MinigamePlayer player : players) {
					player.sendActionBar(TextFormat.GREEN + "Game starting in " + seconds + " seconds");
				}
			}
			
			@Override
			public void onComplete() {
				startGame();
			}
		}.runTaskTimer(MinigameAPI.getInstance(), 0, 20);
	}
	
	public void startGame() {
		setState(MinigameState.STARTING);
		setTeams(false);
		for(MinigamePlayer player : players) {
			player.setDisplayName(player.getTeam().getColor() + player.getDisplayName());
			player.setLevel(chosenMap.getLevel());
						if(player.getTeam().getName() != "")
				player.sendMessage(TextFormat.GREEN + "You are on team " + player.getTeam().getName());
			player.sendActionBar(TextFormat.GREEN + "Starting in 5 seconds");
			player.setImmobile();
		}
		for(SpawnPosition pos : chosenMap.getSpawnPositions()) {
			if(pos instanceof EntitySpawnPosition) {}
			else {
				if(pos.team != null) {
					for(MinigamePlayer player : players) {
						if(player.getTeam() == pos.team) {
							player.teleport(pos.position);
							player.hasSpawned = true;
							break;
						}
					}
				}
			}
		}
		/*for(MinigamePlayer player : players) {
			if(player.hasSpawned) break;
			for(SpawnPosition pos : chosenMap.getSpawnPositions()) {
				if(pos.team == null) {
					
				}
			}
		}*/
		new NukkitRunnable() {
			int seconds = 5;
			@Override
			public void run() {
				seconds--;
				for(MinigamePlayer player : players) {
					player.sendActionBar(TextFormat.GREEN + "Starting in " + seconds + " seconds");
				}
				if(seconds == 0) {
					for(MinigamePlayer player : players) {
						player.setImmobile(false);
					}
					this.cancel();
				}
			}
			
		}.runTaskTimer(MinigameAPI.getInstance(), 20, 20);
		for(MinigamePlayer player : players) {
			//player.switchLevel(maps.get(0).getLevel());
		}
	}

	public void endGame(Team winner) {
		setState(MinigameState.ENDING);
		for(MinigamePlayer player : players) {
			player.sendMessage(TextFormat.YELLOW + "Game Over!");
			if(winner.getName() != "") {
				player.sendMessage(TextFormat.GREEN + winner.getName() + " won the game!");
			}
			player.setScore(0);
		}
		new NukkitRunnable() {
			int seconds = 5;
			@Override
			public void run() {
				seconds--;
				if(seconds == 0) {
					showMinigameStats();
				}
			}
			
		}.runTaskTimer(MinigameAPI.getInstance(), 0, 20);
	}
	
	public void showMinigameStats() {
		setState(MinigameState.ENDED);
		for(MinigamePlayer player : players) {
			player.teleport(lobby.getSpawnLocation());
			player.sendActionBar(TextFormat.YELLOW + "Finding new game in 15 seconds");
		}
		new NukkitRunnable() {
			int seconds = 15;
			@Override
			public void run() {
				seconds--;
				if(seconds == 0) {
					restart();
					this.cancel();
				}
				for(MinigamePlayer player : players) {
					player.sendActionBar(TextFormat.YELLOW + "Finding new game in " + seconds + " seconds");
				}
				
			}
			
		}.runTaskTimer(MinigameAPI.getInstance(), 20, 20);
		
	}
	
	public void restart() {
		for(MinigamePlayer player: players) {
			player.setInGame(false);
			player.setTeam(null);
		}
		setState(MinigameState.RESTARTING);
	}
	
	public final HashMap<Integer, Item> lobbyInventory() {
		HashMap<Integer, Item> map = new HashMap<Integer, Item>();
		Item mapSelector = new Item(Item.PAPER);
		mapSelector.setCustomName("Vote for Map");
		map.put(0, mapSelector);
		return map;
	}
	
	public enum MinigameState {
		LOBBY,
		COUNTDOWN,
		STARTING,
		PLAYING,
		ENDING,
		ENDED,
		RESTARTING
	}

}

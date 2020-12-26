package me.zzolt.MinigameAPI.team;

import java.util.ArrayList;

public class Team {
	
	private ArrayList<MinigamePlayer> players = new ArrayList<MinigamePlayer>();
	private ArrayList<MinigamePlayer> alivePlayers = new ArrayList<MinigamePlayer>();
	
	private String name;
	
	private int maxPlayers;
	
	public Team(String name, int maxPlayers) {
		this.name = name;
		this.maxPlayers = maxPlayers;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<MinigamePlayer> getPlayers() {
		return players;
	}
	
	public void addPlayer(MinigamePlayer player) {
		players.add(player);
		alivePlayers.add(player);
		player.setTeam(this);
	}
	
	public ArrayList<MinigamePlayer> getAlivePlayers() {
		return alivePlayers;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public int getScore() {
		int score = 0;
		for(MinigamePlayer player : players) {
			score += player.getScore();
		}
		return score;
	}

}

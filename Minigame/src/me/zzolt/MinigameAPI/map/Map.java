package me.zzolt.MinigameAPI.map;

import java.util.List;

import cn.nukkit.level.Level;

public class Map {
	
	private String name;
	private int votes;
	private Level level;
	private List<SpawnPosition> spawnPositions;
	
	public Map(String name, Level level, List<SpawnPosition> spawnPositions) {
		this.name = name;
		this.level = level;
		this.spawnPositions = spawnPositions;
	}
	
	public String getName() {
		return name;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public List<SpawnPosition> getSpawnPositions() {
		return spawnPositions;
	}
	
	public int getVotes() {
		return votes;
	}
	
	public void setVotes(int votes) {
		this.votes = votes;
	}

}

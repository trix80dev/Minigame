package me.zzolt.MinigameAPI.map;

import cn.nukkit.entity.Entity;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import me.zzolt.MinigameAPI.team.Team;

public class SpawnPosition {
	
	public Team team;
	public Vector3 position;
	
	public SpawnPosition(Vector3 pos, Team team) {
		this.position = pos;
		this.team = team;
	}


}

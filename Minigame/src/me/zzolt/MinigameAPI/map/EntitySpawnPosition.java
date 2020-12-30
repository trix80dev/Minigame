package me.zzolt.MinigameAPI.map;

import cn.nukkit.entity.Entity;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import me.zzolt.MinigameAPI.team.Team;

public class EntitySpawnPosition extends SpawnPosition {
	
	public EntitySpawnPosition(Vector3 pos, Entity entity, Team team, boolean spawnAtStart) {
		super(pos, team);
	}

}

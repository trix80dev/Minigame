package me.zzolt.MinigameAPI;

import cn.nukkit.entity.Entity;
import cn.nukkit.plugin.PluginBase;
import me.zzolt.MinigameAPI.entity.EntityDummy;
import me.zzolt.MinigameAPI.entity.MinigameEntity;
import me.zzolt.MinigameAPI.listener.PlayerEventListener;

public class MinigameAPI extends PluginBase {
	
	private static MinigameAPI instance;
	
	@Override
	public void onEnable() {
		instance = this;
		this.getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
		Entity.registerEntity("MinigameEntity", MinigameEntity.class);
		Entity.registerEntity("EntityDummy", EntityDummy.class);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static MinigameAPI getInstance() {
		return instance;
	}

}

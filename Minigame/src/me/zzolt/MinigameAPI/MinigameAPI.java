package me.zzolt.MinigameAPI;

import cn.nukkit.plugin.PluginBase;
import me.zzolt.MinigameAPI.listener.PlayerEventListener;

public class MinigameAPI extends PluginBase {
	
	private static MinigameAPI instance;
	
	@Override
	public void onEnable() {
		instance = this;
		this.getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static MinigameAPI getInstance() {
		return instance;
	}

}

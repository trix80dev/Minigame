package me.zzolt.MinigameAPI.util;

import cn.nukkit.scheduler.NukkitRunnable;

public class MinigameCountdown extends NukkitRunnable {
	
	private int seconds;
	
	public MinigameCountdown(int seconds) {
		this.seconds = seconds;
	}

	@Override
	public void run() {
		if(seconds <= 0) {
			onComplete();
			this.cancel();
		}
		onTick(seconds);
		seconds--;
	}
	
	public void onTick(int seconds) {}
	public void onComplete() {}

}

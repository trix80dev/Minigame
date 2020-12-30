package me.zzolt.MinigameAPI.shop;

import java.util.List;

public class ShopCategory extends ShopEntry {
	
	private List<ShopEntry> entries;

	public ShopCategory(String title, List<ShopEntry> entries) {
		super(title);
		this.entries = entries;
	}
	
	public List<ShopEntry> getEntries() {
		return entries;
	}

}

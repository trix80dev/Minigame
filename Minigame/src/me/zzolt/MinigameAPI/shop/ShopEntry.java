package me.zzolt.MinigameAPI.shop;

import java.util.List;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.item.Item;

public class ShopEntry extends ElementButton {
	
	private String title;
	private Item item;
	private Item reward;
	
	public ShopEntry(String title) {
		super(title);
		this.title = title;
	}
	
	public ShopEntry(String title, Item reward, Item item) {
		super(title + "\n" + item.count + " " + item.getName());
		this.title = title;
		this.item = item;
		this.reward = reward;
	}
	
	public ShopEntry(String title, Item reward, int item, int cost) {
		super(title);
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Item getItem() {
		return item;
	}
	
	public Item getReward() {
		return reward;
	}

}

package me.zzolt.MinigameAPI.shop;

import java.util.List;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;

public class Shop extends FormWindowSimple {

	private String title;
	private String content;
	private List<ShopEntry> entries;

	public Shop(String title, String content, List<ShopEntry> entries) {
		super(title, content);
		this.entries = entries;
	}
	
	public List<ShopEntry> getEntries() {
		return entries;
	}
	
	public FormWindowSimple getFormWindow() {
		FormWindowSimple fws = new FormWindowSimple(title, content);
		return fws;
	}

}

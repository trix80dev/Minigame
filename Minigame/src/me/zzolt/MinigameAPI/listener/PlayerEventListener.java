package me.zzolt.MinigameAPI.listener;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.block.BlockIgniteEvent.BlockIgniteCause;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCreationEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.ChangeDimensionPacket;
import cn.nukkit.utils.TextFormat;
import me.zzolt.MinigameAPI.MinigameAPI;
import me.zzolt.MinigameAPI.entity.EntityDummy;
import me.zzolt.MinigameAPI.entity.MinigameEntity;
import me.zzolt.MinigameAPI.event.PlayerJoinMinigameEvent;
import me.zzolt.MinigameAPI.event.PlayerScoreChangeEvent;
import me.zzolt.MinigameAPI.map.Map;
import me.zzolt.MinigameAPI.map.SpawnPosition;
import me.zzolt.MinigameAPI.minigame.Minigame;
import me.zzolt.MinigameAPI.shop.Shop;
import me.zzolt.MinigameAPI.shop.ShopCategory;
import me.zzolt.MinigameAPI.shop.ShopEntry;
import me.zzolt.MinigameAPI.team.MinigamePlayer;
import me.zzolt.MinigameAPI.team.Team;
import me.zzolt.MinigameAPI.util.PlayerUtil;

public class PlayerEventListener implements Listener {
	
	@EventHandler (ignoreCancelled = true)
	public void onPlayerCreation(PlayerCreationEvent event)  {
		event.setPlayerClass(MinigamePlayer.class);
	}
	
	@EventHandler (ignoreCancelled = true)
	public void onPlayerJoinMinigame(PlayerJoinMinigameEvent event) {
		if(event.getMinigame().getPlayers().size() >= event.getMinigame().getMaxPlayers())
			event.setCancelled();
		
		event.getPlayer().teleport(event.getMinigame().getLobby().getSpawnLocation());
		event.getPlayer().getInventory().clearAll();
		event.getPlayer().getInventory().setContents(event.getMinigame().lobbyInventory());
		event.getMinigame().addPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof MinigamePlayer)) return;
		MinigamePlayer player = PlayerUtil.castPlayer((Player) event.getEntity());
		if(event.getFinalDamage() >= player.getHealth()) {
			event.setCancelled();
			if(event.getCause() == DamageCause.VOID)
				player.teleport(player.getLevel().getSpawnLocation());
			player.setHealth(20);
			player.getFoodData().setLevel(20);
			player.die();
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		MinigamePlayer player = PlayerUtil.castPlayer(event.getPlayer());
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR) {
			if(player.getInventory().getItemInHand().getId() == Item.NETHER_STAR) {
				if(player.isInGame()) return;
				ArrayList<Team> teams = new ArrayList<Team>();
				teams.add(new Team("Team 1", TextFormat.AQUA, 1));
				teams.add(new Team("Team 2", TextFormat.RED, 1));
				ArrayList<Map> maps = new ArrayList<Map>();
				ArrayList<SpawnPosition> spawns = new ArrayList<SpawnPosition>();
				spawns.add(new SpawnPosition(new Vector3(22, 74, 12), teams.get(0)));
				spawns.add(new SpawnPosition(new Vector3(43, 74, 19), teams.get(1)));
				maps.add(new Map("Bruh", MinigameAPI.getInstance().getServer().getLevelByName("bruh"), spawns));
				Minigame m = new Minigame(maps, MinigameAPI.getInstance().getServer().getLevelByName("lobby"), 1, 2, 10, false, teams);
				for(Player p : MinigameAPI.getInstance().getServer().getOnlinePlayers().values()) {
					MinigamePlayer p1 = (MinigamePlayer) p;
					m.addPlayer(p1);
				}
			}  else if (player.getInventory().getItemInHand().getId() == Item.NETHER_BRICK) {
				player.setScore(player.getScore() + 1);
			} else if (player.getInventory().getItemInHand().getId() == Item.PAPER) {
				FormWindowSimple fws = new FormWindowSimple("Vote for map", "");
				for(Map map : player.getMinigame().getMaps()) {
					fws.addButton(new ElementButton(map.getName()));
				}
				player.showFormWindow(fws);
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getPlayer().getInventory().getItemInHand().getId() == Item.DIAMOND_SWORD) {
			Skin skin = event.getPlayer().getSkin();
			@SuppressWarnings({ "unchecked", "rawtypes" })
			CompoundTag nbt = new CompoundTag()
	                .putList(new ListTag("Pos")
	                        .add((Tag)new DoubleTag("", event.getPlayer().x))
	                        .add((Tag)new DoubleTag("", event.getPlayer().y + event.getPlayer().getEyeHeight()))
	                        .add((Tag)new DoubleTag("", event.getPlayer().z)))
	                .putList(new ListTag("Motion")
	                        .add((Tag)new DoubleTag("", 0.0))
	                        .add((Tag)new DoubleTag("", 0.0))
	                        .add((Tag)new DoubleTag("", 0.0)))
	                .putList(new ListTag("Rotation")
	                        .add((Tag)new FloatTag("", (float) event.getPlayer().yaw))
	                        .add((Tag)new FloatTag("", (float) event.getPlayer().pitch)))
	                .putFloat("Health", 20.0f)
	                .putString("NameTag", "")
	                .putFloat("FoodSaturationLevel", 20.0f)
	                .putFloat("Scale", 1.0f)
	                .putCompound("Skin", new CompoundTag()
	                        .putBoolean("Transparent", true)
	                        .putByteArray("Data", skin.getSkinData().data)
	                        .putString("Name", "penguin")
	                        .putByteArray("CapeData", skin.getCapeData().data))
	                .putList(new ListTag("Inventory"));
			
			MinigameEntity e = (MinigameEntity) Entity.createEntity("MinigameEntity", event.getPlayer().getLevel().getChunk(event.getPlayer().getFloorX() >> 4, event.getPlayer().getFloorZ() >> 4), nbt);
			e.setSkin(skin);
			e.invulnerable = true;
			e.saveNBT();
			e.setThrower(event.getPlayer());
			e.spawnToAll();
			event.getPlayer().getInventory().remove(event.getPlayer().getInventory().getItemInHand());
		} 
	}
	
	@EventHandler
	public void onIgnite(BlockIgniteEvent event) {
		if(event.getCause() != BlockIgniteCause.FLINT_AND_STEEL) return;
		Block b = event.getBlock().down();
		if(b.getId() != Block.NETHERRACK) return;
		((Player) event.getEntity()).sendMessage("nether");
	}
	
	@EventHandler
	public void onPlayerScoreChange(PlayerScoreChangeEvent event) {
		MinigamePlayer player = event.getPlayer();
		player.sendMessage(TextFormat.GREEN + "You scored! You now have " + event.getScore() + " score!");
		if(event.getScore() >= player.getMinigame().getScoreToWin() && player.getMinigame().getScoreToWin() > 0) {
			player.getMinigame().endGame(player.getTeam());
		}
	}
	
	@EventHandler
	public void onChat(PlayerChatEvent event) {
		if(event.getMessage().contains("shop")) {
			ArrayList<ShopEntry> entries = new ArrayList<ShopEntry>();
			ArrayList<ShopEntry> combatEntries = new ArrayList<ShopEntry>();
			combatEntries.add(new ShopEntry("Stone Sword", new Item(Item.STONE_SWORD), new Item(Item.GOLD_INGOT, 0, 5)));
			combatEntries.add(new ShopEntry("Iron Sword", new Item(Item.IRON_SWORD), new Item(Item.GOLD_INGOT, 0, 10)));
			entries.add(new ShopCategory("Combat", combatEntries));
			FormWindowSimple shop = new FormWindowSimple("Shop", "");
			for(ShopEntry entry : entries) {
				shop.addButton(entry);
			}
			event.getPlayer().showFormWindow(shop);
			return;
		} else if(event.getMessage().contains("spawn")) {
			Skin skin = event.getPlayer().getSkin();
			@SuppressWarnings({ "unchecked", "rawtypes" })
			CompoundTag nbt = new CompoundTag()
	                .putList(new ListTag("Pos")
	                        .add((Tag)new DoubleTag("", event.getPlayer().x))
	                        .add((Tag)new DoubleTag("", event.getPlayer().y))
	                        .add((Tag)new DoubleTag("", event.getPlayer().z)))
	                .putList(new ListTag("Motion")
	                        .add((Tag)new DoubleTag("", 0.0))
	                        .add((Tag)new DoubleTag("", 0.0))
	                        .add((Tag)new DoubleTag("", 0.0)))
	                .putList(new ListTag("Rotation")
	                        .add((Tag)new FloatTag("", 0.0f))
	                        .add((Tag)new FloatTag("", 0.0f)))
	                .putFloat("Health", 20.0f)
	                .putString("NameTag", "")
	                .putFloat("FoodSaturationLevel", 20.0f)
	                .putFloat("Scale", 1.0f)
	                .putCompound("Skin", new CompoundTag()
	                        .putBoolean("Transparent", true)
	                        .putByteArray("Data", skin.getSkinData().data)
	                        .putString("Name", "penguin")
	                        .putByteArray("CapeData", skin.getCapeData().data))
	                .putList(new ListTag("Inventory"));
			
			EntityDummy e = (EntityDummy) Entity.createEntity("EntityDummy", event.getPlayer().getLevel().getChunk(event.getPlayer().getFloorX() >> 4, event.getPlayer().getFloorZ() >> 4), nbt);
			e.setSkin(skin);
			e.saveNBT();
			e.spawnToAll();
		}
		Level level = MinigameAPI.getInstance().getServer().getLevelByName(event.getMessage());
		if(!MinigameAPI.getInstance().getServer().isLevelLoaded(event.getMessage())) {
			event.getPlayer().sendMessage("Level not loaded.");
			MinigameAPI.getInstance().getServer().loadLevel(event.getMessage());
		} else {
			ChangeDimensionPacket dc = new ChangeDimensionPacket();
			dc.dimension = 1;
			dc.x = (float) level.getSpawnLocation().x;
			dc.y = (float) level.getSpawnLocation().y;
			dc.z = (float) level.getSpawnLocation().z;
			dc.respawn = false;
			event.getPlayer().dataPacket(dc);
			dc.dimension = 0;
			event.getPlayer().dataPacket(dc);
		event.getPlayer().switchLevel(level);
		//event.getPlayer().setPosition(level.getSpawnLocation());
		event.getPlayer().teleport(level.getSpawnLocation());
		}
	}
	
	@EventHandler
	public void onPlayerFormResponded(PlayerFormRespondedEvent event) {
		MinigamePlayer player = (MinigamePlayer) event.getPlayer();
		/*if(event.getWindow() instanceof Shop) {
			Shop shop = (Shop) event.getWindow();
			FormResponseSimple frs = (FormResponseSimple) event.getResponse();
			ShopEntry entry = shop.getEntries().get(frs.getClickedButtonId());
			if(entry instanceof ShopCategory) {
				Shop shop2 = new Shop(entry.getTitle(), "", ((ShopCategory) entry).getEntries());
				player.showFormWindow(shop2);
			} else {
				if(player.getInventory().contains(entry.getItem())) {
					player.getInventory().remove(entry.getItem());
					player.getInventory().addItem(entry.getReward());
				} else {
					player.sendMessage("Not enough " + entry.getItem().getName());
				}
			}
		}*/
		if(event.getWindow() instanceof FormWindowSimple) {
			FormWindowSimple fws = (FormWindowSimple) event.getWindow();
			FormResponseSimple response = (FormResponseSimple) event.getResponse();
			if(fws.getTitle() == "Vote for map") {
				player.getMinigame().addVote(player, response.getClickedButton().getText());
			}
		}
	}

}

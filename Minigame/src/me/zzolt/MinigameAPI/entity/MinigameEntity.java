package me.zzolt.MinigameAPI.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;
import me.zzolt.MinigameAPI.MinigameAPI;

public class MinigameEntity extends EntityHuman {

	public MinigameEntity(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
		// TODO Auto-generated constructor stub
	}
	
	private Player thrower;
	private boolean returning = false;
	private ArrayList<Entity> hitEntities = new ArrayList<Entity>();
	
	boolean locked;
	double lockedYaw, lockedPitch;
	int lifetime;
	
	@Override
	public float getWidth() {
		return 0.8F;
	}
	
	@Override
	public float getHeight() {
		return 0.1F;
	}
	
	@Override
	public float getLength() {
		return 0.8F;
	}
	
	public void spawnToAll(Skin skin) { 
		//skin code here
		super.spawnToAll();
	}
	
	@Override
	public boolean onUpdate(int currentTick) {
		super.onUpdate(currentTick);
		double newYaw, newPitch, angle, angle2;
		
		lifetime ++;
		
		if(lifetime > 60 && !returning) {
			returning = true;
			lifetime = 0;
		}
		
		if(lifetime > 180) {
			this.kill();
			this.close();
			thrower.getInventory().setItem(thrower.getInventory().getHeldItemIndex(), Item.get(Item.DIAMOND_SWORD));
			return false;
		}

		if(this.getLevel().getCollidingEntities(getBoundingBox()).length > 1)
		onCollideWithEntity(this.getLevel().getCollidingEntities(boundingBox));
		if(!thrower.isAlive()) {
			this.close();
			this.kill();
			return false;
		}
		if(this.isCollided) {
			//thrower.sendMessage("returning");
			locked = false;
			//returning = true;
			//thrower.sendMessage("lol " + this.collisionBlocks.size());
			
		}
		
		if(this.isCollidedVertically) {
			if(pitch > 0) {
				double yaw1 = yaw + 180 - 60;
				double yaw2 = yaw + 180 + 60;
				yaw1 %= 360;
				yaw2 %= 360;
				if(yaw1 < 0) yaw1 += 360;
				if(yaw2 < 0) yaw2 += 360;
				findNewTarget(yaw1, yaw2, -10, -60);
			} else {
				double yaw1 = yaw + 180 - 60;
				double yaw2 = yaw + 180 + 60;
				yaw1 %= 360;
				yaw2 %= 360;
				if(yaw1 < 0) yaw1 += 360;
				if(yaw2 < 0) yaw2 += 360;
				findNewTarget(yaw1, yaw2, 10, 60);
			}
		}
		
		if(returning) {
			angle = Math.toDegrees(Math.atan2((thrower.z-z),(thrower.x-x)));
	        
	        if(angle < 0) {
	            
	            angle = 360 - (-angle);
	            
	        }
	        
	        newYaw = angle - 90;
	        
	        angle2 = Math.toDegrees(Math.atan2((thrower.y-y),(Math.sqrt(Math.pow(thrower.x-x,2) + Math.pow(thrower.z-z,2)))));
	        
	        if(angle2 < 0) {
	            
	            angle2 = 360 - (-angle2);
	            
	        }
	        
	        newPitch = -angle2;
	        
	        yaw = newYaw;
	        pitch = newPitch;
	        if(this.distance(thrower.getPosition().asVector3f().asVector3()) > 1) {
	            this.setMotion(this.getDirectionVector().multiply(1));
	            
	        } else {
	        	this.kill();
	        	this.close();
				thrower.getInventory().setItem(thrower.getInventory().getHeldItemIndex(), Item.get(Item.DIAMOND_SWORD));
	        	return false;
	        }
		} else {
			if(locked) {
				this.yaw = lockedYaw;
				this.pitch = lockedPitch;
			}
			this.setMotion(this.getDirectionVector().multiply(1));
		}

		/*this.checkGround();
		if(!this.onGround){
			//this.motionY -= this.getGravity();
		} else {
			this.motionX = this.motionZ = 0;
		}
		
		Player closestPlayer = null;
		
		for(Player player : MinigameAPI.getInstance().getServer().getOnlinePlayers().values()) {
			if (closestPlayer == null) {
				closestPlayer = player;
				break;
			}
			if(this.distance(player.getPosition().asVector3f().asVector3()) < this.distance(closestPlayer.getPosition().asVector3f().asVector3())) {
				closestPlayer = player;
			}
		}*/
		
		
		
		/*for(Player player : MinigameAPI.getInstance().getServer().getOnlinePlayers().values()) {
		
			angle = Math.toDegrees(Math.atan2((player.z-z),(player.x-x)));
            
            if(angle < 0) {
                
                angle = 360 - (-angle);
                
            }
            
            newYaw = angle - 90;
            
            angle2 = Math.toDegrees(Math.atan2((player.y-y),(Math.sqrt(Math.pow(player.x-x,2) + Math.pow(player.z-z,2)))));
            
            if(angle2 < 0) {
                
                angle2 = 360 - (-angle2);
                
            }
            
            newPitch = -angle2;
            
            MoveEntityAbsolutePacket packet = new MoveEntityAbsolutePacket();
            packet.eid = this.id;
            //packet.headYaw = newYaw;
            packet.yaw = newYaw;
            packet.pitch = newPitch;
            packet.x = x;
            packet.y = y + getEyeHeight();
            packet.z = z;
            player.dataPacket(packet);
		}*/
		this.move(this.motionX, this.motionY, this.motionZ);
		return true;
	}
	
	public void onCollideWithEntity(Entity[] e) {
		for(Entity ent : e) {
			if(ent.getId() != this.id && ent.getId() != thrower.getId() && !hitEntities.contains(ent)) {
				hitEntities.add(ent);
				ent.attack(4);
				ent.setHealth(20);
				if(hitEntities.size() < 6) {
					findNewTarget(0, 360, -90, 90);
					lifetime = 0;
					//returning = true;
					return;
				}
			}
		}
	}
	
	public void findNewTarget(double yaw1, double yaw2, double pitch1, double pitch2) {
		Entity bestEntity = null;
		Entity[] entities = this.getLevel().getEntities();
		for(Entity e : entities) {
			if(e instanceof MinigameEntity) continue;
			if(!(e instanceof EntityHuman)) continue;
			if(e.getId() == this.id || e.getId() == thrower.getId()) continue;
			if(hitEntities.contains(e)) continue;
			if(distance(e.getPosition().asVector3f().asVector3()) > 7) continue;
			if(bestEntity == null) bestEntity = e;
			if(distance(e.getPosition().asVector3f().asVector3()) > distance(bestEntity.getPosition().asVector3f().asVector3())) continue;
			if(!(yaw1 < angleYawTo(e) && angleYawTo(e) < yaw2)) continue;
		}
		
		if(bestEntity == null) {
			returning = true;
		} else {
			setLock(angleYawTo(bestEntity), anglePitchTo(bestEntity));
		}
	}
	
	public void setLock(double lockYaw, double lockPitch) {
		locked = true;
		lockedYaw = lockYaw;
		lockedPitch = lockPitch;
	}
	
	public double angleYawTo(Entity e) {
		double angle;
		angle = Math.toDegrees(Math.atan2((e.z-z),(e.x-x)));
        
        if(angle < 0) {
            
            angle = 360 - (-angle);
            
        }
        
        return angle - 90;
	}
	
	public double anglePitchTo(Entity e) {
		double angle;
        
        angle = Math.toDegrees(Math.atan2((e.y-y),(Math.sqrt(Math.pow(e.x-x,2) + Math.pow(e.z-z,2)))));
        
        if(angle < 0) {
            
            angle = 360 - (-angle);
            
        }
        
        return -angle;
	}
	
	private void checkGround(){
		AxisAlignedBB[] list = this.level.getCollisionCubes(this, this.level.getTickRate() > 1 ? this.boundingBox.getOffsetBoundingBox(0, -1, 0) : this.boundingBox.addCoord(0, -1, 0), false);

		double maxY = 0;
		for(AxisAlignedBB bb : list){
			if(bb.getMaxY() > maxY){
				maxY = bb.getMaxY();
			}
		}

		this.onGround = (maxY == this.boundingBox.getMinY());
	}
	
	public void setThrower(Player player) {
		this.thrower = player;
	}

}

package game.engine.titans;

import game.engine.interfaces.Attackee;
import game.engine.interfaces.Attacker;
import game.engine.interfaces.Mobil;

public abstract class Titan implements Comparable <Titan>,Mobil,Attacker,Attackee {
	
	private final int baseHealth;
	private int currentHealth;
	private final int baseDamage;
	private final int heightInMeters;
	private int distanceFromBase;
	private int speed;
	private final int resourcesValue;
	private final int dangerLevel;
	
	public Titan(int baseHealth, int baseDamage, int heightInMeters, int distanceFromBase, 
			int speed, int resourcesValue, int dangerLevel){
		this.baseHealth = baseHealth;
		this.currentHealth = baseHealth;
		this.baseDamage = baseDamage;
		this.heightInMeters = heightInMeters;
		this.distanceFromBase = distanceFromBase;
		this.speed = speed;
		this.resourcesValue = resourcesValue;
		this.dangerLevel = dangerLevel;	
	}

	public int getBaseHealth() {
		return baseHealth;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(int health) {
		this.currentHealth = health > 0 ? health : 0;
	}
	
	public int getDamage() {
		return baseDamage;
	}
	
	public int getHeightInMeters() {
		return heightInMeters;
	}

	public int getDistance() {
		return distanceFromBase;
	}

	public void setDistance(int distance) {
		this.distanceFromBase = distance>0? distance : 0;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed>0? speed : 0;
	}
	
	public int getResourcesValue() {
		return resourcesValue;
	}
	
	public int getDangerLevel() {
		return dangerLevel;
	}
	
	@Override 
	public int compareTo(Titan o) {
		return this.distanceFromBase - o.distanceFromBase;
	}	
}

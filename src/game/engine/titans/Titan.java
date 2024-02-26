package game.engine.titans;

public abstract class Titan implements Comparable {
	
	private final int baseHealth;
	private int currentHealth;
	private int baseDamage;
	private final int heightInMeters;
	private int distanceFromBase;
	private int speed;
	private final int resourcesValue;
	private final int dangerLevel;
	
	Titan(int baseHealth, int baseDamage, int heightInMeters, int distanceFromBase, 
			int speed, int resourcesValue, int dangerLevel){
		this.baseHealth = baseHealth;
		currentHealth = baseHealth;
		this.heightInMeters = heightInMeters;
		this.distanceFromBase = distanceFromBase;
		this.speed = speed;
		this.resourcesValue = resourcesValue;
		this.dangerLevel = dangerLevel;	
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public int getDistanceFromBase() {
		return distanceFromBase;
	}

	public void setDistanceFromBase(int distanceFromBase) {
		this.distanceFromBase = distanceFromBase;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getBaseDamage() {
		return baseDamage;
	}
	
	public int getResourcesValue() {
		return resourcesValue;
	}
	
	int compareTo(Titan o) {
		//return this.distanceFromBase>o.distanceFromBase ? 1 : this.distanceFromBase<o.distanceFromBase ? -1 : 0;
		return this.distanceFromBase-distanceFromBase;
	}	
	
}

package game.engine.titans;

public class ArmoredTitan extends Titan {
	
	public static final int TITAN_CODE = 3;
	
	public ArmoredTitan(int baseHealth, int baseDamage, int heightInMeters, int distanceFromBase,
			int speed, int resourcesValue, int dangerLevel){
		super(baseHealth,baseDamage,heightInMeters,distanceFromBase,speed,
				resourcesValue,dangerLevel);
	}	
	public  int takeDamage(int damage) {
		setCurrentHealth(getCurrentHealth() - (1/4) * damage);
		//can we change to double
		return isDefeated() ? getResourcesValue() : 0;
	}
}

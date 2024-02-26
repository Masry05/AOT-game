package game.engine.titans;

public class PureTitan extends Titan {
	
	private static final int TITAN_CODE = 1;
	
	PureTitan(int baseHealth, int baseDamage, int heightInMeters, int distanceFromBase,
			int speed, int resourcesValue, int dangerLevel){
		super(baseHealth,baseDamage,heightInMeters,distanceFromBase,speed,
				resourcesValue,dangerLevel);
	}

	public int compareTo(Object o) {
		return super.compareTo((Titan) o);
	}	
}

package game.engine.weapons;

public class VolleySpreadCannon extends Weapon {
	private static final int WEAPON_CODE=3;
	private final int minRange;
	private final int maxRange;
	
	

	public VolleySpreadCannon(int baseDamage,int minRange,int maxRange) {
		super(baseDamage);
		this.minRange=minRange;
		this.maxRange=maxRange;
	
		
		// TODO Auto-generated constructor stub
	}
	

}

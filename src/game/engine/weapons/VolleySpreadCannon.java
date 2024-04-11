package game.engine.weapons;

import java.util.PriorityQueue;

import game.engine.titans.Titan;

public class VolleySpreadCannon extends Weapon {
	
	public static final int WEAPON_CODE = 3;
	private final int minRange;
	private final int maxRange;
	
	public VolleySpreadCannon(int baseDamage,int minRange,int maxRange) {
		super(baseDamage);
		this.minRange=minRange;
		this.maxRange=maxRange;
	}

	public int getMinRange() {
		return minRange;
	}

	public int getMaxRange() {
		return maxRange;
	}
	
	public int turnAttack(PriorityQueue<Titan> laneTitans) {
		int resources = 0;
		PriorityQueue<Titan> temp = new PriorityQueue<>();
		while(!laneTitans.isEmpty() && laneTitans.peek().getDistance() <= maxRange) {
			Titan titan = laneTitans.poll();
			if( minRange <= titan.getDistance())
				resources += attack(titan);
			if(!titan.isDefeated())
				temp.add(titan);
		}
		while(!temp.isEmpty())
			laneTitans.add(temp.poll());
		return resources;
	}
}

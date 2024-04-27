package game.engine.weapons;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import game.engine.lanes.Lane;
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
		Queue<Titan> temp = new LinkedList<Titan>();
		while(!laneTitans.isEmpty() && laneTitans.peek().getDistance() <= maxRange) {
			Titan titan = laneTitans.poll();
			if( minRange <= titan.getDistance())
				resources += attack(titan);
			if(!titan.isDefeated())
				temp.add(titan);
		}
			laneTitans.addAll(temp);
		return resources;
	}
}

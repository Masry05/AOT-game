package game.engine.weapons;

import java.util.*;

import game.engine.lanes.Lane;
import game.engine.titans.Titan;

public class PiercingCannon extends Weapon {
	
	public static final int WEAPON_CODE = 1;

	public PiercingCannon(int baseDamage) {
		super(baseDamage);
	}

	public int turnAttack(PriorityQueue<Titan> laneTitans) {
		Queue<Titan> temp = new LinkedList<Titan>();
		int resources = 0;
		for(int i=0;i<5 && !laneTitans.isEmpty();i++) {
			Titan titan = laneTitans.poll();
			resources += attack(titan);
			if(!titan.isDefeated())
				temp.add(titan);
		}
			laneTitans.addAll(temp);
		return resources;
	}
	
}

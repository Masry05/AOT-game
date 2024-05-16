
package game.engine.weapons;

import java.util.*;

import game.engine.titans.Titan;

public class WallTrap extends Weapon {
	
	public static final int WEAPON_CODE = 4;

	public WallTrap(int baseDamage) {
		super(baseDamage);
	}
	
	public int turnAttack(PriorityQueue<Titan> laneTitans) {
		int resources = 0;
		if(!laneTitans.isEmpty() && laneTitans.peek().hasReachedTarget()) {
			Titan titan = laneTitans.poll();
			resources += attack(titan);
			if(!titan.isDefeated())
				laneTitans.add(titan);
			else killed++;
		}
		return resources;
	}
}

package game.engine.weapons;

import java.util.*;

import game.engine.titans.Titan;

public class SniperCannon extends Weapon{
	
	public static final int WEAPON_CODE = 2;

	public SniperCannon(int baseDamage) {
		super(baseDamage);
	}
	
	public int turnAttack(PriorityQueue<Titan> laneTitans) {
		int resources = 0;
		if(!laneTitans.isEmpty()) {
			Titan titan = laneTitans.poll();
			resources = attack(titan);
			if(!titan.isDefeated())
				laneTitans.add(titan);
			else killed++;
		}
		return resources;
	}
}

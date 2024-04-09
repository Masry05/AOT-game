package game.engine.weapons;

import java.util.*;
import game.engine.interfaces.Attacker;
import game.engine.titans.*;

public abstract class Weapon implements Attacker{
	
	private final int baseDamage;

	public Weapon(int baseDamage) {
		super();
		this.baseDamage= baseDamage;
	}
	
	public int getDamage() {
		return baseDamage;
	}
	
	public abstract int turnAttack(PriorityQueue<Titan> laneTitans);
}

package game.engine.weapons;

public abstract class Weapon {
	//implements Attacker
	
	private final int baseDamage;

	public Weapon(int baseDamage) {
		this.baseDamage= baseDamage;
	}
	
	public int getBaseDamage() {
		return baseDamage;
	}
}

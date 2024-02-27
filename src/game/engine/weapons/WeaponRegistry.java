package game.engine.weapons;

public class WeaponRegistry {
	private final int code;
	private int price;
	private final int damage;
	private String name;
	private int minRange;
	private int maxRange;

	public WeaponRegistry(int code, int price) {// damage is final so has to be initialized
		this.code = code;
		this.price = price;
	}

	public WeaponRegistry(int code, int price, int damage, String name) {
		this.code = code;
		this.price = price;
		this.damage = damage;
		this.name = name;
	}

	public WeaponRegistry(int code, int price, int damage, String name, int minRange, int maxRange) {
		super();
		this.code = code;
		this.price = price;
		this.damage = damage;
		this.name = name;
		this.minRange = minRange;
		this.maxRange = maxRange;
	}
	public int getCode() {
		return code;
	}

	public int getPrice() {
		return price;
	}	
	
	public int getDamage() {
		return damage;
	}

	public String getName() {
		return name;
	}

	public int getMinRange() {
		return minRange;
	}

	public int getMaxRange() {
		return maxRange;
	}

	


}
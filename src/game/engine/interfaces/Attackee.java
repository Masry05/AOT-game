package game.engine.interfaces;

public interface Attackee {
	int getCurrentHealth();
	void setCurrentHealth(int health);
	int getResourcesValue();
	default boolean isDefeated() {
		return getCurrentHealth()<=0;
	}
	default int takeDamage(int damage) {
		setCurrentHealth(getCurrentHealth()- damage);
		return isDefeated() ? getResourcesValue() : 0;
	}
}

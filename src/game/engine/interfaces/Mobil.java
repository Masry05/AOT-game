package game.engine.interfaces;

public interface Mobil {
	
	int getDistance();
	void setDistance(int distance);
	int getSpeed();
	void setSpeed(int speed);
	
	default boolean hasReachedTarget(){
		return getDistance() <= 0;
	}
	
	default boolean move() {
		setDistance(getDistance() - getSpeed());
		return hasReachedTarget();
	}
	
	
}

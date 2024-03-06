package game.engine.lanes;

import java.util.*;
import game.engine.base.Wall;
import game.engine.titans.Titan;
import game.engine.weapons.Weapon;

public class Lane implements Comparable<Lane>{
	private final Wall laneWall;
	private int dangerLevel;
	private final PriorityQueue<Titan> titans;
	private final ArrayList<Weapon> weapons;
	
	public Lane(Wall laneWall) {
		this.laneWall = laneWall;
		this.dangerLevel = 0;  
		this.titans = new PriorityQueue<Titan>();
		this.weapons = new ArrayList<Weapon>();
	}
	public Wall getLaneWall() {
		return laneWall;
	}
	public int getDangerLevel() {
		return dangerLevel;
	}
	public void setDangerLevel(int dangerLevel) {
		this.dangerLevel = dangerLevel;
	}
	public PriorityQueue <Titan> getTitans() {
		return titans;
	}
	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}
	@Override
	public int compareTo(Lane o) {
		return this.dangerLevel - o.dangerLevel;
	}



	
}
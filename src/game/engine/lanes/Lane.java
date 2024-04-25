package game.engine.lanes;

import java.util.*;
import game.engine.base.Wall;
import game.engine.titans.Titan;
import game.engine.weapons.Weapon;

public class Lane implements Comparable <Lane> {
	private final Wall laneWall;
	private int dangerLevel;
	private final PriorityQueue<Titan> titans;
	private final ArrayList<Weapon> weapons;
	
	public Lane(Wall laneWall) {
		super();
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

	public void addTitan(Titan titan) {
		 titans.add(titan);
	}
	public void addWeapon(Weapon weapon){
		 weapons.add(weapon);
	}

	public boolean isLaneLost() {
		return laneWall.isDefeated();
	}
	
	public void moveLaneTitans() {
		Queue<Titan> temp = new LinkedList<Titan>();
 		while(!titans.isEmpty()) {
		  Titan currentTitan = titans.poll();
		  if(!(currentTitan.hasReachedTarget()))
			  currentTitan.move();
		  temp.add(currentTitan);
		}
		titans.addAll(temp);
     }
	
	public int performLaneTitansAttacks (){
		Queue<Titan> temp = new LinkedList<Titan>();
		int gatheredResources = 0;
		while (!titans.isEmpty()) {
		   Titan currentTitan = titans.poll();
		   temp.add(currentTitan);
		   if (currentTitan.hasReachedTarget()) 
			  gatheredResources += currentTitan.attack(laneWall);     	  
		}
		titans.addAll(temp);
		   return gatheredResources;
	}
	
	public int performLaneWeaponsAttacks() {
		int resourcesGathered=0;
		int length=weapons.size();
		for(int i=0; i< length ;i++) {
		Weapon currentWeapon = weapons.get(i);
		resourcesGathered+=currentWeapon.turnAttack(titans);
	}
		return resourcesGathered;
	}
	
	public void updateLaneDangerLevel() {
		Queue<Titan> temp = new LinkedList<Titan>();
		dangerLevel=0;
		while(!titans.isEmpty()){
		  Titan currentTitan = titans.poll();
		  dangerLevel+= currentTitan.getDangerLevel();
		  temp.add(currentTitan);
		}
		titans.addAll(temp);
	 }
}
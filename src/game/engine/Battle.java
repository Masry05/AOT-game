package game.engine;

import game.engine.weapons.factory.WeaponFactory;
import game.engine.base.Wall;
import game.engine.lanes.Lane;
import game.engine.titans.Titan;
import game.engine.titans.TitanRegistry;
import java.io.IOException;
import java.util.*;
import static game.engine.dataloader.DataLoader.readTitanRegistry;

public class Battle {

	private final static int[][] PHASES_APPROACHING_TITANS = {{ 1, 1, 1, 2, 1, 3, 4 },
															  { 2, 2, 2, 1, 3, 3, 4 },
															  { 4, 4, 4, 4, 4, 4, 4 }};
	private final static int WALL_BASE_HEALTH = 10000;
	private int numberOfTurns;
	private int resourcesGathered;
	private BattlePhase battlePhase;
	private int numberOfTitansPerTurn;
	private int score;
	private int titanSpawnDistance;
	private final WeaponFactory weaponFactory;
	private final HashMap<Integer,TitanRegistry> titansArchives;
	private final ArrayList<Titan> approachingTitans; //gets treated like a queue
	private final PriorityQueue<Lane> lanes;
	private final ArrayList<Lane> originalLanes;
	
	public Battle(int numberOfTurns,int score,int titanSpawnDistance,int initialNumOfLanes, int initialResourcesPerLane)throws IOException{
		super();
		this.numberOfTurns = numberOfTurns;
		this.score = score;
		this.titanSpawnDistance = titanSpawnDistance;
		this.resourcesGathered = initialResourcesPerLane * initialNumOfLanes;
		this.battlePhase = BattlePhase.EARLY;
		this.numberOfTitansPerTurn = 1;
		this.weaponFactory = new WeaponFactory();
		this.titansArchives = readTitanRegistry();
		this.approachingTitans = new ArrayList<Titan> ();
		this.lanes = new PriorityQueue <Lane> ();
		this.originalLanes= new ArrayList <Lane>();
		initializeLanes(initialNumOfLanes);
	}
	
	private void initializeLanes(int numOfLanes) {
		for(int i= numOfLanes ; i>0 ; i--){
			Lane l= new Lane(new Wall(WALL_BASE_HEALTH));
			lanes.add(l);
			originalLanes.add(l);
		}
	}
	
	public void setNumberOfTurns(int numberOfTurns) {
		this.numberOfTurns = numberOfTurns;
	}

	public void setResourcesGathered(int resourcesGathered) {
		this.resourcesGathered = resourcesGathered;
	}

	public void setBattlePhase(BattlePhase battlePhase) {
		this.battlePhase = battlePhase;
	}

	public void setNumberOfTitansPerTurn(int numberOfTitansPerTurn) {
		this.numberOfTitansPerTurn = numberOfTitansPerTurn;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setTitanSpawnDistance(int titanSpawnDistance) {
		this.titanSpawnDistance = titanSpawnDistance;
	}

	public int getNumberOfTurns() {
		return numberOfTurns;
	}

	public int getResourcesGathered() {
		return resourcesGathered;
	}

	public BattlePhase getBattlePhase() {
		return battlePhase;
	}

	public int getNumberOfTitansPerTurn() {
		return numberOfTitansPerTurn;
	}

	public int getScore() {
		return score;
	}

	public int getTitanSpawnDistance() {
		return titanSpawnDistance;
	}

	public WeaponFactory getWeaponFactory() {
		return weaponFactory;
	}

	public HashMap<Integer, TitanRegistry> getTitansArchives() {
		return titansArchives;
	}

	public ArrayList<Titan> getApproachingTitans() {
		return approachingTitans;
	}

	public PriorityQueue<Lane> getLanes() {
		return lanes;
	}

	public ArrayList<Lane> getOriginalLanes() {
		return originalLanes;
	}
	
}

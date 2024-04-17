package game.engine;

import game.engine.weapons.factory.FactoryResponse;
import game.engine.weapons.factory.WeaponFactory;
import game.engine.base.Wall;
import game.engine.exceptions.*;
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
	private final ArrayList<Titan> approachingTitans; 
	private final PriorityQueue<Lane> lanes; //contains only active lanes
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
	public void refillApproachingTitans() {
		int[] phase;
		switch(battlePhase) {
			case EARLY: phase= PHASES_APPROACHING_TITANS[0] ;break;
			case INTENSE: phase= PHASES_APPROACHING_TITANS[1] ;break;
			case GRUMBLING: phase= PHASES_APPROACHING_TITANS[2] ;break;
			default : phase=null;
		}
		for(int i=0;i<phase.length;i++) {
		    TitanRegistry titanType = titansArchives.get(phase[i]);
		    approachingTitans.add(titanType.spawnTitan(titanSpawnDistance));
		}
	}
	public void purchaseWeapon(int weaponCode, Lane lane) throws InsufficientResourcesException,InvalidLaneException{
		if(lane.isLaneLost()) 
			throw new InvalidLaneException();
		FactoryResponse boughtWeapon = weaponFactory.buyWeapon(resourcesGathered,weaponCode);
		resourcesGathered = boughtWeapon.getRemainingResources();
		lane.addWeapon(boughtWeapon.getWeapon());
		performTurn();
	}
	private void addTurnTitansToLane() {
		Lane currentLane= lanes.peek();
		for(int i=0; i<numberOfTitansPerTurn; i++) {
			if(approachingTitans.isEmpty())
				refillApproachingTitans();
			currentLane.addTitan(approachingTitans.remove(0));			
		}
	}
	private void moveTitans() {
		Lane [] temp  = lanes.toArray(new Lane [lanes.size()]);
		for(int i=0;i<temp.length;i++) 
			temp[i].moveLaneTitans();
		for(int i=0;i<temp.length;i++)
			lanes.add(temp[i]);
	}
	private int performWeaponsAttacks() {
		int totalResources=0;
		Lane [] temp  = lanes.toArray(new Lane [lanes.size()]);
		for(int i=0;i<temp.length;i++) 
			totalResources+= temp[i].performLaneWeaponsAttacks();
		for(int i=0;i<temp.length;i++)
			lanes.add(temp[i]);
		score+=totalResources;
		resourcesGathered += totalResources;
		return totalResources;
	}
	private int performTitansAttacks() {//ask what to return exactly
		int totalResources=0;
		PriorityQueue<Lane> temp  =new PriorityQueue<Lane>();
		while(!lanes.isEmpty()) {
			Lane currentLane= lanes.poll();
			int gathered=currentLane.performLaneTitansAttacks();
			totalResources+=gathered;
			if(gathered>=0)
				temp.add(currentLane);
		}
		while(!temp.isEmpty()){
			lanes.add(temp.poll());
		}
		return totalResources;
	}
	private void updateLanesDangerLevels() {
		Lane [] temp  = lanes.toArray(new Lane [lanes.size()]);
		for(int i=0;i<temp.length;i++) { 
		   Lane currentLane = lanes.poll();
		   currentLane.updateLaneDangerLevel();
		}
		for(int i=0;i<temp.length;i++)
			lanes.add(temp[i]);
	}
	private void finalizeTurns() {
		numberOfTurns+=1;
		if(numberOfTurns<15)
			battlePhase=BattlePhase.EARLY;
		else if(numberOfTurns<30)
			battlePhase=BattlePhase.INTENSE;
		else if(numberOfTurns>=30)
			battlePhase=BattlePhase.GRUMBLING;
		if(numberOfTurns>30&&numberOfTurns%5==0)
		    	numberOfTitansPerTurn*=2;
			
	}
	public boolean isGameOver() {
		return lanes.isEmpty();
	}
	public void passTurn() {
		performTurn();
	}
	private void performTurn(){
		moveTitans();
		performWeaponsAttacks();
		updateLanesDangerLevels();
		performTitansAttacks();
		addTurnTitansToLane();
		finalizeTurns();
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

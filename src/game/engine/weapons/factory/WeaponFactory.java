package game.engine.weapons.factory;

import java.io.IOException;
import java.util.*;

import game.engine.exceptions.InsufficientResourcesException;
import game.engine.weapons.*;
import static game.engine.dataloader.DataLoader.readWeaponRegistry;

public class WeaponFactory {

	private final HashMap <Integer,WeaponRegistry> weaponShop;
	
	public WeaponFactory() throws IOException{
		super();
		weaponShop = readWeaponRegistry();
	}

	public HashMap <Integer, WeaponRegistry> getWeaponShop() {
		return weaponShop;
	}
	
	// do we have to remove or just get?
	public FactoryResponse buyWeapon(int resources, int weaponCode) throws InsufficientResourcesException{
		WeaponRegistry weapon = weaponShop.get(weaponCode);
		int remainingResources = resources - weapon.getPrice();
		if(remainingResources<=0) {
			throw new InsufficientResourcesException(resources);
		}
		return new FactoryResponse(weapon.buildWeapon(),remainingResources);
		
	}
	
	public void addWeaponToShop(int code, int price) {
		weaponShop.put(code,new WeaponRegistry(code,price));
	}
	
	public void addWeaponToShop(int code, int price, int damage, String name) {
		weaponShop.put(code,new WeaponRegistry(code,price,damage,name));
	}
	
	public void addWeaponToShop(int code, int price, int damage, String name, int minRange,int maxRange) {
		weaponShop.put(code,new WeaponRegistry(code,price,damage,name,minRange,maxRange));
	}
}

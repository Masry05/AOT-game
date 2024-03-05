package game.engine.weapons.factory;

import java.io.IOException;
import java.util.*;
import game.engine.weapons.WeaponRegistry;
import static game.engine.dataloader.DataLoader.readWeaponRegistry;

public class WeaponFactory {

	private final HashMap<Integer,WeaponRegistry> weaponShop;
	
	public WeaponFactory() throws IOException{
		weaponShop = readWeaponRegistry();
	}

	public HashMap<Integer, WeaponRegistry> getWeaponShop() {
		return weaponShop;
	}
	
}

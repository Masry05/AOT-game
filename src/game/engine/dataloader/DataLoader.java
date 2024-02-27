package game.engine.dataloader;

public class DataLoader {
	private final String TITANS_FILE_NAME="titans.csv";
	private final String WEAPONS_FILE_NAME="weapons.csv";

	public DataLoader() {
		
	}

	public String getTITANS_FILE_NAME() {
		return TITANS_FILE_NAME;
	}

	public String getWEAPONS_FILE_NAME() {
		return WEAPONS_FILE_NAME;
	}

}
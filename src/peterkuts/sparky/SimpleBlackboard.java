package peterkuts.sparky;

import java.io.IOException;
import java.util.HashMap;

import robocode.RobocodeFileOutputStream;

class SimpleBlackboard implements ISparkyBlackborad {

	private HashMap<String, Enemy> enemies;
	
	public SimpleBlackboard() {
		this.enemies = new HashMap<>();
	}
	
	@Override
	public Enemy getEnemyWithName(String name) {
		return enemies.get(name);
	}
	
	public Enemy createEnemyWithName(String name) {
		Enemy enemy = enemies.get(name);
		if (enemy == null) {
			enemy = new Enemy(name);
			enemies.put(name, enemy);
		}
		return enemy;
	}
	
	@Override
	public void init() {
	}

	@Override
	public void run() {
	}

	@Override
	public boolean saveDataToFile(String fullPath) {
		try {
			RobocodeFileOutputStream file = new RobocodeFileOutputStream(fullPath);
			StringBuilder str = new StringBuilder();
			for (Enemy enemy : enemies.values()) {
				str.append("Pack:\n");
				for (EnemyStamp stamp : enemy.getStamps()) {
					str.append(stamp.description());
					str.append("\n");
				}
			}
			byte[] bytes = str.toString().getBytes();
			file.write(bytes, 0, bytes.length);
			file.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}

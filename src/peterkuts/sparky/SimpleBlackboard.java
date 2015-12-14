package peterkuts.sparky;

import java.io.IOException;
import java.util.*;

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
	
	@Override
	public void onPaint(java.awt.Graphics2D g) {
		for (Enemy enemy : enemies.values()) {
			List<EnemyStamp> stamps = enemy.getStamps();
			for (int i = stamps.size() - 1; i >= 0; --i) {
				EnemyStamp stamp = stamps.get(i);
				int posX = (int)stamp.positionX;
				int posY = (int)stamp.positionY;
				double rotation = Math.PI/2-stamp.heading;
				g.translate(posX, posY);
			    g.rotate(rotation);
				g.setColor(java.awt.Color.RED);
			    g.drawRect(-20, -20, 40, 40);
			    g.rotate(-rotation);
				g.translate(-posX, -posY);
			}
		}		
	}
}

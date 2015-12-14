package peterkuts.sparky;

import robocode.*;
//import robocode.util.Utils;

import java.awt.Graphics2D;
//import java.awt.geom.Point2D;
//import java.io.File;
//import java.io.IOException;
//import java.util.*;

public class Sparky extends AdvancedRobot {

	private ISparkyModuleHolder modules;
	
	void init() {
		modules = new SparkyModules(this);
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);
		modules.init();
	}
	
	public void run() {
		init();
		do {
			modules.run();
			execute();
		} while (true);
	}
	
	public void onScannedRobot(ScannedRobotEvent event) {
		modules.getScanner().onScannedRobot(event);
	}
	
	public void onRoundEnded(RoundEndedEvent event) 
	{
		java.io.File d = getDataDirectory();
		modules.getBlackboard().saveDataToFile(String.format("%s/rc-%d.csv", d.getAbsolutePath(), System.currentTimeMillis()));
	}
	
	public void onPaint(Graphics2D g)  {
//		g.setColor(java.awt.Color.RED);
//		double destX = getX() + Math.sin(getGunHeadingRadians())*100;
//		double destY = getY() + Math.cos(getGunHeadingRadians())*100;
//		g.drawLine((int)getX(), (int)getY(), (int)destX, (int)destY);
//		for (Enemy enemy : enemies.values()) {
//			for (int i = enemy.stamps.size() - 1; i >= 0; --i) {
//				EnemyStamp stamp = enemy.stamps.get(i);
//				int posX = (int)stamp.positionX;
//				int posY = (int)stamp.positionY;
//				double rotation = Math.PI/2-stamp.heading;
//				g.translate(posX, posY);
//			    g.rotate(rotation);
//				g.setColor(java.awt.Color.RED);
//			    g.drawRect(-20, -20, 40, 40);
//			    g.rotate(-rotation);
//				g.translate(-posX, -posY);
//			}
//		}		
	}
	
}

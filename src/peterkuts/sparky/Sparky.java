package peterkuts.sparky;

import robocode.*;
import java.awt.Graphics2D;

public class Sparky extends AdvancedRobot {

	private ISparkyModuleHolder modules;

	void init() {
		if (modules == null) {
			modules = new SparkyModules(this);
			modules.init();
		}
	}

	public void run() {
		init();
		do {
			modules.run();
			execute();
		} while (true);
	}

	private String enemyRobotName = "";
	
	public void onScannedRobot(ScannedRobotEvent event) {
		if (enemyRobotName.isEmpty()) {
			enemyRobotName = event.getName();
		}
		modules.getScanner().onScannedRobot(event);
	}
	
	public void onStatus(StatusEvent e) {
		init();
		modules.getScanner().onStatus(e);
	}

	public void onBulletHit(BulletHitEvent event) {
		modules.getGun().onBulletHit(event);
	}
	
	public void onBulletHitBullet(BulletHitBulletEvent event) {
		modules.getGun().onBulletHitBullet(event);
	}
	
	public void onBulletMissed(BulletMissedEvent event) {
		modules.getGun().onBulletMissed(event);
	}
	
	public void onRoundEnded(RoundEndedEvent event) {
		java.io.File d = getDataDirectory();
		if (!d.exists()) {
			try {
				d.mkdir();
			} catch (Exception ex) {
				System.err.println(ex);
				return;
			}
		}
		Storage storage = new Storage(String.format("%s/rc.db", d.getAbsolutePath()));
		storage.initDataBase();
		storage.addBulletData(getName(), enemyRobotName, modules.getGun().getProcessedPredictions());
		storage.closeDataBase();
//		 modules.getGun().saveDataToFile(String.format("%s/gun.csv",d.getAbsolutePath(), System.currentTimeMillis()));
//		 modules.getBlackboard().saveDataToFile(String.format("%s/rc-%d.csv",
//		 d.getAbsolutePath(), System.currentTimeMillis()));
	}
	
	public void onPaint(Graphics2D g) {
		// modules.onPaint(g);
	}

}

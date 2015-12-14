package peterkuts.sparky;

import robocode.*;
import java.awt.Graphics2D;

public class Sparky extends AdvancedRobot {

	private ISparkyModuleHolder modules;

	void init() {
		modules = new SparkyModules(this);
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

	public void onRoundEnded(RoundEndedEvent event) {
		// java.io.File d = getDataDirectory();
		// modules.getBlackboard().saveDataToFile(String.format("%s/rc-%d.csv",
		// d.getAbsolutePath(), System.currentTimeMillis()));
	}

	public void onPaint(Graphics2D g) {
		// modules.onPaint(g);
	}

}

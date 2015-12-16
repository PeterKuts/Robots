package peterkuts;

import robocode.*;

public class AdvTest extends AdvancedRobot {

	public void run() {
		setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		do {
			System.out.println("RUN "+getTime());
			execute();
	    } while (true);
	}
	
	public void onStatus(StatusEvent e) {
		System.out.println("STATUS "+getTime());
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		System.out.println("SCANNED "+getTime());
	}
	
}

package peterkuts;

import robocode.*;
import robocode.util.Utils;

public class AdvTest extends AdvancedRobot {

	public void run() {
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);
		setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		do {
	        scan();
	    } while (true);
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
	    double radarTurn =
	            getHeadingRadians() + e.getBearingRadians()
	            - getRadarHeadingRadians();
	     
	        setTurnRadarRightRadians(1.5*Utils.normalRelativeAngle(radarTurn));
	}
	
}

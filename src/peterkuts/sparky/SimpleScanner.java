package peterkuts.sparky;

import robocode.*;
import robocode.util.Utils;

class SimpleScanner implements ISparkyScanner {

	private ISparkyModuleHolder modules;
	
	public SimpleScanner(ISparkyModuleHolder modules) {
		this.modules = modules;
	}
	
	@Override
	public void init() {
		modules.getRobot().setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
	}

	@Override
	public void run() {
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		AdvancedRobot robot = modules.getRobot();
		double angle = robot.getHeadingRadians() + event.getBearingRadians() - robot.getRadarHeadingRadians();
	    robot.setTurnRadarRightRadians(1.5*Utils.normalRelativeAngle(angle));

	    Enemy enemy = modules.getBlackboard().createEnemyWithName(event.getName());
		enemy.makeStamp(robot, event);
	}

}

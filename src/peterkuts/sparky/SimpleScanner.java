package peterkuts.sparky;

import robocode.*;
import robocode.util.Utils;

class SimpleScanner implements ISparkyScanner {

	private ISparkyModuleHolder modules;
	private Enemy lastTrackedEnemy;

	public SimpleScanner(ISparkyModuleHolder modules) {
		this.modules = modules;
	}

	public void init() {
		AdvancedRobot robot = modules.getRobot();
		robot.setAdjustRadarForRobotTurn(true);
		robot.setAdjustRadarForGunTurn(true);
		robot.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		robot.scan();
	}

	public Enemy getLastTrackedEnemy() {
		return lastTrackedEnemy;
	}

	public void run() {
	}

	public void onPaint(java.awt.Graphics2D g) {
	}

	public void onScannedRobot(ScannedRobotEvent event) {
		AdvancedRobot robot = modules.getRobot();
		double angle = robot.getHeadingRadians() + event.getBearingRadians() - robot.getRadarHeadingRadians();
		robot.setTurnRadarRightRadians(1.5 * Utils.normalRelativeAngle(angle));

		Enemy enemy = modules.getBlackboard().createEnemyWithName(event.getName());
		enemy.makeStamp(robot, event);
		lastTrackedEnemy = enemy;
	}

}

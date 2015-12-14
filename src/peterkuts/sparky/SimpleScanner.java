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
		modules.getRobot().setAdjustRadarForRobotTurn(true);
		modules.getRobot().setAdjustRadarForGunTurn(true);
		modules.getRobot().setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
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
	}

}

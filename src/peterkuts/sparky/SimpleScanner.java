package peterkuts.sparky;

import robocode.*;
import robocode.util.Utils;

class SimpleScanner implements ISparkyScanner {

	enum State {
		Searching,
		Tracking
	}

	private ISparkyModuleHolder modules;
	private Enemy lastTrackedEnemy;
	private State state;
	
	public SimpleScanner(ISparkyModuleHolder modules) {
		this.modules = modules;
		this.state = State.Searching;
	}

	public void init() {
		AdvancedRobot robot = modules.getRobot();
		robot.setAdjustRadarForRobotTurn(true);
		robot.setAdjustRadarForGunTurn(true);
	}

	public Enemy getLastTrackedEnemy() {
		return lastTrackedEnemy;
	}

	public void run() {
		switch (state) {
		case Searching:
			sSearching();
			break;
		case Tracking:
			sTracking();
			break;
		default:
			state = State.Searching;
			break;
		}
	}
	
	void sSearching() {
		modules.getRobot().setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
	}

	void sTracking() {
	}

	public void onStatus(StatusEvent e) {
		lastTrackedEnemy = null;
		state = State.Searching;
	}
	
	public void onScannedRobot(ScannedRobotEvent event) {
		state = State.Tracking;
		AdvancedRobot robot = modules.getRobot();
		double angle = robot.getHeadingRadians() + event.getBearingRadians() - robot.getRadarHeadingRadians();
		angle = Utils.normalRelativeAngle(angle);
		double sign = Math.signum(angle);
		if (sign == 0) {
			sign = 1;
		}
		angle += sign*Math.PI/10;
		robot.setTurnRadarRightRadians(Utils.normalRelativeAngle(angle));
		Enemy enemy = modules.getBlackboard().createEnemyWithName(event.getName());
		enemy.makeStamp(robot, event);
		lastTrackedEnemy = enemy;
	}

	public void onPaint(java.awt.Graphics2D g) {
	}

}

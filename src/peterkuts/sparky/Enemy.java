package peterkuts.sparky;

import java.util.*;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

class Enemy {
	private String name;
	private List<EnemyStamp> stamps;
	private EnemyStamp lastStamp;

	public String getName() {
		return name;
	}

	public EnemyStamp getLastStamp() {
		return lastStamp;
	}

	public List<EnemyStamp> getStamps() {
		return stamps;
	}

	public Enemy(String name) {
		stamps = new ArrayList<>();
	}

	public void makeStamp(AdvancedRobot robot, ScannedRobotEvent enemyRobot) {
		double prevHeading = 0;
		if (lastStamp != null) {
			prevHeading = lastStamp.heading;
		} else {
			prevHeading = enemyRobot.getHeadingRadians();
		}
		double angle = robot.getHeadingRadians() + enemyRobot.getBearingRadians();
		lastStamp = new EnemyStamp(enemyRobot.getTime(), enemyRobot.getEnergy(),
				robot.getX() + enemyRobot.getDistance() * Math.sin(angle),
				robot.getY() + enemyRobot.getDistance() * Math.cos(angle), enemyRobot.getHeadingRadians(),
				enemyRobot.getVelocity(), Utils.normalRelativeAngle(enemyRobot.getHeadingRadians() - prevHeading));
		stamps.add(lastStamp);
	}
}

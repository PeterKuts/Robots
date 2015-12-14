package peterkuts.sparky;

import java.awt.geom.Point2D;

import robocode.*;
import robocode.util.Utils;

class SimpleGun implements ISparkyGun {
	enum State {
		Waiting, Aiming, Firing
	}

	class Prediction {
		double posX;
		double posY;
		double heading;
		double gunBearing;
		double bulletEnegry;
		double time;
	}

	private ISparkyModuleHolder modules;
	private State state;
	private Prediction currentPrediction;

	public SimpleGun(ISparkyModuleHolder modules) {
		this.modules = modules;
		state = State.Waiting;
	}

	public void init() {
		modules.getRobot().setAdjustGunForRobotTurn(true);
	}

	public void run() {
		switch (state) {
		case Waiting:
			sWaiting();
			break;
		case Aiming:
			sAiming();
			break;
		case Firing:
			sFiring();
			break;
		default:
			sUnknown();
			break;
		}
	}

	void sWaiting() {
		Enemy enemy = modules.getScanner().getLastTrackedEnemy();
		if (enemy == null) {
			return;
		}
		EnemyStamp stamp = enemy.getLastStamp();
		if (stamp == null) {
			return;
		}
		currentPrediction = predict(modules.getRobot(), stamp);
		if (currentPrediction == null) {
			return;
		}
		modules.getRobot().setTurnGunRightRadians(currentPrediction.gunBearing);
		state = State.Aiming;
	}

	void sAiming() {
		if (currentPrediction == null) {
			state = State.Waiting;
			return;
		}
		if (modules.getRobot().getGunTurnRemainingRadians() == 0) {
			state = State.Firing;
		}
	}

	void sFiring() {
		if (currentPrediction == null) {
			state = State.Waiting;
			return;
		}
		modules.getRobot().setFire(currentPrediction.bulletEnegry);
		state = State.Waiting;
	}

	void sUnknown() {
		state = State.Waiting;
	}

	public void onPaint(java.awt.Graphics2D g) {
		AdvancedRobot robot = modules.getRobot();
		g.setColor(java.awt.Color.RED);
		double destX = robot.getX() + Math.sin(robot.getGunHeadingRadians()) * 100;
		double destY = robot.getY() + Math.cos(robot.getGunHeadingRadians()) * 100;
		g.drawLine((int) robot.getX(), (int) robot.getY(), (int) destX, (int) destY);
	}

	Prediction predict(AdvancedRobot robot, EnemyStamp stamp) {
		double posX = robot.getX();
		double posY = robot.getY();
		double gunRadians = robot.getGunHeadingRadians();
		double eX = stamp.positionX;
		double eY = stamp.positionY;
		double eH = stamp.heading;
		double eV = stamp.velocity;
		double eA = stamp.angular;
		double time = 0;
		double bulletEnergy = 3;
		double w = 0;
		double h = 0;
		double W = robot.getBattleFieldWidth() - w;
		double H = robot.getBattleFieldHeight() - h;

		double angle = 0;
		double rotationT = 0;
		double bulletT = 0;
		boolean run = true;
		while (run) {
			time++;
			eX += eV * Math.sin(eH);
			eY += eV * Math.cos(eH);
			if (w > eX || eX > W || h > eY || eY > H) {
				eX = Math.max(w, Math.min(eX, W));
				eY = Math.max(h, Math.min(eY, H));
				run = false;
			}
			eH += eA;
			angle = Utils.normalRelativeAngle(Math.atan2(eX - posX, eY - posY) - gunRadians);
			rotationT = Math.abs(angle) / Constants.getGunRotationSpeed();
			bulletT = Point2D.distance(posX, posY, eX, eY) / Constants.getBulletSpeed(bulletEnergy);
			run &= time < rotationT + bulletT;
			run &= time < 50;
		}
		;
		Prediction result = new Prediction();
		result.posX = eX;
		result.posY = eY;
		result.heading = eH;
		result.bulletEnegry = bulletEnergy;
		result.time = time;
		result.gunBearing = angle;
		return result;
	}

	// public List<EnemyStamp> findEnemyPattern(int patternSize) {
	// int count = Math.max(patternSize, 1);
	// if (stamps.size() < count) {
	// return stamps;
	// }
	// if (stamps.size() < 2*count) {
	// return stamps.subList(0, count);
	// }
	// int baseIdx = stamps.size() - count;
	// int patternField = stamps.size() - 2*count;
	// double minV = 1e6;
	// int idx = 0;
	// for (int i = 0; i <= patternField; ++i) {
	// for (int j = 0; j < count; ++j) {
	// EnemyStamp st1 = stamps.get(i);
	// EnemyStamp st2 = stamps.get(baseIdx+j);
	// double v = Math.abs(st1.velocity - st2.velocity) + Math.abs(st1.heading -
	// st2.heading);
	// if (v < minV) {
	// minV = v;
	// idx = i;
	// }
	// }
	// }
	// return stamps.subList(idx, count);
	// }

}

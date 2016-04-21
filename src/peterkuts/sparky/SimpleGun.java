package peterkuts.sparky;

import java.awt.geom.Point2D;
import java.util.*;

import robocode.*;
import robocode.util.Utils;

class SimpleGun implements ISparkyGun {
	enum State {
		Waiting, Aiming, Firing
	}
	
	private Random rand = new Random(System.currentTimeMillis());

	class Prediction {
		Prediction(double bulletEnergy) {
			this.bulletEnegry = bulletEnergy;
			this.fire = false;
			this.didHit = false;
			this.obj = rand.nextDouble();
		}
		
		boolean fire;
		double gunBearing;
		double bulletEnegry;
		double time;
		double bulletTime;
		boolean didHit;
		
		double obj;
		double objective() {
            return obj;//this.fire?bulletEnegry:0;
		}
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

	private HashMap<Bullet, Prediction> bulletMap = new HashMap<Bullet, Prediction>();
	private List<Prediction> processedPredictions = new ArrayList<Prediction>();
	
	void sFiring() {
		if (currentPrediction == null) {
			state = State.Waiting;
			return;
		}
		Bullet bullet = modules.getRobot().setFireBullet(currentPrediction.bulletEnegry);
		bulletMap.put(bullet, currentPrediction);
		currentPrediction = null;
		state = State.Waiting;
	}

	void sUnknown() {
		state = State.Waiting;
	}

	public void onBulletHit(BulletHitEvent event) {
		Bullet b = event.getBullet();
		Prediction p = bulletMap.get(b);
		p.didHit = true;
		processedPredictions.add(p);
		bulletMap.remove(b);
	}
	
	public void onBulletHitBullet(BulletHitBulletEvent event) {
		Bullet b = event.getBullet();
		bulletMap.remove(b);
	}
	
	public void onBulletMissed(BulletMissedEvent event) {
		Bullet b = event.getBullet();
		Prediction p = bulletMap.get(b);
		p.didHit = false;
		processedPredictions.add(p);
		bulletMap.remove(b);
	}

	public java.util.List<SimpleGun.Prediction> getProcessedPredictions() {
		return processedPredictions;
	}

	public boolean saveDataToFile(String fullPath) {
		try {
			RobocodeFileOutputStream file = new RobocodeFileOutputStream(fullPath, true);
			StringBuilder str = new StringBuilder();
			for (Prediction p : processedPredictions) {
				str.append(Math.round(p.bulletTime));
				str.append(',');
				str.append(p.didHit?1:0);
				str.append('\n');
			}
			byte[] bytes = str.toString().getBytes();
			file.write(bytes, 0, bytes.length);
			file.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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
		double w = 0;
		double h = 0;
		double W = robot.getBattleFieldWidth() - w;
		double H = robot.getBattleFieldHeight() - h;

		double time = 0;
		double maxTime = 1000;
		
		List<Prediction> predictions = new ArrayList<>();
		predictions.add(new Prediction(1));
		predictions.add(new Prediction(2));
		predictions.add(new Prediction(3));
		
		boolean run = true;
		do {
			time++;
			eX += eV * Math.sin(eH);
			eY += eV * Math.cos(eH);
			if (w > eX || eX > W || h > eY || eY > H) {
				eX = Math.max(w, Math.min(eX, W));
				eY = Math.max(h, Math.min(eY, H));
				run = false;
			}
			eH += eA;
			double angle = Utils.normalRelativeAngle(Math.atan2(eX - posX, eY - posY) - gunRadians);
			double rotationT = Math.abs(angle) / Rules.GUN_TURN_RATE_RADIANS;
			boolean allPredicitonsDone = true;
			for (Prediction prediction: predictions) {
				if (prediction.fire) {
					continue;
				}
				double bulletT = Point2D.distance(posX, posY, eX, eY) 
						/ Rules.getBulletSpeed(prediction.bulletEnegry);
				double hitT = rotationT + bulletT;
				prediction.gunBearing = angle;
				prediction.time = hitT;
				prediction.bulletTime = bulletT;
				if (hitT <= time) {
					prediction.fire = true;
				} else {
					allPredicitonsDone = false;
				}
			}
			run &= !allPredicitonsDone && time < maxTime;
		} while (run);
		Optional<Prediction> max = predictions.stream().max(new Comparator<Prediction>() {
			public int compare(Prediction o1, Prediction o2) {
				return (int)Math.signum(o1.objective() - o2.objective());
			}
		});
		return max.isPresent()? max.get(): null;
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

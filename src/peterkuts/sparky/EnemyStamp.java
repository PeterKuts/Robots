package peterkuts.sparky;

import robocode.util.Utils;

class EnemyStamp {
	long time;
	double energy;
	double positionX;
	double positionY;
	double heading;
	double velocity;
	double angular;
	
	public EnemyStamp(
			long time,
			double energy,
			double positionX, 
			double positionY,
			double heading,
			double velocity,
			double angular) {
		this.time = time;
		this.energy = energy;
		this.positionX = positionX;
		this.positionY = positionY;
		this.heading = heading;
		this.velocity = velocity;
		this.angular = Utils.normalRelativeAngle(angular);
	}
	
	public double distance(EnemyStamp stamp) {
		return Math.abs(velocity-stamp.velocity) + Math.abs(angular-stamp.angular);
	}
	
	public String description() {
		return String.format("%d\t%f\t%f", time, velocity, angular);
	}
}

package peterkuts.sparky;

class Constants {

	public static double getRadarRotationSpeed() {
		return Math.PI/4; //45 deg
	}

	public static double getGunRotationSpeed() {
		return Math.PI/9; //20 deg
	}

	public static double getBulletSpeed(double energy) {
		return 20 - 3 * energy;
	}

}

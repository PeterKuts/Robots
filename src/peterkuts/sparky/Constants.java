package peterkuts.sparky;

class Constants {

	public static double getGunRotationSpeed() {
		return 20;
	}

	public static double getBulletSpeed(double energy) {
		return 20 - 3 * energy;
	}

}

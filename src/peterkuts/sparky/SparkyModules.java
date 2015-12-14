package peterkuts.sparky;

import robocode.*;

class SparkyModules implements ISparkyModuleHolder {

	private AdvancedRobot robot;
	private ISparkyScanner scanner;
	private ISparkyGun gun;
	private ISparkyDriver driver;
	private ISparkyBlackborad blackboard;

	public SparkyModules(AdvancedRobot robot) {
		this.robot = robot;
		blackboard = new SimpleBlackboard();
		scanner = new SimpleScanner(this);
		gun = new SimpleGun(this);
		driver = new SimpleDriver(this);
	}

	public void init() {
		scanner.init();
		gun.init();
		driver.init();
		blackboard.init();
	}

	public void run() {
		scanner.run();
		gun.run();
		driver.run();
		blackboard.run();
	}

	public void onPaint(java.awt.Graphics2D g) {
		scanner.onPaint(g);
		gun.onPaint(g);
		driver.onPaint(g);
		blackboard.onPaint(g);
	}

	public AdvancedRobot getRobot() {
		return robot;
	}

	public ISparkyScanner getScanner() {
		return scanner;
	}

	public ISparkyGun getGun() {
		return gun;
	}

	public ISparkyDriver getDriver() {
		return driver;
	}

	public ISparkyBlackborad getBlackboard() {
		return blackboard;
	}

}

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
	
	@Override
	public void init() {
		scanner.init();
		gun.init();
		driver.init();
		blackboard.init();
	}
	
	@Override
	public void run() {
		scanner.run();
		gun.run();
		driver.run();
		blackboard.run();
	}
	
	@Override
	public void onPaint(java.awt.Graphics2D g) {
		scanner.onPaint(g);
		gun.onPaint(g);
		driver.onPaint(g);
		blackboard.onPaint(g);
	}

	@Override
	public AdvancedRobot getRobot() {
		return robot;
	}

	@Override
	public ISparkyScanner getScanner() {
		return scanner;
	}

	@Override
	public ISparkyGun getGun() {
		return gun;
	}

	@Override
	public ISparkyDriver getDriver() {
		return driver;
	}

	@Override
	public ISparkyBlackborad getBlackboard() {
		return blackboard;
	}

}

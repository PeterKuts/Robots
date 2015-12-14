package peterkuts.sparky;

interface ISparkyModule {
	void init();
	void run();
	void onPaint(java.awt.Graphics2D g);
}

interface ISparkyModuleHolder extends ISparkyModule {
	robocode.AdvancedRobot getRobot();
	ISparkyScanner getScanner();
	ISparkyGun getGun();
	ISparkyDriver getDriver();
	ISparkyBlackborad getBlackboard();
}

interface ISparkyScanner extends ISparkyModule {
	void onScannedRobot(robocode.ScannedRobotEvent event);
}

interface ISparkyGun extends ISparkyModule {
}

interface ISparkyDriver extends ISparkyModule {
}

interface ISparkyBlackborad extends ISparkyModule {
	Enemy getEnemyWithName(String name);
	Enemy createEnemyWithName(String name);
	boolean saveDataToFile(String fullPath);
}

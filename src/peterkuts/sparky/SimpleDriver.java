package peterkuts.sparky;

class SimpleDriver implements ISparkyDriver {

	private ISparkyModuleHolder modules;
	
	public SimpleDriver(ISparkyModuleHolder modules) {
		this.modules = modules;
	}

	@Override
	public void init() {
	}

	@Override
	public void run() {
	}

}

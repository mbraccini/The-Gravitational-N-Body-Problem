package nbody_pap1314.controller.implementation;

import nbody_pap1314.controller.interfaces.InputListener;

/**
 * Concrete implementation of the InputListener interface.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class Controller implements InputListener {

	/* Monitor that receives the events by the controller */
	protected CommandMonitor monitor;

	public Controller(CommandMonitor monitor) {
		this.monitor = monitor;
	}

	/**
	 * Notifies the start event to the monitor, along with delta time info.
	 */
	public void start(double deltaTime) {
		log("'Start' with delta time (seconds): " + deltaTime);
		monitor.start(deltaTime);
	}

	/**
	 * Notifies the stop event to the monitor.
	 */
	public void stop() {
		log("'Stop'");
		monitor.stop();
	}

	/**
	 * Notifies the pause event to the monitor.
	 */
	public void pause() {
		log("'Pause'");
		monitor.pause();
	}

	/**
	 * Notifies the resume event to the monitor, along with delta time info.
	 */
	public void resume(double deltaTime) {
		log("'Resume' with delta time (seconds): " + deltaTime);
		monitor.resume(deltaTime);
	}

	private void log(String msg) {
		System.out.println("[" + this.getClass().getSimpleName() + "] " + msg);
	}

}

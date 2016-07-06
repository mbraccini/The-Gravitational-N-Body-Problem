package nbody_pap1314.controller.implementation;

/**
 * Monitor that manages the execution.
 * The execution is possible if and only if the condition "start" is true.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class CommandMonitor {

	/* condition which represents the ability to perform the execution. */
	private volatile boolean start;

	/* condition which represents the inability to perform the execution. */
	private volatile boolean stop;

	/* delta time that will be used for the execution. */
	private double deltaTime;

	public CommandMonitor() {
		this.start = false;
		this.stop = false;
		this.deltaTime = 0d;
	}

	/**
	 * Method which gives or not the possibility of running.
	 * If "stop" is true returns zero.
	 * 
	 * @return delta time for the execution
	 */
	public synchronized double canLoop() {
		while(!start && !stop) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		if (stop) {
			start = false;
			stop = false;
		}
		return deltaTime;	/* if stop is true, returns zero */
	}

	/**
	 * Sets the start condition to true, and also sets the deltaTime.
	 * 
	 * @param deltaTime
	 */
	public synchronized void start(double deltaTime) {
		if(!start && (deltaTime != 0d)) {
			start = true;
			stop = false;
			this.deltaTime = deltaTime;
			notify();
		}
	}

	/**
	 * Sets the stop condition to true.
	 */
	public synchronized void stop() {
		if(!stop) {
			start = false;
			stop = true;
			deltaTime = 0d;
			notify();
		}
	}

	/**
	 * Sets the start condition to false.
	 */
	public synchronized void pause() {
		if(start) {
			start = false;
		}
	}

	/**
	 * Invokes the start method with a new deltaTime value.
	 * 
	 * @param deltaTime
	 */
	public synchronized void resume(double deltaTime) {
		start(deltaTime);
	}

}

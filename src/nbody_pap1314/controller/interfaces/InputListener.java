package nbody_pap1314.controller.interfaces;

/**
 * Listener of input events.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public interface InputListener {

	/**
	 * Notifies the start event.
	 * 
	 * @param deltaTime Delta time setted for the simulation.
	 */
	void start(double deltaTime);

	/**
	 * Notifies the stop event.
	 */
	void stop();

	/**
	 * Notifies the pause event.
	 */
	void pause();

	/**
	 * Notifies the resume event.
	 * 
	 * @param deltaTime new delta time for the simulation.
	 */
	void resume(double deltaTime);

}

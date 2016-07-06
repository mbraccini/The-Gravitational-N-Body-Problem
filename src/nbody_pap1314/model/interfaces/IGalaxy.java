package nbody_pap1314.model.interfaces;

/**
 * Interface of a two-dimensional galaxy as a huge collection of two-dimensional
 * astronomical bodies.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public interface IGalaxy {

	/**
	 * Returns the Body with the specified index in the Galaxy.
	 */
	public IBody getBody(int bodyIndex);

	/**
	 * Returns the current number of bodies in the Galaxy.
	 */
	public int getNumBodies();

	/**
	 * Returns true if the Galaxy is empty, false otherwise.
	 */
	public boolean isEmpty();

	/**
	 * Adds a Body to the Galaxy, updating the galaxy radius.
	 */
	public boolean put(IBody body);

	public double getRadius();

	public void setRadius(double radius);

}

package nbody_pap1314.model.implementation;

import java.util.ArrayList;

import nbody_pap1314.model.interfaces.IBody;
import nbody_pap1314.model.interfaces.IGalaxy;

/**
 * Implementation of a two-dimensional galaxy as a huge collection of
 * two-dimensional astronomical bodies.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class Galaxy implements IGalaxy {

	protected ArrayList<IBody> bodies;	/* collection of astronomical bodies.*/
	protected double radius;			/* the galaxy radius */

	/**
	 * Creates a new empty Galaxy.
	 */
	public Galaxy() {
		this.bodies = new ArrayList<IBody>();
		this.radius = 0d;
	}

	/**
	 * Returns the Body with the specified index in the Galaxy.
	 */
	public IBody getBody(int bodyIndex) {
		return bodies.get(bodyIndex);
	}

	/**
	 * Returns the current number of bodies in the Galaxy.
	 */
	public int getNumBodies() {
		return bodies.size();
	}

	/**
	 * Returns true if the Galaxy is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return bodies.isEmpty();
	}

	/**
	 * Adds a Body to the Galaxy, updating the galaxy radius.
	 */
	public boolean put(IBody body) {
		if(bodies.add(body)) {
			double limitX = Math.abs(body.getPosition().getX());
			if (limitX > radius) {
				radius = limitX;
			}
			double limitY = Math.abs(body.getPosition().getY());
			if (limitY > radius) {
				radius = limitY;
			}
			return true;
		}
		return false;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

}

package nbody_pap1314.model.implementation;

import nbody_pap1314.model.interfaces.IBody;
import nbody_pap1314.model.interfaces.IVector;

/**
 * Implementation of a two-dimensional astronomical body with mass, position
 * and speed.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class Body implements IBody {

	protected double mass;
	protected IVector position;
	protected IVector speed;

	/**
	 * Creates a new Body with the specified mass, position and speed.
	 */
	public Body(double mass, IVector position, IVector speed) {
		this.mass = mass;
		this.position = position;
		this.speed = speed;
	}

	public double getMass() {
		return mass;
	}

	public IVector getPosition() {
		return position;
	}

	public IVector getSpeed() {
		return speed;
	}

	public void setPosition(IVector position) {
		this.position = position;
	}

	public void setSpeed(IVector speed) {
		this.speed = speed;
	}

	@Override
	public String toString() {
		return  this.getClass().getSimpleName() +
				"(" + mass + "," + position + "," + speed + ")";
	}

}

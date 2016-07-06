package nbody_pap1314.model.implementation;

import nbody_pap1314.model.interfaces.IBodyUpdate;
import nbody_pap1314.model.interfaces.IVector;

/**
 * Implementation of a Body update in position and speed.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class BodyUpdate implements IBodyUpdate {

	protected final int bodyIndex;	/* index that identifies a Body in a Galaxy */
	protected final IVector position;	/* the new position of the Body */
	protected final IVector speed;	/* the new speed of the Body */

	/**
	 * Creates a new Body update in position and speed for the Body specified
	 * by bodyIndex.
	 */
	public BodyUpdate(int bodyIndex, IVector position, IVector speed) {
		this.bodyIndex = bodyIndex;
		this.position = position;
		this.speed = speed;
	}

	public int getBodyIndex() {
		return bodyIndex;
	}

	public IVector getPosition() {
		return position;
	}

	public IVector getSpeed() {
		return speed;
	}

	@Override
	public String toString() {
		return  this.getClass().getSimpleName() +
				"(" + bodyIndex + "," + position + "," + speed + ")";
	}

}

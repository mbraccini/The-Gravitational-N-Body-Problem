package nbody_pap1314.view.implementation;

import nbody_pap1314.view.interfaces.IBodyUpdateView;

/**
 * Concrete implementations of IBodyUpdateView.
 * Represents a textual update of a body position.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class BodyConsoleView implements IBodyUpdateView {

	protected double x; 		/* the new x coordinate */
	protected double y; 		/* the new y coordinate */

	public BodyConsoleView(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the X value of body, for a Cartesian representation.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the Y value of body, for a Cartesian representation.
	 */
	public double getY() {
		return y;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + x + "," + y + ")";
	}

}

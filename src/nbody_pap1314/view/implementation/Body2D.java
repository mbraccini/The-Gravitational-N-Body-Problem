package nbody_pap1314.view.implementation;

import nbody_pap1314.view.interfaces.IBodyUpdateView;

/**
 * Concrete implementations of IBodyUpdateView.
 * Represents a graphical 2D update of a body.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class Body2D implements IBodyUpdateView {

	protected double x; 		/* the new x coordinate */
	protected double y; 		/* the new y coordinate */
	protected int dimension; 	/* the dimension of the body */

	public Body2D(double x, double y, int dimension) {
		this.x = x;
		this.y = y;
		this.dimension = dimension;
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

	/**
	 * Returns the dimension of a body, for display purpose.
	 */
	public int getDimension() {
		return dimension;
	}

	@Override
	public String toString() {
		return	this.getClass().getSimpleName() +
				"(" + x + "," + y + "," + dimension + ")";
	}

}

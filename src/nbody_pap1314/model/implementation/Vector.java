package nbody_pap1314.model.implementation;

import nbody_pap1314.model.interfaces.IVector;

/**
 * Implementation of a two-dimensional vector.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class Vector implements IVector {

	protected double x;		/* the x component */
	protected double y;		/* the y component */

	/**
	 * Creates a new Vector with the specified components.
	 */
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Returns the Vector magnitude.
	 */
	public double getMagnitude() {
		return Math.sqrt(scalarProduct(this));
	}

	/**
	 * Returns the versor.
	 */
	public IVector getVersor() throws Exception {
		if (getMagnitude() == 0d) {
			throw new Exception();		/* null vector */
		}
		return scale(1d / getMagnitude());
	}

	/**
	 * Returns the scalar product of this Vector and the specified one.
	 */
	public double scalarProduct(IVector vector) {
		return getX() * vector.getX() + getY() * vector.getY();
	}

	/**
	 * Returns the distance between this Vector and the specified one.
	 */
	public double distance(IVector vector) {
		return sub(vector).getMagnitude();
	}

	/**
	 * Returns the sum of this Vector and the specified one.
	 */
	public IVector sum(IVector vector) {
		return new Vector(getX() + vector.getX(), getY() + vector.getY());
	}

	/**
	 * Returns the difference of this Vector and the specified one.
	 */
	public IVector sub(IVector vector) {
		return new Vector(getX() - vector.getX(), getY() - vector.getY());
	}

	/**
	 * Returns this Vector scaled of the specified value.
	 */
	public IVector scale(double value) {
		return new Vector(getX() * value, getY() * value);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + x + "," + y + ")";
	}

}

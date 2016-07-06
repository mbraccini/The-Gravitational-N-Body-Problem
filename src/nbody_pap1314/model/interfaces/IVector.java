package nbody_pap1314.model.interfaces;

/**
 * Interface of a two-dimensional vector.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public interface IVector {

	public double getX();

	public double getY();

	public void setX(double x);

	public void setY(double y);

	/**
	 * Returns the Vector magnitude.
	 */
	public double getMagnitude();

	/**
	 * Returns the versor.
	 */
	public IVector getVersor() throws Exception;

	/**
	 * Returns the scalar product of this Vector and the specified one.
	 */
	public double scalarProduct(IVector vector);

	/**
	 * Returns the distance between this Vector and the specified one.
	 */
	public double distance(IVector vector);

	/**
	 * Returns the sum of this Vector and the specified one.
	 */
	public IVector sum(IVector vector);

	/**
	 * Returns the difference of this Vector and the specified one.
	 */
	public IVector sub(IVector vector);

	/**
	 * Returns this Vector scaled of the specified value.
	 */
	public IVector scale(double value);

}

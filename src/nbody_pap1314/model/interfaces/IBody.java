package nbody_pap1314.model.interfaces;

/**
 * Interface of a two-dimensional astronomical body with mass, position and
 * speed.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public interface IBody {

	public double getMass();

	public IVector getPosition();

	public IVector getSpeed();

	public void setPosition(IVector position);

	public void setSpeed(IVector speed);

}

package nbody_pap1314.model.interfaces;

/**
 * Interface of a Body update in position and speed.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public interface IBodyUpdate {

	public int getBodyIndex();

	public IVector getPosition();

	public IVector getSpeed();

}

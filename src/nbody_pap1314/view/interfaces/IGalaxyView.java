package nbody_pap1314.view.interfaces;

import nbody_pap1314.model.interfaces.IBody;

/**
 * Interface for any galaxy representation.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public interface IGalaxyView {
	
	/**
	 * Add body to view.
	 * 
	 * @param myBody  involved body
	 * @param indexBody involved body's index
	 */
	public void addBody(IBody myBody, int indexBody);
	
	/**
	 *  Refresh the view.
	 */
	public void update();

}

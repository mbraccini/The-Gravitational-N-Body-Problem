package nbody_pap1314.simulator;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import nbody_pap1314.model.interfaces.IBody;
import nbody_pap1314.model.interfaces.IGalaxy;
import nbody_pap1314.view.interfaces.IGalaxyView;

/**
 * This task updates the graphical representation of a Body.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class UpdateBody2DTask implements Callable<Void> {

	protected final IGalaxy galaxy;			/* galaxy */
	protected final int bodyIndex;			/* index of the body to be updated */
	protected final IGalaxyView galaxy2D;	/* graphical representation of the galaxy */
	protected final CountDownLatch doneSignal;

	public UpdateBody2DTask(IGalaxy galaxy, int bodyIndex, IGalaxyView galaxy2D, CountDownLatch doneSignal) {
		this.galaxy = galaxy;
		this.bodyIndex = bodyIndex;
		this.galaxy2D = galaxy2D;
		this.doneSignal = doneSignal;
	}

	public Void call() {
		IBody myBody = galaxy.getBody(bodyIndex);
		galaxy2D.addBody(myBody, bodyIndex);	/* updates a body */
		doneSignal.countDown();					/* task done! */
		return null;
	}

}

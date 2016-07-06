package nbody_pap1314.simulator;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import nbody_pap1314.model.implementation.BodyUpdate;
import nbody_pap1314.model.implementation.Vector;
import nbody_pap1314.model.interfaces.IBody;
import nbody_pap1314.model.interfaces.IGalaxy;
import nbody_pap1314.model.interfaces.IBodyUpdate;
import nbody_pap1314.model.interfaces.IVector;

/**
 * This task computes and update the position and speed of a Body.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class UpdateBodyTask implements Callable<IBodyUpdate> {

	protected final IGalaxy galaxy;				/* galaxy */
	protected final int bodyIndex;				/* index of body to be updated */
	protected final double deltaTimeSeconds;	/* delta time in seconds */
	protected final CountDownLatch doneSignal;

	public UpdateBodyTask(IGalaxy galaxy, int bodyIndex, double deltaTimeSeconds, CountDownLatch doneSignal) {
		this.galaxy = galaxy;
		this.bodyIndex = bodyIndex;
		this.deltaTimeSeconds = deltaTimeSeconds;
		this.doneSignal = doneSignal;
	}

	public IBodyUpdate call() {
		final IBody myBody = galaxy.getBody(bodyIndex);
		final double myMass = myBody.getMass();
		final IVector myPosition = myBody.getPosition();
		final IVector mySpeed = myBody.getSpeed();
		
		IVector totalForce = new Vector(0,0);
		
		for(int i = 0; i < galaxy.getNumBodies(); i++) {
			if (i == bodyIndex) {
				continue;
			}
			final IBody otherBody = galaxy.getBody(i);
			final IVector otherPosition = otherBody.getPosition();
			
			final IVector distance = otherPosition.sub(myPosition);
			final double distanceMagnitude = distance.getMagnitude();
			
			final double forceMagnitude = otherBody.getMass() / (distanceMagnitude * distanceMagnitude);
			
			IVector force = null;
			try {
				force = distance.getVersor().scale(forceMagnitude);
			} catch (Exception e) {
				continue;
			}
			totalForce = totalForce.sum(force);
		}
		
		doneSignal.countDown();	/* read from galaxy ended */
		
		totalForce = totalForce.scale(6.67e-11 * myMass);
		
		final IVector deltaSpeed = totalForce.scale(this.deltaTimeSeconds / myMass);
		final IVector deltaPosition = (mySpeed.sum(deltaSpeed.scale(1/2))).scale(this.deltaTimeSeconds);
		
		/* creates an update for the specified body */
		final IBodyUpdate update = new BodyUpdate(
											bodyIndex,
											myPosition.sum(deltaPosition),
											mySpeed.sum(deltaSpeed));
		
		return update;
	}

}

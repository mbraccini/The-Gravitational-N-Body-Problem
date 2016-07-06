package nbody_pap1314.view.implementation;

import nbody_pap1314.controller.implementation.Counter;
import nbody_pap1314.model.interfaces.IBody;
import nbody_pap1314.view.interfaces.IBodyUpdateView;
import nbody_pap1314.view.interfaces.IGalaxyView;

/**
 * Implementation of a console view.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class GalaxyConsoleView implements IGalaxyView {

	protected int numBodies;	/* number of the bodies in the galaxy */
	protected double radius;	/* galaxy radius */

	protected IBodyUpdateView[] consoleBodiesBefore;	/* on-screen array of bodies' updates */
	protected IBodyUpdateView[] consoleBodiesNext;	/* off-screen array of bodies' updates */

	protected Counter countUpdate;				/* monitor used to count the number of updates */

	protected volatile boolean firstTime;		/* used for not drawing an empty array */

	public GalaxyConsoleView(int numBodies, double radius) {
		this.numBodies = numBodies;
		this.radius = radius;
		this.consoleBodiesBefore = new BodyConsoleView[this.numBodies];
		this.consoleBodiesNext = new BodyConsoleView[this.numBodies];
		this.countUpdate = new Counter(this.numBodies);
		this.firstTime = true;
	}

	public void addBody(IBody myBody, int indexBody) {
		IBodyUpdateView bodyUpdateView = new BodyConsoleView(
												myBody.getPosition().getX(),
												myBody.getPosition().getY());
		
		consoleBodiesNext[indexBody] = bodyUpdateView;
		
		if(!countUpdate.inc()){
			consoleBodiesBefore = consoleBodiesNext;
			IBodyUpdateView[] temp = new BodyConsoleView[numBodies];
			consoleBodiesNext = temp;
		}
	}

	public void update() {
		firstTime = false;
		
		if (!firstTime){
			for(int i = 0; i < numBodies; i++) {
				log("" + consoleBodiesBefore[i]);
			}
		}
	}

	private void log(String msg){
		System.out.println("[" + this.getClass().getSimpleName() + "] " + msg);
	}

}

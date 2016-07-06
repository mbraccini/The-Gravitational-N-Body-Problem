package nbody_pap1314.simulator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import nbody_pap1314.model.interfaces.IGalaxy;
import nbody_pap1314.view.interfaces.IGalaxyView;

/**
 * The implementation of the graphics update phase.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class Galaxy2DUpdaterService {

	protected final ExecutorService executor;	/* fixed thread pool */
	protected final IGalaxyView galaxy2D;		/* graphical representation of the galaxy */

	public Galaxy2DUpdaterService(ExecutorService executor, IGalaxyView galaxy2D) {
		this.executor = executor;
		this.galaxy2D = galaxy2D;
	}

	public long updateBodies2D(IGalaxy galaxy) {
		long executionTime = System.currentTimeMillis();
		
		CountDownLatch doneSignal = new CountDownLatch(galaxy.getNumBodies());
		
		assert doneSignal.getCount() == galaxy.getNumBodies() : "error while initializing CountDownLatch";	/* JPF */
		
		for(int bodyIndex = 0; bodyIndex < galaxy.getNumBodies(); bodyIndex++) {
			try {
				executor.submit(new UpdateBody2DTask(galaxy, bodyIndex, galaxy2D, doneSignal));
			} catch (Exception e) {
				log(e.getClass().getSimpleName() + ": " + bodyIndex);
				continue;
			}
		}
		
		try {
			/* waits for previously submitted tasks to complete execution */
			doneSignal.await();
		} catch (InterruptedException e) {
			log(e.getMessage());
		}
		
		assert doneSignal.getCount() == 0 : "CountDownLatch value must be zero";	/* JPF */
		
		galaxy2D.update();
		
		executionTime = System.currentTimeMillis() - executionTime;
		
		return executionTime;
	}

	private void log(String msg) {
		System.out.println("[" + this.getClass().getSimpleName() + "] " + msg);
	}

}

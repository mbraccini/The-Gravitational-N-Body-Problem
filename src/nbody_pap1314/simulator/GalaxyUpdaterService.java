package nbody_pap1314.simulator;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import nbody_pap1314.model.interfaces.IBody;
import nbody_pap1314.model.interfaces.IGalaxy;
import nbody_pap1314.model.interfaces.IBodyUpdate;

/**
 * The implementation of the galaxy update phase.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class GalaxyUpdaterService {

	protected final ExecutorService executor;	/* fixed thread pool */

	public GalaxyUpdaterService(ExecutorService executor) {
		this.executor = executor;
	}

	public long updateBodies(IGalaxy galaxy, double deltaTime) {
		long executionTime = System.currentTimeMillis();
		
		CountDownLatch doneSignal = new CountDownLatch(galaxy.getNumBodies());
		
		assert doneSignal.getCount() == galaxy.getNumBodies() : "error while initializing CountDownLatch";	/* JPF */
		
		Set<Future<IBodyUpdate>> resultSet = new HashSet<Future<IBodyUpdate>>();
		
		for(int bodyIndex = 0; bodyIndex < galaxy.getNumBodies(); bodyIndex++) {
			try {
				Future<IBodyUpdate> result = executor.submit(new UpdateBodyTask(galaxy, bodyIndex, (deltaTime), doneSignal));
				resultSet.add(result);
			} catch (Exception e) {
				log(e.getClass().getSimpleName() + ": " + bodyIndex);
				continue;
			}
		}
		
		try {
			/* waits for tasks to end reading the galaxy state */
			doneSignal.await();
		} catch (InterruptedException e) {
			log(e.getMessage());
		}
		
		assert doneSignal.getCount() == 0 : "CountDownLatch value must be zero";	/* JPF */
		
		for(Future<IBodyUpdate> future : resultSet) {
			IBodyUpdate bodyUpdate = null;
			
			try {
				/* waits for an update to be available */
				bodyUpdate = future.get();
			} catch (Exception e) {
				log(e.getClass().getSimpleName());
				continue;
			}
			
			if (bodyUpdate != null) {
				IBody body = galaxy.getBody(bodyUpdate.getBodyIndex());
				
				/* updates the position of the specified body */
				body.setPosition(bodyUpdate.getPosition());
				
				/* updates the speed of the specified body */
				body.setSpeed(bodyUpdate.getSpeed());
			}
		}
		
		executionTime = System.currentTimeMillis() - executionTime;
		
		return executionTime; 
	}

	private void log(String msg) {
		System.out.println("[" + this.getClass().getSimpleName() + "] " + msg);
	}

}

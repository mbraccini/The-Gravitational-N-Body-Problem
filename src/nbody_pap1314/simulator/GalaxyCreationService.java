package nbody_pap1314.simulator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import nbody_pap1314.model.implementation.Galaxy;
import nbody_pap1314.model.interfaces.IBody;
import nbody_pap1314.model.interfaces.IGalaxy;

/**
 * The implementation of the galaxy creation phase.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class GalaxyCreationService {

	protected final ExecutorService executor;	/* fixed thread pool */

	public GalaxyCreationService(ExecutorService executor) {
		this.executor = executor;
	}

	public IGalaxy createGalaxy(List<String> lines, boolean logEnabled) {
		long executionTime = System.currentTimeMillis();
		
		Set<Future<IBody>> resultSet = new HashSet<Future<IBody>>();
		IGalaxy galaxy = new Galaxy();	/* creates a new empty galaxy */
		
		assert galaxy.getNumBodies() == 0 : "error while creating a new Galaxy";	/* JPF */
		
		for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
			try {
				final String line = lines.get(lineNumber);
				Future<IBody> result = executor.submit(new CreateBodyTask(line, lineNumber));
				resultSet.add(result);
			} catch (Exception e) {
				log(e.getClass().getSimpleName() + ": " + lineNumber);
				continue;
			}
		}
		
		for(Future<IBody> future : resultSet) {
			IBody body = null;
			
			try {
				body = future.get();	/* waits for a Body to be created */
			} catch (Exception e) {
				log(e.getClass().getSimpleName());
				continue;
			}
			
			if (body != null) {
				if (!galaxy.put(body)) {	/* adds a body to the galaxy */
					break;	/* if the galaxy is full, stop adding bodies */
				}
			}
		}
		
		if (logEnabled) {
			log(galaxy.getNumBodies() + " bodies created in " + (System.currentTimeMillis() - executionTime) + " ms");
		}
		
		return galaxy;
	}

	private void log(String msg) {
		System.out.println("[" + this.getClass().getSimpleName() + "] " + msg);
	}

}

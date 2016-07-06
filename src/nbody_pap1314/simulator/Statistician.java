package nbody_pap1314.simulator;

/**
 * The statistician is used to compute statistics and show them to the user.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class Statistician {

	protected final long printWaitTimeMs;	/* the minimum amount of time between two logs */
	protected long lastPrintTime;			/* the last instant of time a log was printed */
	protected long averageExecutionTimeMs;	/* the average execution time */
	protected long numCycles;				/* the number of cycles done */

	public Statistician(long printWaitTimeMs) {
		this.printWaitTimeMs = printWaitTimeMs;
		resetStatistics();
	}

	/**
	 * Updates statics.
	 */
	public void updateStatistics(long executionTime) {
		averageExecutionTimeMs = ((averageExecutionTimeMs * numCycles) + executionTime) / (numCycles + 1);
		numCycles++;
		if (numCycles == 0) {
			averageExecutionTimeMs = 0;
		}
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastPrintTime > printWaitTimeMs) {
			lastPrintTime = currentTime;
			log("average execution time: " + averageExecutionTimeMs + " ms");
		}
	}

	/**
	 * Resets statics.
	 */
	public void resetStatistics() {
		this.lastPrintTime = System.currentTimeMillis();
		this.averageExecutionTimeMs = 0;
		this.numCycles = 0;
	}

	private void log(String msg) {
		System.out.println("[" + this.getClass().getSimpleName() + "] " + msg);
	}

}

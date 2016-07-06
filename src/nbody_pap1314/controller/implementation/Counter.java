package nbody_pap1314.controller.implementation;

/**
 * Counter monitor with auto-reset feature.
 * If the counter reaches the value set initially, the counter resets to zero.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class Counter {

	private int count;				/* classic counter field */
	private final int initValue;	/* Threshold for reset */

	public Counter(int initValue) {
		this.initValue = initValue;
		this.count = 0;
	}

	/**
	 * Increments the counter and resets if the counter reaches the
	 * threshold value (initValue).
	 * 
	 * @return false if the increment resets the counter
	 */
	public synchronized boolean inc() {
		count++;
		if(count == initValue) {
			count = 0;
			return false;
		}
		return true;
	}

	/**
	 * Returns the counter's value.
	 * 
	 * @return Counter's value
	 */
	public synchronized int getValue() {
		return count;
	}

}

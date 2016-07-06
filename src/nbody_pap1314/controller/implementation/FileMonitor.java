package nbody_pap1314.controller.implementation;

import java.io.File;

/**
 * Monitor for the selection of input file.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class FileMonitor {

	File file;	/* selected file */

	public FileMonitor() {
		this.file = null;
	}

	/**
	 * Sets the file.
	 * 
	 * @param file a file
	 */
	public synchronized void setFile(File file) {
		this.file = file;
		notify();
	}

	/**
	 * Returns the previously selected file.
	 * 
	 * @return selected file
	 */
	public synchronized File getFile() {
		while (file == null) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		File returnFile = file;
		file = null;
		return returnFile;
	}

}

package nbody_pap1314.jpf;

import gov.nasa.jpf.jvm.Verify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nbody_pap1314.controller.implementation.FileMonitor;
import nbody_pap1314.model.interfaces.IGalaxy;
import nbody_pap1314.simulator.GalaxyCreationService;
import nbody_pap1314.simulator.GalaxyUpdaterService;

/**
 * The simulator for JPF.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class SimulatorJPF {

	/* the number of worker threads to be created */
	private final int poolSize = Runtime.getRuntime().availableProcessors() + 1;

	/* monitor used for setting/opening the galaxy configuration file */
	private final FileMonitor fileMonitor;

	/* thread pool */
	private final ExecutorService executor;

	public SimulatorJPF() {
		
		Verify.beginAtomic();	/* JPF will consider this as an atomic section */
		
		fileMonitor = new FileMonitor();	/* creates the FileMonitor */
		
		// JPF-only //
		fileMonitor.setFile(new File("data_file_examples/00003-bodies.dat"));
		//////////////
		
		Verify.endAtomic();		/* (JPF) end of atomic section */
		
		/* creates a fixed thread pool */
		executor = Executors.newFixedThreadPool(poolSize);
		
		Verify.beginAtomic();	/* JPF will consider this as an atomic section */
		
		List<String> lines;
		IGalaxy galaxy;
		
		/* creates the service that will create the galaxy */
		final GalaxyCreationService galaxyCreationService = new GalaxyCreationService(executor);
		
		Verify.endAtomic();		/* (JPF) end of atomic section */
		
		while(true) {
			
			final File file = fileMonitor.getFile();	/* waits for data file */
			
			/* instantiates the galaxy */
			lines = splitFile(file);		/* splits the file in lines */
			galaxy = galaxyCreationService.createGalaxy(lines, true);
			if (galaxy.getNumBodies() > 0) {
				break;
			} else {
				log("Wrong file loaded, please choose the correct one");
			}
		}
		
		Verify.beginAtomic();	/* JPF will consider this as an atomic section */
		
		/* creates the service that will update the galaxy */
		final GalaxyUpdaterService galaxyUpdater = new GalaxyUpdaterService(executor);
		
		/* simulation */
		final double deltaTime = 1d;
		
		Verify.endAtomic();		/* (JPF) end of atomic section */
		
		galaxyUpdater.updateBodies(galaxy, deltaTime);
		
		System.exit(0);
	}

	/**
	 * Splits the file in lines.
	 */
	private List<String> splitFile(File file) {
		List<String> lines = new ArrayList<String>();
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {}
		if (reader == null) {
			return lines;
		}
		
		while(true) {
			String line = null;
			
			try {
				line = reader.readLine();
			} catch (IOException e) {
				break;
			}
			
			if (line == null) {
				break;
			}
			
			if (line.startsWith("#")) {
				continue;	/* ignore comment line */
			}
			
			try {
				lines.add(line);
			} catch (Exception e) {
				break;
			}
		}
		
		try {
			reader.close();
		} catch (Exception e) {}
		return lines;
	}

	private void log(String msg) {
		System.out.println("[" + this.getClass().getSimpleName() + "] " + msg);
	}

	public static void main(String[] args) {
		new SimulatorJPF();
	}

}

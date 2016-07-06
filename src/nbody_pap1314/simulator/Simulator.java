package nbody_pap1314.simulator;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import nbody_pap1314.controller.implementation.Controller;
import nbody_pap1314.controller.implementation.FileMonitor;
import nbody_pap1314.controller.implementation.CommandMonitor;
import nbody_pap1314.model.interfaces.IGalaxy;
import nbody_pap1314.view.implementation.SimulatorGUI;
import nbody_pap1314.view.interfaces.IGalaxyView;

/**
 * The simulator.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class Simulator {

	/* GUI window default size set as a percentage of the screen size */
	private final int initialScreenPercentage = 75;

	/* the number of worker threads to be created */
	private final int poolSize = Runtime.getRuntime().availableProcessors() + 1;

	/* monitor used for setting/opening the galaxy configuration file */
	private final FileMonitor fileMonitor;

	/* thread pool */
	private final ExecutorService executor;

	/* the graphical representation of the Simulator */
	private SimulatorGUI simulatorGUI;

	public Simulator() {
		
		fileMonitor = new FileMonitor();	/* creates the FileMonitor */
		
		/* creates and shows GUI (waits until done) */
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createAndShowGUI();
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		/* creates a fixed thread pool */
		executor = Executors.newFixedThreadPool(poolSize);
		
		log("Fixed thread pool created. Number of threads in the pool: " + poolSize);
		
		List<String> lines;
		IGalaxy galaxy;
		
		/* creates the service that will create the galaxy */
		final GalaxyCreationService galaxyCreationService = new GalaxyCreationService(executor);
		
		while(true) {
			log("Waiting for data file...");
			final File file = fileMonitor.getFile();	/* waits for data file */
			log("File loaded.");
			
			/* instantiates the galaxy */
			lines = splitFile(file);		/* splits the file in lines */
			galaxy = galaxyCreationService.createGalaxy(lines, true);
			if (galaxy.getNumBodies() > 0) {
				break;
			} else {
				log("Wrong file loaded, please choose the correct one");
			}
		}
		
		final int numBodies = galaxy.getNumBodies();
		final double radius = galaxy.getRadius();
		
		/* monitor used for managing start, stop, pause and resume commands */
		final CommandMonitor commandMonitor = new CommandMonitor();
		
		/* listens for start, stop, pause and resume commands from GUI */
		final Controller controller = new Controller(commandMonitor);
		
		/* shows galaxy in GUI */
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					simulatorGUI.initGalaxy2D(numBodies, radius);
					simulatorGUI.addListener(controller);
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		/* gets the graphical representation of the galaxy */
		final IGalaxyView galaxy2D = simulatorGUI.getGalaxy2D();
		
		/* creates the service that will update the galaxy */
		final GalaxyUpdaterService galaxyUpdater = new GalaxyUpdaterService(executor);
		
		/* creates the service that will update the graphical representation of the galaxy */
		final Galaxy2DUpdaterService galaxy2DUpdater = new Galaxy2DUpdaterService(executor, galaxy2D);
		
		/* shows all the bodies in their initial positions */
		galaxy2DUpdater.updateBodies2D(galaxy);
		
		/* used to display statistics about average execution time */
		final Statistician statistician = new Statistician(1000);
		
		/* simulation loop */
		while(true) {
			final double deltaTime = commandMonitor.canLoop();
			if(deltaTime != 0d) {	/* simulation is running */
				long executionTime = System.currentTimeMillis();
				
				/* waits until all bodies have been updated */
				galaxyUpdater.updateBodies(galaxy, deltaTime);
				
				/* waits until all bodies (graphical representations) have been updated */
				galaxy2DUpdater.updateBodies2D(galaxy);
				
				executionTime = System.currentTimeMillis() - executionTime;
				
				/* updates statistics */
				statistician.updateStatistics(executionTime);
			} else {
				/* resets the galaxy to its initial configuration */
				galaxy = galaxyCreationService.createGalaxy(lines, true);
				
				/* shows all the bodies in their initial positions */
				galaxy2DUpdater.updateBodies2D(galaxy);
				
				/* resets statistics */
				statistician.resetStatistics();
			}
		}
	}

	private void createAndShowGUI() {
		/* creates and sets up the window */
		JFrame frame = new JFrame("The Gravitational N-Body Problem");
		frame.addWindowListener(
				new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent we) {
						if (executor != null) {
							try {
								executor.shutdownNow();
							} catch (Exception e) {}
						}
						System.exit(0); /* exits without errors */
					}
				}
		);
		
		/* sets the window properties */
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int height = screenSize.height * initialScreenPercentage / 100;
		int width = screenSize.width * initialScreenPercentage / 100;
		frame.setPreferredSize(new Dimension(width, height));
		frame.setLocation(
			(screenSize.width - width) / 2,
			(screenSize.height - height) / 2
		);
		
		/* adds the graphical representation of the simulator to the window */
		simulatorGUI = new SimulatorGUI(fileMonitor);
		frame.getContentPane().add(simulatorGUI);
		
		/* displays the window */
		frame.pack();
		frame.setVisible(true);
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

}

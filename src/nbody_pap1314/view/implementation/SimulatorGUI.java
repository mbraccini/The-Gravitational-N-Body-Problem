package nbody_pap1314.view.implementation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.*;

import nbody_pap1314.controller.implementation.FileMonitor;
import nbody_pap1314.controller.interfaces.InputListener;
import nbody_pap1314.view.interfaces.IGalaxyView;

/**
 * Main Panel extension of javax.swing.JPanel
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */

public class SimulatorGUI extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	/* default delta time that is set in the JTextField */
	protected final double DEFAULT_DELTA_TIME = 0.001d;
	
	protected JPanel northPanel, centerPanel, southPanel;
	protected JLabel dtLabel;
	protected JTextField dtTextField;
	protected JButton startButton, stopButton, pauseButton;
	protected JButton openButton;
	protected JFileChooser fileChooser;
	protected JTextArea logTextArea;
	
	/* monitor that rules the choose of input file */
	protected FileMonitor fileMonitor;
	
	/* listeners of graphic events */
	private ArrayList<InputListener> listeners;
	
	public SimulatorGUI(FileMonitor fileMonitor) {
		super(new BorderLayout());
		this.fileMonitor = fileMonitor;
		this.listeners = new ArrayList<InputListener>();
		configure();
	}

	/**
	 * Sets the layout and initializes the graphical components.
	 */
	private void configure() {
		this.northPanel = new JPanel();
		this.centerPanel = new JPanel(new GridBagLayout());
		this.southPanel = new JPanel(new BorderLayout());
		
		this.dtLabel = new JLabel("Delta time (seconds):");
		this.dtLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		this.dtLabel.setEnabled(false);
		this.dtTextField = new JTextField(5);
		this.dtTextField.setText("" + DEFAULT_DELTA_TIME);
		this.dtTextField.setEnabled(false);
		this.startButton = new JButton("Start");
		this.startButton.setEnabled(false);
		this.stopButton = new JButton("Stop");
		this.stopButton.setEnabled(false);
		this.pauseButton = new JButton("Pause");
		this.pauseButton.setEnabled(false);
		
		this.openButton = new JButton("Open data file...");
		
		/* JFileChooser */
		this.fileChooser = new JFileChooser();
		this.fileChooser.setCurrentDirectory(new File("."));
		
		this.logTextArea = new JTextArea(3,20);
		this.logTextArea.setMargin(new Insets(5,5,5,5));
		this.logTextArea.setEditable(false);
		this.logTextArea.setBackground(Color.BLACK);
		this.logTextArea.setForeground(Color.WHITE);
		JScrollPane logScrollPane = new JScrollPane(this.logTextArea);
		
		/* Adds the graphical components to the main panel */
		this.northPanel.add(this.dtLabel);
		this.northPanel.add(this.dtTextField);
		this.northPanel.add(Box.createRigidArea(new Dimension(20,0)));
		
		this.northPanel.add(this.startButton);
		this.northPanel.add(this.stopButton);
		this.northPanel.add(this.pauseButton);
		
		this.centerPanel.setBackground(Color.BLACK);
		this.centerPanel.add(this.openButton);
		
		this.southPanel.setBackground(Color.BLACK);
		this.southPanel.add(logScrollPane); 
		redirectSystemStreams();
		
		/* sets listeners */
		this.startButton.addActionListener(this);
		this.stopButton.addActionListener(this);
		this.pauseButton.addActionListener(this);
		this.openButton.addActionListener(this);
		
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Adds a controller that listens the view events.
	 * @param controller 
	 */
	public void addListener(InputListener controller) {
		this.listeners.add(controller);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("Start")) {
			double delta = 0d;
			try {
				delta = Double.parseDouble(this.dtTextField.getText());
			} catch (Exception e1) {
				this.dtTextField.setText("" + (double)0);
			}
			if (delta != 0d) {
				this.notifyStart(delta);
				this.startButton.setEnabled(false);
				this.stopButton.setEnabled(true);
				this.pauseButton.setEnabled(true);
				this.dtTextField.setEnabled(false);
			}
		} else if(cmd.equals("Stop")) {
			this.notifyStop();
			this.pauseButton.setText("Pause");
			this.startButton.setEnabled(true);
			this.stopButton.setEnabled(false);
			this.pauseButton.setEnabled(false);
			this.dtTextField.setEnabled(true);
		} else if(cmd.equals("Pause")) {
			this.notifyPause();
			this.pauseButton.setText("Resume");
			this.startButton.setEnabled(false);
			this.stopButton.setEnabled(true);
			this.pauseButton.setEnabled(true);
			this.dtTextField.setEnabled(true);
		} else if(cmd.equals("Resume")){
			double delta = 0d;
			try {
				delta = Double.parseDouble(this.dtTextField.getText());
			} catch (Exception e1) {
				this.dtTextField.setText("" + (double)0);
			}
			if (delta != 0d) {
				this.notifyResume(delta);
				this.pauseButton.setText("Pause");
				this.startButton.setEnabled(false);
				this.stopButton.setEnabled(true);
				this.pauseButton.setEnabled(true);
				this.dtTextField.setEnabled(false);
			}
		} else if(cmd.equals("Open data file...")) {
			int returnVal = fileChooser.showOpenDialog(this);
			 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	this.fileMonitor.setFile(fileChooser.getSelectedFile());
            }
		}
	}

	/**
	 * Notifies start command to the controllers, previously registered.
	 * 
	 * @param delta delta time selected.
	 */
	private void notifyStart(double delta) {
		for(InputListener listener: this.listeners){
			listener.start(delta);
		}
		
	}

	/**
	 * Notifies stop command to the controllers, previously registered.
	 */
	private void notifyStop(){
		for(InputListener listener: this.listeners){
			listener.stop();
		}
	}

	/**
	 * Notifies pause command to the controllers, previously registered.
	 */
	private void notifyPause(){
		for(InputListener listener: this.listeners){
			listener.pause();
		}
	}

	/**
	 * Notifies resume command to the controllers, previously registered.
	 * 
	 * @param delta delta time selected.
	 */
	private void notifyResume(double delta) {
		for(InputListener listener: this.listeners){
			listener.resume(delta);
		}
	}

	/**
	 * Returns an object of type IGalaxyView
	 * 
	 * @return IGalaxyView
	 */
	public IGalaxyView getGalaxy2D() {
		return (IGalaxyView) this.centerPanel;
	}

	/**
	 * Initializes Galaxy2D and adds it to the main panel.
	 * 
	 * @param numBodies
	 * @param radius
	 */
	public void initGalaxy2D(int numBodies, double radius) {
		this.remove(centerPanel);
		this.centerPanel = new Galaxy2D(numBodies,radius);
		this.add(centerPanel, BorderLayout.CENTER);
		this.centerPanel.setVisible(true);
		this.setVisible(true);
		this.getParent().validate();
		this.getParent().setVisible(true);
		this.dtLabel.setEnabled(true);
		this.dtTextField.setEnabled(true);
		this.startButton.setEnabled(true);
	}
	
	/**
	 * Updates the JtextArea asynchronously, the update is made by EDT.
	 * @param text Text that will be visualized.
	 */
	private void updateTextArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				logTextArea.append(text);
				logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
			}
		});
	}

	/**
	 * Redirects standard output and standard error to the GUI.
	 */
	private void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				updateTextArea(String.valueOf((char) b));
			}
			
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				updateTextArea(new String(b, off, len));
			}
			
			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};
		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}

}

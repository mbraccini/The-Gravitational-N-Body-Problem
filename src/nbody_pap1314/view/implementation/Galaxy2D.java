package nbody_pap1314.view.implementation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import nbody_pap1314.controller.implementation.Counter;
import nbody_pap1314.model.interfaces.IBody;
import nbody_pap1314.view.interfaces.IBodyUpdateView;
import nbody_pap1314.view.interfaces.IGalaxyView;

/**
 * Graphical implementation of a Galaxy, as an extension of javax.swing.JPanel.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class Galaxy2D extends JPanel implements IGalaxyView {

	private static final long serialVersionUID = 1L;

	protected int numBodies;	/* number of the bodies in the galaxy */
	protected double radius;	/* galaxy radius */

	protected IBodyUpdateView[] bodies2DBefore;	/* on-screen array of bodies' updates */
	protected IBodyUpdateView[] bodies2DNext;	/* off-screen array of bodies' updates */
	
	protected int maxDigitDouble;				/* number of Double.MAX_VALUE's digits */

	protected Counter countUpdate;				/* monitor used to count the number of updates */

	protected volatile boolean firstTime;		/* used for not drawing an empty array */

	public Galaxy2D(int numBodies, double radius) {
		setBackground(Color.black);
		this.numBodies = numBodies;
		this.radius = radius;
		this.bodies2DBefore = new Body2D[this.numBodies];
		this.bodies2DNext = new Body2D[this.numBodies];
		this.maxDigitDouble = countDigit(Double.MAX_VALUE);
		this.countUpdate = new Counter(this.numBodies);
		this.firstTime = true;
	}

	/**
	 * Method that realize the real graphic update.
	 * 
	 * @param g
	 */
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(new Color(153, 203, 255));
		
		RenderingHints rh = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		g2d.setRenderingHints(rh);
		
		if(!firstTime) {
			for (int i = 0; i < numBodies; i++) {
				/* if the value is NaN, it doesn't show the body */
				if (!isFinite(bodies2DBefore[i].getX()) ||
						!isFinite(bodies2DBefore[i].getY())) {
					continue;
				}
				
				/* proportion: X/UniverseRadius * Integer.MAX_VALUE */
				double tempX = ((bodies2DBefore[i].getX()/radius)*Integer.MAX_VALUE);
				double tempY = ((bodies2DBefore[i].getY()/radius)*Integer.MAX_VALUE);
				int dimension = ((Body2D) bodies2DBefore[i]).getDimension();
				
				/* fills an oval and then draw it */
				g2d.fillOval(	(int)(((tempX/Integer.MAX_VALUE)*this.getWidth()/2)+this.getWidth()/2)-(dimension/2),
								(int)(((tempY/Integer.MAX_VALUE)*this.getHeight()/2)+this.getHeight()/2)-(dimension/2),
								dimension,
								dimension);
			}
		}
		
		/* draws the cartesian axes */
		g2d.setColor(new Color(255, 255, 0));
		g2d.setStroke(new BasicStroke(0.2f));
		
		g2d.drawLine(this.getWidth()/2, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
		g2d.drawLine(this.getWidth()/2, this.getHeight()/2, this.getWidth()/2, this.getHeight());
		g2d.drawLine(this.getWidth()/2, this.getHeight()/2, -this.getWidth()/2, this.getHeight()/2);
		g2d.drawLine(this.getWidth()/2, this.getHeight()/2, this.getWidth()/2, 0);
		
		String s = new Double(radius).toString();
		g2d.drawString(s, this.getWidth()-(s.length()*8), this.getHeight()/2);
		g2d.drawString(s, this.getWidth()/2, this.getHeight()-3);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	public void addBody(IBody myBody, int indexBody) {
		Body2D b = new Body2D(
					myBody.getPosition().getX(),
					myBody.getPosition().getY(),
					(int)Math.ceil((double)countDigit(myBody.getMass())/maxDigitDouble*10));
		
		bodies2DNext[indexBody] = b;
		
		if(!countUpdate.inc()) {
			bodies2DBefore = bodies2DNext;
			IBodyUpdateView[] temp = new Body2D[numBodies];
			bodies2DNext = temp;
		}
	}

	public void update() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					firstTime = false;
					repaint();
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Returns the number of digits.
	 */
	private int countDigit(double num) {
		int c = 0;
		while (num >= 10) {
			num = num/10;
			c++;
		}
		return ++c;
	}

	private boolean isFinite(double value) {
		if(Double.isInfinite(value) || Double.isNaN(value)) {
			return false;
		}
		return true;
	}

}

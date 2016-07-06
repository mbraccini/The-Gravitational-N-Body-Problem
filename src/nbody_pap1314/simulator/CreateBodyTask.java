package nbody_pap1314.simulator;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

import nbody_pap1314.model.implementation.Body;
import nbody_pap1314.model.implementation.Vector;
import nbody_pap1314.model.interfaces.IBody;

/**
 * This task creates a Body parsing a line of the galaxy configuration file.
 * 
 * @author Michele Braccini
 * @author Alessandro Fantini
 */
public class CreateBodyTask implements Callable<IBody> {

	protected final String line;		/* line of the file to parse */
	protected final int lineNumber;		/* line number */

	public CreateBodyTask(String line, int lineNumber) {
		this.line = line;
		this.lineNumber = lineNumber;
	}

	public IBody call() {
		StringTokenizer tokenizer = null;
		IBody body = null;
		
		try {
			tokenizer = new StringTokenizer(line);
		} catch (Exception e) {
			log(e.getClass().getSimpleName() + ", line skipped");
		}
		
		String currentToken = "";
		double mass;
		double positionX;
		double positionY;
		double speedX;
		double speedY;
		
		try {
			currentToken = "mass";
			mass = Double.parseDouble(tokenizer.nextToken());
			if (!isFinite(mass)) {
				log("mass out of range, line skipped");
				return null;
			}
			else if (mass <= 0) {
				log("mass must be a positive number, line skipped");
				return null;
			}
			
			currentToken = "positionX";
			positionX = Double.parseDouble(tokenizer.nextToken());
			if (!isFinite(positionX)) {
				log("positionX out of range, line skipped");
				return null;
			}
			
			currentToken = "positionY";
			positionY = Double.parseDouble(tokenizer.nextToken());
			if (!isFinite(positionY)) {
				log("positionY out of range, line skipped");
				return null;
			}
			
			currentToken = "speedX";
			speedX = Double.parseDouble(tokenizer.nextToken());
			if (!isFinite(speedX)) {
				log("speedX out of range, line skipped");
				return null;
			}
			
			currentToken = "speedY";
			speedY = Double.parseDouble(tokenizer.nextToken());
			if (!isFinite(speedY)) {
				log("speedY out of range, line skipped");
				return null;
			}
			
			if(tokenizer.hasMoreTokens()) {
				log("WARNING: too many tokens");
			}
			
			body = new Body(mass,
							new Vector(positionX, positionY),
							new Vector(speedX, speedY));
		} catch (NoSuchElementException e) {
			log(currentToken + ": no such element, line skipped");
		} catch (NullPointerException  e) {
			log(currentToken + ": no such element, line skipped");
		} catch (NumberFormatException e) {
			log(currentToken + ": input mismatch, line skipped");
		}
		
		return body;
	}

	private boolean isFinite(double value) {
		if(Double.isInfinite(value) || Double.isNaN(value)) {
			return false;
		}
		return true;
	}

	private void log(String msg) {
		System.out.println("[" + this.getClass().getSimpleName() + " (line " + lineNumber + ")] " + msg);
	}

}

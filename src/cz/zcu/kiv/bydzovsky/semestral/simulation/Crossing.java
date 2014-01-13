package cz.zcu.kiv.bydzovsky.semestral.simulation;

import cz.zcu.kiv.bydzovsky.semestral.Main;
import cz.zcu.fav.kiv.jsim.JSimProcess;
import cz.zcu.fav.kiv.jsim.JSimSimulation;
import java.util.Random;

/**
 * Class representing one-to-two path disambiguation. Accepts a transaction, and
 * sends it to one of defined destination according to given percentage split.
 *
 * @author bydga
 */
public class Crossing extends JSimProcess implements TransactionReceiver {

	TransactionReceiver nextObject1;
	double nextObject1Probability;
	TransactionReceiver nextObject2;
	TransactionReceiver measuredPath;
	double nextObject2Probability;
	private Random generator;
	private double lastReceived = 0;
	private Histogram histogram;

	/**
	 * Creates new instance of Crossing. 
	 * @param name
	 * @param parent
	 * @param measuredPathTo
	 * @throws Exception 
	 */
	public Crossing(String name, JSimSimulation parent, TransactionReceiver measuredPathTo) throws Exception {
		super(name, parent);
		this.measuredPath = measuredPathTo;
		this.generator = new Random(765);
		this.histogram = new Histogram(25);
	}

	@Override
	public void receiveTransaction(Transaction transaction) {

		double rnd = this.generator.nextDouble();
		TransactionReceiver destination = (rnd < this.nextObject1Probability) ? this.nextObject1 : this.nextObject2;
		log(this.getName() + " sending tx " + transaction + " to " + destination.getName());
		destination.receiveTransaction(transaction);

		if (destination == this.measuredPath) {
			double time = this.getParent().getCurrentTime() - lastReceived;
			lastReceived = this.getParent().getCurrentTime();
			this.histogram.add(time);
		}
	}

	/**
	 * Sets first destination of incomming transaction - with given percentage probability that the trasaction will go into this destination.
	 * @param nextObject
	 * @param probability 
	 */
	public void setNextObject1(TransactionReceiver nextObject, double probability) {
		if (probability < 0 || probability > 1) {
			throw new RuntimeException("Invalid probability value");
		}
		this.nextObject1 = nextObject;
		this.nextObject1Probability = probability;
	}

	/**
	 * Sets first destination of incomming transaction - with given percentage probability that the trasaction will go into this destination.
	 * @param nextObject 
	 */
	public void setNextObject2(TransactionReceiver nextObject) {
		this.nextObject2 = nextObject;
	}

	private void log(String msg) {
		if (Main.log) {
			System.out.println(this.getParent().getCurrentTime() + " > " + msg);
		}
	}

	public double getMeanValue() {
		return this.histogram.getMeanValue();
	}

	public double getD() {
		return this.histogram.getVariance();
	}

	public String getHistogram() {
		return this.histogram.getHistogram();
	}
}

package cz.zcu.kiv.bydzovsky.semestral.simulation;

import cz.zcu.kiv.bydzovsky.semestral.Main;
import cz.zcu.fav.kiv.jsim.JSimInvalidParametersException;
import cz.zcu.fav.kiv.jsim.JSimProcess;
import cz.zcu.fav.kiv.jsim.JSimSecurityException;
import cz.zcu.fav.kiv.jsim.JSimSimulation;
import cz.zcu.kiv.bydzovsky.semestral.distributions.Distribution;

/**
 * Class that generates and inserts new transactions (requests) into the
 * simulation.
 *
 * @author bydga
 */
public class Generator extends JSimProcess {

	private Distribution distributionGenerator;
	private TransactionReceiver nextObject;
	private int generatedTransactions;
	private boolean shouldGenerate;

	/**
	 * Sets the flag, that this generator should stop generaing new transactions.
	 */
	public void stopGenerating() {
		this.shouldGenerate = false;
	}

	/**
	 * Returns the number of totally generated transactions
	 *
	 * @return the number of totally generated transactions
	 */
	public int getGeneratedTransactions() {
		return generatedTransactions;
	}

	public Generator(String name, JSimSimulation parent, Distribution distribution) throws Exception {
		super(name, parent);
		this.distributionGenerator = distribution;
		this.shouldGenerate = true;
	}

	/**
	 * Sets next object in the simulation process.
	 *
	 * @param nextObject
	 */
	public void setNextObject(TransactionReceiver nextObject) {
		this.nextObject = nextObject;
	}

	/**
	 * Internal logging method.
	 *
	 * @param msg
	 */
	private void log(String msg) {
		if (Main.log) {
			System.out.println(this.getParent().getCurrentTime() + " > " + msg);
		}
	}

	@Override
	protected void life() {

		try {
			while (this.shouldGenerate) {
				generatedTransactions++;
				Transaction t = new Transaction(this.getParent().getCurrentTime());
				double workTime = this.distributionGenerator.next();
				hold(workTime);
				log(this.getName() + " generated tx " + t + " and will sleep for " + workTime);
				this.nextObject.receiveTransaction(t);
			}

		} catch (JSimSecurityException e) {
			e.printStackTrace();
		} catch (JSimInvalidParametersException ex) {
			ex.printStackTrace();
		}
	}
}

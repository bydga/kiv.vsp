package cz.zcu.kiv.bydzovsky.semestral.simulation;

import cz.zcu.fav.kiv.jsim.JSimProcess;
import cz.zcu.fav.kiv.jsim.JSimSimulation;
import cz.zcu.kiv.bydzovsky.semestral.Main;

/**
 * Ending point of the simulation. Only consumes incomming transactions and counts some statistics.
 * @author bydga
 */
public class Consumer extends JSimProcess implements TransactionReceiver {

	private int consumed = 0;
	private double totalTransactionsTime = 0;

	public Consumer(String name, JSimSimulation parent) throws Exception {
		super(name, parent);
	}

	@Override
	public void receiveTransaction(Transaction transaction) {
		log(this.getName() + " consumed tx " + transaction);
		this.consumed++;
		this.totalTransactionsTime += this.getParent().getCurrentTime() - transaction.getCreatedTime();
	}

	public int getTransactionCount() {
		return this.consumed;
	}

	public double getTotalTransactionTime() {
		return this.totalTransactionsTime;
	}

	private void log(String msg) {
		if (Main.log) {
			System.out.println(this.getParent().getCurrentTime() + " > " + msg);
		}
	}
}

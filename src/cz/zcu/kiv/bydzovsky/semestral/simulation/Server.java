package cz.zcu.kiv.bydzovsky.semestral.simulation;

import cz.zcu.kiv.bydzovsky.semestral.Main;
import cz.zcu.fav.kiv.jsim.JSimHead;
import cz.zcu.fav.kiv.jsim.JSimInvalidParametersException;
import cz.zcu.fav.kiv.jsim.JSimProcess;
import cz.zcu.fav.kiv.jsim.JSimSecurityException;
import cz.zcu.fav.kiv.jsim.JSimSimulation;
import cz.zcu.kiv.bydzovsky.semestral.distributions.Distribution;

/**
 * Servers class, reads transactions from a queue, processes them for some
 * (random) time and finally passes them to a next object.
 *
 * @author bydga
 */
public class Server extends JSimProcess implements TransactionReceiver {

	private JSimHead queue;
	private Distribution distribution;
	private TransactionReceiver nextObject;
	private double totalPassiveTime;
	private int totalRequests;
	private double tq;
	private int actualRequests;

	public void setNextObject(TransactionReceiver nextObject) {
		this.nextObject = nextObject;
	}

	/**
	 * Returns how many seconds has this server been in a passivated state.
	 *
	 * @return how many seconds has this server been in a passivated state.
	 */
	public double getPassiveTime() {
		return this.totalPassiveTime;
	}

	/**
	 * Gets the queue's Lw
	 *
	 * @return the queue's Lw
	 */
	public double getQueueLw() {
		return this.queue.getLw();
	}

	/**
	 * Gets the queue's Tw
	 *
	 * @return the queue's Tw
	 */
	public double getQueueTw() {
		return this.queue.getTw();

	}

	/**
	 * Gets the total requests processed by this server.
	 *
	 * @return the total requests processed by this server.
	 */
	public int getTotalRequests() {
		return this.totalRequests;
	}

	/**
	 * Gets the total Tq of this server
	 *
	 * @return the total Tq of this server
	 */
	public double getTotalTq() {
		return this.tq;
	}

	/**
	 * Gets actual requests that are currently in a queue of this server.
	 *
	 * @return actual requests that are currently in a queue of this server.
	 */
	public int getActualRequests() {
		return this.actualRequests;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveTransaction(Transaction transaction) {
		log(this.getName() + " enqueued tx " + transaction.toString());
		this.actualRequests++;
		try {
			transaction.into(queue);
			if (this.isIdle()) {
				this.activateNow();
			}
		} catch (JSimSecurityException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Initializes new instance of a Server class.
	 *
	 * @param name
	 * @param parent
	 * @param distribution
	 * @throws Exception
	 */
	public Server(String name, JSimSimulation parent, Distribution distribution) throws Exception {
		super(name, parent);
		this.distribution = distribution;
		this.queue = new JSimHead("Queue for " + name, parent);
		this.totalRequests = 0;
		this.tq = 0;
		this.actualRequests = 0;
	}

	private void log(String msg) {
		if (Main.log) {
			System.out.println(this.getParent().getCurrentTime() + " > " + msg);
		}
	}

	@Override
	protected void life() {
		try {
			while (true) {
				if (this.queue.empty()) {
					log(this.getName() + " empty queue - passivated");
					double passiveStart = this.getParent().getCurrentTime();
					this.passivate();
					this.totalPassiveTime += this.getParent().getCurrentTime() - passiveStart;
				} else {
					Transaction t = (Transaction) this.queue.first();
					double enterTime = t.getEnterTime();
					double workTime = this.distribution.next();
					log(this.getName() + " got tx " + t + " from queue, processing for " + workTime);
					t.out();
					this.hold(workTime);
					log(this.getName() + " passing tx " + t + " to next object");
					this.actualRequests--;
					this.nextObject.receiveTransaction(t);
					this.totalRequests++;
					this.tq += this.getParent().getCurrentTime() - enterTime;
				}
			}
		} catch (JSimSecurityException e) {
			e.printStackTrace();
		} catch (JSimInvalidParametersException ex) {
			ex.printStackTrace();
		}
	}
}

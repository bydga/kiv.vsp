package cz.zcu.kiv.bydzovsky.semestral.simulation;

/**
 * Basic interface for the simulation. Every Node in the simulation that accepts a transaction must implement this interface.
 * @author bydga
 */
public interface TransactionReceiver {

	/**
	 * Each object that processes a transaction should do his work in this method.
	 * @param transaction 
	 */
	public void receiveTransaction(Transaction transaction);

	/**
	 * Only for logging purposes, usually this is already implemented by the JSimProcess super-class
	 * @return 
	 */
	public String getName();
}

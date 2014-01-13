package cz.zcu.kiv.bydzovsky.semestral.simulation;

import cz.zcu.fav.kiv.jsim.JSimLink;

/**
 * Simple class representing a transaction (request) in the simulation system.
 * @author bydga
 */
public class Transaction extends JSimLink {

	private double createdTime;

	/**
	 * Initializes new instance of Transaction class. 
	 * @param currentTime time this transacton was created.
	 */
	public Transaction(double currentTime) {
		this.createdTime = currentTime;
	}

	/**
	 * Retutns time this transaction was created.
	 * @return time this transaction was created.
	 */
	public double getCreatedTime() {
		return this.createdTime;
	}

	@Override
	public String toString() {
		//I dont want the fully qualified name cz.zcu.xxx.Transaction@hash. Just the hash is fine. For logging purposes.
		return Integer.toHexString(this.hashCode());
	}
}

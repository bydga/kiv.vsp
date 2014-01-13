package cz.zcu.kiv.bydzovsky.semestral.distributions;

/**
 *	Generic interface for generating random numbers with specific probability distributions.
 * @author bydga
 */
public interface Distribution {
	
	/**
	 * Essential method. Generates next random number with respect to given distribution.
	 * @return  Random number.
	 */
	public double next();
	/**
	 * Returns mean value of this distribution.
	 * @return mean value of this distribution.
	 */
	public double getMean();
	/**
	 * Returns variance of this distribution.
	 * @return 
	 */
	public double getVariance();
	
}

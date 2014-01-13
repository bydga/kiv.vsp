package cz.zcu.kiv.bydzovsky.semestral.distributions;

import java.util.Random;

/**
 * Implementation of exponential distribution random generator.
 *
 * @author bydga
 */
public class ExponentialDitribution implements Distribution {

	private double lambda;
	private Random rand;

	public ExponentialDitribution(double lambda, int seed) {
		if (lambda <= 0) {
			throw new IllegalArgumentException("Invalid lamda value for exponential distribution");
		}

		this.rand = new Random(seed);
		this.lambda = lambda;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return
	 */
	@Override
	public double next() {
		double rnd = this.rand.nextDouble();
		return -Math.log(1 - rnd) / lambda;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return
	 */
	@Override
	public double getMean() {
		return 1 / this.lambda;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return
	 */
	@Override
	public double getVariance() {
		return 1 / Math.pow(lambda, 2);
	}
}

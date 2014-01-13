package cz.zcu.kiv.bydzovsky.semestral.distributions;

import java.util.Random;

/**
 * Implementation of gaussian distribution random generator.
 * @author bydga
 */
public class GaussianDistribution implements Distribution {

	private Random rand;
	private double mean;
	private double variance;

	public GaussianDistribution(double mean, double variance, int seed) {
		this.rand = new Random(seed);
		this.mean = mean;
		this.variance = variance;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return
	 */
	@Override
	public double next() {
		double result;
		do {
			int cnt = 12;
			double sum = 0;
			for (int i = 0; i < cnt; i++) {
				sum += this.rand.nextDouble();
			}
			result = this.mean + this.variance * (sum - 6);
		} while (result < 0);
		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return
	 */
	@Override
	public double getMean() {
		return this.mean;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return
	 */
	@Override
	public double getVariance() {
		return this.variance;
	}
}

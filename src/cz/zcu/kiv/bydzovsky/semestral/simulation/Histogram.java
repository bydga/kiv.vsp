package cz.zcu.kiv.bydzovsky.semestral.simulation;

/**
 *
 * @author bydga
 */
public class Histogram {

	private static final int MAX_STARS = 50;
	private static final int INTERVAL_COUNT = 30;
	private static final int DECIMAL_PLACES = 2;
	private int[] histogram;
	private double intervalSize;
	private double maxCat = 0;
	private int count = 0;
	private double sum = 0;
	private double squareSum = 0;

	/**
	 * Creates new instance of histogram class.
	 * @param max  max value of the set that will be counted and splitted into intervals. Values larger than this value will be grouped as "max+"
	 */
	public Histogram(double max) {
		this.intervalSize = max / INTERVAL_COUNT;
		this.histogram = new int[INTERVAL_COUNT];
	}

	private int getValueInterval(double number) {
		return Math.min((int) (number / this.intervalSize), INTERVAL_COUNT - 1);
	}

	/**
	 * Adds another value into this histogram.
	 *
	 * @param val
	 */
	public void add(double val) {
		int interval = this.getValueInterval(val);
		this.histogram[interval]++;
		if (this.histogram[interval] > maxCat) {
			this.maxCat = this.histogram[interval];
		}

		this.sum += val;
		this.count++;
		this.squareSum += Math.pow(val, 2);
	}

	/**
	 * Returns mean value for the metric that is being measured by this histogram
	 *
	 * @return
	 */
	public double getMeanValue() {
		return this.sum / (double) this.count;
	}

	/**
	 * Returns variance value for the metric that is being measured by this
	 * histogram
	 *
	 * @return
	 */
	public double getVariance() {
		return (this.squareSum / (double) this.count) - Math.pow(getMeanValue(), 2);

	}

	/**
	 * Gets the formatted histogram output
	 *
	 * @return
	 */
	public String getHistogram() {
		StringBuilder sb = new StringBuilder();

		double rangePerStar = this.maxCat / (double) MAX_STARS;

		for (int i = 0; i < this.histogram.length; i++) {

			if (i == this.histogram.length - 1) {
				sb.append(String.format("%." + DECIMAL_PLACES + "f+        : ", (i) * intervalSize));
			} else {
				sb.append(String.format("%5." + DECIMAL_PLACES + "f - %5." + DECIMAL_PLACES + "f : ", (i) * intervalSize, (i + 1) * intervalSize));
			}
			int stars = (int) Math.round(this.histogram[i] / rangePerStar);
			for (int j = 0; j < stars; j++) {
				sb.append("*");
			}
			for (int j = 0; j < MAX_STARS - stars; j++) {
				sb.append(" ");
			}

			sb.append("(").append(this.histogram[i]).append(")\n");
		}

		return sb.toString();

	}
}

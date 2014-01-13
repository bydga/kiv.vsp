package cz.zcu.kiv.bydzovsky.semestral;

import cz.zcu.fav.kiv.jsim.JSimProcess;
import cz.zcu.fav.kiv.jsim.JSimSimulation;
import cz.zcu.kiv.bydzovsky.semestral.distributions.Distribution;
import cz.zcu.kiv.bydzovsky.semestral.distributions.ExponentialDitribution;
import cz.zcu.kiv.bydzovsky.semestral.distributions.GaussianDistribution;
import cz.zcu.kiv.bydzovsky.semestral.simulation.Simulation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry point for the application. Collects input parameters, runs
 * simulation and prints final results.
 *
 * @author bydga
 */
public class Main {

	/**
	 * Dumb flag whether to use logging or not inside simulation (custom logs, not
	 * defaul JSim messages)
	 */
	public static boolean log = false;
	//constants for distributions
	static double lambda1 = 0.6;
	static double lambda2 = 1.2;
	static double mi1 = 2.43;
	static double mi2 = 1.75;
	static double mi3 = 3.36;
	static double mi4 = 2.22;

	/**
	 * Entry point.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		//disable JSim logging
		Logger.getLogger(JSimProcess.class.getName()).setLevel(Level.WARNING);
		Logger.getLogger(JSimSimulation.class.getName()).setLevel(Level.WARNING);


		if (System.getenv().containsKey("VERBOSE")) {
			Main.log = true;
		}

		try {
			//defaults
			int repetitions = 500000;
			System.out.println("_________________________________________________________________________");
			System.out.println("|                          Network simulation                            |");
			System.out.println("|________________________________________________________________________|");
			System.out.println("| by Martin Bydzovsky, A12N0058P, bydgam@gmail.com                       |");
			System.out.println("|________________________________________________________________________|");
			System.out.println();
			System.out.println("If you wish to include detailed simulation info, set the enviroment variable VERBOSE");
			System.out.println("(i.e.: run the script like 'VERBOSE=1 java -jar vsp-final.jar [simulationSteps], [GAUSS|EXP]'\n\n");

			//default run with 1 exp + 3 gauss simulations
			if (args.length != 2) {
				System.out.println("No params found, setting default behaviour: One run with exponential distribution and three runs with normal distribution.");
				System.out.println("Now running simulation with exponential distribution...");
				Distribution g1distr = new ExponentialDitribution(lambda1, 11223);
				Distribution g2distr = new ExponentialDitribution(lambda2, 2345);
				Distribution s1distr = new ExponentialDitribution(mi1, 654);
				Distribution s2distr = new ExponentialDitribution(mi2, 999);
				Distribution s3distr = new ExponentialDitribution(mi3, 23);
				Distribution s4distr = new ExponentialDitribution(mi4, 666);
				Simulation s = new Simulation(repetitions, g1distr, g2distr, s1distr, s2distr, s3distr, s4distr);
				s.run();
				s.shutdown();

				System.out.println("Now running simulation with gaussian distribution, C = 0.05...");
				double C = 0.05;
				g1distr = new GaussianDistribution(1 / lambda1, C * 1 / lambda1, 11223);
				g2distr = new GaussianDistribution(1 / lambda2, C * 1 / lambda2, 2345);
				s1distr = new GaussianDistribution(1 / mi1, C * 1 / mi1, 654);
				s2distr = new GaussianDistribution(1 / mi2, C * 1 / mi2, 999);
				s3distr = new GaussianDistribution(1 / mi3, C * 1 / mi3, 23);
				s4distr = new GaussianDistribution(1 / mi4, C * 1 / mi4, 666);
				s = new Simulation(repetitions, g1distr, g2distr, s1distr, s2distr, s3distr, s4distr);
				s.run();
				s.shutdown();

				System.out.println("Now running simulation with gaussian distribution, C = 0.2...");
				C = 0.2;
				g1distr = new GaussianDistribution(1 / lambda1, C * 1 / lambda1, 11223);
				g2distr = new GaussianDistribution(1 / lambda2, C * 1 / lambda2, 2345);
				s1distr = new GaussianDistribution(1 / mi1, C * 1 / mi1, 654);
				s2distr = new GaussianDistribution(1 / mi2, C * 1 / mi2, 999);
				s3distr = new GaussianDistribution(1 / mi3, C * 1 / mi3, 23);
				s4distr = new GaussianDistribution(1 / mi4, C * 1 / mi4, 666);
				s = new Simulation(repetitions, g1distr, g2distr, s1distr, s2distr, s3distr, s4distr);
				s.run();
				s.shutdown();

				System.out.println("Now running simulation with gaussian distribution, C = 0.7...");
				C = 0.7;
				g1distr = new GaussianDistribution(1 / lambda1, C * 1 / lambda1, 11223);
				g2distr = new GaussianDistribution(1 / lambda2, C * 1 / lambda2, 2345);
				s1distr = new GaussianDistribution(1 / mi1, C * 1 / mi1, 654);
				s2distr = new GaussianDistribution(1 / mi2, C * 1 / mi2, 999);
				s3distr = new GaussianDistribution(1 / mi3, C * 1 / mi3, 23);
				s4distr = new GaussianDistribution(1 / mi4, C * 1 / mi4, 666);
				s = new Simulation(repetitions, g1distr, g2distr, s1distr, s2distr, s3distr, s4distr);
				s.run();
				s.shutdown();



			} else {
				repetitions = Integer.parseInt(args[0]);
				String distribution = args[1].toLowerCase();
				if (distribution.equals("exp")) {
					System.out.println(String.format("Starting exp simulation with parameters: %d", repetitions));
					Distribution g1distr = new ExponentialDitribution(0.6, 11223);
					Distribution g2distr = new ExponentialDitribution(1.2, 2345);
					Distribution s1distr = new ExponentialDitribution(2.43, 654);
					Distribution s2distr = new ExponentialDitribution(1.75, 999);
					Distribution s3distr = new ExponentialDitribution(3.36, 23);
					Distribution s4distr = new ExponentialDitribution(2.22, 666);
					Simulation s = new Simulation(repetitions, g1distr, g2distr, s1distr, s2distr, s3distr, s4distr);
					s.run();
					s.shutdown();
				} else if (distribution.equals("gauss")) {
					double C = 0.4;
					System.out.println(String.format("Starting gauss simulation with parameters: %d and C=%f", repetitions, C));
					Distribution g1distr = new GaussianDistribution(1 / lambda1, C * 1 / lambda1, 11223);
					Distribution g2distr = new GaussianDistribution(1 / lambda2, C * 1 / lambda2, 2345);
					Distribution s1distr = new GaussianDistribution(1 / mi1, C * 1 / mi1, 654);
					Distribution s2distr = new GaussianDistribution(1 / mi2, C * 1 / mi2, 999);
					Distribution s3distr = new GaussianDistribution(1 / mi3, C * 1 / mi3, 23);
					Distribution s4distr = new GaussianDistribution(1 / mi4, C * 1 / mi4, 666);
					Simulation s = new Simulation(repetitions, g1distr, g2distr, s1distr, s2distr, s3distr, s4distr);
					s.run();
					s.shutdown();
				} else {
					throw new RuntimeException("invalid distribution");
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

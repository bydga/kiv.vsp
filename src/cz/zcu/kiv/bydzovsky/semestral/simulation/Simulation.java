package cz.zcu.kiv.bydzovsky.semestral.simulation;

import cz.zcu.fav.kiv.jsim.JSimSimulation;
import cz.zcu.kiv.bydzovsky.semestral.distributions.Distribution;
import java.util.Arrays;

/**
 * Class that handles the whole life of a simulation. Creates all necessary objects in a simulation, sets all parameters and connections and runs the simulation. 
 * Prints all statistics at the end.
 * @author bydga
 */
public class Simulation extends JSimSimulation {

	private double repeats;
	private Generator generator1;
	private Generator generator2;
	private Server server1;
	private Server server2;
	private Server server3;
	private Server server4;
	private Crossing crossing1;
	private Crossing crossing2;
	private Consumer consumer;
	private Spy spy;

	/**
	 * Creates new instance of a Simulation class. Parameters are all necessary configuration params for a given simulation.
	 * @param repeats
	 * @param generator1Distribution
	 * @param generator2Distribution
	 * @param server1Distribution
	 * @param server2Distribution
	 * @param server3Distribution
	 * @param server4Distribution
	 * @throws Exception 
	 */
	public Simulation(int repeats, Distribution generator1Distribution, Distribution generator2Distribution,
					Distribution server1Distribution, Distribution server2Distribution, Distribution server3Distribution, Distribution server4Distribution) throws Exception {

		super("Vsp Simulation");
		this.repeats = repeats;

		this.generator1 = new Generator("generator1", this, generator1Distribution);
		this.generator2 = new Generator("generator2", this, generator2Distribution);
		this.server1 = new Server("server1", this, server1Distribution);
		this.server2 = new Server("server2", this, server2Distribution);
		this.server3 = new Server("server3", this, server3Distribution);
		this.server4 = new Server("server4", this, server4Distribution);
		this.crossing1 = new Crossing("crossing1", this, null);
		this.crossing2 = new Crossing("crossing2", this, this.server2);
		this.consumer = new Consumer("consumer", this);
		this.spy = new Spy("spy", this, Arrays.asList(this.server1, this.server2, this.server3, this.server4));

		//connect all objects
		this.generator1.setNextObject(this.server1);
		this.generator2.setNextObject(this.server2);
		this.server1.setNextObject(this.server3);
		this.server2.setNextObject(this.server3);
		this.server3.setNextObject(this.crossing1);
		this.server4.setNextObject(this.crossing2);
		this.crossing1.setNextObject1(this.server4, 0.7);
		this.crossing1.setNextObject2(this.server1);
		this.crossing2.setNextObject1(this.consumer, 0.9);
		this.crossing2.setNextObject2(this.server2);



	}

	/**
	 * The simulation body. Runs in loop with input-defined steps.
	 *
	 * @throws JSimException if an exception occures
	 */
	public void run() throws Exception {

		this.generator1.activateNow();
		this.generator2.activateNow();
		this.spy.activateNow();

		for (int i = 0; i < repeats; i++) {
			this.step();
		}

		double totalTime = this.getCurrentTime();
		System.out.println("--------------------------------------");
		System.out.println("              STATISTICS");
		System.out.println("--------------------------------------");
		System.out.println();
		System.out.println("Steps simulated: " + this.repeats);
		System.out.println("Total simulation time: " + totalTime);
		System.out.println("Total transactions generated: " + this.generator1.getGeneratedTransactions() + this.generator2.getGeneratedTransactions());
		System.out.println("observations done by spy: " + this.spy.getTotalObservations());
		System.out.println();
		System.out.println("Node statistics:");

		System.out.println("ro1 = " + (1 - this.server1.getPassiveTime() / totalTime));
		System.out.println("ro2 = " + (1 - this.server2.getPassiveTime() / totalTime));
		System.out.println("ro3 = " + (1 - this.server3.getPassiveTime() / totalTime));
		System.out.println("ro4 = " + (1 - this.server4.getPassiveTime() / totalTime));
		System.out.println();

		System.out.println("Lw1 = " + this.server1.getQueueLw());
		System.out.println("Lw2 = " + this.server2.getQueueLw());
		System.out.println("Lw3 = " + this.server3.getQueueLw());
		System.out.println("Lw4 = " + this.server4.getQueueLw());
		System.out.println();

		System.out.println("Lq1 = " + this.spy.getLqForServer(server1));
		System.out.println("Lq2 = " + this.spy.getLqForServer(server2));
		System.out.println("Lq3 = " + this.spy.getLqForServer(server3));
		System.out.println("Lq4 = " + this.spy.getLqForServer(server4));
		System.out.println();

		System.out.println("Tw1 = " + this.server1.getQueueTw());
		System.out.println("Tw2 = " + this.server2.getQueueTw());
		System.out.println("Tw3 = " + this.server3.getQueueTw());
		System.out.println("Tw4 = " + this.server4.getQueueTw());
		System.out.println();

		double tq1 = this.server1.getTotalTq() / this.server1.getTotalRequests();
		double tq2 = this.server2.getTotalTq() / this.server2.getTotalRequests();
		double tq3 = this.server3.getTotalTq() / this.server3.getTotalRequests();
		double tq4 = this.server4.getTotalTq() / this.server4.getTotalRequests();
		System.out.println("Tq1 = " + tq1);
		System.out.println("Tq2 = " + tq2);
		System.out.println("Tq3 = " + tq3);
		System.out.println("Tq4 = " + tq4);
		System.out.println();

		System.out.println("Lambda1 = " + this.spy.getLqForServer(server1) / tq1);
		System.out.println("Lambda2 = " + this.spy.getLqForServer(server2) / tq2);
		System.out.println("Lambda3 = " + this.spy.getLqForServer(server3) / tq3);
		System.out.println("Lambda4 = " + this.spy.getLqForServer(server4) / tq4);
		System.out.println();

		System.out.println("Global statistics:");
		System.out.println("Lq = " + this.spy.getTotalLq());
		System.out.println("Tq = " + this.consumer.getTotalTransactionTime() / this.consumer.getTransactionCount());

		System.out.println();
		System.out.println("Statistics for specific path:");
		System.out.println("E(x) = " + this.crossing2.getMeanValue());
		System.out.println("f(x) = " + 1 / this.crossing2.getMeanValue());
		System.out.println("D(x) = " + this.crossing2.getD());
		System.out.println();
		System.out.println("Histogram:");
		System.out.println(this.crossing2.getHistogram());
	}
}

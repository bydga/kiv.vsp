package cz.zcu.kiv.bydzovsky.semestral.simulation;

import cz.zcu.fav.kiv.jsim.JSimInvalidParametersException;
import cz.zcu.fav.kiv.jsim.JSimProcess;
import cz.zcu.fav.kiv.jsim.JSimSecurityException;
import cz.zcu.fav.kiv.jsim.JSimSimulation;
import cz.zcu.kiv.bydzovsky.semestral.distributions.Distribution;
import cz.zcu.kiv.bydzovsky.semestral.distributions.ExponentialDitribution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that observes the simulation in irregular intervals and measures some
 * statistics.
 *
 * @author bydga
 */
public class Spy extends JSimProcess {

	public Spy(String name, JSimSimulation parent, List<Server> servers) throws Exception {
		super(name, parent);
		this.servers = servers;
	}
	private List<Server> servers;
	private Map<String, List<Integer>> observations = new HashMap<String, List<Integer>>();
	private int cnt = 0;

	public int getTotalObservations() {
		return this.cnt;
	}

	@Override
	protected void life() {

		Distribution d = new ExponentialDitribution(0.2, 988);
		try {
			while (true) {
				//observe all servers
				for (Server s : this.servers) {
					List<Integer> list = this.observations.get(s.getName());
					if (list == null) {
						list = new ArrayList<Integer>();
					}
					list.add(s.getActualRequests());
					this.observations.put(s.getName(), list);
				}
				this.cnt++;
				this.hold(d.next());
			}
		} catch (JSimSecurityException ex) {
			ex.printStackTrace();
		} catch (JSimInvalidParametersException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Returns Lq value for a specified server
	 * @param s server from simulation to get Lq for.
	 * @return Lq value for a specified server
	 */
	public double getLqForServer(Server s) {
		List<Integer> list = this.observations.get(s.getName());
		int sum = 0;
		for (int i : list) {
			sum += i;
		}

		return sum / (double) list.size();
	}

	/**
	 * Returns total Lq for the whole simulation
	 * @return total Lq for the whole simulation
	 */
	public double getTotalLq() {

		double lq = 0;
		for (Server server : this.servers) {
			lq += this.getLqForServer(server);
		}

		return lq;
	}
}

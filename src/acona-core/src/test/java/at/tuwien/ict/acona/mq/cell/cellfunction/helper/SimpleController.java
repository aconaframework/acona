package at.tuwien.ict.acona.mq.cell.cellfunction.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import at.tuwien.ict.acona.mq.cell.cellfunction.CellFunctionThreadImpl;
import at.tuwien.ict.acona.mq.cell.cellfunction.ServiceState;
import at.tuwien.ict.acona.mq.datastructures.ControlCommand;
import at.tuwien.ict.acona.mq.datastructures.Datapoint;
import at.tuwien.ict.acona.mq.datastructures.Request;
import at.tuwien.ict.acona.mq.datastructures.Response;

public class SimpleController extends CellFunctionThreadImpl {

	private static Logger log = LoggerFactory.getLogger(SimpleController.class);

	private int delay = 200;

	@Override
	protected void cellFunctionThreadInit() throws Exception {
		this.delay = Integer.valueOf(this.getFunctionConfig().getProperty("delay", String.valueOf(this.delay)));
	}

	@Override
	protected void executeFunction() throws Exception {
		this.executeBlockingServiceById("servicename", "agent1", 1000);

		log.info("Function sequence controller finished");

	}

	/**
	 * This function loads the agent ids directly from the configuration
	 * 
	 * @param serviceNameId
	 * @param agentNameId
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	private void executeBlockingServiceById(String serviceNameId, String agentNameId, int timeout) throws Exception {
		executeService(this.getFunctionConfig().getProperty(serviceNameId), this.getFunctionConfig().getProperty(agentNameId), timeout);
	}

	private void executeService(String serviceName, String agentName, int timeout) throws Exception {
		String commandDatapoint = this.getDatapointBuilder().generateCellTopic(agentName) + ":" + serviceName + "/command";
		//String resultDatapoint = this.getDatapointBuilder().generateCellTopic(agentName) + "/" + serviceName + "/state";
		log.debug("Execute service={}", serviceName);
		Response result = this.getCommunicator().execute(commandDatapoint, 
				(new Request())
				.setParameter("command", ControlCommand.START)
				.setParameter("blocking", true), 100000);

		log.debug("Service={} executed. Result={}", commandDatapoint, result);
	}

	@Override
	protected void executeCustomPostProcessing() throws Exception {
		log.debug("{}> Start postprocessing", this.getFunctionRootAddress());

		// this.writeLocal(Datapoints.newDatapoint("state").setValue(ServiceState.FINISHED.toString()));
		log.debug("Set finished state");
		this.setFinishedAfterSingleRun(true);
		//this.setServiceState(ServiceState.FINISHED);
		//log.debug("Finished state set");

	}

	@Override
	protected void executeCustomPreProcessing() throws Exception {
		log.debug("{}> Start preprocessing", this.getFunctionRootAddress());

	}

	@Override
	protected void shutDownThreadExecutor() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateCustomDatapointsById(String id, JsonElement data) {
		// TODO Auto-generated method stub

	}

}

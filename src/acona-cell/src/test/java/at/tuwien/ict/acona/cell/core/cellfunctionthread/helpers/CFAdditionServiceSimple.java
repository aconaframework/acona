package at.tuwien.ict.acona.cell.core.cellfunctionthread.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import at.tuwien.ict.acona.cell.cellfunction.CellFunctionThreadImpl;
import at.tuwien.ict.acona.cell.config.DatapointConfig;
import at.tuwien.ict.acona.cell.datastructures.Datapoint;
import at.tuwien.ict.acona.framework.modules.ServiceState;

public class CFAdditionServiceSimple extends CellFunctionThreadImpl {

	private final String COMMANDDATAPOINTNAME = "command";
	private final String STATUSDATAPOINTNAME = "status";
	private final String OPERAND1 = "operand1";
	private final String OPERAND2 = "operand2";
	private final String RESULT = "result";
	
	private final Map<String, DatapointConfig> trackedDatapoints = new HashMap<String, DatapointConfig>();
	
	private double operand1;
	private double operand2;
	
	//private final String inputMemoryAgentName = "InputBufferAgent";
	//private final String memorydatapoint1 = "inputmemory.variable1";	//put into memory mock agent
	//private final String memorydatapoint2 = "inputmemory.variable2";	//put into memory mock agent
	
	public CFAdditionServiceSimple() {
		this.setExecuteOnce(true);	//Run only on demand from controller
	}
	
	@Override
	protected void executeFunction() throws Exception {
		//Read the values needed		
		try {
			operand1 = this.getCommunicator().read(this.trackedDatapoints.get(OPERAND1).getAddress(), this.trackedDatapoints.get(OPERAND1).getAgentid(), 1000000).getValue().getAsDouble();
			operand2 = this.getCommunicator().read(this.trackedDatapoints.get(OPERAND2).getAddress(), this.trackedDatapoints.get(OPERAND2).getAgentid(), 1000000).getValue().getAsDouble();
			
			log.info("read operand1={} and operand2={}", operand1, operand2);
		} catch (Exception e) {
			log.error("Cannot read datapoint", e);
			throw new Exception(e.getMessage());
		}
	
		//Add the values
		double result = operand1 + operand2;
		log.info("result={}", result);
		
		//Now send the result to a result datapoint
		this.getCommunicator().write(Datapoint.newDatapoint(this.trackedDatapoints.get(RESULT).getAddress()).setValue(new JsonPrimitive(result)), this.trackedDatapoints.get(RESULT).getAgentid());
	}

	@Override
	protected void cellFunctionInternalInit() throws Exception {
		//Add the datapoints from the config to the subscriptions
		this.trackedDatapoints.put(OPERAND1, DatapointConfig.newConfig(this.getConfig().getPropertyAsJsonObject(OPERAND1)));
		this.trackedDatapoints.put(OPERAND2, DatapointConfig.newConfig(this.getConfig().getPropertyAsJsonObject(OPERAND2)));
		this.trackedDatapoints.put(RESULT, DatapointConfig.newConfig(this.getConfig().getPropertyAsJsonObject(RESULT)));
		
	}

	@Override
	protected void executePostProcessing() throws Exception {
		//Set status that process is finished. Use it to release subscriptions
		this.getCommunicator().write(Datapoint.newDatapoint(STATUSDATAPOINTNAME).setValue(ServiceState.STOPPED.toString()));
		log.info("Function end after setting status={}", this.getCommunicator().read(STATUSDATAPOINTNAME));
	}

	@Override
	protected void executePreProcessing() throws Exception {
		//Set status that the system is running
		//this.getCommunicator().write(Datapoint.newDatapoint(this.getSubscribedDatapoints().get(STATUSDATAPOINTNAME).getAddress()).setValue(ServiceState.RUNNING.toString()));
		
	}

	@Override
	protected void updateDatapointsById(Map<String, Datapoint> data) {
		//React on the start trigger
		JsonElement value = data.get(COMMANDDATAPOINTNAME).getValue();
		if (data.containsKey(COMMANDDATAPOINTNAME) && data.get(COMMANDDATAPOINTNAME).getValue().isJsonPrimitive()==true) {
			try {
				this.setCommand(data.get(COMMANDDATAPOINTNAME).getValue().getAsString());
			} catch (Exception e) {
				log.error("Cannot read command", e);
			}
		} else {
			log.info("An unknown or empty command was put on the datapoint={}", data);
		}
		
	}

}
package at.tuwien.ict.acona.cell.cellfunction.specialfunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ict.acona.cell.cellfunction.CellFunctionImpl;
import at.tuwien.ict.acona.cell.config.DatapointConfig;
import at.tuwien.ict.acona.cell.datastructures.Datapoint;

public class CFDataStorageUpdate extends CellFunctionImpl {

	private static Logger log = LoggerFactory.getLogger(CFDataStorageUpdate.class);

	protected static final String ACKNOWLEDGE = "OK";
	protected static final String ERROR = "ERROR";
	protected static final String PARAMETERRESULT = "result";

	@Override
	public List<Datapoint> performOperation(Map<String, Datapoint> parameterdata, String caller) {
		List<Datapoint> result = new ArrayList<Datapoint>();
		//
		//		parameterdata.values().forEach(dp -> {
		//			try {
		//				this.getCell().getCommunicator().write(dp);
		//			} catch (Exception e) {
		//				log.error("Cannot write {} to datastorage", dp);
		//			}
		//		});
		//		try {
		//			result.add(Datapoint.newDatapoint(PARAMETERRESULT).setValue(ACKNOWLEDGE));
		//
		//		} catch (Exception e) {
		//			log.error("Cannot perform data storage update on parameter={}", parameterdata, e);
		//			result.add(Datapoint.newDatapoint(PARAMETERRESULT).setValue(ERROR));
		//		}
		//
		return result;
	}

	@Override
	protected void cellFunctionInit() throws Exception {
		log.debug("Datastorageupdate will happen for the following datapoints: {}", this.getSubscribedDatapoints());

		for (DatapointConfig config : this.getFunctionConfig().getSyncDatapoints()) {
			if (config.getAgentid().equals(this.getCell().getLocalName()) || (config.getAgentid().equals(""))) {
				throw new Exception("Function " + this.getFunctionName() + " is not allowed to subscribe datapoints of the own agent, in order to avoid circular references. Erroneous subscription: " + config);
			}
		}

	}

	@Override
	protected void shutDownImplementation() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateDatapointsById(Map<String, Datapoint> data) {
		data.values().forEach(dp -> {
			try {
				log.debug("Update datapoint={}", dp);
				this.getCell().getCommunicator().write(dp);
			} catch (Exception e) {
				log.error("Cannot write {} to datastorage", dp);
			}
		});
	}

}

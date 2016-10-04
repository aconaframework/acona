package at.tuwien.ict.acona.cell.core.cellfunction.helpers;

import com.google.gson.JsonObject;

import at.tuwien.ict.acona.cell.config.CellConfig;
import at.tuwien.ict.acona.cell.datastructures.Datapoint;
import at.tuwien.ict.acona.framework.modules.AconaServiceWithSubscribers;

public class CFIncrementService extends AconaServiceWithSubscribers {
	
	private static final String INCREMENTATIONDATAPOINTNAME = "increment";
	private static final String R = "rawdata";

	@Override
	protected void serviceInit() {
		log.trace("Init service={}", this.getFunctionName());
		
	}

	@Override
	protected void executeFunction() throws Exception {
		//Get the datapoint to increment with 1
		
		//Get settings from config
		CellConfig config2 = this.getConfig().getProperty("test", CellConfig.class);
		
		
		JsonObject rawdata = this.readLocalAsJson(R).getAsJsonObject();
		
		double value = this.readLocal(this.getSubscribedDatapoints().get(INCREMENTATIONDATAPOINTNAME).getAddress()).getValue().getAsDouble();
		log.info("Received value={}", value);
		value++;
		//write new value back to the same datapoint
		this.writeLocal(Datapoint.newDatapoint(this.getSubscribedDatapoints().get(INCREMENTATIONDATAPOINTNAME).getAddress()).setValue(String.valueOf(value)));
		log.debug("Function execution finished");
	}

}

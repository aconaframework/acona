package at.tuwien.ict.acona.cell.cellfunction;

import java.util.List;
import java.util.Map;

import at.tuwien.ict.acona.cell.config.CellFunctionConfig;
import at.tuwien.ict.acona.cell.config.DatapointConfig;
import at.tuwien.ict.acona.cell.core.Cell;
import at.tuwien.ict.acona.cell.datastructures.Datapoint;

public interface CellFunction {
	/**
	 * Initialize the cellfunction with the cell and a jsonobject with settings
	 * 
	 * @param settings:
	 *            Settings shall contain: functionname; subscriptions as a list
	 *            of ID, Agent, datapointaddress and optional conditions; custom
	 *            properties as json objects
	 * @param cell
	 * @return itself, in order to instantiate the cell and init at the same
	 *         time
	 * @throws Exception
	 */
	public CellFunction init(CellFunctionConfig config, Cell cell) throws Exception;

	/**
	 * Update subscribed data
	 * 
	 * @param datapoints,
	 *            which are subscribe
	 * @throws Exception
	 */
	public void updateSubscribedData(Map<String, Datapoint> data, String caller) throws Exception;

	/**
	 * Return the name of the function, which has been specified in the config
	 * file
	 * 
	 * @return Name of the function
	 */
	public String getFunctionName();

	/**
	 * Use these datapoints for the activatorhandler
	 * 
	 * @return Subscriptions
	 */
	public Map<String, DatapointConfig> getSubscribedDatapoints();

	public List<Datapoint> performOperation(Map<String, Datapoint> parameterdata, String caller);

	public void shutDown();

	public CellFunctionConfig getFunctionConfig();
}

package at.tuwien.ict.acona.cell.datastructures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Datapoint {
	private final static String KEYADDRESS = "ADDRESS";
	private final static String KEYTYPE = "TYPE";
	private final static String KEYVALUE = "VALUE";
	
	private String ADDRESS="";
	private String TYPE="";
	private JsonElement VALUE = new JsonObject();
	
	private final static Gson gson = new Gson();
	//private final JsonObject jsondatapoint;
	
	private static Logger log = LoggerFactory.getLogger(Datapoint.class);
	
	private Datapoint(String address) {
		//VALUE = new JsonObject();
		this.ADDRESS = address;
		//this.jsondatapoint.addProperty(KEYADDRESS, address);
		//this.jsondatapoint.addProperty(KEYTYPE, "");
		//this.jsondatapoint.addProperty(KEYVALUE, "");
	}
	
	public static Datapoint newDatapoint(String address) {
		return new Datapoint(address);
	}
	
	public static Datapoint toDatapoint(JsonObject data) throws IllegalArgumentException {
		Datapoint result = null;
		
		try {
			if (Datapoint.isDatapoint(data)==true) {
				result = Datapoint.newDatapoint(data.get(KEYADDRESS).getAsString()).setType(data.get(KEYTYPE).getAsString()).setValue(data.get(KEYVALUE));
			} else {
				throw new IllegalArgumentException("Cannot cast json data to datapoint");
			}
			
		} catch (IllegalArgumentException e) {
			throw e;
		}
		
		return result;
	}
	
	public static Datapoint toDatapoint(String data) throws Exception {
		log.debug("Datapoint to convert={}", data);
		JsonObject jsonData = gson.fromJson(data, JsonObject.class);
		return Datapoint.toDatapoint(jsonData);
	}
	
	public static boolean isDatapoint(JsonObject data) {
		boolean result = false;
		
		if (data.has(KEYADDRESS) && data.has(KEYTYPE) && data.has(KEYVALUE)) {
			result = true;
		}
		
		return result;
	}
	
	public Datapoint setType(String type) {
		this.TYPE = type;
		
		return this;
	}
	
	public Datapoint setValue(String value) {
		this.VALUE = new JsonPrimitive(value);
		
		return this;
	}
	
	public Datapoint setValue(JsonElement value) {
		this.VALUE = value;
		
		return this;
	}
	
	public JsonObject toJsonObject() {
		return gson.fromJson(this.toString(), JsonObject.class);
	}
	
	public String getAddress() {
		return ADDRESS; 
	}
	
	public String getType() {
		return TYPE;
	}
	
	public JsonElement getValue() {
		return VALUE;
	}
	
	public String getValueAsString() {
		return VALUE.getAsJsonPrimitive().getAsString();
	}

	@Override
	public String toString() {
		return gson.toJson(this, Datapoint.class);
	}
	
	
	
	
}

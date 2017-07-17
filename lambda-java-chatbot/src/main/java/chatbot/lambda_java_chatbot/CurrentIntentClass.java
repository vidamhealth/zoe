package chatbot.lambda_java_chatbot;

import java.util.HashMap;
import java.util.Map;

public class CurrentIntentClass {
	
	/**
	 * currentIntent": {
			    "name": "intent-name",
			    "slots": {
			      "slot-name": "value",
			      "slot-name": "value",
			      "slot-name": "value"
			    },
			    "confirmationStatus": "None, Confirmed, or Denied (intent confirmation, if configured)",
			  }
	 */

	String name;
	String confirmationStatus;
	//SlotsClass slots;
	Map<String, String> slots = new HashMap<String, String>();
	
	public CurrentIntentClass(){}
	
	public CurrentIntentClass(String name, Map<String, String> slots, String confirmationStatus){
		this.name = name;
		this.slots = slots;
		this.confirmationStatus = confirmationStatus;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setSlots(Map<String, String> slots){
		this.slots = slots;
	}
	
	public Map<String, String> getSlots(){
		return this.slots;
	}
	
	public String getConfirmationStatus(){
		return this.confirmationStatus;
	}
	
	public void setConfirmationStatus(String confirmationStatus){
		this.confirmationStatus = confirmationStatus;
	}
	
}

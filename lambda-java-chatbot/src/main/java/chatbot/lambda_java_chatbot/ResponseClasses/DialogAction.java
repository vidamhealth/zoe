package chatbot.lambda_java_chatbot.ResponseClasses;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class DialogAction {
	
	/**
	 * "dialogAction": {
        "type": "ElicitSlot",
        "intentName": "BookHotel",
        "slots": {
            "RoomType": null,
            "CheckInDate": null,
            "Nights": null,
            "Location": null
        },
        "slotToElicit": "Location",
        "message": {
            "contentType": "PlainText",
            "content": "We currently do not support Moscow as a valid destination.  Can you try a different city?"
        }
    }
	 */

	String type;
	String intentName;
	String fulfillmentState;
	String slotToElicit;
	ResponseCard responseCard;
	MessageClass message;
	Map<String, String> slots;
	
	public DialogAction() {}
	
	public DialogAction(String type, Map<String, String> slots) {
		this.type = type;
		this.slots = slots;
	}
	
	public DialogAction(String type, String fulfillmentState, MessageClass message) {
		this.type = type;
		this.fulfillmentState = fulfillmentState;
		this.message = message;
	}
	
	public DialogAction(String type, String fulfillmentState, ResponseCard responseCard, MessageClass message) {
		this.type = type;
		this.fulfillmentState = fulfillmentState;
		this.responseCard = responseCard;
		this.message = message;
	}
	
	public DialogAction(String type, String intentName, String slotToElicit,  Map<String, String> slots, MessageClass message) {
		this.type = type;
		this.intentName = intentName;
		this.slotToElicit = slotToElicit;
		this.slots = slots;
		this.message = message;
	}
	
	public DialogAction(String type, String intentName, String slotToElicit,  Map<String, String> slots, ResponseCard responseCard, MessageClass message) {
		this.type = type;
		this.intentName = intentName;
		this.slotToElicit = slotToElicit;
		this.slots = slots;
		this.responseCard = responseCard;
		this.message = message;
	}
	
	public void setResponseCard(ResponseCard responseCard) {
		this.responseCard = responseCard;
	}
	
	public ResponseCard getResponseCard() {
		return this.responseCard;
	}
	
	public void setIntenteName(String intentName) {
		this.intentName = intentName;
	} 
	
	public String getIntentName() {
		return this.intentName;
	}
	
	public void setSlotToElicit(String slotToElicit) {
		this.slotToElicit = slotToElicit;
	}
	
	public String getSlotToElicit() {
		return this.slotToElicit;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setFulfillmentState(String fulfillmentState) {
		this.fulfillmentState = fulfillmentState;
	}
	
	public String getFulfillmentState() {
		return this.fulfillmentState;
	}
	
	public void setSlots(Map<String, String> slots) {
		this.slots = slots;
	}
	
	public Map<String, String> getSlots(){
		return this.slots;
	}
	
	public void setMessage(MessageClass message) {
		this.message = message;
	}
	
	public MessageClass getMessage() {
		return this.message;
	}
	
}

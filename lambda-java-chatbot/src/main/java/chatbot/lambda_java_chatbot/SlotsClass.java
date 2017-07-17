package chatbot.lambda_java_chatbot;

public class SlotsClass {
	
	String slotName;
	//String slotValue;
	
	public SlotsClass(){
		
	}
	
	public SlotsClass(String slotName, String slotValue){
		this.slotName = slotName;
		//this.slotValue = slotValue;
	}
	
	public void setSlotName(String slotName){
		this.slotName = slotName;
	}

	public String getSlotName(){
		return this.slotName;
	}
	
	/*
	public void setSlotValue(String slotValue){
		this.slotValue = slotValue;
	}

	public String getSlotValue(){
		return this.slotValue;
	}
	*/
}

package chatbot.lambda_java_chatbot;

import java.util.HashMap;
import java.util.Map;

public class ResponseClass {
	
	Map<String, String> slots = new HashMap<String, String>();
    String greetings;

    public ResponseClass() {
    }
    
    public ResponseClass(Map<String, String> slots, String greetings) {
        this.greetings = greetings;
        this.slots = slots;
    }
    
    public Map<String, String> getSlots(){
    	    return this.slots;
    }
    
    public void setSlots(Map<String, String> slots) {
    	    this.slots = slots;
    }
    
    public String getGreetings() {
        return greetings;
    }

    public void setGreetings(String greetings) {
        this.greetings = greetings;
    }

}

package chatbot.lambda_java_chatbot.ResponseClasses;

import java.util.HashMap;
import java.util.Map;

public class ButtonsClass {
	
	Map<String, String> button = new HashMap<String, String>();

	public ButtonsClass() {}
	
	public ButtonsClass(Map<String, String> button) {
		this.button = button;
	}
	
	public Map<String, String> getButtons(){
		return this.button;
	}
	
}

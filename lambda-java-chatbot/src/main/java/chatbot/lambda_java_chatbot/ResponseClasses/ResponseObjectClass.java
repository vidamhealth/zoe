package chatbot.lambda_java_chatbot.ResponseClasses;

import java.util.HashMap;
import java.util.Map;

public class ResponseObjectClass {
	
	Map<String, String> sessionAttributes = new HashMap<String, String>();
	DialogAction dialogAction;
	
	public ResponseObjectClass(){}
	
	public ResponseObjectClass(Map<String, String> sessionAttributes, DialogAction dialogAction) {
		this.sessionAttributes = sessionAttributes;
		this.dialogAction = dialogAction;
	}
	
	public void setSessionAttributes(Map<String, String> sessionAttributes) {
		this.sessionAttributes = sessionAttributes;
	}
	
	public Map<String, String> getSessionAttributes(){
		return this.sessionAttributes;
	}
	
	public void setDialogAction(DialogAction dialogAction) {
		this.dialogAction = dialogAction;
	}
	
	public DialogAction getDialogAction() {
		return this.dialogAction;
	}

}

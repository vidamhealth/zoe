package chatbot.lambda_java_chatbot;

public class RequestClass {

	/**
	 * {
  "currentIntent": {
    "name": "intent-name",
    "slots": {
      "slotName": "value",
      "slotName1": "value",
      "slotName2": "value"
    },
    "confirmationStatus": "None, Confirmed, or Denied (intent confirmation, if configured)",
  },
  "bot": {
    "name": "bot-name",
    "alias": "bot-alias",
    "version": "bot-version"
  },
  "userId": "User ID specified in the POST request to Amazon Lex.",
  "inputTranscript": "Text used to process the request",
  "invocationSource": "FulfillmentCodeHook or DialogCodeHook",
  "outputDialogMode": "Text or Voice, based on ContentType request header in runtime API request",
  "messageVersion": "1.0",
  "sessionAttributes": { 
     "key1": "value1",
     "key2": "value2"
  }
}
	 */
	
    CurrentIntentClass currentIntent;
    BotClass bot;
    String userId;
    String inputTranscript;
    String invocationSource;
    String outputDialogMode;
    String messageVersion;

    public RequestClass(CurrentIntentClass currentIntent, BotClass bot, String userId, 
    		String inputTranscript, String invocationSource, String outputDialogMode, String messageVersion) {
    	this.currentIntent = currentIntent;
    	this.userId = userId;
        this.inputTranscript = inputTranscript;
        this.invocationSource = invocationSource;
        this.outputDialogMode = outputDialogMode;
        this.messageVersion = messageVersion;
    }

    public RequestClass() {
    }
    
    public CurrentIntentClass getCurrentIntent() {
        return this.currentIntent;
    }

    public void setCurrentIntent(CurrentIntentClass currentIntent) {
        this.currentIntent = currentIntent;
    }
    
    public void setUserId(String userId){
    	this.userId = userId;
    }
    
    public void setBot(BotClass bot){
    	this.bot = bot;
    }
    
    public BotClass getBot(){
    	return this.bot;
    }
    
    public String getUserId(){
    	return this.userId;
    }
    
    public void setInputTranscript(String inputTranscript){
    	this.inputTranscript = inputTranscript;
    }
    
    public String getInputTranscript(){
    	return this.inputTranscript;
    }

    public void setInvocationSource(String invocationSource){
    	this.invocationSource = invocationSource;
    }
    
    public String getInvocationSource(){
    	return this.invocationSource;
    }
    
    public void setOutputDialogMode(String outputDialogMode){
    	this.outputDialogMode = outputDialogMode;
    }
    
    public String getOutputDialogMode(){
    	return this.outputDialogMode;
    }
    
    public void setMessageVersion(String messageVersion){
    	this.messageVersion = messageVersion;
    }
    
    public String getMessageVersion(){
    	return this.messageVersion;
    }
}
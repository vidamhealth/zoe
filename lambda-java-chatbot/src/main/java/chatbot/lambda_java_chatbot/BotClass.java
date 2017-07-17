package chatbot.lambda_java_chatbot;

public class BotClass {
	
	/**
	 * "bot": {
		    "name": "bot-name",
		    "alias": "bot-alias",
		    "version": "bot-version"
	    }
	 */

	String name;
	String alias;
	String version;
	
	public BotClass(){
		
	}
	
	public BotClass(String name, String alias, String version){
		this.name = name;
		this.alias = alias;
		this.version = version;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setAlias(String alias){
		this.alias = alias;
	}
	
	public String getAlias(){
		return this.alias;
	}
	
	public void setVersion(String version){
		this.version = version;
	}
	
	public String getVersion(){
		return this.version;
	}
}

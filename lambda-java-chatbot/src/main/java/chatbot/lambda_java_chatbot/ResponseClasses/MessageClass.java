package chatbot.lambda_java_chatbot.ResponseClasses;

public class MessageClass {

	String contentType;
	String content;
	
	public MessageClass() {}
	
	public MessageClass(String contentType, String content) {
		this.contentType = contentType;
		this.content = content;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getContentType() {
		return this.contentType;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
	}
}

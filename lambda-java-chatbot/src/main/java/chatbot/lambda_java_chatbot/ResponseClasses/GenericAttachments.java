package chatbot.lambda_java_chatbot.ResponseClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenericAttachments {
	
	/**
	 * "genericAttachments": [
                {
                    "buttons": [
                        {
                            "text": "cleaning (30 min)",
                            "value": "cleaning"
                        },
                        {
                            "text": "root canal (60 min)",
                            "value": "root canal"
                        },
                        {
                            "text": "whitening (30 min)",
                            "value": "whitening"
                        }
                    ],
                    "subTitle": "What type of appointment would you like to schedule?",
                    "title": "Specify Appointment Type"
                }
            ],
	 */
	
	String subTitle;
	String title;
	String imageUrl;
	String attachmentLinkUrl;
	List<Map<String, String>> buttons = new ArrayList<Map<String, String>>();
	
	public GenericAttachments() {}
	
	public GenericAttachments(String subTitle, String title, String imageUrl, String attachmentLinkUrl, List<Map<String, String>> buttons) {
		this.subTitle = subTitle;
		this.title = title;
		this.imageUrl = imageUrl;
		this.buttons = buttons;
		this.attachmentLinkUrl = attachmentLinkUrl;
	}
	
	public String getSubTitle() {
		return this.subTitle;
	}

	public String getTitle() {
		return this.title;
	}
	
	public String getImageUrl() {
		return this.imageUrl;
	}
	
	public String getAttachmentLinkUrl() {
		return this.attachmentLinkUrl;
	}
	
	public List<Map<String, String>> getButtons(){
		return this.buttons;
	}
}

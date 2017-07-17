package chatbot.lambda_java_chatbot.ResponseClasses;

import java.util.ArrayList;
import java.util.List;

public class ResponseCard {
	
	/**
	 * "responseCard": {
            "genericAttachments": [
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
            "version": 1,
            "contentType": "application/vnd.amazonaws.card.generic"
        }
	 */
	
	int version;
	String contentType;
	List<GenericAttachments> genericAttachments = new ArrayList<GenericAttachments>();
	
	public ResponseCard() {}
	
	public ResponseCard(int version, String contentType, List<GenericAttachments> genericAttachments) {
		this.version = version;
		this.contentType = contentType;
		this.genericAttachments = genericAttachments;
	}

	public int getVersion() {
		return this.version;
	}
	
	public String getContentType() {
		return this.contentType;
	}
	
	public List<GenericAttachments> getGenericAttachments() {
		return this.genericAttachments;
	}
}

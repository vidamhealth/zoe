package chatbot.lambda_java_chatbot.Objects;

public class MealObject {
	
	String image;
	String name;
	String text;
	String calories;
	String url;
	
	public MealObject(){}
	
	public MealObject(String image, String name, String text, String calories, String url){
		this.image = image;
		this.name = name;
		this.text = text;
		this.calories = calories;
		this.url = url;
	}
	
	public String getImage(){
		return this.image;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getText(){
		return this.text;
	}
	
	public String getCalories(){
		return this.calories;
	}
	
	public String getUrl(){
		return this.url;
	}
}

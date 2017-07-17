package chatbot.lambda_java_chatbot.Objects;

public class DailyMealPlan {
	
	String day;
	MealObject breakfast;
	MealObject lunch;
	MealObject dinner;
	String calories;
	
	public DailyMealPlan(){}
	
	public DailyMealPlan(String day, MealObject breakfast, MealObject lunch, MealObject dinner, String calories){
		this.day = day;
		this.breakfast = breakfast;
		this.lunch = lunch;
		this.dinner = dinner;
		this.calories = calories;
	}
	
	public String getDay(){
		return this.day;
	}
	
	public MealObject getBreakfast(){
		return this.breakfast;
	}
	
	public MealObject getLunch(){
		return this.lunch;
	}
	
	public MealObject getDinner(){
		return this.dinner;
	}
	
	public String getCalories(){
		return this.calories;
	}

}

package chatbot.lambda_java_chatbot;

public class Consts {
	
	public static String INTENT_NUTRITION_DATA = "GetNutritionData";
	
	public static String INTENT_RECIPES_RECOMMENDATION = "GetRecipesIntent";
	public static String INTENT_GET_PROFILE = "RequestProfileData";
	public static String INTENT_CLOSING = "ClosingIntent";
	public static String INTENT_CREATE_MEAL_PLAN = "GetMealPlan";
	public static String INTENT_MY_WEEKLY_PLAN = "MyWeeklyPlan";
	public static String INTENT_CHANGE_PROFILE = "ChangeMyInfo";
	public static String INTENT_ADD_PROGRESS = "AddProgressIntent";
	
	public static String SLOT_FOOD = "food";
	public static String PROGRESS_PERCENT_SLOT = "progress_percent";
	public static String MEAL_TYPE_SLOT = "meal_type";
	public static String WEEK_DAY_SLOT = "plan_day";
	public static String PLAN_TYPE_SLOT = "plan_type";
	public static String WEIGHT_GOAL_SLOT = "weight_goal";
	public static String FIRST_NAME_SLOT = "first_name";
	public static String AGE_SLOT = "age";
	public static String GENDER_SLOT = "gender";
	public static String WEIGHT_SLOT = "weight";
	public static String HEIGHT_SLOT = "height";
	
	public static String ELICIT_SLOT = "ElicitSlot";
	public static String DELEGATE = "Delegate";
	public static String CLOSE = "Close";
	
	public static String API_URL = "https://api.nal.usda.gov/ndb/search/?format=json&q=bacon&max=1&offset=0&api_key=1IBkwELvwpCl5Sh0idUmLYpIgvCRDdKeS7nUYnax";

}

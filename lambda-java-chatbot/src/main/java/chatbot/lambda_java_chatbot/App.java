package chatbot.lambda_java_chatbot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.RequestHandler;

import chatbot.lambda_java_chatbot.ResponseClasses.ResponseObjectClass;
import chatbot.lambda_java_chatbot.HelperClasses.HelperFunctions;
import chatbot.lambda_java_chatbot.Objects.DailyMealPlan;
import chatbot.lambda_java_chatbot.Objects.MealObject;
import chatbot.lambda_java_chatbot.Objects.ProfileObject;
import chatbot.lambda_java_chatbot.ResponseClasses.ButtonsClass;
import chatbot.lambda_java_chatbot.ResponseClasses.DialogAction;
import chatbot.lambda_java_chatbot.ResponseClasses.GenericAttachments;
import chatbot.lambda_java_chatbot.ResponseClasses.MessageClass;
import chatbot.lambda_java_chatbot.ResponseClasses.ResponseCard;


public class App implements RequestHandler<RequestClass, ResponseObjectClass>{

	private ResponseClass response;
	static DynamoDB dynamoDB;
	String fisrtNameSlot;
	String genderSlot;
	String mealTypeSlot;
	int ageSlot;
	int heightSlot;
	double weightSlot;
	double bmi_value;
    double bmr_value;
    ProfileObject profile;
    boolean createNewPlan = false;
    boolean addNewProgress = false;
    boolean changeName = false;
    boolean changeAge = false;
    boolean changeGender = false;
    boolean changeWeight = false;
    boolean changeHeight = false;
    
   private boolean updateProfileName(RequestClass request, String newName){
    	Table table = dynamoDB.getTable("users_table");
    	try {
    	    UpdateItemSpec updateItemSpec1 = new UpdateItemSpec().withPrimaryKey("userId", request.getUserId())
                    .withUpdateExpression("set #a=:val1")
                    .withNameMap(new NameMap().with("#a", "user_name"))
                    .withValueMap(
                        new ValueMap().withString(":val1", newName))
                    .withReturnValues(ReturnValue.ALL_NEW);
    	    
                table.updateItem(updateItemSpec1);
	    } catch (Exception e) {
	    	     e.printStackTrace();
	    	     return false;
	    }
    	
    	return true;
    }
   
   private boolean updateProfileGender(RequestClass request, String newGender){
	   	Table table = dynamoDB.getTable("users_table");
	   	try {
	   	    UpdateItemSpec updateItemSpec1 = new UpdateItemSpec().withPrimaryKey("userId", request.getUserId())
	                   .withUpdateExpression("set #a=:val1")
	                   .withNameMap(new NameMap().with("#a", "user_gender"))
	                   .withValueMap(
	                       new ValueMap().withString(":val1", newGender))
	                   .withReturnValues(ReturnValue.ALL_NEW);
	   	    
	               table.updateItem(updateItemSpec1);
		    } catch (Exception e) {
		    	     e.printStackTrace();
		    	     return false;
		    }
	   	
	   	return true;
   }
   
   
   private boolean updateProfileAge(RequestClass request, int age){
	   	Table table = dynamoDB.getTable("users_table");
	   	try {
	   	    UpdateItemSpec updateItemSpec1 = new UpdateItemSpec().withPrimaryKey("userId", request.getUserId())
	                   .withUpdateExpression("set #a=:val1")
	                   .withNameMap(new NameMap().with("#a", "user_age"))
	                   .withValueMap(
	                       new ValueMap().withInt(":val1", age))
	                   .withReturnValues(ReturnValue.ALL_NEW);
	   	    
	               table.updateItem(updateItemSpec1);
		    } catch (Exception e) {
		    	     e.printStackTrace();
		    	     return false;
		    }
	   	
	   	return true;
   }
   
   private boolean updateProfileWeight(RequestClass request, double weight){
	   	Table table = dynamoDB.getTable("users_table");
	   	try {
	   	    UpdateItemSpec updateItemSpec1 = new UpdateItemSpec().withPrimaryKey("userId", request.getUserId())
	                   .withUpdateExpression("set #a=:val1")
	                   .withNameMap(new NameMap().with("#a", "user_weight"))
	                   .withValueMap(
	                       new ValueMap().withNumber(":val1", weight))
	                   .withReturnValues(ReturnValue.ALL_NEW);
	   	    
	               table.updateItem(updateItemSpec1);
		    } catch (Exception e) {
		    	     e.printStackTrace();
		    	     return false;
		    }
	   	
	   	return true;
   }
   
   private boolean updateProfileHeight(RequestClass request, int height){
	   	Table table = dynamoDB.getTable("users_table");
	   	try {
	   	    UpdateItemSpec updateItemSpec1 = new UpdateItemSpec().withPrimaryKey("userId", request.getUserId())
	                   .withUpdateExpression("set #a=:val1")
	                   .withNameMap(new NameMap().with("#a", "user_height"))
	                   .withValueMap(
	                       new ValueMap().withInt(":val1", height))
	                   .withReturnValues(ReturnValue.ALL_NEW);
	   	    
	               table.updateItem(updateItemSpec1);
		    } catch (Exception e) {
		    	     e.printStackTrace();
		    	     return false;
		    }
	   	
	   	return true;
  }
	
	public ResponseObjectClass handleRequest(RequestClass request, Context context) {
		
		if (request.getCurrentIntent().getName().equals(Consts.INTENT_CLOSING)) {
						
			MessageClass message = new MessageClass("PlainText", "Bye! See you");
			DialogAction dialogAction = new DialogAction(Consts.CLOSE, "Fulfilled", message);
			ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
			return response;
			
		}else if(request.getCurrentIntent().getName().equals(Consts.INTENT_CHANGE_PROFILE)){
			
			initializeDB();
			ProfileObject profile = retrieveProfileData(request);
			
			Map<String, String> slots = request.getCurrentIntent().getSlots();
			
			String firstName = slots.get(Consts.FIRST_NAME_SLOT);
			if(changeName && request.getCurrentIntent().getConfirmationStatus().equals("Confirmed")){
				changeName = false;
				Map<String, String> sessionAttributes = new HashMap<String, String>();
				DialogAction dialogAction = new DialogAction(Consts.DELEGATE, request.getCurrentIntent().getSlots());
				ResponseObjectClass delegateResponse = new ResponseObjectClass(sessionAttributes, dialogAction);
				return delegateResponse;
			}else if(firstName!=null && !profile.getName().equals(firstName)){
				updateProfileName(request, firstName);
			}else if (firstName == null) {
				if(changeName && request.getCurrentIntent().getConfirmationStatus().equals("Denied")){
					changeName = false;
					slots.put(Consts.FIRST_NAME_SLOT, profile.getName());
					request.getCurrentIntent().setSlots(slots);
				}else{
					changeName = true;
					List<GenericAttachments> list = new ArrayList<GenericAttachments>();
					List<Map<String, String>> confirmationButtonsList = new ArrayList<Map<String, String>>();
					Map<String, String> yesButton = new HashMap<String, String>();
					yesButton.put("text","yes");
					yesButton.put("value","yes");
					confirmationButtonsList.add(yesButton);
					Map<String, String> noButton = new HashMap<String, String>();
					noButton.put("text","no");
					noButton.put("value","no");
					confirmationButtonsList.add(noButton);
					GenericAttachments genericConfirmation = new GenericAttachments(
							null,
							"Change name confirmation", 
							null, 
							null,
							confirmationButtonsList);
					
					list.add(genericConfirmation);
					ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
					MessageClass message = new MessageClass("PlainText", "Do you want to change your first name?");
					DialogAction dialogAction = new DialogAction("ConfirmIntent", Consts.INTENT_CHANGE_PROFILE, null, request.getCurrentIntent().getSlots(), card, message);
					ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
					return response;
				}
				
			} 
			
			
			int ageSlot = (slots.get(Consts.AGE_SLOT) == null ? 0 : Integer.parseInt(slots.get(Consts.AGE_SLOT)));
			if(changeAge && request.getCurrentIntent().getConfirmationStatus().equals("Confirmed")){
				changeAge = false;
				Map<String, String> sessionAttributes = new HashMap<String, String>();
				DialogAction dialogAction = new DialogAction(Consts.DELEGATE, request.getCurrentIntent().getSlots());
				ResponseObjectClass delegateResponse = new ResponseObjectClass(sessionAttributes, dialogAction);
				return delegateResponse;
			}else if(ageSlot!=0 && !(profile.getAge()==ageSlot)){
				updateProfileAge(request, ageSlot);
			}else if (ageSlot == 0) {
				if(changeAge && request.getCurrentIntent().getConfirmationStatus().equals("Denied")){
					changeAge = false;
					slots.put(Consts.AGE_SLOT, String.valueOf(profile.getAge()));
					request.getCurrentIntent().setSlots(slots);
				}else{
					changeAge = true;
					List<GenericAttachments> list = new ArrayList<GenericAttachments>();
					List<Map<String, String>> confirmationButtonsList = new ArrayList<Map<String, String>>();
					Map<String, String> yesButton = new HashMap<String, String>();
					yesButton.put("text","yes");
					yesButton.put("value","yes");
					confirmationButtonsList.add(yesButton);
					Map<String, String> noButton = new HashMap<String, String>();
					noButton.put("text","no");
					noButton.put("value","no");
					confirmationButtonsList.add(noButton);
					GenericAttachments genericConfirmation = new GenericAttachments(
							null,
							"Change age confirmation", 
							null, 
							null,
							confirmationButtonsList);
					
					list.add(genericConfirmation);
					ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
					MessageClass message = new MessageClass("PlainText", "Do you want to change your age?");
					DialogAction dialogAction = new DialogAction("ConfirmIntent", Consts.INTENT_CHANGE_PROFILE, null, request.getCurrentIntent().getSlots(), card, message);
					ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
					return response;
				}
				
			} 
			
			
			String gender = slots.get(Consts.GENDER_SLOT);
			if(changeGender && request.getCurrentIntent().getConfirmationStatus().equals("Confirmed")){
				changeGender = false;
				Map<String, String> sessionAttributes = new HashMap<String, String>();
				DialogAction dialogAction = new DialogAction(Consts.DELEGATE, request.getCurrentIntent().getSlots());
				ResponseObjectClass delegateResponse = new ResponseObjectClass(sessionAttributes, dialogAction);
				return delegateResponse;
			}else if(gender!=null && !profile.getGender().equals(gender)){
				updateProfileGender(request, gender);
			}else if (gender == null) {
				if(changeGender && request.getCurrentIntent().getConfirmationStatus().equals("Denied")){
					changeGender = false;
					slots.put(Consts.GENDER_SLOT, profile.getGender());
					request.getCurrentIntent().setSlots(slots);
				}else{
					changeGender = true;
					List<GenericAttachments> list = new ArrayList<GenericAttachments>();
					List<Map<String, String>> confirmationButtonsList = new ArrayList<Map<String, String>>();
					Map<String, String> yesButton = new HashMap<String, String>();
					yesButton.put("text","yes");
					yesButton.put("value","yes");
					confirmationButtonsList.add(yesButton);
					Map<String, String> noButton = new HashMap<String, String>();
					noButton.put("text","no");
					noButton.put("value","no");
					confirmationButtonsList.add(noButton);
					GenericAttachments genericConfirmation = new GenericAttachments(
							null,
							"Change gender confirmation", 
							null, 
							null,
							confirmationButtonsList);
					
					list.add(genericConfirmation);
					ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
					MessageClass message = new MessageClass("PlainText", "Do you want to change your gender?");
					DialogAction dialogAction = new DialogAction("ConfirmIntent", Consts.INTENT_CHANGE_PROFILE, null, request.getCurrentIntent().getSlots(), card, message);
					ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
					return response;
				}
			} 
			
			
			double weightSlot = (slots.get(Consts.WEIGHT_SLOT) == null ? 0 : Double.parseDouble(slots.get(Consts.WEIGHT_SLOT)));
			if(changeWeight && request.getCurrentIntent().getConfirmationStatus().equals("Confirmed")){
				changeWeight = false;
				Map<String, String> sessionAttributes = new HashMap<String, String>();
				DialogAction dialogAction = new DialogAction(Consts.DELEGATE, request.getCurrentIntent().getSlots());
				ResponseObjectClass delegateResponse = new ResponseObjectClass(sessionAttributes, dialogAction);
				return delegateResponse;
			}else if(weightSlot!=0 && !(profile.getWeight()==weightSlot)){
				updateProfileWeight(request, weightSlot);
			}else if (weightSlot == 0) {
				if(changeWeight && request.getCurrentIntent().getConfirmationStatus().equals("Denied")){
					changeWeight = false;
					slots.put(Consts.WEIGHT_SLOT, String.valueOf(profile.getWeight()));
					request.getCurrentIntent().setSlots(slots);
				}else{
					changeWeight = true;
					List<GenericAttachments> list = new ArrayList<GenericAttachments>();
					List<Map<String, String>> confirmationButtonsList = new ArrayList<Map<String, String>>();
					Map<String, String> yesButton = new HashMap<String, String>();
					yesButton.put("text","yes");
					yesButton.put("value","yes");
					confirmationButtonsList.add(yesButton);
					Map<String, String> noButton = new HashMap<String, String>();
					noButton.put("text","no");
					noButton.put("value","no");
					confirmationButtonsList.add(noButton);
					GenericAttachments genericConfirmation = new GenericAttachments(
							null,
							"Change weight confirmation", 
							null, 
							null,
							confirmationButtonsList);
					
					list.add(genericConfirmation);
					ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
					MessageClass message = new MessageClass("PlainText", "Do you want to change your weight?");
					DialogAction dialogAction = new DialogAction("ConfirmIntent", Consts.INTENT_CHANGE_PROFILE, null, request.getCurrentIntent().getSlots(), card, message);
					ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
					return response;
				}
			} 
			
			
			int heightSlot = (slots.get(Consts.HEIGHT_SLOT) == null ? 0 : Integer.parseInt(slots.get(Consts.HEIGHT_SLOT)));
			if(changeHeight && request.getCurrentIntent().getConfirmationStatus().equals("Confirmed")){
				changeHeight = false;
				Map<String, String> sessionAttributes = new HashMap<String, String>();
				DialogAction dialogAction = new DialogAction(Consts.DELEGATE, request.getCurrentIntent().getSlots());
				ResponseObjectClass delegateResponse = new ResponseObjectClass(sessionAttributes, dialogAction);
				return delegateResponse;
			}else if(heightSlot!=0 && !(profile.getHeight()==heightSlot)){
				updateProfileHeight(request, heightSlot);
			}else if (heightSlot == 0) {
				if(changeHeight && request.getCurrentIntent().getConfirmationStatus().equals("Denied")){
					changeHeight = false;
					slots.put(Consts.HEIGHT_SLOT, String.valueOf(profile.getHeight()));
					request.getCurrentIntent().setSlots(slots);
				}else{
					changeHeight = true;
					List<GenericAttachments> list = new ArrayList<GenericAttachments>();
					List<Map<String, String>> confirmationButtonsList = new ArrayList<Map<String, String>>();
					Map<String, String> yesButton = new HashMap<String, String>();
					yesButton.put("text","yes");
					yesButton.put("value","yes");
					confirmationButtonsList.add(yesButton);
					Map<String, String> noButton = new HashMap<String, String>();
					noButton.put("text","no");
					noButton.put("value","no");
					confirmationButtonsList.add(noButton);
					GenericAttachments genericConfirmation = new GenericAttachments(
							null,
							"Change height confirmation", 
							null, 
							null,
							confirmationButtonsList);
					
					list.add(genericConfirmation);
					ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
					MessageClass message = new MessageClass("PlainText", "Do you want to change your height?");
					DialogAction dialogAction = new DialogAction("ConfirmIntent", Consts.INTENT_CHANGE_PROFILE, null, request.getCurrentIntent().getSlots(), card, message);
					ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
					return response;
				}
				
			} 
			
			List<GenericAttachments> list = new ArrayList<GenericAttachments>();
            List<Map<String, String>> confirmationButtonsList = new ArrayList<Map<String, String>>();
			
			Map<String, String> mainButton = new HashMap<String, String>();
			mainButton.put("text","main menu");
			mainButton.put("value","main menu");
			confirmationButtonsList.add(mainButton);
			//String subTitle, String title, String imageUrl, String attachmentLinkUrl, List<Map<String, String>> buttons
			GenericAttachments generic = new GenericAttachments(
					null,
					"Go to main menu",
					null, 
					null,
					confirmationButtonsList);
			list.add(generic);
			ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
			
			MessageClass message = new MessageClass("PlainText", "You can go back to main menu by typing my name Zoe or just choose the option main menu");
			DialogAction dialogAction = new DialogAction(Consts.CLOSE, "Fulfilled", card, message);
			ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
			return response;
			
		}else if (request.getCurrentIntent().getName().equals(Consts.INTENT_RECIPES_RECOMMENDATION)) {
						
			if (validateRecipeData(request)) {
				
				initializeDB();
				ArrayList<Item> recipes = retrieveRecipeData(request);
				if (recipes.size() > 0) {
					
					int i = randInt(0, recipes.size()-1);
					Item item = recipes.get(i);
					
					List<GenericAttachments> list = new ArrayList<GenericAttachments>();
					GenericAttachments generic = new GenericAttachments(
							item.getString("recipe_name"),
							item.getString("recipe_name"),
							item.getString("image_url"), 
							item.getString("recipe_url"),
							null);
					list.add(generic);
					
					ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
					MessageClass message = new MessageClass("PlainText", "Enjoy your meal! You can go back to main menu by typing my name Zoe or just choose the option main menu");
					DialogAction dialogActionTest = new DialogAction(
							Consts.CLOSE, 
							"Fulfilled", 
							card,
							message);
					ResponseObjectClass responseTest = new ResponseObjectClass(null, dialogActionTest);
					return responseTest;
					
				} else {
					
					MessageClass message = new MessageClass("PlainText", "It seems there was a problem while finding recipes! Please try again. You can go back to main menu by typing my name Zoe or just choose the option main menu");
					DialogAction dialogAction = new DialogAction(Consts.CLOSE, "Fulfilled", message);
					ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
					return response;
					
				}
				
			} else if (mealTypeSlot == null) {
				
				List<GenericAttachments> list = new ArrayList<GenericAttachments>();
				List<Map<String, String>> buttonsList = new ArrayList<Map<String, String>>();
				
				Map<String, String> breakfast = new HashMap<String, String>();
				breakfast.put("text","Breakfast");
				breakfast.put("value","breakfast");
				
				Map<String, String> lunch = new HashMap<String, String>();
				lunch.put("text","Lunch");
				lunch.put("value","lunch");
				
				Map<String, String> dinner = new HashMap<String, String>();
				dinner.put("text","Dinner");
				dinner.put("value","dinner");
				
				buttonsList.add(breakfast);
				buttonsList.add(lunch);
				buttonsList.add(dinner);
				
				GenericAttachments generic = new GenericAttachments(
						null,
						"Choose meal type", 
						"http://del.h-cdn.co/assets/16/51/480x719/gallery-1482350980-delish-slow-cooker-eggplant-parm-03-pin.jpg", 
						null,
						buttonsList);
				list.add(generic);
				
				ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
				MessageClass messageTest = new MessageClass("PlainText", "What type of meal would you like to have?");
				DialogAction dialogActionTest = new DialogAction(Consts.ELICIT_SLOT, Consts.INTENT_RECIPES_RECOMMENDATION, Consts.MEAL_TYPE_SLOT, request.getCurrentIntent().getSlots(), card, messageTest);
				ResponseObjectClass responseTest = new ResponseObjectClass(null, dialogActionTest);
				return responseTest;
				
			} else {
			
				MessageClass message = new MessageClass("PlainText", "It seems we do not support the type you asked. Choose between breakfast, lunch, dinner and healthy snacks");
				DialogAction dialogAction = new DialogAction(Consts.ELICIT_SLOT, Consts.INTENT_RECIPES_RECOMMENDATION, Consts.MEAL_TYPE_SLOT, request.getCurrentIntent().getSlots(), message);
				ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
				return response;
			}
			
		} else if (request.getCurrentIntent().getName().equals(Consts.INTENT_GET_PROFILE)) {
						
			initializeDB();
			ProfileObject profile = retrieveProfileData(request);
			if(profile!=null){
				
				List<GenericAttachments> list = new ArrayList<GenericAttachments>();
				
				/**
				 * Create add progress response card
				 */
				List<Map<String, String>> addProgress = new ArrayList<Map<String, String>>();
				Map<String, String> addProgressButton = new HashMap<String, String>();
				addProgressButton.put("text","Add progress");
				addProgressButton.put("value","add progress");
				addProgress.add(addProgressButton);
				
				GenericAttachments genericAddProgres = new GenericAttachments(
						"Add your daily fulfilment of your plan in percentage",
						"Add your daily progress", 
						"http://komensandiego.org/wp-content/uploads/2014/08/progress_line.jpeg", 
						null,
						addProgress);
				
				/**
				 * Create plan response card
				 */
				List<Map<String, String>> createPlan = new ArrayList<Map<String, String>>();
				Map<String, String> createPlanButton = new HashMap<String, String>();
				createPlanButton.put("text","Create plan");
				createPlanButton.put("value","get new plan");
				createPlan.add(createPlanButton);
				
				GenericAttachments genericCreatePlan = new GenericAttachments(
						"Let's create a meal plan based on your needs "+profile.getName(),
						"Weekly meal plan", 
						"https://realfood.tesco.com/media/images/healthy-eating-h-82fbe2cf-e11f-454b-8f9a-c7562af055a9-0-472x310.jpg", 
						null,
						createPlan);
				
				/**
				 * View existing plan response card
				 */
				List<Map<String, String>> viewPlan = new ArrayList<Map<String, String>>();
				Map<String, String> viewPlanButton = new HashMap<String, String>();
				viewPlanButton.put("text","View plan");
				viewPlanButton.put("value","view my plan");
				viewPlan.add(viewPlanButton);
				
				GenericAttachments genericViewPlan = new GenericAttachments(
						"Check your active weekly plan "+profile.getName(),
						"Your weekly plan", 
						"http://cf.the36thavenue.com/wp-content/uploads/2015/12/Weekly-Meal-Plan-300-the36thavenue.com-.jpg", 
						null,
						viewPlan);
				
				/**
				 * Get a healthy recipe response card
				 */
				List<Map<String, String>> getRecipe = new ArrayList<Map<String, String>>();
				Map<String, String> getRecipeButton = new HashMap<String, String>();
				getRecipeButton.put("text","Find healthy meal");
				getRecipeButton.put("value","find recipe");
				getRecipe.add(getRecipeButton);
				
				GenericAttachments genericGetRecipe = new GenericAttachments(
						profile.getName()+" get tips and healthy meal recipes!",
						"Find a healthy meal", 
						"http://ghk.h-cdn.co/assets/17/26/480x240/landscape-1498675563-tofu-recipes.jpg", 
						null,
						getRecipe);
				
				/**
				 * Change personal data response card
				 */
				List<Map<String, String>> changeData = new ArrayList<Map<String, String>>();
				Map<String, String> changeDataButton = new HashMap<String, String>();
				changeDataButton.put("text","change my info");
				changeDataButton.put("value","change my info");
				changeData.add(changeDataButton);
				
				GenericAttachments genericChangeData = new GenericAttachments(
						profile.getName()+" do you want to change your profile?",
						"Change profile data", 
						"http://www.regna.com.tr/wp-content/uploads/2017/02/regna-musteri-profilleme.jpg", 
						null,
						changeData);

				list.add(genericAddProgres);
				list.add(genericCreatePlan);
				list.add(genericViewPlan);
				list.add(genericGetRecipe);
				list.add(genericChangeData);
				
				ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
				String messageString = "Welcome back "+profile.getName()+"! How can i help you today?";
				if (profile.getMealPlan() != null){		
					DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
					Date dateNow = new Date();
					Date date;
					try {
						date = format.parse(profile.getPlanCreated());
						int diffInDays = (int)( (dateNow.getTime() - date.getTime()) / (1000 * 60 * 60 * 24) );
						int totalProgress;
						if (diffInDays>0 || getProgressDataDate(request, new SimpleDateFormat("yyyy/MM/dd").format(new Date()))){
							diffInDays = diffInDays + 1;
							totalProgress = getProgressData(request, profile.getPlanCreated());
							
							if(totalProgress > 1){
								//""
								messageString = "Welcome back "+profile.getName()+"! Based on your daily progress you fulfilled until now the "+(totalProgress/diffInDays)+"% of your meal plan (if you don't add your progress it will count as 0%)! How can i help you today?";
							}else{
								messageString = "Welcome back "+profile.getName()+"! Based on your progress add it seems that you are not doing very well with your meal plan until now (the days you do not add your progress count as 0)! You may consider of changing your meal plan!";
							}
						}
						
					} catch (ParseException e) {
						e.printStackTrace();
					}		
					
				}
				
				MessageClass message = new MessageClass("PlainText", messageString);
				DialogAction dialogAction = new DialogAction(Consts.CLOSE, "Fulfilled", card, message);
				ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
				return response;
				
			}else{
				
				if (validateProfileData(request)) {
					
					initializeDB();
					insertUserData(request);
					profile = retrieveProfileData(request);
					
					List<GenericAttachments> list = new ArrayList<GenericAttachments>();
					
					/**
					 * Create add progress response card
					 */
					/*
					List<Map<String, String>> addProgress = new ArrayList<Map<String, String>>();
					Map<String, String> addProgressButton = new HashMap<String, String>();
					addProgressButton.put("text","Add progress");
					addProgressButton.put("value","add progress");
					addProgress.add(addProgressButton);
					
					GenericAttachments genericAddProgres = new GenericAttachments(
							"Add your daily fulfilment of your plan",
							"Add your daily progress", 
							"http://komensandiego.org/wp-content/uploads/2014/08/progress_line.jpeg", 
							null,
							addProgress);
							*/
					
					/**
					 * Create plan response card
					 */
					List<Map<String, String>> createPlan = new ArrayList<Map<String, String>>();
					Map<String, String> createPlanButton = new HashMap<String, String>();
					createPlanButton.put("text","Create plan");
					createPlanButton.put("value","get new plan");
					createPlan.add(createPlanButton);
					
					GenericAttachments genericCreatePlan = new GenericAttachments(
							"Let's create a meal plan based on your needs "+fisrtNameSlot,
							"Weekly meal plan", 
							"https://realfood.tesco.com/media/images/healthy-eating-h-82fbe2cf-e11f-454b-8f9a-c7562af055a9-0-472x310.jpg", 
							null,
							createPlan);
					
					/**
					 * View existing plan response card
					 */
					/*
					List<Map<String, String>> viewPlan = new ArrayList<Map<String, String>>();
					Map<String, String> viewPlanButton = new HashMap<String, String>();
					viewPlanButton.put("text","change my info");
					viewPlanButton.put("value","change my info");
					viewPlan.add(viewPlanButton);
					
					GenericAttachments genericViewPlan = new GenericAttachments(
							"Check your active weekly plan "+fisrtNameSlot,
							"Your weekly plan", 
							"http://cf.the36thavenue.com/wp-content/uploads/2015/12/Weekly-Meal-Plan-300-the36thavenue.com-.jpg", 
							null,
							viewPlan);
							*/
					
					/**
					 * Get a healthy recipe response card
					 */
					List<Map<String, String>> getRecipe = new ArrayList<Map<String, String>>();
					Map<String, String> getRecipeButton = new HashMap<String, String>();
					getRecipeButton.put("text","Find healthy meal");
					getRecipeButton.put("value","find recipe");
					getRecipe.add(getRecipeButton);
					
					GenericAttachments genericGetRecipe = new GenericAttachments(
							fisrtNameSlot+" get tips and healthy meal recipes!",
							"Find a healthy meal", 
							"http://ghk.h-cdn.co/assets/17/26/480x240/landscape-1498675563-tofu-recipes.jpg", 
							null,
							getRecipe);
					
					/**
					 * Change personal data response card
					 */
					
					List<Map<String, String>> changeData = new ArrayList<Map<String, String>>();
					Map<String, String> changeDataButton = new HashMap<String, String>();
					changeDataButton.put("text","change my info");
					changeDataButton.put("value","change my info");
					changeData.add(changeDataButton);
					
					GenericAttachments genericChangeData = new GenericAttachments(
							fisrtNameSlot+" do you want to change your profile?",
							"Change profile data", 
							"http://www.regna.com.tr/wp-content/uploads/2017/02/regna-musteri-profilleme.jpg", 
							null,
							changeData);

					//list.add(genericAddProgres);
					//list.add(genericViewPlan);
					list.add(genericCreatePlan);
					list.add(genericChangeData);
					list.add(genericGetRecipe);
					
					ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
					
					String messageString = "";
					if (profile!=null){
						double bmi = profile.getBmi();
						if (bmi >= 25){
							//overweight
							messageString = "Your BMI is "+bmi+", indicating your weight is "+HelperFunctions.checkBmi(bmi, profile.getWeight(), profile.getHeight())+"% above normal People who are overweight or obese are at higher risk for chronic conditions such as high blood pressure, diabetes, and high cholesterol. Talk with your healthcare provider to determine appropriate ways to lose weight.";
						}else if (bmi >= 18.5 && bmi < 25){
							//normal
							messageString = "Well done "+profile.getName()+"! Your BMI is "+bmi+", indicating your weight is within the normal range.";
						}else{
							//underweight
							messageString = "Your BMI is "+bmi+", indicating your weight is "+HelperFunctions.checkBmi(bmi, profile.getWeight(), profile.getHeight())+"% below normal Talk with your healthcare provider to determine possible causes of underweight and if you need to gain weight.";
						}
					}	
					
					messageString = messageString+" \n\n Do you want to continue with setting up your meal plan or change your information and re-evaluate your weight status? In addition, you can search for a healthy meal. Choose one of the options.";
					
					MessageClass message = new MessageClass("PlainText", messageString);
					DialogAction dialogAction = new DialogAction(Consts.CLOSE, "Fulfilled", card, message);
					ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
					return response;
					
				} else {
					Map<String, String> sessionAttributes = new HashMap<String, String>();
					DialogAction dialogAction = new DialogAction(Consts.DELEGATE, request.getCurrentIntent().getSlots());
					ResponseObjectClass delegateResponse = new ResponseObjectClass(sessionAttributes, dialogAction);
					return delegateResponse;
					
				}
				
			}
			
		}else if (request.getCurrentIntent().getName().equals(Consts.INTENT_CREATE_MEAL_PLAN)){
			
			initializeDB();
			ProfileObject profile = retrieveProfileData(request);
			
			if (profile.getMealPlan() != null){
				
				if (!createNewPlan){
				
					if(request.getCurrentIntent().getConfirmationStatus().toLowerCase().equals("confirmed")){
						
						createNewPlan = true;
						
					}else if(request.getCurrentIntent().getConfirmationStatus().toLowerCase().equals("denied")){
						
						List<GenericAttachments> list = new ArrayList<GenericAttachments>();
                        List<Map<String, String>> confirmationButtonsList = new ArrayList<Map<String, String>>();
						
						Map<String, String> mainButton = new HashMap<String, String>();
						mainButton.put("text","main menu");
						mainButton.put("value","main menu");
						confirmationButtonsList.add(mainButton);
						//String subTitle, String title, String imageUrl, String attachmentLinkUrl, List<Map<String, String>> buttons
						GenericAttachments generic = new GenericAttachments(
								null,
								"Go to main menu",
								null, 
								null,
								confirmationButtonsList);
						list.add(generic);
						ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
						
						MessageClass message = new MessageClass("PlainText", "You can go back to main menu by typing my name Zoe or just choose the option main menu");
						DialogAction dialogAction = new DialogAction(Consts.CLOSE, "Fulfilled",card, message);
						ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
						return response;
						
					}else{
						
	                    List<GenericAttachments> list = new ArrayList<GenericAttachments>();
						
						/**
						 * Create confirmation response card
						 */
						List<Map<String, String>> confirmationButtonsList = new ArrayList<Map<String, String>>();
						
						Map<String, String> yesButton = new HashMap<String, String>();
						yesButton.put("text","yes");
						yesButton.put("value","yes");
						confirmationButtonsList.add(yesButton);
						
						Map<String, String> noButton = new HashMap<String, String>();
						noButton.put("text","no");
						noButton.put("value","no");
						confirmationButtonsList.add(noButton);
						
						GenericAttachments genericConfirmation = new GenericAttachments(
								null,
								"Create new plan?", 
								null, 
								null,
								confirmationButtonsList);
						
						list.add(genericConfirmation);
						ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
						MessageClass message = new MessageClass("PlainText", "There is an active meal plan! Are you sure you want to create a new plan?");
						DialogAction dialogAction = new DialogAction("ConfirmIntent", Consts.INTENT_CREATE_MEAL_PLAN, null, request.getCurrentIntent().getSlots(), card, message);
						ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
						return response;
						
					}
					
				}
				
			}
			
			if (validatePlanType(request)){
				
				Map<String, String> slots = request.getCurrentIntent().getSlots();
				String planType = slots.get(Consts.PLAN_TYPE_SLOT);
				ArrayList<Item> plans = new ArrayList<Item>();
				
				if (planType.contains("loose")) {
					
					initializeDB();
					plans = getMealPlan(request, "loose", profile.getBmr());				
					
				}else if (planType.contains("gain")){
					
					initializeDB();
					plans = getMealPlan(request, "gain", profile.getBmr());
					
				}else if (planType.contains("maintain")){
					
					initializeDB();
					plans = getMealPlan(request, "maintain", profile.getBmr());
					
				}
				
				if (plans.size() > 0){
					
					createNewPlan = false;
					
					int i = randInt(0, plans.size()-1);
					Item item = plans.get(i);
					System.out.println("Meal plan json: "+item.getString("plan_json"));	
					if (saveMealPlan(request, item)){
					
						List<GenericAttachments> list = new ArrayList<GenericAttachments>();
						List<Map<String, String>> buttonsList = new ArrayList<Map<String, String>>();
						
						Map<String, String> checkPlan = new HashMap<String, String>();
						checkPlan.put("text","view my plan");
						checkPlan.put("value","view my plan");
						
						Map<String, String> mainMenu = new HashMap<String, String>();
						mainMenu.put("text","main menu");
						mainMenu.put("value","main menu");
						
						buttonsList.add(checkPlan);
						buttonsList.add(mainMenu);
						
						GenericAttachments generic = new GenericAttachments(
								null,
								"What would you like to do next?", 
								null, 
								null,
								buttonsList);
						list.add(generic);
						
						ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
						MessageClass message = new MessageClass("PlainText", "The indicative plan for you is ready. You can view it or you can go to main menu. Choose one option.");
						DialogAction dialogAction = new DialogAction(Consts.CLOSE, "Fulfilled", card, message);
						ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
						return response;
					
				}else{
					
					MessageClass message = new MessageClass("PlainText", "It seems there was a problem while creating your plan! Please try again");
					DialogAction dialogAction = new DialogAction(Consts.CLOSE, "Fulfilled", message);
					ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
					return response;
					
				}
					
				}
				
			}else{
			
				List<GenericAttachments> list = new ArrayList<GenericAttachments>();
				List<Map<String, String>> buttonsList = new ArrayList<Map<String, String>>();
				
				Map<String, String> looseWeight = new HashMap<String, String>();
				looseWeight.put("text","loose weight");
				looseWeight.put("value","loose weight");
		
				Map<String, String> maintainWeight = new HashMap<String, String>();
				maintainWeight.put("text","maintain weight");
				maintainWeight.put("value","maintain weight");
				
				Map<String, String> gainWeight = new HashMap<String, String>();
				gainWeight.put("text","gain weight");
				gainWeight.put("value","gain weight");
				
				buttonsList.add(looseWeight);
				buttonsList.add(maintainWeight);
				buttonsList.add(gainWeight);
				
				GenericAttachments generic = new GenericAttachments(
						"What would you like the meal plan to help you with?",
						"Meal plan", 
						null, 
						null,
						buttonsList);
				list.add(generic);
				ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
				
				String messageString = "What would you like the meal plan to help you with?";
				if (profile!=null){
					double bmi = profile.getBmi();
					if (bmi >= 25 ){
						//overweight
						messageString = "I recommend for you to choose a meal plan lose weight. Before proceed, I suggest to talk with your healthcare provider to determine appropriate ways to lose weight.";
					}else if (bmi >= 18.5 && bmi < 25){
						//normal
						messageString = "I recommend for you to choose a meal plan for maintaining your weight.";
					}else{
						//underweight
						messageString = "I recommend for you to choose a meal plan for gain weight. Before proceed, I suggest to talk with your healthcare provider to determine possible causes of underweight and if you need to gain weight.";
					}
					
					
					messageString = messageString+"\n\n Before you get started, pick the program you want to join. I will customize your personal nutrition plan.";
				}	
				
				MessageClass message = new MessageClass("PlainText", messageString);
				DialogAction dialogAction = new DialogAction(Consts.ELICIT_SLOT, Consts.INTENT_CREATE_MEAL_PLAN, Consts.PLAN_TYPE_SLOT, request.getCurrentIntent().getSlots(), card, message);
				ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
				return response;
				
			}
			
		}else if (request.getCurrentIntent().getName().equals(Consts.INTENT_MY_WEEKLY_PLAN)){
			
			initializeDB();
			ProfileObject profile = retrieveProfileData(request);
			
			if (profile.getMealPlan()!=null){
			
				if (validatePlanDay(request)){
					
					Map<String, String> slots = request.getCurrentIntent().getSlots();
					String weekDaySlot = slots.get(Consts.WEEK_DAY_SLOT);
					initializeDB();
					Item item = getUserPlan(request);
					JSONArray days = new JSONArray(item.getString("user_meal_plan"));
					
					String dailyPlan = "not found";
					JSONObject dayJson;
					if (weekDaySlot.toLowerCase().equals("monday")){
					    dayJson = new JSONObject(String.valueOf(days.get(0)));
					}else if (weekDaySlot.toLowerCase().equals("tuesday")){
						dayJson = new JSONObject(String.valueOf(days.get(1)));
					}else if (weekDaySlot.toLowerCase().equals("wednesday")){
						dayJson = new JSONObject(String.valueOf(days.get(2)));
					}else if (weekDaySlot.toLowerCase().equals("thursday")){
						dayJson = new JSONObject(String.valueOf(days.get(3)));
					}else if (weekDaySlot.toLowerCase().equals("friday")){
						dayJson = new JSONObject(String.valueOf(days.get(4)));
					}else if (weekDaySlot.toLowerCase().equals("saturday")){
						dayJson = new JSONObject(String.valueOf(days.get(5)));
					}else{
						dayJson = new JSONObject(String.valueOf(days.get(6)));
					}
					
					System.out.println("Day json is: "+dayJson.toString());
					JSONObject breakfastJson = dayJson.getJSONObject("breakfast");
				    JSONObject lunchJson = dayJson.getJSONObject("lunch");
				    JSONObject dinnerJson = dayJson.getJSONObject("dinner");
				  
				    MealObject breakfast = new MealObject(
				    		breakfastJson.getString("recipe_image"),
				    		breakfastJson.getString("recipe_name"),
				    		breakfastJson.getString("recipe_text"),
				    		breakfastJson.getString("recipe_calories"),
				    		breakfastJson.getString("recipe_url"));
				    MealObject lunch = new MealObject(
				    		lunchJson.getString("recipe_image"),
				    		lunchJson.getString("recipe_name"),
				    		lunchJson.getString("recipe_text"),
				    		lunchJson.getString("recipe_calories"),
				    		lunchJson.getString("recipe_url"));
				    MealObject dinner = new MealObject(
				    		dinnerJson.getString("recipe_image"),
				    		dinnerJson.getString("recipe_name"),
				    		dinnerJson.getString("recipe_text"),
				    		dinnerJson.getString("recipe_calories"),
				    		dinnerJson.getString("recipe_url"));
				    //String day, MealObject breakfast, MealObject lunch, MealObject dinner, String calories
				    DailyMealPlan dailyMealPlan = new DailyMealPlan(
				    		weekDaySlot.toLowerCase(), 
				    		breakfast,
				    		lunch,
				    		dinner,
				    		null);
					
					List<GenericAttachments> list = new ArrayList<GenericAttachments>();
					
					/**
					 * Breakfast
					 */
					GenericAttachments genericBreakfast = new GenericAttachments(
							dailyMealPlan.getBreakfast().getName(),
							"Breakfast meal", 
							dailyMealPlan.getBreakfast().getImage(), 
							dailyMealPlan.getBreakfast().getUrl(),
							null);
					
					/**
					 * Lunch
					 */
					GenericAttachments genericLunch = new GenericAttachments(
							dailyMealPlan.getLunch().getName(),
							"Lunch meal", 
							dailyMealPlan.getLunch().getImage(), 
							dailyMealPlan.getLunch().getUrl(),
							null);
					
					/**
					 * Dinner
					 */
					GenericAttachments genericDinner = new GenericAttachments(
							dailyMealPlan.getDinner().getName(),
							"Dinner meal", 
							dailyMealPlan.getDinner().getImage(), 
							dailyMealPlan.getDinner().getUrl(),
							null);

					list.add(genericBreakfast);
					list.add(genericLunch);
					list.add(genericDinner);
					
					ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
					MessageClass message = new MessageClass("PlainText", "Your daily meal plan for "+weekDaySlot+"! Now, you can choose another day of the week or typing main menu to go to the main menu:");
					DialogAction dialogAction = new DialogAction(Consts.ELICIT_SLOT, Consts.INTENT_MY_WEEKLY_PLAN, Consts.WEEK_DAY_SLOT, request.getCurrentIntent().getSlots(), card, message);
					ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
					return response;
					
				}else{
					
					Map<String, String> sessionAttributes = new HashMap<String, String>();
					DialogAction dialogAction = new DialogAction(Consts.DELEGATE, request.getCurrentIntent().getSlots());
					ResponseObjectClass delegateResponse = new ResponseObjectClass(sessionAttributes, dialogAction);
					return delegateResponse;
					
				}
				
			}else{
				
				List<GenericAttachments> list = new ArrayList<GenericAttachments>();
				List<Map<String, String>> buttonsList = new ArrayList<Map<String, String>>();
				
				Map<String, String> createPlan = new HashMap<String, String>();
				createPlan.put("text","new plan");
				createPlan.put("value","new plan");
				
				Map<String, String> mainMenu = new HashMap<String, String>();
				mainMenu.put("text","main menu");
				mainMenu.put("value","main menu");
				
				buttonsList.add(createPlan);
				buttonsList.add(mainMenu);
				
				GenericAttachments generic = new GenericAttachments(
						null,
						"What would you like to do next?", 
						null, 
						null,
						buttonsList);
				list.add(generic);
				
				ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
				MessageClass message = new MessageClass("PlainText", "I couldn't find an active meal plan for you! You first have to create one");
				DialogAction dialogAction = new DialogAction(Consts.CLOSE, "Fulfilled", card, message);
				ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
				return response;
				
			}	
			
		}else if (request.getCurrentIntent().getName().equals(Consts.INTENT_ADD_PROGRESS)){
			
			initializeDB();
			ProfileObject profile = retrieveProfileData(request);
			
			if (profile.getMealPlan() != null){
				
				if (getProgressDataDate(request, new SimpleDateFormat("yyyy/MM/dd").format(new Date()))){
					
					if(request.getCurrentIntent().getConfirmationStatus().toLowerCase().equals("confirmed")){
						
						addNewProgress = true;
						
					}else if(request.getCurrentIntent().getConfirmationStatus().toLowerCase().equals("denied")){
						
						List<GenericAttachments> list = new ArrayList<GenericAttachments>();
                        List<Map<String, String>> confirmationButtonsList = new ArrayList<Map<String, String>>();
						
						Map<String, String> mainButton = new HashMap<String, String>();
						mainButton.put("text","main menu");
						mainButton.put("value","main menu");
						confirmationButtonsList.add(mainButton);
						//String subTitle, String title, String imageUrl, String attachmentLinkUrl, List<Map<String, String>> buttons
						GenericAttachments generic = new GenericAttachments(
								null,
								"Go to main menu",
								null, 
								null,
								confirmationButtonsList);
						list.add(generic);
						ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
						
						MessageClass message = new MessageClass("PlainText", "You can go back to main menu by typing my name Zoe or just choose the option main menu");
						DialogAction dialogAction = new DialogAction(Consts.CLOSE, "Fulfilled", card, message);
						ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
						return response;
						
					}else{
						
						if(!addNewProgress){
							
							List<GenericAttachments> list = new ArrayList<GenericAttachments>();
							
							/**
							 * Create confirmation response card
							 */
							List<Map<String, String>> confirmationButtonsList = new ArrayList<Map<String, String>>();
							
							Map<String, String> yesButton = new HashMap<String, String>();
							yesButton.put("text","yes");
							yesButton.put("value","yes");
							confirmationButtonsList.add(yesButton);
							
							Map<String, String> noButton = new HashMap<String, String>();
							noButton.put("text","no");
							noButton.put("value","no");
							confirmationButtonsList.add(noButton);
							
							GenericAttachments genericConfirmation = new GenericAttachments(
									null,
									"Add new progress?", 
									null, 
									null,
									confirmationButtonsList);
							
							list.add(genericConfirmation);
							ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
							MessageClass message = new MessageClass("PlainText", "You have added a progress today. Are you sure you want to overwrite it?");
							DialogAction dialogAction = new DialogAction("ConfirmIntent", Consts.INTENT_ADD_PROGRESS, null, request.getCurrentIntent().getSlots(), card, message);
							ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
							return response;
							
						}
						
					}
					
				}
				
				if (validateProgressSlot(request)){
					
					addNewProgress = false;
					
					Map<String, String> slots = request.getCurrentIntent().getSlots();
					int progress = Integer.parseInt(slots.get(Consts.PROGRESS_PERCENT_SLOT));
					initializeDB();
					
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					Date date = new Date();
					insertProgressData(request, progress, dateFormat.format(date));
					
					List<GenericAttachments> list = new ArrayList<GenericAttachments>();
                    List<Map<String, String>> confirmationButtonsList = new ArrayList<Map<String, String>>();
					
					Map<String, String> mainButton = new HashMap<String, String>();
					mainButton.put("text","main menu");
					mainButton.put("value","main menu");
					confirmationButtonsList.add(mainButton);
					GenericAttachments generic = new GenericAttachments(
							null,
							"Go to main menu",
							null, 
							null,
							confirmationButtonsList);
					list.add(generic);
					ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
					MessageClass message = new MessageClass("PlainText", "Progress was saved. Typing my name or main menu to go back.");
					DialogAction dialogAction = new DialogAction(Consts.CLOSE, "Fulfilled", card, message);
					ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
					return response;
					
				}else{
					
					Map<String, String> sessionAttributes = new HashMap<String, String>();
					DialogAction dialogAction = new DialogAction(Consts.DELEGATE, request.getCurrentIntent().getSlots());
					ResponseObjectClass delegateResponse = new ResponseObjectClass(sessionAttributes, dialogAction);
					return delegateResponse;
					
				}
				
			}else{
				
				List<GenericAttachments> list = new ArrayList<GenericAttachments>();
				List<Map<String, String>> buttonsList = new ArrayList<Map<String, String>>();
				
				Map<String, String> createPlan = new HashMap<String, String>();
				createPlan.put("text","new plan");
				createPlan.put("value","new plan");
				
				Map<String, String> mainMenu = new HashMap<String, String>();
				mainMenu.put("text","main menu");
				mainMenu.put("value","main menu");
				
				buttonsList.add(createPlan);
				buttonsList.add(mainMenu);
				
				GenericAttachments generic = new GenericAttachments(
						null,
						"What would you like to do next?", 
						null, 
						null,
						buttonsList);
				list.add(generic);
				
				ResponseCard card = new ResponseCard(1, "application/vnd.amazonaws.card.generic", list);
				MessageClass message = new MessageClass("PlainText", "I couldn't find an active meal plan for you! You first have to create one");
				DialogAction dialogAction = new DialogAction(Consts.CLOSE, "Fulfilled", card, message);
				ResponseObjectClass response = new ResponseObjectClass(null, dialogAction);
				return response;
				
			}	
		}
		
		return null;
	}
	
	private boolean validateProgressSlot(RequestClass request){
        Map<String, String> slots = request.getCurrentIntent().getSlots();
		
		if (slots.containsKey(Consts.PROGRESS_PERCENT_SLOT)) {
			int progress = (slots.get(Consts.PROGRESS_PERCENT_SLOT) == null ? -1 : Integer.parseInt(slots.get(Consts.PROGRESS_PERCENT_SLOT)));
			if (progress >= 0 && progress <=100) {
				return true;
			}else{
				
				slots.put(Consts.PROGRESS_PERCENT_SLOT, null);
				request.getCurrentIntent().setSlots(slots);
				
				return false;
			}
		}
		
		return false;
		
	}
	
	private boolean validatePlanDay(RequestClass request){
        Map<String, String> slots = request.getCurrentIntent().getSlots();
		
		if (slots.containsKey(Consts.WEEK_DAY_SLOT)) {
			String weekDaySlot = slots.get(Consts.WEEK_DAY_SLOT);
			if (weekDaySlot != null) {
				if(weekDaySlot.toLowerCase().equals("monday")
						|| weekDaySlot.toLowerCase().equals("tuesday")
						|| weekDaySlot.toLowerCase().equals("wednesday")
						|| weekDaySlot.toLowerCase().equals("thursday")
						|| weekDaySlot.toLowerCase().equals("friday")
						|| weekDaySlot.toLowerCase().equals("saturday")
						|| weekDaySlot.toLowerCase().equals("sunday")){
					
					return true;
				}else{
					
					slots.put(Consts.WEEK_DAY_SLOT, null);
					request.getCurrentIntent().setSlots(slots);
					
					return false;
				}
			}
			
			return false;
		}
		return false;
	}
	
    private boolean validateRecipeData(RequestClass request) {
		
		Map<String, String> slots = request.getCurrentIntent().getSlots();
		
		if (slots.containsKey(Consts.MEAL_TYPE_SLOT)) {
			mealTypeSlot = slots.get(Consts.MEAL_TYPE_SLOT);
			if (mealTypeSlot != null) {
				
				if (mealTypeSlot.toLowerCase().equals("lunch") 
						|| mealTypeSlot.toLowerCase().equals("breakfast")
						|| mealTypeSlot.toLowerCase().equals("dinner")
						|| mealTypeSlot.toLowerCase().equals("healthy_snacks")) {
					
					return true;
				}
				
				slots.put(Consts.MEAL_TYPE_SLOT, null);
				request.getCurrentIntent().setSlots(slots);
			}
		}
		return false;
	}
    
    private boolean validatePlanType(RequestClass request){
    	Map<String, String> slots = request.getCurrentIntent().getSlots();
    	
    	if (slots.containsKey(Consts.PLAN_TYPE_SLOT)) {
		    String planType = slots.get(Consts.PLAN_TYPE_SLOT);
			if (planType == null) {
				slots.put(Consts.PLAN_TYPE_SLOT, null);
				request.getCurrentIntent().setSlots(slots);
				return false;
			}
		}
    	
    	return true;
    }
	
    private boolean validateProfileData(RequestClass request) {
		
		Map<String, String> slots = request.getCurrentIntent().getSlots();
		
		if (slots.containsKey(Consts.FIRST_NAME_SLOT)) {
		    fisrtNameSlot = slots.get(Consts.FIRST_NAME_SLOT);
			if (fisrtNameSlot == null) {
				return false;
			}
		}
		
		if (slots.containsKey(Consts.AGE_SLOT)) {
			ageSlot = (slots.get(Consts.AGE_SLOT) == null ? 0 : Integer.parseInt(slots.get(Consts.AGE_SLOT)));
			if (ageSlot <= 0) {
				return false;
			}
		}
		
		if(slots.containsKey(Consts.GENDER_SLOT)) {
			genderSlot = slots.get(Consts.GENDER_SLOT);
			if (genderSlot == null) {
				return false;
			}
		}
		
		if (slots.containsKey(Consts.WEIGHT_SLOT)) {
			weightSlot = (slots.get(Consts.WEIGHT_SLOT) == null ? 0 : Double.parseDouble(slots.get(Consts.WEIGHT_SLOT)));
			if (weightSlot <= 0) {
				return false;
			}
		}
		
		if (slots.containsKey(Consts.HEIGHT_SLOT)) {
			heightSlot = (slots.get(Consts.HEIGHT_SLOT) == null ? 0 : Integer.parseInt(slots.get(Consts.HEIGHT_SLOT)));
			if (heightSlot <= 0) {
				return false;
			}
		}
		
		return true;
	}
    
    private static void initializeDB() {
    	
    	if(dynamoDB == null){
    		//Dhmhtrhs account
    		//AWSCredentials creds = new BasicAWSCredentials("AKIAI6PTDVJELBGSJFLA","69Kq7/t6POutdfDVDczLhavKlSosyuIbYbkv9qN9");
    		
    		//new account
    		AWSCredentials creds = new BasicAWSCredentials("AKIAIS7MIQHSNXT2A6TA","h5rCV3plSWdHocwn6qWQZD2t/ejwnI4n/jrY8pqa");
          	AmazonDynamoDBClient dyndbclient = new AmazonDynamoDBClient(creds);
            dynamoDB = new DynamoDB(dyndbclient);
    	}
    }
    
    private ArrayList<Item> retrieveRecipeData(RequestClass request) {
  
    	    ArrayList<Item> recipes = new ArrayList<Item>();   
    	    Table table = dynamoDB.getTable("recipes_table");
    	    QuerySpec spec = new QuerySpec().withKeyConditionExpression("meal_type = :v_id")
    	            .withValueMap(new ValueMap().withString(":v_id", mealTypeSlot));
    	
      	ItemCollection<QueryOutcome> items = table.query(spec);
      	Iterator<Item> iterator = items.iterator();
      	Item item = null;
      	
      	while (iterator.hasNext()) {
      	    item = iterator.next();
         	recipes.add(item);
      	    System.out.println("Item query "+item.toJSONPretty());
      	}
      	return recipes;
    }
    
    private ArrayList<Item> getMealPlan(RequestClass request, String type, int bmr){
    	ArrayList<Item> plan = new ArrayList<Item>(); 
    	Table table = dynamoDB.getTable("plans_table");

    	bmr = 1500;
    	QuerySpec spec = new QuerySpec().withKeyConditionExpression("plan_key = :v_id")
    			.withValueMap(new ValueMap().withString(":v_id", "key_plan"));
    	
	    ItemCollection<QueryOutcome> items = table.query(spec);
      	Iterator<Item> iterator = items.iterator();
      	Item item = null;
      	
      	while (iterator.hasNext()) {
      	    item = iterator.next();   
      	    
       	    if(type.equals("loose")){
      		    //plan calories must be less than bmr
       	        if (item.getInt("plan_calories") < bmr ){
       	        	plan.add(item);
       	        } 	
	      	}else if(type.equals("gain")){
	      		//plan calories must be more than bmr    
	      		if (item.getInt("plan_calories") > bmr ){
       	        	plan.add(item);
       	        } 
	      	}else{
	      		//plan calories must be almost same with bmr
	      		if (item.getInt("plan_calories") >= (bmr-100) && item.getInt("plan_calories") <= (bmr+100)){
       	        	plan.add(item);
       	        } 
	      	}
      	}
      	
      	System.out.println("Size is: "+plan.size());
      	return plan;
    }
    
    private int getProgressData(RequestClass request, String planCreated){
    	int totalProgress = 0;
    	Table table = dynamoDB.getTable("progress_table");
    	
    	QuerySpec spec = new QuerySpec().withKeyConditionExpression("user_id = :v_id and progress_date >= :v_date")
	            .withValueMap(new ValueMap().withString(":v_id", request.getUserId()).withString(":v_date", planCreated));
	    ItemCollection<QueryOutcome> items = table.query(spec);
      	Iterator<Item> iterator = items.iterator();
      	Item item = null;
      	
      	while (iterator.hasNext()) {
      	  item = iterator.next();   
      	  totalProgress = totalProgress + item.getInt("progress_percent"); 
      	}
      	return totalProgress;
    }
    
    private boolean getProgressDataDate(RequestClass request, String date){
    	Table table = dynamoDB.getTable("progress_table");
    	
    	QuerySpec spec = new QuerySpec().withKeyConditionExpression("user_id = :v_id and progress_date = :v_date")
	            .withValueMap(new ValueMap().withString(":v_id", request.getUserId()).withString(":v_date", date));
	    ItemCollection<QueryOutcome> items = table.query(spec);
      	Iterator<Item> iterator = items.iterator();
      	Item item = null;
      	
      	while (iterator.hasNext()) {
      	  item = iterator.next();   
      	  return true;
      	}
      	return false;
    }
    
    private ProfileObject retrieveProfileData(RequestClass request) {
    	   Table table = dynamoDB.getTable("users_table");
    	   try {
            Item item = table.getItem("userId", request.getUserId(), "userId,user_name,user_gender,user_age,user_weight,user_height,user_BMI,user_BMR,user_meal_plan,user_plan_created", null);
            
            if (item!=null){
            
            	String userId = item.getString("userId");
                String name = item.getString("user_name");
                String gender = item.getString("user_gender");
                int age = item.getInt("user_age");
                double weight = item.getDouble("user_weight");
                int height = item.getInt("user_height");
                double bmi = item.getDouble("user_BMI");
                int bmr = item.getInt("user_BMR");
                
                String mealPlan = null;
                if (item.hasAttribute("user_meal_plan")){
                	mealPlan = item.getString("user_meal_plan");
                }
                
                String planCreated = null;
                if (item.hasAttribute("user_plan_created")){
                	planCreated = item.getString("user_plan_created");
                }
                
                System.out.println("Userid: "+userId+" - name: "+name+" - gender: "+gender+" - ");
                
                profile = new ProfileObject(userId, name, gender, age, weight, height, bmi, bmr, mealPlan, planCreated);
                return profile;
            }            
        }
        catch (Exception e) {
            System.err.println("GetItem failed.");
            System.err.println(e.getMessage());
        }
    	   
    	return null;
    }
    
    private Item getUserPlan(RequestClass request) {
 	   Table table = dynamoDB.getTable("users_table");
 	   try {
         Item item = table.getItem("userId", request.getUserId(), "user_meal_plan", null);
         
         if (item!=null){
             return item;
         }            
       }catch (Exception e) {
	         System.err.println("GetItem failed.");
	         System.err.println(e.getMessage());
	     }
	 	   
	 	return null;
	 }
    
    private boolean insertProgressData(RequestClass request, int progress, String date) {
        Table table = dynamoDB.getTable("progress_table");
        try {
        	    Item item = new Item().withPrimaryKey("user_id", request.getUserId())
        	    		.withString("progress_date", date)
        	    		.withInt("progress_percent", progress);
        	    table.putItem(item);
        } catch (Exception e) {
        	     e.printStackTrace();
        	     return false;
        }
    	
        return true;
    }
    
    private boolean insertUserData(RequestClass request) {
        Table table = dynamoDB.getTable("users_table");
        try {
        	    Item item = new Item().withPrimaryKey("userId", request.getUserId())
        	    		.withString("user_name", fisrtNameSlot)
        	    		.withInt("user_age", ageSlot)
        	    		.withString("user_gender", genderSlot)
        	    		.withDouble("user_weight", weightSlot)
        	    		.withInt("user_height", heightSlot)
        	    		.withDouble("user_BMI", HelperFunctions.calculateBMI(weightSlot, heightSlot))
        	    		.withInt("user_BMR", HelperFunctions.calculateBMR(genderSlot, weightSlot, heightSlot, ageSlot));
        	    table.putItem(item);
        } catch (Exception e) {
        	     e.printStackTrace();
        	     return false;
        }
    	    return true;
    }
    
    private boolean saveMealPlan(RequestClass request,Item plan){
    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
    	
    	Table table = dynamoDB.getTable("users_table");
    	try {
    	    UpdateItemSpec updateItemSpec1 = new UpdateItemSpec().withPrimaryKey("userId", request.getUserId())
                    .withUpdateExpression("set #a=:val1")
                    .withNameMap(new NameMap().with("#a", "user_meal_plan"))
                    .withValueMap(
                        new ValueMap().withString(":val1", plan.getString("plan_json")))
                    .withReturnValues(ReturnValue.ALL_NEW);

    	    UpdateItemSpec updateItemSpec2 = new UpdateItemSpec().withPrimaryKey("userId", request.getUserId())
                    .withUpdateExpression("set #b=:val2")
                    .withNameMap(new NameMap().with("#b", "user_plan_id"))
                    .withValueMap(
                        new ValueMap().withInt(":val2", plan.getInt("plan_id")))
                    .withReturnValues(ReturnValue.ALL_NEW);
    	    
    	    UpdateItemSpec updateItemSpec3 = new UpdateItemSpec().withPrimaryKey("userId", request.getUserId())
                    .withUpdateExpression("set #c=:val3")
                    .withNameMap(new NameMap().with("#c", "user_plan_created"))
                    .withValueMap(
                        new ValueMap().withString(":val3", dateFormat.format(date)))
                    .withReturnValues(ReturnValue.ALL_NEW);
    	    
                table.updateItem(updateItemSpec1);
                table.updateItem(updateItemSpec2);
                table.updateItem(updateItemSpec3);
	    } catch (Exception e) {
	    	     e.printStackTrace();
	    	     return false;
	    }
    	
    	return true;
    }
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
}

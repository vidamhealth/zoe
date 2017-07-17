package chatbot.lambda_java_chatbot.Objects;

public class ProfileObject {
	
	/**
	 * String userId = item.getString("userId");
            String name = item.getString("Name");
            int age = item.getInt("Age");
            String gender = item.getString("Gender");
            int height = item.getInt("height");
            double bmi = item.getDouble("BMI");
            int bmr = item.getInt("BMR");
	 */
	
	String userId;
	String name;
	String gender;
	int age;
	double weight;
	int height;
	int bmr;
	double bmi;
	String mealPlan;
	String planCreated;
	
	public ProfileObject(){}
	
	public ProfileObject(String userId, String name, String gender, 
			int age, double weight, int height, double bmi, int bmr,
			String mealPlan, String planCreated){
		this.userId = userId;
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.bmi = bmi;
		this.bmr = bmr;
		this.mealPlan = mealPlan;
		this.planCreated = planCreated;
	}
	
	public String getUserId(){
		return this.userId;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getGender(){
		return this.gender;
	}

	public int getAge(){
		return this.age;
	}
	
	public double getWeight(){
		return this.weight;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public double getBmi(){
		return this.bmi;
	}
	
	public int getBmr(){
		return this.bmr;
	}
	
	public String getMealPlan(){
		return this.mealPlan;
	}
	
	public String getPlanCreated(){
		return this.planCreated;
	}
}

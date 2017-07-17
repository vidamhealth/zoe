package chatbot.lambda_java_chatbot.HelperClasses;

import java.text.DecimalFormat;

public class HelperFunctions {
	
	public static double calculateBMI(double weight, double height) {
		height = height/100;
		double bmi_value = (weight / (height*height));		
		return Double.parseDouble(String.valueOf(new DecimalFormat("##.#").format(bmi_value)));
	}
	
	public static int calculateBMR(String gender, double weight, int height, int age) {
		Double bmr_value = 0.0;
		if (gender.toLowerCase().equals("male")){
            bmr_value = 88.362 + (13.397 * weight) + (4.799 * (height)) - (5.677 * age);
        }else if (gender.toLowerCase().equals("female")){
            bmr_value = 447.593 + (9.247 * weight) + (3.098 * (height)) - (4.330 * age);
        }
		return bmr_value.intValue();
	}
	
	public static int checkBmi(Double bmi, Double weight_value, double height_value) {
		height_value = height_value/100;
        Double XX= (18.5*(height_value*height_value) - weight_value)*100/ (18.5*(height_value*height_value));
        Double YY= (weight_value - 25*(height_value*height_value))*100/(25*(height_value*height_value));
        if ( bmi > 25 || bmi == 25){
            return YY.intValue();
        }else if ( bmi < 18.5 ){
            return XX.intValue();
        }
        return 0;
    }
}

package com.programs.lifetrack.Data;

public class Dataset {
    /*REGISTER PAGE CONSTANTS*/
    public static final int NAME_ARRAY_INDEX = 0;
    public static final int PASS_ARRAY_INDEX = 1;
    public static final int REPEAT_PASS_ARRAY_INDEX = 2;
    public static final int HEIGHT_ARRAY_INDEX = 3;
    public static final int MASS_ARRAY_INDEX = 4;
    public static final int AGE_ARRAY_INDEX = 5;
    public static final int MINIMUM_AGE_NUMBER = 0;
    public static final int MAXIMUM_AGE_NUMBER = 130;


    /*MAIN PAGE CONSTANTS*/
    public static final int MAX_GLASS_NUMBER = 30;
    public static final int MIN_GLASS_NUMBER = 0;
    public static final int FIRST_MASS = 20;
    public static final int VOLUME_WATER_FOR_FIRST_MASS = 1500;
    public static final int VOLUME_WATER_FOR_EACH_KILOGRAM = 20;
    public static final int GLASS_VOLUME = 350;
    public static final short MASS_MULTIPLY_COEFFICIENT = 10;
    public static final double HEIGHT_MULTIPLY_COEFFICIENT = 6.25;
    public static final short AGE_MULTIPLY_COEFFICIENT = 10;
    public static final short MALE_CALORIES_ADD_AGE_MULTIPLY_COEFFICIENT = 5;
    public static final short FEMALE_CALORIES_ADD_AGE_MULTIPLY_COEFFICIENT = -161;
    public static final String WATER_SCARCITY_QUOTE = "You didn't drink enough water!";
    public static final String WATER_RATE_QUOTE = "You drank the norm of water";
    public static final String WATER_SURPLUS_QUOTE = "You drank more than the norm for today";
    public static final String CALORIES_SCARCITY_QUOTE = "You ate too little today";
    public static final String CALORIES_RATE_QUOTE = "The norm of calories per day is accepted";
    public static final String CALORIES_SURPLUS_QUOTE = "You ate too much today";
    public static final double MAX_IMT_VALUE = 25;
    public static final double MIN_IMT_VALUE = 18.5;
    public static final String GLASS_NUM_NOTIFY_MESSAGE = "A little more! You have one more glass left!";
    public static final String MASS_LESS_NORMAL_QUOTE = "Your imt: %A. Below normal weight";
    public static final String MASS_NORMAL_QUOTE = "Your imt: %A. Normal weight";
    public static final String EXCESS_MASS_QUOTE = "Your imt: %A. Overweight";
    public static final String OBESITY_1ST_DEGREE_QUOTE = "Your imt: %A. Obesity I degree";
    public static final String OBESITY_2ST_DEGREE_QUOTE = "Your imt: %A. Obesity II degree";
    public static final String OBESITY_3ST_DEGREE_QUOTE = "Your imt: %A.Obesity III degree";
}

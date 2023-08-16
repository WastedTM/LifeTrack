package com.programs.lifetrack.Data;

public class MainSetting {
    public static final String APP_VERSION = "2.0";
    public static final String LOG_TAG = "tag";
    public static final int REQUEST_CODE = 8622;
    public static final int PASSWORD_LENGTH = 8;
    public static final String USER_DATA_SP_TAG = "UserData";
    public static final String USER_INFO_SP_TAG = "UserInfo";
    public static final String USER_REGISTER_STATUS_SP_TAG = "register_status";
    public static final String USER_NAME_SP_TAG = "user_name";
    public static final String USER_PASS_SP_TAG = "user_pass";
    public static final String USER_AGE_SP_TAG = "user_age";
    public static final String USER_GENDER_SP_TAG = "user_gender";
    public static final String USER_HEIGHT_SP_TAG = "user_height";
    public static final String USER_MASS_SP_TAG = "user_mass";
    public static final String USER_GLASS_SP_TAG = "user_glass";
    public static final String USER_CALORIES_SP_TAG = "user_calories";
    public static final String USER_DAY_SP_TAG = "user_day";
    public static final String USER_STEP_SP_TAG = "user_step";

    private static String userName = "";

    private static String userPassword = "";

    private static int userAge = 0;

    private static double userHeight = 0;
    
    private static double userMass = 0;
    private static boolean userGender = true;

    public static String getUserPassword(){return userPassword;}
    public static void setUserPassword(String userPassword){MainSetting.userPassword = userPassword;}

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        MainSetting.userName = userName;
    }

    public static double getUserHeight() {
        return userHeight;
    }

    public static void setUserHeight(double userHeight) {
        MainSetting.userHeight = userHeight;
    }

    public static double getUserMass() {
        return userMass;
    }

    public static void setUserMass(double userMass) {
        MainSetting.userMass = userMass;
    }

    public static int getUserAge() {
        return userAge;
    }

    public static void setUserAge(int userAge) {
        MainSetting.userAge = userAge;
    }

    public static boolean getUserGender() {
        return userGender;
    }

    public static void setUserGender(boolean userGender) {
        MainSetting.userGender = userGender;
    }
}

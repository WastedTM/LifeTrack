package com.programs.lifetrack.Pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.programs.lifetrack.Data.MainSetting;
import com.programs.lifetrack.R;

import static com.programs.lifetrack.Data.Dataset.*;

public class RegisterPage extends AppCompatActivity {

    private EditText nameField;
    private EditText passField;
    private EditText repeatPassField;
    private EditText heightField;
    private EditText massField;
    private EditText ageField;
    private CheckBox maleCheckBox;
    private CheckBox femaleCheckBox;
    private Button confirmButton;

    private String userName;
    private String userPassword;
    private String userRepeatPassword;
    private double userHeight;
    private double userMass;
    private int userAge;
    private boolean userGender;

    private boolean[] nextPagePermissions = new boolean[6];

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MainSetting.LOG_TAG, RegisterPage.this.getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        setStartSetting();
        attachButtons();
    }

    private void setStartSetting(){
        setPortraitMode();
        setFullScreenMode();
        setAllElements();
        setAllTexts();
    }

    private void setPortraitMode() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void setFullScreenMode() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setAllElements(){
        try{
            nameField = findViewById(R.id.name_field);
            passField = findViewById(R.id.pass_field);
            repeatPassField = findViewById(R.id.repeat_pass_field);
            heightField = findViewById(R.id.height_field);
            massField = findViewById(R.id.mass_field);
            ageField = findViewById(R.id.age_field);
            maleCheckBox = findViewById(R.id.male_checkbox);
            femaleCheckBox = findViewById(R.id.female_checkbox);

            confirmButton = findViewById(R.id.confirm_button);

            sharedPreferences = getSharedPreferences(MainSetting.USER_DATA_SP_TAG, Context.MODE_PRIVATE);

            maleCheckBox.setChecked(true);
            userGender = true;

            for(int i = 0; i < 5; i++){
                nextPagePermissions[i] = false;
            }

            Log.i(MainSetting.LOG_TAG, "All objects are initialized");
        } catch (Exception e){
            Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            Log.e(MainSetting.LOG_TAG, e.getMessage());
        }
    }

    private void setAllTexts(){
        MainSetting.setUserName("");
        MainSetting.setUserPassword("");
    }

    private void attachButtons(){
        attachConfirmButton();
        attachMaleCheckBox();
        attachFemaleCheckBox();
    }

    private void attachConfirmButton(){
        confirmButton.setOnClickListener(v -> {
            Log.i(MainSetting.LOG_TAG, "Confirm button was pressed");
            userName = nameField.getText().toString();
            checkUserNameField();

            userPassword = passField.getText().toString();
            checkUserPasswordField();

            userRepeatPassword = repeatPassField.getText().toString();
            checkUserRepeatPasswordField();

            if(heightField.getText().toString().equals("")){
               userHeight = 0;
            } else {
                userHeight = Double.parseDouble(heightField.getText().toString());
            }
            checkUserHeight();

            if(massField.getText().toString().equals("")){
                userMass = 0;
            }else{
                userMass = Double.parseDouble(massField.getText().toString());
            }
            checkUserMass();

            if(ageField.getText().toString().equals("")){
                userAge = 0;
            }else{
                userAge = Integer.parseInt(ageField.getText().toString());
            }
            checkUserAge();

            if(checkNextPagePermissions()){
                registerNewUser();
                vibrationClick();
                Intent mainPage = new Intent(RegisterPage.this, MainPage.class);
                startActivity(mainPage);
            } else {
                errorVibrationClick();
            }

        });
    }

    private void checkUserNameField(){
        if(userName == null || userName.equals("") || userName.length() < 2){
            nameField.setHintTextColor(getColor(R.color.lt_light_red));
            nameField.setText("");
            nextPagePermissions[NAME_ARRAY_INDEX] = false;
        } else {
            try{
                MainSetting.setUserName(userName);
                nextPagePermissions[NAME_ARRAY_INDEX] = true;
            } catch (Exception e){
                Log.e(MainSetting.LOG_TAG, e.getMessage());
            }
        }
    }

    private void checkUserPasswordField(){
        if(userPassword == null || userPassword.equals("") || userPassword.length() < MainSetting.PASSWORD_LENGTH){
            passField.setHintTextColor(getColor(R.color.lt_light_red));
            passField.setText("");
            nextPagePermissions[PASS_ARRAY_INDEX] = false;
        } else {
            try{
                MainSetting.setUserPassword(userPassword);
                nextPagePermissions[PASS_ARRAY_INDEX] = true;
            }catch (Exception e){
                Log.e(MainSetting.LOG_TAG, e.getMessage());
            }
        }
    }

    private void checkUserRepeatPasswordField(){
        if(userRepeatPassword == null || userRepeatPassword.equals("")){
            repeatPassField.setHintTextColor(getColor(R.color.lt_light_red));
            repeatPassField.setText("");
            nextPagePermissions[REPEAT_PASS_ARRAY_INDEX] = false;
        } else {
            if(!userRepeatPassword.equals(userPassword)){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
                passField.setTextColor(getColor(R.color.lt_light_red));
                repeatPassField.setTextColor(getColor(R.color.lt_light_red));
                nextPagePermissions[REPEAT_PASS_ARRAY_INDEX] = false;
            } else {
                nextPagePermissions[REPEAT_PASS_ARRAY_INDEX] = true;
            }
        }
    }

    private void checkUserHeight(){
        if(userHeight <= 0 ){
            heightField.setHintTextColor(getColor(R.color.lt_light_red));
            heightField.setText("");
            nextPagePermissions[HEIGHT_ARRAY_INDEX] = false;
        }else{
            try{
                MainSetting.setUserHeight(userHeight);
                nextPagePermissions[HEIGHT_ARRAY_INDEX] = true;
            }catch (Exception e){
                Log.e(MainSetting.LOG_TAG, e.getMessage());
            }
        }
    }

    private void checkUserMass(){
        if(userMass <= 0){
            massField.setHintTextColor(getColor(R.color.lt_light_red));
            massField.setText("");
            nextPagePermissions[MASS_ARRAY_INDEX] = false;
        }else{
            try{
                MainSetting.setUserMass(userMass);
                nextPagePermissions[MASS_ARRAY_INDEX] = true;
            }catch (Exception e){
                Log.e(MainSetting.LOG_TAG, e.getMessage());
            }
        }
    }

    private void checkUserAge(){
        if(userAge <= MINIMUM_AGE_NUMBER || userAge >= MAXIMUM_AGE_NUMBER){
            ageField.setHintTextColor(getColor(R.color.lt_light_red));
            ageField.setText("");
            nextPagePermissions[AGE_ARRAY_INDEX] = false;
        }else{
            try{
                MainSetting.setUserAge(userAge);
                nextPagePermissions[AGE_ARRAY_INDEX] = true;
            }catch (Exception e){
                Log.e(MainSetting.LOG_TAG, e.getMessage());
            }
        }
    }

    private void attachMaleCheckBox(){
        maleCheckBox.setOnClickListener(v -> {
            Log.i(MainSetting.LOG_TAG, "Male checkbox was pressed");
            if (!maleCheckBox.isChecked()){
                maleCheckBox.setChecked(true);
            }
            femaleCheckBox.setChecked(false);
            if(maleCheckBox.isChecked()){
                userGender = true;
            }
            MainSetting.setUserGender(userGender);
        });
    }

    private void attachFemaleCheckBox(){
        femaleCheckBox.setOnClickListener(v -> {
            Log.i(MainSetting.LOG_TAG, "Female checkbox was pressed");
            if (!femaleCheckBox.isChecked()){
                femaleCheckBox.setChecked(true);
            }
            maleCheckBox.setChecked(false);
            if(femaleCheckBox.isChecked()){
                userGender = false;
            }
            MainSetting.setUserGender(userGender);
        });
    }

    private boolean checkNextPagePermissions(){
        for(boolean i : nextPagePermissions){
          if(!i){
            return false;
          }
        }
        return true;
    }

    private void registerNewUser(){
        try{
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(MainSetting.USER_REGISTER_STATUS_SP_TAG, true);
            editor.putString(MainSetting.USER_NAME_SP_TAG, userName);
            editor.putString(MainSetting.USER_PASS_SP_TAG, userPassword);
            editor.putFloat(MainSetting.USER_HEIGHT_SP_TAG, (float) userHeight);
            editor.putFloat(MainSetting.USER_MASS_SP_TAG, (float) userMass);
            editor.putInt(MainSetting.USER_AGE_SP_TAG, userAge);
            editor.putBoolean(MainSetting.USER_GENDER_SP_TAG, userGender);
            editor.apply();

            Log.i(MainSetting.LOG_TAG, "The user is successfully registered");
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(MainSetting.LOG_TAG, e.getMessage());
        }

    }

    private void vibrationClick(){
        new Thread(() -> {
            long mills = 50L;
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            if (vibrator.hasVibrator()) {
                vibrator.vibrate(mills);
            }
        }).start();
    }

    private void errorVibrationClick(){
        new Thread(() -> {
            long mills = 110L;
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            if (vibrator.hasVibrator()) {
                vibrator.vibrate(mills);
            }
        }).start();
    }
}
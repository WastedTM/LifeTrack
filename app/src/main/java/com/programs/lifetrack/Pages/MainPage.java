package com.programs.lifetrack.Pages;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.programs.lifetrack.Data.MainSetting;
import com.programs.lifetrack.R;

import java.util.Date;

import static com.programs.lifetrack.Data.Dataset.*;
import static com.programs.lifetrack.Data.MainSetting.REQUEST_CODE;


public class MainPage extends AppCompatActivity {

    private ImageView addGlassButton;
    private ImageView removeGlassButton;
    private TextView glassNumberText;
    private TextView waterStatus;
    private ImageView addCaloriesButton;
    private ImageView removeCaloriesButton;
    private TextView caloriesNumberText;
    private TextView caloriesStatus;
    private EditText caloriesField;
    private ProgressBar imtProgressBar;
    private TextView imtStatusText;
    private TextView stepNumText;
    private TextView stepNumPerDay;
    private ProgressBar stepProgressBar;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private SensorManager sensorManager;
    private Sensor stepSensor;

    private double caloriesCounter;
    private int glassCounter;
    private int stepCounter;
    private boolean isWriteStep;
    private int stepNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MainSetting.LOG_TAG, MainPage.this.getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        startSetting();
        attachButtons();
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.putInt(MainSetting.USER_STEP_SP_TAG, stepNumber + sp.getInt(MainSetting.USER_STEP_SP_TAG, 0));
        editor.apply();
        sensorManager.unregisterListener(stepListener, stepSensor);
    }

    SensorEventListener stepListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(isWriteStep){
                stepCounter = (int) sensorEvent.values[0];
                isWriteStep = false;
            }
            stepNumber = Math.max((int) sensorEvent.values[0] - stepCounter, 0);
            stepNumText.setText("Step count: " + stepNumber);
            stepNumPerDay.setText("During the day: " + (sp.getInt(MainSetting.USER_STEP_SP_TAG, 0) + stepNumber));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCounting();
                stepNumText.setTextColor(getColor(R.color.white));
            } else {
                stepNumText.setTextColor(getColor(R.color.lt_light_red));
                stepNumText.setText("Permission not granted.");
                Toast.makeText(this, "Permission to access the step sensor is denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startSetting() {
        setPortraitMode();
        setFullScreenMode();
        setAllElements();
        uploadUserData();
        checkDate();
        updateGlassCounterText();
        updateWaterStatusText();
        updateCaloriesNumberText();
        updateCaloriesStatusText();
        setImtBarProgress();
        showHealthActivityPermission();
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

    private void setAllElements() {
        try {
            addGlassButton = findViewById(R.id.add_glass_button);
            removeGlassButton = findViewById(R.id.remove_glass_button);
            glassNumberText = findViewById(R.id.glass_number_text);
            waterStatus = findViewById(R.id.water_status_text);

            addCaloriesButton = findViewById(R.id.add_calories_button);
            removeCaloriesButton = findViewById(R.id.remove_calories_button);
            caloriesNumberText = findViewById(R.id.calories_number_text);
            caloriesStatus = findViewById(R.id.calories_status_text);
            caloriesField = findViewById(R.id.calories_number_field);
            imtProgressBar = findViewById(R.id.imt_progress_bar);
            imtStatusText = findViewById(R.id.imt_status_text);

            sp = getSharedPreferences(MainSetting.USER_INFO_SP_TAG, Context.MODE_PRIVATE);
            editor = sp.edit();
            glassCounter = sp.getInt(MainSetting.USER_GLASS_SP_TAG, 0);
            caloriesCounter = sp.getFloat(MainSetting.USER_CALORIES_SP_TAG, 0);


            stepProgressBar = findViewById(R.id.step_progress_bar);
            stepProgressBar.setMax(12000);
            stepNumText = findViewById(R.id.step_num_text);

            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isWriteStep = true;

            stepNumPerDay = findViewById(R.id.step_count_per_day);
            stepNumPerDay.setText("During the day: " + sp.getInt(MainSetting.USER_STEP_SP_TAG, 0));
        } catch (Exception e){
            Log.e(MainSetting.LOG_TAG, e.getMessage());
            Toast.makeText(this, ("Object initialization error:\n" + e.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    private void attachButtons() {
        attachAddGlassButton();
        attachRemoveGlassButton();
        attachAddCaloriesButton();
        attachRemoveCaloriesButton();
    }

    private void attachAddGlassButton(){
        try{
            addGlassButton.setOnClickListener(v -> {
                if(glassCounter < MAX_GLASS_NUMBER){
                    vibrationClick();
                    glassCounter++;
                    editor.putInt(MainSetting.USER_GLASS_SP_TAG, glassCounter);
                    editor.apply();
                    showGlassNumNotification(glassCounter);
                    updateGlassCounterText();
                    updateWaterStatusText();
                } else {
                    errorVibrationClick();
                }
            });
        } catch (Exception e){
            Log.e(MainSetting.LOG_TAG, e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void attachRemoveGlassButton(){
        try{
            removeGlassButton.setOnClickListener(v -> {
                if(glassCounter > MIN_GLASS_NUMBER){
                    vibrationClick();
                    glassCounter--;
                    editor.putInt(MainSetting.USER_GLASS_SP_TAG, glassCounter);
                    editor.apply();
                    updateGlassCounterText();
                    updateWaterStatusText();
                } else {
                    errorVibrationClick();
                }
            });
        } catch (Exception e){
            Log.e(MainSetting.LOG_TAG, e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void attachAddCaloriesButton(){
        try {
            addCaloriesButton.setOnClickListener(v -> {
                if(!caloriesField.getText().toString().equals("")){
                    vibrationClick();
                    caloriesCounter += Double.parseDouble(caloriesField.getText().toString());
                    editor.putFloat(MainSetting.USER_CALORIES_SP_TAG, (float) caloriesCounter);
                    editor.apply();
                    updateCaloriesNumberText();
                    caloriesField.setText("");
                    updateCaloriesStatusText();
                } else {
                    errorVibrationClick();
                }
            });
        } catch (Exception e){
            Log.e(MainSetting.LOG_TAG, e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void attachRemoveCaloriesButton(){
        try {
            removeCaloriesButton.setOnClickListener(v -> {
                if (!caloriesField.getText().toString().equals("")) {
                    vibrationClick();
                    double removeCaloriesNum = Double.parseDouble(caloriesField.getText().toString());
                    if (removeCaloriesNum > caloriesCounter) {
                        caloriesCounter = 0;
                    } else {
                        caloriesCounter -= removeCaloriesNum;
                    }
                    editor.putFloat(MainSetting.USER_CALORIES_SP_TAG, (float) caloriesCounter);
                    editor.apply();
                    updateCaloriesNumberText();
                    updateCaloriesStatusText();
                    caloriesField.setText("");
                } else {
                    errorVibrationClick();
                }
            });
        } catch (Exception e){
            Log.e(MainSetting.LOG_TAG, e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateGlassCounterText(){
        glassNumberText.setText(glassCounter + "");
    }

    private int getNormalGlassNumber(){
        double userMass = MainSetting.getUserMass();
        double normalWaterVolume;
        int normalGlassNum;
        if(userMass > FIRST_MASS){
            normalWaterVolume = VOLUME_WATER_FOR_FIRST_MASS + (userMass - FIRST_MASS)*VOLUME_WATER_FOR_EACH_KILOGRAM;
        } else {
            normalWaterVolume = VOLUME_WATER_FOR_FIRST_MASS;
        }
        normalGlassNum = (int) (normalWaterVolume/GLASS_VOLUME);
        return normalGlassNum;
    }

    private void updateWaterStatusText(){
        int normalGlassNum = getNormalGlassNumber();
        if(normalGlassNum > glassCounter){
           waterStatus.setText(WATER_SCARCITY_QUOTE);
           waterStatus.setTextColor(getColor(R.color.lt_light_red));
        } else if(normalGlassNum == glassCounter){
            waterStatus.setText(WATER_RATE_QUOTE);
            waterStatus.setTextColor(getColor(R.color.lt_green));
        } else {
            waterStatus.setText(WATER_SURPLUS_QUOTE);
            waterStatus.setTextColor(getColor(R.color.lt_yellow));
        }
    }

    private void updateCaloriesNumberText(){
        caloriesNumberText.setText(caloriesCounter + "");
    }

    private double getNormalCaloriesNumber(){
        double userMass = MainSetting.getUserMass();
        double userHeight = MainSetting.getUserHeight();
        int userAge = MainSetting.getUserAge();
        boolean userGender = true;//MainSetting.getUserGender();
        double caloriesNum;
        if(userGender){
            caloriesNum = (MASS_MULTIPLY_COEFFICIENT * userMass) + (HEIGHT_MULTIPLY_COEFFICIENT * userHeight) - (AGE_MULTIPLY_COEFFICIENT * userAge) + MALE_CALORIES_ADD_AGE_MULTIPLY_COEFFICIENT;
        } else {
            caloriesNum = (MASS_MULTIPLY_COEFFICIENT * userMass) + (HEIGHT_MULTIPLY_COEFFICIENT * userHeight) - (AGE_MULTIPLY_COEFFICIENT * userAge) - FEMALE_CALORIES_ADD_AGE_MULTIPLY_COEFFICIENT;
        }
        return caloriesNum;
    }

    private void updateCaloriesStatusText(){
        double caloriesNum = getNormalCaloriesNumber();
        if(caloriesCounter <= caloriesNum - 100){
            caloriesStatus.setText(CALORIES_SCARCITY_QUOTE);
            caloriesStatus.setTextColor(getColor(R.color.lt_light_red));
        } else if(caloriesCounter > caloriesNum - 100 && caloriesCounter <= caloriesNum + 100){
            caloriesStatus.setText(CALORIES_RATE_QUOTE);
            caloriesStatus.setTextColor(getColor(R.color.lt_green));
        } else {
            caloriesStatus.setText(CALORIES_SURPLUS_QUOTE);
            caloriesStatus.setTextColor(getColor(R.color.lt_yellow));
        }
    }

    private void setImtBarProgress(){
        double userMass = MainSetting.getUserMass();
        double userHeight = MainSetting.getUserHeight()/100;
        double imt = userMass/Math.pow(userHeight,2);
        imtProgressBar.setMax(50);
        imtStatusText.setText(userMass + "/"+userHeight+"/"+imt);
        imtProgressBar.setProgress((int)imt);

        updateImtStatusText(imt);

    }

    private void updateImtStatusText(double imt){
        String imtStatus;
        if(imt < 18.5){
            imtStatus = MASS_LESS_NORMAL_QUOTE;
            imtStatusText.setTextColor(getColor(R.color.lt_light_red));
        } else if(imt >= 18.5 && imt < 25){
            imtStatus = MASS_NORMAL_QUOTE;
            imtStatusText.setTextColor(getColor(R.color.lt_green));
        } else if(imt >= 25 && imt < 30){
            imtStatus = EXCESS_MASS_QUOTE;
            imtStatusText.setTextColor(getColor(R.color.lt_yellow));
        } else if(imt >= 30 && imt < 40){
            imtStatus = OBESITY_1ST_DEGREE_QUOTE;
            imtStatusText.setTextColor(getColor(R.color.lt_yellow));
        } else if(imt >= 40 && imt < 45){
            imtStatus = OBESITY_2ST_DEGREE_QUOTE;
            imtStatusText.setTextColor(getColor(R.color.lt_light_red));
        } else {
            imtStatus = OBESITY_3ST_DEGREE_QUOTE;
            imtStatusText.setTextColor(getColor(R.color.lt_light_red));
        }
        imtStatus = imtStatus.replace("%A", (int)imt+"");
        imtStatusText.setText(imtStatus);
    }

    private void showGlassNumNotification(int glassNum){
        if(getNormalGlassNumber() - glassNum == 1){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel("MyNotify", "MyNotify", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainPage.this, "MyNotify");
            builder.setContentTitle(getString(R.string.app_name));
            //String message = glassCounter + " склянок\n" + caloriesCounter + " ккал";
            builder.setContentText(GLASS_NUM_NOTIFY_MESSAGE);
            builder.setSmallIcon(R.drawable.lightbulb_img);
            try {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.main_logo);
                builder.setLargeIcon(bitmap);

                builder.setAutoCancel(true);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainPage.this);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "No notification permission", Toast.LENGTH_SHORT).show();
                    Log.e(MainSetting.LOG_TAG, "No notification permission");
                    return;
                }
                notificationManagerCompat.notify(1, builder.build());
            } catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadUserData(){
        SharedPreferences sharedPreferences = getSharedPreferences(MainSetting.USER_DATA_SP_TAG, Context.MODE_PRIVATE);
        MainSetting.setUserName(sharedPreferences.getString(MainSetting.USER_NAME_SP_TAG, ""));
        MainSetting.setUserPassword(sharedPreferences.getString(MainSetting.USER_PASS_SP_TAG, ""));
        MainSetting.setUserAge(sharedPreferences.getInt(MainSetting.USER_AGE_SP_TAG, 0));
        MainSetting.setUserHeight(sharedPreferences.getFloat(MainSetting.USER_HEIGHT_SP_TAG, 0f));
        MainSetting.setUserMass(sharedPreferences.getFloat(MainSetting.USER_MASS_SP_TAG, 0f));
        MainSetting.setUserGender(sharedPreferences.getBoolean(MainSetting.USER_GENDER_SP_TAG, false));
    }

    private void checkDate(){
        Date date = new Date();
        if(date.getDate() != sp.getInt(MainSetting.USER_DAY_SP_TAG, -1)){
            editor.putInt(MainSetting.USER_DAY_SP_TAG, date.getDate());
            editor.putInt(MainSetting.USER_GLASS_SP_TAG, 0);
            editor.putFloat(MainSetting.USER_CALORIES_SP_TAG, 0f);
            editor.putInt(MainSetting.USER_STEP_SP_TAG, 0);
            editor.apply();
            glassCounter = 0;
            caloriesCounter = 0;
            stepCounter = 0;
        }
    }

    private void startCounting(){
        if (stepSensor != null) {
            sensorManager.registerListener(stepListener, stepSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Step counter sensor not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void showHealthActivityPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, REQUEST_CODE);
        } else {
            startCounting();
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
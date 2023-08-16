package com.programs.lifetrack.Pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.programs.lifetrack.Data.MainSetting;
import com.programs.lifetrack.R;

public class StartPage extends AppCompatActivity {

    private TextView authorsText;
    private TextView versionText;
    private ImageView mainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkUserRegisterStatus();
        Log.i(MainSetting.LOG_TAG, StartPage.this.getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        setStartSetting();
        attachButtons();
    }

    private void setStartSetting(){
        setPortraitMode();
        setFullScreenMode();
        setAllElement();
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

    private void setAllElement() {
        try{
            authorsText = findViewById(R.id.authors_text);
            versionText = findViewById(R.id.version_text);
            mainButton = findViewById(R.id.main_button);

            Log.i(MainSetting.LOG_TAG, "All elements have been initialized");
        } catch (Exception e){
            Log.e(MainSetting.LOG_TAG, e.getMessage());
        }
    }

    private void setAllTexts(){
        versionText.setText("Version: " + MainSetting.APP_VERSION);
        authorsText.setText(getResources().getText(R.string.authors_text) + " " +
                getResources().getText(R.string.authors_name));
    }

    private void attachButtons(){
        attachMainButton();
    }

    private void attachMainButton (){
        mainButton.setOnClickListener(v -> {
            Log.i(MainSetting.LOG_TAG, "Main button pressed");
            vibrationClick();
            Intent registerPage = new Intent(StartPage.this, RegisterPage.class);
            startActivity(registerPage);
        });
    }

    private void checkUserRegisterStatus(){
        if(getUserRegisterStatus()){
            Log.i(MainSetting.LOG_TAG, "The user is registered. Redirection to another page");
            Intent mainPage = new Intent(StartPage.this, MainPage.class);
            startActivity(mainPage);
        }
    }

    private boolean getUserRegisterStatus(){
        SharedPreferences preferences = getSharedPreferences(MainSetting.USER_DATA_SP_TAG, Context.MODE_PRIVATE);
        return preferences.getBoolean(MainSetting.USER_REGISTER_STATUS_SP_TAG, false);
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
}
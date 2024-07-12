package com.example.extreme_driving.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.extreme_driving.R;
import com.google.android.material.button.MaterialButton;

public class MainMenuActivity extends AppCompatActivity {
    private MaterialButton menu_BTN_sensor;
    private MaterialButton menu_BTN_buttons;
    private MaterialButton menu_BTN_fast;
    private MaterialButton menu_BTN_slow;
    private MaterialButton menu_BTN_startGame;
    private MaterialButton menu_BTN_highscores;
    private String gameMode;
    private String speedMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        findViews();
        initViews();
    }

    private void findViews() {
        menu_BTN_sensor = findViewById(R.id.menu_BTN_sensor);
        menu_BTN_buttons = findViewById(R.id.menu_BTN_buttons);
        menu_BTN_fast = findViewById(R.id.menu_BTN_fast);
        menu_BTN_slow = findViewById(R.id.menu_BTN_slow);
        menu_BTN_startGame = findViewById(R.id.menu_BTN_startGame);
        menu_BTN_highscores = findViewById(R.id.menu_BTN_highscores);
    }

    private void initViews() {
        menu_BTN_sensor.setOnClickListener(v -> setSensorGame());
        menu_BTN_buttons.setOnClickListener(v -> setButtonsGame());
        menu_BTN_fast.setOnClickListener(v -> setSpeedModeFast());
        menu_BTN_slow.setOnClickListener(v -> setSpeedModeSlow());
        menu_BTN_startGame.setOnClickListener(v -> startGame());
        menu_BTN_highscores.setOnClickListener(v -> navigateToHighscores());
    }

    private void setSensorGame() {
        gameMode = "sensor";
        menu_BTN_sensor.setBackgroundColor(ContextCompat.getColor(this, R.color.button_primary_pressed_color_blue_300));
        menu_BTN_buttons.setBackgroundColor(ContextCompat.getColor(this, R.color.button_primary_color_blue_900));
    }

    private void setButtonsGame() {
        gameMode = "buttons";
        menu_BTN_sensor.setBackgroundColor(ContextCompat.getColor(this, R.color.button_primary_color_blue_900));
        menu_BTN_buttons.setBackgroundColor(ContextCompat.getColor(this, R.color.button_primary_pressed_color_blue_300));
    }

    private void setSpeedModeFast() {
        speedMode = "fast";
        menu_BTN_slow.setBackgroundColor(ContextCompat.getColor(this, R.color.button_primary_color_blue_900));
        menu_BTN_fast.setBackgroundColor(ContextCompat.getColor(this, R.color.button_primary_pressed_color_blue_300));
    }

    private void setSpeedModeSlow() {
        speedMode = "slow";
        menu_BTN_slow.setBackgroundColor(ContextCompat.getColor(this, R.color.button_primary_pressed_color_blue_300));
        menu_BTN_fast.setBackgroundColor(ContextCompat.getColor(this, R.color.button_primary_color_blue_900));
    }

    private void startGame() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.KEY_GAME_MODE, gameMode);
        intent.putExtra(MainActivity.KEY_SPEED, speedMode);
        startActivity(intent);
        finish();
    }

    private void navigateToHighscores() {
        Intent intent = new Intent(this, HighscoresActivity.class);
        startActivity(intent);
        finish();
    }
}
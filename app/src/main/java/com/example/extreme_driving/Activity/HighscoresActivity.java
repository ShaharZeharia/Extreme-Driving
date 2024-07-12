
package com.example.extreme_driving.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import com.example.extreme_driving.Fragments.ListFragment;
import com.example.extreme_driving.Fragments.MapFragment;
import com.example.extreme_driving.Interfaces.Callback_ListItemClicked;
import com.example.extreme_driving.Models.Player;
import com.example.extreme_driving.R;
import com.example.extreme_driving.Utilities.SharePreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class HighscoresActivity extends AppCompatActivity {
    private FrameLayout highscores_FRAME_list;
    private FrameLayout highscores_FRAME_map;
    private MaterialButton highscores_BTN_back_to_menu;

    private ListFragment listFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        findViews();
        initViews();
    }

    private ArrayList<Player> loadPlayerList() {
        String playerListJson = SharePreferencesManager.getInstance().getString("players_list", "");
        Gson gson = new Gson();
        return gson.fromJson(playerListJson, new TypeToken<ArrayList<Player>>() {}.getType());
    }

    private void initViews() {
        ArrayList<Player> playerList = loadPlayerList();
        listFragment = new ListFragment(playerList);
        listFragment.setCallbackListItemClicked(new Callback_ListItemClicked() {
            @Override
            public void listItemClicked(double lat, double lon) {
                mapFragment.zoom(lat, lon);
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.highscores_FRAME_list,listFragment).commit();
        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.highscores_FRAME_map,mapFragment).commit();

        highscores_BTN_back_to_menu.setOnClickListener(v -> navigateToMenu());
    }

    private void navigateToMenu() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void findViews(){
        highscores_FRAME_list = findViewById(R.id.highscores_FRAME_list);
        highscores_FRAME_map = findViewById(R.id.highscores_FRAME_map);
        highscores_BTN_back_to_menu = findViewById(R.id.highscores_BTN_back_to_menu);
    }
}

package com.example.extreme_driving.Models;

import androidx.annotation.NonNull;

import com.example.extreme_driving.Utilities.SharePreferencesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PlayerList {
    private static final int NUM_OF_TOP_PLAYERS = 10;
    private ArrayList<Player> players = new ArrayList<>();
    private static PlayerList instance = null;

    private PlayerList() {
        this.players = loadTopPlayersFromPreferences();
        if (players == null)
            this.players = new ArrayList<>();
    }

    public static int getNUM_OF_TOP_PLAYERS() {
        return NUM_OF_TOP_PLAYERS;
    }

    public static PlayerList getInstance() {
        if (instance == null)
            instance = new PlayerList();
        return instance;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        saveTopPlayersToPreferences();
    }

    public ArrayList<Player> getTopPlayers() {
        return new ArrayList<>(players.subList(0, Math.min(NUM_OF_TOP_PLAYERS, players.size())));
    }

    public void sortPlayersByScore() {
        players.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
    }

    private void saveTopPlayersToPreferences() {
        sortPlayersByScore();
        if (this.players.size() > PlayerList.getNUM_OF_TOP_PLAYERS()) {
            ArrayList<Player> topPlayers = new ArrayList<>(this.players.subList(0, PlayerList.getNUM_OF_TOP_PLAYERS()));
            this.players.clear();
            this.players.addAll(topPlayers);
        }
        Gson gson = new Gson();
        String playersJson = gson.toJson(this.players);
        SharePreferencesManager.getInstance().putString("players_list", playersJson);
    }

    private ArrayList<Player> loadTopPlayersFromPreferences() {
        String playersJson = SharePreferencesManager.getInstance().getString("players_list", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Player>>() {
        }.getType();
        return gson.fromJson(playersJson, type);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlayerList{" +
                "players=" + players +
                '}';
    }
}

package com.example.extreme_driving.Models;

import androidx.annotation.NonNull;

public class Player {
    private int score;
    private double lat;
    private double lng;

    public Player() {
    }

    public Player(int score, double lat, double lng) {
        this.score = score;
        this.lat = lat;
        this.lng = lng;
    }

    public int getScore() {
        return score;
    }

    public Player setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Player setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Player setLng(double lng) {
        this.lng = lng;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "Player{" +
                "score=" + score +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
package com.games.ms.model;

public class WelcomeModel {

    String id;
    String day;
    String coins;
    String game_played;
    String collected_days;
    int imgcoins;

    public int getImgcoins() {
        return imgcoins;
    }

    public void setImgcoins(int imgcoins) {
        this.imgcoins = imgcoins;
    }

    public String getCollected_days() {
        return collected_days;
    }

    public void setCollected_days(String collected_days) {
        this.collected_days = collected_days;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getGame_played() {
        return game_played;
    }

    public void setGame_played(String game_played) {
        this.game_played = game_played;
    }
}

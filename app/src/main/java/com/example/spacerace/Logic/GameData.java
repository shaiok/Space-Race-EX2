package com.example.spacerace.Logic;

public class GameData {

    private String name;
    private String mode;
    private int score;
    private int serialNoImg;
    private double latitude;
    private double longitude;

    public GameData() {
        this.name = "-";
        this.score = 000;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public GameData(String name, String mode , int score, double latitude, double longitude) {
        this.name = name;
        this.mode = mode;
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GameData(String name, String mode , int score) {
        this.name = name;
        this.mode = mode;
        this.score = score;
        this.latitude = 0;
        this.longitude = 0;
    }

    public String getMode() {
        return mode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setLocation(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLatitude() {
        return "" + latitude;
    }

    public String getLongitude() {
        return "" + longitude;
    }

    public GameData setSerialNoImg(int serialNoImg) {
        this.serialNoImg = serialNoImg;
        return this;
    }

    public int getImage() {
        return serialNoImg;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String toString() {
        return "TopTen{" +
                "name='" + name + '\'' +
                ", score='" + score + '\'' +
                ", serialNoImg=" + serialNoImg +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';

    }
}

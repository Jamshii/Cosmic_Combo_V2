package com.example.myapplication;

public class LeaderboardEntry {

    private String name;
    private int number;

    public LeaderboardEntry(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }
}

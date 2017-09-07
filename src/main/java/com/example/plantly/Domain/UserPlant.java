package com.example.plantly.Domain;

public class UserPlant {
    public String nickName;
    public String plantSpecies;
    public String light;
    public int waterDays;
    public String poison;

    public UserPlant(String nickName, String plantSpecies, String light, int waterDays, String poison) {
        this.nickName = nickName;
        this.plantSpecies = plantSpecies;
        this.light = light;
        this.waterDays = waterDays;
        this.poison = poison;
    }
}

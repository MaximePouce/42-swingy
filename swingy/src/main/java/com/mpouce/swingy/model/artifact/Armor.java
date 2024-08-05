package com.mpouce.swingy.model.artifact;

public class Armor implements Artifact {
    private String name;
    private int level;
    private int bonus;

    public Armor(String name, int level, int bonus) {
        this.name = name;
        this.level = level;
        this.bonus = bonus;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getBonus() {
        return this.bonus;
    }
}

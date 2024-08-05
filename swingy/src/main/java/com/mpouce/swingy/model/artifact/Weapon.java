package com.mpouce.swingy.model.artifact;

public class Weapon implements Artifact {
    private int id;
    private String name;
    private int level;
    private int bonus;

    public Weapon(int id, String name, int level, int bonus) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.bonus = bonus;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getType() {
        return "Weapon";
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

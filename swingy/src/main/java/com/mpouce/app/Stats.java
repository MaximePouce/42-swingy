package com.mpouce.app;

public class Stats {
    private int maxHitPoints;
    private int hitPoints;
    private int attack;
    private int defense;

    public Stats(int maxHitPoints, int hitPoints, int attack, int defense) {
        this.maxHitPoints = maxHitPoints;
        this.attack = attack;
        this.defense = defense;
        this. hitPoints = hitPoints;
    }

    public int getMaxHitPoints() { return this.maxHitPoints; }
    public int getHitPoints() { return this.hitPoints; }
    public int getAttack() { return this.attack; }
    public int getDefense() { return this.defense; }

    public void addHitPoints(int hitPoints) {
        this.hitPoints = Math.max(0, this.hitPoints + hitPoints);
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void addStats(Stats stats) {
        this.maxHitPoints += stats.getMaxHitPoints();
        this.hitPoints += stats.getMaxHitPoints();
        this.attack += stats.getAttack();
        this.defense += stats.getDefense();
    }
}
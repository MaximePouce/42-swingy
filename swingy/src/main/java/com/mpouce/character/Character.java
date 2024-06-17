package com.mpouce.character;

public class Character {
    protected String name;
    protected int level;
    protected int hitPoints;
    protected int attack;
    protected int defense;

    public Character(String name, int hitPoints, int attack, int defense) {
        this.name = name;
        this.hitPoints = hitPoints;
        this.attack = attack;
        this.defense = defense;
    }

    public String getName() {
        return this.name;
    }

    public int getHitPoints() {
        return this.hitPoints;
    }

    public int getAttack() {
        return this.attack;
    }

    public int getDefense() {
        return this.defense;
    }

    public void attack(Character target) {
        if (target == null) {
            throw new IllegalArgumentException("No target provided.");
        }
        target.takeDamage(this.attack);
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative.");
        }
        int damageTaken = Math.max(0, damage - defense);
        this.hitPoints = Math.max(0, hitPoints - damageTaken);
    }
}
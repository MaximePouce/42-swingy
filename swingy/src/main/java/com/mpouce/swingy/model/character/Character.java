package com.mpouce.swingy.model.character;

import com.mpouce.swingy.Stats;
import com.mpouce.swingy.model.Location;

public class Character {
    private int id;
    private String name;
    private CharacterClass characterClass = null;
    private Stats stats;
    private int experience;

    private int currentHitPoints;
    private int maxHitPoints;
    private int attack;
    private int defense;

    private Location location;

    public Character(String name, Stats stats, int experience) {
        this.name = name;
        this.stats = stats;
        this.experience = experience;
    }

    public Character(String name, int experience, int id, CharacterClass characterClass) {
        this.id = id;
        this.name = name;
        this.experience = experience;
        this.characterClass = characterClass;
        int hitPoints = characterClass.getHitPoints();
        this.currentHitPoints = hitPoints;
        this.maxHitPoints = hitPoints;
        this.attack = characterClass.getAttack();
        this.defense = characterClass.getDefense();
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public CharacterClass getCharacterClass() {
        return this.characterClass;
    }

    public Stats getStats() {
        return this.stats;
    }

    public int getMaxHitPoints() {
        return this.maxHitPoints;
    }

    public int getHitPoints() {
        return this.currentHitPoints;
    }

    public int getExperience() {
        return this.experience;
    }

    public int getAttack() {
        return this.attack;
    }

    public int getDefense() {
        return this.defense;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location newLocation) {
        this.location = newLocation;
    }

    public void addExp(int expGained) {
        this.experience += expGained;
    }

    public int getLevel() {
        int low = 0;
        int high = 100;

        while (low < high - 1) {
            int mid = (low + high) / 2;
            int requiredExperience = getRequiredExp(mid);

            if (requiredExperience == experience) {
                return mid;
            } else if (requiredExperience > experience) {
                high = mid;
            } else {
                low = mid;
            }
        }

        return low;
    }

    public int getRequiredExp(int level) {
        return (level * 1000 + (level - 1) * (level - 1) * 450);
    }

    public void attack(Character target) {
        if (target == null) {
            throw new IllegalArgumentException("No target provided.");
        }
        target.takeDamage(this.stats.getAttack());
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative.");
        }
        int damageTaken = Math.max(0, damage - this.stats.getDefense());
        this.stats.addHitPoints(-1 * damageTaken);
    }
}
package com.mpouce.swingy.model.character;

import com.mpouce.swingy.model.Location;

import com.mpouce.swingy.model.utils.DatabaseUtils;
import com.mpouce.swingy.model.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Character {
    private int id;
    private String name;
    private CharacterClass characterClass = null;
    private int level;
    private int experience;
    private int nextLevelExp;

    private int currentHitPoints;
    private int maxHitPoints;
    private int attack;
    private int defense;

    private Location location;

    public Character(String name, int experience, int hitPoints, int attack, int defense) {
        this.name = name;
        this.maxHitPoints = hitPoints;
        this.currentHitPoints = hitPoints;
        this.attack = attack;
        this.defense = defense;
        this.experience = experience;
    }

    public Character(String name, int experience, int id, CharacterClass characterClass) {
        this.id = id;
        this.name = name;
        this.experience = experience;
        this.characterClass = characterClass;
        this.level = getLevel();
        this.nextLevelExp = getRequiredExp(this.level + 1);

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

    public void setId(int newId) {
        this.id = newId;
    }

    public CharacterClass getCharacterClass() {
        return this.characterClass;
    }

    public void setStats(int maxHitPoints, int currentHitPoints, int attack, int defense) {
        this.maxHitPoints = maxHitPoints;
        this.currentHitPoints = currentHitPoints;
        this.attack = attack;
        this.defense = defense;
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
        if (this.experience >= this.nextLevelExp) {
            levelUp();
        }
    }

    public void levelUp() {
        if (this.characterClass == null) {
            return ;
        }

        this.maxHitPoints += this.characterClass.getHitPointsGrowth();
        this.currentHitPoints += this.characterClass.getHitPointsGrowth();
        this.attack += this.characterClass.getAttackGrowth();
        this.defense += this.characterClass.getDefenseGrowth();
        this.level++;
        this.nextLevelExp = getRequiredExp(this.level + 1);
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
        if (level == 0) {
            return 0;
        }
        return (level * 1000 + (level - 1) * (level - 1) * 450);
    }

    public void battle(Character target) {
        System.out.println(this.name + " is fighting enemy " + target.getName());
        while(this.currentHitPoints > 0 && target.getHitPoints() > 0) {
            attack(target);
            target.attack(this);
        }
        if (this.currentHitPoints > 0) {
            this.addExp(target.getExperience() / 5);
        }
    }

    public void attack(Character target) {
        if (target == null) {
            throw new IllegalArgumentException("No target provided.");
        }
        if (this.currentHitPoints > 0) {
            target.takeDamage(this.attack);
        }
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative.");
        }
        int damageTaken = Math.max(0, damage - this.defense);
        this.currentHitPoints = Math.max(0, this.currentHitPoints - damageTaken);
    }

    public void healDamage(int healing) {
        if (healing < 0) {
            throw new IllegalArgumentException("Healing cannot be negative.");
        }
        this.currentHitPoints = Math.min(this.maxHitPoints, this.currentHitPoints + healing);
    }
}
package com.mpouce.swingy.model.character;

import com.mpouce.swingy.controller.GameController;

import com.mpouce.swingy.model.Location;
import com.mpouce.swingy.model.artifact.Artifact;
import com.mpouce.swingy.model.artifact.ArtifactModel;
import com.mpouce.swingy.model.artifact.Armor;
import com.mpouce.swingy.model.artifact.Helmet;
import com.mpouce.swingy.model.artifact.Weapon;

import com.mpouce.swingy.model.utils.DatabaseUtils;
import com.mpouce.swingy.model.utils.DatabaseConnection;

import java.util.Random;

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
    private Armor armor;
    private Helmet helmet;
    private Weapon weapon;

    public Character(String name, int experience, int hitPoints, int attack, int defense) {
        this.name = name;
        this.maxHitPoints = hitPoints;
        this.currentHitPoints = hitPoints;
        this.attack = attack;
        this.defense = defense;
        this.experience = experience;
    }

    public Character(Character npc) {
        if (npc == null) {
            throw new IllegalArgumentException("Copying null character");
        }
        this.id = npc.id;
        this.name = npc.name;
        this.maxHitPoints = npc.maxHitPoints;
        this.currentHitPoints = npc.currentHitPoints;
        this.attack = npc.attack;
        this.defense = npc.defense;
        this.experience = npc.experience;
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

    public Armor getArmor() {
        return this.armor;
    }

    public Helmet getHelmet() {
        return this.helmet;
    }

    public Weapon getWeapon() {
        return this.weapon;
    }

    public boolean isDead() {
        int min = (this.helmet == null) ? 0 : this.helmet.getBonus() * -1;
        return (this.currentHitPoints <= min);
    }

    public void equipArtifact(Artifact artifact) {
        if (artifact instanceof Armor) {
            if (this.armor != null) {
                CharacterRepository.getInstance().deleteCharacterArtifact(this.id, this.armor.getId());
            }
            this.armor = (Armor) artifact;
            CharacterRepository.getInstance().createCharacterArtifact(this.id, this.armor.getId());
        } else if (artifact instanceof Helmet) {
            if (this.helmet != null) {
                CharacterRepository.getInstance().deleteCharacterArtifact(this.id, this.helmet.getId());
            }
            this.helmet = (Helmet) artifact;
            CharacterRepository.getInstance().createCharacterArtifact(this.id, this.helmet.getId());
        } else if (artifact instanceof Weapon) {
            if (this.weapon != null) {
                CharacterRepository.getInstance().deleteCharacterArtifact(this.id, this.weapon.getId());
            }
            this.weapon = (Weapon) artifact;
            CharacterRepository.getInstance().createCharacterArtifact(this.id, this.weapon.getId());
        } else {
            throw new IllegalArgumentException("Unknown artifact type: " + artifact.getClass().getSimpleName());
        }
    }

    public int getMaxHitPoints() {
        int totalMaxHitPoints = this.maxHitPoints;
        if (this.helmet != null) {
            totalMaxHitPoints += this.helmet.getBonus();
        }
        return totalMaxHitPoints;
    }

    public int getBaseMaxHitPoints() {
        return this.maxHitPoints;
    }

    public int getHitPoints() {
        int totalHitPoints = this.currentHitPoints;
        if (this.helmet != null) {
            totalHitPoints += this.helmet.getBonus();
        }
        return totalHitPoints;
    }

    public int getBaseHitPoints() {
        return this.currentHitPoints;
    }

    public int getExperience() {
        return this.experience;
    }

    public int getAttack() {
        int totalAttack = this.attack;
        if (this.weapon != null) {
            totalAttack += this.weapon.getBonus();
        }
        return totalAttack;
    }

    public int getBaseAttack() {
        return this.attack;
    }

    public int getDefense() {
        int totalDefense = this.defense;
        if (this.armor != null) {
            totalDefense += this.armor.getBonus();
        }
        return totalDefense;
    }

    public int getBaseDefense() {
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
        return getLevel(this.experience);
    }

    public static int getLevel(int experience) {
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

    public static int getRequiredExp(int level) {
        if (level == 0) {
            return 0;
        }
        return (level * 1000 + (level - 1) * (level - 1) * 450);
    }

    public void battle(Character target) {
        System.out.println(this.name + " is fighting enemy " + target.getName());
        int turn = 0;
        while(this.currentHitPoints > 0 && target.getHitPoints() > 0) {
            turn++;
            attack(target);
            target.attack(this);
            if (turn > 15) {
                this.currentHitPoints = 0;
            }
        }
        if (this.currentHitPoints > 0) {
            this.addExp(target.getExperience() / 5);
            target.getLoot();
        }
    }

    public void getLoot() {
        Random rand = new Random();
        int randomValue = rand.nextInt(42);

        if (randomValue % 5 == 0) {
            System.out.println("generating random loot");
            Artifact lootedArtifact = ArtifactModel.getInstance().getRandomArtifact((this.level + 1) / 2 + 1);
            GameController.getInstance().lootArtifact(lootedArtifact);
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
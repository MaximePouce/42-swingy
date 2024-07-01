package com.mpouce.swingy.model.character;

import com.mpouce.swingy.Stats;

public class Character {
    protected String name;

    protected Stats stats;

    protected int experience;

    public Character(String name, Stats stats, int experience) {
        this.name = name;
        this.stats = stats;
        this.experience = experience;
    }

    public String getName() {
        return this.name;
    }

    public Stats getStats() {
        return this.stats;
    }

    public int getLevel() {
        int low = 0;
        int high = 100;

        while (low < high - 1) {
            int mid = (low + high) / 2;
            int requiredExperience = mid * 1000 + (mid - 1) * (mid - 1) * 450;

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
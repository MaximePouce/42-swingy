package com.mpouce.swingy.model.character;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class CharacterTest {
    private Character attacker;
    private Character defender;


    @Before
    public void setUp() {
        attacker = new Character("Attacker", 798450, 100, 20, 0);
        defender = new Character("Defender", 769042, 100, 5, 10);
    }

    @Test
    public void testTakeDamage() {
        defender.takeDamage(15);
        assertEquals(95, defender.getHitPoints());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTakeDamageNegative() {
        defender.takeDamage(-10);
    }

    @Test
    public void testAttack() {
        attacker.attack(defender);
        assertEquals(90, defender.getHitPoints());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttackNoTarget() {
        attacker.attack(null);
    }

    @Test
    public void testHealthCannotBeNegative() {
        defender.takeDamage(9999);
        assertEquals(0, defender.getHitPoints());
    }

    @Test
    public void testGetLevel() {
        assertEquals(42, attacker.getLevel());
        assertEquals(41, defender.getLevel());
    }

    @Test
    public void testBattle() {
        attacker.battle(defender);
        assertEquals(0, defender.getHitPoints());
        assertEquals(55, attacker.getHitPoints());
    }
}
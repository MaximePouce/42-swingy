package com.mpouce.character;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class CharacterTest {
    private Character attacker;
    private Character defender;


    @Before
    public void setUp() {
        attacker = new Character("Attacker", 100, 20, 0);
        defender = new Character("Defender", 100, 5, 10);
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
}
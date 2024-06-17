package com.mpouce.app;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class StatsTest
{
    @Test
    public void TestAddStats()
    {
        Stats stats1 = new Stats(100, 10, 10, 10);
        Stats stats2 = new Stats(50, 5, 5, 5);
        stats1.addStats(stats2);
        assertEquals(150, stats1.getMaxHitPoints());
        assertEquals(60, stats1.getHitPoints());
        assertEquals(15, stats1.getAttack());
        assertEquals(15, stats1.getDefense());
    }
}

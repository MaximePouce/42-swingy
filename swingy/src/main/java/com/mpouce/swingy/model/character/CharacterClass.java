package com.mpouce.swingy.model.character;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacterClass {
    private int id;
    private String name;

    private int maxHitPoints;
    private int attack;
    private int defense;

    private int maxHitPointsGrowth;
    private int attackGrowth;
    private int defenseGrowth;

    public CharacterClass(ResultSet rs) throws SQLException {
        this.id = rs.getInt("classid");
        this.name = rs.getString("name");
        this.maxHitPoints = rs.getInt("health");
        this.attack = rs.getInt("attack");
        this.defense = rs.getInt("defense");
        this.maxHitPointsGrowth = rs.getInt("health_growth");
        this.attackGrowth = rs.getInt("attack_growth");
        this.defenseGrowth = rs.getInt("defense_growth");
    }

    public String getName() { return this.name; }
    public int getId() { return this.id; }
    public int getHitPoints() { return this.maxHitPoints; }
    public int getAttack() { return this.attack; }
    public int getDefense() { return this.defense; }
    public int getHitPointsGrowth() { return this.maxHitPointsGrowth; }
    public int getAttackGrowth() { return this.attackGrowth; }
    public int getDefenseGrowth() { return this.defenseGrowth; }
}
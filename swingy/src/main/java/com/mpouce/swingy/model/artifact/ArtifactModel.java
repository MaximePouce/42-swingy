package com.mpouce.swingy.model.artifact;

import com.mpouce.swingy.model.utils.DatabaseUtils;
import com.mpouce.swingy.model.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ArtifactModel {

    private ArtifactModel() {}

    public static Artifact getRandomArtifact(int lootLevel) {
        String query = "SELECT * FROM artifacts "
                        + "WHERE level < ? "
                        + "ORDER BY RANDOM() "
                        + "LIMIT 1;";
        try {
            PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            stmt.setInt(1, lootLevel);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String name = rs.getString("name");
                int level = rs.getInt("level");
                int bonus = rs.getInt("bonus");
                System.out.println("Creating artifact " + name + " level " + level + " " + type);
                return ArtifactFactory.createArtifact(id, type, name, level, bonus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
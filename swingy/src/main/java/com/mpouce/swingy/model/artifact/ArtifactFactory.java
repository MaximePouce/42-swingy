package com.mpouce.swingy.model.artifact;

public class ArtifactFactory {
    public static Artifact createArtifact(int id, String type, String name, int level, int bonus) {
        switch (type) {
            case "armor":
                return new Armor(id, name, level, bonus);
            case "helmet":
                return new Helmet(id, name, level, bonus);
            case "weapon":
                return new Weapon(id, name, level, bonus);
            default:
                throw new IllegalArgumentException("Unknown artifact type: " + type);
        }
    }
}

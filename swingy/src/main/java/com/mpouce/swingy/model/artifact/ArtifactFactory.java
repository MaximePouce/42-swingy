package com.mpouce.swingy.model.artifact;

public class ArtifactFactory {
    public static Artifact createArtifact(String type, String name, int level, int bonus) {
        switch (type) {
            case "armor":
                return new Armor(name, level, bonus);
            case "helmet":
                return new Helmet(name, level, bonus);
            case "weapon":
                return new Weapon(name, level, bonus);
            default:
                throw new IllegalArgumentException("Unknown artifact type: " + type);
        }
    }
}

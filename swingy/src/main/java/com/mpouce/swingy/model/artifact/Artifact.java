package com.mpouce.swingy.model.artifact;

import com.mpouce.swingy.Stats;

public abstract class Artifact {
    protected Stats stats;
    protected String name;

    public Stats getStats() {
        return this.stats;
    }
}

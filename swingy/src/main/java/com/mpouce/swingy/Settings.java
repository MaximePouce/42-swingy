package com.mpouce.swingy;

public class Settings {
    boolean useGui;
    private static Settings instance;

    private Settings() {
        this.useGui = false;
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public boolean getUseGui() {
        return useGui;
    }

    public void setGui(boolean newState) {
        this.useGui = newState;
    }
}
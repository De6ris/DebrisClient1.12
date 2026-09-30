package com.github.debris.debrisclient.config;

import fi.dy.masa.malilib.config.IConfigHandler;

public abstract class ConfigHandlerImpl implements IConfigHandler {
    private final String name;
    private final String id;

    public ConfigHandlerImpl(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getModName() {
        return this.name;
    }

    @Override
    public String getConfigFileName() {
        return this.id + ".json";
    }

    public String getId() {
        return this.id;
    }
}

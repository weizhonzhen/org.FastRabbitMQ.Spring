package org.FastRabbitMQ.Spring.Aop;

import org.FastRabbitMQ.Spring.Model.ConfigModel;

public class DeleteContext {

    private ConfigModel config;

    public ConfigModel getConfig() {
        return config;
    }

    public void setConfig(ConfigModel config) {
        this.config = config;
    }
}

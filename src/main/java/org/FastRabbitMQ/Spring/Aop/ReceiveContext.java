package org.FastRabbitMQ.Spring.Aop;

import org.FastRabbitMQ.Spring.Model.ConfigModel;
import java.util.HashMap;

public class ReceiveContext {
    private HashMap<String, Object> content;

    private ConfigModel config;

    public HashMap<String, Object> getContent() {
        return content;
    }

    public void setContent(HashMap<String, Object> content) {
        this.content = content;
    }

    public ConfigModel getConfig() {
        return config;
    }

    public void setConfig(ConfigModel config) {
        this.config = config;
    }
}

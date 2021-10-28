package org.FastRabbitMQ.Spring.Aop;

import org.FastRabbitMQ.Spring.Model.ConfigModel;

import java.util.HashMap;

public class ExceptionContext {
    private HashMap<String, Object> content ;

    private boolean isSend;

    private boolean isReceive ;

    private boolean isDelete ;

    private ConfigModel config ;

    private Exception ex ;

    public HashMap<String, Object> getContent() {
        return content;
    }

    public void setContent(HashMap<String, Object> content) {
        this.content = content;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public boolean isReceive() {
        return isReceive;
    }

    public void setReceive(boolean receive) {
        isReceive = receive;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public ConfigModel getConfig() {
        return config;
    }

    public void setConfig(ConfigModel config) {
        this.config = config;
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }
}

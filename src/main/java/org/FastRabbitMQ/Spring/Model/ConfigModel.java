package org.FastRabbitMQ.Spring.Model;

public class ConfigModel {
    private String QueueName;

    private Exchange Exchange;

    private boolean IsAutoAsk;

    public String getQueueName() {
        return QueueName;
    }

    public void setQueueName(String queueName) {
        QueueName = queueName;
    }

    public org.FastRabbitMQ.Spring.Model.Exchange getExchange() {
        return Exchange;
    }

    public void setExchange(org.FastRabbitMQ.Spring.Model.Exchange exchange) {
        Exchange = exchange;
    }

    public boolean isAutoAsk() {
        return IsAutoAsk;
    }

    public void setAutoAsk(boolean autoAsk) {
        IsAutoAsk = autoAsk;
    }
}

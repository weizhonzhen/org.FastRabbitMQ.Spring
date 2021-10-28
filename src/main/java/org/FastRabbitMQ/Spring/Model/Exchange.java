package org.FastRabbitMQ.Spring.Model;

public class Exchange {
    private String ExchangeName ;

    private ExchangeType ExchangeType ;

    private String RouteKey ;

    public String getExchangeName() {
        return ExchangeName;
    }

    public void setExchangeName(String exchangeName) {
        ExchangeName = exchangeName;
    }

    public org.FastRabbitMQ.Spring.Model.ExchangeType getExchangeType() {
        return ExchangeType;
    }

    public void setExchangeType(org.FastRabbitMQ.Spring.Model.ExchangeType exchangeType) {
        ExchangeType = exchangeType;
    }

    public String getRouteKey() {
        return RouteKey;
    }

    public void setRouteKey(String routeKey) {
        RouteKey = routeKey;
    }
}

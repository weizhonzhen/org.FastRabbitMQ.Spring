# org.FastRabbitMQ.Spring

in Application add Annotation
```csharp
@FastRabbitConfig(aopType = FastRabbitAop.class,host = "127.0.0.1",passWord = "guest",
	userName = "guest",port = 5672,virtualHost = "/")
@FastRabbitReceiveConfig(aopType = FastRabbitAop.class,receiveType = FastRabbitEnum.class)

or

@FastRabbitReceiveJson(aopType = FastRabbitAop.class, jsonFile = "db.json",jsonKey = "FastRabbitReceive")
@FastRabbitJson(aopType = FastRabbitAop.class,jsonFile = "db.json",jsonKey = "FastRabbit")


public class FastRabbitAop  implements IFastRabbitAop {
    @Override
    public void Send(SendContext sendContext) {

    }

    @Override
    public void Receive(ReceiveContext receiveContext) {
        
    }

    @Override
    public void Exception(ExceptionContext exceptionContext) {

    }

    @Override
    public void Delete(DeleteContext deleteContext) {

    }
}
```

in resources add db.json
```csharp
{
  "FastRabbit" : {
    "Host": "127.0.0.1",
    "PassWord": "guest",
    "UserName": "guest",
    "Port": 5672,
    "VirtualHost": "/"
  },
  "FastRabbitReceive":[{
    "QueueName": "test",
    "IsAutoAsk": false,
    "Exchange": {
      "ExchangeName": "ex",
      "ExchangeType": "direct",
      "RouteKey": "key"
    }
  }]
}
```

Test
```csharp
@Resource
IFastRabbit iFastRabbit;
  
var config =new ConfigModel();
var map =new HashMap<String,Object>();
map.put("gh","admin");
map.put("kid",101);

config.setQueueName("test");
Exchange exchange =new Exchange();
exchange.setExchangeName("ex");
exchange.setExchangeType(ExchangeType.direct);
exchange.setRouteKey("key");
config.setExchange(exchange);
    
iFastRabbit.send(config,map);

iFastRabbit.delete(config);  
  
```

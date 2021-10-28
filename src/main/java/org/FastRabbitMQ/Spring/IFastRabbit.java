package org.FastRabbitMQ.Spring;

import org.FastRabbitMQ.Spring.Model.ConfigModel;
import org.springframework.stereotype.Component;
import java.util.HashMap;

@Component
public interface IFastRabbit {
    void send(ConfigModel model, HashMap<String, Object> content);

    void delete(ConfigModel model);
}

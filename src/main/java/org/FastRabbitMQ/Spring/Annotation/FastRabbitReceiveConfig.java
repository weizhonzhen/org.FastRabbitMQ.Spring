package org.FastRabbitMQ.Spring.Annotation;

import org.FastRabbitMQ.Spring.Aop.IFastRabbitAop;
import org.FastRabbitMQ.Spring.FastRabbit;
import org.FastRabbitMQ.Spring.Model.ConfigModel;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({FastRabbit.class})
public @interface FastRabbitReceiveConfig {
    String queueName() default "";

    Class<?> aopType() default IFastRabbitAop.class;

    Class<?> receiveType() default ConfigModel.class;
}
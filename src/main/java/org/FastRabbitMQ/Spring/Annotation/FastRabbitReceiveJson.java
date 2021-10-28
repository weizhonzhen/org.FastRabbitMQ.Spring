package org.FastRabbitMQ.Spring.Annotation;

import org.FastRabbitMQ.Spring.Aop.IFastRabbitAop;
import org.FastRabbitMQ.Spring.FastRabbit;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({FastRabbit.class})
public @interface FastRabbitReceiveJson {
    String jsonFile() default "db.json";

    String jsonKey() default "FastRabbitReceive";

    Class<?> aopType() default IFastRabbitAop.class;
}
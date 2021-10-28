package org.FastRabbitMQ.Spring.Annotation;

import org.FastRabbitMQ.Spring.Aop.IFastRabbitAop;
import org.FastRabbitMQ.Spring.FastRabbit;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({FastRabbit.class})
public @interface FastRabbitConfig {
    String host() default "";

    int port() default 5672;

    String userName() default "";

    String passWord() default "";

    String virtualHost() default "/";

    Class<?> aopType() default IFastRabbitAop.class;
}

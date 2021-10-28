package org.FastRabbitMQ.Spring.Aop;

public interface IFastRabbitAop {
    void Send(SendContext context);

    void Receive(ReceiveContext context);

    void Exception(ExceptionContext context);

    void Delete(DeleteContext context);
}

package org.FastRabbitMQ.Spring;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.*;
import org.FastRabbitMQ.Spring.Annotation.FastRabbitConfig;
import org.FastRabbitMQ.Spring.Annotation.FastRabbitJson;
import org.FastRabbitMQ.Spring.Annotation.FastRabbitReceiveConfig;
import org.FastRabbitMQ.Spring.Annotation.FastRabbitReceiveJson;
import org.FastRabbitMQ.Spring.Aop.*;
import org.FastRabbitMQ.Spring.Model.ConfigModel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@ComponentScan(basePackages = {"org.FastRabbitMQ.Spring"})
public class FastRabbit implements IFastRabbit {
    private IFastRabbitAop iFastRabbitAop;
    public FastRabbit() {
        try {
            for (StackTraceElement item : Thread.currentThread().getStackTrace()) {
                FastRabbitJson fastRabbitJson = Thread.currentThread().getContextClassLoader().loadClass(item.getClassName()).getAnnotation(FastRabbitJson.class);
                FastRabbitReceiveJson fastRabbitReceiveJson = Thread.currentThread().getContextClassLoader().loadClass(item.getClassName()).getAnnotation(FastRabbitReceiveJson.class);
                FastRabbitConfig fastRabbitConfig = Thread.currentThread().getContextClassLoader().loadClass(item.getClassName()).getAnnotation(FastRabbitConfig.class);
                FastRabbitReceiveConfig fastRabbitReceiveConfig = Thread.currentThread().getContextClassLoader().loadClass(item.getClassName()).getAnnotation(FastRabbitReceiveConfig.class);

                if (fastRabbitJson != null) {
                    if (fastRabbitJson.aopType().isInterface() || Arrays.stream(fastRabbitJson.aopType().getInterfaces()).noneMatch(a -> a == IFastRabbitAop.class))
                        iFastRabbitAop = null;
                    else
                        iFastRabbitAop = (IFastRabbitAop) fastRabbitJson.aopType().newInstance();

                    String content = content(fastRabbitJson.jsonFile()).get(fastRabbitJson.jsonKey()).toString();
                    InitData model = JSON.parseObject(content, InitData.class);
                    if (model == null || model.getHost() == null || model.getHost().equals("") || fastRabbitJson.jsonFile().equals(""))
                        throw new Exception("FastRabbitJson host or jsonFile is not null");
                    ApplicationContextRegisterRabbit.setBean(model);
                }

                if (fastRabbitConfig != null) {
                    InitData model = new InitData();
                    if (fastRabbitConfig.aopType().isInterface() || Arrays.stream(fastRabbitConfig.aopType().getInterfaces()).noneMatch(a -> a == IFastRabbitAop.class))
                        iFastRabbitAop = null;
                    else
                        iFastRabbitAop = (IFastRabbitAop) fastRabbitConfig.aopType().newInstance();

                    model.setHost(fastRabbitConfig.host());
                    model.setPort(fastRabbitConfig.port());
                    model.setVirtualHost(fastRabbitConfig.virtualHost());
                    model.setUserName(fastRabbitConfig.userName());
                    model.setPassWord(fastRabbitConfig.passWord());
                    if (model.getHost() == null || model.getHost().equals(""))
                        throw new Exception("FastRabbit host is not null");
                    ApplicationContextRegisterRabbit.setBean(model);
                }

                if (fastRabbitReceiveJson != null) {
                    if (fastRabbitReceiveJson.aopType().isInterface() || Arrays.stream(fastRabbitReceiveJson.aopType().getInterfaces()).noneMatch(a -> a == IFastRabbitAop.class))
                        iFastRabbitAop = null;
                    else
                        iFastRabbitAop = (IFastRabbitAop) fastRabbitReceiveJson.aopType().newInstance();

                    String content = content(fastRabbitReceiveJson.jsonFile()).get(fastRabbitReceiveJson.jsonKey()).toString();
                    List<ConfigModel> list = JSON.parseArray(content, ConfigModel.class);
                    if (list == null || fastRabbitReceiveJson.jsonFile().equals(""))
                        throw new Exception("FastRabbitReceiveJson jsonFile is not null");
                    receive(list);
                }

                if (fastRabbitReceiveConfig != null) {
                    if (fastRabbitReceiveConfig.aopType().isInterface() || Arrays.stream(fastRabbitReceiveConfig.aopType().getInterfaces()).noneMatch(a -> a == IFastRabbitAop.class))
                        iFastRabbitAop = null;
                    else
                        iFastRabbitAop = (IFastRabbitAop) fastRabbitReceiveConfig.aopType().newInstance();

                    List<ConfigModel> list = new ArrayList<ConfigModel>();
                    List<Method> methods = Arrays.stream(fastRabbitReceiveConfig.receiveType().getMethods()).filter(a -> a.getReturnType() == List.class).collect(Collectors.toList());
                    methods = methods.stream().filter(a -> ((ParameterizedTypeImpl) a.getGenericReturnType()).getActualTypeArguments()[0] == ConfigModel.class).collect(Collectors.toList());
                    if (methods.size() == 0) {
                        methods = Arrays.stream(fastRabbitReceiveConfig.receiveType().getMethods()).filter(a -> a.getReturnType() == ConfigModel.class).collect(Collectors.toList());

                        if (methods.size() == 0)
                            throw new Exception(String.format("%s not return ConfigModel Method", fastRabbitReceiveConfig.receiveType().getName()));

                        if (fastRabbitReceiveConfig.receiveType().isEnum())
                            list.add((ConfigModel) methods.get(0).invoke(fastRabbitReceiveConfig.receiveType()));
                        else
                            list.add((ConfigModel) methods.get(0).invoke(fastRabbitReceiveConfig.receiveType().newInstance()));
                    }
                    else {
                        if (fastRabbitReceiveConfig.receiveType().isEnum())
                            list = (List<ConfigModel>) methods.get(0).invoke(fastRabbitReceiveConfig.receiveType());
                        else
                            list = (List<ConfigModel>) methods.get(0).invoke(fastRabbitReceiveConfig.receiveType().newInstance());
                    }

                    receive(list);
                }
            }

            if (iFastRabbitAop == null)
                throw new Exception("IFastRabbitAop is not null");

            ApplicationContextRegisterRabbit.setBean(iFastRabbitAop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receive(List<ConfigModel> list ) throws IOException {
        Connection connection = (Connection) ApplicationContextRegisterRabbit.getBean(Connection.class);
        for (ConfigModel config : list) {
            Channel channe = connection.createChannel();
            if (!config.isAutoAsk())
                channe.basicQos(0, 1, false);

            if (config.getExchange() == null)
                channe.queueDeclare(config.getQueueName(), false, false, false, null);
            else {
                channe.exchangeDeclare(config.getExchange().getExchangeName(), config.getExchange().getExchangeType().toString());
                config.setQueueName(String.format("%s_%s", config.getExchange().getExchangeName(), UUID.randomUUID()));
                channe.queueDeclare(config.getQueueName(), false, false, false, null);
                channe.queueBind(config.getQueueName(), config.getExchange().getExchangeName(), config.getExchange().getRouteKey());
            }

            Consumer consumer = new DefaultConsumer(channe) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    HashMap<String, Object> content = JSON.parseObject(body, HashMap.class);
                    ReceiveContext receive = new ReceiveContext();
                    receive.setConfig(config);
                    receive.setContent(content);
                    if (iFastRabbitAop != null)
                        iFastRabbitAop.Receive(receive);
                    if (!config.isAutoAsk())
                        channe.basicAck(envelope.getDeliveryTag(), false);
                }
            };

            channe.basicConsume(config.getQueueName(), config.isAutoAsk(), consumer);
        }
    }

    private Map content(String fileName) {
        String s;
        StringBuilder sb = new StringBuilder();
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (stream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                while ((s = reader.readLine()) != null) {
                    sb.append(s);
                }
                reader.close();
                stream.close();
            }

            return JSON.parseObject(sb.toString(), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void send(ConfigModel model, HashMap<String, Object> content) {
        IFastRabbitAop aop = null;
        try {
            Connection conn = (Connection) ApplicationContextRegisterRabbit.getBean(Connection.class);
            aop = (IFastRabbitAop) ApplicationContextRegisterRabbit.getBean(IFastRabbitAop.class);

            Channel channel = conn.createChannel();
            byte[] body = JSON.toJSONBytes(content);
            if (model.getExchange() == null) {
                channel.queueDeclare(model.getQueueName(), false, false, false, null);
                channel.basicPublish("", model.getQueueName(), null, body);
            } else {
                channel.exchangeDeclare(model.getExchange().getExchangeName(), model.getExchange().getExchangeType().toString());
                channel.basicPublish(model.getExchange().getExchangeName(), model.getExchange().getRouteKey(), null, body);
            }

            SendContext send = new SendContext();
            send.setConfig(model);
            send.setContent(content);
            aop.Send(send);
        } catch (Exception ex) {
            ExceptionContext context = new ExceptionContext();
            context.setContent(content);
            context.setEx(ex);
            context.setSend(true);
            context.setConfig(model);
            assert aop != null;
            aop.Exception(context);
        }
    }

    @Override
    public void delete(ConfigModel model) {
        IFastRabbitAop aop = null;
        try {
            Connection conn = (Connection) ApplicationContextRegisterRabbit.getBean(Connection.class);
            aop = (IFastRabbitAop) ApplicationContextRegisterRabbit.getBean(IFastRabbitAop.class);

            Channel channel = conn.createChannel();

            if (model.getExchange() == null)
                channel.queueDelete(model.getQueueName());
            else
                channel.exchangeDelete(model.getExchange().getExchangeName());

            DeleteContext delete = new DeleteContext();
            delete.setConfig(model);
            aop.Delete(delete);
        } catch (Exception ex) {
            ExceptionContext context = new ExceptionContext();
            context.setEx(ex);
            context.setDelete(true);
            context.setConfig(model);
            assert aop != null;
            aop.Exception(context);
        }
    }
}

class InitData {
    private String Host;

    private int Port  = 5672;

    private String UserName ;

    private String PassWord ;

    private String VirtualHost  = "/";

    private IFastRabbitAop aop ;

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getVirtualHost() {
        return VirtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        VirtualHost = virtualHost;
    }

    public IFastRabbitAop getAop() {
        return aop;
    }

    public void setAop(IFastRabbitAop aop) {
        this.aop = aop;
    }
}

@Component
class ApplicationContextRegisterRabbit implements BeanFactoryPostProcessor {
    private static ConfigurableListableBeanFactory beanFactory;

    public static void setBean(InitData config) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(config.getHost());
            factory.setPort(config.getPort());
            factory.setUsername(config.getUserName());
            factory.setPassword(config.getPassWord());
            factory.setVirtualHost(config.getVirtualHost());
            factory.setAutomaticRecoveryEnabled(true);
            Connection connection = factory.newConnection();
            beanFactory.registerSingleton(Connection.class.getName(), connection);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Object getBean(Class<?> type) {
        return beanFactory.getBean(type.getName());
    }

    public  static  void  setBean(IFastRabbitAop aop) {
        beanFactory.registerSingleton(IFastRabbitAop.class.getName(), aop);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ApplicationContextRegisterRabbit.beanFactory = beanFactory;
    }
}
package cn.edu.xmu.rocketmqdemo.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * @author Ming Qiu
 * @date Created in 2020/11/8 5:49
 **/
@Configuration
public class RocketMQConfig {

    @Value("${rocketmq.name-server}")
    private String namesrv;

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Bean
    public RocketMQTemplate rocketMQTemplate(){
        DefaultMQProducer producer = new DefaultMQProducer();
        int random = new Random().nextInt(100);
        producer.setProducerGroup(String.format("%s-%d",this.producerGroup,random));
        producer.setNamesrvAddr(this.namesrv);
        RocketMQTemplate template = new RocketMQTemplate();
        template.setProducer(producer);
        return template;
    }

}

package cn.edu.xmu.rocketmqdemo.service;

import cn.edu.xmu.rocketmqdemo.model.Log;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import cn.edu.xmu.rocketmqdemo.util.JacksonUtil;

/**
 * 消息消费者
 * @author Ming Qiu
 * @date Created in 2020/11/7 22:47
 **/
@Service
@RocketMQMessageListener(topic = "log-topic", selectorExpression = "1", consumeMode = ConsumeMode.CONCURRENTLY, consumeThreadMax = 1, consumerGroup = "consumer-group")
public class LogConsumerListener implements RocketMQListener<String>{
    private static final Logger logger = LoggerFactory.getLogger(LogConsumerListener.class);
    @Override
    public void onMessage(String message) {
        Log log = JacksonUtil.toObj(message, Log.class);
        logger.info("onMessage: got message log =" + log);
    }
}

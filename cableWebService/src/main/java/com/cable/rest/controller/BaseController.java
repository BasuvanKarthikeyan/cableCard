package com.cable.rest.controller;


import lombok.extern.log4j.Log4j;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;*/
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cable.rest.config.SpringApplicationContext;
@Log4j
public class BaseController {

    @Value("${queue.name}")
    String queueName="Q1";

    @Autowired
    RabbitTemplate mq;  
    
    
    @Value("${queue.MQFlag}")
    String MQFlag;
    
    public Object sendtoMQ(Object request, final String serviceMethodName, final String serviceName) {
       
    	if(MQFlag.equalsIgnoreCase("false")){
    		return SpringApplicationContext.getAppResponse(request, serviceMethodName, serviceName);
    	}
    	
        return mq.convertSendAndReceive(queueName, request, new MessagePostProcessor() {

            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setHeader("ServiceMethodName", serviceMethodName);
                message.getMessageProperties().setHeader("ServiceName", serviceName);
                try
                {
                	SecurityContext context = SecurityContextHolder.getContext();
                    if (context != null) {
                        Authentication authentication = context.getAuthentication();
                        if (authentication != null && authentication.getPrincipal() != null
                                && authentication.getPrincipal() instanceof Integer) {
                        	Integer sid = (Integer) authentication.getPrincipal();
                            message.getMessageProperties().setHeader("SID", sid);

                        }
                    }
                }
                catch(Exception ex)
                {
                	log.error("Exception in BaseController :",ex);
                }               
                
                return message;
            }
        });
    }

}

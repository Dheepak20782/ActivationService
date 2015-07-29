package com.tcs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;






import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tcs.domain.Order;

@RestController
public class MainController {
	
	static Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
    @LoadBalanced
    RestTemplate restTemplate;
	 
	@RequestMapping("/order")
	public Order getOrders(){
	
		Order order = new Order();
		order.setOrderId(new Long(123));
		order.setOrderNumber("abc");
		order.setOrderStatus("COMPLETED");
		logger.info(" Returning Orders" + order);
		return order;
		
	}
	
	@HystrixCommand(fallbackMethod = "defaultActivate")
	@RequestMapping("/activate")
	public Order testActivate(){
	
		logger.info("Trying Primary Activation Activation");
		return restTemplate.getForObject("http://localhost/available", Order.class);
		
		
	}
	
	public Order defaultActivate(){
		
		logger.info("Falling back to  Default Activation");
		Order order = new Order();
		order.setOrderId(new Long(678));
		order.setOrderNumber("Default Order");
		order.setOrderStatus("PENDING");
		logger.info(" Returning Default Orders" + order);
		return order;
		
	}

}

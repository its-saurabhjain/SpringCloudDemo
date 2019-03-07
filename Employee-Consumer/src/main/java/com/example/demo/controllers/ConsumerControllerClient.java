package com.example.demo.controllers;

import java.util.List;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

//import com.netflix.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

public class ConsumerControllerClient {
	//added coded for dynamic service discovery & Zuul API Gateway
	@Autowired
	private DiscoveryClient discoveryClient;
	//@Autowired
	//private LoadBalancerClient loadBalancer;
	
	public void getEmployee() throws RestClientException, IOException {

		//String baseUrl = "http://localhost:8080/employee";
		//Replace the above hard coded service url with dynamic discovery
		//List<ServiceInstance> instances= this.discoveryClient.getInstances("EMPLOYEE-PRODUCER");
		//ServiceInstance serviceInstance=instances.get(0);
		
		/*//uncomment for Load balancing 
		ServiceInstance serviceInstance=loadBalancer.choose("EMPLOYEE-PRODUCER");
		String baseUrl=serviceInstance.getUri().toString();
		System.out.println(serviceInstance.getUri());
					
		baseUrl=baseUrl+"/employee"; */
		
		//for Zuul API gateway
		List<ServiceInstance> instances= this.discoveryClient.getInstances("APIGATEWAY-ZUUL-SERVICE");
		ServiceInstance serviceInstance=instances.get(0);
		String baseUrl = serviceInstance.getUri().toString();

		baseUrl = baseUrl + "/producer/employee";
				
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response=null;
		try{
		response=restTemplate.exchange(baseUrl,
				HttpMethod.GET, getHeaders(),String.class);
		}catch (Exception ex)
		{
			System.out.println(ex);
		}
		System.out.println(response.getBody());
	}
	private static HttpEntity<?> getHeaders() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}

}

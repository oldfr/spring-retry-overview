package com.example.springretryoverview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@Service
public class SecondService {

    @Autowired
    RetryTemplate retryTemplate;

    @Autowired
    private RestTemplate restTemplate;

    public String testAPIService(String arg1, Double arg2) throws URISyntaxException, ConnectException {
        return retryTemplate.execute(ctx -> {
            System.out.println("inside SecondService. Retrying count: "+ctx.getRetryCount()+" at time: "+ LocalDateTime.now());
            RequestEntity<Object> request = new RequestEntity<>(HttpMethod.GET, new URI("http://localhost:8088/students"));
            restTemplate.exchange(request, String.class);
            return "calledAPIInsideRetryTemplate";
        }, (retryContext) -> {
                return "CouldNotCallAPIInsideRetryTemplate";
        });
    }

}

package com.example.springretryoverview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@Service
public class MainService {

    @Autowired
    private RestTemplate restTemplate;

    /***
     * retries 2 times (including first execution) with gap of 2 seconds between each retries. Calls recover method on 5th failure
     * @throws URISyntaxException
     * @throws ConnectException
     */
    @Retryable(retryFor = {ConnectException.class, ArithmeticException.class, ResourceAccessException.class}, maxAttempts = 2, backoff = @Backoff(value = 2000))
    public String testAPIService() throws URISyntaxException, ConnectException {
        try {
            System.out.println("Retry Number : " + RetrySynchronizationManager.getContext().getRetryCount()+" at time:"+ LocalDateTime.now());
//            int a = 8/0;
            RequestEntity<Object> request = new RequestEntity<>(HttpMethod.GET, new URI("http://localhost:8088/students"));
            restTemplate.exchange(request, String.class);
            return "calledAPI";
        }
       /* catch (URISyntaxException uex) {
            throw uex;
        }
        catch (ArithmeticException ax) {
            System.out.println("ArithmeticException occurred");
            throw ax;
        }*/
        catch (Exception ex) {
            System.out.println("other exception occurred. "+ex.getMessage());
            if(ex instanceof  ConnectException) {
                System.out.println("throwing");
                throw new ConnectException();
            }
            if(ex instanceof ResourceAccessException) {
                System.out.println("throwing resourceAccessException");
                throw new ResourceAccessException(ex.getMessage());
            }
        }
        return null;
    }

    @Recover
    public String recover(ConnectException e) throws URISyntaxException {
        System.out.println("ConnectException recovered at:"+LocalDateTime.now());
        return "CouldNotCallAPI";
    }

    @Recover
    public String recover(ArithmeticException ex) {
        System.out.println("ArithmeticException recovered at:"+LocalDateTime.now());
        return "CouldNotCallAPI";
    }

    @Recover
    public String recover(ResourceAccessException ex) {
        System.out.println("ResourceAccessException recovered at:"+LocalDateTime.now());
        return "CouldNotCallAPI";
    }

}

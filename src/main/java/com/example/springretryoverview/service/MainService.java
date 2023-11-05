package com.example.springretryoverview.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(MainService.class);

    @Autowired
    private RestTemplate restTemplate;

    /***
     * This method called 2 times (including first execution) with gap of 2000 milliseconds between each call.
     * And, will call the recover method on 2nd failure
     * @throws URISyntaxException
     * @throws ConnectException
     */
    @Retryable(retryFor = {ConnectException.class, ArithmeticException.class, ResourceAccessException.class}, maxAttempts = 2, backoff = @Backoff(value = 2000))
    public String testAPIService() throws URISyntaxException, ConnectException {
        try {
            logger.info("Retry Number : " + RetrySynchronizationManager.getContext().getRetryCount()+" at time:"+ LocalDateTime.now());
//            int a = 8/0;
            RequestEntity<Object> request = new RequestEntity<>(HttpMethod.GET, new URI("http://localhost:8088/students"));
            restTemplate.exchange(request, String.class);
            return "calledAPI";
        }
       /* catch (URISyntaxException uex) {
            throw uex;
        }
        catch (ArithmeticException ax) {
            logger.info("ArithmeticException occurred");
            throw ax;
        }*/
        catch (Exception ex) {
            logger.info("other exception occurred. "+ex.getMessage());
            if(ex instanceof  ConnectException) {
                logger.info("throwing");
                throw new ConnectException();
            }
            if(ex instanceof ResourceAccessException) {
                logger.info("throwing resourceAccessException");
                throw new ResourceAccessException(ex.getMessage());
            }
        }
        return null;
    }

    @Recover
    public String recover(ConnectException e) throws URISyntaxException {
        logger.info("ConnectException recovered at:"+LocalDateTime.now());
        return "CouldNotCallAPI";
    }

    @Recover
    public String recover(ArithmeticException ex) {
        logger.info("ArithmeticException recovered at:"+LocalDateTime.now());
        return "CouldNotCallAPI";
    }

    @Recover
    public String recover(ResourceAccessException ex) {
        logger.info("ResourceAccessException recovered at:"+LocalDateTime.now());
        return "CouldNotCallAPI";
    }

}

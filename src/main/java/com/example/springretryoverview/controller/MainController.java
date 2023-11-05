package com.example.springretryoverview.controller;

import com.example.springretryoverview.service.MainService;
import com.example.springretryoverview.service.SecondService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.ConnectException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/")
public class MainController {

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MainService service;

    @Autowired
    private SecondService secondService;

    @GetMapping("testRetry")
    public String testRetry() throws URISyntaxException, ConnectException, InterruptedException {
        String first = service.testAPIService();
        logger.info(first);
        return first;
    }

    @GetMapping("testRetryWithTemplate")
    public String testRetryWithTemplate() throws URISyntaxException, ConnectException {
        String second = secondService.testAPIService("test1",0.1);
        logger.info(second);
        return second;
    }
}

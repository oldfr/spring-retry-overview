package com.example.springretryoverview.controller;

import com.example.springretryoverview.service.MainService;
import com.example.springretryoverview.service.SecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.ConnectException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/")
public class MainController {

    @Autowired
    private MainService service;

    @Autowired
    private SecondService secondService;

    @GetMapping("testRetry")
    public String testRetry() throws URISyntaxException, ConnectException {
        String first = service.testAPIService();
        System.out.println(first);
        return first;
    }

    @GetMapping("testRetryWithTemplate")
    public String testRetryWithTemplate() throws URISyntaxException, ConnectException {
        String second = secondService.testAPIService("test1",0.1);
        System.out.println(second);
        return second;
    }
}

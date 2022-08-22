package com.example.trades.producer.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomePageController {	
    private final Logger LOG = LoggerFactory.getLogger(HomePageController.class);
    
    @GetMapping("/")
    public String login(Model model) {
        LOG.info("Request received for index.html");
        String favicon = ("/images/favicon.ico");
        String logo = ("/images/logo.png");
        model.addAttribute("favicon", favicon);
        model.addAttribute("logo", logo);
        return "index";
    }
}
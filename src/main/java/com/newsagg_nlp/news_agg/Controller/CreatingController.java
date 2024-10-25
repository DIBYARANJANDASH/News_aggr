package com.newsagg_nlp.news_agg.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class CreatingController {
    @GetMapping("/hello")
    public String greet(){
        return "Hello";
    }

}

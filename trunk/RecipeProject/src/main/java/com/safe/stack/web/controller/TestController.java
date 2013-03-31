package com.safe.stack.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/")
@Controller
public class TestController {
    
    @RequestMapping(value = "/restCallConsumer", method = RequestMethod.POST)
    public String restCallConsumer(Object anXML) {
	System.out.println("-------------------------");
	System.out.println("anXML: " + String.valueOf(anXML));
	
	return  "recipe/list";
    }
    
    @RequestMapping(value = "/getSomething", method = RequestMethod.GET)
    public String getSomething(String anXML) {
	System.out.println("-------------------------");
	System.out.println("anXML: " + anXML);
	
	return "";
    }

}
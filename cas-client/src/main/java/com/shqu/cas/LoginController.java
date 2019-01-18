package com.shqu.cas;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    
    @RequestMapping(value="/index")
    public String index(){
        return "index";
    }
    
    @RequestMapping(value="/login")
    public String logout(){
        return "logout";
    }
    
}
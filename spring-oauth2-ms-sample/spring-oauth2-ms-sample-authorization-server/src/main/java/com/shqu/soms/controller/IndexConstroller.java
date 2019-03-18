package com.shqu.soms.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexConstroller {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView profile(Principal principal) {
		return new ModelAndView("index", "principal", principal);
	}

}

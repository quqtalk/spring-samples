package com.shqu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestController2 {

	@RequestMapping("/a")
	public String index() {
		return "this is a rest controller";
	}

}

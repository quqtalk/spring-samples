package com.shqu.soms.resource;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/helloworld")
public class HelloworldController {

	@RequestMapping("/hi")
	public String helloworld(Principal p) {
		return "hi " ;
	}

}

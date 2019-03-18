package com.shqu.soms.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * user-info-uri service will be called by oauth2 client to load the
 * authentication info.
 * 
 * @author shqu
 *
 */
@RestController
public class ProfileConstroller {

	@RequestMapping(value = "user/me", method = RequestMethod.GET)
	public Object profile(Principal p) {
		
		return p;
	}
}

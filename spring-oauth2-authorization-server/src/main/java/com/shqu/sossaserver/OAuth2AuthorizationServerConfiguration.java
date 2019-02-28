package com.shqu.soss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration

public class OAuth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Autowired
	// used for the authentication of password grant type
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("secret-key").secret("secret-value").authorizedGrantTypes("authorization_code",
				"client_credentials", "password", "implicit", "refresh_token").scopes("avatar", "email", "slogan")
				.redirectUris("http://www.baidu.com");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager);
	}
}

package com.shqu.cas;

import javax.servlet.http.HttpSessionEvent;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
public class CasSecurityConfig {
	// cas服务
	@Value("${cas.server.url:http://localhost:8080/cas}")
	private String casServerUrl;
	// The service must equal a URL that will be monitored by the
	// CasAuthenticationFilter
	@Value("${cas.service.url:http://localhost:9999/login/cas}")
	private String casServiceUrl;

	@Autowired
	UserDetailService userdetailService;

	@Bean
	public ServiceProperties serviceProperties() {
		// The CasAuthenticationEntryPoint must refer to the ServiceProperties bean
		// (discussed above), which provides the URL to the enterprise’s CAS login
		// server. This is where the user’s browser will be redirected.
		ServiceProperties serviceProperties = new ServiceProperties();
		// 本机服务，访问/login/cas时进行校验登录
		serviceProperties.setService(casServiceUrl);
		serviceProperties.setSendRenew(false);
		return serviceProperties;
	}

	@Bean
	@Primary
	public AuthenticationEntryPoint authenticationEntryPoint(ServiceProperties sP) {

		CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
		// cas登录服务
		entryPoint.setLoginUrl(casServerUrl + "/login");
		entryPoint.setServiceProperties(sP);
		return entryPoint;
	}

	@Bean
	public TicketValidator ticketValidator() {
		// 指定cas校验器
		return new Cas30ServiceTicketValidator(casServerUrl);
	}

	// cas认证
	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {

		CasAuthenticationProvider provider = new CasAuthenticationProvider();
		provider.setServiceProperties(serviceProperties());
		provider.setTicketValidator(ticketValidator());
		// 固定响应用户，在生产环境中需要额外设置用户映射
		provider.setUserDetailsService(userdetailService);
		provider.setKey("an_id_for_this_auth_provider_only");
		return provider;
	}

	@Bean
	public SecurityContextLogoutHandler securityContextLogoutHandler() {
		return new SecurityContextLogoutHandler();
	}

	@Bean
	public LogoutFilter logoutFilter() {
		// 退出后转发路径
		LogoutFilter logoutFilter = new LogoutFilter(casServerUrl + "/logout", securityContextLogoutHandler());
		// cas退出
		logoutFilter.setFilterProcessesUrl("/logout/cas");
		return logoutFilter;
	}

	@Bean
	public SingleSignOutFilter singleSignOutFilter() {
		// 单点退出
		SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setCasServerUrlPrefix(casServerUrl);
		singleSignOutFilter.setIgnoreInitConfiguration(true);
		return singleSignOutFilter;
	}

	// 设置退出监听
	@EventListener
	public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener(HttpSessionEvent event) {
		return new SingleSignOutHttpSessionListener();
	}
}
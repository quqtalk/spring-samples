This project demos how Spring Security SSO works. There are two components, one is autorization server, another is SSO client.

----------

How to run
===

1. Start the server project (on 8080) and client project (on 8081).
2. Access http://localhost:8081/helloworld from browser. This simulates accessing a resource when user are not sign in.
3. Browser will redirect to http://localhost:8080, ask username and password to sign into the authorization server. 
4. In the approval page, choose the scope and click Authorize button.
5. Authentication completed on client application 8081.


Behind the scene
===
1. There is a filter "OAuth2ClientContextFilter" added automatically when @EnableOauth2Sso annotation used. It is seprated from the springSecurityFilterChain. See <a href="#1">how</a>.
2. And, OAuth2AuthenticationProcessingFilter will be added into springSecurityFilterChain. See <a href="#2">how</a>.
3. When unauthenticated user access resource of client application, there will be a AccessDeniedException thrown by AccessDecisionManager.decide() in FilterSecurityInterceptor.
4. ExceptionTranslationFilter will handle AccessDeniedException, AuthenticationEntryPoint will commence the authentication.
5. OAuth2ClientAuthenticationProcessingFilter.attemptAuthentication() will try to do authentication, which is try to get the access token via OAuth2RestOperations.getAccessToken().
6. Since there is no access token can be retireved at this point, a UserRedirectRequiredException will be thrown.
7. OAuth2ClientContextFilter will handle UserRedirectRequiredException, which redirect use to the authorization server "http://localhost:8080/oauth/authorize?client_id=secret-key&redirect_uri=http://localhost:8081/login&response_type=code".
8. User sign into authorization server via browser, then browser redirect back to client application with authroization code and state query parameters.
9. OAuth2ClientAuthenticationProcessingFilter intercepts the request again, and post to "http://localhost:8080/oauth/authorize" to apply access token.
10. Once get token, OAuth2Authentication is contructed via ResourceServerTokenServices.loadAuthentication().
11. Successful authentication logic will be trigger, a good athentication object is set to SecurityContextHolder in AbstractAuthenticationProcessingFilter.successfulAuthentication().

Step 7, 8 and 9 just worked as the general OAuth2 authorization_code grant type progress.

<a name="1">How OAuth2ClientContextFilter is enabled?</a>
===
**There is a @configuration register the OAuth2ClientContextFilter.**
* @Configuration
    + @ConditionalOnClass(EnableOAuth2Client.class)
    + OAuth2RestOperationsConfiguration.SessionScopedConfiguration.oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter, SecurityProperties security) 

**Below is where the OAuth2ClientContextFilter come.**
* @EnableOAuth2Sso
    + @EnableOAuth2Client
    + @Import(OAuth2ClientConfiguration.class)
    + o.s.s.oauth2.config.annotation.web.configuration.OAuth2ClientConfiguration.oauth2ClientContextFilter()

<a name="2">How OAuth2ClientAuthenticationProcessingFilter is enabled?</a>
===
* @EnableOAuth2Sso
    + @Import OAuth2SsoCustomConfiguration.class
    + OAuth2SsoCustomConfiguration.postProcessAfterInitialization.(Object bean, String beanName)
    + SsoSecurityAdapter.invoke(MethodInvocation invocation)
    + SsoSecurityConfigurer.configure(HttpSecurity http)
    + OAuth2ClientAuthenticationConfigurer.configure(HttpSecurity builder)
    + HttpSecurity.addFilterAfter(OAuth2ClientAuthenticationProcessingFilter filter,AbstractPreAuthenticatedProcessingFilter.class);
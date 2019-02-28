This project demos how Spring Security SSO work. There are two components, one is autorization server, another is SSO client.

----------

How to run
===

1. Start the server project (on 8080) and client project (on 8081).
2. Access http://localhost:8081/helloworld from browser. This simulates access a resource when user are not sign in.
3. Browser will redirect to http://localhost:8080, ask username and password to sign into the authorization server. 
4. In the approval page, choose the scope and click Authorize button.
5. Authentication completed on client application 8081.


Behind the scene
===
1. There is a filter "OAuth2ClientContextFilter" added automatically when @EnableOauth2Sso annotation used. It is seprated from the springSecurityFilterChain.
2. And, OAuth2AuthenticationProcessingFilter will be added into springSecurityFilterChain.
3. When unauthenticated user access resource of client application, there will be a AccessDeniedException thrown by AccessDecisionManager.decide() in FilterSecurityInterceptor.
4. ExceptionTranslationFilter will handle AccessDeniedException, AuthenticationEntryPoint will commence the authentication.
5. OAuth2ClientAuthenticationProcessingFilter.attemptAuthentication() will try to do authentication, which is try to get the access token via OAuth2RestOperations.getAccessToken().
6. Since there is no access token can be retireved at this point, a UserRedirectRequiredException will be thrown.
7. OAuth2ClientContextFilter will handle UserRedirectRequiredException, which redirect use to the authorization server "http://localhost:8080/oauth/authorize?client_id=secret-key&redirect_uri=http://localhost:8081/login&response_type=code".
8. User sign into authorization server via browser, athorization redirect back with authroization code and state query parameters.
9. OAuth2ClientAuthenticationProcessingFilter intercepts the request again, and post to "http://localhost:8080/oauth/authorize" to apply access token.
10. Once get token, OAuth2Authentication is contructed via ResourceServerTokenServices.loadAuthentication().
11. Successful authentication will be set into SecurityContextHolder in AbstractAuthenticationProcessingFilter.successfulAuthentication().



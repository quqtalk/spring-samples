Demos standalone OAuth2 server started via Spring Security and Spring Security OAuth2.

Run the project as Spring boot application. Below try different grant types.

PS: parameters such as username, password, client_id, secret, etc. were hard coded both in code and below description.

# 1. authorization_code
1. Access "http://localhost:8080/oauth/authorize?response_type=code&client_id=secret-key&redirect_uri=http://www.baidu.com" in browser.
2. Enter the username and password as "user:123456".
3. Check the scope may grant, then click Authorize button.
4. Get the "code" from the redirect url.
5. Execute in terminal:
```bash
curl -u secret-key:secret-value "http://localhost:8080/oauth/token?grant_type=authorization_code&code=1tyoKe&redirect_uri=http://www.baidu.com" -X POST
```
5. Access token will be reponsed.
```json
{
    "access_token": "f48ac369-8a02-43de-8012-ceaf96599b97", 
    "token_type": "bearer", 
    "refresh_token": "1238746f-59e2-47c1-8564-53b2373d913f", 
    "expires_in": 43088, 
    "scope": "avatar slogan email"
}
```

# 2. implicit
1. Access "http://localhost:8080/oauth/authorize?response_type=token&client_id=secret-key&redirect_uri=http://www.baidu.com" in browser.
2. Enter the username and password as "user:123456".
3. The access token will be in the redirect url, such as:
https://www.baidu.com/#access_token=f48ac369-8a02-43de-8012-ceaf96599b97&token_type=bearer&expires_in=42603&scope=avatar%20slogan%20email

# 3. password

1. Execute in terminal:
```bash
curl -u secret-key:secret-value "http://localhost:8080/oauth/token?grant_type=password&username=user&password=123456" -X POST
```
2.  Access token will be reponsed.
```json
{
	"access_token": "f48ac369-8a02-43de-8012-ceaf96599b97",
	"token_type": "bearer",
	"refresh_token": "1238746f-59e2-47c1-8564-53b2373d913f",
	"expires_in": 42496,
	"scope": "avatar slogan email"
}
```


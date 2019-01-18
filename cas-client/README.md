Spring Security CAS Integration Sample
====
This project demonstrates how to integrate Spring Security with [Apereo CAS](https://www.apereo.org/projects/cas).

#Environments
>Windows 10 x64
>JDK 1.8.0_192
>apache-maven-3.6.0
>apache-tomcat-9.0.14
>CAS 5.3

#Setps
*Build [CAS](https://github.com/apereo/cas-overlay-template/tree/5.3), clone the code and run 
```bash
./build.cmd package
```
*Deploy cas.war to tomcat.
*Change CAS configuration to skip the https restriction (test purpose only)
    >Edit WEB-INF\classes\application.properties, add following line
    ```bash
    cas.tgc.secure=false
    cas.serviceRegistry.initFromJson=true
    ```
    >Edit WEB-INF\classes\services\HTTPSandIMAPS-10000001.json, change the line 
    ```bash
    "serviceId" : "^(https|imaps)://.*",
    ```
    >to
    ```bash
    "serviceId" : "^(https|http|imaps)://.*",
    ```
*Start tomcat, default port is 8080.
*Run this Spring Boot, default port is 9999.
*Access http://localhost:9999/index, browser will redirect to CAS login page.
*Use casuser, password: Mellon to login, browser redirect to home page.
*Click logout to see the Single Sign Out.
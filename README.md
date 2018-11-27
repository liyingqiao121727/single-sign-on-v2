# single-sign-on-v2
Be based on Apereo(Jasig) cas 4.2+, the project can return sso client's login page or json (some sso clients' theme may be different with others, or some sso clients is C/S mode need return json by sso server), by setting the login url on sso client.
(this version is 2.0. you can visit url https://github.com/liyingqiao121727/single-sign-on-v1 for 1.0 version. the new version support the web-server cluster mode by override some code of spring-webflow)

Steps: 

1.Import it to eclipse;

2.Need CAS clients. (configuration is same with Apereo CAS(4.2), make sure the CAS client can conmmunicate with Apereo CAS(4.2));

3.modify the CAS clients login url (For example: When CAS client communicate with Apereo CAS(4.2), the login url You config is "http://cas_server_domain/cas?service=http://cas_client_domain/xxxxxxx", change the url by append "&mode=browser&subpath=login");

4.new url contains 'service', 'mode' and 'subpath', 'service': the same function with Apereo CAS(4.2), 'mode': if mode = client, the CAS server will return json (may be contain some html tags, you can remove them easily), if mode != client, the CAS server will return to you the CAS client login page; 'subpath': the 'service' contains 'http://' and domainName and append '/' and append subpath is CAS client login page with http post.

5.The CAS server will request your login url with some request parameters (such as 'lt', 'execution', etc.). put them to login page in form. the form is needed:

< form method="post" > < input type="text" id="username" name="username" /> < input type="password" id="password" name="password" /> < input type="hidden" name="lt" value="<%=request.getParameter("lt")%>" /> < input type="hidden" name="execution" value="<%=request.getParameter("execution")%>" /> < input type="hidden" name="_eventId" value="submit" /> < input type="hidden" name="warn" value="true" / or false /> </ form>

6.The login page's css, image, js etc. the url of them is your CAS client's resource.

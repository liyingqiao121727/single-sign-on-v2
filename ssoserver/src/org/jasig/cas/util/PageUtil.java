package org.jasig.cas.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.Credential;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.mvc.view.BindingModel;

import com.alibaba.fastjson.JSON;

/**
 * Associate with Page
 * 
 * @author Scott Battaglia
 * @since 3.1
 */
public class PageUtil {

	public static final String SUB_PATH = "subpath"; 

	private static String getUrl(HttpServletRequest request) {
		String service = request.getParameter("service");
		if (null == service || "" == service) {
			return null;
		}
		String[] serviceSplit = service.split("/");
		if (serviceSplit.length < 3) {
			return null;
		}
		String subPath = request.getParameter(SUB_PATH);
		return serviceSplit[0] + "/" + serviceSplit[1] + "/" + 
		serviceSplit[2] +"/" +  subPath;
	}
	
	private static String getParamters(HttpServletRequest request) {
		BindingModel bm = (BindingModel) request.getAttribute(
				"org.springframework.validation.BindingResult.credential");
		List<ObjectError> oes = bm.getAllErrors();
		String errors = "";
		if (oes != null && !oes.isEmpty()) {
			errors = "&errors=" + JSON.toJSONString(oes);
		}
		
		Credential credential = (Credential) request.getAttribute("credential");
		String username = credential.getId();
		if (username != null && username != "") {
			username = "&username=" + username;
		} else {
			username = "";
		}
		return "lt=" + (String) request.getAttribute("loginTicket") + "&execution=" + 
		(String) request.getAttribute("flowExecutionKey") + "&_eventId=submit&warn=true" + 
		errors + username;
	}

	public static String getPage(HttpServletRequest req) {
		String url = getUrl(req);
		if (null == url) {
			return null;
		}
		String parms = getParamters(req);

		if (url.startsWith("https")) {
			return getHttpsPage(url, parms);
		} else {
			return getHttpPage(url, parms);
		}
	}

	private static String getHttpsPage(String url, String parms) {
		SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] {
					new X509TrustManager() {

						@Override
						public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

						}

						@Override
						public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

						}

						@Override
						public X509Certificate[] getAcceptedIssuers() {
							return null;
						}

					}
			}, new SecureRandom());
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder()
				.connectTimeout(java.time.Duration.ofMillis(5000))
				.followRedirects(java.net.http.HttpClient.Redirect.NORMAL).sslContext(ctx)
				.build();
		//2.set read timeout
		java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
				.uri(java.net.URI.create(url))
				.header("Content-Type","application/x-www-form-urlencoded;charset=utf-8")
				.POST(java.net.http.HttpRequest.BodyPublishers.ofString(parms))
				.timeout(java.time.Duration.ofMillis(5009))
				.build();

		java.net.http.HttpResponse<String> response;
		try {
			response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
			return response.body();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String getHttpPage(String url, String parms) {
		java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder()
				.connectTimeout(java.time.Duration.ofMillis(5000))
				.followRedirects(java.net.http.HttpClient.Redirect.NORMAL)
				.build();

		//String data;  .POST(java.net.http.HttpRequest.BodyPublishers.ofString(data))
		//2.set read timeout
		java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
				.uri(java.net.URI.create(url))
				.header("Content-Type","application/x-www-form-urlencoded;charset=utf-8")
				.POST(java.net.http.HttpRequest.BodyPublishers.ofString(parms))
				.timeout(java.time.Duration.ofMillis(5009))
				.build();

		java.net.http.HttpResponse<String> response;
		try {
			response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
			return response.body();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) {
		System.out.println(getHttpPage("https://www.baidu.com", ""));
	}

}
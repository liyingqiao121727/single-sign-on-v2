package com.liyingqiao;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class PageUtil {
	private static Set<String> LOGIN_PAGE_SET= new HashSet<String>(); 

	static {
		LOGIN_PAGE_SET.add("demo1");
		LOGIN_PAGE_SET.add("demo2");
	}

	public static boolean contains(String key) {
		return LOGIN_PAGE_SET.contains(key);
	}

	public static String getPage() {

		/*System.out.println("-------------华丽的分割线----------------");
		Enumeration<String> as = request.getAttributeNames();
		String s = null;
		while (as.hasMoreElements()) {
			s = as.nextElement();
			Object o = request.getAttribute(s);
			System.err.println("attribute: " + s + " : " + request.getAttribute(s));
		}*/
		/*System.out.println("-------------华丽的分割线----------------");
		Enumeration<String> as1 = request.getParameterNames();
		while (as1.hasMoreElements()) {
			s = as1.nextElement();
			System.err.println("Parameter: " + s + " : " + request.getParameter(s));
		}*/
		
		HttpClient client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofMillis(5000))
				.followRedirects(HttpClient.Redirect.NORMAL)
				.build();

		//2.set read timeout
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://www.baidu.com/"))
				.timeout(Duration.ofMillis(5009)).POST(BodyPublishers.ofString(""))
				.build();

		java.net.http.HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			return response.body();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getPage());
	}
}
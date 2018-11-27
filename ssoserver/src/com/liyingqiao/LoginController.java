package com.liyingqiao;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

	@RequestMapping(path = {"/liyingqiao"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String login() {
		return "casLoginView";
	}
	
	@RequestMapping(path = {"/hello"}, method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String hello() {
		return "hello";
	}
	
}

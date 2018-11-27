<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="org.springframework.webflow.mvc.view.BindingModel"%>
<!DOCTYPE html>
<!-- saved from url=(0067)http://www.17sucai.com/preview/668095/2017-07-19/perfect/index.html -->
<html lang="en" class="no-js">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>login</title>
<link rel="stylesheet" type="text/css" href="/cas/css/index/normalize.css">
<link rel="stylesheet" type="text/css" href="/cas/css/index/demo.css">
<link rel="stylesheet" type="text/css" href="/cas/css/index/component.css">

<!--[if IE]>
<script src="js/html5.js"></script>
<![endif]-->
</head>
<body>
	<div class="container demo-1">
		<div class="content">
			<div id="large-header" class="large-header" style="height: 559px;">
				<canvas id="demo-canvas" width="1366" height="559"></canvas>
				<div class="logo_box">
					<h3>Be Based On Apereo CAS</h3>
					<form id="form" name="f" method="post">
					    
						<div class="input_outer">
							<span class="u_user"></span> 
							<input name="username" class="text" style="color: #FFFFFF !important" 
							type="text" placeholder="username is needed" autocomplete="off" 
							id="username" onclick="document.getElementById('userspan').style.visibility='hidden'" />
							<span id="userspan" class="tooltiptext">username</span>
						</div>
						<div class="input_outer">
							<span class="us_uer"></span> 
							<input name="password" class="text" style="color: #FFFFFF !important;" 
							value="" type="password"  placeholder="please input password" 
							id="pwd" onclick="document.getElementById('pwdspan').style.visibility='hidden'" />
							<span id="pwdspan" class="tooltiptext">password</span>
						</div>

						<input type="hidden" name="lt" value="${loginTicket}" /> 
						<input type="hidden" name="execution" value="${flowExecutionKey}" /> 
						<input type="hidden" name="_eventId" value="submit" />
						<div class="mb2">
						<a class="act-but submit" onclick="return validate();" href="javascript:document.getElementById('form').submit();" style="color: #FFFFFF">Login</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

</body>

	<!-- /container -->
	<script src="/cas/js/index/TweenLite.min.js"></script>
	<script src="/cas/js/index/EasePack.min.js"></script>
	<script src="/cas/js/index/rAF.js"></script>
	<script src="/cas/js/index/demo-1.js"></script>
	<script type="text/javascript">
	    var errors = '<%=JSON.toJSONString(((BindingModel) request.getAttribute(
			"org.springframework.validation.BindingResult.credential")).getAllErrors()) %>';
	    load();
	    function load() {
	    	if (errors != 'null' && errors != '[]') {
	    		//errors = window.atob(errors);
	    		var userspan = document.getElementById("userspan");
	    		userspan.innerText = errors;
	    		userspan.style.visibility='visible';
	    	    errors = '';
	    	}
	    }
	    
	    function validate() {
	    	var username = document.getElementById("username");
	    	if (username.value == null || username.value == '') {
	    		var userspan = document.getElementById("userspan");
	    		userspan.innerText = 'please write the username.';
	    		userspan.style.visibility='visible';
	    		return false;
	    	}
	    	var password = document.getElementById("pwd");
	    	if (password.value == null || password.value == '') {
	    		var pwdspan = document.getElementById("pwdspan");
	    		pwdspan.innerText = 'please write the password.';
	    		pwdspan.style.visibility='visible';
	    		return false;
	    	}
	    	
	    	return true;
	    }
	</script>
</html>

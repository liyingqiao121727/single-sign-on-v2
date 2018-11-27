<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${mode != 'client'}">
		<%@page import="org.jasig.cas.util.PageUtil"%>
		<%
		    String jspPage = PageUtil.getPage(request);
		   	if (null != jspPage) {
				out.print(jspPage);
			}
		%>
		<c:choose>
		<c:when test="<%=null == jspPage%>">
			<jsp:include page="casLoginView1.jsp" />
		</c:when>
		</c:choose>
	</c:when>
	<c:otherwise>
	    <%@page import="org.springframework.webflow.mvc.view.BindingModel"%>
	    <%@page import="java.util.List"%>
	    <%@page import="org.springframework.validation.ObjectError"%>
	    <%@page import="com.alibaba.fastjson.JSON"%>
	    <%@page import="org.jasig.cas.authentication.Credential"%>
		<%
			response.setContentType("application/json;charset=utf-8");//指定返回的格式为JSON格式
			//setContentType与setCharacterEncoding的顺序不能调换，否则还是无法解决中文乱码的问题
			response.setCharacterEncoding("UTF-8");
			
			BindingModel bm = (BindingModel) request.getAttribute(
					"org.springframework.validation.BindingResult.credential");
			List<ObjectError> oes = bm.getAllErrors();
			String errors = "";
			if (oes != null && !oes.isEmpty()) {
				errors = ", \"errors\" : \"" + JSON.toJSONString(oes) + "\"";
			}
			Credential credential = (Credential) request.getAttribute("credential");
			String username = credential.getId();
			if (username != null && username != "") {
				username = ", \"username\" : \"" + username + "\"";
			} else {
				username = "";
			}
			out.print("{" + " \"service\": \"" + request.getParameter("service") + "\"," + " \"loginTicket\":\""
					+ request.getAttribute("loginTicket") + "\"," + " \"flowExecutionKey\":\""
					+ request.getAttribute("flowExecutionKey") + "\","
					+ " \"_eventId\":\"submit\", \"warn\":true," + " \"action\":\""
					+ "https://localhost/cas/login?" + request.getQueryString() + "\"" 
					+ errors + username + " }");
		%>
	</c:otherwise>
</c:choose>
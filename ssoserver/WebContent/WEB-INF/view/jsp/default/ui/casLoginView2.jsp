<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${mode != 'client'}">
		<%@page import="org.jasig.cas.util.PageUtil"%>
		<%
		    String path = request.getParameter(PageUtil.SUB_PATH);
		   	if (null == path || "" == path) {
				path = "casLoginView1.jsp";
			} else {
				path = "../../page/" + path;
			}
		%>
		<jsp:include page="<%=path %>" />

	</c:when>
	<c:otherwise>
		<%
			response.setContentType("application/json;charset=utf-8");//指定返回的格式为JSON格式
			//setContentType与setCharacterEncoding的顺序不能调换，否则还是无法解决中文乱码的问题
			response.setCharacterEncoding("UTF-8");

			out.print("{" + " \"service\": \"" + request.getParameter("service") + "\"," + " \"loginTicket\":\""
					+ request.getAttribute("loginTicket") + "\"," + " \"flowExecutionKey\":\""
					+ request.getAttribute("flowExecutionKey") + "\","
					+ " \"_eventId\":\"submit\", \"warn\":true," + " \"action\":\""
					+ "https://localhost/cas/login?" + request.getQueryString() + "\"" + "}");
		%>
	</c:otherwise>
</c:choose>
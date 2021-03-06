<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h1> hello world demo1</h1>
<c:if test="${not pageContext.request.secure}">
  <div id="msg" class="errors">
    <h2>Non-secure Connection</h2>
    <p>You are currently accessing CAS over a non-secure connection.  Single Sign On WILL NOT WORK.  In order to have single sign on work, you MUST log in over HTTPS.</p>
  </div>
</c:if>

<div class="box" id="login">
  <form:form method="post" id="fm1" commandName="${commandName}" htmlEscape="true">

    <form:errors path="*" id="msg" cssClass="errors" element="div" htmlEscape="false" />
  
    <h2><spring:message code="screen.welcome.instructions" /></h2>
  
    <section class="row">
      <label for="username"><spring:message code="screen.welcome.label.netid" /></label>
      <c:choose>
        <c:when test="${not empty sessionScope.openIdLocalId}">
          <strong>${sessionScope.openIdLocalId}</strong>
          <input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
        </c:when>
        <c:otherwise>
          <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
          <form:input cssClass="required" cssErrorClass="error" id="username" size="25" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="off" htmlEscape="true" />
        </c:otherwise>
      </c:choose>
    </section>
    
    <section class="row">
      <label for="password"><spring:message code="screen.welcome.label.password" /></label>
      <%--
      NOTE: Certain browsers will offer the option of caching passwords for a user.  There is a non-standard attribute,
      "autocomplete" that when set to "off" will tell certain browsers not to prompt to cache credentials.  For more
      information, see the following web page:
      http://www.technofundo.com/tech/web/ie_autocomplete.html
      --%>
      <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
      <form:password cssClass="required" cssErrorClass="error" id="password" size="25" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
    </section>
    
    <section class="row check">
      <input id="warn" name="warn" value="true" tabindex="3" accesskey="<spring:message code="screen.welcome.label.warn.accesskey" />" type="checkbox" />
      <label for="warn"><spring:message code="screen.welcome.label.warn" /></label>
    </section>
    
    <section class="row btn-row">
      <input type="hidden" name="lt" value="${loginTicket}" />
      <input type="hidden" name="execution" value="${flowExecutionKey}" />
      <input type="hidden" name="_eventId" value="submit" />

      <input class="btn-submit" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="4" type="submit" />
      <input class="btn-reset" name="reset" accesskey="c" value="<spring:message code="screen.welcome.button.clear" />" tabindex="5" type="reset" />
    </section>
  </form:form>
</div>
  
<div id="sidebar">
  <div class="sidebar-content">
    <p><spring:message code="screen.welcome.security" /></p>
    
    <div id="list-languages">
      <%final String queryString = request.getQueryString() == null ? "" : request.getQueryString().replaceAll("&locale=([A-Za-z][A-Za-z]_)?[A-Za-z][A-Za-z]|^locale=([A-Za-z][A-Za-z]_)?[A-Za-z][A-Za-z]", "");%>
      <c:set var='query' value='<%=queryString%>' />
      <c:set var="xquery" value="${fn:escapeXml(query)}" />
      
      <h3>Languages:</h3>
      
      <c:choose>
        <c:when test="${not empty requestScope['isMobile'] and not empty mobileCss}">
          <form method="get" action="login?${xquery}">
            <select name="locale">
              <option value="en">English</option>
              <option value="es">Spanish</option>
              <option value="fr">French</option>
              <option value="ru">Russian</option>
              <option value="nl">Nederlands</option>
              <option value="sv">Svenska</option>
              <option value="it">Italiano</option>
              <option value="ur">Urdu</option>
              <option value="zh_CN">Chinese (Simplified)</option>
              <option value="zh_TW">Chinese (Traditional)</option>
              <option value="de">Deutsch</option>
              <option value="ja">Japanese</option>
              <option value="hr">Croatian</option>
              <option value="cs">Czech</option>
              <option value="sl">Slovenian</option>
              <option value="pl">Polish</option>
              <option value="ca">Catalan</option>
              <option value="mk">Macedonian</option>
              <option value="fa">Farsi</option>
              <option value="ar">Arabic</option>
              <option value="pt_PT">Portuguese</option>
              <option value="pt_BR">Portuguese (Brazil)</option>
            </select>
            <input type="submit" value="Switch">
          </form>
        </c:when>
        <c:otherwise>
          <c:set var="loginUrl" value="login?${xquery}${not empty xquery ? '&' : ''}locale=" />
          <ul>
            <li class="first"><a href="${loginUrl}en">English</a></li>
            <li><a href="${loginUrl}es">Spanish</a></li>
            <li><a href="${loginUrl}fr">French</a></li>
            <li><a href="${loginUrl}ru">Russian</a></li>
            <li><a href="${loginUrl}nl">Nederlands</a></li>
            <li><a href="${loginUrl}sv">Svenska</a></li>
            <li><a href="${loginUrl}it">Italiano</a></li>
            <li><a href="${loginUrl}ur">Urdu</a></li>
            <li><a href="${loginUrl}zh_CN">Chinese (Simplified)</a></li>
            <li><a href="${loginUrl}zh_TW">Chinese (Traditional)</a></li>
            <li><a href="${loginUrl}de">Deutsch</a></li>
            <li><a href="${loginUrl}ja">Japanese</a></li>
            <li><a href="${loginUrl}hr">Croatian</a></li>
            <li><a href="${loginUrl}cs">Czech</a></li>
            <li><a href="${loginUrl}sl">Slovenian</a></li>
            <li><a href="${loginUrl}ca">Catalan</a></li>
            <li><a href="${loginUrl}mk">Macedonian</a></li>
            <li><a href="${loginUrl}fa">Farsi</a></li>
            <li><a href="${loginUrl}ar">Arabic</a></li>
            <li><a href="${loginUrl}pt_PT">Portuguese</a></li>
            <li><a href="${loginUrl}pt_BR">Portuguese (Brazil)</a></li>
            <li class="last"><a href="${loginUrl}pl">Polish</a></li>
          </ul>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</div>
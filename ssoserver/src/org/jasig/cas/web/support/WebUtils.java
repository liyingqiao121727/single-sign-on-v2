/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.web.support;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.authentication.principal.WebApplicationService;
import org.jasig.cas.jedis.RedisManagement;
import org.jasig.cas.logout.LogoutRequest;
import org.springframework.util.Assert;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import redis.clients.jedis.JedisCommands;

/**
 * Common utilities for the web tier.
 *
 * @author Scott Battaglia

 * @since 3.1
 */
public final class WebUtils {

    private WebUtils() {}

    /** Request attribute that contains message key describing details of authorization failure.*/
    public static final String CAS_ACCESS_DENIED_REASON = "CAS_ACCESS_DENIED_REASON";

    public static HttpServletRequest getHttpServletRequest(
        final RequestContext context) {
        Assert.isInstanceOf(ServletExternalContext.class, context
            .getExternalContext(),
            "Cannot obtain HttpServletRequest from event of type: "
                + context.getExternalContext().getClass().getName());

        return (HttpServletRequest) context.getExternalContext().getNativeRequest();
    }

    public static HttpServletResponse getHttpServletResponse(
        final RequestContext context) {
        Assert.isInstanceOf(ServletExternalContext.class, context
            .getExternalContext(),
            "Cannot obtain HttpServletResponse from event of type: "
                + context.getExternalContext().getClass().getName());
        return (HttpServletResponse) context.getExternalContext()
            .getNativeResponse();
    }

    public static WebApplicationService getService(
        final List<ArgumentExtractor> argumentExtractors,
        final HttpServletRequest request) {
        for (final ArgumentExtractor argumentExtractor : argumentExtractors) {
            final WebApplicationService service = argumentExtractor
                .extractService(request);

            if (service != null) {
                return service;
            }
        }

        return null;
    }

    public static WebApplicationService getService(
        final List<ArgumentExtractor> argumentExtractors,
        final RequestContext context) {
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        return getService(argumentExtractors, request);
    }

    public static WebApplicationService getService(
        final RequestContext context) {
        return (WebApplicationService) context.getFlowScope().get("service");
    }

    public static void putTicketGrantingTicketInRequestScope(
        final RequestContext context, final String ticketValue) {
        context.getRequestScope().put("ticketGrantingTicketId", ticketValue);
        setRedis(context, ticketValue);
    }

    public static void putTicketGrantingTicketInFlowScope(
        final RequestContext context, final String ticketValue) {
        context.getFlowScope().put("ticketGrantingTicketId", ticketValue);
        setRedis(context, ticketValue);
    }
    
    private static void setRedis(final RequestContext context, String ticketValue) {
    	HttpServletRequest request = getHttpServletRequest(context);
        if (null == request) {
			return ;
		}
		String sessionId = request.getRequestedSessionId();
		if (null == sessionId) {
			HttpSession session = request.getSession(false);
			if (session == null) {
				return ;
			}
			sessionId = session.getId();
		}
		
		JedisCommands jedisCommands = RedisManagement.getJedisCommands();
		jedisCommands.set(sessionId, ticketValue);
		try {
			RedisManagement.close(jedisCommands);
		} catch (IOException e) {
			//e.printStackTrace();
		}
    }

    public static String getTicketGrantingTicketId(
        final RequestContext context) {
    	String ticket = (String) context.getRequestScope().get("ticketGrantingTicketId");
    	if (null != ticket) {
    		return ticket;
    	}
		ticket = (String) context.getFlowScope().get("ticketGrantingTicketId");
		if (null != ticket) {
			return ticket;
		}
		/*HttpServletRequest request = getHttpServletRequest(context);
		if (null == request) {
			return null;
		}
		String sessionId = request.getRequestedSessionId();
		if (null == sessionId) {
			HttpSession session = request.getSession(false);
			if (session == null) {
				return null;
			}
			sessionId = session.getId();
		}
		
		JedisCommands jedisCommands = RedisManagement.getJedisCommands();
		ticket = jedisCommands.get(sessionId);
		try {
			RedisManagement.close(jedisCommands);
		} catch (IOException e) {
			//e.printStackTrace();
		}*/
		
        return ticket;
    }

    public static void putServiceTicketInRequestScope(
        final RequestContext context, final String ticketValue) {
        context.getRequestScope().put("serviceTicketId", ticketValue);
    }

    public static String getServiceTicketFromRequestScope(
        final RequestContext context) {
        return context.getRequestScope().getString("serviceTicketId");
    }

    public static void putLoginTicket(final RequestContext context, final String ticket) {
        context.getFlowScope().put("loginTicket", ticket);
    }

    public static String getLoginTicketFromFlowScope(final RequestContext context) {
        // Getting the saved LT destroys it in support of one-time-use
        // See section 3.5.1 of http://www.jasig.org/cas/protocol
        final String lt = (String) context.getFlowScope().remove("loginTicket");
        return lt != null ? lt : "";
    }

    public static String getLoginTicketFromRequest(final RequestContext context) {
       return context.getRequestParameters().get("lt");
    }

    public static void putLogoutRequests(final RequestContext context, final List<LogoutRequest> requests) {
        context.getFlowScope().put("logoutRequests", requests);
    }

    @SuppressWarnings("unchecked")
	public static List<LogoutRequest> getLogoutRequests(final RequestContext context) {
        return (List<LogoutRequest>) context.getFlowScope().get("logoutRequests");
    }
}
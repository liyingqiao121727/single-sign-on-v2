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
package org.jasig.cas.authentication.principal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of a WebApplicationService.
 *
 * @author Scott Battaglia
 * @since 3.1
 */
public abstract class AbstractWebApplicationService implements SingleLogoutService {

    private static final long serialVersionUID = 1L;

    /** Logger instance. **/
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractWebApplicationService.class);

    private static final Map<String, Object> EMPTY_MAP = Collections.unmodifiableMap(new HashMap<String, Object>());
    /** The id of the service. */
    private final String id;

    /** The original url provided, used to reconstruct the redirect url. */
    private final String originalUrl;

    private final String artifactId;

    private Principal principal;

    private boolean loggedOutAlready = false;

    protected AbstractWebApplicationService(final String id, final String originalUrl,
            final String artifactId) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.artifactId = artifactId;
    }

    public final String toString() {
        return this.id;
    }

    public final String getId() {
        return this.id;
    }

    public final String getArtifactId() {
        return this.artifactId;
    }

    public final Map<String, Object> getAttributes() {
        return EMPTY_MAP;
    }

    protected static String cleanupUrl(final String url) {
        if (url == null) {
            return null;
        }

        final int jsessionPosition = url.indexOf(";jsession");

        if (jsessionPosition == -1) {
            return url;
        }

        final int questionMarkPosition = url.indexOf("?");

        if (questionMarkPosition < jsessionPosition) {
            return url.substring(0, url.indexOf(";jsession"));
        }

        return url.substring(0, jsessionPosition)
            + url.substring(questionMarkPosition);
    }

    /**
     * Return the original url provided (as <code>service</code> or <code>targetService</code> request parameter).
     * Used to reconstruct the redirect url.
     *
     * @return the original url provided.
     */
    public final String getOriginalUrl() {
        return this.originalUrl;
    }

    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }

        if (object instanceof Service) {
            final Service service = (Service) object;

            return getId().equals(service.getId());
        }

        return false;
    }

    public int hashCode() {
        final int prime = 41;
        int result = 1;
        result = prime * result
            + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    protected Principal getPrincipal() {
        return this.principal;
    }

    public void setPrincipal(final Principal principal) {
        this.principal = principal;
    }

    public boolean matches(final Service service) {
        return this.id.equals(service.getId());
    }

    /**
     * Return if the service is already logged out.
     *
     * @return if the service is already logged out.
     */
    public boolean isLoggedOutAlready() {
        return loggedOutAlready;
    }

    /**
     * Set if the service is already logged out.
     *
     * @param loggedOutAlready if the service is already logged out.
     */
    public final void setLoggedOutAlready(final boolean loggedOutAlready) {
        this.loggedOutAlready = loggedOutAlready;
    }
}

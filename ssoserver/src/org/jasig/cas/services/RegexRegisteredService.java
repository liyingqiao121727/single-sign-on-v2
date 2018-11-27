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
package org.jasig.cas.services;

import org.jasig.cas.authentication.principal.Service;

import java.util.regex.Pattern;

/**
 * Mutable registered service that uses Java regular expressions for service matching.
 *
 * @author Marvin S. Addison
 */
public class RegexRegisteredService extends AbstractRegisteredService {

    private static final long serialVersionUID = 1L;

    private transient Pattern servicePattern;

    public void setServiceId(final String id) {
        serviceId = id;
    }

    public boolean matches(final Service service) {
        if (servicePattern == null) {
            servicePattern = createPattern(serviceId);
        }
        return service != null && servicePattern.matcher(service.getId()).matches();
    }

    protected AbstractRegisteredService newInstance() {
        return new RegexRegisteredService();
    }

    private Pattern createPattern(final String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null.");
        }
        return Pattern.compile(pattern);
    }
}

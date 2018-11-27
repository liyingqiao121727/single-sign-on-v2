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
package org.jasig.cas.authentication.support;

import org.jasig.cas.Message;

/**
 * Message conveying account password expiration warning details.
 *
 * @author Misagh Moayyed
 * @author Marvin S. Addison
 * @since 4.0
 */
public class PasswordExpiringWarningMessage extends Message {
    /** Serialization version marker. */
    private static final long serialVersionUID = 1L;

    /** Message bundle code. */
    private static final String CODE = "password.expiration.warning";

    /**
     * Creates a new instance.
     *
     * @param defaultMsg  Default warning message.
     * @param days Days to password expiration.
     * @param passwordChangeUrl Password change URL.
     */
    public PasswordExpiringWarningMessage(final String defaultMsg, final long days, final String passwordChangeUrl) {
        super(CODE, defaultMsg, days, passwordChangeUrl);
    }

    public long getDaysToExpiration() {
        return (Long) getParams()[0];
    }

    public String getPasswordChangeUrl() {
        return (String) getParams()[1];
    }
}

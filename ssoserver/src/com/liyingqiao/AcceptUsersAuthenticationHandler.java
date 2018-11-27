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
package com.liyingqiao;

import java.security.GeneralSecurityException;
import org.bson.Document;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.mongo.MongoManagement;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;

/**
 * Handler that contains a list of valid users and passwords. Useful if there is
 * a small list of users that we wish to allow. An example use case may be if
 * there are existing handlers that make calls to LDAP, etc. but there is a need
 * for additional users we don't want in LDAP. With the chain of command
 * processing of handlers, this handler could be added to check before LDAP and
 * provide the list of additional users. The list of acceptable users is stored
 * in a map. The key of the map is the username and the password is the object
 * retrieved from doing map.get(KEY).
 * <p>
 * Note that this class makes an unmodifiable copy of whatever map is provided
 * to it.
 *
 * @author Scott Battaglia
 * @author Marvin S. Addison
 *
 * @since 3.0
 */
public class AcceptUsersAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {


    /** {@inheritDoc} */
    @Override
    protected final HandlerResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential credential)
            throws GeneralSecurityException, PreventedException {

        final String username = credential.getUsername();
        StringBuilder sb = new StringBuilder("{'username':").append('\'').append(username).append('\'').append('}');
        Document userDoc = MongoManagement.findOne("sso_user", "user", sb.toString());

        if (userDoc == null) {
           logger.debug("{} was not found in the map.", username);
           throw new AccountNotFoundException(username + " not found in backing map.");
        }

        final String cachedPassword = userDoc.getString("password");
        final String encodedPassword = this.getPasswordEncoder().encode(credential.getPassword());
        if (!cachedPassword.equals(encodedPassword)) {
            throw new FailedLoginException();
        }
        
        userDoc.remove("password");
		String s = userDoc.toJson();
		credential.setUsername(s);
        return createHandlerResult(credential, new SimplePrincipal(s), null);
    }
    
}

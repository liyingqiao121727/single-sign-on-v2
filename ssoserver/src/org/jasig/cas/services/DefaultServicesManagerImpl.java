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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.jasig.cas.authentication.principal.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Default implementation of the {@link ServicesManager} interface. If there are
 * no services registered with the server, it considers the ServicecsManager
 * disabled and will not prevent any service from using CAS.
 *
 * @author Scott Battaglia
 * @since 3.1
 */
public final class DefaultServicesManagerImpl implements ReloadableServicesManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Instance of ServiceRegistryDao. */
    @NotNull
    private ServiceRegistryDao serviceRegistryDao;

    public void setServiceRegistryDao(ServiceRegistryDao serviceRegistryDao) {
		this.serviceRegistryDao = serviceRegistryDao;
	}

	/** Map to store all services. */
    private ConcurrentHashMap<Long, RegisteredService> services = new ConcurrentHashMap<Long, RegisteredService>();

    public DefaultServicesManagerImpl(
        final ServiceRegistryDao serviceRegistryDao) {
        this(serviceRegistryDao, new ArrayList<String>());
    }

    /**
     * Constructs an instance of the {@link DefaultServicesManagerImpl} where the default RegisteredService
     * can include a set of default attributes to use if no services are defined in the registry.
     *
     * @param serviceRegistryDao the Service Registry Dao.
     * @param defaultAttributes the list of default attributes to use.
     */
    public DefaultServicesManagerImpl(final ServiceRegistryDao serviceRegistryDao,
            final List<String> defaultAttributes) {
        this.serviceRegistryDao = serviceRegistryDao;

        load();
    }
    public DefaultServicesManagerImpl() {
    	
    }

    /*@Audit(action = "DELETE_SERVICE", actionResolverName = "DELETE_SERVICE_ACTION_RESOLVER",
            resourceResolverName = "DELETE_SERVICE_RESOURCE_RESOLVER")*/
    public synchronized RegisteredService delete(final long id) {
        final RegisteredService r = findServiceBy(id);
        if (r == null) {
            return null;
        }

        this.serviceRegistryDao.delete(r);
        this.services.remove(id);

        return r;
    }

    /**
     * {@inheritDoc}
     * Note, if the repository is empty, this implementation will return a default service to grant all access.
     * <p>
     * This preserves default CAS behavior.
     */
    @Override
    public RegisteredService findServiceBy(final Service service) {
        final Collection<RegisteredService> c = convertToTreeSet();

        for (final RegisteredService r : c) {
            if (r.matches(service)) {
                return r;
            }
        }

        return null;
    }

    public RegisteredService findServiceBy(final long id) {
        final RegisteredService r = this.services.get(id);

        try {
            return r == null ? null : r.clone();
        } catch (final CloneNotSupportedException e) {
            return r;
        }
    }

    protected TreeSet<RegisteredService> convertToTreeSet() {
        return new TreeSet<RegisteredService>(this.services.values());
    }

    public Collection<RegisteredService> getAllServices() {
        return Collections.unmodifiableCollection(convertToTreeSet());
    }

    public boolean matchesExistingService(final Service service) {
        return findServiceBy(service) != null;
    }

    /*@Audit(action = "SAVE_SERVICE", actionResolverName = "SAVE_SERVICE_ACTION_RESOLVER",
            resourceResolverName = "SAVE_SERVICE_RESOURCE_RESOLVER")*/
    public synchronized RegisteredService save(final RegisteredService registeredService) {
        final RegisteredService r = this.serviceRegistryDao.save(registeredService);
        this.services.put(r.getId(), r);
        return r;
    }

    public void reload() {
        logger.info("Reloading registered services.");
        load();
    }

    private void load() {
        final ConcurrentHashMap<Long, RegisteredService> localServices =
                new ConcurrentHashMap<Long, RegisteredService>();

        for (final RegisteredService r : this.serviceRegistryDao.load()) {
            logger.debug("Adding registered service {}", r.getServiceId());
            localServices.put(r.getId(), r);
        }

        this.services = localServices;
        logger.info(String.format("Loaded %s services.", this.services.size()));
    }
}

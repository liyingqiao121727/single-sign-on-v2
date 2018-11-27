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
package org.jasig.cas.ticket.registry;

import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the TicketRegistry that is backed by a ConcurrentHashMap.
 *
 * @author Scott Battaglia
 * @since 3.0
 */
public final class DefaultTicketRegistry extends AbstractTicketRegistry  {

	/** A HashMap to contain the tickets. */
	private final Map<String, Ticket> cache;

	public DefaultTicketRegistry() {
		this.cache = new ConcurrentHashMap<String, Ticket>();
	}

	/**
	 * Creates a new, empty registry with the specified initial capacity, load
	 * factor, and concurrency level.
	 *
	 * @param initialCapacity - the initial capacity. The implementation
	 * performs internal sizing to accommodate this many elements.
	 * @param loadFactor - the load factor threshold, used to control resizing.
	 * Resizing may be performed when the average number of elements per bin
	 * exceeds this threshold.
	 * @param concurrencyLevel - the estimated number of concurrently updating
	 * threads. The implementation performs internal sizing to try to
	 * accommodate this many threads.
	 */
	public DefaultTicketRegistry(final int initialCapacity, final float loadFactor, final int concurrencyLevel) {
		this.cache = new ConcurrentHashMap<String, Ticket>(initialCapacity, loadFactor, concurrencyLevel);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException if the Ticket is null.
	 */
	@Override
	public void addTicket(final Ticket ticket) {
		Assert.notNull(ticket, "ticket cannot be null");

		logger.debug("Added ticket [{}] to registry.", ticket.getId());
		this.cache.put(ticket.getId(), ticket);
	}

	public Ticket getTicket(final String ticketId) {
		if (ticketId == null) {
			return null;
		}

		logger.debug("Attempting to retrieve ticket [{}]", ticketId);
        final Ticket ticket = this.cache.get(ticketId);

		if (ticket != null) {
			logger.debug("Ticket [{}] found in registry.", ticketId);
		}

		return ticket;
	}

	public boolean deleteTicket(final String ticketId) {
		if (ticketId == null) {
			return false;
		}
		logger.debug("Removing ticket [{}] from registry", ticketId);
		return (this.cache.remove(ticketId) != null);
	}

	public Collection<Ticket> getTickets() {
		return Collections.emptyList();//Collections.unmodifiableCollection(this.cache.values());
	}

	public int sessionCount() {
		int count = 0;
		for (Ticket t : this.cache.values()) {
			if (t instanceof TicketGrantingTicket) {
				count++;
			}
		}
		return count;
	}

	public int serviceTicketCount() {
		int count = 0;
		for (Ticket t : this.cache.values()) {
			if (t instanceof ServiceTicket) {
				count++;
			}
		}
		return count;
	}
}

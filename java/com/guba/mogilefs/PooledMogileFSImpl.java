/*
 * Created on Jun 15, 2005
 *
 * 
 */
package com.guba.mogilefs;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * MogileFS implementation that keeps a pool of tracker connections
 * and can handle access by multiple threads.
 * 
 * @author eml@guba.com
 */
public class PooledMogileFSImpl extends BaseMogileFSImpl {

	private int maxTrackerConnections;
	private int maxIdleConnections;
	private long maxIdleTimeMillis;
	private int trackerConnectTimeout = -1;
	private int trackerReadTimeout = -1;

	/**
	 * Set things up. Make sure you pass in at least one valid tracker, or
	 * you'll get an exception.
	 *
	 * @throws com.guba.mogilefs.NoTrackersException
	 *             if we can't connect to at least one tracker
	 */
	public PooledMogileFSImpl(final String domain, final String trackerStrings[], final int maxTrackerConnections,
			final int maxIdleConnections, final long maxIdleTimeMillis)
	throws NoTrackersException, BadHostFormatException {
		this(domain, trackerStrings, maxTrackerConnections, maxIdleConnections, maxIdleTimeMillis, false);
	}

	/**
	 * Set things up. Make sure you pass in at least one valid tracker, or
	 * you'll get an exception.
	 * 
	 * @throws NoTrackersException
	 *             if we can't connect to at least one tracker
	 */
	public PooledMogileFSImpl(final String domain, final String trackerStrings[], final int maxTrackerConnections,
			final int maxIdleConnections, final long maxIdleTimeMillis, final boolean shouldKeepPathOrder)
	throws NoTrackersException, BadHostFormatException {
		super(domain, trackerStrings, shouldKeepPathOrder);

		this.maxIdleConnections = maxIdleConnections;
		this.maxTrackerConnections = maxTrackerConnections;
		this.maxIdleTimeMillis = maxIdleTimeMillis;
	}

	/**
	 * Set the max number of times to try retry storing a file with 'storeFile' or
	 * deleting a file with 'delete'. If this is -1, then never stop retrying. This value
	 * defaults to -1.
	 * 
	 * @param maxRetries
	 */
	public void setTrackerTimeouts(int connectTimeout, int readTimeout) {
		this.trackerConnectTimeout = connectTimeout;
		this.trackerReadTimeout = readTimeout;
	}

	@Override
	protected ObjectPool buildBackendPool() {
		// create a new pool of Backend objects
		return new GenericObjectPool(new PoolableBackendFactory(trackers, trackerConnectTimeout, trackerReadTimeout),
				maxTrackerConnections,
				GenericObjectPool.WHEN_EXHAUSTED_BLOCK,
				1000 * 60,  // wait for up to 60 seconds if we run out
				maxIdleConnections,
				1,     // minIdle (** 1? **)
				true, // test on borrow
				true, // test on return
				20 * 1000, // time between eviction runs millis
				-1,  // number of tests per eviction run
				maxIdleTimeMillis, // number of seconds before an object is considered idle
				true, // test while idle
				5 * 1000); //softMinEvictableIdleTimeMillis
	}

}

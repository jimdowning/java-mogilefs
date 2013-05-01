/*
 * Created on Jun 15, 2005
 *
 * 
 */
package com.guba.mogilefs;

/**
 * This exception means we weren't able to get in touch with any trackers after
 * looping through and trying all of them.
 * 
 * @author eml
 */
public class NoTrackersException extends MogileException {

	public NoTrackersException(String message, Throwable reason) {
		super(message, reason);
	}

	public NoTrackersException() {
		super();
	}

	public NoTrackersException(String message) {
		super(message);
	}

	public NoTrackersException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 1L;

}

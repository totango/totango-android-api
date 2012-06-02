/*
 * @(#)ConnectionFailedException.java   May 31, 2012
 *
 * Copyright 2012 Totango Inc.
 */

package com.totango.api;

/**
 * This exception throws on communication failure with Totango server.
 */
public class ConnectionFailedException 
	extends TotangoException 
{
	private static final long serialVersionUID = 1L;

	public ConnectionFailedException(String msg) 
	{
		super(msg);
	}

	public ConnectionFailedException(String msg, Throwable th) 
	{
		super(msg, th);
	}
}

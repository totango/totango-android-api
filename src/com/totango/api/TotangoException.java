/*
 * @(#)TotangoException.java   May 31, 2012
 *
 * Copyright 2012 Totango Inc.
 */

package com.totango.api;

/**
 * This is a super based exception thrown on failure by {@link Totango}.
 */
public class TotangoException 
	extends Exception 
{

	private static final long serialVersionUID = 1L;

	public TotangoException(String msg) 
	{
		super(msg);
	}

	public TotangoException(Throwable ex) 
	{
		super(ex);
	}

	public TotangoException(String msg, Throwable ex) 
	{
		super(msg, ex);
	}
}

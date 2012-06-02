/*
 * @(#)ExceededLengthValue.java   May 31, 2012
 *
 * Copyright 2012 Totango Inc.
 */

package com.totango.api;

/**
 * This exception throws if {@link Totango.setDisplayName} is larger 128 characters.
 */
public class ExceededLengthValue 
	extends TotangoException 
{
	private static final long serialVersionUID = 1L;

	public ExceededLengthValue(String msg) 
	{
		super(msg);
	}

	public ExceededLengthValue(String msg, Throwable ex) 
	{
		super(msg, ex);
	}
}

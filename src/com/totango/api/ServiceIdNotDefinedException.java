/*
 * @(#)ServiceIdNotDefinedException.java   May 31, 2012
 *
 * Copyright 2012 Totango Inc.
 */

package com.totango.api;

/**
 * This exception throws if serviceId is not setted for {@link Totango} instance.
 */
public class ServiceIdNotDefinedException 
	extends TotangoException 
{
	private static final long serialVersionUID = 1L;

	public ServiceIdNotDefinedException(String msg) 
	{
		super(msg);
	}

	public ServiceIdNotDefinedException(String msg, Throwable ex) 
	{
		super(msg, ex);
	}
}

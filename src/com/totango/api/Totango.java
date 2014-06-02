/*
 * @(#)Totango.java   May 31, 2012
 *
 * Copyright 2014 Totango Inc.
 */

package com.totango.api;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * This class provides a simple, easy to use API through which an Android/Java application sends Activity Events
 * corresponding to something a user has done on the application. 
 * Each such event contains not only information about the specific activity, but also identifiers of the specific
 * customer and user who conducted it.  
 * For example, the following is an (logical) Activity event: 
 * John from FedEx created a document in the document management module.
 * 
 * Totango tracker is a singleton class that provides a global point of access to it.
 * Example usage:
 * 
 *  1. Totango.getInstance().setServiceId("SP-XXXX-XX");
 *  2. Totango.getInstance().identify("mike@zendesk.com", "Mike");
 *  3. Totango.getInstance().track("Login", "Project Management");
 */
public class Totango 
{
    private final String TOTANGO_BASE_URL = "https://sdr.totango.com/pixel.gif/";

    private final String PREFIX_ACCOUNT_ID = "sdr_o";
    private final String PREFIX_SERVICE = "sdr_s";
    private final String PREFIX_USER = "sdr_u";
    private final String PREFIX_ACTIVITY = "sdr_a";
    private final String PREFIX_MODULE = "sdr_m";
    private final String PREFIX_DISPLAY_NAME = "sdr_odn";

    private final String CHARSET = "UTF-8";

    private final int DISPLAY_NAME_MAX_LENGTH = 128;

    private String serviceId;
    private String accountID;
    private String userName;

    private static Totango totango = null;

    private Totango() 
    {
    }

    /**
     * @return a singleton Totango instance.
     */
    public static synchronized Totango getInstance() 
    {
        if (totango == null) 
        {
        	totango = new Totango();
        }
        
        return totango;
    }

    /**
     * Initialize tracking with provided service-id.
     * This method should be called before starting usage the track or identify methods.
     * 
     * @param serviceId the serviceId SP-XXXX-XX
     */
    public void setServiceId(String serviceId) 
    {
        this.serviceId = serviceId;
    }
    
    
    /**
     * It is sometimes convenient to set the Account and User once, and have them carry 
     * through to any Activities tracked. The identify() method allows you to do that. 
     * The method is typically called immediately after the user successfully logins to your service. 
     * The information is then stored in Totango instance and used to associate the identity 
     * of any subsequent activity reported through track(). 
     * This is useful so that you do not have to carry around the user-context everywhere you want
     * to track events.
     * 
     * @param accountID
     * @param userName
     */
    public void identify(String accountID, String userName) 
    {
    	if ( this.serviceId == null )
     	   throw new NullPointerException("Invalid operation, serviceID not setted");

        if (accountID == null || userName == null)
            throw new NullPointerException("Arguments can't be null");

        this.accountID = accountID;
        this.userName = userName;
    }

    /**
     * Every time you want to track an event, use the track method and provide 
     * the Activity, Module, Account an User
     * 
     * @param accountID
     * @param userName
     * @param activity
     * @param module
     * @throws TotangoException throws on communication error with Totango server.
     */
    public void track(String accountID, String userName, String activity, String module) 
    	throws TotangoException
    {
    	if ( this.serviceId == null )
    	   throw new NullPointerException("Invalid operation, serviceID not setted");
    	
        if (activity == null || module == null || accountID == null || userName == null) 
            throw new NullPointerException("Arguments can't be null");

        try 
        {
            StringBuilder builder = new StringBuilder(TOTANGO_BASE_URL);
            builder.append("?" + PREFIX_SERVICE + "=");
            builder.append(serviceId);
            builder.append("&" + PREFIX_ACCOUNT_ID + "=");
            builder.append(URLEncoder.encode(accountID, CHARSET));
            builder.append("&" + PREFIX_USER + "=");
            builder.append(URLEncoder.encode(userName, CHARSET));
            builder.append("&" + PREFIX_ACTIVITY + "=");
            builder.append(URLEncoder.encode(activity, CHARSET));
            builder.append("&" + PREFIX_MODULE + "=");
            builder.append(URLEncoder.encode(module, CHARSET));

            new URL(builder.toString()).getContent();
        } catch (Exception e) 
        {
        	throw new ConnectionFailedException("Connection failed to Totango server.", e);
        }
    }

    
    /**
	 * Every time you want to track an event, use the track method and provide 
     * the Activity, Module.
     * Use the identify() method to set the Account and User once for Totango tracker instance. 
     * In that case you can omit those fields form the call to track().
     * 
     * @param activity
     * @param module
     * @throws TotangoException throws on communication error with Totango server.
     */
    public void track(String activity, String module) 
    	throws TotangoException
    {
        track(this.accountID, this.userName, activity, module);
    }

    
    /**
     * Account attributes are extra pieces of information that can be associated with
     * an account, and are later used in Totango for all sorts of interesting analysis
     * purposes.
     * 
     * @param attributes the account attributes
     * @throws TotangoException throws on communication error with Totango server.
     */
    public void setAccountAttributes(Map<String, String> attributes) 
    	throws TotangoException 
    {
        try 
        {
            StringBuilder builder = new StringBuilder(TOTANGO_BASE_URL);
            builder.append("?" + PREFIX_SERVICE + "=");
            builder.append(serviceId);
            builder.append("&" + PREFIX_ACCOUNT_ID + "=");
            builder.append(URLEncoder.encode(accountID, CHARSET));

            for (Map.Entry<String, String> attribute : attributes.entrySet()) 
            {
                builder.append("&" + PREFIX_ACCOUNT_ID + ".");
                builder.append(URLEncoder.encode(attribute.getKey(), CHARSET));
                builder.append("=" + URLEncoder.encode(attribute.getValue(), CHARSET));
            }

            builder.append("&" + PREFIX_USER + "=");
            builder.append(URLEncoder.encode(userName, CHARSET));

            new URL(builder.toString()).getContent();
        } catch (Exception e) 
        {
            throw new ConnectionFailedException("Connection failed to Totango server.", e);
        }
    }


    /**
     * Set display Name to provide a ‘human friendly’ string which Totango 
     * will use as the account’s name in its UI and reports. 
     * The display-name is limited to 128 characters. 
     * 
     * @param displayName the ‘human friendly’ display name.
     * 
     * @throws ExceededLengthValue If displayName is larger 128 characters.
     * @throws NullPointerException If displayName is <code>null</code>
     */
    public void setDisplayName(String displayName) 
    		throws TotangoException 
    {
        if (displayName == null) 
            throw new NullPointerException("Display name can't be null");

        if (displayName.length() > DISPLAY_NAME_MAX_LENGTH) 
            throw new ExceededLengthValue("Display name should be up to 128 characters");

        try 
        {
            StringBuilder builder = new StringBuilder(TOTANGO_BASE_URL);
            builder.append("?" + PREFIX_SERVICE + "=");
            builder.append(serviceId);
            builder.append("&" + PREFIX_ACCOUNT_ID + "=");
            builder.append(URLEncoder.encode(accountID, CHARSET));
            builder.append("&" + PREFIX_USER + "=");
            builder.append(URLEncoder.encode(userName, CHARSET));
            builder.append("&" + PREFIX_DISPLAY_NAME + "=");
            builder.append(URLEncoder.encode(displayName, CHARSET));

            new URL(builder.toString()).getContent();
            
        } catch (Exception e) 
        {
          throw new ConnectionFailedException("Connection failed to Totango server.", e);
        }
    }
}

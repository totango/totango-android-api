/*
 * @(#)TotangoTest.java   May 31, 2012
 *
 * Copyright 2012 Totango Inc.
 */

package com.totango.example;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.totango.api.Totango;
import com.totango.api.TotangoException;

/**
 * This application provides a simple example of Totango API through which a Android application sends
 * Activity Events corresponding to something a user has done on the application.  
 */
public class TotangoTest extends Activity {

    private static final String SALES_MANAGER = "Sales Manager";
    private static final String SUCCESS_MANAGER = "Success Manager";
    private static final String REGION = "Region";

    private final String SERVICE_ID = "SP-18030-01";
    private final String TRACK = "track";
    private final String SET_ATTRIBUTES = "Set attributes";
    private final String SET_DISPLAY_NAME = "Set display name";
    private final String MODULE = "Project Management";
    private final String CALLED_METHOD = "Called method";

    private String org = "Zendesk";
    private String user = "mike@zendesk.com";
    private String displayName = "Michael";

    private boolean identified = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // set serviceID
        Totango.getInstance().setServiceId(SERVICE_ID);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(TotangoTest.this);
                dialog.setTitle("Identify");
                dialog.setContentView(R.layout.login);

                ((EditText) dialog.findViewById(R.id.etLoginOrg)).setText(org);
                ((EditText) dialog.findViewById(R.id.etLoginUser)).setText(user);

                Button btnCancel = (Button) dialog.findViewById(R.id.btnLoginCancel);
                btnCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                    }
                });

                Button btnIdentify = (Button) dialog.findViewById(R.id.btnLoginLogin);
                btnIdentify.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        org = ((EditText) dialog.findViewById(R.id.etLoginOrg)).getText().toString();
                        user = ((EditText) dialog.findViewById(R.id.etLoginUser)).getText().toString();

                        if (org.equals("") || user.equals("")) {
                            return;
                        }
                        dialog.dismiss();

                        Totango.getInstance().identify(org, user);

                        identified = true;

                        AlertDialog identifyDialog = new AlertDialog.Builder(TotangoTest.this).create();
                        identifyDialog.setTitle(CALLED_METHOD);
                        identifyDialog.setMessage(String.format("identify\norg: %s,\nuser: %s", org, user));
                        identifyDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                track("Login", MODULE);
                                return;
                            }
                        });
                        identifyDialog.show();
                    }
                });

                dialog.show();
            }
        });
        Button btnNewProject = (Button) findViewById(R.id.btnNewProject);
        btnNewProject.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                track(getResources().getString(R.string.newProject), MODULE);
            }
        });
        Button btnNewTask = (Button) findViewById(R.id.btnNewTask);
        btnNewTask.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                track(getResources().getString(R.string.newTask), MODULE);
            }
        });
        Button btnShareProject = (Button) findViewById(R.id.btnShareProject);
        btnShareProject.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                track(getResources().getString(R.string.shareProject), MODULE);
            }
        });
        Button btnAddMilestone = (Button) findViewById(R.id.btnAddMilestone);
        btnAddMilestone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                track("Another org", "Another user", getResources().getString(R.string.addMilestone), MODULE);
            }
        });
        Button btnAddTimesheet = (Button) findViewById(R.id.btnAddTimesheet);
        btnAddTimesheet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                track("Another org", "Another user", getResources().getString(R.string.addTimesheet), MODULE);
            }
        });
        Button btnSetAttributes = (Button) findViewById(R.id.btnSetAttributes);
        btnSetAttributes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	setAccountAttributes();
            }
        });
        Button btnSetDisplayName = (Button) findViewById(R.id.btnSetDisplayName);
        btnSetDisplayName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(TotangoTest.this);
                dialog.setTitle(SET_DISPLAY_NAME);
                dialog.setContentView(R.layout.displayname);

                ((EditText) dialog.findViewById(R.id.etDisplayName)).setText(displayName);

                Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdateDisplayName);
                btnUpdate.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        displayName = ((EditText) dialog.findViewById(R.id.etDisplayName)).getText().toString();

                        setDisplayName(displayName);

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        showDialog("Define serviceID", "Please don't forget to setup ServiceID:\nSP-XXXX-XX");

    }

    private void track(String activity, String module) {
        if (!identified) {
            showDialog(TRACK, "User should be identified first");
            return;
        }
        
        try{
        	Totango.getInstance().track(activity, module);
        	showDialog(CALLED_METHOD, String.format("track\nactivity: %s,\nmodule: %s", activity, module));
        }catch( TotangoException ex )
        {
        	showDialog("Failed to track event", ex.getMessage());        	
        }
    }

    private void track(String org, String user, String activity, String module) {
    	try{
    		Totango.getInstance().track(org, user, activity, module);
	        showDialog(CALLED_METHOD,
	            String.format("track\norg: %s,\nuser: %s,\nactivity: %s,\nmodule: %s", org, user, activity, module));
        }catch( TotangoException ex )
        {
        	showDialog("Failed to track event", ex.getMessage());        	
        }
    }

    private void setAccountAttributes() {
        if (!identified) {
            showDialog(SET_ATTRIBUTES, "User should be identified first");
            return;
        }

        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(SALES_MANAGER, "John Smith");
        attributes.put(SUCCESS_MANAGER, "John Smith");
        attributes.put(REGION, "USA");

        try{
	        Totango.getInstance().setAccountAttributes(attributes);
	        showDialog(CALLED_METHOD, String.format("setAccountAttributes\n%s: John Smith,\n%s: John Smith,\n%s: USA",
	            SALES_MANAGER, SUCCESS_MANAGER, REGION));
        }catch( TotangoException ex )
        {
        	showDialog("Failed to set attributes", ex.getMessage());
        }
    }

    public void setDisplayName(String displayName) {
        if (!identified) {
            showDialog(SET_DISPLAY_NAME, "User should be identified first");
            return;
        }

        try {
            Totango.getInstance().setDisplayName(displayName);
            showDialog(CALLED_METHOD, String.format("setDisplayName\nDisplay name: %s", displayName));
        } catch (TotangoException e) {
            showDialog("Failed to set display name", e.getMessage());
        }
    }

    private void showDialog(String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(TotangoTest.this).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        
        dialog.show();
    }
}

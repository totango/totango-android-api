Getting Started

Requirements

To integrate Totango tracking capabilities with your Android app, you will need the following:
Android SDK
Totango Android SDK 

Setup
Open IDE and create a new Android project. 
Add Totango library dependency to your project

The Totango SDK should work with any Android device running Android 2.1 or higher.

An example application is included with the SDK. 

* Using the SDK

Initialize tracker by setting serviceID property on the tracker singleton obtained via Totango.getInstance(). 
It is often convenient to call this method directly in the onCreate method.
If you are using same account ID and user name for your calls it is also good place to set it. 
Use identify(String accountId, String userName) method to save this attributes in Totango object. 
For example:
```
import com.totango.api.Totango;

private String serviceId = "your Totango service ID here in the format SP-xxxx-01";
private String accountId = "Id of this account";
private String user = "user@example.com";

Totango totango = Totango.getInstance();
totango.setServiceId(serviceId);
totango.identify(accountId, userName);
```
* Tracking

To track user activity simply call method track(String activity, String module) if you want to use account ID and user name stored with previous call to identify(String accountId, String userName).
If you want to override stored account ID and user name - use track(String accountId, String userName, String activity, String module) method. 
This methods are blocking, so with a slow network it may take some time. This methods returns operation result. 
For example:

totango.track("Activity", "Module");
or 
totango.track("Organization", "User", "Activity", "Module");

* Setting Account Attributes

To set account attributes use method setAccountAttributes(Map<String, String> attributes) which treats map keys as "key":"value" values.

Map<String, String> attributes = new HashMap<String, String>();
attributes.put("Sales Manager", "John Smith");
attributes.put("Success Manager", "John Smith");
attributes.put("Region", "USA");

totango.setAccountAttributes(attributes);

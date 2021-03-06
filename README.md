# SanderSoft SmartThings Repository

# This app is Unsupported by the devloper who now uses Hubitat.  The new SmartThings mobile client and platform is unreliable and buggy.

## My SmartThings Applications

### Garage Door Virtual Contact Sensor SmartApp and Device Handler (DTH)
- Eliminates the need for using physical z-wave sensors attached to MyQ controlled garage doors
- Works in concert with [MQ Lite SmartApp](https://github.com/brbeaird/SmartThings_MyQ)
- The DTH works with your customized code which needs to: 
	- Poll an e-mail account for targeted emails from [Sears Craftsman Assurelink Website](https://assurelink.craftsman.com/) concerning your MyQ controlled garage door activity states (ie. open/close).  
	- Parses those e-mails for the door name and open/close state and updates SmartThing virtual contact sensors with garage door status condition.  
	- *Note: Customized Python dameon code not provided for the Raspberry PI server.  Requires knowledge of Python programming language, installed prerequisite software and setting up a dameon service.  Contact me for information of my Python code or create your own*.

	**OR**

	- [IFTTT](https://ifttt.com/) (No Hardware, No Custom Coding except creating an IFTTT applet)
		* Create a [virtual ST contact garage switch ](https://github.com/ph4r/SmartThings_MyQ/blob/master/devicetypes/ph4r/virtual-contact-sensor-switch.groovy) in the ST IDE, one for each garage door and name each device with a unique name (Garage1, Garage 2, etc).
		* Make sure you have notifications 'ON' in your MyQ app so that emails are sent to your email provider.
		* Authorize the ST virtual contact sensors to be controlled by IFTTT 
		* Create an IFTTT applet per garage door state, ie one for an 'open' and one for 'closed' state. Each IFTTT applet should search the email for the sender (:from in Gmail), and search the subject line (:subject in Gmail) for door name and state. If using another email system, achieve the same using IFTTT applets.
			- Look for targeted emails from [Sears Craftsman Assurelink Website](https://assurelink.craftsman.com/) concerning your MyQ controlled garage door activity states (ie. open/close) and your door name.
		* Connect each IFTTT applet you created to the named virtual contact sensor for the garage door. 
		* No custom SmartApp or DTH needed.
		* No server or coding
		* No polling MyQ (Response times from IFTTT to SmartThings to trigger open/close states is slightly delayed based on email poll throttling from IFTTT.		  

### Open Door Sensor SmartApp
- Reports on doors with contact sensors that are left open for a specified period of time.

### Button Controller {Legacy} SmartApp
- Works with multiple Minimote devices using child apps

### Alarm Panel Monitor
- Works with Vista/Honeywell Alarms

### Alarmdecoder SmartApp
- Provides integration with Alarm Decoder hardware

### BWA Device Type Handler (DTH)
- Provides cloud connected status of a Balboa Model 20p WiFi connected Hot Tub.  
- Requires my companion SmartApp which is currently available to the public. 

### RestServer Device Type Handler (DTH)
- Provides Ping Response from a local network REST API Webserver.  
- Code for the restAPI Webserver is not public

### My MimoLite Device Handler (DTH)
- Provides for the dual capabilities (switch/sensor) of the MimoLite device

## My Google Chrome Applications

### Reload all Browser Tabs Except (Chrome Extension) 

[Link to Repository](https://github.com/KurtSanders/MySmartThingsPersonal/tree/master/Chrome/reload-all-tabs-except)

- This Google Chrome extension reloads or refreshes all tabs except those Tabs that match a user defined keyword URL list.
- Features
	- Quick keyboard shortcut (alt+shift+r) which can be customizable.
	- Browser Action to have a refresh button on the toolbar. You can hide it if you don't want it.
	- You can choose to reload all tabs from the context menu except for those tabs that are excluded because they match a URL base keyword.
	- You can customize the preference whether to refresh all windows, pinned tabs or just the current one, as well as all tabs to the right or left.

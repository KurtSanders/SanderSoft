/**
 *  AlarmDecoder Network Appliance
 *
 *  Copyright 2016 Nu Tech Software Solutions, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
import groovy.json.JsonSlurper;

preferences {
    section() {
        input("api_key", "password", title: "API Key", description: "The key to access the REST API", required: true)
        input("user_code", "password", title: "Alarm Code", description: "The user code for the panel", required: true)
        input("panel_type", "enum", title: "Panel Type", description: "Type of panel", options: ["ADEMCO", "DSC"], defaultValue: "ADEMCO", required: true)
    }
    section() {
        input("zonetracker1zone" , "number" , title: "ZoneTracker Sensor #1"  , description: "Zone number to associate with this contact sensor.")
        input("zonetracker2zone" , "number" , title: "ZoneTracker Sensor #2"  , description: "Zone number to associate with this contact sensor.")
        input("zonetracker3zone" , "number" , title: "ZoneTracker Sensor #3"  , description: "Zone number to associate with this contact sensor.")
        input("zonetracker4zone" , "number" , title: "ZoneTracker Sensor #4"  , description: "Zone number to associate with this contact sensor.")
        input("zonetracker5zone" , "number" , title: "ZoneTracker Sensor #5"  , description: "Zone number to associate with this contact sensor.")
        input("zonetracker6zone" , "number" , title: "ZoneTracker Sensor #6"  , description: "Zone number to associate with this contact sensor.")
        input("zonetracker7zone" , "number" , title: "ZoneTracker Sensor #7"  , description: "Zone number to associate with this contact sensor.")
        input("zonetracker8zone" , "number" , title: "ZoneTracker Sensor #8"  , description: "Zone number to associate with this contact sensor.")
        input("zonetracker9zone" , "number" , title: "ZoneTracker Sensor #9"  , description: "Zone number to associate with this contact sensor.")
        input("zonetracker10zone", "number" , title: "ZoneTracker Sensor #10" , description: "Zone number to associate with this contact sensor.")
        input("zonetracker11zone", "number" , title: "ZoneTracker Sensor #11" , description: "Zone number to associate with this contact sensor.")
        input("zonetracker12zone", "number" , title: "ZoneTracker Sensor #12" , description: "Zone number to associate with this contact sensor.")
    }
}

metadata {
    definition (name: "network appliance", namespace: "alarmdecoder", author: "Scott Petersen") {
        capability "Refresh"
        capability "Switch"             // STAY
        capability "Lock"               // AWAY
        capability "Alarm"              // PANIC
        capability "smokeDetector"      // FIRE
        capability "Actuator"

        attribute "panel_state", "enum", ["armed", "armed_stay", "armed_away", "disarmed", "alarming", "fire"]
        attribute "armed", "enum", ["armed", "disarmed", "arming", "disarming"]
        attribute "panic_state", "string"
        attribute "zoneStatus1", "number"
        attribute "zoneStatus2", "number"
        attribute "zoneStatus3", "number"
        attribute "zoneStatus4", "number"
        attribute "zoneStatus5", "number"
        attribute "zoneStatus6", "number"
        attribute "zoneStatus7", "number"
        attribute "zoneStatus8", "number"
        attribute "zoneStatus9", "number"
        attribute "zoneStatus10", "number"
        attribute "zoneStatus11", "number"
        attribute "zoneStatus12", "number"
        attribute "panel_on_battery", "string"
        attribute "panel_panicked", "string"
        attribute "panel_powered", "string"
        attribute "listZones", "string"

        command "disarm"
        command "arm_stay"
        command "arm_away"
        command "panic"
        command "panic1"
        command "panic2"
        command "listzonesbutton"
    }

    simulator {
        // TODO: define status and reply messages here
    }

    tiles(scale: 2) {
        multiAttributeTile(name: "status", type: "generic", width: 6, height: 4) {
            tileAttribute("device.panel_state", key: "PRIMARY_CONTROL") {
                attributeState "armed", label: 'Armed', icon: "st.security.alarm.on", backgroundColor: "#ffa81e"
                attributeState "armed_stay", label: 'Armed (STAY)', icon: "st.security.alarm.on", backgroundColor: "#ffa81e"
                attributeState "armed_away", label: 'Armed (AWAY)', icon: "st.security.alarm.on", backgroundColor: "#ffa81e"
                attributeState "disarmed", label: 'Disarmed', icon: "st.security.alarm.off", backgroundColor: "#79b821", defaultState: true
                attributeState "alarming", label: 'Alarming!', icon: "st.home.home2", backgroundColor: "#ff4000"
                attributeState "fire", label: 'Fire!', icon: "st.contact.contact.closed", backgroundColor: "#ff0000"
            }
            tileAttribute("device.panel_ready", key: "SECONDARY_CONTROL") {
                attributeState("panel_ready", label:'${currentValue}')
            }
        }

        standardTile("arm_disarm", "device.armed", inactiveLabel: false, width: 2, height: 2) {
            state "armed", action:"lock.unlock", icon:"st.security.alarm.off", label: "DISARM", nextState: "disarming"
            state "disarmed", action:"lock.lock", icon:"st.security.alarm.on", label: "AWAY", nextState: "arming"
            state "arming", action:"lock.unlock", icon:"st.security.alarm.off", label: "ARMING", nextState: "armed"
            state "disarming", action:"lock.lock", icon:"st.security.alarm.on", label: "DISARMING", nextState: "disarmed"
        }

        standardTile("switch", "device.switch", inactiveLabel: false, width: 2, height: 2) {
            state "on", action:"switch.off", icon:"st.switches.switch.on", backgroundColor: "#FFFFFF", label: "DISARM"
            state "off", action:"switch.on", icon:"st.switches.switch.off", backgroundColor: "#008000", label: "ARM"
        }
        standardTile("stay_disarm", "device.armed", inactiveLabel: false, width: 2, height: 2) {
            state "armed", action:"switch.off", icon:"st.security.alarm.off", label: "DISARM", backgroundColor: "#ffa81e", nextState: "disarming"
            state "disarmed", action:"switch.on", icon:"st.Home.home4", label: "STAY", backgroundColor: "#79b821", nextState: "arming"
            state "arming", action:"switch.off", icon:"st.security.alarm.off", label: "ARMING", nextState: "armed"
            state "disarming", action:"switch.on", icon:"st.Home.home4", label: "DISARMING", nextState: "disarmed"
        }

        standardTile("panic", "device.panic_state", inactiveLabel: false, width: 2, height: 2) {
            state "default", icon:"st.Health & Wellness.health9", label: "PANIC", nextState: "panic1", action: "panic1"
            state "panic1", icon: "st.Health & Wellness.health9", label: "PANIC", nextState: "panic2", action: "panic2", backgroundColor: "#ffa81e"
            state "panic2", icon: "st.Health & Wellness.health9", label: "PANIC", nextState: "default", action: "alarm.both", backgroundColor: "#ff4000"
        }
        standardTile("panel_on_battery", "device.panel_on_battery", inactiveLabel: false, width: 2, height: 2, decoration: "flat", wordWrap: true) {
            state "0"  , label: 'A/C'    ,  icon: "st.switches.switch.on" , backgroundColor: "#008000"
            state "1"  , label: 'Battery',  icon: "st.secondary.activity" , backgroundColor: "#ff0000"
        }
        standardTile("listzonesbutton", "device.listzonesbutton", width: 2, height: 2, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false) {
            state "listzonesbutton", label: 'Update', action:"listzonesbutton", backgroundColor: "#79b821", icon:"st.Home.home2"
        }
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "default", label: 'Refresh', action:"refresh.refresh", icon:"st.secondary.refresh-icon", backgroundColor: "#6495ed"
        }
        valueTile("listofzones", "device.listofzones", inactiveLabel: false, width: 6, height: 6, decoration: "flat", wordWrap: true) {
            state "listofzones", label: '${currentValue}'
        }
        valueTile("zoneStatus1", "device.zoneStatus1", inactiveLabel: false, width: 1, height: 1) {
            state "default", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }
        
        valueTile("zoneStatus2", "device.zoneStatus2", inactiveLabel: false, width: 1, height: 1) {
            state "default", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus3", "device.zoneStatus3", inactiveLabel: false, width: 1, height: 1) {
            state "default", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus4", "device.zoneStatus4", inactiveLabel: false, width: 1, height: 1) {
            state "default", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus5", "device.zoneStatus5", inactiveLabel: false, width: 1, height: 1) {
            state "default", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus6", "device.zoneStatus6", inactiveLabel: false, width: 1, height: 1) {
            state "default", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus7", "device.zoneStatus7", inactiveLabel: false, width: 1, height: 1) {
            state "default", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus8", "device.zoneStatus8", inactiveLabel: false, width: 1, height: 1) {
            state "default", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus9", "device.zoneStatus9", inactiveLabel: false, width: 1, height: 1) {
            state "default", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus10", "device.zoneStatus10", inactiveLabel: false, width: 1, height: 1) {
            state "default", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus11", "device.zoneStatus11", inactiveLabel: false, width: 1, height: 1) {
            state "default", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus12", "device.zoneStatus12", inactiveLabel: false, width: 1, height: 1) {
            state "default", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }
        main(["status"])
        details(
            ["status", 
             "stay_disarm", 
             "arm_disarm", 
             "panic", 
             "zoneStatus1", "zoneStatus2", "zoneStatus3", "zoneStatus4", "zoneStatus5", "zoneStatus6", "zoneStatus7", "zoneStatus8", "zoneStatus9", "zoneStatus10", "zoneStatus11", "zoneStatus12", 
             "panel_on_battery",
             "listzonesbutton",
             "refresh",
             "listofzones"
            ]
        )
    }
}

/*** Handlers ***/

def installed() {
    for (def i = 1; i <= 12; i++)
        sendEvent(name: "zoneStatus${i}", value: "", displayed: false)
}

def updated() {
    log.trace "--- handler.updated"
    state.faulted_zones = []
    state.panel_state = "disarmed"
    state.fire = false
    state.alarming = false
    state.armed = false
    state.panic_started = null;

    for (def i = 1; i <= 12; i++)
        sendEvent(name: "zoneStatus${i}", value: "", displayed: false)
}

def uninstalled() {
    log.trace "--- handler.uninstalled"
}

// parse events into attributes
def parse(String description) {
    def msg = parseLanMessage(description)
    if (msg?.json?.zones != null) {
        log.info "==> ZONE DATA <==="
        //    def body = msg.body              // => request body as a string
        //    log.info "==> msg.body-> ${body}"
        //    def status = msg.status          // => http status code of the response
        def jsonZones = msg.json.zones.sort{it.zone_id}              // => any JSON included in response body, as a data structure of lists and maps
//        log.info "jsonZones -> ${jsonZones}"
        log.info "Count of Reported Zones: ${jsonZones.size()}"
        def i = 0
        def listofzones = ""
        def listofzonesMap = []
        for (i = 0; i < jsonZones.size(); i++) {
            listofzones += sprintf("Zone: %02d %s\n", jsonZones[i].zone_id, jsonZones[i].name.center(30,'-'))
            listofzonesMap << [zone:jsonZones[i].zone_id, name:jsonZones[i].name]
        }
        def timeNow = new Date().format('EEE MMM d, h:mm:ss a',location.timeZone)
        listofzones += sprintf("Zone Map Updated at %s", timeNow)
        log.debug  "listofzonesMap-> ${listofzonesMap}"
        sendEvent(name: "listofzones", value: listofzones)
        return
    }
    def events = []
    def event = parseEventMessage(description)
    //    log.info "==> event -> ${event}"

    // HTTP
    if (event?.body && event?.headers) {
        def slurper = new JsonSlurper()
        String bodyText = new String(event.body.decodeBase64())
        //        log.info "bodyText->${bodyText}"
        def result = slurper.parseText(bodyText)

        log.trace("--- handler.parse: http result=${result}")

        // Update alarm system state.
        if (result.error != null)
            log.error("Error accessing AlarmDecoder API: ${result.error.code} - ${result.error.message}")
        else if (result.panel_armed != null)
            update_state(result).each { e-> events << e }
    }

//    log.debug("--- handler.parse: resulting events=${events}")

    return events
}

/*** Capabilities ***/

def listzonesbutton() { 
    log.debug "-----listzones button pushed"
    listAlarmZones()    
//    def listZones = listAlarmZones()    
//	sendEvent(name: "listZones", value: listZones)
}

def on() {
    log.trace("--- switch.on (arm stay)")
    sendEvent(name: "armed", value: "armed")    // NOTE: Not sure if it's the best way to accomplish it,
                                                //       but solves the weird tile state issues I was having.
    return delayBetween([
        arm_stay(),
        refresh()
    ], 2000)
}

def off() {
    log.trace("--- switch.off (disarm)")

    sendEvent(name: "armed", value: "disarmed") // NOTE: Not sure if it's the best way to accomplish it,
                                                //       but solves the weird tile state issues I was having.
    return delayBetween([
        disarm(),
        refresh()
    ], 2000)
}

def strobe() {
    log.trace("--- alarm.strobe, do nothing")
}

def siren() {
    log.trace("--- alarm.siren, do nothing")
}

def both() {
    log.trace("--- alarm.both (panic)")

    state.panic_started = null;

    return delayBetween([
        panic(),
        refresh()
    ], 2000)
}

def lock() {
    log.trace("--- lock.lock (arm)")

    sendEvent(name: "switch", value: "on")
    sendEvent(name: "armed", value: "armed")    // NOTE: Not sure if it's the best way to accomplish it,
                                                //       but solves the weird tile state issues I was having.

    return delayBetween([
        arm_away(),
        refresh()
    ], 2000)
}

def unlock() {
    log.trace("--- lock.unlock (disarm)")

    sendEvent(name: "switch", value: "off")
    sendEvent(name: "armed", value: "disarmed") // NOTE: Not sure if it's the best way to accomplish it,
                                                //       but solves the weird tile state issues I was having.

    return delayBetween([
        disarm(),
        refresh()
    ], 2000)
}

def refresh() {
    //    log.trace("--- handler.refresh")

    def urn = getDataValue("urn")
    def apikey = _get_api_key()

    return hub_http_get(urn, "/api/v1/alarmdecoder?apikey=${apikey}")
}

def listAlarmZones() {
    log.trace("--- handler.listAlarmZones")

    def urn_new = getDataValue("urn")
    def apikey = _get_api_key()

    return hub_http_get_zones(urn_new, "/api/v1/zones?apikey=${apikey}")
}

/*** Commands ***/

def disarm() {
    log.trace("--- disarm")
    sendEvent(name: "switch", value: "off")

    def user_code = _get_user_code()
    def keys = ""

    if (settings.panel_type == "ADEMCO")
        keys = "${user_code}1"
    else if (settings.panel_type == "DSC")
        keys = "${user_code}"
    else
        log.warn("--- disarm: unknown panel_type.")

    return send_keys(keys)
}

def arm_away() {
    log.trace("--- arm_away")
    sendEvent(name: "switch", value: "on")

    def user_code = _get_user_code()
    def keys = ""

    if (settings.panel_type == "ADEMCO")
        keys = "${user_code}2"
    else if (settings.panel_type == "DSC")
        keys = "<S5>"
    else
        log.warn("--- arm_away: unknown panel_type.")

    return send_keys(keys)
}

def arm_stay() {
    log.trace("--- arm_stay")
    sendEvent(name: "switch", value: "on")

    def user_code = _get_user_code()
    def keys = ""

    if (settings.panel_type == "ADEMCO")
        keys = "${user_code}3"
    else if (settings.panel_type == "DSC")
        keys = "<S4>"
    else
        log.warn("--- arm_stay: unknown panel_type.")

    return send_keys(keys)
}

def panic() {
    log.trace("--- panic")

    def keys = "<S2>"
    return send_keys(keys)
}

def panic1() {
    state.panic_started = new Date().time

    runIn(10, checkPanic);

    log.trace("Panic stage 1: ${state.panic_started}")
}

def panic2() {
    state.panic_started = new Date().time

    runIn(10, checkPanic);

    log.trace("Panic stage 2: ${state.panic_started}")
}

def checkPanic() {
    log.trace("checkPanic");
    if (state.panic_started != null && new Date().time - state.panic_started >= 10) {
        sendEvent(name: "panic_state", value: "default", isStateChange: true);
        log.trace("clearing panic");
    }
}
/*** Business Logic ***/

def update_state(data) {
//    log.info "--- update_state(data) -> ${data}"
//    log.info "==>last_message_received-> ${data.last_message_received.split(',')[3]}"

    def events = []
    def armed = data.panel_armed || (data.panel_armed_stay != null && data.panel_armed_stay == true)
    def panel_state = armed ? "armed" : "disarmed"

    if (data.panel_alarming)
        panel_state = "alarming"
    if (data.panel_fire_detected)  // NOTE: Fire overrides alarm since it's definitely more serious.
        panel_state = "fire"

//	log.info "data.panel_on_battery: ${data.panel_on_battery}"
//	log.info "data.panel_ready: ${data.panel_ready}"
    
    events << createEvent(name: "panel_on_battery", value: data.panel_on_battery?"1":"0")
    events << createEvent(name: "lock", value: data.panel_armed ? "locked" : "unlocked")
    events << createEvent(name: "armed", value: armed ? "armed" : "disarmed", displayed: false)
    events << createEvent(name: "alarm", value: data.panel_alarming ? "both" : "off")
    events << createEvent(name: "smoke", value: data.panel_fire_detected ? "detected" : "clear")
    events << createEvent(name: "panel_state", value: panel_state)

    // Create an event to notify Smart Home Monitor.
    def alarm_status = "off"
    if (armed)
    {
        alarm_status = "away"
        if (data.panel_armed_stay == true)
        alarm_status = "stay"
        if (state.panel_state=="disarmed") {
            def timeNow = new Date().format('EEE MMM d, h:mm:ss a',location.timeZone)
            log.info "Arm State Date updated to ${timeNow}"
            data.panel_ready = sprintf("Armed %s at %s", alarm_status.toUpperCase(), timeNow)
            events << createEvent(name: "panel_ready", value: data.panel_ready)
        }
    } else {
        def panel_message = data.last_message_received.split(',')[3]
        if (panel_message.contains('FAULT')) {
            data.panel_ready = panel_message.replace("\"", "").trim().toLowerCase().split()*.capitalize().join(" ")
            log.info "data.panel_ready-> ${data.panel_ready}"
        } else {
            data.panel_ready = data.panel_ready?"Ready to Arm":"Not Ready to Arm"
        }
        events << createEvent(name: "panel_ready", value: data.panel_ready)
    }
    events << createEvent(name: "alarmStatus", value: alarm_status, isStateChange: true, displayed: false)
    sendEvent(name: "switch", value: armed ? "on" : "off")

    def zone_events = build_zone_events(data)
    events = events.plus(zone_events)

    // Set new states.
    state.panel_state = panel_state
    state.fire = data.panel_fire_detected
    state.alarming = data.panel_alarming
    state.armed = data.panel_armed

    return events
}

/*** Utility ***/

private def build_zone_events(data) {
    def events = []

    // TODO: probably remove this.
    if (state.faulted_zones == null)
        state.faulted_zones = []

    //log.trace("Previous faulted zones: ${state.faulted_zones}")

    def current_faults = data.panel_zones_faulted
    def number_of_zones_faulted = current_faults.size()

    def new_faults = current_faults.minus(state.faulted_zones)
    def cleared_faults = state.faulted_zones.minus(current_faults)

    //log.trace("Current faulted zones: ${temp_faultedzones}")
    //log.trace("New faults: ${new_faults}")
    //log.trace("Cleared faults: ${cleared_faults}")

    // Trigger switches for newly faulted zones.
    for (def i = 0; i < new_faults.size(); i++)
    {
        //log.trace("Setting switch ${new_faults[i]}")
        def switch_events = update_zone_switches(new_faults[i], true)
        events = events.plus(switch_events)
    }

    // Reset switches for cleared zones.
    for (def i = 0; i < cleared_faults.size(); i++)
    {
        //log.trace("Clearing switch ${cleared_faults[i]}")
        def switch_events = update_zone_switches(cleared_faults[i], false)
        events = events.plus(switch_events)
    }

    //log.trace("Filling zone tiles..")

    // Fill zone tiles
    for (def i = 1; i <= 12; i++) {
        if (number_of_zones_faulted > 0 && i <= number_of_zones_faulted) {
            if (device.currentValue("zoneStatus${i}") != current_faults[i-1])
                events << createEvent(name: "zoneStatus${i}", value: current_faults[i-1], displayed: false)
        }
        else {
            if (device.currentValue("zoneStatus${i}") != null)
                events << createEvent(name: "zoneStatus${i}", value: "", displayed: false)  // HACK: Setting this to an empty string has less issues
                                                                                            //       than setting it to null.  Setting it to null
                                                                                            //       results in a NumberFormatException after installing
                                                                                            //       a new version of the device type.
        }
    }

    state.faulted_zones = current_faults

    return events
}

private def update_zone_switches(zone, faulted) {
    def events = []

    // Iterate through the zone tracker settings.  If the zone number matches,
    // trigger an event for the service manager to use to flip the virtual
    // switches.
    for (def i = 1; i <= 12; i++) {
        if (zone == settings."zonetracker${i}zone") {
            if (faulted)
            events << createEvent(name: "zone-on", value: i, isStateChange: true, displayed: false)
            else
                events << createEvent(name: "zone-off", value: i, isStateChange: true, displayed: false)
        }
    }

    return events
}

private def parseEventMessage(String description) {
    def event = [:]
    def parts = description.split(',')

    parts.each { part ->
        part = part.trim()
        if (part.startsWith('devicetype:')) {
            def valueString = part.split(":")[1].trim()
            event.devicetype = valueString
        }
        else if (part.startsWith('mac:')) {
            def valueString = part.split(":")[1].trim()
            if (valueString) {
                event.mac = valueString
            }
        }
        else if (part.startsWith('networkAddress:')) {
            def valueString = part.split(":")[1].trim()
            if (valueString) {
                event.ip = valueString
            }
        }
        else if (part.startsWith('deviceAddress:')) {
            def valueString = part.split(":")[1].trim()
            if (valueString) {
                event.port = valueString
            }
        }
        else if (part.startsWith('ssdpPath:')) {
            part -= "ssdpPath:"
            def valueString = part.trim()
            if (valueString) {
                event.ssdpPath = valueString
            }
        }
        else if (part.startsWith('ssdpUSN:')) {
            part -= "ssdpUSN:"
            def valueString = part.trim()
            if (valueString) {
                event.ssdpUSN = valueString
            }
        }
        else if (part.startsWith('ssdpTerm:')) {
            part -= "ssdpTerm:"
            def valueString = part.trim()
            if (valueString) {
                event.ssdpTerm = valueString
            }
        }
        else if (part.startsWith('headers')) {
            part -= "headers:"
            def valueString = part.trim()
            if (valueString) {
                event.headers = valueString
            }
        }
        else if (part.startsWith('body')) {
            part -= "body:"
            def valueString = part.trim()
            if (valueString) {
                event.body = valueString
            }
        }
    }

    event
}

def send_keys(keys) {
    log.trace("--- send_keys: keys=${keys}")

    def urn = getDataValue("urn")
    def apikey = _get_api_key()

    return hub_http_post(urn, "/api/v1/alarmdecoder/send?apikey=${apikey}", """{ "keys": "${keys}" }""")
}

def hub_http_get(host, path) {
    log.trace "--- hub_http_get: host=${host}, path=${path}"

    def httpRequest = [
        method:     "GET",
        path:       path,
        headers:    [ HOST: host ]
    ]

    return new physicalgraph.device.HubAction(httpRequest, "${host}")
}

def hub_http_post(host, path, body) {
    log.trace "--- hub_http_post: host=${host}, path=${path}"

    def httpRequest = [
        method:     "POST",
        path:       path,
        headers:    [ HOST: host, "Content-Type": "application/json" ],
        body:       body
    ]

    return new physicalgraph.device.HubAction(httpRequest, "${host}")
}

def _get_user_code() {
    def user_code = settings.user_code

    return user_code
}

def _get_api_key() {
    def api_key = settings.api_key

    return api_key
}

def hub_http_get_zones(host, path) {
    log.trace "--- hub_http_get_zones: host=${host}, path=${path}"

    def httpRequestZones = [
        method:     "GET",
        path:       path,
        headers:    [ HOST: host ]
    ]

    return new physicalgraph.device.HubAction(httpRequestZones, "${host}")
}

var App2AndroidBridge = {};
var Android2App = {};

var setDeviceInfoCallback = null;

Android2App.showOrder = null;
Android2App.approveOrder = null;
Android2App.cancelOrder = null;
Android2App.holdOrder = null;


App2AndroidBridge.callPhoneNumber = function(phoneNumber) {
    var bridge = window.App2Android;

    if(bridge != null && bridge != undefined) {
        bridge.callPhoneNumber(phoneNumber);
    }
};

App2AndroidBridge.setAuthentication = function(username, password) {
    var bridge = window.App2Android;

    if(bridge != null && bridge != undefined) {
        bridge.setAuthentication(username, password);
    }
};

App2AndroidBridge.addToContacts = function(person) {
    var bridge = window.App2Android;

    if(bridge != null && bridge != undefined) {
        var info = {
            firstName: person.firstName,
            lastName: person.lastName,
            officePhone: person.phoneOffice,
            mobilePhone: person.phoneMobile
        };
        bridge.addToContacts(JSON.stringify(info));
    }
};

App2AndroidBridge.isPhoneAvailable = function() {
    var available = false;

    var bridge = window.App2Android;
    if(bridge != null && bridge != undefined) {
        available = true;
    }

    return available;
};

App2AndroidBridge.logout = function(person) {
    var bridge = window.App2Android;

    if(bridge != null && bridge != undefined) {
        bridge.logout();
    }
};

App2AndroidBridge.getDeviceInfo = function(callback) {
    var bridge = window.App2Android;
    if(bridge != null && bridge != undefined) {
        setDeviceInfoCallback = callback;
        bridge.getDeviceInfo();
    }
};

Android2App.setDeviceInfo = function(json) {
    if(setDeviceInfoCallback != null) {
        setDeviceInfoCallback(json);
        setDeviceInfoCallback = null;
    }
};

var App2AndroidBridge = {};
var Android2App = {};

var setDeviceInfoCallback = null;
var setCameraAvailableCallback = null;
var captureImageCallback = null;


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

App2AndroidBridge.captureImage = function(callback) {
    var bridge = window.App2Android;
    if(bridge != null && bridge != undefined) {
        captureImageCallback = callback;
        bridge.captureImage();
    }
};

App2AndroidBridge.isCameraAvailable = function(callback) {
    var bridge = window.App2Android;
    if(bridge != null && bridge != undefined) {
        setCameraAvailableCallback = callback;
        bridge.isCameraAvailable();
    }
};

Android2App.setDeviceInfo = function(json) {
    if(setDeviceInfoCallback != null) {
        setDeviceInfoCallback(json);
        setDeviceInfoCallback = null;
    }
};

Android2App.setCameraAvailable = function(available) {
    if(setCameraAvailableCallback != null) {
        setCameraAvailableCallback(available);
        setCameraAvailableCallback = null;
    }
};

window.sendImage = function(imgData) {
    if(captureImageCallback != null) {
        captureImageCallback(imgData);
        captureImageCallback = null;
    }
};

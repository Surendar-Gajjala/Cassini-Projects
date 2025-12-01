this["__ws_debug"] =
/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// identity function for calling harmony imports with the correct context
/******/ 	__webpack_require__.i = function(value) { return value; };
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 29);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var index_1 = __webpack_require__(59);
var index_2 = __webpack_require__(52);
var app_settings_1 = __webpack_require__(4);
var pointer_1 = __webpack_require__(43);
var app_states_1 = __webpack_require__(5);
var mobile_native_loading_1 = __webpack_require__(42);
var XMLParser = __webpack_require__(11);
var mobile_print_modal_1 = __webpack_require__(44);
var ui = __webpack_require__(0);
var app = __webpack_require__(29);
var SVG_NS = 'http://www.w3.org/2000/svg';
var XLINK_NS = 'http://www.w3.org/1999/xlink';
;
function calculateFontColor(color) {
    var rgb = color.substr(1);
    var int = parseInt(rgb, 16);
    var r = ((int >> 16) & 255) / 255;
    var g = ((int >> 8) & 255) / 255;
    var b = (int & 255) / 255;
    var rgbArr = [r, g, b];
    for (var j = 0; j < rgbArr.length; j++) {
        if (rgbArr[j] <= 0.03928) {
            rgbArr[j] = rgbArr[j] / 12.92;
        }
        else {
            rgbArr[j] = Math.pow((rgbArr[j] + 0.055) / 1.055, 2.4);
        }
    }
    var luminance = 0.2126 * rgbArr[0] + 0.7152 * rgbArr[1] + 0.0722 * rgbArr[2];
    var fontColor = (luminance > 0.179) ? 'black' : 'white';
    return fontColor;
}
exports.calculateFontColor = calculateFontColor;
function applyIndent(el, level) {
    el.style.marginLeft = (level * 10) + 'px';
}
exports.applyIndent = applyIndent;
exports.mobileSettings = {
    contextMenuStyle: 'radial-menu',
    activePath: []
};
exports.mockCtrl = false;
var wvApp;
var wvRibbon;
var uiBlocker;
var uiBlockerText;
var wvCanvas;
var wvDock;
var nativeLoadingModal;
var nativeLoadingModalElement;
var statusDialog;
var statusDialogTitle;
var statusDialogRowWrapper;
var statusDialogOpened;
exports.paneCloseTouch = false;
function setPaneCloseTouch(state) {
    exports.paneCloseTouch = state;
}
exports.setPaneCloseTouch = setPaneCloseTouch;
function toggleTooltipClick() {
    exports.showTooltips = !exports.showTooltips;
}
exports.toggleTooltipClick = toggleTooltipClick;
var leftPaneMenu;
var rightPaneMenu;
var rightBadgeOffset = 0;
exports.interactionBadges = [];
var paneBadges = [];
exports.paneBadgeIdx = [];
var localLeftMoreBadge = [];
var pointerWrapper;
var documentBarWrapper;
var pagePos;
var pageCountEl;
var lastPage;
var extraLayerBadge;
var sliderWrapper;
var sliderVal;
var lastSendSliderVal = 0;
var sliderHandlePos = 0;
var stopExplosion = false;
;
var flyXDirection;
var flyYDirection;
var canvasControlRatio;
var lastDirection = [];
var maxSpeed;
var speedPos;
var flightDirection = 'front';
exports.flythroughMouseButton = 'Left';
var flyActive = false;
var speedChangeTouch = false;
var flythroughSpeed;
exports.translationObjects = [];
function translate(langKey, lang) {
    if (!lang) {
        lang = app_settings_1.settings.lang;
    }
    if (_lang_data[langKey] && _lang_data[langKey][lang]) {
        return _lang_data[langKey][lang];
    }
    else if (_custom_lang_data[langKey] && _custom_lang_data[langKey][lang]) {
        return _custom_lang_data[langKey][lang];
    }
    else if (_lang_data[langKey] && _lang_data[langKey]['ENU']) {
        return _lang_data[langKey]['ENU'];
    }
    else if (_custom_lang_data[langKey] && _custom_lang_data[langKey]['ENU']) {
        return _custom_lang_data[langKey]['ENU'];
    }
    return langKey;
}
exports.translate = translate;
function resizeClient(viewportWidth, viewportHeight) {
    if (document.body.clientWidth / 2 > 300) {
        $(exports.leftPaneWrapper).width(document.body.clientWidth / 2);
        $(exports.rightPaneWrapper).width(document.body.clientWidth / 2);
    }
    else {
        $(exports.leftPaneWrapper).width(300);
        $(exports.rightPaneWrapper).width(300);
    }
    if (exports.paneController.leftDefaultAdapter.openPane === 'wv-file') {
        $(exports.leftPaneWrapper).css('left', 'calc(-90%)');
    }
    viewportWidth = parseInt(viewportWidth.split('px')[0]);
    viewportHeight = parseInt(viewportHeight.split('px')[0]);
    vsClient.resize(viewportWidth, viewportHeight);
    exports.ribbon.resize();
    calculateLeftBadgePositions();
    calculateRightBadgePositions();
    $('.mobile-pane-menu').css('max-height', wvDock.clientHeight - 10);
    pointerWrapper.resize();
    if (nativeLoadingModalElement) {
        nativeLoadingModalElement.resize();
    }
    var mobileBrowser = $('#mobile-browser-outer-wrapper');
    if (mobileBrowser.length > 0) {
        var outerWrapper = $('#mobile-browser-outer-wrapper')[0];
        outerWrapper.style.height = 'calc(100% - ' + ($('#mobile-browser-footer').outerHeight() + 40) + 'px)';
    }
}
function resizeHandler() {
    wvApp = document.getElementById('wv-app');
    wvRibbon = document.getElementById('wv-ribbon');
    wvDock = document.getElementById('wv-dock');
    wvDock.style.height = window.innerHeight - wvRibbon.offsetHeight + 'px';
    wvDock.style.width = window.innerWidth + 'px';
    wvCanvas.style.height = wvDock.style.height;
    resizeClient(wvDock.style.width, wvDock.style.height);
}
exports.resizeHandler = resizeHandler;
function hideAll() {
    $(exports.leftPaneWrapper).css('left', 'calc(-90%)');
    $(exports.rightPaneWrapper).css('right', 'calc(-90%)');
    leftPaneMenu.style.pointerEvents = 'none';
    leftPaneMenu.style.opacity = '0';
    rightPaneMenu.style.pointerEvents = 'none';
    rightPaneMenu.style.opacity = '0';
    statusDialog.style.opacity = '0';
}
exports.hideAll = hideAll;
function mapCountry(lang) {
    switch (lang) {
        case 'ENU':
            return 'en';
        case 'DEU':
            return 'de';
        case 'ITA':
            return 'it';
        case 'ESP':
            return 'es';
        case 'FRA':
            return 'fr';
        case 'KOR':
            return 'kor';
        case 'CHS':
            return 'zh';
        case 'JPN':
            return 'ja';
        default:
            return 'en';
    }
}
exports.mapCountry = mapCountry;
function init() {
    var _this = this;
    var contextMenuStyle = localStorage.getItem('MobileWVContextMenuStyle');
    if (contextMenuStyle !== null) {
        exports.mobileSettings.contextMenuStyle = contextMenuStyle;
    }
    window.addEventListener('touchstart', function (ev) {
        if (ev.target !== vsClient.vsCanvas.canvas2D && ev.target !== vsClient.vsCanvas.canvasGL && (ev instanceof TouchEvent && ev.touches.length > 1)) {
            ev.preventDefault();
        }
    });
    wvApp = document.getElementById('wv-app');
    wvRibbon = document.getElementById('wv-ribbon');
    wvRibbon.className = 'mobile-ribbon';
    wvDock = document.getElementById('wv-dock');
    wvDock.style.height = window.innerHeight - wvRibbon.offsetHeight + 'px';
    wvDock.style.width = window.innerWidth + 'px';
    wvCanvas = document.createElement('div');
    wvCanvas.id = 'wv-canvas';
    wvCanvas.style.height = wvDock.style.height;
    wvCanvas.style.width = '100%';
    $(wvCanvas).kendoTouch({
        minHold: 400,
        hold: function (e) {
            if (app_states_1.appStates.interactionMode.indexOf('Flythrough') === -1) {
                if (exports.mobileSettings.contextMenuStyle === 'radial-menu') {
                    if ($('#mobile-radial-contetxt-menu').length > 0) {
                        $('#mobile-radial-contetxt-menu').remove();
                    }
                    var radialMenuContainer_1 = document.createElement('div');
                    radialMenuContainer_1.id = 'mobile-radial-contetxt-menu';
                    var radialMenuWrapper = document.createElement('ul');
                    radialMenuContainer_1.style.position = 'absolute';
                    radialMenuContainer_1.style.left = e.touch.x.location - 100 + 'px';
                    radialMenuContainer_1.style.top = e.touch.y.location - 100 + 'px';
                    radialMenuWrapper.className = 'mobile-radial-context-menu';
                    radialMenuContainer_1.appendChild(radialMenuWrapper);
                    var menuLength = _MobileRadialMenuEntry.length;
                    var circleD = 200 * Math.PI / menuLength;
                    var innerCircle = document.createElement('div');
                    innerCircle.className = 'mobile-radial-context-menu-inner-circle';
                    radialMenuContainer_1.appendChild(innerCircle);
                    document.body.appendChild(radialMenuContainer_1);
                    var _loop_2 = function (i) {
                        var item = _MobileRadialMenuEntry[i];
                        var slice = document.createElement('li');
                        slice.className = 'mobile-radial-context-menu-slice';
                        var angle = 360 / menuLength;
                        slice.style.transform = 'rotate(' + (67.5 + (i * angle)) + 'deg) skew(45deg)';
                        var sliceInnerWrapper = document.createElement('div');
                        sliceInnerWrapper.className = 'mobile-radial-context-menu-slice-inner-wrapper';
                        slice.appendChild(sliceInnerWrapper);
                        sliceInnerWrapper.id = 'mobile-radial-context-menu-slice_' + i;
                        var sliceText = document.createElement('span');
                        sliceText.className = 'mobile-radial-context-menu-slice-text';
                        if ((67.5 + i * angle) > 157.5 && (67.5 + i * angle) < 337.5) {
                            sliceText.style.transform = 'rotate(180deg)';
                        }
                        sliceText.id = item.langId;
                        sliceText.textContent = translate(item.langId, app_settings_1.settings.lang);
                        sliceText.style.maxWidth = circleD - 10 + 'px';
                        exports.translationObjects.push(sliceText);
                        sliceInnerWrapper.appendChild(sliceText);
                        var contentWrapper = document.createElement('div');
                        contentWrapper.className = 'mobile-radial-context-menu-slice-content-wrapper';
                        contentWrapper.style.transform = 'rotate(-' + (i * angle) + 'deg)';
                        sliceInnerWrapper.appendChild(contentWrapper);
                        var iconWrapper = document.createElement('div');
                        iconWrapper.className = 'mobile-radial-context-menu-slice-icon';
                        var icon = document.createElementNS(SVG_NS, 'svg');
                        icon.style.width = '20px';
                        icon.style.height = '20px';
                        var useIcon_1 = document.createElementNS(SVG_NS, 'use');
                        useIcon_1.setAttributeNS(XLINK_NS, 'href', '#' + item.iconId);
                        icon.appendChild(useIcon_1);
                        iconWrapper.appendChild(icon);
                        contentWrapper.appendChild(iconWrapper);
                        radialMenuWrapper.appendChild(slice);
                        $(sliceInnerWrapper).click(function (ev) {
                            item.function(vsClient);
                        });
                    };
                    for (var i = 0; i < menuLength; i++) {
                        _loop_2(i);
                    }
                    $(document).click(function (e) {
                        if (!$(e.target).hasClass('mobile-radial-context-menu-slice-inner-wrapper')) {
                            $(radialMenuContainer_1).remove();
                        }
                    });
                }
                else if (exports.mobileSettings.contextMenuStyle === 'list-menu') {
                    var ev = {
                        pageX: e.touch.x.location,
                        pageY: e.touch.y.location
                    };
                    vsClient.vsCanvas.rightClickTimeStamp = e.event.timeStamp;
                }
            }
        },
        drag: function (e) {
            e.preventDefault();
            if ($('#mobile-radial-contetxt-menu').length > 0) {
                e.event.preventDefault();
                e.event.stopImmediatePropagation();
                var radialMenu = $('#mobile-radial-contetxt-menu')[0];
                var highestElement = document.elementFromPoint(e.touch.x.location, e.touch.y.location);
                if (highestElement === radialMenu || $.contains(radialMenu, highestElement)) {
                    if ($(highestElement).hasClass('mobile-radial-context-menu-slice-inner-wrapper')) {
                        if (!$(highestElement).hasClass('mobile-radial-context-menu-slice-active')) {
                            $('.mobile-radial-context-menu-slice-inner-wrapper').removeClass('mobile-radial-context-menu-slice-active');
                            $(highestElement).addClass('mobile-radial-context-menu-slice-active');
                        }
                    }
                    else {
                        $('.mobile-radial-context-menu-slice-inner-wrapper').removeClass('mobile-radial-context-menu-slice-active');
                    }
                }
            }
        },
        dragend: function (e) {
            if ($('#mobile-radial-contetxt-menu').length > 0) {
                var radialMenu = $('#mobile-radial-contetxt-menu')[0];
                var highestElement = document.elementFromPoint(e.touch.x.location, e.touch.y.location);
                if (highestElement === radialMenu || $.contains(radialMenu, highestElement)) {
                    if ($(highestElement).hasClass('mobile-radial-context-menu-slice-inner-wrapper')) {
                        try {
                            var index = parseInt(highestElement.id.replace('mobile-radial-context-menu-slice_', ''));
                            var item = _MobileRadialMenuEntry[index];
                            item.function(vsClient);
                            $(radialMenu).remove();
                        }
                        catch (e) { }
                    }
                }
            }
        }
    });
    wvCanvas.addEventListener('contextmenu', function (ev) {
        ev.preventDefault();
        if (exports.mobileSettings.contextMenuStyle === 'radial-menu') {
            return;
        }
        $('#contextMenu').remove();
        $(document).off('mousedown', vsClient.vsCanvas.closeContextMenu);
        $(document).off('keyup', vsClient.vsCanvas.closeContextMenu);
        vsClient.vsCanvas.createContextMenu(ev);
    });
    wvDock.appendChild(wvCanvas);
    window.onresize = resizeHandler;
    if (navigator.platform === 'iPad' && navigator.userAgent.match('CriOS')) {
        window.onorientationchange = function () {
            setTimeout(function (e) {
                resizeHandler();
            }, 200);
        };
    }
    uiBlocker = document.createElement('div');
    uiBlocker.className = 'fullscreen-loading-wrapper';
    uiBlockerText = document.createElement('span');
    uiBlockerText.className = 'fullscreen-loading-text';
    uiBlockerText.textContent = "";
    uiBlocker.appendChild(uiBlockerText);
    var spinnerIcon = document.createElement('i');
    spinnerIcon.className = 'fullscreen-loading-icon fal fa-spinner-third fa-spin';
    uiBlocker.appendChild(spinnerIcon);
    exports.ribbon = new index_1.MobileRibbon(wvRibbon);
    exports.ribbon.initialize();
    exports.leftPaneWrapper = document.createElement('div');
    exports.leftPaneWrapper.className = 'mobile-left-pane';
    wvApp.appendChild(exports.leftPaneWrapper);
    exports.rightPaneWrapper = document.createElement('div');
    exports.rightPaneWrapper.className = 'mobile-right-pane';
    wvApp.appendChild(exports.rightPaneWrapper);
    statusDialog = document.createElement('div');
    statusDialog.className = 'mobile-status-dialog';
    statusDialog.style.opacity = '0';
    statusDialogTitle = document.createElement('div');
    statusDialogTitle.className = 'mobile-status-dialog-title';
    statusDialog.appendChild(statusDialogTitle);
    statusDialogRowWrapper = document.createElement('div');
    statusDialog.appendChild(statusDialogRowWrapper);
    document.body.appendChild(statusDialog);
    exports.paneController = new index_2.MobilePanes(exports.leftPaneWrapper, exports.rightPaneWrapper);
    leftPaneMenu = document.createElement('div');
    leftPaneMenu.style.pointerEvents = 'none';
    leftPaneMenu.style.opacity = '0';
    leftPaneMenu.className = 'mobile-pane-menu';
    leftPaneMenu.style.left = '50px';
    rightPaneMenu = document.createElement('div');
    rightPaneMenu.style.pointerEvents = 'none';
    rightPaneMenu.style.opacity = '0';
    rightPaneMenu.className = 'mobile-pane-menu';
    rightPaneMenu.style.right = '50px';
    wvCanvas.addEventListener('mousedown', function (ev) {
        if (exports.paneController.leftDefaultAdapter.paneEl.style.left === '0px'
            || exports.paneController.rightDefaultAdapter.paneEl.style.right === '0px'
            || exports.ribbon.adapter.levelArr.length > 1) {
            ev.stopImmediatePropagation();
            ev.preventDefault();
            exports.paneCloseTouch = true;
        }
        hideAll();
        exports.ribbon.adapter.closeRibbons();
        exports.paneController.leftDefaultAdapter.clearFileBrowser();
    }, true);
    wvCanvas.addEventListener('touchstart', function (ev) {
        if (exports.paneController.leftDefaultAdapter.paneEl.style.left === '0px'
            || exports.paneController.rightDefaultAdapter.paneEl.style.right === '0px'
            || exports.ribbon.adapter.levelArr.length > 1) {
            ev.stopImmediatePropagation();
            ev.preventDefault();
        }
        hideAll();
        exports.ribbon.adapter.closeRibbons();
        exports.paneController.leftDefaultAdapter.clearFileBrowser();
    }, true);
    $(wvRibbon).click(function () {
        leftPaneMenu.style.pointerEvents = 'none';
        leftPaneMenu.style.opacity = '0';
        rightPaneMenu.style.pointerEvents = 'none';
        rightPaneMenu.style.opacity = '0';
    });
    vsClient.on('@changeLang', function (lang) {
        for (var i = 0; i < exports.translationObjects.length; i++) {
            exports.translationObjects[i].textContent = translate(exports.translationObjects[i].id, lang);
        }
    });
    vsClient.on('ExplodeByDistance', function () {
        if (lastSendSliderVal !== sliderVal) {
            var xml = '<Call Method="ExplodeByDistance"><Position>' + Math.round(sliderVal) + '</Position></Call>';
            vsClient.command.executeXML(xml);
            lastSendSliderVal = sliderVal;
        }
        else {
            stopExplosion = true;
        }
    });
    vsClient.on('ShowMessageBox', function (ev) {
        var ignoredInfo = ['WARN_NO_STATE_CHANGE', 'ERR_SCENE_ID_NOT_FOUND', 'ERR_VIEW_ID_NOT_FOUND', 'ERR_ID_ALREADY_EXISTS'];
        try {
            if (_IgnoredErrors && _IgnoredErrors.length > 0) {
                ignoredInfo = ignoredInfo.concat(_IgnoredErrors);
            }
        }
        catch (e) { }
        var message = {
            code: parseInt(ev.ErrorCode),
            info: ev.ErrorInfo,
            method: ev.Identifier,
            message: _this.translate('ID_' + ev.ErrorInfo),
            type: ev.Type[1]
        };
        if (ignoredInfo.indexOf(message.info) === -1) {
            var title = message.method;
            var code = {
                key: 'code',
                value: message.message
            };
            var content = [code];
            if (message.type === 'SocketError') {
                openStatusDialog(title, content, -1);
            }
            else {
                openStatusDialog(title, content, 1);
            }
        }
    });
    vsClient.on('openPrintDialog', function (ev) {
        var printModal = new mobile_print_modal_1.MobilePrintModal(vsClient);
    });
    var floatingActionButton = document.createElement('div');
    floatingActionButton.className = 'mobile-floating-action-button';
    floatingActionButton.id = 'mobile-floating-action-button';
    var pointerIcon = document.createElementNS(SVG_NS, 'svg');
    pointerIcon.style.marginTop = '8px';
    pointerIcon.style.width = '50px';
    pointerIcon.style.height = '35px';
    var pointerUseIcon = document.createElementNS(SVG_NS, 'use');
    pointerUseIcon.setAttributeNS(XLINK_NS, 'href', '#mobile_UI_picker');
    pointerIcon.appendChild(pointerUseIcon);
    floatingActionButton.appendChild(pointerIcon);
    document.body.appendChild(floatingActionButton);
    exports.leftPaneWrapper.style.maxWidth = 'calc(100% -' + (floatingActionButton.clientWidth + 5) + 'px )';
    exports.rightPaneWrapper.style.maxWidth = 'calc(100% -' + (floatingActionButton.clientWidth + 5) + 'px )';
    $(floatingActionButton).click(function (ev) {
        if ($(floatingActionButton).hasClass('mobile-floating-action-button-active')) {
            $(floatingActionButton).removeClass('mobile-floating-action-button-active');
            pointerWrapper.hide();
        }
        else {
            $(floatingActionButton).addClass('mobile-floating-action-button-active');
            pointerWrapper.show();
        }
    });
    var floatingDeactivateButton = document.createElement('div');
    floatingDeactivateButton.className = 'mobile-floating-deactivate-button';
    floatingDeactivateButton.id = 'mobile-floating-deactivate-button';
    var deactivateIcon = document.createElementNS(SVG_NS, 'svg');
    deactivateIcon.style.marginTop = '8px';
    deactivateIcon.style.width = '50px';
    deactivateIcon.style.height = '35px';
    var deactivateUseIcon = document.createElementNS(SVG_NS, 'use');
    deactivateUseIcon.setAttributeNS(XLINK_NS, 'href', '#deactivate_all');
    deactivateIcon.appendChild(deactivateUseIcon);
    floatingDeactivateButton.appendChild(deactivateIcon);
    $(floatingDeactivateButton).click(function (ev) {
        var xml = '<Call Method="ActivateInteraction"><Interaction>Deactivate_AllInteractions</Interaction></Call>';
        vsClient.command.executeXML(xml);
        var settings = localStorage.getItem('VSWVSettings');
        if (settings === null) {
            settings = localStorage.getItem('VSWVDefaultSettings');
        }
        var settingsObj = XMLParser.parseXML(settings);
        var leftMouseMode = settingsObj.Settings.Scene.Mouse.LeftButtonFunction;
        try {
            leftMouseMode = _MobileTouchControl;
        }
        catch (e) { }
        xml = '<Call Method="SetSceneSettings"><Settings><Scene><Mouse><LeftButtonFunction>' + leftMouseMode + '</LeftButtonFunction></Mouse></Scene></Settings></Call>';
        vsClient.command.executeXML(xml);
        app_states_1.appStates.selectionMode = 'Select';
        xml = '<Call Method="SetSelectionMode"><SelectionMode>' + app_states_1.appStates.selectionMode + '</SelectionMode></Call>';
        vsClient.command.executeXML(xml);
        vsClient.emit('Select', 'Select');
        $('#mobile-text-search-panel').hide();
        hideAll();
        if (ui.paneBadgeIdx.indexOf('wv-quickcompare') !== -1) {
            ui.togglePaneBadge('QuickComparePane');
        }
        var quickCompareButton = ui.ribbon.adapter.getButtonByUID('rQuickCompare');
        if (quickCompareButton) {
            quickCompareButton.highlight(false);
        }
        xml = '<Call Method="ActivateRotation"><Activate>false</Activate></Call>';
        vsClient.command.executeXML(xml);
        var spinButton = exports.ribbon.adapter.getButton('ID_RIBBON_SPIN');
        if (spinButton) {
            spinButton.state.active = false;
            spinButton.highlight(false);
        }
        xml = '<Call Method="StopPlayConfigurationViews"></Call>';
        vsClient.command.executeXML(xml);
    });
    document.body.appendChild(floatingDeactivateButton);
    var floatingFullscreenButton = document.createElement('div');
    floatingFullscreenButton.className = 'mobile-floating-fullscreen-button';
    try {
        if (_MobileRightMoreBadge.length === 0) {
            floatingFullscreenButton.style.right = '5px';
        }
    }
    catch (e) { }
    var fullscreenIcon = document.createElement('i');
    $(fullscreenIcon).addClass('fa fa-expand mobile-more-badge');
    floatingFullscreenButton.appendChild(fullscreenIcon);
    $(floatingFullscreenButton).click(function (ev) {
        if ($(fullscreenIcon).hasClass('fa-expand')) {
            $(fullscreenIcon).removeClass('fa-expand').addClass('fa-compress');
            GoInFullscreen(document.documentElement);
        }
        else {
            $(fullscreenIcon).removeClass('fa-compress').addClass('fa-expand');
            GoOutFullscreen();
        }
    });
    document.body.appendChild(floatingFullscreenButton);
    function GoInFullscreen(element) {
        if (element.requestFullscreen)
            document.body.requestFullscreen();
        else if (element.mozRequestFullScreen)
            element.mozRequestFullScreen();
        else if (element.webkitRequestFullscreen)
            element.webkitRequestFullscreen();
        else if (element.msRequestFullscreen)
            element.msRequestFullscreen();
    }
    function GoOutFullscreen() {
        if (document.exitFullscreen)
            document.exitFullscreen();
        else if (document.webkitExitFullscreen)
            document.webkitExitFullscreen();
    }
    document.body.appendChild(floatingDeactivateButton);
    var floatingCtrlButton = document.createElement('div');
    floatingCtrlButton.className = 'mobile-floating-ctrl-button mobile-more-badge';
    try {
        if (_MobileLeftMoreBadge.length === 0) {
            floatingCtrlButton.style.left = '5px';
        }
    }
    catch (e) { }
    var ctrlIcon = document.createElementNS(SVG_NS, 'svg');
    ctrlIcon.style.height = '40px';
    ctrlIcon.style.verticalAlign = 'middle';
    ctrlIcon.style.width = '20px';
    ctrlIcon.style.height = '20px';
    var useIcon = document.createElementNS(SVG_NS, 'use');
    useIcon.setAttributeNS(XLINK_NS, 'href', '#mobile_UI_multiselect');
    ctrlIcon.appendChild(useIcon);
    floatingCtrlButton.appendChild(ctrlIcon);
    $(floatingCtrlButton).click(function (ev) {
        exports.mockCtrl = !exports.mockCtrl;
        if (exports.mockCtrl) {
            $(floatingCtrlButton).addClass('mobile-badge-highlighted');
        }
        else {
            $(floatingCtrlButton).removeClass('mobile-badge-highlighted');
        }
    });
    document.body.appendChild(floatingCtrlButton);
    try {
        if (_MobileLeftMoreBadge.length > 0) {
            localLeftMoreBadge = _MobileLeftMoreBadge;
        }
    }
    catch (e) { }
    var leftBadgeOffset = 0;
    if (localLeftMoreBadge.length > 0) {
        leftBadgeOffset = 1;
        var leftMoreBadge = document.createElement('div');
        leftMoreBadge.style.left = '5px';
        leftMoreBadge.id = 'left-more-badge';
        $(leftMoreBadge).addClass('mobile-pane-badge');
        leftMoreBadge.style.top = '5px';
        leftMoreBadge.style.textAlign = 'center';
        var ellipsisLeft = document.createElement('i');
        $(ellipsisLeft).addClass('fa fa-ellipsis-v mobile-more-badge');
        leftMoreBadge.appendChild(ellipsisLeft);
        document.body.appendChild(leftMoreBadge);
        fillLeftMoreBadge();
        document.body.appendChild(leftPaneMenu);
        $(leftMoreBadge).click(function (ev) {
            rightPaneMenu.style.pointerEvents = 'none';
            rightPaneMenu.style.opacity = '0';
            if (leftPaneMenu.style.opacity !== '0') {
                leftPaneMenu.style.pointerEvents = 'none';
                leftPaneMenu.style.opacity = '0';
            }
            else {
                leftPaneMenu.style.pointerEvents = 'auto';
                leftPaneMenu.style.opacity = '1';
            }
        });
    }
    try {
        for (var i = 0; i < _MobileBadgesLeft.length; i++) {
            exports.paneBadgeIdx.push(_MobileBadgesLeft[i]);
        }
    }
    catch (e) { }
    calculateLeftBadgePositions();
    rightBadgeOffset = 0;
    try {
        if (_MobileRightMoreBadge.length > 0) {
            rightBadgeOffset = 1;
            var rightMoreBadge = document.createElement('div');
            rightMoreBadge.style.right = '5px';
            rightMoreBadge.id = 'right-more-badge';
            $(rightMoreBadge).addClass('mobile-pane-badge');
            rightMoreBadge.style.top = '5px';
            rightMoreBadge.style.textAlign = 'center';
            var ellipsisRight = document.createElement('i');
            $(ellipsisRight).addClass('fa fa-ellipsis-v mobile-more-badge');
            rightMoreBadge.appendChild(ellipsisRight);
            document.body.appendChild(rightMoreBadge);
            var _loop_1 = function (lm) {
                var paneId = _MobileRightMoreBadge[lm];
                var badge = document.createElement('div');
                badge.id = paneId;
                $(badge).addClass('mobile-pane-menu-badge');
                var icon = document.createElementNS(SVG_NS, 'svg');
                icon.style.height = '40px';
                icon.style.verticalAlign = 'middle';
                icon.style.width = '20px';
                icon.style.height = '20px';
                var useIcon_2 = document.createElementNS(SVG_NS, 'use');
                useIcon_2.setAttributeNS(XLINK_NS, 'href', '#' + paneId);
                icon.appendChild(useIcon_2);
                badge.appendChild(icon);
                rightPaneMenu.appendChild(badge);
                var rightBadgeTitle = document.createElement('span');
                rightBadgeTitle.className = 'mobile-pane-menu-item-title';
                rightBadgeTitle.textContent = translate(paneId, app_settings_1.settings.lang);
                rightBadgeTitle.id = "ID_" + paneId;
                exports.translationObjects.push(rightBadgeTitle);
                badge.appendChild(rightBadgeTitle);
                $(badge).click(function (ev) {
                    rightPaneMenu.style.opacity = '0';
                    rightPaneMenu.style.pointerEvents = 'none';
                    exports.paneController.render($(badge).attr('id'), 'right');
                });
            };
            for (var lm = 0; lm < _MobileRightMoreBadge.length; lm++) {
                _loop_1(lm);
            }
            document.body.appendChild(rightPaneMenu);
            $(rightMoreBadge).click(function (ev) {
                leftPaneMenu.style.pointerEvents = 'none';
                leftPaneMenu.style.opacity = '0';
                if (rightPaneMenu.style.opacity !== '0') {
                    rightPaneMenu.style.pointerEvents = 'none';
                    rightPaneMenu.style.opacity = '0';
                }
                else {
                    rightPaneMenu.style.pointerEvents = 'auto';
                    rightPaneMenu.style.opacity = '1';
                }
            });
        }
    }
    catch (e) { }
    calculateRightBadgePositions();
    pointerWrapper = new pointer_1.MobilePointer(vsClient, wvCanvas);
    return wvCanvas;
}
exports.init = init;
function showSessionRestoreDialog(sessions) {
    var selectedSessions = [];
    var sessionRows = [];
    var overlay = document.createElement('div');
    overlay.className = 'fullscreen-loading-wrapper';
    document.body.appendChild(overlay);
    var sessionBox = document.createElement('div');
    sessionBox.className = 'session-box';
    overlay.appendChild(sessionBox);
    var title = document.createElement('div');
    title.textContent = 'Restore sessions';
    sessionBox.appendChild(title);
    var showAllRow = document.createElement('div');
    showAllRow.className = 'session-select-all-row';
    showAllRow.textContent = 'Select all';
    sessionBox.appendChild(showAllRow);
    var selectAllIcon = document.createElement('i');
    selectAllIcon.className = 'fal fa-square session-select-all-icon';
    showAllRow.onclick = function (e) {
        var iconEl = $(selectAllIcon);
        if (iconEl.hasClass('fa-square')) {
            selectedSessions = sessions.map(function (s) { return s.Id; });
        }
        else {
            selectedSessions = [];
        }
        iconEl.toggleClass('fa-square');
        iconEl.toggleClass('fa-check-square');
        sessionRows.forEach(function (row) {
            var rowEl = $(row);
            rowEl.toggleClass('fa-square');
            rowEl.toggleClass('fa-check-square');
        });
    };
    showAllRow.appendChild(selectAllIcon);
    sessions.forEach(function (session, index) {
        var row = document.createElement('div');
        row.className = 'session-row';
        session.Lifetime = parseInt(session.Lifetime) / 60000;
        var startedMinute = session.Lifetime > 1 ? Math.round(session.Lifetime) + ' minute(s)' : '< 1 minute';
        row.textContent = 'Session ' + (index + 1) + ', started ' + startedMinute + ' ago';
        row.id = session;
        var icon = document.createElement('i');
        icon.className = 'fal fa-square session-select-icon';
        row.onclick = function (e) {
            $(icon).toggleClass('fa-square');
            $(icon).toggleClass('fa-check-square');
            if (selectedSessions.indexOf(session.Id) === -1) {
                selectedSessions.push(session.Id);
            }
            else {
                selectedSessions.splice(selectedSessions.indexOf(session.Id), 1);
            }
        };
        row.appendChild(icon);
        sessionRows.push(icon);
        sessionBox.appendChild(row);
    });
    var continueButton = document.createElement('button');
    continueButton.textContent = 'OK';
    continueButton.className = 'session-continue-button';
    continueButton.onclick = function (e) {
        sessions.forEach(function (session) {
            var xml = '<Request Type="CloseSessions">';
            if (selectedSessions.indexOf(session.Id) === -1) {
                xml += '<Session>' + session.Id + '</Session>';
            }
            xml += '</Request>';
            vsClient.command.executeXML(xml);
        });
        var url = window.location.origin + window.location.pathname.replace('/index.html', '/');
        selectedSessions.forEach(function (session) {
            window.open(url + 'index.html?session=' + session, '_blank');
        });
        selectedSessions = undefined;
        sessionRows = undefined;
        vsClient.connect();
        if (app_states_1.appStates.backendToken) {
            vsClient.SessionToken(app_states_1.appStates.backendToken);
        }
        $(overlay).remove();
    };
    sessionBox.appendChild(continueButton);
}
exports.showSessionRestoreDialog = showSessionRestoreDialog;
function blockUI(descr) {
    uiBlockerText.textContent = descr !== undefined ? descr : '';
    if (!wvApp.contains(uiBlocker)) {
        wvApp.appendChild(uiBlocker);
    }
}
exports.blockUI = blockUI;
function unblockUI() {
    if (wvApp && wvApp.contains(uiBlocker)) {
        wvApp.removeChild(uiBlocker);
    }
}
exports.unblockUI = unblockUI;
function serializedLoad(fileList, init) {
    var isNative;
    if (init) {
        isNative = openFile(fileList.shift());
    }
    else {
        isNative = importFile(fileList.shift());
    }
    if (isNative && fileList.length > 0) {
        vsClient.once('@NativeLoad', function (res) {
            serializedLoad(fileList, false);
        });
    }
    else if (fileList.length > 0) {
        serializedLoad(fileList, false);
    }
}
exports.serializedLoad = serializedLoad;
function openNativeLoadingModal(filename, cb) {
    try {
        if (_NativeLoadingModal) {
            nativeLoadingModal = _NativeLoadingModal;
        }
    }
    catch (e) {
        nativeLoadingModal = false;
    }
    var fileNameParts = filename.toLowerCase().split('.');
    var nativeLoadingSettings = localStorage.getItem('nativeLoadingSettings');
    var nativeLoadingSettingsObject = { rememberMe: false };
    if (nativeLoadingSettings !== null) {
        nativeLoadingSettingsObject = JSON.parse(nativeLoadingSettings);
    }
    var excludedFileFormats = ['3dvs', 'vsxml', 'dxf', 'dwf', 'dwg', 'mil', 'txt', 'xml', 'cg4',
        'catdrawing', 'cgm', 'drw', 'ger', ' tiff', 'bmp', 'webp', '906', '907',
        'gbr', 'cs-ger', 'ss-ger', 'phd', 'gif', 'plt', 'jpeg', 'jpg', 'jp2', 'xls', 'xlsx',
        'ppt', 'pptx', 'doc', 'docx', 'dgn', 'odg', 'odp', 'ods', 'odt', 'png', 'dft', 'slddrw', 'tif'];
    if (excludedFileFormats.indexOf(fileNameParts[fileNameParts.length - 1]) === -1
        && (nativeLoadingSettingsObject && nativeLoadingSettingsObject.rememberMe !== true) && nativeLoadingModal === true) {
        exports.paneController.leftDefaultAdapter.clearFileBrowser();
        if (!nativeLoadingModalElement) {
            nativeLoadingModalElement = new mobile_native_loading_1.MobileLoadingModal(vsClient, filename, cb);
        }
        else {
            nativeLoadingModalElement.setFile(filename);
            nativeLoadingModalElement.setCallback(cb);
        }
        nativeLoadingModalElement.show();
        return true;
    }
    else {
        return false;
    }
}
exports.openNativeLoadingModal = openNativeLoadingModal;
function openFile(file) {
    app_states_1.appStates.fileName = file;
    $('#mobile-floating-deactivate-button').click();
    var showModal = openNativeLoadingModal(file, vsClient.OpenFile.bind(vsClient));
    if (!showModal) {
        vsClient.OpenFile(file);
    }
    return showModal;
}
exports.openFile = openFile;
function importFile(filename) {
    var showModal = openNativeLoadingModal(filename, vsClient.ImportFile.bind(vsClient));
    if (!showModal) {
        vsClient.ImportFile(filename);
    }
    return showModal;
}
exports.importFile = importFile;
function hide2DExtraLayerBadge() {
    $('#mobile-text-search-panel').hide();
    $(documentBarWrapper).hide();
    $(extraLayerBadge).hide();
}
exports.hide2DExtraLayerBadge = hide2DExtraLayerBadge;
function show2DExtraLayerBadge() {
    $('#mobile-text-search-panel').show();
    $(documentBarWrapper).show();
    if ($(extraLayerBadge).length > 0) {
        $(extraLayerBadge).show();
        extraLayerBadge.className = 'mobile-floating-extra-layer-button mobile-badge-highlighted';
    }
    else {
        extraLayerBadge = document.createElement('div');
        extraLayerBadge.id = 'mobile-2D-extra-layer';
        extraLayerBadge.className = 'mobile-floating-extra-layer-button mobile-badge-highlighted';
        var layerIcon = document.createElement('i');
        layerIcon.className = 'fas fa-clone';
        $(extraLayerBadge).off('click').click(function (ev) {
            if ($(extraLayerBadge).hasClass('mobile-badge-highlighted')) {
                $('#mobile-text-search-panel').hide();
                $(documentBarWrapper).hide();
            }
            else {
                $('#mobile-text-search-panel').show();
                $(documentBarWrapper).show();
            }
            $(extraLayerBadge).toggleClass('mobile-badge-highlighted');
        });
        extraLayerBadge.appendChild(layerIcon);
        document.body.appendChild(extraLayerBadge);
    }
}
exports.show2DExtraLayerBadge = show2DExtraLayerBadge;
function hideDocumentBar() {
    $(documentBarWrapper).hide();
}
exports.hideDocumentBar = hideDocumentBar;
function showDocumentBar() {
    var pageCount = app_states_1.appStates.pageCount;
    lastPage = '1';
    if ($('#mobile-document-bar').length > 0) {
        $(documentBarWrapper).show();
        pagePos.value = '1';
        pageCountEl.textContent = '/ ' + app_states_1.appStates.pageCount;
    }
    else {
        documentBarWrapper = document.createElement('div');
        documentBarWrapper.id = 'mobile-document-bar';
        documentBarWrapper.className = 'mobile-document-bar';
        $(documentBarWrapper).empty();
        var prevPage = document.createElement('i');
        prevPage.className = 'fa fa-chevron-left mobile-document-bar-icon';
        $(prevPage).click(function (ev) {
            var xml = '<Call Method="PreviousPage"></Call>';
            vsClient.command.executeXML(xml);
        });
        pagePos = document.createElement('input');
        pagePos.className = 'mobile-document-bar-input';
        pagePos.value = '1';
        $(pagePos).change(function (ev) {
            var xml = '<Call Method="GotoPage"><Page>' + pagePos.value + '</Page></Call>';
            vsClient.command.executeXML(xml);
            vsClient.once('GotoPage', function (res) {
                if (res.Error === 'SUCCESS') {
                    lastPage = pagePos.value;
                }
                else {
                    pagePos.value = lastPage;
                }
            });
        });
        pageCountEl = document.createElement('span');
        pageCountEl.className = 'mobile-document-bar-page-count';
        pageCountEl.textContent = '/ ' + app_states_1.appStates.pageCount;
        var nextPage = document.createElement('i');
        nextPage.className = 'fa fa-chevron-right mobile-document-bar-icon';
        $(nextPage).click(function (ev) {
            var xml = '<Call Method="NextPage"></Call>';
            vsClient.command.executeXML(xml);
        });
        document.body.appendChild(documentBarWrapper);
        documentBarWrapper.appendChild(prevPage);
        documentBarWrapper.appendChild(pagePos);
        documentBarWrapper.appendChild(pageCountEl);
        documentBarWrapper.appendChild(nextPage);
    }
}
exports.showDocumentBar = showDocumentBar;
function pageChange(page) {
    pagePos.value = page;
}
exports.pageChange = pageChange;
function calculateLeftBadgePositions() {
    for (var i = 0; i < paneBadges.length; i++) {
        $(paneBadges[i]).remove();
    }
    paneBadges = [];
    var leftBadgeOffset = 0;
    if (localLeftMoreBadge.length > 0) {
        leftBadgeOffset = 1;
    }
    fillLeftMoreBadge();
    try {
        for (var defaultBadge = _MobileBadgesLeft.length - 1; defaultBadge >= 0; defaultBadge--) {
            var id = _MobileBadgesLeft[defaultBadge];
            exports.paneBadgeIdx.splice(exports.paneBadgeIdx.indexOf(id), 1);
            exports.paneBadgeIdx.unshift(id);
        }
        for (var leftBadge = 0; leftBadge < exports.paneBadgeIdx.length; leftBadge++) {
            var paneId = exports.paneBadgeIdx[leftBadge];
            if (leftBadge * 45 > wvDock.clientHeight - leftBadgeOffset * 45 - (leftBadge + 1 * 45) - 60) {
                if (localLeftMoreBadge.indexOf(paneId) === -1) {
                    localLeftMoreBadge.push(paneId);
                }
                else {
                    localLeftMoreBadge.splice(localLeftMoreBadge.indexOf(paneId), 1);
                }
                fillLeftMoreBadge();
            }
            else {
                createPaneBadge(paneId);
            }
        }
    }
    catch (e) { }
}
function fillLeftMoreBadge() {
    $(leftPaneMenu).empty();
    var _loop_3 = function (lm) {
        var paneId = _MobileLeftMoreBadge[lm];
        var badge = document.createElement('div');
        badge.id = paneId;
        $(badge).addClass('mobile-pane-menu-badge');
        var icon = document.createElementNS(SVG_NS, 'svg');
        icon.style.height = '40px';
        icon.style.verticalAlign = 'middle';
        icon.style.width = '20px';
        icon.style.height = '20px';
        var useIcon = document.createElementNS(SVG_NS, 'use');
        useIcon.setAttributeNS(XLINK_NS, 'href', '#' + paneId);
        icon.appendChild(useIcon);
        badge.appendChild(icon);
        leftPaneMenu.appendChild(badge);
        var leftBadgeTitle = document.createElement('span');
        leftBadgeTitle.className = 'mobile-pane-menu-item-title';
        leftBadgeTitle.textContent = translate(paneId, app_settings_1.settings.lang);
        leftBadgeTitle.id = "ID_" + paneId;
        exports.translationObjects.push(leftBadgeTitle);
        badge.appendChild(leftBadgeTitle);
        $(badge).click(function (ev) {
            leftPaneMenu.style.pointerEvents = 'none';
            leftPaneMenu.style.opacity = '0';
            exports.paneController.render($(badge).attr('id'), 'left');
        });
    };
    for (var lm = 0; lm < localLeftMoreBadge.length; lm++) {
        _loop_3(lm);
    }
}
function calculateRightBadgePositions() {
    var minTakenHeight = 110;
    var interactionSpace = 0;
    if (exports.interactionBadges.length > 0) {
        interactionSpace = 45;
    }
    var ghostSpace = 0;
    if (app_states_1.appStates.selectionMode === 'Ghost') {
        ghostSpace = 45;
    }
    var maxHeight = wvDock.clientHeight;
    var remainingHeight = maxHeight - minTakenHeight - ghostSpace - interactionSpace;
    var unghostBadge = $('#unghostBadge');
    var unghostAllBadge = $('#unghostAllBadge');
    var toggleSelectionModeBadge = $('#toggleSelectionModeBadge');
    var spacePadding = 0;
    if (remainingHeight > 90 && app_states_1.appStates.selectionMode === 'Ghost') {
        remainingHeight -= 90;
        var stackedInteractionsCount = layoutInteractionBadges(remainingHeight + interactionSpace);
        remainingHeight -= stackedInteractionsCount * 45 - interactionSpace;
        remainingHeight -= layoutFloatingMainActionBadges(remainingHeight);
        spacePadding = remainingHeight / 2;
        var posTop = 50 + stackedInteractionsCount * 45 + spacePadding;
        toggleSelectionModeBadge.css({ 'right': '5px', top: posTop + 'px' });
        unghostAllBadge.css({ 'right': '5px', top: posTop + 45 + 'px' });
        unghostBadge.css({ 'right': '5px', top: posTop + 90 + 'px' });
    }
    else if (app_states_1.appStates.selectionMode === 'Ghost') {
        var stackedInteractionsCount = layoutInteractionBadges(remainingHeight + interactionSpace);
        remainingHeight -= stackedInteractionsCount * 45 - interactionSpace;
        remainingHeight -= layoutFloatingMainActionBadges(remainingHeight);
        spacePadding = remainingHeight / 2;
        var posTop = 50 + stackedInteractionsCount * 45 + spacePadding;
        toggleSelectionModeBadge.css({ 'right': '5px', top: posTop + 'px' });
        unghostAllBadge.css({ 'right': '50px', top: posTop + 'px' });
        unghostBadge.css({ 'right': '100px', top: posTop + 'px' });
    }
    else {
        var stackedInteractionsCount = layoutInteractionBadges(remainingHeight);
        remainingHeight -= stackedInteractionsCount * 45;
        remainingHeight -= layoutFloatingMainActionBadges(remainingHeight);
    }
}
function removeFlyControls() {
    var interactions = exports.interactionBadges.length;
    for (var i = 0; i < interactions; i++) {
        var badge = exports.interactionBadges[i];
        if (badge.className.indexOf('wv-flythrough') !== -1) {
            $(badge).show();
        }
    }
    $('#mobile-floating-deactivate-button').removeClass('main-action-flythrough-position');
    $('#mobile-floating-action-button').show();
    $('#mobile-flythrough-controls').remove();
    $('#mobile-flythrough-speed-slider').remove();
}
exports.removeFlyControls = removeFlyControls;
function createFlyControls() {
    var interactions = exports.interactionBadges.length;
    for (var i = 0; i < interactions; i++) {
        var badge = exports.interactionBadges[i];
        if (badge.className.indexOf('wv-flythrough') === -1) {
            $(badge).hide();
        }
        else {
            $(badge).addClass('flythrough-action-badge-position');
        }
    }
    $('#mobile-floating-deactivate-button').addClass('main-action-flythrough-position');
    $('#mobile-floating-action-button').hide();
    var outerDiv = document.createElement('div');
    outerDiv.className = 'mobile-flythrough-outer-circle';
    outerDiv.id = 'mobile-flythrough-controls';
    document.body.appendChild(outerDiv);
    var transparencyWrapper = document.createElement('div');
    transparencyWrapper.className = 'mobile-flythrough-transparency-wrapper';
    outerDiv.appendChild(transparencyWrapper);
    var handle = document.createElement('div');
    handle.className = 'mobile-flythrough-handle';
    outerDiv.appendChild(handle);
    var upIcon = document.createElement('i');
    upIcon.className = 'mobile-flythrough-direction-icon fas fa-caret-up';
    upIcon.style.left = '0';
    upIcon.style.right = '0';
    upIcon.ontouchstart = function (e) {
        e.preventDefault();
        var xml = '<Call Method="ApplyFlythroughCommand"><FlythroughCommand>PanUp</FlythroughCommand><Activate>true</Activate></Call>';
        vsClient.command.executeXML(xml);
    };
    upIcon.ontouchend = function (e) {
        e.preventDefault();
        var xml = '<Call Method="ApplyFlythroughCommand"><FlythroughCommand>PanUp</FlythroughCommand><Activate>false</Activate></Call>';
        vsClient.command.executeXML(xml);
    };
    outerDiv.appendChild(upIcon);
    var rightIcon = document.createElement('i');
    rightIcon.className = 'mobile-flythrough-direction-icon fas fa-caret-right';
    rightIcon.style.top = '55px';
    rightIcon.style.right = '12px';
    outerDiv.appendChild(rightIcon);
    rightIcon.ontouchstart = function (e) {
        e.preventDefault();
        var xml = '<Call Method="ApplyFlythroughCommand"><FlythroughCommand>PanRight</FlythroughCommand><Activate>true</Activate></Call>';
        vsClient.command.executeXML(xml);
    };
    rightIcon.ontouchend = function (e) {
        e.preventDefault();
        var xml = '<Call Method="ApplyFlythroughCommand"><FlythroughCommand>PanRight</FlythroughCommand><Activate>false</Activate></Call>';
        vsClient.command.executeXML(xml);
    };
    var downIcon = document.createElement('i');
    downIcon.className = 'mobile-flythrough-direction-icon fas fa-caret-down';
    downIcon.style.bottom = '0';
    downIcon.style.right = '0';
    downIcon.style.left = '0';
    outerDiv.appendChild(downIcon);
    downIcon.ontouchstart = function (e) {
        e.preventDefault();
        var xml = '<Call Method="ApplyFlythroughCommand"><FlythroughCommand>PanDown</FlythroughCommand><Activate>true</Activate></Call>';
        vsClient.command.executeXML(xml);
    };
    downIcon.ontouchend = function (e) {
        e.preventDefault();
        var xml = '<Call Method="ApplyFlythroughCommand"><FlythroughCommand>PanDown</FlythroughCommand><Activate>false</Activate></Call>';
        vsClient.command.executeXML(xml);
    };
    var leftIcon = document.createElement('i');
    leftIcon.className = 'mobile-flythrough-direction-icon fas fa-caret-left';
    leftIcon.style.top = '55px';
    leftIcon.style.left = '12px';
    outerDiv.appendChild(leftIcon);
    leftIcon.ontouchstart = function (e) {
        e.preventDefault();
        var xml = '<Call Method="ApplyFlythroughCommand"><FlythroughCommand>PanLeft</FlythroughCommand><Activate>true</Activate></Call>';
        vsClient.command.executeXML(xml);
    };
    leftIcon.ontouchend = function (e) {
        e.preventDefault();
        var xml = '<Call Method="ApplyFlythroughCommand"><FlythroughCommand>PanLeft</FlythroughCommand><Activate>false</Activate></Call>';
        vsClient.command.executeXML(xml);
    };
    var innerUpIcon = document.createElement('i');
    innerUpIcon.className = 'mobile-flythrough-inner-direction-icon far fa-angle-up';
    innerUpIcon.style.left = '0';
    innerUpIcon.style.right = '0';
    handle.appendChild(innerUpIcon);
    var innerRightIcon = document.createElement('i');
    innerRightIcon.className = 'mobile-flythrough-inner-direction-icon far fa-angle-right';
    innerRightIcon.style.right = '5px';
    innerRightIcon.style.top = '15px';
    handle.appendChild(innerRightIcon);
    var innerDownIcon = document.createElement('i');
    innerDownIcon.className = 'mobile-flythrough-inner-direction-icon far fa-angle-down';
    innerDownIcon.style.left = '0';
    innerDownIcon.style.right = '0';
    innerDownIcon.style.bottom = '0';
    handle.appendChild(innerDownIcon);
    var innerLeftIcon = document.createElement('i');
    innerLeftIcon.className = 'mobile-flythrough-inner-direction-icon far fa-angle-left';
    innerLeftIcon.style.left = '5px';
    innerLeftIcon.style.top = '15px';
    handle.appendChild(innerLeftIcon);
    $(handle).kendoTouch({
        touchstart: function (e) {
            e.preventDefault();
            flyActive = true;
            canvasControlRatio = mapMouseToFlyControl();
            flyXDirection = e.touch.x.location;
            flyYDirection = e.touch.y.location;
            var mockMouseDown = '<Call Method="MouseDown" Response="false"><X>' + canvasControlRatio.center[0] + '</X><Y>' + canvasControlRatio.center[1];
            mockMouseDown += '</Y><MouseButton>' + exports.flythroughMouseButton + '</MouseButton></Call>';
            vsClient.command.executeXMLMouse(mockMouseDown);
            document.addEventListener('touchend', mockMouseUpForFly);
        },
        drag: function (e) {
            e.preventDefault();
            var deltaX = e.touch.x.location - flyXDirection;
            var deltaY = e.touch.y.location - flyYDirection;
            var x = 50 + deltaX;
            var y = 50 + deltaY;
            if (Math.sqrt(Math.pow(x - 50, 2) + Math.pow(y - 50, 2)) < 50) {
                handle.style.top = 50 + deltaY + 'px';
                handle.style.left = 50 + deltaX + 'px';
                lastDirection = [deltaX, deltaY];
            }
            var moveX = canvasControlRatio.center[0] + lastDirection[0] * canvasControlRatio.widthRatio;
            var moveY = canvasControlRatio.center[1] + lastDirection[1] * canvasControlRatio.heightRatio;
            var mockMouseMove = '<Call Method="MouseMove" Response="true"><X>' + moveX + '</X><Y>' + moveY;
            mockMouseMove += '</Y><MouseButton>None</MouseButton></Call>';
            vsClient.command.executeXMLMouse(mockMouseMove);
        },
        dragend: function (e) {
            e.preventDefault();
            flyActive = false;
            handle.style.top = 50 + 'px';
            handle.style.left = 50 + 'px';
            var mockMouseUp = '<Call Method="MouseUp" Response="false"><X>' + canvasControlRatio.center[0] + '</X><Y>' + canvasControlRatio.center[1];
            mockMouseUp += '</Y><MouseButton>' + exports.flythroughMouseButton + '</MouseButton></Call>';
            vsClient.command.executeXMLMouse(mockMouseUp);
            document.removeEventListener('touchend', mockMouseUpForFly);
        }
    });
    var optSpeed = parseInt(app.sceneSettings.Scene.Flythrough.SpeedFactor);
    maxSpeed = 2 * optSpeed;
    flythroughSpeed = optSpeed;
    var speedSliderWrapper = document.createElement('div');
    speedSliderWrapper.className = 'mobile-flythrough-speed-slider-wrapper';
    speedSliderWrapper.id = 'mobile-flythrough-speed-slider';
    document.body.appendChild(speedSliderWrapper);
    var speedSliderBar = document.createElement('div');
    speedSliderBar.className = 'mobile-flythrough-speed-slider-bar';
    speedSliderWrapper.appendChild(speedSliderBar);
    var centerEl = document.createElement('div');
    centerEl.className = 'mobile-flythrough-speed-slider-bar-center';
    speedSliderBar.appendChild(centerEl);
    var handleEl = document.createElement('div');
    handleEl.className = 'mobile-flythrough-speed-slider-bar-handle';
    speedSliderBar.appendChild(handleEl);
    var speedUpIcon = document.createElement('i');
    speedUpIcon.className = 'far fa-angle-up mobile-flythrough-speed-up-icon';
    handleEl.appendChild(speedUpIcon);
    var speedDownIcon = document.createElement('i');
    speedDownIcon.className = 'far fa-angle-down mobile-flythrough-speed-up-icon';
    speedDownIcon.style.bottom = '0';
    handleEl.appendChild(speedDownIcon);
    var filledBar = document.createElement('div');
    filledBar.className = 'mobile-flythrough-speed-slider-bar-filled';
    speedSliderWrapper.appendChild(filledBar);
    var initPos = getPositionFromSpeed(optSpeed, speedSliderBar);
    handleEl.style.top = initPos + 'px';
    filledBar.style.height = centerEl.getBoundingClientRect().top - handleEl.getBoundingClientRect().bottom + 'px';
    speedPos = initPos;
    $(handleEl).kendoTouch({
        dragstart: function (e) {
            e.preventDefault();
            if (flyActive) {
                speedChangeTouch = true;
            }
            else {
                speedChangeTouch = false;
            }
        },
        drag: function (e) {
            e.preventDefault();
            var delta = speedPos - (e.touch.y.location - speedSliderBar.offsetTop);
            var pos = speedPos - delta;
            if (pos >= -25 && pos <= speedSliderBar.offsetHeight - 25) {
                handleEl.style.top = pos + 'px';
                setSpeedFromPosition(pos + 25, speedSliderBar);
                if (flightDirection === 'front') {
                    filledBar.style.height = centerEl.getBoundingClientRect().top - handleEl.getBoundingClientRect().bottom + 'px';
                    filledBar.style.bottom = 'calc(50% + 9px)';
                    filledBar.style.top = '';
                }
                else {
                    filledBar.style.height = handleEl.getBoundingClientRect().top - centerEl.getBoundingClientRect().bottom + 'px';
                    filledBar.style.top = 'calc(50% - 4px)';
                    filledBar.style.bottom = '';
                }
            }
            if (flyActive) {
                speedChangeTouch = true;
            }
            else {
                speedChangeTouch = false;
            }
        },
        dragend: function (e) {
            e.preventDefault();
            if (flyActive) {
                speedChangeTouch = true;
            }
            else {
                speedChangeTouch = false;
            }
        }
    });
    window.addEventListener('resize', function (ev) {
        if (flightDirection === 'front') {
            var initPosFront = getPositionFromSpeed(flythroughSpeed, speedSliderBar) - 25;
            handleEl.style.top = initPosFront + 'px';
            speedPos = initPosFront;
            filledBar.style.height = centerEl.getBoundingClientRect().top - handleEl.getBoundingClientRect().bottom + 'px';
            filledBar.style.bottom = 'calc(50% + 9px)';
            filledBar.style.top = '';
        }
        else {
            var initPosBack = getPositionFromSpeed(maxSpeed - flythroughSpeed, speedSliderBar) + (speedSliderBar.offsetHeight / 2);
            handleEl.style.top = initPosBack + 'px';
            speedPos = initPosBack;
            filledBar.style.height = handleEl.getBoundingClientRect().top - centerEl.getBoundingClientRect().bottom + 'px';
            filledBar.style.top = 'calc(50% - 4px)';
            filledBar.style.bottom = '';
        }
    });
}
exports.createFlyControls = createFlyControls;
function setSpeedFromPosition(position, bar) {
    var height = bar.offsetHeight;
    var directedHeight = height / 2;
    var positionFactor;
    var direction;
    if (position > directedHeight) {
        direction = 'back';
        positionFactor = (directedHeight - (height - position)) / directedHeight;
        exports.flythroughMouseButton = 'Right';
    }
    else {
        direction = 'front';
        positionFactor = (directedHeight - position) / directedHeight;
        exports.flythroughMouseButton = 'Left';
    }
    if (flyActive && direction !== flightDirection) {
        var mockMouseDown = '<Call Method="MouseDown" Response="false"><X>' + canvasControlRatio.center[0] + '</X><Y>' + canvasControlRatio.center[1];
        mockMouseDown += '</Y><MouseButton>' + exports.flythroughMouseButton + '</MouseButton></Call>';
        vsClient.command.executeXMLMouse(mockMouseDown);
    }
    flightDirection = direction;
    var absoluteSpeed = (1 - positionFactor) * maxSpeed;
    if (absoluteSpeed < 1) {
        absoluteSpeed = 1;
    }
    flythroughSpeed = absoluteSpeed;
    var settingsCall = '<Call Method="SetSceneSettings"><Settings><Scene><Flythrough><SpeedFactor>' + absoluteSpeed + '</SpeedFactor></Flythrough></Scene></Settings></Call>';
    vsClient.command.executeXML(settingsCall);
}
function getPositionFromSpeed(speed, bar) {
    var height = bar.offsetHeight;
    var directedHeight = height / 2;
    var speedFactor = speed / maxSpeed;
    var absolutePos = directedHeight * speedFactor;
    return absolutePos;
}
function mockMouseUpForFly(e) {
    e.preventDefault();
    if (!speedChangeTouch) {
        document.removeEventListener('touchend', mockMouseUpForFly);
        var mockMouseUp = '<Call Method="MouseUp" Response="false"><X>' + canvasControlRatio.center[0] + '</X><Y>' + canvasControlRatio.center[1];
        mockMouseUp += '</Y><MouseButton>Left</MouseButton></Call>';
        flyActive = false;
        vsClient.command.executeXMLMouse(mockMouseUp);
    }
}
function mapMouseToFlyControl() {
    var canvasWidth = vsClient.vsCanvas.width;
    var canvasHeight = vsClient.vsCanvas.height;
    var widthRatio = canvasWidth / 150;
    var heightRatio = canvasHeight / 150;
    var center = [Math.round(canvasWidth / 2), Math.round(canvasHeight) / 2];
    return {
        center: center,
        widthRatio: widthRatio,
        heightRatio: heightRatio
    };
}
function layoutInteractionBadges(availableSpace) {
    var interactionBadgesCount = exports.interactionBadges.length;
    while (availableSpace - 45 > 0 && interactionBadgesCount > 0) {
        var badgeIndex = exports.interactionBadges.length - interactionBadgesCount;
        var interactionBadge = exports.interactionBadges[badgeIndex];
        interactionBadge.style.top = 50 + badgeIndex * 45 + 'px';
        interactionBadge.style.right = '5px';
        availableSpace -= 45;
        interactionBadgesCount--;
    }
    for (var i = 0; i < interactionBadgesCount; i++) {
        var badgeIndex = exports.interactionBadges.length - i - 1;
        var interactionBadge = exports.interactionBadges[badgeIndex];
        interactionBadge.style.top = '50px';
        if (interactionBadgesCount === exports.interactionBadges.length) {
            interactionBadge.style.right = 5 + (interactionBadgesCount - i - 1) * 50 + 'px';
        }
        else {
            interactionBadge.style.right = 5 + (interactionBadgesCount - i) * 50 + 'px';
        }
    }
    return exports.interactionBadges.length - interactionBadgesCount;
}
function layoutFloatingMainActionBadges(availableSpace) {
    var fActionBadge = $('#mobile-floating-action-button');
    var fDeactivateBadge = $('#mobile-floating-deactivate-button');
    var ribbonOffset = (exports.ribbon.adapter.levelArr.length - 1) * 63;
    if (availableSpace > 55) {
        fActionBadge.css({ 'right': '5px', 'bottom': 55 + ribbonOffset + 'px' });
        fDeactivateBadge.css({ 'right': '5px', 'bottom': 110 + ribbonOffset + 'px' });
        return 55;
    }
    else {
        fActionBadge.css({ 'right': '65px', 'bottom': 55 + ribbonOffset + 'px' });
        fDeactivateBadge.css({ 'right': '5px', 'bottom': 55 + ribbonOffset + 'px' });
        return 0;
    }
}
function createInteractionBadge(paneId) {
    var badge = document.createElement('div');
    $(badge).addClass('mobile-pane-badge mobile-badge-highlighted');
    $(badge).addClass(paneId);
    var position = exports.interactionBadges.length;
    var icon = document.createElementNS(SVG_NS, 'svg');
    icon.style.width = '20px';
    icon.style.height = '20px';
    icon.style.marginTop = '10px';
    icon.style.marginLeft = "10px";
    var useIcon = document.createElementNS(SVG_NS, 'use');
    useIcon.setAttributeNS(XLINK_NS, 'href', '#' + paneId);
    icon.appendChild(useIcon);
    badge.appendChild(icon);
    document.body.appendChild(badge);
    exports.interactionBadges.push(badge);
    $(badge).click(function (ev) {
        if (paneId.indexOf('no_pane_') !== 0) {
            exports.paneController.render(paneId, 'right');
        }
    });
    calculateRightBadgePositions();
}
exports.createInteractionBadge = createInteractionBadge;
function togglePaneBadge(paneId) {
    paneId = exports.paneController.mapPaneTemplateToId(paneId);
    if (paneId === 'wv-appsettings') {
        return true;
    }
    var bool = true;
    if (exports.paneBadgeIdx.indexOf(paneId) === -1) {
        var hastLeftMoreBadge = localLeftMoreBadge.length > 0 ? 1 : 0;
        if ((paneBadges.length + 1) * 45 > wvDock.clientHeight - 60) {
            var lastBadge = paneBadges.pop();
            localLeftMoreBadge.push(lastBadge.id.replace('_paneBadge', ''));
            $(lastBadge).remove();
            fillLeftMoreBadge();
        }
        exports.paneBadgeIdx.push(paneId);
        calculateLeftBadgePositions();
    }
    else {
        deletePaneBadge(paneId);
        bool = false;
    }
    return bool;
}
exports.togglePaneBadge = togglePaneBadge;
function createPaneBadge(paneId) {
    var badge = document.createElement('div');
    badge.style.left = '5px';
    $(badge).addClass('mobile-pane-badge');
    badge.id = paneId + '_paneBadge';
    var position = paneBadges.length;
    if (localLeftMoreBadge.length > 0) {
        position++;
    }
    badge.style.top = position * 45 + 5 + 'px';
    var icon = document.createElementNS(SVG_NS, 'svg');
    if (paneId === 'wv-modeltree') {
        icon.style.width = '30px';
        icon.style.height = '30px';
        icon.style.marginTop = '4px';
        icon.style.marginLeft = "9px";
    }
    else {
        icon.style.width = '20px';
        icon.style.height = '20px';
        icon.style.marginTop = '10px';
        icon.style.marginLeft = "10px";
    }
    var useIcon = document.createElementNS(SVG_NS, 'use');
    useIcon.setAttributeNS(XLINK_NS, 'href', '#' + paneId);
    icon.appendChild(useIcon);
    badge.appendChild(icon);
    document.body.appendChild(badge);
    paneBadges.push(badge);
    if (exports.paneBadgeIdx.indexOf(paneId) === -1) {
        exports.paneBadgeIdx.unshift(paneId);
    }
    $(badge).click(function (ev) {
        if (_MobileRightMoreBadge.indexOf(paneId) !== -1) {
            exports.paneController.render(paneId, 'right');
        }
        else {
            exports.paneController.render(paneId, 'left');
        }
    });
}
exports.createPaneBadge = createPaneBadge;
function createFileOpenBadge() {
    var badge = document.createElement('div');
    badge.id = 'mobile-file-open-badge';
    $(badge).addClass('mobile-pane-badge mobile-file-open-badge');
    var icon = document.createElementNS(SVG_NS, 'svg');
    $(icon).addClass('mobile-file-open-badge-icon');
    var useIcon = document.createElementNS(SVG_NS, 'use');
    useIcon.setAttributeNS(XLINK_NS, 'href', '#2_oeffnen');
    icon.appendChild(useIcon);
    badge.appendChild(icon);
    document.body.appendChild(badge);
    $(badge).click(function (ev) {
        vsClient.emit('openFileDialog');
    });
}
exports.createFileOpenBadge = createFileOpenBadge;
function toggleGhostBadges(state, oldSelectionMode) {
    if (state === true) {
        var unghostBadge = document.createElement('div');
        unghostBadge.id = "unghostBadge";
        $(unghostBadge).addClass('mobile-pane-badge');
        var unghostIcon = document.createElementNS(SVG_NS, 'svg');
        unghostIcon.style.width = '30px';
        unghostIcon.style.height = '30px';
        unghostIcon.style.marginTop = '5px';
        unghostIcon.style.marginLeft = "5px";
        var useIcon1 = document.createElementNS(SVG_NS, 'use');
        useIcon1.setAttributeNS(XLINK_NS, 'href', '#unghost_badge');
        unghostIcon.appendChild(useIcon1);
        unghostBadge.appendChild(unghostIcon);
        document.body.appendChild(unghostBadge);
        $(unghostBadge).click(function (ev) {
            var xml = '<Call Method="UnghostLastNode"></Call>';
            vsClient.command.executeXML(xml);
        });
        var unghostAllBadge = document.createElement('div');
        unghostAllBadge.id = "unghostAllBadge";
        $(unghostAllBadge).addClass('mobile-pane-badge');
        var unghostAllIcon = document.createElementNS(SVG_NS, 'svg');
        unghostAllIcon.style.width = '25px';
        unghostAllIcon.style.height = '25px';
        unghostAllIcon.style.marginTop = '8px';
        unghostAllIcon.style.marginLeft = "8px";
        var useIcon2 = document.createElementNS(SVG_NS, 'use');
        useIcon2.setAttributeNS(XLINK_NS, 'href', '#2_ghost_ausschalten');
        unghostAllIcon.appendChild(useIcon2);
        unghostAllBadge.appendChild(unghostAllIcon);
        document.body.appendChild(unghostAllBadge);
        $(unghostAllBadge).click(function (ev) {
            vsClient.command.executeXML('<Call Method="UnGhostAll"></Call>');
        });
        var ghostActive_1 = true;
        var toggleMode_1 = document.createElement('div');
        toggleMode_1.id = "toggleSelectionModeBadge";
        $(toggleMode_1).addClass('mobile-pane-badge mobile-badge-highlighted');
        var toggleModeIcon = document.createElementNS(SVG_NS, 'svg');
        toggleModeIcon.style.width = '25px';
        toggleModeIcon.style.height = '25px';
        toggleModeIcon.style.marginTop = '8px';
        toggleModeIcon.style.marginLeft = "8px";
        var useIcon3 = document.createElementNS(SVG_NS, 'use');
        useIcon3.setAttributeNS(XLINK_NS, 'href', '#7_ghost');
        toggleModeIcon.appendChild(useIcon3);
        toggleMode_1.appendChild(toggleModeIcon);
        document.body.appendChild(toggleMode_1);
        $(toggleMode_1).click(function (ev) {
            if (ghostActive_1) {
                vsClient.command.executeXML('<Call Method="SetSelectionMode"><SelectionMode>' + oldSelectionMode + '</SelectionMode></Call>');
                $(toggleMode_1).removeClass('mobile-badge-highlighted');
            }
            else {
                vsClient.command.executeXML('<Call Method="SetSelectionMode"><SelectionMode>Ghost</SelectionMode></Call>');
                $(toggleMode_1).addClass('mobile-badge-highlighted');
            }
            ghostActive_1 = !ghostActive_1;
        });
        calculateRightBadgePositions();
    }
    else {
        $('#unghostBadge').remove();
        $('#unghostAllBadge').remove();
        $('#toggleSelectionModeBadge').remove();
    }
}
exports.toggleGhostBadges = toggleGhostBadges;
function deleteInteractionBadge(paneId) {
    var index;
    for (var i = 0; i < exports.interactionBadges.length; i++) {
        if ($(exports.interactionBadges[i]).hasClass(paneId)) {
            $(exports.interactionBadges[i]).remove();
            index = i;
        }
        else if (index !== undefined) {
            var position = i - 1;
            exports.interactionBadges[i].style.top = (position + rightBadgeOffset) * 45 + 5 + 'px';
        }
    }
    if (index !== undefined) {
        exports.interactionBadges.splice(index, 1);
        calculateRightBadgePositions();
    }
}
exports.deleteInteractionBadge = deleteInteractionBadge;
function deletePaneBadge(paneId) {
    var changeDetected = false;
    if (exports.paneBadgeIdx.indexOf(paneId) !== -1) {
        changeDetected = true;
        exports.paneBadgeIdx.splice(exports.paneBadgeIdx.indexOf(paneId), 1);
    }
    if (localLeftMoreBadge.indexOf(paneId) !== -1) {
        changeDetected = true;
        localLeftMoreBadge.splice(localLeftMoreBadge.indexOf(paneId), 1);
    }
    if (changeDetected) {
        calculateLeftBadgePositions();
    }
}
exports.deletePaneBadge = deletePaneBadge;
function toast(success) {
    var toastWrapper = document.createElement('div');
    toastWrapper.className = 'toast mobile-toast';
    toastWrapper.style.backgroundColor = success ? 'darkgreen' : 'darkred';
    var toastIcon = document.createElement('i');
    toastIcon.className = success ? 'far fa-check' : 'far fa-exclamation';
    toastWrapper.appendChild(toastIcon);
    document.body.appendChild(toastWrapper);
    setTimeout(function () {
        toastWrapper.remove();
    }, 2000);
}
exports.toast = toast;
function openStatusDialog(title, textRows, statusCode) {
    statusDialog.style.opacity = '0';
    statusDialogOpened = new Date().getDate();
    var refDate = new Date().getDate();
    $(statusDialogRowWrapper).empty();
    switch (statusCode) {
        case 0:
            statusDialogTitle.textContent = translate(title, app_settings_1.settings.lang);
            exports.translationObjects.push(statusDialogTitle);
            for (var i = 0; i < textRows.length; i++) {
                var textRow = document.createElement('div');
                textRow.className = 'mobile-status-dialog-message';
                var key = document.createElement('span');
                key.textContent = textRows[i].key;
                textRow.appendChild(key);
                var separator = document.createElement('span');
                separator.textContent = "  :  ";
                textRow.appendChild(separator);
                var value = document.createElement('span');
                value.textContent = textRows[i].value;
                textRow.appendChild(value);
                statusDialogRowWrapper.appendChild(textRow);
            }
            statusDialog.style.opacity = '1';
            setTimeout(function () {
                if (statusDialogOpened === refDate) {
                    statusDialog.style.opacity = '0';
                    exports.translationObjects.splice(exports.translationObjects.indexOf(statusDialogTitle), 1);
                }
            }, 3000);
            break;
        case 1:
            statusDialogTitle.textContent = title;
            for (var i = 0; i < textRows.length; i++) {
                var textRow = document.createElement('div');
                var text = document.createElement('span');
                if (textRows[i].key === 'code') {
                    $(text).addClass('mobile-status-error-text');
                }
                else {
                    textRow.style.textAlign = 'center';
                }
                text.innerHTML = textRows[i].value.replace(/\\n/g, '\r');
                textRow.appendChild(text);
                statusDialogRowWrapper.appendChild(textRow);
                statusDialog.style.opacity = '1';
                setTimeout(function () {
                    statusDialog.style.opacity = '0';
                }, 4000);
            }
            break;
        case -1: {
            statusDialogTitle.textContent = title;
            for (var i = 0; i < textRows.length; i++) {
                var textRow = document.createElement('div');
                var text = document.createElement('span');
                if (textRows[i].key === 'code') {
                    $(text).addClass('mobile-status-error-text');
                }
                else {
                    textRow.style.textAlign = 'center';
                }
                text.innerHTML = translate(textRows[i].value).replace(/\\n/g, '\r');
                textRow.appendChild(text);
                statusDialogRowWrapper.appendChild(textRow);
                statusDialog.style.opacity = '1';
            }
            break;
        }
    }
}
exports.openStatusDialog = openStatusDialog;
function buildNeighbourhoodSearchPanel() {
    var neighbourhoodSearchPanel = document.createElement('div');
    neighbourhoodSearchPanel.className = 'mobile-neighbourhood-search-panel';
    neighbourhoodSearchPanel.id = 'mobile-neighbourhood-search-panel';
    document.body.appendChild(neighbourhoodSearchPanel);
    var typeSelect = document.createElement('select');
    typeSelect.className = 'mobile-transformation-type';
    typeSelect.id = 'mobile-neighborhood-search-type';
    neighbourhoodSearchPanel.appendChild(typeSelect);
    var optionBox = document.createElement('option');
    optionBox.value = 'Box';
    optionBox.id = 'ID_NEIGHBOURHOOD_SEARCH_SHAPE_BOX';
    optionBox.textContent = translate('ID_NEIGHBOURHOOD_SEARCH_SHAPE_BOX', app_settings_1.settings.lang);
    exports.translationObjects.push(optionBox);
    typeSelect.appendChild(optionBox);
    var optionSphere = document.createElement('option');
    optionSphere.value = 'Sphere';
    optionSphere.id = 'ID_NEIGHBOURHOOD_SEARCH_SHAPE_SPHERE';
    optionSphere.textContent = translate('ID_NEIGHBOURHOOD_SEARCH_SHAPE_SPHERE', app_settings_1.settings.lang);
    exports.translationObjects.push(optionSphere);
    typeSelect.appendChild(optionSphere);
    var optionLinear = document.createElement('option');
    optionLinear.value = 'Linear';
    optionLinear.id = 'ID_NEIGHBOURHOOD_SEARCH_SHAPE_LINEAR';
    optionLinear.textContent = translate('ID_NEIGHBOURHOOD_SEARCH_SHAPE_LINEAR', app_settings_1.settings.lang);
    exports.translationObjects.push(optionLinear);
    typeSelect.appendChild(optionLinear);
    typeSelect.addEventListener('change', function (ev) {
        var val = typeSelect.value;
        var xml = '<Call Method="SetSceneSettings"><Settings><Scene><Tools><NeighbourhoodSearch><Mode><Shape>' + val;
        xml += '</Shape></Mode></NeighbourhoodSearch></Tools></Scene></Settings></Call>';
        vsClient.command.executeXML(xml);
    });
    var computeSearch = document.createElement('div');
    computeSearch.className = 'mobile-neighbourhood-search-compute-button';
    var computeIcon = document.createElement('i');
    computeIcon.className = 'far fa-search mobile-neighbourhood-search-compute-icon';
    computeSearch.appendChild(computeIcon);
    $(computeSearch).click(function (ev) {
        var xml = '<Call Method="ExecuteNeighbourhoodSearch"></Call>';
        vsClient.command.executeXML(xml);
        vsClient.once('ExecuteNeighbourhoodSearch', function (res) {
            if (res.Error === 'SUCCESS') {
                if ($('#UID_NEIGHBOURHOOD_SEARCH_CLOSE_SELECTION_MODE_FLAG').val() === 'true') {
                    vsClient.command.executeXML('<Call Method="SetSelectionMode"><SelectionMode>Select</SelectionMode></Call>');
                    vsClient.emit('Select', 'Select');
                }
            }
        });
    });
    neighbourhoodSearchPanel.appendChild(computeSearch);
}
exports.buildNeighbourhoodSearchPanel = buildNeighbourhoodSearchPanel;


/***/ }),
/* 1 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Retrieve the Unicode code point at the specified index in the specified
 * string.
 *
 * @param {string} str The string from which to retrieve the Unicode code
 *                     point.
 * @param {number} index The specified index.
 *
 * @returns {number} The Unicode code point at the specified position.
 *
 * @private
 */
function getCodePoint(str, index) {
    var size = str.length;
    if (index < 0 || index >= size) {
        return undefined;
    }
    var first = str.charCodeAt(index);
    if (first >= 0xD800 && first <= 0xDBFF && size > index + 1) {
        var second = str.charCodeAt(index + 1);
        if (second >= 0xDC00 && second <= 0xDFFF) {
            return (first - 0xD800) * 0x400 + second - 0xDC00 + 0x10000;
        }
    }
    return first;
}
exports.getCodePoint = getCodePoint;
/**
 * Determines whether a number is an integer.
 *
 * @param {number} value The number to check.
 *
 * @returns {boolean} Whether or not the number is an integer.
 *
 * @private
 */
function isInteger(value) {
    return typeof value === "number" &&
        isFinite(value) &&
        Math.floor(value) === value;
}
exports.isInteger = isInteger;
/**
 * Returns true if the specified value are of any of the specified types, as
 * determined by the Object.prototype.toString.call function.
 *
 * @param {*} val The specified value.
 * @param {...string[]} types The specified types.
 *
 * @returns {boolean} Whether or not the specified value are of any of the
 *                    specified types.
 *
 * @private
 */
function isType(val) {
    var types = [];
    for (var _i = 1; _i < arguments.length; _i++) {
        types[_i - 1] = arguments[_i];
    }
    for (var _a = 0, types_1 = types; _a < types_1.length; _a++) {
        var type = types_1[_a];
        if (Object.prototype.toString.call(val) === "[object " + type + "]") {
            return true;
        }
    }
    return false;
}
exports.isType = isType;


/***/ }),
/* 2 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var utils_1 = __webpack_require__(1);
/**
 * Represents an XML node.
 *
 * This class is the root class of the XML node hierarchy. It should not be
 * directly instantiated; one of its subclasses should be used instead.
 *
 * @protected
 */
var XmlNode = (function () {
    /**
     * Initializes a new instance of the {@link XmlNode} class.
     */
    function XmlNode() {
        this._parent = undefined;
        this._children = [];
    }
    Object.defineProperty(XmlNode.prototype, "parent", {
        /**
         * Gets the parent of this node.
         *
         * @returns {XmlNode} The parent of this node.
         */
        get: function () {
            return this._parent;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Gets this node's children.
     *
     * Throws an exception if this node cannot have any children. Consult the
     * appropriate subclass documentation for more details.
     *
     * @returns {XmlNode[]} This node's children.
     */
    XmlNode.prototype.children = function () {
        return this._children.slice();
    };
    /**
     * Inserts the specified node into this node's children at the specified
     * index. The node is not inserted if it is already present. If this node
     * already has a parent, it is removed from that parent.
     *
     * Throws an exception if this node cannot have any children, or if the
     * specified node cannot be added at the specified index. Consult the
     * appropriate subclass documentation for more details.
     *
     * @param {XmlNode} node   The node to insert.
     * @param {number} [index] The index at which to insert the node. Nodes at
     *                         or after the index are shifted to the right. If
     *                         no index is specified, the node is inserted at
     *                         the end.
     *
     * @returns {XmlNode} The node inserted into this node's children, or
     *                    undefined if no node was inserted.
     */
    XmlNode.prototype.insertChild = function (node, index) {
        if (index === void 0) { index = this._children.length; }
        if (!(node instanceof XmlNode)) {
            throw new TypeError("node should be an instance of XmlNode");
        }
        else if (!utils_1.isType(index, "Number") || !utils_1.isInteger(index)) {
            throw new TypeError("index should be an integer");
        }
        else if (index < 0 || index > this._children.length) {
            throw new RangeError("index should respect children array bounds");
        }
        if (this._children.indexOf(node) === -1) {
            if (!utils_1.isType(node.parent, "Undefined")) {
                node.parent.removeChild(node);
            }
            node._parent = this;
            this._children.splice(index, 0, node);
            return node;
        }
        return undefined;
    };
    /**
     * Gets the node that follows this one, or undefined if no such node
     * exists or if this node has no parent.
     *
     * @returns {XmlNode} The node that follows this one, or undefined if no
     *                    such node exists or if this node has no parent.
     */
    XmlNode.prototype.next = function () {
        if (utils_1.isType(this.parent, "Undefined")) {
            return undefined;
        }
        else if (this.parent._children.indexOf(this) ===
            this.parent._children.length - 1) {
            return undefined;
        }
        return this.parent._children[this.parent._children.indexOf(this) + 1];
    };
    /**
     * Gets the node that is previous to this one, or undefined if no such node
     * exists or if this node has no parent.
     *
     * @returns {XmlNode} The node that is previous to this one, or undefined
     *                    if no such node exists or if this node has no parent.
     */
    XmlNode.prototype.prev = function () {
        if (utils_1.isType(this.parent, "Undefined")) {
            return undefined;
        }
        else if (this.parent._children.indexOf(this) === 0) {
            return undefined;
        }
        return this.parent._children[this.parent._children.indexOf(this) - 1];
    };
    /**
     * Removes this node from its parent if this node has a parent.
     *
     * @returns {XmlNode} This node's parent, or undefined if it has no parent.
     */
    XmlNode.prototype.remove = function () {
        if (!utils_1.isType(this.parent, "Undefined")) {
            var parent_1 = this.parent;
            this.parent.removeChild(this);
            return parent_1;
        }
        return undefined;
    };
    /**
     * Removes the specified node from this node's children.
     *
     * Throws an exception if this node cannot have any children, or if the
     * specified node cannot be removed. Consult the appropriate subclass
     * documentation for more details.
     *
     * @param {XmlNode} node The node to remove.
     *
     * @returns {boolean} Whether a node was removed.
     */
    XmlNode.prototype.removeChild = function (node) {
        if (!(node instanceof XmlNode)) {
            throw new Error("node should be an instance of XmlNode");
        }
        var index = this._children.indexOf(node);
        if (index !== -1) {
            node._parent = undefined;
            this._children.splice(index, 1);
            return true;
        }
        return false;
    };
    /**
     * Removes the node at the specified index from this node's children.
     *
     * Throws an exception if this node cannot have any children, or if the
     * node at the specified index cannot be removed. Consult the appropriate
     * subclass documentation for more details.
     *
     * @param {number} index The index at which the node to be removed is
     *                       located.
     *
     * @returns {XmlNode} The node that was removed, or undefined if no node
     *                    was removed.
     */
    XmlNode.prototype.removeChildAtIndex = function (index) {
        if (!utils_1.isType(index, "Number") || !utils_1.isInteger(index)) {
            throw new TypeError("index should be a number");
        }
        else if (index < 0 || index >= this._children.length) {
            throw new RangeError("index should respect children array bounds");
        }
        var node = this._children[index];
        node._parent = undefined;
        this._children.splice(index, 1);
        return node;
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlNode.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        throw new Error("toString not implemented for XmlNode");
    };
    /**
     * Returns the root node of the current hierarchy. If this node has no
     * parent, this node itself is returned.
     *
     * @returns {XmlNode} The root node of the current hierarchy.
     */
    XmlNode.prototype.top = function () {
        if (utils_1.isType(this.parent, "Undefined")) {
            return this;
        }
        else {
            return this.parent.top();
        }
    };
    /**
     * Gets the parent of this node.
     *
     * @returns {XmlNode} The parent of this node.
     */
    XmlNode.prototype.up = function () {
        return this.parent;
    };
    return XmlNode;
}());
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlNode;


/***/ }),
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Verifies that the specified string only contains characters permitted by the
 * XML specification.
 *
 * @param {string} str The string to validate.
 *
 * @returns {boolean} Whether the specified string only contains characters
 *                    permitted by the XML specification.
 *
 * @private
 */
function validateChar(str) {
    var charRegex = "\\u0009|\\u000A|\\u000D|[\\u0020-\\uD7FF]";
    var surrogateCharRegex = "[\\uD800-\\uDBFF][\\uDC00-\\uDFFF]";
    return new RegExp("^((" + charRegex + ")|(" + surrogateCharRegex + "))*$")
        .test(str);
}
exports.validateChar = validateChar;
/**
 * Verifies that the specified string only contains a single character, and
 * that this character is permitted by the XML specification.
 *
 * @param {string} str The string to validate.
 *
 * @returns {boolean} Whether the specified string only contains a single
 *                    character, and that this character is permitted by the
 *                    XML specification.
 *
 * @private
 */
function validateSingleChar(str) {
    if (str.length === 1) {
        return new RegExp("^\\u0009|\\u000A|\\u000D|[\\u0020-\\uD7FF]$")
            .test(str);
    }
    else if (str.length === 2) {
        return new RegExp("^[\\uD800-\\uDBFF][\\uDC00-\\uDFFF]$").test(str);
    }
    else {
        return false;
    }
}
exports.validateSingleChar = validateSingleChar;
/**
 * Verifies that the specified string only contains characters permitted by the
 * XML specification for names.
 *
 * @param {string} str The string to validate.
 *
 * @returns {boolean} Whether the specified string only contains characters
 *                    permitted by the XML specification for names.
 *
 * @private
 */
function validateName(str) {
    if (str.length === 0) {
        return false;
    }
    var nameStartChar = ":|[A-Z]|_|[a-z]|[\\u00C0-\\u00D6]|[\\u00D8-\\u00F6]" +
        "|[\\u00F8-\\u02FF]|[\\u0370-\\u037D]|[\\u037F-\\u1FFF]"
        +
            "|[\\u200C-\\u200D]|[\\u2070-\\u218F]|[\\u2C00-\\u2FEF]"
        +
            "|[\\u3001-\\uD7FF]|[\\uF900-\\uFDCF]|[\\uFDF0-\\uFFFD]";
    var nameStartCharWithSurrogatePair = "[\\uD800-\\uDB7F][\\uDC00-\\uDFFF]";
    var nameChar = nameStartChar + "|-|\\.|[0-9]|\\u00B7|[\\u0300-\\u036F]" +
        "|[\\u203F-\\u2040]";
    var nameCharWithSurrogatePair = nameChar + "|" +
        nameStartCharWithSurrogatePair;
    if (new RegExp("^" + nameStartChar + "$").test(str.charAt(0))) {
        if (str.length === 1) {
            return true;
        }
        return new RegExp("^(" + nameCharWithSurrogatePair + ")+$")
            .test(str.substr(1));
    }
    else if (str.length >= 2) {
        if (new RegExp("^" + nameStartCharWithSurrogatePair + "$")
            .test(str.substr(0, 2))) {
            if (str.length === 2) {
                return true;
            }
            return new RegExp("^(" + nameCharWithSurrogatePair + ")+$")
                .test(str.substr(2));
        }
    }
    return false;
}
exports.validateName = validateName;


/***/ }),
/* 4 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
exports.settings = {
    lang: 'ENU',
    appId: 'wv-app',
    ribbonId: 'wv-ribbon',
    dockId: 'wv-dock',
    minWidth: 600,
    minHeight: 480,
    ribbon: {
        expandablesContainerId: 'wv-ribbon-expandables',
        dropdownsContainerId: 'wv-ribbon-dropdowns',
        dropdownItemHeight: 30,
        buttonWidth: 70,
        tabPageHeight: 90,
        buttonMouseHoverColor: 'rgb(132,203,234)',
        buttonHighlightColor: 'rgb(255, 198, 98)',
        buttonDefaultColor: ''
    },
};


/***/ }),
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var AppStates = (function () {
    function AppStates() {
        this.VisShareSpec = "";
        this.ribbonWidth = 0;
        this.ribbonAvailable = 0;
        this.ribbonVis = true;
        this.fullScreenMode = false;
        this.mainColor = 'rgb(214, 124, 28)';
        this.clientInitialized = false;
        this.fileLoaded = false;
        this.nodesSelected = false;
        this.selectionMode = 'Select';
        this.interactionMode = [];
        this.lastInteractionModes = [];
        this.renderingModes = [];
        this.pageCount = '';
        this.mouseActive = false;
        this.mouseLeftButtonDown = false;
        this.mouseLeftButtonMode = 'RectZoom';
        this.mouseRightButtonMode = 'Rotate';
        this.mouseMiddleButtonMode = 'Pan';
        this.drawRect = true;
        this.touchActive = false;
        this.touchGestureActive = false;
        this.touchStatus = 'cancel';
        this.ongoingTouches = [];
        this.touchDist = -1;
        this.touchMid = [];
        this.lastGestureDistance = 0;
        this.lastGestureDelta = 0;
        this.lastGestureCenterX = 0;
        this.lastGestureCenterY = 0;
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.activePath = '';
        this.panes = [
            { paneId: 'wv-modeltree', pos: 1, minimized: false, visible: true },
            { paneId: 'wv-views', pos: 1, minimized: false, visible: true },
            { paneId: 'wv-properties', pos: 2, minimized: false, visible: true }
        ];
        this.defaultPanes = [];
        this.containersChanged = false;
        this.commandPaneOpened = false;
        this.lockPanes = false;
        this.popoutPanes = [];
        this.popedOutWindows = [];
        this.preventGlobalKeyEvents = false;
        this.qsMode = false;
        this.aboutLinks = [
            "https://viewer.kisters.de/en/kisters-products/3dviewstation-webviewer.html",
            "https://viewer.kisters.de/help/webviewer",
            "http://viewer.kisters.de/en/news/news.html",
            "https://viewer.kisters.de/en/blog.html",
            "https://www.youtube.com/user/KistersViewer",
            "https://twitter.com/KistersViewer",
            "https://www.facebook.com/KistersViewer"
        ];
        this.version = '';
        this.paneHelpLinks = {
            'wv-views': '3dvs_wv_tut_functions_views.php',
            'wv-draftangle': '3dvs_wv_tut_functions_draft.php',
            'wv-exactcompare': '3dvs_wv_tut_functions_comparison.php',
            'wv-filter': '3dvs_wv_tut_functions_filter.php',
            'wv-flythrough': '3dvs_wv_tut_functions_flythroughmode.php',
            'wv-markup': '3dvs_wv_tut_functions_annotations.php',
            'wv-markup-reduced': '3dvs_wv_tut_functions_annotations.php',
            'wv-measurement': '3dvs_wv_tut_functions_measurement.php',
            'wv-mirror': '3dvs_wv_tut_functions_transformations.php',
            'wv-projectedarea': '3dvs_wv_tut_functions_projectedarea.php',
            'wv-properties': '3dvs_wv_tut_functions_propertiesselectedobjects.php',
            'wv-quickcompare': '3dvs_wv_tut_functions_comparison.php',
            'wv-section': '3dvs_wv_tut_functions_section.php',
            'wv-transform': '3dvs_wv_tut_functions_transformations.php',
            'wv-wallthickness': '3dvs_wv_tut_functions_wallthicknessheatmap.php',
            'wv-neutralaxis': '3dvs_wv_tut_functions_neutralaxis.php',
            'wv-neighbourhoodsearch': '3dvs_wv_tut_functions_neighborhoodsearch.php',
            'wv-bom': '3dvs_wv_tut_functions_techdocandbom_bom.php',
            'wv-callout': '3dvs_wv_tut_functions_techdocandbom_callouts.php',
            'wv-explode': '3dvs_wv_tut_functions_transformations.php',
            'wv-clash': '3dvs_wv_tut_functions_clashdetection.php',
            'wv-selections': '3dvs_wv_tut_functions_savingselections.php'
        };
    }
    AppStates.prototype.reset = function () {
        this.selectionMode = 'Select';
        this.interactionMode = [];
        this.renderingModes = [];
        this.pageCount = '';
        this.lastInteractionModes = [];
        this.preventGlobalKeyEvents = false;
        this.resetMouse();
        this.resetTouch();
    };
    AppStates.prototype.resetMouse = function () {
        this.mouseActive = false;
        this.mouseLeftButtonDown = false;
        this.mouseLeftButtonMode = 'RectZoom';
    };
    AppStates.prototype.resetTouch = function () {
        this.touchActive = false;
        this.touchGestureActive = false;
        this.touchStatus = 'cancel';
        this.ongoingTouches = [];
        this.touchDist = -1;
        this.touchMid = [];
        this.lastGestureDistance = 0;
        this.lastGestureDelta = 0;
        this.lastGestureCenterX = 0;
        this.lastGestureCenterY = 0;
    };
    return AppStates;
}());
exports.appStates = new AppStates();


/***/ }),
/* 6 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
// Copyright Joyent, Inc. and other Node contributors.
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the
// following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
// USE OR OTHER DEALINGS IN THE SOFTWARE.



var R = typeof Reflect === 'object' ? Reflect : null
var ReflectApply = R && typeof R.apply === 'function'
  ? R.apply
  : function ReflectApply(target, receiver, args) {
    return Function.prototype.apply.call(target, receiver, args);
  }

var ReflectOwnKeys
if (R && typeof R.ownKeys === 'function') {
  ReflectOwnKeys = R.ownKeys
} else if (Object.getOwnPropertySymbols) {
  ReflectOwnKeys = function ReflectOwnKeys(target) {
    return Object.getOwnPropertyNames(target)
      .concat(Object.getOwnPropertySymbols(target));
  };
} else {
  ReflectOwnKeys = function ReflectOwnKeys(target) {
    return Object.getOwnPropertyNames(target);
  };
}

function ProcessEmitWarning(warning) {
  if (console && console.warn) console.warn(warning);
}

var NumberIsNaN = Number.isNaN || function NumberIsNaN(value) {
  return value !== value;
}

function EventEmitter() {
  EventEmitter.init.call(this);
}
module.exports = EventEmitter;

// Backwards-compat with node 0.10.x
EventEmitter.EventEmitter = EventEmitter;

EventEmitter.prototype._events = undefined;
EventEmitter.prototype._eventsCount = 0;
EventEmitter.prototype._maxListeners = undefined;

// By default EventEmitters will print a warning if more than 10 listeners are
// added to it. This is a useful default which helps finding memory leaks.
var defaultMaxListeners = 10;

Object.defineProperty(EventEmitter, 'defaultMaxListeners', {
  enumerable: true,
  get: function() {
    return defaultMaxListeners;
  },
  set: function(arg) {
    if (typeof arg !== 'number' || arg < 0 || NumberIsNaN(arg)) {
      throw new RangeError('The value of "defaultMaxListeners" is out of range. It must be a non-negative number. Received ' + arg + '.');
    }
    defaultMaxListeners = arg;
  }
});

EventEmitter.init = function() {

  if (this._events === undefined ||
      this._events === Object.getPrototypeOf(this)._events) {
    this._events = Object.create(null);
    this._eventsCount = 0;
  }

  this._maxListeners = this._maxListeners || undefined;
};

// Obviously not all Emitters should be limited to 10. This function allows
// that to be increased. Set to zero for unlimited.
EventEmitter.prototype.setMaxListeners = function setMaxListeners(n) {
  if (typeof n !== 'number' || n < 0 || NumberIsNaN(n)) {
    throw new RangeError('The value of "n" is out of range. It must be a non-negative number. Received ' + n + '.');
  }
  this._maxListeners = n;
  return this;
};

function $getMaxListeners(that) {
  if (that._maxListeners === undefined)
    return EventEmitter.defaultMaxListeners;
  return that._maxListeners;
}

EventEmitter.prototype.getMaxListeners = function getMaxListeners() {
  return $getMaxListeners(this);
};

EventEmitter.prototype.emit = function emit(type) {
  var args = [];
  for (var i = 1; i < arguments.length; i++) args.push(arguments[i]);
  var doError = (type === 'error');

  var events = this._events;
  if (events !== undefined)
    doError = (doError && events.error === undefined);
  else if (!doError)
    return false;

  // If there is no 'error' event listener then throw.
  if (doError) {
    var er;
    if (args.length > 0)
      er = args[0];
    if (er instanceof Error) {
      // Note: The comments on the `throw` lines are intentional, they show
      // up in Node's output if this results in an unhandled exception.
      throw er; // Unhandled 'error' event
    }
    // At least give some kind of context to the user
    var err = new Error('Unhandled error.' + (er ? ' (' + er.message + ')' : ''));
    err.context = er;
    throw err; // Unhandled 'error' event
  }

  var handler = events[type];

  if (handler === undefined)
    return false;

  if (typeof handler === 'function') {
    ReflectApply(handler, this, args);
  } else {
    var len = handler.length;
    var listeners = arrayClone(handler, len);
    for (var i = 0; i < len; ++i)
      ReflectApply(listeners[i], this, args);
  }

  return true;
};

function _addListener(target, type, listener, prepend) {
  var m;
  var events;
  var existing;

  if (typeof listener !== 'function') {
    throw new TypeError('The "listener" argument must be of type Function. Received type ' + typeof listener);
  }

  events = target._events;
  if (events === undefined) {
    events = target._events = Object.create(null);
    target._eventsCount = 0;
  } else {
    // To avoid recursion in the case that type === "newListener"! Before
    // adding it to the listeners, first emit "newListener".
    if (events.newListener !== undefined) {
      target.emit('newListener', type,
                  listener.listener ? listener.listener : listener);

      // Re-assign `events` because a newListener handler could have caused the
      // this._events to be assigned to a new object
      events = target._events;
    }
    existing = events[type];
  }

  if (existing === undefined) {
    // Optimize the case of one listener. Don't need the extra array object.
    existing = events[type] = listener;
    ++target._eventsCount;
  } else {
    if (typeof existing === 'function') {
      // Adding the second element, need to change to array.
      existing = events[type] =
        prepend ? [listener, existing] : [existing, listener];
      // If we've already got an array, just append.
    } else if (prepend) {
      existing.unshift(listener);
    } else {
      existing.push(listener);
    }

    // Check for listener leak
    m = $getMaxListeners(target);
    if (m > 0 && existing.length > m && !existing.warned) {
      existing.warned = true;
      // No error code for this since it is a Warning
      // eslint-disable-next-line no-restricted-syntax
      var w = new Error('Possible EventEmitter memory leak detected. ' +
                          existing.length + ' ' + String(type) + ' listeners ' +
                          'added. Use emitter.setMaxListeners() to ' +
                          'increase limit');
      w.name = 'MaxListenersExceededWarning';
      w.emitter = target;
      w.type = type;
      w.count = existing.length;
      ProcessEmitWarning(w);
    }
  }

  return target;
}

EventEmitter.prototype.addListener = function addListener(type, listener) {
  return _addListener(this, type, listener, false);
};

EventEmitter.prototype.on = EventEmitter.prototype.addListener;

EventEmitter.prototype.prependListener =
    function prependListener(type, listener) {
      return _addListener(this, type, listener, true);
    };

function onceWrapper() {
  var args = [];
  for (var i = 0; i < arguments.length; i++) args.push(arguments[i]);
  if (!this.fired) {
    this.target.removeListener(this.type, this.wrapFn);
    this.fired = true;
    ReflectApply(this.listener, this.target, args);
  }
}

function _onceWrap(target, type, listener) {
  var state = { fired: false, wrapFn: undefined, target: target, type: type, listener: listener };
  var wrapped = onceWrapper.bind(state);
  wrapped.listener = listener;
  state.wrapFn = wrapped;
  return wrapped;
}

EventEmitter.prototype.once = function once(type, listener) {
  if (typeof listener !== 'function') {
    throw new TypeError('The "listener" argument must be of type Function. Received type ' + typeof listener);
  }
  this.on(type, _onceWrap(this, type, listener));
  return this;
};

EventEmitter.prototype.prependOnceListener =
    function prependOnceListener(type, listener) {
      if (typeof listener !== 'function') {
        throw new TypeError('The "listener" argument must be of type Function. Received type ' + typeof listener);
      }
      this.prependListener(type, _onceWrap(this, type, listener));
      return this;
    };

// Emits a 'removeListener' event if and only if the listener was removed.
EventEmitter.prototype.removeListener =
    function removeListener(type, listener) {
      var list, events, position, i, originalListener;

      if (typeof listener !== 'function') {
        throw new TypeError('The "listener" argument must be of type Function. Received type ' + typeof listener);
      }

      events = this._events;
      if (events === undefined)
        return this;

      list = events[type];
      if (list === undefined)
        return this;

      if (list === listener || list.listener === listener) {
        if (--this._eventsCount === 0)
          this._events = Object.create(null);
        else {
          delete events[type];
          if (events.removeListener)
            this.emit('removeListener', type, list.listener || listener);
        }
      } else if (typeof list !== 'function') {
        position = -1;

        for (i = list.length - 1; i >= 0; i--) {
          if (list[i] === listener || list[i].listener === listener) {
            originalListener = list[i].listener;
            position = i;
            break;
          }
        }

        if (position < 0)
          return this;

        if (position === 0)
          list.shift();
        else {
          spliceOne(list, position);
        }

        if (list.length === 1)
          events[type] = list[0];

        if (events.removeListener !== undefined)
          this.emit('removeListener', type, originalListener || listener);
      }

      return this;
    };

EventEmitter.prototype.off = EventEmitter.prototype.removeListener;

EventEmitter.prototype.removeAllListeners =
    function removeAllListeners(type) {
      var listeners, events, i;

      events = this._events;
      if (events === undefined)
        return this;

      // not listening for removeListener, no need to emit
      if (events.removeListener === undefined) {
        if (arguments.length === 0) {
          this._events = Object.create(null);
          this._eventsCount = 0;
        } else if (events[type] !== undefined) {
          if (--this._eventsCount === 0)
            this._events = Object.create(null);
          else
            delete events[type];
        }
        return this;
      }

      // emit removeListener for all listeners on all events
      if (arguments.length === 0) {
        var keys = Object.keys(events);
        var key;
        for (i = 0; i < keys.length; ++i) {
          key = keys[i];
          if (key === 'removeListener') continue;
          this.removeAllListeners(key);
        }
        this.removeAllListeners('removeListener');
        this._events = Object.create(null);
        this._eventsCount = 0;
        return this;
      }

      listeners = events[type];

      if (typeof listeners === 'function') {
        this.removeListener(type, listeners);
      } else if (listeners !== undefined) {
        // LIFO order
        for (i = listeners.length - 1; i >= 0; i--) {
          this.removeListener(type, listeners[i]);
        }
      }

      return this;
    };

function _listeners(target, type, unwrap) {
  var events = target._events;

  if (events === undefined)
    return [];

  var evlistener = events[type];
  if (evlistener === undefined)
    return [];

  if (typeof evlistener === 'function')
    return unwrap ? [evlistener.listener || evlistener] : [evlistener];

  return unwrap ?
    unwrapListeners(evlistener) : arrayClone(evlistener, evlistener.length);
}

EventEmitter.prototype.listeners = function listeners(type) {
  return _listeners(this, type, true);
};

EventEmitter.prototype.rawListeners = function rawListeners(type) {
  return _listeners(this, type, false);
};

EventEmitter.listenerCount = function(emitter, type) {
  if (typeof emitter.listenerCount === 'function') {
    return emitter.listenerCount(type);
  } else {
    return listenerCount.call(emitter, type);
  }
};

EventEmitter.prototype.listenerCount = listenerCount;
function listenerCount(type) {
  var events = this._events;

  if (events !== undefined) {
    var evlistener = events[type];

    if (typeof evlistener === 'function') {
      return 1;
    } else if (evlistener !== undefined) {
      return evlistener.length;
    }
  }

  return 0;
}

EventEmitter.prototype.eventNames = function eventNames() {
  return this._eventsCount > 0 ? ReflectOwnKeys(this._events) : [];
};

function arrayClone(arr, n) {
  var copy = new Array(n);
  for (var i = 0; i < n; ++i)
    copy[i] = arr[i];
  return copy;
}

function spliceOne(list, index) {
  for (; index + 1 < list.length; index++)
    list[index] = list[index + 1];
  list.pop();
}

function unwrapListeners(arr) {
  var ret = new Array(arr.length);
  for (var i = 0; i < ret.length; ++i) {
    ret[i] = arr[i].listener || arr[i];
  }
  return ret;
}


/***/ }),
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var utils_1 = __webpack_require__(1);
/**
 * @private
 */
var stringOptionsDefaults = {
    doubleQuotes: false,
    indent: "    ",
    newline: "\n",
    pretty: true,
};
Object.freeze(stringOptionsDefaults);
/**
 * Validates a string options object and replaces undefined values with their
 * appropriate defaults.
 *
 * @param {IStringOptions} options The string options object to validate.
 *
 * @returns {IStringOptions} The updated string options object.
 *
 * @private
 */
function validateStringOptions(options) {
    if (!utils_1.isType(options.doubleQuotes, "Boolean", "Undefined")) {
        throw new TypeError("options.doubleQuotes should be a boolean or"
            + " undefined");
    }
    if (!utils_1.isType(options.doubleQuotes, "Boolean")) {
        options.doubleQuotes = stringOptionsDefaults.doubleQuotes;
    }
    if (!utils_1.isType(options.indent, "String", "Undefined")) {
        throw new TypeError("options.indent should be a string or undefined");
    }
    if (!utils_1.isType(options.indent, "String")) {
        options.indent = stringOptionsDefaults.indent;
    }
    if (!utils_1.isType(options.newline, "String", "Undefined")) {
        throw new TypeError("options.newline should be a string or undefined");
    }
    if (!utils_1.isType(options.newline, "String")) {
        options.newline = stringOptionsDefaults.newline;
    }
    if (!utils_1.isType(options.pretty, "Boolean", "Undefined")) {
        throw new TypeError("options.pretty should be a boolean or undefined");
    }
    if (!utils_1.isType(options.pretty, "Boolean")) {
        options.pretty = stringOptionsDefaults.pretty;
    }
    return options;
}
exports.validateStringOptions = validateStringOptions;
/**
 * @private
 */
var declarationOptionsDefaults = {
    encoding: undefined,
    standalone: undefined,
    version: "1.0",
};
Object.freeze(declarationOptionsDefaults);
/**
 * Validates an XML declaration options object and replaces undefined values
 * with their appropriate defaults.
 *
 * @param {IDeclarationOptions} options The XML declaration options object to
 *                                     validate.
 *
 * @returns {IDeclarationOptions} The updated XML declaration options object.
 *
 * @private
 */
function validateDeclarationOptions(options) {
    if (!utils_1.isType(options.encoding, "String", "Undefined")) {
        throw new TypeError("options.encoding should be a string or undefined");
    }
    if (!utils_1.isType(options.standalone, "String", "Undefined")) {
        throw new TypeError("options.standalone should be a string or" +
            " undefined");
    }
    if (!utils_1.isType(options.version, "String", "Undefined")) {
        throw new TypeError("options.version should be a string or undefined");
    }
    if (!utils_1.isType(options.version, "String")) {
        options.version = declarationOptionsDefaults.version;
    }
    return options;
}
exports.validateDeclarationOptions = validateDeclarationOptions;


/***/ }),
/* 8 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlNode_1 = __webpack_require__(2);
/**
 * Represents an XML comment.
 *
 * An XML character reference is structured as follows, where `{content}` is
 * the text of the comment.
 *
 * ```xml
 * <!--{content}-->
 * ```
 *
 * The `{content}` value is a property of this node.
 *
 * XmlComment nodes cannot have any children.
 */
var XmlComment = (function (_super) {
    __extends(XmlComment, _super);
    /**
     * Initializes a new instance of the {@link XmlComment} class.
     *
     * @param {string} content The content of the comment.
     */
    function XmlComment(content) {
        _super.call(this);
        this.content = content;
    }
    Object.defineProperty(XmlComment.prototype, "content", {
        /**
         * Gets the content of the comment.
         *
         * @returns {string} The content of the comment.
         */
        get: function () {
            return this._content;
        },
        /**
         * Sets the content of the comment.
         *
         * @param {string} content The content of the comment.
         */
        set: function (content) {
            if (!utils_1.isType(content, "String")) {
                throw new TypeError("content should be a string");
            }
            else if (!validate_1.validateChar(content)) {
                throw new Error("content should not contain characters" +
                    " not allowed in XML");
            }
            else if (!/^([^-]|-[^-])*$/.test(content)) {
                throw new Error("content should not contain the string '--' or" +
                    " end with '-'");
            }
            this._content = content;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Throws an exception since {@link XmlComment} nodes cannot have any
     * children.
     */
    XmlComment.prototype.children = function () {
        throw new Error("XmlComment nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlComment} nodes cannot have any
     * children.
     */
    XmlComment.prototype.insertChild = function (node, index) {
        throw new Error("XmlComment nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlComment} nodes cannot have any
     * children.
     */
    XmlComment.prototype.removeChild = function (node) {
        throw new Error("XmlComment nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlComment} nodes cannot have any
     * children.
     */
    XmlComment.prototype.removeChildAtIndex = function (index) {
        throw new Error("XmlComment nodes cannot have children");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlComment.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        return "<!--" + this.content + "-->";
    };
    return XmlComment;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlComment;


/***/ }),
/* 9 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlNode_1 = __webpack_require__(2);
/**
 * Represents an XML processing instruction.
 *
 * An XML processing instruction is structured as follows, where `{target}`
 * and `{content}` are the target and content of the processing instruction
 * respectively.
 *
 * ```xml
 * <?{target} {content}?>
 * ```
 *
 * The `{target}` and `{content}` values are properties of this node.
 *
 * XmlProcInst nodes cannot have any children.
 */
var XmlProcInst = (function (_super) {
    __extends(XmlProcInst, _super);
    /**
     * Initializes a new instance of the {@link XmlProcInst} class.
     *
     * @param {string} target    The target of the processing instruction.
     * @param {string} [content] The data of the processing instruction, or
     *                           undefined if there is no target.
     */
    function XmlProcInst(target, content) {
        _super.call(this);
        this.target = target;
        this.content = content;
    }
    Object.defineProperty(XmlProcInst.prototype, "target", {
        /**
         * Gets the target of the processing instruction.
         *
         * @returns {string} The target of the processing instruction.
         */
        get: function () {
            return this._target;
        },
        /**
         * Sets the target of the processing instruction.
         *
         * @param {string} target The target of the processing instruction.
         */
        set: function (target) {
            if (!utils_1.isType(target, "String")) {
                throw new TypeError("target should be a string");
            }
            else if (!validate_1.validateChar(target)) {
                throw new Error("target should not contain characters" +
                    " not allowed in XML");
            }
            else if (target === "xml") {
                throw new Error("target should not be the string 'xml'");
            }
            this._target = target;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(XmlProcInst.prototype, "content", {
        /**
         * Gets the data of the processing instruction.
         *
         * @returns {string} The data of the processing instruction. This value
         *                   may be undefined.
         */
        get: function () {
            return this._content;
        },
        /**
         * Sets the data of the processing instruction.
         *
         * @param {string} content The data of the processing instruction. This
         *                         value may be undefined.
         */
        set: function (content) {
            if (!utils_1.isType(content, "String", "Undefined")) {
                throw new TypeError("data should be a string or undefined");
            }
            if (utils_1.isType(content, "String")) {
                if (!validate_1.validateChar(content)) {
                    throw new Error("data should not contain characters" +
                        " not allowed in XML");
                }
                else if (/\?>/.test(content)) {
                    throw new Error("data should not contain the string '?>'");
                }
            }
            this._content = content;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Throws an exception since {@link XmlProcInst} nodes cannot have any
     * children.
     */
    XmlProcInst.prototype.children = function () {
        throw new Error("XmlProcInst nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlProcInst} nodes cannot have any
     * children.
     */
    XmlProcInst.prototype.insertChild = function (node, index) {
        throw new Error("XmlProcInst nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlProcInst} nodes cannot have any
     * children.
     */
    XmlProcInst.prototype.removeChild = function (node) {
        throw new Error("XmlProcInst nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlProcInst} nodes cannot have any
     * children.
     */
    XmlProcInst.prototype.removeChildAtIndex = function (index) {
        throw new Error("XmlProcInst nodes cannot have children");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlProcInst.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        if (this.content === undefined) {
            return "<?" + this.target + "?>";
        }
        else {
            return "<?" + this.target + " " + this.content + "?>";
        }
    };
    return XmlProcInst;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlProcInst;


/***/ }),
/* 10 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_RESULT__;// TinyColor v1.4.1
// https://github.com/bgrins/TinyColor
// Brian Grinstead, MIT License

(function(Math) {

var trimLeft = /^\s+/,
    trimRight = /\s+$/,
    tinyCounter = 0,
    mathRound = Math.round,
    mathMin = Math.min,
    mathMax = Math.max,
    mathRandom = Math.random;

function tinycolor (color, opts) {

    color = (color) ? color : '';
    opts = opts || { };

    // If input is already a tinycolor, return itself
    if (color instanceof tinycolor) {
       return color;
    }
    // If we are called as a function, call using new instead
    if (!(this instanceof tinycolor)) {
        return new tinycolor(color, opts);
    }

    var rgb = inputToRGB(color);
    this._originalInput = color,
    this._r = rgb.r,
    this._g = rgb.g,
    this._b = rgb.b,
    this._a = rgb.a,
    this._roundA = mathRound(100*this._a) / 100,
    this._format = opts.format || rgb.format;
    this._gradientType = opts.gradientType;

    // Don't let the range of [0,255] come back in [0,1].
    // Potentially lose a little bit of precision here, but will fix issues where
    // .5 gets interpreted as half of the total, instead of half of 1
    // If it was supposed to be 128, this was already taken care of by `inputToRgb`
    if (this._r < 1) { this._r = mathRound(this._r); }
    if (this._g < 1) { this._g = mathRound(this._g); }
    if (this._b < 1) { this._b = mathRound(this._b); }

    this._ok = rgb.ok;
    this._tc_id = tinyCounter++;
}

tinycolor.prototype = {
    isDark: function() {
        return this.getBrightness() < 128;
    },
    isLight: function() {
        return !this.isDark();
    },
    isValid: function() {
        return this._ok;
    },
    getOriginalInput: function() {
      return this._originalInput;
    },
    getFormat: function() {
        return this._format;
    },
    getAlpha: function() {
        return this._a;
    },
    getBrightness: function() {
        //http://www.w3.org/TR/AERT#color-contrast
        var rgb = this.toRgb();
        return (rgb.r * 299 + rgb.g * 587 + rgb.b * 114) / 1000;
    },
    getLuminance: function() {
        //http://www.w3.org/TR/2008/REC-WCAG20-20081211/#relativeluminancedef
        var rgb = this.toRgb();
        var RsRGB, GsRGB, BsRGB, R, G, B;
        RsRGB = rgb.r/255;
        GsRGB = rgb.g/255;
        BsRGB = rgb.b/255;

        if (RsRGB <= 0.03928) {R = RsRGB / 12.92;} else {R = Math.pow(((RsRGB + 0.055) / 1.055), 2.4);}
        if (GsRGB <= 0.03928) {G = GsRGB / 12.92;} else {G = Math.pow(((GsRGB + 0.055) / 1.055), 2.4);}
        if (BsRGB <= 0.03928) {B = BsRGB / 12.92;} else {B = Math.pow(((BsRGB + 0.055) / 1.055), 2.4);}
        return (0.2126 * R) + (0.7152 * G) + (0.0722 * B);
    },
    setAlpha: function(value) {
        this._a = boundAlpha(value);
        this._roundA = mathRound(100*this._a) / 100;
        return this;
    },
    toHsv: function() {
        var hsv = rgbToHsv(this._r, this._g, this._b);
        return { h: hsv.h * 360, s: hsv.s, v: hsv.v, a: this._a };
    },
    toHsvString: function() {
        var hsv = rgbToHsv(this._r, this._g, this._b);
        var h = mathRound(hsv.h * 360), s = mathRound(hsv.s * 100), v = mathRound(hsv.v * 100);
        return (this._a == 1) ?
          "hsv("  + h + ", " + s + "%, " + v + "%)" :
          "hsva(" + h + ", " + s + "%, " + v + "%, "+ this._roundA + ")";
    },
    toHsl: function() {
        var hsl = rgbToHsl(this._r, this._g, this._b);
        return { h: hsl.h * 360, s: hsl.s, l: hsl.l, a: this._a };
    },
    toHslString: function() {
        var hsl = rgbToHsl(this._r, this._g, this._b);
        var h = mathRound(hsl.h * 360), s = mathRound(hsl.s * 100), l = mathRound(hsl.l * 100);
        return (this._a == 1) ?
          "hsl("  + h + ", " + s + "%, " + l + "%)" :
          "hsla(" + h + ", " + s + "%, " + l + "%, "+ this._roundA + ")";
    },
    toHex: function(allow3Char) {
        return rgbToHex(this._r, this._g, this._b, allow3Char);
    },
    toHexString: function(allow3Char) {
        return '#' + this.toHex(allow3Char);
    },
    toHex8: function(allow4Char) {
        return rgbaToHex(this._r, this._g, this._b, this._a, allow4Char);
    },
    toHex8String: function(allow4Char) {
        return '#' + this.toHex8(allow4Char);
    },
    toRgb: function() {
        return { r: mathRound(this._r), g: mathRound(this._g), b: mathRound(this._b), a: this._a };
    },
    toRgbString: function() {
        return (this._a == 1) ?
          "rgb("  + mathRound(this._r) + ", " + mathRound(this._g) + ", " + mathRound(this._b) + ")" :
          "rgba(" + mathRound(this._r) + ", " + mathRound(this._g) + ", " + mathRound(this._b) + ", " + this._roundA + ")";
    },
    toPercentageRgb: function() {
        return { r: mathRound(bound01(this._r, 255) * 100) + "%", g: mathRound(bound01(this._g, 255) * 100) + "%", b: mathRound(bound01(this._b, 255) * 100) + "%", a: this._a };
    },
    toPercentageRgbString: function() {
        return (this._a == 1) ?
          "rgb("  + mathRound(bound01(this._r, 255) * 100) + "%, " + mathRound(bound01(this._g, 255) * 100) + "%, " + mathRound(bound01(this._b, 255) * 100) + "%)" :
          "rgba(" + mathRound(bound01(this._r, 255) * 100) + "%, " + mathRound(bound01(this._g, 255) * 100) + "%, " + mathRound(bound01(this._b, 255) * 100) + "%, " + this._roundA + ")";
    },
    toName: function() {
        if (this._a === 0) {
            return "transparent";
        }

        if (this._a < 1) {
            return false;
        }

        return hexNames[rgbToHex(this._r, this._g, this._b, true)] || false;
    },
    toFilter: function(secondColor) {
        var hex8String = '#' + rgbaToArgbHex(this._r, this._g, this._b, this._a);
        var secondHex8String = hex8String;
        var gradientType = this._gradientType ? "GradientType = 1, " : "";

        if (secondColor) {
            var s = tinycolor(secondColor);
            secondHex8String = '#' + rgbaToArgbHex(s._r, s._g, s._b, s._a);
        }

        return "progid:DXImageTransform.Microsoft.gradient("+gradientType+"startColorstr="+hex8String+",endColorstr="+secondHex8String+")";
    },
    toString: function(format) {
        var formatSet = !!format;
        format = format || this._format;

        var formattedString = false;
        var hasAlpha = this._a < 1 && this._a >= 0;
        var needsAlphaFormat = !formatSet && hasAlpha && (format === "hex" || format === "hex6" || format === "hex3" || format === "hex4" || format === "hex8" || format === "name");

        if (needsAlphaFormat) {
            // Special case for "transparent", all other non-alpha formats
            // will return rgba when there is transparency.
            if (format === "name" && this._a === 0) {
                return this.toName();
            }
            return this.toRgbString();
        }
        if (format === "rgb") {
            formattedString = this.toRgbString();
        }
        if (format === "prgb") {
            formattedString = this.toPercentageRgbString();
        }
        if (format === "hex" || format === "hex6") {
            formattedString = this.toHexString();
        }
        if (format === "hex3") {
            formattedString = this.toHexString(true);
        }
        if (format === "hex4") {
            formattedString = this.toHex8String(true);
        }
        if (format === "hex8") {
            formattedString = this.toHex8String();
        }
        if (format === "name") {
            formattedString = this.toName();
        }
        if (format === "hsl") {
            formattedString = this.toHslString();
        }
        if (format === "hsv") {
            formattedString = this.toHsvString();
        }

        return formattedString || this.toHexString();
    },
    clone: function() {
        return tinycolor(this.toString());
    },

    _applyModification: function(fn, args) {
        var color = fn.apply(null, [this].concat([].slice.call(args)));
        this._r = color._r;
        this._g = color._g;
        this._b = color._b;
        this.setAlpha(color._a);
        return this;
    },
    lighten: function() {
        return this._applyModification(lighten, arguments);
    },
    brighten: function() {
        return this._applyModification(brighten, arguments);
    },
    darken: function() {
        return this._applyModification(darken, arguments);
    },
    desaturate: function() {
        return this._applyModification(desaturate, arguments);
    },
    saturate: function() {
        return this._applyModification(saturate, arguments);
    },
    greyscale: function() {
        return this._applyModification(greyscale, arguments);
    },
    spin: function() {
        return this._applyModification(spin, arguments);
    },

    _applyCombination: function(fn, args) {
        return fn.apply(null, [this].concat([].slice.call(args)));
    },
    analogous: function() {
        return this._applyCombination(analogous, arguments);
    },
    complement: function() {
        return this._applyCombination(complement, arguments);
    },
    monochromatic: function() {
        return this._applyCombination(monochromatic, arguments);
    },
    splitcomplement: function() {
        return this._applyCombination(splitcomplement, arguments);
    },
    triad: function() {
        return this._applyCombination(triad, arguments);
    },
    tetrad: function() {
        return this._applyCombination(tetrad, arguments);
    }
};

// If input is an object, force 1 into "1.0" to handle ratios properly
// String input requires "1.0" as input, so 1 will be treated as 1
tinycolor.fromRatio = function(color, opts) {
    if (typeof color == "object") {
        var newColor = {};
        for (var i in color) {
            if (color.hasOwnProperty(i)) {
                if (i === "a") {
                    newColor[i] = color[i];
                }
                else {
                    newColor[i] = convertToPercentage(color[i]);
                }
            }
        }
        color = newColor;
    }

    return tinycolor(color, opts);
};

// Given a string or object, convert that input to RGB
// Possible string inputs:
//
//     "red"
//     "#f00" or "f00"
//     "#ff0000" or "ff0000"
//     "#ff000000" or "ff000000"
//     "rgb 255 0 0" or "rgb (255, 0, 0)"
//     "rgb 1.0 0 0" or "rgb (1, 0, 0)"
//     "rgba (255, 0, 0, 1)" or "rgba 255, 0, 0, 1"
//     "rgba (1.0, 0, 0, 1)" or "rgba 1.0, 0, 0, 1"
//     "hsl(0, 100%, 50%)" or "hsl 0 100% 50%"
//     "hsla(0, 100%, 50%, 1)" or "hsla 0 100% 50%, 1"
//     "hsv(0, 100%, 100%)" or "hsv 0 100% 100%"
//
function inputToRGB(color) {

    var rgb = { r: 0, g: 0, b: 0 };
    var a = 1;
    var s = null;
    var v = null;
    var l = null;
    var ok = false;
    var format = false;

    if (typeof color == "string") {
        color = stringInputToObject(color);
    }

    if (typeof color == "object") {
        if (isValidCSSUnit(color.r) && isValidCSSUnit(color.g) && isValidCSSUnit(color.b)) {
            rgb = rgbToRgb(color.r, color.g, color.b);
            ok = true;
            format = String(color.r).substr(-1) === "%" ? "prgb" : "rgb";
        }
        else if (isValidCSSUnit(color.h) && isValidCSSUnit(color.s) && isValidCSSUnit(color.v)) {
            s = convertToPercentage(color.s);
            v = convertToPercentage(color.v);
            rgb = hsvToRgb(color.h, s, v);
            ok = true;
            format = "hsv";
        }
        else if (isValidCSSUnit(color.h) && isValidCSSUnit(color.s) && isValidCSSUnit(color.l)) {
            s = convertToPercentage(color.s);
            l = convertToPercentage(color.l);
            rgb = hslToRgb(color.h, s, l);
            ok = true;
            format = "hsl";
        }

        if (color.hasOwnProperty("a")) {
            a = color.a;
        }
    }

    a = boundAlpha(a);

    return {
        ok: ok,
        format: color.format || format,
        r: mathMin(255, mathMax(rgb.r, 0)),
        g: mathMin(255, mathMax(rgb.g, 0)),
        b: mathMin(255, mathMax(rgb.b, 0)),
        a: a
    };
}


// Conversion Functions
// --------------------

// `rgbToHsl`, `rgbToHsv`, `hslToRgb`, `hsvToRgb` modified from:
// <http://mjijackson.com/2008/02/rgb-to-hsl-and-rgb-to-hsv-color-model-conversion-algorithms-in-javascript>

// `rgbToRgb`
// Handle bounds / percentage checking to conform to CSS color spec
// <http://www.w3.org/TR/css3-color/>
// *Assumes:* r, g, b in [0, 255] or [0, 1]
// *Returns:* { r, g, b } in [0, 255]
function rgbToRgb(r, g, b){
    return {
        r: bound01(r, 255) * 255,
        g: bound01(g, 255) * 255,
        b: bound01(b, 255) * 255
    };
}

// `rgbToHsl`
// Converts an RGB color value to HSL.
// *Assumes:* r, g, and b are contained in [0, 255] or [0, 1]
// *Returns:* { h, s, l } in [0,1]
function rgbToHsl(r, g, b) {

    r = bound01(r, 255);
    g = bound01(g, 255);
    b = bound01(b, 255);

    var max = mathMax(r, g, b), min = mathMin(r, g, b);
    var h, s, l = (max + min) / 2;

    if(max == min) {
        h = s = 0; // achromatic
    }
    else {
        var d = max - min;
        s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
        switch(max) {
            case r: h = (g - b) / d + (g < b ? 6 : 0); break;
            case g: h = (b - r) / d + 2; break;
            case b: h = (r - g) / d + 4; break;
        }

        h /= 6;
    }

    return { h: h, s: s, l: l };
}

// `hslToRgb`
// Converts an HSL color value to RGB.
// *Assumes:* h is contained in [0, 1] or [0, 360] and s and l are contained [0, 1] or [0, 100]
// *Returns:* { r, g, b } in the set [0, 255]
function hslToRgb(h, s, l) {
    var r, g, b;

    h = bound01(h, 360);
    s = bound01(s, 100);
    l = bound01(l, 100);

    function hue2rgb(p, q, t) {
        if(t < 0) t += 1;
        if(t > 1) t -= 1;
        if(t < 1/6) return p + (q - p) * 6 * t;
        if(t < 1/2) return q;
        if(t < 2/3) return p + (q - p) * (2/3 - t) * 6;
        return p;
    }

    if(s === 0) {
        r = g = b = l; // achromatic
    }
    else {
        var q = l < 0.5 ? l * (1 + s) : l + s - l * s;
        var p = 2 * l - q;
        r = hue2rgb(p, q, h + 1/3);
        g = hue2rgb(p, q, h);
        b = hue2rgb(p, q, h - 1/3);
    }

    return { r: r * 255, g: g * 255, b: b * 255 };
}

// `rgbToHsv`
// Converts an RGB color value to HSV
// *Assumes:* r, g, and b are contained in the set [0, 255] or [0, 1]
// *Returns:* { h, s, v } in [0,1]
function rgbToHsv(r, g, b) {

    r = bound01(r, 255);
    g = bound01(g, 255);
    b = bound01(b, 255);

    var max = mathMax(r, g, b), min = mathMin(r, g, b);
    var h, s, v = max;

    var d = max - min;
    s = max === 0 ? 0 : d / max;

    if(max == min) {
        h = 0; // achromatic
    }
    else {
        switch(max) {
            case r: h = (g - b) / d + (g < b ? 6 : 0); break;
            case g: h = (b - r) / d + 2; break;
            case b: h = (r - g) / d + 4; break;
        }
        h /= 6;
    }
    return { h: h, s: s, v: v };
}

// `hsvToRgb`
// Converts an HSV color value to RGB.
// *Assumes:* h is contained in [0, 1] or [0, 360] and s and v are contained in [0, 1] or [0, 100]
// *Returns:* { r, g, b } in the set [0, 255]
 function hsvToRgb(h, s, v) {

    h = bound01(h, 360) * 6;
    s = bound01(s, 100);
    v = bound01(v, 100);

    var i = Math.floor(h),
        f = h - i,
        p = v * (1 - s),
        q = v * (1 - f * s),
        t = v * (1 - (1 - f) * s),
        mod = i % 6,
        r = [v, q, p, p, t, v][mod],
        g = [t, v, v, q, p, p][mod],
        b = [p, p, t, v, v, q][mod];

    return { r: r * 255, g: g * 255, b: b * 255 };
}

// `rgbToHex`
// Converts an RGB color to hex
// Assumes r, g, and b are contained in the set [0, 255]
// Returns a 3 or 6 character hex
function rgbToHex(r, g, b, allow3Char) {

    var hex = [
        pad2(mathRound(r).toString(16)),
        pad2(mathRound(g).toString(16)),
        pad2(mathRound(b).toString(16))
    ];

    // Return a 3 character hex if possible
    if (allow3Char && hex[0].charAt(0) == hex[0].charAt(1) && hex[1].charAt(0) == hex[1].charAt(1) && hex[2].charAt(0) == hex[2].charAt(1)) {
        return hex[0].charAt(0) + hex[1].charAt(0) + hex[2].charAt(0);
    }

    return hex.join("");
}

// `rgbaToHex`
// Converts an RGBA color plus alpha transparency to hex
// Assumes r, g, b are contained in the set [0, 255] and
// a in [0, 1]. Returns a 4 or 8 character rgba hex
function rgbaToHex(r, g, b, a, allow4Char) {

    var hex = [
        pad2(mathRound(r).toString(16)),
        pad2(mathRound(g).toString(16)),
        pad2(mathRound(b).toString(16)),
        pad2(convertDecimalToHex(a))
    ];

    // Return a 4 character hex if possible
    if (allow4Char && hex[0].charAt(0) == hex[0].charAt(1) && hex[1].charAt(0) == hex[1].charAt(1) && hex[2].charAt(0) == hex[2].charAt(1) && hex[3].charAt(0) == hex[3].charAt(1)) {
        return hex[0].charAt(0) + hex[1].charAt(0) + hex[2].charAt(0) + hex[3].charAt(0);
    }

    return hex.join("");
}

// `rgbaToArgbHex`
// Converts an RGBA color to an ARGB Hex8 string
// Rarely used, but required for "toFilter()"
function rgbaToArgbHex(r, g, b, a) {

    var hex = [
        pad2(convertDecimalToHex(a)),
        pad2(mathRound(r).toString(16)),
        pad2(mathRound(g).toString(16)),
        pad2(mathRound(b).toString(16))
    ];

    return hex.join("");
}

// `equals`
// Can be called with any tinycolor input
tinycolor.equals = function (color1, color2) {
    if (!color1 || !color2) { return false; }
    return tinycolor(color1).toRgbString() == tinycolor(color2).toRgbString();
};

tinycolor.random = function() {
    return tinycolor.fromRatio({
        r: mathRandom(),
        g: mathRandom(),
        b: mathRandom()
    });
};


// Modification Functions
// ----------------------
// Thanks to less.js for some of the basics here
// <https://github.com/cloudhead/less.js/blob/master/lib/less/functions.js>

function desaturate(color, amount) {
    amount = (amount === 0) ? 0 : (amount || 10);
    var hsl = tinycolor(color).toHsl();
    hsl.s -= amount / 100;
    hsl.s = clamp01(hsl.s);
    return tinycolor(hsl);
}

function saturate(color, amount) {
    amount = (amount === 0) ? 0 : (amount || 10);
    var hsl = tinycolor(color).toHsl();
    hsl.s += amount / 100;
    hsl.s = clamp01(hsl.s);
    return tinycolor(hsl);
}

function greyscale(color) {
    return tinycolor(color).desaturate(100);
}

function lighten (color, amount) {
    amount = (amount === 0) ? 0 : (amount || 10);
    var hsl = tinycolor(color).toHsl();
    hsl.l += amount / 100;
    hsl.l = clamp01(hsl.l);
    return tinycolor(hsl);
}

function brighten(color, amount) {
    amount = (amount === 0) ? 0 : (amount || 10);
    var rgb = tinycolor(color).toRgb();
    rgb.r = mathMax(0, mathMin(255, rgb.r - mathRound(255 * - (amount / 100))));
    rgb.g = mathMax(0, mathMin(255, rgb.g - mathRound(255 * - (amount / 100))));
    rgb.b = mathMax(0, mathMin(255, rgb.b - mathRound(255 * - (amount / 100))));
    return tinycolor(rgb);
}

function darken (color, amount) {
    amount = (amount === 0) ? 0 : (amount || 10);
    var hsl = tinycolor(color).toHsl();
    hsl.l -= amount / 100;
    hsl.l = clamp01(hsl.l);
    return tinycolor(hsl);
}

// Spin takes a positive or negative amount within [-360, 360] indicating the change of hue.
// Values outside of this range will be wrapped into this range.
function spin(color, amount) {
    var hsl = tinycolor(color).toHsl();
    var hue = (hsl.h + amount) % 360;
    hsl.h = hue < 0 ? 360 + hue : hue;
    return tinycolor(hsl);
}

// Combination Functions
// ---------------------
// Thanks to jQuery xColor for some of the ideas behind these
// <https://github.com/infusion/jQuery-xcolor/blob/master/jquery.xcolor.js>

function complement(color) {
    var hsl = tinycolor(color).toHsl();
    hsl.h = (hsl.h + 180) % 360;
    return tinycolor(hsl);
}

function triad(color) {
    var hsl = tinycolor(color).toHsl();
    var h = hsl.h;
    return [
        tinycolor(color),
        tinycolor({ h: (h + 120) % 360, s: hsl.s, l: hsl.l }),
        tinycolor({ h: (h + 240) % 360, s: hsl.s, l: hsl.l })
    ];
}

function tetrad(color) {
    var hsl = tinycolor(color).toHsl();
    var h = hsl.h;
    return [
        tinycolor(color),
        tinycolor({ h: (h + 90) % 360, s: hsl.s, l: hsl.l }),
        tinycolor({ h: (h + 180) % 360, s: hsl.s, l: hsl.l }),
        tinycolor({ h: (h + 270) % 360, s: hsl.s, l: hsl.l })
    ];
}

function splitcomplement(color) {
    var hsl = tinycolor(color).toHsl();
    var h = hsl.h;
    return [
        tinycolor(color),
        tinycolor({ h: (h + 72) % 360, s: hsl.s, l: hsl.l}),
        tinycolor({ h: (h + 216) % 360, s: hsl.s, l: hsl.l})
    ];
}

function analogous(color, results, slices) {
    results = results || 6;
    slices = slices || 30;

    var hsl = tinycolor(color).toHsl();
    var part = 360 / slices;
    var ret = [tinycolor(color)];

    for (hsl.h = ((hsl.h - (part * results >> 1)) + 720) % 360; --results; ) {
        hsl.h = (hsl.h + part) % 360;
        ret.push(tinycolor(hsl));
    }
    return ret;
}

function monochromatic(color, results) {
    results = results || 6;
    var hsv = tinycolor(color).toHsv();
    var h = hsv.h, s = hsv.s, v = hsv.v;
    var ret = [];
    var modification = 1 / results;

    while (results--) {
        ret.push(tinycolor({ h: h, s: s, v: v}));
        v = (v + modification) % 1;
    }

    return ret;
}

// Utility Functions
// ---------------------

tinycolor.mix = function(color1, color2, amount) {
    amount = (amount === 0) ? 0 : (amount || 50);

    var rgb1 = tinycolor(color1).toRgb();
    var rgb2 = tinycolor(color2).toRgb();

    var p = amount / 100;

    var rgba = {
        r: ((rgb2.r - rgb1.r) * p) + rgb1.r,
        g: ((rgb2.g - rgb1.g) * p) + rgb1.g,
        b: ((rgb2.b - rgb1.b) * p) + rgb1.b,
        a: ((rgb2.a - rgb1.a) * p) + rgb1.a
    };

    return tinycolor(rgba);
};


// Readability Functions
// ---------------------
// <http://www.w3.org/TR/2008/REC-WCAG20-20081211/#contrast-ratiodef (WCAG Version 2)

// `contrast`
// Analyze the 2 colors and returns the color contrast defined by (WCAG Version 2)
tinycolor.readability = function(color1, color2) {
    var c1 = tinycolor(color1);
    var c2 = tinycolor(color2);
    return (Math.max(c1.getLuminance(),c2.getLuminance())+0.05) / (Math.min(c1.getLuminance(),c2.getLuminance())+0.05);
};

// `isReadable`
// Ensure that foreground and background color combinations meet WCAG2 guidelines.
// The third argument is an optional Object.
//      the 'level' property states 'AA' or 'AAA' - if missing or invalid, it defaults to 'AA';
//      the 'size' property states 'large' or 'small' - if missing or invalid, it defaults to 'small'.
// If the entire object is absent, isReadable defaults to {level:"AA",size:"small"}.

// *Example*
//    tinycolor.isReadable("#000", "#111") => false
//    tinycolor.isReadable("#000", "#111",{level:"AA",size:"large"}) => false
tinycolor.isReadable = function(color1, color2, wcag2) {
    var readability = tinycolor.readability(color1, color2);
    var wcag2Parms, out;

    out = false;

    wcag2Parms = validateWCAG2Parms(wcag2);
    switch (wcag2Parms.level + wcag2Parms.size) {
        case "AAsmall":
        case "AAAlarge":
            out = readability >= 4.5;
            break;
        case "AAlarge":
            out = readability >= 3;
            break;
        case "AAAsmall":
            out = readability >= 7;
            break;
    }
    return out;

};

// `mostReadable`
// Given a base color and a list of possible foreground or background
// colors for that base, returns the most readable color.
// Optionally returns Black or White if the most readable color is unreadable.
// *Example*
//    tinycolor.mostReadable(tinycolor.mostReadable("#123", ["#124", "#125"],{includeFallbackColors:false}).toHexString(); // "#112255"
//    tinycolor.mostReadable(tinycolor.mostReadable("#123", ["#124", "#125"],{includeFallbackColors:true}).toHexString();  // "#ffffff"
//    tinycolor.mostReadable("#a8015a", ["#faf3f3"],{includeFallbackColors:true,level:"AAA",size:"large"}).toHexString(); // "#faf3f3"
//    tinycolor.mostReadable("#a8015a", ["#faf3f3"],{includeFallbackColors:true,level:"AAA",size:"small"}).toHexString(); // "#ffffff"
tinycolor.mostReadable = function(baseColor, colorList, args) {
    var bestColor = null;
    var bestScore = 0;
    var readability;
    var includeFallbackColors, level, size ;
    args = args || {};
    includeFallbackColors = args.includeFallbackColors ;
    level = args.level;
    size = args.size;

    for (var i= 0; i < colorList.length ; i++) {
        readability = tinycolor.readability(baseColor, colorList[i]);
        if (readability > bestScore) {
            bestScore = readability;
            bestColor = tinycolor(colorList[i]);
        }
    }

    if (tinycolor.isReadable(baseColor, bestColor, {"level":level,"size":size}) || !includeFallbackColors) {
        return bestColor;
    }
    else {
        args.includeFallbackColors=false;
        return tinycolor.mostReadable(baseColor,["#fff", "#000"],args);
    }
};


// Big List of Colors
// ------------------
// <http://www.w3.org/TR/css3-color/#svg-color>
var names = tinycolor.names = {
    aliceblue: "f0f8ff",
    antiquewhite: "faebd7",
    aqua: "0ff",
    aquamarine: "7fffd4",
    azure: "f0ffff",
    beige: "f5f5dc",
    bisque: "ffe4c4",
    black: "000",
    blanchedalmond: "ffebcd",
    blue: "00f",
    blueviolet: "8a2be2",
    brown: "a52a2a",
    burlywood: "deb887",
    burntsienna: "ea7e5d",
    cadetblue: "5f9ea0",
    chartreuse: "7fff00",
    chocolate: "d2691e",
    coral: "ff7f50",
    cornflowerblue: "6495ed",
    cornsilk: "fff8dc",
    crimson: "dc143c",
    cyan: "0ff",
    darkblue: "00008b",
    darkcyan: "008b8b",
    darkgoldenrod: "b8860b",
    darkgray: "a9a9a9",
    darkgreen: "006400",
    darkgrey: "a9a9a9",
    darkkhaki: "bdb76b",
    darkmagenta: "8b008b",
    darkolivegreen: "556b2f",
    darkorange: "ff8c00",
    darkorchid: "9932cc",
    darkred: "8b0000",
    darksalmon: "e9967a",
    darkseagreen: "8fbc8f",
    darkslateblue: "483d8b",
    darkslategray: "2f4f4f",
    darkslategrey: "2f4f4f",
    darkturquoise: "00ced1",
    darkviolet: "9400d3",
    deeppink: "ff1493",
    deepskyblue: "00bfff",
    dimgray: "696969",
    dimgrey: "696969",
    dodgerblue: "1e90ff",
    firebrick: "b22222",
    floralwhite: "fffaf0",
    forestgreen: "228b22",
    fuchsia: "f0f",
    gainsboro: "dcdcdc",
    ghostwhite: "f8f8ff",
    gold: "ffd700",
    goldenrod: "daa520",
    gray: "808080",
    green: "008000",
    greenyellow: "adff2f",
    grey: "808080",
    honeydew: "f0fff0",
    hotpink: "ff69b4",
    indianred: "cd5c5c",
    indigo: "4b0082",
    ivory: "fffff0",
    khaki: "f0e68c",
    lavender: "e6e6fa",
    lavenderblush: "fff0f5",
    lawngreen: "7cfc00",
    lemonchiffon: "fffacd",
    lightblue: "add8e6",
    lightcoral: "f08080",
    lightcyan: "e0ffff",
    lightgoldenrodyellow: "fafad2",
    lightgray: "d3d3d3",
    lightgreen: "90ee90",
    lightgrey: "d3d3d3",
    lightpink: "ffb6c1",
    lightsalmon: "ffa07a",
    lightseagreen: "20b2aa",
    lightskyblue: "87cefa",
    lightslategray: "789",
    lightslategrey: "789",
    lightsteelblue: "b0c4de",
    lightyellow: "ffffe0",
    lime: "0f0",
    limegreen: "32cd32",
    linen: "faf0e6",
    magenta: "f0f",
    maroon: "800000",
    mediumaquamarine: "66cdaa",
    mediumblue: "0000cd",
    mediumorchid: "ba55d3",
    mediumpurple: "9370db",
    mediumseagreen: "3cb371",
    mediumslateblue: "7b68ee",
    mediumspringgreen: "00fa9a",
    mediumturquoise: "48d1cc",
    mediumvioletred: "c71585",
    midnightblue: "191970",
    mintcream: "f5fffa",
    mistyrose: "ffe4e1",
    moccasin: "ffe4b5",
    navajowhite: "ffdead",
    navy: "000080",
    oldlace: "fdf5e6",
    olive: "808000",
    olivedrab: "6b8e23",
    orange: "ffa500",
    orangered: "ff4500",
    orchid: "da70d6",
    palegoldenrod: "eee8aa",
    palegreen: "98fb98",
    paleturquoise: "afeeee",
    palevioletred: "db7093",
    papayawhip: "ffefd5",
    peachpuff: "ffdab9",
    peru: "cd853f",
    pink: "ffc0cb",
    plum: "dda0dd",
    powderblue: "b0e0e6",
    purple: "800080",
    rebeccapurple: "663399",
    red: "f00",
    rosybrown: "bc8f8f",
    royalblue: "4169e1",
    saddlebrown: "8b4513",
    salmon: "fa8072",
    sandybrown: "f4a460",
    seagreen: "2e8b57",
    seashell: "fff5ee",
    sienna: "a0522d",
    silver: "c0c0c0",
    skyblue: "87ceeb",
    slateblue: "6a5acd",
    slategray: "708090",
    slategrey: "708090",
    snow: "fffafa",
    springgreen: "00ff7f",
    steelblue: "4682b4",
    tan: "d2b48c",
    teal: "008080",
    thistle: "d8bfd8",
    tomato: "ff6347",
    turquoise: "40e0d0",
    violet: "ee82ee",
    wheat: "f5deb3",
    white: "fff",
    whitesmoke: "f5f5f5",
    yellow: "ff0",
    yellowgreen: "9acd32"
};

// Make it easy to access colors via `hexNames[hex]`
var hexNames = tinycolor.hexNames = flip(names);


// Utilities
// ---------

// `{ 'name1': 'val1' }` becomes `{ 'val1': 'name1' }`
function flip(o) {
    var flipped = { };
    for (var i in o) {
        if (o.hasOwnProperty(i)) {
            flipped[o[i]] = i;
        }
    }
    return flipped;
}

// Return a valid alpha value [0,1] with all invalid values being set to 1
function boundAlpha(a) {
    a = parseFloat(a);

    if (isNaN(a) || a < 0 || a > 1) {
        a = 1;
    }

    return a;
}

// Take input from [0, n] and return it as [0, 1]
function bound01(n, max) {
    if (isOnePointZero(n)) { n = "100%"; }

    var processPercent = isPercentage(n);
    n = mathMin(max, mathMax(0, parseFloat(n)));

    // Automatically convert percentage into number
    if (processPercent) {
        n = parseInt(n * max, 10) / 100;
    }

    // Handle floating point rounding errors
    if ((Math.abs(n - max) < 0.000001)) {
        return 1;
    }

    // Convert into [0, 1] range if it isn't already
    return (n % max) / parseFloat(max);
}

// Force a number between 0 and 1
function clamp01(val) {
    return mathMin(1, mathMax(0, val));
}

// Parse a base-16 hex value into a base-10 integer
function parseIntFromHex(val) {
    return parseInt(val, 16);
}

// Need to handle 1.0 as 100%, since once it is a number, there is no difference between it and 1
// <http://stackoverflow.com/questions/7422072/javascript-how-to-detect-number-as-a-decimal-including-1-0>
function isOnePointZero(n) {
    return typeof n == "string" && n.indexOf('.') != -1 && parseFloat(n) === 1;
}

// Check to see if string passed in is a percentage
function isPercentage(n) {
    return typeof n === "string" && n.indexOf('%') != -1;
}

// Force a hex value to have 2 characters
function pad2(c) {
    return c.length == 1 ? '0' + c : '' + c;
}

// Replace a decimal with it's percentage value
function convertToPercentage(n) {
    if (n <= 1) {
        n = (n * 100) + "%";
    }

    return n;
}

// Converts a decimal to a hex value
function convertDecimalToHex(d) {
    return Math.round(parseFloat(d) * 255).toString(16);
}
// Converts a hex value to a decimal
function convertHexToDecimal(h) {
    return (parseIntFromHex(h) / 255);
}

var matchers = (function() {

    // <http://www.w3.org/TR/css3-values/#integers>
    var CSS_INTEGER = "[-\\+]?\\d+%?";

    // <http://www.w3.org/TR/css3-values/#number-value>
    var CSS_NUMBER = "[-\\+]?\\d*\\.\\d+%?";

    // Allow positive/negative integer/number.  Don't capture the either/or, just the entire outcome.
    var CSS_UNIT = "(?:" + CSS_NUMBER + ")|(?:" + CSS_INTEGER + ")";

    // Actual matching.
    // Parentheses and commas are optional, but not required.
    // Whitespace can take the place of commas or opening paren
    var PERMISSIVE_MATCH3 = "[\\s|\\(]+(" + CSS_UNIT + ")[,|\\s]+(" + CSS_UNIT + ")[,|\\s]+(" + CSS_UNIT + ")\\s*\\)?";
    var PERMISSIVE_MATCH4 = "[\\s|\\(]+(" + CSS_UNIT + ")[,|\\s]+(" + CSS_UNIT + ")[,|\\s]+(" + CSS_UNIT + ")[,|\\s]+(" + CSS_UNIT + ")\\s*\\)?";

    return {
        CSS_UNIT: new RegExp(CSS_UNIT),
        rgb: new RegExp("rgb" + PERMISSIVE_MATCH3),
        rgba: new RegExp("rgba" + PERMISSIVE_MATCH4),
        hsl: new RegExp("hsl" + PERMISSIVE_MATCH3),
        hsla: new RegExp("hsla" + PERMISSIVE_MATCH4),
        hsv: new RegExp("hsv" + PERMISSIVE_MATCH3),
        hsva: new RegExp("hsva" + PERMISSIVE_MATCH4),
        hex3: /^#?([0-9a-fA-F]{1})([0-9a-fA-F]{1})([0-9a-fA-F]{1})$/,
        hex6: /^#?([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})$/,
        hex4: /^#?([0-9a-fA-F]{1})([0-9a-fA-F]{1})([0-9a-fA-F]{1})([0-9a-fA-F]{1})$/,
        hex8: /^#?([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})$/
    };
})();

// `isValidCSSUnit`
// Take in a single string / number and check to see if it looks like a CSS unit
// (see `matchers` above for definition).
function isValidCSSUnit(color) {
    return !!matchers.CSS_UNIT.exec(color);
}

// `stringInputToObject`
// Permissive string parsing.  Take in a number of formats, and output an object
// based on detected format.  Returns `{ r, g, b }` or `{ h, s, l }` or `{ h, s, v}`
function stringInputToObject(color) {

    color = color.replace(trimLeft,'').replace(trimRight, '').toLowerCase();
    var named = false;
    if (names[color]) {
        color = names[color];
        named = true;
    }
    else if (color == 'transparent') {
        return { r: 0, g: 0, b: 0, a: 0, format: "name" };
    }

    // Try to match string input using regular expressions.
    // Keep most of the number bounding out of this function - don't worry about [0,1] or [0,100] or [0,360]
    // Just return an object and let the conversion functions handle that.
    // This way the result will be the same whether the tinycolor is initialized with string or object.
    var match;
    if ((match = matchers.rgb.exec(color))) {
        return { r: match[1], g: match[2], b: match[3] };
    }
    if ((match = matchers.rgba.exec(color))) {
        return { r: match[1], g: match[2], b: match[3], a: match[4] };
    }
    if ((match = matchers.hsl.exec(color))) {
        return { h: match[1], s: match[2], l: match[3] };
    }
    if ((match = matchers.hsla.exec(color))) {
        return { h: match[1], s: match[2], l: match[3], a: match[4] };
    }
    if ((match = matchers.hsv.exec(color))) {
        return { h: match[1], s: match[2], v: match[3] };
    }
    if ((match = matchers.hsva.exec(color))) {
        return { h: match[1], s: match[2], v: match[3], a: match[4] };
    }
    if ((match = matchers.hex8.exec(color))) {
        return {
            r: parseIntFromHex(match[1]),
            g: parseIntFromHex(match[2]),
            b: parseIntFromHex(match[3]),
            a: convertHexToDecimal(match[4]),
            format: named ? "name" : "hex8"
        };
    }
    if ((match = matchers.hex6.exec(color))) {
        return {
            r: parseIntFromHex(match[1]),
            g: parseIntFromHex(match[2]),
            b: parseIntFromHex(match[3]),
            format: named ? "name" : "hex"
        };
    }
    if ((match = matchers.hex4.exec(color))) {
        return {
            r: parseIntFromHex(match[1] + '' + match[1]),
            g: parseIntFromHex(match[2] + '' + match[2]),
            b: parseIntFromHex(match[3] + '' + match[3]),
            a: convertHexToDecimal(match[4] + '' + match[4]),
            format: named ? "name" : "hex8"
        };
    }
    if ((match = matchers.hex3.exec(color))) {
        return {
            r: parseIntFromHex(match[1] + '' + match[1]),
            g: parseIntFromHex(match[2] + '' + match[2]),
            b: parseIntFromHex(match[3] + '' + match[3]),
            format: named ? "name" : "hex"
        };
    }

    return false;
}

function validateWCAG2Parms(parms) {
    // return valid WCAG2 parms for isReadable.
    // If input parms are invalid, return {"level":"AA", "size":"small"}
    var level, size;
    parms = parms || {"level":"AA", "size":"small"};
    level = (parms.level || "AA").toUpperCase();
    size = (parms.size || "small").toLowerCase();
    if (level !== "AA" && level !== "AAA") {
        level = "AA";
    }
    if (size !== "small" && size !== "large") {
        size = "small";
    }
    return {"level":level, "size":size};
}

// Node: Export function
if (typeof module !== "undefined" && module.exports) {
    module.exports = tinycolor;
}
// AMD/requirejs: Define the module
else if (true) {
    !(__WEBPACK_AMD_DEFINE_RESULT__ = function () {return tinycolor;}.call(exports, __webpack_require__, exports, module),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
}
// Browser: Expose to window
else {
    window.tinycolor = tinycolor;
}

})(Math);


/***/ }),
/* 11 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
function parseXML(str) {
    var parser = new DOMParser();
    var doc = parser.parseFromString(str, 'application/xml');
    var result = {
        '__text__': '',
        '__ordinal__': []
    };
    var root = doc.documentElement;
    function traverse(e, parentObj) {
        var childObj = undefined;
        childObj = {
            '__text__': '',
            '__ordinal__': []
        };
        for (var i = 0; i < e.attributes.length; i++) {
            var key = e.attributes[i].name;
            childObj[key] = e.attributes[i].value;
            childObj['__ordinal__'].push(key);
        }
        if (e.childNodes.length == 1 && e.childNodes[0].nodeType === 3) {
            var textContent = (e.textContent).trim();
            if (e.attributes.length > 0) {
                childObj['__text__'] = textContent;
            }
            else {
                childObj = textContent;
            }
        }
        else if (e.childNodes.length == 0 && e.attributes.length == 0) {
            childObj = '';
        }
        if (parentObj) {
            if (parentObj[e.nodeName]) {
                if (Array.isArray(parentObj[e.nodeName])) {
                    parentObj[e.nodeName].push(childObj);
                    parentObj['__ordinal__'].push(e.nodeName);
                }
                else {
                    var lastObj = parentObj[e.nodeName];
                    parentObj[e.nodeName] = [];
                    parentObj[e.nodeName].push(lastObj);
                    parentObj[e.nodeName].push(childObj);
                    parentObj['__ordinal__'].push(e.nodeName);
                }
            }
            else {
                if (typeof parentObj === "string") {
                    parentObj = childObj;
                }
                else {
                    if (e.nodeName === 'Option') {
                        parentObj[e.nodeName] = [];
                        parentObj[e.nodeName].push(childObj);
                    }
                    else {
                        parentObj[e.nodeName] = childObj;
                    }
                    parentObj['__ordinal__'].push(e.nodeName);
                }
            }
        }
        for (var i = 0; i < e.childNodes.length; i++) {
            var children = e.childNodes;
            var childNode = children.item(i);
            if (childNode instanceof Element) {
                traverse(childNode, childObj);
            }
        }
    }
    traverse(root, result);
    return result;
}
exports.parseXML = parseXML;
function createXMLNode(path, attrs) {
    var doc = document.implementation.createDocument(null, '', null);
    var parentElement = doc;
    var currentElement;
    for (var _i = 0, path_1 = path; _i < path_1.length; _i++) {
        var pathName = path_1[_i];
        currentElement = doc.createElement(pathName);
        parentElement.appendChild(currentElement);
        parentElement = currentElement;
    }
    if (attrs.length > 0) {
        for (var i = 0; i < attrs.length; i++) {
            var name_val = attrs[i].split('=');
            var name_1 = name_val[0];
            var value = name_val[1];
            doc.firstElementChild.setAttribute(name_1, value);
        }
    }
    return doc;
}
function serializeWithValue(settingsPath, value, attrs) {
    if (attrs === void 0) { attrs = []; }
    var doc = createXMLNode(settingsPath, attrs);
    var q = settingsPath.join(' ');
    var element = doc.querySelector(q);
    element.textContent = value;
    var serializer = new XMLSerializer();
    return serializer.serializeToString(doc);
}
exports.serializeWithValue = serializeWithValue;


/***/ }),
/* 12 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlNode_1 = __webpack_require__(2);
/**
 * Represents an XML character reference.
 *
 * An XML character reference is structured as follows, where `{dec}` is the
 * decimal representation code point corresponding to a particular Unicode
 * character:
 *
 * ```xml
 * &#{dec};
 * ```
 *
 * The corresponding hexadecimal version is structured as follows, where
 * `{hex}` is the hexadecimal representation code point corresponding to a
 * particular Unicode character:
 *
 * ```xml
 * &#x{hex};
 * ```
 *
 * Unicode characters outside of the Basic Multilingual Plane are represented
 * using a surrogate pair consisting of two character references.
 *
 * The `{dec}` and `{hex}` values are defined by the `char` and `hex`
 * properties of this node; the former is the character to be represented while
 * the latter indicates whether the decimal or hexadecimal representation
 * should be used.
 *
 * XmlCharRef nodes cannot have any children.
 */
var XmlCharRef = (function (_super) {
    __extends(XmlCharRef, _super);
    /**
     * Initializes a new instance of the {@link XmlCharRef} class.
     *
     * @param {string} char   The character to represent using the reference.
     * @param {boolean} [hex] Whether to use the hexadecimal or decimal
     *                        representation for the reference. If left
     *                        undefined, decimal is the default.
     */
    function XmlCharRef(char, hex) {
        if (hex === void 0) { hex = false; }
        _super.call(this);
        this.char = char;
        this.hex = hex;
    }
    Object.defineProperty(XmlCharRef.prototype, "char", {
        /**
         * Gets the character to represent using the reference.
         *
         * @returns {string} The character to represent using the reference.
         */
        get: function () {
            return this._char;
        },
        /**
         * Sets the character to represent using the reference.
         *
         * @param {string} char The character to represent using the reference.
         */
        set: function (char) {
            if (!utils_1.isType(char, "String")) {
                throw new TypeError("char should be a string");
            }
            else if (!validate_1.validateSingleChar(char)) {
                throw new Error("char should contain a single character, and this" +
                    " character should be allowed in XML");
            }
            this._char = char;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(XmlCharRef.prototype, "hex", {
        /**
         * Gets whether or not to use the hexadecimal or decimal representation for
         * the reference.
         *
         * @returns {boolean} Whether or not to use the hexadecimal or decimal
         *                    representation for the reference.
         */
        get: function () {
            return this._hex;
        },
        /**
         * Sets whether or not to use the hexadecimal or decimal representation for
         * the reference.
         *
         * @param {boolean} hex Whether or not to use the hexadecimal or decimal
         *                      representation for the reference.
         */
        set: function (hex) {
            if (!utils_1.isType(hex, "Boolean")) {
                throw new TypeError("hex should be a boolean");
            }
            this._hex = hex;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Throws an exception since {@link XmlCharRef} nodes cannot have any
     * children.
     */
    XmlCharRef.prototype.children = function () {
        throw new Error("XmlCharRef nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlCharRef} nodes cannot have any
     * children.
     */
    XmlCharRef.prototype.insertChild = function (node, index) {
        throw new Error("XmlCharRef nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlCharRef} nodes cannot have any
     * children.
     */
    XmlCharRef.prototype.removeChild = function (node) {
        throw new Error("XmlCharRef nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlCharRef} nodes cannot have any
     * children.
     */
    XmlCharRef.prototype.removeChildAtIndex = function (index) {
        throw new Error("XmlCharRef nodes cannot have children");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlCharRef.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        var char;
        if (this.char.length === 1) {
            char = this.char.charCodeAt(0);
        }
        else {
            char = utils_1.getCodePoint(this.char, 0);
        }
        if (this.hex) {
            return "&#x" + char.toString(16) + ";";
        }
        else {
            return "&#" + char + ";";
        }
    };
    return XmlCharRef;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlCharRef;


/***/ }),
/* 13 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlNode_1 = __webpack_require__(2);
/**
 * Represents an XML entity reference.
 *
 * An XML entity reference is structured as follows, where
 * `{entity}` is name of the entity to be referenced:
 *
 * ```xml
 * &{entity};
 * ```
 *
 * The `{entity}` value is a property of this node.
 *
 * XmlEntityRef nodes cannot have any children.
 */
var XmlEntityRef = (function (_super) {
    __extends(XmlEntityRef, _super);
    /**
     * Initializes a new instance of the {@link XmlEntityRef} class.
     *
     * @param {string} entity The entity to be referenced.
     */
    function XmlEntityRef(entity) {
        _super.call(this);
        this.entity = entity;
    }
    Object.defineProperty(XmlEntityRef.prototype, "entity", {
        /**
         * Gets the entity to be referenced.
         *
         * @returns {string} The entity to be referenced.
         */
        get: function () {
            return this._entity;
        },
        /**
         * Sets the entity to be referenced.
         *
         * @param {string} entity The entity to be referenced.
         */
        set: function (entity) {
            if (!utils_1.isType(entity, "String")) {
                throw new TypeError("entity should be a string");
            }
            else if (!validate_1.validateName(entity)) {
                throw new Error("entity should not contain characters" +
                    " not allowed in XML names");
            }
            this._entity = entity;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Throws an exception since {@link XmlEntityRef} nodes cannot have any
     * children.
     */
    XmlEntityRef.prototype.children = function () {
        throw new Error("XmlEntityRef nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlEntityRef} nodes cannot have any
     * children.
     */
    XmlEntityRef.prototype.insertChild = function (node, index) {
        throw new Error("XmlEntityRef nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlEntityRef} nodes cannot have any
     * children.
     */
    XmlEntityRef.prototype.removeChild = function (node) {
        throw new Error("XmlEntityRef nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlEntityRef} nodes cannot have any
     * children.
     */
    XmlEntityRef.prototype.removeChildAtIndex = function (index) {
        throw new Error("XmlEntityRef nodes cannot have children");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlEntityRef.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        return "&" + this.entity + ";";
    };
    return XmlEntityRef;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlEntityRef;


/***/ }),
/* 14 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var escape_1 = __webpack_require__(18);
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlNode_1 = __webpack_require__(2);
/**
 * Represents text in an XML document.
 *
 * This text may only consist of character data, not markup. Restricted
 * characters, such as the ampersand (`&`) and the opening angle bracket (`<`)
 * are all automatically escaped.
 *
 * To create an character reference or entity reference, you should use
 * {@link XmlCharRef} or {@link XmlEntityRef} respectively instead.
 *
 * XmlText nodes cannot have any children.
 */
var XmlText = (function (_super) {
    __extends(XmlText, _super);
    /**
     * Initializes a new instance of the {@link XmlText} class.
     *
     * @param {string} text Arbitrary character data.
     */
    function XmlText(text) {
        _super.call(this);
        this.text = text;
    }
    Object.defineProperty(XmlText.prototype, "text", {
        /**
         * Gets the text associated with this node.
         *
         * @returns {string} The text associated with this node.
         */
        get: function () {
            return this._text;
        },
        /**
         * Sets the text associated with this node.
         *
         * @param {string} text Arbitrary character data.
         */
        set: function (text) {
            if (!utils_1.isType(text, "String")) {
                throw new TypeError("text should be a string");
            }
            else if (!validate_1.validateChar(text)) {
                throw new Error("text should not contain characters not allowed" +
                    " in XML");
            }
            this._text = text;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Throws an exception since {@link XmlText} nodes cannot have any
     * children.
     */
    XmlText.prototype.children = function () {
        throw new Error("XmlText nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlText} nodes cannot have any
     * children.
     */
    XmlText.prototype.insertChild = function (node, index) {
        throw new Error("XmlText nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlText} nodes cannot have any
     * children.
     */
    XmlText.prototype.removeChild = function (node) {
        throw new Error("XmlText nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlText} nodes cannot have any
     * children.
     */
    XmlText.prototype.removeChildAtIndex = function (index) {
        throw new Error("XmlText nodes cannot have children");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlText.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        var str = this.text;
        str = escape_1.escapeAmpersands(str);
        str = escape_1.escapeLeftAngleBrackets(str);
        return str;
    };
    return XmlText;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlText;


/***/ }),
/* 15 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
var XMLParser = __webpack_require__(11);
var custom_palette_1 = __webpack_require__(30);
var node_types_1 = __webpack_require__(40);
var ui = __webpack_require__(0);
var tinycolor = __webpack_require__(10);
var SVG_NS = 'http://www.w3.org/2000/svg';
var XLINK_NS = 'http://www.w3.org/1999/xlink';
var MobilePaneItem = (function () {
    function MobilePaneItem(paneEl, readOnly, client) {
        var _this = this;
        this.tooltipVisible = false;
        this.paneEl = paneEl;
        this.visible = false;
        this.client = client;
        this.val = undefined;
        this.readOnly = readOnly;
        this.titleEl = document.createElement('div');
        this.wrapperEl = document.createElement('div');
        this.wrapperEl.ontouchstart = function (e) {
            if (ui.showTooltips && ((_this instanceof MobileGroup && _this.tooltip) || !(_this instanceof MobileGroup))) {
                e.preventDefault();
                e.stopPropagation();
                _this.showTooltip();
                _this.tooltipVisible = true;
            }
        };
        this.wrapperEl.onmousedown = function (e) {
            if (ui.showTooltips && ((_this instanceof MobileGroup && _this.tooltip) || !(_this instanceof MobileGroup))) {
                e.preventDefault();
                e.stopPropagation();
                _this.showTooltip();
                _this.tooltipVisible = true;
            }
        };
        setTimeout(function () {
            _this.applyTooltipListener();
        });
    }
    MobilePaneItem.prototype.applyTooltipListener = function () {
        this.tooltipWrapper = $(this.paneEl).parent().children('#mobile-pane-tooltip')[0];
        $(this.tooltipWrapper).hide();
    };
    MobilePaneItem.prototype.highlightSearchString = function (searchString) {
        if (!searchString) {
            this.titleEl.innerHTML = this.key;
            return;
        }
        var length = searchString.length;
        var start = this.key.toLowerCase().indexOf(searchString);
        var titleLength = this.key.length;
        var formattedTitle = "";
        if (start !== -1) {
            for (var i = 0; i < titleLength; i++) {
                if (i >= start && i < start + length) {
                    formattedTitle += '<span class="pane-search-text-highlight">' + this.key[i] + '</span>';
                }
                else {
                    formattedTitle += this.key[i];
                }
            }
            this.titleEl.innerHTML = formattedTitle;
        }
        else {
            this.titleEl.innerHTML = this.key;
        }
    };
    MobilePaneItem.prototype.showTooltip = function () {
        $(this.tooltipWrapper).empty();
        var tooltipTitle = document.createElement('div');
        tooltipTitle.textContent = this.key;
        tooltipTitle.className = 'mobile-pane-tooltip-title';
        var tooltipText = document.createElement('div');
        tooltipText.textContent = this.tooltip;
        tooltipText.className = 'mobile-pane-tooltip-message';
        this.tooltipWrapper.appendChild(tooltipTitle);
        this.tooltipWrapper.appendChild(tooltipText);
        $(this.tooltipWrapper).show();
        document.addEventListener('touchstart', this.detectOusideClick.bind(this));
        document.addEventListener('mousedown', this.detectOusideClick.bind(this));
    };
    MobilePaneItem.prototype.setToolTip = function (tooltip) {
        this.tooltip = tooltip;
    };
    MobilePaneItem.prototype.detectOusideClick = function (ev) {
        if (ev.target !== this.tooltipWrapper && !this.tooltipWrapper.contains(ev.target)) {
            $(this.tooltipWrapper).hide();
            document.removeEventListener('touchstart', this.detectOusideClick.bind(this));
            document.removeEventListener('mousedown', this.detectOusideClick.bind(this));
            this.tooltipVisible = false;
        }
    };
    MobilePaneItem.prototype.hide = function () {
        if (this instanceof MobileGroup) {
            $(this.panelContainer).hide();
        }
        $(this.wrapperEl).hide();
    };
    MobilePaneItem.prototype.show = function () {
        if (this.children && this instanceof MobileGroup) {
            var vis = false;
            for (var i = 0; i < this.children.length; i++) {
                if (this.children[i] instanceof MobileGroup) {
                    var group = this.children[i];
                    vis = this.hasVisibleChildren(group);
                    if (vis) {
                        break;
                    }
                }
                else {
                    vis = this.children[i].visible;
                    if (vis) {
                        break;
                    }
                }
            }
            if (vis && (!this.parent || !this.parent.isCollapsed)) {
                $(this.wrapperEl).show();
                $(this.panelContainer).show();
            }
        }
        else if ((this.val !== undefined || (this instanceof MobileSelect && (this.valueEl.id === 'UID_PRESET' || this.valueEl.id === 'UID_DENSITY_PRESET')))
            && this.parent && !this.parent.isCollapsed) {
            $(this.wrapperEl).show();
        }
    };
    MobilePaneItem.prototype.hasVisibleChildren = function (group) {
        var vis = false;
        for (var i = 0; i < group.children.length; i++) {
            if (group.children[i] instanceof MobileGroup) {
                var childGroup = group.children[i];
                vis = this.hasVisibleChildren(childGroup);
                if (vis) {
                    return true;
                }
            }
            else {
                vis = group.children[i].visible;
                if (vis) {
                    return true;
                }
            }
        }
        return vis;
    };
    return MobilePaneItem;
}());
exports.MobilePaneItem = MobilePaneItem;
var MobileGroup = (function (_super) {
    __extends(MobileGroup, _super);
    function MobileGroup(paneEl, groupName, isCollapsed, parent, level, client, flag) {
        var _this = _super.call(this, paneEl, false, client) || this;
        _this.level = level;
        _this.flag = flag;
        _this.wrapperEl.className = "mobile-pane-group";
        _this.wrapperEl.style.marginLeft = (level * 10) + 'px';
        _this.panelContainer = document.createElement('div');
        _this.panelContainer.style.marginBottom = '5px';
        _this.titleEl.className = 'mobile-pane-group-title';
        _this.setKey(groupName);
        _this.wrapperEl.appendChild(_this.titleEl);
        _this.toggleIcon = document.createElement('i');
        _this.toggleIcon.className = 'fas fa-chevron-circle-right mobile-pane-group-toggle-icon';
        _this.visible = true;
        _this.val = 'group';
        if (!parent) {
            paneEl.appendChild(_this.wrapperEl);
            paneEl.appendChild(_this.panelContainer);
        }
        else {
            _this.wrapperEl.appendChild(_this.toggleIcon);
            parent.panelContainer.appendChild(_this.wrapperEl);
            parent.panelContainer.appendChild(_this.panelContainer);
        }
        _this.isCollapsed = isCollapsed;
        _this.children = [];
        _this.parent = parent;
        if ((!_this.isCollapsed && _this.parent) || !parent) {
            _this.show();
        }
        else {
            _this.hide();
        }
        $(_this.wrapperEl).click(function (e) {
            e.stopPropagation();
            if (!_this.parent) {
                return;
            }
            if (_this.isCollapsed) {
                _this.expand();
            }
            else {
                _this.collapse();
            }
        });
        if (parent) {
            parent.addChild(_this);
        }
        return _this;
    }
    MobileGroup.prototype.hasChild = function (child) {
        var isChild = false;
        for (var i = 0; i < this.children.length; i++) {
            if (this.children[i] instanceof MobileGroup) {
                var childGroup = this.children[i];
                isChild = childGroup.hasChild(child);
                if (isChild) {
                    return true;
                }
            }
            else {
                isChild = this.children[i] === child;
                if (isChild) {
                    return true;
                }
            }
        }
        return isChild;
    };
    MobileGroup.prototype.setValue = function (val) {
    };
    MobileGroup.prototype.setKey = function (key) {
        this.key = key.toString();
        this.titleEl.textContent = key;
    };
    MobileGroup.prototype.addChild = function (child) {
        this.children.push(child);
    };
    MobileGroup.prototype.collapse = function (hideSelf) {
        if (hideSelf === void 0) { hideSelf = false; }
        this.isCollapsed = true;
        $(this.panelContainer).hide();
        $(this.toggleIcon).removeClass('fa-chevron-circle-down');
        $(this.toggleIcon).addClass('fa-chevron-circle-right');
        for (var i = 0; i < this.children.length; i++) {
            this.children[i].hide();
            if (this.children[i] instanceof MobileGroup) {
                var group = this.children[i];
                if (group.isCollapsed) {
                    group.collapse();
                }
            }
        }
        if (hideSelf) {
            this.hide();
        }
    };
    MobileGroup.prototype.expand = function (showSelf) {
        if (showSelf === void 0) { showSelf = false; }
        if (showSelf) {
            this.show();
        }
        this.isCollapsed = false;
        $(this.panelContainer).show();
        $(this.toggleIcon).removeClass('fa-chevron-circle-right');
        $(this.toggleIcon).addClass('fa-chevron-circle-down');
        for (var i = 0; i < this.children.length; i++) {
            if (this.children[i].visible) {
                this.children[i].show();
            }
            if (this.children[i] instanceof MobileGroup && this.children[i].visible) {
                var group = this.children[i];
                if (!group.isCollapsed) {
                    group.expand();
                }
            }
        }
    };
    MobileGroup.prototype.clear = function () {
        $(this.panelContainer).empty();
        for (var i = 0; i < this.children.length; i++) {
            if (this.children[i] instanceof MobileGroup) {
                var childGroup = this.children[i];
                childGroup.clear();
            }
        }
        this.children = [];
    };
    return MobileGroup;
}(MobilePaneItem));
exports.MobileGroup = MobileGroup;
var MobileInput = (function (_super) {
    __extends(MobileInput, _super);
    function MobileInput(paneEl, source, path, title, parent, level, readOnly, client, callback) {
        var _this = _super.call(this, paneEl, readOnly, client) || this;
        _this.isNumber = false;
        _this.path = path;
        _this.parent = parent;
        _this.level = level;
        _this.source = source;
        _this.client = client;
        _this.callback = callback;
        _this.wrapperEl.className = "mobile-pane-input-wrapper";
        _this.wrapperEl.style.marginLeft = (level * 10) + 'px';
        if (!parent) {
            paneEl.appendChild(_this.wrapperEl);
        }
        else {
            parent.panelContainer.appendChild(_this.wrapperEl);
        }
        _this.titleEl.className = 'mobile-pane-input-title';
        _this.setKey(title);
        _this.wrapperEl.appendChild(_this.titleEl);
        _this.valueEl = document.createElement('div');
        _this.valueEl.contentEditable = (!_this.readOnly).toString();
        if (!_this.readOnly) {
            _this.valueEl.style.border = '1px solid rgb(169, 169, 169)';
            _this.valueEl.style.padding = '2px';
        }
        _this.valueEl.className = 'mobile-pane-input-val';
        _this.wrapperEl.appendChild(_this.valueEl);
        if (!_this.parent || !_this.parent.isCollapsed) {
            _this.show();
        }
        else {
            _this.hide();
        }
        if (_this.path || _this.callback) {
            _this.valueEl.addEventListener('keyup', function (ev) {
                ev.preventDefault();
                ev.stopPropagation();
            });
            _this.valueEl.addEventListener('change', function (ev) {
                if (_this.unit) {
                    _this.valueEl.textContent += _this.unit === '°' ? _this.unit : ' ' + _this.unit;
                }
                _this.fireChange();
            });
            _this.valueEl.addEventListener('blur', function (ev) {
                if (_this.unit) {
                    _this.valueEl.textContent += _this.unit === '°' ? _this.unit : ' ' + _this.unit;
                }
                _this.fireChange();
            });
            _this.valueEl.addEventListener('focus', function (ev) {
                if (_this.suffix) {
                    _this.valueEl.textContent = _this.val;
                }
                else if (_this.unit || _this.isNumber) {
                    _this.valueEl.textContent = parseFloat(_this.val).toString();
                }
            });
        }
        else {
            _this.visible = true;
            _this.val = "none";
        }
        if (parent) {
            parent.addChild(_this);
        }
        return _this;
    }
    MobileInput.prototype.fireChange = function () {
        if (this.callback) {
            this.callback(this.valueEl.textContent);
        }
        else {
            var val = (this.suffix && this.suffix === '%') ? (parseFloat(this.valueEl.textContent) / 100).toString() : this.valueEl.textContent;
            var xml = XMLParser.serializeWithValue(this.path, val);
            if (this.source !== 'setting') {
                xml = createSettingsPath(xml, this.source);
            }
            else {
                if ($('#mobile-settings-pane-default-toggle').val() === 'true'
                    && (this.paneEl.parentElement.style.left === '0px' || this.paneEl.parentElement.style.right === '0px')) {
                    xml = '<Call Method="SetSettings">' + xml + '</Call>';
                }
                else {
                    xml = '<Call Method="SetSceneSettings">' + xml + '</Call>';
                }
            }
            this.client.command.executeXML(xml.replace(/\n<\//, "</"));
            if (this.path.indexOf('ClashDetection') !== -1) {
                this.client.emit('ClashVisualizationChanged');
            }
        }
    };
    MobileInput.prototype.setValue = function (val) {
        this.val = val;
        var displayVal = val;
        if (this.suffix && this.valueEl !== document.activeElement && !this.unit) {
            displayVal = this.suffix === '%' ? displayVal * 100 : displayVal;
            this.val = displayVal;
            displayVal += this.suffix === '°' ? this.suffix : ' ' + this.suffix;
        }
        this.valueEl.textContent = displayVal;
    };
    MobileInput.prototype.setKey = function (key) {
        this.key = key.toString();
        this.titleEl.textContent = key;
    };
    MobileInput.prototype.setUnit = function (unit) {
        this.unit = unit;
    };
    return MobileInput;
}(MobilePaneItem));
exports.MobileInput = MobileInput;
var MobileFlag = (function () {
    function MobileFlag(path, client, paneEl) {
        this.stateString = "";
        this.options = [];
        this.path = path;
        this.client = client;
        this.paneEl = paneEl;
    }
    MobileFlag.prototype.addOption = function (option) {
        this.options.push(option);
    };
    MobileFlag.prototype.setOptions = function (string) {
        var stringParts;
        if (string === undefined || string === null || string === '') {
            stringParts = [''];
        }
        else {
            stringParts = string.split(' ');
        }
        this.options.forEach(function (option) {
            if (stringParts.indexOf(option.optionVal) === -1) {
                option.setValue('false');
            }
            else {
                option.setValue('true');
            }
        });
    };
    MobileFlag.prototype.triggerChange = function () {
        this.stateString = "";
        for (var i = 0; i < this.options.length; i++) {
            this.stateString += this.options[i].val ? this.options[i].optionVal : "";
            if (i !== this.options.length - 1) {
                this.stateString += " ";
            }
        }
        var xml = XMLParser.serializeWithValue(this.path, this.stateString);
        if ($('#mobile-settings-pane-default-toggle').val() === 'true'
            && (this.paneEl.parentElement.style.left === '0px' || this.paneEl.parentElement.style.right === '0px')) {
            xml = '<Call Method="SetSettings">' + xml + '</Call>';
        }
        else {
            xml = '<Call Method="SetSceneSettings">' + xml + '</Call>';
        }
        this.client.command.executeXML(xml);
    };
    return MobileFlag;
}());
exports.MobileFlag = MobileFlag;
var MobileFlagOption = (function (_super) {
    __extends(MobileFlagOption, _super);
    function MobileFlagOption(paneEl, optionVal, title, parent, level, readOnly, client, flag) {
        var _this = _super.call(this, paneEl, readOnly, client) || this;
        _this.parent = parent;
        _this.level = level;
        _this.flag = flag;
        _this.val = false;
        _this.optionVal = optionVal;
        _this.wrapperEl.className = "mobile-pane-switch-wrapper";
        _this.wrapperEl.style.marginLeft = (level * 10) + 'px';
        if (!parent) {
            paneEl.appendChild(_this.wrapperEl);
        }
        else {
            parent.panelContainer.appendChild(_this.wrapperEl);
        }
        _this.hiddenCheckbox = document.createElement('input');
        _this.hiddenCheckbox.type = 'checkbox';
        _this.hiddenCheckbox.value = _this.val;
        _this.hiddenCheckbox.style.display = 'none';
        _this.wrapperEl.appendChild(_this.hiddenCheckbox);
        _this.titleEl.className = 'mobile-pane-switch-title';
        _this.setKey(title);
        _this.wrapperEl.appendChild(_this.titleEl);
        _this.boxEl = document.createElement('div');
        _this.boxEl.className = 'switch-wrapper switch-on mobile-switch';
        _this.wrapperEl.appendChild(_this.boxEl);
        _this.handleEl = document.createElement('div');
        _this.handleEl.className = 'switch-handle';
        _this.boxEl.appendChild(_this.handleEl);
        $(_this.boxEl).click(function (e) {
            _this.setValue(!_this.val);
            _this.flag.triggerChange();
        });
        if (!_this.parent || !_this.parent.isCollapsed) {
            _this.show();
        }
        else {
            _this.hide();
        }
        if (parent) {
            parent.addChild(_this);
        }
        return _this;
    }
    MobileFlagOption.prototype.setValue = function (val) {
        this.val = (val === 'true' || val === true);
        this.hiddenCheckbox.value = this.val;
        if (this.val) {
            $(this.boxEl).removeClass('switch-off').addClass('switch-on');
        }
        else {
            $(this.boxEl).removeClass('switch-on').addClass('switch-off');
        }
    };
    MobileFlagOption.prototype.setKey = function (key) {
        this.key = key.toString();
        this.titleEl.textContent = key;
    };
    return MobileFlagOption;
}(MobilePaneItem));
exports.MobileFlagOption = MobileFlagOption;
var MobileSwitch = (function (_super) {
    __extends(MobileSwitch, _super);
    function MobileSwitch(paneEl, source, path, title, parent, level, readOnly, client, callback) {
        var _this = _super.call(this, paneEl, readOnly, client) || this;
        _this.path = path;
        _this.parent = parent;
        _this.level = level;
        _this.source = source;
        _this.wrapperEl.className = "mobile-pane-switch-wrapper";
        _this.wrapperEl.style.marginLeft = (level * 10) + 'px';
        if (!parent) {
            paneEl.appendChild(_this.wrapperEl);
        }
        else {
            parent.panelContainer.appendChild(_this.wrapperEl);
        }
        _this.hiddenCheckbox = document.createElement('input');
        _this.hiddenCheckbox.type = 'checkbox';
        _this.hiddenCheckbox.value = _this.val;
        _this.hiddenCheckbox.style.display = 'none';
        _this.wrapperEl.appendChild(_this.hiddenCheckbox);
        _this.titleEl.className = 'mobile-pane-switch-title';
        _this.setKey(title);
        _this.wrapperEl.appendChild(_this.titleEl);
        _this.boxEl = document.createElement('div');
        _this.boxEl.className = 'switch-wrapper switch-on mobile-switch';
        _this.wrapperEl.appendChild(_this.boxEl);
        _this.handleEl = document.createElement('div');
        _this.handleEl.className = 'switch-handle';
        _this.boxEl.appendChild(_this.handleEl);
        $(_this.boxEl).click(function (e) {
            if (callback) {
                callback(_this.val);
                return;
            }
            if (path) {
                var val = _this.val ? 'false' : 'true';
                var xml = XMLParser.serializeWithValue(_this.path, val);
                if (_this.source !== 'setting') {
                    xml = createSettingsPath(xml, _this.source);
                }
                else {
                    if ($('#mobile-settings-pane-default-toggle').val() === 'true'
                        && (_this.paneEl.parentElement.style.left === '0px' || _this.paneEl.parentElement.style.right === '0px')) {
                        xml = '<Call Method="SetSettings">' + xml + '</Call>';
                    }
                    else {
                        xml = '<Call Method="SetSceneSettings">' + xml + '</Call>';
                    }
                }
                client.command.executeXML(xml);
                if (_this.path.indexOf('ClashDetection') !== -1) {
                    client.emit('ClashVisualizationChanged');
                }
            }
            else {
                _this.setValue(!_this.val + '');
            }
        });
        if (!_this.parent || !_this.parent.isCollapsed) {
            _this.show();
        }
        else {
            _this.hide();
        }
        if (parent) {
            parent.addChild(_this);
        }
        return _this;
    }
    MobileSwitch.prototype.setValue = function (val) {
        this.val = (val === 'true');
        this.hiddenCheckbox.value = this.val;
        if (this.val) {
            $(this.boxEl).removeClass('switch-off').addClass('switch-on');
        }
        else {
            $(this.boxEl).removeClass('switch-on').addClass('switch-off');
        }
    };
    MobileSwitch.prototype.setKey = function (key) {
        this.key = key.toString();
        this.titleEl.textContent = key;
    };
    return MobileSwitch;
}(MobilePaneItem));
exports.MobileSwitch = MobileSwitch;
var MobileSelect = (function (_super) {
    __extends(MobileSelect, _super);
    function MobileSelect(paneEl, source, path, title, parent, level, readOnly, options, client, callback) {
        var _this = _super.call(this, paneEl, readOnly, client) || this;
        _this.path = path;
        _this.parent = parent;
        _this.level = level;
        _this.source = source;
        _this.wrapperEl.className = "mobile-pane-select-wrapper";
        _this.wrapperEl.style.marginLeft = (level * 10) + 'px';
        if (!parent) {
            paneEl.appendChild(_this.wrapperEl);
        }
        else {
            parent.panelContainer.appendChild(_this.wrapperEl);
        }
        _this.titleEl.className = 'mobile-pane-select-title';
        _this.setKey(title);
        _this.wrapperEl.appendChild(_this.titleEl);
        _this.valueEl = document.createElement('select');
        _this.valueEl.className = 'mobile-pane-select-val';
        if (options && options.length > 0) {
            _this.addOptions(options);
        }
        _this.wrapperEl.appendChild(_this.valueEl);
        if (!_this.parent || !_this.parent.isCollapsed) {
            _this.show();
        }
        else {
            _this.hide();
        }
        _this.valueEl.addEventListener('change', function (ev) {
            var val = _this.valueEl.value;
            if (callback) {
                callback(val);
                return;
            }
            if (_this.valueEl.id === 'UID_PRESET') {
                var xml_1 = '<Event Type="NodePropertiesChanged"><PhysicalProperties><Density>';
                xml_1 += val;
                xml_1 += '</Density></PhysicalProperties></Event>';
                client.command.executeXML(xml_1);
                return;
            }
            var xml = XMLParser.serializeWithValue(_this.path, val);
            if (_this.source !== 'setting') {
                if (_this.path[1] === 'Preset' && val !== 'USERDEF') {
                    var xml_2 = '<Call Method="ActivatePreset"><Type>WallthicknessHeatmap</Type>';
                    xml_2 += '<Identifier>' + val + '</Identifier></Call>';
                    client.command.executeXML(xml_2);
                    client.on('ActivatePreset', function (ev) {
                        client.command.executeXML('<Call Method="GetSceneSettings"></Call>');
                    });
                }
                xml = createSettingsPath(xml, _this.source);
            }
            else {
                if ($('#mobile-settings-pane-default-toggle').val() === 'true'
                    && (_this.paneEl.parentElement.style.left === '0px' || _this.paneEl.parentElement.style.right === '0px')) {
                    xml = '<Call Method="SetSettings">' + xml + '</Call>';
                }
                else {
                    xml = '<Call Method="SetSceneSettings">' + xml + '</Call>';
                }
            }
            client.command.executeXML(xml);
            if (_this.path.indexOf('ClashDetection') !== -1) {
                client.emit('ClashVisualizationChanged');
            }
        });
        if (parent) {
            parent.addChild(_this);
        }
        return _this;
    }
    MobileSelect.prototype.clearOptions = function () {
        this.options = [];
        $(this.valueEl).empty();
    };
    MobileSelect.prototype.addOptions = function (options) {
        this.options = options;
        for (var i = 0; i < options.length; i++) {
            var optionEl = document.createElement('option');
            optionEl.value = options[i].value;
            optionEl.textContent = options[i].display;
            this.valueEl.appendChild(optionEl);
        }
    };
    MobileSelect.prototype.setValue = function (val) {
        for (var i = 0; i < this.options.length; i++) {
            if (this.options[i].value === val) {
                this.val = this.options[i].value;
                this.valueEl.value = val;
                break;
            }
        }
    };
    MobileSelect.prototype.setKey = function (key) {
        this.key = key.toString();
        this.titleEl.textContent = key;
    };
    return MobileSelect;
}(MobilePaneItem));
exports.MobileSelect = MobileSelect;
var MobileButton = (function (_super) {
    __extends(MobileButton, _super);
    function MobileButton(paneEl, title, parent, level, readOnly, client, callback) {
        var _this = _super.call(this, paneEl, readOnly, client) || this;
        _this.parent = parent;
        _this.level = level;
        _this.callback = callback;
        _this.wrapperEl.className = "mobile-pane-button-wrapper";
        _this.wrapperEl.style.marginLeft = (level * 10) + 'px';
        _this.val = 'button';
        _this.visible = true;
        if (!parent) {
            paneEl.appendChild(_this.wrapperEl);
        }
        else {
            parent.panelContainer.appendChild(_this.wrapperEl);
        }
        _this.titleEl.className = 'mobile-pane-button-title';
        _this.setKey(title);
        _this.wrapperEl.appendChild(_this.titleEl);
        if (!_this.parent || !_this.parent.isCollapsed) {
            _this.show();
        }
        else {
            _this.hide();
        }
        if (_this.callback) {
            _this.wrapperEl.onclick = function (e) {
                _this.callback();
            };
        }
        if (parent) {
            parent.addChild(_this);
        }
        return _this;
    }
    MobileButton.prototype.setValue = function (val) {
    };
    MobileButton.prototype.setKey = function (key) {
        this.key = key.toString();
        this.titleEl.textContent = key;
    };
    return MobileButton;
}(MobilePaneItem));
exports.MobileButton = MobileButton;
var MobileSlider = (function (_super) {
    __extends(MobileSlider, _super);
    function MobileSlider(paneEl, source, title, path, parent, level, readOnly, client, callback) {
        var _this = _super.call(this, paneEl, readOnly, client) || this;
        _this.parent = parent;
        _this.level = level;
        _this.source = source;
        _this.path = path;
        _this.callback = callback;
        _this.wrapperEl.className = "mobile-pane-slider-wrapper";
        _this.wrapperEl.style.marginLeft = (level * 10) + 'px';
        _this.position = 0;
        _this.handlePosStyle = {};
        _this.fedBarWidthStyle = {};
        _this.sliderOffset = 0;
        _this.sliderWidth = 0;
        _this.handleMouseDown = _this.handleMouseDown.bind(_this);
        _this.handleMouseMove = _this.handleMouseMove.bind(_this);
        _this.handleMouseUp = _this.handleMouseUp.bind(_this);
        if (!parent) {
            paneEl.appendChild(_this.wrapperEl);
        }
        else {
            parent.panelContainer.appendChild(_this.wrapperEl);
        }
        _this.titleEl.className = 'mobile-pane-slider-title';
        _this.setKey(title);
        _this.wrapperEl.appendChild(_this.titleEl);
        _this.inputEl = document.createElement('input');
        _this.inputEl.className = 'slider-value-input';
        _this.inputEl.min = '0';
        _this.inputEl.max = '100';
        _this.inputEl.style.width = '40px';
        $(_this.inputEl).blur(function (ev) {
            _this.setValue(parseFloat(_this.inputEl.value) / 100);
        });
        $(_this.inputEl).change(function (ev) {
            _this.handleInputChanged(ev, false);
        });
        _this.wrapperEl.appendChild(_this.inputEl);
        var sliderWrapper = document.createElement('div');
        sliderWrapper.className = 'slider-wrapper';
        sliderWrapper.style.width = 'calc(100% - 50px)';
        _this.wrapperEl.appendChild(sliderWrapper);
        var sliderBar = document.createElement('div');
        sliderBar.style.position = 'relative';
        sliderBar.className = 'slider-bar';
        sliderBar.onmousedown = function (ev) {
            _this.handleClick(ev);
        };
        sliderWrapper.appendChild(sliderBar);
        _this.fedBar = document.createElement('span');
        _this.fedBar.className = 'slider-bar-filled';
        sliderBar.appendChild(_this.fedBar);
        _this.handleEl = document.createElement('span');
        _this.handleEl.style.marginLeft = '0';
        $(_this.handleEl).addClass('slider-handle draggable');
        $(_this.handleEl).kendoTouch({
            dragstart: function (ev) {
                _this.handleMouseDown(ev);
            },
            dragend: function (ev) {
                _this.handleMouseUp(ev);
            },
            drag: function (ev) {
                _this.handleMouseMove(ev);
            }
        });
        _this.handleEl.onmousedown = function (ev) {
            _this.handleMouseDown(ev);
        };
        sliderBar.appendChild(_this.handleEl);
        var emptyBar = document.createElement('span');
        emptyBar.className = 'slider-bar-empty';
        emptyBar.style.width = '100%';
        sliderBar.appendChild(emptyBar);
        if (!_this.parent || !_this.parent.isCollapsed) {
            _this.show();
        }
        else {
            _this.hide();
        }
        if (parent) {
            parent.addChild(_this);
        }
        return _this;
    }
    MobileSlider.prototype.setValue = function (val) {
        var initialSet = false;
        if (this.inputEl.value === '' || !this.inputEl.value || this.inputEl.value === undefined) {
            initialSet = true;
        }
        this.inputEl.value = Math.round(val * 100) + '';
        this.val = val;
        if (initialSet || parseFloat(this.handleEl.style.left.replace('%', "")) !== val) {
            var mockEvent = {
                target: this.inputEl,
                val: this.val
            };
            this.handleInputChanged(mockEvent, true);
        }
    };
    MobileSlider.prototype.setKey = function (key) {
        this.key = key.toString();
        this.titleEl.textContent = key;
    };
    MobileSlider.prototype.handleInputChanged = function (e, mockEvent) {
        var val = e.target.value;
        var sliderWidth = $(e.target).next().children().first().width();
        var positionAbsolute = (sliderWidth / 100) * val;
        var fullElementWidth = sliderWidth;
        var positionRelativeToFullWidth;
        if (positionAbsolute <= 5) {
            positionRelativeToFullWidth = '0px';
        }
        else {
            positionRelativeToFullWidth = 100 / (fullElementWidth / positionAbsolute) + '%';
        }
        this.handleEl.style.left = positionRelativeToFullWidth;
        this.fedBar.style.width = positionRelativeToFullWidth;
        var fValue = parseFloat(val) / 100;
        if (!mockEvent) {
            this.setValue(fValue);
            if (this.callback) {
                this.callback(fValue);
            }
            else {
                var xml = XMLParser.serializeWithValue(this.path, fValue.toString());
                this.fireChange(xml);
            }
        }
    };
    MobileSlider.prototype.handleClick = function (e) {
        this.sliderWidth = $(e.target).width();
        this.sliderOffset = $(e.target).offset().left;
        var positionAbsolute = Math.min(Math.max(0, (e.pageX - this.sliderOffset)), this.sliderWidth);
        var valueRelativeToHundred = 100 / (this.sliderWidth / positionAbsolute);
        var fullElementWidth = this.sliderWidth;
        var positionRelativeToFullWidth;
        if (positionAbsolute <= 5) {
            positionRelativeToFullWidth = '0px';
        }
        else {
            positionRelativeToFullWidth = 100 / (fullElementWidth / positionAbsolute) + '%';
        }
        this.handleEl.style.left = positionRelativeToFullWidth;
        this.fedBar.style.width = positionRelativeToFullWidth;
        this.setValue(valueRelativeToHundred / 100);
        if (this.callback) {
            this.callback(this.val);
        }
        else {
            var xml = XMLParser.serializeWithValue(this.path, this.val);
            this.fireChange(xml);
        }
    };
    MobileSlider.prototype.handleMouseMove = function (e) {
        var positionAbsolute;
        if (e.touch) {
            positionAbsolute = Math.min(Math.max(0, (e.touch.x.location - this.sliderOffset)), this.sliderWidth);
        }
        else {
            positionAbsolute = Math.min(Math.max(0, (e.pageX - this.sliderOffset - 5)), this.sliderWidth);
        }
        var valueRelativeToHundred = 100 / (this.sliderWidth / positionAbsolute);
        var fullElementWidth = this.sliderWidth;
        var positionRelativeToFullWidth;
        if (positionAbsolute <= 5) {
            positionRelativeToFullWidth = '0px';
        }
        else {
            positionRelativeToFullWidth = 100 / (fullElementWidth / positionAbsolute) + '%';
        }
        this.handleEl.style.left = positionRelativeToFullWidth;
        this.fedBar.style.width = positionRelativeToFullWidth;
        this.setValue(valueRelativeToHundred / 100);
    };
    MobileSlider.prototype.handleMouseUp = function (e) {
        var positionAbsolute;
        if (e.touch) {
            positionAbsolute = Math.min(Math.max(0, (e.touch.x.location - this.sliderOffset - 5)), this.sliderWidth);
        }
        else {
            positionAbsolute = Math.min(Math.max(0, (e.pageX - this.sliderOffset - 5)), this.sliderWidth);
        }
        var valueRelativeToHundred = 100 / (this.sliderWidth / positionAbsolute);
        this.setValue(valueRelativeToHundred / 100);
        if (this.callback) {
            this.callback(this.val);
        }
        else {
            var xml = XMLParser.serializeWithValue(this.path, this.val);
            this.fireChange(xml);
        }
        document.removeEventListener('mouseup', this.handleMouseUp);
        document.removeEventListener('mousemove', this.handleMouseMove);
    };
    MobileSlider.prototype.handleMouseDown = function (e) {
        e.preventDefault();
        if (e.touch) {
            this.sliderOffset = $(e.event.target).parent().offset().left;
            this.sliderWidth = $(e.event.target).parent().width();
        }
        else {
            e.stopPropagation();
            this.sliderOffset = $(e.target).parent().offset().left;
            this.sliderWidth = $(e.target).parent().width();
        }
        document.addEventListener('mouseup', this.handleMouseUp);
        document.addEventListener('mousemove', this.handleMouseMove);
    };
    MobileSlider.prototype.fireChange = function (xml) {
        if (this.source !== 'setting') {
            xml = createSettingsPath(xml, this.source);
        }
        else {
            if ($('#mobile-settings-pane-default-toggle').val() === 'true'
                && (this.paneEl.parentElement.style.left === '0px' || this.paneEl.parentElement.style.right === '0px')) {
                xml = '<Call Method="SetSettings">' + xml + '</Call>';
            }
            else {
                xml = '<Call Method="SetSceneSettings">' + xml + '</Call>';
            }
        }
        this.client.command.executeXML(xml);
        if (this.path.indexOf('ClashDetection') !== -1) {
            this.client.emit('ClashVisualizationChanged');
        }
    };
    return MobileSlider;
}(MobilePaneItem));
exports.MobileSlider = MobileSlider;
var MobileSteppedSlider = (function (_super) {
    __extends(MobileSteppedSlider, _super);
    function MobileSteppedSlider(paneEl, title, parent, level, readOnly, client, callback) {
        var _this = _super.call(this, paneEl, readOnly, client) || this;
        _this.step = 0;
        _this.maxSteps = 1;
        _this.parent = parent;
        _this.level = level;
        _this.callback = callback;
        _this.wrapperEl.className = "mobile-pane-slider-wrapper";
        _this.wrapperEl.style.marginLeft = (level * 10) + 'px';
        _this.handlePosStyle = {};
        _this.fedBarWidthStyle = {};
        _this.sliderOffset = 0;
        _this.sliderWidth = 0;
        _this.handleMouseDown = _this.handleMouseDown.bind(_this);
        _this.handleMouseMove = _this.handleMouseMove.bind(_this);
        _this.handleMouseUp = _this.handleMouseUp.bind(_this);
        if (!parent) {
            paneEl.appendChild(_this.wrapperEl);
        }
        else {
            parent.panelContainer.appendChild(_this.wrapperEl);
        }
        _this.titleEl.className = 'mobile-pane-slider-title';
        _this.setKey(title);
        _this.wrapperEl.appendChild(_this.titleEl);
        _this.inputEl = document.createElement('input');
        _this.inputEl.className = 'slider-value-input';
        _this.inputEl.min = '0';
        _this.inputEl.max = '100';
        _this.inputEl.style.width = '20px';
        $(_this.inputEl).blur(function (ev) {
            _this.callback(parseInt(_this.inputEl.value));
            _this.setValue(parseFloat(_this.inputEl.value) / 100);
        });
        $(_this.inputEl).change(function (ev) {
            _this.handleInputChanged(ev, false);
        });
        _this.wrapperEl.appendChild(_this.inputEl);
        _this.maxStepEl = document.createElement('span');
        _this.maxStepEl.textContent = '/1';
        _this.maxStepEl.className = 'plain-stepped-slider-max-val';
        _this.wrapperEl.appendChild(_this.maxStepEl);
        var sliderWrapper = document.createElement('div');
        sliderWrapper.className = 'slider-wrapper';
        sliderWrapper.style.width = 'calc(100% - 50px)';
        _this.wrapperEl.appendChild(sliderWrapper);
        var sliderBar = document.createElement('div');
        sliderBar.style.position = 'relative';
        sliderBar.className = 'slider-bar';
        sliderBar.onmousedown = function (ev) {
            _this.handleClick(ev);
        };
        sliderWrapper.appendChild(sliderBar);
        _this.fedBar = document.createElement('span');
        _this.fedBar.className = 'slider-bar-filled';
        sliderBar.appendChild(_this.fedBar);
        _this.handleEl = document.createElement('span');
        _this.handleEl.style.marginLeft = '0';
        $(_this.handleEl).addClass('slider-handle draggable');
        $(_this.handleEl).kendoTouch({
            dragstart: function (ev) {
                _this.handleMouseDown(ev);
            },
            dragend: function (ev) {
                _this.handleMouseUp(ev);
            },
            drag: function (ev) {
                _this.handleMouseMove(ev);
            }
        });
        _this.handleEl.onmousedown = function (ev) {
            _this.handleMouseDown(ev);
        };
        sliderBar.appendChild(_this.handleEl);
        var emptyBar = document.createElement('span');
        emptyBar.className = 'slider-bar-empty';
        emptyBar.style.width = '100%';
        sliderBar.appendChild(emptyBar);
        if (!_this.parent || !_this.parent.isCollapsed) {
            _this.show();
        }
        else {
            _this.hide();
        }
        if (parent) {
            parent.addChild(_this);
        }
        _this.setValue(0);
        return _this;
    }
    MobileSteppedSlider.prototype.setMaxSteps = function (maxSteps) {
        this.maxSteps = parseInt(maxSteps);
        this.maxStepEl.textContent = '/' + maxSteps;
    };
    MobileSteppedSlider.prototype.setValue = function (val) {
        if (val < 0) {
            val = 0;
        }
        else if (val > this.maxSteps) {
            val = this.maxSteps;
        }
        else if (val === NaN) {
            val = 0;
        }
        this.step = val;
        this.inputEl.value = val.toString();
        var relativeStepDistance = 100 / this.maxSteps;
        var positionRelativeToFullWidth = (val * relativeStepDistance) + '%';
        this.handleEl.style.left = positionRelativeToFullWidth;
        this.fedBar.style.width = positionRelativeToFullWidth;
    };
    MobileSteppedSlider.prototype.setKey = function (key) {
        this.key = key.toString();
        this.titleEl.textContent = key;
    };
    MobileSteppedSlider.prototype.handleInputChanged = function (e, mockEvent) {
        var val = parseInt(e.target.value);
        this.setValue(val);
        this.callback(val);
    };
    MobileSteppedSlider.prototype.handleClick = function (e) {
        this.sliderWidth = $(e.target).width();
        this.sliderOffset = $(e.target).offset().left;
        var positionAbsolute = Math.min(Math.max(0, (e.pageX - this.sliderOffset)), this.sliderWidth);
        var valueRelativeToHundred = 100 / (this.sliderWidth / positionAbsolute);
        var relativeStepDistance = 100 / this.maxSteps;
        var step = Math.round(valueRelativeToHundred / relativeStepDistance);
        this.setValue(step);
        this.callback(step);
    };
    MobileSteppedSlider.prototype.handleMouseMove = function (e) {
        var positionAbsolute;
        if (e.touch) {
            positionAbsolute = Math.min(Math.max(0, (e.touch.x.location - this.sliderOffset)), this.sliderWidth);
        }
        else {
            positionAbsolute = Math.min(Math.max(0, (e.pageX - this.sliderOffset - 5)), this.sliderWidth);
        }
        var valueRelativeToHundred = 100 / (this.sliderWidth / positionAbsolute);
        var relativeStepDistance = 100 / this.maxSteps;
        var step = Math.round(valueRelativeToHundred / relativeStepDistance);
        if (step !== this.step) {
            this.setValue(step);
            this.callback(step);
        }
    };
    MobileSteppedSlider.prototype.handleMouseUp = function (e) {
        document.removeEventListener('mouseup', this.handleMouseUp);
        document.removeEventListener('mousemove', this.handleMouseMove);
    };
    MobileSteppedSlider.prototype.handleMouseDown = function (e) {
        e.preventDefault();
        if (e.touch) {
            this.sliderOffset = $(e.event.target).parent().offset().left;
            this.sliderWidth = $(e.event.target).parent().width();
        }
        else {
            e.stopPropagation();
            this.sliderOffset = $(e.target).parent().offset().left;
            this.sliderWidth = $(e.target).parent().width();
        }
        document.addEventListener('mouseup', this.handleMouseUp);
        document.addEventListener('mousemove', this.handleMouseMove);
    };
    return MobileSteppedSlider;
}(MobilePaneItem));
exports.MobileSteppedSlider = MobileSteppedSlider;
var MobileColorPicker = (function (_super) {
    __extends(MobileColorPicker, _super);
    function MobileColorPicker(paneEl, source, path, title, showAlpha, parent, level, readOnly, client, callback) {
        var _this = _super.call(this, paneEl, readOnly, client) || this;
        _this.path = path;
        _this.parent = parent;
        _this.level = level;
        _this.source = source;
        _this.callback = callback;
        _this.wrapperEl.className = "mobile-pane-color-wrapper";
        _this.wrapperEl.style.marginLeft = (level * 10) + 'px';
        _this.showAlpha = showAlpha;
        _this.isDefaultSet = false;
        if (!path) {
            _this.visible = true;
        }
        if (!parent) {
            paneEl.appendChild(_this.wrapperEl);
        }
        else {
            parent.panelContainer.appendChild(_this.wrapperEl);
        }
        _this.titleEl.className = 'mobile-pane-color-title';
        _this.setKey(title);
        _this.wrapperEl.appendChild(_this.titleEl);
        _this.colorEl = document.createElement('span');
        _this.colorEl.className = 'mobile-pane-color-box';
        _this.titleEl.appendChild(_this.colorEl);
        _this.iconEl = document.createElement('i');
        $(_this.iconEl).addClass('fa fa-paint-brush mobile-pane-color-icon');
        _this.colorEl.appendChild(_this.iconEl);
        _this.wrapperEl.addEventListener('click', function (ev) {
            _this.openPalette();
        }, false);
        if (!_this.parent || !_this.parent.isCollapsed) {
            _this.show();
        }
        else {
            _this.hide();
        }
        if (parent) {
            parent.addChild(_this);
        }
        return _this;
    }
    MobileColorPicker.prototype.openPalette = function () {
        var _this = this;
        var activeColor = this.colorEl.dataset['lastPickedColor'];
        this.colorPicker = new custom_palette_1.CustomPalette(activeColor, this.showAlpha);
        this.colorPicker.on('customColorChosen', function (color) { return _this.updateElementState(color); });
    };
    MobileColorPicker.prototype.updateElementState = function (color) {
        this.colorEl.dataset['lastPickedColor'] = color;
        this.colorEl.style.backgroundColor = color;
        var hashHex = tinycolor(color).toHexString();
        if (color.indexOf('rgba') !== -1) {
            var alpha = Math.round(tinycolor(color)._a * 255).toString(16);
            hashHex = hashHex.replace('#', '#' + alpha);
        }
        if (this.callback) {
            this.callback(hashHex);
        }
        else {
            var xml = XMLParser.serializeWithValue(this.path, hashHex);
            if (this.source !== 'setting') {
                xml = createSettingsPath(xml, this.source);
            }
            else {
                if ($('#mobile-settings-pane-default-toggle').val() === 'true'
                    && (this.paneEl.parentElement.style.left === '0px' || this.paneEl.parentElement.style.right === '0px')) {
                    xml = '<Call Method="SetSettings">' + xml + '</Call>';
                }
                else {
                    xml = '<Call Method="SetSceneSettings">' + xml + '</Call>';
                }
            }
            this.client.command.executeXML(xml);
            if (this.path.indexOf('ClashDetection') !== -1) {
                this.client.emit('ClashVisualizationChanged');
            }
        }
    };
    MobileColorPicker.prototype.setValue = function (hexColor) {
        this.val = hexColor;
        var RGBAColor = hexToRgba(hexColor);
        var alpha = RGBAColor.a;
        var cssColor;
        if (this.showAlpha) {
            cssColor = "rgba(" + RGBAColor.r + ", " + RGBAColor.g + ", " + RGBAColor.b + ", " + alpha + ")";
        }
        else {
            cssColor = "rgb(" + RGBAColor.r + ", " + RGBAColor.g + ", " + RGBAColor.b + ")";
        }
        if (!this.isDefaultSet) {
            this.colorEl.dataset['defaultColor'] = cssColor;
            this.isDefaultSet = true;
        }
        this.colorEl.dataset['lastPickedColor'] = cssColor;
        this.colorEl.style.backgroundColor = cssColor;
        this.iconEl.style.color = getColorForBackground(hexColor);
    };
    MobileColorPicker.prototype.setKey = function (key) {
        this.key = key.toString();
        this.titleEl.textContent = key;
    };
    return MobileColorPicker;
}(MobilePaneItem));
exports.MobileColorPicker = MobileColorPicker;
var MobileKeyValue = (function (_super) {
    __extends(MobileKeyValue, _super);
    function MobileKeyValue(paneEl, title, source, keyValuePair, parent, level, readOnly, client) {
        var _this = _super.call(this, paneEl, readOnly, client) || this;
        _this.isDefaultSet = false;
        _this.parent = parent;
        _this.level = level;
        _this.wrapperEl.className = "mobile-pane-key-value-wrapper";
        _this.wrapperEl.style.marginLeft = (level * 10) + 'px';
        _this.source = source;
        _this.val = 'keyVal';
        _this.visible = true;
        _this.keyValuePair = keyValuePair;
        if (!parent) {
            paneEl.appendChild(_this.wrapperEl);
        }
        else {
            parent.panelContainer.appendChild(_this.wrapperEl);
        }
        _this.titleEl.className = 'mobile-pane-key-value-title';
        _this.setKey(title);
        _this.wrapperEl.appendChild(_this.titleEl);
        var _loop_1 = function (i) {
            var key = this_1.keyValuePair[i];
            var innerWrapper = void 0;
            var callBackFunc = void 0;
            if (i === 0) {
                this_1.keyEl = document.createElement('div');
                this_1.keyEl.className = 'mobile-pane-key';
                this_1.wrapperEl.appendChild(this_1.keyEl);
                innerWrapper = this_1.keyEl;
            }
            else {
                this_1.valEl = document.createElement('div');
                this_1.valEl.className = 'mobile-pane-key';
                this_1.wrapperEl.appendChild(this_1.valEl);
                innerWrapper = this_1.valEl;
                this_1.setValueVal = callBackFunc;
            }
            switch (key.type) {
                case 'Float':
                    if (key.addition) {
                        var prefix = document.createElement('span');
                        prefix.className = 'mobile-pane-key-prefix';
                        prefix.textContent = key.addition;
                        innerWrapper.appendChild(prefix);
                    }
                    var keyInput_1 = document.createElement('input');
                    keyInput_1.className = 'mobile-pane-input-val';
                    innerWrapper.appendChild(keyInput_1);
                    if (key.setting === undefined && (key.name === 'Angle' || key.name === 'Value')) {
                        key.setting = this_1.keyValuePair[1].setting;
                        if (key.setting.includes('Color1')) {
                            key.setting = key.setting.replace('Color1', "Angle1");
                        }
                        if (key.setting.includes('Color5')) {
                            key.setting = key.setting.replace('Color5', "Angle2");
                        }
                    }
                    var changeFunc_1 = function (val) {
                        var value = parseFloat(keyInput_1.value);
                        if ((key.name.split('.').indexOf('Angle1') !== -1 && value < 0) || (key.name.split('.').indexOf('Angle2') !== -1 && value > 0)
                            || key.name.split('.').indexOf('Tolerance') !== -1 && value < 0) {
                            value *= -1;
                        }
                        var xml = XMLParser.serializeWithValue(key.setting.split('.'), value.toString());
                        if ($('#mobile-settings-pane-default-toggle').val() === 'true'
                            && (_this.paneEl.parentElement.style.left === '0px' || _this.paneEl.parentElement.style.right === '0px')) {
                            xml = '<Call Method="SetSettings">' + xml + '</Call>';
                        }
                        else {
                            xml = '<Call Method="SetSceneSettings">' + xml + '</Call>';
                        }
                        client.command.executeXML(xml);
                    };
                    keyInput_1.addEventListener('change', function (ev) {
                        changeFunc_1(keyInput_1.value);
                    });
                    callBackFunc = function (val) {
                        keyInput_1.value = val;
                    };
                    break;
                case 'Color':
                    var keyColor_1 = document.createElement('span');
                    keyColor_1.className = 'mobile-pane-color-box';
                    var iconEl_1 = document.createElement('i');
                    $(iconEl_1).addClass('fa fa-paint-brush mobile-pane-color-icon');
                    keyColor_1.appendChild(iconEl_1);
                    callBackFunc = function (color) {
                        _this.val = color;
                        var RGBAColor = hexToRgba(color);
                        var alpha = RGBAColor.a;
                        var cssColor;
                        if (key.addition === 'RGBA') {
                            cssColor = "rgba(" + RGBAColor.r + ", " + RGBAColor.g + ", " + RGBAColor.b + ", " + alpha + ")";
                        }
                        else {
                            cssColor = "rgb(" + RGBAColor.r + ", " + RGBAColor.g + ", " + RGBAColor.b + ")";
                        }
                        if (!_this.isDefaultSet) {
                            keyColor_1.dataset['defaultColor'] = cssColor;
                            _this.isDefaultSet = true;
                        }
                        keyColor_1.dataset['lastPickedColor'] = cssColor;
                        keyColor_1.style.backgroundColor = cssColor;
                        iconEl_1.style.color = getColorForBackground(color);
                    };
                    innerWrapper.addEventListener('click', function (ev) {
                        _this.openPalette(keyColor_1, key);
                    }, false);
                    innerWrapper.appendChild(keyColor_1);
                    break;
            }
            if (i === 0) {
                this_1.setKeyVal = callBackFunc;
            }
            else {
                this_1.setValueVal = callBackFunc;
            }
        };
        var this_1 = this;
        for (var i = 0; i < _this.keyValuePair.length; i++) {
            _loop_1(i);
        }
        if (!_this.parent || !_this.parent.isCollapsed) {
            _this.show();
        }
        else {
            _this.hide();
        }
        if (parent) {
            parent.addChild(_this);
        }
        return _this;
    }
    MobileKeyValue.prototype.openPalette = function (element, entry) {
        var _this = this;
        var activeColor = element.dataset['lastPickedColor'];
        var colorPicker = new custom_palette_1.CustomPalette(activeColor, entry.addition === 'RGBA');
        colorPicker.on('customColorChosen', function (color) { return _this.updateElementState(color, element, entry); });
    };
    MobileKeyValue.prototype.updateElementState = function (color, element, entry) {
        element.dataset['lastPickedColor'] = color;
        element.style.backgroundColor = color;
        var hashHex = tinycolor(color).toHexString();
        var xml = XMLParser.serializeWithValue(entry.setting.split('.'), hashHex);
        if ($('#mobile-settings-pane-default-toggle').val() === 'true'
            && (this.paneEl.parentElement.style.left === '0px' || this.paneEl.parentElement.style.right === '0px')) {
            xml = '<Call Method="SetSettings">' + xml + '</Call>';
        }
        else {
            xml = '<Call Method="SetSceneSettings">' + xml + '</Call>';
        }
        this.client.command.executeXML(xml);
    };
    MobileKeyValue.prototype.setValue = function (val) {
        this.setKeyVal(val[0]);
        this.setValueVal(val[1]);
    };
    MobileKeyValue.prototype.setKey = function (key) {
        this.key = key.toString();
        this.titleEl.textContent = key;
    };
    return MobileKeyValue;
}(MobilePaneItem));
exports.MobileKeyValue = MobileKeyValue;
var MobileFolder = (function () {
    function MobileFolder(parentEl, client, folderItem, breadcrumbBar) {
        var _this = this;
        this.parentEl = parentEl;
        this.client = client;
        this.path = folderItem.path;
        this.breadcrumbBar = breadcrumbBar;
        var wrapperEl = document.createElement('div');
        wrapperEl.className = 'mobile-folder';
        parentEl.appendChild(wrapperEl);
        var iconEl = document.createElement('i');
        $(iconEl).addClass('fa fa-folder mobile-folder-icon');
        wrapperEl.appendChild(iconEl);
        var titleEl = document.createElement('span');
        titleEl.className = 'mobile-folder-title';
        titleEl.textContent = folderItem.key.replace('\\', '');
        ;
        wrapperEl.appendChild(titleEl);
        var dateEl = document.createElement('span');
        dateEl.className = 'mobile-folder-date';
        var date = folderItem.date.replace(/-/g, '/');
        dateEl.textContent = new Date(date).toLocaleString();
        wrapperEl.appendChild(dateEl);
        $(wrapperEl).click(function (ev) {
            _this.client.emit('@FolderClicked', folderItem.path);
            _this.breadcrumbBar.addBreadcrumb(folderItem);
        });
        wrapperEl.style.width = titleEl.clientWidth + iconEl.clientWidth + 'px';
    }
    return MobileFolder;
}());
exports.MobileFolder = MobileFolder;
var MobileFile = (function () {
    function MobileFile(parentEl, client, fileItem, mode) {
        var _this = this;
        this.parentEl = parentEl;
        this.client = client;
        this.path = fileItem.path;
        var wrapperEl = document.createElement('div');
        wrapperEl.className = 'mobile-file';
        parentEl.appendChild(wrapperEl);
        var iconEl = document.createElement('i');
        $(iconEl).addClass('far fa-file mobile-file-icon');
        wrapperEl.appendChild(iconEl);
        var titleEl = document.createElement('span');
        titleEl.className = 'mobile-file-title';
        titleEl.textContent = fileItem.key;
        wrapperEl.appendChild(titleEl);
        var sizeEl = document.createElement('span');
        sizeEl.className = 'mobile-file-size';
        sizeEl.textContent = this.calculateSize(fileItem.size);
        wrapperEl.appendChild(sizeEl);
        var dateEl = document.createElement('span');
        dateEl.className = 'mobile-file-date';
        var date = fileItem.date.replace(/-/g, '/');
        dateEl.textContent = new Date(date).toLocaleString();
        dateEl.style.paddingLeft = $(wrapperEl).width() / 10 + 'px';
        sizeEl.appendChild(dateEl);
        wrapperEl.onclick = function (ev) {
            ev.preventDefault();
            ev.stopPropagation();
            $(wrapperEl).toggleClass('mobile-file-selected');
            _this.client.emit('@FileClicked', fileItem.path);
        };
        wrapperEl.style.width = titleEl.clientWidth + iconEl.clientWidth + 'px';
    }
    MobileFile.prototype.calculateSize = function (sizeInB) {
        var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
        var i = Math.floor(Math.log(sizeInB) / Math.log(1024));
        if (i === 0) {
            return sizeInB + ' ' + sizes[i];
        }
        return (sizeInB / Math.pow(1024, i)).toFixed(1) + ' ' + sizes[i];
    };
    return MobileFile;
}());
exports.MobileFile = MobileFile;
var MobileBreadcrumbBar = (function () {
    function MobileBreadcrumbBar(parentEl, client, fileBrowser) {
        var _this = this;
        this.crumbElements = [];
        this.idMap = [];
        this.activePath = '';
        this.parentEl = parentEl;
        this.client = client;
        this.fileBrowser = fileBrowser;
        this.barWrapper = document.createElement('ul');
        this.barWrapper.className = 'mobile-file-browser-breadcrumb-bar';
        this.barWrapper.style.display = 'none';
        var rootItem = {
            key: 'Root',
            path: '',
            date: ''
        };
        var rootCrumb = new MobileBreadcrumb(this.barWrapper, this, rootItem);
        this.parentEl.appendChild(this.barWrapper);
        this.lastCrumbElWrapper = document.createElement('ul');
        this.lastCrumbElWrapper.className = 'mobile-file-browser-breadcrumb-bar';
        this.parentEl.appendChild(this.lastCrumbElWrapper);
        this.showMoreEl = document.createElement('li');
        var showMoreText = document.createElement('a');
        showMoreText.textContent = '.../...';
        this.showMoreEl.appendChild(showMoreText);
        this.showMoreEl.onclick = function (e) {
            _this.showFullBar();
            _this.fileBrowser.resize();
        };
        this.lastCrumbElWrapper.appendChild(this.showMoreEl);
        this.showMoreEl.style.display = 'none';
    }
    MobileBreadcrumbBar.prototype.addBreadcrumb = function (item) {
        if (this.idMap.indexOf(item.path) !== -1) {
            return;
        }
        this.fileBrowser.resize();
        var crumb = new MobileBreadcrumb(this.barWrapper, this, item);
        this.crumbElements.push(crumb);
        this.idMap.push(item.path);
        if (this.lastBreadcrumb) {
            this.lastBreadcrumb.remove();
        }
        if (this.idMap.length === 2) {
            $(this.showMoreEl).show();
        }
        else if (this.idMap.length < 2) {
            $(this.showMoreEl).hide();
        }
        this.lastBreadcrumb = new MobileBreadcrumb(this.lastCrumbElWrapper, this, item);
    };
    MobileBreadcrumbBar.prototype.showFullBar = function () {
        $(this.lastCrumbElWrapper).hide();
        $(this.barWrapper).show();
    };
    MobileBreadcrumbBar.prototype.hideFullbar = function () {
        $(this.lastCrumbElWrapper).show();
        $(this.barWrapper).hide();
    };
    MobileBreadcrumbBar.prototype.popCrumb = function () {
        if (this.crumbElements.length > 1) {
            this.removeBreadcrumbByPath(this.crumbElements[this.idMap.length - 2].item.path);
        }
        else {
            this.removeBreadcrumbByPath("");
        }
    };
    MobileBreadcrumbBar.prototype.removeBreadcrumbByPath = function (path) {
        if (path === "") {
            var length_1 = this.idMap.length;
            for (var i = 0; i < length_1; i++) {
                this.crumbElements[i].remove();
            }
            this.idMap = [];
            this.crumbElements = [];
            this.lastBreadcrumb.remove();
            $(this.showMoreEl).hide();
            this.activePath = path;
            this.changePath();
            this.fileBrowser.resize();
            return;
        }
        var startIndex;
        var breadcrumbLength = this.idMap.length;
        for (var i = 0; i < breadcrumbLength; i++) {
            if (this.idMap[i] === path) {
                startIndex = i + 1;
                break;
            }
        }
        if (startIndex) {
            this.idMap.splice(startIndex, this.idMap.length - 1);
            for (var j = startIndex; j < breadcrumbLength; j++) {
                var crumb = this.crumbElements[j];
                crumb.remove();
            }
            this.crumbElements.splice(startIndex, this.crumbElements.length - 1);
        }
        this.activePath = path;
        this.lastBreadcrumb.remove();
        if (this.idMap.length > 1) {
            $(this.showMoreEl).show();
        }
        this.lastBreadcrumb = new MobileBreadcrumb(this.lastCrumbElWrapper, this, this.crumbElements[this.idMap.length - 1].item);
        this.changePath();
    };
    MobileBreadcrumbBar.prototype.changePath = function () {
        this.client.emit('@FolderClicked', this.activePath);
    };
    return MobileBreadcrumbBar;
}());
exports.MobileBreadcrumbBar = MobileBreadcrumbBar;
var MobileBreadcrumb = (function () {
    function MobileBreadcrumb(parentEl, breadcrumbBar, breadcrumb) {
        var _this = this;
        this.parentEl = parentEl;
        this.bar = breadcrumbBar;
        this.item = breadcrumb;
        this.itemEl = document.createElement('li');
        var itemTextEl = document.createElement('a');
        itemTextEl.textContent = this.item.key.replace('\\', '');
        this.itemEl.appendChild(itemTextEl);
        this.itemEl.onclick = function (e) {
            breadcrumbBar.removeBreadcrumbByPath(_this.item.path);
        };
        this.parentEl.appendChild(this.itemEl);
    }
    MobileBreadcrumb.prototype.remove = function () {
        $(this.itemEl).remove();
    };
    return MobileBreadcrumb;
}());
exports.MobileBreadcrumb = MobileBreadcrumb;
var MobileTreeRow = (function () {
    function MobileTreeRow(parentEl, client, options, visIndex) {
        var _this = this;
        this.parentEl = parentEl;
        this.client = client;
        this.options = options;
        this.lineHeight = 20;
        this.selected = options.Selection;
        this.itemEl = document.createElement('div');
        this.itemEl.className = 'mobile-modeltree-row';
        this.itemEl.style.marginLeft = 37 + options.Level * 10 + 'px';
        var rowWrapper = document.createElement('div');
        rowWrapper.className = 'mobile-modeltree-row-wrapper';
        var visibility = document.createElement('span');
        visibility.classList.add('modeltree-item-visibility');
        visibility.style.position = 'absolute';
        visibility.style.left = '10px';
        switch (options.Visibility) {
            case 0:
                visibility.classList.add('fa');
                visibility.classList.add('fa-eye-slash');
                visibility.style.color = 'black';
                break;
            case 1:
                visibility.classList.add('fa');
                visibility.classList.add('fa-eye');
                visibility.style.color = 'black';
                break;
            case 2:
                visibility.style.color = 'black';
                break;
            case 3:
                visibility.classList.add('fa');
                visibility.classList.add('fa-eye');
                visibility.style.color = 'lightsteelblue';
                break;
        }
        visibility.addEventListener('click', function (ev) {
            ev.stopPropagation();
            var s = '<Call Method="VirtualTreeViewToggleVisibility"><NodeId>' + options.NodeId + '</NodeId></Call>';
            _this.client.command.executeXML(s);
        });
        rowWrapper.appendChild(visibility);
        rowWrapper.appendChild(this.itemEl);
        switch (options.Selection) {
            case 0:
                rowWrapper.classList.add('treeitem-selection-none');
                break;
            case 1:
                rowWrapper.classList.add('treeitem-selection-highlight');
                break;
            case 2:
                rowWrapper.classList.add('treeitem-selection-parent-selected');
                break;
        }
        var expander = document.createElement('span');
        expander.className = 'mobile-modeltree-expander';
        switch (options.ExpanderState) {
            case 0:
                expander.style.marginLeft = '20px';
                break;
            case 1:
                expander.style.display = 'inline-block';
                var expanderWrapper = document.createElementNS(SVG_NS, 'svg');
                expanderWrapper.style.width = '20px';
                expanderWrapper.style.height = '10px';
                var downAngle = document.createElementNS(SVG_NS, "polygon");
                downAngle.setAttributeNS(null, 'points', "10,0 0,10 10,10");
                downAngle.setAttributeNS(null, 'style', "fill:black");
                expanderWrapper.appendChild(downAngle);
                expander.appendChild(expanderWrapper);
                break;
            case 2:
                expander.style.display = 'inline-block';
                var expanderWrapper2 = document.createElementNS(SVG_NS, 'svg');
                expanderWrapper2.style.height = '12px';
                expanderWrapper2.style.width = '20px';
                expanderWrapper2.style.zIndex = '50';
                var rightAngle = document.createElementNS(SVG_NS, "polygon");
                rightAngle.setAttributeNS(null, 'points', "4,0 4,12 12,6");
                rightAngle.setAttributeNS(null, 'style', "fill:black");
                expanderWrapper2.appendChild(rightAngle);
                expander.appendChild(expanderWrapper2);
                break;
        }
        expander.addEventListener('click', function (ev) {
            ev.stopPropagation();
            var s = '<Call Method="VirtualTreeViewToggleExpansion"><NodeId>' + options.NodeId + '</NodeId></Call>';
            _this.client.command.executeXML(s);
        }, false);
        this.itemEl.appendChild(expander);
        var nodeTypeIcon = document.createElementNS(SVG_NS, 'svg');
        nodeTypeIcon.setAttribute('class', 'viewpane-item-icon');
        nodeTypeIcon.style.width = this.lineHeight * 0.8 + 'px';
        nodeTypeIcon.style.height = this.lineHeight * 0.8 + 'px';
        nodeTypeIcon.style.marginRight = '2px';
        var useEl = document.createElementNS(SVG_NS, 'use');
        useEl.setAttributeNS(XLINK_NS, 'href', '#' + GetNodeItemImage(options.NodeType));
        if (GetNodeItemImage(options.NodeType) === 'ICON_GEOMETRY' || GetNodeItemImage(options.NodeType) === 'ICON_ROOT') {
            $(nodeTypeIcon).addClass('viewpane-item-icon-translate-down');
        }
        nodeTypeIcon.appendChild(useEl);
        this.itemEl.appendChild(nodeTypeIcon);
        var nodeName = document.createElement('span');
        nodeName.textContent = options.NodeName;
        nodeName.className = 'mobile-modeltree-row-name';
        this.itemEl.appendChild(nodeName);
        var lineWrapper = document.createElementNS(SVG_NS, 'svg');
        lineWrapper.style.width = '100%';
        lineWrapper.style.height = '100%';
        lineWrapper.style.position = 'absolute';
        lineWrapper.style.pointerEvents = 'none';
        var hLine = document.createElementNS(SVG_NS, "line");
        hLine.setAttributeNS(null, 'x1', (34 + options.Level * 10).toString());
        hLine.setAttributeNS(null, 'x2', (41 + options.Level * 10).toString());
        hLine.setAttributeNS(null, 'y1', '10');
        hLine.setAttributeNS(null, 'y2', '10');
        hLine.setAttributeNS(null, "style", "stroke:black;stroke-width:2");
        ;
        lineWrapper.appendChild(hLine);
        var lineWrapper2 = document.createElementNS(SVG_NS, 'svg');
        lineWrapper2.style.width = '100%';
        lineWrapper2.style.height = '100%';
        lineWrapper2.style.position = 'absolute';
        lineWrapper2.style.top = '0';
        lineWrapper2.style.zIndex = '1';
        lineWrapper2.style.pointerEvents = 'none';
        var vLine = document.createElementNS(SVG_NS, "line");
        vLine.setAttributeNS(null, 'x1', (options.Level * 10 + 35).toString());
        vLine.setAttributeNS(null, 'x2', (options.Level * 10 + 35).toString());
        if (options.Parent !== -1) {
            vLine.setAttributeNS(null, 'y1', ((options.Parent + 1) * 20).toString());
        }
        else {
            vLine.setAttributeNS(null, 'y1', '0');
        }
        vLine.setAttributeNS(null, 'y2', (10 + visIndex * 20).toString());
        vLine.setAttributeNS(null, "style", "stroke:black;stroke-width:2");
        lineWrapper2.appendChild(vLine);
        this.parentEl.appendChild(lineWrapper2);
        this.parentEl.appendChild(lineWrapper);
        this.parentEl.appendChild(rowWrapper);
    }
    return MobileTreeRow;
}());
exports.MobileTreeRow = MobileTreeRow;
function hexToRgba(hex) {
    var bigint, r, g, b, a;
    var aRgb;
    if (hex[0] == '#') {
        aRgb = hex.substr(1);
    }
    else {
        aRgb = hex.substr(2);
    }
    bigint = parseInt(aRgb, 16);
    r = (bigint >> 16) & 255;
    g = (bigint >> 8) & 255;
    b = bigint & 255;
    var rgb = r + "," + g + "," + b;
    a = ((bigint >> 24) & 255) / 255;
    return {
        a: a,
        r: r,
        g: g,
        b: b
    };
}
function createSettingsPath(call, eventType) {
    var callWithEventType = call.replace('<Event>', '<Event Type="' + eventType + '">');
    callWithEventType = callWithEventType.replace('<Call Method="SetSceneSettings">', "");
    callWithEventType = callWithEventType.replace('</Call>', "");
    return callWithEventType;
}
function getColorForBackground(color) {
    var rgb = color.substr(1);
    var int = parseInt(rgb, 16);
    var r = ((int >> 16) & 255) / 255;
    var g = ((int >> 8) & 255) / 255;
    var b = (int & 255) / 255;
    var rgbArr = [r, g, b];
    for (var j = 0; j < rgbArr.length; j++) {
        if (rgbArr[j] <= 0.03928) {
            rgbArr[j] = rgbArr[j] / 12.92;
        }
        else {
            rgbArr[j] = Math.pow((rgbArr[j] + 0.055) / 1.055, 2.4);
        }
    }
    var luminance = 0.2126 * rgbArr[0] + 0.7152 * rgbArr[1] + 0.0722 * rgbArr[2];
    return (luminance > 0.179) ? '#878787' : 'lightgray';
}
function GetNodeItemImage(nodeType) {
    var svgId;
    switch (nodeType) {
        case node_types_1.NodeType.MasterRootNode:
            svgId = 'ICON_SCENE';
            break;
        case node_types_1.NodeType.Root:
            svgId = 'ICON_ROOT';
            break;
        case node_types_1.NodeType.ProductOccurrence:
            svgId = 'ICON_PO';
            break;
        case node_types_1.NodeType.DoNotUse1:
            svgId = 'ICON_GEOMETRY';
            break;
        case node_types_1.NodeType.DoNotUse2:
            svgId = 'ICON_GEOMETRY';
            break;
        case node_types_1.NodeType.Ri_BrepModel:
            svgId = 'ICON_GEOMETRY';
            break;
        case node_types_1.NodeType.Ri_PolyBrepModel:
            svgId = 'ICON_GEOMETRY';
            break;
        case node_types_1.NodeType.Ri_Set:
            svgId = 'ICON_GEOMETRY';
            break;
        case node_types_1.NodeType.Ri_PolyWire:
            svgId = 'ICON_WIRE';
            break;
        case node_types_1.NodeType.Ri_TessMarkup:
            svgId = 'ICON_MARKUP';
            break;
        case node_types_1.NodeType.DoNotUse3:
            svgId = '';
            break;
        case node_types_1.NodeType.Part:
            svgId = 'ICON_PART';
            break;
        case node_types_1.NodeType.AxisRoot:
            svgId = 'ICON_WIRE';
            break;
        case node_types_1.NodeType.Axis:
            svgId = 'ICON_WIRE';
            break;
        case node_types_1.NodeType.PMIView:
            svgId = 'ICON_VIEW';
            break;
        case node_types_1.NodeType.Annotation:
            svgId = 'ICON_MARKUP';
            break;
        case node_types_1.NodeType.AnnotationItem:
            svgId = 'ICON_MARKUP';
            break;
        case node_types_1.NodeType.PmiMarkup_Unknown:
        case node_types_1.NodeType.PmiMarkup_Text:
        case node_types_1.NodeType.PmiMarkup_Dimension:
        case node_types_1.NodeType.PmiMarkup_Arrow:
        case node_types_1.NodeType.PmiMarkup_Balloon:
        case node_types_1.NodeType.PmiMarkup_CircleCenter:
        case node_types_1.NodeType.PmiMarkup_Coordinate:
        case node_types_1.NodeType.PmiMarkup_Datum:
        case node_types_1.NodeType.PmiMarkup_Fastener:
        case node_types_1.NodeType.PmiMarkup_Gdt:
        case node_types_1.NodeType.PmiMarkup_Locator:
        case node_types_1.NodeType.PmiMarkup_MeasurementPoint:
        case node_types_1.NodeType.PmiMarkup_Roughness:
        case node_types_1.NodeType.PmiMarkup_Welding:
        case node_types_1.NodeType.PmiMarkup_Table:
        case node_types_1.NodeType.PmiMarkup_Other:
            svgId = 'ICON_MARKUP';
            break;
        case node_types_1.NodeType.DataAnalysisResult:
            svgId = 'ICON_HEATMAP';
            break;
        case node_types_1.NodeType.AnnotationReference:
            svgId = 'ICON_MARKUP';
            break;
        case node_types_1.NodeType.AnnotationSet:
            svgId = 'ICON_MARKUP';
            break;
        case node_types_1.NodeType.Curve:
            svgId = 'ICON_WIRE';
            break;
        case node_types_1.NodeType.Plane:
            svgId = 'ICON_PLANE';
            break;
        case node_types_1.NodeType.Solid:
            svgId = 'ICON_GEOMETRY';
            break;
        case node_types_1.NodeType.Wire:
            svgId = 'ICON_WIRE';
            break;
        case node_types_1.NodeType.Dimensioning:
            svgId = 'ICON_PMI_DISTANCE';
            break;
        case node_types_1.NodeType.MarkupSet:
            svgId = 'ICON_FOLDER';
            break;
        case node_types_1.NodeType.Markup_Circle:
            svgId = 'ICON_MARKUP_CIRCLE';
            break;
        case node_types_1.NodeType.Markup_Rectangle:
            svgId = 'ICON_RECTANGLE';
            break;
        case node_types_1.NodeType.Markup_Freehand:
            svgId = 'ICON_LINE_FREEHAND';
            break;
        case node_types_1.NodeType.Markup_Text:
            svgId = 'ICON_NOTE';
            break;
        case node_types_1.NodeType.OffsetLine:
            svgId = 'ICON_OFFSETLINE';
            break;
        case node_types_1.NodeType.Drawing_Block:
            svgId = 'ICON_FOLDER';
            break;
        case node_types_1.NodeType.Drawing_BlockBackground:
            svgId = 'ICON_FOLDER';
            break;
        case node_types_1.NodeType.Drawing_BlockBasic:
            svgId = 'ICON_FOLDER';
            break;
        case node_types_1.NodeType.Drawing_BlockOperator:
            svgId = 'ICON_FOLDER';
            break;
        case node_types_1.NodeType.Drawing_Sheet:
            svgId = 'ICON_SHEET';
            break;
        case node_types_1.NodeType.Drawing_View:
            svgId = 'ICON_VIEW';
            break;
        case node_types_1.NodeType.Drawing_Model:
            svgId = 'ICON_SHEET';
            break;
        case node_types_1.NodeType.CutGeometry_Set:
            svgId = 'ICON_FOLDER';
            break;
        case node_types_1.NodeType.CutGeometry_Wire:
            svgId = 'ICON_WIRE';
            break;
        case node_types_1.NodeType.CutGeometry_Surface:
            svgId = 'ICON_PLANE';
            break;
        case node_types_1.NodeType.Document2D:
            svgId = 'ICON_SHEET';
            break;
        default:
            svgId = 'ICON_GEOMETRY';
    }
    return svgId;
}


/***/ }),
/* 16 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var options_1 = __webpack_require__(33);
var utils_1 = __webpack_require__(17);
var xmlcreate_1 = __webpack_require__(34);
/**
 * Parses a string into XML.
 *
 * @param {string} str                            The string to parse into XML.
 * @param {XmlAttribute|XmlElement} parentElement The XML element or attribute
 *                                                that will contain the string.
 * @param {IOptions} options                      Options for parsing the
 *                                                string into XML.
 *
 * @private
 */
function parseString(str, parentElement, options) {
    var requiresCdata = function (s) {
        return (options.cdataInvalidChars && (s.indexOf("<") !== -1
            || s.indexOf("&") !== -1))
            || options.cdataKeys.indexOf(parentElement.name) !== -1
            || options.cdataKeys.indexOf("*") !== -1;
    };
    if (parentElement instanceof xmlcreate_1.XmlElement && requiresCdata(str)) {
        var cdataStrs = str.split("]]>");
        for (var i = 0; i < cdataStrs.length; i++) {
            if (requiresCdata(cdataStrs[i])) {
                parentElement.cdata(cdataStrs[i]);
            }
            else {
                parentElement.text(cdataStrs[i]);
            }
            if (i < cdataStrs.length - 1) {
                parentElement.text("]]>");
            }
        }
    }
    else {
        parentElement.text(str);
    }
}
/**
 * Parses an attribute into XML.
 *
 * @param {string} name              The name of the attribute.
 * @param {string} value             The value of the attribute.
 * @param {XmlElement} parentElement The XML element that will contain the
 *                                   string.
 * @param {IOptions} options         Options for parsing the attribute into XML.
 *
 * @private
 */
function parseAttribute(name, value, parentElement, options) {
    var attribute = parentElement.attribute(name, "");
    if (utils_1.isType(value, "String") || utils_1.isType(value, "Number")
        || utils_1.isType(value, "Boolean") || utils_1.isType(value, "Undefined")
        || utils_1.isType(value, "Null")) {
        parseString(utils_1.stringify(value), attribute, options);
    }
    else {
        throw new Error("attribute value for name '" + name + "' should be a"
            + " primitive (string, number, boolean, null, or"
            + " undefined)");
    }
}
/**
 * Parses an object or Map entry into XML.
 *
 * @param {string} key               The key associated with the object or Map
 *                                   entry.
 * @param {*} value                  The object or map entry.
 * @param {XmlElement} parentElement The XML element that will contain the
 *                                   object or map entry.
 * @param {IOptions} options         Options for parsing the object or map
 *                                   entry into XML.
 *
 * @private
 */
function parseObjectOrMapEntry(key, value, parentElement, options) {
    // Alias key
    if (key === options.aliasString) {
        if (!utils_1.isType(value, "String")) {
            throw new Error("aliasString value for " + value
                + " should be a string");
        }
        parentElement.name = value;
        return;
    }
    // Attributes key
    if (key.indexOf(options.attributeString) === 0) {
        if (utils_1.isType(value, "Object")) {
            for (var _i = 0, _a = Object.keys(value); _i < _a.length; _i++) {
                var subkey = _a[_i];
                parseAttribute(subkey, value[subkey], parentElement, options);
            }
        }
        else {
            throw new Error("attributes object for " + key + " should be an"
                + " object");
        }
        return;
    }
    // Value key
    if (key.indexOf(options.valueString) === 0) {
        if (utils_1.isType(value, "String") || utils_1.isType(value, "Number")
            || utils_1.isType(value, "Boolean") || utils_1.isType(value, "Null")
            || utils_1.isType(value, "Undefined")) {
            parseValue(key, value, parentElement, options);
            return;
        }
        else {
            throw new Error("value " + value + " should be a primitive"
                + " (string, number, boolean, null, or undefined)");
        }
    }
    // Standard handling (create new element for entry)
    var element = parentElement;
    if (!utils_1.isType(value, "Array") && !utils_1.isType(value, "Set")) {
        element = parentElement.element(key);
    }
    parseValue(key, value, element, options);
}
/**
 * Parses an Object or Map into XML.
 *
 * @param {*} objectOrMap            The object or map to parse into XML.
 * @param {XmlElement} parentElement The XML element that will contain the
 *                                   object.
 * @param {IOptions} options         Options for parsing the object into XML.
 *
 * @private
 */
function parseObjectOrMap(objectOrMap, parentElement, options) {
    if (utils_1.isType(objectOrMap, "Map")) {
        objectOrMap.forEach(function (value, key) {
            parseObjectOrMapEntry(utils_1.stringify(key), value, parentElement, options);
        });
    }
    else {
        for (var _i = 0, _a = Object.keys(objectOrMap); _i < _a.length; _i++) {
            var key = _a[_i];
            parseObjectOrMapEntry(key, objectOrMap[key], parentElement, options);
        }
    }
}
/**
 * Parses an array or Set into XML.
 *
 * @param {string} key               The key associated with the array or set to
 *                                   parse into XML.
 * @param {*} arrayOrSet             The array or set to parse into XML.
 * @param {XmlElement} parentElement The XML element that will contain the
 *                                   function.
 * @param {IOptions} options         Options for parsing the array or set into
 *                                   XML.
 *
 * @private
 */
function parseArrayOrSet(key, arrayOrSet, parentElement, options) {
    var arrayNameFunc;
    if (options.wrapHandlers.hasOwnProperty("*")) {
        arrayNameFunc = options.wrapHandlers["*"];
    }
    if (options.wrapHandlers.hasOwnProperty(key)) {
        arrayNameFunc = options.wrapHandlers[key];
    }
    var arrayKey = key;
    var arrayElement = parentElement;
    if (!utils_1.isType(arrayNameFunc, "Undefined")) {
        var arrayNameFuncKey = arrayNameFunc(arrayKey, arrayOrSet);
        if (utils_1.isType(arrayNameFuncKey, "String")) {
            arrayKey = arrayNameFuncKey;
            arrayElement = parentElement.element(key);
        }
        else if (!utils_1.isType(arrayNameFuncKey, "Null")) {
            throw new Error("wrapHandlers function for " + arrayKey
                + " should return a string or null");
        }
    }
    arrayOrSet.forEach(function (item) {
        var element = arrayElement;
        if (!utils_1.isType(item, "Array") && !utils_1.isType(item, "Set")) {
            element = arrayElement.element(arrayKey);
        }
        parseValue(arrayKey, item, element, options);
    });
}
/**
 * Parses an arbitrary JavaScript value into XML.
 *
 * @param {string} key               The key associated with the value to parse
 *                                   into XML.
 * @param {*} value                  The value to parse into XML.
 * @param {XmlElement} parentElement The XML element that will contain the
 *                                   value.
 * @param {IOptions} options         Options for parsing the value into XML.
 *
 * @private
 */
function parseValue(key, value, parentElement, options) {
    // If a handler for a particular type is user-defined, use that handler
    // instead of the defaults
    var type = Object.prototype.toString.call(value);
    var handler;
    if (options.typeHandlers.hasOwnProperty("*")) {
        handler = options.typeHandlers["*"];
    }
    if (options.typeHandlers.hasOwnProperty(type)) {
        handler = options.typeHandlers[type];
    }
    if (!utils_1.isType(handler, "Undefined")) {
        value = handler(value);
    }
    if (utils_1.isType(value, "Object") || utils_1.isType(value, "Map")) {
        parseObjectOrMap(value, parentElement, options);
        return;
    }
    if (utils_1.isType(value, "Array") || utils_1.isType(value, "Set")) {
        parseArrayOrSet(key, value, parentElement, options);
        return;
    }
    parseString(utils_1.stringify(value), parentElement, options);
}
/**
 * Returns a XML document corresponding to the specified value.
 *
 * @param {string} root      The name of the root XML element. When the value is
 *                           converted to XML, it will be a child of this root
 *                           element.
 * @param {*} value          The value to convert to XML.
 * @param {IOptions} options Options for parsing the value into XML.
 *
 * @returns {XmlDocument} An XML document corresponding to the specified value.
 *
 * @private
 */
function parseToDocument(root, value, options) {
    var document = new xmlcreate_1.XmlDocument(root);
    if (options.declaration.include) {
        document.decl(options.declaration);
    }
    if (options.dtd.include) {
        document.dtd(options.dtd.name, options.dtd.sysId, options.dtd.pubId);
    }
    parseValue(root, value, document.root(), options);
    return document;
}
/**
 * Returns a XML string representation of the specified object.
 *
 * @param {string} root        The name of the root XML element. When the
 *                             object is converted to XML, it will be a
 *                             child of this root element.
 * @param {*} object           The object to convert to XML.
 * @param {IOptions} [options] Options for parsing the object and
 *                             formatting the resulting XML.
 *
 * @returns {string} An XML string representation of the specified object.
 */
function parse(root, object, options) {
    if (options === void 0) { options = {}; }
    options = options_1.validateOptions(options);
    var document = parseToDocument(root, object, options);
    return document.toString(options.format);
}
exports.parse = parse;


/***/ }),
/* 17 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Returns true if the specified value are of any of the specified types, as
 * determined by the Object.prototype.toString.call function.
 *
 * @param {*} val The specified value.
 * @param {...string[]} types The specified types.
 *
 * @returns {boolean} Whether or not the specified value are of any of the
 *                    specified types.
 *
 * @private
 */
function isType(val) {
    var types = [];
    for (var _i = 1; _i < arguments.length; _i++) {
        types[_i - 1] = arguments[_i];
    }
    for (var _a = 0, types_1 = types; _a < types_1.length; _a++) {
        var type = types_1[_a];
        if (Object.prototype.toString.call(val) === "[object " + type + "]") {
            return true;
        }
    }
    return false;
}
exports.isType = isType;
/**
 * Converts a value into a string.
 *
 * @param {*} value The value to convert to a string.
 *
 * @returns {String} The string representation of the specified value.
 *
 * @private
 */
function stringify(value) {
    if (!isType(value, "Undefined") && !isType(value, "Null")) {
        if (!isType(value.toString, "Function")) {
            value = value.toString();
        }
    }
    return String(value);
}
exports.stringify = stringify;


/***/ }),
/* 18 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Replaces ampersands (&) with the appropriate XML character reference.
 *
 * @param {string} str The string to escape.
 *
 * @returns {string} A copy of the specified string with ampersands escaped.
 *
 * @private
 */
function escapeAmpersands(str) {
    return str.replace(/&/g, "&amp;");
}
exports.escapeAmpersands = escapeAmpersands;
/**
 * Replaces left angle brackets (&lt;) with the appropriate XML character
 * reference.
 *
 * @param {string} str The string to escape.
 *
 * @returns {string} A copy of the specified string with left angle brackets
 *                   escaped.
 *
 * @private
 */
function escapeLeftAngleBrackets(str) {
    return str.replace(/</g, "&lt;");
}
exports.escapeLeftAngleBrackets = escapeLeftAngleBrackets;
/**
 * Replaces single quotes (") with the appropriate XML character reference.
 *
 * @param {string} str The string to escape.
 *
 * @returns {string} A copy of the specified string with single quotes escaped.
 *
 * @private
 */
function escapeSingleQuotes(str) {
    return str.replace(/'/g, "&apos;");
}
exports.escapeSingleQuotes = escapeSingleQuotes;
/**
 * Replaces double quotes (") with the appropriate XML character reference.
 *
 * @param {string} str The string to escape.
 *
 * @returns {string} A copy of the specified string with double quotes escaped.
 *
 * @private
 */
function escapeDoubleQuotes(str) {
    return str.replace(/"/g, "&quot;");
}
exports.escapeDoubleQuotes = escapeDoubleQuotes;


/***/ }),
/* 19 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var escape_1 = __webpack_require__(18);
var options_1 = __webpack_require__(7);
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlCharRef_1 = __webpack_require__(12);
var XmlEntityRef_1 = __webpack_require__(13);
var XmlNode_1 = __webpack_require__(2);
var XmlText_1 = __webpack_require__(14);
/**
 * Represents an XML element attribute.
 *
 * An XML element attribute is part of the start tag of an element and is
 * structured as follows, where `{name}` is the name of the attribute and
 * `{value}` is the value of the attribute:
 *
 * ```xml
 * <element {name}="{value}">
 * ```
 *
 * The `{name}` value is a property of this node, while the `{value}` property
 * consists of the children of this node.
 *
 * XmlAttribute nodes must have at least one child, and can have an unlimited
 * number of {@link XmlCharRef}, {@link XmlEntityRef}, and {@link XmlText}
 * nodes as children.
 */
var XmlAttribute = (function (_super) {
    __extends(XmlAttribute, _super);
    /**
     * Initializes a new instance of the {@link XmlAttribute} class.
     *
     * @param {string} name             The name of the XML attribute.
     * @param {XmlNode|XmlNode[]} value The initial value of the XML attribute.
     *                                  Additional children can be added later.
     *                                  Only {@link XmlCharRef},
     *                                  {@link XmlEntityRef}, and
     *                                  {@link XmlText} nodes are permitted.
     */
    function XmlAttribute(name, value) {
        _super.call(this);
        this.name = name;
        if (utils_1.isType(value, "Array")) {
            for (var _i = 0, _a = value; _i < _a.length; _i++) {
                var node = _a[_i];
                this.insertChild(node);
            }
        }
        else {
            this.insertChild(value);
        }
    }
    Object.defineProperty(XmlAttribute.prototype, "name", {
        /**
         * Gets the name of this attribute.
         *
         * @returns {string} The name of this attribute.
         */
        get: function () {
            return this._name;
        },
        /**
         * Sets the name of this attribute.
         *
         * @param {string} name The name of this attribute.
         */
        set: function (name) {
            if (!utils_1.isType(name, "String")) {
                throw new TypeError("name should be a string");
            }
            else if (!validate_1.validateName(name)) {
                throw new Error("name should not contain characters not" +
                    " allowed in XML names");
            }
            this._name = name;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Inserts a new XML character reference at the specified index.
     *
     * @param {string} char    The character to represent using the reference.
     * @param {boolean} [hex]  Whether to use the hexadecimal or decimal
     *                         representation for the reference. If left
     *                         undefined, decimal is the default.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlCharRef} The newly created XML declaration.
     */
    XmlAttribute.prototype.charRef = function (char, hex, index) {
        var charRef = new XmlCharRef_1.default(char, hex);
        this.insertChild(charRef, index);
        return charRef;
    };
    /**
     * Inserts a new XML entity reference at the specified index.
     *
     * @param {string} entity  The entity to be referenced.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlEntityRef} The newly created XML declaration.
     */
    XmlAttribute.prototype.entityRef = function (entity, index) {
        var charRef = new XmlEntityRef_1.default(entity);
        this.insertChild(charRef, index);
        return charRef;
    };
    /**
     * Inserts the specified node into this node's children at the specified
     * index. The node is not inserted if it is already present. If this node
     * already has a parent, it is removed from that parent.
     *
     * Note that only {@link XmlCharRef}, {@link XmlEntityRef}, and
     * {@link XmlText} nodes can be inserted; otherwise, an exception will be
     * thrown.
     *
     * @param {XmlNode} node   The node to insert.
     * @param {number} [index] The index at which to insert the node. Nodes at
     *                         or after the index are shifted to the right. If
     *                         no index is specified, the node is inserted at
     *                         the end.
     *
     * @returns {XmlNode} The node inserted into this node's children, or
     *                    undefined if no node was inserted.
     */
    XmlAttribute.prototype.insertChild = function (node, index) {
        if (!(node instanceof XmlCharRef_1.default || node instanceof XmlEntityRef_1.default ||
            node instanceof XmlText_1.default)) {
            throw new TypeError("node should be an instance of XmlCharRef," +
                " XmlEntityRef, or XmlText");
        }
        return _super.prototype.insertChild.call(this, node, index);
    };
    /**
     * Removes the specified node from this node's children.
     *
     * Note that this node must have at least one child. Attempts to remove
     * the last child node will result in an exception.
     *
     * @param {XmlNode} node The node to remove.
     *
     * @returns {boolean} Whether a node was removed.
     */
    XmlAttribute.prototype.removeChild = function (node) {
        if (this._children.length === 1) {
            throw new Error("XmlAttribute nodes must have at least one child");
        }
        return _super.prototype.removeChild.call(this, node);
    };
    /**
     * Removes the node at the specified index from this node's children.
     *
     * Note that this node must have at least one child. Attempts to remove
     * the last child node will result in an exception.
     *
     * @param {number} index The index at which the node to be removed is
     *                       located.
     *
     * @returns {XmlNode} The node that was removed, or undefined if no node
     *                    was removed.
     */
    XmlAttribute.prototype.removeChildAtIndex = function (index) {
        if (this._children.length === 1) {
            throw new Error("XmlAttribute nodes must have at least one child");
        }
        return _super.prototype.removeChildAtIndex.call(this, index);
    };
    /**
     * Inserts a new XML text node at the specified index.
     *
     * @param {string} text    Arbitrary character data.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlText} The newly created XML declaration.
     */
    XmlAttribute.prototype.text = function (text, index) {
        var textNode = new XmlText_1.default(text);
        this.insertChild(textNode, index);
        return textNode;
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlAttribute.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        options_1.validateStringOptions(options);
        var quote = options.doubleQuotes ? "\"" : "'";
        var str = this.name + "=" + quote;
        for (var _i = 0, _a = this._children; _i < _a.length; _i++) {
            var child = _a[_i];
            if (options.doubleQuotes) {
                str += escape_1.escapeDoubleQuotes(child.toString(options));
            }
            else {
                str += escape_1.escapeSingleQuotes(child.toString(options));
            }
        }
        str += quote;
        return str;
    };
    return XmlAttribute;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlAttribute;


/***/ }),
/* 20 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlNode_1 = __webpack_require__(2);
/**
 * Represents an XML CDATA section.
 *
 * An XML CDATA section is structured as follows, where `{data}` is the
 * character data of the section:
 *
 * ```xml
 * <![CDATA[{data}]]>
 * ```
 *
 * The `{data}` value is a property of this node.
 *
 * XmlCdata nodes cannot have any children.
 */
var XmlCdata = (function (_super) {
    __extends(XmlCdata, _super);
    /**
     * Initializes a new instance of the {@link XmlCdata} class.
     *
     * @param {string} data The character data of the CDATA section.
     */
    function XmlCdata(data) {
        _super.call(this);
        this.data = data;
    }
    Object.defineProperty(XmlCdata.prototype, "data", {
        /**
         * Gets the character data of the CDATA section.
         *
         * @returns {string} The character data of the CDATA section.
         */
        get: function () {
            return this._data;
        },
        /**
         * Sets the character data of the CDATA section.
         *
         * @param {string} data The character data of the CDATA section.
         */
        set: function (data) {
            if (!utils_1.isType(data, "String")) {
                throw new TypeError("character data should be a string");
            }
            else if (!validate_1.validateChar(data)) {
                throw new Error("character data should not contain characters not" +
                    " allowed in XML");
            }
            else if (/]]>/.test(data)) {
                throw new Error("data should not contain the string ']]>'");
            }
            this._data = data;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Throws an exception since {@link XmlCdata} nodes cannot have any
     * children.
     */
    XmlCdata.prototype.children = function () {
        throw new Error("XmlCdata nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlCdata} nodes cannot have any
     * children.
     */
    XmlCdata.prototype.insertChild = function (node, index) {
        throw new Error("XmlCdata nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlCdata} nodes cannot have any
     * children.
     */
    XmlCdata.prototype.removeChild = function (node) {
        throw new Error("XmlCdata nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlCdata} nodes cannot have any
     * children.
     */
    XmlCdata.prototype.removeChildAtIndex = function (index) {
        throw new Error("XmlCdata nodes cannot have children");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlCdata.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        return "<![CDATA[" + this.data + "]]>";
    };
    return XmlCdata;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlCdata;


/***/ }),
/* 21 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var options_1 = __webpack_require__(7);
var utils_1 = __webpack_require__(1);
var XmlNode_1 = __webpack_require__(2);
/**
 * Represents an XML declaration.
 *
 * An XML declaration is structured as follows, where `{version}` is the XML
 * version, `{encoding}` is the encoding of the document, and `{standalone}`
 * is either "yes" or "no", depending on whether the document may contain
 * external markup declarations:
 *
 * ```xml
 * <?xml version="{version}" encoding="{encoding}" standalone="{standalone}"?>
 * ```
 *
 * The `{version}`, `{encoding}`, and `{standalone}` values are properties of
 * this node.
 *
 * XmlDecl nodes cannot have any children.
 */
var XmlDecl = (function (_super) {
    __extends(XmlDecl, _super);
    /**
     * Initializes a new instance of the {@link XmlDecl} class.
     *
     * @param {IDeclarationOptions} [options] The options associated with the
     *     XML declaration.
     */
    function XmlDecl(options) {
        if (options === void 0) { options = {}; }
        _super.call(this);
        options_1.validateDeclarationOptions(options);
        this.encoding = options.encoding;
        this.standalone = options.standalone;
        this.version = options.version;
    }
    Object.defineProperty(XmlDecl.prototype, "encoding", {
        /**
         * Gets the XML encoding to be included in the declaration.
         *
         * @returns {string} The XML encoding to be included in the declaration.
         *                   This value may be undefined.
         */
        get: function () {
            return this._encoding;
        },
        /**
         * Sets the XML encoding to be included in the declaration.
         *
         * @param encoding {string} The XML encoding to be included in the
         *                          declaration. This value must be a valid
         *                          encoding. If left undefined, no encoding is
         *                          included.
         */
        set: function (encoding) {
            if (utils_1.isType(encoding, "String")) {
                if (!/^[A-Za-z][A-Za-z0-9._-]*$/.test(encoding)) {
                    throw new Error("encoding should be a valid XML encoding");
                }
            }
            else if (!utils_1.isType(encoding, "Undefined")) {
                throw new TypeError("name should be a string or undefined");
            }
            this._encoding = encoding;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(XmlDecl.prototype, "standalone", {
        /**
         * Gets the XML standalone attribute to be included in the declaration.
         *
         * @returns {string} The XML standalone attribute to be included in the
         *                   declaration. This value may be undefined.
         */
        get: function () {
            return this._standalone;
        },
        /**
         * Sets the XML standalone attribute to be included in the declaration.
         *
         * @param {string} standalone The XML standalone attribute to be included.
         *                            This value must be "yes" or "no". If left
         *                            undefined, no standalone attribute is
         *                            included.
         */
        set: function (standalone) {
            if (utils_1.isType(standalone, "String")) {
                if (!/^(yes|no)$/.test(standalone)) {
                    throw new Error("standalone should be either the string" +
                        " 'yes' or the string 'no'");
                }
            }
            else if (!utils_1.isType(standalone, "Undefined")) {
                throw new TypeError("standalone should be a string or undefined");
            }
            this._standalone = standalone;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(XmlDecl.prototype, "version", {
        /**
         * Gets the XML version to be included in the declaration.
         *
         * @returns {string} The XML version to tbe included in the declaration.
         */
        get: function () {
            return this._version;
        },
        /**
         * Sets the XML version to be included in the declaration.
         *
         * @param {string} version The XML version to be included in the
         *                         declaration. This value must be a valid XML
         *                         version number. If left undefined, the default
         *                         version is "1.0".
         */
        set: function (version) {
            if (!utils_1.isType(version, "String")) {
                throw new TypeError("version should be a string");
            }
            else if (!/^1\.[0-9]+$/.test(version)) {
                throw new Error("version should be a valid XML version");
            }
            this._version = version;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Throws an exception since {@link XmlDecl} nodes cannot have any
     * children.
     */
    XmlDecl.prototype.children = function () {
        throw new Error("XmlDecl nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDecl} nodes cannot have any
     * children.
     */
    XmlDecl.prototype.insertChild = function (node, index) {
        throw new Error("XmlDecl nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDecl} nodes cannot have any
     * children.
     */
    XmlDecl.prototype.removeChild = function (node) {
        throw new Error("XmlDecl nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDecl} nodes cannot have any
     * children.
     */
    XmlDecl.prototype.removeChildAtIndex = function (index) {
        throw new Error("XmlDecl nodes cannot have children");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlDecl.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        options_1.validateStringOptions(options);
        var quote = options.doubleQuotes ? '"' : "'";
        var str = "<?xml version=" + quote + this.version + quote;
        if (utils_1.isType(this.encoding, "String")) {
            str += " encoding=" + quote + this.encoding + quote;
        }
        if (utils_1.isType(this.standalone, "String")) {
            str += " standalone=" + quote + this.standalone + quote;
        }
        str += "?>";
        return str;
    };
    return XmlDecl;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlDecl;


/***/ }),
/* 22 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var options_1 = __webpack_require__(7);
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlComment_1 = __webpack_require__(8);
var XmlDtdAttlist_1 = __webpack_require__(23);
var XmlDtdElement_1 = __webpack_require__(24);
var XmlDtdEntity_1 = __webpack_require__(25);
var XmlDtdNotation_1 = __webpack_require__(26);
var XmlDtdParamEntityRef_1 = __webpack_require__(27);
var XmlNode_1 = __webpack_require__(2);
var XmlProcInst_1 = __webpack_require__(9);
/**
 * Represents an XML document type definition (DTD).
 *
 * An XML document type definition  is structured as follows, where `{name}` is
 * the name of the DTD, `{sysId}` is the system identifier of the DTD,
 * `{pubId}` is the public identifier of the DTD, and `{intSubset}` is the
 * internal subset of the DTD:
 *
 * ```xml
 * <!DOCTYPE {name} SYSTEM "{sysId}" PUBLIC "{pubId}" [
 *     {intSubset}
 * ]>
 * ```
 *
 * The `{name}`, `{pubId}`, and `{sysId}` values are properties of the node,
 * while the `{intSubset}` value consists of the children of this node.
 *
 * XmlDtd nodes can have an unlimited number of {@link XmlComment},
 * {@link XmlDtdAttlist}, {@link XmlDtdElement}, {@link XmlDtdEntity},
 * {@link XmlDtdNotation}, {@link XmlDtdParamEntityRef}, and
 * {@link XmlProcInst} nodes.
 */
var XmlDtd = (function (_super) {
    __extends(XmlDtd, _super);
    /**
     * Initializes a new instance of the {@link XmlDtd} class.
     *
     * @param {string} name    The name of the DTD.
     * @param {string} [sysId] The system identifier of the DTD, excluding
     *                         quotation marks.
     * @param {string} [pubId] The public identifier of the DTD, excluding
     *                         quotation marks. If a public identifier is
     *                         provided, a system identifier must be provided
     *                         as well.
     */
    function XmlDtd(name, sysId, pubId) {
        _super.call(this);
        this.name = name;
        this.sysId = sysId;
        this.pubId = pubId;
    }
    Object.defineProperty(XmlDtd.prototype, "name", {
        /**
         * Gets the name of the DTD.
         *
         * @returns {string} The name of the DTD.
         */
        get: function () {
            return this._name;
        },
        /**
         * Sets the name of the DTD.
         *
         * @param {string} name The name of the DTD.
         */
        set: function (name) {
            if (!utils_1.isType(name, "String")) {
                throw new TypeError("name should be a string");
            }
            else if (!validate_1.validateName(name)) {
                throw new Error("name should not contain characters not" +
                    " allowed in XML names");
            }
            this._name = name;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(XmlDtd.prototype, "pubId", {
        /**
         * Gets the public identifier of the DTD, excluding quotation marks.
         *
         * @returns {string} The public identifier of the DTD, excluding quotation
         *                   marks. This value may be undefined.
         */
        get: function () {
            return this._pubId;
        },
        /**
         * Sets the public identifier of the DTD, excluding quotation marks. If a
         * public identifier is provided, a system identifier must be provided as
         * well.
         *
         * @param {string} pubId The public identifier of the DTD, excluding
         *                       quotation marks. This value may be undefined.
         */
        set: function (pubId) {
            if (utils_1.isType(pubId, "String")) {
                if (!/^(\u0020|\u000D|\u000A|[a-zA-Z0-9]|[-'()+,./:=?;!*#@$_%])*$/
                    .test(pubId)) {
                    throw new Error("pubId should not contain characters not" +
                        " allowed in public identifiers");
                }
                else if (utils_1.isType(this.sysId, "Undefined")) {
                    throw new Error("pubId should not be defined if sysId is" +
                        " undefined");
                }
            }
            else if (!utils_1.isType(pubId, "Undefined")) {
                throw new TypeError("pubId should be a string or undefined");
            }
            this._pubId = pubId;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(XmlDtd.prototype, "sysId", {
        /**
         * Gets the system identifier of the DTD, excluding quotation marks.
         *
         * @returns {string} The system identifier of the DTD, excluding quotation
         *                   marks. This value may be undefined.
         */
        get: function () {
            return this._sysId;
        },
        /**
         * Sets the system identifier of the DTD, excluding quotation marks.
         *
         * @param {string} sysId The system identifier of the DTD, excluding
         *                       quotation marks. This value may be undefined.
         */
        set: function (sysId) {
            if (utils_1.isType(sysId, "String")) {
                if (!validate_1.validateChar(sysId)) {
                    throw new Error("sysId should not contain characters not" +
                        " allowed in XML");
                }
                else if (sysId.indexOf("'") !== -1 &&
                    sysId.indexOf("\"") !== -1) {
                    throw new Error("sysId should not contain both single quotes" +
                        " and double quotes");
                }
            }
            else if (utils_1.isType(sysId, "Undefined")) {
                if (!utils_1.isType(this.pubId, "Undefined")) {
                    throw new Error("sysId should not be undefined if pubId is" +
                        " defined");
                }
            }
            else {
                throw new TypeError("sysId should be a string or undefined");
            }
            this._sysId = sysId;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Inserts a new attribute-list declaration at the specified index. If no
     * index is specified, the node is inserted at the end of this node's
     * children.
     *
     * @param {string} text    The text of the attribute-list declaration.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlDtdAttlist} The newly created attribute-list declaration.
     */
    XmlDtd.prototype.attlist = function (text, index) {
        var attlist = new XmlDtdAttlist_1.default(text);
        this.insertChild(attlist, index);
        return attlist;
    };
    /**
     * Inserts a new comment at the specified index. If no index is specified,
     * the node is inserted at the end of this node's children.
     *
     * @param {string} content The data of the comment.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlComment} The newly created comment.
     */
    XmlDtd.prototype.comment = function (content, index) {
        var comment = new XmlComment_1.default(content);
        this.insertChild(comment, index);
        return comment;
    };
    /**
     * Inserts a new element declaration at the specified index. If no index is
     * specified, the node is inserted at the end of this node's children.
     *
     * @param {string} text    The text of the element declaration.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlDtdElement} The newly created element declaration.
     */
    XmlDtd.prototype.element = function (text, index) {
        var element = new XmlDtdElement_1.default(text);
        this.insertChild(element, index);
        return element;
    };
    /**
     * Inserts a new entity declaration at the specified index. If no index is
     * specified, the node is inserted at the end of this node's children.
     *
     * @param {string} text    The text of the entity declaration.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlDtdEntity} The newly created entity declaration.
     */
    XmlDtd.prototype.entity = function (text, index) {
        var entity = new XmlDtdEntity_1.default(text);
        this.insertChild(entity, index);
        return entity;
    };
    /**
     * Inserts the specified node into this node's children at the specified
     * index. The node is not inserted if it is already present. If this node
     * already has a parent, it is removed from that parent.
     *
     * Only {@link XmlComment}, {@link XmlDtdAttlist}, {@link XmlDtdElement},
     * {@link XmlDtdEntity}, {@link XmlDtdNotation}, and {@link XmlProcInst}
     * nodes can be inserted; otherwise an exception will be thrown.
     *
     * @param {XmlNode} node   The node to insert.
     * @param {number} [index] The index at which to insert the node. Nodes at
     *                         or after the index are shifted to the right. If
     *                         no index is specified, the node is inserted at
     *                         the end.
     *
     * @returns {XmlNode} The node inserted into this node's children, or
     *                    undefined if no node was inserted.
     */
    XmlDtd.prototype.insertChild = function (node, index) {
        if (!(node instanceof XmlComment_1.default || node instanceof XmlDtdAttlist_1.default ||
            node instanceof XmlDtdElement_1.default || node instanceof XmlDtdEntity_1.default ||
            node instanceof XmlDtdNotation_1.default ||
            node instanceof XmlDtdParamEntityRef_1.default ||
            node instanceof XmlProcInst_1.default)) {
            throw new TypeError("node should be an instance of XmlComment,"
                + " XmlDtdAttlist, XmlDtdElement, XmlDtdEntity,"
                + " XmlDtdNotation, XmlDtdParamEntityRef, or"
                + " XmlProcInst");
        }
        return _super.prototype.insertChild.call(this, node, index);
    };
    /**
     * Inserts a new notation declaration at the specified index. If no index is
     * specified, the node is inserted at the end of this node's children.
     *
     * @param {string} text    The text of the notation declaration.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlDtdNotation} The newly created notation declaration.
     */
    XmlDtd.prototype.notation = function (text, index) {
        var notation = new XmlDtdNotation_1.default(text);
        this.insertChild(notation, index);
        return notation;
    };
    /**
     * Inserts a new parameter entity reference at the specified index. If no
     * index is specified, the node is inserted at the end of this node's
     * children.
     *
     * @param {string} entity  The entity to reference.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlDtdParamEntityRef} The newly created parameter entity
     *                                 reference.
     */
    XmlDtd.prototype.paramEntityRef = function (entity, index) {
        var paramEntity = new XmlDtdParamEntityRef_1.default(entity);
        this.insertChild(paramEntity, index);
        return paramEntity;
    };
    /**
     * Inserts a new processing instruction at the specified index. If no index
     * is specified, the node is inserted at the end of this node's children.
     *
     * @param {string} target    The target of the processing instruction.
     * @param {string} [content] The data of the processing instruction, or
     *                           undefined if there is no target.
     * @param {number} [index]   The index at which the node should be inserted.
     *                           If no index is specified, the node is inserted
     *                           at the end of this node's children.
     *
     * @returns {XmlProcInst} The newly created processing instruction.
     */
    XmlDtd.prototype.procInst = function (target, content, index) {
        var procInst = new XmlProcInst_1.default(target, content);
        this.insertChild(procInst, index);
        return procInst;
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlDtd.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        options_1.validateStringOptions(options);
        var str = "<!DOCTYPE " + this.name;
        if (utils_1.isType(this.pubId, "Undefined")) {
            if (!utils_1.isType(this.sysId, "Undefined")) {
                str += " ";
                str = appendId("SYSTEM", this.sysId, str, options);
            }
        }
        else {
            str += " ";
            str = appendId("PUBLIC", this.pubId, str, options);
            str = appendId("", this.sysId, str, options);
        }
        if (this._children.length !== 0) {
            str += " [";
            for (var _i = 0, _a = this._children; _i < _a.length; _i++) {
                var node = _a[_i];
                if (options.pretty) {
                    str += options.newline + options.indent;
                }
                str += node.toString(options);
            }
            if (options.pretty) {
                str += options.newline;
            }
            str += "]>";
        }
        else {
            str += ">";
        }
        return str;
    };
    return XmlDtd;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlDtd;
/**
 * Appends the XML string representation of a public or system identifier to
 * an existing string.
 *
 * @param {string} type           "SYSTEM", "PUBLIC", or ""
 * @param {string} value          The value of the identifier.
 * @param {string} str            The string to which the string representation
 *                                should be appended.
 * @param {IStringOptions} options Formatting options for the string
 *                                representation.
 *
 * @returns {string} The updated string.
 *
 * @private
 */
function appendId(type, value, str, options) {
    str += type + " ";
    if (options.doubleQuotes) {
        if (value.indexOf("\"") !== -1) {
            throw new Error("options.doubleQuotes inconsistent with" +
                " sysId or pubId");
        }
        str += "\"" + value + "\"";
    }
    else {
        if (value.indexOf("'") !== -1) {
            throw new Error("options.doubleQuotes inconsistent with" +
                " sysId or pubId");
        }
        str += "'" + value + "'";
    }
    return str;
}


/***/ }),
/* 23 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlNode_1 = __webpack_require__(2);
/**
 * Represents an XML attribute-list declaration in a document type definition.
 *
 * An XML attribute-list declaration is structured as follows, where `{text}`
 * is the text of the declaration:
 *
 * ```xml
 * <!ATTLIST {text}>
 * ```
 *
 * The `{text}` value is a property of this node.
 *
 * XmlDtdAttlist nodes cannot have any children.
 */
var XmlDtdAttlist = (function (_super) {
    __extends(XmlDtdAttlist, _super);
    /**
     * Initializes a new instance of the {@link XmlDtdAttlist} class.
     *
     * @param {string} text The text associated with the XML attribute-list
     *                      declaration.
     */
    function XmlDtdAttlist(text) {
        _super.call(this);
        this.text = text;
    }
    Object.defineProperty(XmlDtdAttlist.prototype, "text", {
        /**
         * Gets the text associated with the XML attribute-list declaration.
         *
         * @return {string} The text associated with the XML attribute-list
         *                  declaration.
         */
        get: function () {
            return this._text;
        },
        /**
         * Sets the text associated with the XML attribute-list declaration.
         *
         * @param {string} text The text associated with the XML attribute-list
         *                      declaration.
         */
        set: function (text) {
            if (!utils_1.isType(text, "String")) {
                throw new TypeError("text should be a string");
            }
            else if (!validate_1.validateChar(text)) {
                throw new Error("data should not contain characters" +
                    " not allowed in XML");
            }
            this._text = text;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Throws an exception since {@link XmlDtdAttlist} nodes cannot have any
     * children.
     */
    XmlDtdAttlist.prototype.children = function () {
        throw new Error("XmlDtdAttlist nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdAttlist} nodes cannot have any
     * children.
     */
    XmlDtdAttlist.prototype.insertChild = function (node, index) {
        throw new Error("XmlDtdAttlist nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdAttlist} nodes cannot have any
     * children.
     */
    XmlDtdAttlist.prototype.removeChild = function (node) {
        throw new Error("XmlDtdAttlist nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdAttlist} nodes cannot have any
     * children.
     */
    XmlDtdAttlist.prototype.removeChildAtIndex = function (index) {
        throw new Error("XmlDtdAttlist nodes cannot have children");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlDtdAttlist.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        return "<!ATTLIST " + this.text + ">";
    };
    return XmlDtdAttlist;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlDtdAttlist;


/***/ }),
/* 24 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlNode_1 = __webpack_require__(2);
/**
 * Represents an XML element declaration in a document type definition.
 *
 * An XML element declaration is structured as follows, where `{text}` is the
 * text of the declaration:
 *
 * ```xml
 * <!ELEMENT {text}>
 * ```
 *
 * The `{text}` value is a property of this node.
 *
 * XmlDtdElement nodes cannot have any children.
 */
var XmlDtdElement = (function (_super) {
    __extends(XmlDtdElement, _super);
    /**
     * Initializes a new instance of the {@link XmlDtdElement} class.
     *
     * @param {string} text The text associated with the XML element
     *                      declaration.
     */
    function XmlDtdElement(text) {
        _super.call(this);
        this.text = text;
    }
    Object.defineProperty(XmlDtdElement.prototype, "text", {
        /**
         * Gets the text associated with the XML element declaration.
         *
         * @return {string} The text associated with the XML element declaration.
         */
        get: function () {
            return this._text;
        },
        /**
         * Sets the text associated with the XML element declaration.
         *
         * @param {string} text The text associated with the XML element
         *                      declaration.
         */
        set: function (text) {
            if (!utils_1.isType(text, "String")) {
                throw new TypeError("text should be a string");
            }
            else if (!validate_1.validateChar(text)) {
                throw new Error("data should not contain characters" +
                    " not allowed in XML");
            }
            this._text = text;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Throws an exception since {@link XmlDtdElement} nodes cannot have any
     * children.
     */
    XmlDtdElement.prototype.children = function () {
        throw new Error("XmlDtdElement nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdElement} nodes cannot have any
     * children.
     */
    XmlDtdElement.prototype.insertChild = function (node, index) {
        throw new Error("XmlDtdElement nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdElement} nodes cannot have any
     * children.
     */
    XmlDtdElement.prototype.removeChild = function (node) {
        throw new Error("XmlDtdElement nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdElement} nodes cannot have any
     * children.
     */
    XmlDtdElement.prototype.removeChildAtIndex = function (index) {
        throw new Error("XmlDtdElement nodes cannot have children");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlDtdElement.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        return "<!ELEMENT " + this.text + ">";
    };
    return XmlDtdElement;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlDtdElement;


/***/ }),
/* 25 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlNode_1 = __webpack_require__(2);
/**
 * Represents an XML entity declaration in a document type definition.
 *
 * An XML entity declaration is structured as follows, where `{text}` is the
 * text of the declaration:
 *
 * ```xml
 * <!ENTITY {text}>
 * ```
 *
 * The `{text}` value is a property of this node.
 *
 * XmlDtdEntity nodes cannot have any children.
 */
var XmlDtdEntity = (function (_super) {
    __extends(XmlDtdEntity, _super);
    /**
     * Initializes a new instance of the {@link XmlDtdEntity} class.
     *
     * @param {string} text The text associated with the XML entity
     *                      declaration.
     */
    function XmlDtdEntity(text) {
        _super.call(this);
        this.text = text;
    }
    Object.defineProperty(XmlDtdEntity.prototype, "text", {
        /**
         * Gets the text associated with the XML entity declaration.
         *
         * @return {string} The text associated with the XML entity declaration.
         */
        get: function () {
            return this._text;
        },
        /**
         * Sets the text associated with the XML entity declaration.
         *
         * @param {string} text The text associated with the XML entity declaration.
         */
        set: function (text) {
            if (!utils_1.isType(text, "String")) {
                throw new TypeError("text should be a string");
            }
            else if (!validate_1.validateChar(text)) {
                throw new Error("data should not contain characters" +
                    " not allowed in XML");
            }
            this._text = text;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Throws an exception since {@link XmlDtdEntity} nodes cannot have any
     * children.
     */
    XmlDtdEntity.prototype.children = function () {
        throw new Error("XmlDtdEntity nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdEntity} nodes cannot have any
     * children.
     */
    XmlDtdEntity.prototype.insertChild = function (node, index) {
        throw new Error("XmlDtdEntity nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdEntity} nodes cannot have any
     * children.
     */
    XmlDtdEntity.prototype.removeChild = function (node) {
        throw new Error("XmlDtdEntity nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdEntity} nodes cannot have any
     * children.
     */
    XmlDtdEntity.prototype.removeChildAtIndex = function (index) {
        throw new Error("XmlDtdEntity nodes cannot have children");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlDtdEntity.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        return "<!ENTITY " + this.text + ">";
    };
    return XmlDtdEntity;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlDtdEntity;


/***/ }),
/* 26 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlNode_1 = __webpack_require__(2);
/**
 * Represents an XML notation declaration in a document type definition.
 *
 * An XML notation declaration is structured as follows, where `{text}` is the
 * text of the declaration:
 *
 * ```xml
 * <!NOTATION {text}>
 * ```
 *
 * The `{text}` value is a property of this node.
 *
 * XmlDtdNotation nodes cannot have any children.
 */
var XmlDtdNotation = (function (_super) {
    __extends(XmlDtdNotation, _super);
    /**
     * Initializes a new instance of the {@link XmlDtdNotation} class.
     *
     * @param {string} text The text associated with the XML notation
     *                      declaration.
     */
    function XmlDtdNotation(text) {
        _super.call(this);
        this.text = text;
    }
    Object.defineProperty(XmlDtdNotation.prototype, "text", {
        /**
         * Gets the text associated with the XML notation declaration.
         *
         * @return {string} The text associated with the XML notation declaration.
         */
        get: function () {
            return this._text;
        },
        /**
         * Sets the text associated with the XML notation declaration.
         *
         * @param {string} text The text associated with the XML notation
         *                      declaration.
         */
        set: function (text) {
            if (!utils_1.isType(text, "String")) {
                throw new TypeError("text should be a string");
            }
            else if (!validate_1.validateChar(text)) {
                throw new Error("data should not contain characters" +
                    " not allowed in XML");
            }
            this._text = text;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Throws an exception since {@link XmlDtdNotation} nodes cannot have any
     * children.
     */
    XmlDtdNotation.prototype.children = function () {
        throw new Error("XmlDtdNotation nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdNotation} nodes cannot have any
     * children.
     */
    XmlDtdNotation.prototype.insertChild = function (node, index) {
        throw new Error("XmlDtdNotation nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdNotation} nodes cannot have any
     * children.
     */
    XmlDtdNotation.prototype.removeChild = function (node) {
        throw new Error("XmlDtdNotation nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdNotation} nodes cannot have any
     * children.
     */
    XmlDtdNotation.prototype.removeChildAtIndex = function (index) {
        throw new Error("XmlDtdNotation nodes cannot have children");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlDtdNotation.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        return "<!NOTATION " + this.text + ">";
    };
    return XmlDtdNotation;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlDtdNotation;


/***/ }),
/* 27 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlNode_1 = __webpack_require__(2);
/**
 * Represents an XML parameter entity reference in a document type definition.
 *
 * An XML parameter entity reference is structured as follows, where `{entity}`
 * is the name of the entity:
 *
 * ```xml
 * %{entity};
 * ```
 *
 * The `{entity}` value is a property of this node.
 *
 * XmlDtdParamEntityRef nodes cannot have any children.
 */
var XmlDtdParamEntityRef = (function (_super) {
    __extends(XmlDtdParamEntityRef, _super);
    /**
     * Initializes a new instance of the {@link XmlDtdParamEntityRef} class.
     *
     * @param {string} entity The entity to be referenced.
     */
    function XmlDtdParamEntityRef(entity) {
        _super.call(this);
        this.entity = entity;
    }
    Object.defineProperty(XmlDtdParamEntityRef.prototype, "entity", {
        /**
         * Gets the entity to be referenced.
         *
         * @returns {string} The entity to be referenced.
         */
        get: function () {
            return this._entity;
        },
        /**
         * Sets the entity to be referenced.
         *
         * @param {string} entity The entity to be referenced.
         */
        set: function (entity) {
            if (!utils_1.isType(entity, "String")) {
                throw new TypeError("entity should be a string");
            }
            else if (!validate_1.validateName(entity)) {
                throw new Error("entity should not contain characters" +
                    " not allowed in XML names");
            }
            this._entity = entity;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Throws an exception since {@link XmlDtdParamEntityRef} nodes cannot have
     * any children.
     */
    XmlDtdParamEntityRef.prototype.children = function () {
        throw new Error("XmlDtdParamEntityRef nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdParamEntityRef} nodes cannot have
     * any children.
     */
    XmlDtdParamEntityRef.prototype.insertChild = function (node, index) {
        throw new Error("XmlDtdParamEntityRef nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdParamEntityRef} nodes cannot have
     * any children.
     */
    XmlDtdParamEntityRef.prototype.removeChild = function (node) {
        throw new Error("XmlDtdParamEntityRef nodes cannot have children");
    };
    /**
     * Throws an exception since {@link XmlDtdParamEntityRef} nodes cannot have
     * any children.
     */
    XmlDtdParamEntityRef.prototype.removeChildAtIndex = function (index) {
        throw new Error("XmlDtdParamEntityRef nodes cannot have children");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlDtdParamEntityRef.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        return "%" + this.entity + ";";
    };
    return XmlDtdParamEntityRef;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlDtdParamEntityRef;


/***/ }),
/* 28 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var options_1 = __webpack_require__(7);
var utils_1 = __webpack_require__(1);
var validate_1 = __webpack_require__(3);
var XmlAttribute_1 = __webpack_require__(19);
var XmlCdata_1 = __webpack_require__(20);
var XmlCharRef_1 = __webpack_require__(12);
var XmlComment_1 = __webpack_require__(8);
var XmlEntityRef_1 = __webpack_require__(13);
var XmlNode_1 = __webpack_require__(2);
var XmlProcInst_1 = __webpack_require__(9);
var XmlText_1 = __webpack_require__(14);
/**
 * Represents an XML element.
 *
 * A sample XML element is structured as follows, where `{name}` is the name
 * of the element:
 *
 * ```xml
 * <{name} attname="attvalue">
 *     <subelem/>
 *     <?pitarget picontent?>
 *     text
 * </{name}></pre>
 * ```
 *
 * The `{name}` value is a property of the node, while the attributes and
 * children of the element (such as other elements, processing instructions,
 * and text) are children of this node.
 *
 * XmlElement nodes can have an unlimited number of {@link XmlAttribute},
 * {@link XmlCdata}, {@link XmlCharRef}, {@link XmlComment},
 * {@link XmlElement}, {@link XmlEntityRef}, {@link XmlProcInst}, or
 * {@link XmlText} nodes as children.
 */
var XmlElement = (function (_super) {
    __extends(XmlElement, _super);
    /**
     * Initializes a new instance of the {@link XmlElement} class.
     *
     * @param {string} name The name of the element.
     */
    function XmlElement(name) {
        _super.call(this);
        this.name = name;
    }
    Object.defineProperty(XmlElement.prototype, "name", {
        /**
         * Gets the name of the element.
         *
         * @returns {string} The name of the element.
         */
        get: function () {
            return this._name;
        },
        /**
         * Sets the name of the element.
         *
         * @param {string} name The name of the element.
         */
        set: function (name) {
            if (!utils_1.isType(name, "String")) {
                throw new TypeError("name should be a string");
            }
            else if (!validate_1.validateName(name)) {
                throw new Error("name should not contain characters not" +
                    " allowed in XML names");
            }
            this._name = name;
        },
        enumerable: true,
        configurable: true
    });
    /**
     * Inserts an new attribute at the specified index. If no index is
     * specified, the node is inserted at the end of this node's children.
     *
     * @param {string} name
     *        The name of the attribute.
     * @param {string|XmlNode|Array.<string|XmlNode>} value
     *        The value of the attribute. Strings are converted to XmlText
     *        nodes.
     * @param {number} [index]
     *        The index at which the node should be inserted. If no index is
     *        specified, the node is inserted at the end of this node's
     *        children.
     *
     * @returns {XmlAttribute} The newly created attribute.
     */
    XmlElement.prototype.attribute = function (name, value, index) {
        if (utils_1.isType(value, "String")) {
            value = new XmlText_1.default(value);
        }
        else if (utils_1.isType(value, "Array")) {
            var arrayVal = value;
            for (var i = 0; i < arrayVal.length; i++) {
                if (utils_1.isType(arrayVal[i], "String")) {
                    var strVal = arrayVal[i];
                    arrayVal[i] = new XmlText_1.default(strVal);
                }
            }
        }
        var attribute = new XmlAttribute_1.default(name, value);
        this.insertChild(attribute, index);
        return attribute;
    };
    /**
     * Returns an array containing all of the children of this node that are
     * instances of {@link XmlAttribute}.
     *
     * @returns {XmlAttribute[]} An array containing all of the children of
     *                           this node that are instances of
     *                           {@link XmlAttribute}.
     */
    XmlElement.prototype.attributes = function () {
        return this._children.filter(function (node) { return node instanceof XmlAttribute_1.default; });
    };
    /**
     * Inserts a new CDATA section at the specified index. If no index is
     * specified, the node is inserted at the end of this node's children.
     *
     * @param {string} content The data of the CDATA section.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlCdata} The newly created CDATA section.
     */
    XmlElement.prototype.cdata = function (content, index) {
        var cdata = new XmlCdata_1.default(content);
        this.insertChild(cdata, index);
        return cdata;
    };
    /**
     * Inserts a new character reference at the specified index. If no index
     * is specified, the node is inserted at the end of this node's children.
     *
     * @param {string} char    The character to represent using the reference.
     * @param {boolean} [hex]  Whether to use the hexadecimal or decimal
     *                         representation for the reference. If left
     *                         undefined, decimal is the default.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlCharRef} The newly created character reference.
     */
    XmlElement.prototype.charRef = function (char, hex, index) {
        var charRef = new XmlCharRef_1.default(char, hex);
        this.insertChild(charRef, index);
        return charRef;
    };
    /**
     * Inserts a new comment at the specified index. If no index is specified,
     * the node is inserted at the end of this node's children.
     *
     * @param {string} content The data of the comment.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlComment} The newly created comment.
     */
    XmlElement.prototype.comment = function (content, index) {
        var comment = new XmlComment_1.default(content);
        this.insertChild(comment, index);
        return comment;
    };
    /**
     * Inserts a new element at the specified index. If no index is specified,
     * the node is inserted at the end of this node's children.
     *
     * @param {string} name    The name of the element.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlElement} The newly created element.
     */
    XmlElement.prototype.element = function (name, index) {
        var element = new XmlElement(name);
        this.insertChild(element, index);
        return element;
    };
    /**
     * Inserts a new entity reference at the specified index. If no index is
     * specified, the node is inserted at the end of this node's children.
     *
     * @param {string} entity  The entity to be referenced.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlEntityRef} The newly created entity reference.
     */
    XmlElement.prototype.entityRef = function (entity, index) {
        var entityRef = new XmlEntityRef_1.default(entity);
        this.insertChild(entityRef, index);
        return entityRef;
    };
    /**
     * Inserts the specified node into this node's children at the specified
     * index. The node is not inserted if it is already present. If this node
     * already has a parent, it is removed from that parent.
     *
     * Note that only {@link XmlAttribute}, {@link XmlCdata},
     * {@link XmlCharRef}, {@link XmlComment}, {@link XmlElement},
     * {@link XmlEntityRef}, {@link XmlProcInst}, or {@link XmlText} nodes can
     * be inserted; otherwise, an exception will be thrown.
     *
     * @param {XmlNode} node   The node to insert.
     * @param {number} [index] The index at which to insert the node. Nodes at
     *                         or after the index are shifted to the right. If
     *                         no index is specified, the node is inserted at
     *                         the end.
     *
     * @returns {XmlNode} The node inserted into this node's children, or
     *                    undefined if no node was inserted.
     */
    XmlElement.prototype.insertChild = function (node, index) {
        if (!(node instanceof XmlAttribute_1.default ||
            node instanceof XmlCdata_1.default ||
            node instanceof XmlCharRef_1.default ||
            node instanceof XmlComment_1.default ||
            node instanceof XmlElement ||
            node instanceof XmlEntityRef_1.default ||
            node instanceof XmlProcInst_1.default ||
            node instanceof XmlText_1.default)) {
            throw new TypeError("node should be an instance of XmlAttribute,"
                + " XmlCdata, XmlCharRef, XmlComment,"
                + " XmlElement, XmlEntityRef, XmlProcInst,"
                + " or XmlText");
        }
        if (node instanceof XmlAttribute_1.default) {
            var attributes = this._children.filter(function (n) { return n instanceof XmlAttribute_1.default; });
            for (var _i = 0, _a = attributes; _i < _a.length; _i++) {
                var attribute = _a[_i];
                if (attribute.name === node.name) {
                    throw new Error("element already contains an" +
                        " XmlAttribute object with name "
                        + node.name);
                }
            }
        }
        return _super.prototype.insertChild.call(this, node, index);
    };
    /**
     * Inserts a new processing instruction at the specified index. If no index
     * is specified, the node is inserted at the end of this node's children.
     *
     * @param {string} target    The target of the processing instruction.
     * @param {string} [content] The data of the processing instruction, or
     *                           undefined if there is no target.
     * @param {number} [index]   The index at which the node should be inserted.
     *                           If no index is specified, the node is inserted
     *                           at the end of this node's children.
     *
     * @returns {XmlProcInst} The newly created processing instruction.
     */
    XmlElement.prototype.procInst = function (target, content, index) {
        var procInst = new XmlProcInst_1.default(target, content);
        this.insertChild(procInst, index);
        return procInst;
    };
    /**
     * Inserts some new text at the specified index. If no index is specified,
     * the node is inserted at the end of this node's children.
     *
     * @param {string} text    Arbitrary character data.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlText} The newly created text node.
     */
    XmlElement.prototype.text = function (text, index) {
        var txt = new XmlText_1.default(text);
        this.insertChild(txt, index);
        return txt;
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlElement.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        options_1.validateStringOptions(options);
        var attributes = this.attributes();
        var nodes = this._children.filter(function (node) {
            return attributes.indexOf(node) === -1;
        });
        // Element tag start
        var str = "<" + this._name;
        // Attributes
        for (var _i = 0, attributes_1 = attributes; _i < attributes_1.length; _i++) {
            var attribute = attributes_1[_i];
            str += " " + attribute.toString(options);
        }
        // Child nodes
        if (nodes.length > 0) {
            // Element non-empty tag end
            str += ">";
            var indenter = function (line) { return options.indent + line; };
            for (var i = 0; i < nodes.length; i++) {
                var next = nodes[i];
                var nextStr = next.toString(options);
                var prev = i > 0 ? nodes[i - 1] : undefined;
                // Line break before child nodes unless all nodes, or at least
                // the most recent two, are of type XmlCharacterReference,
                // XmlEntityReference, or XmlText
                if (options.pretty) {
                    if (!allSameLineNodes(nodes)) {
                        if (!(i > 0 && onSameLine(next, prev))) {
                            str += options.newline;
                            nextStr = nextStr.split(options.newline)
                                .map(indenter)
                                .join(options.newline);
                        }
                    }
                }
                str += nextStr;
            }
            // Line break before end tag unless all nodes are of type
            // XmlCharacterReference, XmlEntityReference, or XmlText
            if (options.pretty) {
                if (!allSameLineNodes(nodes)) {
                    str += options.newline;
                }
            }
            // Element end tag
            str += "</" + this._name + ">";
        }
        else {
            // Element empty tag end
            str += "/>";
        }
        return str;
    };
    return XmlElement;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlElement;
/**
 * Returns true if the specified nodes are all of type {@link XmlCharRef},
 * {@link XmlEntityRef}, or {@link XmlText}.
 *
 * @param {XmlNode[]} nodes The specified nodes.
 *
 * @returns {boolean} Whether or not the specified nodes are all of type
 *                    {@link XmlCharRef}, {@link XmlEntityRef}, or
 *                    {@link XmlText}.
 *
 * @private
 */
function allSameLineNodes(nodes) {
    for (var _i = 0, nodes_1 = nodes; _i < nodes_1.length; _i++) {
        var node = nodes_1[_i];
        if (!((node instanceof XmlCharRef_1.default ||
            node instanceof XmlEntityRef_1.default ||
            node instanceof XmlText_1.default))) {
            return false;
        }
    }
    return true;
}
/**
 * Returns true if the specified nodes are all of type {@link XmlCharRef},
 * {@link XmlEntityRef}, or {@link XmlText}.
 *
 * @param {XmlNode} prev The first specified node.
 * @param {XmlNode} next The second specified node.
 *
 * @returns {boolean} Whether or not the specified nodes are all of type
 *                    {@link XmlCharRef}, {@link XmlEntityRef}, or
 *                    {@link XmlText}.
 *
 * @private
 */
function onSameLine(prev, next) {
    return (prev instanceof XmlCharRef_1.default ||
        prev instanceof XmlEntityRef_1.default ||
        prev instanceof XmlText_1.default) &&
        (next instanceof XmlCharRef_1.default ||
            next instanceof XmlEntityRef_1.default ||
            next instanceof XmlText_1.default);
}


/***/ }),
/* 29 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __assign = (this && this.__assign) || Object.assign || function(t) {
    for (var s, i = 1, n = arguments.length; i < n; i++) {
        s = arguments[i];
        for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
            t[p] = s[p];
    }
    return t;
};
Object.defineProperty(exports, "__esModule", { value: true });
var ui = __webpack_require__(0);
var js2xmlparser = __webpack_require__(16);
var app_states_1 = __webpack_require__(5);
var XMLParser = __webpack_require__(11);
exports.settings = {};
exports.sceneSettings = {};
var holdActive = false;
var hasTouchHandlers = false;
var dbHandledInTouch = false;
var hasClicked = false;
var middleClick = false;
var wvCanvas = ui.init();
var websocketAddress = "ws://localhost:8085/ws";
try {
    websocketAddress = g_sWebSocketAdress;
}
catch (e) {
    console.warn('Please define a WebSocket address!');
}
var parkedSession = localStorage.getItem('WVParkedSession');
var client = localStorage.getItem('WVClient');
var connectedToSession = false;
var initOptions = {};
initOptions.blockOnLoad = true;
var isNewSession = false;
var clientInitialized = false;
if (client && client !== null) {
    initOptions.testForSessions = true;
    initOptions.client = client;
}
else {
    initOptions.testForSessions = false;
}
if (parkedSession && parkedSession !== null) {
    initOptions.sessionToConnect = parkedSession;
    localStorage.removeItem('WVParkedSession');
    connectedToSession = true;
    vsClient.initialize(wvCanvas, websocketAddress, initOptions);
}
else {
    vsClient.initialize(wvCanvas, websocketAddress, initOptions);
}
$(document.body).addClass('mobile');
vsClient.on('GetSessions', function (res) {
    var disableSessionRestore;
    try {
        disableSessionRestore = _DisableSessionRestore;
    }
    catch (e) {
        disableSessionRestore = false;
    }
    var queryString = window.location.search.substr(1);
    var kvs = queryString.split('&');
    var query = {};
    for (var _i = 0, kvs_1 = kvs; _i < kvs_1.length; _i++) {
        var kv = kvs_1[_i];
        var _a = kv.split('='), k = _a[0], v = _a[1];
        query[k] = v;
    }
    if (typeof query['session'] !== 'undefined') {
        return;
    }
    if (res && res.Session && !Array.isArray(res.Session)) {
        res.Session = [res.Session];
    }
    if (res.Session && res.Session.length > 0 && !disableSessionRestore) {
        ui.showSessionRestoreDialog(res.Session);
    }
    else {
        vsClient.connect();
        if (app_states_1.appStates.backendToken) {
            vsClient.SessionToken(app_states_1.appStates.backendToken);
        }
    }
});
vsClient.on('ConnectionLost', function () {
    ui.blockUI('Connection lost.\n Trying to reconnect..');
});
vsClient.on('ConnectionRestored', function () {
    ui.unblockUI();
});
vsClient.on('initialized', function (response) {
    ui.resizeHandler();
    var xml = '<Call Method="GetSettings"></Call>';
    vsClient.command.executeXML(xml);
    var queryString = window.location.search.substr(1);
    var kvs = queryString.split('&');
    var query = {};
    for (var _i = 0, kvs_2 = kvs; _i < kvs_2.length; _i++) {
        var kv = kvs_2[_i];
        var _a = kv.split('='), k = _a[0], v = _a[1];
        query[k] = v;
    }
    vsClient.once('NewScene3D', function (response) {
        var events = '<Call Method="AddEventListener">';
        events += '<Event>ActivateInteraction</Event>';
        events += '<Event>DeactivateInteraction</Event>';
        events += '<Event>SelectionChanged</Event>';
        events += '<Event>SelectionChangedCustomId</Event>';
        events += '<Event>NodePropertiesChanged</Event></Call>';
        vsClient.command.executeXMLNoMirror(events);
        vsClient.AutoResize();
        openFileHandler(response);
    });
    if (typeof query['session'] !== 'undefined') {
        vsClient.initOptions.sessionToConnect = query['session'];
        connectedToSession = true;
        vsClient.connect();
    }
    if (typeof query['token'] !== 'undefined') {
        app_states_1.appStates.backendToken = decodeURIComponent(query['token']);
        vsClient.on('UpdateScene', function (response) {
            handleVisShareOpen(response);
        });
        if (!client) {
            vsClient.SessionToken(app_states_1.appStates.backendToken);
        }
        else {
            vsClient.sessionInfo.token = app_states_1.appStates.backendToken;
        }
        vsClient.on('OpenFile', function (response) {
            handleVisShareOpen(response);
            var events = '<Call Method="AddEventListener">';
            events += '<Event>ActivateInteraction</Event>';
            events += '<Event>DeactivateInteraction</Event>';
            events += '<Event>SelectionChanged</Event>';
            events += '<Event>SelectionChangedCustomId</Event>';
            events += '<Event>NodePropertiesChanged</Event></Call>';
            vsClient.command.executeXMLNoMirror(events);
            vsClient.AutoResize();
            openFileHandler(response);
        });
        vsClient.on('SessionInitialized', function (res) {
            setSessionInfo(res);
            prepareApp(true);
        });
        vsClient.on('VisShareSave', function (res) {
            var shareBackendCall = {
                Method: 'ShareSave',
                Token: vsClient.sessionInfo.token
            };
            vsClient.command.executeXML(JSON.stringify(shareBackendCall));
            ui.blockUI();
            vsClient.once('SaveAs', function (res) {
                ui.unblockUI();
            });
        });
        vsClient.on('VisShareSaveAs', function (res) {
            var shareBackendCall = {
                Method: 'ShareSaveAs',
                Token: vsClient.sessionInfo.token
            };
            vsClient.command.executeXML(JSON.stringify(shareBackendCall));
            ui.blockUI();
            vsClient.once('SaveAs', function (res) {
                ui.unblockUI();
            });
        });
    }
    else {
        ui.ribbon.deleteShareRibbon(true);
        vsClient.on('SessionInitialized', function (res) {
            setSessionInfo(res);
            prepareApp(false);
        });
    }
});
function setSessionInfo(res) {
    var session = res.Session;
    var client = res.Client;
    isNewSession = res.NewSession;
    if (client) {
        localStorage.setItem('WVClient', client);
    }
    if (session) {
        if (app_states_1.appStates.session && isNewSession) {
            var sessionError = {
                ErrorCode: 0,
                ErrorInfo: 'SessionInvalid',
                Identifier: 'SessionInvalid',
                Message: 'Invalid session.',
                Type: ['Event', 'Error']
            };
            vsClient.emit('ShowMessageBox', sessionError);
            vsClient.vsCanvas.setDefaultImage();
        }
        app_states_1.appStates.session = session;
    }
}
function mapLanguage(browserLang) {
    var country = browserLang.split('-')[0].toLowerCase();
    switch (country) {
        case 'en':
            return 'ENU';
        case 'de':
            return 'DEU';
        case 'it':
            return 'ITA';
        case 'es':
            return 'ESP';
        case 'fr':
            return 'FRA';
        case 'ko':
            return 'KOR';
        case 'zh':
            return 'CHS';
        case 'ja':
            return 'JPN';
        default:
            return 'ENU';
    }
}
function prepareApp(isBackendMode) {
    if (clientInitialized) {
        return;
    }
    clientInitialized = true;
    var lang = localStorage.getItem('WVLang');
    if (!lang) {
        lang = mapLanguage(navigator.language);
    }
    var xml = '<Call Method="SetSettings"><Settings><Renderer><General><Language>' + lang + '</Language></General></Renderer></Settings></Call>';
    vsClient.command.executeXML(xml);
    vsClient.emit('@changeLang', lang);
    var GetSettings = "<Call Method=\"GetSettings\"></Call>";
    vsClient.command.executeXML(GetSettings);
    vsClient.once('GetSettings', function (response, xml) {
        var cachedSettings = localStorage.getItem('VSWVSettings');
        response = removeProp(response, '__text__');
        response = removeProp(response, '__ordinal__');
        delete response.Settings.Renderer.General.Language;
        var version;
        if (response.Settings.Version) {
            if (Array.isArray(response.Settings.Version)) {
                version = response.Settings.Version[0];
            }
            else {
                version = response.Settings.Version;
            }
        }
        delete response.Settings.Version;
        var call = js2xmlparser.parse('Settings', response.Settings).replace("<?xml version='1.0'?>", "");
        call = call.replace('<Settings>', '<Settings Version="' + version + '">');
        localStorage.setItem('VSWVDefaultSettings', call);
        if (cachedSettings === null) {
            localStorage.setItem('VSWVSettings', call);
        }
        else {
            applyCachedSettings();
        }
        vsClient.on('GetSettings', function (response, xml) {
            response = removeProp(response, '__text__');
            response = removeProp(response, '__ordinal__');
            delete response.Settings.Renderer.General.Language;
            var version;
            if (response.Settings.Version) {
                if (Array.isArray(response.Settings.Version)) {
                    version = response.Settings.Version[0];
                }
                else {
                    version = response.Settings.Version;
                }
            }
            delete response.Settings.Version;
            var call = js2xmlparser.parse('Settings', response.Settings).replace("<?xml version='1.0'?>", "");
            call = call.replace('<Settings>', '<Settings Version="' + version + '">');
            localStorage.setItem('VSWVSettings', call);
        });
        var mouseXML = '<Call Method="SetSettings"><Settings><Scene><Mouse><LeftButtonFunction>Rotate</LeftButtonFunction></Mouse></Scene></Settings></Call>';
        vsClient.command.executeXML(mouseXML);
        vsClient.on('SetSettings', function (e) {
            var GetSettings = "<Call Method=\"GetSettings\"></Call>";
            vsClient.command.executeXML(GetSettings);
        });
    });
    vsClient.on('GetSceneSettings', GetSceneSettingsCallback);
    if (isBackendMode === false) {
        vsClient.on('OpenFile', openFileHandler);
        if (window.location.search.length > 0) {
            interceptQueryString(false);
        }
        else {
            ui.createFileOpenBadge();
        }
    }
    else {
        interceptQueryString(true);
    }
    if (connectedToSession) {
        var type = localStorage.getItem('WVFileType') ? localStorage.getItem('WVFileType') : '3D';
        localStorage.removeItem('WVFileType');
        var visShareSpec = localStorage.getItem('WVVisShareSpec') ? localStorage.getItem('WVVisShareSpec') : '';
        localStorage.removeItem('WVVisShareSpec');
        var fileName = localStorage.getItem('WVFileName') ? localStorage.getItem('WVFileName') : '';
        localStorage.removeItem('WVFileName');
        app_states_1.appStates.fileType = type;
        vsClient.emit('OpenFile', { Error: 'SUCCESS', Type: type, CustomId: visShareSpec, FileName: fileName });
        ui.resizeHandler();
    }
    var RegisterEvents = "\n    <Call Method=\"AddEventListener\">\n        <Event>MeasurementChanged</Event>\n        <Event>TransformChanged</Event>\n        <Event>DraftAngleChanged</Event>\n        <Event>SectionChanged</Event>\n        <Event>ProjectedAreaChanged</Event>\n        <Event>TextSearchChanged</Event>\n        <Event>NeighbourhoodSearchChanged</Event>\n        <Event>ShowMessageBox</Event>\n        <Event>ImportFinished</Event>\n        <Event>CalloutSelectionChanged</Event>\n        <Event>SavedSelectionsChanged</Event>\n        <Event>SplitResultUpdated</Event>\n        <Event>WallthicknessValuesChanged</Event>\n        <Event>ActivateCompare</Event>\n        <Event>CalloutAutoAlignmentChanged</Event>\n        <Event>BandAnalysis</Event>\n        <Event>ClashResultUpdated</Event>\n        <Event>ConfigurationViewPlaySelectionChange</Event>\n        <Event>PresetChanged</Event>\n    </Call>\n    ";
    vsClient.on('DraftAngleChanged', function (response) {
        var message = createStatusRow(response);
        ui.openStatusDialog('ID_wv-draftangle', message, 0);
    });
    vsClient.on('MeasurementChanged', function (response) {
        if (response.Mock) {
            return;
        }
        var message = createStatusRow(response);
        ui.openStatusDialog('ID_wv-measurement', message, 0);
    });
    vsClient.on('TransformChanged', function (response) {
        var transformInteraction = false;
        for (var i = 0; i < app_states_1.appStates.interactionMode.length; i++) {
            if (app_states_1.appStates.interactionMode[0].indexOf('Transform') === 0) {
                transformInteraction = true;
            }
        }
        if (transformInteraction) {
            var message = createStatusRow(response.Position);
            ui.openStatusDialog('ID_wv-transform', message, 0);
        }
    });
    vsClient.command.executeXML(RegisterEvents);
    var versionCall = '<Call Method="GetVersionInfo"></Call>';
    vsClient.command.executeXMLAsync(versionCall);
    vsClient.once("GetVersionInfo", function (response) {
        var versionArr = response.Version.split('.');
        versionArr.pop();
        app_states_1.appStates.version = versionArr.join('.');
    });
}
function handleVisShareOpen(response) {
    if (response.CustomId && response.CustomId.indexOf('VSOpen') !== -1) {
        app_states_1.appStates.VisShareSpec = response.CustomId;
        if (response.CustomId.indexOf('VSName') !== -1) {
            document.title = response.CustomId.split('VSName')[1];
            ;
        }
        var accessRights = response.CustomId.split('VSOpen')[1].split('_')[0];
        if (accessRights === 'Write') {
            ui.ribbon.deleteShareRibbon(false);
        }
        else if (accessRights === 'None') {
            ui.ribbon.deleteShareRibbon(true);
        }
    }
    else {
        ui.ribbon.deleteShareRibbon(true);
    }
}
function createStatusRow(event) {
    var type = event.__ordinal__;
    var rows = [];
    for (var i = 0; i < type.length; i++) {
        if (type[i] !== 'Type' && type[i] !== 'ViewId') {
            var typeName = type[i];
            var suffix = event[typeName].__ordinal__[0];
            var text = event[typeName].__text__ + " " + event[typeName][suffix];
            var row = {
                key: typeName,
                value: text
            };
            rows.push(row);
        }
    }
    return rows;
}
vsClient.on('PreviousPage', function (res) {
    ui.pageChange(res.Page);
});
vsClient.on('NextPage', function (res) {
    ui.pageChange(res.Page);
});
vsClient.on('TreeviewUpdated', function (treeviewInfo) {
    if (ui.paneController.leftDefaultAdapter.openPane === 'wv-modeltree') {
        ui.paneController.leftDefaultAdapter.virtualTree = treeviewInfo;
        ui.paneController.leftDefaultAdapter.treePane.updateTree(ui.paneController.leftDefaultAdapter.virtualTree);
    }
    if (ui.paneController.rightDefaultAdapter.openPane === 'wv-modeltree') {
        ui.paneController.rightDefaultAdapter.virtualTree = treeviewInfo;
        ui.paneController.rightDefaultAdapter.treePane.updateTree(ui.paneController.rightDefaultAdapter.virtualTree);
    }
});
vsClient.on('SplitResultUpdated', function (res, xml) {
    if (ui.paneBadgeIdx.indexOf('wv-split') === -1) {
        ui.createPaneBadge('wv-split');
    }
    if (!Array.isArray(res.Data)) {
        res.Data = [res.Data];
    }
    var splitSession = res.SplitGeneralData;
    if (!Array.isArray(res.Data[0].SplitDirectionId)) {
        res.Data[0].SplitDirectionId = [res.Data[0].SplitDirectionId];
    }
    ui.paneController.leftDefaultAdapter.clearSplitGroupTable();
    ui.paneController.rightDefaultAdapter.clearSplitGroupTable();
    for (var i = 0; i < res.Data[0].SplitDirectionId.length; i++) {
        var item = res.Data[0].SplitDirectionId[i];
        var fullSession = __assign({}, splitSession, item);
        fullSession.SplitDirectionId = item.Id;
        fullSession.Error = 'SUCCESS';
        var adapter = ui.paneController.leftDefaultAdapter;
        var session = {
            id: parseInt(fullSession.SplitSessionId),
            name: fullSession.SplitSessionName,
            directionId: parseInt(fullSession.SplitDirectionId),
            directionName: fullSession.SplitDirectionName,
            upColor: fullSession.SplitGroupUpColor,
            upNumPatches: parseInt(fullSession.SplitGroupUpNumPatches),
            downColor: fullSession.SplitGroupDownColor,
            downNumPatches: parseInt(fullSession.SplitGroupDownNumPatches),
            unassignedColor: fullSession.SplitGroupUnassignedColor,
            unassignedNumPatches: parseInt(fullSession.SplitGroupUnassignedNumPatches),
            directionEl: undefined,
            upEl: undefined,
            downEl: undefined,
            maxExplodeDistance: parseFloat(fullSession.SplitFullExplodeDistance),
            sliderPos: fullSession.SplitExplodeSliderPosition ? parseFloat(fullSession.SplitExplodeSliderPosition) : 0
        };
        try {
            if (_MobileRightMoreBadge && _MobileRightMoreBadge.indexOf('wv-split') !== -1) {
                adapter = ui.paneController.rightDefaultAdapter;
            }
        }
        catch (e) { }
        if (adapter.openPane === 'wv-split') {
            adapter.appendSplitSession(session);
        }
        else {
            adapter.sessions.push(session);
        }
    }
});
vsClient.on('PresetChanged', function (res) {
    if (res.Type.indexOf('WallthicknessHeatmap') !== -1) {
        $('#UID_PRESETS').val(res.Name === "" ? 'USERDEF' : res.Name);
    }
});
vsClient.on('GetConfigurationViewInformation', function (response) {
    if (!response.ConfigurationView) {
        return;
    }
    var configurationViewsJson = Array.isArray(response.ConfigurationView) ? response.ConfigurationView : [response.ConfigurationView];
});
vsClient.on('SelectionChanged', function (response) {
    var nodeId = response.NodeId;
    if (typeof nodeId !== 'undefined') {
    }
    if (ui.paneController.leftDefaultAdapter.openPane === 'wv-bom') {
        ui.paneController.leftDefaultAdapter.selectCallouts();
    }
    if (ui.paneController.rightDefaultAdapter.openPane === 'wv-bom') {
        ui.paneController.rightDefaultAdapter.selectCallouts();
    }
});
vsClient.on('NodePropertiesChanged', function (ev) {
    if (ev.General) {
        app_states_1.appStates.drawRect = ev.General.Type !== 'Markup_Text';
        var color_1 = ev.General.Color.replace('#', '');
        $('.mobile-filter-color-list-row').each(function () {
            if (this.id === 'ColorSearchResult_' + color_1) {
                $(this).css('background-color', 'rgb(255, 128, 0)');
            }
            else {
                $(this).css('background-color', 'white');
            }
        });
    }
    else {
        $('.mobile-filter-color-list-row').each(function () {
            $(this).css('background-color', 'white');
        });
    }
});
vsClient.on('ActivateCompare', function (res) {
    if (ui.paneBadgeIdx.indexOf('wv-quickcompare') === -1) {
        ui.createPaneBadge('wv-quickcompare');
        ui.paneController.render('wv-quickcompare', 'left');
    }
});
vsClient.on('CalloutAutoAlignmentChanged', function (res) {
    if (res && res.State) {
        var button = ui.ribbon.adapter.getButton('ID_RIBBON_CALLOUTS_AUTO_ALIGNMENT');
        if (button) {
            button.highlight(res.State === '1');
        }
    }
});
function GetSceneSettingsCallback(response, xml) {
    if (response['Error'] === 'SUCCESS') {
        var renderModes = response['Settings']['Scene']['Visualization']['RenderMode'];
        vsClient.emit('RenderMode', renderModes.split(' '));
        exports.sceneSettings = response.Settings;
        var leftMouseButtonMode = response.Settings.Scene.Mouse.LeftButtonFunction;
        var rightMouseButtonMode = response.Settings.Scene.Mouse.RightButtonFunction;
        var middleMouseButtonMode = response.Settings.Scene.Mouse.MiddleButtonFunction;
        app_states_1.appStates.mouseLeftButtonMode = leftMouseButtonMode;
        app_states_1.appStates.mouseRightButtonMode = rightMouseButtonMode;
        app_states_1.appStates.mouseMiddleButtonMode = middleMouseButtonMode;
        vsClient.emit('LMB', leftMouseButtonMode);
    }
}
function TouchPosCorrectionX(pos) {
    var rect = wvCanvas.getBoundingClientRect();
    var mouseX = pos - rect.left;
    return mouseX;
}
function TouchPosCorrectionY(pos) {
    var rect = wvCanvas.getBoundingClientRect();
    var mouseY = pos - rect.top;
    return mouseY;
}
function HandleMouseForTouchGestureStart(touchevent) {
    app_states_1.appStates.lastGestureDistance = touchevent.distance;
    app_states_1.appStates.lastGestureCenterX = TouchPosCorrectionX(touchevent.center.x);
    app_states_1.appStates.lastGestureCenterY = TouchPosCorrectionY(touchevent.center.y);
    var sCommands = '<Call Method="SetMouseButtonFunctions" Response="false"><LeftMouseButtonFunction>Pan</LeftMouseButtonFunction></Call>';
    sCommands += '<Call Method="MouseDown" Response="false"><X>' + app_states_1.appStates.lastGestureCenterX + '</X><Y>' + app_states_1.appStates.lastGestureCenterY + '</Y><MouseButton>Left</MouseButton></Call>';
    vsClient.command.executeXMLMouse(sCommands);
}
function HandleMouseForTouchGesture(touchevent) {
    var bSendZoom = false;
    app_states_1.appStates.lastGestureDelta = touchevent.distance - app_states_1.appStates.lastGestureDistance;
    if (Math.abs(app_states_1.appStates.lastGestureDelta) > 10) {
        bSendZoom = true;
        app_states_1.appStates.lastGestureDistance = touchevent.distance;
    }
    app_states_1.appStates.lastGestureCenterX = TouchPosCorrectionX(touchevent.center.x);
    app_states_1.appStates.lastGestureCenterY = TouchPosCorrectionY(touchevent.center.y);
    var sMouseButtonDown = '<MouseButton>Left</MouseButton>';
    var callMove = '<Call Method="MouseMove" Response="true"><X>' + app_states_1.appStates.lastGestureCenterX + '</X><Y>' + app_states_1.appStates.lastGestureCenterY + '</Y>' + sMouseButtonDown + '</Call>';
    if (bSendZoom) {
        vsClient.command.executeXMLZoomMouseWheel(app_states_1.appStates.lastGestureCenterX, app_states_1.appStates.lastGestureCenterY, app_states_1.appStates.lastGestureDelta);
    }
    else {
        vsClient.command.executeXMLMouse(callMove);
    }
}
function HandleMouseForTouchGestureEnd() {
    var sCommands = '<Call Method="MouseUp" Response="false"><X>' + app_states_1.appStates.lastGestureCenterX + '</X><Y>' + app_states_1.appStates.lastGestureCenterY + '</Y><MouseButton>Left</MouseButton></Call>';
    sCommands += '<Call Method="SetMouseButtonFunctions" Response="false"><LeftMouseButtonFunction>' + app_states_1.appStates.mouseLeftButtonMode + '</LeftMouseButtonFunction></Call>';
    vsClient.command.executeXMLMouse(sCommands);
}
function HandleTouchDragStart(touchevent) {
    if (!app_states_1.appStates.touchGestureActive && !app_states_1.appStates.mouseActive) {
        touchevent.preventDefault();
        var mouseX = TouchPosCorrectionX(touchevent.touch.x.client);
        var mouseY = TouchPosCorrectionY(touchevent.touch.y.client);
        if (app_states_1.appStates.mouseLeftButtonMode === 'RectZoom' || app_states_1.appStates.mouseLeftButtonMode == 'RectSelect') {
            vsClient.startRect(mouseX, mouseY);
        }
        var s = '<Call Method="MouseMove" Response="true"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>None</MouseButton></Call>';
        var mouseButton = 'Left';
        if (app_states_1.appStates.interactionMode.indexOf('Flythrough') !== -1) {
            mouseButton = 'Right';
        }
        s += '<Call Method="MouseDown" Response="false"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>' + mouseButton + '</MouseButton></Call>';
        vsClient.command.executeXMLMouse(s);
    }
}
function HandleTouchDrag(touchevent) {
    if (!app_states_1.appStates.touchGestureActive && !app_states_1.appStates.mouseActive) {
        touchevent.preventDefault();
        var mouseX = TouchPosCorrectionX(touchevent.touch.x.client);
        var mouseY = TouchPosCorrectionY(touchevent.touch.y.client);
        var mouseButton = '<MouseButton>Left</MouseButton>';
        if (app_states_1.appStates.interactionMode.indexOf('Flythrough') !== -1) {
            mouseButton = '<MouseButton>Right</MouseButton>';
        }
        var callMove = '<Call Method="MouseMove" Response="true"><X>' + mouseX + '</X><Y>' + mouseY + '</Y>' + mouseButton + '</Call>';
        if ((app_states_1.appStates.mouseLeftButtonMode === 'RectZoom' || app_states_1.appStates.mouseLeftButtonMode == 'RectSelect') && app_states_1.appStates.drawRect
            && app_states_1.appStates.interactionMode.indexOf('Flythrough') === -1) {
            vsClient.drawRect(mouseX, mouseY);
        }
        else {
            vsClient.command.executeXMLMouseMove(callMove);
        }
    }
}
function HandleTouchDragEnd(touchevent) {
    if (!app_states_1.appStates.touchGestureActive && !app_states_1.appStates.mouseActive) {
        touchevent.preventDefault();
        var mouseX = TouchPosCorrectionX(touchevent.touch.x.client);
        var mouseY = TouchPosCorrectionY(touchevent.touch.y.client);
        var mouseButton = 'Left';
        if (app_states_1.appStates.interactionMode.indexOf('Flythrough') !== -1) {
            mouseButton = 'Right';
        }
        var xml = '<Call Method="MouseUp" Response="false"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>' + mouseButton + '</MouseButton></Call>';
        vsClient.command.executeXMLMouse(xml);
    }
}
function mousedownHandler(ev) {
    if (app_states_1.appStates.touchActive) {
        return;
    }
    app_states_1.appStates.mouseActive = true;
    hasClicked = true;
    var boundingRect = wvCanvas.getBoundingClientRect();
    var offsetLeft = boundingRect.left;
    var offsetTop = boundingRect.top;
    var mouseX = ev.pageX - offsetLeft;
    var mouseY = ev.pageY - offsetTop;
    var button = 'None';
    switch (ev.which) {
        case 1:
            $('#wv-dock').toggleClass('disable-selection');
            app_states_1.appStates.mouseLeftButtonDown = true;
            button = 'Left';
            if (app_states_1.appStates.mouseLeftButtonMode === 'RectZoom' || app_states_1.appStates.mouseLeftButtonMode === 'RectSelect') {
                vsClient.startRect(mouseX, mouseY);
            }
            break;
        case 2:
            button = 'Middle';
            if (app_states_1.appStates.mouseMiddleButtonMode === 'RectZoom' || app_states_1.appStates.mouseMiddleButtonMode === 'RectSelect') {
                vsClient.startRect(mouseX, mouseY);
            }
            break;
        case 3:
            button = 'Right';
            if (app_states_1.appStates.mouseRightButtonMode === 'RectZoom' || app_states_1.appStates.mouseRightButtonMode === 'RectSelect') {
                vsClient.startRect(mouseX, mouseY);
            }
            break;
    }
    var xml = '<Call Method="MouseDown"><X>'
        + mouseX + '</X><Y>'
        + mouseY + '</Y>'
        + '<MouseButton>'
        + button + '</MouseButton></Call>';
    vsClient.command.executeXML(xml);
}
function mousemoveHandler(ev) {
    if (app_states_1.appStates.touchActive) {
        return;
    }
    if (ev.pageX === app_states_1.appStates.lastPagePosX && ev.pageY === app_states_1.appStates.lastPagePosY) {
        return;
    }
    app_states_1.appStates.lastPagePosX = ev.pageX;
    app_states_1.appStates.lastPagePosY = ev.pageY;
    var boundingRect = wvCanvas.getBoundingClientRect();
    var offsetLeft = boundingRect.left;
    var offsetTop = boundingRect.top;
    var mouseX = ev.pageX - offsetLeft;
    var mouseY = ev.pageY - offsetTop;
    vsClient.mouseX = mouseX;
    vsClient.mouseY = mouseY;
    var button = 'None';
    switch (ev.which) {
        case 1:
            if (ev.button > 1) {
                button = 'Middle';
                if ((app_states_1.appStates.mouseLeftButtonMode === 'RectZoom' || app_states_1.appStates.mouseLeftButtonMode == 'RectSelect') && app_states_1.appStates.drawRect
                    && app_states_1.appStates.interactionMode.indexOf('Flythrough') === -1) {
                    if (!middleClick) {
                        vsClient.startRect(mouseX, mouseY);
                        middleClick = true;
                        var xml = '<Call Method="MouseDown"><X>'
                            + mouseX + '</X><Y>'
                            + mouseY + '</Y>'
                            + '<MouseButton>'
                            + button + '</MouseButton></Call>';
                        vsClient.command.executeXMLNoMirror(xml);
                        return;
                    }
                    vsClient.drawRect(mouseX, mouseY);
                    return;
                }
                break;
            }
            button = 'Left';
            if ((app_states_1.appStates.mouseLeftButtonMode === 'RectZoom' || app_states_1.appStates.mouseLeftButtonMode == 'RectSelect')
                && app_states_1.appStates.drawRect && app_states_1.appStates.interactionMode.indexOf('Flythrough') === -1) {
                vsClient.drawRect(mouseX, mouseY);
                return;
            }
            break;
        case 2:
            if ((app_states_1.appStates.mouseLeftButtonMode === 'RectZoom' || app_states_1.appStates.mouseLeftButtonMode == 'RectSelect') && app_states_1.appStates.drawRect
                && app_states_1.appStates.interactionMode.indexOf('Flythrough') === -1) {
                vsClient.drawRect(mouseX, mouseY);
                return;
            }
            button = 'Middle';
            break;
        case 3:
            if ((app_states_1.appStates.mouseLeftButtonMode === 'RectZoom' || app_states_1.appStates.mouseLeftButtonMode == 'RectSelect')
                && app_states_1.appStates.drawRect && app_states_1.appStates.interactionMode.indexOf('Flythrough') === -1) {
                vsClient.drawRect(mouseX, mouseY);
                return;
            }
            break;
    }
    var xml = '<Call Method="MouseMove" Response="true"><X>'
        + mouseX + '</X><Y>'
        + mouseY + '</Y><MouseButton>'
        + button + '</MouseButton></Call>';
    vsClient.command.executeXMLMouseMove(xml);
}
function mouseupHandler(ev) {
    if (app_states_1.appStates.touchActive) {
        return;
    }
    var boundingRect = wvCanvas.getBoundingClientRect();
    var offsetLeft = boundingRect.left;
    var offsetTop = boundingRect.top;
    var mouseX = ev.pageX - offsetLeft;
    var mouseY = ev.pageY - offsetTop;
    var button = 'None';
    switch (ev.which) {
        case 1:
            $('#wv-dock').toggleClass('disable-selection');
            button = 'Left';
            app_states_1.appStates.mouseLeftButtonDown = false;
            break;
        case 2:
            button = 'Middle';
            break;
        case 3:
            button = 'Right';
            break;
    }
    var mouseFlag = '';
    if (ev.ctrlKey === true || ui.mockCtrl) {
        mouseFlag = '<MouseFlag>Ctrl</MouseFlag>';
    }
    if (middleClick) {
        middleClick = false;
        button = 'Middle';
    }
    var xml = '<Call Method="MouseUp" Response="false"><X>'
        + mouseX + '</X><Y>'
        + mouseY + '</Y>'
        + mouseFlag
        + '<MouseButton>' + button + '</MouseButton></Call>';
    vsClient.command.executeXMLMouse(xml);
    app_states_1.appStates.mouseActive = false;
}
function dblclickHandler(ev) {
    ev.preventDefault();
    ev.stopPropagation();
    var boundingRect = wvCanvas.getBoundingClientRect();
    var offsetLeft = boundingRect.left;
    var offsetTop = boundingRect.top;
    var mouseX = ev.pageX - offsetLeft;
    var mouseY = ev.pageY - offsetTop;
    var button = 'None';
    if (ev.which === 1)
        button = 'Left';
    dbClickMethod(mouseX, mouseY, button);
}
function dbClickMethod(x, y, button) {
    if (dbHandledInTouch) {
        dbHandledInTouch = false;
        return;
    }
    var xml = '<Event Type="NodePropertiesChanged"></Event>';
    vsClient.command.executeXML(xml);
    vsClient.once('NodePropertiesChanged', function (res) {
        if (res && res.General && res.General.Name === 'MarkupSet') {
            var xml_1 = '<Call Method="Undo"><Steps>1</Steps></Call>';
            vsClient.command.executeXML(xml_1);
            vsClient.once('Undo', function () {
                vsClient.once('NodePropertiesChanged', function (resp) {
                    createMarkupOverlay(resp, x, y);
                });
            });
        }
        else if (res && res.General && res.General.Name === 'CreatedNotes') {
            var xml_2 = '<Call Method="Undo"><Steps>1</Steps></Call>';
            vsClient.command.executeXML(xml_2);
            vsClient.once('Undo', function () {
                if (res && res.General.Name === 'MarkupSet') {
                    var xml_3 = '<Call Method="Undo"><Steps>1</Steps></Call>';
                    vsClient.command.executeXML(xml_3);
                    vsClient.once('Undo', function () {
                        vsClient.once('NodePropertiesChanged', function (response) {
                            createMarkupOverlay(response, x, y);
                        });
                    });
                }
                else {
                    var dbClickXML = '<Call Method="MouseDoubleClick" Response="false"><X>'
                        + x + '</X><Y>'
                        + y + '</Y><MouseButton>'
                        + button + '</MouseButton></Call>';
                    vsClient.command.executeXMLMouse(dbClickXML);
                }
            });
        }
        else if (res && res.Markup) {
            createMarkupOverlay(res, x, y);
        }
        else {
            var dbClickXML = '<Call Method="MouseDoubleClick" Response="false"><X>'
                + x + '</X><Y>'
                + y + '</Y><MouseButton>'
                + button + '</MouseButton></Call>';
            vsClient.command.executeXMLMouse(dbClickXML);
        }
    });
}
function createMarkupOverlay(res, x, y) {
    var boundingRect = wvCanvas.getBoundingClientRect();
    var offsetLeft = boundingRect.left;
    var offsetTop = boundingRect.top;
    var text;
    var documentOverlay = document.createElement('div');
    documentOverlay.className = 'mobile-page-overlay';
    var appDiv = document.getElementById('wv-app');
    document.body.appendChild(documentOverlay);
    var markupTextField = document.createElement('div');
    markupTextField.contentEditable = 'true';
    markupTextField.className = 'mobile-editable-markup-overlay';
    var initialText = res.Markup.Text.Content;
    $(markupTextField).html(initialText);
    documentOverlay.appendChild(markupTextField);
    markupTextField.focus();
    window.getSelection().selectAllChildren(markupTextField);
    markupTextField.addEventListener('keydown', function (e) {
        if (e.keyCode === 27) {
            var xml = '<Event Type="NodePropertiesChanged"><Markup><Text><Content>' + initialText + '</Content></Text></Markup></Event>';
            vsClient.command.executeXML(xml);
            $(markupTextField).off('blur');
            $(markupTextField).remove();
            $(documentOverlay).remove();
        }
    });
    $(markupTextField).on('blur', function (e) {
        text = markupTextField.innerText;
        var xml = '<Event Type="NodePropertiesChanged"><Markup><Text><Content>' + text + '</Content></Text></Markup></Event>';
        vsClient.command.executeXML(xml.replace(/\n<\//, "</"));
        $(markupTextField).remove();
        $(documentOverlay).remove();
    });
}
function wheelHandler(ev) {
    ev.preventDefault();
    var boundingRect = wvCanvas.getBoundingClientRect();
    var offsetLeft = boundingRect.left;
    var offsetTop = boundingRect.top;
    var mouseX = ev.pageX - offsetLeft;
    var mouseY = ev.pageY - offsetTop;
    var deltaY = ev.deltaY;
    var correctedDeltaY = navigator.userAgent.indexOf("Firefox") != -1 ? deltaY * -0.1 : deltaY * -0.005;
    var xml = '<Call Method="MouseWheel" Response="false"><X>'
        + mouseX + '</X><Y>' + mouseY + '</Y><Delta>'
        + correctedDeltaY + ' </Delta></Call>';
    vsClient.command.executeXMLMouse(xml);
}
function applyCachedSettings() {
    try {
        if (_UseCachedSettings) {
            var settingsCall = '<Call Method="SetSettings">' + localStorage.getItem('VSWVSettings') + '</Call>';
            vsClient.command.executeXMLNoMirror(settingsCall);
        }
    }
    catch (e) {
    }
}
var removeProp = function (obj, prop) {
    Object.keys(obj).forEach(function (key) {
        return (key === prop) && delete obj[key] ||
            (obj[key] && typeof obj[key] === 'object') && removeProp(obj[key], prop);
    });
    return obj;
};
function openFileHandler(response) {
    try {
        if (response.Type === '3D') {
            for (var i = 0; i < _3DReducedSet.length; i++) {
                var id = _3DReducedSet[i];
                $('#' + id).hide();
            }
            for (var i = 0; i < _2DReducedSet.length; i++) {
                var id = _2DReducedSet[i];
                $('#' + id).show();
            }
        }
        else if (response.Type === '2D') {
            for (var i = 0; i < _3DReducedSet.length; i++) {
                var id = _3DReducedSet[i];
                $('#' + id).show();
            }
            for (var i = 0; i < _2DReducedSet.length; i++) {
                var id = _2DReducedSet[i];
                $('#' + id).hide();
            }
        }
    }
    catch (e) { }
    if (!response.SceneId) {
        app_states_1.appStates.fileName = response.FileName;
    }
    if (!(response.Error == "SUCCESS")) {
        alert("Error on loading file!");
    }
    else {
        $('#mobile-file-open-badge').remove();
        var GetSettings = "<Call Method=\"GetSceneSettings\"></Call>";
        vsClient.command.executeXML(GetSettings);
        app_states_1.appStates.reset();
        app_states_1.appStates.fileLoaded = true;
        app_states_1.appStates.fileType = response.Type;
        if (response.Type === '2D') {
            var pageCount = "<Call Method=\"GetPageCount\"></Call>";
            vsClient.command.executeXML(pageCount);
            vsClient.once('GetPageCount', function (res) {
                if (res.Error !== 'SUCCESS') {
                    app_states_1.appStates.pageCount = '';
                }
                else {
                    app_states_1.appStates.pageCount = res.Pages;
                }
                ui.showDocumentBar();
                ui.show2DExtraLayerBadge();
            });
        }
        else {
            ui.hideDocumentBar();
            ui.hide2DExtraLayerBadge();
        }
        if (ui.paneController.leftDefaultAdapter.sessions.length > 0) {
            ui.paneController.leftDefaultAdapter.sessions = [];
            ui.paneController.leftDefaultAdapter.activeSession = undefined;
            ui.paneController.leftDefaultAdapter.resultInitialized = false;
            $(ui.paneController.leftDefaultAdapter.resultGroup.wrapperEl).remove();
            $(ui.paneController.leftDefaultAdapter.resultGroup.panelContainer).remove();
            $(ui.paneController.leftDefaultAdapter.splitGroupTable).remove();
        }
        else if (ui.paneController.rightDefaultAdapter.sessions.length > 0) {
            ui.paneController.rightDefaultAdapter.sessions = [];
            ui.paneController.rightDefaultAdapter.activeSession = undefined;
            ui.paneController.rightDefaultAdapter.resultInitialized = false;
            $(ui.paneController.rightDefaultAdapter.resultGroup.wrapperEl).remove();
            $(ui.paneController.rightDefaultAdapter.resultGroup.panelContainer).remove();
            $(ui.paneController.rightDefaultAdapter.splitGroupTable).remove();
        }
        var leftMouseMode = 'Rotate';
        try {
            leftMouseMode = _MobileTouchControl;
        }
        catch (e) { }
        var mouseXML = '<Call Method="SetSceneSettings"><Settings><Scene><Mouse><LeftButtonFunction>' + leftMouseMode + '</LeftButtonFunction></Mouse></Scene></Settings></Call>';
        vsClient.command.executeXML(mouseXML);
        app_states_1.appStates.selectionMode = 'Select';
        var xml = '<Call Method="SetSelectionMode"><SelectionMode>' + app_states_1.appStates.selectionMode + '</SelectionMode></Call>';
        vsClient.command.executeXML(xml);
        vsClient.emit('Select', 'Select');
        var getConfigurationView = '<Call Method="GetConfigurationViewInformation"><Image>true</Image></Call>';
        vsClient.command.executeXMLAsync(getConfigurationView);
        if (response.Type !== '3D') {
            var pageCount = "<Call Method=\"GetPageCount\"></Call>";
            vsClient.command.executeXMLAsync(pageCount);
            vsClient.once('GetPageCount', function (res) {
                if (res.Error !== 'SUCCESS') {
                    app_states_1.appStates.pageCount = '';
                }
                else {
                    app_states_1.appStates.pageCount = res.Pages;
                }
            });
        }
        $(wvCanvas).off('mousedown').on('mousedown', function (e) {
            mousedownHandler(e);
        });
        $(wvCanvas).off('mousemove').on('mousemove', function (e) {
            mousemoveHandler(e);
        });
        $(wvCanvas).off('mouseup').on('mouseup', function (e) {
            mouseupHandler(e);
        });
        wvCanvas.addEventListener('dblclick', dblclickHandler, false);
        wvCanvas.addEventListener('wheel', wheelHandler, false);
        wvCanvas.ontouchmove = stopDefaultMouseEventPropagation;
        if (!hasTouchHandlers) {
            hasTouchHandlers = true;
            $(wvCanvas).kendoTouch({
                doubleTapTimeout: 250,
                multiTouch: true,
                enableSwipe: false,
                touchstart: function (e) {
                    if (app_states_1.appStates.mouseActive || (e.event.originalEvent['pointerType'] && e.event.originalEvent['pointerType'] !== 'touch')) {
                        e.sender.cancel();
                    }
                    else {
                        app_states_1.appStates.touchActive = true;
                    }
                },
                gesturestart: function (e) {
                    app_states_1.appStates.touchGestureActive = true;
                    HandleMouseForTouchGestureStart(e);
                },
                gesturechange: function (e) {
                    HandleMouseForTouchGesture(e);
                },
                gestureend: function (e) {
                    e.preventDefault();
                    HandleMouseForTouchGestureEnd();
                },
                drag: function (e) {
                    if (holdActive) {
                        TouchHoldMove(e);
                        return;
                    }
                    HandleTouchDrag(e);
                },
                dragstart: function (e) {
                    if (holdActive) {
                        TouchHoldMove(e);
                        return;
                    }
                    HandleTouchDragStart(e);
                },
                dragend: function (e) {
                    if (holdActive) {
                        e.sender.cancel();
                        holdActive = false;
                        var mouseX = TouchPosCorrectionX(e.touch.x.client);
                        var mouseY = TouchPosCorrectionY(e.touch.y.client);
                        var mouseFlag = '';
                        if (e.event.altKey === true || e.event.ctrlKey === true || ui.mockCtrl) {
                            mouseFlag = '<MouseFlag>Ctrl</MouseFlag>';
                        }
                        var s = '<Call Method="MouseMove" Response="true"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>None</MouseButton></Call>';
                        s += '<Call Method="MouseUp" Response="false"><X>' + mouseX + '</X><Y>' + mouseY + '</Y>' + mouseFlag + '<MouseButton>Left</MouseButton></Call>';
                        vsClient.command.executeXMLMouse(s);
                        return;
                    }
                    HandleTouchDragEnd(e);
                    app_states_1.appStates.touchActive = false;
                    app_states_1.appStates.touchGestureActive = false;
                },
                hold: function (e) {
                    if (app_states_1.appStates.interactionMode.indexOf('Flythrough') !== -1) {
                        TouchHoldMove(e);
                    }
                },
                doubletap: function (e) {
                    e.preventDefault();
                    dbHandledInTouch = false;
                    var boundingRect = wvCanvas.getBoundingClientRect();
                    var offsetLeft = boundingRect.left;
                    var offsetTop = boundingRect.top;
                    var mouseX = e.touch.x.location - offsetLeft;
                    var mouseY = e.touch.y.location - offsetTop;
                    var button = 'None';
                    if (e.event.which !== undefined && (e.event.which === 1 || e.event.which === 0)) {
                        button = 'Left';
                    }
                    dbClickMethod(mouseX, mouseY, button);
                    dbHandledInTouch = true;
                },
                tap: function (e) {
                    if (hasClicked) {
                        hasClicked = false;
                        return;
                    }
                    else {
                        if (ui.paneCloseTouch) {
                            ui.setPaneCloseTouch(false);
                            return;
                        }
                        var boundingRect = wvCanvas.getBoundingClientRect();
                        var offsetLeft = boundingRect.left;
                        var offsetTop = boundingRect.top;
                        var mouseX = e.touch.x.location - offsetLeft;
                        var mouseY = e.touch.y.location - offsetTop;
                        var button = 'Left';
                        $('#wv-dock').toggleClass('disable-selection');
                        app_states_1.appStates.mouseLeftButtonDown = true;
                        if (app_states_1.appStates.mouseLeftButtonMode === 'RectZoom' || app_states_1.appStates.mouseLeftButtonMode === 'RectSelect') {
                            vsClient.startRect(mouseX, mouseY);
                        }
                        var xml_4 = '<Call Method="MouseMove" Response="true"><X>';
                        xml_4 += mouseX;
                        xml_4 += '</X><Y>';
                        xml_4 += mouseY;
                        xml_4 += '</Y><MouseButton>None</MouseButton></Call>';
                        ;
                        vsClient.command.executeXMLNoMirror(xml_4);
                        xml_4 = '<Call Method="MouseDown"><X>';
                        xml_4 += mouseX + '</X><Y>';
                        xml_4 += mouseY + '</Y>';
                        xml_4 += '<MouseButton>';
                        xml_4 += button + '</MouseButton></Call>';
                        vsClient.command.executeXMLNoMirror(xml_4);
                        xml_4 = '<Call Method="MouseUp"><X>';
                        xml_4 += mouseX + '</X><Y>';
                        xml_4 += mouseY + '</Y>';
                        if (e.event.altKey === true || e.event.ctrlKey === true || ui.mockCtrl) {
                            xml_4 += '<MouseFlag>Ctrl</MouseFlag>';
                        }
                        xml_4 += '<MouseButton>';
                        xml_4 += button + '</MouseButton></Call>';
                        vsClient.command.executeXMLNoMirror(xml_4);
                    }
                }
            });
        }
    }
}
function TouchHoldMove(e) {
    if (!app_states_1.appStates.touchActive) {
        e.sender.cancel();
        return;
    }
    var mouseX = TouchPosCorrectionX(e.touch.x.client);
    var mouseY = TouchPosCorrectionY(e.touch.y.client);
    var mouseFlag = '';
    if (e.event.altKey === true || e.event.ctrlKey === true || ui.mockCtrl) {
        mouseFlag = '<MouseFlag>Ctrl</MouseFlag>';
    }
    var s = '<Call Method="MouseMove" Response="true"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>None</MouseButton></Call>';
    if (!holdActive) {
        s += '<Call Method="MouseDown" Response="false"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>Left</MouseButton></Call>';
        holdActive = true;
    }
    vsClient.command.executeXMLMouse(s);
}
function stopDefaultMouseEventPropagation(e) {
    e.preventDefault();
    e.stopPropagation();
}
window.addEventListener("keyup", function (e) {
    var xmlSelect = '';
    var xml = '';
    switch (e.keyCode) {
        case 27:
            var s = '<Call Method="ActivateInteraction"><Interaction>Deactivate_AllInteractions</Interaction></Call>';
            vsClient.command.executeXML(s);
            vsClient.once('ActivateInteraction', function (response) {
            });
            var settings_1 = localStorage.getItem('VSWVSettings');
            if (settings_1 === null) {
                settings_1 = localStorage.getItem('VSWVDefaultSettings');
            }
            var settingsObj = XMLParser.parseXML(settings_1);
            var leftMouseMode = settingsObj.Settings.Scene.Mouse.LeftButtonFunction;
            try {
                leftMouseMode = _MobileTouchControl;
            }
            catch (e) { }
            var rightMouseMode = settingsObj.Settings.Scene.Mouse.RightButtonFunction;
            var middleMouseMode = settingsObj.Settings.Scene.Mouse.MiddleButtonFunction;
            var mouseSettingsCall = '<Call Method="SetSceneSettings">';
            var settingsPrePath = '<Settings><Scene><Mouse>';
            var settingsPostPath = '</Mouse></Scene></Settings></Call>';
            mouseSettingsCall += settingsPrePath + '<LeftButtonFunction> ' + leftMouseMode + ' </LeftButtonFunction>';
            mouseSettingsCall += '<RightButtonFunction>' + rightMouseMode + '</RighButtonFunction>';
            mouseSettingsCall += '<MiddleButtonFunction>' + middleMouseMode + '</MiddleButtonFunction>' + settingsPostPath;
            vsClient.command.executeXML(mouseSettingsCall);
            app_states_1.appStates.selectionMode = 'Select';
            xmlSelect = '<Call Method="SetSelectionMode"><SelectionMode>' + app_states_1.appStates.selectionMode + '</SelectionMode></Call>';
            vsClient.command.executeXML(xmlSelect);
            $('#mobile-text-search-panel').hide();
            ui.hideAll();
            if (ui.paneBadgeIdx.indexOf('wv-quickcompare') !== -1) {
                ui.togglePaneBadge('QuickComparePane');
            }
            var quickCompareButton = ui.ribbon.adapter.getButtonByUID('rQuickCompare');
            if (quickCompareButton) {
                quickCompareButton.highlight(false);
            }
            break;
        case 46:
            xml = '<Call Method="ModifySelection"><SelectionModifier>Delete</SelectionModifier></Call>';
            vsClient.command.executeXML(xml);
            break;
    }
}, false);
window.addEventListener("keydown", function (e) {
    var xml = '';
    switch (e.keyCode) {
        case e.altKey && 65:
            xml = '<Call Method="ShowAll"></Call>';
            break;
        case e.altKey && 73:
            xml = '<Call Method="ModifySelection"><SelectionModifier>Isolate</SelectionModifier></Call>';
            break;
        case e.altKey && 72:
            xml = '<Call Method="ModifySelection"><SelectionModifier>Hide</SelectionModifier></Call>';
            break;
        case e.altKey && 71:
            xml = '<Call Method="ModifySelection"><SelectionModifier>Ghost</SelectionModifier></Call>';
            break;
        case e.altKey && 83:
            xml = '<Call Method="ModifySelection"><SelectionModifier>UnGhost</SelectionModifier></Call>';
            break;
        case e.altKey && 65:
            xml = '<Call Method="ShowAll"></Call>';
            break;
        case e.ctrlKey && 65:
            e.preventDefault();
            if (!e.shiftKey) {
                xml = '<Call Method="SelectAllGeometries"></Call>';
            }
            else {
                xml = '<Call Method="ModifySelection"><SelectionModifier>Deselect</SelectionModifier></Call>';
            }
            break;
        case e.ctrlKey && e.shiftKey && 65:
            xml = '<Call Method="ModifySelection"><SelectionModifier>Deselect</SelectionModifier></Call>';
            break;
        case e.ctrlKey && 73:
            xml = '<Call Method="ModifySelection"><SelectionModifier>Invert</SelectionModifier></Call>';
            break;
        case e.ctrlKey && 67:
            xml = '<Call Method="ModifySelection"><SelectionModifier>Copy</SelectionModifier></Call>';
            break;
        case e.ctrlKey && 86:
            xml = '<Call Method="ModifySelection"><SelectionModifier>PasteToSelection</SelectionModifier></Call>';
            break;
        case e.ctrlKey && 90:
            xml = '<Call Method="Undo"><Steps>1</Steps></Call>';
            break;
        case e.ctrlKey && 85:
            e.preventDefault();
            xml = '<Call Method="GetSceneSettings"></Call>';
            vsClient.command.executeXMLAsync(xml);
            vsClient.once('GetSceneSettings', function (response) {
                var gridStatus = response.Settings.Scene.Visualization.ShowGrid;
                gridStatus = gridStatus === 'true' ? 'false' : 'true';
                xml = '<Call Method="SetSceneSettings">';
                xml += '<Settings><Scene><Visualization><ShowGrid>' + gridStatus;
                xml += '</ShowGrid></Visualization></Scene></Settings></Call>';
                vsClient.command.executeXMLAsync(xml);
            });
            return;
        case 112:
            e.preventDefault();
            $('#wv-ribbon-about').click();
            return;
        default:
            return;
    }
    vsClient.command.executeXML(xml);
}, false);
vsClient.on('Relay', function (res) {
    var message = res.__text__;
    if (message === 'VisShare_SaveAsDone') {
        ui.toast(true);
    }
    else if (message.startsWith('ShowNativeLoadingModal_') && !connectedToSession) {
        var parsedFileArr = message.split('_');
        parsedFileArr.shift();
        var filename = parsedFileArr.join('_');
        var showNativeLoading = ui.openNativeLoadingModal(filename, resumeBackendLoading.bind(vsClient));
        if (!showNativeLoading) {
            resumeBackendLoading(filename);
        }
    }
    else if (message.startsWith('VisShareBlockUI')) {
        var method = message.split('_')[1];
        vsClient.blockUI();
        vsClient.once(method, function (res) {
            vsClient.unblockUI();
        });
    }
});
function resumeBackendLoading(fileName) {
    vsClient.relayMessageToBackend('NativeLoadingModalFinished');
}
function interceptQueryString(isBackend) {
    var queryString = window.location.search.substr(1);
    var kvs = queryString.split('&');
    var query = {};
    for (var _i = 0, kvs_3 = kvs; _i < kvs_3.length; _i++) {
        var kv = kvs_3[_i];
        var _a = kv.split('='), k = _a[0], v = _a[1];
        query[k] = v;
    }
    var file1, file2;
    if (query['closesessions']) {
        app_states_1.appStates.sessionUrlClose = query['closesessions'];
        localStorage.removeItem('WVSessionCloseID');
        localStorage.setItem('WVSessionCloseID', query['closesessions']);
        window.addEventListener('storage', function (e) {
            var urlCloseCommand = localStorage.getItem('WVSessionCloseID');
            if (e.key === 'WVSessionCloseID' && urlCloseCommand && urlCloseCommand !== null && urlCloseCommand !== '' && app_states_1.appStates.sessionUrlClose === urlCloseCommand) {
                vsClient.destroy();
                var url = location.href.split('/');
                url.pop();
                window.open(url.join('/') + '/session_in_use_index.html', '_self');
            }
        });
    }
    if (query['compare'] === '2d') {
        if (query['file1']) {
            file1 = decodeURIComponent(query['file1']);
        }
        if (query['file2']) {
            file2 = decodeURIComponent(query['file2']);
        }
        if (query['open1']) {
            file1 = decodeURIComponent(escape(window.atob(query['open1'])));
        }
        if (query['open2']) {
            file2 = decodeURIComponent(escape(window.atob(query['open2'])));
        }
        if (file1 && file2) {
            vsClient.OpenFile(file1);
            vsClient.on('ActivateGraphicalCompare', function (ev) {
                if (ev.CustomId == 'full_ui_2d_compare') {
                    if (!(ev.Error == "SUCCESS")) {
                        alert("Error on 2D compare: " + ev.Error + " Info: " + ev.ErrorInfo);
                        console.log("Error on 2D compare: " + ev.Error + " Info: " + ev.ErrorInfo);
                    }
                }
            });
            vsClient.on('OpenFile', function () {
                var XML = "\n                <Call Method=\"ImportFile\">\n                <FileName>" + file2 + "</FileName>\n              </Call>";
                vsClient.command.executeXML(XML);
                var XML2 = "\n                <Call Method=\"ActivateGraphicalCompare\"><Activate>true</Activate> CustomId=\"full_ui_2d_compare\"</Call>";
                vsClient.command.executeXML(XML2);
            });
        }
    }
    else if (query['compare'] === 'quick') {
        compareByURI(query, 'full_ui_quick_compare', 'ActivateGraphicalCompare');
    }
    else if (query['compare'] === 'exact') {
        compareByURI(query, 'full_ui_exact_compare', 'ComputeBrepCompare');
    }
    else if (typeof query['file'] !== 'undefined') {
        var showModal = query['NativeLoadingModal'];
        var filename = decodeURIComponent(query['file']);
        if (showModal && showModal === 'true') {
            ui.openNativeLoadingModal(filename, vsClient.OpenFile.bind(vsClient));
        }
        else {
            vsClient.OpenFile(filename);
        }
    }
    else if (typeof query['open'] !== 'undefined') {
        var filename = decodeURIComponent(escape(window.atob(query['open'])));
        var showModal = query['NativeLoadingModal'];
        if (showModal && showModal === 'true') {
            ui.openNativeLoadingModal(filename, vsClient.OpenFile.bind(vsClient));
        }
        else {
            vsClient.OpenFile(filename);
        }
    }
}
function compareByURI(query, customId, compareMethod) {
    var file1, file2;
    if (query['file1']) {
        file1 = decodeURIComponent(query['file1']);
    }
    if (query['file2']) {
        file2 = decodeURIComponent(query['file2']);
    }
    if (query['open1']) {
        file1 = decodeURIComponent(escape(window.atob(query['open1'])));
    }
    if (query['open2']) {
        file2 = decodeURIComponent(escape(window.atob(query['open2'])));
    }
    if (file1 && file2) {
        if (vsClient.initOptions.blockOnLoad) {
            vsClient.blockUI();
        }
        vsClient.once(compareMethod, function (ev) {
            vsClient.unblockUI();
            if (ev.CustomId === customId) {
                if (ev.Error != "SUCCESS") {
                    if (compareMethod === 'ComputeBrepCompare') {
                        console.log("Error on exact compare: " + ev.ErrorInfo + " Try quick compare.");
                        compareByURI(query, 'full_ui_quick_compare', 'ActivateGraphicalCompare');
                    }
                    else {
                        console.log("Error on quick compare: " + ev.Error + " Info: " + ev.ErrorInfo);
                    }
                }
                else {
                    handleCompare(ev);
                }
            }
        });
        vsClient.NewScene3D();
        vsClient.once('NewScene3D', function (response) {
            handleNewSceneForCompare(response, file1, file2, customId, compareMethod);
        });
    }
}
function handleCompare(ev) {
    if (ev.Error == 'SUCCESS') {
        var xml_5 = "<Call Method=\"AlignView\"><Alignment>Isometric</Alignment></Call><Call Method=\"ModifySelection\"><SelectionModifier>Deselect</SelectionModifier></Call>";
        vsClient.command.executeXML(xml_5);
    }
    var xml = "<Call Method=\"ResumeRendering\"/>";
    vsClient.command.executeXML(xml);
}
function handleNewSceneForCompare(response, file1, file2, customId, compareMethod) {
    openFileHandler(response);
    var xml = "\n    <Call Method=\"SetSettings\">\n        <Settings>\n            <Import3D>\n                <General>\n                    <ReadMode>PreferGeometry</ReadMode>\n                    <ReadPMIs>false</ReadPMIs>\n                    <ReadSolids>true</ReadSolids>\n                    <ReadConstructionAndReferences>false</ReadConstructionAndReferences>\n                </General>\n                <_3DVS>\n                    <IncrementalLoading>false</IncrementalLoading>\n                    <RemoveBrep>false</RemoveBrep>\n                </_3DVS>\n            </Import3D>\n        </Settings>\n    </Call>\n\n    <Call Method=\"PauseRendering\"/>\n\n    <Call Method=\"UpdateScene\">\n        <VSXML>\n            <Node CustomId=\"Root1\" Name=\"Compare1\" Type=\"Root\">\n                <Node CustomId=\"V1\" Name=\"V1\" Type=\"ProductOccurrence\" FileName=\"" + file1 + "\" />\n            </Node>\n            <Node CustomId=\"Root2\" Name=\"Compare2\" Type=\"Root\">\n                <Node CustomId=\"V2\" Name=\"V2\" Type=\"ProductOccurrence\" FileName=\"" + file2 + "\" />\n            </Node>\n        </VSXML>\n    </Call>\n\n    <Call Method=\"FitAll\"/>\n\n    <Call Method=\"SetSelectedNodes\">\n    <NodeId>#V1</NodeId>\n    <NodeId>#V2</NodeId>\n    </Call>\n\n        ";
    var fulLCompareCall = '<Call Method="ComputeBrepCompare" CustomId="full_ui_exact_compare"></Call>';
    var quickCompareCall = '<Call Method="ActivateGraphicalCompare" CustomId="full_ui_quick_compare"><Activate>true</Activate></Call>';
    xml += compareMethod === 'ComputeBrepCompare' ? fulLCompareCall : quickCompareCall;
    vsClient.command.executeXML(xml);
}


/***/ }),
/* 30 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
var events_1 = __webpack_require__(6);
var tooltip_1 = __webpack_require__(41);
var app_settings_1 = __webpack_require__(4);
var tinycolor = __webpack_require__(10);
var SVG_NS = 'http://www.w3.org/2000/svg';
var XLINK_NS = 'http://www.w3.org/1999/xlink';
var paletteColorsRGBA = [
    ['rgba(0,0,0,1)', 'rgba(128,0,0,1)', 'rgba(0,128,0,1)', 'rgba(128,128,0,1)', 'rgba(0,0,128,1)'],
    ['rgba(128,0,128,1)', 'rgba(0,128,128,1)', 'rgba(192,192,192,1)', 'rgba(192,220,192,1)', 'rgba(166,202,240,1)'],
    ['rgba(255,251,240,1)', 'rgba(160,160,164,1)', 'rgba(128,128,128,1)', 'rgba(255,0,0,1)', 'rgba(0,255,0,1)'],
    ['rgba(255,255,0,1)', 'rgba(0,0,255,1)', 'rgba(255,0,255,1)', 'rgba(0,255,255,1)', 'rgba(255,255,255,1)']
];
var paletteColorsRGB = [
    ['rgb(0,0,0)', 'rgb(128,0,0)', 'rgb(0,128,0)', 'rgb(128,128,0)', 'rgb(0,0,128)'],
    ['rgb(128,0,128)', 'rgb(0,128,128)', 'rgb(192,192,192)', 'rgb(192,220,192)', 'rgb(166,202,240)'],
    ['rgb(255,251,240)', 'rgb(160,160,164)', 'rgb(128,128,128)', 'rgb(255,0,0)', 'rgb(0,255,0)'],
    ['rgb(255,255,0)', 'rgb(0,0,255)', 'rgb(255,0,255)', 'rgb(0,255,255)', 'rgb(255,255,255)']
];
var rgbPalette;
var rgbaPalette;
try {
    rgbPalette = _ColorPalette;
}
catch (e) {
    rgbPalette = paletteColorsRGB;
}
try {
    rgbaPalette = _ColorPaletteWithAlpha;
}
catch (e) {
    rgbaPalette = paletteColorsRGBA;
}
var CustomPalette = (function (_super) {
    __extends(CustomPalette, _super);
    function CustomPalette(activeColor, showAlpha) {
        var _this = _super.call(this) || this;
        _this.nodes = [];
        _this.close = function (ev) {
            if (ev.target !== _this.mount || !_this.mount.contains(ev.target)) {
                _this.mount.style.display = 'none';
                $(_this.pixelBox).remove();
                document.body.removeChild(_this.mount);
                document.removeEventListener('click', _this.close);
            }
        };
        _this.pickPixel = function (e) {
            var ctx = this.canvas.getContext('2d');
            var x = e.pageX - this.canvas.getBoundingClientRect().left;
            var y = e.pageY - this.canvas.getBoundingClientRect().top;
            this.pixelBox.style.top = (e.pageY + 22) + 'px';
            this.pixelBox.style.left = (e.pageX + 22) + 'px';
            var imageData = ctx.getImageData(x, y, 1, 1);
            var pixel = imageData.data;
            this.colorPrevField.style.backgroundColor = 'rgb(' + pixel[0] + ', ' + pixel[1] + ', ' + pixel[2] + ')';
            this.colorPrevText.textContent = 'rgb(' + pixel[0] + ', ' + pixel[1] + ', ' + pixel[2] + ')';
        };
        _this.selectPixelColor = function (e) {
            e.preventDefault();
            e.stopPropagation();
            $(this.pixelBox).hide();
            this.canvas.removeEventListener('mousemove', this.pickPixel);
            this.canvas.removeEventListener('mousedown', this.selectPixelColor);
            var rgba = this.colorPrevText.textContent.replace('rgb', 'rgba').replace(')', ' ,1)');
            this.emit('customColorChosen', rgba);
            $(this.spectrumEl).spectrum('destroy');
        };
        _this.mount = document.createElement('div');
        _this.mount.style.position = 'absolute';
        _this.activeColor = activeColor;
        _this.showAlpha = showAlpha;
        _this.pixelBox = document.createElement('div');
        _this.pixelBox.className = 'color-pipette-preview-wrapper';
        _this.colorPrevField = document.createElement('span');
        _this.colorPrevField.className = 'color-pipette-preview-field';
        _this.colorPrevText = document.createElement('span');
        _this.colorPrevText.className = 'color-pipette-preview-text';
        _this.pixelBox.appendChild(_this.colorPrevField);
        _this.pixelBox.appendChild(_this.colorPrevText);
        document.body.appendChild(_this.pixelBox);
        $(_this.pixelBox).hide();
        document.body.appendChild(_this.mount);
        setTimeout(function () {
            document.addEventListener('click', _this.close);
        });
        _this.spectrumEl = document.createElement('div');
        _this.spectrumEl.id = 'spectrum-picker-el';
        _this.mount.appendChild(_this.spectrumEl);
        var $spEl = $(_this.spectrumEl);
        $spEl.attr("alpha", _this.showAlpha);
        $(_this.spectrumEl).spectrum({
            flat: true,
            color: _this.activeColor,
            showAlpha: _this.showAlpha,
            showInput: true,
            showInitial: true,
            showPalette: true,
            showSelectionPalette: true,
            maxSelectionSize: 40,
            preferredFormat: "rgb",
            localStorageKey: 'customColorStorage',
            palette: _this.showAlpha ? rgbaPalette : rgbPalette,
            show: function (color) {
                if ($('.sp-alpha-enabled').length > 0) {
                    $('.color-palette-pipette-picker-wrapper').css('top', '274px');
                }
                if ($(this).attr("alpha") === 'false') {
                    $('.sp-input').val(computeRgb(color));
                    $('#sp-hex-input').val(computeHex(color));
                    $('.sp-input').on('keyup', function (event) {
                        if (event.keyCode === 13) {
                            $(this).val(computeRgb(this.value));
                            $('#sp-hex-input').val(computeHex(this.value));
                        }
                    });
                    $('.sp-input').blur(function (event) {
                        $(this).val(computeRgb(this.value));
                        $('#sp-hex-input').val(computeHex(this.value));
                    });
                }
                else {
                    $('#sp-hex-input').val('#' + computeHexWithAlpha(color));
                    $('.sp-input').on('keyup', function (event) {
                        if (event.keyCode === 13) {
                            $('#sp-hex-input').val(computeHexWithAlpha(this.value));
                        }
                    });
                    $('.sp-input').blur(function (event) {
                        $('#sp-hex-input').val(computeHexWithAlpha(this.value));
                    });
                }
            },
            move: function (color) {
                if ($(this).attr("alpha") === 'false') {
                    $('.sp-input').val(computeRgb(color));
                    $('#sp-hex-input').val(computeHex(color));
                }
                else {
                    $('#sp-hex-input').val(computeHexWithAlpha(color));
                }
            },
            change: function (color) {
                var rgbColor;
                if (_this.showAlpha) {
                    rgbColor = computeRgb(color);
                }
                else {
                    rgbColor = computeRgbWithAlpha(color);
                }
                _this.chosenColor = rgbColor;
            }
        });
        var viewportWidth = Math.min(window.innerWidth, document.documentElement.offsetWidth);
        var viewportHeight = Math.max(window.innerHeight, document.documentElement.offsetHeight);
        var width = 280;
        var height = 300;
        _this.mount.style.top = 0.5 * (viewportHeight - height) + 'px';
        _this.mount.style.left = 0.5 * (viewportWidth - width) + 'px';
        _this.mount.className = 'wv-color-picker-popup';
        var $mount = $(_this.mount);
        _this.hexInputEl = document.createElement('input');
        _this.hexInputEl.id = 'sp-hex-input';
        _this.hexInputEl.className = 'sp-input';
        _this.hexInputEl.style.marginTop = '5px';
        $(_this.hexInputEl).attr("alpha", _this.showAlpha);
        if (_this.showAlpha) {
            $(_this.hexInputEl).val(computeHexWithAlpha(_this.activeColor));
        }
        else {
            $(_this.hexInputEl).val(computeHex(_this.activeColor));
        }
        var pipette = document.createElement('div');
        pipette.className = 'color-palette-pipette-picker-wrapper';
        if (_this.showAlpha) {
            pipette.style.top = '274px';
        }
        var pipetteWrapper = document.createElement('span');
        pipetteWrapper.className = 'color-palette-pipette-picker-icon';
        var pipetteToolTip = new tooltip_1.WVTooltip(pipetteWrapper);
        pipetteToolTip.setTitle(_this.translateTooltip('ID_COLOR_PICKER_PALETTE_CHOOSE_FROM_PART'));
        pipetteToolTip.setOrientation('bottom');
        var pipetteIcon = document.createElementNS(SVG_NS, 'svg');
        pipetteIcon.style.width = '25px';
        pipetteIcon.style.height = '25px';
        var pipetteUseIcon = document.createElementNS(SVG_NS, 'use');
        pipetteUseIcon.setAttributeNS(XLINK_NS, 'href', '#10_auswahl_durch_farbe');
        pipetteIcon.appendChild(pipetteUseIcon);
        pipetteWrapper.appendChild(pipetteIcon);
        var paletteWrapper = document.createElement('span');
        paletteWrapper.className = 'color-palette-pipette-picker-icon';
        paletteWrapper.style.marginLeft = '5px';
        var paletteTooltip = new tooltip_1.WVTooltip(paletteWrapper);
        paletteTooltip.setTitle(_this.translateTooltip('ID_COLOR_PICKER_PALETTE_CHOOSE_FROM_PIXEL'));
        paletteTooltip.setOrientation('bottom');
        var paletteIcon = document.createElementNS(SVG_NS, 'svg');
        paletteIcon.style.width = '25px';
        paletteIcon.style.height = '25px';
        paletteIcon.style.position = 'relative';
        paletteIcon.style.top = '2px';
        var paletteUseIcon = document.createElementNS(SVG_NS, 'use');
        paletteUseIcon.setAttributeNS(XLINK_NS, 'href', '#palette_pipette');
        paletteIcon.appendChild(paletteUseIcon);
        paletteWrapper.appendChild(paletteIcon);
        pipette.appendChild(pipetteWrapper);
        pipette.appendChild(paletteWrapper);
        _this.pickPixel = _this.pickPixel.bind(_this);
        _this.selectPixelColor = _this.selectPixelColor.bind(_this);
        paletteIcon.onclick = function (e) {
            e.preventDefault();
            e.stopPropagation();
            _this.canvas = vsClient.vsCanvas.canvas2D;
            $(_this.pixelBox).show();
            $(_this.mount).hide();
            _this.canvas.addEventListener('mousemove', _this.pickPixel);
            _this.canvas.addEventListener('mousedown', _this.selectPixelColor);
        };
        pipetteIcon.onclick = function (e) {
            e.preventDefault();
            e.stopPropagation();
            $(_this.mount).hide();
            var xml = '<Call Method="GetSelectedNodes"></Call>';
            vsClient.command.executeXML(xml);
            vsClient.once('GetSelectedNodes', function (nodeRes) {
                if (Array.isArray(nodeRes.NodeId)) {
                    _this.nodes = nodeRes.NodeId;
                }
                else {
                    _this.nodes = [nodeRes.NodeId];
                }
                vsClient.once('NodePropertiesChanged', function (res) {
                    if (res.General) {
                        var rgba_1 = computeRgbWithAlpha(res.General.Color);
                        var oldSelectionCall = '<Call Method="SetSelectedNodes">';
                        var selectedNodesLength = _this.nodes.length;
                        for (var i = 0; i < selectedNodesLength; i++) {
                            oldSelectionCall += '<NodeId>' + _this.nodes[i] + '</NodeId>';
                        }
                        oldSelectionCall += '</Call>';
                        vsClient.command.executeXML(oldSelectionCall);
                        vsClient.once('SetSelectedNodes', function () {
                            _this.emit('customColorChosen', rgba_1);
                            $(_this.spectrumEl).spectrum('destroy');
                            vsClient.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Deselect</SelectionModifier></Call>');
                        });
                    }
                    else {
                        vsClient.once('NodePropertiesChanged', function (res) {
                            if (res.General) {
                                var rgba_2 = computeRgbWithAlpha(res.General.Color);
                                var oldSelectionCall = '<Call Method="SetSelectedNodes">' + _this.nodes.toString() + '</Call>';
                                vsClient.command.executeXML(oldSelectionCall);
                                vsClient.once('SetSelectedNodes', function () {
                                    _this.emit('customColorChosen', rgba_2);
                                    $(_this.spectrumEl).spectrum('destroy');
                                    vsClient.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Deselect</SelectionModifier></Call>');
                                });
                            }
                            else {
                                $(_this.spectrumEl).spectrum('destroy');
                                vsClient.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Deselect</SelectionModifier></Call>');
                            }
                        });
                    }
                });
            });
        };
        $('.sp-input-container').append(_this.hexInputEl);
        $('.sp-picker-container').append(pipette);
        $(_this.hexInputEl).blur(function (event) {
            if ($(this).attr("alpha") === 'false') {
                $('.sp-input').val(computeRgb(this.value));
                $(this).val(computeHex(this.value));
            }
            else {
                $('.sp-input').val(computeRgbWithAlpha(this.value));
                $(this).val(computeHexWithAlpha(this.value));
            }
        });
        $(_this.hexInputEl).on('keyup', function (event) {
            if (event.keyCode === 13) {
                $('#spectrum-picker-el').spectrum('set', this.value);
                if ($(this).attr("alpha") === 'false') {
                    $('.sp-input').val(computeRgb(this.value));
                    $(this).val(computeHex(this.value));
                }
                else {
                    $('.sp-input').val(computeRgbWithAlpha(this.value));
                    $(this).val(computeHexWithAlpha(this.value));
                }
            }
        });
        $mount.find('.sp-cancel').on('click', function (e) {
            $(_this.pixelBox).remove();
            $spEl.spectrum('destroy');
        });
        $mount.find('.sp-choose').on('click', function (e) {
            $(_this.pixelBox).remove();
            var colorValue = tinycolor($spEl.spectrum('get'));
            var alpha = colorValue.getAlpha();
            colorValue.setAlpha(alpha === 1 ? 1.0 : alpha);
            var RGBString = _this.showAlpha ? computeRgbWithAlpha(colorValue._originalInput) : computeRgb(colorValue._originalInput);
            _this.emit('customColorChosen', RGBString);
            $spEl.spectrum('destroy');
        });
        function computeHexWithAlpha(color) {
            var colorObj;
            if (color._originalInput) {
                colorObj = tinycolor(color._originalInput);
            }
            else {
                colorObj = tinycolor(color);
            }
            return '#' + (Math.round(colorObj.toRgb().a * 255)).toString(16).toUpperCase() + colorObj.toHex().toUpperCase();
        }
        function computeHex(color) {
            var colorObj;
            if (color._originalInput) {
                colorObj = tinycolor(color._originalInput);
            }
            else {
                colorObj = tinycolor(color);
            }
            return '#' + colorObj.toHex().toUpperCase();
        }
        function computeRgb(color) {
            var rgb;
            if (color._originalInput) {
                rgb = tinycolor(color._originalInput).toRgb();
            }
            else {
                rgb = tinycolor(color).toRgb();
            }
            return 'rgb(' + rgb.r + ', ' + rgb.g + ', ' + rgb.b + ')';
        }
        function computeRgbWithAlpha(color) {
            var rgb;
            var alpha;
            if (color._originalInput) {
                rgb = tinycolor(color._originalInput).toRgb();
            }
            else {
                alpha = color.slice(1, 3);
                rgb = tinycolor('#' + color.slice(3) + alpha).toRgb();
            }
            return 'rgba(' + rgb.r + ', ' + rgb.g + ', ' + rgb.b + ', ' + rgb.a + ')';
        }
        return _this;
    }
    CustomPalette.prototype.translateTooltip = function (langId) {
        var tooltip;
        var lang = app_settings_1.settings.lang;
        if (_lang_data[langId] && _lang_data[langId][lang]) {
            tooltip = _lang_data[langId][lang];
        }
        else if (_custom_lang_data && _custom_lang_data[langId] && _custom_lang_data[langId][lang]) {
            tooltip = _custom_lang_data[langId][lang];
        }
        else if (_lang_data[langId] && _lang_data[langId]['ENU']) {
            tooltip = _lang_data[langId]['ENU'];
        }
        else if (_custom_lang_data && _custom_lang_data[langId] && _custom_lang_data[langId]['ENU']) {
            tooltip = _custom_lang_data[langId]['ENU'];
        }
        else {
            tooltip = langId;
        }
        return tooltip;
    };
    return CustomPalette;
}(events_1.EventEmitter));
exports.CustomPalette = CustomPalette;


/***/ }),
/* 31 */
/***/ (function(module, exports) {

var g;

// This works in non-strict mode
g = (function() {
	return this;
})();

try {
	// This works if eval is allowed (see CSP)
	g = g || Function("return this")() || (1,eval)("this");
} catch(e) {
	// This works if the window reference is available
	if(typeof window === "object")
		g = window;
}

// g can still be undefined, but nothing to do about it...
// We return undefined, instead of nothing here, so it's
// easier to handle this case. if(!global) { ...}

module.exports = g;


/***/ }),
/* 32 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var deselectCurrent = __webpack_require__(39);

var defaultMessage = 'Copy to clipboard: #{key}, Enter';

function format(message) {
  var copyKey = (/mac os x/i.test(navigator.userAgent) ? '⌘' : 'Ctrl') + '+C';
  return message.replace(/#{\s*key\s*}/g, copyKey);
}

function copy(text, options) {
  var debug, message, reselectPrevious, range, selection, mark, success = false;
  if (!options) { options = {}; }
  debug = options.debug || false;
  try {
    reselectPrevious = deselectCurrent();

    range = document.createRange();
    selection = document.getSelection();

    mark = document.createElement('span');
    mark.textContent = text;
    // reset user styles for span element
    mark.style.all = 'unset';
    // prevents scrolling to the end of the page
    mark.style.position = 'fixed';
    mark.style.top = 0;
    mark.style.clip = 'rect(0, 0, 0, 0)';
    // used to preserve spaces and line breaks
    mark.style.whiteSpace = 'pre';
    // do not inherit user-select (it may be `none`)
    mark.style.webkitUserSelect = 'text';
    mark.style.MozUserSelect = 'text';
    mark.style.msUserSelect = 'text';
    mark.style.userSelect = 'text';

    document.body.appendChild(mark);

    range.selectNode(mark);
    selection.addRange(range);

    var successful = document.execCommand('copy');
    if (!successful) {
      throw new Error('copy command was unsuccessful');
    }
    success = true;
  } catch (err) {
    debug && console.error('unable to copy using execCommand: ', err);
    debug && console.warn('trying IE specific stuff');
    try {
      window.clipboardData.setData('text', text);
      success = true;
    } catch (err) {
      debug && console.error('unable to copy using clipboardData: ', err);
      debug && console.error('falling back to prompt');
      message = format('message' in options ? options.message : defaultMessage);
      window.prompt(message, text);
    }
  } finally {
    if (selection) {
      if (typeof selection.removeRange == 'function') {
        selection.removeRange(range);
      } else {
        selection.removeAllRanges();
      }
    }

    if (mark) {
      document.body.removeChild(mark);
    }
    reselectPrevious();
  }

  return success;
}

module.exports = copy;


/***/ }),
/* 33 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var utils_1 = __webpack_require__(17);
/**
 * @private
 */
var defaults = {
    aliasString: "=",
    attributeString: "@",
    cdataInvalidChars: false,
    cdataKeys: [],
    declaration: {
        include: true
    },
    dtd: {
        include: false
    },
    format: {},
    typeHandlers: {},
    valueString: "#",
    wrapHandlers: {}
};
Object.freeze(defaults);
/**
 * Validates the cdataKeys property of an options object.
 *
 * @param {string[]} cdataKeys The cdataKeys object.
 *
 * @returns {string[]} The updated cdataKeys object.
 *
 * @private
 */
function validateCdataKeys(cdataKeys) {
    for (var _i = 0, cdataKeys_1 = cdataKeys; _i < cdataKeys_1.length; _i++) {
        var key = cdataKeys_1[_i];
        if (!utils_1.isType(key, "String")) {
            throw new TypeError(key + " should be a string");
        }
    }
    return cdataKeys;
}
/**
 * Validates the declaration property of an options object.
 *
 * @param {IDeclarationOptions} declaration The declaration object.
 *
 * @returns {IDeclarationOptions} The updated declaration object.
 *
 * @private
 */
function validateDecl(declaration) {
    if (!utils_1.isType(declaration.include, "Boolean", "Undefined")) {
        throw new TypeError("declaration.include should be a string or" +
            " undefined");
    }
    if (!utils_1.isType(declaration.include, "Boolean")) {
        declaration.include = defaults.declaration.include;
    }
    return declaration;
}
/**
 * Validates the dtd property of an options object.
 *
 * @param {IDtdOptions} dtd The dtd object.
 *
 * @returns {IDtdOptions} The updated dtd object.
 *
 * @private
 */
function validateDtd(dtd) {
    if (!utils_1.isType(dtd.include, "Boolean", "Undefined")) {
        throw new TypeError("dtdOptions.include should be a string or" +
            " undefined");
    }
    if (!utils_1.isType(dtd.include, "Boolean")) {
        dtd.include = defaults.dtd.include;
    }
    return dtd;
}
/**
 * Validates the typeHandlers property of an options object.
 *
 * @param {ITypeHandlers} typeHandlers The typeHandlers object.
 *
 * @returns {ITypeHandlers} The updated typeHandlers object.
 *
 * @private
 */
function validateTypeHandlers(typeHandlers) {
    for (var key in typeHandlers) {
        if (typeHandlers.hasOwnProperty(key)) {
            if (!utils_1.isType(typeHandlers[key], "Function")) {
                throw new TypeError("options.typeHandlers['" + key + "']" +
                    " should be a Function");
            }
        }
    }
    return typeHandlers;
}
/**
 * Validates the wrapHandlers property of an options object.
 *
 * @param {IWrapHandlers} wrapHandlers The wrapHandlers object.
 *
 * @return {IWrapHandlers} The updated wrapHandlers object.
 *
 * @private
 */
function validateWrapHandlers(wrapHandlers) {
    for (var key in wrapHandlers) {
        if (wrapHandlers.hasOwnProperty(key)) {
            if (!utils_1.isType(wrapHandlers[key], "Function")) {
                throw new TypeError("options.wrapHandlers"
                    + "['" + key + "'] should be a Function");
            }
        }
    }
    return wrapHandlers;
}
/**
 * Validates an options object and replaces undefined values with their
 * appropriate defaults.
 *
 * @param {IOptions} options The options object to validate.
 *
 * @returns {IOptions} The updated options object.
 *
 * @private
 */
function validateOptions(options) {
    if (!utils_1.isType(options.aliasString, "String", "Undefined")) {
        throw new TypeError("options.aliasString should be a string or"
            + " undefined");
    }
    if (!utils_1.isType(options.aliasString, "String")) {
        options.aliasString = defaults.aliasString;
    }
    if (!utils_1.isType(options.attributeString, "String", "Undefined")) {
        throw new TypeError("options.attributeString should be a string or" +
            " undefined");
    }
    if (!utils_1.isType(options.attributeString, "String")) {
        options.attributeString = defaults.attributeString;
    }
    if (!utils_1.isType(options.cdataInvalidChars, "Boolean", "Undefined")) {
        throw new TypeError("options.cdataInvalidChars should be a boolean or"
            + " undefined");
    }
    if (!utils_1.isType(options.cdataInvalidChars, "Boolean")) {
        options.cdataInvalidChars = defaults.cdataInvalidChars;
    }
    if (!utils_1.isType(options.cdataKeys, "Array", "Undefined")) {
        throw new TypeError("options.cdataKeys should be an Array or" +
            " undefined");
    }
    if (!utils_1.isType(options.cdataKeys, "Array")) {
        options.cdataKeys = defaults.cdataKeys;
    }
    options.cdataKeys = validateCdataKeys(options.cdataKeys);
    if (!utils_1.isType(options.declaration, "Object", "Undefined")) {
        throw new TypeError("options.declaration should be an Object or"
            + " undefined");
    }
    if (!utils_1.isType(options.declaration, "Object")) {
        options.declaration = defaults.declaration;
    }
    options.declaration = validateDecl(options.declaration);
    if (!utils_1.isType(options.dtd, "Object", "Undefined")) {
        throw new TypeError("options.dtd should be an Object or undefined");
    }
    if (!utils_1.isType(options.dtd, "Object")) {
        options.dtd = defaults.dtd;
    }
    options.dtd = validateDtd(options.dtd);
    if (!utils_1.isType(options.format, "Object", "Undefined")) {
        throw new TypeError("options.format should be an Object or undefined");
    }
    if (!utils_1.isType(options.format, "Object")) {
        options.format = defaults.format;
    }
    if (!utils_1.isType(options.typeHandlers, "Object", "Undefined")) {
        throw new TypeError("options.typeHandlers should be an Object or" +
            " undefined");
    }
    if (!utils_1.isType(options.typeHandlers, "Object")) {
        options.typeHandlers = defaults.typeHandlers;
    }
    options.typeHandlers = validateTypeHandlers(options.typeHandlers);
    if (!utils_1.isType(options.valueString, "String", "Undefined")) {
        throw new TypeError("options.valueString should be a string or" +
            " undefined");
    }
    if (!utils_1.isType(options.valueString, "String")) {
        options.valueString = defaults.valueString;
    }
    if (!utils_1.isType(options.wrapHandlers, "Object", "Undefined")) {
        throw new TypeError("options.wrapHandlers should be an"
            + " Object or undefined");
    }
    if (!utils_1.isType(options.wrapHandlers, "Object")) {
        options.wrapHandlers = defaults.wrapHandlers;
    }
    options.wrapHandlers = validateWrapHandlers(options.wrapHandlers);
    return options;
}
exports.validateOptions = validateOptions;


/***/ }),
/* 34 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var XmlAttribute_1 = __webpack_require__(19);
exports.XmlAttribute = XmlAttribute_1.default;
var XmlCdata_1 = __webpack_require__(20);
exports.XmlCdata = XmlCdata_1.default;
var XmlCharRef_1 = __webpack_require__(12);
exports.XmlCharRef = XmlCharRef_1.default;
var XmlComment_1 = __webpack_require__(8);
exports.XmlComment = XmlComment_1.default;
var XmlDecl_1 = __webpack_require__(21);
exports.XmlDecl = XmlDecl_1.default;
var XmlDocument_1 = __webpack_require__(35);
exports.XmlDocument = XmlDocument_1.default;
var XmlDtd_1 = __webpack_require__(22);
exports.XmlDtd = XmlDtd_1.default;
var XmlDtdAttlist_1 = __webpack_require__(23);
exports.XmlDtdAttlist = XmlDtdAttlist_1.default;
var XmlDtdElement_1 = __webpack_require__(24);
exports.XmlDtdElement = XmlDtdElement_1.default;
var XmlDtdEntity_1 = __webpack_require__(25);
exports.XmlDtdEntity = XmlDtdEntity_1.default;
var XmlDtdNotation_1 = __webpack_require__(26);
exports.XmlDtdNotation = XmlDtdNotation_1.default;
var XmlDtdParamEntityRef_1 = __webpack_require__(27);
exports.XmlDtdParamEntityRef = XmlDtdParamEntityRef_1.default;
var XmlElement_1 = __webpack_require__(28);
exports.XmlElement = XmlElement_1.default;
var XmlEntityRef_1 = __webpack_require__(13);
exports.XmlEntityRef = XmlEntityRef_1.default;
var XmlNode_1 = __webpack_require__(2);
exports.XmlNode = XmlNode_1.default;
var XmlProcInst_1 = __webpack_require__(9);
exports.XmlProcInst = XmlProcInst_1.default;
var XmlText_1 = __webpack_require__(14);
exports.XmlText = XmlText_1.default;
/**
 * Creates a new XML document.
 *
 * @param {string} root The name of the root element of the document.
 *
 * @returns {XmlDocument} The new XML document.
 */
function document(root) {
    return new XmlDocument_1.default(root);
}
exports.document = document;


/***/ }),
/* 35 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (C) 2016 Michael Kourlas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var options_1 = __webpack_require__(7);
var utils_1 = __webpack_require__(1);
var XmlComment_1 = __webpack_require__(8);
var XmlDecl_1 = __webpack_require__(21);
var XmlDtd_1 = __webpack_require__(22);
var XmlElement_1 = __webpack_require__(28);
var XmlNode_1 = __webpack_require__(2);
var XmlProcInst_1 = __webpack_require__(9);
/**
 * Represents an XML document.
 *
 * A sample XML document is structured as follows:
 *
 * ```xml
 * <?xml version="1.0" encoding="UTF-8"?>
 * <DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 *                      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
 * <html>
 *     <head>
 *         <title>My page title</title>
 *     </head>
 *     <body>
 *         <h1>Welcome!</h1>
 *         <p>I hope you enjoy visiting my website.</p>
 *         <img src="picture.png"/>
 *     </body>
 * </html>
 * ```
 *
 * Each component of the document, such as the XML declaration, document type
 * definition, and root element, are children of this node.
 *
 * XmlDocument nodes must have exactly one {@link XmlElement} child, which is
 * the document's root element.
 *
 * XmlDocument nodes can have exactly one {@link XmlDecl} and {@link XmlDtd}
 * child in that order, so long as they precede the {@link XmlElement} node.
 *
 * XmlDocument nodes can have an unlimited number of {@link XmlComment} or
 * {@link XmlProcInst} nodes, so long as they follow the {@link XmlDecl} node,
 * if one exists.
 */
var XmlDocument = (function (_super) {
    __extends(XmlDocument, _super);
    /**
     * Initializes a new instance of the {@link XmlDocument} class.
     *
     * @param {string} root The name of the root element.
     */
    function XmlDocument(root) {
        _super.call(this);
        _super.prototype.insertChild.call(this, new XmlElement_1.default(root));
    }
    /**
     * Inserts a new comment at the specified index. If no index is specified,
     * the node is inserted at the end of this node's children.
     *
     * @param {string} content The data of the comment.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         at the end of this node's children.
     *
     * @returns {XmlComment} The newly created element.
     */
    XmlDocument.prototype.comment = function (content, index) {
        var comment = new XmlComment_1.default(content);
        this.insertChild(comment, index);
        return comment;
    };
    /**
     * Inserts a new XML declaration at the beginning of this node's children.
     *
     * @param {IDeclarationOptions} [options] The options associated with the
     *                                       XML declaration.
     *
     * @returns {XmlDecl} The newly created XML declaration.
     */
    XmlDocument.prototype.decl = function (options) {
        var declaration = new XmlDecl_1.default(options);
        this.insertChild(declaration, 0);
        return declaration;
    };
    /**
     * Inserts a new XML document type definition. Unless a different index is
     * specified, the node is inserted immediately after the XML declaration
     * if one exists, or at the beginning of this node's children if one does
     * not.
     *
     * @param {string} name    The name of the DTD.
     * @param {string} [sysId] The system identifier of the DTD, excluding
     *                         quotation marks.
     * @param {string} [pubId] The public identifier of the DTD, excluding
     *                         quotation marks. If a public identifier is
     *                         provided, a system identifier must be provided
     *                         as well.
     * @param {number} [index] The index at which the node should be inserted.
     *                         If no index is specified, the node is inserted
     *                         immediately after the XML declaration if one
     *                         exists, or at the beginning of this node's
     *                         children if one does not.
     *
     * @returns {XmlDtd} The newly created XML document type definition.
     */
    XmlDocument.prototype.dtd = function (name, sysId, pubId, index) {
        var dtd = new XmlDtd_1.default(name, sysId, pubId);
        if (utils_1.isType(index, "Undefined")) {
            if (this._children[0] instanceof XmlDecl_1.default) {
                index = 1;
            }
            else {
                index = 0;
            }
        }
        this.insertChild(dtd, index);
        return dtd;
    };
    /**
     * Inserts the specified node into this node's children at the specified
     * index. The node is not inserted if it is already present. If this node
     * already has a parent, it is removed from that parent.
     *
     * Only {@link XmlComment}, {@link XmlDecl}, {@link XmlDtd}, or
     * {@link XmlProcInst} nodes can be inserted. Furthermore, {@link XmlDecl}
     * and {@link XmlDtd} nodes must be inserted in that order and must
     * precede the {@link XmlElement} node. In addition, {@link XmlComment} or
     * {@link XmlProcInst} nodes must follow the {@link XmlDecl} node.
     *
     * @param {XmlNode} node   The node to insert.
     * @param {number} [index] The index at which to insert the node. Nodes at
     *                         or after the index are shifted to the right. If
     *                         no index is specified, the node is inserted at
     *                         the end.
     *
     * @returns {XmlNode} The node inserted into this node's children, or
     *                    undefined if no node was inserted.
     */
    XmlDocument.prototype.insertChild = function (node, index) {
        if (!(node instanceof XmlComment_1.default || node instanceof XmlDecl_1.default ||
            node instanceof XmlDtd_1.default || node instanceof XmlProcInst_1.default)) {
            throw new TypeError("node should be an instance of" +
                " XmlComment, XmlDecl, XmlDtd, or XmlProcInst");
        }
        if (node instanceof XmlComment_1.default || node instanceof XmlProcInst_1.default) {
            if (this._children[0] instanceof XmlDecl_1.default) {
                if (index === 0) {
                    throw new Error("XmlComment or XmlProcInst node should be" +
                        " inserted after the XmlDecl node");
                }
            }
        }
        else if (node instanceof XmlDecl_1.default) {
            if (this._children[0] instanceof XmlDecl_1.default) {
                throw new Error("XmlDocument node should only contain one" +
                    " XmlDecl node");
            }
            if (index !== 0) {
                throw new Error("XmlDecl node should be inserted at the" +
                    " beginning of an XmlDocument node");
            }
        }
        else if (node instanceof XmlDtd_1.default) {
            if (this._children[0] instanceof XmlDecl_1.default) {
                if (index === 0) {
                    throw new Error("XmlDtd node should be inserted after" +
                        " the XmlDecl node");
                }
            }
            for (var i = 0; i < index && i < this._children.length; i++) {
                if (this._children[i] instanceof XmlElement_1.default) {
                    throw new Error("XmlDtd node should be inserted before" +
                        " the XmlElement node");
                }
            }
            for (var _i = 0, _a = this._children; _i < _a.length; _i++) {
                var child = _a[_i];
                if (child instanceof XmlDtd_1.default) {
                    throw new Error("XmlDocument node should only contain" +
                        " one XmlDtd node");
                }
            }
        }
        return _super.prototype.insertChild.call(this, node, index);
    };
    /**
     * Inserts a new processing instruction at the specified index. If no index
     * is specified, the node is inserted at the end of this node's children.
     *
     * @param {string} target    The target of the processing instruction.
     * @param {string} [content] The data of the processing instruction, or
     *                           undefined if there is no target.
     * @param {number} [index]   The index at which the node should be inserted.
     *                           If no index is specified, the node is inserted
     *                           at the end of this node's children.
     *
     * @returns {XmlProcInst} The newly created processing instruction.
     */
    XmlDocument.prototype.procInst = function (target, content, index) {
        var procInst = new XmlProcInst_1.default(target, content);
        this.insertChild(procInst, index);
        return procInst;
    };
    /**
     * Removes the specified node from this node's children.
     *
     * Note that {@link XmlElement} nodes cannot be removed from this node;
     * attempts to do so will result in an exception being thrown.
     *
     * @param {XmlNode} node The node to remove.
     *
     * @returns {boolean} Whether a node was removed.
     */
    XmlDocument.prototype.removeChild = function (node) {
        if (node instanceof XmlElement_1.default) {
            throw new Error("XmlElement nodes cannot be removed from" +
                " XmlDocument nodes");
        }
        return _super.prototype.removeChild.call(this, node);
    };
    /**
     * Removes the node at the specified index from this node's children.
     *
     * Note that {@link XmlElement} nodes cannot be removed from this node;
     * attempts to do so will result in an exception being thrown.
     *
     * @param {number} index The index at which the node to be removed is
     *                       located.
     *
     * @returns {XmlNode} The node that was removed, or undefined if no node
     *                    was removed.
     */
    XmlDocument.prototype.removeChildAtIndex = function (index) {
        if (this._children[index] instanceof XmlElement_1.default) {
            throw new Error("XmlElement nodes cannot be removed from" +
                " XmlDocument nodes");
        }
        return _super.prototype.removeChildAtIndex.call(this, index);
    };
    /**
     * Returns the root element of this document.
     *
     * @returns {XmlNode} The root element of this document.
     */
    XmlDocument.prototype.root = function () {
        for (var _i = 0, _a = this._children; _i < _a.length; _i++) {
            var node = _a[_i];
            if (node instanceof XmlElement_1.default) {
                return node;
            }
        }
        throw new Error("XmlDocument does not contain a root node");
    };
    /**
     * Returns an XML string representation of this node.
     *
     * @param {IStringOptions} [options] Formatting options for the string
     *                                  representation.
     *
     * @returns {string} An XML string representation of this node.
     */
    XmlDocument.prototype.toString = function (options) {
        if (options === void 0) { options = {}; }
        options_1.validateStringOptions(options);
        var str = "";
        for (var _i = 0, _a = this._children; _i < _a.length; _i++) {
            var node = _a[_i];
            str += node.toString(options);
            if (options.pretty) {
                str += options.newline;
            }
        }
        var len = str.length - options.newline.length;
        if (str.substr(len) === options.newline) {
            str = str.substr(0, len);
        }
        return str;
    };
    return XmlDocument;
}(XmlNode_1.default));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = XmlDocument;


/***/ }),
/* 36 */
/***/ (function(module, exports) {

// shim for using process in browser
var process = module.exports = {};

// cached from whatever global is present so that test runners that stub it
// don't break things.  But we need to wrap it in a try catch in case it is
// wrapped in strict mode code which doesn't define any globals.  It's inside a
// function because try/catches deoptimize in certain engines.

var cachedSetTimeout;
var cachedClearTimeout;

function defaultSetTimout() {
    throw new Error('setTimeout has not been defined');
}
function defaultClearTimeout () {
    throw new Error('clearTimeout has not been defined');
}
(function () {
    try {
        if (typeof setTimeout === 'function') {
            cachedSetTimeout = setTimeout;
        } else {
            cachedSetTimeout = defaultSetTimout;
        }
    } catch (e) {
        cachedSetTimeout = defaultSetTimout;
    }
    try {
        if (typeof clearTimeout === 'function') {
            cachedClearTimeout = clearTimeout;
        } else {
            cachedClearTimeout = defaultClearTimeout;
        }
    } catch (e) {
        cachedClearTimeout = defaultClearTimeout;
    }
} ())
function runTimeout(fun) {
    if (cachedSetTimeout === setTimeout) {
        //normal enviroments in sane situations
        return setTimeout(fun, 0);
    }
    // if setTimeout wasn't available but was latter defined
    if ((cachedSetTimeout === defaultSetTimout || !cachedSetTimeout) && setTimeout) {
        cachedSetTimeout = setTimeout;
        return setTimeout(fun, 0);
    }
    try {
        // when when somebody has screwed with setTimeout but no I.E. maddness
        return cachedSetTimeout(fun, 0);
    } catch(e){
        try {
            // When we are in I.E. but the script has been evaled so I.E. doesn't trust the global object when called normally
            return cachedSetTimeout.call(null, fun, 0);
        } catch(e){
            // same as above but when it's a version of I.E. that must have the global object for 'this', hopfully our context correct otherwise it will throw a global error
            return cachedSetTimeout.call(this, fun, 0);
        }
    }


}
function runClearTimeout(marker) {
    if (cachedClearTimeout === clearTimeout) {
        //normal enviroments in sane situations
        return clearTimeout(marker);
    }
    // if clearTimeout wasn't available but was latter defined
    if ((cachedClearTimeout === defaultClearTimeout || !cachedClearTimeout) && clearTimeout) {
        cachedClearTimeout = clearTimeout;
        return clearTimeout(marker);
    }
    try {
        // when when somebody has screwed with setTimeout but no I.E. maddness
        return cachedClearTimeout(marker);
    } catch (e){
        try {
            // When we are in I.E. but the script has been evaled so I.E. doesn't  trust the global object when called normally
            return cachedClearTimeout.call(null, marker);
        } catch (e){
            // same as above but when it's a version of I.E. that must have the global object for 'this', hopfully our context correct otherwise it will throw a global error.
            // Some versions of I.E. have different rules for clearTimeout vs setTimeout
            return cachedClearTimeout.call(this, marker);
        }
    }



}
var queue = [];
var draining = false;
var currentQueue;
var queueIndex = -1;

function cleanUpNextTick() {
    if (!draining || !currentQueue) {
        return;
    }
    draining = false;
    if (currentQueue.length) {
        queue = currentQueue.concat(queue);
    } else {
        queueIndex = -1;
    }
    if (queue.length) {
        drainQueue();
    }
}

function drainQueue() {
    if (draining) {
        return;
    }
    var timeout = runTimeout(cleanUpNextTick);
    draining = true;

    var len = queue.length;
    while(len) {
        currentQueue = queue;
        queue = [];
        while (++queueIndex < len) {
            if (currentQueue) {
                currentQueue[queueIndex].run();
            }
        }
        queueIndex = -1;
        len = queue.length;
    }
    currentQueue = null;
    draining = false;
    runClearTimeout(timeout);
}

process.nextTick = function (fun) {
    var args = new Array(arguments.length - 1);
    if (arguments.length > 1) {
        for (var i = 1; i < arguments.length; i++) {
            args[i - 1] = arguments[i];
        }
    }
    queue.push(new Item(fun, args));
    if (queue.length === 1 && !draining) {
        runTimeout(drainQueue);
    }
};

// v8 likes predictible objects
function Item(fun, array) {
    this.fun = fun;
    this.array = array;
}
Item.prototype.run = function () {
    this.fun.apply(null, this.array);
};
process.title = 'browser';
process.browser = true;
process.env = {};
process.argv = [];
process.version = ''; // empty string to avoid regexp issues
process.versions = {};

function noop() {}

process.on = noop;
process.addListener = noop;
process.once = noop;
process.off = noop;
process.removeListener = noop;
process.removeAllListeners = noop;
process.emit = noop;
process.prependListener = noop;
process.prependOnceListener = noop;

process.listeners = function (name) { return [] }

process.binding = function (name) {
    throw new Error('process.binding is not supported');
};

process.cwd = function () { return '/' };
process.chdir = function (dir) {
    throw new Error('process.chdir is not supported');
};
process.umask = function() { return 0; };


/***/ }),
/* 37 */
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function(global, process) {(function (global, undefined) {
    "use strict";

    if (global.setImmediate) {
        return;
    }

    var nextHandle = 1; // Spec says greater than zero
    var tasksByHandle = {};
    var currentlyRunningATask = false;
    var doc = global.document;
    var registerImmediate;

    function setImmediate(callback) {
      // Callback can either be a function or a string
      if (typeof callback !== "function") {
        callback = new Function("" + callback);
      }
      // Copy function arguments
      var args = new Array(arguments.length - 1);
      for (var i = 0; i < args.length; i++) {
          args[i] = arguments[i + 1];
      }
      // Store and register the task
      var task = { callback: callback, args: args };
      tasksByHandle[nextHandle] = task;
      registerImmediate(nextHandle);
      return nextHandle++;
    }

    function clearImmediate(handle) {
        delete tasksByHandle[handle];
    }

    function run(task) {
        var callback = task.callback;
        var args = task.args;
        switch (args.length) {
        case 0:
            callback();
            break;
        case 1:
            callback(args[0]);
            break;
        case 2:
            callback(args[0], args[1]);
            break;
        case 3:
            callback(args[0], args[1], args[2]);
            break;
        default:
            callback.apply(undefined, args);
            break;
        }
    }

    function runIfPresent(handle) {
        // From the spec: "Wait until any invocations of this algorithm started before this one have completed."
        // So if we're currently running a task, we'll need to delay this invocation.
        if (currentlyRunningATask) {
            // Delay by doing a setTimeout. setImmediate was tried instead, but in Firefox 7 it generated a
            // "too much recursion" error.
            setTimeout(runIfPresent, 0, handle);
        } else {
            var task = tasksByHandle[handle];
            if (task) {
                currentlyRunningATask = true;
                try {
                    run(task);
                } finally {
                    clearImmediate(handle);
                    currentlyRunningATask = false;
                }
            }
        }
    }

    function installNextTickImplementation() {
        registerImmediate = function(handle) {
            process.nextTick(function () { runIfPresent(handle); });
        };
    }

    function canUsePostMessage() {
        // The test against `importScripts` prevents this implementation from being installed inside a web worker,
        // where `global.postMessage` means something completely different and can't be used for this purpose.
        if (global.postMessage && !global.importScripts) {
            var postMessageIsAsynchronous = true;
            var oldOnMessage = global.onmessage;
            global.onmessage = function() {
                postMessageIsAsynchronous = false;
            };
            global.postMessage("", "*");
            global.onmessage = oldOnMessage;
            return postMessageIsAsynchronous;
        }
    }

    function installPostMessageImplementation() {
        // Installs an event handler on `global` for the `message` event: see
        // * https://developer.mozilla.org/en/DOM/window.postMessage
        // * http://www.whatwg.org/specs/web-apps/current-work/multipage/comms.html#crossDocumentMessages

        var messagePrefix = "setImmediate$" + Math.random() + "$";
        var onGlobalMessage = function(event) {
            if (event.source === global &&
                typeof event.data === "string" &&
                event.data.indexOf(messagePrefix) === 0) {
                runIfPresent(+event.data.slice(messagePrefix.length));
            }
        };

        if (global.addEventListener) {
            global.addEventListener("message", onGlobalMessage, false);
        } else {
            global.attachEvent("onmessage", onGlobalMessage);
        }

        registerImmediate = function(handle) {
            global.postMessage(messagePrefix + handle, "*");
        };
    }

    function installMessageChannelImplementation() {
        var channel = new MessageChannel();
        channel.port1.onmessage = function(event) {
            var handle = event.data;
            runIfPresent(handle);
        };

        registerImmediate = function(handle) {
            channel.port2.postMessage(handle);
        };
    }

    function installReadyStateChangeImplementation() {
        var html = doc.documentElement;
        registerImmediate = function(handle) {
            // Create a <script> element; its readystatechange event will be fired asynchronously once it is inserted
            // into the document. Do so, thus queuing up the task. Remember to clean up once it's been called.
            var script = doc.createElement("script");
            script.onreadystatechange = function () {
                runIfPresent(handle);
                script.onreadystatechange = null;
                html.removeChild(script);
                script = null;
            };
            html.appendChild(script);
        };
    }

    function installSetTimeoutImplementation() {
        registerImmediate = function(handle) {
            setTimeout(runIfPresent, 0, handle);
        };
    }

    // If supported, we should attach to the prototype of global, since that is where setTimeout et al. live.
    var attachTo = Object.getPrototypeOf && Object.getPrototypeOf(global);
    attachTo = attachTo && attachTo.setTimeout ? attachTo : global;

    // Don't get fooled by e.g. browserify environments.
    if ({}.toString.call(global.process) === "[object process]") {
        // For Node.js before 0.9
        installNextTickImplementation();

    } else if (canUsePostMessage()) {
        // For non-IE10 modern browsers
        installPostMessageImplementation();

    } else if (global.MessageChannel) {
        // For web workers, where supported
        installMessageChannelImplementation();

    } else if (doc && "onreadystatechange" in doc.createElement("script")) {
        // For IE 6–8
        installReadyStateChangeImplementation();

    } else {
        // For older browsers
        installSetTimeoutImplementation();
    }

    attachTo.setImmediate = setImmediate;
    attachTo.clearImmediate = clearImmediate;
}(typeof self === "undefined" ? typeof global === "undefined" ? this : global : self));

/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__(31), __webpack_require__(36)))

/***/ }),
/* 38 */
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function(global) {var scope = (typeof global !== "undefined" && global) ||
            (typeof self !== "undefined" && self) ||
            window;
var apply = Function.prototype.apply;

// DOM APIs, for completeness

exports.setTimeout = function() {
  return new Timeout(apply.call(setTimeout, scope, arguments), clearTimeout);
};
exports.setInterval = function() {
  return new Timeout(apply.call(setInterval, scope, arguments), clearInterval);
};
exports.clearTimeout =
exports.clearInterval = function(timeout) {
  if (timeout) {
    timeout.close();
  }
};

function Timeout(id, clearFn) {
  this._id = id;
  this._clearFn = clearFn;
}
Timeout.prototype.unref = Timeout.prototype.ref = function() {};
Timeout.prototype.close = function() {
  this._clearFn.call(scope, this._id);
};

// Does not start the time, just sets up the members needed.
exports.enroll = function(item, msecs) {
  clearTimeout(item._idleTimeoutId);
  item._idleTimeout = msecs;
};

exports.unenroll = function(item) {
  clearTimeout(item._idleTimeoutId);
  item._idleTimeout = -1;
};

exports._unrefActive = exports.active = function(item) {
  clearTimeout(item._idleTimeoutId);

  var msecs = item._idleTimeout;
  if (msecs >= 0) {
    item._idleTimeoutId = setTimeout(function onTimeout() {
      if (item._onTimeout)
        item._onTimeout();
    }, msecs);
  }
};

// setimmediate attaches itself to the global object
__webpack_require__(37);
// On some exotic environments, it's not clear which object `setimmediate` was
// able to install onto.  Search each possibility in the same order as the
// `setimmediate` library.
exports.setImmediate = (typeof self !== "undefined" && self.setImmediate) ||
                       (typeof global !== "undefined" && global.setImmediate) ||
                       (this && this.setImmediate);
exports.clearImmediate = (typeof self !== "undefined" && self.clearImmediate) ||
                         (typeof global !== "undefined" && global.clearImmediate) ||
                         (this && this.clearImmediate);

/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__(31)))

/***/ }),
/* 39 */
/***/ (function(module, exports) {


module.exports = function () {
  var selection = document.getSelection();
  if (!selection.rangeCount) {
    return function () {};
  }
  var active = document.activeElement;

  var ranges = [];
  for (var i = 0; i < selection.rangeCount; i++) {
    ranges.push(selection.getRangeAt(i));
  }

  switch (active.tagName.toUpperCase()) { // .toUpperCase handles XHTML
    case 'INPUT':
    case 'TEXTAREA':
      active.blur();
      break;

    default:
      active = null;
      break;
  }

  selection.removeAllRanges();
  return function () {
    selection.type === 'Caret' &&
    selection.removeAllRanges();

    if (!selection.rangeCount) {
      ranges.forEach(function(range) {
        selection.addRange(range);
      });
    }

    active &&
    active.focus();
  };
};


/***/ }),
/* 40 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var NodeType;
(function (NodeType) {
    NodeType[NodeType["None"] = 0] = "None";
    NodeType[NodeType["Root"] = 1] = "Root";
    NodeType[NodeType["ProductOccurrence"] = 2] = "ProductOccurrence";
    NodeType[NodeType["DoNotUse1"] = 3] = "DoNotUse1";
    NodeType[NodeType["DoNotUse2"] = 4] = "DoNotUse2";
    NodeType[NodeType["Ri_BrepModel"] = 5] = "Ri_BrepModel";
    NodeType[NodeType["Ri_PolyBrepModel"] = 6] = "Ri_PolyBrepModel";
    NodeType[NodeType["Ri_Set"] = 7] = "Ri_Set";
    NodeType[NodeType["Ri_PolyWire"] = 8] = "Ri_PolyWire";
    NodeType[NodeType["Ri_TessMarkup"] = 9] = "Ri_TessMarkup";
    NodeType[NodeType["DoNotUse3"] = 10] = "DoNotUse3";
    NodeType[NodeType["Part"] = 11] = "Part";
    NodeType[NodeType["AxisRoot"] = 12] = "AxisRoot";
    NodeType[NodeType["Axis"] = 13] = "Axis";
    NodeType[NodeType["PMIView"] = 14] = "PMIView";
    NodeType[NodeType["Annotation"] = 15] = "Annotation";
    NodeType[NodeType["PLACEHOLDER_AnnotationItem"] = 16] = "PLACEHOLDER_AnnotationItem";
    NodeType[NodeType["AnnotationReference"] = 17] = "AnnotationReference";
    NodeType[NodeType["AnnotationSet"] = 18] = "AnnotationSet";
    NodeType[NodeType["Curve"] = 19] = "Curve";
    NodeType[NodeType["Line"] = 20] = "Line";
    NodeType[NodeType["Plane"] = 21] = "Plane";
    NodeType[NodeType["Direction"] = 22] = "Direction";
    NodeType[NodeType["Point"] = 23] = "Point";
    NodeType[NodeType["PointSet"] = 24] = "PointSet";
    NodeType[NodeType["CoordinateSystem"] = 25] = "CoordinateSystem";
    NodeType[NodeType["Handle_Root"] = 26] = "Handle_Root";
    NodeType[NodeType["Handle_Arrow"] = 27] = "Handle_Arrow";
    NodeType[NodeType["Handle_Sphere"] = 28] = "Handle_Sphere";
    NodeType[NodeType["Handle_Curlyarrow"] = 29] = "Handle_Curlyarrow";
    NodeType[NodeType["Solid"] = 30] = "Solid";
    NodeType[NodeType["Wire"] = 31] = "Wire";
    NodeType[NodeType["AnnotationItem"] = 32] = "AnnotationItem";
    NodeType[NodeType["Markup_Text"] = 33] = "Markup_Text";
    NodeType[NodeType["Dimensioning"] = 34] = "Dimensioning";
    NodeType[NodeType["PointMarker"] = 35] = "PointMarker";
    NodeType[NodeType["EdgeMarker"] = 36] = "EdgeMarker";
    NodeType[NodeType["CircleMarker"] = 37] = "CircleMarker";
    NodeType[NodeType["AxisMarker"] = 38] = "AxisMarker";
    NodeType[NodeType["ReferenceLine"] = 39] = "ReferenceLine";
    NodeType[NodeType["LineMarker"] = 40] = "LineMarker";
    NodeType[NodeType["TriangleMesh"] = 41] = "TriangleMesh";
    NodeType[NodeType["Scene"] = 42] = "Scene";
    NodeType[NodeType["Markup_Circle"] = 43] = "Markup_Circle";
    NodeType[NodeType["Markup_Rectangle"] = 44] = "Markup_Rectangle";
    NodeType[NodeType["Markup_Freehand"] = 45] = "Markup_Freehand";
    NodeType[NodeType["CutGeometry_Surface"] = 46] = "CutGeometry_Surface";
    NodeType[NodeType["CutGeometry_Wire"] = 47] = "CutGeometry_Wire";
    NodeType[NodeType["PmiMarkup_Unknown"] = 48] = "PmiMarkup_Unknown";
    NodeType[NodeType["PmiMarkup_Text"] = 49] = "PmiMarkup_Text";
    NodeType[NodeType["PmiMarkup_Dimension"] = 50] = "PmiMarkup_Dimension";
    NodeType[NodeType["PmiMarkup_Arrow"] = 51] = "PmiMarkup_Arrow";
    NodeType[NodeType["PmiMarkup_Balloon"] = 52] = "PmiMarkup_Balloon";
    NodeType[NodeType["PmiMarkup_CircleCenter"] = 53] = "PmiMarkup_CircleCenter";
    NodeType[NodeType["PmiMarkup_Coordinate"] = 54] = "PmiMarkup_Coordinate";
    NodeType[NodeType["PmiMarkup_Datum"] = 55] = "PmiMarkup_Datum";
    NodeType[NodeType["PmiMarkup_Fastener"] = 56] = "PmiMarkup_Fastener";
    NodeType[NodeType["PmiMarkup_Gdt"] = 57] = "PmiMarkup_Gdt";
    NodeType[NodeType["PmiMarkup_Locator"] = 58] = "PmiMarkup_Locator";
    NodeType[NodeType["PmiMarkup_MeasurementPoint"] = 59] = "PmiMarkup_MeasurementPoint";
    NodeType[NodeType["PmiMarkup_Roughness"] = 60] = "PmiMarkup_Roughness";
    NodeType[NodeType["PmiMarkup_Welding"] = 61] = "PmiMarkup_Welding";
    NodeType[NodeType["PmiMarkup_Table"] = 62] = "PmiMarkup_Table";
    NodeType[NodeType["PmiMarkup_Other"] = 63] = "PmiMarkup_Other";
    NodeType[NodeType["OffsetLine"] = 64] = "OffsetLine";
    NodeType[NodeType["Drawing"] = 65] = "Drawing";
    NodeType[NodeType["Drawing_Model"] = 66] = "Drawing_Model";
    NodeType[NodeType["Drawing_Sheet"] = 67] = "Drawing_Sheet";
    NodeType[NodeType["Drawing_View"] = 68] = "Drawing_View";
    NodeType[NodeType["Drawing_Entity"] = 69] = "Drawing_Entity";
    NodeType[NodeType["Drawing_Block"] = 70] = "Drawing_Block";
    NodeType[NodeType["Drawing_BlockBasic"] = 71] = "Drawing_BlockBasic";
    NodeType[NodeType["Drawing_BlockOperator"] = 72] = "Drawing_BlockOperator";
    NodeType[NodeType["Drawing_BlockBackground"] = 73] = "Drawing_BlockBackground";
    NodeType[NodeType["Drawing_ClipFrame"] = 74] = "Drawing_ClipFrame";
    NodeType[NodeType["Drawing_Curve"] = 75] = "Drawing_Curve";
    NodeType[NodeType["Drawing_FilledArea"] = 76] = "Drawing_FilledArea";
    NodeType[NodeType["Drawing_Picture"] = 77] = "Drawing_Picture";
    NodeType[NodeType["Drawing_Vertices"] = 78] = "Drawing_Vertices";
    NodeType[NodeType["Dummy"] = 79] = "Dummy";
    NodeType[NodeType["CutGeometry_Set"] = 80] = "CutGeometry_Set";
    NodeType[NodeType["MasterRootNode"] = 81] = "MasterRootNode";
    NodeType[NodeType["MarkupSet"] = 82] = "MarkupSet";
    NodeType[NodeType["DataAnalysisResult"] = 83] = "DataAnalysisResult";
    NodeType[NodeType["Markup"] = 84] = "Markup";
    NodeType[NodeType["UnitTranslation"] = 85] = "UnitTranslation";
    NodeType[NodeType["Document2D"] = 86] = "Document2D";
    NodeType[NodeType["Various"] = 4294967294] = "Various";
    NodeType[NodeType["Unknown"] = 4294967295] = "Unknown";
})(NodeType = exports.NodeType || (exports.NodeType = {}));
;


/***/ }),
/* 41 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var WVTooltip = (function () {
    function WVTooltip(anchorEl) {
        var _this = this;
        this.attributesRow = [];
        this.attributes = [];
        this.orientation = 'top';
        this.triggerTime = 1000;
        this.isActive = false;
        this.anchorEl = anchorEl;
        this.wrapperEl = document.createElement('div');
        this.wrapperEl.className = 'webviewer-tooltip-wrapper';
        this.titleEl = document.createElement('span');
        this.titleEl.className = 'webviewer-tooltip-title';
        this.attributeWrapper = document.createElement('ul');
        this.attributeWrapper.className = 'webviewer-tooltip-attributes-wrapper';
        $(this.attributeWrapper).hide();
        this.wrapperEl.appendChild(this.titleEl);
        this.wrapperEl.appendChild(this.attributeWrapper);
        this.anchorEl.onmouseenter = function (e) {
            _this.isActive = true;
            setTimeout(function () {
                if (_this.isActive && document.getElementsByClassName('webviewer-tooltip-wrapper').length === 0) {
                    _this.show();
                }
                else if (_this.isActive) {
                    $('.webviewer-tooltip-wrapper').each(function () {
                        $(this).remove();
                    });
                    _this.show();
                }
            }, _this.triggerTime);
        };
        this.anchorEl.onmouseleave = function (e) {
            _this.isActive = false;
            _this.hide();
        };
    }
    WVTooltip.prototype.addAttribute = function (attribute) {
        if (attribute === "" || attribute === undefined) {
            return;
        }
        if (this.attributes.length === 0) {
            $(this.attributeWrapper).show();
        }
        var attributeRow = document.createElement('li');
        attributeRow.className = 'webviewer-tooltip-attribute';
        attributeRow.innerHTML = attribute.replace(/\\n/g, '\r');
        this.attributes.push(attribute);
        this.attributesRow.push(attributeRow);
        this.attributeWrapper.appendChild(attributeRow);
    };
    WVTooltip.prototype.show = function () {
        var topPos = this.anchorEl.getBoundingClientRect().top;
        if ((!this.title || this.title === "" || this.title === null) && this.attributesRow.length === 0 || topPos === 0) {
            return;
        }
        document.body.appendChild(this.wrapperEl);
        if (this.wrapperEl.offsetWidth + this.anchorEl.getBoundingClientRect().left > document.body.offsetWidth) {
            this.wrapperEl.style.left = document.body.offsetWidth - this.wrapperEl.offsetWidth + 'px';
        }
        else {
            this.wrapperEl.style.left = this.anchorEl.getBoundingClientRect().left + 'px';
        }
        var height = this.wrapperEl.offsetHeight;
        if (this.orientation === 'top') {
            if (topPos - height < 0) {
                this.wrapperEl.style.bottom = (document.body.offsetHeight - topPos - this.anchorEl.offsetHeight - 5) + 'px';
            }
            else {
                this.wrapperEl.style.bottom = (document.body.offsetHeight - topPos + 5) + 'px';
            }
        }
        else {
            if (topPos + height > document.body.offsetHeight) {
                this.wrapperEl.style.bottom = (document.body.offsetHeight - topPos + 5) + 'px';
            }
            else {
                this.wrapperEl.style.bottom = (document.body.offsetHeight - topPos - height - this.anchorEl.offsetHeight - 5) + 'px';
            }
        }
    };
    WVTooltip.prototype.hide = function () {
        $(this.wrapperEl).remove();
    };
    WVTooltip.prototype.setTitle = function (title) {
        this.title = title;
        this.titleEl.innerHTML = title ? title.replace(/\\n/g, '\r') : '';
    };
    WVTooltip.prototype.setOrientation = function (orientation) {
        this.orientation = orientation;
    };
    WVTooltip.prototype.setTriggerTime = function (triggerTime) {
        this.triggerTime = triggerTime;
    };
    WVTooltip.prototype.removeAttributes = function () {
        this.attributes = [];
        var length = this.attributesRow.length;
        for (var i = 0; i < length; i++) {
            $(this.attributesRow[i]).remove();
        }
        this.attributesRow = [];
        $(this.attributeWrapper).hide();
    };
    WVTooltip.prototype.setKey = function (key) {
        this.key = key;
    };
    return WVTooltip;
}());
exports.WVTooltip = WVTooltip;


/***/ }),
/* 42 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var ui = __webpack_require__(0);
var switch_1 = __webpack_require__(50);
var option_toggle_1 = __webpack_require__(49);
var loading_slider_1 = __webpack_require__(48);
var MobileLoadingModal = (function () {
    function MobileLoadingModal(client, file, callback) {
        var _this = this;
        this.tooltipsActive = false;
        this.client = client;
        this.file = file;
        this.callback = callback;
        this.modalWrapper = document.createElement('div');
        this.modalWrapper.className = 'native-loading-modal';
        this.isOpen = true;
        this.detectOutsideClick = this.detectOutsideClick.bind(this);
        this.detectEscPressed = this.detectEscPressed.bind(this);
        this.setTesselation = this.setTesselation.bind(this);
        this.setImportMode = this.setImportMode.bind(this);
        this.setSliderStep = this.setSliderStep.bind(this);
        this.detectTooltipOusideClick = this.detectTooltipOusideClick.bind(this);
        var titleEl = document.createElement('span');
        titleEl.className = 'native-loading-modal-title';
        titleEl.textContent = ui.translate('ID_IMPORTDIALOG_TITLE_TEXT');
        titleEl.id = 'ID_IMPORTDIALOG_TITLE_TEXT';
        ui.translationObjects.push(titleEl);
        this.modalWrapper.appendChild(titleEl);
        this.tooltipCircle = document.createElement('i');
        this.tooltipCircle.className = 'fas fa-question-circle mobile-native-loading-tooltip-toggle';
        this.modalWrapper.appendChild(this.tooltipCircle);
        var contentWrapper = document.createElement('div');
        contentWrapper.className = 'native-loading-modal-content-wrapper';
        contentWrapper.style.marginTop = '20px';
        contentWrapper.style.height = 'calc(50% - 20px)';
        this.modalWrapper.appendChild(contentWrapper);
        this.slider = new loading_slider_1.MobileLoadingSlider(contentWrapper, true, this.setSliderStep);
        this.preferGeometrySwitch = new option_toggle_1.MobileOptionToggle(contentWrapper, 'ID_IMPORTDIALOG_SWITCH_READMODE_TEXT_BREP', 'ID_IMPORTDIALOG_SWITCH_READMODE_TEXT_TESSELLATION', 'ID_IMPORTDIALOG_SWITCH_READMODE_TOOLTIP', this.setTesselation);
        this.prefer3DSwitch = new option_toggle_1.MobileOptionToggle(contentWrapper, 'ID_IMPORTDIALOG_SWITCH_IMPORTMODE_TEXT_2D', 'ID_IMPORTDIALOG_SWITCH_IMPORTMODE_TEXT_3D', 'ID_IMPORTDIALOG_SWITCH_IMPORTMODE_TOOLTIP', this.setImportMode);
        try {
            if (_Hide2DSwitch) {
                $(this.prefer3DSwitch.wrapperEl).hide();
            }
        }
        catch (e) { }
        this.buttonWrapper = document.createElement('div');
        this.buttonWrapper.className = 'native-loading-buttons';
        this.modalWrapper.appendChild(this.buttonWrapper);
        this.skipButton = document.createElement('button');
        this.skipButton.textContent = ui.translate('ID_IMPORTDIALOG_BUTTON_SKIP');
        this.skipButton.id = 'ID_IMPORTDIALOG_BUTTON_SKIP';
        ui.translationObjects.push(this.skipButton);
        this.skipButton.className = 'native-loading-skip';
        this.buttonWrapper.appendChild(this.skipButton);
        $(this.skipButton).click(function (e) {
            _this.skip();
        });
        this.rememberMeSwitch = new switch_1.MobileDefSwitch(this.buttonWrapper, 'ID_IMPORTDIALOG_SWITCH_DONOTSHOWAGAIN_TEXT', 'remember-me-switch', false);
        this.submitButton = document.createElement('button');
        this.submitButton.className = 'native-loading-ok';
        this.submitButton.textContent = ui.translate('ID_IMPORTDIALOG_BUTTON_OK');
        this.submitButton.id = 'ID_IMPORTDIALOG_BUTTON_OK';
        ui.translationObjects.push(this.submitButton);
        this.buttonWrapper.appendChild(this.submitButton);
        $(this.submitButton).click(function (e) {
            var step = _this.slider.getStep();
            var nativeLoadingSettings = {
                prefer2D: _this.prefer3DSwitch.getVal(),
                preferTesselation: _this.preferGeometrySwitch.getVal(),
                step: step,
                rememberMe: _this.rememberMeSwitch.getVal()
            };
            _this.initSettings = nativeLoadingSettings;
            _this.submit(nativeLoadingSettings);
        });
        document.body.appendChild(this.modalWrapper);
        var storage = localStorage.getItem('nativeLoadingSettings');
        if (storage !== null) {
            var parsedStorage = JSON.parse(storage);
            this.initSettings = parsedStorage;
        }
        else {
            this.initSettings = {
                step: 3,
                prefer2D: true,
                rememberMe: false,
                preferTesselation: true
            };
            this.slider.stepTo(3);
        }
        this.resize();
        this.tooltipCircle.onclick = function (e) {
            _this.setTooltipMode(!_this.tooltipsActive);
        };
    }
    MobileLoadingModal.prototype.setCallback = function (callback) {
        this.callback = callback;
    };
    MobileLoadingModal.prototype.setTesselation = function (state) {
        if (this.preferGeometrySwitch) {
            this.slider.setTesselationMode(!state);
            if (!state) {
                this.preferGeometrySwitch.setHighlightColor('darkred');
            }
            else {
                this.preferGeometrySwitch.setHighlightColor('darkgreen');
            }
        }
    };
    MobileLoadingModal.prototype.setImportMode = function (state) {
    };
    MobileLoadingModal.prototype.setSliderStep = function (pos) {
        if (this.preferGeometrySwitch && this.prefer3DSwitch) {
            var preset = void 0;
            switch (pos) {
                case 0:
                    preset = _IncludeHiddenNativeLoading;
                    break;
                case 1:
                    preset = _CompleteNativeLoading;
                    break;
                case 2:
                    preset = _ModerateNativeLoading;
                    break;
                case 3:
                    preset = _FastNativeLoading;
                    break;
            }
            var preferBREP = preset.indexOf('PreferGeometry') !== -1;
            if (preferBREP) {
                this.preferGeometrySwitch.setVal(false);
            }
            var preferTesselation = preset.indexOf('PreferTessellation') !== -1;
            if (preferTesselation) {
                this.preferGeometrySwitch.setVal(true);
            }
            var prefer3D = preset.indexOf('Prefer3D') !== -1;
            if (prefer3D) {
                this.prefer3DSwitch.setVal(true);
            }
            var prefer2D = preset.indexOf('Prefer2D') !== -1;
            if (prefer2D) {
                this.prefer3DSwitch.setVal(false);
            }
        }
    };
    MobileLoadingModal.prototype.resize = function () {
        this.slider.resize();
        this.prefer3DSwitch.resize();
        this.preferGeometrySwitch.resize();
    };
    MobileLoadingModal.prototype.skip = function () {
        var showAgain = this.rememberMeSwitch.getVal();
        this.initSettings.rememberMe = showAgain;
        localStorage.setItem('nativeLoadingSettings', JSON.stringify(this.initSettings));
        this.hide();
    };
    MobileLoadingModal.prototype.hide = function (cancel) {
        document.body.removeEventListener('click', this.detectOutsideClick);
        document.body.removeEventListener('keyup', this.detectEscPressed);
        this.isOpen = false;
        if (!cancel) {
            this.callback(this.file);
            this.client.emit('@NativeLoad', this.file);
        }
        this.file = undefined;
        this.callback = undefined;
        $(this.modalWrapper).hide();
    };
    MobileLoadingModal.prototype.setFile = function (filename) {
        this.file = filename;
    };
    MobileLoadingModal.prototype.show = function () {
        this.isOpen = true;
        $(this.modalWrapper).show();
        this.setTooltipMode(false);
        document.body.addEventListener('click', this.detectOutsideClick);
        document.body.addEventListener('keyup', this.detectEscPressed);
        var storedSettings = localStorage.getItem('nativeLoadingSettings');
        if (storedSettings === null) {
            this.initSettings = {
                step: 3,
                prefer2D: true,
                preferTesselation: true
            };
        }
        this.prefer3DSwitch.setVal(this.initSettings.prefer2D);
        this.preferGeometrySwitch.setVal(this.initSettings.preferTesselation);
        this.slider.stepTo(this.initSettings.step);
        this.rememberMeSwitch.setVal(false);
        var buttonWidthSum = this.submitButton.offsetWidth + this.skipButton.offsetWidth + 100;
        this.rememberMeSwitch.wrapperEl.style.width = 'calc(100% - ' + buttonWidthSum + 'px)';
        this.rememberMeSwitch.titleEl.style.overflow = 'hidden';
        this.rememberMeSwitch.titleEl.style.textOverflow = 'ellipsis';
        this.rememberMeSwitch.titleEl.style.whiteSpace = 'nowrap';
        this.rememberMeSwitch.titleEl.style.width = 'calc(100% - 40px)';
    };
    MobileLoadingModal.prototype.detectOutsideClick = function (e) {
        if (!(e.target === this.modalWrapper || this.modalWrapper.contains(e.target))) {
            this.hide(true);
        }
    };
    MobileLoadingModal.prototype.setTooltipMode = function (state) {
        this.tooltipsActive = state;
        this.prefer3DSwitch.setTooltipMode(state);
        this.preferGeometrySwitch.setTooltipMode(state);
        this.slider.setTooltipMode(state);
        if (state) {
            this.showTooltip();
            this.buttonWrapper.style.pointerEvents = 'none';
            $(this.tooltipCircle).addClass('mobile-native-loading-tooltip-toggle-active');
        }
        else {
            this.buttonWrapper.style.pointerEvents = '';
            $(this.tooltipCircle).removeClass('mobile-native-loading-tooltip-toggle-active');
        }
    };
    MobileLoadingModal.prototype.showTooltip = function () {
        this.tooltipWrapper = document.createElement('div');
        this.tooltipWrapper.className = 'mobile-pane-tooltip';
        this.modalWrapper.appendChild(this.tooltipWrapper);
        var tooltipText = document.createElement('div');
        tooltipText.textContent = ui.translate('ID_MOBILE_HELP_TOOLTIP-MESSAGE');
        tooltipText.className = 'mobile-pane-tooltip-message';
        this.tooltipWrapper.appendChild(tooltipText);
        $(this.tooltipWrapper).show();
        document.addEventListener('touchstart', this.detectTooltipOusideClick);
        document.addEventListener('mousedown', this.detectTooltipOusideClick);
    };
    MobileLoadingModal.prototype.detectTooltipOusideClick = function (e) {
        if (!(e.target === this.tooltipWrapper || this.tooltipWrapper.contains(e.target))) {
            $(this.tooltipWrapper).remove();
            document.removeEventListener('touchstart', this.detectTooltipOusideClick);
            document.removeEventListener('mousedown', this.detectTooltipOusideClick);
        }
    };
    MobileLoadingModal.prototype.detectEscPressed = function (e) {
        e.preventDefault();
        e.stopPropagation();
        if (e.keyCode === 27) {
            this.hide(true);
        }
    };
    MobileLoadingModal.prototype.submit = function (nativeLoadingSettings) {
        var _this = this;
        localStorage.removeItem('nativeLoadingSettings');
        localStorage.setItem('nativeLoadingSettings', JSON.stringify(nativeLoadingSettings));
        var xml = '<Call Method="SetSettings">';
        switch (nativeLoadingSettings.step) {
            case 0:
                xml += _IncludeHiddenNativeLoading;
                break;
            case 1:
                xml += _CompleteNativeLoading;
                break;
            case 2:
                xml += _ModerateNativeLoading;
                break;
            case 3:
                xml += _FastNativeLoading;
                break;
        }
        xml += '</Call>';
        this.client.command.executeXML(xml);
        var prefer2DString = nativeLoadingSettings.prefer2D ? 'Prefer3D' : 'Prefer2D';
        var preferTesselationString = nativeLoadingSettings.preferTesselation === false ? 'PreferGeometry' : 'PreferTessellation';
        xml = '<Call Method="SetSettings"><Settings><Import3D><General><ImportMode>' + prefer2DString + '</ImportMode>';
        xml += '<ReadMode>' + preferTesselationString + '</ReadMode></General ></Import3D ></Settings ></Call > ';
        this.client.command.executeXML(xml);
        this.client.once('SetSettings', function (ev) {
            _this.hide();
        });
    };
    return MobileLoadingModal;
}());
exports.MobileLoadingModal = MobileLoadingModal;


/***/ }),
/* 43 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var app_states_1 = __webpack_require__(5);
var ui = __webpack_require__(0);
var SVG_NS = 'http://www.w3.org/2000/svg';
var XLINK_NS = 'http://www.w3.org/1999/xlink';
var MobilePointer = (function () {
    function MobilePointer(client, canvas) {
        var _this = this;
        this.client = client;
        this.canvas = canvas;
        this.pointerWrapper = document.createElement('div');
        this.pointerWrapper.className = 'mobile-pointer';
        this.pointerWrapper.style.opacity = '0';
        this.pointerWrapper.style.pointerEvents = 'none';
        this.resize();
        var svgWrapper = document.createElementNS(SVG_NS, 'svg');
        svgWrapper.style.width = '100px';
        svgWrapper.style.height = '100px';
        var floatingSvg = document.createElementNS(SVG_NS, 'use');
        floatingSvg.setAttributeNS(XLINK_NS, 'href', '#mobile_picker');
        svgWrapper.appendChild(floatingSvg);
        var handle = document.createElement('div');
        handle.className = 'mobile-pointer-handle';
        $(handle).kendoTouch({
            drag: function (e) {
                _this.pointerDrag(e, _this.pointerWrapper);
            },
            tap: function (e) {
                _this.pointerTap(e);
            }
        });
        this.pointerWrapper.appendChild(handle);
        this.pointerWrapper.appendChild(svgWrapper);
        document.body.appendChild(this.pointerWrapper);
    }
    MobilePointer.prototype.pointerTap = function (ev) {
        var y = this.pointerWrapper.offsetTop;
        var x = this.pointerWrapper.offsetLeft + 100;
        var boundingRect = this.canvas.getBoundingClientRect();
        var offsetLeft = boundingRect.left;
        var offsetTop = boundingRect.top;
        var mouseX = x - offsetLeft;
        var mouseY = y - offsetTop;
        var xml = '<Call Method="MouseMove" Response="true"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>Left</MouseButton></Call>';
        this.client.command.executeXMLMouseMove(xml);
        xml = '<Call Method="MouseDown"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>Left</MouseButton></Call>';
        this.client.command.executeXMLMouse(xml);
        var mouseFlag;
        if (ui.mockCtrl) {
            mouseFlag = '<MouseFlag>Ctrl</MouseFlag>';
        }
        xml = '<Call Method="MouseUp" Response="false"><X>' + mouseX + '</X><Y>' + mouseY + '</Y>';
        xml += mouseFlag + '<MouseButton>Left</MouseButton></Call>';
        this.client.command.executeXMLMouse(xml);
    };
    MobilePointer.prototype.pointerDrag = function (ev, pointerWrapper) {
        ev.event.preventDefault();
        var deltaY = ev.touch.y.location + 25;
        var deltaX = ev.touch.x.location - 25;
        var boundingRect = this.canvas.getBoundingClientRect();
        var offsetLeft = boundingRect.left;
        var offsetTop = boundingRect.top;
        var mouseX = ev.pageX - offsetLeft;
        var mouseY = ev.pageY - offsetTop;
        if (deltaY - 100 >= 0 && deltaY <= window.innerHeight) {
            pointerWrapper.style.bottom = window.innerHeight - deltaY + 'px';
            mouseY = deltaY - 100;
        }
        else {
            mouseY = this.mousePointerEvent.pageY;
        }
        if (deltaX >= 0 && deltaX + 40 <= window.innerWidth) {
            pointerWrapper.style.left = deltaX + 'px';
            mouseX = deltaX + 100;
        }
        else {
            mouseX = this.mousePointerEvent.pageX;
        }
        this.mousePointerEvent = {
            pageX: mouseX,
            pageY: mouseY
        };
        app_states_1.appStates.lastPagePosX = mouseX;
        app_states_1.appStates.lastPagePosY = mouseY;
        mouseX -= offsetLeft;
        mouseY -= offsetTop;
        this.client.mouseX = mouseX;
        this.client.mouseY = mouseY;
        var xml = '<Call Method="MouseMove" Response="true"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>Left</MouseButton></Call>';
        this.client.command.executeXMLMouseMove(xml);
    };
    MobilePointer.prototype.show = function () {
        this.pointerWrapper.style.opacity = '1';
        this.pointerWrapper.style.pointerEvents = '';
    };
    MobilePointer.prototype.hide = function () {
        this.pointerWrapper.style.pointerEvents = 'none';
        this.pointerWrapper.style.opacity = '0';
    };
    MobilePointer.prototype.resize = function () {
        this.pointerWrapper.style.bottom = (document.body.clientHeight / 2 - 100) + 'px';
        this.pointerWrapper.style.left = (document.body.clientWidth / 2 - 100) + 'px';
    };
    return MobilePointer;
}());
exports.MobilePointer = MobilePointer;


/***/ }),
/* 44 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var radiobutton_group_1 = __webpack_require__(47);
var ui = __webpack_require__(0);
var color_picker_1 = __webpack_require__(45);
var mobile_callback_switch_1 = __webpack_require__(46);
var app_states_1 = __webpack_require__(5);
;
;
var MobilePrintModal = (function () {
    function MobilePrintModal(client) {
        var _this = this;
        this.textModuleHeader = '%file';
        this.textModuleFooter = '%file';
        this.orientation = 'Portrait';
        this.isInit = false;
        this.client = client;
        this.isOpen = true;
        this.detectOutsideClick = this.detectOutsideClick.bind(this);
        this.detectEscPressed = this.detectEscPressed.bind(this);
        this.submit = this.submit.bind(this);
        this.resize = this.resize.bind(this);
        this.setWhiteBackgroundSetting = this.setWhiteBackgroundSetting.bind(this);
        this.setLineWidthEnabledSetting = this.setLineWidthEnabledSetting.bind(this);
        this.setTiledSetting = this.setTiledSetting.bind(this);
        this.setFontColorSetting = this.setFontColorSetting.bind(this);
        var settingsCall = '<Call Method="GetSettings"></Call>';
        this.client.command.executeXML(settingsCall);
        this.client.on('GetSettings', function (res) {
            if (_this.isOpen) {
                _this.printSettings = res.Settings.Print;
                _this.orientation = _this.printSettings.Orientation;
                _this.dpi = parseInt(_this.printSettings.PageDPI);
                if (!_this.isInit) {
                    _this.init();
                }
                else {
                    _this.updateUI(_this.printSettings);
                }
                _this.getPrintPreview();
            }
        });
        this.printFontNameEnum = [];
        this.printFontStyleEnum = [
            { langId: "ID_PRINT_MODAL_FONT_REGULAR", value: "0" },
            { langId: "ID_PRINT_MODAL_FONT_BOLD", value: "1" },
            { langId: "ID_PRINT_MODAL_FONT_ITALIC", value: "2" },
            { langId: "ID_PRINT_MODAL_FONT_BOLD_ITALIC", value: "3" },
            { langId: "ID_PRINT_MODAL_FONT_UNDERLINE", value: "4" },
            { langId: "ID_PRINT_MODAL_FONT_STRIKEOUT", value: "8" }
        ];
        try {
            this.printPaperSizeEnum = _PrintPaperSizes;
        }
        catch (e) {
            this.printPaperSizeEnum = [
                { langId: "-", width: -1, height: -1, dpi: 600 },
                { langId: "A0", width: 841, height: 1189, dpi: 200 },
                { langId: "A1", width: 594, height: 841, dpi: 300 },
                { langId: "A2", width: 420, height: 594, dpi: 400 },
                { langId: "A3", width: 297, height: 420, dpi: 500 },
                { langId: "A4", width: 210, height: 297, dpi: 600 },
                { langId: "A5", width: 148, height: 210, dpi: 600 },
                { langId: "Letter", width: 216, height: 279, dpi: 600 },
                { langId: "Legal", width: 216, height: 256, dpi: 600 },
                { langId: "Ledger", width: 279, height: 432, dpi: 500 }
            ];
        }
        document.body.addEventListener('mousedown', this.detectOutsideClick);
        document.body.addEventListener('keyup', this.detectEscPressed);
    }
    MobilePrintModal.prototype.init = function () {
        var _this = this;
        var isLandscape = window.innerHeight < window.innerWidth;
        this.modalWrapper = document.createElement('div');
        this.modalWrapper.className = 'mobile-print-modal';
        var imageWrapper = document.createElement('div');
        imageWrapper.className = 'mobile-print-modal-site-wrapper';
        this.modalWrapper.appendChild(imageWrapper);
        imageWrapper.style.textAlign = 'center';
        var paperSizeWrapper = document.createElement('div');
        imageWrapper.appendChild(paperSizeWrapper);
        var paperSizeText = document.createElement('div');
        paperSizeText.className = 'mobile-print-modal-size-wrapper';
        this.widthEl = document.createElement('span');
        this.widthEl.textContent = this.ppiToMM(this.printSettings.PageWidth) + ' mm';
        paperSizeText.appendChild(this.widthEl);
        var separator = document.createElement('span');
        separator.textContent = ' x ';
        paperSizeText.appendChild(separator);
        this.heightEl = document.createElement('span');
        this.heightEl.textContent = this.ppiToMM(this.printSettings.PageHeight) + ' mm';
        paperSizeText.appendChild(this.heightEl);
        paperSizeWrapper.appendChild(paperSizeText);
        this.paperSizeSelect = document.createElement('select');
        this.paperSizeSelect.style.marginRight = '15px';
        this.paperSizeSelect.style.cssFloat = 'right';
        this.paperSizeSelect.className = 'print-modal-select';
        var pSizeLength = this.printPaperSizeEnum.length;
        for (var i = 0; i < pSizeLength; i++) {
            var option = document.createElement('option');
            option.value = i.toString();
            option.text = ui.translate(this.printPaperSizeEnum[i].langId);
            option.selected = this.isSelectedPaperSize(this.printPaperSizeEnum[i].width, this.printPaperSizeEnum[i].height);
            this.paperSizeSelect.appendChild(option);
        }
        this.paperSizeSelect.onchange = function (e) {
            var i = parseInt(_this.paperSizeSelect.value);
            _this.dpi = _this.printPaperSizeEnum[i].dpi;
            var width = _this.mmToPPI(_this.printPaperSizeEnum[i].width.toString());
            var height = _this.mmToPPI(_this.printPaperSizeEnum[i].height.toString());
            var xml = '<Call Method="SetSettings"><Settings><Print>';
            xml += '<PageWidth>';
            xml += _this.orientation === 'Portrait' ? width : height;
            xml += '</PageWidth>';
            xml += '<PageHeight>';
            xml += _this.orientation === 'Portrait' ? height : width;
            xml += '</PageHeight>';
            xml += '<PageDPI>' + _this.dpi + '</PageDPI>';
            xml += '</Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        paperSizeWrapper.appendChild(this.paperSizeSelect);
        var imageCanvas = document.createElement('div');
        imageCanvas.className = 'mobile-print-image-canvas';
        imageWrapper.appendChild(imageCanvas);
        this.previewImage = document.createElement('img');
        this.previewImage.className = 'mobile-print-modal-preview-image';
        imageCanvas.appendChild(this.previewImage);
        var sizeGroupTitle = document.createElement('div');
        sizeGroupTitle.className = 'mobile-print-modal-option-group-title';
        imageWrapper.appendChild(sizeGroupTitle);
        var sizeToggleIcon = document.createElement('i');
        sizeToggleIcon.className = 'fas fa-chevron-circle-right mobile-pane-group-toggle-icon';
        sizeGroupTitle.appendChild(sizeToggleIcon);
        var sizeTitle = document.createElement('span');
        sizeTitle.textContent = ui.translate('ID_PRINT_MODAL_DOCUMENT_SIZE');
        sizeGroupTitle.appendChild(sizeTitle);
        var sizeGroup = document.createElement('div');
        sizeGroup.className = 'print-modal-option-group';
        sizeGroup.style.textAlign = 'left';
        imageWrapper.appendChild(sizeGroup);
        sizeGroupTitle.onclick = function (e) {
            if ($(sizeToggleIcon).hasClass('fa-chevron-circle-right')) {
                $(sizeToggleIcon).addClass('fa-chevron-circle-down');
                $(sizeToggleIcon).removeClass('fa-chevron-circle-right');
                $(sizeGroup).show();
            }
            else {
                $(sizeToggleIcon).removeClass('fa-chevron-circle-down');
                $(sizeToggleIcon).addClass('fa-chevron-circle-right');
                $(sizeGroup).hide();
            }
        };
        $(sizeGroup).hide();
        var widthWrapper = document.createElement('div');
        widthWrapper.className = 'print-modal-long-input-wrapper';
        sizeGroup.appendChild(widthWrapper);
        var widthTitle = document.createElement('span');
        widthTitle.textContent = ui.translate('ID_TEXT_PAPERWIDTH');
        widthTitle.className = 'print-modal-long-input-title';
        widthWrapper.appendChild(widthTitle);
        this.widthInput = document.createElement('input');
        this.widthInput.className = 'print-modal-long-input';
        this.widthInput.value = this.ppiToMM(this.printSettings.PageWidth).toString();
        this.widthInput.onchange = function (e) {
            var val = _this.mmToPPI(_this.widthInput.value);
            var height = _this.printSettings.PageHeight;
            var xml = '<Call Method="SetSettings"><Settings><Print><PageWidth>' + val + '</PageWidth>';
            xml += val > height ? '<Orientation>Landscape</Orientation>' : '<Orientation>Portrait</Orientation>';
            xml += '</Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        widthWrapper.appendChild(this.widthInput);
        var heightWrapper = document.createElement('div');
        heightWrapper.className = 'print-modal-long-input-wrapper';
        sizeGroup.appendChild(heightWrapper);
        var heightTitle = document.createElement('span');
        heightTitle.textContent = ui.translate('ID_TEXT_PAPERHEIGHT');
        heightTitle.className = 'print-modal-long-input-title';
        heightWrapper.appendChild(heightTitle);
        this.heightInput = document.createElement('input');
        this.heightInput.className = 'print-modal-long-input';
        this.heightInput.value = this.ppiToMM(this.printSettings.PageHeight).toString();
        this.heightInput.onchange = function (e) {
            var val = _this.mmToPPI(_this.heightInput.value);
            var width = _this.printSettings.PageWidth;
            var xml = '<Call Method="SetSettings"><Settings><Print><PageHeight>' + val + '</PageHeight>';
            xml += val > width ? '<Orientation>Portrait</Orientation>' : '<Orientation>Landscape</Orientation>';
            xml += '</Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        heightWrapper.appendChild(this.heightInput);
        var dpiWrapper = document.createElement('div');
        dpiWrapper.className = 'print-modal-long-input-wrapper';
        sizeGroup.appendChild(dpiWrapper);
        var dpiTitle = document.createElement('span');
        dpiTitle.textContent = ui.translate('ID_DPI');
        dpiTitle.className = 'print-modal-long-input-title';
        dpiWrapper.appendChild(dpiTitle);
        this.dpiInput = document.createElement('input');
        this.dpiInput.className = 'print-modal-long-input';
        this.dpiInput.value = this.printSettings.PageDPI;
        this.dpiInput.onchange = function (e) {
            _this.dpi = parseInt(_this.dpiInput.value);
            var xml = '<Call Method="SetSettings"><Settings><Print>';
            xml += '<PageHeight>' + _this.mmToPPI(_this.heightInput.value) + '</PageHeight>';
            xml += '<PageWidth>' + _this.mmToPPI(_this.widthInput.value) + '</PageWidth><PageDPI>' + _this.dpiInput.value + '</PageDPI></Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        dpiWrapper.appendChild(this.dpiInput);
        this.getPrintPreview();
        this.client.on('CreatePrintPreview', function (res) {
            var src = 'data:image/png;base64, ' + res.Image;
            _this.previewImage.src = src;
        });
        var horizontalDivider = document.createElement('hr');
        this.modalWrapper.appendChild(horizontalDivider);
        var optionsWrapper = document.createElement('div');
        optionsWrapper.className = 'mobile-print-modal-site-wrapper';
        this.modalWrapper.appendChild(optionsWrapper);
        this.defaultOptionWrapper = document.createElement('div');
        this.defaultOptionWrapper.className = 'mobile-print-modal-options-wrapper';
        optionsWrapper.appendChild(this.defaultOptionWrapper);
        this.textOptionWrapper = document.createElement('div');
        this.textOptionWrapper.className = 'mobile-print-modal-options-wrapper';
        optionsWrapper.appendChild(this.textOptionWrapper);
        var enumTemplateCall = '<Call Method="GetTemplate"><Type>PrintTemplate</Type></Call>';
        this.client.command.executeXML(enumTemplateCall);
        this.client.on('GetTemplate.PrintTemplate', function (res) {
            _this.fillOptions(res);
            _this.buildTextOptions();
            _this.buildDefaultOptions();
        });
        this.buttonRow = document.createElement('div');
        this.buttonRow.className = 'mobile-print-modal-button-row';
        this.modalWrapper.appendChild(this.buttonRow);
        var hideOpenPDFButton = false;
        try {
            hideOpenPDFButton = _BrowserPrintButtonDeactivated;
        }
        catch (e) { }
        if (!hideOpenPDFButton) {
            var openPDFButton = document.createElement('i');
            openPDFButton.className = 'print-modal-icon fas fa-print';
            openPDFButton.style.marginRight = '15px';
            this.buttonRow.appendChild(openPDFButton);
            openPDFButton.onclick = function (e) {
                _this.createPDF(true);
            };
        }
        var downloadPDFButton = document.createElement('i');
        downloadPDFButton.className = 'print-modal-icon fas fa-download';
        downloadPDFButton.style.marginRight = '15px';
        this.buttonRow.appendChild(downloadPDFButton);
        downloadPDFButton.onclick = function (e) {
            _this.createPDF(false);
        };
        var printButton = document.createElement('button');
        printButton.className = 'print-modal-button';
        printButton.textContent = ui.translate('ID_PRINT_MODAL_PRINT');
        var submitButton = document.createElement('button');
        submitButton.className = 'print-modal-button';
        submitButton.style.cssFloat = 'right';
        submitButton.textContent = 'OK';
        this.buttonRow.appendChild(submitButton);
        submitButton.onclick = this.submit;
        document.body.appendChild(this.modalWrapper);
        window.addEventListener('resize', this.resize);
        if (isLandscape) {
            $(horizontalDivider).hide();
            $(imageWrapper).addClass('mobile-print-modal-image-landscape');
            $(imageWrapper).removeClass('mobile-print-modal-image-portrait');
            $(optionsWrapper).addClass('mobile-print-modal-options-landscape');
            $(optionsWrapper).removeClass('mobile-print-modal-options-portrait');
        }
        else {
            $(horizontalDivider).show();
            $(imageWrapper).removeClass('mobile-print-modal-image-landscape');
            $(imageWrapper).addClass('mobile-print-modal-image-portrait');
            $(optionsWrapper).removeClass('mobile-print-modal-options-landscape');
            $(optionsWrapper).addClass('mobile-print-modal-options-portrait');
        }
        window.onorientationchange = function (e) {
            setTimeout(function () {
                isLandscape = window.innerHeight < window.innerWidth;
                if (isLandscape) {
                    $(horizontalDivider).hide();
                    $(imageWrapper).addClass('mobile-print-modal-image-landscape');
                    $(imageWrapper).removeClass('mobile-print-modal-image-portrait');
                    $(optionsWrapper).addClass('mobile-print-modal-options-landscape');
                    $(optionsWrapper).removeClass('mobile-print-modal-options-portrait');
                }
                else {
                    $(horizontalDivider).show();
                    $(imageWrapper).removeClass('mobile-print-modal-image-landscape');
                    $(imageWrapper).addClass('mobile-print-modal-image-portrait');
                    $(optionsWrapper).removeClass('mobile-print-modal-options-landscape');
                    $(optionsWrapper).addClass('mobile-print-modal-options-portrait');
                }
            }, 1000);
        };
        this.isInit = true;
    };
    MobilePrintModal.prototype.updateUI = function (settings) {
        var sLength = this.printScaleEnum.length;
        for (var i = 0; i < sLength; i++) {
            if (settings.ScaleMode === this.printScaleEnum[i].value) {
                this.scalingGroup.setStateByIndex(i, true);
            }
        }
        this.scaleDynamicValue.value = settings.Scale;
        var alength = this.printOrientationEnum.length;
        this.orientation = settings.Orientation;
        for (var i = 0; i < alength; i++) {
            if (settings.Orientation === this.printOrientationEnum[i].value) {
                this.orientationGroup.setStateByIndex(i, true);
            }
        }
        this.whiteBackgroundSwitch.setVal(settings.WhiteBackground === 'true');
        this.lineWidthSwitch.setVal(settings.LineWidthEnabled === 'true');
        var uLength = this.printLineWidthUnit.length;
        for (var i = 0; i < uLength; i++) {
            if (settings.LineWidthUnit === this.printLineWidthUnit[i].value) {
                this.unitSelect.selectedIndex = i;
            }
        }
        var rLength = this.printRangeEnum.length;
        for (var i = 0; i < rLength; i++) {
            if (settings.PageRange === this.printRangeEnum[i].value) {
                this.rangeGroup.setStateByIndex(i, true);
            }
        }
        this.fromInput.value = settings.PageRangeFirst;
        this.toInput.value = settings.PageRangeLast;
        this.widthInput.value = this.ppiToMM(settings.PageWidth).toString();
        this.widthEl.textContent = this.ppiToMM(settings.PageWidth).toString() + ' mm';
        this.heightInput.value = this.ppiToMM(settings.PageHeight).toString();
        this.heightEl.textContent = this.ppiToMM(settings.PageHeight).toString() + ' mm';
        var pSizeLength = this.printPaperSizeEnum.length;
        var predefinedSize = false;
        for (var i = 0; i < pSizeLength; i++) {
            if (this.isSelectedPaperSize(this.printPaperSizeEnum[i].width, this.printPaperSizeEnum[i].height)) {
                predefinedSize = true;
                this.paperSizeSelect.selectedIndex = i;
            }
        }
        if (!predefinedSize) {
            this.paperSizeSelect.selectedIndex = 0;
        }
        var dpi = settings.PageDPI;
        this.dpiInput.value = dpi;
    };
    MobilePrintModal.prototype.ppiToMM = function (ppi) {
        return Math.round((parseInt(ppi) / this.dpi) * 25.4);
    };
    MobilePrintModal.prototype.mmToPPI = function (mm) {
        return Math.round((parseInt(mm) / 25.4) * this.dpi);
    };
    MobilePrintModal.prototype.isSelectedPaperSize = function (width, height) {
        width = this.mmToPPI(width.toString());
        height = this.mmToPPI(height.toString());
        var actualWidth = parseInt(this.printSettings.PageWidth);
        var actualHeight = parseInt(this.printSettings.PageHeight);
        switch (this.orientation) {
            case 'Portrait':
                return (width === actualWidth && height === actualHeight);
            case 'Landscape':
                return (width === actualHeight && height === actualWidth);
            default:
                return false;
        }
    };
    MobilePrintModal.prototype.getPrintPreview = function () {
        var xml = '<Call Method="CreatePrintPreview">';
        xml += '<Width>200</Width>';
        xml += '<Height>200</Height>';
        xml += '</Call>';
        this.client.command.executeXML(xml);
    };
    MobilePrintModal.prototype.buildDefaultOptions = function () {
        var _this = this;
        $(this.defaultOptionWrapper).empty();
        this.scalingGroup = new radiobutton_group_1.RadioButtonGroup(this.defaultOptionWrapper, 'ID_PRINT_SCALING');
        var sLength = this.printScaleEnum.length;
        var _loop_1 = function (i) {
            var scaleOption = {
                value: this_1.printScaleEnum[i].value,
                langId: this_1.printScaleEnum[i].langId,
                initialState: this_1.printSettings.ScaleMode === this_1.printScaleEnum[i].value
            };
            var callback = function () {
                var xml = '<Call Method="SetSettings"><Settings><Print><ScaleMode>' + scaleOption.value + '</ScaleMode></Print></Settings></Call>';
                _this.client.command.executeXML(xml);
            };
            this_1.scalingGroup.addButton(scaleOption, callback);
            if (scaleOption.value === 'Scale') {
                var scaleStaticValue = document.createElement('div');
                scaleStaticValue.textContent = '1:';
                scaleStaticValue.className = 'print-modal-static-text';
                var buttonAnchor = this_1.scalingGroup.buttons[this_1.scalingGroup.buttons.length - 1].wrapperEl;
                buttonAnchor.appendChild(scaleStaticValue);
                this_1.scaleDynamicValue = document.createElement('input');
                this_1.scaleDynamicValue.className = 'print-modal-input';
                this_1.scaleDynamicValue.value = this_1.printSettings.Scale;
                this_1.scaleDynamicValue.onchange = function (e) {
                    var xml = '<Call Method="SetSettings"><Settings><Print><Scale>' + _this.scaleDynamicValue.value + '</Scale>';
                    xml += '</Print></Settings></Call>';
                    _this.client.command.executeXML(xml);
                };
                buttonAnchor.appendChild(this_1.scaleDynamicValue);
            }
        };
        var this_1 = this;
        for (var i = 0; i < sLength; i++) {
            _loop_1(i);
        }
        this.orientationGroup = new radiobutton_group_1.RadioButtonGroup(this.defaultOptionWrapper, 'ID_PRINT_ORIENTATION');
        var oLength = this.printOrientationEnum.length;
        var _loop_2 = function (i) {
            var orientationOption = {
                value: this_2.printOrientationEnum[i].value,
                langId: this_2.printOrientationEnum[i].langId,
                initialState: this_2.printSettings.Orientation === this_2.printOrientationEnum[i].value
            };
            var callback = function () {
                var xml = '<Call Method="SetSettings"><Settings><Print><Orientation>' + orientationOption.value + '</Orientation>';
                var width = _this.printSettings.PageWidth;
                var height = _this.printSettings.PageHeight;
                switch (orientationOption.value) {
                    case 'Landscape':
                    case 'Portrait':
                        xml += '<PageWidth>' + height + '</PageWidth>';
                        xml += '<PageHeight>' + width + '</PageHeight>';
                        break;
                }
                xml += '</Print></Settings></Call>';
                _this.client.command.executeXML(xml);
            };
            this_2.orientationGroup.addButton(orientationOption, callback);
        };
        var this_2 = this;
        for (var i = 0; i < oLength; i++) {
            _loop_2(i);
        }
        var miscGroupTitle = document.createElement('div');
        miscGroupTitle.className = 'mobile-print-modal-option-group-title';
        this.defaultOptionWrapper.appendChild(miscGroupTitle);
        var miscTitle = document.createElement('span');
        miscTitle.textContent = ui.translate('ID_PRINT_MISC');
        miscGroupTitle.appendChild(miscTitle);
        var miscToggleIcon = document.createElement('i');
        miscToggleIcon.className = 'fas fa-chevron-circle-right mobile-pane-group-toggle-icon';
        miscGroupTitle.appendChild(miscToggleIcon);
        var miscGroup = document.createElement('div');
        miscGroup.className = 'print-modal-option-group';
        this.defaultOptionWrapper.appendChild(miscGroup);
        miscGroupTitle.onclick = function (e) {
            if ($(miscToggleIcon).hasClass('fa-chevron-circle-right')) {
                $(miscToggleIcon).addClass('fa-chevron-circle-down');
                $(miscToggleIcon).removeClass('fa-chevron-circle-right');
                $(miscGroup).show();
            }
            else {
                $(miscToggleIcon).removeClass('fa-chevron-circle-down');
                $(miscToggleIcon).addClass('fa-chevron-circle-right');
                $(miscGroup).hide();
            }
        };
        $(miscGroup).hide();
        this.whiteBackgroundSwitch = new mobile_callback_switch_1.MobileCallbackSwitch(miscGroup, 'ID_WHITE_BACKGROUND', this.setWhiteBackgroundSetting, 'print', true);
        this.lineWidthSwitch = new mobile_callback_switch_1.MobileCallbackSwitch(miscGroup, 'ID_CUSTOM_LINEWIDTH', this.setLineWidthEnabledSetting, 'print', true);
        var lineWidthInput = document.createElement('input');
        lineWidthInput.className = 'print-modal-input';
        lineWidthInput.style.marginLeft = '15px';
        lineWidthInput.value = this.printSettings.LineWidth;
        lineWidthInput.onchange = function (e) {
            var xml = '<Call Method="SetSettings"><Settings><Print><LineWidth>' + lineWidthInput.value + '</LineWidth></Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        this.lineWidthSwitch.wrapperEl.appendChild(lineWidthInput);
        this.unitSelect = document.createElement('select');
        this.unitSelect.className = 'print-modal-select';
        var uLength = this.printLineWidthUnit.length;
        for (var i = 0; i < uLength; i++) {
            var option = document.createElement('option');
            option.value = this.printLineWidthUnit[i].value;
            option.text = ui.translate(this.printLineWidthUnit[i].langId);
            option.selected = this.printSettings.LineWidthUnit === this.printLineWidthUnit[i].value;
            this.unitSelect.appendChild(option);
        }
        this.unitSelect.onchange = function (e) {
            var xml = '<Call Method="SetSettings"><Settings><Print><LineWidthUnit>' + _this.unitSelect.value + '</LineWidthUnit></Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        this.lineWidthSwitch.wrapperEl.appendChild(this.unitSelect);
        this.tiledSwitch = new mobile_callback_switch_1.MobileCallbackSwitch(miscGroup, 'ID_PRINT_TILED', this.setTiledSetting, 'print', false);
        this.rangeGroup = new radiobutton_group_1.RadioButtonGroup(this.defaultOptionWrapper, 'ID_PRINT_RANGE');
        var rLength = this.printRangeEnum.length;
        var _loop_3 = function (i) {
            var rangeOption = {
                value: this_3.printRangeEnum[i].value,
                langId: this_3.printRangeEnum[i].langId,
                initialState: this_3.printSettings.PageRange === this_3.printRangeEnum[i].value
            };
            var callback = function () {
                var xml = '<Call Method="SetSettings"><Settings><Print><PageRange>' + rangeOption.value + '</PageRange></Print></Settings></Call>';
                _this.client.command.executeXML(xml);
            };
            this_3.rangeGroup.addButton(rangeOption, callback);
            if (rangeOption.value === 'Range') {
                var fromTitle = document.createElement('div');
                fromTitle.textContent = ui.translate('ID_PRINT_RANGE_FROM') + ':';
                fromTitle.className = 'print-modal-static-text';
                var buttonAnchor = this_3.rangeGroup.buttons[this_3.rangeGroup.buttons.length - 1].wrapperEl;
                buttonAnchor.appendChild(fromTitle);
                this_3.fromInput = document.createElement('input');
                this_3.fromInput.className = 'print-modal-input';
                this_3.fromInput.value = this_3.printSettings.PageRangeFirst;
                this_3.fromInput.onchange = function (e) {
                    var xml = '<Call Method="SetSettings"><Settings><Print><PageRangeFirst>' + _this.fromInput.value + '</PageRangeFirst></Print></Settings></Call>';
                    _this.client.command.executeXML(xml);
                };
                buttonAnchor.appendChild(this_3.fromInput);
                var toTitle = document.createElement('div');
                toTitle.textContent = ui.translate('ID_PRINT_RANGE_TO') + ':';
                toTitle.className = 'print-modal-static-text';
                buttonAnchor.appendChild(toTitle);
                this_3.toInput = document.createElement('input');
                this_3.toInput.className = 'print-modal-input';
                this_3.toInput.value = this_3.printSettings.PageRangeLast;
                this_3.toInput.onchange = function (e) {
                    var xml = '<Call Method="SetSettings"><Settings><Print><PageRangeLast>' + _this.toInput.value + '</PageRangeLast></Print></Settings></Call>';
                    _this.client.command.executeXML(xml);
                };
                buttonAnchor.appendChild(this_3.toInput);
            }
        };
        var this_3 = this;
        for (var i = 0; i < rLength; i++) {
            _loop_3(i);
        }
    };
    MobilePrintModal.prototype.buildTextOptions = function () {
        var _this = this;
        $(this.textOptionWrapper).empty();
        var headerGroupTitle = document.createElement('div');
        headerGroupTitle.className = 'mobile-print-modal-option-group-title';
        this.textOptionWrapper.appendChild(headerGroupTitle);
        var headerTitle = document.createElement('span');
        headerTitle.textContent = ui.translate('ID_PRINT_HEADER');
        headerGroupTitle.appendChild(headerTitle);
        var headerToggleIcon = document.createElement('i');
        headerToggleIcon.className = 'fas fa-chevron-circle-right mobile-pane-group-toggle-icon';
        headerGroupTitle.appendChild(headerToggleIcon);
        var headerGroup = document.createElement('div');
        headerGroup.className = 'print-modal-option-group';
        this.textOptionWrapper.appendChild(headerGroup);
        headerGroupTitle.onclick = function (e) {
            if ($(headerToggleIcon).hasClass('fa-chevron-circle-right')) {
                $(headerToggleIcon).addClass('fa-chevron-circle-down');
                $(headerToggleIcon).removeClass('fa-chevron-circle-right');
                $(headerGroup).show();
            }
            else {
                $(headerToggleIcon).removeClass('fa-chevron-circle-down');
                $(headerToggleIcon).addClass('fa-chevron-circle-right');
                $(headerGroup).hide();
            }
        };
        $(headerGroup).hide();
        var headerLeftWrapper = document.createElement('div');
        headerLeftWrapper.className = 'print-modal-long-input-wrapper';
        headerGroup.appendChild(headerLeftWrapper);
        var headerLeftTitle = document.createElement('span');
        headerLeftTitle.className = 'print-modal-long-input-title';
        headerLeftTitle.textContent = ui.translate('ID_PRINT_HEADER_LEFT');
        headerLeftWrapper.appendChild(headerLeftTitle);
        var headerLeftInput = document.createElement('input');
        headerLeftInput.className = 'print-modal-long-input';
        headerLeftInput.value = this.printSettings.HeaderLeft;
        this.activeTextInputHeader = headerLeftInput;
        headerLeftInput.onchange = function (e) {
            var xml = '<Call Method="SetSettings"><Settings><Print><HeaderLeft>' + headerLeftInput.value + '</HeaderLeft></Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        headerLeftInput.onfocus = function (e) {
            _this.activeTextInputHeader = headerLeftInput;
        };
        headerLeftWrapper.appendChild(headerLeftInput);
        var headerCenterWrapper = document.createElement('div');
        headerCenterWrapper.className = 'print-modal-long-input-wrapper';
        headerGroup.appendChild(headerCenterWrapper);
        var headerCenterTitle = document.createElement('span');
        headerCenterTitle.textContent = ui.translate('ID_PRINT_HEADER_CENTER');
        headerCenterTitle.className = 'print-modal-long-input-title';
        headerCenterWrapper.appendChild(headerCenterTitle);
        var headerCenterInput = document.createElement('input');
        headerCenterInput.className = 'print-modal-long-input';
        headerCenterInput.value = this.printSettings.HeaderCenter;
        headerCenterInput.onchange = function (e) {
            var xml = '<Call Method="SetSettings"><Settings><Print><HeaderCenter>' + headerCenterInput.value + '</HeaderCenter></Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        headerCenterInput.onfocus = function (e) {
            _this.activeTextInputHeader = headerCenterInput;
        };
        headerCenterWrapper.appendChild(headerCenterInput);
        var headerRightWrapper = document.createElement('div');
        headerRightWrapper.className = 'print-modal-long-input-wrapper';
        headerGroup.appendChild(headerRightWrapper);
        var headerRightTitle = document.createElement('span');
        headerRightTitle.textContent = ui.translate('ID_PRINT_HEADER_RIGHT');
        headerRightTitle.className = 'print-modal-long-input-title';
        headerRightWrapper.appendChild(headerRightTitle);
        var headerRightInput = document.createElement('input');
        headerRightInput.className = 'print-modal-long-input';
        headerRightInput.value = this.printSettings.HeaderRight;
        headerRightInput.onchange = function (e) {
            var xml = '<Call Method="SetSettings"><Settings><Print><HeaderRight>' + headerRightInput.value + '</HeaderRight></Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        headerRightInput.onfocus = function (e) {
            _this.activeTextInputHeader = headerRightInput;
        };
        headerRightWrapper.appendChild(headerRightInput);
        var textActionButtonHeader = document.createElement('button');
        textActionButtonHeader.className = 'print-modal-button';
        textActionButtonHeader.textContent = ui.translate('ID_PRINT_INSERT_VARIABLE');
        headerGroup.appendChild(textActionButtonHeader);
        var textActionSelectHeader = document.createElement('select');
        textActionSelectHeader.className = 'print-modal-select';
        headerGroup.appendChild(textActionSelectHeader);
        var textActionFileOptionHeader = document.createElement('option');
        textActionFileOptionHeader.value = '%file';
        textActionFileOptionHeader.selected = true;
        textActionFileOptionHeader.text = ui.translate('ID_PRINT_INSERT_VARIABLE_FILE_NAME');
        textActionSelectHeader.appendChild(textActionFileOptionHeader);
        var textActionDateOptionHeader = document.createElement('option');
        textActionDateOptionHeader.value = '%date';
        textActionDateOptionHeader.text = ui.translate('ID_PRINT_INSERT_VARIABLE_DATE');
        textActionSelectHeader.appendChild(textActionDateOptionHeader);
        var textActionTimeOptionHeader = document.createElement('option');
        textActionTimeOptionHeader.value = '%time';
        textActionTimeOptionHeader.text = ui.translate('ID_PRINT_INSERT_VARIABLE_TIME');
        textActionSelectHeader.appendChild(textActionTimeOptionHeader);
        var textActionUserHeader = document.createElement('option');
        textActionUserHeader.value = '%user';
        textActionUserHeader.text = ui.translate('ID_PRINT_INSERT_VARIABLE_USER_NAME');
        textActionSelectHeader.appendChild(textActionUserHeader);
        textActionSelectHeader.onchange = function (e) {
            _this.textModuleHeader = textActionSelectHeader.value;
        };
        textActionButtonHeader.onclick = function (e) {
            var tempVal = _this.activeTextInputHeader.value;
            tempVal += _this.textModuleHeader;
            $(_this.activeTextInputHeader).val(tempVal);
            $(_this.activeTextInputHeader).trigger('change');
        };
        var footerGroupTitle = document.createElement('div');
        footerGroupTitle.className = 'mobile-print-modal-option-group-title';
        this.textOptionWrapper.appendChild(footerGroupTitle);
        var footerTitle = document.createElement('span');
        footerTitle.textContent = ui.translate('ID_PRINT_FOOTER');
        footerGroupTitle.appendChild(footerTitle);
        var footerToggleIcon = document.createElement('i');
        footerToggleIcon.className = 'fas fa-chevron-circle-right mobile-pane-group-toggle-icon';
        footerGroupTitle.appendChild(footerToggleIcon);
        var footerGroup = document.createElement('div');
        footerGroup.className = 'print-modal-option-group';
        this.textOptionWrapper.appendChild(footerGroup);
        footerGroupTitle.onclick = function (e) {
            if ($(footerToggleIcon).hasClass('fa-chevron-circle-right')) {
                $(footerToggleIcon).addClass('fa-chevron-circle-down');
                $(footerToggleIcon).removeClass('fa-chevron-circle-right');
                $(footerGroup).show();
            }
            else {
                $(footerToggleIcon).removeClass('fa-chevron-circle-down');
                $(footerToggleIcon).addClass('fa-chevron-circle-right');
                $(footerGroup).hide();
            }
        };
        $(footerGroup).hide();
        var footerLeftWrapper = document.createElement('div');
        footerLeftWrapper.className = 'print-modal-long-input-wrapper';
        footerGroup.appendChild(footerLeftWrapper);
        var footerLeftTitle = document.createElement('span');
        footerLeftTitle.textContent = ui.translate('ID_PRINT_FOOTER_LEFT');
        footerLeftTitle.className = 'print-modal-long-input-title';
        footerLeftWrapper.appendChild(footerLeftTitle);
        var footerLeftInput = document.createElement('input');
        footerLeftInput.className = 'print-modal-long-input';
        footerLeftInput.value = this.printSettings.FooterLeft;
        footerLeftInput.onchange = function (e) {
            var xml = '<Call Method="SetSettings"><Settings><Print><FooterLeft>' + footerLeftInput.value + '</FooterLeft></Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        footerLeftInput.onfocus = function (e) {
            _this.activeTextInputFooter = footerLeftInput;
        };
        headerRightWrapper.appendChild(headerRightInput);
        footerLeftWrapper.appendChild(footerLeftInput);
        var footerCenterWrapper = document.createElement('div');
        footerCenterWrapper.className = 'print-modal-long-input-wrapper';
        footerGroup.appendChild(footerCenterWrapper);
        var footerCenterTitle = document.createElement('span');
        footerCenterTitle.textContent = ui.translate('ID_PRINT_FOOTER_CENTER');
        footerCenterTitle.className = 'print-modal-long-input-title';
        footerCenterWrapper.appendChild(footerCenterTitle);
        var footerCenterInput = document.createElement('input');
        footerCenterInput.className = 'print-modal-long-input';
        footerCenterInput.value = this.printSettings.FooterCenter;
        footerCenterInput.onchange = function (e) {
            var xml = '<Call Method="SetSettings"><Settings><Print><FooterCenter>' + footerCenterInput.value + '</FooterCenter></Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        footerCenterInput.onfocus = function (e) {
            _this.activeTextInputFooter = footerCenterInput;
        };
        footerCenterWrapper.appendChild(footerCenterInput);
        var footerRightWrapper = document.createElement('div');
        footerRightWrapper.className = 'print-modal-long-input-wrapper';
        footerGroup.appendChild(footerRightWrapper);
        var footerRightTitle = document.createElement('span');
        footerRightTitle.textContent = ui.translate('ID_PRINT_FOOTER_RIGHT');
        footerRightTitle.className = 'print-modal-long-input-title';
        footerRightWrapper.appendChild(footerRightTitle);
        var footerRightInput = document.createElement('input');
        footerRightInput.className = 'print-modal-long-input';
        footerRightInput.value = this.printSettings.FooterRight;
        footerRightInput.onchange = function (e) {
            var xml = '<Call Method="SetSettings"><Settings><Print><FooterRight>' + footerRightInput.value + '</FooterRight></Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        footerRightInput.onfocus = function (e) {
            _this.activeTextInputFooter = footerRightInput;
        };
        footerRightWrapper.appendChild(footerRightInput);
        var textActionButtonFooter = document.createElement('button');
        textActionButtonFooter.className = 'print-modal-button';
        textActionButtonFooter.textContent = ui.translate('ID_PRINT_INSERT_VARIABLE');
        footerGroup.appendChild(textActionButtonFooter);
        var textActionSelectFooter = document.createElement('select');
        textActionSelectFooter.className = 'print-modal-select';
        footerGroup.appendChild(textActionSelectFooter);
        var textActionFileOptionFooter = document.createElement('option');
        textActionFileOptionFooter.value = '%file';
        textActionFileOptionFooter.selected = true;
        textActionFileOptionFooter.text = ui.translate('ID_PRINT_INSERT_VARIABLE_FILE_NAME');
        textActionSelectFooter.appendChild(textActionFileOptionFooter);
        var textActionDateOptionFooter = document.createElement('option');
        textActionDateOptionFooter.value = '%date';
        textActionDateOptionFooter.text = ui.translate('ID_PRINT_INSERT_VARIABLE_DATE');
        textActionSelectFooter.appendChild(textActionDateOptionFooter);
        var textActionTimeOptionFooter = document.createElement('option');
        textActionTimeOptionFooter.value = '%time';
        textActionTimeOptionFooter.text = ui.translate('ID_PRINT_INSERT_VARIABLE_TIME');
        textActionSelectFooter.appendChild(textActionTimeOptionFooter);
        var textActionUserFooter = document.createElement('option');
        textActionUserFooter.value = '%user';
        textActionUserFooter.text = ui.translate('ID_PRINT_INSERT_VARIABLE_USER_NAME');
        textActionSelectFooter.appendChild(textActionUserFooter);
        textActionSelectFooter.onchange = function (e) {
            _this.textModuleFooter = textActionSelectFooter.value;
        };
        textActionButtonFooter.onclick = function (e) {
            var tempVal = _this.activeTextInputFooter.value;
            tempVal += _this.textModuleFooter;
            $(_this.activeTextInputFooter).val(tempVal);
            $(_this.activeTextInputFooter).trigger('change');
        };
        var fontGroupTitle = document.createElement('div');
        fontGroupTitle.className = 'mobile-print-modal-option-group-title';
        this.textOptionWrapper.appendChild(fontGroupTitle);
        var fontTitle = document.createElement('span');
        fontTitle.textContent = ui.translate('ID_PRINT_MODAL_FONT');
        fontGroupTitle.appendChild(fontTitle);
        var fontToggleIcon = document.createElement('i');
        fontToggleIcon.className = 'fas fa-chevron-circle-right mobile-pane-group-toggle-icon';
        fontGroupTitle.appendChild(fontToggleIcon);
        var fontGroup = document.createElement('div');
        fontGroup.className = 'print-modal-option-group';
        this.textOptionWrapper.appendChild(fontGroup);
        fontGroupTitle.onclick = function (e) {
            if ($(fontToggleIcon).hasClass('fa-chevron-circle-right')) {
                $(fontToggleIcon).addClass('fa-chevron-circle-down');
                $(fontToggleIcon).removeClass('fa-chevron-circle-right');
                $(fontGroup).show();
            }
            else {
                $(fontToggleIcon).removeClass('fa-chevron-circle-down');
                $(fontToggleIcon).addClass('fa-chevron-circle-right');
                $(fontGroup).hide();
            }
        };
        $(fontGroup).hide();
        var fontNameWrapper = document.createElement('div');
        fontNameWrapper.className = 'print-modal-long-input-wrapper';
        fontGroup.appendChild(fontNameWrapper);
        var fontNameTitle = document.createElement('div');
        fontNameTitle.className = 'print-modal-long-input-title';
        fontNameTitle.textContent = ui.translate('ID_PRINT_SET_FONT');
        fontNameWrapper.appendChild(fontNameTitle);
        var fontName = document.createElement('select');
        fontName.className = 'print-modal-select';
        var xml = '<Call Method="GetFonts"></Call>';
        this.client.command.executeXML(xml);
        fontName.onchange = function (e) {
            var xml = '<Call Method="SetSettings"><Settings><Print><BannerFontName>' + fontName.value + '</BannerFontName></Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        fontNameWrapper.appendChild(fontName);
        this.client.on('GetFonts', function (res) {
            _this.printFontNameEnum = res.Font;
            var fontCount = _this.printFontNameEnum.length;
            for (var i = 0; i < fontCount; i++) {
                var fontNameOptionItem = _this.printFontNameEnum[i];
                var fontNameOption = document.createElement('option');
                fontNameOption.text = fontNameOptionItem;
                fontNameOption.value = fontNameOptionItem;
                if (_this.printSettings.BannerFontName === fontNameOptionItem) {
                    fontNameOption.selected = true;
                }
                fontName.appendChild(fontNameOption);
            }
        });
        var fontSizeWrapper = document.createElement('div');
        fontSizeWrapper.className = 'print-modal-long-input-wrapper';
        fontGroup.appendChild(fontSizeWrapper);
        var fontSizeTitle = document.createElement('div');
        fontSizeTitle.className = 'print-modal-long-input-title';
        fontSizeTitle.textContent = ui.translate('ID_FONT_SIZE');
        fontSizeWrapper.appendChild(fontSizeTitle);
        var fontSize = document.createElement('input');
        fontSize.className = 'print-modal-input';
        fontSize.value = this.printSettings.BannerFontSize;
        fontSize.onchange = function (e) {
            var xml = '<Call Method="SetSettings"><Settings><Print><BannerFontSize>' + fontSize.value + '</BannerFontSize></Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        fontSizeWrapper.appendChild(fontSize);
        var fontStyleWrapper = document.createElement('div');
        fontStyleWrapper.className = 'print-modal-long-input-wrapper';
        fontGroup.appendChild(fontStyleWrapper);
        var fontStyleTitle = document.createElement('div');
        fontStyleTitle.className = 'print-modal-long-input-title';
        fontStyleTitle.textContent = ui.translate('ID_PRINT_MODAL_FONT_STYLE');
        fontStyleWrapper.appendChild(fontStyleTitle);
        var fontStyle = document.createElement('select');
        fontStyle.className = 'print-modal-select';
        var fontStyleCount = this.printFontStyleEnum.length;
        for (var i = 0; i < fontStyleCount; i++) {
            var fontStyleOptionItem = this.printFontStyleEnum[i];
            var fontStyleOption = document.createElement('option');
            fontStyleOption.text = ui.translate(fontStyleOptionItem.langId);
            fontStyleOption.value = fontStyleOptionItem.value;
            if (this.printSettings.BannerFontStyle === fontStyleOptionItem.value) {
                fontStyleOption.selected = true;
            }
            fontStyle.appendChild(fontStyleOption);
        }
        fontStyle.onchange = function (e) {
            var xml = '<Call Method="SetSettings"><Settings><Print><BannerFontStyle>' + fontStyle.value + '</BannerFontStyle></Print></Settings></Call>';
            _this.client.command.executeXML(xml);
        };
        fontStyleWrapper.appendChild(fontStyle);
        var fontColorWrapper = document.createElement('div');
        fontColorWrapper.className = 'print-modal-long-input-wrapper';
        fontGroup.appendChild(fontColorWrapper);
        var fontColorTitle = document.createElement('div');
        fontColorTitle.className = 'print-modal-long-input-title';
        fontColorTitle.textContent = ui.translate('ID_TEXT_COLOR');
        fontColorWrapper.appendChild(fontColorTitle);
        this.fontColor = new color_picker_1.ModalColorPicker(fontColorWrapper, this.setFontColorSetting);
        this.fontColor.setColor(this.printSettings.BannerFontColor);
    };
    MobilePrintModal.prototype.createPDF = function (open) {
        var xml = '<Call Method="GetPrintImageAsPDF"></Call>';
        this.client.command.executeXML(xml);
        ui.blockUI();
        var name = app_states_1.appStates.fileName ? app_states_1.appStates.fileName + '.pdf' : 'print.pdf';
        this.client.once('GetPrintImageAsPDF', function (res) {
            ui.unblockUI();
            var byteCharacters = atob(res.Image);
            var byteNumbers = new Array(byteCharacters.length);
            for (var i = 0; i < byteCharacters.length; i++) {
                byteNumbers[i] = byteCharacters.charCodeAt(i);
            }
            var byteArray = new Uint8Array(byteNumbers);
            var blob = new Blob([byteArray], {
                type: 'application/pdf'
            });
            if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                if (open) {
                    window.navigator.msSaveOrOpenBlob(blob);
                }
                else {
                    window.navigator.msSaveOrOpenBlob(blob, name);
                }
            }
            else {
                var blobUrl = URL.createObjectURL(blob);
                if (open) {
                    window.open(blobUrl);
                }
                else {
                    var link = document.createElement('a');
                    link.href = URL.createObjectURL(blob);
                    link.download = name;
                    document.body.appendChild(link);
                    link.click();
                    link.remove();
                }
            }
        });
    };
    MobilePrintModal.prototype.setFontColorSetting = function (color) {
        var xml = '<Call Method="SetSettings"><Settings><Print><BannerFontColor>' + color + '</BannerFontColor></Print></Settings></Call>';
        this.client.command.executeXML(xml);
    };
    MobilePrintModal.prototype.setWhiteBackgroundSetting = function (state) {
        var xml = '<Call Method="SetSettings"><Settings><Print><WhiteBackground>' + state + '</WhiteBackground></Print></Settings></Call>';
        this.client.command.executeXML(xml);
    };
    MobilePrintModal.prototype.setLineWidthEnabledSetting = function (state) {
        var xml = '<Call Method="SetSettings"><Settings><Print><LineWidthEnabled>' + state + '</LineWidthEnabled></Print></Settings></Call>';
        this.client.command.executeXML(xml);
    };
    MobilePrintModal.prototype.setTiledSetting = function (state) {
        var xml = '<Call Method="SetSettings"><Settings><Print><Tiled>' + state + '</Tiled></Print></Settings></Call>';
        this.client.command.executeXML(xml);
    };
    MobilePrintModal.prototype.fillOptions = function (template) {
        this.printRangeEnum = [];
        this.printScaleEnum = [];
        this.printOrientationEnum = [];
        this.printLineWidthUnit = [];
        var enums = template.Source.Template.Enum;
        var range = enums[0].Options.Option;
        var rangeLength = range.length;
        for (var r = 0; r < rangeLength; r++) {
            var rangeOpt = {
                value: range[r].Value,
                langId: range[r].DisplayName
            };
            this.printRangeEnum.push(rangeOpt);
        }
        var scale = enums[1].Options.Option;
        var scaleLength = scale.length;
        for (var s = 0; s < scaleLength; s++) {
            var scaleOpt = {
                value: scale[s].Value,
                langId: scale[s].DisplayName
            };
            this.printScaleEnum.push(scaleOpt);
        }
        var orientation = enums[2].Options.Option;
        var orientationLength = orientation.length;
        for (var o = 0; o < orientationLength; o++) {
            var orientationOpt = {
                value: orientation[o].Value,
                langId: orientation[o].DisplayName
            };
            this.printOrientationEnum.push(orientationOpt);
        }
        var unit = enums[3].Options.Option;
        var unitLength = unit.length;
        for (var u = 0; u < unitLength; u++) {
            var unitOpt = {
                value: unit[u].Value,
                langId: unit[u].DisplayName
            };
            this.printLineWidthUnit.push(unitOpt);
        }
    };
    MobilePrintModal.prototype.resize = function () {
    };
    MobilePrintModal.prototype.hide = function () {
        window.removeEventListener('resize', this.resize);
        document.body.removeEventListener('mousedown', this.detectOutsideClick);
        document.body.removeEventListener('keyup', this.detectEscPressed);
        this.isOpen = false;
        $(this.modalWrapper).remove();
    };
    MobilePrintModal.prototype.detectOutsideClick = function (e) {
        if (!(e.target === this.modalWrapper || this.modalWrapper.contains(e.target) || $('.wv-color-picker-popup').find(e.target).length > 0)) {
            this.submit();
        }
    };
    MobilePrintModal.prototype.detectEscPressed = function (e) {
        e.preventDefault();
        e.stopPropagation();
        if (e.keyCode === 27) {
            this.submit();
        }
    };
    MobilePrintModal.prototype.submit = function () {
        this.hide();
    };
    return MobilePrintModal;
}());
exports.MobilePrintModal = MobilePrintModal;


/***/ }),
/* 45 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var custom_palette_1 = __webpack_require__(30);
var tinycolor = __webpack_require__(10);
var ModalColorPicker = (function () {
    function ModalColorPicker(parentEl, callback) {
        var _this = this;
        this.isDefaultSet = false;
        this.parentEl = parentEl;
        this.callback = callback;
        this.colorpicker = document.createElement('span');
        this.colorpicker.classList.add('modal-colorpicker');
        this.parentEl.appendChild(this.colorpicker);
        this.colorpicker.addEventListener('click', function (ev) {
            _this.openPalette();
        }, false);
    }
    ModalColorPicker.prototype.openPalette = function () {
        var _this = this;
        var activeColor = this.colorpicker.dataset['lastPickedColor'];
        this.palette = new custom_palette_1.CustomPalette(activeColor, this.showAlpha);
        this.palette.on('customColorChosen', function (color) { return _this.updateElementState(color); });
    };
    ModalColorPicker.prototype.updateElementState = function (color) {
        this.colorpicker.dataset['lastPickedColor'] = color;
        this.colorpicker.style.backgroundColor = color;
        var hashHex = tinycolor(color).toHexString();
        this.color = hashHex;
        this.callback(this.color);
    };
    ModalColorPicker.prototype.setColor = function (hexColor) {
        var RGBAColor = hexToRgba(hexColor);
        this.alpha = RGBAColor.a;
        var cssColor;
        if (this.showAlpha) {
            cssColor = "rgba(" + RGBAColor.r + ", " + RGBAColor.g + ", " + RGBAColor.b + ", " + this.alpha + ")";
        }
        else {
            cssColor = "rgb(" + RGBAColor.r + ", " + RGBAColor.g + ", " + RGBAColor.b + ")";
        }
        if (!this.isDefaultSet) {
            this.colorpicker.dataset['defaultColor'] = cssColor;
            this.isDefaultSet = true;
        }
        this.colorpicker.dataset['lastPickedColor'] = cssColor;
        this.colorpicker.style.backgroundColor = cssColor;
    };
    return ModalColorPicker;
}());
exports.ModalColorPicker = ModalColorPicker;
function hexToRgba(hex) {
    var bigint, r, g, b, a;
    var aRgb;
    if (hex[0] == '#') {
        aRgb = hex.substr(1);
    }
    else {
        aRgb = hex.substr(2);
    }
    bigint = parseInt(aRgb, 16);
    r = (bigint >> 16) & 255;
    g = (bigint >> 8) & 255;
    b = bigint & 255;
    var rgb = r + "," + g + "," + b;
    a = ((bigint >> 24) & 255) / 255;
    return {
        a: a,
        r: r,
        g: g,
        b: b
    };
}


/***/ }),
/* 46 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var ui = __webpack_require__(0);
var MobileCallbackSwitch = (function () {
    function MobileCallbackSwitch(parent, titleId, callBack, preClass, initialVal) {
        var _this = this;
        this.val = false;
        this.parent = parent;
        this.wrapperEl = document.createElement('div');
        this.wrapperEl.className = preClass + '-browser-switch-wrapper';
        this.parent.appendChild(this.wrapperEl);
        this.callBack = callBack;
        if (initialVal) {
            this.val = initialVal;
        }
        this.hiddenCheckbox = document.createElement('input');
        this.hiddenCheckbox.type = 'checkbox';
        this.hiddenCheckbox.value = this.val + '';
        this.hiddenCheckbox.style.display = 'none';
        this.wrapperEl.appendChild(this.hiddenCheckbox);
        this.titleEl = document.createElement('div');
        this.titleEl.className = preClass + '-browser-switch-title';
        this.titleEl.id = titleId;
        this.titleEl.textContent = ui.translate(titleId);
        this.wrapperEl.appendChild(this.titleEl);
        this.boxEl = document.createElement('div');
        this.boxEl.className = 'switch-wrapper switch';
        var statusClass = this.val ? 'switch-on' : 'switch-off';
        $(this.boxEl).addClass(statusClass);
        this.wrapperEl.appendChild(this.boxEl);
        this.handleEl = document.createElement('div');
        this.handleEl.className = 'switch-handle';
        this.boxEl.appendChild(this.handleEl);
        $(this.boxEl).click(function (e) {
            _this.val = !_this.val;
            var val = _this.val ? 'true' : 'false';
            $(_this.hiddenCheckbox).val(val);
            $(_this.hiddenCheckbox).trigger('change');
            if (_this.val) {
                $(_this.boxEl).removeClass('switch-off').addClass('switch-on');
            }
            else {
                $(_this.boxEl).removeClass('switch-on').addClass('switch-off');
            }
            if (_this.callBack) {
                _this.callBack(_this.val);
            }
        });
        this.setVal(this.val);
    }
    MobileCallbackSwitch.prototype.getTitleEl = function () {
        return this.titleEl;
    };
    MobileCallbackSwitch.prototype.getClickEl = function () {
        return this.hiddenCheckbox;
    };
    MobileCallbackSwitch.prototype.getVal = function () {
        return this.val;
    };
    MobileCallbackSwitch.prototype.setVal = function (val) {
        var valString = val ? 'true' : 'false';
        this.val = val;
        $(this.hiddenCheckbox).val(valString);
        $(this.hiddenCheckbox).trigger('change');
        if (this.val) {
            $(this.boxEl).removeClass('switch-off').addClass('switch-on');
        }
        else {
            $(this.boxEl).removeClass('switch-on').addClass('switch-off');
        }
    };
    return MobileCallbackSwitch;
}());
exports.MobileCallbackSwitch = MobileCallbackSwitch;


/***/ }),
/* 47 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var ui = __webpack_require__(0);
var RadioButtonGroup = (function () {
    function RadioButtonGroup(parentEl, titleId, initCollapsed) {
        var _this = this;
        this.buttons = [];
        this.newButtonIndex = 0;
        this.isCollapsed = true;
        this.parentEl = parentEl;
        this.titleId = titleId;
        if (initCollapsed) {
            this.isCollapsed = initCollapsed;
        }
        this.groupWrapper = document.createElement('div');
        this.groupWrapper.className = 'radio-button-group';
        this.parentEl.appendChild(this.groupWrapper);
        this.titleEl = document.createElement('div');
        this.titleEl.className = 'mobile-radio-button-group-title';
        this.groupWrapper.appendChild(this.titleEl);
        var title = document.createElement('span');
        title.textContent = ui.translate(titleId);
        this.titleEl.appendChild(title);
        var toggleIcon = document.createElement('i');
        toggleIcon.className = 'fas fa-chevron-circle-right mobile-pane-group-toggle-icon';
        this.titleEl.appendChild(toggleIcon);
        this.titleEl.onclick = function (e) {
            if (_this.isCollapsed) {
                $(toggleIcon).addClass('fa-chevron-circle-down');
                $(toggleIcon).removeClass('fa-chevron-circle-right');
                $(_this.buttonGroup).show();
            }
            else {
                $(toggleIcon).removeClass('fa-chevron-circle-down');
                $(toggleIcon).addClass('fa-chevron-circle-right');
                $(_this.buttonGroup).hide();
            }
            _this.isCollapsed = !_this.isCollapsed;
        };
        this.buttonGroup = document.createElement('div');
        this.buttonGroup.className = 'radio-button-group-content';
        this.groupWrapper.appendChild(this.buttonGroup);
        $(this.buttonGroup).hide();
    }
    RadioButtonGroup.prototype.toggleStates = function (index) {
        var length = this.buttons.length;
        for (var i = 0; i < length; i++) {
            if (i !== index) {
                this.buttons[i].setState(false);
            }
        }
    };
    RadioButtonGroup.prototype.setStateByIndex = function (index, state) {
        if (index < this.buttons.length) {
            this.buttons[index].setState(state);
        }
    };
    RadioButtonGroup.prototype.addButton = function (item, callback) {
        var button = new RadioButton(this.buttonGroup, item, this, this.newButtonIndex, callback);
        this.buttons.push(button);
        button.setState(item.initialState);
        this.newButtonIndex++;
    };
    RadioButtonGroup.prototype.setTitle = function () {
        var length = this.buttons.length;
        for (var i = 0; i < length; i++) {
            this.buttons[i].setTitle();
        }
        this.titleEl.textContent = ui.translate(this.titleId);
    };
    return RadioButtonGroup;
}());
exports.RadioButtonGroup = RadioButtonGroup;
var RadioButton = (function () {
    function RadioButton(parentEl, item, group, index, callback) {
        var _this = this;
        this.parentEl = parentEl;
        this.item = item;
        this.group = group;
        this.titleId = item.langId;
        this.index = index;
        this.callback = callback;
        this.wrapperEl = document.createElement('div');
        this.wrapperEl.className = 'radio-button-wrapper';
        this.parentEl.appendChild(this.wrapperEl);
        var outerCircle = document.createElement('div');
        outerCircle.className = 'radio-button-container';
        this.wrapperEl.appendChild(outerCircle);
        this.innerCircle = document.createElement('div');
        this.innerCircle.className = 'radio-button-fill';
        outerCircle.appendChild(this.innerCircle);
        this.titleEl = document.createElement('div');
        this.titleEl.className = 'radio-button-title';
        this.titleEl.textContent = ui.translate(this.titleId);
        this.wrapperEl.appendChild(this.titleEl);
        outerCircle.onclick = function (e) {
            if (!_this.state) {
                if (_this.callback) {
                    _this.callback();
                }
                _this.setState(true);
            }
        };
        outerCircle.onmouseenter = function (e) {
            _this.innerCircle.style.backgroundColor = 'rgb(214, 124, 28)';
        };
        outerCircle.onmouseleave = function (e) {
            if (!_this.state) {
                _this.innerCircle.style.backgroundColor = 'white';
            }
        };
    }
    RadioButton.prototype.setState = function (state) {
        this.state = state;
        if (state) {
            this.innerCircle.style.backgroundColor = 'rgb(214, 124, 28)';
            this.group.toggleStates(this.index);
        }
        else {
            this.innerCircle.style.backgroundColor = 'white';
        }
    };
    RadioButton.prototype.setTitle = function () {
        this.titleEl.textContent = ui.translate(this.titleId);
    };
    return RadioButton;
}());


/***/ }),
/* 48 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var ui = __webpack_require__(0);
var app_settings_1 = __webpack_require__(4);
var sliderStepIdMapping = ['ID_IMPORTDIALOG_SLIDER_TEXT_0', 'ID_IMPORTDIALOG_SLIDER_TEXT_1', 'ID_IMPORTDIALOG_SLIDER_TEXT_2', 'ID_IMPORTDIALOG_SLIDER_TEXT_3'];
var sliderStepTooltipIdMapping = ['ID_IMPORTDIALOG_SLIDER_TOOLTIP_0', 'ID_IMPORTDIALOG_SLIDER_TOOLTIP_1', 'ID_IMPORTDIALOG_SLIDER_TOOLTIP_2', 'ID_IMPORTDIALOG_SLIDER_TOOLTIP_3'];
var MobileLoadingSlider = (function () {
    function MobileLoadingSlider(parent, loadTesselation, callback) {
        var _this = this;
        this.activeTooltips = false;
        this.callback = callback;
        this.parent = parent;
        this.detectTooltipOusideClick = this.detectTooltipOusideClick.bind(this);
        this.wrapperEl = document.createElement('div');
        this.wrapperEl.className = 'stepped-slider-wrapper';
        this.parent.appendChild(this.wrapperEl);
        this.sliderWrapper = document.createElement('div');
        this.sliderWrapper.className = 'stepped-slider-bar';
        this.wrapperEl.appendChild(this.sliderWrapper);
        this.handle = document.createElement('span');
        this.handle.className = 'stepped-slider-handle draggable';
        this.handle.id = 'mobile-native-loading-handle';
        this.sliderWrapper.appendChild(this.handle);
        this.bar = document.createElement('span');
        this.bar.className = 'stepped-slider-bar-filled';
        this.bar.id = 'mobile-native-loading-bar';
        this.sliderWrapper.appendChild(this.bar);
        var _loop_1 = function (i) {
            var step = document.createElement('span');
            step.id = 'Step_' + i;
            step.className = 'slider-step';
            $(step).css('left', i * 133);
            var placeHolder = document.createElement('span');
            step.appendChild(placeHolder);
            var stepText = document.createElement('span');
            stepText.className = 'slider-step-title';
            var langId = sliderStepIdMapping[i];
            stepText.id = langId;
            stepText.textContent = ui.translate(langId, app_settings_1.settings.lang);
            ui.translationObjects.push(stepText);
            step.appendChild(stepText);
            this_1.sliderWrapper.appendChild(step);
            stepText.onclick = function (e) {
                if (_this.activeTooltips) {
                    e.preventDefault();
                    e.stopPropagation();
                    _this.showTooltip(i);
                }
            };
            step.onclick = function (e) {
                if (_this.activeTooltips) {
                    e.preventDefault();
                    e.stopPropagation();
                    _this.showTooltip(i);
                }
                else {
                    _this.stepTo(i);
                }
            };
        };
        var this_1 = this;
        for (var i = 0; i < sliderStepIdMapping.length; i++) {
            _loop_1(i);
        }
        var emptyBar = document.createElement('span');
        emptyBar.className = 'stepped-slider-bar-empty';
        $(emptyBar).click(function (e) {
            var pos = e.pageX - $(e.target).parent().offset().left;
            var lowerStep = Math.floor(pos / 133);
            var higherStep = Math.ceil(pos / 133);
            if (!_this.activeTooltips) {
                if (higherStep > _this.step) {
                    _this.stepTo(higherStep);
                }
                else {
                    _this.stepTo(lowerStep);
                }
            }
            else {
                if (higherStep > _this.step) {
                    _this.showTooltip(higherStep);
                }
                else {
                    _this.showTooltip(lowerStep);
                }
            }
        });
        this.sliderWrapper.appendChild(emptyBar);
        this.setTesselationMode(loadTesselation);
        this.shrink = 0;
        $(this.handle).kendoTouch({
            dragstart: function (e) {
                if (!_this.activeTooltips) {
                    _this.offset = $(_this.parent).offset().left;
                }
            },
            drag: function (e) {
                if (!_this.activeTooltips) {
                    var widthFactor = 500 - document.body.clientWidth;
                    if (e.touch.x.location - _this.offset <= 404 - widthFactor && e.touch.x.location - _this.offset >= 0) {
                        _this.handle.style.left = e.touch.x.location - _this.offset + 'px';
                        _this.bar.style.width = e.touch.x.location - _this.offset + 'px';
                    }
                }
            },
            dragend: function (e) {
                if (!_this.activeTooltips) {
                    var value = parseInt($(_this.handle).css('left'));
                    _this.stepTo(Math.round(value / (133 - _this.shrink)));
                }
            }
        });
    }
    MobileLoadingSlider.prototype.setTooltipMode = function (state) {
        this.activeTooltips = state;
        if (state) {
            this.handle.style.pointerEvents = 'none';
        }
        else {
            this.handle.style.pointerEvents = '';
        }
    };
    MobileLoadingSlider.prototype.showTooltip = function (pos) {
        this.tooltipWrapper = document.createElement('div');
        this.tooltipWrapper.className = 'mobile-pane-tooltip';
        this.wrapperEl.appendChild(this.tooltipWrapper);
        var tooltipText = document.createElement('div');
        tooltipText.innerHTML = ui.translate(sliderStepTooltipIdMapping[pos]).replace(/\\n/g, '\r');
        tooltipText.className = 'mobile-pane-tooltip-message';
        this.tooltipWrapper.appendChild(tooltipText);
        $(this.tooltipWrapper).show();
        document.addEventListener('touchstart', this.detectTooltipOusideClick);
        document.addEventListener('mousedown', this.detectTooltipOusideClick);
    };
    MobileLoadingSlider.prototype.detectTooltipOusideClick = function (e) {
        if (!(e.target === this.tooltipWrapper || this.tooltipWrapper.contains(e.target))) {
            $(this.tooltipWrapper).remove();
            document.removeEventListener('touchstart', this.detectTooltipOusideClick);
            document.removeEventListener('mousedown', this.detectTooltipOusideClick);
        }
    };
    MobileLoadingSlider.prototype.stepTo = function (step) {
        this.callback(step);
        this.step = step;
        $('#mobile-native-loading-bar').css('width', step * 133 - step * this.shrink);
        $('#mobile-native-loading-handle').css('left', step * 133 - step * this.shrink + 4);
        var loadTesselation = this.loadTesselation;
        $('.slider-step').each(function (index) {
            var stepNumber = parseInt(this.id.split('_')[1]);
            if (loadTesselation) {
                $(this).removeClass('slider-step-green-hover').addClass('slider-step-orange-hover');
                if (stepNumber === step) {
                    $(this).children('.slider-step-title').css('color', 'darkred');
                }
                else {
                    $(this).children('.slider-step-title').css('color', 'black');
                }
            }
            else {
                $(this).removeClass('slider-step-orange-hover').addClass('slider-step-green-hover');
                if (stepNumber === step) {
                    $(this).children('.slider-step-title').css('color', 'darkgreen');
                }
                else {
                    $(this).children('.slider-step-title').css('color', 'black');
                }
            }
            if (stepNumber < step) {
                $(this).children().first().addClass('slider-step-highlighted');
            }
            else {
                $(this).children().first().removeClass('slider-step-highlighted');
            }
        });
        if (loadTesselation) {
            $('#mobile-native-loading-handle').css('background-color', 'darkred');
            $('.stepped-slider-bar-filled').css('background-color', 'darkred');
            $('.slider-step-highlighted').css('background-color', 'darkred');
        }
        else {
            $('#mobile-native-loading-handle').css('background-color', 'darkgreen');
            $('.stepped-slider-bar-filled').css('background-color', 'darkgreen');
            $('.slider-step-highlighted').css('background-color', 'darkgreen');
        }
    };
    MobileLoadingSlider.prototype.setTesselationMode = function (bool) {
        this.loadTesselation = bool;
        if (bool) {
            $('#mobile-native-loading-handle').css('background-color', 'darkred');
            $('.stepped-slider-bar-filled').css('background-color', 'darkred');
            $('.slider-step-highlighted').css('background-color', 'darkred');
            var distance_1 = this.step;
            $('.slider-step').each(function (index) {
                var stepNumber = parseInt(this.id.split('_')[1]);
                $(this).removeClass('slider-step-green-hover').addClass('slider-step-orange-hover');
                if (stepNumber === distance_1) {
                    $(this).children('.slider-step-title').css('color', 'darkred');
                }
                else {
                    $(this).children('.slider-step-title').css('color', 'black');
                }
            });
        }
        else {
            $('#mobile-native-loading-handle').css('background-color', 'darkgreen');
            $('.stepped-slider-bar-filled').css('background-color', 'darkgreen');
            $('.slider-step-highlighted').css('background-color', 'darkgreen');
            var distance_2 = this.step;
            $('.slider-step').each(function (index) {
                var stepNumber = parseInt(this.id.split('_')[1]);
                $(this).removeClass('slider-step-orange-hover').addClass('slider-step-green-hover');
                if (stepNumber === distance_2) {
                    $(this).children('.slider-step-title').css('color', 'darkgreen');
                }
                else {
                    $(this).children('.slider-step-title').css('color', 'black');
                }
            });
        }
    };
    MobileLoadingSlider.prototype.resize = function () {
        var widthFactor = 500 - document.body.clientWidth;
        if (widthFactor > 0) {
            this.sliderWrapper.style.width = 418 - widthFactor + 'px';
            var shrink_1 = widthFactor / 3;
            this.shrink = shrink_1;
            this.bar.style.width = this.step * 133 - this.step * this.shrink + 'px';
            this.handle.style.left = this.step * 133 - this.step * this.shrink + 4 + 'px';
            $('.slider-step').each(function (index) {
                this.style.left = index * 133 - index * shrink_1 + 'px';
            });
        }
        else {
            this.sliderWrapper.style.width = 418 + 'px';
            this.shrink = 0;
            var shrink_2 = 0;
            this.bar.style.width = this.step * 133 - this.step * this.shrink + 'px';
            this.handle.style.left = this.step * 133 - this.step * this.shrink + 4 + 'px';
            $('.slider-step').each(function (index) {
                this.style.left = index * 133 - index * shrink_2 + 'px';
            });
        }
    };
    MobileLoadingSlider.prototype.getStep = function () {
        return this.step;
    };
    return MobileLoadingSlider;
}());
exports.MobileLoadingSlider = MobileLoadingSlider;


/***/ }),
/* 49 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var ui = __webpack_require__(0);
var MobileOptionToggle = (function () {
    function MobileOptionToggle(parent, titleIdLeft, titleIdRight, tooltipId, clickCallback, initialVal) {
        var _this = this;
        this.val = false;
        this.activeTooltips = false;
        this.parent = parent;
        this.clickCallback = clickCallback;
        this.titleIdLeft = titleIdLeft;
        this.titleIdRight = titleIdRight;
        this.tooltipId = tooltipId;
        this.detectTooltipOusideClick = this.detectTooltipOusideClick.bind(this);
        this.wrapperEl = document.createElement('div');
        this.wrapperEl.className = 'option-toggle';
        this.parent.appendChild(this.wrapperEl);
        this.wrapperEl.onclick = function (e) {
            if (_this.activeTooltips) {
                e.preventDefault();
                e.stopPropagation();
                _this.showTooltip(_this.tooltipId);
            }
        };
        if (initialVal) {
            this.val = initialVal === 'right';
        }
        this.titleElLeft = document.createElement('div');
        this.titleElLeft.className = 'option-toggle-title';
        this.titleElLeft.id = titleIdLeft;
        this.titleElLeft.textContent = ui.translate(titleIdLeft);
        ui.translationObjects.push(this.titleElLeft);
        this.wrapperEl.appendChild(this.titleElLeft);
        this.boxEl = document.createElement('div');
        this.boxEl.className = 'option-toggle-wrapper option-toggle-wrapper';
        var statusClass = this.val ? 'toggle-on' : 'toggle-off';
        $(this.boxEl).addClass(statusClass);
        this.wrapperEl.appendChild(this.boxEl);
        this.handleEl = document.createElement('div');
        this.handleEl.className = 'toggle-handle';
        this.boxEl.appendChild(this.handleEl);
        this.titleElRight = document.createElement('div');
        this.titleElRight.className = 'option-toggle-title';
        this.titleElRight.id = titleIdRight;
        this.titleElRight.textContent = ui.translate(titleIdRight);
        ui.translationObjects.push(this.titleElRight);
        this.wrapperEl.appendChild(this.titleElRight);
        this.titleElLeft.style.left = 'calc(50% - ' + (this.titleElLeft.offsetWidth + 15) + 'px)';
        $(this.boxEl).click(function (e) {
            if (!_this.activeTooltips) {
                _this.setVal(!_this.val);
            }
        });
        this.setVal(this.val);
    }
    MobileOptionToggle.prototype.setTooltipMode = function (state) {
        this.activeTooltips = state;
    };
    MobileOptionToggle.prototype.showTooltip = function (key) {
        this.tooltipWrapper = document.createElement('div');
        this.tooltipWrapper.className = 'mobile-pane-tooltip';
        this.wrapperEl.appendChild(this.tooltipWrapper);
        var tooltipText = document.createElement('div');
        tooltipText.innerHTML = ui.translate(key).replace(/\\n/g, '\r');
        tooltipText.className = 'mobile-pane-tooltip-message';
        this.tooltipWrapper.appendChild(tooltipText);
        $(this.tooltipWrapper).show();
        document.addEventListener('touchstart', this.detectTooltipOusideClick);
        document.addEventListener('mousedown', this.detectTooltipOusideClick);
    };
    MobileOptionToggle.prototype.detectTooltipOusideClick = function (e) {
        if (!(e.target === this.tooltipWrapper || this.tooltipWrapper.contains(e.target))) {
            $(this.tooltipWrapper).remove();
            document.removeEventListener('touchstart', this.detectTooltipOusideClick);
            document.removeEventListener('mousedown', this.detectTooltipOusideClick);
        }
    };
    MobileOptionToggle.prototype.resize = function () {
        this.titleElLeft.style.left = 'calc(50% - ' + (this.titleElLeft.offsetWidth + 15) + 'px)';
    };
    MobileOptionToggle.prototype.getVal = function () {
        return this.val;
    };
    MobileOptionToggle.prototype.setVal = function (val) {
        this.val = val;
        this.clickCallback(this.val);
        if (this.val) {
            $(this.boxEl).removeClass('toggle-off').addClass('toggle-on');
            $(this.titleElLeft).removeClass('option-toggle-title-active');
            $(this.titleElRight).addClass('option-toggle-title-active');
        }
        else {
            $(this.boxEl).removeClass('toggle-on').addClass('toggle-off');
            $(this.titleElLeft).addClass('option-toggle-title-active');
            $(this.titleElRight).removeClass('option-toggle-title-active');
        }
    };
    MobileOptionToggle.prototype.changeLang = function () {
        this.titleElLeft.textContent = ui.translate(this.titleIdLeft);
        this.titleElRight.textContent = ui.translate(this.titleIdRight);
    };
    MobileOptionToggle.prototype.setHighlightColor = function (color) {
        if (this.val) {
            this.titleElRight.style.color = color;
            this.titleElLeft.style.color = 'black';
        }
        else {
            this.titleElLeft.style.color = color;
            this.titleElRight.style.color = 'black';
        }
    };
    return MobileOptionToggle;
}());
exports.MobileOptionToggle = MobileOptionToggle;


/***/ }),
/* 50 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var ui = __webpack_require__(0);
var app_settings_1 = __webpack_require__(4);
var MobileDefSwitch = (function () {
    function MobileDefSwitch(parent, titleId, className, initialVal) {
        var _this = this;
        this.val = false;
        this.parent = parent;
        this.wrapperEl = document.createElement('div');
        this.wrapperEl.className = className;
        this.parent.appendChild(this.wrapperEl);
        if (initialVal) {
            this.val = initialVal;
        }
        this.hiddenCheckbox = document.createElement('input');
        this.hiddenCheckbox.type = 'checkbox';
        this.hiddenCheckbox.value = this.val + '';
        this.hiddenCheckbox.style.display = 'none';
        this.wrapperEl.appendChild(this.hiddenCheckbox);
        this.boxEl = document.createElement('div');
        this.boxEl.className = 'switch-wrapper';
        this.boxEl.style.display = 'inline-block';
        var statusClass = this.val ? 'switch-on' : 'switch-off';
        $(this.boxEl).addClass(statusClass);
        this.wrapperEl.appendChild(this.boxEl);
        this.titleEl = document.createElement('div');
        this.titleEl.className = 'switch-title';
        this.titleEl.id = titleId;
        this.titleEl.textContent = ui.translate(titleId, app_settings_1.settings.lang);
        ui.translationObjects.push(this.titleEl);
        this.wrapperEl.appendChild(this.titleEl);
        this.handleEl = document.createElement('div');
        this.handleEl.className = 'switch-handle';
        this.boxEl.appendChild(this.handleEl);
        $(this.boxEl).click(function (e) {
            _this.val = !_this.val;
            var val = _this.val ? 'true' : 'false';
            $(_this.hiddenCheckbox).val(val);
            $(_this.hiddenCheckbox).trigger('change');
            if (_this.val) {
                $(_this.boxEl).removeClass('switch-off').addClass('switch-on');
            }
            else {
                $(_this.boxEl).removeClass('switch-on').addClass('switch-off');
            }
        });
        this.setVal(this.val);
    }
    MobileDefSwitch.prototype.getTitleEl = function () {
        return this.titleEl;
    };
    MobileDefSwitch.prototype.getClickEl = function () {
        return this.hiddenCheckbox;
    };
    MobileDefSwitch.prototype.getVal = function () {
        return this.val;
    };
    MobileDefSwitch.prototype.setVal = function (val) {
        var valString = val ? 'true' : 'false';
        this.val = val;
        $(this.hiddenCheckbox).val(valString);
        $(this.hiddenCheckbox).trigger('change');
        if (this.val) {
            $(this.boxEl).removeClass('switch-off').addClass('switch-on');
        }
        else {
            $(this.boxEl).removeClass('switch-on').addClass('switch-off');
        }
    };
    return MobileDefSwitch;
}());
exports.MobileDefSwitch = MobileDefSwitch;


/***/ }),
/* 51 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
var events = __webpack_require__(6);
var widgets_1 = __webpack_require__(15);
var wv_files_1 = __webpack_require__(54);
var wv_views_1 = __webpack_require__(57);
var ui = __webpack_require__(0);
var wv_modeltree_1 = __webpack_require__(55);
var wv_selections_1 = __webpack_require__(56);
var app_settings_1 = __webpack_require__(4);
var wv_appsettings_1 = __webpack_require__(53);
var js2xmlparser = __webpack_require__(16);
var app_states_1 = __webpack_require__(5);
var copy = __webpack_require__(32);
var SVG_NS = 'http://www.w3.org/2000/svg';
var XLINK_NS = 'http://www.w3.org/1999/xlink';
var tinycolor = __webpack_require__(10);
var PaneAdapter = (function (_super) {
    __extends(PaneAdapter, _super);
    function PaneAdapter(el, client, position) {
        var _this = _super.call(this) || this;
        _this.currentSizeLevel = 1;
        _this.activeOverlays = [];
        _this.flags = [];
        _this.registeredEvents = [];
        _this.sessions = [];
        _this.resultInitialized = false;
        _this.multiselect = false;
        _this.headerKeys = [];
        _this.headerNames = [];
        _this.selection = [];
        _this.calloutInputs = [];
        _this.rows = [];
        _this.defaultCols = ['Number', 'Name', 'Quantity', 'CalloutId'];
        _this.sum = 0;
        _this.startIndex = 1;
        _this.filterList = [];
        _this.explodeInit = false;
        _this.explodeDistance = 0;
        _this.explodeStep = 0;
        _this.maxExplodeSteps = 0;
        _this.explodeType = 'AxisAligned';
        _this.nodeLevel = 0;
        _this.isHierarchically = false;
        _this.paneEl = el;
        _this.settingsMap = [];
        _this.groups = [];
        _this.client = client;
        _this.buttons = [];
        _this.eventData = [];
        _this.position = position;
        _this.client.on('NodePropertiesChanged', function (ev) {
            _this.registerEvent(ev);
        });
        _this.client.on('DraftAngleChanged', function (ev) {
            _this.registerEvent(ev);
        });
        _this.client.on('MeasurementChanged', function (ev) {
            _this.registerEvent(ev);
        });
        _this.client.on('TransformChanged', function (ev) {
            _this.registerEvent(ev);
        });
        _this.client.on('SectionChanged', function (ev) {
            _this.registerEvent(ev);
        });
        _this.client.on('SearchText', function (ev) {
            _this.registerEvent(ev);
        });
        _this.client.on('TextSearchChanged', function (ev) {
            _this.registerEvent(ev);
        });
        _this.client.on('NeighbourhoodSearchChanged', function (ev) {
            _this.registerEvent(ev);
            var input = $('#mobile-neighbourhood-search-input');
            input.val(ev.Radius);
        });
        _this.client.once('ActivateTransform', function (ev) {
            _this.checkTransformEvent(ev);
        });
        _this.client.on('Resize', function () {
            _this.resizeTextSearchPanel();
            _this.resizeTransformPanel();
        });
        _this.client.on('GetBillOfMaterial', function (res) {
            if (res.BillOfMaterial && _this.openPane === 'wv-bom') {
                _this.buildHeader(res.BillOfMaterial.Template.Column, 'BOM');
                _this.buildBody(Array.isArray(res.BillOfMaterial.Data.Item) ? res.BillOfMaterial.Data.Item : [res.BillOfMaterial.Data.Item], 'BOM');
            }
        });
        _this.client.on('ClashResultUpdated', function (res) {
            if (res.ClashDetection && _this.openPane === 'wv-clash') {
                _this.selectedIndex = undefined;
                _this.buildHeader(res.ClashDetection.Template.Column, 'clash');
                var clashResults = res.ClashDetection.Data.Item;
                if (!Array.isArray(clashResults)) {
                    clashResults = [clashResults];
                }
                _this.buildBody(clashResults, 'clash');
            }
        });
        _this.client.on('ClashVisualizationChanged', function (res) {
            if (_this.selectedIndex) {
                var xml = '<Call Method="SelectClashIndex">';
                xml += '<Index>' + _this.selectedIndex + '</Index>';
                xml += '</Call>';
                _this.client.command.executeXML(xml);
            }
        });
        return _this;
    }
    PaneAdapter.prototype.checkTransformEvent = function (ev) {
        var _this = this;
        if ($('#mobile-transformation-panel').length === 0 && (ev.indexOf('Rotate') !== -1 || ev.indexOf('Scale') !== -1 || ev.indexOf('Move') !== -1)) {
            var index = this.activeOverlays.indexOf('transformation');
            if (index === -1) {
                this.activeOverlays.push('transformation');
            }
            this.buildTransformationPanel(ev);
        }
        else {
            this.client.once('ActivateTransform', function (res) {
                _this.checkTransformEvent(res);
            });
        }
    };
    PaneAdapter.prototype.registerEvent = function (ev) {
        var type = ev.Type;
        var newType = true;
        for (var i = 0; i < this.eventData.length; i++) {
            if (this.eventData[i].Type === type) {
                this.eventData[i] = ev;
                newType = false;
            }
        }
        if (newType) {
            this.eventData.push(ev);
        }
        if (this.registeredEvents.indexOf(type) === -1) {
            this.registeredEvents.push(type);
        }
        this.updateFromEvent(ev);
    };
    PaneAdapter.prototype.setOpenPane = function (paneId) {
        this.openPane = paneId;
    };
    PaneAdapter.prototype.showHelpTooltip = function () {
        $(this.tooltipWrapper).empty();
        var tooltipTitle = document.createElement('div');
        tooltipTitle.textContent = ui.translate('ID_MOBILE_HELP_TOOLTIP-TITLE', app_settings_1.settings.lang);
        tooltipTitle.className = 'mobile-pane-tooltip-title';
        var tooltipText = document.createElement('div');
        tooltipText.textContent = ui.translate('ID_MOBILE_HELP_TOOLTIP-MESSAGE', app_settings_1.settings.lang);
        tooltipText.className = 'mobile-pane-tooltip-message';
        var tutorialLink = document.createElement('a');
        tutorialLink.textContent = ui.translate('ID_MOBILE_TUT_LINK');
        tutorialLink.style.display = 'block';
        tutorialLink.style.color = 'rgb(214, 124, 28)';
        var offlineHelp = false;
        try {
            offlineHelp = _UseOfflineHelp;
        }
        catch (e) {
        }
        var cat = offlineHelp ? app_states_1.appStates.paneHelpLinks[this.openPane].replace('php', 'html') : app_states_1.appStates.paneHelpLinks[this.openPane];
        var url = offlineHelp ? '/Help/webviewer/' + ui.mapCountry(app_settings_1.settings.lang) + '/' + cat : 'https://viewer.kisters.de/help/webviewer/' + app_states_1.appStates.version + '/' + ui.mapCountry(app_settings_1.settings.lang) + '/' + cat;
        tutorialLink.target = '_blank';
        tutorialLink.href = url;
        this.tooltipWrapper.appendChild(tooltipTitle);
        this.tooltipWrapper.appendChild(tooltipText);
        this.tooltipWrapper.appendChild(tutorialLink);
        $(this.tooltipWrapper).show();
        document.addEventListener('touchstart', this.detectTooltipOusideClick.bind(this));
    };
    PaneAdapter.prototype.detectTooltipOusideClick = function (ev) {
        if (ev.target !== this.tooltipWrapper && !this.tooltipWrapper.contains(ev.target)) {
            ev.preventDefault();
            ev.stopPropagation();
            $(this.tooltipWrapper).hide();
            document.removeEventListener('touchstart', this.detectTooltipOusideClick.bind(this));
        }
    };
    PaneAdapter.prototype.load = function (template, paneId) {
        var _this = this;
        $(this.wrapperEl).off('contextmenu');
        $(this.paneEl).empty();
        $(this.paneEl).removeAttr('data-role');
        $('#mobile-text-search-panel').hide();
        this.titleEl = document.createElement('div');
        this.titleEl.className = "mobile-pane-title";
        var iconWrapper = document.createElement('span');
        iconWrapper.className = 'mobile-pane-icon-wrapper';
        if (this.position === 'left') {
            iconWrapper.style.right = '10px';
        }
        else {
            iconWrapper.style.left = '10px';
        }
        this.paneEl.appendChild(iconWrapper);
        this.helpIcon = document.createElement('i');
        this.helpIcon.className = 'fas fa-question-circle mobile-pane-icon-' + this.position;
        this.helpIcon.onclick = function (e) {
            ui.toggleTooltipClick();
            $(_this.helpIcon).toggleClass('mobile-toggle-tooltip-icon-active');
            if (ui.showTooltips) {
                _this.showHelpTooltip();
            }
        };
        this.resizeIcon = document.createElement('i');
        this.resizeIcon.className = 'fas fa-sort fa-rotate-90 mobile-pane-icon-' + this.position;
        this.resizeIcon.onclick = function (e) {
            if (_this.currentSizeLevel === 0) {
                $(_this.paneEl).width(document.body.clientWidth / 2);
                _this.currentSizeLevel = 1;
            }
            else if (_this.currentSizeLevel === 1) {
                $(_this.paneEl).width(document.body.clientWidth);
                _this.currentSizeLevel = 2;
            }
            else {
                $(_this.paneEl).width(document.body.clientWidth / 4);
                _this.currentSizeLevel = 0;
            }
            _this.client.emit('Resize');
        };
        this.closeIcon = document.createElement('i');
        this.closeIcon.className = 'fas fa-chevron-' + this.position + ' mobile-pane-icon-' + this.position;
        this.closeIcon.onclick = function (e) {
            _this.paneEl.style[_this.position] = 'calc(-90%)';
        };
        if (this.position === 'left') {
            iconWrapper.appendChild(this.helpIcon);
            iconWrapper.appendChild(this.resizeIcon);
            iconWrapper.appendChild(this.closeIcon);
            this.titleEl.style.textAlign = 'left';
            this.titleEl.style.paddingRight = '90px';
        }
        else {
            iconWrapper.appendChild(this.closeIcon);
            iconWrapper.appendChild(this.resizeIcon);
            iconWrapper.appendChild(this.helpIcon);
            this.titleEl.style.textAlign = 'right';
            this.titleEl.style.paddingRight = '5px';
        }
        if (document.body.clientWidth / 2 > 300) {
            $(this.paneEl).width(document.body.clientWidth / 2);
        }
        else {
            $(this.paneEl).width(300);
        }
        this.openPane = paneId;
        if (template === 'custom') {
            switch (paneId) {
                case 'wv-views':
                    this.paneEl.appendChild(this.titleEl);
                    this.wrapperEl = document.createElement('div');
                    this.wrapperEl.className = 'mobile-pane-wrapper';
                    this.titleEl.textContent = ui.translate('ID_VIEWS', app_settings_1.settings.lang);
                    this.titleEl.style.marginBottom = '0';
                    this.titleEl.style.position = 'absolute';
                    this.titleEl.style.width = '100%';
                    this.titleEl.id = 'ID_VIEWS';
                    var viewsPane = new wv_views_1.MobileViewPane(this.paneEl, this.client);
                    this.paneEl.style.overflow = 'hidden';
                    break;
                case 'wv-selections':
                    this.paneEl.appendChild(this.titleEl);
                    this.wrapperEl = document.createElement('div');
                    this.wrapperEl.className = 'mobile-pane-wrapper';
                    this.titleEl.textContent = ui.translate('ID_SELECTIONS_WND', app_settings_1.settings.lang);
                    this.titleEl.id = 'ID_SELECTIONS_WND';
                    var selectionsPane = new wv_selections_1.MobileSelectionPane(this.paneEl, this.client);
                    this.paneEl.style.overflow = 'hidden';
                    var xml = '<Call Method="GetSavedSelections"></Call>';
                    this.client.command.executeXML(xml);
                    break;
                case 'wv-modeltree':
                    this.openModelTree();
                    break;
                case 'wv-appsettings':
                    this.paneEl.appendChild(this.titleEl);
                    this.wrapperEl = document.createElement('div');
                    this.wrapperEl.className = 'mobile-pane-wrapper';
                    this.titleEl.textContent = ui.translate('ID_APPSETTINGS_WND', app_settings_1.settings.lang);
                    this.titleEl.id = 'ID_APPSETTINGS_WND';
                    var appSettingsPane = new wv_appsettings_1.MobileAppSettingsPane(this.paneEl, this.client);
                    this.paneEl.style.overflow = 'hidden';
                    break;
            }
            ui.translationObjects.push(this.titleEl);
        }
        else {
            this.paneEl.style.overflow = 'hidden';
            this.paneEl.appendChild(this.titleEl);
            this.wrapperEl = document.createElement('div');
            this.wrapperEl.className = 'mobile-pane-wrapper';
            this.tooltipWrapper = document.createElement('div');
            this.tooltipWrapper.className = 'mobile-pane-tooltip';
            this.tooltipWrapper.id = 'mobile-pane-tooltip';
            $(this.tooltipWrapper).hide();
            this.paneEl.appendChild(this.tooltipWrapper);
            if (paneId === 'wv-settings') {
                var defaultToggleWrapper = document.createElement('div');
                defaultToggleWrapper.className = "mobile-pane-settings-toggle-wrapper";
                var hiddenCheckbox_1 = document.createElement('input');
                hiddenCheckbox_1.type = 'checkbox';
                hiddenCheckbox_1.value = 'true';
                hiddenCheckbox_1.style.display = 'none';
                hiddenCheckbox_1.id = 'mobile-settings-pane-default-toggle';
                defaultToggleWrapper.appendChild(hiddenCheckbox_1);
                var toggleDefaultTitle = document.createElement('span');
                toggleDefaultTitle.textContent = 'Default';
                defaultToggleWrapper.appendChild(toggleDefaultTitle);
                var boxEl_1 = document.createElement('div');
                boxEl_1.className = 'switch-wrapper switch-on mobile-switch';
                boxEl_1.style.borderColor = 'white';
                var handleEl = document.createElement('div');
                handleEl.className = 'switch-handle';
                boxEl_1.appendChild(handleEl);
                defaultToggleWrapper.appendChild(boxEl_1);
                $(boxEl_1).click(function (e) {
                    hiddenCheckbox_1.value = hiddenCheckbox_1.value === 'true' ? 'false' : 'true';
                    if (hiddenCheckbox_1.value === 'true') {
                        $(boxEl_1).removeClass('switch-off').addClass('switch-on');
                    }
                    else {
                        $(boxEl_1).removeClass('switch-on').addClass('switch-off');
                    }
                    _this.updateValues();
                });
                this.paneEl.appendChild(defaultToggleWrapper);
                var buttons = document.createElement('div');
                buttons.classList.add('mobile-pane-buttons-tab');
                buttons.style.position = 'relative';
                var expandAll = this.createButton('mobile-expand-all-settings', 'button_plus_double');
                expandAll.addEventListener('click', function (ev) {
                    _this.expandAll();
                });
                var collapseAll = this.createButton('mobile-collapse-all-settings', 'button_minus_double');
                collapseAll.addEventListener('click', function (ev) {
                    _this.collapseAll();
                });
                var save = this.createButton('mobile-save-settings', 'scene_settings_save');
                save.addEventListener('click', function (ev) {
                    var sceneSettings = _this.sceneSettings.Scene;
                    var mergedSettings = _this.settings;
                    mergedSettings.Scene = sceneSettings;
                    mergedSettings = removeProp(mergedSettings, '__text__');
                    mergedSettings = removeProp(mergedSettings, '__ordinal__');
                    delete mergedSettings.Renderer.General.Language;
                    var xml = js2xmlparser.parse('Settings', mergedSettings).replace("<?xml version='1.0'?>", "");
                    var settingsCall = '<Call Method="SetSettings">' + xml + '</Call>';
                    _this.client.command.executeXML(settingsCall);
                });
                var refresh = this.createButton('mobile-refresh-settings', 'view_button_refresh');
                refresh.addEventListener('click', function (ev) {
                    localStorage.setItem('VSWVSettings', localStorage.getItem('VSWVDefaultSettings'));
                    var settingsCall = '<Call Method="SetSettings">' + localStorage.getItem('VSWVDefaultSettings') + '</Call>';
                    _this.client.command.executeXMLAsync(settingsCall);
                    settingsCall = '<Call Method="GetSettings"></Call>';
                    _this.client.command.executeXMLAsync(settingsCall);
                    localStorage.removeItem('nativeLoadingSettings');
                });
                buttons.appendChild(expandAll);
                buttons.appendChild(collapseAll);
                buttons.appendChild(save);
                buttons.appendChild(refresh);
                this.paneEl.appendChild(buttons);
                var settingsSearchFieldWrapper = document.createElement('div');
                settingsSearchFieldWrapper.className = 'mobile-pane-search-field-wrapper';
                var settingsSearchField_1 = document.createElement('input');
                settingsSearchField_1.className = 'mobile-pane-search-field';
                settingsSearchField_1.id = 'mobile-settings-search-input';
                var settingsSearchFieldIconWrapper = document.createElement('div');
                settingsSearchFieldIconWrapper.className = 'mobile-pane-search-field-icon-wrapper';
                var settingsSearchFieldIcon = document.createElement('i');
                $(settingsSearchFieldIcon).addClass('fa fa-search mobile-pane-search-field-icon');
                settingsSearchFieldIconWrapper.appendChild(settingsSearchFieldIcon);
                $(settingsSearchField_1).keyup(function (ev) {
                    var groupLength = _this.groups.length;
                    for (var g = 0; g < groupLength; g++) {
                        var group = _this.groups[g];
                        _this.showAll(group);
                    }
                    _this.searchedGroups = [];
                    _this.foundChildren = [];
                    var searchString = settingsSearchField_1.value.toLowerCase();
                    if (searchString !== '') {
                        for (var g = 0; g < groupLength; g++) {
                            var group = _this.groups[g];
                            _this.handleGroupSearch(group, searchString);
                        }
                        for (var sg = 0; sg < _this.searchedGroups.length; sg++) {
                            var searchGroup = _this.searchedGroups[sg];
                            _this.enableGroupVisibility(searchGroup, searchString);
                        }
                        _this.cleanGroups();
                        _this.collapseAll();
                        _this.expandAll();
                    }
                    else {
                        for (var g = 0; g < groupLength; g++) {
                            var group = _this.groups[g];
                            _this.showAll(group);
                        }
                        _this.expandAll();
                        _this.collapseAll();
                    }
                });
                settingsSearchFieldWrapper.appendChild(settingsSearchField_1);
                settingsSearchFieldWrapper.appendChild(settingsSearchFieldIconWrapper);
                this.paneEl.appendChild(settingsSearchFieldWrapper);
            }
            else if (paneId === 'wv-bom' || paneId === 'wv-clash') {
                var buttons = document.createElement('div');
                buttons.classList.add('mobile-pane-buttons-tab');
                buttons.style.top = '-10px';
                buttons.style.position = 'relative';
                buttons.style.height = '26px';
                var multiselect = this.createButton('viewpane-multi', 'mobile_UI_check_light');
                var iconElement_1 = $(multiselect).find('use');
                multiselect.addEventListener('click', function () {
                    _this.multiselect = !_this.multiselect;
                    if (!_this.multiselect) {
                        iconElement_1[0].setAttributeNS(XLINK_NS, 'href', '#mobile_UI_check_light');
                    }
                    else {
                        iconElement_1[0].setAttributeNS(XLINK_NS, 'href', '#mobile_UI_check_full');
                    }
                }, false);
                buttons.appendChild(multiselect);
                var copyIconWrapper = document.createElement('span');
                copyIconWrapper.className = 'mobile-bom-pane-copy-icon-wrapper';
                var copyIcon = document.createElement('i');
                copyIcon.className = 'far fa-copy mobile-bom-pane-copy-icon';
                copyIconWrapper.onclick = function (e) {
                    var header = _this.headerNames;
                    var idSet = false;
                    if (paneId === 'wv-bom') {
                        _this.rows.map(function (item) {
                            if (item['CalloutId'] && item['CalloutId'] !== '' && item['CalloutId'] !== null) {
                                idSet = true;
                            }
                        });
                        header = _this.headerNames.filter(function (item) { return item !== 'ID' || idSet; });
                    }
                    var csvText = header.join(';');
                    csvText += '\n';
                    for (var i = 0; i < _this.rows.length; i++) {
                        for (var l = 0; l < _this.headerKeys.length; l++) {
                            if (paneId === 'wv-bom' && _this.headerKeys[l] === 'CalloutId' && !idSet) {
                                continue;
                            }
                            var val = _this.rows[i][_this.headerKeys[l]];
                            if (paneId === 'wv-bom' && val === undefined) {
                                for (var k = 0; k < _this.rows[i]['Attribute'].length; k++) {
                                    if (_this.rows[i]['Attribute'][k].Name === _this.headerKeys[l]) {
                                        val = _this.rows[i]['Attribute'][k]['__text__'];
                                    }
                                }
                            }
                            csvText += val;
                            csvText += l === _this.headerKeys.length - 1 ? '\n' : ';';
                        }
                    }
                    if (paneId === 'wv-bom') {
                        var skipCol = idSet ? ';;' : ';';
                        csvText = csvText.replace('ID', 'CalloutId');
                        csvText += skipCol + _this.sum.toString();
                    }
                    copy(csvText);
                };
                copyIconWrapper.appendChild(copyIcon);
                buttons.appendChild(copyIconWrapper);
                this.paneEl.appendChild(buttons);
                var tableWrapperEl = document.createElement('div');
                tableWrapperEl.className = 'bom-pane-table-wrapper';
                this.wrapperEl.appendChild(tableWrapperEl);
                this.tableEl = document.createElement('table');
                this.tableEl.className = 'mobile-pane-table';
                this.tableEl.width = '100%';
                tableWrapperEl.appendChild(this.tableEl);
                var tableHeaderEl = document.createElement('thead');
                this.tableEl.appendChild(tableHeaderEl);
                this.tableHeaderRowEl = document.createElement('tr');
                this.tableHeaderRowEl.className = 'pane-item';
                tableHeaderEl.appendChild(this.tableHeaderRowEl);
                this.tableBodyEl = document.createElement('tbody');
                this.tableEl.appendChild(this.tableBodyEl);
                paneId === 'wv-bom' ? this.buildDefaultBOMHeader() : this.buildDefaultClashHeader();
                var placeholderRow = document.createElement('tr');
                this.tableBodyEl.appendChild(placeholderRow);
                if (paneId === 'wv-bom') {
                    this.buildSumRow();
                }
                var buttonWrapperEl = document.createElement('div');
                buttonWrapperEl.className = 'mobile-pane-button-wrapper';
                buttonWrapperEl.onclick = function (e) {
                    var xml;
                    if (paneId === 'wv-bom') {
                        xml = '<Call Method="Compute"><Mode>BillOfMaterial</Mode></Call>';
                    }
                    else {
                        xml = '<Call Method="Compute"><Mode>ClashAsync</Mode></Call>';
                    }
                    _this.client.command.executeXML(xml);
                    _this.client.once('Compute', function (res) {
                        if (res.BillOfMaterial) {
                            _this.buildHeader(res.BillOfMaterial.Template.Column, 'BOM');
                            _this.buildBody(Array.isArray(res.BillOfMaterial.Data.Item) ? res.BillOfMaterial.Data.Item : [res.BillOfMaterial.Data.Item], 'BOM');
                        }
                    });
                };
                this.wrapperEl.appendChild(buttonWrapperEl);
                var buttonTextEl = document.createElement('div');
                buttonTextEl.className = 'mobile-pane-button-title';
                buttonTextEl.textContent = "Update";
                buttonWrapperEl.appendChild(buttonTextEl);
            }
            else if (paneId === 'wv-split') {
                var buttonRowTable = document.createElement('div');
                buttonRowTable.className = 'mobile-pane-button-wrapper';
                var splitRestartSessionButton = new widgets_1.MobileButton(buttonRowTable, ui.translate('ID_SPLIT_RESTART_SESSION_BUTTON'), undefined, 0, false, this.client, this.restartSession.bind(this));
                this.wrapperEl.appendChild(buttonRowTable);
            }
            else if (paneId === 'wv-explode') {
                var tableEl = document.createElement('table');
                tableEl.className = 'pane-content';
                this.wrapperEl.appendChild(tableEl);
                this.tableBodyEl = document.createElement('tbody');
                tableEl.appendChild(this.tableBodyEl);
                var placeholderRow = document.createElement('tr');
                this.tableBodyEl.appendChild(placeholderRow);
                this.wrapperEl.appendChild(tableEl);
                this.explodeGroupEl = new widgets_1.MobileGroup(this.wrapperEl, ui.translate('ID_TRANSFORM_PANE_EXPLODE'), false, undefined, 0, this.client);
                this.explodeSliderEl = new widgets_1.MobileSlider(this.wrapperEl, undefined, ui.translate('ID_TRANSFORM_PANE_EXPLODE_LEVEL'), undefined, this.explodeGroupEl, 0, false, this.client, this.explode.bind(this));
                this.explodeSteppedSliderEl = new widgets_1.MobileSteppedSlider(this.wrapperEl, ui.translate('ID_TRANSFORM_PANE_EXPLODE_LEVEL'), this.explodeGroupEl, 0, false, this.client, this.explodeStepped.bind(this));
                this.explodeSliderEl.setValue(0);
                this.isHierarchicallyEl = new widgets_1.MobileSwitch(this.wrapperEl, undefined, undefined, ui.translate('ID_TRANSFORM_PANE_EXPLODE_HIERARCHY'), this.explodeGroupEl, 0, false, this.client, this.setHierarchically.bind(this));
                this.explodeTypeEl = new widgets_1.MobileSelect(this.wrapperEl, undefined, undefined, ui.translate('ID_EXPLOSION_TYPE'), this.explodeGroupEl, 0, false, [], this.client, this.setExplodeType.bind(this));
                this.setTypeOptions();
                this.explodeLevelEl = new widgets_1.MobileInput(this.wrapperEl, undefined, undefined, ui.translate('ID_EXPLOSION_NODE_LEVEL'), this.explodeGroupEl, 0, true, this.client, this.setLevel.bind(this));
                this.nextLevelButtonEl = new widgets_1.MobileButton(this.wrapperEl, ui.translate('ID_EXPLOSION_NEXT_LEVEL'), this.explodeGroupEl, 0, false, this.client, this.nextLevel.bind(this));
                this.prevLevelButtonEl = new widgets_1.MobileButton(this.wrapperEl, ui.translate('ID_EXPLOSION_PREVIOUS_LEVEL'), this.explodeGroupEl, 0, false, this.client, this.prevLevel.bind(this));
                this.resetExplosionButtonEl = new widgets_1.MobileButton(this.wrapperEl, ui.translate('ID_EXPLOSION_RESET'), this.explodeGroupEl, 0, false, this.client, this.resetExplosion.bind(this));
                if (!this.explodeInit) {
                    this.client.on('GetSelectionNodeLevel', function (res) {
                        _this.nodeLevel = res.Level;
                        _this.explodeLevelEl.setValue(_this.nodeLevel);
                    });
                    this.client.on('SelectionChanged', function (res) {
                        var xml = '<Call Method="GetSelectionExplosionValue"></Call>';
                        xml += '<Call Method="GetSelectionNodeLevel"></Call>';
                        _this.client.command.executeXML(xml);
                    });
                    this.client.on('GetSelectionExplosionValue', function (res) {
                        _this.explodeSliderEl.setValue(res.Result / 100);
                    });
                }
                this.explodeInit = true;
            }
            this.paneEl.appendChild(this.wrapperEl);
            this.settingsMap = [];
            this.groups = [];
            this.buttons = [];
            this.template = template;
            var typeIdx = {};
            var order = template['__ordinal__'];
            var el = void 0;
            this.titleEl.textContent = ui.translate('ID_' + paneId);
            for (var i = 0; i < order.length; i++) {
                var type = order[i];
                if (typeIdx[type] === undefined) {
                    typeIdx[type] = 0;
                    if (!Array.isArray(template[type])) {
                        template[type] = [template[type]];
                    }
                    el = template[type][0];
                    if (typeof el === 'object') {
                        this.buildItem(el, type, undefined, -1);
                    }
                }
                else {
                    typeIdx[type]++;
                    el = template[type][typeIdx[type]];
                    if (typeof el === 'object') {
                        this.buildItem(el, type, undefined, -1);
                    }
                }
            }
            if (template['Event']) {
                if (this.registeredEvents.indexOf(template['Event']) === -1) {
                    this.client.on(template['Event'], function (ev) {
                        var type = ev.Type;
                        var newType = true;
                        for (var i = 0; i < _this.eventData.length; i++) {
                            if (_this.eventData[i].Type === type) {
                                _this.eventData[i] = ev;
                                newType = false;
                            }
                        }
                        if (newType) {
                            _this.eventData.push(ev);
                        }
                        _this.updateFromEvent(ev);
                        _this.cleanGroups();
                    });
                    this.registeredEvents.push(template['Event']);
                }
            }
            this.updateValues();
            this.cleanGroups();
            this.applyButtonMethods(paneId);
            if (paneId === 'wv-filter') {
                this.filterTable = document.createElement('table');
                this.filterTable.style.marginLeft = '20px';
                this.filterTable.style.width = 'calc(100% - 40px)';
                this.wrapperEl.appendChild(this.filterTable);
                this.client.on('@OpenedFile', function () {
                    _this.removeOldResults();
                });
                var tableHeader = document.createElement('thead');
                this.filterTable.appendChild(tableHeader);
                var tableHeaderRow = document.createElement('tr');
                tableHeaderRow.className = 'pane-item';
                tableHeader.appendChild(tableHeaderRow);
                var colorHeader = document.createElement('th');
                colorHeader.className = 'filter-pane-table-header';
                tableHeaderRow.appendChild(colorHeader);
                this.colorHeaderTextEl = document.createElement('span');
                this.colorHeaderTextEl.textContent = ui.translate('ID_COLOR');
                colorHeader.appendChild(this.colorHeaderTextEl);
                var colorHeaderIcon_1 = document.createElement('i');
                colorHeaderIcon_1.className = 'far fa-sort filter-pane-sort-icon';
                colorHeader.appendChild(colorHeaderIcon_1);
                var colorSortOrderAsc_1 = false;
                colorHeader.onclick = function (e) {
                    if (colorSortOrderAsc_1) {
                        colorSortOrderAsc_1 = false;
                        colorHeaderIcon_1.className = 'fa fa-sort-up filter-pane-sort-icon';
                    }
                    else {
                        colorSortOrderAsc_1 = true;
                        colorHeaderIcon_1.className = 'fa fa-sort-down filter-pane-sort-icon';
                    }
                    _this.filterList.sort(function (a, b) {
                        var val1 = tinycolor(a['Color']).toHsv().h;
                        var val2 = tinycolor(b['Color']).toHsv().h;
                        return _this.sort(val1, val2, !colorSortOrderAsc_1);
                    });
                    _this.buildColorTable();
                };
                var countHeader = document.createElement('th');
                countHeader.className = 'filter-pane-table-header';
                tableHeaderRow.appendChild(countHeader);
                this.countHeaderTextEl = document.createElement('span');
                this.countHeaderTextEl.textContent = ui.translate('ID_BOM_QUANTITY');
                countHeader.appendChild(this.countHeaderTextEl);
                var countHeaderIcon_1 = document.createElement('i');
                countHeaderIcon_1.className = 'far fa-sort filter-pane-sort-icon';
                countHeader.appendChild(countHeaderIcon_1);
                var countSortOrderAsc_1 = true;
                countHeader.onclick = function (e) {
                    if (countSortOrderAsc_1) {
                        countSortOrderAsc_1 = false;
                        countHeaderIcon_1.className = 'fa fa-sort-up filter-pane-sort-icon';
                    }
                    else {
                        countSortOrderAsc_1 = true;
                        countHeaderIcon_1.className = 'fa fa-sort-down filter-pane-sort-icon';
                    }
                    _this.filterList.sort(function (a, b) {
                        var val1 = a['Quantity'];
                        var val2 = b['Quantity'];
                        return _this.sort(val1, val2, !countSortOrderAsc_1);
                    });
                    _this.buildColorTable();
                };
                this.filterTableBodyEl = document.createElement('tbody');
                this.filterTableBodyEl.className = 'mobile-pane-color-list-wrapper';
                this.filterTable.appendChild(this.filterTableBodyEl);
                $(this.filterTable).hide();
                this.client.on('ComputeColorList', function () {
                    var xml = '<Call Method="GetAllGeometryColors">';
                    xml += '<IncludeHiddenGeometries>' + _this.sceneSettings.Scene.Tools.Search.IncludeHiddenGeometries + '</IncludeHiddenGeometries>';
                    xml += '</Call>';
                    _this.client.command.executeXMLAsync(xml);
                    _this.client.once('GetAllGeometryColors', function (response) {
                        _this.filterList = response.Result;
                        _this.buildColorTable();
                    });
                });
                $(this.wrapperEl).off('contextmenu').on('contextmenu', function (ev) {
                    _this.handleContextMenu(ev);
                });
            }
            if (paneId === 'wv-textsearch' && $('#mobile-text-search-panel').length === 0) {
                this.buildTextSearch();
            }
            else if (paneId === 'wv-textsearch' && $('#mobile-text-search-panel').length !== 0) {
                $('#mobile-text-search-panel').show();
            }
            if ($('#mobile-2D-extra-layer').length > 0 && !$($('#mobile-2D-extra-layer')[0]).hasClass('mobile-badge-highlighted')) {
                $('#mobile-text-search-panel').hide();
            }
            if (paneId === 'wv-split') {
                if (this.sessions.length > 0) {
                    this.resultInitialized = false;
                    for (var i = 0; i < this.sessions.length; i++) {
                        this.sessions[i]['Error'] = 'SUCCESS';
                        this.appendSplitSession(this.sessions[i], true);
                    }
                    this.activateSession(this.activeSession);
                }
            }
            this.wrapperEl.style.height = 'calc(100% - ' + this.wrapperEl.offsetTop + 'px)';
        }
    };
    PaneAdapter.prototype.handleContextMenu = function (ev) {
        ev.preventDefault();
        $('#contextMenu').remove();
        $(document).off('mousedown', this.client.vsCanvas.closeContextMenu);
        $(document).off('keyup', this.client.vsCanvas.closeContextMenu);
        this.client.vsCanvas.createContextMenu(ev);
    };
    PaneAdapter.prototype.enableGroupVisibility = function (group, searchString) {
        var hitCount = this.foundChildren.length;
        var childCount = group.children.length;
        for (var i = 0; i < hitCount; i++) {
            var foundChild = this.foundChildren[i];
            if (group.hasChild(foundChild)) {
                group.visible = true;
                if (group.key.toLowerCase().indexOf(searchString) === -1 || group.level === 1) {
                    return;
                }
            }
        }
        for (var childIndex = 0; childIndex < childCount; childIndex++) {
            var child = group.children[childIndex];
            var settings_1 = this.sceneSettings;
            if (this.openPane === 'wv-settings' && $('#mobile-settings-pane-default-toggle').val() === 'true') {
                settings_1 = this.settings;
            }
            child.visible = this.getSettingForKey(child['path'], settings_1) !== undefined;
            if (child instanceof widgets_1.MobileGroup) {
                this.enableGroupVisibility(child, searchString);
            }
        }
    };
    PaneAdapter.prototype.collapseAll = function () {
        for (var i = 0; i < this.groups.length - 1; i++) {
            this.groups[i].collapse();
        }
    };
    PaneAdapter.prototype.expandAll = function () {
        for (var i = 0; i < this.groups.length; i++) {
            this.groups[i].expand();
        }
    };
    PaneAdapter.prototype.showAll = function (group) {
        for (var i = 0; i < group.children.length; i++) {
            var child = group.children[i];
            child.visible = true;
            child.highlightSearchString(undefined);
            if (child instanceof widgets_1.MobileGroup) {
                this.showAll(child);
            }
        }
    };
    PaneAdapter.prototype.handleGroupSearch = function (group, searchString) {
        var childCount = group.children.length;
        for (var i = 0; i < childCount; i++) {
            var child = group.children[i];
            child.highlightSearchString(searchString);
            if (child instanceof widgets_1.MobileGroup) {
                this.handleGroupSearch(child, searchString);
                continue;
            }
            var val = child.key.toString().toLowerCase();
            var settings_2 = this.sceneSettings;
            if (this.openPane === 'wv-settings' && $('#mobile-settings-pane-default-toggle').val() === 'true') {
                settings_2 = this.settings;
            }
            if (val.search(searchString) === -1 || this.getSettingForKey(child['path'], settings_2) === undefined) {
                child.visible = false;
            }
            else {
                child.visible = true;
                this.foundChildren.push(child);
            }
        }
        if (group.key.toString().toLowerCase().search(searchString) !== -1 || group.hasVisibleChildren(group)) {
            this.searchedGroups.push(group);
            group.visible = true;
        }
        else {
            group.visible = false;
        }
    };
    PaneAdapter.prototype.buildTransformationPanel = function (transformationType) {
        var _this = this;
        var remainingWidth = document.body.clientWidth - 100;
        var transformationPanel = document.createElement('div');
        transformationPanel.className = 'mobile-transformation-panel';
        transformationPanel.id = 'mobile-transformation-panel';
        document.body.appendChild(transformationPanel);
        var transformationInputWrapper = document.createElement('div');
        transformationInputWrapper.className = 'mobile-transformation-input-wrapper';
        transformationPanel.appendChild(transformationInputWrapper);
        var inputTripleWrapper = document.createElement('div');
        inputTripleWrapper.className = 'mobile-transformation-triple-wrapper';
        var inputXWrapper = document.createElement('div');
        inputXWrapper.className = 'mobile-transformation-triple-input-wrapper';
        var inputXTitle = document.createElement('span');
        inputXTitle.className = 'mobile-transformation-input-title';
        inputXTitle.textContent = 'X';
        inputXWrapper.appendChild(inputXTitle);
        var inputX = document.createElement('input');
        inputX.className = 'mobile-transformation-input-triple';
        inputX.id = 'mobile-transformation-input-x';
        inputX.onkeyup = function (e) {
            e.preventDefault();
            e.stopPropagation();
        };
        inputXWrapper.appendChild(inputX);
        inputTripleWrapper.appendChild(inputXWrapper);
        var inputYWrapper = document.createElement('div');
        inputYWrapper.className = 'mobile-transformation-triple-input-wrapper';
        var inputYTitle = document.createElement('span');
        inputYTitle.className = 'mobile-transformation-input-title';
        inputYTitle.textContent = 'Y';
        inputYWrapper.appendChild(inputYTitle);
        var inputY = document.createElement('input');
        inputY.className = 'mobile-transformation-input-triple';
        inputY.id = 'mobile-transformation-input-y';
        inputY.onkeyup = function (e) {
            e.preventDefault();
            e.stopPropagation();
        };
        inputYWrapper.appendChild(inputY);
        inputTripleWrapper.appendChild(inputYWrapper);
        var inputZWrapper = document.createElement('div');
        inputZWrapper.className = 'mobile-transformation-triple-input-wrapper';
        var inputZTitle = document.createElement('span');
        inputZTitle.className = 'mobile-transformation-input-title';
        inputZTitle.textContent = 'Z';
        inputZWrapper.appendChild(inputZTitle);
        var inputZ = document.createElement('input');
        inputZ.className = 'mobile-transformation-input-triple';
        inputZ.id = 'mobile-transformation-input-x';
        inputZ.onkeyup = function (e) {
            e.preventDefault();
            e.stopPropagation();
        };
        inputZWrapper.appendChild(inputZ);
        inputTripleWrapper.appendChild(inputZWrapper);
        var scaleInputWrapper = document.createElement('div');
        scaleInputWrapper.className = 'mobile-transformation-scale-wrapper';
        var scaleInputTitle = document.createElement('span');
        scaleInputTitle.className = 'mobile-transformation-input-title';
        scaleInputTitle.textContent = ui.translate('ID_TRANSFORM_SCALING', app_settings_1.settings.lang);
        scaleInputTitle.id = 'ID_TRANSFORM_SCALING';
        ui.translationObjects.push(scaleInputTitle);
        scaleInputWrapper.appendChild(scaleInputTitle);
        var scaleInput = document.createElement('input');
        scaleInput.className = 'mobile-transformation-scale-input';
        scaleInput.id = 'mobile-transformation-scale-input';
        scaleInput.onkeyup = function (e) {
            e.preventDefault();
            e.stopPropagation();
        };
        scaleInputWrapper.appendChild(scaleInput);
        transformationInputWrapper.appendChild(inputTripleWrapper);
        transformationInputWrapper.appendChild(scaleInputWrapper);
        var typeSelect = document.createElement('select');
        typeSelect.className = 'mobile-transformation-type';
        typeSelect.id = 'mobile-transformation-type';
        transformationPanel.appendChild(typeSelect);
        var optionTranslation = document.createElement('option');
        optionTranslation.value = 'Translation';
        optionTranslation.id = 'ID_TRANSLATION';
        optionTranslation.textContent = ui.translate('ID_TRANSLATION', app_settings_1.settings.lang);
        ui.translationObjects.push(optionTranslation);
        typeSelect.appendChild(optionTranslation);
        var optionRotation = document.createElement('option');
        optionRotation.value = 'Rotation';
        optionRotation.id = 'ID_GRP_ROTATION';
        optionRotation.textContent = ui.translate('ID_GRP_ROTATION', app_settings_1.settings.lang);
        ui.translationObjects.push(optionRotation);
        typeSelect.appendChild(optionRotation);
        var optionScale = document.createElement('option');
        optionScale.value = 'Scaling';
        optionScale.id = 'ID_TRANSFORM_SCALING';
        optionScale.textContent = ui.translate('ID_TRANSFORM_SCALING', app_settings_1.settings.lang);
        ui.translationObjects.push(optionScale);
        typeSelect.appendChild(optionScale);
        var action = transformationType.replace('Transformation_', '');
        switch (action) {
            case 'Move':
                $(scaleInputWrapper).hide();
                $(inputTripleWrapper).show();
                $(typeSelect).val('Translation');
                break;
            case 'Rotate':
                $(scaleInputWrapper).hide();
                $(inputTripleWrapper).show();
                $(typeSelect).val('Rotation');
                break;
            case 'Scale':
                $(scaleInputWrapper).show();
                $(inputTripleWrapper).hide();
                $(typeSelect).val('Scaling');
                break;
        }
        if ($(transformationPanel).outerWidth() > remainingWidth) {
            $(transformationPanel).addClass('mobile-vertical-transformation-panel');
            $('.mobile-transformation-input-title').addClass('mobile-vertical-transformation-panel-input');
        }
        else {
            $(transformationPanel).removeClass('mobile-vertical-transformation-panel');
            $('.mobile-transformation-input-title').removeClass('mobile-vertical-transformation-panel-input');
        }
        typeSelect.addEventListener('change', function (ev) {
            var val = typeSelect.value;
            var activateInteraction;
            switch (val) {
                case 'Translation':
                    activateInteraction = '<Call Method="ActivateInteraction"><Interaction>Transformation_Move</Interaction></Call>';
                    $(scaleInputWrapper).hide();
                    $(inputTripleWrapper).show();
                    break;
                case 'Rotation':
                    activateInteraction = '<Call Method="ActivateInteraction"><Interaction>Transformation_Rotate</Interaction></Call>';
                    $(scaleInputWrapper).hide();
                    $(inputTripleWrapper).show();
                    break;
                case 'Scaling':
                    activateInteraction = '<Call Method="ActivateInteraction"><Interaction>Transformation_Scale</Interaction></Call>';
                    $(scaleInputWrapper).show();
                    $(inputTripleWrapper).hide();
                    break;
            }
            _this.client.command.executeXML(activateInteraction);
        });
        this.client.on('TransformChanged', function (e) {
            var val = typeSelect.value;
            switch (val) {
                case 'Translation':
                    $(inputX).val(e.Translation.X.__text__);
                    $(inputY).val(e.Translation.Y.__text__);
                    $(inputZ).val(e.Translation.Z.__text__);
                    break;
                case 'Rotation':
                    $(inputX).val(e.Rotation.X.__text__);
                    $(inputY).val(e.Rotation.Y.__text__);
                    $(inputZ).val(e.Rotation.Z.__text__);
                    break;
                case 'Scaling':
                    $(scaleInput).val(e.Scaling.Scale.__text__);
                    break;
            }
        });
        $(inputX).change(function (ev) {
            var xml = '<Event Type="TransformChanged"><' + typeSelect.value + '><X>' + inputX.value + '</X></' + typeSelect.value + '></Event>';
            _this.client.command.executeXML(xml);
        });
        $(inputY).change(function (ev) {
            var xml = '<Event Type="TransformChanged"><' + typeSelect.value + '><Y>' + inputY.value + '</Y></' + typeSelect.value + '></Event>';
            _this.client.command.executeXML(xml);
        });
        $(inputZ).change(function (ev) {
            var xml = '<Event Type="TransformChanged"><' + typeSelect.value + '><Z>' + inputZ.value + '</Z></' + typeSelect.value + '></Event>';
            _this.client.command.executeXML(xml);
        });
        $(scaleInput).change(function (ev) {
            var xml = '<Event Type="TransformChanged"><Scaling><Scale>' + scaleInput.value + '</Scale></Scaling></Event>';
            _this.client.command.executeXML(xml);
        });
        this.client.on('ActivateTransform', function (ev) {
            var action = ev.replace('Transformation_', '');
            if (action !== 'Rotate' && action !== 'Move' && action !== 'Scale') {
                return;
            }
            if ($('#mobile-transformation-panel').length !== 0) {
                var index = _this.activeOverlays.indexOf('transformation');
                if (index === -1) {
                    _this.activeOverlays.push('transformation');
                }
                $('#mobile-transformation-panel').show();
            }
            switch (action) {
                case 'Move':
                    $(scaleInputWrapper).hide();
                    $(inputTripleWrapper).show();
                    $(typeSelect).val('Translation');
                    break;
                case 'Rotate':
                    $(scaleInputWrapper).hide();
                    $(inputTripleWrapper).show();
                    $(typeSelect).val('Rotation');
                    break;
                case 'Scale':
                    $(scaleInputWrapper).show();
                    $(inputTripleWrapper).hide();
                    $(typeSelect).val('Scaling');
                    break;
            }
        });
        this.client.on('DeactivateTransform', function (ev) {
            if ($('#mobile-transformation-panel').length !== 0) {
                $('#mobile-transformation-panel').hide();
                var index = _this.activeOverlays.indexOf('transformation');
                if (index !== -1) {
                    _this.activeOverlays.splice(index, 1);
                }
            }
        });
    };
    PaneAdapter.prototype.resizeTransformPanel = function () {
        this.activeOverlays.indexOf('transformation');
        if ($('#mobile-transformation-panel').length !== 0 && this.activeOverlays.indexOf('transformation') !== -1) {
            var transformationPanel = $('#mobile-transformation-panel')[0];
            var remainingWidth = document.body.clientWidth - 100;
            if ($(transformationPanel).outerWidth() > remainingWidth) {
                $(transformationPanel).addClass('mobile-vertical-transformation-panel');
                $('.mobile-transformation-input-title').addClass('mobile-vertical-transformation-panel-input');
            }
            else {
                $(transformationPanel).removeClass('mobile-vertical-transformation-panel');
                $('.mobile-transformation-input-title').removeClass('mobile-vertical-transformation-panel-input');
            }
        }
    };
    PaneAdapter.prototype.buildTextSearch = function () {
        var _this = this;
        var resultCount = 0;
        var textSearchPanel = document.createElement('div');
        textSearchPanel.className = 'mobile-text-search-panel';
        textSearchPanel.id = 'mobile-text-search-panel';
        document.body.appendChild(textSearchPanel);
        var textSearchActionWrapper = document.createElement('div');
        textSearchActionWrapper.className = 'mobile-text-search-action-wrapper';
        textSearchActionWrapper.id = 'mobile-text-search-action-wrapper';
        textSearchPanel.appendChild(textSearchActionWrapper);
        var prevIcon = document.createElement('i');
        $(prevIcon).addClass('fa fa-chevron-left mobile-text-search-icon');
        var textField = document.createElement('input');
        textField.className = 'mobile-text-search-input';
        textField.id = 'mobile-text-search-input';
        var nextIcon = document.createElement('i');
        $(nextIcon).addClass('fa fa-chevron-right mobile-text-search-icon');
        var result = document.createElement('span');
        result.className = 'mobile-text-search-result';
        result.textContent = '0/0';
        this.client.on('TextSearchChangeIndex', function (index) {
            var i = parseInt(index) + 1;
            result.textContent = i + '/' + resultCount;
        });
        this.client.on('TextSearch', function (res) {
            $(textField).val($('#UID_TEXT_SEARCH_TEXT').val());
            result.textContent = '1/' + res;
        });
        this.client.on('SearchText', function (res) {
            resultCount = res.Results;
            result.textContent = '1/' + res.Results;
            if (res.Error === 'SUCCESS') {
                _this.client.emit('TextSearch', res.Results);
            }
        });
        $(textField).on('change', function (e) {
            var searchText = textField.value;
            $('#UID_TEXT_SEARCH_TEXT').val(textField.value);
            if (searchText !== '') {
                var xml = '<Call Method="SearchText"><SearchString>' + searchText + '</SearchString></Call>';
                _this.client.command.executeXML(xml);
            }
        });
        $(prevIcon).click(function (e) {
            var xml = '<Call Method="HighlightPreviousTextSearchResult"></Call>';
            _this.client.command.executeXML(xml);
            _this.client.once('HighlightPreviousTextSearchResult', function (res) {
                if (res.Error === 'SUCCESS') {
                    var xml_1 = '<Call Method="GetHighlightedTextSearchResultIndex" />';
                    _this.client.command.executeXML(xml_1);
                    _this.client.once('GetHighlightedTextSearchResultIndex', function (res) {
                        var i = parseInt(res.Index) + 1;
                        result.textContent = i + '/' + resultCount;
                    });
                }
            });
        });
        $(nextIcon).click(function (e) {
            var xml = '<Call Method="HighlightNextTextSearchResult"></Call>';
            _this.client.command.executeXML(xml);
            _this.client.once('HighlightNextTextSearchResult', function (res) {
                if (res.Error === 'SUCCESS') {
                    var xml_2 = '<Call Method="GetHighlightedTextSearchResultIndex" />';
                    _this.client.command.executeXML(xml_2);
                    _this.client.once('GetHighlightedTextSearchResultIndex', function (res) {
                        var i = parseInt(res.Index) + 1;
                        result.textContent = i + '/' + resultCount;
                    });
                }
            });
        });
        textSearchActionWrapper.appendChild(prevIcon);
        textSearchActionWrapper.appendChild(textField);
        textSearchActionWrapper.appendChild(nextIcon);
        textSearchActionWrapper.appendChild(result);
        if (textSearchActionWrapper.clientWidth < 340) {
            textField.style.width = textSearchPanel.clientWidth - 80 + 'px';
        }
    };
    PaneAdapter.prototype.resizeTextSearchPanel = function () {
        if ($('mobile-text-search-panel').length !== 0) {
            var textField = $('mobile-text-search-input')[0];
            var textSearchActionWrapper = $('mobile-text-search-action-wrapper')[0];
            var textSearchPanel = $('mobile-text-search-panel')[0];
            if (textSearchActionWrapper.clientWidth < 340) {
                textField.style.width = textSearchPanel.clientWidth - 80 + 'px';
            }
            else {
                textField.style.width = 'auto';
            }
        }
    };
    PaneAdapter.prototype.cleanGroups = function () {
        for (var i = 0; i < this.groups.length; i++) {
            var group = this.groups[i];
            if (!group.hasVisibleChildren(group) && group.parent && !group.flag) {
                group.hide();
                for (var g = 0; g < group.children.length; g++) {
                    group.children[g].hide();
                }
            }
            else if (!group.parent) {
                group.expand();
            }
        }
        if (this.openPane === 'wv-measurement') {
            var wtGroup = void 0;
            var pointGroup = void 0;
            var distanceGroup = void 0;
            var planeToAxisGroup = void 0;
            var projectedLineGroup = void 0;
            for (var i = 0; i < this.groups.length; i++) {
                if (this.groups[i].id) {
                    switch (this.groups[i].id) {
                        case 'UID_MEASUREMENT_PANE_DISTANCE':
                            distanceGroup = this.groups[i];
                            break;
                        case 'UID_MEASUREMENT_PANE_POINT':
                            pointGroup = this.groups[i];
                            break;
                        case 'UID_MEASUREMENT_PANE_WALLTHICKNESS':
                            wtGroup = this.groups[i];
                            break;
                        case 'UID_MEASUREMENT_PANE_PLANE_TO_AXIS':
                            planeToAxisGroup = this.groups[i];
                            break;
                        case 'UID_MEASUREMENT_PANE_PLANE_PROJECTED_LINE':
                            projectedLineGroup = this.groups[i];
                            break;
                        default:
                            break;
                    }
                }
            }
            ;
            if (app_states_1.appStates.interactionMode.indexOf('Measurement_WallThickness_RayBased') !== -1
                || app_states_1.appStates.interactionMode.indexOf('Measurement_WallThickness_RollingBall') !== -1) {
                wtGroup.expand(true);
                distanceGroup.collapse(true);
                pointGroup.collapse(true);
                planeToAxisGroup.collapse(true);
                projectedLineGroup.collapse(true);
            }
            else if (app_states_1.appStates.interactionMode.indexOf('Measurement_PointCoordinate') !== -1) {
                wtGroup.collapse(true);
                distanceGroup.collapse(true);
                pointGroup.expand(true);
                planeToAxisGroup.collapse(true);
                projectedLineGroup.collapse(true);
            }
            else if (app_states_1.appStates.interactionMode.indexOf('Measurement_AngleBetweenPlaneDirection') !== -1) {
                wtGroup.collapse(true);
                distanceGroup.collapse(true);
                pointGroup.collapse(true);
                planeToAxisGroup.expand(true);
                projectedLineGroup.collapse(true);
            }
            else if (app_states_1.appStates.interactionMode.indexOf('Measurement_AngleBetweenTwoProjectedLines') !== -1) {
                wtGroup.collapse(true);
                distanceGroup.collapse(true);
                pointGroup.collapse(true);
                planeToAxisGroup.collapse(true);
                projectedLineGroup.expand(true);
            }
            else {
                var isDistance = false;
                for (var i = 0; i < app_states_1.appStates.interactionMode.length; i++) {
                    if (app_states_1.appStates.interactionMode[i].indexOf('Measurement') !== -1 && app_states_1.appStates.interactionMode[i].indexOf('Distance') !== -1) {
                        isDistance = true;
                        break;
                    }
                }
                wtGroup.collapse(true);
                pointGroup.collapse(true);
                planeToAxisGroup.collapse(true);
                projectedLineGroup.collapse(true);
                if (isDistance) {
                    distanceGroup.expand(true);
                }
                else {
                    distanceGroup.collapse(true);
                }
            }
        }
    };
    PaneAdapter.prototype.cleanGroup = function (group) {
        if (!group.hasVisibleChildren(group) && group.parent && !group.flag) {
            group.hide();
        }
    };
    PaneAdapter.prototype.createElement = function (element, parent, level) {
        if (element['IsHidden'] && element['IsHidden'] === 'true') {
            return;
        }
        var typeIdx = {};
        var order = element['__ordinal__'];
        var el;
        for (var i = 0; i < order.length; i++) {
            var type = order[i];
            if (typeIdx[type] === undefined) {
                typeIdx[type] = 0;
                if (!Array.isArray(element[type])) {
                    element[type] = [element[type]];
                }
                el = element[type][0];
                if (typeof el === 'object') {
                    this.buildItem(el, type, parent, level);
                }
            }
            else {
                typeIdx[type]++;
                el = element[type][typeIdx[type]];
                if (typeof el === 'object') {
                    this.buildItem(el, type, parent, level);
                }
            }
        }
    };
    PaneAdapter.prototype.buildItem = function (el, type, parent, level) {
        var _this = this;
        if (el['IsHidden'] && el['IsHidden'] === 'true') {
            return;
        }
        var child = el;
        var sourceType;
        var path;
        var mapEntry;
        var readOnly;
        var settingsLength = this.settingsMap.length;
        switch (type) {
            case 'Group':
                level++;
                var group = new widgets_1.MobileGroup(this.wrapperEl, el['DisplayName'], true, parent, level, this.client);
                if (child['UId']) {
                    group.id = Array.isArray(child['UId']) ? child['UId'][0] : child['UId'];
                }
                this.createElement(el, group, level);
                this.groups.push(group);
                group.setToolTip(child['Tooltip']);
                break;
            case 'Text':
            case 'Float':
                if (child['DisplayType'] && child['DisplayType'] === 'Slider') {
                    if (child['Setting']) {
                        sourceType = 'setting';
                        path = child['Setting'].split('.');
                    }
                    else if (child['EventPath']) {
                        sourceType = this.template['Event'];
                        path = child['EventPath'].split('.');
                    }
                    var title_1 = child['DisplayName'];
                    readOnly = child['IsReadOnly'] === 'true';
                    var slider = new widgets_1.MobileSlider(this.wrapperEl, sourceType, title_1, path, parent, level, readOnly, this.client);
                    if (child['UId']) {
                        slider.inputEl.id = child['UId'];
                    }
                    mapEntry = {
                        el: slider,
                        setting: path
                    };
                    this.settingsMap.push(mapEntry);
                    break;
                }
            case 'Integer':
                if (child['Setting']) {
                    sourceType = 'setting';
                    path = child['Setting'].split('.');
                }
                else if (child['EventPath']) {
                    sourceType = this.template['Event'];
                    path = child['EventPath'].split('.');
                }
                else {
                    sourceType = 'none';
                    path = undefined;
                }
                var title = child['DisplayName'];
                readOnly = child['IsReadOnly'] === 'true';
                var input = new widgets_1.MobileInput(this.wrapperEl, sourceType, path, title, parent, level, readOnly, this.client);
                if (type === 'Float' || type === 'Integer') {
                    input.isNumber = true;
                }
                if (child['UId']) {
                    input.valueEl.id = child['UId'];
                }
                if (child['Suffix']) {
                    input.suffix = child['Suffix'];
                }
                if (sourceType === 'none') {
                    input.val = 'show';
                    input.visible = true;
                }
                else {
                    mapEntry = {
                        el: input,
                        setting: path
                    };
                    this.settingsMap.push(mapEntry);
                }
                break;
            case 'Bool':
                if (child['Setting']) {
                    sourceType = 'setting';
                    path = child['Setting'].split('.');
                }
                else if (child['EventPath']) {
                    sourceType = this.template['Event'];
                    path = child['EventPath'].split('.');
                }
                else {
                    sourceType = 'none';
                    path = undefined;
                }
                title = child['DisplayName'];
                readOnly = child['IsReadOnly'] === 'true';
                var mSwitch = new widgets_1.MobileSwitch(this.wrapperEl, sourceType, path, title, parent, level, readOnly, this.client);
                if (child['UId']) {
                    mSwitch.hiddenCheckbox.id = child['UId'];
                }
                if (sourceType === 'none') {
                    mSwitch.setValue('false');
                    mSwitch.visible = true;
                }
                else {
                    mapEntry = {
                        el: mSwitch,
                        setting: path
                    };
                    this.settingsMap.push(mapEntry);
                }
                break;
            case 'EnumFlag':
                if (child['Setting']) {
                    sourceType = 'setting';
                    var s = Array.isArray(child['Setting']) ? child['Setting'][0] : child['Setting'];
                    path = s.split('.');
                    readOnly = child['IsReadOnly'] === 'true';
                    var flag_1 = new widgets_1.MobileFlag(path, this.client, this.wrapperEl);
                    level++;
                    var flagGroup_1 = new widgets_1.MobileGroup(this.wrapperEl, el['DisplayName'], true, parent, level, this.client, flag_1);
                    flagGroup_1.visible = true;
                    if (child['UId']) {
                        group.id = child['UId'];
                    }
                    this.createElement(el, flagGroup_1, level);
                    this.groups.push(flagGroup_1);
                    flagGroup_1.setToolTip(child['Tooltip']);
                    var options_1 = child['Options'][0]['Option'];
                    options_1.forEach(function (val) {
                        var option = new widgets_1.MobileFlagOption(_this.wrapperEl, val['Value'], val['DisplayName'], flagGroup_1, level, readOnly, _this.client, flag_1);
                        option.visible = true;
                        flag_1.addOption(option);
                    });
                    mapEntry = {
                        el: flagGroup_1,
                        setting: path
                    };
                    this.settingsMap.push(mapEntry);
                }
                break;
            case 'Enum':
                if (child['Setting']) {
                    sourceType = 'setting';
                    path = child['Setting'].split('.');
                }
                else if (child['EventPath']) {
                    sourceType = this.template['Event'];
                    path = child['EventPath'].split('.');
                }
                title = child['DisplayName'];
                var options = [];
                for (var j = 0; j < child['Options']['Option'].length; j++) {
                    var option = {
                        display: child['Options']['Option'][j]['DisplayName'],
                        value: child['Options']['Option'][j]['Value']
                    };
                    options.push(option);
                }
                if (child['UId'] === 'UID_PRESETS') {
                    options.push({ display: ui.translate('ID_WALLTHICKNESS_PRESET_USERDEFINED'), value: 'USERDEF' });
                }
                readOnly = child['IsReadOnly'] === 'true';
                var select = new widgets_1.MobileSelect(this.wrapperEl, sourceType, path, title, parent, level, readOnly, options, this.client);
                if (child['UId']) {
                    select.valueEl.id = child['UId'];
                }
                if (sourceType === 'none' || (child['EventPath'] && path[1] === 'Preset')) {
                    select.setValue(options[0].value);
                    select.visible = true;
                }
                else {
                    mapEntry = {
                        el: select,
                        setting: path
                    };
                    this.settingsMap.push(mapEntry);
                }
                break;
            case 'Button':
                title = child['DisplayName'];
                readOnly = child['IsReadOnly'] === 'true';
                var button = new widgets_1.MobileButton(this.wrapperEl, title, parent, level, readOnly, this.client);
                if (child['UId']) {
                    button.titleEl.id = child['UId'];
                }
                this.buttons.push(button);
                break;
            case 'KeyValue':
                title = child['Name'];
                sourceType = 'keyVal';
                var children = [];
                for (var e = 0; e < child['__ordinal__'].length; e++) {
                    var indexName = child['__ordinal__'][e];
                    if (indexName !== 'Name') {
                        var refEl = child[indexName];
                        switch (indexName) {
                            case 'Float':
                                var newFloat = {
                                    type: indexName,
                                    addition: refEl['Prefix'],
                                    setting: refEl['Setting'],
                                    name: refEl['Name']
                                };
                                children.push(newFloat);
                                break;
                            case 'Color':
                                var newColor = {
                                    type: indexName,
                                    addition: refEl['Mode'],
                                    setting: refEl['Setting'],
                                    name: refEl['Name']
                                };
                                children.push(newColor);
                                break;
                        }
                    }
                }
                readOnly = child['IsReadOnly'] === 'true';
                var mKeyValue = new widgets_1.MobileKeyValue(this.wrapperEl, title, sourceType, children, parent, level, readOnly, this.client);
                mapEntry = {
                    el: mKeyValue,
                    setting: ['Setting']
                };
                this.settingsMap.push(mapEntry);
                break;
            case 'Color':
                if (child['Setting']) {
                    sourceType = 'setting';
                    path = child['Setting'].split('.');
                }
                else if (child['EventPath']) {
                    sourceType = this.template['Event'];
                    path = child['EventPath'].split('.');
                }
                var showAlpha = void 0;
                if (child['Mode'] === 'RGB') {
                    showAlpha = false;
                }
                else {
                    showAlpha = true;
                }
                title = child['DisplayName'];
                readOnly = child['IsReadOnly'] === 'true';
                var mColorPicker = new widgets_1.MobileColorPicker(this.wrapperEl, sourceType, path, title, showAlpha, parent, level, readOnly, this.client);
                mapEntry = {
                    el: mColorPicker,
                    setting: path
                };
                this.settingsMap.push(mapEntry);
                break;
        }
        if (settingsLength < this.settingsMap.length) {
            this.settingsMap[settingsLength].el.setToolTip(child['Tooltip']);
        }
    };
    PaneAdapter.prototype.sort = function (val1, val2, sortAsc) {
        return sortAsc ? (val1 >= val2 ? 1 : -1) : (val2 >= val1 ? 1 : -1);
    };
    PaneAdapter.prototype.buildColorTable = function () {
        var _this = this;
        this.removeOldResults();
        $(this.filterTable).show();
        var res = this.filterList;
        var resultTitle = document.createElement('span');
        resultTitle.textContent = '';
        for (var i = 0; i < res.length; i++) {
            var resultRow = document.createElement('tr');
            resultRow.className = 'mobile-filter-color-list-row';
            resultRow.id = 'ColorSearchResult_' + res[i].Color.replace('#', "");
            var resultColorWrapper = document.createElement('td');
            resultColorWrapper.className = '';
            var resultColor = document.createElement('div');
            resultColor.className = 'mobile-filter-color-list-color';
            resultColor.style.listStyle = 'none';
            var color = '#' + res[i].Color.slice(3);
            resultColor.textContent = color;
            resultColor.style.backgroundColor = color;
            resultColor.style.textAlign = 'center';
            var rgb = color.substr(1);
            var int = parseInt(rgb, 16);
            var r = ((int >> 16) & 255) / 255;
            var g = ((int >> 8) & 255) / 255;
            var b = (int & 255) / 255;
            var rgbArr = [r, g, b];
            for (var j = 0; j < rgbArr.length; j++) {
                if (rgbArr[j] <= 0.03928) {
                    rgbArr[j] = rgbArr[j] / 12.92;
                }
                else {
                    rgbArr[j] = Math.pow((rgbArr[j] + 0.055) / 1.055, 2.4);
                }
            }
            var luminance = 0.2126 * rgbArr[0] + 0.7152 * rgbArr[1] + 0.0722 * rgbArr[2];
            var fontColor = (luminance > 0.179) ? 'black' : 'white';
            resultColor.style.color = fontColor;
            $(resultColor).mousedown(function (e) {
                var xml = '<Call Method="SearchNodes">';
                xml += '<Color>' + e.target.textContent + '</Color>';
                xml += '<SelectionMode>Select</SelectionMode>';
                xml += '<IncludeHiddenGeometries>' + _this.sceneSettings.Scene.Tools.Search.IncludeHiddenGeometries + '</IncludeHiddenGeometries>';
                xml += '</Call>';
                _this.client.command.executeXML(xml);
            });
            resultColorWrapper.appendChild(resultColor);
            resultRow.appendChild(resultColorWrapper);
            var resultCountWrapper = document.createElement('td');
            resultCountWrapper.className = '';
            var resultCount = document.createElement('div');
            resultCount.className = 'mobile-filter-color-list-count';
            resultCount.textContent = res[i].Quantity;
            resultCountWrapper.appendChild(resultCount);
            resultRow.appendChild(resultCountWrapper);
            this.filterTableBodyEl.appendChild(resultRow);
        }
    };
    PaneAdapter.prototype.removeOldResults = function () {
        $('.mobile-filter-color-list-row').each(function () {
            $(this).remove();
        });
    };
    PaneAdapter.prototype.openFileBrowser = function (mode) {
        var _this = this;
        var filesPerPage = 50;
        try {
            if (_MobileFilesPerPage) {
                filesPerPage = _MobileFilesPerPage;
            }
        }
        catch (e) { }
        var path = "";
        if (ui.mobileSettings.activePath !== []) {
            var length_1 = ui.mobileSettings.activePath.length;
            for (var i = 0; i < length_1; i++) {
                path += ui.mobileSettings.activePath[i] + '\\';
            }
        }
        var currentFolder = this.client.command.makeBinaryPath(path, true, 0, filesPerPage - 1);
        this.client.sendBinaryPackage(1, 210, currentFolder);
        $(this.paneEl).empty();
        if (document.body.clientWidth / 2 > 300) {
            $(this.paneEl).width(document.body.clientWidth / 2);
        }
        else {
            $(this.paneEl).width(300);
        }
        this.titleEl = document.createElement('div');
        this.titleEl.className = "mobile-pane-title";
        this.titleEl.id = 'mobile-breadcrumb';
        this.paneEl.appendChild(this.titleEl);
        this.openPane = 'wv-file';
        this.settingsMap = [];
        this.titleEl.textContent = 'File browser';
        this.titleEl.style.position = 'absolute';
        this.titleEl.style.width = '100%';
        this.paneEl.style.overflow = 'hidden';
        this.titleEl.style.paddingLeft = '35px';
        this.client.once('GetFolderContent', function (folderContent) {
            var fileBrowser = new wv_files_1.MobileFileBrowser(_this.paneEl, _this.client, folderContent, mode);
            fileBrowser.setInitPath(ui.mobileSettings.activePath);
        });
        this.client.once('@OpenedFile', function (repsonse) {
            _this.clearFileBrowser();
        });
    };
    PaneAdapter.prototype.clearFileBrowser = function () {
        if (this.openPane === 'wv-file') {
            $(this.paneEl).empty();
            this.paneEl.style.left = 'calc(-90%)';
            this.paneEl.style.overflow = 'auto';
            $('#wv_mobile_multi_file_submit').remove();
        }
    };
    PaneAdapter.prototype.openModelTree = function () {
        this.titleEl = document.createElement('div');
        this.titleEl.className = "mobile-pane-title";
        this.paneEl.appendChild(this.titleEl);
        this.openPane = 'wv-modeltree';
        this.settingsMap = [];
        this.titleEl.textContent = ui.translate('ID_' + this.openPane);
        this.paneEl.style.overflow = 'hidden';
        this.treePane = new wv_modeltree_1.MobileModeltree(this.paneEl, this.client);
        if (this.virtualTree) {
            this.treePane.updateTree(this.virtualTree);
        }
    };
    PaneAdapter.prototype.appendSplitSession = function (session, reload) {
        var _this = this;
        if (reload === void 0) { reload = false; }
        if (!this.resultInitialized) {
            this.resultGroup = new widgets_1.MobileGroup(this.wrapperEl, ui.translate('ID_SPLIT_RESULTS'), false, undefined, 0, this.client);
            this.resultSessionIdEl = new widgets_1.MobileInput(this.wrapperEl, undefined, undefined, ui.translate('ID_SPLIT_SESSION_ID'), this.resultGroup, 0, false, this.client);
            this.resultSessionNameEl = new widgets_1.MobileInput(this.wrapperEl, undefined, undefined, ui.translate('ID_SPLIT_SESSION_NAME'), this.resultGroup, 0, false, this.client, this.setSessionName.bind(this));
            this.resultExplodeDistanceEl = new widgets_1.MobileInput(this.wrapperEl, undefined, undefined, ui.translate('ID_SPLIT_EXPLODE_DISTANCE'), this.resultGroup, 0, false, this.client, this.setMaxExplodeDistance.bind(this));
            this.resultSliderEl = new widgets_1.MobileSlider(this.wrapperEl, undefined, ui.translate('ID_SPLIT_EXPLODE_LEVEL'), undefined, this.resultGroup, 0, false, this.client, this.setDistance.bind(this));
            this.resultSliderEl.setValue(0);
            var unassignedGroup = new widgets_1.MobileGroup(this.wrapperEl, ui.translate('ID_SPLIT_RESULT_GROUP_UNASSIGNED'), false, this.resultGroup, 1, this.client);
            this.unassignedPatches = new widgets_1.MobileInput(this.wrapperEl, undefined, undefined, ui.translate('ID_SPLIT_NUMBER_OF_UNASSIGNED_PATCHES'), unassignedGroup, 1, true, this.client);
            this.unassignedColor = new widgets_1.MobileColorPicker(this.wrapperEl, undefined, undefined, ui.translate('ID_COLOR'), false, unassignedGroup, 1, false, this.client, this.setUnassignedColor.bind(this));
            var unassignedButton = new widgets_1.MobileButton(this.wrapperEl, ui.translate('ID_SPLIT_REGROUP_PATCH'), unassignedGroup, 1, false, this.client, this.moveUnassigned.bind(this));
            var upGroup = new widgets_1.MobileGroup(this.wrapperEl, ui.translate('ID_SPLIT_RESULT_GROUP_UP'), false, this.resultGroup, 1, this.client);
            this.upColor = new widgets_1.MobileColorPicker(this.wrapperEl, undefined, undefined, ui.translate('ID_COLOR'), false, upGroup, 1, false, this.client, this.setUpColor.bind(this));
            var upButton = new widgets_1.MobileButton(this.wrapperEl, ui.translate('ID_SPLIT_REGROUP_PATCH'), upGroup, 1, false, this.client, this.moveUp.bind(this));
            var downGroup = new widgets_1.MobileGroup(this.wrapperEl, ui.translate('ID_SPLIT_RESULT_GROUP_DOWN'), false, this.resultGroup, 1, this.client);
            this.downColor = new widgets_1.MobileColorPicker(this.wrapperEl, undefined, undefined, ui.translate('ID_COLOR'), false, downGroup, 1, false, this.client, this.setDownColor.bind(this));
            var downButton = new widgets_1.MobileButton(this.wrapperEl, ui.translate('ID_SPLIT_REGROUP_PATCH'), downGroup, 1, false, this.client, this.moveDown.bind(this));
            this.resultInitialized = true;
            this.splitGroupTable = document.createElement('table');
            this.splitGroupTable.className = 'mobile-pane-table';
            this.wrapperEl.appendChild(this.splitGroupTable);
            var tableHeader = document.createElement('thead');
            this.splitGroupTable.appendChild(tableHeader);
            var headerRow = document.createElement('tr');
            tableHeader.appendChild(headerRow);
            this.directionCol = document.createElement('th');
            this.directionCol.textContent = ui.translate('ID_SPLIT_DIRECTION_VECTOR');
            headerRow.appendChild(this.directionCol);
            this.upCol = document.createElement('th');
            this.upCol.textContent = ui.translate('ID_SPLIT_RESULT_GROUP_UP');
            headerRow.appendChild(this.upCol);
            this.downCol = document.createElement('th');
            this.downCol.textContent = ui.translate('ID_SPLIT_RESULT_GROUP_DOWN');
            headerRow.appendChild(this.downCol);
            this.splitGroupTableBody = document.createElement('tbody');
            this.splitGroupTable.appendChild(this.splitGroupTableBody);
            var placeholderRow = document.createElement('tr');
            this.splitGroupTableBody.appendChild(placeholderRow);
        }
        var tableRow = document.createElement('tr');
        tableRow.className = 'pane-item';
        tableRow.onclick = function (e) {
            _this.activateSession(session);
        };
        this.splitGroupTableBody.appendChild(tableRow);
        session.directionEl = document.createElement('td');
        session.directionEl.textContent = session.directionName;
        tableRow.appendChild(session.directionEl);
        session.upEl = document.createElement('td');
        session.upEl.textContent = session.upNumPatches.toString();
        session.upEl.style.backgroundColor = '#' + session.upColor.slice(3);
        session.upEl.style.color = ui.calculateFontColor('#' + session.downColor.slice(3));
        tableRow.appendChild(session.upEl);
        session.downEl = document.createElement('td');
        session.downEl.textContent = session.downNumPatches.toString();
        session.downEl.style.backgroundColor = '#' + session.downColor.slice(3);
        session.downEl.style.color = ui.calculateFontColor('#' + session.downColor.slice(3));
        tableRow.appendChild(session.downEl);
        if (!reload) {
            this.sessions.push(session);
        }
        this.activateSession(session);
    };
    PaneAdapter.prototype.setSessionName = function (val) {
    };
    PaneAdapter.prototype.setMaxExplodeDistance = function (val) {
        var xml = '<Call Method="SetSplitFullExplodeDistance">';
        xml += '<SplitFullExplodeDistance>' + parseFloat(val) + '</SplitFullExplodeDistance>';
        xml += '</Call>';
        this.client.command.executeXML(xml);
    };
    PaneAdapter.prototype.setSplitDirection = function () {
        var xml = '<Call Method="SetSplitDirection">';
        xml += '<X>' + $("#SplitXDirection").text() + '</X>';
        xml += '<Y>' + $("#SplitYDirection").text() + '</Y>';
        xml += '<Z>' + $("#SplitZDirection").text() + '</Z>';
        xml += '</Call>';
        this.client.command.executeXML(xml);
    };
    PaneAdapter.prototype.clearSplitGroupTable = function () {
        $(this.splitGroupTableBody).empty();
        this.sessions = [];
    };
    PaneAdapter.prototype.restartSession = function () {
        if (!this.resultGroup) {
            return;
        }
        var xml = '<Call Method="StartNewSplitSession"></Call>';
        this.client.command.executeXML(xml);
        $(this.splitGroupTable).hide();
        this.sessions = [];
        this.resultGroup.collapse();
        this.clearSplitGroupTable();
        $(this.resultGroup.wrapperEl).hide();
    };
    PaneAdapter.prototype.activateSession = function (session) {
        this.resultExplodeDistanceEl.setValue(session.maxExplodeDistance);
        this.resultSliderEl.setValue(session.sliderPos / 100);
        this.resultSessionIdEl.setValue(session.id);
        this.resultSessionNameEl.setValue(session.name);
        this.unassignedPatches.setValue(session.unassignedNumPatches);
        this.unassignedColor.setValue(session.unassignedColor);
        this.upColor.setValue(session.upColor);
        this.downColor.setValue(session.downColor);
        this.activeSession = session;
        $('.split-group-active').removeClass('split-group-active');
        $(session.directionEl).addClass('split-group-active');
    };
    PaneAdapter.prototype.setDistance = function (val) {
        var xml = '<Call Method="ExplodeSplitGroups">';
        xml += '<Position>' + parseInt((val * 100).toString()) + '</Position>';
        xml += '</Call>';
        this.client.command.executeXML(xml);
    };
    PaneAdapter.prototype.setUnassignedColor = function (val) {
        var xml = '<Call Method="SetSplitUnassignedColor">';
        xml += '<SplitDirectionId>' + this.activeSession.directionId + '</SplitDirectionId>';
        xml += '<SplitGroupUnassignedColor>' + val + '</SplitGroupUnassignedColor>';
        xml += '</Call>';
        this.client.command.executeXML(xml);
        this.activeSession.unassignedColor = val;
    };
    PaneAdapter.prototype.setUpColor = function (val) {
        var xml = '<Call Method="SetSplitGroupUpColor">';
        xml += '<SplitDirectionId>' + this.activeSession.directionId + '</SplitDirectionId>';
        xml += '<SplitGroupUpColor>' + val + '</SplitGroupUpColor>';
        xml += '</Call>';
        this.client.command.executeXML(xml);
        this.activeSession.upColor = val;
        this.activeSession.upEl.style.backgroundColor = val;
    };
    PaneAdapter.prototype.setDownColor = function (val) {
        var xml = '<Call Method="SetSplitGroupDownColor">';
        xml += '<SplitDirectionId>' + this.activeSession.directionId + '</SplitDirectionId>';
        xml += '<SplitGroupDownColor>' + val + '</SplitGroupDownColor>';
        xml += '</Call>';
        this.client.command.executeXML(xml);
        this.activeSession.downColor = val;
        this.activeSession.downEl.style.backgroundColor = val;
    };
    PaneAdapter.prototype.moveUnassigned = function () {
        var xml = '<Call Method="UnassignSplitSelectedPatches"></Call>';
        this.client.command.executeXML(xml);
    };
    PaneAdapter.prototype.moveUp = function () {
        var xml = '<Call Method="AssignSplitSelectedPatchesToGroupUp">';
        xml += '<SplitDirectionId>' + this.activeSession.directionId + '</SplitDirectionId>';
        xml += '</Call>';
        this.client.command.executeXML(xml);
    };
    PaneAdapter.prototype.moveDown = function () {
        var xml = '<Call Method="AssignSplitSelectedPatchesToGroupDown">';
        xml += '<SplitDirectionId>' + this.activeSession.directionId + '</SplitDirectionId>';
        xml += '</Call>';
        this.client.command.executeXML(xml);
    };
    PaneAdapter.prototype.buildHeader = function (template, type) {
        var _this = this;
        $(this.tableHeaderRowEl).empty();
        this.headerKeys = [];
        var _loop_1 = function (h) {
            var rowHeader = document.createElement('th');
            rowHeader.textContent = template[h].DisplayName;
            rowHeader.className = 'pane-item-entry-name';
            this_1.headerKeys.push(template[h].Id);
            this_1.headerNames.push(template[h].DisplayName);
            var sortWrapper = document.createElement('span');
            rowHeader.appendChild(sortWrapper);
            var sortIcon = document.createElement('i');
            sortIcon.className = 'fas fa-sort bom-pane-sort-icon';
            sortWrapper.appendChild(sortIcon);
            rowHeader.onclick = function (e) {
                if (sortIcon.className.indexOf('fa-sort-up') !== -1) {
                    sortIcon.className = 'fas fa-sort-down bom-pane-sort-icon';
                    _this.sortTable(false, template[h].Id, type);
                }
                else {
                    sortIcon.className = 'fas fa-sort-up bom-pane-sort-icon';
                    _this.sortTable(false, template[h].Id, type);
                }
            };
            this_1.tableHeaderRowEl.appendChild(rowHeader);
        };
        var this_1 = this;
        for (var h = 0; h < template.length; h++) {
            _loop_1(h);
        }
    };
    PaneAdapter.prototype.buildBody = function (rows, type) {
        if (!rows) {
            return;
        }
        this.calloutInputs = [];
        this.rows = [];
        this.sum = 0;
        $(this.tableBodyEl).empty();
        var placeholderRow = document.createElement('tr');
        this.tableBodyEl.appendChild(placeholderRow);
        for (var r = 0; r < rows.length; r++) {
            this.buildRow(rows[r], type);
        }
        if (type === 'BOM') {
            this.buildSumRow();
        }
    };
    PaneAdapter.prototype.buildRow = function (row, type) {
        var _this = this;
        this.rows.push(row);
        var rowEl = document.createElement('tr');
        rowEl.className = 'pane-item';
        rowEl.id = type + '_ROW_ID' + row['Id'];
        rowEl.onclick = function (e) {
            if (type === 'BOM') {
                _this.BOMRowClicked(row, e);
            }
            else if (type === 'clash') {
                _this.clashRowClicked(row);
            }
        };
        this.tableBodyEl.appendChild(rowEl);
        var _loop_2 = function (i) {
            var cellEl = document.createElement('td');
            cellEl.className = 'pane-item-entry-name';
            if (type === 'BOM') {
                if (this_2.headerKeys[i] === 'CalloutId') {
                    this_2.calloutInputs.push(cellEl);
                    cellEl.contentEditable = 'true';
                    cellEl.onblur = function (e) {
                        if (cellEl.textContent && cellEl.textContent !== '') {
                            var xml = '<Call Method="UpdateBillOfMaterialRowCalloutId">';
                            xml += '<RowId>' + row['Id'] + '</RowId>';
                            xml += '<CalloutId>' + cellEl.textContent + '</CalloutId></Call>';
                            row.CalloutId = cellEl.textContent;
                            _this.client.command.executeXML(xml);
                        }
                        else {
                            var xml = '<Call Method="ClearBillOfMaterialCalloutId">';
                            xml += '<RowId>' + row['Id'] + '</RowId></Call>';
                            row.CalloutId = cellEl.textContent;
                            _this.client.command.executeXML(xml);
                        }
                    };
                    cellEl.onkeydown = function (e) {
                        if (e.keyCode === 13) {
                            cellEl.blur();
                        }
                    };
                }
                cellEl.textContent = this_2.defaultCols.indexOf(this_2.headerKeys[i]) === -1 ? this_2.searchAttribute(row, this_2.headerKeys[i]) : row[this_2.headerKeys[i]];
                if (this_2.headerKeys[i] === 'Quantity') {
                    this_2.sum += parseInt(row[this_2.headerKeys[i]]);
                }
            }
            else if (type === 'clash') {
                cellEl.textContent = row[this_2.headerKeys[i]];
            }
            rowEl.appendChild(cellEl);
        };
        var this_2 = this;
        for (var i = 0; i < this.headerKeys.length; i++) {
            _loop_2(i);
        }
    };
    PaneAdapter.prototype.BOMRowClicked = function (row, event) {
        var xml = '<Call Method="SelectBillOfMaterialRows">';
        xml += '<RowId>' + row.Id + '</RowId>';
        xml += '<AddToSelection>' + (event.ctrlKey || this.multiselect) + '</AddToSelection>';
        xml += '</Call>';
        this.client.command.executeXML(xml);
    };
    PaneAdapter.prototype.clashRowClicked = function (row) {
        var xml = '<Call Method="SelectClashIndex">';
        xml += '<Index>' + row.Id + '</Index>';
        xml += '</Call>';
        this.selectedIndex = row.Id;
        this.client.command.executeXML(xml);
    };
    PaneAdapter.prototype.sortTable = function (asc, key, type) {
        var sortedRows = this.rows.sort(function (item1, item2) {
            var val1, val2;
            var preVal1 = item1[key];
            var preVal2 = item2[key];
            if (type === 'BOM') {
                if (item1[key] === undefined) {
                    for (var i = 0; i < item1['Attribute'].length; i++) {
                        if (item1['Attribute'][i]['Name'] === key) {
                            preVal1 = item1['Attribute'][i]['__text__'];
                            preVal2 = item2['Attribute'][i]['__text__'];
                        }
                    }
                }
            }
            val2 = isNaN(parseFloat(preVal1)) ? preVal1.toUpperCase() : parseFloat(preVal2);
            val1 = isNaN(parseFloat(preVal1)) ? preVal2.toUpperCase() : parseFloat(preVal1);
            if (val1 > val2) {
                return asc ? 1 : -1;
            }
            else {
                return asc ? -1 : 1;
            }
        });
        this.buildBody(sortedRows, type);
    };
    PaneAdapter.prototype.searchAttribute = function (row, key) {
        if (row.Attribute) {
            var attributes = Array.isArray(row.Attribute) ? row.Attribute : [row.Attribute];
            for (var i = 0; i < attributes.length; i++) {
                if (attributes[i]['Name'] === key) {
                    return attributes[i]['__text__'];
                }
            }
            return '';
        }
    };
    PaneAdapter.prototype.buildSumRow = function () {
        var sumRowEl = document.createElement('tr');
        this.tableBodyEl.appendChild(sumRowEl);
        var sumPlaceholderFirstEl = document.createElement('td');
        sumPlaceholderFirstEl.className = 'pane-item';
        sumRowEl.appendChild(sumPlaceholderFirstEl);
        var sumPlaceholderSecondEl = document.createElement('td');
        sumPlaceholderSecondEl.className = 'pane-item';
        sumRowEl.appendChild(sumPlaceholderSecondEl);
        var sumCellEl = document.createElement('td');
        sumCellEl.className = 'bom-pane-sum-cell pane-item';
        sumCellEl.textContent = this.sum.toString();
        sumRowEl.appendChild(sumCellEl);
        var sumPlaceholderThirdEl = document.createElement('td');
        sumPlaceholderThirdEl.className = 'pane-item';
        sumRowEl.appendChild(sumPlaceholderThirdEl);
    };
    PaneAdapter.prototype.buildDefaultBOMHeader = function () {
        $(this.tableHeaderRowEl).empty();
        var idRow = document.createElement('th');
        idRow.textContent = ui.translate('ID_BOM_COLUMN_CALLOUT_ID');
        idRow.className = 'pane-item-entry-name';
        this.tableHeaderRowEl.appendChild(idRow);
        var nameRow = document.createElement('th');
        nameRow.textContent = ui.translate('ID_BOM_PARTNAME');
        nameRow.className = 'pane-item-entry-name';
        this.tableHeaderRowEl.appendChild(nameRow);
        var quantityRow = document.createElement('th');
        quantityRow.textContent = ui.translate('ID_BOM_QUANTITY');
        quantityRow.className = 'pane-item-entry-name';
        this.tableHeaderRowEl.appendChild(quantityRow);
    };
    PaneAdapter.prototype.buildDefaultClashHeader = function () {
        $(this.tableHeaderRowEl).empty();
        var idRow = document.createElement('th');
        idRow.textContent = ui.translate('ID_NUM');
        idRow.className = 'pane-item-entry-name';
        this.tableHeaderRowEl.appendChild(idRow);
        var nameRow = document.createElement('th');
        nameRow.textContent = ui.translate('ID_CLASH_NODE_NAME');
        nameRow.className = 'pane-item-entry-name';
        this.tableHeaderRowEl.appendChild(nameRow);
        var otherNameRow = document.createElement('th');
        otherNameRow.textContent = ui.translate('ID_CLASH_NODE_NAME');
        otherNameRow.className = 'pane-item-entry-name';
        this.tableHeaderRowEl.appendChild(otherNameRow);
        var statusRow = document.createElement('th');
        statusRow.textContent = ui.translate('ID_CLASH_STATUS');
        statusRow.className = 'pane-item-entry-name';
        this.tableHeaderRowEl.appendChild(statusRow);
        var depthRow = document.createElement('th');
        depthRow.textContent = ui.translate('ID_CLASH_DEPTH');
        depthRow.className = 'pane-item-entry-name';
        this.tableHeaderRowEl.appendChild(depthRow);
    };
    PaneAdapter.prototype.selectCallouts = function () {
        var _this = this;
        var xml = '<Call Method="GetSelectedBillOfMaterialRows"></Call>';
        this.client.command.executeXML(xml);
        this.client.once('GetSelectedBillOfMaterialRows', function (res) {
            for (var i = 0; i < _this.rows.length; i++) {
                $('#BOM_ROW_ID' + _this.rows[i]['Id']).removeClass('bom-pane-row-selected');
            }
            if (res.RowId) {
                var rows = Array.isArray(res.RowId) ? res.RowId : [res.RowId];
                for (var i = 0; i < rows.length; i++) {
                    $('#BOM_ROW_ID' + rows[i]).addClass('bom-pane-row-selected');
                }
            }
        });
    };
    PaneAdapter.prototype.setExplodeType = function (type) {
        this.explodeType = type;
        if (!this.isHierarchically) {
            this.explode(this.explodeDistance);
        }
        else {
            this.explodeStepped(this.explodeStep);
        }
    };
    PaneAdapter.prototype.setHierarchically = function (isHierarchically) {
        this.isHierarchically = isHierarchically;
        this.explodeGroupEl.children.pop();
        if (isHierarchically) {
            this.explodeSliderEl.hide();
            this.explodeSteppedSliderEl.show();
        }
        else {
            this.explodeSliderEl.show();
            this.explodeSteppedSliderEl.hide();
        }
        this.explodeSliderEl.setValue(0);
        this.explodeSteppedSliderEl.setValue(0);
    };
    PaneAdapter.prototype.setLevel = function () {
    };
    PaneAdapter.prototype.nextLevel = function () {
        var xml = '<Call Method="SelectNextExplosionLevel"></Call>';
        this.client.command.executeXML(xml);
        var getLevelCall = '<Call Method="GetSelectionNodeLevel"></Call>';
        this.client.command.executeXML(getLevelCall);
    };
    PaneAdapter.prototype.prevLevel = function () {
        var xml = '<Call Method="SelectPreviousExplosionLevel"></Call>';
        this.client.command.executeXML(xml);
        var getLevelCall = '<Call Method="GetSelectionNodeLevel"></Call>';
        this.client.command.executeXML(getLevelCall);
    };
    PaneAdapter.prototype.resetExplosion = function () {
        var xml = '<Call Method="ResetExplosion"></Call>';
        this.client.command.executeXML(xml);
        var getLevelCall = '<Call Method="GetSelectionNodeLevel"></Call>';
        this.client.command.executeXML(getLevelCall);
    };
    PaneAdapter.prototype.setTypeOptions = function () {
        this.explodeTypeEl.clearOptions();
        var coordinateAxisOption = {
            display: ui.translate('ID_EXPLOSION_TYPE_AXES_ALIGNED'),
            value: 'AxisAligned',
        };
        var sphericalOption = {
            display: ui.translate('ID_EXPLOSION_TYPE_SPHERICAL'),
            value: 'Spherical',
        };
        var xAxisOption = {
            display: ui.translate('ID_EXPLOSION_TYPE_X_AXIS'),
            value: 'AxisX',
        };
        var yAxisOption = {
            display: ui.translate('ID_EXPLOSION_TYPE_Y_AXIS'),
            value: 'AxisY',
        };
        var zAxisOption = {
            display: ui.translate('ID_EXPLOSION_TYPE_Z_AXIS'),
            value: 'AxisZ',
        };
        var radialXYOption = {
            display: ui.translate('ID_EXPLOSION_TYPE_RADIAL_XY'),
            value: 'RadialXY',
        };
        var radialYZOption = {
            display: ui.translate('ID_EXPLOSION_TYPE_RADIAL_YZ'),
            value: 'RadialYZ',
        };
        var radialZXOption = {
            display: ui.translate('ID_EXPLOSION_TYPE_RADIAL_ZX'),
            value: 'RadialZX',
        };
        this.explodeTypeEl.addOptions([coordinateAxisOption, sphericalOption, xAxisOption, yAxisOption, zAxisOption, radialXYOption, radialYZOption, radialZXOption]);
    };
    PaneAdapter.prototype.explode = function (value) {
        var xml = '<Call Method="ExplodeByDistance"><Position>' + (Math.round(value * 100)) + '</Position>';
        xml += '<ExplosionType>' + this.explodeType + '</ExplosionType></Call>';
        this.client.command.executeXML(xml);
        this.explodeDistance = value;
        this.explodeStep = 0;
        this.explodeSteppedSliderEl.setValue(0);
    };
    PaneAdapter.prototype.explodeStepped = function (value) {
        var _this = this;
        var xml = '<Call Method="GetMaxTreeDepth"></Call>';
        this.client.command.executeXML(xml);
        this.client.once('GetMaxTreeDepth', function (res) {
            if (res && res.MaxDepth) {
                _this.explodeSteppedSliderEl.setMaxSteps(res.MaxDepth);
                _this.maxExplodeSteps = parseInt(res.MaxDepth);
                var xml_3 = '<Call Method="ExplodeHierarchically"><Level>' + value + '</Level>';
                xml_3 += '<ExplosionType>' + _this.explodeType + '</ExplosionType></Call>';
                _this.client.command.executeXML(xml_3);
                _this.explodeSliderEl.setValue(1);
                _this.explodeDistance = 1;
                _this.explodeStep = value;
            }
        });
    };
    PaneAdapter.prototype.updateValues = function () {
        for (var _i = 0, _a = this.settingsMap; _i < _a.length; _i++) {
            var el = _a[_i];
            el.el.hide();
        }
        var settings = this.sceneSettings;
        if (this.openPane === 'wv-settings' && $('#mobile-settings-pane-default-toggle').val() === 'true') {
            settings = this.settings;
        }
        for (var i = 0; i < this.settingsMap.length; i++) {
            var element = this.settingsMap[i].el;
            var newSetting = void 0;
            if (element instanceof widgets_1.MobileKeyValue) {
                var keySetting = void 0;
                if (element.keyValuePair[0].setting) {
                    keySetting = this.getSettingForKey(element.keyValuePair[0].setting.split('.'), settings);
                }
                var valSetting = this.getSettingForKey(element.keyValuePair[1].setting.split('.'), settings);
                if (keySetting === undefined || valSetting === undefined) {
                    newSetting = undefined;
                }
                else {
                    newSetting = [];
                    newSetting.push(keySetting);
                    newSetting.push(valSetting);
                }
            }
            else if (element instanceof widgets_1.MobileGroup && element.flag) {
                newSetting = this.getSettingForKey(this.settingsMap[i].setting, settings);
                element.flag.setOptions(newSetting);
                continue;
            }
            else {
                newSetting = this.getSettingForKey(this.settingsMap[i].setting, settings);
            }
            if (newSetting || (element['path'] && element['path'].indexOf('AttributesToDisplay') !== -1)
                || (element instanceof widgets_1.MobileSelect && (element.valueEl.id === 'UID_PRESET' || element.valueEl.id === 'UID_DENSITY_PRESET'))) {
                element.visible = true;
                if (element['path'] && element['path'].indexOf('AttributesToDisplay') !== -1 && !newSetting) {
                    newSetting = '';
                }
                element.setValue(newSetting);
                element.show();
                if (element.parent && !element.parent.isCollapsed) {
                    element.parent.show();
                }
            }
            else {
                element.visible = false;
                element.hide();
            }
        }
        for (var j = 0; j < this.eventData.length; j++) {
            this.updateFromEvent(this.eventData[j]);
        }
        this.cleanGroups();
        if (this.openPane === 'wv-settings') {
            if ($('#mobile-settings-search-input').val() !== '') {
                $('#mobile-settings-search-input').keyup();
            }
        }
    };
    PaneAdapter.prototype.updateFromEvent = function (event) {
        var _this = this;
        for (var i = 0; i < this.settingsMap.length; i++) {
            var element = this.settingsMap[i].el;
            if (this.settingsMap[i].setting && this.settingsMap[i].setting[0] !== 'Event') {
                continue;
            }
            if (this.template.Event[0] !== event.Type) {
                continue;
            }
            var newSetting = void 0;
            if (element instanceof widgets_1.MobileKeyValue) {
                var keySetting = this.getSettingForKey(element.keyValuePair[0].setting.split('.'), event);
                var valSetting = this.getSettingForKey(element.keyValuePair[1].setting.split('.'), event);
                if (keySetting === undefined || valSetting === undefined) {
                    newSetting = undefined;
                }
                else {
                    newSetting = [];
                    newSetting.push(keySetting);
                    newSetting.push(valSetting);
                }
            }
            else {
                newSetting = this.getSettingForKey(this.settingsMap[i].setting, event);
            }
            if (newSetting || (element['path'] && element['path'].indexOf('AttributesToDisplay') !== -1)
                || (element instanceof widgets_1.MobileSelect && (element.valueEl.id === 'UID_PRESET' || element.valueEl.id === 'UID_DENSITY_PRESET'))) {
                element.visible = true;
                element.setValue(newSetting);
                if (this.unit && element instanceof widgets_1.MobileInput) {
                    element.setUnit(this.unit);
                }
                element.show();
                if (element.parent) {
                    element.parent.expand();
                    element.parent.show();
                }
                if (this.settingsMap[i].setting && this.settingsMap[i].setting.length > 1 && this.settingsMap[i].setting[1] !== 'AccumulatedLength' && event.Type === 'MeasurementChanged') {
                    this.buttons[0].visible = false;
                    this.buttons[0].hide();
                    this.buttons[1].visible = false;
                    this.buttons[1].hide();
                }
                else if (this.settingsMap[i].setting && this.settingsMap[i].setting.length > 1 && this.settingsMap[i].setting[1] === 'AccumulatedLength' && event.Type === 'MeasurementChanged') {
                    this.buttons[0].visible = true;
                    this.buttons[0].show();
                    this.buttons[1].visible = true;
                    this.buttons[1].show();
                }
            }
            else {
                if (element.source === 'event' || this.template.Event[0] === event.Type) {
                    element.visible = false;
                    element.hide();
                }
            }
        }
        if (event && event.Type === 'NodePropertiesChanged' && this.openPane === 'wv-properties') {
            if (!event.PhysicalProperties) {
                this.buttons[0].visible = false;
                this.buttons[0].hide();
            }
            else {
                this.buttons[0].visible = true;
                this.buttons[0].show();
            }
            if (!event.Attributes) {
                var attributeGroup_1;
                this.groups.forEach(function (g) {
                    if (g.id === 'UID_PROPERTIES_PANE_ATTRIBUTES') {
                        g.visible = false;
                        attributeGroup_1 = g;
                        attributeGroup_1.clear();
                        attributeGroup_1.hide();
                    }
                });
            }
            else {
                var attributeGroup_2;
                this.groups.forEach(function (g) {
                    if (g.id === 'UID_PROPERTIES_PANE_ATTRIBUTES') {
                        g.visible = true;
                        attributeGroup_2 = g;
                        attributeGroup_2.clear();
                        var attributes = event.Attributes.Attribute;
                        if (!Array.isArray(attributes)) {
                            attributes = [attributes];
                        }
                        for (var i = 0; i < attributes.length; i++) {
                            var attribute = attributes[i];
                            var attributeElement = new widgets_1.MobileInput(_this.wrapperEl, undefined, undefined, attribute.Key, attributeGroup_2, 2, true, _this.client);
                            attributeElement.visible = true;
                            attributeElement.setValue(attribute.Value);
                            attributeElement.show();
                            attributeGroup_2.expand(true);
                        }
                    }
                });
            }
            if (event.Type && event.Type === 'NodePropertiesChanged' && event.PhysicalProperties) {
                $('#UID_PRESET').val(parseFloat(event.PhysicalProperties.Density.__text__).toFixed(6));
            }
        }
    };
    PaneAdapter.prototype.getSettingForKey = function (path, settings) {
        this.unit = undefined;
        if (!path) {
            return undefined;
        }
        var subSetting = settings;
        var firstEl = path.shift();
        for (var i = 0; i < path.length; i++) {
            if (!subSetting[path[i]]) {
                path.unshift(firstEl);
                return undefined;
            }
            else {
                subSetting = subSetting[path[i]];
            }
        }
        path.unshift(firstEl);
        if (subSetting instanceof Object) {
            var res = subSetting['__text__'];
            if (subSetting['Unit']) {
                this.unit = subSetting['Unit'];
                res += " " + subSetting['Unit'];
            }
            return res;
        }
        return subSetting;
    };
    PaneAdapter.prototype.applyButtonMethods = function (paneId) {
        var _this = this;
        switch (paneId) {
            case 'wv-textsearch':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var searchText = $('#UID_TEXT_SEARCH_TEXT').val();
                    if (searchText !== '') {
                        _this.client.once('SearchText', function (res) {
                            if (res.Error === 'SUCCESS') {
                                _this.client.emit('TextSearch', res.Results);
                            }
                        });
                        var xml = '<Call Method="SearchText"><SearchString>' + searchText + '</SearchString></Call>';
                        _this.client.command.executeXML(xml);
                    }
                });
                $(this.buttons[1].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="HighlightNextTextSearchResult"></Call>';
                    _this.client.command.executeXML(xml);
                    _this.client.once('HighlightNextTextSearchResult', function (res) {
                        if (res.Error === 'SUCCESS') {
                            var xml_4 = '<Call Method="GetHighlightedTextSearchResultIndex" />';
                            _this.client.command.executeXML(xml_4);
                            _this.client.once('GetHighlightedTextSearchResultIndex', function (res) {
                                _this.client.emit('TextSearchChangeIndex', res.Index);
                            });
                        }
                    });
                });
                $(this.buttons[2].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="HighlightPreviousTextSearchResult"></Call>';
                    _this.client.command.executeXML(xml);
                    _this.client.once('HighlightPreviousTextSearchResult', function (res) {
                        if (res.Error === 'SUCCESS') {
                            var xml_5 = '<Call Method="GetHighlightedTextSearchResultIndex" />';
                            _this.client.command.executeXML(xml_5);
                            _this.client.once('GetHighlightedTextSearchResultIndex', function (res) {
                                _this.client.emit('TextSearchChangeIndex', res.Index);
                            });
                        }
                    });
                });
                break;
            case 'wv-flythrough':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    ui.ribbon.adapter.handleActivateInteraction('Flythrough_SelectStartPositionAndDirection');
                });
                break;
            case 'wv-measurement':
                if (this.buttons.length > 0) {
                    $(this.buttons[0].titleEl).off('click').click(function (ev) {
                        var xml = '<Call Method="MeasureSelectedWireLength"></Call>';
                        _this.client.command.executeXML(xml);
                    });
                    $(this.buttons[1].titleEl).off('click').click(function (ev) {
                        var xml = '<Call Method="FinishCurrentAction"></Call>';
                        _this.client.command.executeXML(xml);
                    });
                }
                break;
            case 'wv-section':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SectionCenterHandles"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[1].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SectionAlignAxis"><Axis>X</Axis></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[2].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SectionAlignAxis"><Axis>Y</Axis></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[3].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SectionAlignAxis"><Axis>Z</Axis></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[4].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SectionSetNormal">';
                    xml += '<X>' + _this.getSettingForKey(['Movement', 'Normal', 'X'], _this.settings);
                    +'</X>';
                    xml += '<Y>' + _this.getSettingForKey(['Movement', 'Normal', 'Y'], _this.settings);
                    +'</Y>';
                    xml += '<Z>' + _this.getSettingForKey(['Movement', 'Normal', 'Z'], _this.settings);
                    +'</Z>';
                    xml += '</Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[5].titleEl).click(function (ev) {
                    var xml = '<Call Method="Compute"><Mode>SectionGeometry</Mode>';
                    xml += '<Type>Filled</Type>';
                    if ($('#UID_SECTION_PANE_CREATE_MULTIPLE').val() === 'true') {
                        xml += '<Step>' + $('#UID_SECTION_PANE_STEP_WIDTH').text() + '</Step>';
                    }
                    xml += '</Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[6].titleEl).click(function (ev) {
                    var xml = '<Call Method="Compute"><Mode>SectionGeometry</Mode>';
                    xml += '<Type>Wire</Type>';
                    if ($('#UID_SECTION_PANE_CREATE_MULTIPLE').val() === 'true') {
                        xml += '<Step>' + $('#UID_SECTION_PANE_STEP_WIDTH').val() + '</Step>';
                    }
                    xml += '</Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[7].titleEl).click(function (ev) {
                    var xml = '<Call Method="ExportSectionGeometry"><ExportFormat2D>SVG</ExportFormat2D></Call>';
                    _this.client.command.executeXML(xml);
                    _this.client.on('ExportSectionGeometry', function (res) {
                        console.log(res);
                    });
                });
                break;
            case 'wv-draftangle':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SetDraftVector"><Direction>X</Direction></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[1].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SetDraftVector"><Direction>Y</Direction></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[2].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SetDraftVector"><Direction>Z</Direction></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[3].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="RevertDraftVector" />';
                    _this.client.command.executeXML(xml);
                });
                break;
            case 'wv-projectedarea':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="Compute"><Mode>ProjectedArea</Mode></Call>';
                    _this.client.command.executeXML(xml);
                });
                break;
            case 'wv-wallthickness':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="Compute"><Mode>Wallthickness</Mode></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[1].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="StopComputation"></Call>';
                    _this.client.command.executeXML(xml);
                });
                break;
            case 'wv-drillhole':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="Compute"><Mode>Drillhole</Mode></Call>';
                    _this.client.command.executeXML(xml);
                });
                break;
            case 'wv-mirror':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var createCopy = $('#UID_MIRROR_CREATE_COPY').val();
                    var mirrorPlane = $('#UID_MIRROR_AT_PLANE').val();
                    var mirrorAt = $('#UID_MIRROR_AT').val();
                    var xml = '<Call Method="MirrorSelectedNodes">';
                    xml += '<Copy>' + createCopy + '</Copy>';
                    xml += '<Axis>' + mirrorPlane + '</Axis>';
                    xml += '<Location>' + mirrorAt + '</Location>';
                    xml += '</Call>';
                    _this.client.command.executeXML(xml);
                });
                break;
            case 'wv-filter':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var name = $('#UID_SEARCH_BY_NAME_TEXT').text();
                    if (name && name !== "" && name !== null) {
                        _this.searchNodesByName(name);
                    }
                });
                $(this.buttons[1].titleEl).off('click').click(function (ev) {
                    var name = $('#UID_SEARCH_BY_NAME_TEXT').text();
                    if (name && name !== "" && name !== null) {
                        var attribute = $('#UID_SEARCH_BY_ATTRIBUTE_NAME').text();
                        var value = $('#UID_SEARCH_BY_ATTRIBUTE_VALUE').text();
                        _this.searchNodesByAttribute(attribute, value);
                    }
                });
                $(this.buttons[2].titleEl).off('click').click(function (ev) {
                    var searchString = $('#UID_SEARCH_BY_STRING_TEXT').text();
                    if (searchString && searchString !== "" && searchString !== null) {
                        _this.searchNodes(searchString);
                    }
                });
                $(this.buttons[3].titleEl).off('click').click(function (ev) {
                    _this.client.emit('ComputeColorList');
                });
                break;
            case 'wv-distortion':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="Compute"><Mode>Distortion</Mode></Call>';
                    _this.client.command.executeXML(xml);
                });
                break;
            case 'wv-visibilitytest':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="Compute"><Mode>VisibilityTest</Mode></Call>';
                    _this.client.command.executeXML(xml);
                });
                break;
            case 'wv-neutralaxis':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="Compute"><Mode>NeutralAxis</Mode></Call>';
                    _this.client.command.executeXML(xml);
                });
                break;
            case 'wv-neighbourhoodsearch':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="ApplyNeighbourhoodSearchSettings"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[1].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="ExecuteNeighbourhoodSearch"></Call>';
                    _this.client.command.executeXML(xml);
                    _this.client.once('ExecuteNeighbourhoodSearch', function (res) {
                        if (res.Error === 'SUCCESS') {
                            if ($('#UID_NEIGHBOURHOOD_SEARCH_CLOSE_SELECTION_MODE_FLAG').val() === 'true') {
                                _this.client.command.executeXML('<Call Method="SetSelectionMode"><SelectionMode>Select</SelectionMode></Call>');
                                _this.client.emit('Select', 'Select');
                            }
                        }
                    });
                });
                break;
            case 'wv-properties':
                $(this.buttons[0].titleEl).off('click').on('click', function (ev) {
                    var xml = '<Call Method="VisualizeGravityCenter">';
                    xml += '</Call>';
                    _this.client.command.executeXML(xml);
                });
                break;
            case 'wv-split':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="ActivateSplitDirectionPick"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[1].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SetSplitDirectionXAxis"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[2].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SetSplitDirectionYAxis"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[3].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SetSplitDirectionZAxis"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[4].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="Compute"><Mode>Split</Mode></Call>';
                    _this.client.command.executeXML(xml);
                    _this.client.once('Compute', function (res) {
                    });
                });
                $("#SplitXDirection").text(0);
                $("#SplitYDirection").text(0);
                $("#SplitZDirection").text(0);
                $("#SplitXDirection").off('blur').blur(function (ev) {
                    _this.setSplitDirection();
                });
                $("#SplitYDirection").off('blur').blur(function (ev) {
                    _this.setSplitDirection();
                });
                $("#SplitZDirection").off('blur').blur(function (ev) {
                    _this.setSplitDirection();
                });
                break;
            case 'wv-bom':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="GenerateBOMCalloutIds">';
                    xml += '<StartId>' + _this.startIndex + '</StartId></Call>';
                    _this.client.command.executeXML(xml);
                    _this.client.once('GenerateBOMCalloutIds', function (resp) {
                        _this.startIndex = parseFloat(resp.StartId);
                        _this.startIndexEl.textContent = resp.StartId;
                        var xml = '<Call Method="GetBillOfMaterial"></Call>';
                        _this.client.command.executeXML(xml);
                    });
                });
                $(this.buttons[1].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="ClearBillOfMaterialCalloutIds"></Call>';
                    _this.client.command.executeXML(xml);
                    xml = '<Call Method="GetBillOfMaterial"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[2].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="GenerateBOMCallouts"></Call>';
                    _this.client.command.executeXML(xml);
                });
                this.startIndexEl = document.getElementById('BOMTechDocStartId');
                if (this.startIndexEl) {
                    if (this.startIndexEl.textContent === '') {
                        this.startIndexEl.textContent = "1";
                        this.startIndex = 1;
                    }
                    this.startIndexEl.onblur = function (e) {
                        if (isNaN(parseInt(_this.startIndexEl.textContent))) {
                            _this.startIndexEl.textContent = _this.startIndex.toString();
                        }
                        else {
                            _this.startIndex = parseInt(_this.startIndexEl.textContent);
                            _this.startIndexEl.textContent = _this.startIndex.toString();
                        }
                    };
                }
                this.client.on('@OpenedFile', function (res) {
                    $(_this.tableBodyEl).empty();
                    _this.buildDefaultBOMHeader();
                    _this.buildSumRow();
                });
                break;
            case 'wv-animation':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="ActivateInteraction"><Interaction>Animation_AssignCoordinateToNode</Interaction></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[1].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SetSceneSettings"><Settings><Scene><General><Animation>true';
                    xml += '</Animation></General></Scene></Settings></Call>';
                    ui.ribbon.adapter.buttons.map(function (button) {
                        if (button.id === 'ID_RIBBON_ANIMATE') {
                            button.highlight(true);
                        }
                    });
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[2].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="SetSceneSettings"><Settings><Scene><General><Animation>false';
                    xml += '</Animation></General></Scene></Settings></Call>';
                    ui.ribbon.adapter.buttons.map(function (button) {
                        if (button.id === 'ID_RIBBON_ANIMATE') {
                            button.highlight(false);
                        }
                    });
                    _this.client.command.executeXML(xml);
                });
                break;
            case 'wv-geometry-tools':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="OptimizeIdenticalInstances"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[1].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="GenerateFaces"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[2].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="RemoveFaces"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[3].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="GenerateNormals"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[4].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="RemoveNormals"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[5].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="GenerateWires"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[6].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="RemoveWires"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[7].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="GenerateLODGeometry"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[8].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="DetectGeometryCopies"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[9].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="RecenterGeometriesToBoundingboxCenter"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[10].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="RecenterGeometriesToBoundingboxMinimum"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[11].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="DetectTinyGeometries"></Call>';
                    _this.client.command.executeXML(xml);
                });
                break;
            case 'wv-bandanalysis':
                $(this.buttons[0].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="BandAnalysisComputeMinimalDistance"></Call>';
                    _this.client.command.executeXML(xml);
                });
                $(this.buttons[1].titleEl).off('click').click(function (ev) {
                    var xml = '<Call Method="BandAnalysisCompute"></Call>';
                    _this.client.command.executeXML(xml);
                });
                break;
        }
    };
    PaneAdapter.prototype.searchNodesByAttribute = function (attribute, value) {
        var searchString = '"' + attribute + '"' + '="' + value + '"';
        this.searchNodes(searchString);
    };
    PaneAdapter.prototype.searchNodesByName = function (name) {
        var searchString = 'Name' + '="' + name + '"';
        this.searchNodes(searchString);
    };
    PaneAdapter.prototype.searchNodes = function (searchString) {
        var xml = '<Call Method="SearchNodes">';
        xml += '<SearchString>' + searchString + '</SearchString>';
        xml += '<SelectionMode>Select</SelectionMode>';
        xml += '<IncludeHiddenGeometries>' + this.sceneSettings.Scene.Tools.Search.IncludeHiddenGeometries + '</IncludeHiddenGeometries>';
        xml += '</Call>';
        this.client.command.executeXML(xml);
    };
    PaneAdapter.prototype.createButton = function (id, icon) {
        var el = document.createElement('span');
        el.id = id;
        var elWrapper = document.createElementNS(SVG_NS, 'svg');
        elWrapper.style.width = '19px';
        elWrapper.style.height = '19px';
        var elIcon = document.createElementNS(SVG_NS, 'use');
        elIcon.setAttributeNS(XLINK_NS, 'href', '#' + icon);
        elWrapper.appendChild(elIcon);
        el.appendChild(elWrapper);
        return el;
    };
    return PaneAdapter;
}(events.EventEmitter));
exports.PaneAdapter = PaneAdapter;
var removeProp = function (obj, prop) {
    Object.keys(obj).forEach(function (key) {
        return (key === prop) && delete obj[key] ||
            (obj[key] && typeof obj[key] === 'object') && removeProp(obj[key], prop);
    });
    return obj;
};


/***/ }),
/* 52 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
var events = __webpack_require__(6);
var ui = __webpack_require__(0);
var adapter = __webpack_require__(51);
var MobilePanes = (function (_super) {
    __extends(MobilePanes, _super);
    function MobilePanes(leftEl, rightEl) {
        var _this = _super.call(this) || this;
        _this.loadedTemplates = [];
        _this.langChanged = false;
        _this.leftDefaultAdapter = new adapter.PaneAdapter(leftEl, vsClient, 'left');
        _this.rightDefaultAdapter = new adapter.PaneAdapter(rightEl, vsClient, 'right');
        vsClient.on('GetSettings', function (response) {
            _this.settings = response.Settings;
            _this.updateSettings();
        });
        vsClient.on('GetSceneSettings', function (response) {
            _this.sceneSettings = response.Settings;
            _this.updateSettings();
        });
        vsClient.on('SetSceneSettings', function (response) {
            vsClient.command.executeXML('<Call Method="GetSceneSettings"></Call>');
        });
        vsClient.on('openFileDialog', function (response) {
            $(ui.leftPaneWrapper).css('left', 0);
            _this.leftDefaultAdapter.openFileBrowser('open');
        });
        vsClient.on('importFileDialog', function (response) {
            $(ui.leftPaneWrapper).css('left', 0);
            _this.leftDefaultAdapter.openFileBrowser('import');
        });
        vsClient.on('@changeLang', function (key) {
            _this.langChanged = true;
            var _loop_1 = function (i) {
                var paneId = _this.loadedTemplates[i].paneId;
                var paneTemplate = _this.mapPaneIdToTemplate(paneId);
                var xml = '<Call Method="GetTemplate"><Type>' + paneTemplate + '</Type></Call>';
                vsClient.command.executeXML(xml);
                vsClient.once('GetTemplate.' + paneTemplate, function (response) {
                    if (response.Error === 'SUCCESS') {
                        var template = response['Source']['Template'];
                        _this.loadedTemplates[i].template = template;
                        if (_this.leftDefaultAdapter.openPane && _this.leftDefaultAdapter.openPane === paneId) {
                            _this.leftDefaultAdapter.load(template, paneId);
                        }
                        else if (_this.rightDefaultAdapter.openPane && _this.rightDefaultAdapter.openPane === paneId) {
                            _this.rightDefaultAdapter.load(template, paneId);
                        }
                    }
                });
            };
            for (var i = 0; i < _this.loadedTemplates.length; i++) {
                _loop_1(i);
            }
        });
        return _this;
    }
    MobilePanes.prototype.updateSettings = function () {
        this.leftDefaultAdapter.settings = this.settings;
        this.leftDefaultAdapter.sceneSettings = this.sceneSettings;
        this.leftDefaultAdapter.updateValues();
        this.rightDefaultAdapter.settings = this.settings;
        this.rightDefaultAdapter.sceneSettings = this.sceneSettings;
        this.rightDefaultAdapter.updateValues();
    };
    MobilePanes.prototype.render = function (paneId, position) {
        var _this = this;
        if (ui.showTooltips === true) {
            ui.toggleTooltipClick();
        }
        try {
            if (_DisabledPanes.indexOf(paneId) !== -1) {
                return;
            }
        }
        catch (e) { }
        var adapter;
        var paneTemplate = paneId;
        if (paneId.indexOf('wv-') === 0) {
            paneTemplate = this.mapPaneIdToTemplate(paneId);
        }
        else {
            paneId = this.mapPaneTemplateToId(paneId);
        }
        if (position === 'left') {
            adapter = this.leftDefaultAdapter;
            adapter.paneEl.style.left = '0';
            this.rightDefaultAdapter.paneEl.style.right = 'calc(-90%)';
        }
        else {
            adapter = this.rightDefaultAdapter;
            adapter.paneEl.style.right = '0';
            this.leftDefaultAdapter.paneEl.style.left = 'calc(-90%)';
            if (this.leftDefaultAdapter.openPane === 'wv-file') {
                this.leftDefaultAdapter.clearFileBrowser();
            }
        }
        $(adapter.helpIcon).removeClass('mobile-toggle-tooltip-icon-active');
        if (adapter.openPane === paneId && !this.langChanged) {
            if (paneId === 'wv-measurement') {
                adapter.cleanGroups();
            }
            return;
        }
        this.langChanged = false;
        var newPane = true;
        switch (paneId) {
            case 'wv-views':
                adapter.load("custom", paneId);
                return;
            case 'wv-modeltree':
                adapter.load("custom", paneId);
                return;
            case 'wv-selections':
                adapter.load("custom", paneId);
                return;
            case 'wv-appsettings':
                adapter.load("custom", paneId);
                return;
        }
        for (var i = 0; i < this.loadedTemplates.length; i++) {
            if (this.loadedTemplates[i].paneId === paneId) {
                adapter.load(this.loadedTemplates[i].template, paneId);
                newPane = false;
            }
        }
        if (newPane) {
            var xml = '<Call Method="GetTemplate"><Type>' + paneTemplate + '</Type></Call>';
            vsClient.command.executeXML(xml);
            vsClient.once('GetTemplate.' + paneTemplate, function (response) {
                if (response.Error === 'SUCCESS') {
                    var template = {
                        template: response['Source']['Template'],
                        paneId: paneId
                    };
                    _this.loadedTemplates.push(template);
                    adapter.load(template.template, paneId);
                }
            });
        }
    };
    MobilePanes.prototype.mapPaneIdToTemplate = function (paneId) {
        switch (paneId) {
            case 'wv-settings':
                return 'SettingsPane';
            case 'wv-modeltree':
                return 'Custom_ModelTree';
            case 'wv-views':
                return 'Custom_Views';
            case 'wv-distortion':
                return 'DistortionPane';
            case 'wv-draftangle':
                return 'DraftAnglePane';
            case 'wv-exactcompare':
                return 'ExactComparePane';
            case 'wv-filter':
                return 'FilterPane';
            case 'wv-flythrough':
                return 'FlyThroughPane';
            case 'wv-markup':
                return 'MarkupPane';
            case 'wv-markup-reduced':
                return 'MarkupPane_Reduced';
            case 'wv-measurement':
                return 'MeasurementPane';
            case 'wv-mirror':
                return 'MirrorPane';
            case 'wv-projectedarea':
                return 'ProjectedAreaPane';
            case 'wv-properties':
                return 'PropertiesPane';
            case 'wv-quickcompare':
                return 'QuickComparePane';
            case 'wv-section':
                return 'SectionPane';
            case 'wv-textsearch':
                return 'TextSearchPane';
            case 'wv-transform':
                return 'TransformPane';
            case 'wv-visibilitytest':
                return 'VisibilityTest';
            case 'wv-wallthickness':
                return 'WallThicknessPane';
            case 'wv-drillhole':
                return 'DrillholePane';
            case 'wv-neutralaxis':
                return 'NeutralAxisPane';
            case 'wv-neighbourhoodsearch':
                return 'NeighbourhoodSearchPane';
            case 'wv-split':
                return 'SplitPane';
            case 'wv-bom':
                return 'BOMPane';
            case 'wv-clash':
                return 'ClashPane';
            case 'wv-callout':
                return 'CalloutPane';
            case 'wv-appsettings':
                return 'Custom_AppSettings';
            case 'wv-explode':
                return 'ExplodePane';
            case 'wv-animation':
                return 'AnimationPane';
            case 'wv-geometry-tools':
                return 'GeometryToolsPane';
            case 'wv-bandanalysis':
                return 'BandAnalysisPane';
        }
    };
    MobilePanes.prototype.mapPaneTemplateToId = function (paneId) {
        switch (paneId) {
            case 'SettingsPane':
                return 'wv-settings';
            case 'Custom_ModelTree':
                return 'wv-modeltree';
            case 'Custom_Views':
                return 'wv-views';
            case 'DistortionPane':
                return 'wv-distortion';
            case 'DraftAnglePane':
                return 'wv-draftangle';
            case 'ExactComparePane':
                return 'wv-exactcompare';
            case 'FilterPane':
                return 'wv-filter';
            case 'FlyThroughPane':
                return 'wv-flythrough';
            case 'MarkupPane':
                return 'wv-markup';
            case 'MarkupPane_Reduced':
                return 'wv-markup-reduced';
            case 'MeasurementPane':
                return 'wv-measurement';
            case 'MirrorPane':
                return 'wv-mirror';
            case 'ProjectedAreaPane':
                return 'wv-projectedarea';
            case 'PropertiesPane':
                return 'wv-properties';
            case 'QuickComparePane':
                return 'wv-quickcompare';
            case 'SectionPane':
                return 'wv-section';
            case 'TextSearchPane':
                return 'wv-textsearch';
            case 'TransformPane':
                return 'wv-transform';
            case 'VisibilityTestPane':
                return 'wv-visibilitytest';
            case 'WallThicknessPane':
                return 'wv-wallthickness';
            case 'DrillholePane':
                return 'wv-drillhole';
            case 'NeutralAxisPane':
                return 'wv-neutralaxis';
            case 'NeighbourhoodSearchPane':
                return 'wv-neighbourhoodsearch';
            case 'SplitPane':
                return 'wv-split';
            case 'ExplodePane':
                return 'wv-explode';
            case 'BOMPane':
                return 'wv-bom';
            case 'ClashPane':
                return 'wv-clash';
            case 'CalloutPane':
                return 'wv-callout';
            case 'Custom_AppSettings':
                return 'wv-appsettings';
            case 'DrillholePane':
                return 'wv-animation';
            case 'AnimationPane':
                return 'wv-animation';
            case 'GeometryToolsPane':
                return 'wv-geometry-tools';
            case 'BandAnalysisPane':
                return 'wv-bandanalysis';
        }
    };
    return MobilePanes;
}(events.EventEmitter));
exports.MobilePanes = MobilePanes;


/***/ }),
/* 53 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var ui = __webpack_require__(0);
var app_settings_1 = __webpack_require__(4);
var SVG_NS = 'http://www.w3.org/2000/svg';
var XLINK_NS = 'http://www.w3.org/1999/xlink';
var MobileAppSettingsPane = (function () {
    function MobileAppSettingsPane(paneEl, client) {
        var _this = this;
        this.paneEl = paneEl;
        this.client = client;
        var titleOffset = $('#ID_APPSETTINGS_WND').outerHeight();
        this.wrapperEl = document.createElement('div');
        this.wrapperEl.className = 'mobile-pane-wrapper';
        this.buttons = document.createElement('div');
        this.buttons.classList.add('mobile-pane-buttons-tab');
        this.buttons.style.position = 'relative';
        this.paneEl.appendChild(this.buttons);
        var appSettingsRoot = document.createElement('div');
        appSettingsRoot.className = 'mobile-pane-group';
        appSettingsRoot.textContent = ui.translate('ID_APPSETTINGS_WND', app_settings_1.settings.lang);
        appSettingsRoot.id = 'ID_APPSETTINGS_WND';
        ui.translationObjects.push(appSettingsRoot);
        this.wrapperEl.appendChild(appSettingsRoot);
        var appSettingsRootWrapper = document.createElement('div');
        appSettingsRootWrapper.style.marginBottom = '5px';
        this.wrapperEl.appendChild(appSettingsRootWrapper);
        var contextMenuWrapper = document.createElement('div');
        contextMenuWrapper.className = "mobile-pane-select-wrapper";
        appSettingsRootWrapper.appendChild(contextMenuWrapper);
        var contextMenuTitle = document.createElement('div');
        contextMenuTitle.className = 'mobile-pane-select-title';
        contextMenuTitle.id = 'ID_APPSETTINGS_CONTEXT_MENU_STYLE';
        contextMenuTitle.textContent = ui.translate('ID_APPSETTINGS_CONTEXT_MENU_STYLE', app_settings_1.settings.lang);
        ui.translationObjects.push(contextMenuTitle);
        contextMenuWrapper.appendChild(contextMenuTitle);
        this.contextMenuVal = document.createElement('select');
        this.contextMenuVal.className = 'mobile-pane-select-val';
        var optionRadial = document.createElement('option');
        optionRadial.value = 'radial-menu';
        optionRadial.id = 'ID_APPSETTINGS_CONTEXT_MENU_RADIAL';
        optionRadial.textContent = ui.translate('ID_APPSETTINGS_CONTEXT_MENU_RADIAL', app_settings_1.settings.lang);
        ui.translationObjects.push(optionRadial);
        this.contextMenuVal.appendChild(optionRadial);
        var optionList = document.createElement('option');
        optionList.value = 'list-menu';
        optionList.id = 'ID_APPSETTINGS_CONTEXT_MENU_LIST';
        optionList.textContent = ui.translate('ID_APPSETTINGS_CONTEXT_MENU_LIST', app_settings_1.settings.lang);
        ui.translationObjects.push(optionList);
        this.contextMenuVal.appendChild(optionList);
        this.contextMenuVal.addEventListener('change', function (ev) {
            var val = _this.contextMenuVal.value;
            localStorage.setItem('MobileWVContextMenuStyle', val);
            ui.mobileSettings.contextMenuStyle = val;
        });
        var contextSettings = localStorage.getItem('MobileWVContextMenuStyle');
        if (contextSettings !== null) {
            this.contextMenuVal.value = contextSettings;
        }
        contextMenuWrapper.appendChild(this.contextMenuVal);
        var updateSelection = createButton('selectionspane-update', 'view_button_refresh');
        updateSelection.addEventListener('click', function (ev) {
            _this.reset();
        });
        this.buttons.appendChild(updateSelection);
        this.paneEl.appendChild(this.wrapperEl);
    }
    MobileAppSettingsPane.prototype.reset = function () {
        this.contextMenuVal.value = 'radial-menu';
        localStorage.removeItem('MobileWVContextMenuStyle');
        ui.mobileSettings.contextMenuStyle = 'radial-menu';
    };
    return MobileAppSettingsPane;
}());
exports.MobileAppSettingsPane = MobileAppSettingsPane;
function createButton(id, icon) {
    var el = document.createElement('span');
    el.id = id;
    el.style.verticalAlign = 'middle';
    var elWrapper = document.createElementNS(SVG_NS, 'svg');
    elWrapper.style.width = '19px';
    elWrapper.style.height = '19px';
    var elIcon = document.createElementNS(SVG_NS, 'use');
    elIcon.setAttributeNS(XLINK_NS, 'href', '#' + icon);
    elWrapper.appendChild(elIcon);
    el.appendChild(elWrapper);
    return el;
}


/***/ }),
/* 54 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var widgets_1 = __webpack_require__(15);
var ui = __webpack_require__(0);
var app_settings_1 = __webpack_require__(4);
var SVG_NS = 'http://www.w3.org/2000/svg';
var XLINK_NS = 'http://www.w3.org/1999/xlink';
var MobileFileBrowser = (function () {
    function MobileFileBrowser(paneEl, client, folderContent, mode) {
        var _this = this;
        this.scrollTimeOut = 0;
        this.scrollSpeed = 1;
        this.filesPerPage = 50;
        this.multiselectActive = false;
        this.fileList = [];
        this.page = 1;
        this.paneEl = paneEl;
        this.client = client;
        this.folder = folderContent;
        this.path = '';
        this.pathChanged = false;
        this.scrollStart = 0;
        this.mode = mode;
        this.getFileNumber();
        try {
            this.filesPerPage = _MobileFilesPerPage;
        }
        catch (e) { }
        this.browserActionBar = document.createElement('div');
        this.browserActionBar.className = 'mobile-file-browser-action-bar';
        this.paneEl.appendChild(this.browserActionBar);
        var multiselectIcon = document.createElement('i');
        $(multiselectIcon).addClass('far fa-check-square mobile-filebrowser-icon');
        this.browserActionBar.appendChild(multiselectIcon);
        var multiFileSubmitButton = document.createElement('div');
        multiFileSubmitButton.id = 'wv_mobile_multi_file_submit';
        multiFileSubmitButton.className = 'mobile-file-browser-multi-submit-button';
        multiFileSubmitButton.style.display = 'none';
        var multiFileSubmitIcon = document.createElement('i');
        multiFileSubmitIcon.className = 'fas fa-check mobile-file-browser-multi-submit-icon';
        multiFileSubmitButton.appendChild(multiFileSubmitIcon);
        document.body.appendChild(multiFileSubmitButton);
        multiFileSubmitButton.onclick = function (e) {
            e.preventDefault();
            e.stopPropagation();
            var length = _this.fileList.length;
            if (length > 0) {
                ui.serializedLoad(_this.fileList, true);
            }
            _this.fileList = [];
        };
        multiselectIcon.addEventListener('click', function () {
            _this.multiselectActive = !_this.multiselectActive;
            if (_this.multiselectActive) {
                multiFileSubmitButton.style.left = (_this.paneEl.clientWidth - 30) + 'px';
                $(multiFileSubmitButton).show();
            }
            else {
                $(multiFileSubmitButton).hide();
            }
            $(multiselectIcon).toggleClass('mobile-modeltree-multiselect-enabled');
            if ($(multiselectIcon).hasClass('fa')) {
                $(multiselectIcon).removeClass('fa').addClass('far');
            }
            else {
                $(multiselectIcon).removeClass('far').addClass('fa');
            }
        }, false);
        var homeEl = document.createElement('span');
        homeEl.className = 'mobile-filebrowser-icon-wrapper';
        var homeWrapper = document.createElementNS(SVG_NS, 'svg');
        homeWrapper.style.width = '21px';
        homeWrapper.style.height = '21px';
        var homeIcon = document.createElementNS(SVG_NS, 'use');
        homeIcon.setAttributeNS(XLINK_NS, 'href', '#view_button_home');
        homeWrapper.appendChild(homeIcon);
        homeEl.appendChild(homeWrapper);
        homeEl.onclick = function (e) {
            _this.breadcrumbBar.removeBreadcrumbByPath("");
        };
        this.browserActionBar.appendChild(homeEl);
        var backButton = document.createElement('i');
        $(backButton).addClass('fas fa-arrow-alt-circle-left mobile-filebrowser-icon');
        $(backButton).click(function (ev) {
            _this.breadcrumbBar.popCrumb();
        });
        this.browserActionBar.appendChild(backButton);
        var searchFieldWrapper = document.createElement('div');
        searchFieldWrapper.className = 'mobile-filebrowser-search-field-wrapper';
        var searchField = document.createElement('input');
        searchField.className = 'mobile-filebrowser-search-field';
        searchFieldWrapper.appendChild(searchField);
        searchField.onchange = function (e) {
            console.log(searchField.value);
        };
        var searchFieldIcon = document.createElement('i');
        searchFieldIcon.className = 'fas fa-times-circle mobile-filebrowser-icon';
        searchFieldWrapper.appendChild(searchFieldIcon);
        searchFieldIcon.onclick = function (e) {
            searchField.value = '';
        };
        this.breadcrumbBar = new widgets_1.MobileBreadcrumbBar(this.browserActionBar, this.client, this);
        this.outerWrapper = document.createElement('div');
        this.paneEl.appendChild(this.outerWrapper);
        this.wrapperEl = document.createElement('div');
        this.wrapperEl.className = 'mobile-file-browser';
        this.offset = $('#mobile-breadcrumb').outerHeight(true);
        this.wrapperEl.style.top = '0px';
        this.loadingWrapper = document.createElement('div');
        this.loadingWrapper.className = 'fullscreen-loading-wrapper';
        this.loadingWrapper.style.left = "0";
        var spinnerIcon = document.createElement('i');
        spinnerIcon.className = 'fullscreen-loading-icon fal fa-spinner-third fa-spin';
        this.loadingWrapper.appendChild(spinnerIcon);
        this.outerWrapper.appendChild(this.wrapperEl);
        this.outerWrapper.className = 'mobile-browser-outer-wrapper';
        this.outerWrapper.style.top = '30px';
        this.outerWrapper.id = 'mobile-browser-outer-wrapper';
        var isTouch = hasTouch;
        if (isTouch) {
            this.outerWrapper.style.overflowY = 'overlay';
        }
        else {
            $(this.wrapperEl).kendoTouch({
                drag: function (e) {
                    _this.drag(e);
                },
                dragstart: function (e) {
                    _this.dragStart(e);
                }
            });
            $(this.wrapperEl).off('wheel').on('wheel', function (ev) {
                var delta = 0;
                var scrollTop = parseFloat($(_this.wrapperEl).css('top'));
                _this.wrapperHeight = $(_this.wrapperEl).outerHeight() - $(_this.wrapperEl).parent().outerHeight() + _this.offset + 31;
                if (ev.originalEvent instanceof WheelEvent) {
                    if (ev.originalEvent.deltaY > 0) {
                        var adjusmentToScrollEnd = 0;
                        if (scrollTop + _this.wrapperHeight < 50) {
                            adjusmentToScrollEnd = 50 - (scrollTop + _this.wrapperHeight);
                        }
                        delta = -50 + adjusmentToScrollEnd;
                    }
                    else if (ev.originalEvent.deltaY < 0) {
                        var adjusmentToScrollTop = 0;
                        if (scrollTop > -50) {
                            adjusmentToScrollTop = 50 + scrollTop;
                        }
                        delta = 50 - adjusmentToScrollTop;
                    }
                    _this.wrapperEl.style.top = scrollTop + delta + 'px';
                }
            });
        }
        var footer = document.createElement('div');
        footer.className = 'mobile-browser-footer';
        this.paneEl.appendChild(footer);
        this.paginationRow = document.createElement('div');
        this.paginationRow.className = 'mobile-browser-pagination-wrapper';
        var firstPage = document.createElement('div');
        firstPage.className = 'mobile-browser-pagination-icon';
        var firstPageIcon = document.createElement('i');
        firstPageIcon.className = 'far fa-chevron-double-left';
        $(firstPage).off('click').on('click', function (ev) {
            _this.page = 1;
            _this.actualPageInput.value = _this.page.toString();
            $(_this.actualPageInput).trigger("change");
        });
        firstPage.appendChild(firstPageIcon);
        this.paginationRow.appendChild(firstPage);
        var prevPage = document.createElement('div');
        prevPage.className = 'mobile-browser-pagination-icon';
        var prevPageIcon = document.createElement('i');
        prevPageIcon.className = 'far fa-chevron-left';
        $(prevPage).off('click').on('click', function (ev) {
            if (_this.page > 1) {
                _this.page--;
            }
            _this.actualPageInput.value = _this.page.toString();
            $(_this.actualPageInput).trigger("change");
        });
        prevPage.appendChild(prevPageIcon);
        this.paginationRow.appendChild(prevPage);
        this.actualPageInput = document.createElement('input');
        this.actualPageInput.className = 'mobile-browser-actual-page-input';
        this.actualPageInput.value = this.page.toString();
        $(this.actualPageInput).change(function (ev) {
            _this.page = parseInt(_this.actualPageInput.value);
            var newFolder = _this.client.command.makeBinaryPath(_this.path, true, (_this.page - 1) * _this.filesPerPage, (_this.page * _this.filesPerPage) - 1);
            _this.startLoadingBlocker();
            _this.client.sendBinaryPackage(1, 210, newFolder);
        });
        var maxPage = document.createElement('span');
        maxPage.className = 'mobile-browser-max-page';
        maxPage.textContent = ' /' + Math.ceil(this.size / this.filesPerPage);
        this.paginationRow.appendChild(this.actualPageInput);
        this.paginationRow.appendChild(maxPage);
        var nextPage = document.createElement('div');
        nextPage.className = 'mobile-browser-pagination-icon';
        var nextPageIcon = document.createElement('i');
        nextPageIcon.className = 'far fa-chevron-right';
        $(nextPage).off('click').on('click', function (ev) {
            if (_this.page < _this.size / _this.filesPerPage) {
                _this.page++;
                _this.actualPageInput.value = _this.page.toString();
                $(_this.actualPageInput).trigger("change");
            }
        });
        nextPage.appendChild(nextPageIcon);
        this.paginationRow.appendChild(nextPage);
        var lastPage = document.createElement('div');
        lastPage.className = 'mobile-browser-pagination-icon';
        var lastPageIcon = document.createElement('i');
        lastPageIcon.className = 'far fa-chevron-double-right';
        $(lastPage).off('click').on('click', function (ev) {
            _this.page = Math.ceil(_this.size / _this.filesPerPage);
            _this.actualPageInput.value = _this.page.toString();
            $(_this.actualPageInput).trigger("change");
        });
        lastPage.appendChild(lastPageIcon);
        this.paginationRow.appendChild(lastPage);
        footer.appendChild(this.paginationRow);
        var sizeWrapper = document.createElement('div');
        sizeWrapper.style.display = 'inline-block';
        sizeWrapper.style.cssFloat = 'right';
        this.sizeEl = document.createElement('div');
        this.sizeEl.className = 'mobile-file-browser-folder-size';
        var folderFileLabel = document.createElement('div');
        folderFileLabel.className = 'mobile-file-browser-folder-size';
        folderFileLabel.id = 'ID_MOBILE_FILE_BROWSER_SIZE_LABEL';
        folderFileLabel.textContent = ui.translate('ID_MOBILE_FILE_BROWSER_SIZE_LABEL', app_settings_1.settings.lang);
        ui.translationObjects.push(folderFileLabel);
        sizeWrapper.appendChild(folderFileLabel);
        sizeWrapper.appendChild(this.sizeEl);
        footer.appendChild(sizeWrapper);
        this.client.removeAllListeners('@FolderClicked');
        this.client.on('@FolderClicked', function (response) {
            var newFolder = _this.client.command.makeBinaryPath(response, true, 0, _this.filesPerPage - 1);
            _this.client.sendBinaryPackage(1, 210, newFolder);
            _this.startLoadingBlocker();
            _this.breadcrumbBar.hideFullbar();
            _this.path = response;
            _this.pathChanged = true;
            var serializedPath = _this.path.split('\\');
            if (serializedPath.indexOf("") !== -1) {
                serializedPath.splice(serializedPath.indexOf(""), 1);
            }
            ui.mobileSettings.activePath = serializedPath;
        });
        this.client.removeAllListeners('@FileClicked');
        this.client.on('@FileClicked', function (filename) {
            if (!_this.multiselectActive) {
                if (mode === 'open') {
                    ui.openFile(filename);
                }
                else if (mode === 'import') {
                    ui.importFile(filename);
                }
            }
            else {
                var index = _this.fileList.indexOf(filename);
                if (index === -1) {
                    _this.fileList.push(filename);
                }
                else {
                    _this.fileList.splice(index, 1);
                }
            }
        });
        this.client.removeAllListeners('@GetFolderContent');
        this.client.on('GetFolderContent', function (folderContent) {
            _this.folder = folderContent;
            _this.getFileNumber();
            _this.build();
            _this.stopLoadingBlocker();
            _this.pathChanged = false;
        });
        this.client.removeAllListeners('@GetFolderFileCount');
        this.client.on('GetFolderFileCount', function (folderSize) {
            _this.sizeEl.textContent = folderSize;
            _this.size = folderSize;
            _this.wrapperEl.style.top = '0';
            if (folderSize <= _this.filesPerPage) {
                $(_this.paginationRow).hide();
            }
            else {
                $(_this.paginationRow).show();
            }
            maxPage.textContent = ' /' + Math.ceil(folderSize / _this.filesPerPage);
        });
        this.build();
        this.outerWrapper.style.height = 'calc(100% - ' + (75 + this.browserActionBar.offsetHeight) + 'px)';
    }
    MobileFileBrowser.prototype.resize = function () {
        this.outerWrapper.style.height = 'calc(100% - ' + (75 + this.browserActionBar.offsetHeight) + 'px)';
    };
    MobileFileBrowser.prototype.setInitPath = function (path) {
        var length = path.length;
        var pathName = '';
        for (var i = 0; i < length; i++) {
            pathName += path[i] + '\\';
            var pathItem = {
                key: path[i],
                path: pathName,
                date: ''
            };
            this.breadcrumbBar.addBreadcrumb(pathItem);
        }
        this.path = pathName;
    };
    MobileFileBrowser.prototype.getFileNumber = function () {
        var sizeCall = this.client.command.makeBinaryPath(this.path, false, 0, 100);
        this.client.sendBinaryPackage(1, 211, sizeCall);
    };
    MobileFileBrowser.prototype.build = function () {
        $(this.wrapperEl).empty();
        for (var i = 0; i < this.folder.length; i++) {
            if (!this.folder[i].size) {
                var newFolder = void 0;
                newFolder = {
                    key: this.folder[i].key,
                    date: this.folder[i].modified,
                    path: this.folder[i].path
                };
                var folder = new widgets_1.MobileFolder(this.wrapperEl, this.client, newFolder, this.breadcrumbBar);
            }
            else {
                var newFile = void 0;
                newFile =
                    {
                        key: this.folder[i].key,
                        date: this.folder[i].modified,
                        path: this.folder[i].path,
                        size: this.folder[i].size
                    };
                var folder = new widgets_1.MobileFile(this.wrapperEl, this.client, newFile, this.mode);
            }
        }
    };
    MobileFileBrowser.prototype.startLoadingBlocker = function () {
        this.wrapperEl.appendChild(this.loadingWrapper);
    };
    MobileFileBrowser.prototype.stopLoadingBlocker = function () {
        if (this.wrapperEl.contains(this.loadingWrapper)) {
            this.wrapperEl.removeChild(this.loadingWrapper);
        }
    };
    MobileFileBrowser.prototype.drag = function (e) {
        var distance = (e.touch.y.location - this.scrollStart) * this.scrollSpeed;
        var scrollTop = parseFloat($(this.wrapperEl).css('top'));
        if (distance > 40 || distance < -40) {
            this.scrollStart = e.touch.y.location;
        }
        else {
            return;
        }
        if (distance < 0 && scrollTop + distance >= -(this.wrapperHeight)) {
            this.wrapperEl.style.top = scrollTop + distance + 'px';
        }
        else if (distance < 0) {
            this.wrapperEl.style.top = -(this.wrapperHeight) + 'px';
            this.scrollSpeed = 1;
            this.scrollTimeOut = 0;
        }
        else if (distance > 0 && scrollTop + distance <= 0) {
            this.wrapperEl.style.top = scrollTop + distance + 'px';
        }
        else if (distance > 0) {
            this.wrapperEl.style.top = 0 + 'px';
            this.scrollSpeed = 1;
            this.scrollTimeOut = 0;
        }
    };
    MobileFileBrowser.prototype.dragStart = function (e) {
        var timeStamp = new Date().getTime();
        if (timeStamp - this.scrollTimeOut > 500) {
            this.scrollSpeed = 1;
        }
        else {
            this.scrollSpeed *= 1;
        }
        this.scrollTimeOut = timeStamp;
        this.scrollStart = e.touch.y.location;
        this.wrapperHeight = $(this.wrapperEl).outerHeight() - $(this.wrapperEl).parent().outerHeight() + this.offset + 31;
    };
    return MobileFileBrowser;
}());
exports.MobileFileBrowser = MobileFileBrowser;


/***/ }),
/* 55 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var widgets_1 = __webpack_require__(15);
var timers_1 = __webpack_require__(38);
var SVG_NS = 'http://www.w3.org/2000/svg';
var XLINK_NS = 'http://www.w3.org/1999/xlink';
var MobileModeltree = (function () {
    function MobileModeltree(paneEl, client) {
        var _this = this;
        this.rowElementArray = [];
        this.firstScrollHit = 0;
        this.scrollPosition = 0;
        this.scrollTravelled = 0;
        this.mouseScrollActive = false;
        this.barOntouchstart = function (ev) {
            _this.lastPageY = ev.touches[0].pageY;
        };
        this.barOntouchmove = function (ev) {
            var delta = ev.touches[0].pageY - _this.lastPageY;
            _this.lastPageY = ev.touches[0].pageY;
            _this.scrollPosition += delta;
            _this.scrollTravelled += delta;
            if (_this.scrollPosition < 0) {
                _this.scrollPosition = 0;
            }
            if (_this.scrollPosition > _this.treeWrapper.offsetHeight - _this.bar.offsetHeight) {
                _this.scrollPosition = _this.treeWrapper.offsetHeight - _this.bar.offsetHeight;
            }
            _this.bar.style.top = _this.scrollPosition + 'px';
        };
        this.barOnclick = function (ev) {
            _this.lastPageY = ev.pageY;
            document.addEventListener('mousemove', _this.barOndrag, false);
            document.addEventListener('mouseup', _this.barOnstop, false);
        };
        this.barOndrag = function (ev) {
            var delta = ev.pageY - _this.lastPageY;
            _this.lastPageY = ev.pageY;
            _this.scrollPosition += delta;
            _this.scrollTravelled += delta;
            if (_this.scrollPosition < 0) {
                _this.scrollPosition = 0;
            }
            if (_this.scrollPosition > _this.treeWrapper.offsetHeight - _this.bar.offsetHeight) {
                _this.scrollPosition = _this.treeWrapper.offsetHeight - _this.bar.offsetHeight;
            }
            _this.bar.style.top = _this.scrollPosition + 'px';
        };
        this.barOnstop = function (ev) {
            var step = Math.ceil(Math.abs((_this.scrollTravelled) / (20 * _this.maxVisible)) * _this.expandedNodesSize);
            var s;
            if (_this.scrollTravelled > 0) {
                s = '<Call Method="VirtualTreeViewScrollDown"><Steps>' + step + '</Steps></Call>';
                _this.client.command.executeXML(s);
            }
            else if (_this.scrollTravelled < 0) {
                s = '<Call Method="VirtualTreeViewScrollUp"><Steps>' + step + '</Steps></Call>';
                _this.client.command.executeXML(s);
            }
            document.removeEventListener('mousemove', _this.barOndrag);
            document.removeEventListener('mouseup', _this.barOnstop);
            _this.scrollTravelled = 0;
        };
        this.paneEl = paneEl;
        this.client = client;
        this.dragAccelerator = 4;
        this.dragTick = 0;
        this.isScrolling = false;
        this.multiselect = false;
        this.selectedRows = [];
        this.lastDragEndTimeStamp = new Date().getTime();
        this.wrapperEl = document.createElement('div');
        this.wrapperEl.className = 'mobile-modeltree';
        this.bar = document.createElement('div');
        this.bar.className = 'mobile-modeltree-bar';
        var actionRow = document.createElement('div');
        actionRow.className = 'mobile-modeltree-action-row';
        this.paneEl.appendChild(actionRow);
        var deleteEl = document.createElement('span');
        deleteEl.className = 'mobile-modeltree-delete';
        var deleteIcon = document.createElement('i');
        $(deleteIcon).addClass('fa fa-trash mobile-modeltree-delete-icon');
        deleteEl.appendChild(deleteIcon);
        deleteIcon.addEventListener('click', function () {
            xml = '<Call Method="ModifySelection"><SelectionModifier>Delete</SelectionModifier></Call>';
            _this.client.command.executeXML(xml);
        }, false);
        actionRow.appendChild(deleteEl);
        var multiselectEl = document.createElement('span');
        multiselectEl.className = 'mobile-modeltree-multiselect';
        this.multiselectIcon = document.createElement('i');
        $(this.multiselectIcon).addClass('far fa-check-square mobile-modeltree-multiselect-icon');
        multiselectEl.appendChild(this.multiselectIcon);
        this.multiselectIcon.addEventListener('click', function () {
            $(_this.multiselectIcon).toggleClass('mobile-modeltree-multiselect-enabled');
            if ($(_this.multiselectIcon).hasClass('fa')) {
                $(_this.multiselectIcon).removeClass('fa').addClass('far');
            }
            else {
                $(_this.multiselectIcon).removeClass('far').addClass('fa');
            }
        }, false);
        actionRow.appendChild(multiselectEl);
        this.expandAllEl = document.createElement('span');
        this.expandAllEl.style.marginLeft = '66px';
        var expandAllWrapper = document.createElementNS(SVG_NS, 'svg');
        expandAllWrapper.style.width = '20px';
        expandAllWrapper.style.height = '20px';
        expandAllWrapper.style.marginTop = '5px';
        var expandAllIcon = document.createElementNS(SVG_NS, 'use');
        expandAllIcon.setAttributeNS(XLINK_NS, 'href', '#' + 'button_plus_double');
        expandAllWrapper.appendChild(expandAllIcon);
        this.expandAllEl.appendChild(expandAllWrapper);
        this.expandAllEl.addEventListener('click', function () {
            var s = '<Call Method="VirtualTreeViewExpandAll"></Call>';
            _this.client.command.executeXML(s);
        }, false);
        actionRow.appendChild(this.expandAllEl);
        this.collapseAllEl = document.createElement('span');
        var collapseAllWrapper = document.createElementNS(SVG_NS, 'svg');
        collapseAllWrapper.style.width = '20px';
        collapseAllWrapper.style.height = '20px';
        collapseAllWrapper.style.marginTop = '5px';
        var collapseAllIcon = document.createElementNS(SVG_NS, 'use');
        collapseAllIcon.setAttributeNS(XLINK_NS, 'href', '#' + 'button_minus_double');
        collapseAllWrapper.appendChild(collapseAllIcon);
        this.collapseAllEl.appendChild(collapseAllWrapper);
        this.collapseAllEl.addEventListener('click', function () {
            var s = '<Call Method="VirtualTreeViewCollapseAll"></Call>';
            _this.client.command.executeXML(s);
        }, false);
        actionRow.appendChild(this.collapseAllEl);
        this.expandEl = document.createElement('span');
        var expandSelectedWrapper = document.createElementNS(SVG_NS, 'svg');
        expandSelectedWrapper.style.width = '19px';
        expandSelectedWrapper.style.height = '19px';
        expandSelectedWrapper.style.marginTop = '5px';
        var expandSelectedIcon = document.createElementNS(SVG_NS, 'use');
        expandSelectedIcon.setAttributeNS(XLINK_NS, 'href', '#' + 'button_plus');
        expandSelectedWrapper.appendChild(expandSelectedIcon);
        this.expandEl.appendChild(expandSelectedWrapper);
        this.expandEl.addEventListener('click', function () {
            var s = '<Call Method="VirtualTreeViewExpandSelected"></Call>';
            _this.client.command.executeXML(s);
        }, false);
        actionRow.appendChild(this.expandEl);
        this.collapseEl = document.createElement('span');
        var collapseSelectedWrapper = document.createElementNS(SVG_NS, 'svg');
        collapseSelectedWrapper.style.width = '19px';
        collapseSelectedWrapper.style.height = '19px';
        collapseSelectedWrapper.style.marginTop = '5px';
        var collapseSelectedIcon = document.createElementNS(SVG_NS, 'use');
        collapseSelectedIcon.setAttributeNS(XLINK_NS, 'href', '#' + 'button_minus');
        collapseSelectedWrapper.appendChild(collapseSelectedIcon);
        this.collapseEl.appendChild(collapseSelectedWrapper);
        this.collapseEl.addEventListener('click', function () {
            var s = '<Call Method="VirtualTreeViewCollapseSelected"></Call>';
            _this.client.command.executeXML(s);
        }, false);
        actionRow.appendChild(this.collapseEl);
        this.toggleLevelEl = document.createElement('span');
        var expandSelectedLevelWrapper = document.createElementNS(SVG_NS, 'svg');
        expandSelectedLevelWrapper.style.width = '19px';
        expandSelectedLevelWrapper.style.height = '19px';
        expandSelectedLevelWrapper.style.marginTop = '5px';
        var expandSelectedLevelIcon = document.createElementNS(SVG_NS, 'use');
        expandSelectedLevelIcon.setAttributeNS(XLINK_NS, 'href', '#' + 'button_divide');
        expandSelectedLevelWrapper.appendChild(expandSelectedLevelIcon);
        this.toggleLevelEl.appendChild(expandSelectedLevelWrapper);
        this.toggleLevelEl.addEventListener('click', function () {
            var s = '<Call Method="VirtualTreeViewExpandToSelectedLevel"></Call>';
            _this.client.command.executeXML(s);
        }, false);
        actionRow.appendChild(this.toggleLevelEl);
        this.selectSameLevelEl = document.createElement('span');
        var selectSameLevelWrapper = document.createElementNS(SVG_NS, 'svg');
        selectSameLevelWrapper.style.width = '19px';
        selectSameLevelWrapper.style.height = '19px';
        selectSameLevelWrapper.style.marginTop = '5px';
        var selectSameLevelIcon = document.createElementNS(SVG_NS, 'use');
        selectSameLevelIcon.setAttributeNS(XLINK_NS, 'href', '#' + 'button_divide');
        selectSameLevelWrapper.appendChild(selectSameLevelIcon);
        this.selectSameLevelEl.appendChild(selectSameLevelWrapper);
        this.selectSameLevelEl.addEventListener('click', function () {
            var s = '<Call Method="VirtualTreeViewSelectSameLevelNodes"></Call>';
            _this.client.command.executeXML(s);
        }, false);
        actionRow.appendChild(this.selectSameLevelEl);
        this.treeWrapper = document.createElement('div');
        this.treeWrapper.className = 'mobile-modeltree-tree-wrapper';
        this.wrapperEl.appendChild(this.treeWrapper);
        this.paneEl.appendChild(this.wrapperEl);
        this.maxVisible = (this.wrapperEl.clientHeight - actionRow.clientHeight - 32) / 20;
        var xml = '<Call Method="VirtualTreeViewSetSize"><Size>' + Math.floor(this.maxVisible) + '</Size></Call>';
        this.client.command.executeXML(xml);
        this.scrollOnKey = this.scrollOnKey.bind(this);
        this.wrapperEl.addEventListener('mouseenter', function (ev) {
            _this.toggleScrollEvents(true);
        });
        this.wrapperEl.addEventListener('mouseleave', function (ev) {
            _this.toggleScrollEvents(false);
        });
        this.wrapperEl.addEventListener('wheel', function (ev) {
            _this.bar.style.width = '15px';
            _this.bar.style.right = -_this.wrapperEl.scrollLeft + 'px';
            _this.activateScrollBarForMouse();
            if (ev.deltaY > 0) {
                var xml_1 = '<Call Method="VirtualTreeViewScrollDown"><Steps>1</Steps></Call>';
                _this.client.command.executeXML(xml_1);
                _this.client.once('VirtualTreeViewScrollDown', function (res) {
                    _this.calculateBar();
                });
            }
            else {
                var xml_2 = '<Call Method="VirtualTreeViewScrollUp"><Steps>1</Steps></Call>';
                _this.client.command.executeXML(xml_2);
                _this.client.once('VirtualTreeViewScrollUp', function (res) {
                    _this.calculateBar();
                });
            }
        });
        this.wrapperEl.appendChild(this.bar);
        $(this.wrapperEl).off('contextmenu').on('contextmenu', function (ev) {
            ev.preventDefault();
            $('#contextMenu').remove();
            $(document).off('mousedown', _this.client.vsCanvas.closeContextMenu);
            $(document).off('keyup', _this.client.vsCanvas.closeContextMenu);
            _this.client.vsCanvas.createContextMenu(ev);
        });
    }
    MobileModeltree.prototype.activateScrollBarForMouse = function () {
        var _this = this;
        if (!this.mouseScrollActive) {
            this.scrollBarRail = document.createElement('div');
            this.scrollBarRail.className = 'modeltree-bar-rail';
            this.wrapperEl.appendChild(this.scrollBarRail);
            this.scrollBarRail.addEventListener('click', function (ev) {
                var deltaClick = parseInt(_this.bar.style.top.replace("px", "")) - ev.offsetY;
                var page = _this.maxVisible;
                if (deltaClick > 0) {
                    var xml = '<Call Method="VirtualTreeViewScrollUp"><Steps>' + page + '</Steps></Call>';
                    _this.client.command.executeXMLAsync(xml);
                }
                else {
                    var xml = '<Call Method="VirtualTreeViewScrollDown"><Steps>' + page + '</Steps></Call>';
                    _this.client.command.executeXMLAsync(xml);
                }
            });
            this.bar.addEventListener('mousedown', this.barOnclick, false);
            this.bar.addEventListener('touchstart', this.barOntouchstart);
            this.bar.addEventListener('touchmove', this.barOntouchmove);
            this.bar.addEventListener('touchend', this.barOnstop);
            this.mouseScrollActive = true;
        }
    };
    MobileModeltree.prototype.toggleScrollEvents = function (state) {
        if (state) {
            $(document).off('keydown', this.scrollOnKey).on('keydown', this.scrollOnKey);
        }
        else {
            $(document).off('keydown', this.scrollOnKey);
        }
    };
    MobileModeltree.prototype.drag = function (e) {
        var loc = e.touch.y.location;
        var distance = this.touchStart - loc;
        var xml;
        this.dragTick++;
        if (distance > 30 && this.dragTick % 2 === 0) {
            this.touchStart = e.touch.y.location;
            xml = '<Call Method="VirtualTreeViewScrollDown"><Steps>' + this.dragAccelerator + '</Steps></Call>';
            this.client.command.executeXML(xml);
        }
        else if (distance < -30 && this.dragTick % 2 === 0) {
            this.touchStart = e.touch.y.location;
            xml = '<Call Method="VirtualTreeViewScrollUp"><Steps>' + this.dragAccelerator + '</Steps></Call>';
            this.client.command.executeXML(xml);
        }
    };
    MobileModeltree.prototype.dragStart = function (e) {
        this.bar.style.width = '5px';
        this.bar.style.right = -this.wrapperEl.scrollLeft + 'px';
        this.isScrolling = true;
        this.touchStart = e.touch.y.location;
        var timeStamp = new Date().getTime();
        if (timeStamp - this.lastDragEndTimeStamp < 500) {
            if (this.dragAccelerator < 32) {
                this.dragAccelerator *= 2;
            }
        }
        else {
            this.dragAccelerator = 4;
        }
    };
    MobileModeltree.prototype.dragEnd = function (e) {
        var _this = this;
        this.isScrolling = false;
        this.dragTick = 0;
        this.lastDragEndTimeStamp = new Date().getTime();
        timers_1.setTimeout(function () {
            if (!_this.isScrolling) {
                _this.bar.style.width = '0px';
            }
        }, 500);
    };
    MobileModeltree.prototype.build = function () {
        var _this = this;
        $(this.treeWrapper).detach();
        this.treeWrapper = document.createElement('div');
        this.treeWrapper.className = 'mobile-modeltree-tree-wrapper';
        this.wrapperEl.appendChild(this.treeWrapper);
        for (var item = this.rows.length - 1; item >= 0; item--) {
            var hasParent = -1;
            for (var predecessor = item - 1; predecessor >= 0; predecessor--) {
                if (this.rows[predecessor].Level + 1 === this.rows[item].Level) {
                    hasParent = predecessor;
                    break;
                }
            }
            this.rows[item].Parent = hasParent;
        }
        this.rowElementArray = [];
        var initialSelectionArray = [];
        var _loop_1 = function (i) {
            if (this_1.rows[i].NodeType === 81) {
                var sceneName = this_1.rows[i].NodeName;
                try {
                    sceneName = _SceneNameOverride;
                }
                catch (e) { }
                this_1.rows[i].NodeName = sceneName;
            }
            var row = new widgets_1.MobileTreeRow(this_1.treeWrapper, this_1.client, this_1.rows[i], i);
            this_1.rowElementArray.push(row);
            $(row.itemEl).click(function (ev) {
                var el = ev.target;
                if ($(el).hasClass('modeltree-item-visibility') || $(el).hasClass('mobile-modeltree-expander')) {
                    return;
                }
                else {
                    if ($(_this.multiselectIcon).hasClass('mobile-modeltree-multiselect-enabled')) {
                        if (_this.selectedRow) {
                            _this.selectedRows.push(_this.selectedRow);
                            _this.selectedRow = undefined;
                        }
                        var deselectRow = -1;
                        var nodeIdString = '';
                        for (var id = 0; id < _this.selectedRows.length; id++) {
                            if (_this.selectedRows[id].options.NodeId === row.options.NodeId) {
                                deselectRow = id;
                                continue;
                            }
                            var stringPart = '<NodeId>' + _this.selectedRows[id].options.NodeId + '</NodeId>';
                            nodeIdString += stringPart;
                        }
                        if (deselectRow === -1) {
                            _this.selectedRows.push(row);
                            var stringPart = '<NodeId>' + row.options.NodeId + '</NodeId>';
                            nodeIdString += stringPart;
                        }
                        else {
                            _this.selectedRows.splice(deselectRow, 1);
                        }
                        var xml = '<Call Method="SetSelectedNodes">' + nodeIdString + '</Call>';
                        _this.client.command.executeXML(xml);
                    }
                    else {
                        _this.selectedRows = [];
                        _this.selectedRow = row;
                        var xml = '<Call Method="SetSelectedNodes"><NodeId>' + row.options.NodeId + '</NodeId></Call>';
                        _this.client.command.executeXML(xml);
                    }
                }
            });
            if (row.selected === 1) {
                initialSelectionArray.push(row);
            }
        };
        var this_1 = this;
        for (var i = 0; i < this.rows.length; i++) {
            _loop_1(i);
        }
        var selectionCount = initialSelectionArray.length;
        if (initialSelectionArray.length > 1) {
            this.selectedRows = [];
            this.selectedRow = undefined;
            for (var sel = 0; sel < selectionCount; sel++) {
                this.selectedRows.push(initialSelectionArray[sel]);
            }
        }
        else if (selectionCount === 1) {
            this.selectedRows = [];
            this.selectedRow = initialSelectionArray[0];
        }
        $(this.treeWrapper).kendoTouch({
            drag: function (e) {
                _this.drag(e);
            },
            dragstart: function (e) {
                _this.dragStart(e);
            },
            dragend: function (e) {
                _this.dragEnd(e);
            }
        });
    };
    MobileModeltree.prototype.scrollOnKey = function (ev) {
        var _this = this;
        if (ev.keyCode === 37 || ev.keyCode === 39) {
            if (ev.keyCode === 37) {
                var s = '<Call Method="VirtualTreeViewCollapseSelected"></Call>';
                this.client.command.executeXML(s);
            }
            else {
                var s = '<Call Method="VirtualTreeViewExpandSelected"></Call>';
                this.client.command.executeXML(s);
            }
            return;
        }
        if (ev.keyCode !== 40 && ev.keyCode !== 38 || Date.now() - this.firstScrollHit < 200) {
            return;
        }
        this.firstScrollHit = Date.now();
        if (this.selectedRows.length === 0 && !this.selectedRow) {
            if (ev.keyCode === 40) {
                var xml = '<Call Method="VirtualTreeViewScrollDown"><Steps>1</Steps></Call>';
                this.client.command.executeXML(xml);
            }
            else if (ev.keyCode === 38) {
                var xml = '<Call Method="VirtualTreeViewScrollUp"><Steps>1</Steps></Call>';
                this.client.command.executeXML(xml);
            }
        }
        else {
            var itemCount = this.rows.length;
            var selectedItemIndex = void 0;
            for (var i = 0; i < itemCount; i++) {
                if (this.selectedRows.length > 0) {
                    if (this.rows[i].NodeId === this.selectedRows[this.selectedRows.length - 1].options.NodeId) {
                        selectedItemIndex = i;
                        break;
                    }
                }
                else if (this.rows[i].NodeId === this.selectedRow.options.NodeId) {
                    selectedItemIndex = i;
                    break;
                }
            }
            if (selectedItemIndex === 0 && ev.keyCode === 38) {
                var xml = '<Call Method="VirtualTreeViewScrollUp"><Steps>1</Steps></Call>';
                this.client.command.executeXML(xml);
                this.client.once('TreeviewUpdated', function () {
                    _this.rowElementArray[0].itemEl.click();
                });
            }
            else if (selectedItemIndex === itemCount - 1 && ev.keyCode === 40) {
                var xml = '<Call Method="VirtualTreeViewScrollDown"><Steps>1</Steps></Call>';
                this.client.command.executeXML(xml);
                this.client.once('TreeviewUpdated', function () {
                    _this.rowElementArray[_this.rowElementArray.length - 1].itemEl.click();
                });
            }
            else if (ev.keyCode === 38) {
                var newSelectedItem = this.rows[selectedItemIndex - 1];
                var length_1 = this.rows.length;
                for (var i = 0; i < length_1; i++) {
                    if (this.rowElementArray[i].options.NodeId === newSelectedItem.NodeId) {
                        this.rowElementArray[i].itemEl.click();
                    }
                }
            }
            else if (ev.keyCode === 40) {
                var newSelectedItem = this.rows[selectedItemIndex + 1];
                var length_2 = this.rows.length;
                for (var i = 0; i < length_2; i++) {
                    if (this.rowElementArray[i].options.NodeId === newSelectedItem.NodeId) {
                        this.rowElementArray[i].itemEl.click();
                    }
                }
            }
        }
    };
    MobileModeltree.prototype.updateTree = function (tree) {
        this.rows = tree.Items;
        this.expandedNodesSize = tree.ExpandedNodes;
        this.virtualScrollPos = tree.VirtualScrollPosition;
        this.calculateBar();
        this.build();
    };
    MobileModeltree.prototype.calculateBar = function () {
        if (this.expandedNodesSize > this.maxVisible) {
            var containerHeight = this.maxVisible * 20;
            var heightRatio = this.maxVisible / this.expandedNodesSize;
            var minHeight = 20;
            var barHeight = heightRatio * containerHeight;
            if (barHeight < minHeight) {
                this.bar.style.height = minHeight + 'px';
            }
            else {
                this.bar.style.height = heightRatio * 100 + '%';
            }
            var positionRatio = this.virtualScrollPos / this.expandedNodesSize;
            var barTop = positionRatio * containerHeight;
            if (barTop > containerHeight - minHeight) {
                barTop = containerHeight - minHeight;
            }
            this.bar.style.top = barTop + 'px';
        }
        else {
            this.bar.style.height = '0px';
        }
    };
    return MobileModeltree;
}());
exports.MobileModeltree = MobileModeltree;


/***/ }),
/* 56 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var ui = __webpack_require__(0);
var app_settings_1 = __webpack_require__(4);
var SVG_NS = 'http://www.w3.org/2000/svg';
var XLINK_NS = 'http://www.w3.org/1999/xlink';
var MobileSelectionPane = (function () {
    function MobileSelectionPane(paneEl, client) {
        var _this = this;
        this.rows = [];
        this.multiSelect = false;
        this.lastIndex = 0;
        this.selections = [];
        this.multiSelections = [];
        this.multiRows = [];
        this.paneEl = paneEl;
        this.client = client;
        var titleOffset = $('#ID_SELECTIONS_WND').outerHeight();
        this.wrapperEl = document.createElement('div');
        this.wrapperEl.className = 'mobile-pane-wrapper';
        this.buttons = document.createElement('div');
        this.buttons.classList.add('mobile-pane-buttons-tab');
        this.buttons.style.position = 'relative';
        this.paneEl.appendChild(this.buttons);
        this.selectionsWrapper = document.createElement('div');
        this.selectionsWrapper.className = 'mobile-pane-group';
        this.wrapperEl.appendChild(this.selectionsWrapper);
        this.wrapperEl.addEventListener('contextmenu', function (ev) {
            ev.preventDefault();
            $('#contextMenu').remove();
            $(document).off('mousedown', _this.client.vsCanvas.closeContextMenu);
            $(document).off('keyup', _this.client.vsCanvas.closeContextMenu);
            _this.client.vsCanvas.createContextMenu(ev);
        });
        var titleRow = document.createElement('span');
        titleRow.style.paddingLeft = '5px';
        titleRow.id = 'ID_SELECTIONS_COLUMN_NAME';
        titleRow.textContent = ui.translate('ID_SELECTIONS_COLUMN_NAME', app_settings_1.settings.lang);
        ui.translationObjects.push(titleRow);
        this.selectionsWrapper.appendChild(titleRow);
        this.client.on('OpenFile', function (ev) {
            _this.reset();
        });
        this.client.on('NewScene3D', function (ev) {
            _this.reset();
        });
        this.client.on('GetSavedSelections', function (res) {
            _this.buildSelectionTable(res);
        });
        this.client.on('SavedSelectionsChanged', function (res) {
            _this.buildSelectionTable(res, false);
        });
        this.rowWrapper = document.createElement('div');
        this.wrapperEl.appendChild(this.rowWrapper);
        var multiselectEl = document.createElement('span');
        multiselectEl.style.verticalAlign = 'super';
        var multiselectIcon = document.createElement('i');
        $(multiselectIcon).addClass('far fa-check-square mobile-selections-multiselect-icon');
        multiselectEl.appendChild(multiselectIcon);
        multiselectIcon.addEventListener('click', function () {
            $(multiselectIcon).toggleClass('mobile-modeltree-multiselect-enabled');
            if ($(multiselectIcon).hasClass('fa')) {
                _this.multiSelect = false;
                $(multiselectIcon).removeClass('fa').addClass('far');
            }
            else {
                _this.multiSelect = true;
                $(multiselectIcon).removeClass('far').addClass('fa');
            }
        }, false);
        var addSelection = createButton('selectionspane-add', 'view_button_plus');
        addSelection.addEventListener('click', function (ev) {
            var createCall = '<Call Method="SaveCurrentSelection"></Call>';
            _this.client.command.executeXML(createCall);
        });
        var updateSelection = createButton('selectionspane-update', 'view_button_refresh');
        updateSelection.addEventListener('click', function (ev) {
            var row = _this.selectedRow;
            var updateXML = '<Call Method="UpdateSavedSelection"><Index>' + row.id.split('_')[1] + '</Index></Call>';
            _this.client.command.executeXML(updateXML);
        });
        var removeSelection = createButton('selectionspane-remove', 'view_button_cross');
        removeSelection.addEventListener('click', function (ev) {
            var xml = '<Call Method="DeleteSavedSelections">';
            for (var i = 0; i < _this.multiRows.length; i++) {
                var row = _this.multiRows[i];
                xml += '<Index>' + row.id.split('_')[1] + '</Index>';
            }
            xml += '</Call>';
            _this.client.command.executeXML(xml);
            _this.multiRows = [];
        });
        this.buttons.appendChild(multiselectEl);
        this.buttons.appendChild(addSelection);
        this.buttons.appendChild(updateSelection);
        this.buttons.appendChild(removeSelection);
        this.paneEl.appendChild(this.wrapperEl);
    }
    MobileSelectionPane.prototype.reset = function (getNewSessions, resetSelection) {
        if (getNewSessions === void 0) { getNewSessions = true; }
        if (resetSelection === void 0) { resetSelection = true; }
        for (var i = 0; i < this.rows.length; i++) {
            $(this.rows[i]).remove();
        }
        this.rows = [];
        if (resetSelection) {
            this.multiRows = [];
            this.multiSelections = [];
            this.selectedRow = undefined;
        }
        this.selections = [];
        if (getNewSessions) {
            var xml = '<Call Method="GetSavedSelections"></Call>';
            this.client.command.executeXML(xml);
        }
    };
    MobileSelectionPane.prototype.buildSelectionTable = function (res, resetSelection) {
        if (resetSelection === void 0) { resetSelection = true; }
        this.reset(false, resetSelection);
        if (!res.Selection) {
            return;
        }
        if (res.Selection && !Array.isArray(res.Selection)) {
            res.Selection = [res.Selection];
        }
        for (var i = 0; i < res.Selection.length; i++) {
            this.addRow(res.Selection[i].Index, res.Selection[i].Name, !resetSelection);
        }
    };
    MobileSelectionPane.prototype.enableTextChange = function (element) {
        var _this = this;
        var el = $(element);
        el.attr('contenteditable', 'true');
        var range = document.createRange();
        range.selectNodeContents(element);
        var sel = window.getSelection();
        sel.removeAllRanges();
        sel.addRange(range);
        el.focus();
        el.blur(function (e) {
            $(e.target).off('blur');
            $(e.target).attr('contenteditable', 'false');
            var newName = e.target['textContent'];
            var index = e.target.parentElement.parentElement.id.split('_')[1];
            var xml = '<Call Method="RenameSavedSelection"><Index>' + index + '</Index>';
            xml += '<Name>' + newName + '</Name></Call>';
            _this.client.command.executeXML(xml);
        });
        el.on('keydown', function (e) {
            if (e.keyCode === 13) {
                e.preventDefault();
                $(e.target).off('keyup');
                $(e.target).attr('contenteditable', 'false');
            }
        });
    };
    MobileSelectionPane.prototype.addRow = function (index, name, applySelectionState) {
        var _this = this;
        if (applySelectionState === void 0) { applySelectionState = false; }
        var newRow = document.createElement('div');
        newRow.className = 'mobile-selection-row';
        newRow.id = 'Selection_' + index;
        var newRowEditIcon = document.createElement('i');
        newRowEditIcon.style.fontSize = '16px';
        newRowEditIcon.className = 'fas fa-edit selection-pane-row-icon';
        newRowEditIcon.onclick = function (ev) {
            _this.enableTextChange(newRowText);
        };
        var newRowText = document.createElement('span');
        newRowText.textContent = name;
        newRowText.addEventListener('keyup', function (ev) {
            ev.preventDefault();
            ev.stopPropagation();
        });
        var newRowBadge = document.createElement('div');
        newRowBadge.className = 'selection-pane-row-badge';
        newRowBadge.style.marginTop = '3px';
        newRowBadge.id = 'Selection_' + index + '_Number';
        newRowBadge.textContent = '-';
        var changeText = false;
        $(newRowText).mousedown(function (ev) {
            changeText = true;
            setTimeout(function () {
                if (changeText) {
                    _this.enableTextChange(newRowText);
                }
            }, 500);
        });
        $(newRowText).mouseup(function (ev) {
            changeText = false;
        });
        newRow.onclick = function (ev) {
            var selection = newRow.id.split('_')[1];
            if (ev.ctrlKey || _this.multiSelect) {
                $(newRow).toggleClass('selection-pane-row-active');
                if (_this.multiRows.indexOf(newRow) === -1) {
                    _this.multiRows.push(newRow);
                    if (_this.multiSelections.indexOf(selection) === -1) {
                        _this.multiSelections.push(selection);
                    }
                    var xml = '<Call Method="ActivateSavedSelections">';
                    for (var n = 0; n < _this.multiSelections.length; n++) {
                        xml += '<Index>' + _this.multiSelections[n] + '</Index>';
                    }
                    xml += '</Call>';
                    _this.client.command.executeXML(xml);
                }
                else {
                    var index_1 = _this.multiRows.indexOf(newRow);
                    _this.multiRows.splice(index_1, 1);
                    for (var s = 0; s < selection.length; s++) {
                        if (_this.multiSelections.indexOf(selection) !== -1) {
                            _this.multiSelections.splice(_this.multiSelections.indexOf(selection), 1);
                        }
                    }
                    var xml = '<Call Method="ActivateSavedSelections">';
                    for (var n = 0; n < _this.multiSelections.length; n++) {
                        xml += '<Index>' + _this.multiSelections[n] + '</Index>';
                    }
                    xml += '</Call>';
                    _this.client.command.executeXML(xml);
                }
            }
            else {
                $('.selection-pane-row-active').each(function (ev) {
                    $(this).removeClass('selection-pane-row-active');
                });
                $(newRow).addClass('selection-pane-row-active');
                _this.selectedRow = newRow;
                _this.multiRows = [];
                _this.multiSelections = [];
                _this.multiRows.push(newRow);
                _this.multiSelections.push(selection);
                var xml = '<Call Method="ActivateSavedSelections">';
                xml += '<Index>' + selection + '</Index>';
                xml += '</Call>';
                _this.client.command.executeXML(xml);
            }
        };
        newRow.appendChild(newRowEditIcon);
        newRow.appendChild(newRowText);
        this.rowWrapper.appendChild(newRow);
        this.rows.push(newRow);
        var newSelection = {
            id: 'Selection_' + index,
            count: '-'
        };
        this.selections.push(newSelection);
        if (applySelectionState && this.selectedRow && this.selectedRow.id.split('_')[1] === index) {
            $(newRow).addClass('selection-pane-row-active');
        }
    };
    return MobileSelectionPane;
}());
exports.MobileSelectionPane = MobileSelectionPane;
function createButton(id, icon) {
    var el = document.createElement('span');
    el.id = id;
    el.style.verticalAlign = 'middle';
    var elWrapper = document.createElementNS(SVG_NS, 'svg');
    elWrapper.style.width = '19px';
    elWrapper.style.height = '19px';
    var elIcon = document.createElementNS(SVG_NS, 'use');
    elIcon.setAttributeNS(XLINK_NS, 'href', '#' + icon);
    elWrapper.appendChild(elIcon);
    el.appendChild(elWrapper);
    return el;
}


/***/ }),
/* 57 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var app_states_1 = __webpack_require__(5);
var SVG_NS = 'http://www.w3.org/2000/svg';
var XLINK_NS = 'http://www.w3.org/1999/xlink';
var MobileViewPane = (function () {
    function MobileViewPane(paneEl, client) {
        var _this = this;
        this.currentPosition = 0;
        this.positionArray = [];
        this.hasMultiSelect = false;
        this.inDragPos = -1;
        this.paneEl = paneEl;
        this.client = client;
        this.scrollStart = 0;
        var titleOffset = $('#ID_VIEWS').outerHeight();
        this.wrapperEl = document.createElement('div');
        this.wrapperEl.className = 'mobile-view-browser';
        this.buttons = document.createElement('div');
        this.buttons.classList.add('mobile-pane-buttons-tab');
        this.buttons.style.top = titleOffset + 'px';
        var addView = this.createButton('viewpane-add', 'view_button_plus');
        addView.addEventListener('click', function (ev) {
            var createConfigurationView = '<Call Method="CreateConfigurationView"></Call>';
            _this.client.command.executeXML(createConfigurationView);
            var getConfigurationView = '<Call Method="GetConfigurationViewInformation"><Image>true</Image></Call>';
            _this.client.command.executeXML(getConfigurationView);
        });
        var deleteView = this.createButton('viewpane-delete', 'view_button_cross');
        deleteView.addEventListener('click', function (ev) {
            var mutliSelectLength = _this.positionArray.length;
            if (mutliSelectLength > 0) {
                var numArray = _this.positionArray.sort(function (a, b) { return a - b; });
                for (var i = 0; i < mutliSelectLength; i++) {
                    var positionXml = '<Position>' + (numArray[i] - i) + '</Position>';
                    var deleteConfigurationView = '<Call Method="DeleteConfigurationView">' + positionXml + '</Call>';
                    _this.client.command.executeXML(deleteConfigurationView);
                }
            }
            else {
                var positionXml = '<Position>' + _this.currentPosition + '</Position>';
                var deleteConfigurationView = '<Call Method="DeleteConfigurationView">' + positionXml + '</Call>';
                _this.client.command.executeXML(deleteConfigurationView);
            }
            var getConfigurationView = '<Call Method="GetConfigurationViewInformation"><Image>true</Image></Call>';
            _this.client.command.executeXML(getConfigurationView);
        });
        var updateView = this.createButton('viewpane-update', 'view_button_refresh');
        updateView.addEventListener('click', function (ev) {
            var mutliSelectLength = _this.positionArray.length;
            if (mutliSelectLength > 0) {
                for (var i = 0; i < mutliSelectLength; i++) {
                    var positionXml = '<Position>' + _this.positionArray[i] + '</Position>';
                    var updateConfigurationView = '<Call Method="UpdateConfigurationView">' + positionXml + '</Call>';
                    _this.client.command.executeXML(updateConfigurationView);
                }
            }
            else {
                var positionXml = '<Position>' + _this.currentPosition + '</Position>';
                var updateConfigurationView = '<Call Method="UpdateConfigurationView">' + positionXml + '</Call>';
                _this.client.command.executeXML(updateConfigurationView);
            }
            var getConfigurationView = '<Call Method="GetConfigurationViewInformation"><Image>true</Image></Call>';
            _this.client.command.executeXML(getConfigurationView);
        });
        var home = this.createButton('viewpane-last-activated', 'view_button_home');
        home.addEventListener('click', function (ev) {
            var xml = '<Call Method="ActivateOnSaveConfigurationView"></Call>';
            _this.client.command.executeXML(xml);
        });
        var reversePlay = this.createButton('viewpane-reverse-play', 'view_button_left');
        reversePlay.addEventListener('click', function (ev) {
            var xml = '<Call Method="StartPlayConfigurationViews"><Reverse>true</Reverse><ConfigurationView>' + _this.currentPosition + '</ConfigurationView></Call>';
            _this.client.command.executeXML(xml);
        });
        var stop = this.createButton('viewpane-stop', 'view_button_stop');
        stop.addEventListener('click', function (ev) {
            stopPlayMode();
        });
        this.client.on('OpenFile', function (ev) {
            stopPlayMode();
        });
        this.client.on('NewScene3D', function (ev) {
            stopPlayMode();
        });
        this.client.on('ImportFile', function (ev) {
            stopPlayMode();
        });
        this.client.on('GetSceneSettings', function (response) {
            if (response.Error === 'SUCCESS') {
                _this.duration = parseInt(response.Settings.Scene.ConfigurationView.ViewPlay.DurationTime);
            }
        });
        var xml = '<Call Method="GetSceneSettings"></Call>';
        this.client.command.executeXML(xml);
        var play = this.createButton('viewpane-play', 'view_button_right');
        play.addEventListener('click', function (ev) {
            var xml = '<Call Method="StartPlayConfigurationViews"><Reverse>false</Reverse><ConfigurationView>' + _this.currentPosition + '</ConfigurationView></Call>';
            _this.client.command.executeXML(xml);
        });
        this.client.on('ConfigurationViewPlaySelectionChange', function (res) {
            var pos = parseInt(res.Selection);
            var activeViewEvent = {
                pos: pos,
                view: _this.client.getConfigurationViews()[pos]
            };
            _this.client.emit('ConfigurationViewChanged', activeViewEvent);
        });
        this.client.on('ConfigurationViewChanged', function (event) {
            _this.currentPosition = event.pos;
            _this.positionArray = [event.pos];
            _this.setHighlight();
            if ($(_this.viewsWrapper).outerHeight() <= $(_this.wrapperEl).outerHeight() - _this.offset) {
                return;
            }
            var viewItemHeight = $('.viewpane-item').height();
            var offsetToCurrentView = $(_this.viewElements[_this.currentPosition]).offset().top + viewItemHeight;
            var offsetLowerEdge = $(_this.wrapperEl).outerHeight();
            if (offsetToCurrentView > offsetLowerEdge) {
                var newOffsetTop = _this.calculateMinMaxScroll(-1, parseInt($(_this.viewsWrapper).css('top')) - viewItemHeight);
                _this.viewsWrapper.style.top = newOffsetTop;
            }
        });
        var stopPlayMode = function () {
            var xml = '<Call Method="StopPlayConfigurationViews"></Call>';
            _this.client.command.executeXML(xml);
        };
        var multiselect = this.createButton('viewpane-multi', 'mobile_UI_check_light');
        var iconElement = $(multiselect).find('use');
        multiselect.addEventListener('click', function () {
            _this.hasMultiSelect = !_this.hasMultiSelect;
            if (!_this.hasMultiSelect) {
                iconElement[0].setAttributeNS(XLINK_NS, 'href', '#mobile_UI_check_light');
            }
            else {
                iconElement[0].setAttributeNS(XLINK_NS, 'href', '#mobile_UI_check_full');
            }
        }, false);
        var selectAllButton = document.createElement('i');
        selectAllButton.className = 'fas fa-check-double view-pane-icon';
        selectAllButton.style.bottom = '5px';
        selectAllButton.style.fontSize = '12px';
        selectAllButton.onclick = function (e) {
            _this.selectAll();
        };
        var downloadPDFButton = document.createElement('i');
        downloadPDFButton.className = 'fas fa-file-pdf view-pane-icon';
        downloadPDFButton.style.bottom = '5px';
        downloadPDFButton.style.fontSize = '12px';
        downloadPDFButton.onclick = function (e) {
            _this.createPDF();
        };
        this.buttons.appendChild(multiselect);
        this.buttons.appendChild(selectAllButton);
        this.buttons.appendChild(home);
        this.buttons.appendChild(addView);
        this.buttons.appendChild(updateView);
        this.buttons.appendChild(deleteView);
        this.buttons.appendChild(reversePlay);
        this.buttons.appendChild(stop);
        this.buttons.appendChild(play);
        this.buttons.appendChild(downloadPDFButton);
        this.paneEl.appendChild(this.buttons);
        this.paneEl.appendChild(this.wrapperEl);
        this.offset = $(this.buttons).outerHeight() + titleOffset + 10;
        this.client.on('GetConfigurationViewInformation', function (response) {
            if (!response.ConfigurationView) {
                return;
            }
            _this.views = [];
            var configurationViewsJson = Array.isArray(response.ConfigurationView) ? response.ConfigurationView : [response.ConfigurationView];
            _this.client.setConfigurationViews(configurationViewsJson);
            for (var i = 0; i < configurationViewsJson.length; i++) {
                var viewObject = configurationViewsJson[i];
                var view = {
                    position: viewObject.Position,
                    name: viewObject.Name,
                    image: viewObject.Image
                };
                _this.views.push(view);
            }
            _this.positionArray = [];
            _this.build();
            if (_this.viewElements.length && (!_this.currentPosition || _this.currentPosition === 0)) {
                _this.client.setDefaultConfigurationView(configurationViewsJson[0]);
            }
            var imagesPerRow = Math.floor($(_this.paneEl).width() / 170);
            var remainingSpace = $(_this.paneEl).width() - (imagesPerRow * 170) - 20;
            var imageHeightRatio = 90 / 160;
            var newImageWidth = 160 + remainingSpace / imagesPerRow;
            var newImageHeight = newImageWidth * imageHeightRatio;
            _this.wrapperEl.style.width = imagesPerRow * (newImageWidth + 10) + 'px';
            for (var i = 0; i < _this.viewElements.length; i++) {
                $(_this.viewElements[i]).children('img').width(newImageWidth - 40);
                $(_this.viewElements[i]).children('img').height(newImageHeight - 40);
            }
        });
        this.client.command.executeXML('<Call Method="GetConfigurationViewInformation"></Call>');
        $(this.paneEl).kendoTouch({
            drag: function (e) {
                _this.drag(e);
            },
            dragstart: function (e) {
                _this.dragStart(e);
            }
        });
        this.client.on('Resize', function () {
            setTimeout(function () {
                var imagesPerRow = Math.floor($(_this.paneEl).width() / 170);
                var remainingSpace = $(_this.paneEl).width() - (imagesPerRow * 170) - 20;
                var imageHeightRatio = 90 / 160;
                var newImageWidth = 160 + remainingSpace / imagesPerRow;
                var newImageHeight = newImageWidth * imageHeightRatio;
                _this.wrapperEl.style.width = imagesPerRow * (newImageWidth + 10) + 'px';
                for (var i = 0; i < _this.viewElements.length; i++) {
                    $(_this.viewElements[i]).children('img').width(newImageWidth - 40);
                    $(_this.viewElements[i]).children('img').height(newImageHeight - 40);
                }
            }, 500);
        });
    }
    MobileViewPane.prototype.drag = function (e) {
        if ($(this.viewsWrapper).outerHeight() <= $(this.wrapperEl).outerHeight() - this.offset) {
            return;
        }
        var distance = e.touch.y.location - this.scrollStart;
        var scrollTop = parseInt($(this.viewsWrapper).css('top'));
        if (distance > 15 || distance < -15) {
            this.scrollStart = e.touch.y.location;
        }
        if (distance < 0) {
            this.viewsWrapper.style.top = this.calculateMinMaxScroll(-1, distance + scrollTop);
        }
        else if (distance > 0) {
            this.viewsWrapper.style.top = this.calculateMinMaxScroll(1, distance + scrollTop);
        }
    };
    MobileViewPane.prototype.calculateMinMaxScroll = function (orientation, scrollSum) {
        this.wrapperHeight = $(this.viewsWrapper).outerHeight() - $(this.wrapperEl).outerHeight();
        if (orientation < 0 && scrollSum < -(this.wrapperHeight)) {
            return -(this.wrapperHeight) + 'px';
        }
        else if (orientation > 0 && scrollSum > this.offset) {
            return this.offset + 'px';
        }
        else {
            return scrollSum + 'px';
        }
    };
    MobileViewPane.prototype.dragStart = function (e) {
        this.scrollStart = e.touch.y.location;
    };
    MobileViewPane.prototype.build = function () {
        $(this.wrapperEl).empty();
        this.viewsWrapper = document.createElement('div');
        this.viewsWrapper.style.position = 'absolute';
        this.viewsWrapper.style.zIndex = '10';
        this.viewsWrapper.style.top = this.offset + 'px';
        this.wrapperEl.appendChild(this.viewsWrapper);
        this.update();
    };
    MobileViewPane.prototype.selectAll = function () {
        var _this = this;
        this.positionArray = [];
        this.viewElements.forEach(function (view, index) {
            _this.positionArray.push(index);
        });
        this.setHighlight();
    };
    MobileViewPane.prototype.createPDF = function () {
        var xml = '<Call Method="GetViewsAsPDF" CustomId="WVViewPDF">';
        if (this.positionArray.length > 0) {
            var sortedArray = this.positionArray.sort(function (a, b) { return a - b; });
            sortedArray.forEach(function (pos) {
                xml += '<Id>' + pos + '</Id>';
            });
        }
        else {
            xml += '<Id>' + this.currentPosition + '</Id>';
        }
        xml += '</Call>';
        this.client.command.executeXML(xml);
        this.client.once('GetViewsAsPDF', function (res) {
            if (res.CustomId && res.CustomId.indexOf('WVViewPDF') !== -1) {
                var name_1 = app_states_1.appStates.fileName ? 'ViewExport_' + app_states_1.appStates.fileName + '.pdf' : 'ViewExport.pdf';
                var byteCharacters = atob(res.Image);
                var byteNumbers = new Array(byteCharacters.length);
                for (var i = 0; i < byteCharacters.length; i++) {
                    byteNumbers[i] = byteCharacters.charCodeAt(i);
                }
                var byteArray = new Uint8Array(byteNumbers);
                var blob = new Blob([byteArray], {
                    type: 'application/pdf'
                });
                if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                    window.navigator.msSaveOrOpenBlob(blob, name_1);
                }
                else {
                    var link = document.createElement('a');
                    link.href = URL.createObjectURL(blob);
                    link.download = name_1;
                    document.body.appendChild(link);
                    link.click();
                    link.remove();
                }
            }
        });
    };
    MobileViewPane.prototype.createButton = function (id, icon) {
        var el = document.createElement('span');
        el.id = id;
        var elWrapper = document.createElementNS(SVG_NS, 'svg');
        elWrapper.style.width = '19px';
        elWrapper.style.height = '19px';
        var elIcon = document.createElementNS(SVG_NS, 'use');
        elIcon.setAttributeNS(XLINK_NS, 'href', '#' + icon);
        elWrapper.appendChild(elIcon);
        el.appendChild(elWrapper);
        return el;
    };
    MobileViewPane.prototype.update = function () {
        var _this = this;
        var tabindexBegin = 1000;
        this.viewElements = [];
        var _loop_1 = function (itemInfo) {
            var viewEl = document.createElement('div');
            viewEl.classList.add('viewpane-item');
            viewEl.tabIndex = tabindexBegin++;
            var viewImg = document.createElement('img');
            viewImg.width = 160;
            viewImg.height = 90;
            viewImg.style.border = '1px solid #000';
            viewImg.src = 'data:image/jpeg;base64,' + itemInfo.image;
            viewImg.draggable = false;
            viewImg.style.pointerEvents = 'none';
            viewEl.draggable = true;
            var iconBar = document.createElement('div');
            iconBar.className = 'viewpane-item-action-bar';
            var activateIcon = document.createElement('i');
            activateIcon.className = 'fas fa-play view-action-icon';
            iconBar.appendChild(activateIcon);
            activateIcon.onclick = function (e) {
                _this.client.setConfigurationViewPosition(parseInt(itemInfo.position));
            };
            var onlyCameraIcon = document.createElement('i');
            onlyCameraIcon.className = 'fas fa-video view-action-icon';
            iconBar.appendChild(onlyCameraIcon);
            onlyCameraIcon.onclick = function (e) {
                _this.client.setConfigurationViewPosition(parseInt(itemInfo.position), true, false);
            };
            var withoutCameraIcon = document.createElement('i');
            withoutCameraIcon.className = 'fas fa-video-slash view-action-icon';
            iconBar.appendChild(withoutCameraIcon);
            withoutCameraIcon.onclick = function (e) {
                _this.client.setConfigurationViewPosition(parseInt(itemInfo.position), false, true);
            };
            var viewName = document.createElement('p');
            viewName.appendChild(document.createTextNode(itemInfo.name));
            viewEl.appendChild(viewImg);
            viewEl.appendChild(iconBar);
            viewEl.appendChild(viewName);
            viewEl.addEventListener('contextmenu', function (ev) {
                ev.preventDefault();
            });
            viewEl.ondragstart = function (e) {
                _this.inDragPos = parseInt(itemInfo.position);
            };
            viewEl.ondragenter = function (e) {
                e.preventDefault();
                viewImg.style.borderLeft = '5px solid ' + app_states_1.appStates.mainColor;
            };
            viewEl.ondragleave = function (e) {
                e.preventDefault();
                viewImg.style.borderLeft = '1px solid rgb(0, 0, 0)';
            };
            viewEl.ondragover = function (e) {
                e.preventDefault();
                viewImg.style.borderLeft = '5px solid ' + app_states_1.appStates.mainColor;
            };
            viewEl.ondrop = function (e) {
                if (_this.inDragPos !== -1) {
                    var xml = '<Call Method="MoveConfigurationView">';
                    xml += '<ConfigurationView>' + _this.inDragPos + '</ConfigurationView>';
                    xml += '<Position>' + parseInt(itemInfo.position) + '</Position></Call>';
                    _this.inDragPos = -1;
                    xml += '<Call Method="GetConfigurationViewInformation"><Image>true</Image></Call>';
                    _this.client.command.executeXML(xml);
                }
            };
            viewEl.addEventListener('click', function (ev) {
                var pos = parseInt(itemInfo.position) || 0;
                if (_this.hasMultiSelect) {
                    if (_this.positionArray.indexOf(pos) === -1) {
                        _this.positionArray.push(pos);
                    }
                    else {
                        _this.positionArray.splice(_this.positionArray.indexOf(pos), 1);
                    }
                    if (_this.positionArray.indexOf(_this.currentPosition) === -1 && _this.currentPosition !== undefined) {
                        _this.positionArray.push(_this.currentPosition);
                    }
                    _this.setHighlight();
                    _this.currentPosition = undefined;
                }
                else {
                    _this.currentPosition = pos;
                    _this.positionArray = [pos];
                    _this.setHighlight();
                }
            });
            viewEl.addEventListener('dblclick', function (ev) {
                _this.activateView(itemInfo);
            });
            $(viewEl).kendoTouch({
                doubletap: function (ev) {
                    _this.activateView(itemInfo);
                },
                hold: function (ev) {
                    ev.preventDefault();
                    var el = $(viewName);
                    el.attr('contenteditable', 'true');
                    el.focus();
                    el.blur(function (e) {
                        e.stopPropagation();
                        _this.setName(e, itemInfo.position);
                        $(e.target).off('blur');
                        $(e.target).attr('contenteditable', 'false');
                    });
                    el.on('keydown', function (e) {
                        e.stopPropagation();
                        if (e.keyCode === 13) {
                            e.preventDefault();
                            $(e.target).off('keydown');
                            $(e.target).attr('contenteditable', 'false');
                        }
                    });
                    el.on('keyup', function (e) {
                        e.stopPropagation();
                    });
                }
            });
            this_1.viewsWrapper.appendChild(viewEl);
            this_1.viewElements.push(viewEl);
            this_1.currentPosition = 0;
            this_1.positionArray = [0];
            this_1.setHighlight();
        };
        var this_1 = this;
        for (var _i = 0, _a = this.views; _i < _a.length; _i++) {
            var itemInfo = _a[_i];
            _loop_1(itemInfo);
        }
        ;
    };
    MobileViewPane.prototype.setName = function (e, position) {
        var command = '<Call Method="UpdateConfigurationView"><Position>' + position + '</Position>';
        command += '<Name>' + e.target.textContent + '</Name></Call>';
        this.client.command.executeXML(command);
    };
    MobileViewPane.prototype.activateView = function (itemInfo) {
        this.client.setConfigurationViewPosition(parseInt(itemInfo.position));
    };
    MobileViewPane.prototype.setHighlight = function () {
        for (var i = 0; i < this.viewElements.length; i++) {
            if (this.positionArray.length > 0) {
                if (this.positionArray.indexOf(i) !== -1) {
                    $(this.viewElements[i]).addClass('mobile-active-view');
                }
                else {
                    $(this.viewElements[i]).removeClass('mobile-active-view');
                }
            }
            else {
                if (i === this.currentPosition) {
                    $(this.viewElements[i]).addClass('mobile-active-view');
                }
                else {
                    $(this.viewElements[i]).removeClass('mobile-active-view');
                }
            }
        }
    };
    return MobileViewPane;
}());
exports.MobileViewPane = MobileViewPane;


/***/ }),
/* 58 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
var events = __webpack_require__(6);
var ribbonButton_1 = __webpack_require__(60);
var app_states_1 = __webpack_require__(5);
var app_settings_1 = __webpack_require__(4);
var ui = __webpack_require__(0);
function getLanguages() {
    var langs = [
        { key: 'ENU', value: 'English' },
        { key: 'DEU', value: 'Deutsch' },
        { key: 'ITA', value: 'Italiano' },
        { key: 'ESP', value: 'Español' },
        { key: 'FRA', value: 'Français' },
        { key: 'KOR', value: '한국어' },
        { key: 'CHS', value: '中文' },
        { key: 'JPN', value: '日本語' },
    ];
    var ribbonLang = [];
    var _loop_1 = function (i) {
        var ribbon = {
            iconId: '',
            langId: langs[i].value,
            id: langs[i].key,
            method: function (client) {
                var xml = '<Call Method="SetSettings"><Settings><Renderer><General><Language>' + langs[i].key + '</Language></General></Renderer></Settings></Call>';
                client.command.executeXML(xml);
                client.emit('@changeLang', langs[i].key);
                localStorage.setItem('WVLang', langs[i].key);
            }
        };
        ribbonLang.push(ribbon);
    };
    for (var i = 0; i < langs.length; i++) {
        _loop_1(i);
    }
    return ribbonLang;
}
var appRibbon = {
    iconId: '',
    langId: 'App',
    id: 'rApp',
    children: [
        {
            iconId: 'mobile_UI_question_circle',
            langId: 'ID_RIBBON_ABOUT',
            id: 'rAbout',
            method: function (client) {
                client.emit('openAbout');
            }
        },
        {
            iconId: 'mobile_UI_language',
            langId: 'ID_LANGUAGE',
            id: 'rLangs',
            children: getLanguages()
        },
        {
            iconId: 'mobile_UI_gears',
            langId: 'ID_RIBBON_APP_SETTINGS',
            id: 'rAppSettings',
            paneId: 'Custom_AppSettings',
            method: function (client) {
            }
        }
    ]
};
var startDrag;
var RibbonAdapter = (function (_super) {
    __extends(RibbonAdapter, _super);
    function RibbonAdapter(el, ribbon) {
        var _this = _super.call(this) || this;
        _this.backendMode = false;
        _this.urlOpenMode = false;
        _this.visShareState = 2;
        var queryString = window.location.search.substr(1);
        var kvs = queryString.split('&');
        var query = {};
        for (var _i = 0, kvs_1 = kvs; _i < kvs_1.length; _i++) {
            var kv = kvs_1[_i];
            var _a = kv.split('='), k = _a[0], v = _a[1];
            query[k] = v;
        }
        if (typeof query['token'] !== 'undefined') {
            _this.backendMode = true;
        }
        if (typeof query['file'] !== 'undefined' || typeof query['open'] !== 'undefined') {
            try {
                if (!_ShowFileRibbonOnUrlOpen) {
                    _this.urlOpenMode = true;
                }
                else {
                    _this.urlOpenMode = false;
                }
            }
            catch (e) {
                _this.urlOpenMode = true;
            }
        }
        _this.firstLevel = [];
        _this.el = el;
        _this.ribbon = ribbon;
        _this.levelArr = [];
        _this.selectionButtons = [];
        _this.LMBButtons = [];
        _this.interactionButtons = [];
        _this.renderModeButtons = [];
        _this.buttons = [];
        var showFullClientButton = true;
        try {
            if (_KISTERSWebViewerHideFullClient === true) {
                showFullClientButton = false;
            }
        }
        catch (e) { }
        if (showFullClientButton) {
            var fullClientRibbonButton = {
                iconId: 'mobile_UI_desktop',
                langId: 'ID_RIBBON_DESKTOP_UI',
                id: 'rSwitchDesktopUI',
                method: function (client, state, ribbon) {
                    localStorage.setItem('WVParkedSession', app_states_1.appStates.session);
                    localStorage.setItem('WVFileType', app_states_1.appStates.fileType);
                    localStorage.setItem('WVVisShareSpec', app_states_1.appStates.VisShareSpec);
                    localStorage.setItem('WVFileName', app_states_1.appStates.fileName);
                    localStorage.setItem('WVUseMobile', 'false');
                    var call = '<Request Type="Park"><Timeout>0</Timeout></Request>';
                    client.sessionInfo.socket.send(call);
                    setTimeout(function () {
                        window.location.reload();
                    }, 100);
                }
            };
            appRibbon.children.push(fullClientRibbonButton);
        }
        _this.ribbon.on('button-clicked', function (button) {
            if (button.context.paneId === 'SplitPane' && !button.state.active) {
                if (ui.paneController.leftDefaultAdapter.sessions.length > 0) {
                    ui.paneController.leftDefaultAdapter.sessions = [];
                    ui.paneController.leftDefaultAdapter.activeSession = undefined;
                    ui.paneController.leftDefaultAdapter.resultInitialized = false;
                    $(ui.paneController.leftDefaultAdapter.resultGroup.wrapperEl).remove();
                    $(ui.paneController.leftDefaultAdapter.resultGroup.panelContainer).remove();
                    $(ui.paneController.leftDefaultAdapter.splitGroupTable).remove();
                }
                else if (ui.paneController.rightDefaultAdapter.sessions.length > 0) {
                    ui.paneController.rightDefaultAdapter.sessions = [];
                    ui.paneController.rightDefaultAdapter.activeSession = undefined;
                    ui.paneController.rightDefaultAdapter.resultInitialized = false;
                    $(ui.paneController.rightDefaultAdapter.resultGroup.wrapperEl).remove();
                    $(ui.paneController.rightDefaultAdapter.resultGroup.panelContainer).remove();
                    $(ui.paneController.rightDefaultAdapter.splitGroupTable).remove();
                }
            }
            var level = button.state.level - 1;
            var _loop_2 = function (i) {
                if (_this.levelArr[level][i].UID === button.UID) {
                    if (!_this.levelArr[level][i].state.settingsDependent && !_this.levelArr[level][i].context.paneId) {
                        _this.levelArr[level][i].highlight(true);
                    }
                    else {
                        _this.ribbon.vsClient.once('GetSceneSettings', function (ev) {
                            if (_this.levelArr[level] && _this.levelArr[level][i]) {
                                _this.levelArr[level][i].checkSettings(true);
                            }
                        });
                    }
                }
                else {
                    if (_this.levelArr[level][i].context.interaction === undefined
                        && !_this.levelArr[level][i].state.settingsDependent
                        && !_this.levelArr[level][i].context.paneId) {
                        _this.levelArr[level][i].highlight(false);
                    }
                    else if (_this.levelArr[level][i].state.settingsDependent) {
                        _this.ribbon.vsClient.once('GetSceneSettings', function (ev) {
                            if (_this.levelArr[level]) {
                                _this.levelArr[level][i].checkSettings(true);
                            }
                        });
                    }
                }
            };
            for (var i = 0; i < _this.levelArr[level].length; i++) {
                _loop_2(i);
            }
            if (button.context.interactionClass) {
                switch (button.context.interactionClass) {
                    case 'LMB':
                        _this.ribbon.vsClient.emit('LMB', button.context.interaction);
                        break;
                    case 'Select':
                        _this.ribbon.vsClient.emit('Select', button.context.interaction);
                        break;
                    case 'RenderMode':
                        _this.ribbon.vsClient.emit('RenderMode', button.context.interaction);
                        break;
                }
            }
        });
        _this.ribbon.vsClient.on('@changeLang', function (key) {
            for (var i = 0; i < _this.buttons.length; i++) {
                app_settings_1.settings.lang = key;
                _this.buttons[i].update();
            }
        });
        return _this;
    }
    RibbonAdapter.prototype.load = function (ribbonConf, element) {
        this.ribbonConf = ribbonConf;
        ribbonConf.push(appRibbon);
        try {
            if (Array.isArray(_CustomRibbon)) {
                for (var group in _CustomRibbon) {
                    if (!_CustomRibbon.hasOwnProperty(group)) {
                        continue;
                    }
                    ribbonConf.push(_CustomRibbon[group]);
                }
            }
            else {
                ribbonConf.push(_CustomRibbon);
            }
        }
        catch (e) { }
        this.firstLevel = [];
        var row = this.createRow(ribbonConf, this.firstLevel, undefined);
        if (row) {
            this.registerRow(this.firstLevel, row);
        }
    };
    RibbonAdapter.prototype.createRow = function (parent, row, button) {
        var _this = this;
        var level = 0;
        if (button && button.state.active === true) {
            level = button.state.level;
            if (level < this.levelArr.length) {
                var length_1 = this.levelArr.length - 1;
                for (var start = length_1; start >= level; start--) {
                    this.deleteRow(start);
                }
            }
            return;
        }
        var context;
        var rowDiv = document.createElement('div');
        rowDiv.className = 'mobile-ribbon-row-wrapper';
        if (button) {
            level = button.state.level;
            if (level < this.levelArr.length) {
                this.closeHigherLevel(level);
            }
        }
        if (level === 0) {
            rowDiv.style.bottom = '0';
            rowDiv.style.top = '0';
            rowDiv.style.border = 'none';
        }
        else {
            rowDiv.style.borderRadius = '8px';
            rowDiv.style.bottom = level * 49 + (level - 1) * 15 + 'px';
        }
        for (var i = 0; i < parent.length; i++) {
            var entry = parent[i];
            if ((this.backendMode || this.urlOpenMode)
                && (entry.langId === 'ID_RIBBON_FILE' || entry.langId === 'ID_RIBBON_IMPORT')) {
                continue;
            }
            if (entry.langId === 'ID_RIBBON_VISSHARE_SAVE' && this.visShareState !== 2) {
                continue;
            }
            if (entry.langId === 'ID_RIBBON_VISSHARE_FILE' && this.visShareState === 0) {
                continue;
            }
            try {
                if (entry.langId in _functions && _functions[entry.langId] === false) {
                    continue;
                }
            }
            catch (e) { }
            try {
                if (entry.langId in _RibbonGroups && _RibbonGroups[entry.langId] === false) {
                    continue;
                }
            }
            catch (e) { }
            try {
                if (entry.langId in _Tabs && _Tabs[entry.langId] === false) {
                    continue;
                }
            }
            catch (e) { }
            try {
                var fileType = app_states_1.appStates.fileType;
                if (fileType) {
                    if (fileType === '3D') {
                        if (_3DReducedSet.indexOf(entry.langId) !== -1) {
                            continue;
                        }
                    }
                    else if (fileType === '2D') {
                        if (_2DReducedSet.indexOf(entry.langId) !== -1) {
                            continue;
                        }
                    }
                }
            }
            catch (e) { }
            var button_1 = this.getButtonByUID(entry.id);
            if (!button_1) {
                context = {
                    iconId: entry.iconId,
                    langId: entry.langId,
                    children: undefined,
                    childrenIdx: [],
                    ribbon: this.ribbon,
                    method: undefined,
                    paneId: entry.paneId
                };
                if (entry.children) {
                    context.children = entry.children;
                    context.childrenIdx = [];
                    for (var i_1 = 0; i_1 < entry.children.length; i_1++) {
                        context.childrenIdx.push(entry.children[i_1].langId);
                    }
                }
                else if (entry.method) {
                    context.method = entry.method;
                }
                if (entry.interaction) {
                    context.interaction = entry.interaction;
                    context.interactionClass = entry.interactionClass;
                }
                button_1 = new ribbonButton_1.RibbonButton(entry.id, entry.langId, rowDiv, context, this);
                if (entry.interactionClass) {
                    switch (entry.interactionClass) {
                        case 'Select':
                            this.selectionButtons.push(button_1);
                            break;
                        case 'LMB':
                            this.LMBButtons.push(button_1);
                            break;
                        case 'Interaction':
                            this.interactionButtons.push(button_1);
                            break;
                        case 'RenderMode':
                            this.renderModeButtons.push(button_1);
                            break;
                    }
                }
                if (button_1.id === 'ID_RIBBON_COMPARE_MODEL' && ui.paneBadgeIdx.indexOf('wv-quickcompare') !== -1) {
                    button_1.state.active = true;
                    button_1.highlight(true);
                }
                this.buttons.push(button_1);
            }
            row.push(button_1);
        }
        var caretArr = [];
        var leftCaret = document.createElement('i');
        $(leftCaret).addClass('fa fa-angle-left mobile-ribbon-caret-left');
        caretArr.push(leftCaret);
        rowDiv.appendChild(leftCaret);
        var rightCaret = document.createElement('i');
        $(rightCaret).addClass('fa fa-angle-right mobile-ribbon-caret-right');
        caretArr.push(rightCaret);
        rowDiv.appendChild(rightCaret);
        $(rowDiv).kendoTouch({
            drag: function (e) {
                _this.dragRow(e, $(rowDiv), caretArr);
            },
            dragstart: function (e) {
                _this.startRowDrag(e, $(rowDiv));
            }
        });
        this.el.appendChild(rowDiv);
        return [rowDiv, leftCaret, rightCaret];
    };
    RibbonAdapter.prototype.resize = function () {
        this.closeRibbons();
        var firstRow = $('.mobile-ribbon-row-wrapper').first();
        var widthDelta = document.body.clientWidth - firstRow.outerWidth();
        $('.mobile-ribbon-caret-left').css('display', 'none');
        if (widthDelta >= 0) {
            $('.mobile-ribbon-caret-right').css('display', 'none');
        }
        else {
            $('.mobile-ribbon-caret-right').css('display', 'block');
            $('.mobile-ribbon-caret-right').css('left', window.innerWidth - 15);
        }
        firstRow.css('left', '0');
    };
    RibbonAdapter.prototype.getButton = function (langId) {
        for (var i = 0; i < this.buttons.length; i++) {
            if (this.buttons[i].id === langId) {
                return this.buttons[i];
            }
        }
        return undefined;
    };
    RibbonAdapter.prototype.getButtonByUID = function (id) {
        for (var i = 0; i < this.buttons.length; i++) {
            if (this.buttons[i].UID === id) {
                return this.buttons[i];
            }
        }
        return undefined;
    };
    RibbonAdapter.prototype.registerRow = function (row, rowElements) {
        $('#mobile-floating-deactivate-button').css('bottom', '+=63');
        $('#mobile-floating-action-button').css('bottom', '+=63');
        var windowWidth = $(window).width();
        if (this.levelArr.length > 1) {
            var lastRow = this.levelArr[this.levelArr.length - 1];
            for (var i = 0; i < lastRow.length; i++) {
                lastRow[i].hideText();
            }
            var lastRowWidth = $(lastRow[0].target).width();
            var lastRowWidthDiff = windowWidth - lastRowWidth;
            if (lastRowWidthDiff > 0) {
                lastRow[0].target.style.left = lastRowWidthDiff / 2 + 'px';
            }
        }
        this.levelArr.push(row);
        for (var button = 0; button < row.length; button++) {
            row[button].render(rowElements[0]);
            row[button].showText();
        }
        if (rowElements && $(rowElements[0]).width() > window.innerWidth) {
            rowElements[2].style.left = window.innerWidth - 15 + 'px';
            rowElements[2].style.display = 'block';
        }
        var rowWidth = $(rowElements[0]).width();
        var rowWidthDiff = windowWidth - rowWidth;
        if (rowWidthDiff > 0) {
            rowElements[0].style.left = rowWidthDiff / 2 + 'px';
        }
    };
    RibbonAdapter.prototype.deleteRow = function (index) {
        var wrapper;
        for (var buttonIndex = 0; buttonIndex < this.levelArr[index].length; buttonIndex++) {
            var button = void 0;
            button = this.levelArr[index][buttonIndex];
            if (button.target && !wrapper) {
                wrapper = button.target;
            }
            button.destroy();
        }
        if (wrapper) {
            $(wrapper).remove();
        }
        this.levelArr.splice(index, 1);
        $('#mobile-floating-deactivate-button').css('bottom', '-=63');
        $('#mobile-floating-action-button').css('bottom', '-=63');
    };
    RibbonAdapter.prototype.closeRibbons = function () {
        for (var i = this.levelArr.length; i > 1; i--) {
            this.deleteRow(i - 1);
        }
        var mockButton = new ribbonButton_1.RibbonButton('mockButtonToClose', 'mockButtonToClose', undefined, {}, this);
        mockButton.state.level = 1;
        this.ribbon.emit('button-clicked', mockButton);
    };
    RibbonAdapter.prototype.closeHigherLevel = function (level) {
        var length = this.levelArr.length - 1;
        for (var start = length; start >= level; start--) {
            this.deleteRow(start);
        }
        var row = this.levelArr[level - 1];
        for (var button = 0; button < row.length; button++) {
            row[button].showText();
        }
    };
    RibbonAdapter.prototype.startRowDrag = function (e, el) {
        startDrag = e.touch.x.location - parseInt(el.css('left'));
    };
    RibbonAdapter.prototype.dragRow = function (e, el, caretArr) {
        if (el.width() <= window.innerWidth) {
            return;
        }
        caretArr[0].style.display = 'block';
        caretArr[1].style.display = 'block';
        var newPosition = startDrag - e.touch.x.location;
        if (newPosition < 0) {
            caretArr[0].style.display = 'none';
            newPosition = 0;
        }
        if (newPosition > el.width() - window.innerWidth + 2) {
            caretArr[1].style.display = 'none';
            newPosition = el.width() - window.innerWidth + 2;
        }
        caretArr[0].style.left = newPosition + 'px';
        caretArr[1].style.left = newPosition + window.innerWidth - 15 + 'px';
        el.css('left', newPosition * -1);
    };
    RibbonAdapter.prototype.handleActivateInteraction = function (mode) {
        if (app_states_1.appStates.interactionMode.indexOf(mode) === -1) {
            this.ribbon.activateInteraction(mode);
        }
        else {
            this.ribbon.deactivateInteraction(mode);
        }
    };
    return RibbonAdapter;
}(events.EventEmitter));
exports.RibbonAdapter = RibbonAdapter;


/***/ }),
/* 59 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
var app_states_1 = __webpack_require__(5);
var app_settings_1 = __webpack_require__(4);
var events = __webpack_require__(6);
var ui = __webpack_require__(0);
var template_1 = __webpack_require__(61);
var adapter = __webpack_require__(58);
var MobileRibbon = (function (_super) {
    __extends(MobileRibbon, _super);
    function MobileRibbon(el) {
        var _this = _super.call(this) || this;
        _this.closeAboutDialog = function (e) {
            if ($(e.target).hasClass('mobile-ribbon-about-button') ||
                $(e.target).parent().parent().parent().attr('id') === 'ID_RIBBON_ABOUT' ||
                $(e.target).parent().attr('id') === 'ID_RIBBON_ABOUT' ||
                $(e.target).attr('id') === 'ID_RIBBON_ABOUT' ||
                e.target.nodeName === 'svg' || e.target.nodeName === 'path') {
                return;
            }
            var dialog = document.getElementById('aboutDialog');
            var versionWrapper = document.getElementsByClassName('mobile-ribbon-about-version-wrapper')[0];
            if ((!versionWrapper.contains(e.target) && e.type === 'click') || (e.type === 'keyup' && e.keyCode === 27)) {
                _this.aboutOpen = false;
                dialog.style.display = 'none';
                $(document).off('click', _this.closeAboutDialog);
                $(document).off('keyup', _this.closeAboutDialog);
            }
        };
        _this.vsClient = vsClient;
        _this.aboutOpen = false;
        var canvas = document.getElementById('wv-canvas');
        canvas.onclick = function () {
            _this.adapter.closeRibbons();
        };
        _this.aboutDialog = _this._buildAboutDialog();
        document.body.appendChild(_this.aboutDialog);
        _this.adapter = new adapter.RibbonAdapter(el, _this);
        return _this;
    }
    MobileRibbon.prototype.initialize = function () {
        var _this = this;
        this.render();
        vsClient.on('ActivateInteraction', function (ev) {
            var mode;
            mode = ev['Interaction'];
            if (mode && mode.indexOf('Split') === 0) {
                return;
            }
            if (!mode || mode === 'Flythrough_SelectStartPositionAndDirection') {
                return;
            }
            switch (mode) {
                case 'CuttingPlane_FromPointNormal':
                    mode = 'Section_FromPointNormal';
                    break;
                case 'CuttingPlane':
                    mode = 'Section';
                    break;
            }
            var paneId = _this.getPaneIdForInteraction(mode);
            if (app_states_1.appStates.interactionMode.indexOf(mode) === -1) {
                app_states_1.appStates.interactionMode.push(mode);
            }
            if (!paneId) {
                paneId = 'no_pane_' + mode;
            }
            for (var i = 0; i < _this.adapter.interactionButtons.length; i++) {
                var button = _this.adapter.interactionButtons[i];
                if (app_states_1.appStates.interactionMode.indexOf(button.context.interaction) !== -1) {
                    button.highlight(true);
                }
            }
            for (var i = 0; i < ui.interactionBadges.length; i++) {
                if (ui.interactionBadges[i].id === paneId) {
                    $(ui.interactionBadges[i]).addClass('mobile-badge-highlighted');
                }
            }
            ui.createInteractionBadge(paneId);
            if (mode === 'Flythrough') {
                ui.createFlyControls();
                vsClient.command.executeXML('<Call Method="GetSceneSettings"></Call>');
            }
            if (mode.indexOf('Transformation') === 0) {
                vsClient.emit('ActivateTransform', mode);
            }
            else {
                vsClient.emit('DeactivateTransform', mode);
            }
            if (mode.indexOf('Measurement') === 0) {
                var event_1 = { Mock: true, Type: 'MeasurementChanged' };
                if (mode === 'Measurement_EdgeLengthAccumulated') {
                    event_1['AccumulatedLength'] = 0;
                }
                vsClient.emit('MeasurementChanged', event_1);
            }
        });
        vsClient.on('DeactivateInteraction', function (ev) {
            var mode;
            mode = ev['Interaction'];
            if (mode && mode.indexOf('Split') === 0) {
                return;
            }
            switch (mode) {
                case 'CuttingPlane_FromPointNormal':
                    mode = 'Section_FromPointNormal';
                    break;
                case 'CuttingPlane':
                    mode = 'Section';
                    break;
            }
            var paneId = _this.getPaneIdForInteraction(mode);
            if (app_states_1.appStates.interactionMode.indexOf(mode) !== -1) {
                app_states_1.appStates.interactionMode.splice(app_states_1.appStates.interactionMode.indexOf(mode), 1);
            }
            if (!paneId) {
                paneId = 'no_pane_' + mode;
            }
            for (var i = 0; i < _this.adapter.interactionButtons.length; i++) {
                var button = _this.adapter.interactionButtons[i];
                if (app_states_1.appStates.interactionMode.indexOf(button.context.interaction) === -1) {
                    button.highlight(false);
                }
            }
            for (var i = 0; i < ui.interactionBadges.length; i++) {
                if (ui.interactionBadges[i].id === paneId) {
                    $(ui.interactionBadges[i]).removeClass('mobile-badge-highlighted');
                }
            }
            if (mode !== 'Flythrough_SelectStartPositionAndDirection') {
                ui.deleteInteractionBadge(paneId);
            }
            if (mode === 'Flythrough') {
                ui.removeFlyControls();
            }
            if (mode.indexOf('Transformation') === 0) {
                vsClient.emit('DeactivateTransform', mode);
            }
        });
        vsClient.on('LMB', function (ev) {
            app_states_1.appStates.mouseLeftButtonMode = ev;
            for (var i = 0; i < _this.adapter.LMBButtons.length; i++) {
                var button = _this.adapter.LMBButtons[i];
                if (app_states_1.appStates.mouseLeftButtonMode === button.context.interaction) {
                    button.highlight(true);
                }
                else {
                    button.highlight(false);
                }
            }
        });
        vsClient.on('Select', function (ev) {
            var oldMode = app_states_1.appStates.selectionMode;
            app_states_1.appStates.selectionMode = ev;
            if (ev === 'Neighbourhood') {
                if ($('#mobile-neighbourhood-search-panel').length === 0) {
                    ui.buildNeighbourhoodSearchPanel();
                }
                ui.togglePaneBadge('NeighbourhoodSearchPane');
                $('#mobile-neighbourhood-search-panel').show();
            }
            else {
                ui.deletePaneBadge('wv-neighbourhoodsearch');
                $('#mobile-neighbourhood-search-panel').hide();
            }
            for (var i = 0; i < _this.adapter.selectionButtons.length; i++) {
                var button = _this.adapter.selectionButtons[i];
                if (app_states_1.appStates.selectionMode === button.context.interaction) {
                    button.highlight(true);
                }
                else {
                    button.highlight(false);
                }
            }
            if (ev === 'Ghost') {
                ui.toggleGhostBadges(true, oldMode);
            }
            else {
                ui.toggleGhostBadges(false, oldMode);
            }
        });
        vsClient.on('RenderMode', function (ev) {
            var modes = app_states_1.appStates.renderingModes;
            if (Array.isArray(ev)) {
                var renderModes = ev;
                app_states_1.appStates.renderingModes = renderModes;
            }
            else {
                if (modes.indexOf(ev) !== -1) {
                    modes.splice(modes.indexOf(ev), 1);
                }
                else {
                    modes.push(ev);
                    if (modes.length === 1 && modes[0] === 'Perspective') {
                        modes.push('Solid');
                    }
                }
                var xml = '<Call Method="SetSceneSettings"><Settings><Scene><Visualization><RenderMode>';
                for (var i = 0; i < modes.length; i++) {
                    xml = xml + modes[i] + ' ';
                }
                xml = xml + '</RenderMode></Visualization></Scene></Settings></Call>';
                vsClient.command.executeXML(xml);
            }
            for (var i = 0; i < _this.adapter.renderModeButtons.length; i++) {
                var button = _this.adapter.renderModeButtons[i];
                if (modes.indexOf(button.context.interaction) !== -1) {
                    button.highlight(true);
                }
                else {
                    button.highlight(false);
                }
            }
        });
    };
    MobileRibbon.prototype.getPaneIdForInteraction = function (mode) {
        switch (mode) {
            case 'CuttingPlane_FromPointNormal':
                mode = 'Section_FromPointNormal';
                break;
            case 'CuttingPlane':
                mode = 'Section';
                break;
        }
        var generalMode = mode.split('_')[0];
        var paneId;
        switch (generalMode) {
            case 'Measurement':
                paneId = 'wv-measurement';
                break;
            case 'Section':
                paneId = 'wv-section';
                break;
            case 'Transformation':
                paneId = 'wv-transform';
                break;
            case 'DraftAngle':
                paneId = 'wv-draftangle';
                break;
            case 'Markup':
                if (mode === 'Markup_Circle' || mode === 'Markup_Freehand' || mode === 'Markup_Rectangle') {
                    paneId = 'wv-markup-reduced';
                }
                else {
                    paneId = 'wv-markup';
                }
                break;
            case 'Flythrough':
                paneId = 'wv-flythrough';
                break;
            case 'BoundaryCondition_Axis':
                paneId = 'no_pane_BoundaryCondition_Axis';
                break;
            case 'BoundaryCondition_AxisByCircle':
                paneId = 'no_pane_BoundaryCondition_AxisByCircle';
                break;
            case 'BoundaryCondition_AxisByLine':
                paneId = 'no_pane_BoundaryCondition_AxisByLine';
                break;
            case 'Callout':
                paneId = 'no_pane_' + mode;
                break;
            default:
                paneId = undefined;
                break;
        }
        return paneId;
    };
    MobileRibbon.prototype.render = function () {
        this.adapter.load(template_1.ribbonConf.ribbonConf, undefined);
    };
    MobileRibbon.prototype.resize = function () {
        this._buildAboutDialog();
        this.adapter.resize();
    };
    MobileRibbon.prototype.reset = function () {
    };
    MobileRibbon.prototype.openPage = function (url) {
        var offlineHelp = false;
        try {
            offlineHelp = _UseOfflineHelp;
        }
        catch (e) {
        }
        if (url === 'https://viewer.kisters.de/help/webviewer') {
            url = offlineHelp ? '/Help/webviewer/' + ui.mapCountry(app_settings_1.settings.lang) + '/index.html' : url + '/' + app_states_1.appStates.version + '/' + ui.mapCountry(app_settings_1.settings.lang) + '/index.php';
        }
        window.open(url, '_blank');
    };
    MobileRibbon.prototype._buildAboutDialog = function () {
        var _this = this;
        var heightWidthRatio = 0.54;
        var bodyHeight = document.body.clientHeight;
        var bodyWidth = document.body.clientWidth;
        var widthRatio = 0.9;
        var imageHeight = bodyWidth * heightWidthRatio * widthRatio;
        while (imageHeight + 50 > bodyHeight) {
            widthRatio -= 0.05;
            imageHeight = bodyWidth * heightWidthRatio * widthRatio;
        }
        var offsetTop = 0;
        if (bodyHeight - imageHeight > 50) {
            offsetTop = (bodyHeight - imageHeight - 50) / 2;
        }
        var buttons;
        try {
            buttons = _AboutLinks;
        }
        catch (e) {
            buttons = app_states_1.appStates.aboutLinks;
        }
        var buttonWidth = imageHeight * 0.11;
        var buttonPadding = buttonWidth * 0.265;
        if ($('#aboutDialog').length > 0) {
            $('#aboutDialogImage').css('marginTop', offsetTop);
            $('#aboutDialogImage').attr('width', widthRatio * 100 + '%');
            var offsetRight_1 = bodyWidth - (bodyWidth * widthRatio) - (bodyWidth * 0.05);
            var leftButtonPos_1 = ((buttons.length - 1) * buttonWidth) + ((buttons.length - 1) * buttonPadding) + offsetRight_1;
            $('.mobile-ribbon-about-button').each(function (index) {
                $(this).css({
                    'width': buttonWidth + 'px',
                    'height': buttonWidth * 0.95 + 'px',
                    'bottom': offsetTop + 52 + (buttonWidth * 0.05) + 'px',
                    'right': leftButtonPos_1 - index * buttonWidth - (index * buttonPadding) + 'px'
                });
            });
            $('.mobile-ribbon-about-default-text-wrapper').css({
                'bottom': offsetTop + 50 + (buttonWidth * 1.2) + 'px',
                'left': bodyWidth * 0.05 + (bodyWidth * widthRatio * 0.13) + 'px',
                'width': bodyWidth * widthRatio - (bodyWidth * 0.16),
                'font-size': imageHeight * 0.04 + 'px',
                'height': imageHeight * 0.05 + 'px'
            });
            $('.mobile-ribbon-about-default-copyright').css({
                'font-size': imageHeight * 0.03 + 'px',
            });
            $('.mobile-ribbon-about-version-wrapper').css({
                'bottom': offsetTop + 50 + (buttonWidth * 2.8) + 'px',
                'left': bodyWidth * 0.05 + (bodyWidth * widthRatio * 0.56) + 'px',
                'width': bodyWidth * widthRatio - (bodyWidth * widthRatio * 0.56),
                'font-size': imageHeight * 0.04 + 'px'
            });
            return;
        }
        var aboutDialogWrapper = document.createElement('div');
        aboutDialogWrapper.classList.add('ribbon-about-wrapper');
        aboutDialogWrapper.id = "aboutDialog";
        aboutDialogWrapper.style.display = 'none';
        aboutDialogWrapper.style.width = '100%';
        aboutDialogWrapper.style.height = '100%';
        var aboutDialogImage = document.createElement('img');
        aboutDialogImage.src = '3D-VS_About-Screen.png';
        aboutDialogImage.id = 'aboutDialogImage';
        aboutDialogImage.setAttribute('width', widthRatio * 100 + '%');
        aboutDialogImage.style.marginTop = offsetTop + 'px';
        aboutDialogImage.style.marginLeft = '5%';
        var defaultTextWrapper = document.createElement('div');
        defaultTextWrapper.classList.add('mobile-ribbon-about-default-text-wrapper');
        var defaultTextEdition = document.createElement('span');
        defaultTextEdition.classList.add('mobile-ribbon-about-default-text');
        defaultTextEdition.textContent = "WebViewer-Edition";
        var defaultTextCopyRight = document.createElement('span');
        defaultTextCopyRight.classList.add('mobile-ribbon-about-default-copyright');
        defaultTextCopyRight.textContent = "\u00A92020 by KISTERS All rights reserved";
        var versionTextWrapper = document.createElement('div');
        versionTextWrapper.classList.add('mobile-ribbon-about-version-wrapper');
        var programVersionWrapper = document.createElement('div');
        var programVersionTitle = document.createElement('span');
        programVersionTitle.textContent = _lang_data['ID_ABOUT_PROGRAMVERSION'][app_settings_1.settings.lang] + " ";
        var programVersion = document.createElement('span');
        programVersion.id = "program_version_text";
        programVersionWrapper.appendChild(programVersionTitle);
        programVersionWrapper.appendChild(programVersion);
        var versionImporterWrapper = document.createElement('div');
        var versionImporterTitle = document.createElement('span');
        versionImporterTitle.textContent = "Importer: ";
        var versionImporter = document.createElement('span');
        versionImporter.id = "version_importer_text";
        versionImporterWrapper.appendChild(versionImporterTitle);
        versionImporterWrapper.appendChild(versionImporter);
        vsClient.on('@changeLang', function (lang) {
            programVersionTitle.textContent = _lang_data['ID_ABOUT_PROGRAMVERSION'][app_settings_1.settings.lang] + " ";
            versionImporterTitle.textContent = "Importer: ";
        });
        versionTextWrapper.appendChild(programVersionWrapper);
        versionTextWrapper.appendChild(versionImporterWrapper);
        aboutDialogWrapper.appendChild(versionTextWrapper);
        defaultTextWrapper.appendChild(defaultTextEdition);
        defaultTextWrapper.appendChild(defaultTextCopyRight);
        aboutDialogWrapper.appendChild(defaultTextWrapper);
        aboutDialogWrapper.appendChild(aboutDialogImage);
        var offsetRight = bodyWidth - (bodyWidth * widthRatio) - (bodyWidth * 0.05);
        var leftButtonPos = ((buttons.length - 1) * buttonWidth) + ((buttons.length - 1) * buttonPadding) + offsetRight;
        var _loop_1 = function (i) {
            var button = document.createElement('div');
            button.classList.add('mobile-ribbon-about-button');
            button.style.right = (leftButtonPos - (i * buttonWidth)) + 'px';
            button.style.width = buttonWidth + 'px';
            button.style.height = buttonWidth + 'px';
            $(button).click(function (e) {
                e.preventDefault();
                _this.openPage(buttons[i]);
            });
            aboutDialogWrapper.appendChild(button);
        };
        for (var i = 0; i < buttons.length; i++) {
            _loop_1(i);
        }
        vsClient.on('openAbout', function () {
            if (_this.aboutOpen === false) {
                var xml = '<Call Method="GetVersionInfo"></Call>';
                vsClient.command.executeXML(xml);
                vsClient.once("GetVersionInfo", function (response) {
                    var versionArr = response.Version.split('.');
                    versionArr.pop();
                    app_states_1.appStates.version = versionArr.join('.');
                    $('#program_version_text').text(response.LongVersion);
                    $('#version_importer_text').text(response.ImporterVersion);
                });
                $(aboutDialogWrapper).show();
                $(document).on('click', _this.closeAboutDialog);
                $(document).on('keyup', _this.closeAboutDialog);
                _this.aboutOpen = true;
            }
            else {
                $(aboutDialogWrapper).hide();
                $(document).off('click', _this.closeAboutDialog);
                $(document).off('keyup', _this.closeAboutDialog);
                _this.aboutOpen = false;
            }
        });
        return aboutDialogWrapper;
    };
    MobileRibbon.prototype.activateInteraction = function (action) {
        var s = '<Call Method="ActivateInteraction"><Interaction>' + action + '</Interaction></Call>';
        vsClient.command.executeXML(s);
    };
    MobileRibbon.prototype.deactivateInteraction = function (mode) {
        if (mode === void 0) { mode = 'Deactivate_AllInteractions'; }
        var modeCategory = mode.split('_')[0];
        var categoryToDeactivate = 'Deactivate_' + modeCategory;
        var s = '<Call Method="ActivateInteraction"><Interaction>' + categoryToDeactivate + '</Interaction></Call>';
        vsClient.command.executeXML(s);
    };
    MobileRibbon.prototype.deleteShareRibbon = function (deleteSaveAs) {
        this.adapter.visShareState = deleteSaveAs ? 0 : 1;
    };
    return MobileRibbon;
}(events.EventEmitter));
exports.MobileRibbon = MobileRibbon;


/***/ }),
/* 60 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var app_settings_1 = __webpack_require__(4);
var app_states_1 = __webpack_require__(5);
var ui = __webpack_require__(0);
var SVG_NS = 'http://www.w3.org/2000/svg';
var XLINK_NS = 'http://www.w3.org/1999/xlink';
var RibbonButton = (function () {
    function RibbonButton(UID, id, target, context, adapter) {
        this.el = document.createElement('div');
        this.el.id = id;
        this.el.classList.add('mobile-ribbon-button');
        this.context = context;
        this.target = target;
        this.id = id;
        this.UID = UID;
        this.adapter = adapter;
        this.state = { active: false, level: adapter.levelArr.length + 1 };
        this.shown = false;
        this.interaction = context.interaction;
        var innerWrapper = document.createElement('div');
        innerWrapper.classList.add('mobile-ribbon-button-wrapper');
        this.label = document.createElement('div');
        this.label.classList.add('mobile-ribbon-button-text');
        if (this.context.iconId !== '') {
            this.el.style.padding = '6px';
            if (this.context.children) {
                var chevronUp = document.createElement('i');
                chevronUp.className = 'fas fa-chevron-up';
                this.el.appendChild(chevronUp);
                this.el.style.paddingTop = '4px';
            }
            else {
                this.label.style.paddingTop = '10px';
                innerWrapper.style.paddingTop = '2px';
            }
            this.icon = document.createElementNS(SVG_NS, 'svg');
            this.icon.setAttribute('class', 'mobile-ribbon-button-icon');
            var useEl = document.createElementNS(SVG_NS, 'use');
            useEl.setAttributeNS(XLINK_NS, 'href', '#' + this.context.iconId);
            this.icon.appendChild(useEl);
            this.label.style.display = 'none';
            innerWrapper.appendChild(this.icon);
        }
        this.el.appendChild(this.label);
        this.el.appendChild(innerWrapper);
        this.addClick(this.state, context.method);
        if (this.target) {
            this.target.appendChild(this.el);
        }
    }
    RibbonButton.prototype.showText = function () {
        if (this.context.iconId !== '') {
            this.label.style.display = '';
            this.icon.style.height = '20px';
            this.el.style.paddingBottom = '6px';
            if (!this.context.children) {
                this.el.style.paddingTop = '6px';
            }
        }
    };
    RibbonButton.prototype.hideText = function () {
        if (this.context.iconId !== '') {
            this.label.style.display = 'none';
            this.icon.style.height = '30px';
            this.el.style.paddingBottom = '9px';
            if (!this.context.children) {
                this.el.style.paddingTop = '12px';
            }
        }
    };
    RibbonButton.prototype.setText = function () {
        var langId = this.context.langId.replace('__N', '');
        if (_lang_data[langId] && _lang_data[langId][app_settings_1.settings.lang]) {
            return _lang_data[langId][app_settings_1.settings.lang];
        }
        else if (_custom_lang_data[langId] && _custom_lang_data[langId][app_settings_1.settings.lang]) {
            return _custom_lang_data[langId][app_settings_1.settings.lang];
        }
        else if (_lang_data[langId] && _lang_data[langId]['ENU']) {
            return _lang_data[langId]['ENU'];
        }
        else if (_custom_lang_data[langId] && _custom_lang_data[langId]['ENU']) {
            return _custom_lang_data[langId]['ENU'];
        }
        return langId;
    };
    RibbonButton.prototype.render = function (target) {
        this.label.textContent = this.setText();
        if (this.context.interactionClass) {
            switch (this.context.interactionClass) {
                case 'Interaction':
                    if (app_states_1.appStates.interactionMode.indexOf(this.context.interaction) !== -1) {
                        this.highlight(true);
                    }
                    break;
                case 'Select':
                    if (app_states_1.appStates.selectionMode === this.context.interaction) {
                        this.highlight(true);
                    }
                    else if (this.shown) {
                        this.highlight(false);
                    }
                    break;
                case 'LMB':
                    if (app_states_1.appStates.mouseLeftButtonMode === this.context.interaction) {
                        this.highlight(true);
                    }
                    else if (this.shown) {
                        this.highlight(false);
                    }
                    break;
                case 'RenderMode':
                    if (app_states_1.appStates.renderingModes.indexOf(this.context.interaction) !== -1) {
                        this.highlight(true);
                    }
                    else {
                        this.highlight(false);
                    }
                    break;
            }
        }
        this.checkSettings(true);
        this.shown = true;
        target.appendChild(this.el);
        this.target = target;
    };
    RibbonButton.prototype.checkSettings = function (forceHighlight) {
        if (ui.paneController && ui.paneController.sceneSettings) {
            var dimensioning = ui.paneController.sceneSettings.Scene.Tools.Dimensioning.Dimensioning === 'true';
            var grid = ui.paneController.sceneSettings.Scene.Visualization.ShowGrid === 'true';
            var arcMode = ui.paneController.sceneSettings.Scene.Tools.CircleSelection.SelectCircleWith3Points === 'false';
            var selectAssociatedNode = ui.paneController.sceneSettings.Scene.Tools.Callout.SelectAssociatedNodes === 'true';
            var multiAnchors = ui.paneController.sceneSettings.Scene.Tools.Callout.MultiAnchorGroups === 'true';
            var freeRotateMouseMode = ui.paneController.sceneSettings.Scene.Mouse.UseFreeRotate === 'true';
            var shouldAnimate = ui.paneController.sceneSettings.Scene.General.Animation === 'true';
            if (this.id === 'ID_RIBBON_MEASUREMENT__N') {
                this.setSettingDependentState(!dimensioning, forceHighlight);
            }
            else if (this.id === 'ID_RIBBON_DIMENSION') {
                this.setSettingDependentState(dimensioning, forceHighlight);
            }
            else if (this.id === 'rGrid') {
                this.setSettingDependentState(grid, forceHighlight);
            }
            else if (this.id === 'ID_RIBBON_ARC') {
                this.setSettingDependentState(arcMode, forceHighlight);
            }
            else if (this.id === 'ID_RIBBON_3_POINTS') {
                this.setSettingDependentState(!arcMode, forceHighlight);
            }
            else if (this.id === 'ID_RIBBON_CALLOUTS_SELECT_ASSOCIATED_NODES') {
                this.setSettingDependentState(selectAssociatedNode, forceHighlight);
            }
            else if (this.id === 'ID_RIBBON_CALLOUTS_MULTIPLE_CONNECTIONS') {
                this.setSettingDependentState(multiAnchors, forceHighlight);
            }
            else if (this.id === 'ID_MOUSE_USE_FREE_ROTATE') {
                this.setSettingDependentState(freeRotateMouseMode, forceHighlight);
            }
            else if (this.id === 'ID_RIBBON_ANIMATE' && this.UID === 'rAnimationStart') {
                this.setSettingDependentState(shouldAnimate, forceHighlight);
            }
        }
    };
    RibbonButton.prototype.setSettingDependentState = function (bool, forceHighlight) {
        this.state.active = bool;
        this.state.settingsDependent = true;
        if (forceHighlight) {
            this.highlight(bool);
        }
    };
    RibbonButton.prototype.destroy = function () {
        this.shown = false;
        $(this.el).remove();
    };
    RibbonButton.prototype.update = function () {
        this.label.textContent = this.setText();
    };
    RibbonButton.prototype.addClick = function (state, clickCallback) {
        var _this = this;
        this.el.addEventListener('click', function (ev) {
            if (clickCallback) {
                clickCallback(vsClient, state, _this.adapter);
                _this.adapter.closeHigherLevel(_this.state.level);
            }
            else {
                if (_this.context.children) {
                    var newRow = [];
                    var row = _this.adapter.createRow(_this.context.children, newRow, _this);
                    _this.adapter.registerRow(newRow, row);
                }
            }
            if (_this.interaction && _this.context.interactionClass === 'Interaction') {
                _this.adapter.handleActivateInteraction(_this.interaction);
            }
            else if (_this.interaction && _this.context.interactionClass === 'LMB') {
                var xml_1 = '<Call Method="SetSceneSettings"><Settings><Scene><Mouse><LeftButtonFunction>';
                xml_1 += _this.interaction;
                xml_1 += '</LeftButtonFunction></Mouse></Scene></Settings></Call>';
                vsClient.command.executeXML(xml_1);
            }
            else if (_this.interaction && _this.context.interactionClass === 'Select') {
                var xml = '<Call Method="SetSelectionMode"><SelectionMode>' + _this.interaction + '</SelectionMode></Call>';
                vsClient.command.executeXML(xml);
            }
            if (_this.context.paneId) {
                var paneId = ui.paneController.mapPaneTemplateToId(_this.context.paneId);
                var hiddenPane = false;
                try {
                    if (_DisabledPanes.indexOf(paneId) !== -1) {
                        hiddenPane = true;
                    }
                }
                catch (e) { }
                var wasDeleted = false;
                if (!hiddenPane) {
                    wasDeleted = ui.togglePaneBadge(_this.context.paneId);
                }
                if (wasDeleted) {
                    try {
                        if (_MobileRightMoreBadge.indexOf(paneId) !== -1) {
                            ui.paneController.render(paneId, 'right');
                        }
                        else {
                            ui.paneController.render(paneId, 'left');
                        }
                    }
                    catch (e) {
                        ui.paneController.render(paneId, 'left');
                    }
                }
                if (_this.context.paneId !== 'Custom_AppSettings') {
                    if (_this.context.paneId === 'ClashPane' && !wasDeleted) {
                        var clashCloseCall = '<Call Method="DeactivateClash"></Call>';
                        vsClient.command.executeXML(clashCloseCall);
                    }
                    _this.highlight(wasDeleted);
                }
                else {
                    $(_this.el).addClass('ribbon-active');
                    setTimeout(function () {
                        $(_this.el).removeClass('ribbon-active');
                    }, 200);
                }
            }
            _this.context.ribbon.emit('button-clicked', _this);
        });
    };
    RibbonButton.prototype.highlight = function (bool) {
        var _this = this;
        if (this.context.interaction || this.context.paneId) {
            this.state.active = bool;
        }
        if (bool === true) {
            if (this.context.children) {
                $(this.el).addClass('ribbon-parent-active');
            }
            else {
                $(this.el).addClass('ribbon-active');
                if (!this.context.interaction && !this.state.active) {
                    setTimeout(function () {
                        if (!_this.state.active) {
                            $(_this.el).removeClass('ribbon-active');
                        }
                    }, 200);
                }
            }
        }
        else {
            if (this.context.children) {
                $(this.el).removeClass('ribbon-parent-active');
            }
            else {
                $(this.el).removeClass('ribbon-active');
            }
        }
    };
    return RibbonButton;
}());
exports.RibbonButton = RibbonButton;


/***/ }),
/* 61 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var RibbonConf = (function () {
    function RibbonConf() {
        this.ribbonConf = [
            {
                iconId: '',
                langId: 'ID_RIBBON_START',
                id: 'rStart',
                children: [
                    {
                        iconId: '2_oeffnen',
                        langId: 'ID_RIBBON_FILE',
                        id: 'rOpen',
                        method: function (client) {
                            client.emit('openFileDialog');
                        }
                    },
                    {
                        iconId: '3_importieren',
                        langId: 'ID_RIBBON_IMPORT',
                        id: 'rImport',
                        method: function (client) {
                            client.emit('importFileDialog');
                        }
                    },
                    {
                        iconId: '4_speichern',
                        langId: 'ID_RIBBON_VISSHARE_FILE',
                        id: 'rVisShare',
                        children: [
                            {
                                iconId: '4_speichern',
                                langId: 'ID_RIBBON_VISSHARE_SAVE',
                                id: 'rVisShareSave',
                                method: function (client) {
                                    client.emit('VisShareSave');
                                }
                            },
                            {
                                iconId: '4_speichern',
                                langId: 'ID_RIBBON_VISSHARE_SAVE_AS',
                                id: 'rVisShareSaveAs',
                                method: function (client) {
                                    client.emit('VisShareSaveAs');
                                }
                            }
                        ]
                    },
                    {
                        iconId: '6_drucken',
                        langId: 'ID_RIBBON_PRINT',
                        id: 'rPrint',
                        method: function (client) {
                            client.emit('openPrintDialog');
                        }
                    },
                    {
                        iconId: '1_letzte_aktionen',
                        langId: 'ID_RIBBON_LAST_ACTIONS',
                        id: 'rUndo',
                        method: function (client) {
                            var xml = '<Call Method="Undo"></Call>';
                            client.command.executeXML(xml);
                        }
                    },
                    {
                        iconId: '1_freies_verschieben',
                        langId: 'ID_RIBBON_CONTROLS',
                        id: 'rControls',
                        children: [
                            {
                                iconId: '1_zoom_rahmen',
                                langId: 'ID_RIBBON_ZOOM_RECTANGLE',
                                id: 'rZoomRect',
                                interactionClass: 'LMB',
                                interaction: 'RectZoom',
                                method: function (client) {
                                }
                            }, {
                                iconId: '2_drehen',
                                langId: 'ID_RIBBON_ROTATE',
                                id: 'rRotate',
                                interactionClass: 'LMB',
                                interaction: 'Rotate',
                                method: function (client) {
                                }
                            }, {
                                iconId: '3_pan',
                                langId: 'ID_RIBBON_PAN',
                                id: 'rPan',
                                interactionClass: 'LMB',
                                interaction: 'Pan',
                                method: function (client) {
                                }
                            }, {
                                iconId: '3_drehen',
                                langId: 'ID_RIBBON_TURNTABLE',
                                id: 'rTurntable',
                                interactionClass: 'LMB',
                                interaction: 'Turntable',
                                method: function (client) {
                                }
                            }, {
                                iconId: '4_auswahl_rahmen',
                                langId: 'ID_RIBBON_SELECTION_FRAME',
                                id: 'rSelectionFrame',
                                interactionClass: 'LMB',
                                interaction: 'RectSelect',
                                method: function (client) {
                                }
                            }, {
                                iconId: '5_zoom',
                                langId: 'ID_RIBBON_ZOOM',
                                id: 'rZoom',
                                interactionClass: 'LMB',
                                interaction: 'Zoom',
                                method: function (client) {
                                }
                            },
                            {
                                iconId: 'bim_mode',
                                langId: 'ID_MOUSE_USE_FREE_ROTATE',
                                id: 'rFreeRotateMouseMode',
                                interactionClass: 'FreeRotateMouseMode',
                                method: function (client, state) {
                                    var xml = '<Call Method="SetSceneSettings"><Settings><Scene><Mouse><UseFreeRotate>';
                                    xml += !state.active;
                                    xml += '</UseFreeRotate></Mouse></Scene></Settings></Call>';
                                    client.command.executeXML(xml);
                                    state.active = !state.active;
                                }
                            },
                        ]
                    },
                    {
                        iconId: '1_selektieren',
                        langId: 'ID_RIBBON_SELECTION_MODE',
                        id: 'rSelectionMode',
                        children: [
                            {
                                iconId: '1_selektieren',
                                langId: 'ID_RIBBON_SELECT',
                                id: 'rSelecetMode',
                                interactionClass: 'Select',
                                interaction: 'Select',
                                method: function (client) {
                                }
                            }, {
                                iconId: '3_verbergen',
                                langId: 'ID_RIBBON_HIDE',
                                id: 'rHideMode',
                                interactionClass: 'Select',
                                interaction: 'Hide',
                                method: function (client) {
                                }
                            }, {
                                iconId: '4_loeschen',
                                langId: 'ID_RIBBON_DELETE',
                                id: 'rDeleteMode',
                                interactionClass: 'Select',
                                interaction: 'Delete',
                                method: function (client) {
                                }
                            }, {
                                iconId: '5_ghost',
                                langId: 'ID_RIBBON_GHOST',
                                id: 'rGhostMode',
                                interactionClass: 'Select',
                                interaction: 'Ghost',
                                method: function (client) {
                                }
                            },
                            {
                                iconId: '2_nachbarschaft',
                                langId: 'ID_RIBBON_NEIGHBOURHOOD_SEARCH',
                                id: 'rNeighbourHood',
                                interactionClass: 'Select',
                                interaction: 'Neighbourhood',
                                method: function (client) {
                                }
                            },
                            {
                                iconId: '10_auswahl_durch_farbe',
                                langId: 'ID_RIBBON_COLOR',
                                id: 'rColorMode',
                                interactionClass: 'Select',
                                interaction: 'Color',
                                method: function (client) {
                                }
                            },
                            {
                                iconId: 'selection_mode_instance',
                                langId: 'ID_RIBBON_INSTANCE',
                                id: 'rInstanceMode',
                                interactionClass: 'Select',
                                interaction: 'Instance',
                                method: function (client) {
                                }
                            }
                        ]
                    }, {
                        iconId: '3_isolieren',
                        langId: 'ID_RIBBON_SELECTION',
                        id: 'rSelection',
                        children: [
                            {
                                iconId: '1_alles_selektieren',
                                langId: 'ID_RIBBON_SELECT_ALL',
                                id: 'rSelectAll',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="SelectAllGeometries"></Call>');
                                }
                            },
                            {
                                iconId: '2_auswahl_umkehren',
                                langId: 'ID_RIBBON_INVERT_SELECTION',
                                id: 'rInvertSelection',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Invert</SelectionModifier></Call>');
                                }
                            },
                            {
                                iconId: '3_isolieren',
                                langId: 'ID_RIBBON_ISOLATE',
                                id: 'rIsolate',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Isolate</SelectionModifier></Call>');
                                }
                            },
                            {
                                iconId: '4_anzeigen',
                                langId: 'ID_SHOW_SELECTION',
                                id: 'rShow',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Show</SelectionModifier></Call>');
                                }
                            },
                            {
                                iconId: '5_verbergen',
                                langId: 'ID_HIDE_SELECTION',
                                id: 'rHide',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Hide</SelectionModifier></Call>');
                                }
                            },
                            {
                                iconId: '6_loeschen',
                                langId: 'ID_DELETE_SELECTION',
                                id: 'rDelete',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Delete</SelectionModifier></Call>');
                                }
                            },
                            {
                                iconId: '7_ghost',
                                langId: 'ID_GHOST_SELECTION',
                                id: 'rGhost',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Ghost</SelectionModifier></Call>');
                                }
                            },
                            {
                                iconId: '8_ghost_ausschalten',
                                langId: 'ID_RIBBON_UNGHOST',
                                id: 'rUnGhost',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>UnGhost</SelectionModifier></Call>');
                                }
                            },
                            {
                                iconId: '9_auswahl_aufheben',
                                langId: 'ID_RIBBON_DESELECT',
                                id: 'rDeselect',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Deselect</SelectionModifier></Call>');
                                }
                            },
                            {
                                iconId: '10_auswahl_durch_farbe',
                                langId: 'ID_RIBBON_COLOR',
                                id: 'rColor',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>SearchByColor</SelectionModifier></Call>');
                                }
                            }
                        ]
                    },
                    {
                        iconId: '5_sichtbarkeit_umkehren',
                        langId: 'ID_RIBBON_SHOW/HIDE',
                        id: 'rShowHide',
                        children: [
                            {
                                iconId: '1_alles_anzeigen',
                                langId: 'ID_RIBBON_SHOW_ALL',
                                id: 'rShowAll',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ShowAll"></Call>');
                                }
                            }, {
                                iconId: '2_ghost_ausschalten',
                                langId: 'ID_RIBBON_UNGHOST_ALL',
                                id: 'rUnGhostAll',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="UnGhostAll"></Call>');
                                }
                            }, {
                                iconId: '3_markups_verbergen',
                                langId: 'ID_RIBBON_HIDE_ALL_MARKUPS',
                                id: 'rHideAllMarkups',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="HideAllMarkups"></Call>');
                                }
                            }, {
                                iconId: '4_pmis_verbergen',
                                langId: 'ID_RIBBON_HIDE_ALL_PMIS',
                                id: 'rHideAllPMIS',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="HideAllPMIs"></Call>');
                                }
                            }, {
                                iconId: '5_sichtbarkeit_umkehren',
                                langId: 'ID_RIBBON_INVERT_VISIBILITY',
                                id: 'rInvertVis',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="InvertVisibility"></Call>');
                                }
                            }
                        ]
                    },
                    {
                        iconId: '1_anpassen',
                        langId: 'ID_RIBBON_ZOOM',
                        id: 'rZoomS',
                        children: [
                            {
                                iconId: '1_anpassen',
                                langId: 'ID_RIBBON_FIT_ALL',
                                id: 'rFitAllS',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="FitAll"></Call>');
                                }
                            }, {
                                iconId: '2_vergroessern',
                                langId: 'ID_RIBBON_ZOOM_IN',
                                id: 'rZoomInS',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ZoomIn"></Call>');
                                }
                            }, {
                                iconId: '3_verkleinern',
                                langId: 'ID_RIBBON_ZOOM_OUT',
                                id: 'rZoomOutS',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ZoomOut"></Call>');
                                }
                            }
                        ]
                    }
                ]
            }, {
                iconId: '',
                langId: 'ID_RIBBON_DOCUMENT',
                id: 'rDocument',
                children: [
                    {
                        iconId: '3_pan',
                        langId: 'ID_RIBBON_PAN',
                        id: 'rPanD',
                        method: function (client) {
                            var s = "<Call Method=\"SetSceneSettings\">\n                        <Settings>\n                            <Scene>\n                            <Mouse>\n                            <LeftButtonFunction>\n                               Pan\n                            </LeftButtonFunction>\n                            </Mouse>\n                            </Scene>\n                        </Settings>\n                     </Call>";
                            client.command.executeXML(s);
                        }
                    }, {
                        iconId: '1_zoom_rahmen',
                        langId: 'ID_RIBBON_ZOOM_RECTANGLE',
                        id: 'rZoomRectD',
                        interactionClass: 'LMB',
                        interaction: 'RectZoom',
                        method: function (client) {
                        }
                    }, {
                        iconId: '1_vorherige',
                        langId: 'ID_RIBBON_PREVIOUS',
                        id: 'rPrev',
                        method: function (client) {
                            var xml = '<Call Method="PreviousPage"></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: '2_naechtse',
                        langId: 'ID_RIBBON_NEXT',
                        id: 'rNext',
                        method: function (client) {
                            var xml = '<Call Method="NextPage"></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: '3_270_Grad',
                        langId: 'ID_RIBBON_ROTATE',
                        id: 'rRotateS',
                        children: [
                            {
                                iconId: '1_90_Grad',
                                langId: 'ID_RIBBON_90',
                                id: 'r90',
                                method: function (client) {
                                    var xml = '<Call Method="RotatePage"></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '2_180_Grad',
                                langId: 'ID_RIBBON_180',
                                id: 'r180',
                                method: function (client) {
                                    var xml = '<Call Method="RotatePage"></Call>';
                                    client.command.executeXML(xml);
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '3_270_Grad',
                                langId: 'ID_RIBBON_270',
                                id: 'r270',
                                method: function (client) {
                                    var xml = '<Call Method="RotatePage"></Call>';
                                    client.command.executeXML(xml);
                                    client.command.executeXML(xml);
                                    client.command.executeXML(xml);
                                }
                            }
                        ]
                    }, {
                        iconId: '1_anpassen',
                        langId: 'ID_RIBBON_ZOOM',
                        id: 'rZoomD',
                        children: [
                            {
                                iconId: '1_anpassen',
                                langId: 'ID_RIBBON_FIT_ALL',
                                id: 'rFittAllD',
                                method: function (client) {
                                    var xml = '<Call Method="FitAll"></Call>';
                                    client.command.executeXML(xml);
                                }
                            },
                            {
                                iconId: '2_an_fensterbreite_anpassen',
                                langId: 'ID_RIBBON_FIT_WIDTH',
                                id: 'rFitWidth',
                                method: function (client) {
                                    var xml = '<Call Method="FitWidth"></Call>';
                                    client.command.executeXML(xml);
                                }
                            },
                            {
                                iconId: '3_an_fensterhoehe_anpassen',
                                langId: 'ID_RIBBON_FIT_HEIGHT',
                                id: 'rFitHeight',
                                method: function (client) {
                                    var xml = '<Call Method="FitHeight"></Call>';
                                    client.command.executeXML(xml);
                                }
                            },
                            {
                                iconId: '4_vergroessern',
                                langId: 'ID_RIBBON_ZOOM_IN',
                                id: 'rZoomInD',
                                method: function (client) {
                                    var xml = '<Call Method="ZoomIn"></Call>';
                                    client.command.executeXML(xml);
                                }
                            },
                            {
                                iconId: '5_verkleinern',
                                langId: 'ID_RIBBON_ZOOM_OUT',
                                id: 'rZoomOutD',
                                method: function (client) {
                                    var xml = '<Call Method="ZoomOut"></Call>';
                                    client.command.executeXML(xml);
                                }
                            }
                        ]
                    },
                    {
                        iconId: '1_text_search',
                        langId: 'ID_TEXT_SEARCH',
                        id: 'rTextSearch',
                        paneId: 'TextSearchPane',
                        method: function (client) {
                        }
                    },
                    {
                        iconId: '3_illustration',
                        langId: 'ID_RIBBON_ILLUSTRATION',
                        id: 'rIllustration',
                        interactionClass: 'RenderMode',
                        interaction: 'Illustration',
                        method: function (client) {
                        }
                    }, {
                        iconId: '1_invertieren',
                        langId: 'ID_RIBBON_INVERT',
                        id: 'rInvertD',
                        interactionClass: 'RenderMode',
                        interaction: 'Invert',
                        method: function (client) {
                        }
                    }, {
                        iconId: '2_exakt',
                        langId: 'ID_RIBBON_COMPARE',
                        id: 'rDocCompare',
                        paneId: 'QuickComparePane',
                        method: function (client, state) {
                            var xml;
                            if (state.active) {
                                xml = '<Call Method="ActivateGraphicalCompare"><Activate>false</Activate></Call>';
                            }
                            else {
                                xml = '<Call Method="ActivateGraphicalCompare"><Activate>true</Activate></Call>';
                            }
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: '1_text',
                        langId: 'ID_RIBBON_3D_MARKUP',
                        id: 'rMarkup2D',
                        interactionClass: 'Interaction',
                        children: [
                            {
                                iconId: '1_text',
                                langId: 'ID_RIBBON_TEXT',
                                id: 'rMarkupText2D',
                                interactionClass: 'Interaction',
                                interaction: 'Markup_Text',
                                method: function (client) {
                                }
                            }, {
                                iconId: '2_kreis',
                                langId: 'ID_RIBBON_CIRCLE__N',
                                id: 'rMarkupCircle2D',
                                interactionClass: 'Interaction',
                                interaction: 'Markup_Circle',
                                method: function (client) {
                                }
                            }, {
                                iconId: 'fixed_markup',
                                langId: 'ID_RIBBON_FIXED_TEXT',
                                id: 'rMarkupFixed2D',
                                interactionClass: 'Interaction',
                                interaction: 'Markup_Text_Fixed',
                                method: function (client) {
                                }
                            }, {
                                iconId: '3_rechteck',
                                langId: 'ID_RIBBON_RECTANGLE',
                                id: 'rMarkupRect2D',
                                interactionClass: 'Interaction',
                                interaction: 'Markup_Rectangle',
                                method: function (client) {
                                }
                            }, {
                                iconId: '4_freihand',
                                langId: 'ID_RIBBON_FREEHAND',
                                id: 'rMarkupFree2D',
                                interactionClass: 'Interaction',
                                interaction: 'Markup_Freehand',
                                method: function (client) {
                                }
                            }
                        ]
                    }
                ]
            },
            {
                iconId: '',
                langId: 'ID_RIBBON_VIEW',
                id: 'rView',
                children: [
                    {
                        iconId: '3_links',
                        langId: 'ID_RIBBON_ALIGNMENT',
                        id: 'rAlignment',
                        children: [
                            {
                                iconId: '1_isometrisch',
                                langId: 'ID_RIBBON_ISOMETRIC',
                                id: 'rIsometric',
                                method: function (client) {
                                    var xml = '<Call Method="AlignView"><Alignment>Isometric</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '2_oben',
                                langId: 'ID_RIBBON_TOP',
                                id: 'rTop',
                                method: function (client) {
                                    var xml = '<Call Method="AlignView"><Alignment>Top</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '3_links',
                                langId: 'ID_RIBBON_LEFT',
                                id: 'rLeft',
                                method: function (client) {
                                    var xml = '<Call Method="AlignView"><Alignment>Left</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '4_vorne',
                                langId: 'ID_RIBBON_FRONT',
                                id: 'rFront',
                                method: function (client) {
                                    var xml = '<Call Method="AlignView"><Alignment>Front</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '5_unten',
                                langId: 'ID_RIBBON_BOTTOM',
                                id: 'rBottom',
                                method: function (client) {
                                    var xml = '<Call Method="AlignView"><Alignment>Bottom</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '6_rechts',
                                langId: 'ID_RIBBON_RIGHT',
                                id: 'rRight',
                                method: function (client) {
                                    var xml = '<Call Method="AlignView"><Alignment>Right</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '7_hinten',
                                langId: 'ID_RIBBON_BACK',
                                id: 'rBack',
                                method: function (client) {
                                    var xml = '<Call Method="AlignView"><Alignment>Back</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            }
                        ]
                    }, {
                        iconId: '3_270_Grad',
                        langId: 'ID_RIBBON_ROTATE',
                        id: 'rViewRotateS',
                        children: [
                            {
                                iconId: '1_90_Grad',
                                langId: 'ID_RIBBON_90',
                                id: 'rView90',
                                method: function (client) {
                                    var xml = '<Call Method="RotatePage"></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '2_180_Grad',
                                langId: 'ID_RIBBON_180',
                                id: 'rView180',
                                method: function (client) {
                                    var xml = '<Call Method="RotatePage"></Call>';
                                    client.command.executeXML(xml);
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '3_270_Grad',
                                langId: 'ID_RIBBON_270',
                                id: 'rView270',
                                method: function (client) {
                                    var xml = '<Call Method="RotatePage"></Call>';
                                    client.command.executeXML(xml);
                                    client.command.executeXML(xml);
                                    client.command.executeXML(xml);
                                }
                            }
                        ]
                    },
                    {
                        iconId: '1_anpassen',
                        langId: 'ID_RIBBON_ZOOM',
                        id: 'rZoomV',
                        children: [
                            {
                                iconId: '1_anpassen',
                                langId: 'ID_RIBBON_FIT_ALL',
                                id: 'rFittAllV',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="FitAll"></Call>');
                                }
                            }, {
                                iconId: '2_vergroessern',
                                langId: 'ID_RIBBON_ZOOM_IN',
                                id: 'rZoomInV',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ZoomIn"></Call>');
                                }
                            }, {
                                iconId: '3_verkleinern',
                                langId: 'ID_RIBBON_ZOOM_OUT',
                                id: 'rZoomOutV',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ZoomOut"></Call>');
                                }
                            }, {
                                iconId: '4_focus',
                                langId: 'ID_RIBBON_FOCUS',
                                id: 'rFocusV',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Focus</SelectionModifier></Call>');
                                }
                            }, {
                                iconId: 'focus_and_transition',
                                langId: 'ID_RIBBON_FOCUS_TRANSITION',
                                id: 'rFocusTransition',
                                method: function (client) {
                                    client.command.executeXML("<Call Method=\"Focus\">\n                                <Ghost>true</Ghost>\n                                <FadeTime>\n                                  <ZoomOut>500</ZoomOut>\n                                  <Translation>500</Translation>\n                                  <Rotation>1000</Rotation>\n                                  <ZoomIn>1000</ZoomIn>\n                                </FadeTime></Call>");
                                }
                            }, {
                                iconId: 'set_origin',
                                langId: 'ID_RIBBON_SET_ORIGIN',
                                id: 'rValidateCoord',
                                method: function (client) {
                                    client.command.executeXML('<Call Method="SetOrigin"></Call>');
                                }
                            }
                        ]
                    },
                    {
                        iconId: '1_koerper',
                        langId: 'ID_RIBBON_RENDERING_MODE',
                        id: 'rRenderingMode',
                        children: [
                            {
                                iconId: '1_koerper',
                                langId: 'ID_RIBBON_SOLID',
                                id: 'rSolid',
                                interactionClass: 'RenderMode',
                                interaction: 'Solid',
                                method: function (client) {
                                }
                            }, {
                                iconId: '2_drahtmodell',
                                langId: 'ID_RIBBON_WIRES',
                                id: 'rWires',
                                interactionClass: 'RenderMode',
                                interaction: 'Wires',
                                method: function (client) {
                                }
                            }, {
                                iconId: '3_illustration',
                                langId: 'ID_RIBBON_ILLUSTRATION',
                                id: 'rIllustrationRM',
                                interactionClass: 'RenderMode',
                                interaction: 'Illustration',
                                method: function (client) {
                                }
                            }, {
                                iconId: '4_perspektive',
                                langId: 'ID_RIBBON_PERSPECTIVE',
                                id: 'rPrespectiveRM',
                                interactionClass: 'RenderMode',
                                interaction: 'Perspective',
                                method: function (client) {
                                }
                            }
                        ]
                    },
                    {
                        iconId: '3_schatten',
                        langId: 'ID_RIBBON_GRAPHIC_EFFECTS',
                        id: 'rGraphicEffects',
                        children: [
                            {
                                iconId: '1_kontur',
                                langId: 'ID_RIBBON_SILHOUETTE',
                                id: 'rSilhouette',
                                interactionClass: 'RenderMode',
                                interaction: 'Silhouette',
                                method: function (client) {
                                }
                            }, {
                                iconId: '5_undurchsichtig',
                                langId: 'ID_RIBBON_OPAQUE',
                                id: 'rOpaque',
                                interactionClass: 'RenderMode',
                                interaction: 'Opaque',
                                method: function (client) {
                                }
                            }, {
                                iconId: '3_schatten',
                                langId: 'ID_RIBBON_SHADOW',
                                id: 'rShadow',
                                interactionClass: 'RenderMode',
                                interaction: 'Shadow',
                                method: function (client) {
                                }
                            }, {
                                iconId: '4_material',
                                langId: 'ID_RIBBON_MATERIAL',
                                id: 'rMaterial',
                                interactionClass: 'RenderMode',
                                interaction: 'Material',
                                method: function (client) {
                                }
                            }
                        ]
                    },
                    {
                        iconId: '1_fly_through',
                        langId: 'ID_RIBBON_FLYTHROUGH',
                        id: 'rFlythrough',
                        interactionClass: 'Interaction',
                        interaction: 'Flythrough',
                        method: function (client) {
                        }
                    }, {
                        iconId: '1_raster_darstellen',
                        langId: 'ID_RIBBON_SHOW_GRID',
                        id: 'rGrid',
                        interactionClass: 'Grid',
                        method: function (client, state) {
                            state.active = !state.active;
                            var xml = '<Settings><Scene><Visualization><ShowGrid>' + state.active + '</ShowGrid></Visualization></Scene></Settings>';
                            client.command.executeXML('<Call Method="SetSceneSettings">' + xml + '</Call>');
                        }
                    }, {
                        iconId: 'animation_start',
                        langId: 'ID_RIBBON_ANIMATE',
                        id: 'rAnimationCat',
                        children: [
                            {
                                iconId: '1_drehen',
                                langId: 'ID_RIBBON_SPIN',
                                id: 'rRotation',
                                method: function (client, state) {
                                    var xml = '<Call Method="ActivateRotation"><Activate>' + !state.active + '</Activate></Call>';
                                    state.active = !state.active;
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: 'animation_start',
                                langId: 'ID_RIBBON_ANIMATE',
                                id: 'rAnimationStart',
                                method: function (client, state) {
                                    var xml = '<Call Method="SetSceneSettings"><Settings><Scene><General><Animation>' + !state.active;
                                    xml += '</Animation></General></Scene></Settings></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: 'animation_settings',
                                langId: 'ID_CREATE_ANIMATION',
                                id: 'rAnimationSettings',
                                paneId: 'AnimationPane',
                                method: function () {
                                }
                            }
                        ]
                    }
                ]
            },
            {
                iconId: '',
                langId: 'ID_RIBBON_MEASUREMENT',
                id: 'rMeasurement',
                children: [
                    {
                        iconId: '1_measurement',
                        langId: 'ID_RIBBON_DIMENSION_MODE',
                        id: 'rMeasurementModeCat',
                        children: [
                            {
                                iconId: '1_measurement',
                                langId: 'ID_RIBBON_MEASUREMENT__N',
                                id: 'rMeasurementMode',
                                method: function (client) {
                                    var xml = "<Call Method=\"SetSceneSettings\">\n                                    <Settings>\n                                        <Scene><Tools>\n                                        <Dimensioning>\n                                        <Dimensioning Type=\"bool\">false</Dimensioning>\n                                        </Dimensioning>\n                                        </Tools></Scene>\n                                    </Settings>\n                                </Call>";
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '2_dimensioning',
                                langId: 'ID_RIBBON_DIMENSION',
                                id: 'rDimensionMode',
                                method: function (client) {
                                    var xml = "<Call Method=\"SetSceneSettings\">\n                                    <Settings>\n                                        <Scene><Tools>\n                                        <Dimensioning>\n                                        <Dimensioning Type=\"bool\">true</Dimensioning>\n                                        </Dimensioning>\n                                        </Tools></Scene>\n                                    </Settings>\n                                </Call>";
                                    client.command.executeXML(xml);
                                }
                            }
                        ]
                    }, {
                        iconId: '1_kreisbogen',
                        langId: 'ID_RIBBON_CIRCLE_MODE',
                        id: 'rCircleMode',
                        children: [{
                                iconId: '1_kreisbogen',
                                langId: 'ID_RIBBON_ARC',
                                id: 'rArc',
                                interactionClass: 'CircleSelection',
                                method: function (client) {
                                    var xml = '<Call Method="SetSceneSettings"><Settings><Scene><Tools><CircleSelection><SelectCircleWith3Points>';
                                    xml += 'false';
                                    xml += '</SelectCircleWith3Points></CircleSelection></Tools></Scene></Settings></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '2_3_punkte',
                                langId: 'ID_RIBBON_3_POINTS',
                                id: 'rThreePoint',
                                interactionClass: 'CircleSelection',
                                method: function (client) {
                                    var xml = '<Call Method="SetSceneSettings"><Settings><Scene><Tools><CircleSelection><SelectCircleWith3Points>';
                                    xml += 'true';
                                    xml += '</SelectCircleWith3Points></CircleSelection></Tools></Scene></Settings></Call>';
                                    client.command.executeXML(xml);
                                }
                            }
                        ]
                    }, {
                        iconId: '1_punkt',
                        langId: 'ID_RIBBON_COORDINATE',
                        id: 'rCoordinate',
                        interactionClass: 'Interaction',
                        interaction: 'Measurement_PointCoordinate',
                        method: function (client) {
                        }
                    }, {
                        iconId: '1_laenge',
                        langId: 'ID_RIBBON_LENGTH',
                        id: 'rLengthGroup',
                        children: [
                            {
                                iconId: '1_laenge',
                                langId: 'ID_RIBBON_LENGTH',
                                id: 'rLength',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_EdgeLength',
                                method: function (client) {
                                }
                            }, {
                                iconId: '2_kumulierte_laenge',
                                langId: 'ID_RIBBON_ACCUMULATED_LENGTH',
                                id: 'rAccLength',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_EdgeLengthAccumulated',
                                method: function (client) {
                                }
                            }
                        ]
                    }, {
                        iconId: '1_1_radius',
                        langId: 'ID_RIBBON_CIRCLE',
                        id: 'rMeasurementCircle',
                        interactionClass: 'Interaction',
                        children: [
                            {
                                iconId: '1_1_radius',
                                langId: 'ID_RIBBON_RADIUS',
                                id: 'rRadius',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_CircleRadius',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_2_durchmesser',
                                langId: 'ID_RIBBON_DIAMETER',
                                id: 'rDiameter',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_CircleDiameter',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_3_umfang',
                                langId: 'ID_RIBBON_PERIMETER',
                                id: 'rPerimeter',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_CirclePerimeter',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_4_mittelpunkt',
                                langId: 'ID_RIBBON_CENTER',
                                id: 'rMeasureCircleCenter',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_CircleCenter',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_5_flaeche',
                                langId: 'ID_RIBBON_AREA',
                                id: 'rMeasureCircleArea',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_CircleArea',
                                method: function (client) {
                                }
                            }
                        ]
                    }, {
                        iconId: '1_1_punkt_zu_punkt',
                        langId: 'ID_RIBBON_DISTANCE',
                        id: 'rMeasureDist',
                        interactionClass: 'Interaction',
                        children: [
                            {
                                iconId: '1_1_punkt_zu_punkt',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_TWO_POINTS',
                                id: 'rMeasurePtoP',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistancePointToPoint',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_2_punkt_zu_kante',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_POINT_AND_EDGE',
                                id: 'rMeasurePtoE',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistancePointToEdge',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_3_punkt_zu_kreisachse',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_POINT_AND_CIRCLE_AXIS',
                                id: 'rMeasurePtoA',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistancePointToAxis',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_4_punkt_zu_koerper',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_POINT_AND_SOLID',
                                id: 'rMeasurePtoS',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistancePointToSolid',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_5_kante_zu_kante',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_TWO_EDGES',
                                id: 'rMeasureEtoE',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistanceEdgeToEdge',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_6_kante_zu_oberflaeche',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_EDGE_AND_FACE',
                                id: 'rMeasureEtoF',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistanceEdgeToFace',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_7_kante_zu_koerper',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_EDGE_AND_SOLID',
                                id: 'rMeasureEtoS',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistanceEdgeToSolid',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_8_kreisachse_zu_kreisachse',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_TWO_CIRCLE_AXES',
                                id: 'rMeasureAtoA',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistanceAxisToAxis',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_8_kreisachse_zu_kreisachse',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_TWO_CIRCLE_CENTERS',
                                id: 'rMeasureCCtoCC',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistanceCenterToCenter',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_9_kreismittelpunkt_zu_kante',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_CIRCLE_AXIS_AND_EDGE',
                                id: 'rMeasureAtoE',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistanceAxisToEdge',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_10_oberflaeche_zu_oberflaeche',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_TWO_FACES',
                                id: 'rMeasureFtoF',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistanceFaceToFace',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_11_oberflaeche_zu_mittelpunkt',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_FACE_AND_CENTER',
                                id: 'rMeasureFtoC',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistanceFaceToCenter',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_12_oberflaeche_zu_koerper',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_FACE_AND_SOLID',
                                id: 'rMeasureFtoC',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistanceFaceToSolid',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_13_koerper_zu_koerper',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_TWO_SOLIDS',
                                id: 'rMeasureStoS',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistanceSolidToSolid',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_14_cylinder_center_to_face',
                                langId: 'ID_RIBBON_DISTANCE_BETWEEN_PIPE_CENTER_AND_FACE',
                                id: 'rMeasureCCtoF',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_DistanceCylinderCenterToFace',
                                method: function (client) {
                                }
                            }
                        ]
                    }, {
                        iconId: '1_1_kreisachse_zu_kreisachse',
                        langId: 'ID_RIBBON_ANGLE',
                        id: 'rMeasurementAngle',
                        interactionClass: 'Interaction',
                        children: [
                            {
                                iconId: '1_1_kreisachse_zu_kreisachse',
                                langId: 'ID_RIBBON_ANGLE_BETWEEN_TWO_AXES',
                                id: 'rMeasureAngleAtoA',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_AngleBetweenTwoAxes',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_2_kreisachse_zu_kante',
                                langId: 'ID_RIBBON_ANGLE_BETWEEN_AXIS_AND_EDGE',
                                id: 'rMeasureAngleAtoE',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_AngleBetweenAxisEdge',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_3_linie_zu_linie',
                                langId: 'ID_RIBBON_ANGLE_BETWEEN_TWO_LINES',
                                id: 'rMeasureAngleLtoL',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_AngleBetweenTwoLines',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_3_linie_zu_linie',
                                langId: 'ID_RIBBON_ANGLE_BETWEEN_TWO_PROJECTED_LINES',
                                id: 'rMeasureAngleProjLineToLine',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_AngleBetweenTwoProjectedLines',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_4_ebene_zu_ebene',
                                langId: 'ID_RIBBON_ANGLE_BETWEEN_TWO_PLANES',
                                id: 'rMeasureAnglePtP',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_AngleBetweenTwoPlanes',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_5_ebene_zu_richtung',
                                langId: 'ID_RIBBON_ANGLE_BETWEEN_PLANE_AND_DIRECTION',
                                id: 'rMeasureAnglePtoD',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_AngleBetweenPlaneDirection',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_6_winkel_aus_drei_punkten',
                                langId: 'ID_RIBBON_ANGLE_DEFINED_BY_THREE_POINTS',
                                id: 'rMeasureAngleThreePoint',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_AngleFromThreePoints',
                                method: function (client) {
                                }
                            }
                        ]
                    }, {
                        iconId: 'measure_face',
                        langId: 'ID_RIBBON_FACE',
                        id: 'rMeasureFaceArea',
                        interactionClass: 'Interaction',
                        interaction: 'Measurement_FaceArea',
                        method: function (client) {
                        }
                    },
                    {
                        iconId: '1_wandstaerke',
                        langId: 'ID_RIBBON_WALL_THICKNESS',
                        id: 'rMeasureWallthickness',
                        children: [
                            {
                                iconId: 'ray_based',
                                langId: 'ID_WALLTHICKNESS_COMPUTATION_MODE_RAY',
                                id: 'rMeasureWallthicknessRay',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_WallThickness_RayBased',
                                method: function (client) {
                                }
                            }, {
                                iconId: 'rolling_ball',
                                langId: 'ID_WALLTHICKNESS_COMPUTATION_MODE_ROLLING_BALL',
                                id: 'rMeasureWallthicknessRollingBall',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_WallThickness_RollingBall',
                                method: function (client) {
                                }
                            }
                        ]
                    }, {
                        iconId: '1_1_welt',
                        langId: 'ID_RIBBON_BOUNDING_BOX',
                        id: 'rMeasureBB',
                        children: [
                            {
                                iconId: '1_1_welt',
                                langId: 'ID_RIBBON_WORLD',
                                id: 'rMeasureBBWorld',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_BoundingBoxWorld',
                                method: function (client) {
                                    var xml = '<Call Method="ModifySelection"><SelectionModifier>ComputeWorldBoundingBox</SelectionModifier></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: '1_2_bauteil',
                                langId: 'ID_RIBBON_INDIVIDUAL',
                                id: 'rMeasureBBPart',
                                interactionClass: 'Interaction',
                                interaction: 'Measurement_BoundingBoxPart',
                                method: function (client) {
                                    var xml = '<Call Method="ModifySelection"><SelectionModifier>ComputePartBoundingBox</SelectionModifier></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: 'bbox_min',
                                langId: 'ID_RIBBON_PRECISE',
                                id: 'rMeasureMin',
                                method: function (client) {
                                    var xml = '<Call Method="ModifySelection"><SelectionModifier>ComputeMinimalBoundingBoxPrecise</SelectionModifier></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: 'bbox_min_quick',
                                langId: 'ID_RIBBON_QUICK',
                                id: 'rMeasureMinQuick',
                                method: function (client) {
                                    var xml = '<Call Method="ModifySelection"><SelectionModifier>ComputeMinimalBoundingBoxFast</SelectionModifier></Call>';
                                    client.command.executeXML(xml);
                                }
                            }
                        ]
                    }, {
                        iconId: '2_bohrung',
                        langId: 'ID_RIBBON_DRILLHOLE',
                        id: 'rMeasureDrillhole',
                        interactionClass: 'Interaction',
                        interaction: 'Measurement_CircleForDrillhole',
                        method: function (client) {
                        }
                    }
                ]
            }, {
                iconId: '',
                langId: 'ID_RIBBON_ANALYZE',
                id: 'rAnalyze',
                children: [
                    {
                        iconId: 'section_category',
                        langId: 'ID_RIBBON_SECTION',
                        id: 'rCut',
                        children: [
                            {
                                iconId: '1_1_schnittebene_aus_punkt',
                                langId: 'ID_RIBBON_CUTTING_PLANE_FROM_POINT_NORMAL',
                                id: 'rCutPoint',
                                interactionClass: 'Interaction',
                                interaction: 'Section_FromPointNormal',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_2_schnittebene',
                                langId: 'ID_RIBBON_CUTTING_PLANE',
                                id: 'rCutPlane',
                                interactionClass: 'Interaction',
                                interaction: 'Section',
                                method: function (client) {
                                }
                            }, {
                                iconId: 'section_three_points',
                                langId: 'ID_RIBBON_CUTTING_PLANE_FROM_3_POINTS',
                                id: 'rCutThreePoints',
                                interactionClass: 'Interaction',
                                interaction: 'Section_From3Points',
                                method: function (client) {
                                }
                            }, {
                                iconId: 'section_three_circle_centers',
                                langId: 'ID_RIBBON_CUTTING_PLANE_FROM_3_CIRCLE_CENTERS',
                                id: 'rCutThreeCircleCenter',
                                interactionClass: 'Interaction',
                                interaction: 'Section_From3CircleCenters',
                                method: function (client) {
                                }
                            }
                        ]
                    }, {
                        iconId: '1_schnell',
                        langId: 'ID_RIBBON_COMPARE_MODEL',
                        id: 'rQuickCompare',
                        paneId: 'QuickComparePane',
                        method: function (client, state) {
                            var xml;
                            if (state.active) {
                                xml = '<Call Method="ActivateGraphicalCompare"><Activate>false</Activate></Call>';
                            }
                            else {
                                xml = '<Call Method="ActivateGraphicalCompare"><Activate>true</Activate></Call>';
                            }
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: '2_exakt',
                        langId: 'ID_RIBBON_BREP_PART',
                        id: 'rExactCompare',
                        paneId: 'ExactComparePane',
                        method: function (client) {
                            var xml = '<Call Method="ComputeBrepCompare"></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: '1_entformungsschraegen_analyse',
                        langId: 'ID_RIBBON_DRAFT_ANGLE',
                        id: 'rDraftAngle',
                        interactionClass: 'Interaction',
                        interaction: 'DraftAngle',
                        method: function (client) {
                        }
                    }, {
                        iconId: '1_projizierte_flaeche',
                        langId: 'ID_RIBBON_PROJECTED_AREA',
                        id: 'rProjectedArea',
                        paneId: 'ProjectedAreaPane',
                        method: function (client) {
                        }
                    }, {
                        iconId: 'clash',
                        langId: 'ID_RIBBON_CLASH',
                        id: 'rClash',
                        paneId: 'ClashPane',
                        method: function (client) {
                        }
                    },
                    {
                        iconId: 'clearance',
                        langId: 'ID_BAND_ANALYSIS_WND',
                        id: 'rBandAnalysis',
                        paneId: 'BandAnalysisPane',
                        method: function (client) {
                        }
                    }, {
                        iconId: '1_wandstaerke',
                        langId: 'ID_RIBBON_WALL_THICKNESS',
                        id: 'rHeatMap',
                        paneId: 'WallThicknessPane',
                        method: function (client) {
                        }
                    }, {
                        iconId: '1_physikalische_eigenschaften',
                        langId: 'ID_PHYSICAL_PROPERTIES',
                        id: 'rPhysProps',
                        method: function (client) {
                            var xml = '<Call Method="ModifySelection"><SelectionModifier>ComputePhysicalProperties</SelectionModifier></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: '3_neutrale_faser',
                        langId: 'ID_RIBBON_NEUTRAL_AXIS',
                        id: 'rNeutralAxis',
                        paneId: 'NeutralAxisPane',
                        method: function (client) {
                        }
                    }, {
                        iconId: '2_bohrung',
                        langId: 'ID_DRILLHOLE',
                        id: 'rDrillhole',
                        paneId: 'DrillholePane',
                        method: function (client) {
                        }
                    }, {
                        iconId: 'split',
                        langId: 'ID_RIBBON_SPLIT',
                        id: 'rSplit',
                        paneId: 'SplitPane',
                        method: function (client, state) {
                            var xml;
                            if (!state.active) {
                                xml = '<Call Method="ActivateInteraction"><Interaction>Split_SelectDirection</Interaction></Call>';
                            }
                            else {
                                xml = '<Call Method="ActivateInteraction"><Interaction>Deactivate_Split</Interaction></Call>';
                            }
                            client.command.executeXML(xml);
                        }
                    }
                ]
            }, {
                iconId: '',
                langId: 'ID_RIBBON_TRANSFORMATION',
                id: 'rTransformation',
                children: [
                    {
                        iconId: '1_freies_verschieben',
                        langId: 'ID_RIBBON_FREE_DRAG',
                        id: 'rFreeDrag',
                        interactionClass: 'Interaction',
                        interaction: 'Transformation_FreeDrag',
                        method: function (client) {
                        }
                    }, {
                        iconId: '2_verschieben',
                        langId: 'ID_RIBBON_MOVE',
                        id: 'rTransMove',
                        interactionClass: 'Interaction',
                        interaction: 'Transformation_Move',
                        method: function (client) {
                        }
                    }, {
                        iconId: '3_drehen',
                        langId: 'ID_RIBBON_ROTATE',
                        id: 'rTransRotate',
                        interactionClass: 'Interaction',
                        interaction: 'Transformation_Rotate',
                        method: function (client) {
                        }
                    }, {
                        iconId: 'scale',
                        langId: 'ID_RIBBON_SCALE',
                        id: 'rTransScale',
                        interactionClass: 'Interaction',
                        interaction: 'Transformation_Scale',
                        method: function (client) {
                        }
                    }, {
                        iconId: '6_position_zuruecksetzen',
                        langId: 'ID_RIBBON_RESET_TRANSFORMATION',
                        id: 'rTransReset',
                        method: function (client) {
                            var xml = '<Call Method="ModifySelection"><SelectionModifier>ResetTransformation</SelectionModifier></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: '4_spiegeln',
                        langId: 'ID_RIBBON_MIRROR',
                        id: 'rMirror',
                        paneId: 'MirrorPane',
                        method: function (client) {
                        }
                    }, {
                        iconId: '5_explodieren',
                        langId: 'ID_RIBBON_EXPLODE',
                        id: 'rExplode',
                        paneId: 'ExplodePane',
                        method: function (client) {
                        }
                    }, {
                        iconId: 'activate_axis',
                        langId: 'ID_RIBBON_BOUNDARY_CONDITION',
                        id: 'rBoundCond',
                        children: [
                            {
                                iconId: 'activate_axis',
                                langId: 'ID_RIBBON_ACTIVATE',
                                id: 'rBoundCondAxis',
                                interactionClass: 'Interaction',
                                interaction: 'BoundaryCondition_Axis',
                                method: function (client) {
                                }
                            }, {
                                iconId: 'condition_axis_by_circle',
                                langId: 'ID_RIBBON_BOUNDARY_CONDITION_BY_CIRCLE',
                                id: 'rBoundCondCircle',
                                interactionClass: 'Interaction',
                                interaction: 'BoundaryCondition_AxisByCircle',
                                method: function (client) {
                                }
                            }, {
                                iconId: 'condition_axis_by_line',
                                langId: 'ID_RIBBON_BOUNDARY_CONDITION_BY_LINE',
                                id: 'rBoundCondLine',
                                interactionClass: 'Interaction',
                                interaction: 'BoundaryCondition_AxisByLine',
                                method: function (client) {
                                }
                            }
                        ]
                    }, {
                        iconId: '3_pan',
                        langId: 'ID_RIBBON_ALIGNING_MOVE',
                        id: 'rAlignMove',
                        children: [
                            {
                                iconId: 'align_move_point_point',
                                langId: 'ID_RIBBON_POINT_TO_POINT',
                                id: 'rAlignMovePoint',
                                interactionClass: 'Interaction',
                                interaction: 'Transformation_Align_Move_PointToPoint',
                                method: function (client) {
                                }
                            }, {
                                iconId: 'align_move_plane_plane',
                                langId: 'ID_RIBBON_PLANE_TO_PLANE',
                                id: 'rAlignMovePlane',
                                interactionClass: 'Interaction',
                                interaction: 'Transformation_Align_Move_PlaneToPlane',
                                method: function (client) {
                                }
                            }, {
                                iconId: 'align_move_center_center',
                                langId: 'ID_RIBBON_CIRCLE_CENTER_TO_CIRCLE_CENTER',
                                id: 'rAlignMoveCenter',
                                interactionClass: 'Interaction',
                                interaction: 'Transformation_Align_Move_CircleCenterToCircleCenter',
                                method: function (client) {
                                }
                            }
                        ]
                    }, {
                        iconId: '3_270_Grad',
                        langId: 'ID_RIBBON_ALIGNING_ROTATE',
                        id: 'rAlignRotate',
                        children: [
                            {
                                iconId: '1_normal_zu_normal',
                                langId: 'ID_RIBBON_NORMAL_TO_NORMAL',
                                id: 'rAlignRotateNormal',
                                interactionClass: 'Interaction',
                                interaction: 'Transformation_Align_Rotate_NormalToNormal',
                                method: function (client) {
                                }
                            }, {
                                iconId: '2_punkt_zu_punkt',
                                langId: 'ID_RIBBON_POINT_TO_POINT',
                                id: 'rAlignRotatePoint',
                                interactionClass: 'Interaction',
                                interaction: 'Transformation_Align_Rotate_PointToPoint',
                                method: function (client) {
                                }
                            }, {
                                iconId: '3_kreisachse_zu_kreisachse',
                                langId: 'ID_RIBBON_CIRCLE_CENTER_TO_CIRCLE_CENTER',
                                id: 'rAlignRotateCirecleCenter',
                                interactionClass: 'Interaction',
                                interaction: 'Transformation_Align_Rotate_CircleCenterToCircleCenter',
                                method: function (client) {
                                }
                            }
                        ]
                    }, {
                        iconId: '4_move_and_rotate',
                        langId: 'ID_RIBBON_ALIGNING_MOVE_ROTATE',
                        id: 'rAlignmoveRotate',
                        children: [
                            {
                                iconId: '1_koordinatensystem_zu_koordinatensystem',
                                langId: 'ID_RIBBON_COORDINATE_SYSTEM_TO_COORDINATE_SYSTEM',
                                id: 'rAlignmoveRotateCoord',
                                interactionClass: 'Interaction',
                                interaction: 'Transformation_Align_CoordinateSystemToCoordinateSystem',
                                method: function (client) {
                                }
                            }, {
                                iconId: '2_kreisachse_zu_kreisachse',
                                langId: 'ID_RIBBON_CIRCLE_AXIS_TO_CIRCLE_AXIS',
                                id: 'rAlignmoveRotateCircleAxis',
                                interactionClass: 'Interaction',
                                interaction: 'Transformation_Align_CircleAxisToCircleAxis',
                                method: function (client) {
                                }
                            }, {
                                iconId: '3_oberflaeche_zu_oberflaeche',
                                langId: 'ID_RIBBON_PLANE_TO_PLANE',
                                id: 'rAlignmoveRotatePlane',
                                interactionClass: 'Interaction',
                                interaction: 'Transformation_Align_PlaneToPlane',
                                method: function (client) {
                                }
                            }
                        ]
                    }
                ]
            }, {
                iconId: '',
                langId: 'ID_RIBBON_TOOLS',
                id: 'rTools',
                children: [
                    {
                        iconId: '1_filter',
                        langId: 'ID_RIBBON_SEARCH',
                        id: 'rFilter',
                        paneId: 'FilterPane',
                        method: function (client) {
                        }
                    }, {
                        iconId: 'number_of_triangles',
                        langId: 'ID_RIBBON_GEOMETRY_TOOLS',
                        id: 'rGeomTools',
                        paneId: 'GeometryToolsPane',
                        method: function (client) {
                        }
                    }, {
                        iconId: 'distortion',
                        langId: 'ID_RIBBON_DISTORT',
                        id: 'rDistortion',
                        paneId: 'DistortionPane',
                        method: function (client) {
                        }
                    }, {
                        iconId: 'visibility_test',
                        langId: 'ID_VISIBILITY_TEST',
                        id: 'rVisTest',
                        paneId: 'VisibilityTestPane',
                        method: function (client) {
                        }
                    }
                ]
            }, {
                iconId: '',
                langId: 'ID_RIBBON_MODEL',
                id: 'rModel',
                children: [
                    {
                        iconId: '1_retessellieren',
                        langId: 'ID_RIBBON_RETESSELATE',
                        id: 'rRetesselate',
                        children: [
                            {
                                iconId: 'extra_low_tesselation',
                                langId: 'ID_RIBBON_EXTRALOW',
                                id: 'rRetessExLow',
                                method: function (client) {
                                    var xml = '<Call Method="Retesselate"><Level>ExtraLow</Level></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: 'low_tesselation',
                                langId: 'ID_RIBBON_LOW',
                                id: 'rRetessLow',
                                method: function (client) {
                                    var xml = '<Call Method="Retesselate"><Level>Low</Level></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: 'medium_tesselation',
                                langId: 'ID_RIBBON_MEDIUM',
                                id: 'rRetessMedium',
                                method: function (client) {
                                    var xml = '<Call Method="Retesselate"><Level>Medium</Level></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: 'high_tesselation',
                                langId: 'ID_RIBBON_HIGH',
                                id: 'rRetessHigh',
                                method: function (client) {
                                    var xml = '<Call Method="Retesselate"><Level>High</Level></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: 'extra_high_tesselation',
                                langId: 'ID_RIBBON_EXTRAHIGH',
                                id: 'rRetessExHigh',
                                method: function (client) {
                                    var xml = '<Call Method="Retesselate"><Level>ExtraHigh</Level></Call>';
                                    client.command.executeXML(xml);
                                }
                            }, {
                                iconId: 'custom_tesselation',
                                langId: 'ID_RIBBON_USER_DEFINED',
                                id: 'rRetessUserDef',
                                method: function (client) {
                                    var xml = '<Call Method="Retesselate"><Level>Userdefined</Level></Call>';
                                    client.command.executeXML(xml);
                                }
                            }
                        ]
                    },
                    {
                        iconId: 'coordinate_system',
                        langId: 'ID_RIBBON_COORDINATE_SYSTEM',
                        id: 'rCoordSys',
                        children: [
                            {
                                iconId: '1_kreismittelpunkt',
                                langId: 'ID_RIBBON_BY_CIRCLE_CENTER',
                                id: 'rCoordSysCC',
                                interactionClass: 'Interaction',
                                interaction: 'EntityCreation_CoordinateSystemByCircleCenter',
                                method: function (client) {
                                }
                            }, {
                                iconId: '1_kreisachse',
                                langId: 'ID_RIBBON_BY_CYLINDER_AXIS',
                                id: 'rCoordSysCylAx',
                                interactionClass: 'Interaction',
                                interaction: 'EntityCreation_CoordinateSystemByCylinderAxis',
                                method: function (client) {
                                }
                            }, {
                                iconId: '2_3_ebenen_im_rechten_winkel',
                                langId: 'ID_RIBBON_BY_3_PLANES',
                                id: 'rCoordSysThreePlanes',
                                interactionClass: 'Interaction',
                                interaction: 'EntityCreation_CoordinateSystemBy3Planes',
                                method: function (client) {
                                }
                            }, {
                                iconId: '3_3_punkte_im_rechten_winkel',
                                langId: 'ID_RIBBON_BY_3_POINTS',
                                id: 'rCoordSysThreepoints',
                                interactionClass: 'Interaction',
                                interaction: 'EntityCreation_CoordinateSystemBy3Points',
                                method: function (client) {
                                }
                            }, {
                                iconId: '4_senkrechte_linien',
                                langId: 'ID_RIBBON_BY_EDGES',
                                id: 'rCoordSysEdges',
                                interactionClass: 'Interaction',
                                interaction: 'EntityCreation_CoordinateSystemByLines',
                                method: function (client) {
                                }
                            }, {
                                iconId: '5_normale',
                                langId: 'ID_RIBBON_BY_NORMAL',
                                id: 'rCoordSysNormal',
                                interactionClass: 'Interaction',
                                interaction: 'EntityCreation_CoordinateSystemByNormal',
                                method: function (client) {
                                }
                            }, {
                                iconId: '6_linie_und_punkt',
                                langId: 'ID_RIBBON_BY_EDGE_AND_POINT',
                                id: 'rCoordSysEandP',
                                interactionClass: 'Interaction',
                                interaction: 'EntityCreation_CoordinateSystemByLineAndPoint',
                                method: function (client) {
                                }
                            }, {
                                iconId: '7_begrenzungsrahmen',
                                langId: 'ID_RIBBON_BY_BOUNDING_BOX',
                                id: 'rCoordSysBB',
                                interactionClass: 'Interaction',
                                interaction: 'EntityCreation_CoordinateSystemByBoundingBox',
                                method: function (client) {
                                }
                            }, {
                                iconId: 'coordinate_system_world',
                                langId: 'ID_RIBBON_BY_CURRENT_GLOBAL',
                                id: 'rCoordSysCurrGlobal',
                                interactionClass: 'Interaction',
                                interaction: 'EntityCreation_CoordinateSystemGlobal',
                                method: function (client) {
                                }
                            }, {
                                iconId: 'coordinate_system_part',
                                langId: 'ID_RIBBON_BY_SELECTED_PART',
                                id: 'rCoordSysSelectedPart',
                                interactionClass: 'Interaction',
                                interaction: 'EntityCreation_CoordinateSystemPart',
                                method: function (client) {
                                }
                            }, {
                                iconId: '8_aktivieren',
                                langId: 'ID_RIBBON_ACTIVATE_COORDINATESYSTEM',
                                id: 'rCoordSysActivate',
                                method: function (client) {
                                    var xml = '<Call Method="ActivateSelectedCoordinateSystem"></Call>';
                                    client.commant.executeXML(xml);
                                }
                            }, {
                                iconId: '9_zuruecksetzen',
                                langId: 'ID_RESET_COORDINATESYSTEM',
                                id: 'rCoordSysReset',
                                method: function (client) {
                                    var xml = '<Call Method="ResetCoordinateSystem"></Call>';
                                    client.commant.executeXML(xml);
                                }
                            }
                        ]
                    }, {
                        iconId: '1_kreismittelpunkt',
                        langId: 'ID_RIBBON_CIRCLE_CENTER',
                        id: 'rCreatePointCC',
                        interactionClass: 'Interaction',
                        interaction: 'EntityCreation_PointByCircleCenter',
                        method: function (client) {
                        }
                    }, {
                        iconId: '2_punkt',
                        langId: 'ID_RIBBON_POINT',
                        id: 'rCreatePointPos',
                        interactionClass: 'Interaction',
                        interaction: 'EntityCreation_PointByPickPosition',
                        method: function (client) {
                        }
                    }, {
                        iconId: '1_kreisachse',
                        langId: 'ID_RIBBON_CIRCLE_AXIS',
                        id: 'rCreateWireCC',
                        interactionClass: 'Interaction',
                        interaction: 'EntityCreation_WireByCircleCenter',
                        method: function (client) {
                        }
                    }, {
                        iconId: '2_punkt_zu_punkt',
                        langId: 'ID_RIBBON_POINT_TO_POINT',
                        id: 'rCreateWirePtoP',
                        interactionClass: 'Interaction',
                        interaction: 'EntityCreation_WirePointToPoint',
                        method: function (client) {
                        }
                    }, {
                        iconId: '1_flaeche_extrahieren',
                        langId: 'ID_RIBBON_EXTRACT_FACES',
                        id: 'rExtractFaces',
                        method: function (client) {
                            var xml = '<Call Method="ModifySelection"><SelectionModifier>ExtractFaces</SelectionModifier></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: 'merge_objects',
                        langId: 'ID_RIBBON_MERGE_OBJECTS',
                        id: 'rMergeObjects',
                        method: function (client) {
                            var xml = '<Call Method="ModifySelection"><SelectionModifier>MergeObjects</SelectionModifier></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: 'merge_objects_rec',
                        langId: 'ID_RIBBON_MERGE_OBJECTS_HIERARCHICAL',
                        id: 'rMergeObjectsRec',
                        method: function (client) {
                            var xml = '<Call Method="ModifySelection"><SelectionModifier>MergeObjectsHierarchically</SelectionModifier></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: '2_huellgeometrie',
                        langId: 'ID_RIBBON_CONVEX_HULL',
                        id: 'rConvexHull',
                        method: function (client) {
                            var xml = '<Call Method="ModifySelection"><SelectionModifier>ComputeHullGeometry</SelectionModifier></Call>';
                            client.command.executeXML(xml);
                        }
                    }
                ]
            },
            {
                iconId: '',
                langId: 'ID_RIBBON_TECH_DOC',
                id: 'rTechDoc',
                children: [
                    {
                        iconId: '1_text',
                        langId: 'ID_RIBBON_3D_MARKUP',
                        id: 'rMarkup',
                        interactionClass: 'Interaction',
                        children: [
                            {
                                iconId: '1_text',
                                langId: 'ID_RIBBON_TEXT',
                                id: 'rMarkupText',
                                interactionClass: 'Interaction',
                                interaction: 'Markup_Text',
                                method: function (client) {
                                }
                            }, {
                                iconId: '2_kreis',
                                langId: 'ID_RIBBON_CIRCLE__N',
                                id: 'rMarkupCircle',
                                interactionClass: 'Interaction',
                                interaction: 'Markup_Circle',
                                method: function (client) {
                                }
                            }, {
                                iconId: 'fixed_markup',
                                langId: 'ID_RIBBON_FIXED_TEXT',
                                id: 'rMarkupFixed',
                                interactionClass: 'Interaction',
                                interaction: 'Markup_Text_Fixed',
                                method: function (client) {
                                }
                            }, {
                                iconId: '3_rechteck',
                                langId: 'ID_RIBBON_RECTANGLE',
                                id: 'rMarkupRect',
                                interactionClass: 'Interaction',
                                interaction: 'Markup_Rectangle',
                                method: function (client) {
                                }
                            }, {
                                iconId: '4_freihand',
                                langId: 'ID_RIBBON_FREEHAND',
                                id: 'rMarkupFree',
                                interactionClass: 'Interaction',
                                interaction: 'Markup_Freehand',
                                method: function (client) {
                                }
                            }
                        ]
                    }, {
                        iconId: '1_BOM',
                        langId: 'ID_RIBBON_BOM',
                        id: 'rBomPane',
                        paneId: 'BOMPane',
                        method: function (client) {
                        }
                    }, {
                        iconId: 'callout_mode_square',
                        langId: 'ID_RIBBON_CALLOUTS_ALIGNMENT_PANEL',
                        id: 'rCalloutsAlignment',
                        children: [
                            {
                                iconId: 'callout_mode_square',
                                langId: 'ID_RIBBON_CALLOUTS_ALIGN_RECTANGLE',
                                id: 'rCalloutAlignRect',
                                method: function (client) {
                                    var xml = '<Call Method="SetCalloutAlignment"><Alignment>Rectangle</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            },
                            {
                                iconId: 'callout_mode_circle',
                                langId: 'ID_RIBBON_CALLOUTS_ALIGN_CIRCLE',
                                id: 'rCalloutAlignCircle',
                                method: function (client) {
                                    var xml = '<Call Method="SetCalloutAlignment"><Alignment>Circle</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            },
                            {
                                iconId: 'callout_mode_top_bottom',
                                langId: 'ID_RIBBON_CALLOUTS_ALIGN_TOP_AND_BOTTOM',
                                id: 'rCalloutAlignTopBottom',
                                method: function (client) {
                                    var xml = '<Call Method="SetCalloutAlignment"><Alignment>TopBottom</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            },
                            {
                                iconId: 'callout_mode_left_right',
                                langId: 'ID_RIBBON_CALLOUTS_ALIGN_LEFT_AND_RIGHT',
                                id: 'rCalloutLeftRight',
                                method: function (client) {
                                    var xml = '<Call Method="SetCalloutAlignment"><Alignment>LeftRight</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            },
                            {
                                iconId: 'callout_mode_align_bottom',
                                langId: 'ID_RIBBON_CALLOUTS_ALIGN_BOTTOM',
                                id: 'rCalloutBottom',
                                method: function (client) {
                                    var xml = '<Call Method="SetCalloutAlignment"><Alignment>Bottom</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            },
                            {
                                iconId: 'callout_mode_align_top',
                                langId: 'ID_RIBBON_CALLOUTS_ALIGN_TOP',
                                id: 'rCalloutTop',
                                method: function (client) {
                                    var xml = '<Call Method="SetCalloutAlignment"><Alignment>Top</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            },
                            {
                                iconId: 'callout_mode_align_left',
                                langId: 'ID_RIBBON_CALLOUTS_ALIGN_LEFT',
                                id: 'rCalloutLeft',
                                method: function (client) {
                                    var xml = '<Call Method="SetCalloutAlignment"><Alignment>Left</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            },
                            {
                                iconId: 'callout_mode_align_right',
                                langId: 'ID_RIBBON_CALLOUTS_ALIGN_RIGHT',
                                id: 'rCalloutRight',
                                method: function (client) {
                                    var xml = '<Call Method="SetCalloutAlignment"><Alignment>Right</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            },
                            {
                                iconId: 'callout_mode_near_to_solid',
                                langId: 'ID_RIBBON_CALLOUTS_ALIGN_NEAR_TO_SOLID',
                                id: 'rCalloutNearToSolid',
                                method: function (client) {
                                    var xml = '<Call Method="SetCalloutAlignment"><Alignment>NearToSolid</Alignment></Call>';
                                    client.command.executeXML(xml);
                                }
                            }
                        ]
                    },
                    {
                        iconId: 'callout_auto_alignment',
                        langId: 'ID_RIBBON_CALLOUTS_AUTO_ALIGNMENT',
                        id: 'rCalloutAutoAlign',
                        method: function (client, state) {
                            var xml;
                            if (!state.active) {
                                xml = '<Call Method="EnableAutoCalloutAlignment"></Call>';
                                client.command.executeXML(xml);
                                xml = '<Call Method="AlignCallouts"></Call>';
                                client.command.executeXML(xml);
                            }
                            else {
                                xml = '<Call Method="DisableAutoCalloutAlignment"></Call>';
                                client.command.executeXML(xml);
                            }
                            state.active = !state.active;
                        }
                    }, {
                        iconId: 'create_callouts',
                        langId: 'ID_RIBBON_CREATE',
                        id: 'rCalloutCreate',
                        interactionClass: 'Interaction',
                        interaction: 'Callout_Creation',
                        method: function (client, state) {
                        }
                    }, {
                        iconId: 'callout_group',
                        langId: 'ID_RIBBON_CALLOUTS_FOR_SELECTION',
                        id: 'rCalloutGroup',
                        method: function (client, state) {
                            var xml = '<Call Method="CreateCalloutGroup"></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: 'callout',
                        langId: 'ID_RIBBON_SETTINGS',
                        id: 'rCalloutPane',
                        paneId: 'CalloutPane',
                        method: function (client) {
                        }
                    }, {
                        iconId: 'callout_show_all',
                        langId: 'ID_RIBBON_CALLOUTS_SHOW_ALL',
                        id: 'rCalloutShowAll',
                        method: function (client) {
                            var xml = '<Call Method="ShowAllCallouts"></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: 'callout_lock',
                        langId: 'ID_RIBBON_LOCK_CALLOUTS',
                        id: 'rCalloutLock',
                        method: function (client, state) {
                            var xml = '<Call Method="LockCallouts"></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: 'callout_unlock',
                        langId: 'ID_RIBBON_UNLOCK_CALLOUTS',
                        id: 'rCalloutUnlock',
                        method: function (client, state) {
                            var xml = '<Call Method="UnlockCallouts"></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: 'select_associated_solid',
                        langId: 'ID_RIBBON_CALLOUTS_SELECT_ASSOCIATED_NODES',
                        interactionClass: 'SelectAssociatedNodes',
                        id: 'rCalloutSelectWithNodes',
                        method: function (client, state) {
                            var xml = '<Call Method="SetSceneSettings"><Settings><Scene><Tools><Callout><SelectAssociatedNodes>';
                            xml += !state.active;
                            xml += '</SelectAssociatedNodes></Callout></Tools></Scene></Settings></Call>';
                            client.command.executeXML(xml);
                            state.active = !state.active;
                        }
                    }, {
                        iconId: 'callout_multiple_anchor',
                        langId: 'ID_RIBBON_CALLOUTS_MULTIPLE_CONNECTIONS',
                        id: 'rCalloutMultiAnchor',
                        interactionClass: 'MultiAnchorGroups',
                        method: function (client, state) {
                            var xml = '<Call Method="SetSceneSettings"><Settings><Scene><Tools><Callout><MultiAnchorGroups>';
                            xml += !state.active;
                            xml += '</MultiAnchorGroups></Callout></Tools></Scene></Settings></Call>';
                            client.command.executeXML(xml);
                            state.active = !state.active;
                        }
                    }, {
                        iconId: 'move_anchor',
                        langId: 'ID_RIBBON_EDIT',
                        id: 'rCalloutMove',
                        interactionClass: 'Interaction',
                        interaction: 'Callout_Modification',
                        method: function (client, state) {
                        }
                    },
                    {
                        iconId: 'delete_anchor',
                        langId: 'ID_RIBBON_DELETE',
                        id: 'rCalloutDelete',
                        interactionClass: 'Interaction',
                        interaction: 'Callout_Deletion',
                        method: function (client, state) {
                        }
                    }, {
                        iconId: '5_sichtbarkeit_umkehren',
                        langId: 'ID_RIBBON_INVERT_VISIBILITY',
                        id: 'rCalloutAnchorInvertVis',
                        interactionClass: 'Interaction',
                        interaction: 'Callout_Visibility',
                        method: function (client, state) {
                        }
                    }, {
                        iconId: 'callout_select_visible',
                        langId: 'ID_RIBBON_CALLOUTS_SELECT_VISIBLE',
                        id: 'rCalloutSelectVis',
                        method: function (client) {
                            var xml = '<Call Method="SelectVisibleCallouts"></Call>';
                            client.command.executeXML(xml);
                        }
                    }, {
                        iconId: 'callout_delete',
                        langId: 'ID_RIBBON_CALLOUTS_DELETE_VISIBLE',
                        id: 'rCalloutDelVis',
                        method: function (client) {
                            var xml = '<Call Method="DeleteVisibleCallouts"></Call>';
                            client.command.executeXML(xml);
                        }
                    },
                    {
                        iconId: 'callout_hide',
                        langId: 'ID_RIBBON_CALLOUTS_HIDE_VISIBLE',
                        id: 'rCalloutHideVis',
                        method: function (client) {
                            var xml = '<Call Method="HideVisibleCallouts"></Call>';
                            client.command.executeXML(xml);
                        }
                    }
                ]
            }
        ];
    }
    return RibbonConf;
}());
exports.ribbonConf = new RibbonConf();


/***/ })
/******/ ]);
//# sourceMappingURL=webviewer-mobile-bundle.js.map
require.config({
    shim: {
        "app/desktop/desktop.app": {
            deps: [
                "angular",
                "angular-ui-router",
                "angular-bootstrap",
                "angular-animate",
                "angular-ui-calendar",
                "angular-calendar",
                "angular-cookies",
                "angular-css",
                "angular-daterangepicker",
                "angular-ui-ace",
                "angular-ui-select",
                "angular-validator",
                "angular-xeditable",
                "angular-google-maps",
                "angular-simple-logger",
                "lodash",
                "fullcalendar",
                "jquery-cookie",
                "jquery-maskedinput",
                "jquery-ui",
                "jquery.easyui",
                "jquery.tagsinput",
                "jquery.validate",
                "moment",
                "sugar",
                "wysihtml5",
                "rzslider"
            ]
        },
        "angular-animate": {
            deps: [
                "angular"
            ]
        },
        "angular-bootstrap": {
            deps: [
                "angular"
            ]
        },
        "angular-calendar": {
            deps: [
                "angular"
            ]
        },
        "angular-cookies": {
            deps: [
                "angular"
            ]
        },
        "angular-css": {
            deps: [
                "angular"
            ]
        },
        "angular-daterangepicker": {
            deps: [
                "angular",
                "bootstrap"
            ]
        },
        "angular-ui-ace": {
            deps: [
                "angular"
            ]
        },
        "angular-ui-router": {
            deps: [
                "angular"
            ]
        },
        "angular-ui-select": {
            deps: [
                "angular"
            ]
        },
        "angular-validator": {
            deps: [
                "angular"
            ]
        },
        "angular-xeditable": {
            deps: [
                "angular"
            ]
        },
        "bootstrap-rtl": {
            deps: [
                "bootstrap"
            ]
        },
        fullcalendar: {
            deps: [
                "jquery",
                "moment"
            ]
        },
        "jquery-cookie": {
            deps: [
                "jquery"
            ]
        },
        "jquery-maskedinput": {
            deps: [
                "jquery"
            ]
        },
        "jquery-ui": {
            deps: [
                "jquery"
            ]
        },
        "jquery.easyui": {
            deps: [
                "jquery"
            ]
        },
        "jquery.tagsinput": {
            deps: [
                "jquery"
            ]
        },

        "angular-ui-calendar": {
            deps: [
                "angular", "fullcalendar"
            ]
        },
        "jquery.validate": {
            deps: [
                "jquery"
            ]
        },
        "bootstrap": {
            deps: [
                "jquery"
            ]
        },
        "rzslider": {
            deps: [
                "angular", "jquery", "bootstrap"
            ]
        },
        "angular-google-maps": {
            deps: [
                "angular", "lodash", "angular-simple-logger"
            ]
        },
        "angular-simple-logger": {
            deps: [
                "angular"
            ]
        }
    },
    paths: {
        "angular-calendar": "app/assets/bower_components/angular-calendar/dist/calendar.min",
        "angular-xeditable": "app/assets/bower_components/angular-xeditable/dist/js/xeditable.min",
        angular: "app/assets/bower_components/angular/angular.min",
        "weather-icons": "app/assets/bower_components/weather-icons/font/*",
        bootstrap: "app/assets/bower_components/bootstrap/dist/js/bootstrap.min",
        "angular-bootstrap": "app/assets/bower_components/angular-bootstrap/ui-bootstrap-tpls.min",
        modernizr: "app/assets/bower_components/modernizr/lib/cli",
        retina: "app/assets/bower_components/retina.js/dist/retina.min",
        "jquery.easyui": "app/assets/bower_components/jquery.easyui/jquery.easyui.min",
        "jquery-maskedinput": "app/assets/bower_components/jquery-maskedinput/dist/jquery.maskedinput.min",
        select2: "app/assets/bower_components/select2/dist/js/select2.min",
        "jquery-ui": "app/assets/bower_components/jquery-ui/jquery-ui.min",
        "angular-ui-ace": "app/assets/bower_components/angular-ui-ace/ui-ace.min",
        "jquery-toggles": "app/assets/bower_components/jquery-toggles/toggles.min",
        "angular-animate": "app/assets/bower_components/angular-animate/angular-animate.min",
        "angular-validator": "app/assets/bower_components/angular-validator/dist/angular-validator.min",
        "jquery-cookie": "app/assets/bower_components/jquery-cookie/jquery.cookie",
        "sugar": "app/assets/bower_components/sugar/dist/sugar.min",
        "angular-ui-router": "app/assets/bower_components/angular-ui-router/release/angular-ui-router.min",
        jquery: "app/assets/bower_components/jquery/dist/jquery.min",
        "angular-cookies": "app/assets/bower_components/angular-cookies/angular-cookies.min",
        "wysihtml5": "app/assets/bower_components/wysihtml5/dist/wysihtml5-0.2.0.min",
        "mobile-detect": "app/assets/bower_components/mobile-detect/mobile-detect.min",
        "jquery-migrate": "app/assets/bower_components/jquery-migrate/jquery-migrate.min",
        "jquery.validate": "app/assets/bower_components/jquery.validate/dist/jquery.validate.min",
        "angular-ui-select": "app/assets/bower_components/angular-ui-select/dist/select.min",
        "angular-validator-rules": "app/assets/bower_components/angular-validator/dist/angular-validator-rules.min",
        moment: "app/assets/bower_components/moment/moment",
        "moment-timezone-with-data": "app/assets/bower_components/moment-timezone/builds/moment-timezone-with-data.min",
        "angular-daterangepicker": "app/assets/bower_components/angular-daterangepicker/js/angular-daterangepicker.min",
        "bootstrap-timepicker": 'app/assets/template/js/bootstrap-timepicker.min',
        "angular-css": "app/assets/bower_components/angular-css/angular-css.min",
        respond: "app/assets/bower_components/respond/dest/respond.min",
        requirejs: "app/assets/bower_components/requirejs/require",
        fullcalendar: "app/assets/bower_components/fullcalendar/dist/fullcalendar.min",
        html5shiv: "app/assets/bower_components/html5shiv/dist/html5shiv.min",
        "jquery.tagsinput": "app/assets/bower_components/jquery.tagsinput/src/jquery.tagsinput",
        "split-pane": 'app/assets/bower_components/split-pane/split-pane',
        dropzone: 'app/assets/bower_components/dropzone/dist/dropzone',
        handsontable: 'app/assets/bower_components/handsontable/dist/handsontable.full.min',
        underscore: 'app/assets/bower_components/underscore/underscore-min',
        "bpmn-viewer": 'app/assets/bower_components/bpmn-js/dist/bpmn-viewer',
        "bpmn-navigated-viewer": 'app/assets/bower_components/bpmn-js/dist/bpmn-navigated-viewer',
        "bpmn-modeler": 'app/assets/bower_components/bpmn-js/dist/bpmn-modeler',
        "angular-ui-calendar": 'app/assets/bower_components/angular-ui-calendar/src/calendar',
        "rzslider": 'app/assets/js/rzslider/rzslider',
        "angular-google-maps": 'app/assets/bower_components/angular-google-maps/dist/angular-google-maps.min',
        "lodash": 'app/assets/bower_components/lodash/dist/lodash.min',
        "angular-simple-logger": 'app/assets/bower_components/angular-simple-logger/dist/angular-simple-logger.min'
    },
    packages: [

    ],
    baseUrl: "/",
    waitSeconds: 0,
    urlArgs: "bust=" + (new Date()).getTime()
});

require (
    [
        'app/desktop/desktop.app',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/appViewDirective',
        'app/assets/bower_components/cassini-platform/js/libs/hashtable',
        'app/assets/bower_components/cassini-platform/js/utils/jsUtils',
        'app/assets/bower_components/cassini-platform/js/utils/jsEnum',
        'app/assets/bower_components/cassini-platform/js/utils/uiUtils'
    ],
    function(app)
    {
        console.log("App loading...");
        angular.bootstrap(document, ['app']);
    }
);

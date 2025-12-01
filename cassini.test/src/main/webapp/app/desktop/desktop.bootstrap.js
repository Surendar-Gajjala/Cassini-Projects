/**
 * Created by Suresh Cassini on 03-Jul-18.
 */

require.config({
    shim: {
        "app/desktop/desktop.app": {
            deps: [
                "ace",
                "angular",
                "angular-ui-router",
                "angular-bootstrap",
                "angular-animate",
                "angular-resource",
                "angular-route",
                "angular-calendar",
                "angular-cookies",
                "angular-css",
                "angular-daterangepicker",
                "angular-ui-ace",
                "angular-ui-select",
                "angular-xeditable",
                "bootstrap3-wysihtml5",
                "jquery-cookie",
                "jquery-ui",
                "jquery.easyui",
                "jquery.tagsinput",
                "jquery.validate",
                "moment",
                "sugar",
                "wysihtml5",
                "angular-sanitize",
                "pascalprecht.translate",
                "angular-translate-handler-log",
                "messageformat",
                "angular-translate-interpolation-messageformat",
                "angular-translate-loader-partial",
                "angular-translate-loader-static-files",
                "angular-translate-storage-local",
                "angular-translate-storage-cookie",
                "bootstrap-datetimepicker"
            ]
        },
        "ace": {
            deps: []
        },
        "angular-animate": {
            deps: [
                "angular"
            ]
        },
        "angular-resource": {
            deps: [
                "angular"
            ]
        },
        "angular-route": {
            deps: [
                "angular"
            ]
        },
        "angular-ui-router": {
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
        "angular-ui-select": {
            deps: [
                "angular"
            ]
        },
        "angular-xeditable": {
            deps: [
                "angular"
            ]
        },
        "bootstrap3-wysihtml5": {
            deps: [
                "wysihtml5"
            ]
        },

        "jquery-cookie": {
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
        "morris": {
            deps: [
                "jquery"
            ]
        },
        "angular-vertilize": {
            deps: [
                "angular"
            ]
        },

        "pascalprecht.translate": {
            deps: [
                "angular"
            ]
        },
        "angular-translate-handler-log": {
            deps: [
                "pascalprecht.translate"
            ]
        },
        "angular-translate-storage-cookie": {
            deps: [
                "pascalprecht.translate"
            ]
        },
        "angular-translate-storage-local": {
            deps: [
                "pascalprecht.translate"
            ]
        },
        "angular-translate-loader-static-files": {
            deps: [
                "angular-translate-loader-partial"
            ]
        },
        "angular-sanitize": {
            deps: [
                "angular"
            ]
        },
        "angular-translate-interpolation-messageformat": {
            deps: [
                "messageformat"
            ]
        },
        "angular-translate-loader-partial": {
            deps: [
                "angular-translate-interpolation-messageformat"
            ]
        },
        "messageformat": {
            deps: [
                "angular-translate-handler-log"
            ]
        }
    },
    paths: {
        ace: "app/assets/bower_components/ace-builds/src/ace",
        angular: "app/assets/bower_components/angular/angular.min",
        bootstrap: "app/assets/bower_components/bootstrap/dist/js/bootstrap.min",
        modernizr: "app/assets/bower_components/modernizr/lib/cli",
        retina: "app/assets/bower_components/retina.js/dist/retina.min",
        select2: "app/assets/bower_components/select2/dist/js/select2.min",
        morris: "app/assets/bower_components/morris.js/morris.min",
        jquery: "app/assets/bower_components/jquery/dist/jquery.min",
        moment: "app/assets/bower_components/moment/moment",
        respond: "app/assets/bower_components/respond/dest/respond.min",
        requirejs: "app/assets/bower_components/requirejs/require",
        html5shiv: "app/assets/bower_components/html5shiv/dist/html5shiv.min",
        "angular-calendar": "app/assets/bower_components/angular-calendar/dist/calendar.min",
        "angular-xeditable": "app/assets/bower_components/angular-xeditable/dist/js/xeditable.min",
        "weather-icons": "app/assets/bower_components/weather-icons/font/*",
        "angular-bootstrap": "app/assets/bower_components/angular-bootstrap/ui-bootstrap-tpls.min",
        "jquery.easyui": "app/assets/bower_components/jquery.easyui/jquery.easyui.min",
        "jquery-ui": "app/assets/bower_components/jquery-ui/jquery-ui.min",
        "angular-ui-ace": "app/assets/bower_components/angular-ui-ace/ui-ace.min",
        "angular-animate": "app/assets/bower_components/angular-animate/angular-animate.min",
        "angular-route": "app/assets/bower_components/angular-route/angular-route.min",
        "angular-resource": "app/assets/bower_components/angular-resource/angular-resource.min",
        "angular-validator": "app/assets/bower_components/angular-validator/dist/angular-validator.min",
        "jquery-cookie": "app/assets/bower_components/jquery-cookie/jquery.cookie",
        "sugar": "app/assets/bower_components/sugar/dist/sugar.min",
        "angular-ui-router": "app/assets/bower_components/angular-ui-router/release/angular-ui-router.min",
        "angular-cookies": "app/assets/bower_components/angular-cookies/angular-cookies.min",
        "bootstrap3-wysihtml5": "app/assets/bower_components/bootstrap3-wysihtml5/dist/bootstrap-wysihtml5-0.0.2.min",
        "wysihtml5": "app/assets/bower_components/wysihtml5/dist/wysihtml5-0.2.0.min",
        "jquery.validate": "app/assets/bower_components/jquery.validate/dist/jquery.validate.min",
        "angular-ui-select": "app/assets/bower_components/angular-ui-select/dist/select.min",
        "angular-daterangepicker": "app/assets/bower_components/angular-daterangepicker/js/angular-daterangepicker.min",
        "angular-css": "app/assets/bower_components/angular-css/angular-css.min",
        "jquery.tagsinput": "app/assets/bower_components/jquery.tagsinput/src/jquery.tagsinput",
        "split-pane": 'app/assets/bower_components/split-pane/split-pane',
        "dropzone": 'app/assets/bower_components/dropzone/dist/dropzone',
        "angular-sanitize": 'app/assets/bower_components/angular-sanitize/angular-sanitize',
        "pascalprecht.translate": 'app/assets/bower_components/angular-translate/angular-translate',
        "angular-translate-handler-log": 'app/assets/bower_components/angular-translate-handler-log/angular-translate-handler-log',
        "messageformat": 'app/assets/bower_components/messageformat/messageformat',
        "angular-translate-interpolation-messageformat": 'app/assets/bower_components/angular-translate-interpolation-messageformat/angular-translate-interpolation-messageformat',
        "angular-translate-loader-partial": 'app/assets/bower_components/angular-translate-loader-partial/angular-translate-loader-partial',
        "angular-translate-loader-static-files": 'app/assets/bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files',
        "angular-translate-storage-local": 'app/assets/bower_components/angular-translate-storage-local/angular-translate-storage-local',
        "angular-translate-storage-cookie": 'app/assets/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie',
        "bootstrap-datetimepicker": "app/assets/bower_components/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker"
    },
    packages: [],
    baseUrl: "/",
    waitSeconds: 0,
    urlArgs: "bust=" + (new Date()).getTime()
});

require(
    [
        'app/desktop/desktop.app',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/appViewDirective',
        'app/assets/bower_components/cassini-platform/js/libs/hashtable',
        'app/assets/bower_components/cassini-platform/js/utils/jsUtils',
        'app/assets/bower_components/cassini-platform/js/utils/jsEnum',
        'app/assets/bower_components/cassini-platform/js/utils/uiUtils'
    ],
    function (app) {
        console.log("App loading...");
        angular.bootstrap(document, ['app']);
    }
);

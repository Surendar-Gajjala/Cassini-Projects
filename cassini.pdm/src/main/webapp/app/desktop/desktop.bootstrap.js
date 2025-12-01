require.config({
    shim: {
        "app/desktop/desktop.app": {
            deps: [
                "angular",
                "angular-ui-router",
                "angular-bootstrap",
                "angular-animate",
                "angular-resource",
                "angular-route",
                "angular-cookies",
                "angular-css",
                "jquery-ui",
                "angular-xeditable",
                "sugar",
                'daterangepicker',
                'angular-daterangepicker',
                "angular-ui-ace",
                "angular-ui-select",
                "angular-validator",
                "angular-xeditable",
                "bootstrap3-wysihtml5",
                "fullcalendar",
                "jquery-cookie",
                "jquery-maskedinput",
                "jquery-ui",
                "jquery.easyui",
                "jquery.tagsinput",
                "jquery.validate",
                "moment",
                "wysihtml5",
                "ng-tags-input",
                "angular-sanitize",
                "angular_translate",
                "angular-translate-handler-log",
                "messageformat",
                "messageformatEn",
                "messageformatWrapper",
                "angular-translate-interpolation-messageformat",
                "angular-translate-loader-partial",
                "angular-translate-loader-static-files",
                "angular-translate-storage-local",
                "angular-translate-storage-cookie"
            ]
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
        "angular-bootstrap": {
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
        'daterangepicker': {
            deps: ['jquery', 'bootstrap', 'moment']
        },
        "angular-daterangepicker": {
            deps: ['angular', 'daterangepicker', 'moment']
        },
        "angular-morris-chart": {
            deps: [
                "angular",
                "morris"
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
        "bootstrap3-wysihtml5": {
            deps: [
                "wysihtml5"
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
        "treegrid-dnd": {
            deps: [
                "jquery", "jquery.easyui"
            ]
        },
        "loading-overlay": {
            deps: [
                "angular"
            ]
        },
        "angular-ui-calendar": {
            deps: [
                "jquery", "angular", "fullcalendar"
            ]
        },
        "bootstrap-timepicker": {
            deps: [
                "bootstrap", "jquery"
            ]
        },
        "jquery-orgchart": {
            deps: [
                "jquery"
            ]
        },
        "ng-tags-input": {
            deps: [
                "angular", "jquery"
            ]
        },
        "angular_translate": {
            deps: [
                "angular"
            ]
        },
        "angular-translate-handler-log": {
            deps: [
                "angular_translate"
            ]
        },
        "angular-translate-storage-cookie":{
            deps:[
                "angular_translate"
            ]
        },
        "angular-translate-storage-local": {
            deps: [
                "angular_translate"
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
                "angular_translate",
                "messageformatEn"
            ]
        },
        "angular-translate-loader-partial": {
            deps: [
                "angular-translate-interpolation-messageformat"
            ]
        },
        "messageformat": {
            deps: [
                "angular"
            ]
        },
        "messageformatWrapper":{
            deps:[
                "messageformat"
            ]
        },
        "messageformatEn": {
            deps: [
                "angular",
                "messageformatWrapper"
            ]
        }
    },
    paths: {
        "angular-xeditable": "app/assets/bower_components/angular-xeditable/dist/js/xeditable.min",
        "angular-flot": "app/assets/bower_components/angular-flot/angular-flot",
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
        morris: "app/assets/bower_components/morris.js/morris.min",
        "angular-ui-ace": "app/assets/bower_components/angular-ui-ace/ui-ace.min",
        "jquery-toggles": "app/assets/bower_components/jquery-toggles/toggles.min",
        "angular-animate": "app/assets/bower_components/angular-animate/angular-animate.min",
        "angular-route": "app/assets/bower_components/angular-route/angular-route.min",
        "angular-resource": "app/assets/bower_components/angular-resource/angular-resource.min",
        "angular-validator": "app/assets/bower_components/angular-validator/dist/angular-validator.min",
        "jquery-cookie": "app/assets/bower_components/jquery-cookie/jquery.cookie",
        sugar: "app/assets/bower_components/sugar/dist/sugar.min",
        "angular-ui-router": "app/assets/bower_components/angular-ui-router/release/angular-ui-router.min",
        jquery: "app/assets/bower_components/jquery/dist/jquery.min",
        "angular-cookies": "app/assets/bower_components/angular-cookies/angular-cookies.min",
        "bootstrap3-wysihtml5": "app/assets/bower_components/bootstrap3-wysihtml5/dist/bootstrap-wysihtml5-0.0.2.min",
        "wysihtml5": "app/assets/bower_components/wysihtml5/dist/wysihtml5-0.2.0.min",
        "mobile-detect": "app/assets/bower_components/mobile-detect/mobile-detect.min",
        "jquery-migrate": "app/assets/bower_components/jquery-migrate/jquery-migrate.min",
        "jquery.validate": "app/assets/bower_components/jquery.validate/dist/jquery.validate.min",
        "angular-ui-select": "app/assets/bower_components/angular-ui-select/dist/select.min",
        "angular-validator-rules": "app/assets/bower_components/angular-validator/dist/angular-validator-rules.min",
        moment: "app/assets/bower_components/moment/moment",
        "moment-timezone-with-data": "app/assets/bower_components/moment-timezone/builds/moment-timezone-with-data.min",
        'daterangepicker': 'app/assets/bower_components/bootstrap-daterangepicker/daterangepicker',
        "angular-daterangepicker": "app/assets/bower_components/angular-daterangepicker/js/angular-daterangepicker.min",
        "angular-css": "app/assets/bower_components/angular-css/angular-css.min",
        respond: "app/assets/bower_components/respond/dest/respond.min",
        requirejs: "app/assets/bower_components/requirejs/require",
        fullcalendar: "app/assets/bower_components/fullcalendar/dist/fullcalendar.min",
        html5shiv: "app/assets/bower_components/html5shiv/dist/html5shiv.min",
        "jquery.tagsinput": "app/assets/bower_components/jquery.tagsinput/src/jquery.tagsinput",
        "treegrid-dnd": 'app/assets/js/treegrid-dnd',
        "loading-overlay": 'app/assets/js/loading/treasure-overlay-spinner.min',
        "split-pane": 'app/assets/bower_components/split-pane/split-pane',
        dropzone: 'app/assets/bower_components/dropzone/dist/dropzone',
        handsontable: 'app/assets/bower_components/handsontable/dist/handsontable.full.min',
        "angular-ui-calendar": 'app/assets/bower_components/angular-ui-calendar/src/calendar',
        "bootstrap-timepicker": 'app/assets/template/js/bootstrap-timepicker.min',
        "jquery-orgchart": 'app/assets/js/jquery.orgchart',
        "ng-tags-input":'app/assets/bower_components/ng-tags-input/ng-tags-input',
        "angular-sanitize": 'app/assets/bower_components/angular-sanitize/angular-sanitize',
        "angular_translate": 'app/assets/bower_components/angular-translate/angular-translate',
        "angular-translate-handler-log": 'app/assets/bower_components/angular-translate-handler-log/angular-translate-handler-log',
        "messageformat": 'app/assets/bower_components/messageformat/messageformat',
        "messageformatEn": 'app/assets/bower_components/messageformat/locale/en',
        "messageformatWrapper":'app/assets/bower_components/cassini-platform/js/utils/messageFormatWrapper',
        "angular-translate-interpolation-messageformat": 'app/assets/bower_components/angular-translate-interpolation-messageformat/angular-translate-interpolation-messageformat',
        "angular-translate-loader-partial": 'app/assets/bower_components/angular-translate-loader-partial/angular-translate-loader-partial',
        "angular-translate-loader-static-files": 'app/assets/bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files',
        "angular-translate-storage-local": 'app/assets/bower_components/angular-translate-storage-local/angular-translate-storage-local',
        "angular-translate-storage-cookie": 'app/assets/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie'
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

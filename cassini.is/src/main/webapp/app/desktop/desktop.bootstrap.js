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
                "jquery.easyui",
                "jquery.tagsinput",
                "jquery.validate",
                "moment",
                "wysihtml5",
                "ng-tags-input",
                "ng-file-upload",
                "angular-sanitize",
                "pascalprecht.translate",
                "angular-translate-handler-log",
                "messageformat",
                "angular-translate-interpolation-messageformat",
                "angular-translate-loader-partial",
                "angular-translate-loader-static-files",
                "angular-translate-storage-local",
                "angular-translate-storage-cookie",
                "angular-ui-tab-scroll",
                "angular-ui-calendar",
                "bootstrap-datetimepicker",
                "angular-flot",
                "orgChart",
                "svg",
                "apexcharts"

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
                "jquery", "fullcalendar", "angular"
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
        "ng-file-upload": {
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
        "angular-ui-tab-scroll": {
            deps: [
                "angular", "angular-bootstrap"
            ]
        },
        "messageformat": {
            deps: [
                "angular-translate-handler-log"
            ]
        },
        "flot": {
            deps: ['jquery']
        },
        "flot-resize": {
            deps: ['jquery', 'flot']
        },
        "flot-canvas": {
            deps: ['jquery', 'flot']
        },
        "flot-symbol": {
            deps: ['jquery', 'flot']
        },
        "flot-crosshair": {
            deps: ['jquery', 'flot']
        },
        "flot-categories": {
            deps: ['jquery', 'flot']
        },
        "flot-pie": {
            deps: ['jquery', 'flot']
        },
        "angular-flot": {
            deps: ['jquery', 'angular', 'flot', 'flot-resize', 'flot-canvas', 'flot-symbol', 'flot-crosshair', 'flot-categories', 'flot-pie']
        },
        "orgChart": {
            deps: []
        },
        "apexcharts": {
            deps: ['svg']
        }
    },
    paths: {
        "angular-xeditable": "app/assets/bower_components/angular-xeditable/dist/js/xeditable.min",
        "angular-flot": "app/assets/js/angular-flot/angular-flot",
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
        "angular-morris-chart": "app/assets/bower_components/angular-morris-chart/src/angular-morris-chart.min",
        "angular-validator": "app/assets/bower_components/angular-validator/dist/angular-validator.min",
        "jquery-cookie": "app/assets/bower_components/jquery-cookie/jquery.cookie",
        sugar: "app/assets/bower_components/sugar/dist/sugar.min",
        "angular-ui-router": "app/assets/bower_components/angular-ui-router/release/angular-ui-router.min",
        jquery: "app/assets/bower_components/jquery/dist/jquery.min",
        "angular-cookies": "app/assets/bower_components/angular-cookies/angular-cookies.min",
        "bootstrap3-wysihtml5": "app/assets/template/js/bootstrap-wysihtml5",
        "wysihtml5": "app/assets/bower_components/wysihtml5/src/wysihtml5",
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
        "bpmn-viewer": 'app/assets/bower_components/bpmn-js/dist/bpmn-viewer',
        "bpmn-navigated-viewer": 'app/assets/bower_components/bpmn-js/dist/bpmn-navigated-viewer',
        "bpmn-modeler": 'app/assets/bower_components/bpmn-js/dist/bpmn-modeler',
        "angular-ui-calendar": 'app/assets/js/angular-ui-calendar/src/calendar',
        "bootstrap-timepicker": 'app/assets/template/js/bootstrap-timepicker.min',
        "jquery-orgchart": 'app/assets/js/jquery.orgchart',
        "ng-tags-input": 'app/assets/bower_components/ng-tags-input/ng-tags-input',
        "ng-file-upload": "app/assets/bower_components/ng-file-upload/ng-file-upload.min",
        "angular-sanitize": 'app/assets/bower_components/angular-sanitize/angular-sanitize',
        "pascalprecht.translate": 'app/assets/bower_components/angular-translate/angular-translate',
        "angular-translate-handler-log": 'app/assets/bower_components/angular-translate-handler-log/angular-translate-handler-log',
        "messageformat": 'app/assets/bower_components/messageformat/messageformat',
        "angular-translate-interpolation-messageformat": 'app/assets/bower_components/angular-translate-interpolation-messageformat/angular-translate-interpolation-messageformat',
        "angular-translate-loader-partial": 'app/assets/bower_components/angular-translate-loader-partial/angular-translate-loader-partial',
        "angular-translate-loader-static-files": 'app/assets/bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files',
        "angular-translate-storage-local": 'app/assets/bower_components/angular-translate-storage-local/angular-translate-storage-local',
        "angular-translate-storage-cookie": 'app/assets/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie',
        "angular-ui-tab-scroll": "app/assets/bower_components/angular-ui-tab-scroll/angular-ui-tab-scroll",
        "bootstrap-datetimepicker": "app/assets/bower_components/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker",
        "flot": "app/assets/js/angular-flot/flot/jquery.flot.min",
        "flot-resize": "app/assets/js/angular-flot/flot/jquery.flot.resize.min",
        "flot-canvas": "app/assets/js/angular-flot/flot/jquery.flot.canvas.min",
        "flot-symbol": "app/assets/js/angular-flot/flot/jquery.flot.symbol.min",
        "flot-crosshair": "app/assets/js/angular-flot/flot/jquery.flot.crosshair.min",
        "flot-categories": "app/assets/js/angular-flot/flot/jquery.flot.categories.min",
        "flot-pie": "app/assets/js/angular-flot/flot/jquery.flot.pie.min",
        "orgChart": "app/assets/js/orgChart/getorgchart",
        "svg": "app/assets/js/node_modules/svg.js/dist/svg",
        "apexcharts": "app/assets/js/node_modules/apexcharts/dist/apexcharts"
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

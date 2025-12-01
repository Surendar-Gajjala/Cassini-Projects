require.config({
    baseUrl: '/',
	waitSeconds: 0,
	urlArgs: "bust=" + (new Date()).getTime(),
    paths: {
		'angular': 'app/assets/libs/angular/angular.min',
		'angular-sanitize': 'app/assets/libs/angular-sanitize/angular-sanitize.min',
		'angular-cookies': 'app/assets/libs/angular/angular-cookies.min',
		'angular-storage': 'app/assets/libs/angular-storage/ngStorage.min',
		'angular-route': 'app/assets/libs/angular-ui-router/release/angular-ui-router.min',
		'bootstrap': 'app/assets/libs/bootstrap/bootstrap.min',

		'angular-xeditable': 'app/assets/libs/angular-xeditable/js/xeditable.min',
		'angular-ui-select': 'app/assets/libs/angular-ui-select/select',
		'angular-treegrid': 'app/assets/libs/angular-treegrid/tree-grid-directive',
		'bootstrap-contextmenu': 'app/assets/libs/bootstrap/bootstrap-contextmenu',
		'bootstrap-notify': 'app/assets/libs/bootstrap/bootstrap-notify.min',

		'jquery': 'app/assets/libs/jquery/dist/jquery.min',
		'jquery-ui': 'app/assets/libs/jquery/jquery-ui.min',
		'jquery-easyui': 'app/assets/libs/jquery/jquery-easyui/jquery.easyui.min',
		'jquery-cookies': 'app/assets/libs/jquery/jquery.cookies',
		'jquery-sparkline': 'app/assets/libs/jquery/jquery.sparkline.min',
		'jquery-migrate': 'app/assets/libs/jquery/jquery-migrate.min',
		'jquery-gritter': 'app/assets/libs/jquery/jquery.gritter.min',

		'angular-css': 'app/assets/libs/angular-css/angular-css.min',
		'angular-bootstrap': 'app/assets/libs/angular-bootstrap/ui-bootstrap-tpls.min',
		'angular-animate': 'app/assets/libs/angular-animate/angular-animate.min',
		'select2': 'app/assets/libs/select2/select2.min',
		'toggles': 'app/assets/libs/misc/toggles.min',
		'moment': 'app/assets/libs/misc/moment',
		'cassini-ui-utils': 'app/shared/utils/uiUtils',
		'cassini-js-utils': 'app/shared/utils/jsUtils',
		'daterangepicker': 'app/assets/libs/daterangepicker/daterangepicker',
		'hopscotch': 'app/assets/libs/hopscotch/js/hopscotch.min',

		'angular-daterangepicker': 'app/assets/libs/daterangepicker/angular-daterangepicker.min',
		'ckeditor': 'app/assets/libs/ckeditor/ckeditor',
		'sugar': 'app/assets/libs/misc/sugar.min',
		'loading-overlay': 'app/assets/libs/loading/treasure-overlay-spinner.min',
		'lodash': 'app/assets/libs/angular-google-maps/lodash.min',
		'angular-simple-logger': 'app/assets/libs/angular-google-maps/angular-simple-logger',
		'angular-google-maps': 'app/assets/libs/angular-google-maps/angular-google-maps.min',
		'ace-editor': 'app/assets/libs/angular-ace/ace-editor/src-min-noconflict/ace',
		'ui-ace': 'app/assets/libs/angular-ace/ui-ace',

		'flot': 'app/assets/libs/angular-flot/flot/jquery.flot.min',
		'flot-resize': 'app/assets/libs/angular-flot/flot/jquery.flot.resize.min',
		'flot-canvas': 'app/assets/libs/angular-flot/flot/jquery.flot.canvas.min',
		'flot-symbol': 'app/assets/libs/angular-flot/flot/jquery.flot.symbol.min',
		'flot-crosshair': 'app/assets/libs/angular-flot/flot/jquery.flot.crosshair.min',
		'flot-categories': 'app/assets/libs/angular-flot/flot/jquery.flot.categories.min',
		'flot-pie': 'app/assets/libs/angular-flot/flot/jquery.flot.pie.min',
		'angular-flot': 'app/assets/libs/angular-flot/angular-flot',

		'raphael': 'app/assets/libs/angular-morris/morris/raphael-min',
		'morris': 'app/assets/libs/angular-morris/morris/morris',
		'angular-morris': 'app/assets/libs/angular-morris/angular-morris-chart',
		'angular-validator': 'app/assets/libs/angular-validation/angular-validator.min',
		"bootstrap-timepicker": 'app/assets/libs/bootstrap/bootstrap-timepicker.min',
		'dropzone': 'app/assets/libs/dropzone/dist/dropzone',

		"angular-calendar": "app/assets/libs/angular-calendar/calendar.min",
		'fullcalendar': "app/assets/libs/angular-calendar/fullcalendar",
		"angular-ui-calendar": 'app/assets/libs/angular-calendar/calendar',
		//Mobile libraries
		'mobile-detect': 'app/assets/libs/mobile-detect/mobile-detect.min',
	},
	shim: {
		'app/app.modules': {
			deps: [
				'angular', 'angular-sanitize','bootstrap-timepicker', 'angular-cookies', 'angular-route', 'angular-css', 'angular-treegrid',
				'bootstrap', 'angular-xeditable', 'angular-ui-select', 'angular-storage',
				'angular-bootstrap', 'angular-animate','select2', 'toggles', 'moment',
				'cassini-ui-utils', 'cassini-js-utils', 'bootstrap-contextmenu', 'bootstrap-notify',
				'daterangepicker', 'angular-daterangepicker', 'ckeditor', 'sugar', 'loading-overlay', 'lodash', 'angular-simple-logger', 'angular-google-maps', 'ace-editor', 'ui-ace', 'raphael', 'morris', 'angular-flot',
                'angular-morris', 'angular-validator', 'hopscotch', 'mobile-detect'
			]
		},
		'angular-sanitize': {
			deps: ['angular']
		},
		"bootstrap-timepicker": {
			deps: [
				"bootstrap", "jquery"
			]
		},
		"angular-calendar": {
			deps: [
				"angular"
			]
		},
		fullcalendar: {
			deps: [
				"jquery",
				"moment"
			]
		},
		"angular-ui-calendar": {
			deps: [
				"angular", "fullcalendar"
			]
		},
		'angular-storage': {
			deps: ['angular']
		},
		'angular-cookies': {
			deps: ['angular']
		},
		'angular-route': {
			deps: ['angular']
		},
		'angular-bootstrap': {
			deps: ['bootstrap', 'angular']
		},
		'angular-animate': {
			deps: ['angular']
		},
		'angular-css': {
			deps: ['angular-route']
		},
		'angular-xeditable': {
			deps: ['angular']
		},
		'angular-treegrid': {
			deps: ['angular', 'bootstrap']
		},
		'bootstrap': {
			deps: ['jquery', 'jquery-ui', 'jquery-easyui', 'jquery-gritter',
				'jquery-migrate', 'jquery-cookies', 'jquery-sparkline'
			]
		},
		'jquery-ui': {
			deps: ['jquery']
		},
		'jquery-cookies': {
			deps: ['jquery']
		},
		'jquery-migrate': {
			deps: ['jquery']
		},
		'jquery-sparkline': {
			deps: ['jquery']
		},
		'jquery-easyui': {
			deps: ['jquery', 'jquery-ui']
		},
		'jquery-gritter': {
			deps: ['jquery', 'jquery-ui']
		},
		'toggles': {
			deps: ['jquery', 'jquery-ui']
		},
		'select2': {
			deps: ['jquery', 'jquery-ui']
		},
		'bootstrap-contextmenu': {
			deps: ['bootstrap']
		},
		'bootstrap-notify': {
			deps: ['jquery', 'bootstrap']
		},
		'angular-ui-select': {
			deps: ['angular', 'bootstrap']
		},
		'daterangepicker': {
			deps: ['jquery', 'bootstrap', 'moment']
		},
		'angular-daterangepicker': {
			deps: ['angular', 'daterangepicker', 'moment']
		},
		'ckeditor': {
			deps: ['jquery']
		},
		'sugar': {
			deps: []
		},
		'loading-overlay': {
			'deps': ['angular']
		},
		'angular-simple-logger': {
			'deps': ['angular']
		},
		'angular-google-maps': {
			'deps': ['jquery', 'lodash', 'angular', 'angular-simple-logger']
		},
		'ui-ace': {
			'deps': ['angular', 'ace-editor']
		},
		'morris': {
			'deps': ['jquery', 'raphael']
		},
		'flot': {
			'deps': ['jquery']
		},
		'flot-resize': {
			'deps': ['jquery', 'flot']
		},
		'flot-canvas': {
			'deps': ['jquery', 'flot']
		},
		'flot-symbol': {
			'deps': ['jquery', 'flot']
		},
		'flot-crosshair': {
			'deps': ['jquery', 'flot']
		},
		'flot-categories': {
			'deps': ['jquery', 'flot']
		},
		'flot-pie': {
			'deps': ['jquery', 'flot']
		},
		'angular-flot': {
			'deps': ['jquery', 'angular', 'flot', 'flot-resize','flot-canvas','flot-symbol','flot-crosshair','flot-categories','flot-pie']
		},
		'angular-morris': {
			'deps': ['angular', 'raphael', 'morris']
		},
        'angular-validator': {
            'deps': ['angular']
        }
	}
});

require (
	[
		'app/app.modules',
		'app/components/main/navController',
		'app/components/main/appViewDirective',
		'app/assets/libs/misc/hashtable'
    ],
    function(app)
    {
		console.log("App loading...");
		angular.bootstrap(document, ['app']);
    }
);
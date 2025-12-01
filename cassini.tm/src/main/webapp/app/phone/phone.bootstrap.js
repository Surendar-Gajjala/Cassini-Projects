require.config({
    shim: {
        'app/phone/phone.app': {
            deps: [
                "angular",
                "angular-ui-router",
                "angular-animate",
                "angular-cookies",
                "angular-css",
                'angular-sanitize',
                'angular-aria',
                'angular-material',
                'angular-animate',
                'angular-material-icons',
                "moment",
                "sugar",
                "jquery",
                "android-bridge"
            ]
        },
        'angular-sanitize': {
            deps: ['angular']
        },
        'angular-cookies': {
            deps: ['angular']
        },
        'angular-route': {
            deps: ['angular']
        },
        'angular-css': {
            deps: ['angular']
        },
        'angular-aria': {
            deps: ['angular']
        },
        'angular-animate': {
            deps: ['angular']
        },
        'angular-material': {
            deps: ['angular', 'angular-animate', 'angular-aria']
        },
        'angular-material-icons': {
            deps: ['angular']
        },
        'angular-ui-router': {
            deps: ['angular']
        },
        'angular-ui-select': {
            deps: ['angular']
        },
        'bootstrap': {
            deps: ['bootstrap']
        }
    },
    baseUrl: '/',
    waitSeconds: 0,
    urlArgs: "bust=" + (new Date()).getTime(),
    paths: {
        'angular': "app/assets/bower_components/angular/angular.min",
        'angular-sanitize': 'app/assets/bower_components/angular-sanitize/angular-sanitize.min',
        'angular-cookies': 'app/assets/bower_components/angular-cookies/angular-cookies.min',
        'angular-ui-router': 'app/assets/bower_components/angular-ui-router/release/angular-ui-router.min',
        'angular-css': 'app/assets/bower_components/angular-css/angular-css.min',
        'jquery': 'app/assets/bower_components/jquery/dist/jquery.min',
        'angular-ui-select': 'app/assets/bower_components/angular-ui-select/dist/select.min',

        'angular-material': 'app/assets/bower_components/angular-material/angular-material.min',
        'angular-animate': 'app/assets/bower_components/angular-animate/angular-animate.min',
        'angular-aria': 'app/assets/bower_components/angular-aria/angular-aria.min',
        'angular-material-icons': 'app/assets/bower_components/angular-material-icons/angular-material-icons.min',

        'sugar': 'app/assets/bower_components/sugar/sugar.min',
        'bootstrap' :'app/assets/bower_components/bootstrap/dist/js/bootstrap.min',

        'cassini-js-utils': 'app/shared/utils/jsUtils',
        'moment': "app/assets/bower_components/moment/moment",
        'android-bridge': 'app/phone/util/androidBridge'
    }
});

require (
    [
        'app/phone/phone.app',
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
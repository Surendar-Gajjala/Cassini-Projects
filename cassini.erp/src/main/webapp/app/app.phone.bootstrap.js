require.config({
    baseUrl: '/',
    waitSeconds: 0,
    urlArgs: "bust=" + (new Date()).getTime(),
    paths: {
        'angular': 'app/assets/libs/angular-material/angular.min',
        'angular-sanitize': 'app/assets/libs/angular-material/angular-sanitize.min',
        'angular-cookies': 'app/assets/libs/angular-material/angular-cookies.min',
        'angular-route': 'app/assets/libs/angular-ui-router/release/angular-ui-router.min',
        'angular-css': 'app/assets/libs/angular-css/angular-css.min',

        'angular-material': 'app/assets/libs/angular-material/angular-material.min',
        'angular-animate': 'app/assets/libs/angular-material/angular-animate.min',
        'angular-aria': 'app/assets/libs/angular-material/angular-aria.min',
        'angular-material-icons': 'app/assets/libs/angular-material/angular-material-icons.min',

        'jquery': 'app/assets/libs/jquery/dist/jquery.min',
        'moment': 'app/assets/libs/misc/moment',

        'sugar': 'app/assets/libs/misc/sugar.min',

        'cassini-js-utils': 'app/shared/utils/jsUtils',
        'android-bridge': 'app/mobile/util/androidBridge'
    },
    shim: {
        'app/app.modules': {
            deps: [
                'angular',
                'angular-sanitize',
                'angular-cookies',
                'angular-route',
                'angular-css',

                'angular-material',
                'angular-animate',
                'angular-aria',
                'angular-material-icons',

                'jquery',
                'moment',

                'sugar',

                'cassini-js-utils',
                'android-bridge'
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
        'cassini-js-utils': {
            deps: ['jquery']
        }
    }
});

require (
    [
        'app/app.modules',
        'app/assets/libs/misc/hashtable'
    ],
    function(app)
    {
        console.log("App loading...");
        angular.bootstrap(document, ['app']);
    }
);
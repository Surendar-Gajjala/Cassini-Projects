define(
    [
        'bootstrap',
        'angular-ui-select',
        'app/assets/bower_components/cassini-platform/app/shared/modules/depends.module'
    ],

    function() {
        return angular.module('app.root', [
                'app.depends',
                'ngAnimate',
                'ngCookies',
                'ui.router',
                'ui.bootstrap',
                'ui.select',
                'door3.css',
                'xeditable',
                'rzModule',
                'uiGmapgoogle-maps'
            ]
        );
    }
);
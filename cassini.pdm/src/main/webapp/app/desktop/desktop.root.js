define(
    [
        'bootstrap',
        'angular-ui-calendar',
        'angular-ui-select',
        'app/assets/bower_components/cassini-platform/app/shared/modules/depends.module'
    ],

    function() {
        return angular.module('app.root', [
                'app.depends',
                'ngAnimate',
                'ngSanitize',
                'ngCookies',
                'ui.router',
                'ui.calendar',
                'ui.bootstrap',
                'ui.select',
                'door3.css',
                'xeditable',
                'daterangepicker',
                'ngTagsInput',
                'pascalprecht.translate'
            ]
        );
    }
);
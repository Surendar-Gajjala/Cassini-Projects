define(
    [
        'bootstrap',
        'angular-ui-calendar',
        'angular-ui-select',
        'angular-summernote',
        'app/assets/bower_components/cassini-platform/app/shared/modules/depends.module'
    ],

    function () {
        return angular.module('app.root', [
                'app.depends',
                'ngAnimate',
                'ngSanitize',
                'ngCookies',
                'ngResource',
                'ngRoute',
                'ui.router',
                'ui.bootstrap',
                'ui.select',
                'door3.css',
                'xeditable',
                'pascalprecht.translate',
                'summernote',
                'ngFileSaver',
                'ui.tab.scroll',
                'ui.calendar'
            ]
        );
    }
);
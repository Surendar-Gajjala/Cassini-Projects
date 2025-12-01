/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
define(
    [
        'bootstrap',
        'angular-ui-select',
        'angular-xeditable',
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
                'pascalprecht.translate'
            ]
        );
    }
);
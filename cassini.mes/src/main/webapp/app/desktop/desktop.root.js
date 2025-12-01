define(
    [
        'bootstrap',
        'angular-ui-select',
        'angular-xeditable',
        'app/assets/bower_components/cassini-platform/app/shared/modules/depends.module'
    ],

    function() {
        return angular.module('app.root', [
                'app.depends',
                'ngCookies',
                'ui.router',
                'ui.bootstrap',
                'ui.select',
                'door3.css',
                'xeditable',
                'angularjs-dropdown-multiselect'
            ]
        );
    }
);
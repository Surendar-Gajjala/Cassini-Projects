define(
    [
        'app/assets/bower_components/cassini-platform/app/shared/modules/depends.module'
    ],

    function() {
        return angular.module('app.root', [
                'app.depends',
                'ngMaterial',
                'ngAnimate',
                'ngMdIcons',
                'ngCookies',
                'ui.router',
                'door3.css'
            ]
        );
    }
);
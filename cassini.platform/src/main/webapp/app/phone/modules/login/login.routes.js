define(
    [
        'app/phone/phone.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'login': {
                    url: '/login?fromapp',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/phone/modules/login/loginView.jsp',
                    controller: 'LoginController as loginVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/phone/modules/login/loginController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
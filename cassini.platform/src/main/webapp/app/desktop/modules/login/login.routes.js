define(
    [
        'app/desktop/desktop.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'login': {
                    url: '/login?expired',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/login/loginView.jsp',
                    controller: 'LoginController as loginVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/login/loginController'],
                    css: cssConfig.getViewCss('login')
                }
            }
        };
    }
);
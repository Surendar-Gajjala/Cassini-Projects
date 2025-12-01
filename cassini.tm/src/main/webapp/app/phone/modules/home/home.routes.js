define(
    [
        'app/phone/phone.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.home': {
                    url: '/home',
                    templateUrl: 'app/phone/modules/home/homeView.jsp',
                    controller: 'HomeController as homeVm',
                    resolve: ['app/phone/modules/home/homeController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
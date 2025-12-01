define(
    [
        'app/phone/phone.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app': {
                    url: '/app',
                    templateUrl: 'app/phone/modules/main/mainView.jsp',
                    controller: 'MainController as mainVm',
                    resolve: ['app/phone/modules/main/mainController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
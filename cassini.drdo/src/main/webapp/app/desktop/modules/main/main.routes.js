define(
    [
        'app/desktop/desktop.css',

    ],
    function(cssConfig) {
        return {
            routes: {
                'app': {
                    url: '/app',
                    templateUrl: 'app/desktop/modules/main/mainView.jsp',
                    controller: 'MainController as mainVm',
                    resolve: ['app/desktop/modules/main/mainController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
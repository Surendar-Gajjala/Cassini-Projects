define(
    [
        'app/desktop/desktop.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.media': {
                    url: '/media',
                    templateUrl: 'app/desktop/modules/media/mediaView.jsp',
                    controller: 'MediaController as mediaVm',
                    resolve: ['app/desktop/modules/media/mediaController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
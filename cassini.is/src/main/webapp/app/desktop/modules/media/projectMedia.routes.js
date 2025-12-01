/**
 * Created by swapna on 03/10/18.
 */
define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.pm.project.media': {
                    url: '/media',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/media/projectMediaMainView.jsp',
                    controller: 'ProjectMediaMainController as mediaMainVm',
                    resolve: ['app/desktop/modules/media/projectMediaMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.media.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/media/all/projectMediaView.jsp',
                    controller: 'ProjectMediaController as projectMediaVm',
                    resolve: ['app/desktop/modules/media/all/projectMediaController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
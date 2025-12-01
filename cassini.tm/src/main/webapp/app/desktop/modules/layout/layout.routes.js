/**
 * Created by Anusha on 12-08-2016.
 */
define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.layout': {
                    url: '/layout',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/layout/layoutMainView.jsp',
                    controller: 'LayoutMainController as layoutMainVm',
                    resolve: ['app/desktop/modules/layout/layoutMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.layout.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/layout/all/layoutView.jsp',
                    controller: 'LayoutController as layoutVm',
                    resolve: ['app/desktop/modules/layout/all/layoutController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.layout.details': {
                    url: '/:layoutId',
                    templateUrl: 'app/desktop/modules/layout/details/layoutDetailsView.jsp',
                    controller: 'LayoutDetailsController as layoutDetailsVm',
                    resolve: ['app/desktop/modules/layout/details/layoutDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
            }
        };
    }
);

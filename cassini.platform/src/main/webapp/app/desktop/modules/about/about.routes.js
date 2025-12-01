define(
    [
        'app/desktop/desktop.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.about': {
                    url: '/about',
                    abstract: true,
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/about/aboutView.jsp',
                    controller: 'AboutController as aboutVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/about/aboutController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.about.main': {
                    url: '/main',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/about/main/aboutMainView.jsp',
                    controller: 'AboutMainController as aboutMainVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/about/main/aboutMainController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.sharing': {
                    url: '/sharing',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/sharing/sharingMainView.jsp',
                    controller: 'SharingMainController as shareVm',
                    resolve: ['app/desktop/modules/sharing/sharingMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.sharing.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/sharing/all/allSharedToView.jsp',
                    controller: 'AllSharedToController as allSharedToVm',
                    resolve: ['app/desktop/modules/sharing/all/allSharedToController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
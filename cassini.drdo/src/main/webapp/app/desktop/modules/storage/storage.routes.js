define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.storage': {
                    url: '/storage',
                    templateUrl: 'app/desktop/modules/storage/storageView.jsp',
                    controller: 'StorageController as storageVm',
                    resolve: ['app/desktop/modules/storage/storageController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    });

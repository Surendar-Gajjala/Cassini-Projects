define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.exim': {
                    url: '/exim',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/exim/eximMainView.jsp',
                    controller: 'ExportImportController as eximVm',
                    resolve: ['app/desktop/modules/exim/eximMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.exim.export': {
                    url: '/export',
                    templateUrl: 'app/desktop/modules/exim/export/exportView.jsp',
                    controller: 'ExportController as exportVm',
                    resolve: ['app/desktop/modules/exim/export/exportController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.exim.import': {
                    url: '/import',
                    templateUrl: 'app/desktop/modules/exim/import/importView.jsp',
                    controller: 'ImportController as importVm',
                    resolve: ['app/desktop/modules/exim/import/importController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.exim.individual.import': {
                    url: '/individual/import',
                    templateUrl: 'app/desktop/modules/exim/import/importView.jsp',
                    controller: 'IndividualImportController as individualImportVm',
                    resolve: ['app/desktop/modules/exim/import/importController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
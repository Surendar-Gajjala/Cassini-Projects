define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.suppliers': {
                    url: '/suppliers',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/supplier/supplierMainView.jsp',
                    controller: 'SupplierMainController as supplierMainVm',
                    resolve: ['app/desktop/modules/supplier/supplierMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.suppliers.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/supplier/all/suppliersView.jsp',
                    controller: 'SuppliersController as suppliersVm',
                    resolve: ['app/desktop/modules/supplier/all/suppliersController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
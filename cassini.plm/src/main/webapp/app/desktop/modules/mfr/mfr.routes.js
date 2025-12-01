define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.mfr': {
                    url: '/mfr',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mfr/mfrMainView.jsp',
                    controller: 'MfrMainController as mfrMainVm',
                    resolve: ['app/desktop/modules/mfr/mfrMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mfr.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mfr/all/allMfrsView.jsp',
                    controller: 'AllMfrsController as allMfrsVm',
                    resolve: ['app/desktop/modules/mfr/all/allMfrsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mfr.details': {
                    url: '/details/:manufacturerId?tab',
                    templateUrl: 'app/desktop/modules/mfr/details/mfrDetailsView.jsp',
                    controller: 'MfrDetailsController as mfrDetailsVm',
                    resolve: ['app/desktop/modules/mfr/details/mfrDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mfr.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/mfr/new/newMfrView.jsp',
                    controller: 'NewMfrController as newMfrVm',
                    resolve: ['app/desktop/modules/mfr/new/newMfrController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mfr.supplier': {
                    url: '/supplier',
                    templateUrl: 'app/desktop/modules/mfr/supplier/supplierHomeView.jsp',
                    controller: 'SupplierHomeController as supplierHomeVm',
                    resolve: ['app/desktop/modules/mfr/supplier/supplierHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mfr.supplier.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mfr/supplier/all/allSuppliersView.jsp',
                    controller: 'AllSuppliersController as allSuppliersVm',
                    resolve: ['app/desktop/modules/mfr/supplier/all/allSuppliersController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mfr.supplier.details': {
                    url: '/details/:supplierId?tab',
                    templateUrl: 'app/desktop/modules/mfr/supplier/details/supplierDetailsView.jsp',
                    controller: 'SupplierDetailsController as supplierDetailsVm',
                    resolve: ['app/desktop/modules/mfr/supplier/details/supplierDetailsController'],
                    css: cssConfig.getViewCss('app')
                },

            }
        };
    }
);
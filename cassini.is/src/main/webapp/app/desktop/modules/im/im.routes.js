define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.im': {
                    url: '/im',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/im/imMainView.jsp',
                    controller: 'ImMainController as imMainVm',
                    resolve: ['app/desktop/modules/im/imMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.inventory': {
                    url: '/inventory',
                    templateUrl: 'app/desktop/modules/im/inventory/inventoryView.jsp',
                    controller: 'InventoryController as inventoryVm',
                    resolve: ['app/desktop/modules/im/inventory/inventoryController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.stores': {
                    url: '/stores',
                    templateUrl: 'app/desktop/modules/im/stores/storesView.jsp',
                    controller: 'StoresController as storesVm',
                    resolve: ['app/desktop/modules/im/stores/storesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.stores.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/im/stores/all/allStoresView.jsp',
                    controller: 'StoresAllController as storesVm',
                    resolve: ['app/desktop/modules/im/stores/all/allStoresController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.stores.details': {
                    url: '/:storeId?mode',
                    templateUrl: 'app/desktop/modules/im/stores/details/storeDetailsView.jsp',
                    controller: 'StoreDetailsController as storeVm',
                    resolve: ['app/desktop/modules/im/stores/details/storeDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.stores.stock': {
                    url: '/:storeId',
                    templateUrl: 'app/desktop/modules/im/stores/stock/stockView.jsp',
                    controller: 'StockController as stockVm',
                    resolve: ['app/desktop/modules/im/stores/stock/stockController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.stores.stock.received': {
                    url: '/received',
                    templateUrl: 'app/desktop/modules/im/stores/stock/received/stockReceivedView.jsp',
                    controller: 'StockReceivedController as receivedVm',
                    resolve: ['app/desktop/modules/im/stores/stock/received/stockReceivedController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.stores.stock.issued': {
                    url: '/issued',
                    templateUrl: 'app/desktop/modules/im/stores/stock/issued/stockIssuedView.jsp',
                    controller: 'StockIssuedController as issuedVm',
                    resolve: ['app/desktop/modules/im/stores/stock/issued/stockIssuedController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
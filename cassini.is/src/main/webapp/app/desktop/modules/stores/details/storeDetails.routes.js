define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.store.receivedItem': {
                    url: '/storeDetails',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/stores/details/storeDetailsMainView.jsp',
                    controller: 'StoreDetailsMainController as storeDetailsMainVm',
                    resolve: ['app/desktop/modules/stores/details/storeDetailsMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.receivedItem.details': {
                    url: '/:storeId/receviedItemId?mode',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/stockReceived/tabs/basic/stockTabReceivedItemBasicView.jsp',
                    controller: 'StockTabReceivedItemBasicController as stockTabReceivedItemBasicVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/stockReceived/tabs/basic/stockTabReceivedItemBasicController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.newRequisition': {
                    url: '/:storeId/newRequisition',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/requisitions/new/newRequisitionDialogView.jsp',
                    controller: 'NewRequestController as newRequestVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/requisitions/new/newRequisitionDialogController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
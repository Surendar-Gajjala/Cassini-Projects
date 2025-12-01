define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.store': {
                    url: '/stores',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/stores/storeMainView.jsp',
                    controller: 'StoreMainController as storeMainVm',
                    resolve: ['app/desktop/modules/stores/storeMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/stores/all/storesView.jsp',
                    controller: 'StoresController as allStoresVm',
                    resolve: ['app/desktop/modules/stores/all/storesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.inventory': {
                    url: '/inventory',
                    templateUrl: 'app/desktop/modules/stores/inventory/inventoryView.jsp',
                    controller: 'ItemStoreInventoryController as itemStoreInventoryVm',
                    resolve: ['app/desktop/modules/stores/inventory/inventoryController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.store.supplier': {
                    url: '/supplier',
                    templateUrl: 'app/desktop/modules/stores/supplier/all/allSuppliersView.jsp',
                    controller: 'SuppliersAllController as suppliersVm',
                    resolve: ['app/desktop/modules/stores/supplier/all/allSuppliersController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.store.supplierDetails': {
                    url: '/supplier/:supplierId',
                    templateUrl: 'app/desktop/modules/stores/supplier/details/supplierDetailsView.jsp',
                    controller: 'SuppliersDetailsController as supplierDetailsVm',
                    resolve: ['app/desktop/modules/stores/supplier/details/supplierDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.allScrapDetails': {
                    url: '/allScrap',
                    templateUrl: 'app/desktop/modules/stores/scrapRequest/all/allScrapRequestView.jsp',
                    controller: 'AllScrapRequestController as allScrapVm',
                    resolve: ['app/desktop/modules/stores/scrapRequest/all/allScrapRequestController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.store.details': {
                    url: '/:storeId?mode',
                    templateUrl: 'app/desktop/modules/stores/details/storeDetailsView.jsp',
                    controller: 'StoreDetailController as storeDetailsVm',
                    resolve: ['app/desktop/modules/stores/details/storeDetailsController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.store.stock': {
                    url: '/:storeId',
                    templateUrl: 'app/desktop/modules/stores/stock/stockView.jsp',
                    controller: 'StoreStockController as storeStockVm',
                    resolve: ['app/desktop/modules/stores/stock/stockController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.stock.issued': {
                    url: '/issued',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/issues/all/stockIssuesView.jsp',
                    controller: 'StockIssuesController as stockIssuesVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/issues/all/stockIssuesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.newStockIssue': {
                    url: '/:storeId/newIssue',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/issues/new/newStockIssueView.jsp',
                    controller: 'NewStockIssueController as newStockIssueVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/issues/new/newStockIssueController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.newStockReceive': {
                    url: '/:storeId/newReceive',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/receives/new/newStockReceiveDialogView.jsp',
                    controller: 'NewStockReceiveDialogController as newStockReceiveVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/receives/new/newStockReceiveDialogController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.stock.receiveDetails': {
                    url: '/received/:receiveId',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/receives/details/stockReceiveDetailsView.jsp',
                    controller: 'StockReceiveDetailsController as stockReceiveDetailsVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/receives/details/stockReceiveDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.stock.issueDetails': {
                    url: '/issue/:issueId',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/issues/details/stockIssueDetailsView.jsp',
                    controller: 'StockIssueDetailsController as stockIssueDetailsVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/issues/details/stockIssueDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.stock.loanIssuedDetails': {
                    url: '/loanIssued/:loanId',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/loanIssued/details/loanIssuedDetailsView.jsp',
                    controller: 'LoanDetailsController as loanDetailsVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/loanIssued/details/loanIssuedDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.stock.loanReceivedDetails': {
                    url: '/loanReceived/:loanId',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/loanReceived/details/loanReceivedDetailsView.jsp',
                    controller: 'LoanReceivedDetailsController as loanReceivedDetailsVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/loanReceived/details/loanReceivedDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.newLoan': {
                    url: '/:storeId/newLoan',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/loanIssued/new/newLoanDialogView.jsp',
                    controller: 'NewLoanDialogController as newLoanVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/loanIssued/new/newLoanDialogController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.scrapDetails': {
                    url: '/scrapDetails/:scrapDetailsId',
                    templateUrl: 'app/desktop/modules/stores/scrapRequest/scrapDetailsView.jsp',
                    controller: 'ScrapDetailsController as scrapDetailsVm',
                    resolve: ['app/desktop/modules/stores/scrapRequest/scrapDetailsController.js'],
                    css: cssConfig.getViewCss('app')
                },
                'app.newScrap': {
                    url: '/newScrap',
                    templateUrl: 'app/desktop/modules/stores/scrapRequest/new/newScrapRequestView.jsp',
                    controller: 'NewScrapRequestController as newScrapRequestVm',
                    resolve: ['app/desktop/modules/stores/scrapRequest/new/newScrapRequestController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.requsitions': {
                    url: '/requsitions',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/requisitions/all/requisitionsView.jsp',
                    controller: 'RequestsController as requestsVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/requisitions/all/requisitionsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.newRequisition': {
                    url: '/:storeId/newRequisition',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/requisitions/new/newRequisitionDialogView.jsp',
                    controller: 'NewRequestController as newRequestVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/requisitions/new/newRequisitionDialogController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.store.stock.requisitionDetails': {
                    url: '/requisition/:requisitionId',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/requisitions/details/requisitionDetailsView.jsp',
                    controller: 'RequisitionDetailsController as reqDetailsVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/requisitions/details/requisitionDetailsController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.store.indent': {
                    url: '/:storeId/newIndent',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/indents/new/newIndentView.jsp',
                    controller: 'NewIndentController as newIndentVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/indents/new/newIndentController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.store.stock.indentDetails': {
                    url: '/:storeId/indents/:indentId',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/indents/details/indentDetailsView.jsp',
                    controller: 'IndentDetailsController as indentDetailsVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/indents/details/indentDetailsController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.store.stock.purchaseOrderDetails': {
                    url: '/:storeId/purchaseOrders/:purchaseOrderId',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/purchaseOrders/details/purchaseOrderDetailsView.jsp',
                    controller: 'PurchaseOrderDetailsController as purchaseOrderDetailsVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/purchaseOrders/details/purchaseOrderDetailsController']
                },

                'app.store.newRoadChallan': {
                    url: '/:storeId/newRoadChallan',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/roadChallan/new/newRoadChallanDialogView.jsp',
                    controller: 'NewRoadChallanController as newRoadChallanVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/roadChallan/new/newRoadChallanDialogController'],
                    css: cssConfig.getViewCss('app')

                },
                'app.store.stock.roadChallanDetails': {
                    url: '/roadChallan/:roadchallanId',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/roadChallan/details/tabs/roadChallanDetailsView.jsp',
                    controller: 'RoadChallanDetailsController as roadChallanDetailsVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/roadChallan/details/tabs/roadChallanDetailsController']
                },

                'app.store.purchaseOrder': {
                    url: '/:storeId/purchaseOrders/:purchaseOrderId?mode',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/purchaseOrders/new/newPurchaseOrderView.jsp',
                    controller: 'NewPurchaseOrderController as newPurchaseOrderVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/purchaseOrders/new/newPurchaseOrderController']
                },
                'app.store.stockReturns': {
                    url: '/stockReturns',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/stockReturn/all/allStockReturnsView.jsp',
                    controller: 'StockReturnsController as stockReturnsVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/stockReturn/all/allStockReturnsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.store.newStockReturn': {
                    url: '/:storeId/newStockReturn',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/stockReturn/new/newStockReturnView.jsp',
                    controller: 'NewStockReturnController as newStockReturnVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/stockReturn/new/newStockReturnController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.store.stock.stockReturnDetails': {
                    url: '/stockReturn/:stockReturnId',
                    templateUrl: 'app/desktop/modules/stores/details/tabs/stockReturn/details/stockReturnDetailsView.jsp',
                    controller: 'StockReturnDetailsController as stockReturnDetailsVm',
                    resolve: ['app/desktop/modules/stores/details/tabs/stockReturn/details/stockReturnDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        }
    });
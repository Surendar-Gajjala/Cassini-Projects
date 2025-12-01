/**
 * Created by swapna on 21/01/19.
 */
define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.contracts': {
                    url: '/contracts',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/subContracts/subContractsMainView.jsp',
                    controller: 'ContractMainController as contractMainVm',
                    resolve: ['app/desktop/modules/subContracts/subContractsMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.contracts.contractors': {
                    url: '/contractors',
                    templateUrl: 'app/desktop/modules/subContracts/contractors/all/contractorsView.jsp',
                    controller: 'ContractorsController as contractorsVm',
                    resolve: ['app/desktop/modules/subContracts/contractors/all/contractorsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.contracts.workOrders': {
                    url: '/workOrders',
                    templateUrl: 'app/desktop/modules/subContracts/workOrders/all/workOrdersView.jsp',
                    controller: 'WorkOrdersController as workOrdersVm',
                    resolve: ['app/desktop/modules/subContracts/workOrders/all/workOrdersController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.contracts.workOrderDetails': {
                    url: '/:workOrderId',
                    templateUrl: 'app/desktop/modules/subContracts/workOrders/details/workOrderDetailsView.jsp',
                    controller: 'WorkOrderDetailsController as workOrderVm',
                    resolve: ['app/desktop/modules/subContracts/workOrders/details/workOrderDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        }
    });
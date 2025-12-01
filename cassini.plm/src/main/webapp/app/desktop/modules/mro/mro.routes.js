define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.mro': {
                    url: '/mro',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mro/mroMainView.jsp',
                    controller: 'MROMainController as mroMainVm',
                    resolve: ['app/desktop/modules/mro/mroMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.sparePart': {
                    url: '/sparePart',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mro/spareParts/sparePartsHomeView.jsp',
                    controller: 'SparePartsHomeController as sparePartsHomeVm',
                    resolve: ['app/desktop/modules/mro/spareParts/sparePartsHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.sparePart.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mro/spareParts/all/allSparePartsView.jsp',
                    controller: 'AllSparePartController as allSparePartVm',
                    resolve: ['app/desktop/modules/mro/spareParts/all/allSparePartsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.sparePart.details': {
                    url: '/:sparePartId?tab',
                    templateUrl: 'app/desktop/modules/mro/spareParts/details/sparePartsDetailsView.jsp',
                    controller: 'SparePartsDetailsController as sparePartDetailsVm',
                    resolve: ['app/desktop/modules/mro/spareParts/details/sparePartsDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.maintenancePlan': {
                    url: '/maintenanceplans',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mro/maintenancePlan/maintenanceHomeView.jsp',
                    controller: 'MaintenanceHomeController as maintenanceHomeVm',
                    resolve: ['app/desktop/modules/mro/maintenancePlan/maintenancePlanHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.maintenancePlan.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/mro/maintenancePlan/all/allMaintenancePlanView.jsp',
                    controller: 'AllMaintenancePlanPlanController as allMaintenancePlanVm',
                    resolve: ['app/desktop/modules/mro/maintenancePlan/all/allMaintenancePlanController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.maintenancePlan.details': {
                    url: '/:maintenancePlanId?tab',
                    templateUrl: 'app/desktop/modules/mro/maintenancePlan/details/maintenancePlanDetailsView.jsp',
                    controller: 'MaintenanceDetailsController as maintenancePlanDetailsVm',
                    resolve: ['app/desktop/modules/mro/maintenancePlan/details/maintenancePlanDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.asset': {
                    url: '/asset',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mro/assets/assetsHomeView.jsp',
                    controller: 'AssetsHomeController as assetsHomeVm',
                    resolve: ['app/desktop/modules/mro/assets/assetsHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.asset.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mro/assets/all/allAssetsView.jsp',
                    controller: 'AllAssetsController as allAssetsVm',
                    resolve: ['app/desktop/modules/mro/assets/all/allAssetsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.asset.details': {
                    url: '/:assetId?tab',
                    templateUrl: 'app/desktop/modules/mro/assets/details/assetDetailsView.jsp',
                    controller: 'AssetDetailsController as assetDetailsVm',
                    resolve: ['app/desktop/modules/mro/assets/details/assetDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.workRequest': {
                    url: '/workRequest',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mro/workRequest/workRequestHomeView.jsp',
                    controller: 'WorkRequestHomeController as workRequestHomeVm',
                    resolve: ['app/desktop/modules/mro/workRequest/workRequestHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.workRequest.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mro/workRequest/all/allWorkRequestsView.jsp',
                    controller: 'AllWorkRequestsController as allWorkRequestsVm',
                    resolve: ['app/desktop/modules/mro/workRequest/all/allWorkRequestsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.workRequest.details': {
                    url: '/:workRequestId?tab',
                    templateUrl: 'app/desktop/modules/mro/workRequest/details/workRequestDetailsView.jsp',
                    controller: 'WorkRequestDetailsController as workRequestDetailsVm',
                    resolve: ['app/desktop/modules/mro/workRequest/details/workRequestDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.workOrder': {
                    url: '/workorders',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mro/workOrder/workOrdersHomeView.jsp',
                    controller: 'WorkOrdersHomeController as workOrdersHomeVm',
                    resolve: ['app/desktop/modules/mro/workOrder/workOrdersHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.workOrder.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mro/workOrder/all/workOrdersView.jsp',
                    controller: 'WorkOrdersController as workOrdersVm',
                    resolve: ['app/desktop/modules/mro/workOrder/all/workOrdersController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.workOrder.details': {
                    url: '/:workOrderId?tab',
                    templateUrl: 'app/desktop/modules/mro/workOrder/details/workOrderDetailsView.jsp',
                    controller: 'WorkOrderDetailsController as workOrderDetailsVm',
                    resolve: ['app/desktop/modules/mro/workOrder/details/workOrderDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.meter': {
                    url: '/meter',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mro/meter/meterHomeView.jsp',
                    controller: 'MeterHomeController as meterHomeVm',
                    resolve: ['app/desktop/modules/mro/meter/meterHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.meter.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mro/meter/all/allMetersView.jsp',
                    controller: 'AllMetersController as allMetersVm',
                    resolve: ['app/desktop/modules/mro/meter/all/allMetersController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mro.meter.details': {
                    url: '/:meterId?tab',
                    templateUrl: 'app/desktop/modules/mro/meter/details/meterDetailsView.jsp',
                    controller: 'MeterDetailsController as meterDetailsVm',
                    resolve: ['app/desktop/modules/mro/meter/details/meterDetailsController'],
                    css: cssConfig.getViewCss('app')
                }

            }
        };
    }
);
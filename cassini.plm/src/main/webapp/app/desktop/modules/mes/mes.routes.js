define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.mes': {
                    url: '/mes',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/mesMainView.jsp',
                    controller: 'MesMainController as mesMainVm',
                    resolve: ['app/desktop/modules/mes/mesMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData': {
                    url: '/masterdata',
                    templateUrl: 'app/desktop/modules/mes/masterData/mesMasterDataHomeView.jsp',
                    controller: 'MESMasterDataHomeController as mesMasterDataMainVm',
                    resolve: ['app/desktop/modules/mes/masterData/mesMasterDataHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.plant': {
                    url: '/plant',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/plant/plantHomeView.jsp',
                    controller: 'PlantHomeController as plantHomeVm',
                    resolve: ['app/desktop/modules/mes/plant/plantHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.plant.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mes/plant/all/allPlantView.jsp',
                    controller: 'AllPlantController as allPlantVm',
                    resolve: ['app/desktop/modules/mes/plant/all/allPlantController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.plant.details': {
                    url: '/:plantId?tab?workflowActivity',
                    templateUrl: 'app/desktop/modules/mes/plant/details/plantDetailsView.jsp',
                    controller: 'PlantDetailsController as plantDetailsVm',
                    resolve: ['app/desktop/modules/mes/plant/details/plantDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.assemblyline': {
                    url: '/assemblyline',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/assemblyLine/assemblyLineHomeView.jsp',
                    controller: 'AssemblyLineHomeController as assemblyLineHomeVm',
                    resolve: ['app/desktop/modules/mes/assemblyLine/assemblyLineHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.assemblyline.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mes/assemblyLine/all/allAssemblyLineView.jsp',
                    controller: 'AllAssemblyLineController as allAssemblyLineVm',
                    resolve: ['app/desktop/modules/mes/assemblyLine/all/allAssemblyLineController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.assemblyline.details': {
                    url: '/:assemblyLineId?tab',
                    templateUrl: 'app/desktop/modules/mes/assemblyLine/details/assemblyLineDetailsView.jsp',
                    controller: 'AssemblyLineDetailsController as assemblyLineDetailsVm',
                    resolve: ['app/desktop/modules/mes/assemblyLine/details/assemblyLineDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.workcenter': {
                    url: '/workcenter',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/workCenter/workCenterHomeView.jsp',
                    controller: 'WorkCenterHomeController as workCenterHomeVm',
                    resolve: ['app/desktop/modules/mes/workCenter/workCenterHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.workcenter.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mes/workCenter/all/allWorkCenterView.jsp',
                    controller: 'AllWorkCenterController as allWorkCenterVm',
                    resolve: ['app/desktop/modules/mes/workCenter/all/allWorkCenterController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.workcenter.details': {
                    url: '/:workcenterId?tab?workflowActivity',
                    templateUrl: 'app/desktop/modules/mes/workCenter/details/workCenterDetailsView.jsp',
                    controller: 'WorkCenterDetailsController as workCenterDetailsVm',
                    resolve: ['app/desktop/modules/mes/workCenter/details/workCenterDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.tool': {
                    url: '/tool',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/tool/toolHomeView.jsp',
                    controller: 'ToolHomeController as toolHomeVm',
                    resolve: ['app/desktop/modules/mes/tool/toolHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.tool.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mes/tool/all/allToolView.jsp',
                    controller: 'AllToolController as allToolVm',
                    resolve: ['app/desktop/modules/mes/tool/all/allToolController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.tool.details': {
                    url: '/:toolId?tab?workflowActivity',
                    templateUrl: 'app/desktop/modules/mes/tool/details/toolDetailsView.jsp',
                    controller: 'ToolDetailsController as toolDetailsVm',
                    resolve: ['app/desktop/modules/mes/tool/details/toolDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.machine': {
                    url: '/machine',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/machine/machineHomeView.jsp',
                    controller: 'MachineHomeController as machineHomeVm',
                    resolve: ['app/desktop/modules/mes/machine/machineHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.machine.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mes/machine/all/allMachineView.jsp',
                    controller: 'AllMachineController as allMachineVm',
                    resolve: ['app/desktop/modules/mes/machine/all/allMachineController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.machine.details': {
                    url: '/:machineId?tab?workflowActivity',
                    templateUrl: 'app/desktop/modules/mes/machine/details/machineDetailsView.jsp',
                    controller: 'MachineDetailsController as machineDetailsVm',
                    resolve: ['app/desktop/modules/mes/machine/details/machineDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.operation': {
                    url: '/operation',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/operations/operationsHomeView.jsp',
                    controller: 'OperationHomeController as operationHomeVm',
                    resolve: ['app/desktop/modules/mes/operations/operationsHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.operation.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mes/operations/all/allOperationsView.jsp',
                    controller: 'AllOperationsController as allOperationVm',
                    resolve: ['app/desktop/modules/mes/operations/all/allOperationsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.operation.details': {
                    url: '/:operationId?tab?workflowActivity',
                    templateUrl: 'app/desktop/modules/mes/operations/details/operationsDetailsView.jsp',
                    controller: 'OperationDetailsController as operationDetailsVm',
                    resolve: ['app/desktop/modules/mes/operations/details/operationsDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.material': {
                    url: '/material',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/material/materialHomeView.jsp',
                    controller: 'MaterialHomeController as materialHomeVm',
                    resolve: ['app/desktop/modules/mes/material/materialHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.material.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mes/material/all/allMaterialView.jsp',
                    controller: 'AllMaterialController as allMaterialVm',
                    resolve: ['app/desktop/modules/mes/material/all/allMaterialController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.material.details': {
                    url: '/:materialId?tab?workflowActivity',
                    templateUrl: 'app/desktop/modules/mes/material/details/materialDetailsView.jsp',
                    controller: 'MaterialDetailsController as materialDetailsVm',
                    resolve: ['app/desktop/modules/mes/material/details/materialDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.shift': {
                    url: '/shift',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/shift/shiftHomeView.jsp',
                    controller: 'ShiftHomeController as shiftHomeVm',
                    resolve: ['app/desktop/modules/mes/shift/shiftHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.shift.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mes/shift/all/allShiftView.jsp',
                    controller: 'AllShiftController as allShiftVm',
                    resolve: ['app/desktop/modules/mes/shift/all/allShiftController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.shift.details': {
                    url: '/:shiftId?tab?workflowActivity',
                    templateUrl: 'app/desktop/modules/mes/shift/details/shiftDetailsView.jsp',
                    controller: 'ShiftDetailsController as shiftDetailsVm',
                    resolve: ['app/desktop/modules/mes/shift/details/shiftDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.productionOrder': {
                    url: '/productionOrder',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/productionOrder/productionOrderHomeView.jsp',
                    controller: 'ProductionOrderHomeController as productionOrderHomeVm',
                    resolve: ['app/desktop/modules/mes/productionOrder/productionOrderHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.productionOrder.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/mes/productionOrder/all/allProductionOrderView.jsp',
                    controller: 'AllProductionOrderController as allProductionOrderVm',
                    resolve: ['app/desktop/modules/mes/productionOrder/all/allProductionOrderController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.productionOrder.details': {
                    url: '/:productionOrderId?tab',
                    templateUrl: 'app/desktop/modules/mes/productionOrder/details/productionOrderDetailsView.jsp',
                    controller: 'ProductionOrderDetailsController as productionOrderDetailsVm',
                    resolve: ['app/desktop/modules/mes/productionOrder/details/productionOrderDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.mbomInstance': {
                    url: '/mbomInstance',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/mbomInstance/mbomInstanceHomeView.jsp',
                    controller: 'MBOMInstanceHomeController as productionOrderHomeVm',
                    resolve: ['app/desktop/modules/mes/mbomInstance/mbomInstanceHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.mbomInstance.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/mes/mbomInstance/all/allMBOMInstanceView.jsp',
                    controller: 'AllMBOMInstanceController as allMBOMInstanceVm',
                    resolve: ['app/desktop/modules/mes/mbomInstance/all/allMBOMInstanceController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.mbomInstance.details': {
                    url: '/:mbomInstanceId?tab',
                    templateUrl: 'app/desktop/modules/mes/mbomInstance/details/mbomInstanceDetailsView.jsp',
                    controller: 'MBOMInstanceDetailsController as mbomInstanceDetailsVm',
                    resolve: ['app/desktop/modules/mes/mbomInstance/details/mbomInstanceDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.mbomInstance.operationDetails': {
                    url: '/:mbomInstanceId/operations/:operationId?tab',
                    templateUrl: 'app/desktop/modules/mes/mbomInstance/operationDetails/mbomInstanceOperationDetailsView.jsp',
                    controller: 'MBOMInstanceOperationDetailsController as mbomInstanceOperationDetailsVm',
                    resolve: ['app/desktop/modules/mes/mbomInstance/operationDetails/mbomInstanceOperationDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.mbom': {
                    url: '/mbom',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/mbom/mbomHomeView.jsp',
                    controller: 'MBOMHomeController as MBOMHomeVm',
                    resolve: ['app/desktop/modules/mes/mbom/mbomHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.mbom.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/mes/mbom/all/allMBOMView.jsp',
                    controller: 'AllMBOMController as allMBOMVm',
                    resolve: ['app/desktop/modules/mes/mbom/all/allMBOMController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.mbom.details': {
                    url: '/:mbomId?tab',
                    templateUrl: 'app/desktop/modules/mes/mbom/details/mbomDetailsView.jsp',
                    controller: 'MBOMDetailsController as mbomDetailsVm',
                    resolve: ['app/desktop/modules/mes/mbom/details/mbomDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.bop': {
                    url: '/bop',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/bop/bopHomeView.jsp',
                    controller: 'BOPHomeController as BOPHomeVm',
                    resolve: ['app/desktop/modules/mes/bop/bopHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.bop.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/mes/bop/all/allBOPView.jsp',
                    controller: 'AllBOPController as allBOPVm',
                    resolve: ['app/desktop/modules/mes/bop/all/allBOPController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.bop.details': {
                    url: '/:bopId?tab',
                    templateUrl: 'app/desktop/modules/mes/bop/details/bopDetailsView.jsp',
                    controller: 'BOPDetailsController as bopDetailsVm',
                    resolve: ['app/desktop/modules/mes/bop/details/bopDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.bop.planDetails': {
                    url: '/:bopId/plan/:bopPlanId?tab',
                    templateUrl: 'app/desktop/modules/mes/bop/planDetails/bopPlanDetailsView.jsp',
                    controller: 'BOPPlanDetailsController as bopPlanDetailsVm',
                    resolve: ['app/desktop/modules/mes/bop/planDetails/bopPlanDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.jigsAndFixtures': {
                    url: '/jigsAndFixtures',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/jigsAndFixtures/jigsAndFixturesHomeView.jsp',
                    controller: 'JigsAndFixturesHomeController as jigsAndFixturesHomeVm',
                    resolve: ['app/desktop/modules/mes/jigsAndFixtures/jigsAndFixturesHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.jigsAndFixtures.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/mes/jigsAndFixtures/all/allJigsAndFixturesView.jsp',
                    controller: 'AllJigsAndFixturesController as allJigsAndFixturesVm',
                    resolve: ['app/desktop/modules/mes/jigsAndFixtures/all/allJigsAndFixturesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.jigsAndFixtures.details': {
                    url: '/:jigsFixId?tab',
                    templateUrl: 'app/desktop/modules/mes/jigsAndFixtures/details/jigsAndFixturesDetailsView.jsp',
                    controller: 'JigsAndFixturesDetailsController as jigsAndFixturesDetailsVm',
                    resolve: ['app/desktop/modules/mes/jigsAndFixtures/details/jigsAndFixturesDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.manpower': {
                    url: '/manpower',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/manpower/manpowerHomeView.jsp',
                    controller: 'ManpowerHomeController as manpowerHomeVm',
                    resolve: ['app/desktop/modules/mes/manpower/manpowerHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.manpower.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/mes/manpower/all/allManpowerView.jsp',
                    controller: 'AllManpowerController as allManpowerVm',
                    resolve: ['app/desktop/modules/mes/manpower/all/allManpowerController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.manpower.details': {
                    url: '/:manpowerId?tab',
                    templateUrl: 'app/desktop/modules/mes/manpower/details/manpowerDetalisView.jsp',
                    controller: 'ManpowerDetailsController as manpowerDetailsVm',
                    resolve: ['app/desktop/modules/mes/manpower/details/manpowerDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.maintenanceAndRepair': {
                    url: '/maintenanceAndRepair',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/maintenanceAndRepair/maintenanceAndRepairsHomeView.jsp',
                    controller: 'MaintenanceAndRepairHomeController as maintenanceAndRepairHomeVm',
                    resolve: ['app/desktop/modules/mes/maintenanceAndRepair/maintenanceAndRepairsHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.maintenanceAndRepair.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/mes/maintenanceAndRepair/all/allMaintenanceAndRepairsView.jsp',
                    controller: 'AllMaintenanceAndRepairController as allMaintenanceAndRepairVm',
                    resolve: ['app/desktop/modules/mes/maintenanceAndRepair/all/allMaintenanceAndRepairsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.maintenanceAndRepair.details': {
                    url: '/:maintenanceAndRepairId?tab',
                    templateUrl: 'app/desktop/modules/mes/maintenanceAndRepair/details/maintenanceAndRepairsDetailsView.jsp',
                    controller: 'MaintenanceAndRepairDetailsController as maintenanceAndRepairDetailsVm',
                    resolve: ['app/desktop/modules/mes/maintenanceAndRepair/details/maintenanceAndRepairsDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.equipment': {
                    url: '/equipment',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/equipment/equipmentHomeView.jsp',
                    controller: 'EquipmentHomeController as equipmentHomeVm',
                    resolve: ['app/desktop/modules/mes/equipment/equipmentHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.equipment.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mes/equipment/all/allEquipmentView.jsp',
                    controller: 'AllEquipmentController as allEquipmentVm',
                    resolve: ['app/desktop/modules/mes/equipment/all/allEquipmentController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.equipment.details': {
                    url: '/:equipmentId?tab',
                    templateUrl: 'app/desktop/modules/mes/equipment/details/equipmentDetailsView.jsp',
                    controller: 'EquipmentDetailsController as equipmentDetailsVm',
                    resolve: ['app/desktop/modules/mes/equipment/details/equipmentDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.instrument': {
                    url: '/instrument',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mes/instrument/instrumentHomeView.jsp',
                    controller: 'InstrumentHomeController as instrumentHomeVm',
                    resolve: ['app/desktop/modules/mes/instrument/instrumentHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.instrument.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/mes/instrument/all/allInstrumentView.jsp',
                    controller: 'AllInstrumentController as allInstrumentVm',
                    resolve: ['app/desktop/modules/mes/instrument/all/allInstrumentController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mes.masterData.instrument.details': {
                    url: '/:instrumentId?tab',
                    templateUrl: 'app/desktop/modules/mes/instrument/details/instrumentDetailsView.jsp',
                    controller: 'InstrumentDetailsController as instrumentDetailsVm',
                    resolve: ['app/desktop/modules/mes/instrument/details/instrumentDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);
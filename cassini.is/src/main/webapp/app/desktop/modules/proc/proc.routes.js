define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.proc': {
                    url: '/proc',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/proc/procMainView.jsp',
                    controller: 'ProcMainController as procMainVm',
                    resolve: ['app/desktop/modules/proc/procMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.classification': {
                    url: '/classification',
                    templateUrl: 'app/desktop/modules/proc/classification/classificationView.jsp',
                    controller: 'ClassificationController as classVm',
                    resolve: ['app/desktop/modules/proc/classification/classificationController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.materials': {
                    url: '/materials',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/proc/materials/materialsMainView.jsp',
                    controller: 'MaterialsMainController as materialsMainVm',
                    resolve: ['app/desktop/modules/proc/materials/materialsMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.materials.all': {
                    url: '/all?masterDataMode',
                    templateUrl: 'app/desktop/modules/proc/materials/all/materialsView.jsp',
                    controller: 'MaterialsController as materialsVm',
                    resolve: ['app/desktop/modules/proc/materials/all/materialsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.materials.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/proc/materials/new/newMaterialView.jsp',
                    controller: 'NewMaterialController as newMaterialVm',
                    resolve: ['app/desktop/modules/proc/materials/new/newMaterialController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.materials.edit': {
                    url: '/:materialId/edit',
                    templateUrl: 'app/desktop/modules/proc/materials/edit/editMaterialView.jsp',
                    controller: 'EditMaterialController as materialEditVm',
                    resolve: ['app/desktop/modules/proc/materials/edit/editMaterialController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.materials.details': {
                    url: '/:materialId?mode',
                    templateUrl: 'app/desktop/modules/proc/materials/details/materialDetailsView.jsp',
                    controller: 'MaterialDetailsController as materialDetailsVm',
                    resolve: ['app/desktop/modules/proc/materials/details/materialDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.machines': {
                    url: '/machines',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/proc/machines/machinesMainView.jsp',
                    controller: 'MachinesMainController as machinesMainVm',
                    resolve: ['app/desktop/modules/proc/machines/machinesMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.machines.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/proc/machines/all/machinesView.jsp',
                    controller: 'MachinesController as machinesVm',
                    resolve: ['app/desktop/modules/proc/machines/all/machinesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.machines.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/proc/machines/new/newMachineView.jsp',
                    controller: 'NewMachineController as newMachineVm',
                    resolve: ['app/desktop/modules/proc/machines/new/newMachineController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.machines.edit': {
                    url: '/:machineId/edit',
                    templateUrl: 'app/desktop/modules/proc/machines/edit/editMachineView.jsp',
                    controller: 'EditMachineController as machineEditVm',
                    resolve: ['app/desktop/modules/proc/machines/edit/editMachineController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.machines.details': {
                    url: '/:machineId',
                    templateUrl: 'app/desktop/modules/proc/machines/details/machineDetailsView.jsp',
                    controller: 'MachineDetailsController as machineDetailsVm',
                    resolve: ['app/desktop/modules/proc/machines/details/machineDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.manpower': {
                    url: '/manpower',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/proc/manpower/manpowerMainView.jsp',
                    controller: 'ManpowerMainController as manpowerMainVm',
                    resolve: ['app/desktop/modules/proc/manpower/manpowerMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.manpower.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/proc/manpower/all/manpowerView.jsp',
                    controller: 'ManpowerController as manpowerVm',
                    resolve: ['app/desktop/modules/proc/manpower/all/manpowerController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.manpower.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/proc/manpower/new/newManpowerView.jsp',
                    controller: 'NewManpowerController as newManpowerVm',
                    resolve: ['app/desktop/modules/proc/manpower/new/newManpowerController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.manpower.edit': {
                    url: '/:manpowerId/edit',
                    templateUrl: 'app/desktop/modules/proc/manpower/edit/editManpowerView.jsp',
                    controller: 'EditManpowerController as manpowerEditVm',
                    resolve: ['app/desktop/modules/proc/manpower/edit/editManpowerController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.proc.manpower.details': {
                    url: '/:manpowerId',
                    templateUrl: 'app/desktop/modules/proc/manpower/details/manpowerDetailsView.jsp',
                    controller: 'ManpowerDetailsController as manpowerDetailsVm',
                    resolve: ['app/desktop/modules/proc/manpower/details/manpowerDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
            }
        };
    }
);
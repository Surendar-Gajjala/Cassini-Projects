<div style="height: 100%;overflow: auto !important;">
    <style scoped>
        .master-data-left {
            width: 200px;
            border-right: 1px solid #eee;
        }

        .master-data-left .master-data-header {
            height: 50px;
            border-bottom: 1px solid #ddd;
        }

        .master-data-left .master-data-header h3 {
            font-size: 20px;
            margin: 0;
            line-height: 50px;
            padding-left: 10px;
        }

        .master-data-right {
            width: calc(100% - 200px);
        }

        .master-data {
            padding-left: 10px;
            cursor: pointer;
            height: 32px;
            line-height: 30px;
            border-bottom: 1px dotted #ddd;
        }

        .master-data:hover {
            background-color: #d6e1e0 !important;
        }

        .selected-master-data, .selected-master-data:hover {
            color: white;
            background-color: #0081c2 !important;
        }

        tr.base-unit td {
            font-weight: bolder !important;
            color: #337ab7 !important;
        }
    </style>

    <div style="display: flex;height: 100%;">
        <div class="master-data-left">
            <div class="master-data-header"><h3 class="" style="font-size: 20px;margin: 0;padding-bottom: 10px;" translate>MFG_MASTER_DATA</h3></div>
            <div class="master-data"
                 ng-show="isProfileMenu('manufacturing.plants') && hasPermission('plant','view')"
                 ng-class="{'selected-master-data': mesMasterDataMainVm.selectedMasterData == 'plants'}"
                 ng-click="mesMasterDataMainVm.selectMasterData('plants')"
                 ui-sref="app.mes.masterData.plant.all">
                <span translate>PLANT_TYPE</span>
            </div>
            <div class="master-data"
                 ng-show="isProfileMenu('manufacturing.assemblyLines') && hasPermission('assemblyline','view')"
                 ng-class="{'selected-master-data': mesMasterDataMainVm.selectedMasterData == 'assemblyLine'}"
                 ng-click="mesMasterDataMainVm.selectMasterData('assemblyLine')"
                 ui-sref="app.mes.masterData.assemblyline.all">
                <span translate>ASSEMBLY_LINES</span>
            </div>
            <div class="master-data"
                 ng-show="isProfileMenu('manufacturing.work-centers') && hasPermission('workcenter','view')"
                 ng-class="{'selected-master-data': mesMasterDataMainVm.selectedMasterData == 'workCenter'}"
                 ng-click="mesMasterDataMainVm.selectMasterData('workCenter')"
                 ui-sref="app.mes.masterData.workcenter.all">
                <span translate>WORK_CENTER</span>
            </div>
            <div class="master-data"
                 ng-show="isProfileMenu('manufacturing.machines') && hasPermission('machine','view')"
                 ng-class="{'selected-master-data': mesMasterDataMainVm.selectedMasterData == 'machine'}"
                 ng-click="mesMasterDataMainVm.selectMasterData('machine')"
                 ui-sref="app.mes.masterData.machine.all">
                <span translate>MACHINE_TITLE</span>
            </div>
            <div class="master-data"
                 ng-show="isProfileMenu('manufacturing.equipments') && hasPermission('equipment','view')"
                 ng-class="{'selected-master-data': mesMasterDataMainVm.selectedMasterData == 'equipments'}"
                 ng-click="mesMasterDataMainVm.selectMasterData('equipments')"
                 ui-sref="app.mes.masterData.equipment.all">
                <span translate>EQUIPMENTS</span>
            </div>
            <div class="master-data"
                 ng-show="isProfileMenu('manufacturing.instruments') && hasPermission('instrument','view')"
                 ng-class="{'selected-master-data': mesMasterDataMainVm.selectedMasterData == 'instruments'}"
                 ng-click="mesMasterDataMainVm.selectMasterData('instruments')"
                 ui-sref="app.mes.masterData.instrument.all">
                <span translate>INSTRUMENTS</span>
            </div>
            <div class="master-data"
                 ng-show="isProfileMenu('manufacturing.tools') && hasPermission('tool','view')"
                 ng-class="{'selected-master-data': mesMasterDataMainVm.selectedMasterData == 'tools'}"
                 ng-click="mesMasterDataMainVm.selectMasterData('tools')"
                 ui-sref="app.mes.masterData.tool.all">
                <span translate>TOOLS</span>
            </div>
            <div class="master-data"
                 ng-show="isProfileMenu('manufacturing.jigs-fixtures') && hasPermission('jigfixture','view')"
                 ng-class="{'selected-master-data': mesMasterDataMainVm.selectedMasterData == 'jigFixtures'}"
                 ng-click="mesMasterDataMainVm.selectMasterData('jigFixtures')"
                 ui-sref="app.mes.masterData.jigsAndFixtures.all">
                <span translate>JIGS_FIXTURES</span>
            </div>
            <div class="master-data"
                 ng-show="isProfileMenu('manufacturing.materials') && hasPermission('material','view')"
                 ng-class="{'selected-master-data': mesMasterDataMainVm.selectedMasterData == 'material'}"
                 ng-click="mesMasterDataMainVm.selectMasterData('material')"
                 ui-sref="app.mes.masterData.material.all">
                <span translate>MATERIALS</span>
            </div>
            <div class="master-data"
                 ng-show="isProfileMenu('manufacturing.manpower') && hasPermission('manpower','view')"
                 ng-class="{'selected-master-data': mesMasterDataMainVm.selectedMasterData == 'manpower'}"
                 ng-click="mesMasterDataMainVm.selectMasterData('manpower')"
                 ui-sref="app.mes.masterData.manpower.all">
                <span translate>MANPOWER</span>
            </div>
            <div class="master-data"
                 ng-show="isProfileMenu('manufacturing.shifts') && hasPermission('shift','view')"
                 ng-class="{'selected-master-data': mesMasterDataMainVm.selectedMasterData == 'shift'}"
                 ng-click="mesMasterDataMainVm.selectMasterData('shift')"
                 ui-sref="app.mes.masterData.shift.all">
                <span translate>SHIFTS</span>
            </div>
            <div class="master-data"
                 ng-show="isProfileMenu('manufacturing.operations') && hasPermission('operation','view')"
                 ng-class="{'selected-master-data': mesMasterDataMainVm.selectedMasterData == 'operation'}"
                 ng-click="mesMasterDataMainVm.selectMasterData('operation')"
                 ui-sref="app.mes.masterData.operation.all">
                <span translate>OPERATIONS</span>
            </div>
        </div>
        <div class="master-data-right">
            <div ui-view></div>
        </div>
    </div>
</div>

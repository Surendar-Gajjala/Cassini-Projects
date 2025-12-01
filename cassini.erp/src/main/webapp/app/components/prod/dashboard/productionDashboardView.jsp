<div>
    <div class="row">
    <div class="col-md-12">
        <div ng-include="templates.bom" ng-controller="BomController"></div>
    </div>
        </div>
    <div class="row">
        <div class="col-md-6">
            <div ng-include="templates.workcenter" ng-controller="WorkcenterController"></div>
        </div>

        <div class="col-md-6">
            <div ng-include="templates.machine" ng-controller="MachineController"></div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            <div ng-include="templates.process" ng-controller="ProcessController"></div>
        </div>
        <div class="col-md-6">
            <div ng-include="templates.procesStep" ng-controller="ProcessStepController"></div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            <div ng-include="templates.workshiftEmp" ng-controller="EmployeeShiftController"></div>
        </div>

    </div>
</div>
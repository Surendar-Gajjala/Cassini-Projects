<div class="view-container">
    <div class="view-toolbar">
        <div ng-if="allShiftsVm.tabs.morningShift.active">
            <div>
                <button class="btn btn-sm btn-primary mr5 btn-sm" ng-click="allShiftsVm.createShift();">Add Shift</button>
            </div>
        </div>
    </div>

    <div class="view-content no-padding">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="project-details-tabs">
                    <uib-tabset>
                        <uib-tab heading="{{allShiftsVm.tabs.morningShift.heading}}"
                                 active="allShiftsVm.tabs.morningShift.active"
                                 select="allShiftsVm.shiftDetailsTabActivated(allShiftsVm.tabs.morningShift.id)">
                            <div ng-include="allShiftsVm.tabs.morningShift.template"
                                 ng-controller="MorningShiftController as morningShiftVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{allShiftsVm.tabs.eveningShift.heading}}"
                                 active="allShiftsVm.tabs.eveningShift.active"
                                 select="allShiftsVm.shiftDetailsTabActivated(allShiftsVm.tabs.eveningShift.id)">
                            <div ng-include="allShiftsVm.tabs.eveningShift.template"
                                 ng-controller="EveningShiftController as eveningShiftVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{allShiftsVm.tabs.nightShift.heading}}"
                                 active="allShiftsVm.tabs.nightShift.active"
                                 select="allShiftsVm.shiftDetailsTabActivated(allShiftsVm.tabs.nightShift.id)">
                            <div ng-include="allShiftsVm.tabs.nightShift.template"
                                 ng-controller="NightShiftController as nightShiftVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

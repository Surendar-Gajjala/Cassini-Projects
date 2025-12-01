<div class="view-container">
    <div class="view-toolbar" >
        <button class="btn btn-sm btn-success min-width" ng-click="personDetailsVm.back();">Back</button>
        <button class="btn btn-sm btn-info min-width" ng-click="personDetailsVm.updatePerson(); personDetailsVm.updateEmergencyContact(); personDetailsVm.updateOtherInfo();">Save</button>

    </div>

    <div class="view-content no-padding">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="project-details-tabs">
                    <uib-tabset>
                        <uib-tab heading="{{personDetailsVm.tabs.basic.heading}}"
                                 active="personDetailsVm.tabs.basic.active"
                                 select="personDetailsVm.personDetailsTabActivated(personDetailsVm.tabs.basic.id)">
                            <div ng-include="personDetailsVm.tabs.basic.template"
                                 ng-controller="PersonBasicInfoController as personBasicInfoVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{personDetailsVm.tabs.emergencyContact.heading}}"
                                 active="personDetailsVm.tabs.emergencyContact.active"
                                 select="personDetailsVm.personDetailsTabActivated(personDetailsVm.tabs.emergencyContact.id)">
                            <div ng-include="personDetailsVm.tabs.emergencyContact.template"
                                 ng-controller="PersonEmergencyContactController as personEmergencyContactVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{personDetailsVm.tabs.otherInfo.heading}}"
                                 active="personDetailsVm.tabs.otherInfo.active"
                                 select="personDetailsVm.personDetailsTabActivated(personDetailsVm.tabs.otherInfo.id)">
                            <div ng-include="personDetailsVm.tabs.otherInfo.template"
                                 ng-controller="PersonOtherInfoController as personOtherInfoVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{personDetailsVm.tabs.tasks.heading}}"
                                 active="personDetailsVm.tabs.tasks.active"
                                 select="personDetailsVm.personDetailsTabActivated(personDetailsVm.tabs.tasks.id)">
                            <div ng-include="personDetailsVm.tabs.tasks.template"
                                 ng-controller="PersonTasksController as personTasksVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>


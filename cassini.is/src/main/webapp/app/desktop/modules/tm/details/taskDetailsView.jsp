<style scoped>
    .tab-content {
        padding-left: 10px !important;
        padding-right: 10px !important;
    }

    .tab-content .tab-pane {
        overflow: auto !important;
    }

    .tab-pane {
        position: relative;
    }

    .tab-content .tab-pane .responsive-table {
        height: 100%;
        position: absolute;
        overflow: auto !important;
        padding: 5px;
    }

    .tab-content .tab-pane .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px !important;
        z-index: 5;
        background-color: #fff;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default min-width" ng-click="taskDetailsVm.back()">Back</button>
                <button ng-show="taskDetailsVm.tabs.basic.active && task.status == 'ASSIGNED' "
                        class="min-width btn btn-sm btn-info"
                        ng-click="taskDetailsVm.startTask()"
                        ng-disabled="((!login.person.isProjectOwner || !login.person.isTaskOwner || !hasPermission('permission.tasks.edit')) && selectedProject.locked)">
                    Start
                </button>
                <button ng-show="taskDetailsVm.tabs.basic.active"
                        ng-if="task.status == 'INPROGRESS' && !task.hasProblems"
                        class="min-width btn btn-sm btn-danger"
                        ng-click="taskDetailsVm.finishTask()"
                        ng-disabled="(selectedProject.locked) && !(hasPermission('permission.tasks.edit') || login.person.isProjectOwner || login.person.isTaskOwner)">
                    Finish
                </button>
                <button ng-show="taskDetailsVm.tabs.basic.active"
                        ng-if="task.percentComplete > 0 && task.inspectionResult != 'ACCEPTED' && (login.person.isTaskOwner || login.person.isProjectOwner || (task.inspectedByPerson.id == login.person.id))"
                        class="min-width btn btn-sm btn-danger"
                        ng-click="taskDetailsVm.inspectTask()"
                        ng-disabled="(selectedProject.locked == true) && !(login.person.isProjectOwner || login.person.isTaskOwner || (task.inspectedByPerson.id == login.person.id))">
                    Inspect
                </button>
                <%--<button ng-show="taskDetailsVm.tabs.documents.active" class="btn btn-sm btn-info min-width"--%>
                <%--ng-click="taskDetailsVm.addTaskDocuments()"--%>
                <%--ng-disabled="(task.status == 'FINISHED' || selectedProject.locked == true) && (hasPermission('permission.tasks.addDocuments') || !login.person.isProjectOwner || !login.person.isTaskOwner)">--%>
                <%--AddDocuments--%>
                <%--</button>--%>
                <%--<button ng-show="taskDetailsVm.tabs.drawings.active" class="btn btn-sm btn-info min-width"--%>
                <%--ng-click="taskDetailsVm.addTaskDrawings()"--%>
                <%--ng-disabled="(task.status == 'FINISHED' || selectedProject.locked == true) && (hasPermission('permission.tasks.addDrawings') || !login.person.isProjectOwner || !login.person.isTaskOwner)">--%>
                <%--AddDrawings--%>
                <%--</button>--%>
                <button ng-show="taskDetailsVm.tabs.manpower.active" class="btn btn-sm btn-success min-width"
                        ng-click="taskDetailsVm.addManpower()"
                        ng-disabled="(task.status == 'FINISHED' || selectedProject.locked == true) || !(hasPermission('permission.tasks.addManpower') || login.person.isProjectOwner || login.person.isTaskOwner)">
                    Add Manpower
                </button>
                <button ng-show="taskDetailsVm.tabs.machine.active" class="btn btn-sm btn-success min-width"
                        ng-click="taskDetailsVm.addMachine()"
                        ng-disabled="(task.status == 'FINISHED' || selectedProject.locked == true) || !(hasPermission('permission.tasks.addMachine') || login.person.isProjectOwner || login.person.isTaskOwner)">
                    Add Machine
                </button>
                <button ng-show="taskDetailsVm.tabs.machine.active" class="btn btn-sm btn-primary min-width"
                        ng-if="taskDetailsVm.resourceMachines.length > 0" ng-click="machineTaskVm.updateMachines()">
                    Update
                </button>
                <button ng-show="taskDetailsVm.tabs.material.active" class="btn btn-sm btn-success min-width"
                        ng-click="taskDetailsVm.addMaterial()"
                        ng-disabled="(task.status == 'FINISHED' || selectedProject.locked == true) || !(hasPermission('permission.tasks.addMaterial') || login.person.isProjectOwner || login.person.isTaskOwner)">
                    Add Material
                </button>
                <button ng-show="taskDetailsVm.tabs.material.active" class="btn btn-sm btn-primary min-width"
                        ng-if="taskDetailsVm.resourceMaterials.length > 0" ng-click="taskDetailsVm.updateMaterials()"
                        ng-disabled="selectedProject.locked == true">
                    Update
                </button>
                <button ng-show="taskDetailsVm.tabs.problems.active" class="btn btn-sm btn-primary min-width"
                        ng-click="taskDetailsVm.newProblem()"
                        ng-disabled="selectedProject.locked == true || task.status == 'ASSIGNED'<%-- || !(login.person.isProjectOwner || login.person.isTaskOwner)--%>">
                    New Problem
                </button>
            </div>
            <div ng-show="taskDetailsVm.tabs.parts.active">
                <free-text-search on-clear="taskDetailsVm.resetPage"
                                  on-search="taskDetailsVm.freeTextSearch"></free-text-search>
            </div>
            <h4 ng-hide="taskDetailsVm.tabs.parts.active"
                style="text-align:right;float: right;margin: 0;padding-right: 10px;vertical-align: middle;width: 500px;max-width:500px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;">
                Task Details : {{viewInfo.title}}</h4>
        </div>

    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="storeVm.activeTab">
                        <uib-tab heading="{{taskDetailsVm.tabs.basic.heading}}"
                                 select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.basic.id)">
                            <div ng-include="taskDetailsVm.tabs.basic.template"
                                 ng-controller="TaskBasicDetailsController as taskBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="manpower"
                                 heading="{{taskDetailsVm.tabs.manpower.heading}}"
                                 select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.manpower.id)">
                            <div ng-include="taskDetailsVm.tabs.manpower.template"
                                 ng-controller="ManpowerTaskController as manpowerTaskVm"></div>
                        </uib-tab>
                        <uib-tab id="material"
                                 heading="{{taskDetailsVm.tabs.material.heading}}"
                                 select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.material.id)">
                            <div ng-include="taskDetailsVm.tabs.material.template"
                                 ng-controller="MaterialTaskController as materialTaskVm"></div>
                        </uib-tab>
                        <uib-tab id="machine"
                                 heading="{{taskDetailsVm.tabs.machine.heading}}"
                                 select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.machine.id)">
                            <div ng-include="taskDetailsVm.tabs.machine.template"
                                 ng-controller="MachineTaskController as machineTaskVm"></div>
                        </uib-tab>
                        <uib-tab id="workflow"
                                 heading="{{taskDetailsVm.tabs.workflow.heading}}"
                                 select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.workflow.id)">
                            <div ng-include="taskDetailsVm.tabs.workflow.template"
                                 ng-controller="TaskWorkflowController as taskWorkflowVm"></div>
                        </uib-tab>
                        <uib-tab id="files"
                                 heading="{{taskDetailsVm.tabs.attachments.heading}}"
                                 select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.attachments.id)">
                            <div ng-include="taskDetailsVm.tabs.attachments.template"
                                 ng-controller="TaskAttachmentsController as taskAttachmentVm"></div>
                        </uib-tab>
                        <uib-tab id="media"
                                 heading="{{taskDetailsVm.tabs.media.heading}}"
                                 select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.media.id)">
                            <div ng-include="taskDetailsVm.tabs.media.template"
                                 ng-controller="TaskMediaController as taskMediaVm"></div>
                        </uib-tab>
                        <uib-tab id="problems"
                                 heading="{{taskDetailsVm.tabs.problems.heading}}"
                                 select="taskDetailsVm.taskDetailsTabActivated(taskDetailsVm.tabs.problems.id)">
                            <div ng-include="taskDetailsVm.tabs.problems.template"
                                 ng-controller="TaskProblemController as problemVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

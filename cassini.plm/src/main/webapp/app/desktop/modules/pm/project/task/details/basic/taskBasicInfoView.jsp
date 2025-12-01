<style scoped>
    .activityTask-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        width: 200px;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        right: 100%;
        opacity: 0;
        transition: opacity 1s;
    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 102%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }

    /* The Close Button */
    span.closeimage {
        position: absolute !important;
        top: 50px !important;
        right: 50px !important;

        font-size: 40px !important;
        font-weight: bold !important;
        transition: 0.3s !important;
        cursor: pointer !important;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus,
    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb !important;
        text-decoration: none !important;
        cursor: pointer !important;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb !important;
        text-decoration: none !important;
        cursor: pointer !important;
    }

    .fa-times-circle:before {
        content: "\f057";
        margin-left: -12px !important;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .browse-control {
        -moz-border-radius: 3px;
        -webkit-border-radius: 3px;
        border-radius: 3px;
        padding: 5px;
        height: auto;
        -moz-box-shadow: none;
        -webkit-box-shadow: none;
        box-shadow: none;
        font-size: 13px;
        border: 1px solid #ccc;
    }

    /* .btn-default {
         background: #e4e7ea !important;
         color: #636e7b !important;
     }*/
</style>
<div class="row row-eq-height" style="margin: 0">
    <div class="item-details col-sm-12" style="padding: 30px;">

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right" style="font-size: 14px">
                <span translate>NAME</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" e-style="width:250px"
                   ng-if="taskBasicVm.task.percentComplete < 100 && taskWritePermission"
                   onaftersave="taskBasicVm.updateTask(taskBasicVm.task)"
                   title="{{'CLICK_TO_SET_VALUE' | translate}}"
                   editable-text="taskBasicVm.task.name">{{taskBasicVm.task.name}}</a>
                <span ng-if="taskBasicVm.task.percentComplete == 100 || taskReadPermission">{{taskBasicVm.task.name}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>DESCRIPTION</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" e-style="width:250px"
                   ng-if="taskBasicVm.task.percentComplete < 100 && taskWritePermission"
                   onaftersave="taskBasicVm.updateTask(taskBasicVm.task)"
                   editable-text="taskBasicVm.task.description"
                   ng-bind-html="taskBasicVm.task.description || 'CLICK_TO_ENTER_DESCRIPTION' | translate"
                   title="{{'CLICK_TO_ENTER_DESCRIPTION' | translate}}">{{taskBasicVm.task.description}} </a>

                <span ng-if="taskBasicVm.task.percentComplete == 100 || taskReadPermission"
                      ng-bind-html="taskBasicVm.task.description"></span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>ASSIGNED_TO</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="#" e-style="width: 50%"
                   ng-if="taskBasicVm.task.percentComplete < 100 && taskWritePermission"
                   onaftersave="taskBasicVm.updateTask()"
                   editable-select="taskBasicVm.task.assignedToObject"
                   e-ng-options="person as person.fullName for person in taskBasicVm.task.persons track by person.id"
                   translate>
                    {{taskBasicVm.task.assignedToObject.fullName || 'CLICK_TO_ADD_PERSON' }}
                </a>
                <span ng-if="taskBasicVm.task.percentComplete ==100 || taskReadPermission"
                <%--ng-if="!hasPermission('project','edit')"--%>>{{taskBasicVm.task.assignedToObject.fullName}}</span>
            </div>
        </div>


        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>Task Completion Status</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span><task-status task="taskBasicVm.task"></task-status></span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>Workflow Status</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{taskBasicVm.task.taskStatus}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>PERCENT</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" e-style="width:250px"
                   ng-if="taskBasicVm.task.percentComplete < 100 && taskWritePermission"
                   onaftersave="taskBasicVm.updateTask(taskBasicVm.task)"
                   editable-text="taskBasicVm.task.percentComplete"
                   ng-bind-html="taskBasicVm.task.percentComplete || 'CLICK_TO_SET_PERCENT' | translate"
                   title="{{'CLICK_TO_SET_PERCENT' | translate}}">{{taskBasicVm.task.percentComplete}} </a>

                <span ng-if="taskBasicVm.task.percentComplete == 100 || taskReadPermission"
                      ng-bind-html="taskBasicVm.task.percentComplete"></span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>PERCENT_COMPLETE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <div ng-if="taskBasicVm.task.percentComplete < 100" style="max-width: 200px;"
                     class="activityTask-progress progress text-center">
                    <div style="width:{{taskBasicVm.task.percentComplete}}%"
                         class="progress-bar progress-bar-primary progress-bar-striped active"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                        <span style="margin-left: 2px;">{{taskBasicVm.task.percentComplete}}%</span>
                    </div>
                </div>
                <div ng-if="taskBasicVm.task.percentComplete == 100" style="max-width: 200px;"
                     class="activityTask-progress progress text-center">
                    <div style="width:{{taskBasicVm.task.percentComplete}}%"
                         class="progress-bar progress-bar-success progress-bar-striped active"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                        <span style="margin-left: 2px;">{{taskBasicVm.task.percentComplete}}%</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{taskBasicVm.task.createdByObject.firstName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{taskBasicVm.task.createdDate}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MODIFIED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{taskBasicVm.task.createdByObject.firstName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MODIFIED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{taskBasicVm.task.modifiedDate}}</span>
            </div>
        </div>

        <basic-attribute-details-view object-type="PROJECTTASK"
                                      quality-type="PROJECTTASK"
                                      has-permission="hasPermission('projecttask','edit')"
                                      object-id="task.id"></basic-attribute-details-view>
    </div>
</div>

<style scoped>
    .item-details > div.row.master-att:last-child,
    .item-details > div.row.revision-att:last-child {
        border-bottom: 0 !important;
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

    .img-model .closeimage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeimage1:hover,
    .img-model .closeimage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeimage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeimage1:hover,
    .img-model .closeimage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    i.fa-times-circle {
        margin-left: 60px !important;
        color: gray;
        cursor: pointer;
    }
</style>


<div class="item-details col-sm-12" style="padding: 30px;">

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{projectBasicVm.project.type.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>NAME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="" e-style="width:250px"
               ng-if="(projectBasicVm.project.actualStartDate == null || projectBasicVm.project.actualStartDate == '' ) && projectDetailsPermission"
               onaftersave="projectBasicVm.updateProject()"
               editable-text="projectBasicVm.project.name"
               title="{{projectBasicVm.nameTooltip}}">
                <span ng-bind-html="projectBasicVm.project.name"></span>
            </a>

            <span ng-if="(projectBasicVm.project.actualStartDate != null && !projectDetailsPermission) || (projectBasicVm.project.actualStartDate != null && projectDetailsPermission)  || (projectBasicVm.project.actualStartDate == null && !projectDetailsPermission)"
                  ng-bind-html="projectBasicVm.project.name"></span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href=""
               ng-if="(projectBasicVm.project.actualFinishDate == null || projectBasicVm.project.actualFinishDate == '' ) && projectDetailsPermission"
               onaftersave="projectBasicVm.updateProject()"
               editable-textarea="projectBasicVm.project.description"
               title="{{projectBasicVm.clickToUpdateDes}}">
                <span ng-bind-html="(projectBasicVm.project.description)  || 'CLICK_TO_UPDATE_DESCRIPTION' | translate"
                      title="{{projectBasicVm.project.description}}">
                </span>
            </a>
          <span ng-if="(projectBasicVm.project.actualFinishDate != null && !projectDetailsPermission) || (projectBasicVm.project.actualFinishDate != null && projectDetailsPermission)  || (projectBasicVm.project.actualFinishDate == null && !projectDetailsPermission)"
                ng-bind-html="projectBasicVm.project.description"></span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{projectBasicVm.project.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>PROJECT_MANAGER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" style="line-height: 20px">
            <a href="#" e-style="width: 50%"
               ng-if="(projectBasicVm.project.actualFinishDate == null || projectBasicVm.project.actualFinishDate == '' ) && projectDetailsPermission"
               onaftersave="projectBasicVm.updateProject()"
               editable-select="projectBasicVm.project.projectManagerObject"
               title="{{projectBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in projectBasicVm.persons track by person.id">

                {{projectBasicVm.project.projectManagerObject.fullName}}
            </a>
            <span ng-if="(projectBasicVm.project.actualFinishDate != null && !projectDetailsPermission) || (projectBasicVm.project.actualFinishDate != null && projectDetailsPermission)  || (projectBasicVm.project.actualFinishDate == null && !projectDetailsPermission)">
                {{projectBasicVm.project.projectManagerObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <workflow-status-settings workflow="projectBasicVm.project"></workflow-status-settings>
            <!-- <wf-status workflow="projectBasicVm.project"></wf-status> -->
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>PLANNED_START_DATE</span> :
        </div>

        <div class="value col-xs-8 col-sm-9">

            <div class="input-group">
                <span ng-if="projectPercentComplete < 100">
                <a href="" e-style="width:250px" style="font-size: 17px"
                   ng-if="projectBasicVm.project.editMode == false && projectDetailsPermission"
                   ng-click="projectBasicVm.changeFinishDate()"
                   title="{{'UPDATE_PLANNED_START_DATE' | translate}}">
                    <span>{{projectBasicVm.project.plannedStartDate || 'UPDATE_PLANNED_START_DATE' | translate}}</span>

                </a>
                <span
                        ng-if="!projectDetailsPermission">{{projectBasicVm.project.plannedStartDate}}</span>

                <div ng-if="projectBasicVm.project.editMode == true && projectDetailsPermission"
                     class="btn-group"
                     style="width: 300px;">
                    <div class="input-group" style="width: 200px;">
                        <input type="text" class="form-control" date-picker-edit
                               ng-model="projectBasicVm.project.plannedStartDate"
                               name="attDate" placeholder="dd/mm/yyyy">
                                    <span class="input-group-addon">
                                        <i class="glyphicon glyphicon-calendar"></i></span>
                    </div>
                    <div class="btn-group" style="margin-left: 202px;margin-top: -38px;">
                        <button ng-show="projectBasicVm.project.editMode == true"
                                class="btn btn-sm btn-primary"
                                type="button" title="{{'SUBMIT' | translate}}"
                                ng-click="projectBasicVm.updateProject()"><i class="fa fa-check"></i>
                        </button>

                        <button ng-show="projectBasicVm.project.editMode == true"
                                class="btn btn-sm btn-default"
                                type="button" title="{{'CANCEL' | translate}}"
                                ng-click="projectBasicVm.cancelFinishDate()"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>
            </span>
                </span>
            <span ng-if="projectPercentComplete == 100 && !projectDetailsPermission">
                {{projectBasicVm.project.plannedStartDate}}</span>
            </div>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>PLANNED_FINISH_DATE</span> :
        </div>

        <div class="value col-xs-8 col-sm-9">
            <div class="input-group">
                <span ng-if="projectPercentComplete < 100">
                <a href="" e-style="width:250px" style="font-size: 17px"
                   ng-if="projectBasicVm.finishDateFlag == false && projectDetailsPermission"
                   ng-click="projectBasicVm.changeFinishDatee()"
                   title="{{'UPDATE_PLANNED_FINISH_DATE' | translate}}">
                    <span>{{projectBasicVm.project.plannedFinishDate || 'UPDATE_PLANNED_FINISH_DATE' | translate}}</span>

                </a>
                <span ng-if="!projectDetailsPermission">{{projectBasicVm.project.plannedFinishDate}}</span>

                <div ng-if="projectBasicVm.finishDateFlag == true && projectDetailsPermission"
                     class="btn-group" style="width: 300px;">
                    <div class="input-group" style="width: 200px;">
                        <input type="text" class="form-control" date-picker-edit
                               ng-model="projectBasicVm.project.plannedFinishDate"
                               name="attDate" placeholder="dd/mm/yyyy">
                                    <span class="input-group-addon">
                                        <i class="glyphicon glyphicon-calendar"></i></span>
                    </div>
                    <div class="btn-group" style="margin-left: 202px;margin-top: -38px;">
                        <button ng-show="projectBasicVm.finishDateFlag == true"
                                class="btn btn-sm btn-primary"
                                type="button" title="{{'SUBMIT' | translate}}"
                                ng-click="projectBasicVm.updateProject()"><i class="fa fa-check"></i>
                        </button>

                        <button ng-show="projectBasicVm.finishDateFlag == true"
                                class="btn btn-sm btn-default"
                                type="button" title="{{'CANCEL' | translate}}"
                                ng-click="projectBasicVm.cancelFinishDatee()"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>
            </span>
            <span ng-if="projectPercentComplete == 100 && !projectDetailsPermission">
                {{projectBasicVm.project.plannedFinishDate}}</span>
            </div>


        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>ACTUAL_START_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{projectBasicVm.project.actualStartDate}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>ACTUAL_FINISH_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{projectBasicVm.project.actualFinishDate}}</span>
        </div>
    </div>


    <!-- <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>PROGRAM</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{projectBasicVm.program.name}}</span>
        </div>
    </div> -->


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>Make Conversation Private</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <input type="checkbox" id="makeConversation" switch="none" checked=""
                   ng-model="projectBasicVm.project.makeConversationPrivate"
                   ng-disabled="projectBasicVm.project.projectManager != loginPersonDetails.person.id"
                   ng-change="projectBasicVm.updateProject()">
            <label style="margin: 1px" for="makeConversation" data-on-label="Yes" data-off-label="No"></label>
        </div>
    </div>

    <object-attribute-details-view object-type="PMOBJECTTYPE"
                                   actual-object-type="PROJECT"
                                   has-permission="projectDetailsPermission"
                                   object-type-id="projectBasicVm.project.type.id"
                                   object-id="projectBasicVm.projectId"></object-attribute-details-view>
</div>




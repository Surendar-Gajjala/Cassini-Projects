<style>

    /* The Close Button */
    .img-model .closeimage {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeimage:hover,
    .img-model .closeimage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
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

    .image.image_resized > img {
        width: 100%;
        height: 100%;
    }

    p .marker-yellow {
        background-color: yellow;
    }

    .ck.ck-reset_all.ck-widget__type-around,
    .ck.ck-widget__selection-handle,
    .ck.ck-reset_all, .ck.ck-reset_all,
    .ck-editor__editable.ck-editor__nested-editable.ck-placeholder {
        display: none !important;
    }

    .item-details .req-description {
        max-width: 1000px;
    }

    .image.image_resized > img {
        width: 300px !important;
    }

</style>

<div ng-if="reqBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_REQUIREMENTS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="reqBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqBasicVm.reqVersion.master.number}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqBasicVm.reqVersion.master.type.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <a href=""
               ng-if="reqBasicVm.reqVersion.lifeCyclePhase.phaseType != 'RELEASED' <%--&& reqBasicVm.reqVersion.master.ignoreForRelease != true--%>"
               e-style="width:250px" onaftersave="reqBasicVm.updateRequirement()"
               editable-text="reqBasicVm.reqVersion.name">
                <span ng-bind-html="reqBasicVm.reqVersion.name || 'ADD_NAME' | translate"></span>
            </a>
            <span ng-if="reqBasicVm.reqVersion.lifeCyclePhase.phaseType == 'RELEASED' <%--|| reqBasicVm.reqVersion.master.ignoreForRelease == true--%>">
                {{reqBasicVm.reqVersion.name}}
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
               <!-- <a class="req-description" href=""
               ng-if="reqBasicVm.reqVersion.lifeCyclePhase.phaseType != 'RELEASED'"
               onaftersave="reqBasicVm.updateRequirement()"
               editable-textarea="reqBasicVm.reqVersion.description">
                <span 
                ng-bind-html="reqBasicVm.reqVersion.descriptionHtml || 'CLICK_TO_ENTER_DESCRIPTION' | translate"></span> 
            </a>
             <span ng-if="reqBasicVm.reqVersion.lifeCyclePhase.phaseType == 'RELEASED'"
                   ng-bind-html="reqBasicVm.reqVersion.descriptionHtml">
            </span>   -->
              <span ng-if="reqBasicVm.editDescription == false"
            ng-bind-html="reqBasicVm.reqVersion.descriptionHtml">
      </span> 
          <a class="fa fa-pencil row-edit-btn" style="padding-left: 7px;cursor: pointer;"
             ng-if="reqBasicVm.editDescription != true && reqBasicVm.reqVersion.lifeCyclePhase.phaseType != 'RELEASED'"
             ng-click="reqBasicVm.editRequirement()" title="{{'CLICK_TO_EDIT_VALUE' | translate}}">
          </a>

          <div ng-show="reqBasicVm.editDescription == true">
              <summernote ng-model="reqBasicVm.reqVersion.description"></summernote>
          </div>

          <button ng-show="reqBasicVm.editDescription == true" title="{{saveAttributeTitle}}"
                  ng-click="reqBasicVm.updateRequirement()"><i class="fa fa-check"></i>
          </button>

          <button ng-show="reqBasicVm.editDescription == true" title="{{cancelChangesTitle}}"
                  ng-click="reqBasicVm.cancelRequirement()"><i class="fa fa-times"></i>
          </button>  
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ASSIGNED_TO</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px"
               ng-if="reqBasicVm.reqVersion.lifeCyclePhase.phaseType != 'RELEASED' <%--&& reqBasicVm.reqVersion.master.ignoreForRelease != true--%>"
               onaftersave="reqBasicVm.updateRequirement()"
               editable-select="reqBasicVm.reqVersion.assignedToObject"
               title="{{reqBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in reqBasicVm.persons track by person.id">
                {{reqBasicVm.reqVersion.assignedToObject.fullName}}
            </a>
             <span ng-if="reqBasicVm.reqVersion.lifeCyclePhase.phaseType == 'RELEASED' <%--|| reqBasicVm.reqVersion.master.ignoreForRelease == true--%>">
                {{reqBasicVm.reqVersion.assignedToObject.fullName}}
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <workflow-status-settings workflow="reqBasicVm.reqObject"></workflow-status-settings>
            <!-- <span>{{reqBasicVm.reqObject.workflowStatus}}</span> -->
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>PLANNED_FINISH_DATE</span> :
        </div>

        <div class="label col-xs-4 col-sm-3 text-right">

            <div class="input-group">
                <a href="" e-style="width:250px" style="font-size: 17px"
                   ng-if="reqBasicVm.finishDateFlag == false && reqBasicVm.reqVersion.lifeCyclePhase.phaseType != 'RELEASED' <%--&&
                   reqBasicVm.reqVersion.master.ignoreForRelease != true--%>"
                   ng-click="reqBasicVm.changeFinishDate()" placeholder="{{'SET_PLANNED_FINISH_DATE' | translate}}"
                   title="{{'UPDATE_PLANNED_FINISH_DATE' | translate}}">
                    <span ng-bind-html="(reqBasicVm.reqVersion.plannedFinishDate) || 'SET_PLANNED_FINISH_DATE' | translate"></span>

                </a>
                 <span ng-if="reqBasicVm.reqVersion.lifeCyclePhase.phaseType == 'RELEASED' || reqBasicVm.reqVersion.master.ignoreForRelease == true">
                {{reqBasicVm.reqVersion.plannedFinishDate}}
                 </span>


                <div ng-if="reqBasicVm.finishDateFlag == true" class="btn-group" style="display: flex;">
                    <div class="input-group" style="width: 200px;">
                        <input type="text" class="form-control" start-finish-date-picker placeholder="{{'SELECT_PLANNED_FINISH_DATE' | translate}}"
                               ng-model="reqBasicVm.reqVersion.plannedFinishDate"
                               name="attDate">
                                <i class="fa fa-times" ng-if="reqBasicVm.reqVersion.plannedFinishDate != null"
                               style="position: absolute;margin-top: 10px;margin-left: -15px;z-index: 10; cursor: pointer;"
                               ng-click="reqBasicVm.reqVersion.plannedFinishDate = null"></i>
                        <button ng-show="reqBasicVm.finishDateFlag == true"
                                style="margin-left: 10px;"
                                class="btn btn-sm btn-primary"
                                type="button" title="{{'SUBMIT' | translate}}"
                                ng-click="reqBasicVm.updateRequirement()"><i class="fa fa-check"></i>
                        </button>

                        <button ng-show="reqBasicVm.finishDateFlag == true"
                                class="btn btn-sm btn-default"
                                type="button" title="{{'CANCEL' | translate}}"
                                ng-click="reqBasicVm.cancelFinishDate()"><i class="fa fa-times"></i>
                        </button>
                    </div>
                    <!-- <div class="btn-group" style="margin-left: 202px;margin-top: -34px;">
                        <button ng-show="reqBasicVm.finishDateFlag == true"
                                class="btn btn-sm btn-primary"
                                type="button" title="{{'SUBMIT' | translate}}"
                                ng-click="reqBasicVm.updateRequirement()"><i class="fa fa-check"></i>
                        </button>

                        <button ng-show="reqBasicVm.finishDateFlag == true"
                                class="btn btn-sm btn-default"
                                type="button" title="{{'CANCEL' | translate}}"
                                ng-click="reqBasicVm.cancelFinishDate()"><i class="fa fa-times"></i>
                        </button>
                    </div> -->
                </div>
            </div>


        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>Version</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqBasicVm.reqVersion.version}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PRIORITY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <priority object="reqBasicVm.reqVersion"></priority>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>LIFE_CYCLE_PHASE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <item-status item="reqBasicVm.reqVersion"></item-status>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqBasicVm.reqVersion.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqBasicVm.reqVersion.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqBasicVm.reqVersion.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqBasicVm.reqVersion.modifiedDate}}</span>
        </div>
    </div>
    <basic-attribute-details-view object-type="REQUIREMENT"
                                  quality-type="REQUIREMENT"
                                  has-permission="true"
                                  object-id="reqVersion.master.id"></basic-attribute-details-view>
    <object-attribute-details-view object-type="REQUIREMENTDOCUMENTTYPE" show-attributes="true"
                                   actual-object-type="{{reqVersion.master.objectType}}"
                                   has-permission="true"
                                   object-type-id="reqVersion.master.type.id"
                                   object-id="reqVersion.id"></object-attribute-details-view>

</div>

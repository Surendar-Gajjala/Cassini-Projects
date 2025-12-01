<div class="item-details col-sm-12" style="padding: 30px;">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{programBasicVm.program.type.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>NAME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="" e-style="width:250px"
               ng-if="programDetailsPermission"
               onaftersave="programBasicVm.updateProgram()"
               editable-text="programBasicVm.program.name"
               title="{{programBasicVm.nameTooltip}}">
                <span ng-bind-html="programBasicVm.program.name"></span>
            </a>

            <span
                    ng-if="!programDetailsPermission"
                    ng-bind-html="programBasicVm.program.name"></span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href=""
               ng-if="programDetailsPermission"
               onaftersave="programBasicVm.updateProgram()"
               editable-textarea="programBasicVm.program.description"
               title="{{programBasicVm.clickToUpdateDes}}">
                <span ng-bind-html="(programBasicVm.program.description)  || 'CLICK_TO_UPDATE_DESCRIPTION' | translate"
                      title="{{programBasicVm.program.description}}">
                </span>
            </a>
          <span
                  ng-if="!programDetailsPermission"
                  ng-bind-html="programBasicVm.program.description"></span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>PROGRAM_MANAGER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" style="line-height: 20px">
            <a href="#" e-style="width: 50%"
               ng-if="programDetailsPermission"
               onaftersave="programBasicVm.updateProgram()"
               editable-select="programBasicVm.program.programManagerObject"
               title="{{programBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in programBasicVm.persons track by person.id">

                {{programBasicVm.program.programManagerObject.fullName}}
            </a>
            <span
                    ng-if="!programDetailsPermission">
            {{programBasicVm.program.programManagerObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <workflow-status-settings workflow="programBasicVm.program"></workflow-status-settings>
            <!-- <span>{{programBasicVm.program.workflowStatus}}</span> -->
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{programBasicVm.program.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{programBasicVm.program.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{programBasicVm.program.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{programBasicVm.program.modifiedDate}}</span>
        </div>
    </div>

    <object-attribute-details-view object-type="PMOBJECTTYPE"
                                   actual-object-type="PROGRAM"
                                   has-permission="hasPermission('program','edit') || programTeamAccess ==true || (loginPersonDetails.external && sharedProjectPermission == 'WRITE') || loginPersonDetails.person.id == programInfo.programManager"
                                   object-type-id="programBasicVm.program.type.id"
                                   object-id="programBasicVm.programId"></object-attribute-details-view>
</div>




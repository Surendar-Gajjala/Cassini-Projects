<style>
    table {
        table-layout: auto;
    }
</style>
<div class="responsive-table">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 15px;">
                <i class="la la-plus" style="cursor: pointer"
                   title="{{templateTeamVm.addMember}}"
                   ng-disabled="projectPercentComplete == 100"
                   ng-if="hasPermission('project','edit') || (loginPersonDetails.person.id == projectInfo.projectManager && external.external== true)"
                   ng-click="templateTeamVm.addPerson()"></i>
            </th>
            <th translate="NAME"></th>
            <th translate="PHONE_NUMBER"></th>
            <th translate="EMAIL"></th>
            <th translate="ROLE"></th>
            <th class="actions-col">
                <span translate>ACTIONS</span>
                <i class="fa fa-check-circle" ng-click="templateTeamVm.saveAll()"
                   ng-if="templateTeamVm.selectedPersons.length > 1"
                   title="Save"
                   style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                   ng-if="templateTeamVm.selectedPersons.length > 1"
                   ng-click="templateTeamVm.removeAll()" title="Remove"></i>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="templateTeamVm.loading == true">
            <td colspan="11">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5"> {{ 'LOADING_PROJECT_MEMBERS ' | translate }}
                        </span>
            </td>
        </tr>

        <tr ng-if="templateTeamVm.loading == false && templateTeamVm.projectTemplateMembers.content.length == 0">
            <td colspan="11" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/ProjectTeam.png" alt="" class="image">

                    <div class="message" translate>NO_MEMBERS</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="projectPerson in templateTeamVm.projectTemplateMembers.content">
            <td></td>
            <td style="cursor: pointer">{{projectPerson.personObject.fullName}}</td>
            <td>{{projectPerson.personObject.phoneMobile}}</td>
            <td>{{projectPerson.personObject.email}}</td>
            <td style="width: 200px;">
                <span ng-if="projectPerson.editMode != true">{{projectPerson.role}}</span>
                <input ng-if="projectPerson.editMode == true" type=" text" class="form-control input-sm"
                       ng-model="projectPerson.role">
            </td>
            <td class="text-center">
                <span class="btn-group"
                      ng-if="projectPerson.editMode == true"
                      style="margin: -1px">
               <i title="{{ 'SAVE' | translate }}"
                  ng-click="templateTeamVm.saveTeam(projectPerson)"
                  class="la la-check">
               </i>
               <i ng-show="projectPerson.isNew == true"
                  title="{{ 'REMOVE' | translate }}"
                  ng-click="templateTeamVm.onCancel(projectPerson)"
                  class="la la-times">
               </i>
                <i ng-show="projectPerson.isNew == false"
                   title="{{ 'CANCEL_CHANGES' | translate }}"
                   ng-click="templateTeamVm.cancelChanges(projectPerson)"
                   class="la la-times">
                </i>
                </span>
                <span class="row-menu" ng-if="!projectPerson.editMode" uib-dropdown dropdown-append-to-body
                      style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="templateTeamVm.editTeam(projectPerson)"
                                       translate>EDIT</a>
                                </li>
                                <li ng-disabled="projectPercentComplete == 100"
                                    ng-if="hasPermission('project','delete')"
                                    ng-click="templateTeamVm.deleteProjectPerson(projectPerson)"><a href="" translate>DELETE_SPECPERSON</a>
                                </li>
                                <plugin-table-actions context="projectTemplate.team"
                                                      object-value="projectPerson"></plugin-table-actions>
                            </ul>
                        </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
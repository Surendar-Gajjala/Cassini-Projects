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
                   title="{{programResourcesVm.addMember}}"
                   ng-if="programDetailsPermission && hasPermission('program','edit')"
                   ng-click="programResourcesVm.addPerson()"></i>
            </th>
            <th translate="NAME"></th>
            <th translate="PHONE_NUMBER"></th>
            <th translate="EMAIL"></th>
            <th translate="ROLE"></th>
            <!-- <th class="actions-col" translate="ACTIONS"></th> -->
            <th style="width: 100px;text-align: center">
                <span translate>ACTIONS</span>
                <i class="fa fa-check-circle" ng-click="programResourcesVm.saveAll()"
                   ng-if="programResourcesVm.selectedPersons.length > 1"
                   title="Save"
                   style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                   ng-if="programResourcesVm.selectedPersons.length > 1"
                   ng-click="programResourcesVm.removeAll()" title="Remove"></i>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="programResourcesVm.loading == true">
            <td colspan="11">
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                         class="mr5"> {{ 'LOADING_RESOURCES' | translate }}
                </span>
            </td>
        </tr>

        <tr ng-if="programResourcesVm.loading == false &&  selectPersons.content.length == 0">
            <td colspan="11" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/ProjectTeam.png" alt="" class="image">

                    <div class="message" translate>NO_RESOURCES</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="programResource in selectPersons">
            <td></td>
            <td style="cursor: pointer">{{programResource.personObject.fullName}}</td>
            <td>{{programResource.personObject.phoneMobile}}</td>
            <td>{{programResource.personObject.email}}</td>
            <td>
                <span ng-if="!programResource.editMode">{{programResource.role}}</span>
                <input type="text" ng-model="programResource.role" class="form-control"
                       ng-enter="programResourcesVm.saveProgramResource(programResource)"
                       ng-if="programResource.editMode"/>
            </td>
            <td class="text-center">
                <span class="btn-group" style="margin: 0">
                    <i title="{{'SAVE' | translate}}" ng-if="programResource.editMode == true" style="cursor: pointer"
                       ng-click="programResourcesVm.saveProgramResource(programResource)"
                       class="la la-check">
                    </i>
                    <i title="{{'CANCEL' | translate}}"
                       ng-if="programResource.editMode == true && programResource.isNew == true" style="cursor: pointer"
                       ng-click="programResourcesVm.cancelChanges(programResource)"
                       class="la la-times">
                    </i>
                    <i title="{{'CANCEL' | translate}}"
                       ng-if="programResource.editMode == true && programResource.isNew == false"
                       style="cursor: pointer"
                       ng-click="programResourcesVm.cancel(programResource)"
                       class="la la-times">
                    </i>
               
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                      ng-if="!programResource.editMode">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li>
                            <a href=""
                               ng-if="programDetailsPermission"
                               ng-click="programResourcesVm.editProgramResource(programResource)"
                               translate>EDIT_RESOURCE</a>
                            <a href=""
                               ng-if="programDetailsPermission || hasPermission('program','delete')"
                               ng-click="programResourcesVm.deleteProgramResource(programResource)"
                               translate>REMOVE_RESOURCE</a>
                        </li>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
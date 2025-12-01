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
                   title="{{shiftPersonsVm.addMember}}"
                   ng-click="shiftPersonsVm.addShiftPerson()"></i>
            </th>
            <th translate="NAME"></th>
            <th translate="PHONE_NUMBER"></th>
            <th translate="EMAIL"></th>
            <th translate="NOTES"></th>
            <!-- // <th class="actions-col" translate="ACTIONS"></th> -->
            <th style="width: 100px;text-align: center">
                <span translate>ACTIONS</span>
                <i class="fa fa-check-circle" ng-click="shiftPersonsVm.saveAll()"
                   ng-if="shiftPersonsVm.selectedPersons.length > 1"
                   title="Save"
                   style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                   ng-if="shiftPersonsVm.selectedPersons.length > 1"
                   ng-click="shiftPersonsVm.removeAll()" title="Remove"></i>
            </th>

        </tr>
        </thead>
        <tbody>
        <tr ng-if="shiftPersonsVm.loading == true">
            <td colspan="11">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5"> {{ 'LOADING_PROJECT_MEMBERS ' | translate }}
                        </span>
            </td>
        </tr>

        <tr ng-if="shiftPersonsVm.loading == false && selectPersons.content.length == 0">
            <td colspan="11" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/ProjectTeam.png" alt="" class="image">

                    <div class="message" translate>NO_SHIFT_MEMBERS</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="shiftPerson in selectPersons.content">
            <td></td>
            <td style="cursor: pointer">{{shiftPerson.personObject.fullName}}</td>
            <td>{{shiftPerson.personObject.phoneMobile}}</td>
            <td>{{shiftPerson.personObject.email}}</td>
            <td style="width: 200px;">
                <span ng-if="shiftPerson.editMode != true">{{shiftPerson.notes}}</span>
                <input ng-if="shiftPerson.editMode == true" type=" text" class="form-control input-sm"
                       ng-model="shiftPerson.notes">
            </td>
            <td class="text-center">
            <span class="btn-group"
                  ng-if="shiftPerson.editMode == true"
                  style="margin: -1px">
               <i title="{{ 'SAVE' | translate }}"
                  ng-click="shiftPersonsVm.saveShiftPerson(shiftPerson)"
                  class="la la-check">
               </i>
               <i ng-show="shiftPersonsVm.itemFlag == true"
                  title="{{ 'REMOVE' | translate }}"
                  ng-click="shiftPersonsVm.onCancel(shiftPerson)"
                  class="la la-times">
               </i>
                <i ng-show="shiftPersonsVm.itemFlag == false"
                   title="{{ 'CANCEL_CHANGES' | translate }}"
                   ng-click="shiftPersonsVm.cancelChanges(shiftPerson)"
                   class="la la-times">
                </i>
           </span>
           <span class="row-menu"
                 ng-hide="shiftPerson.editMode == true"
                 uib-dropdown
                 dropdown-append-to-body
                 style="min-width: 50px">
               <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
               <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                   style="z-index: 9999 !important;">
                   <li>
                       <a href=""
                          ng-click="shiftPersonsVm.editShiftPerson(shiftPerson)"
                          translate>EDIT</a>
                   </li>

                   <li>
                       <a href=""
                          ng-click="shiftPersonsVm.deleteShiftPerson(shiftPerson)"
                          translate>Remove Person</a>
                   </li>
               </ul>
           </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
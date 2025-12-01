<div class="responsive-table" style="padding: 10px;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th><i class="la la-plus" title={{permissionsVm.personAdd}} style="cursor: pointer"
                   ng-if="(selectedSpecification.createdByObject.id == loginPersonDetails.person.id || hasPermission('admin','all'))&& selectedSpecification.lifecyclePhase.phaseType != 'RELEASED'"
                   ng-click="addPerson()"></i></th>
            <th class="col-width-250" translate>NAME</th>
            <th class="" translate>ALL_PERMISSION</th>
            <th class="" translate>EDIT_PERMISSION</th>
            <th class="" translate>DELETE_PERMISSION</th>
            <th class="" translate>ACCEPT_PERMISSION</th>
            <th class="" translate>STATUS_CHANGE_PERMISSION</th>
            <th class="" translate>IMPORT_PERMISSION</th>
            <th class="" translate>EXPORT_PERMISSION</th>
            <th translate>ACTIONS</th>
        </tr>
        </thead>
        <tr ng-if="permissionsVm.specPersons.length == 0">
            <td colspan="10" translate>NO_PERMISSION</td>
        </tr>
        <tr ng-repeat="spec in permissionsVm.specPersons">
            <td></td>
            <td>
                {{spec.specUserObject.fullName}}
            </td>
            <td style="text-align: center;">
                <input type="checkbox" ng-click="permissionsVm.selectAllPermission(spec)"
                       ng-disabled="(selectedSpecification.createdByObject.id != loginPersonDetails.person.id && !hasPermission('admin','all')) || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                       ng-model="spec.all">
            </td>
            <td style="text-align: center;">
                <input type="checkbox" ng-click="permissionsVm.selectPermission(spec)"
                       ng-disabled="(selectedSpecification.createdByObject.id != loginPersonDetails.person.id && !hasPermission('admin','all')) || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                       ng-model="spec.editPermission">
            </td>
            <td style="text-align: center;">
                <input type="checkbox" ng-click="permissionsVm.selectPermission(spec)"
                       ng-disabled="(selectedSpecification.createdByObject.id != loginPersonDetails.person.id && !hasPermission('admin','all')) || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                       ng-model="spec.deletePermission">
            </td>
            <td style="text-align: center;">
                <input type="checkbox"
                       ng-click="permissionsVm.selectPermission(spec)"
                       ng-disabled="(selectedSpecification.createdByObject.id != loginPersonDetails.person.id && !hasPermission('admin','all')) || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                       ng-model="spec.acceptRejectPermission">
            </td>
            <td style="text-align: center;">
                <input type="checkbox"
                       ng-click="permissionsVm.selectPermission(spec)"
                       ng-disabled="(selectedSpecification.createdByObject.id != loginPersonDetails.person.id && !hasPermission('admin','all')) || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                       ng-model="spec.statusChangePermission">
            </td>
            <td style="text-align: center;">
                <input type="checkbox"
                       ng-click="permissionsVm.selectPermission(spec)"
                       ng-disabled="(selectedSpecification.createdByObject.id != loginPersonDetails.person.id && !hasPermission('admin','all')) || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                       ng-model="spec.importPermission">
            </td>
            <td style="text-align: center;">
                <input type="checkbox"
                       ng-click="permissionsVm.selectPermission(spec)"
                       ng-disabled="(selectedSpecification.createdByObject.id != loginPersonDetails.person.id && !hasPermission('admin','all')) || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                       ng-model="spec.exportPermission">
            </td>
            <td style="width: 50px;" class="text-center">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                             <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-class="{'disabled':selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' || (!hasPermission('admin','all') && selectedSpecification.createdByObject.id != loginPersonDetails.person.id)}"
                                    ng-click="permissionsVm.deleteSpecPerson(spec)">
                                    <a href="" translate>DELETE_SPECPERSON</a>
                                </li>

                            </ul>
                        </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
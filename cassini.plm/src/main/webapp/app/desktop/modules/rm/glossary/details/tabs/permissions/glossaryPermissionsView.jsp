<style scoped>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }

</style>

<div class="responsive-table" style="padding: 10px;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th><i class="la la-plus" title="{{'ADD_PERSON' | translate}}" style="cursor: pointer"
                   ng-if="hasPermission('admin','all') && selectedGlossary.lifeCyclePhase.phaseType != 'RELEASED'"
                   ng-click="addGlossaryPersons()"></i></th>
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
        <tr ng-if="glossaryPermissionVm.glossaryPersons.length == 0">
            <td colspan="10" translate>NO_PERMISSION</td>
        </tr>
        <tr ng-repeat="glossaryPerson in glossaryPermissionVm.glossaryPersons">
            <td></td>
            <td>
                {{glossaryPerson.glossaryUserObject.fullName}}
            </td>
            <td style="text-align: center;">
                <input type="checkbox" ng-disabled="!hasPermission('admin','all')"
                       ng-click="glossaryPermissionVm.selectAllPermission(glossaryPerson)"
                       ng-model="glossaryPerson.all">
            </td>
            <td style="text-align: center;">
                <input type="checkbox" ng-disabled="!hasPermission('admin','all')"
                       ng-click="glossaryPermissionVm.selectPermission(glossaryPerson)"
                       ng-model="glossaryPerson.editPermission">
            </td>
            <td style="text-align: center;">
                <input type="checkbox" ng-disabled="!hasPermission('admin','all')"
                       ng-click="glossaryPermissionVm.selectPermission(glossaryPerson)"
                       ng-model="glossaryPerson.deletePermission">
            </td>
            <td style="text-align: center;">
                <input type="checkbox" ng-disabled="!hasPermission('admin','all')"
                       ng-click="glossaryPermissionVm.selectPermission(glossaryPerson)"
                       ng-model="glossaryPerson.acceptRejectPermission">
            </td>
            <td style="text-align: center;">
                <input type="checkbox" ng-disabled="!hasPermission('admin','all')"
                       ng-click="glossaryPermissionVm.selectPermission(glossaryPerson)"
                       ng-model="glossaryPerson.statusChangePermission">
            </td>
            <td style="text-align: center;">
                <input type="checkbox" ng-disabled="!hasPermission('admin','all')"
                       ng-click="glossaryPermissionVm.selectPermission(glossaryPerson)"
                       ng-model="glossaryPerson.importPermission">
            </td>
            <td style="text-align: center;">
                <input type="checkbox" ng-disabled="!hasPermission('admin','all')"
                       ng-click="glossaryPermissionVm.selectPermission(glossaryPerson)"
                       ng-model="glossaryPerson.exportPermission">
            </td>
            <td class="text-center">
                     <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-click="glossaryPermissionVm.deleteGlossaryPerson(glossaryPerson)"
                                    ng-show="selectedGlossary.lifeCyclePhase.phaseType != 'RELEASED'">
                                    <a href="" translate>DELETE_SPECPERSON</a>
                                </li>
                            </ul>
                         </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>

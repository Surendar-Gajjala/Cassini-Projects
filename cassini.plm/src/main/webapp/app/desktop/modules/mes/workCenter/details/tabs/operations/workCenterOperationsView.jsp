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
                   ng-click="workCenterOperationsVm.addOperations()" title="Add Operations"></i>
            </th>
            <th style="width: 150px" translate>NUMBER</th>
            <th class="col-width-150" translate>TYPE</th>
            <th class="col-width-200" translate>NAME</th>
            <th class="col-width-250" translate>DESCRIPTION</th>
            <th class="col-width-150" translate>MODIFIED_BY</th>
            <th class="col-width-150" translate>MODIFIED_DATE</th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="workCenterOperationsVm.loading == true">
            <td colspan="11">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5"> {{ 'LOADING_OPERATIONS ' | translate }}
                        </span>
            </td>
        </tr>

        <tr ng-if="workCenterOperationsVm.loading == false && workCenterOperationsVm.workcenterOperations.length == 0">
            <td colspan="11" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/ProjectTeam.png" alt="" class="image">

                    <div class="message" translate>NO_OPERATIONS</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="workcenterOperation in workCenterOperationsVm.workcenterOperations">
            <td></td>
            <td style="width: 150px;">
                <a ng-if="!loginPersonDetails.external" href=""
                   ng-click="workCenterOperationsVm.showOperations(workcenterOperation)"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    <span ng-bind-html="workcenterOperation.operationObject.number | highlightText: freeTextQuery"></span>
                </a>
            </td>
            <td class="col-width-150">
                <span ng-bind-html="workcenterOperation.operationObject.type.name | highlightText: freeTextQuery"></span>
            </td>
            <td class="col-width-200">
                <span ng-bind-html="workcenterOperation.operationObject.name | highlightText: freeTextQuery"></span>
            </td>
            <td  class="col-width-250">
                <span ng-bind-html="workcenterOperation.operationObject.description  | highlightText: freeTextQuery"></span>
            </td>
            <td  class="col-width-250">
                <span ng-bind-html="workcenterOperation.modifiedName | highlightText: freeTextQuery"></span>
            </td>
            <td  class="col-width-250">
                <span ng-bind-html="workcenterOperation.operationObject.modifiedDate  | highlightText: freeTextQuery"></span>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li>
                            <a href=""
                               ng-click="workCenterOperationsVm.removeOperation(workcenterOperation)" translate>Remove</a>
                        </li>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
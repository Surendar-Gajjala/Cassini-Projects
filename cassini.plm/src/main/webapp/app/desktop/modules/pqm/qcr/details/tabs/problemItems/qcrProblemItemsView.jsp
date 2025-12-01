<affected-items ng-show="qcrFor == 'PR'" object-id="qcrProblemItemsVm.qcrId" object-type="QCR"
                delete-item="qcrProblemItemsVm.deleteItem"
                has-permission="hasPermission('qcr','edit') && !qcrReleased"></affected-items>
<div class='responsive-table' ng-show="qcrFor == 'NCR'">
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 20px;" ng-if="!qcrReleased && hasPermission('qcr','edit')">
                <i class="la la-plus dropdown-toggle"
                   ng-click="qcrProblemItemsVm.addProblemMaterials()" title="{{addProblemItemTitle}}"
                   style="cursor: pointer"></i>
            </th>
            <th style="width: 1% !important;white-space: nowrap;">
                <span translate>PART_NUMBER</span>
            </th>
            <th style="width: 150px">
                <span translate>PART_NAME</span>
            </th>
            <th>
                <span translate>MANUFACTURER_PART_TYPE</span>
            </th>
            <th>
                <span translate>NCR</span>
            </th>
            <th translate>NOTES</th>
            <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;">
                <span translate>ACTIONS</span>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="qcrProblemItemsVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_RELATED_ITEMS</span>
            </td>
        </tr>
        <tr ng-if="qcrProblemItemsVm.loading == false && qcrProblemItemsVm.problemItems.length == 0">
            <td colspan="12"><span translate>NO_RELATED_ITEMS</span></td>
        </tr>
        <tr ng-repeat="problemItem in qcrProblemItemsVm.problemItems">
            <td ng-if="!qcrReleased"></td>
            <td style="width: 1% !important;white-space: nowrap;">
                <a title="{{clickToShowDetails}}" ng-click="showAffectedItem()"
                   ui-sref="app.mfr.mfrparts.details({mfrId:problemItem.material.manufacturer,manufacturePartId:problemItem.material.id})">
                    {{problemItem.material.partNumber}}
                </a>
            </td>
            <td>
                <span>{{problemItem.material.partName}}</span>
            </td>
            <td>
                <span>{{problemItem.material.mfrPartType.name}}</span>
            </td>
            <td>
                <a ui-sref="app.pqm.ncr.details({ncrId: problemItem.ncr})"
                   ng-click="showProblemReport()"
                   title="{{clickToShowDetails}}">{{problemItem.ncrNumber}}
                </a>
            </td>
            <td class="description-column">
                <span ng-if="!problemItem.editMode">{{problemItem.notes}}</span>
                <input ng-if="problemItem.editMode" class="form-control" ng-model="problemItem.notes"/>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                 <span class="btn-group"
                       ng-if="problemItem.editMode == true"
                       style="margin: 0">
                    <i ng-show="problemItem.isNew == true"
                       title="{{'SAVE' | translate}}"
                       ng-click="qcrProblemItemsVm.save(problemItem)"
                       class="la la-check">
                    </i>
                    <i ng-show="problemItem.isNew == true"
                       title="{{'CANCEL' | translate}}"
                       ng-click="qcrProblemItemsVm.onCancel(problemItem)"
                       class="la la-times">
                    </i>
                      <i ng-show="problemItem.isNew == false"
                         title="{{'SAVE' | translate}}"
                         ng-click="qcrProblemItemsVm.updateItem(problemItem)"
                         class="la la-check">
                      </i>
                     <i ng-show="problemItem.isNew == false"
                        title="{{'CANCEL' | translate}}"
                        ng-click="qcrProblemItemsVm.cancelChanges(problemItem)"
                        class="la la-times">
                     </i>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="problemItem.editMode == false && !qcrReleased && hasPermission('qcr','edit')"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="problemItem.editMode == false"
                            ng-click="qcrProblemItemsVm.editItem(problemItem)">
                            <a translate>EDIT_PART</a>
                        </li>
                        <li ng-if="problemItem.ncr == null"
                            ng-click="qcrProblemItemsVm.deleteItem(problemItem)">
                            <a translate>REMOVE_PART</a>
                        </li>
                        <plugin-table-actions context="object.affected" object-value="problemItem"></plugin-table-actions>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
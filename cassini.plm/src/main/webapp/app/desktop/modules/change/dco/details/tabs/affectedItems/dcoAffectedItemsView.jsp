<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 20px;"
                ng-if="(hasPermission('change','dco','edit') || hasPermission('change','edit')) && !dco.isReleased && dco.statusType != 'REJECTED' && !dco.startWorkflow">
                <i class="la la-plus dropdown-toggle" ng-click="dcoAffectedItemsVm.showDcoItems()"
                   style="cursor: pointer" title="{{addAffectedItemTitle}}"></i>
            </th>
            <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
            <th style="width: 150px;" translate>ITEM_TYPE</th>
            <th style="width: 200px;" class="col-width-250" translate>ITEM_NAME</th>
            <th style="width: 200px;" translate>DCR</th>
            <th style="width: 150px;text-align: center" translate>FROM_REVISION</th>
            <%--<th style="width: 200px" translate>FROM_LIFECYCLE</th>--%>
            <th style="width: 150px;text-align: center" translate>TO_REVISION</th>
            <%--<th style="width: 200px" translate>TO_LIFECYCLE</th>--%>
            <th translate>NOTES</th>
            <th class="actions-col sticky-col sticky-actions-col" style="width: 80px;text-align: center;" translate>
                ACTIONS
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="dcoAffectedItemsVm.items.length == 0">
            <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/affectedItems.png" alt="" class="image">

                    <div class="message">{{ 'NO_ITEMS' | translate}} {{itemsTitle}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="item in dcoAffectedItemsVm.items track by $index">
            <td style="width: 20px;"
                ng-if="(hasPermission('change','dco','edit') || hasPermission('change','edit')) && !dco.isReleased && dco.statusType != 'REJECTED' && !dco.startWorkflow"></td>
            <td style="width: 1% !important;white-space: nowrap;">
                <a href="" ng-click="dcoAffectedItemsVm.showItem(item)"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    <span ng-bind-html="item.itemNumber"></span>
                </a>
            </td>
            <td style="width: 150px;">{{item.itemType}}</td>
            <td class="col-width-200" title="{{item.itemName}}"><span
                    ng-bind-html="item.itemName "></span>
            </td>
            <td style="width: 200px;">
                <span ng-repeat="dcr in item.plmdcrs">
                    <a href="" ng-click="dcoAffectedItemsVm.showDcr(dcr)">{{dcr.crNumber}}<span
                            ng-if="($index + 1) < item.plmdcrs.length"> , </span></a>
                </span>
            </td>
            <td style="width: 150px; text-align: center">
                <a href="" ng-click="dcoAffectedItemsVm.showFromRevision(item)" title="{{clickToShowDetails}}">{{item.fromRevision}}</a>
            </td>
            <%--<td style="width: 200px;">{{item.fromLifeCycle.phase}}</td>--%>
            <td style="width: 150px;text-align: center;">
                <span ng-if="(item.toItem == null || item.toItem == '') || item.editMode">{{item.toRevision}}</span>
                <a href="" ng-if="item.toItem != null && item.toItem != '' && !item.editMode"
                   ng-click="dcoAffectedItemsVm.showToRevision(item)"
                   title="{{clickToShowDetails}}">{{item.toRevision}}</a>
            </td>

            <%--<td style="width: 200px">
              <span ng-if="item.editMode == true">
                  <ui-select ng-model="item.toLifeCycle" theme="bootstrap"
                             style="width:100%">
                      <ui-select-match placeholder="Select">{{$select.selected.phase}}
                      </ui-select-match>
                      <ui-select-choices
                              repeat="phase in item.toLifecyclePhases | filter: $select.search">
                          <div ng-bind="phase.phase"></div>
                      </ui-select-choices>
                  </ui-select>
              </span>
                 <span ng-if="item.editMode != true">
                  {{item.toLifeCycle.phase}}
               </span>
            </td>--%>
            <td class="description-column">
                <span ng-if="item.editMode == true">
                    <input type="text" class="form-control" style="width: 150px;" ng-model="item.notes"/>
                </span>
                <span ng-if="item.editMode == false">
                    {{item.notes}}
                </span>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                 <span class="btn-group" ng-if="item.editMode == true" style="margin: 0">
                    <i ng-show="dcoAffectedItemsVm.itemFlag == true"
                       title="{{'SAVE' | translate}}"
                       ng-click="dcoAffectedItemsVm.save(item)"
                       class="la la-check">
                    </i>
                    <i ng-show="dcoAffectedItemsVm.itemFlag == true"
                       title="{{cancelChangesTitle}}"
                       ng-click="dcoAffectedItemsVm.onCancel(item)"
                       class="la la-times">

                    </i>
                      <i ng-show="dcoAffectedItemsVm.itemFlag == false"
                         title="{{'SAVE' | translate}}"
                         ng-click="dcoAffectedItemsVm.updateItem(item)"
                         class="la la-check">
                      </i>
                     <i ng-show="dcoAffectedItemsVm.itemFlag == false"
                        title="{{cancelChangesTitle}}"
                        ng-click="dcoAffectedItemsVm.cancelChanges(item)"
                        class="la la-times">
                     </i>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="item.editMode == false && (hasPermission('change','dco','edit') || hasPermission('change','edit')) && !dco.startWorkflow && !dco.isReleased && dco.statusType != 'REJECTED'"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="item.editMode == false"
                            ng-click="dcoAffectedItemsVm.editItem(item)">
                            <a translate>EDIT_ITEM</a>
                        </li>
                        <li ng-if="item.editMode == false"
                            title="{{dco.revisionsCreated ? cannotDeleteItem:''}}">
                            <a ng-class="{'disabled': item.plmdcrs.length > 0 || dco.revisionsCreated || dco.startWorkflow}"
                               ng-click="dcoAffectedItemsVm.deleteItem(item)" translate>REMOVE_ITEM</a>
                        </li>
                        <plugin-table-actions context="dco.affected" object-value="item"></plugin-table-actions>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div>
    <style scoped>
        table {
            table-layout: fixed;
        }

        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }
    </style>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 30px;">
                    <i class="la la-plus" title="{{itemMfrVm.addMfrParts}}"
                       ng-if="!itemRevision.released && !itemRevision.rejected && external.external != true && itemMfrVm.lockedOwner"
                       style="cursor: pointer" ng-click="selectMfrParts()"></i>
                    <i class="la la-plus" title="{{itemMfrVm.addMfrParts}}"
                       ng-if="!itemRevision.released && !itemRevision.rejected && sharedPermission == 'WRITE' && external.external == true"
                       style="cursor: pointer" ng-click="selectMfrParts()"></i>
                </th>
                <th style="width: 200px;" translate>PART_NUMBER</th>
                <th class="col-width-250" translate>PART_NAME</th>
                <th style="width: 200px;" translate>MANUFACTURER_PART_TYPE</th>
                <th style="width: 200px;" translate>LIFECYCLE</th>
                <th class="col-width-250" translate>MANUFACTURER</th>
                <th style="width: 200px;" translate>STATUS</th>
                <th style="width: 200px;">Notes</th>
                <th ng-hide="external.external == true && sharedPermission == 'READ'"
                    style="width: 100px;text-align: center">
                    <span translate>ACTIONS</span>
                    <i class="fa fa-check-circle" ng-click="itemMfrVm.saveAll()"
                       ng-if="itemMfrVm.selectedParts.length > 1"
                       title="Save"
                       style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                    <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                       ng-click="itemMfrVm.removeAll()" title="Remove"
                       ng-if="itemMfrVm.selectedParts.length > 1"></i>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="itemMfrVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_MANUFACTURER_PARTS</span>
                </td>
            </tr>
            <tr ng-if="itemMfrVm.loading == false && mfrparts.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/ManufacturerParts.png" alt="" class="image">

                        <div class="message" translate>NO_MANUFACTURER_PARTS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-if="mfrparts.length > 0"
                ng-repeat="mfrpart in mfrparts">
                <td>
                    <a ng-if="itemMfrVm.itemId != mfrpart.item && item.configured" title="{{configurableItemPart}}"
                       class="fa fa-cog"
                       aria-hidden="true"></a>
                </td>
                <td ng-if="external.external == false">
                    <a href="" ng-if="mfrViewPermission"
                       ng-click="itemMfrVm.showMfrPartsDetails(mfrpart)">{{mfrpart.manufacturerPart.partNumber}}</a>

                    <span ng-if="!mfrViewPermission">{{mfrpart.manufacturerPart.partNumber}}</span>
                </td>
                <td ng-if="external.external == true ">
                    <span>{{mfrpart.manufacturerPart.partNumber}}</span>
                    <%--<a href="" ng-click="itemMfrVm.showMfrPartsDetails(mfrpart)">{{mfrpart.manufacturerPart.partNumber}}</a>--%>
                </td>
                <%--<td ng-if="external.external == true && sharedPermission == 'READ'">--%>
                    <%--{{mfrpart.manufacturerPart.partNumber}}--%>
                <%--</td>--%>
                <td class="col-width-250">{{mfrpart.manufacturerPart.partName}}</td>
                <td>{{mfrpart.manufacturerPart.mfrPartType.name}}</td>
                <td class="col-width-250">
                    <item-status item="mfrpart.manufacturerPart"></item-status>
                </td>
                <td class="col-width-250">
                    <span>{{mfrpart.manufacturerPart.mfrName}}</span>
                    <%--<a href="" ng-click="itemMfrVm.showMfr(mfrpart)">
                        <span>{{mfrpart.manufacturerPart.mfrName}}</span>
                    </a>--%>
                </td>
                <td>
                <span ng-if="mfrpart.editMode == true">
                      <ui-select ng-model="mfrpart.status" theme="bootstrap"
                                 style="width:100%">
                          <ui-select-match placeholder="Select">{{$select.selected}}</ui-select-match>
                          <ui-select-choices repeat="status in mfrpart.statuses | filter: $select.search">
                              <div ng-bind="status"></div>
                          </ui-select-choices>
                      </ui-select>
                </span>
                    <span ng-if="mfrpart.editMode != true">
                         <mfr-status mfrpart="mfrpart"></mfr-status>
                    </span>
                </td>
                <td>
                    <span ng-if="mfrpart.editMode == false">
                        <span title="{{mfrpart.notes}}"
                              ng-bind-html="mfrpart.notes ">
                    </span>
                        {{mfrpart.notes.length > 20 ? '...' : ''}}
                    </span>

                 <span ng-if="mfrpart.editMode == true">
                    <input type="text" class="form-control" style="width: 150px;" ng-model="mfrpart.notes"/>
                </span>
                </td>
                <td class="text-center" ng-hide="external.external == true && sharedPermission == 'READ'">
                     <span class="btn-group"
                           ng-if="mfrpart.editMode == true"
                           style="margin: -1px">
                    <i
                        title="{{'SAVE' | translate}}"
                            ng-click="itemMfrVm.createMfrPart(mfrpart)"
                       class="la la-check">
                    </i>
                    <i ng-show="itemMfrVm.itemFlag == true"
                            title="{{cancelChangesTitle}}"
                            ng-click="itemMfrVm.onCancel(mfrpart)"
                       class="la la-times">
                    </i>
                     <i ng-show="itemMfrVm.itemFlag == false"
                             title="{{cancelChangesTitle}}"
                             ng-click="itemMfrVm.cancelChanges(mfrpart)"
                             class="la la-times">
                     </i>
                </span>
                <span class="row-menu" ng-hide="mfrpart.editMode == true" uib-dropdown
                      dropdown-append-to-body
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="mfrpart.editMode == false && external.external == false && deleteItemPermission"
                            title="{{itemMfrVm.itemId != mfrpart.item ? thisIsConfigurableItemPart:''}}">
                            <a href="" ng-click="itemMfrVm.editMfrPart(mfrpart)"
                               ng-class="{'disabled':itemLifeCycleStatus == 'RELEASED' || itemMfrVm.itemId != mfrpart.item
                               || (selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission)}"
                               translate>EDIT_PART</a>
                        </li>
                        <li ng-if="mfrpart.editMode == false && external.external == false && deleteItemPermission"
                            title="{{itemMfrVm.itemId != mfrpart.item ? thisIsConfigurableItemPart:''}}">
                            <a href="" ng-class="{'disabled':itemLifeCycleStatus == 'RELEASED' || itemMfrVm.itemId != mfrpart.item
                               || (selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission)}"
                               ng-click="itemMfrVm.deleteMfrpart(mfrpart)" translate>REMOVE_PART</a>
                        </li>
                        <li ng-if="external.external == true && sharedPermission == 'WRITE'"
                            title="{{itemMfrVm.itemId != mfrpart.item ? thisIsConfigurableItemPart:''}}">
                            <a href=""
                               ng-class="{'disabled':itemLifeCycleStatus == 'RELEASED' || itemMfrVm.itemId != mfrpart.item || (selectedItemInfo.lockObject && loginPersonDetails.person.id != selectedItemInfo.lockedBy.id && !adminPermission)}"
                               ng-click="itemMfrVm.deleteMfrpart(mfrpart)" translate>REMOVE_PART</a>
                        </li>
                        <plugin-table-actions context="item.mfr" object-value="mfrpart"></plugin-table-actions>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
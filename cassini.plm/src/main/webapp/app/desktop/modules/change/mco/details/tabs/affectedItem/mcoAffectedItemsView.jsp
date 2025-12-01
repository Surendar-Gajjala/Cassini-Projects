<div>
    <style scoped>
        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }
    </style>
    <div ng-if="mco.objectType == 'OEMPARTMCO'" class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 20px;"
                    ng-if="(hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED' && !mco.startWorkflow">
                    <i class="la la-plus dropdown-toggle" ng-click="mcoAffectedItemsVm.addAffectedItems()"
                       title="{{addAffectedItemTitle}}" style="cursor: pointer"></i>
                </th>
                <th style="width: 150px;" translate>PART_NUMBER</th>
                <th style="width: 150px" translate>PART_NAME</th>
                <th style="width: 150px;" translate>MANUFACTURER_PART_TYPE</th>
                <th style="width: 150px;" translate>CHANGE_TYPE</th>
                <th translate>REPLACEMENT_PART_NUMBER</th>
                <th translate>REPLACEMENT_PART_NAME</th>
                <th translate>NOTES</th>
                <th style="width: 100px; text-align: center">
                    <span translate>ACTIONS</span>
                    <i class="fa fa-check-circle" ng-click="mcoAffectedItemsVm.saveAll()"
                       ng-if="mcoAffectedItemsVm.selectedItems.length > 1"
                       title="Save"
                       style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                    <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                       ng-click="mcoAffectedItemsVm.removeAll()" title="Remove"
                       ng-if="mcoAffectedItemsVm.selectedItems.length > 1"></i>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="mcoAffectedItemsVm.loading == true">
                <td colspan="15">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">
                        <span translate>LOADING_AFFECTIVE_ITEM</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="mcoAffectedItemsVm.loading == false && mcoAffectedItemsVm.affectedItems.length == 0">
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
            <tr ng-repeat="affectedItem in mcoAffectedItemsVm.affectedItems track by $index">
                <td ng-if="(hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED' && !mco.startWorkflow"></td>
                <td>
                    <a title="{{clickToShowDetails}}"
                       ng-click="showPart()"
                       ui-sref="app.mfr.mfrparts.details({mfrId:affectedItem.mfrId,manufacturePartId:affectedItem.material})">
                        {{affectedItem.materialNumber}}
                    </a>
                </td>
                <td>{{affectedItem.materialName}}</td>
                <td>{{affectedItem.materialType}}</td>
                <td style="width: 150px !important;">
                    <form>
                        <mco-change-type ng-if="!affectedItem.editMode"
                                         type="affectedItem.changeType"></mco-change-type>
                        <ui-select ng-model="affectedItem.changeType" theme="bootstrap" style="width:100%"
                                   ng-if="affectedItem.editMode">
                            <ui-select-match placeholder="Select Change Type">{{$select.selected.label}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="changeType.value as changeType in mcoAffectedItemsVm.changeTypes | filter: $select.search">
                                <div ng-bind="changeType.label"></div>
                            </ui-select-choices>
                        </ui-select>
                    </form>
                </td>
                <td>
                    <span ng-if="affectedItem.changeType == 'REPLACED'">{{affectedItem.replacementNumber}}</span>
                    <i ng-if="affectedItem.editMode && affectedItem.changeType == 'REPLACED'"
                       class="fa fa-pencil-square-o"
                       style="font-size: 14px;padding-left: 10px;"
                       ng-click="mcoAffectedItemsVm.loadReplacementParts(affectedItem)"></i>
                </td>
                <td><span ng-if="affectedItem.changeType == 'REPLACED'">{{affectedItem.replacementName}}</span></td>
                <td class="description-column">
                <span ng-if="affectedItem.editMode == true">
                   <form>
                       <input type="text" class="form-control" ng-model="affectedItem.notes"/>
                   </form>
                </span>
                <span ng-if="affectedItem.editMode == false">
                    {{affectedItem.notes}}
                </span>
                </td>
                <td class="text-center">
                 <span class="btn-group"
                       ng-if="affectedItem.editMode == true"
                       style="margin: 0">
                    <i ng-show="affectedItem.isNew == true"
                       title="{{'SAVE' | translate}}"
                       ng-click="mcoAffectedItemsVm.save(affectedItem)"
                       class="la la-check">
                    </i>
                    <i ng-show="affectedItem.isNew == true"
                       title="{{cancelChangesTitle}}"
                       ng-click="mcoAffectedItemsVm.onCancel(affectedItem)"
                       class="la la-times">
                    </i>
                      <i ng-show="affectedItem.isNew == false"
                         title="{{'SAVE' | translate}}"
                         ng-click="mcoAffectedItemsVm.updateItem(affectedItem)"
                         class="la la-check">

                      </i>
                     <i ng-show="affectedItem.isNew == false"
                        title="{{cancelChangesTitle}}"
                        ng-click="mcoAffectedItemsVm.cancelChanges(affectedItem)"
                        class="la la-times">
                     </i>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="affectedItem.editMode == false && (hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED' && !mco.startWorkflow"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="affectedItem.editMode == false"
                            ng-click="mcoAffectedItemsVm.editItem(affectedItem)">
                            <a translate>EDIT_ITEM</a>
                        </li>
                        <li ng-if="affectedItem.editMode == false && !affectedItem.qcrItem"
                            ng-click="mcoAffectedItemsVm.deleteItem(affectedItem)">
                            <a translate>REMOVE_ITEM</a>
                        </li>
                        <plugin-table-actions context="mco.affected" object-value="affectedItem"></plugin-table-actions>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div ng-if="mco.objectType == 'ITEMMCO'" class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 20px;"
                    ng-if="(hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED' && !mco.startWorkflow">
                    <i class="la la-plus dropdown-toggle" ng-click="mcoAffectedItemsVm.addAffectedMboms()"
                       title="{{addAffectedMBOMTitle}}" style="cursor: pointer"></i>
                </th>
                <th style="width: 150px;" translate>NUMBER</th>
                <th style="width: 150px;" translate>TYPE</th>
                <th style="width: 150px" translate>NAME</th>
                <th style="width: 100px;" translate>DESCRIPTION</th>
                <th style="width: 100px;" translate>ITEM</th>
                <th style="width: 150px;" translate>FROM_REVISION</th>
                <th style="width: 150px;" translate>TO_REVISION</th>
                <th style="width: 150px;" translate>EFFECTIVE_DATE</th>
                <th class="description-column" translate>NOTES</th>
                <th style="width: 100px; text-align: center">
                    <span translate>ACTIONS</span>
                    <i class="fa fa-check-circle" ng-click="mcoAffectedItemsVm.saveAllMboms()"
                       ng-if="mcoAffectedItemsVm.selectedMboms.length > 1"
                       title="Save"
                       style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                    <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                       ng-click="mcoAffectedItemsVm.removeAllMboms()" title="Remove"
                       ng-if="mcoAffectedItemsVm.selectedMboms.length > 1"></i>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="mcoAffectedItemsVm.loading == true">
                <td colspan="15">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">
                        <span translate>LOADING_AFFECTIVE_ITEM</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="mcoAffectedItemsVm.loading == false && mcoAffectedItemsVm.affectedMboms.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/MBOM.png" alt="" class="image">

                        <div class="message">{{ 'NO_MBOMS' | translate}} {{itemsTitle}}</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="affectedMbom in mcoAffectedItemsVm.affectedMboms track by $index">
                <td ng-if="(hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED' && !mco.startWorkflow"></td>
                <td>
                    <a title="{{clickToShowDetails}}"
                       ng-click="mcoAffectedItemsVm.showMbom(affectedMbom)"
                       ui-sref="'app.mes.mbom.details'({mbomId:affectedMbom.mbomId, tab: 'details.basic'})">
                        {{affectedMbom.number}}
                    </a>
                </td>
                <td>{{affectedMbom.type}}</td>
                <td>{{affectedMbom.name}}</td>
                <td class="col-width-250" title="{{affectedMbom.description}}">
                    {{affectedMbom.description}}
                </td>
                <td>{{affectedMbom.itemName}} - {{affectedMbom.itemRevision}}</td>
                <td style="width: 150px; text-align: center">
                    <a href="" ng-click="mcoAffectedItemsVm.showFromRevision(affectedMbom)"
                       title="{{clickToShowDetails}}">{{affectedMbom.fromRevision}}</a>
                </td>
                <td style="width: 150px;text-align: center;">
                    <span ng-if="(affectedMbom.toItem == null || affectedMbom.toItem == '') || item.editMode">{{affectedMbom.toRevision}}</span>
                    <a href="" ng-if="affectedMbom.toItem != null && affectedMbom.toItem != '' && !item.editMode"
                       ng-click="mcoAffectedItemsVm.showToRevision(affectedMbom)"
                       title="{{clickToShowDetails}}">{{affectedMbom.toRevision}}</a>
                </td>
                <td>
                    <span ng-if="affectedMbom.isNew == true || affectedMbom.editMode == true">
                        <input style="margin: 0;width: 125px;" type="text" class="form-control input-sm"
                               placeholder="{{effectiveDatePlaceholder}}" start-date-picker
                               ng-model="affectedMbom.newEffectiveDate" ng-enter="mcoAffectedItemsVm.onOk(mbom)"/>
                        <i class="fa fa-times" ng-if="affectedMbom.newEffectiveDate != null"
                           style="float: right;margin-top: -25px;margin-right: 10px;"
                           ng-click="affectedMbom.newEffectiveDate = null"></i>
                    </span>
                    <span ng-if="affectedMbom.isNew == false && affectedMbom.editMode == false">{{affectedMbom.effectiveDate}}</span>
                </td>
                <td class="description-column">
                    <form>
                        <span ng-if="affectedMbom.isNew == true || affectedMbom.editMode == true">
                            <input autocomplete="off" id="notes" style="margin: 0" type="text"
                                   class="form-control input-sm"
                                   name="notes"
                                   ng-model="affectedMbom.newNotes" ng-enter="mcoAffectedItemsVm.onOk(affectedMbom)">
                        </span>
                        <span ng-if="affectedMbom.isNew == false && affectedMbom.editMode == false">
                            {{affectedMbom.notes}}
                        </span>
                    </form>
                </td>
                <td class="text-center">
                 <span class="btn-group"
                       ng-if="affectedMbom.editMode == true"
                       style="margin: 0">
                    <i ng-show="affectedMbom.isNew == true"
                       title="{{'SAVE' | translate}}"
                       ng-click="mcoAffectedItemsVm.saveMbom(affectedMbom)"
                       class="la la-check">
                    </i>
                    <i ng-show="affectedMbom.isNew == true"
                       title="{{cancelChangesTitle}}"
                       ng-click="mcoAffectedItemsVm.onCancelMbom(affectedMbom)"
                       class="la la-times">
                    </i>
                      <i ng-show="affectedMbom.isNew == false"
                         title="{{'SAVE' | translate}}"
                         ng-click="mcoAffectedItemsVm.updateMbom(affectedMbom)"
                         class="la la-check">

                      </i>
                     <i ng-show="affectedMbom.isNew == false"
                        title="{{cancelChangesTitle}}"
                        ng-click="mcoAffectedItemsVm.cancelMbomChanges(affectedMbom)"
                        class="la la-times">
                     </i>
                 </span>
                 <span class="row-menu" uib-dropdown dropdown-append-to-body
                       ng-if="affectedMbom.editMode == false && (hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED' && !mco.startWorkflow"
                       style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="affectedMbom.editMode == false"
                            ng-click="mcoAffectedItemsVm.editMbom(affectedMbom)">
                            <a translate>EDIT_MBOM</a>
                        </li>
                        <li ng-if="affectedMbom.editMode == false"
                            ng-click="mcoAffectedItemsVm.deleteMbom(affectedMbom)">
                            <a translate>REMOVE_MBOM</a>
                        </li>
                        <plugin-table-actions context="mco.affected" object-value="affectedMbom"></plugin-table-actions>
                    </ul>
                 </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
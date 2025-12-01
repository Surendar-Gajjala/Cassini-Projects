<div class='responsive-table'>
    <style scoped>
        table.highlight-row tbody tr:hover td .label-count {
            background-color: #859cad !important;
            color: #fff;
        }
    </style>

    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 20px;">
                <i class="la la-plus dropdown-toggle" ng-click="customObjectBomVm.addCustomObjects()"
                   title="Add Custom Objects"
                   style="cursor: pointer"></i>
            </th>
            <th style="width: 1% !important;white-space: nowrap;">
                <span ng-if="customObjectBomVm.bomObjects.length > 0 && customObjectBomVm.showExpandAll">
                    <i class="mr5 fa" style="cursor: pointer; font-size: 16px;margin-left: -10px;"
                       title="{{customObjectBomVm.expandedAll ? collapseAll : expandAll}}"
                       ng-class="{'fa-caret-right': customObjectBomVm.expandedAll == false, 'fa-caret-down': customObjectBomVm.expandedAll == true}"
                       ng-click="customObjectBomVm.expandAllBom()"></i>
                </span>
                <span translate>NUMBER</span>
            </th>
            <th class="col-width-150" translate>NAME</th>
            <th class="col-width-150" translate>TYPE</th>
            <th class="col-width-250" translate>DESCRIPTION</th>
            <th class="col-width-100" translate>QUANTITY</th>
            <th class="col-width-200" translate>NOTES</th>
            <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;">
                <span translate>ACTIONS</span>
                <i class="fa fa-check-circle" ng-click="customObjectBomVm.saveAll()"
                   ng-if="customObjectBomVm.addedCustomObjects.length > 1"
                   title="Save"
                   style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                   ng-click="customObjectBomVm.removeAll()" title="Remove"
                   ng-if="customObjectBomVm.addedCustomObjects.length > 1"></i>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="customObjectBomVm.loading == true">
            <td colspan="15">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">
                        <span translate>LOADING_OBJECTS</span>
                    </span>
            </td>
        </tr>
        <tr ng-if="customObjectBomVm.loading == false && customObjectBomVm.bomObjects.length == 0">
            <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/affectedItems.png" alt="" class="image">

                    <div class="message">{{ 'NO_OBJECTS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-if="customObjectBomVm.loading == false"
            ng-repeat="bomObject in customObjectBomVm.bomObjects track by $index">
            <td>
                <i class="la la-plus dropdown-toggle" ng-if="!bomObject.editMode"
                   ng-click="customObjectBomVm.addCustomObjects(bomObject)"
                   title="Add Custom Objects"
                   style="cursor: pointer"></i>
            </td>
            <td style="width: 1% !important;white-space: nowrap;">
                <span class="level{{bomObject.level}}">
                    <i ng-if="bomObject.hasBom && bomObject.isNew == false && bomObject.editMode == false"
                       class="fa"
                       style="cursor: pointer;margin-left: -10px;margin-right: 2px;"
                       title="{{customObjectBomVm.ExpandCollapse}}"
                       ng-class="{'fa-caret-right': (bomObject.expanded == false || bomObject.expanded == null || bomObject.expanded == undefined),
                       'fa-caret-down': bomObject.expanded == true}"
                       ng-click="customObjectBomVm.toggleNode(bomObject)">
                    </i>
                    <a ng-click="customObjectBomVm.showCustomObject(bomObject)"
                       ng-if="bomObject.id != null && bomObject.isNew == false">
                        <span ng-bind-html="bomObject.number | highlightText: freeTextQuerys"></span>
                    </a>
                    <span ng-if="bomObject.isNew">{{bomObject.number}}</span>
                    <span class="label label-default label-count"
                          ng-if="bomObject.count > 0 && bomObject.hasBom && bomObject.isNew == false && bomObject.editMode == false"
                          title="{{customObjectBomVm.ExpandCollapse}}"
                          ng-click="customObjectBomVm.toggleNode(bomObject)"
                          style="font-size: 12px;background-color: #e4dddd;padding: 1px 4px !important;"
                          ng-bind-html="bomObject.count"></span>
                </span>
                <%--<a href="" ng-click="customObjectBomVm.showCustomObject(bomObject)"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    <span ng-bind-html="bomObject.number | highlightText: freeTextQuery"></span>
                </a>--%>
            </td>
            <td class="col-width-150">
                <span ng-bind-html="bomObject.name | highlightText: freeTextQuery"></span>
            </td>
            <td class="col-width-150">
                <span ng-bind-html="bomObject.typeName | highlightText: freeTextQuery"></span>
            </td>
            <td class="col-width-250">
                <span ng-bind-html="bomObject.description | highlightText: freeTextQuery"></span>
            </td>
            <td class="col-width-100">
                <span ng-if="!bomObject.editMode">{{bomObject.quantity}}</span>
                <input ng-if="bomObject.editMode" type="number" class="form-control" ng-model="bomObject.quantity"
                       style="width: 100px;"/>
            </td>
            <td class="col-width-200">
                <span ng-if="!bomObject.editMode">{{bomObject.notes}}</span>
                <input ng-if="bomObject.editMode" type="text" class="form-control" ng-model="bomObject.notes"
                       style="width: 200px;"/>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                 <span class="btn-group" ng-if="bomObject.editMode == true" style="margin: 0">
                    <i ng-show="bomObject.isNew == true"
                       title="{{'SAVE' | translate}}"
                       ng-click="customObjectBomVm.save(bomObject)">
                        <i class="la la-check"></i>
                    </i>
                    <i ng-show="bomObject.isNew == true"
                       title="{{cancelChangesTitle}}"
                       ng-click="customObjectBomVm.onCancel(bomObject)"
                       class="la la-times">
                    </i>
                      <i ng-show="bomObject.isNew == false"
                         title="{{'SAVE' | translate}}"
                         ng-click="customObjectBomVm.updateObject(bomObject)"
                         class="la la-check">
                      </i>
                     <i ng-show="bomObject.isNew == false"
                        title="{{cancelChangesTitle}}"
                        ng-click="customObjectBomVm.cancelChanges(bomObject)"
                        class="la la-times">
                     </i>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="bomObject.editMode == false"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="customObjectBomVm.editObject(bomObject)">
                            <a translate>EDIT_OBJECT</a>
                        </li>
                        <li id="removeAffectedItem" ng-click="customObjectBomVm.removeCustomObjectBom(bomObject)">
                            <a translate>REMOVE_OBJECT</a>
                        </li>
                        <plugin-table-actions context="custom.bom" object-value="bomObject"></plugin-table-actions>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
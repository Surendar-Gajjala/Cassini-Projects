<div>
    <div class='responsive-table' id="whereUsedTable">
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 1% !important;white-space: nowrap;">
                    <i class="mr10 fa" style="cursor: pointer; font-size: 16px;"
                       ng-if="customObjectWhereUsedVm.showExpandAll"
                       title="{{customObjectWhereUsedVm.expandedAll ? collapseAllTitle : expandAllTitle}}"
                       ng-class="{'fa-caret-right': customObjectWhereUsedVm.expandedAll == false, 'fa-caret-down': customObjectWhereUsedVm.expandedAll == true}"
                       ng-click="customObjectWhereUsedVm.expandAllWhereUsed()"></i>
                    <span translate>NUMBER</span>
                </th>
                <th class="col-width-250" translate>NAME</th>
                <th translate>TYPE</th>
                <th class="description-column" translate>DESCRIPTION</th>
                <th style="text-align: center" translate>QUANTITY</th>
                <th class="description-column" translate>NOTES</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="customObjectWhereUsedVm.loading == true">
                <td colspan="9">
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                         class="mr5"><span translate>LOADING_ITEMS</span>
                </span>
                </td>
            </tr>

            <tr ng-if="customObjectWhereUsedVm.loading == false && customObjectWhereUsedVm.bomItems.length == 0">
                <td colspan="11" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/whereUsed.png" alt="" class="image">

                        <div class="message">{{ 'NO_ITEMS' | translate}}</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="item in customObjectWhereUsedVm.bomItems">
                <td style="width: 1% !important;white-space: nowrap;">
                <span class="level{{item.level}}">
                    <i ng-if="item.children.length > 0" class="mr5 fa"
                       style="cursor: pointer;" title="{{customObjectWhereUsedVm.ExpandCollapse}}"
                       ng-class="{'fa-caret-right': (item.expanded == false || item.expanded == null || item.expanded == undefined),
                                  'fa-caret-down': item.expanded == true}"
                       ng-click="customObjectWhereUsedVm.toggleNode(item)"></i>
                    <a ng-if="loginPersonDetails.external == false" href=""
                       ng-click="customObjectWhereUsedVm.whereUsedItem(item)">
                        <span ng-class="{'ml8': item.children.length == 0}">{{item.parent.number}}</span>
                    </a>
                    <span ng-if="loginPersonDetails.external == true" ng-class="{'ml8': item.children.length == 0}">{{item.parent.number}}</span>
                </span>
                </td>
                <td class="col-width-250">{{item.parent.name}}</td>
                <td>{{item.parent.type.name}}</td>
                <td class="description-column" title="{{item.parent.description}}"><span
                        ng-bind-html="item.parent.description "></span>

                </td>
                <td style="text-align: center">{{item.quantity}}</td>
                <td class="description-column">{{item.notes}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
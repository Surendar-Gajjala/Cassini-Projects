<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .tooltip {
        position: relative;
        display: inline-block;
        border-bottom: 1px dotted black; /* If you want dots under the hoverable text */
    }

    .pending-eco-tooltip {
        position: relative;
        display: inline-block;
    }

    .pending-eco-tooltip .pending-eco-tooltip-text {
        visibility: hidden;
        width: 300px;
        background-color: white;
        text-align: left;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        left: 100%;
        opacity: 0;
        transition: opacity 1s;
        padding: 2px;
        border: 1px solid black;
    }

    .pending-eco-tooltip:hover .pending-eco-tooltip-text {
        visibility: visible;
        opacity: 1;
    }

    #rightSidePanelContent {
        overflow: hidden !important;
    }

    .item-selection-table table th {
        position: -webkit-sticky;
        position: sticky;
        top: -1px !important;
        z-index: 5 !important;
    }

    .table-div {
        position: absolute;
        top: 142px !important;
        bottom: 50px !important;
        left: 0;
        right: 0;
    }

    .open > .dropdown-toggle.btn {
        color: #091007 !important;
    }

</style>
<div style="padding: 0px 10px;">
    <%--<h4 class="section-title" style="margin: 0px;" translate>FILTERS</h4>--%>

    <form class="form-inline ng-pristine ng-valid" style="margin: 10px 0px;display: flex;flex-direction: row;">
        <div class="form-group" style="flex-grow: 1;margin-right: 0;">
            <div class="input-group input-group-sm" style="">
                <div class="input-group-btn dropdown" uib-dropdown="" style="">
                    <button uib-dropdown-toggle="" class="btn btn-default dropdown-toggle" type="button"
                            aria-haspopup="true" aria-expanded="false" style="background-color: #fff;">
                        <span translate="" class="ng-scope">Select</span> <span class="caret"
                                                                                style="margin-left: 4px;"></span>
                    </button>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <classification-tree on-select-type="itemsSelectionVm.onSelectType"
                                                 class="ng-isolate-scope">
                                <div class="classification-pane">
                                    <ul id="classificationTree" class="easyui-tree tree">
                                        <li>
                                            <div id="_easyui_tree_1" class="tree-node"><span
                                                    class="tree-hit tree-expanded"></span><span
                                                    class="tree-icon tree-folder tree-folder-open classification-root"></span><span
                                                    class="tree-title">Classification</span></div>
                                            <ul>
                                                <li>
                                                    <div id="_easyui_tree_2" class="tree-node"><span
                                                            class="tree-indent"></span><span class="tree-indent"></span><span
                                                            class="tree-icon tree-file itemtype-node"></span><span
                                                            class="tree-title">Assembly</span></div>
                                                </li>
                                                <li>
                                                    <div id="_easyui_tree_3" class="tree-node"><span
                                                            class="tree-indent"></span><span class="tree-indent"></span><span
                                                            class="tree-icon tree-file itemtype-node"></span><span
                                                            class="tree-title">Document</span></div>
                                                </li>
                                                <li>
                                                    <div id="_easyui_tree_4" class="tree-node"><span
                                                            class="tree-indent"></span><span class="tree-indent"></span><span
                                                            class="tree-icon tree-file itemtype-node"></span><span
                                                            class="tree-title">Drawing</span></div>
                                                </li>
                                                <li>
                                                    <div id="_easyui_tree_5" class="tree-node"><span
                                                            class="tree-indent"></span><span
                                                            class="tree-hit tree-collapsed"></span><span
                                                            class="tree-icon tree-folder itemtype-node"></span><span
                                                            class="tree-title">Part</span></div>
                                                    <ul style="display:none">
                                                        <li>
                                                            <div id="_easyui_tree_6" class="tree-node"><span
                                                                    class="tree-indent"></span><span
                                                                    class="tree-indent"></span><span
                                                                    class="tree-indent"></span><span
                                                                    class="tree-icon tree-file itemtype-node"></span><span
                                                                    class="tree-title">Electrical</span></div>
                                                        </li>
                                                        <li>
                                                            <div id="_easyui_tree_7" class="tree-node"><span
                                                                    class="tree-indent"></span><span
                                                                    class="tree-indent"></span><span
                                                                    class="tree-indent"></span><span
                                                                    class="tree-icon tree-file itemtype-node"></span><span
                                                                    class="tree-title">Mechanical</span></div>
                                                        </li>
                                                        <li>
                                                            <div id="_easyui_tree_8" class="tree-node"><span
                                                                    class="tree-indent"></span><span
                                                                    class="tree-indent"></span><span
                                                                    class="tree-indent"></span><span
                                                                    class="tree-icon tree-file itemtype-node"></span><span
                                                                    class="tree-title">Software</span></div>
                                                        </li>
                                                    </ul>
                                                </li>
                                            </ul>
                                        </li>
                                    </ul>
                                </div>
                            </classification-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       style="background-color: #fff;" ng-model="itemsSelectionVm.selectedType.name" readonly="">

            </div>
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="itemsSelectionVm.itemsFilters.itemNumber"
                   ng-change="itemsSelectionVm.searchFilterItem()"
                   ng-enter="itemsSelectionVm.searchFilterItem()"
                   ng-model-options="{ debounce: 500 }"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                   placeholder="{{'ITEM_NUMBER_TITLE' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="itemsSelectionVm.itemsFilters.itemName"
                   ng-change="itemsSelectionVm.searchFilterItem()"
                   ng-enter="itemsSelectionVm.searchFilterItem()"
                   ng-model-options="{ debounce: 500 }"
                   style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                   placeholder="{{'ITEM_NAME_PLACEHOLDER_TITLE' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">

            <!-- ngIf: itemsSelectionVm.clear == true -->
            <button ng-click="itemsSelectionVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="itemsSelectionVm.clear">Clear
            </button>
            <!-- end ngIf: itemsSelectionVm.clear == true -->
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="" ng-if="itemsSelectionVm.selectionMode != 'substituteItem'">
                    <span style="" translate>SELECTED_ITEMS</span>
                    <span class="badge">{{itemsSelectionVm.selectedItems.length}}</span>
                    <a href="" ng-click="itemsSelectionVm.clearSelection()"
                       ng-if="itemsSelectionVm.selectedItems.length > 0">Clear</a>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="itemsSelectionVm.items.numberOfElements ==0">
                            {{(itemsSelectionVm.pageable.page*itemsSelectionVm.pageable.size)}}
                        </span>

                                    <span ng-if="itemsSelectionVm.items.numberOfElements > 0">
                            {{(itemsSelectionVm.pageable.page*itemsSelectionVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="itemsSelectionVm.items.last ==false">{{((itemsSelectionVm.pageable.page+1)*itemsSelectionVm.pageable.size)}}</span>
                                    <span ng-if="itemsSelectionVm.items.last == true">{{itemsSelectionVm.items.totalElements}}</span>

                                 <span translate> OF </span>
                                {{itemsSelectionVm.items.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10">  Page {{itemsSelectionVm.items.totalElements != 0 ?
                        itemsSelectionVm.items.number+1:0}} <span translate>OF</span> {{itemsSelectionVm.items.totalPages}}</span>
                        <a href="" ng-click="itemsSelectionVm.previousPage()"
                           ng-class="{'disabled': itemsSelectionVm.items.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="itemsSelectionVm.nextPage()"
                           ng-class="{'disabled': itemsSelectionVm.items.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-12 table-div" style="padding:0px; height: auto;overflow: auto;">
            <div class="item-selection-table">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th style="text-align: center;width: 20px !important;">
                            <input ng-if=" itemsSelectionVm.selectionMode != 'substituteItem' && itemsSelectionVm.items.content.length != 0 "
                                   name="item"
                                   type="checkbox" ng-model="itemsSelectionVm.selectAllCheck"
                                   ng-click="itemsSelectionVm.selectAll(check);" ng-checked="check">
                        </th>
                        <th style="width: 1% !important;white-space: nowrap"></th>
                        <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
                        <th style="width: 180px;" translate>ITEM_NAME</th>
                        <th style="width: 150px;" translate>ITEM_TYPE</th>
                        <th style="width: 200px;" translate>DESCRIPTION</th>
                        <th style="width: 70px;text-align: center" translate>REVISION</th>
                        <th style="width: 100px;" translate>LIFECYCLE</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="itemsSelectionVm.loading == true">
                        <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                        </td>
                    </tr>
                    <tr ng-if="itemsSelectionVm.items.content.length == 0 && itemsSelectionVm.loading == false">
                        <td colspan="15" translate>NO_ITEMS</td>
                    </tr>
                    <tr ng-if="itemsSelectionVm.items.content.length > 0 && itemsSelectionVm.loading == false"
                        ng-repeat="item in itemsSelectionVm.items.content">
                        <td style="vertical-align: middle;text-align: center;width: 20px !important;"
                            class="pending-eco-tooltip">
                            <input ng-if="itemsSelectionVm.selectionMode != 'substituteItem'" name="item"
                                   type="checkbox"
                                   ng-model="item.selected" ng-disabled="item.pendingEco || item.alreadyExist"
                                   title="{{item.alreadyExist ? alreadyAddedHasProblemItem :''}}"
                                   ng-click="itemsSelectionVm.selectCheck(item)">

                            <input ng-if="itemsSelectionVm.selectionMode == 'substituteItem'" name="item" type="radio"
                                   ng-model="item.selected"
                                   ng-checked=false ng-click="itemsSelectionVm.selectReplaceItem(item)">

                            <div class="pending-eco-tooltip-text" ng-if="item.pendingEco">
                            <span>{{itemHasPendingChangeOrder}} :
                                <a href="" title="{{clickToShowDetails}}"
                                   ui-sref="app.changes.eco.details({ecoId:item.changeId})">{{item.ecoNumber}}</a>
                            </span>
                            </div>
                        </td>

                        <td>
                            <a ng-if="item.configurable == true" title="{{itemsSelectionVm.configurableItem}}"
                               class="fa fa-cog" aria-hidden="true">
                            </a>
                        </td>
                        <td style="width: 1% !important;white-space: nowrap;">
                            <span class="level{{item.level}}">
                                <i class="fa" style="cursor:pointer;"
                                   ng-class="{'fa-caret-right': (item.expanded == false || item.expanded == null || item.expanded == undefined),'fa-caret-down': item.expanded == true}"
                                   ng-if="item.hasBom && itemsSelectionVm.selectionMode == 'PROBLEMREPORT'"
                                   ng-click="itemsSelectionVm.toggleNode(item)"></i>
                                <span ng-bind-html="item.itemNumber | highlightText: itemsSelectionVm.itemsFilters.itemNumber"></span>
                            </span>
                        </td>
                        <td style="vertical-align: middle;"><span
                                ng-bind-html="item.itemName | limitTo: 17 | highlightText: itemsSelectionVm.itemsFilters.itemName"></span>
                            {{item.itemName.length > 17 ? '...' : ''}}
                        </td>
                        <td style="vertical-align: middle;">
                            <span ng-bind-html="item.itemType.name | highlightText: itemsSelectionVm.itemsFilters.typeName"></span>
                            {{item.itemType.name.length > 17 ? '...' : ''}}
                        </td>
                        <td style="vertical-align: middle;">
                            {{item.description}}
                        </td>
                        <td style="text-align: center">
                        <span ng-if="itemsSelectionVm.selectionMode == 'ecos' || itemsSelectionVm.selectionMode == 'dcos' || itemsSelectionVm.selectionMode == 'ECR' || itemsSelectionVm.selectionMode == 'DCR' || itemsSelectionVm.selectionMode == 'QCR'">
                            <span ng-if="!item.latestRevisionObject.rejected">{{item.latestRevisionObject.revision}}</span>
                            <span ng-if="item.latestRevisionObject.rejected">{{item.latestReleasedRevisionObject.revision}}</span>
                            <span ng-if="item.latestRevisionObject.rejected && (item.latestReleasedRevisionObject == '' || item.latestReleasedRevisionObject == null)">{{item.rev.revision}}</span>
                        </span>
                            <span ng-if="itemsSelectionVm.selectionMode != 'ecos' && itemsSelectionVm.selectionMode != 'dcos' && itemsSelectionVm.selectionMode != 'ECR' && itemsSelectionVm.selectionMode != 'DCR' && itemsSelectionVm.selectionMode != 'PROBLEMREPORT' && itemsSelectionVm.selectionMode != 'QCR'">{{item.latestRevisionObject.revision}}</span>
                            <span ng-if="itemsSelectionVm.selectionMode == 'PROBLEMREPORT'">
                                <span ng-if="itemsSelectionVm.problemReport != null && itemsSelectionVm.problemReport.product != null && itemsSelectionVm.problemReport.product != ''">{{item.asReleasedRevisionObject.revision}}</span>
                                <span ng-if="itemsSelectionVm.problemReport != null && (itemsSelectionVm.problemReport.product == null || itemsSelectionVm.problemReport.product == '')">{{item.latestReleasedRevisionObject.revision}}</span>
                            </span>
                        </td>
                        <td>
                        <span ng-if="itemsSelectionVm.selectionMode == 'ecos' || itemsSelectionVm.selectionMode == 'dcos' || itemsSelectionVm.selectionMode == 'ECR' || itemsSelectionVm.selectionMode == 'DCR' || itemsSelectionVm.selectionMode == 'QCR'">
                            <item-status ng-if="!item.latestRevisionObject.rejected"
                                         item="item.latestRevisionObject"></item-status>
                            <item-status ng-if="item.latestRevisionObject.rejected"
                                         item="item.latestReleasedRevisionObject"></item-status>
                            <item-status
                                    ng-if="item.latestRevisionObject.rejected && (item.latestReleasedRevisionObject == '' || item.latestReleasedRevisionObject == null)"
                                    item="item.rev"></item-status>
                        </span>
                        <span ng-if="itemsSelectionVm.selectionMode != 'ecos' && itemsSelectionVm.selectionMode != 'dcos' && itemsSelectionVm.selectionMode != 'ECR' && itemsSelectionVm.selectionMode != 'DCR' && itemsSelectionVm.selectionMode != 'PROBLEMREPORT' && itemsSelectionVm.selectionMode != 'QCR'">
                            <item-status item="item.latestRevisionObject"></item-status>
                        </span>
                        <span ng-if="itemsSelectionVm.selectionMode == 'PROBLEMREPORT' && problemReport.product != null">
                            <item-status item="item.asReleasedRevisionObject"></item-status>
                        </span>
                        <span ng-if="itemsSelectionVm.selectionMode == 'PROBLEMREPORT' && problemReport.product == null">
                            <item-status item="item.latestReleasedRevisionObject"></item-status>
                        </span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <br>
    <br>
</div>

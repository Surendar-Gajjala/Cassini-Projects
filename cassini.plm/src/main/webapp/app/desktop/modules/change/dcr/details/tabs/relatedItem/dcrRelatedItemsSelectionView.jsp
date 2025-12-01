<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }
</style>
<div style="padding: 0px 10px;">
    <%--<h4 class="section-title" style="margin: 0px;" translate>FILTERS</h4>--%>

    <form class="form-inline ng-pristine ng-valid" style="margin: 10px 0px;display: flex;flex-direction: row;">
        <div class="form-group" style="flex-grow: 1;margin-right: 0;">
            <div class="input-group input-group-sm" style="">
                <div class="input-group-btn dropdown" uib-dropdown="" style="">
                    <button uib-dropdown-toggle="" class="btn btn-default dropdown-toggle" type="button"
                            aria-haspopup="true" aria-expanded="false"
                            style="background-color: #fff;color: #333 !important">
                        <span class="ng-scope" translate>SELECT</span> <span class="caret"
                                                                             style="margin-left: 4px;"></span>
                    </button>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <classification-tree
                                    on-select-type="relatedItemsVm.onSelectType"></classification-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       style="background-color: #fff;" ng-model="relatedItemsVm.selectedType.name" readonly="">

            </div>
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="relatedItemsVm.itemsFilters.itemNumber"
                   ng-change="relatedItemsVm.searchFilterItem()"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;" placeholder="Item Number"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="relatedItemsVm.itemsFilters.itemName"
                   ng-change="relatedItemsVm.searchFilterItem()"
                   style="margin-left: -16px; height: 30px; margin-top: 0px; width: 100%;margin-top: 0;"
                   placeholder="Item Name" class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">

            <!-- ngIf: relatedItemsVm.clear == true -->
            <button ng-click="relatedItemsVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="relatedItemsVm.clear">Clear
            </button>
            <!-- end ngIf: relatedItemsVm.clear == true -->
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_ITEMS</span>
                    <span class="badge">{{relatedItemsVm.selectedItems.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="relatedItemsVm.items.numberOfElements ==0">
                            {{(relatedItemsVm.pageable.page*relatedItemsVm.pageable.size)}}
                        </span>

                                    <span ng-if="relatedItemsVm.items.numberOfElements > 0">
                            {{(relatedItemsVm.pageable.page*relatedItemsVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="relatedItemsVm.items.last ==false">{{((relatedItemsVm.pageable.page+1)*relatedItemsVm.pageable.size)}}</span>
                                    <span ng-if="relatedItemsVm.items.last == true">{{relatedItemsVm.items.totalElements}}</span>

                                 <span translate> OF </span>
                                {{relatedItemsVm.items.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10">  Page {{relatedItemsVm.items.totalElements != 0 ?
                        relatedItemsVm.items.number+1:0}} <span translate>OF</span> {{relatedItemsVm.items.totalPages}}</span>
                        <a href="" ng-click="relatedItemsVm.previousPage()"
                           ng-class="{'disabled': relatedItemsVm.items.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="relatedItemsVm.nextPage()"
                           ng-class="{'disabled': relatedItemsVm.items.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-12" style="padding:0px;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center;width: 20px !important;">
                        <input name="item" type="checkbox" ng-model="relatedItemsVm.selectAllCheck"
                               ng-if="relatedItemsVm.items.content.length != 0"
                               ng-click="relatedItemsVm.selectAll(check);" ng-checked="check">
                    </th>
                    <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
                    <th style="width: 180px;" translate>ITEM_NAME</th>
                    <th style="width: 150px;" translate>ITEM_TYPE</th>
                    <th style="width: 200px;" translate>DESCRIPTION</th>
                    <th style="width: 70px;text-align: center" translate>REVISION</th>
                    <th style="width: 100px;" translate>LIFECYCLE</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="relatedItemsVm.items.content.length == 0">
                    <td colspan="15" translate>NO_ITEMS</td>
                </tr>
                <tr ng-if="relatedItemsVm.items.content.length > 0"
                    ng-repeat="item in relatedItemsVm.items.content">
                    <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                        <input name="item" type="checkbox" ng-model="item.selected"
                               ng-click="relatedItemsVm.selectCheck(item)">
                    </td>

                    <td style="vertical-align: middle;width: 1% !important;white-space: nowrap;">
                        <span ng-bind-html="item.itemNumber | highlightText: relatedItemsVm.itemsFilters.itemNumber"></span>
                    </td>

                    <td style="vertical-align: middle;" title="{{item.itemName}}"><span
                            ng-bind-html="item.itemName | limitTo: 17 | highlightText: relatedItemsVm.itemsFilters.itemName"></span>
                        {{item.itemName.length > 17 ? '...' : ''}}
                    </td>

                    <td style="vertical-align: middle;" title="{{item.itemType.name}}"><span
                            ng-bind-html="item.itemType.name | limitTo: 17 | highlightText: relatedItemsVm.itemsFilters.typeName"></span>
                        {{item.itemType.name.length > 17 ? '...' : ''}}
                    </td>
                    <td style="vertical-align: middle;" title="{{item.description}}">
                        {{item.description}}
                    </td>
                    <td style="text-align: center">
                        {{item.latestRevisionObject.revision}}
                    </td>
                    <td>
                        <item-status item="item.latestRevisionObject"></item-status>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>

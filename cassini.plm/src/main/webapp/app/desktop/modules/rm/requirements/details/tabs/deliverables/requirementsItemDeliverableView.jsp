<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .round1 {
        width: 50%;
        border-radius: 26px;
        padding: 4px 5px 7px 142px;
        top: 0;
        left: 0;
        z-index: 5;
        border: 1px #e6e8ed solid;
        background-color: rgb(241, 243, 244);
        outline: none;
    }

    .round1 > input:focus {
        border: 1px solid #ddd !important;
        box-shadow: none;
    }

    .fa-search {
        position: relative;
        margin-top: 11px;
        color: grey;
        opacity: 0.5;
        margin-right: -28px !important;
    }

    i.fa-times-circle {
        margin-left: -25px;
        color: gray;
        cursor: pointer;
    }


</style>

<div class="responsive-table" style="padding: 10px;">

    <h4 style="margin: 0px;" translate>FILTERS</h4>

    <form class="form-inline" style="margin: 5px 0px;">

        <div class="form-group">
            <div class="input-group">
                <div class="input-group-btn" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                    <button class="btn btn-default dropdown-toggle" uib-dropdown-toggle>
                        <span translate>SELECT</span> <span class="caret" style="margin-left: 4px;"></span>
                    </button>
                    <div uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                         style="z-index: 9999 !important; margin-right: -56px !important;">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <classification-tree
                                    on-select-type="requirementsItemDeliverableVm.onSelectType"></classification-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control" name="title" style="width: 150px;"
                       ng-model="requirementsItemDeliverableVm.selectedType.name" readonly>
            </div>
        </div>

        <div class="form-group">
            <input type="text" ng-model="requirementsItemDeliverableVm.filters.itemNumber"
                   style="width: 165px;"
                   ng-change="requirementsItemDeliverableVm.searchItems()"
                   placeholder="{{'ITEM_NUMBER' | translate}}"
                   class="form-control">
        </div>
        <div class="form-group">
            <input type="text" ng-model="requirementsItemDeliverableVm.filters.name"
                   style="width: 165px;" ng-change="requirementsItemDeliverableVm.searchItems()"
                   placeholder=" {{'ITEM_NAME' | translate}}"
                   class="form-control">
        </div>
        <div class="pull-right" style="margin-top: 5px;">
            <%--<button class="btn btn-success btn-sm"
                    ng-click="requirementsItemDeliverableVm.searchItems()" translate>SEARCH
            </button>--%>
            <button class="btn btn-sm btn-danger" ng-if="requirementsItemDeliverableVm.clear == true"
                    ng-click="requirementsItemDeliverableVm.clearFilter()" translate>CLEAR
            </button>
        </div>
    </form>
    <div class="col-md-12" style="border: 1px solid lightgrey;">
        <div class="pull-right text-center" style="padding: 10px;">
            <div ng-if="requirementsItemDeliverableVm.showItemDeliverable == true">
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        <span translate>DISPLAYING</span> {{requirementsItemDeliverableVm.reqDeliverables.numberOfElements}} <span
                                        translate>OF</span>
                                            {{requirementsItemDeliverableVm.reqDeliverables.totalElements}}<span
                                        translate>AN</span>
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{requirementsItemDeliverableVm.reqDeliverables.totalElements != 0 ? requirementsItemDeliverableVm.reqDeliverables.number+1:0}} <span
                        translate>OF</span> {{requirementsItemDeliverableVm.reqDeliverables.totalPages}}</span>
                <a href="" ng-click="requirementsItemDeliverableVm.previousPage()"
                   ng-class="{'disabled': requirementsItemDeliverableVm.reqDeliverables.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="requirementsItemDeliverableVm.nextPage()"
                   ng-class="{'disabled': requirementsItemDeliverableVm.reqDeliverables.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 30px">
                <div class="ckbox ckbox-default" style="display: inline-block;">
                    <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                           ng-model="requirementsItemDeliverableVm.selectedAll"
                           ng-click="requirementsItemDeliverableVm.checkAll(check)">
                    <label for="item{{$index}}" class="item-selection-checkbox"></label>
                </div>
            </th>
            <th style="width: 1% !important;white-space: nowrap;" translate="ITEM_ALL_ITEM_NUMBER"></th>
            <th style="width: 150px" translate="ITEM_ALL_ITEM_TYPE"></th>
            <th style="width: 150px" translate="ITEM_ALL_ITEM_NAME"></th>
            <th style="width: 200px;" translate="ITEM_ALL_DESCRIPTION"></th>
            <th style="width: 100px; text-align: center;" translate="ITEM_ALL_REVISION"></th>
            <th style="width: 150px" translate="ITEM_ALL_LIFECYCLE"></th>
            <th style="width: 150px" translate="ITEM_ALL_RELEASED_DATE"></th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="requirementsItemDeliverableVm.loading == true">
            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
            </td>
        </tr>

        <tr ng-if="requirementsItemDeliverableVm.loading == false && requirementsItemDeliverableVm.reqDeliverables.content.length == 0">
            <td colspan="25" translate>NO_DELIVERABLES</td>
        </tr>

        <tr ng-repeat="item in requirementsItemDeliverableVm.reqDeliverables.content">
            <td>
                <div class="ckbox ckbox-default" style="display: inline-block;">
                    <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                           ng-model="item.selected" ng-click="requirementsItemDeliverableVm.select(item)">
                    <label for="item{{$index}}" class="item-selection-checkbox"></label>
                </div>
            </td>

            <td style="width: 1% !important;white-space: nowrap;">
                <a href="" ng-click="requirementsItemDeliverableVm.showItem(item)"
                   ng-if="item.configurable == true" title="{{configurableItem}}" class="fa fa-cog">
                </a>
                    <span title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                          ng-bind-html="item.itemNumber | highlightText: freeTextQuery"></span>
            </td>
            <td style="width: 150px"><span
                    ng-bind-html="item.itemType.name | highlightText: freeTextQuery"></span>
            </td>
            <td style="width: 150px"><span ng-bind-html="item.itemName | highlightText: freeTextQuery"></span>
            </td>
            <td id="td" title="{{item.description}}">
                <span ng-bind-html="item.description  | highlightText: freeTextQuery"></span>
            </td>
            <td style="width: 150px; text-align: center;">{{item.latestRevisionObject.revision}}</td>
            <td style="width: 150px">
                <item-status item="item.latestRevisionObject"></item-status>
            </td>
            <td style="width: 150px">
                <span>{{item.latestRevisionObject.releasedDate}}</span>
            </td>

        </tr>
        </tbody>
    </table>
</div>


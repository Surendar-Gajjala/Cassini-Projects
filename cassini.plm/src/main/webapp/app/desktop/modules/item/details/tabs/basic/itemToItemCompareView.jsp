<div>

    <style scoped>

        .formAlign {
            text-align: center !important;
            margin: 0 143px 0 0;
            padding: 11px !important;
        }

        .radio-inline {
            font-weight: bolder !important;
        }

        .searchBar {
            width: 295px;
            border-radius: 15px !important;
            padding-left: 30px;
            height: 30px;
            display: inline-block !important;
            background-color: rgb(241, 243, 244);
            border: 1px solid #ddd;
            box-shadow: none;
        }

        .search-box > input {
            width: 295px;
            border-radius: 15px !important;
            padding-left: 30px !important;
            height: 30px;
            display: inline-block !important;
            background-color: rgb(241, 243, 244);
            border: 1px solid #ddd;
            box-shadow: none;
        }

        .search-box > input:hover {
            background-color: rgb(229, 231, 234);
        }

        .search-box > input:focus {
            box-shadow: none;
        }

        .search-box i.fa-search {
            z-index: 4 !important;
            position: absolute;
            margin-top: 11px;
            margin-left: 10px;
            color: grey;
            opacity: 0.5;
        }

        .search-box i.clear-search {
            margin-left: -27px;
            color: gray;
            cursor: pointer;
            z-index: 4 !important;
            position: absolute;
            margin-top: 12px;
        }

    </style>
    <div class="container" style="width: 100% !important;">
        <div class="text-center" style="margin: 10px 0">
            <div class="search-box" style="position: relative;">
                <i class="fa fa-search"></i>
                <input type="text"
                       class="form-control input-sm"
                       ng-model="itemToItemCompareVm.filters.searchQuery"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="itemToItemCompareVm.loadAllCommonTypeItems()"
                       ng-enter="itemToItemCompareVm.loadAllCommonTypeItems()">
            </div>
        </div>
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-5" style="padding-left: 0;padding-top: 10px;">
                <input type="checkbox"
                       ng-model="itemToItemCompareVm.checked"
                       ng-click="itemToItemCompareVm.itemSearch()">
                <span style="margin-left: 8px;" translate>SHOW_ONLY_LATEST_REVISIONS</span>
            </div>
            <div class="col-md-7" style="padding: 0">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span> {{itemToItemCompareVm.fromItems.numberOfElements}} <span
                                        translate> OF </span> {{itemToItemCompareVm.fromItems.totalElements}} <span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10">
                            Page {{itemToItemCompareVm.fromItems.totalElements != 0 ? itemToItemCompareVm.fromItems.number+1:0}}
                            <span translate>OF</span> {{itemToItemCompareVm.fromItems.totalPages}}</span>
                        <a href="" ng-click="itemToItemCompareVm.previousPage()"
                           ng-class="{'disabled': itemToItemCompareVm.fromItems.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="itemToItemCompareVm.nextPage()"
                           ng-class="{'disabled': itemToItemCompareVm.fromItems.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
        <div class="responsive-table">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th></th>
                    <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
                    <th translate>ITEM_NAME</th>
                    <th style="width: 100px;text-align: center" translate>REVISION</th>
                    <th style="width: 110px;" translate>ITEM_ALL_LIFECYCLE</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="itemToItemCompareVm.filters.searchQuery == '' || itemToItemCompareVm.fromItems.content.length == 0">
                    <td colspan="25" translate>NO_ITEMS</td>
                </tr>
                <tr ng-hide="itemToItemCompareVm.filters.searchQuery == ''"
                    ng-repeat="value in itemToItemCompareVm.fromItems.content">
                    <td>
                        <input type="radio" ng-checked="value.checked" name="value" value="value"
                               ng-click="itemToItemCompareVm.selectedItem(value, $event)">
                    </td>
                    <td style="width: 1% !important;white-space: nowrap;">
                        <span ng-bind-html="value.itemNumber | highlightText: itemToItemCompareVm.filters.searchQuery"></span>
                    </td>
                    <td title="{{value.itemName}}">
                        <span ng-bind-html="value.itemName  | highlightText: itemToItemCompareVm.filters.searchQuery"></span>
                    </td>
                    <td style="text-align: center">{{value.rev.revision}}</td>
                    <td>
                        <item-status item="value.rev"></item-status>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
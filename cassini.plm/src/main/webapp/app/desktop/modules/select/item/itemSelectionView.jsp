<style>
    .search-box > input {
        width: 250px;
        border-radius: 3px !important;
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
        margin-right: -25px !important;
        z-index: 4 !important;
        margin-top: 11px;
        margin-left: 10px;
        color: grey;
        opacity: 0.5;
    }

    .search-box i.clear-search {
        margin-left: -25px;
        cursor: pointer;
        z-index: 4 !important;
        margin-top: 11px;
        font-size: 14px;
        opacity: 0.6;
    }

    #items-table thead th {
        position: sticky;
        top: 33px;
    }

    .sidePanel-content {
        height: 515px !important
    }

</style>
<div class="view-container" fitcontent>
    <div class="sidePanel-content no-padding" style="padding: 10px !important;">
        <div>
            <div class="search-box" style="position: fixed;margin-top: -10px;background: #f9fbfe;width: 620px;">
                <i class="fa fa-search"></i>

                <input type="text" id="freeTextSearchInput"
                       autocomplete="off"
                       class="form-control input-sm"
                       ng-model="itemSelectVm.searchTerm"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="itemSelectVm.freeTextSearch()"
                       ng-init="itemSelectVm.resetPage()"
                       ng-enter="onSearch(searchTerm)">
                <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                   ng-show="itemSelectVm.searchTerm.length > 0 || filterSearch == true"
                   ng-click="itemSelectVm.resetPage()"></i>

                <span style="margin-left: 6px !important;">
                        <medium>
                            <span style="margin-right: 10px;">
                                <span translate>DISPLAYING</span>
                                  <span ng-if="itemSelectVm.items.numberOfElements == 0">
                                    {{(itemSelectVm.pageable.page*itemSelectVm.pageable.size)}}
                                </span>
                                <span ng-if="itemSelectVm.items.numberOfElements > 0">
                                    {{(itemSelectVm.pageable.page*itemSelectVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="itemSelectVm.items.last ==false">{{((itemSelectVm.pageable.page+1)*itemSelectVm.pageable.size)}}</span>
                                <span ng-if="itemSelectVm.items.last == true">{{itemSelectVm.items.totalElements}}</span>
                                 <span translate> OF </span>
                                            {{itemSelectVm.items.totalElements}}
                            </span>
                        </medium>
                    </span>
                     <span class="mr10"> Page {{itemSelectVm.items.totalElements != 0 ?
                     itemSelectVm.items.number+1:0}} <span translate>OF</span> {{itemSelectVm.items.totalPages}}
                    </span>
                <a href="" ng-click="itemSelectVm.previousPage()"
                   ng-class="{'disabled': itemSelectVm.items.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="itemSelectVm.nextPage()"
                   ng-class="{'disabled': itemSelectVm.items.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>

        </div>

        <table id="items-table" class="table table-striped highlight-row" style="margin-top: 40px;">
            <thead>
            <tr>
                <th style="width: 40px" translate>SELECT</th>
                <th style="width: 200px;" translate>ITEM_NUMBER</th>
                <th style="width: 200px;" translate>ITEM_NAME</th>
                <th style="width: 200px;" translate>ITEM_TYPE</th>
                <th style="width: 200px;" translate>DESCRIPTION</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="itemSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5"><span translate>LOADING_ITEMS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="itemSelectVm.loading == false && itemSelectVm.items.content.length == 0">
                <td colspan="12"><span translate>NO_ITEMS</span></td>
            </tr>

            <tr ng-repeat="item in itemSelectVm.items.content" class="highlight-row">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="item.checked" name="item" value="item"
                           ng-click="itemSelectVm.radioChange(item)">
                </td>
                <td><span
                        ng-bind-html="item.itemNumber | highlightText: freeTextQuery"></span>
                </td>
                <td><span
                        ng-bind-html="item.itemName | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 200px;">{{item.itemType.name}}</td>
                <td><span
                        ng-bind-html="item.description | highlightText: freeTextQuery"></span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>



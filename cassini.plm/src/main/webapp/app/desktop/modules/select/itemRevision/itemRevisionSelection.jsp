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

    #revisions-table thead th {
        position: sticky;
        top: 33px;
    }

    .sidePanel-content {
        height: 515px !important;
    }


</style>
<div class="view-container" fitcontent>
    <div class="sidePanel-content no-padding" style="padding: 10px !important;">
        <%--<div style="padding: 10px;">
            <div class="pull-right text-center">
                <span ng-if="itemRevSelectVm.loading == false"><small><span translate>PAGE</span>
                    {{itemRevSelectVm.itemRevison.totalElements != 0 ?
                    itemRevSelectVm.itemRevison.number+1:0}} of
                    {{itemRevSelectVm.itemRevison.totalPages}}

                </small></span>
                <br>

                <div class="btn-group" style="margin-bottom: 0">
                    <button class="btn btn-xs btn-default" ng-click="itemRevSelectVm.previousPage()"
                            ng-disabled="itemRevSelectVm.itemRevisons.first">
                        <i class="fa fa-chevron-left"></i></button>
                    <button class="btn btn-xs btn-default" ng-click="itemRevSelectVm.nextPage()"
                            ng-disabled="itemRevSelectVm.itemRevisons.last">
                        <i class="fa fa-chevron-right"></i></button>
                </div>
                <br>
                <span ng-if="itemRevSelectVm.itemRevisons.totalElements == 1"><small>
                    {{itemRevSelectVm.itemRevisons.totalElements}}
                    <span translate>ITEMS</span>
                </small></span>
                <span ng-if="itemRevSelectVm.itemRevisons.totalElements > 1"><small>
                    {{itemRevSelectVm.itemRevisons.totalElements}}
                    <span translate>ITEM_REVISIONS</span>
                </small></span>
            </div>
        </div>--%>

        <div>
            <div class="search-box" style="position: fixed;margin-top: -10px;background: #f9fbfe;width: 620px;">
                <i class="fa fa-search"></i>

                <input type="text" id="freeTextSearchInput"
                       autocomplete="off"
                       class="form-control input-sm"
                       ng-model="itemRevSelectVm.searchTerm"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="itemRevSelectVm.freeTextSearch()"
                       ng-init="itemRevSelectVm.resetPage()"
                       ng-enter="itemRevSelectVm.freeTextSearch()">
                <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                   ng-show="itemRevSelectVm.searchTerm.length > 0 || filterSearch == true"
                   ng-click="itemRevSelectVm.resetPage()"></i>

                <span style="margin-left: 0px !important;">
                        <medium>
                            <span style="margin-right: 10px;">
                                <span translate>DISPLAYING</span>
                                  <span ng-if="itemRevSelectVm.itemRevisons.numberOfElements == 0">
                                    {{(itemRevSelectVm.pageable.page*itemRevSelectVm.pageable.size)}}
                                </span>
                                <span ng-if="itemRevSelectVm.itemRevisons.numberOfElements > 0">
                                    {{(itemRevSelectVm.pageable.page*itemRevSelectVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="itemRevSelectVm.itemRevisons.last ==false">{{((itemRevSelectVm.pageable.page+1)*itemRevSelectVm.pageable.size)}}</span>
                                <span ng-if="itemRevSelectVm.itemRevisons.last == true">{{itemRevSelectVm.itemRevisons.totalElements}}</span>
                                 <span translate> OF </span>
                                            {{itemRevSelectVm.itemRevisons.totalElements}}
                            </span>
                        </medium>
                    </span>
                     <span class="mr10"> Page {{itemRevSelectVm.itemRevisons.totalElements != 0 ?
                     itemRevSelectVm.itemRevisons.number+1:0}} <span translate>OF</span> {{itemRevSelectVm.itemRevisons.totalPages}}
                    </span>
                <a href="" ng-click="itemRevSelectVm.previousPage()"
                   ng-class="{'disabled': itemRevSelectVm.itemRevisons.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="itemRevSelectVm.nextPage()"
                   ng-class="{'disabled': itemRevSelectVm.itemRevisons.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>

        </div>

            <table id="revisions-table" class="table table-striped highlight-row" style="margin-top: 40px;">
            <thead>
            <tr>
                <th style="width: 40px" translate>SELECT</th>
                <th style="width: 200px;" translate>ITEM_NUMBER</th>
                <th style="width: 200px;" translate>ITEM_NAME</th>
                <th style="width: 200px;" translate>ITEM_TYPE</th>
                <th style="width: 100px;text-align: center" translate>REVISION</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="itemRevSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5"><span translate>LOADING_ITEMS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="itemRevSelectVm.loading == false && itemRevSelectVm.itemRevisons.content.length == 0">
                <td colspan="12"><span translate>NO_ITEMS</span></td>
            </tr>

            <tr ng-repeat="item in itemRevSelectVm.itemRevisons.content"
                ng-click="item.checked = !item.checked; itemRevSelectVm.radioChange(item, $event)"
                ng-dblclick="item.checked = !item.checked; itemRevSelectVm.selectRadioChange(item, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="item.checked" name="item" value="item"
                           ng-click="itemRevSelectVm.radioChange(item, $event)"
                           ng-dblclick="itemRevSelectVm.selectRadioChange(item, $event)">
                </td>
                <td style="width: 200px;">
                    {{item.itemMasterObject.itemNumber}}
                </td>
                <td style="width: 200px;">
                    {{item.itemMasterObject.itemName}}
                </td>
                <td style="width: 200px;">{{item.itemMasterObject.itemType.name}}</td>
                <td style="width: 100px;text-align: center;">{{item.revision}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>



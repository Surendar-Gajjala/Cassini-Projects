<style>
    #td {
        display: run-in;
        word-wrap: break-word;
        width: 300px;
        white-space: normal;
        text-align: left;
    }

    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">Items</span>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="itemsVm.showNewItem()">New Item</button>
            <div class="btn-group">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span>Search</span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="itemsVm.itemSearch()"><a href="">Simple</a></li>
                    <li ng-click="itemsVm.advancedSearch()"><a href="">Advanced</a></li>
                </ul>
            </div>

            <%--<div class="btn-group" uib-dropdown>
                <button uib-dropdown-toggle class="btn btn-sm btn-warning dropdown-toggle" type="button">
                    Add to Folder <span class="caret"></span>
                </button>
                <div class="dropdown-menu" role="menu" style="min-height: 100px;">
                    <div>
                        <folder-tree on-select-folder="onSelectFolder"></folder-tree>
                    </div>
                </div>
            </div>--%>
        </div>

        <free-text-search on-clear="itemsVm.resetPage" on-search="itemsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="items{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                   ng-model="item.selected" ng-click="itemsVm.selectAll()">
                            <label for="items{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th style="width: 150px">Item Number</th>
                    <th style="width: 150px">Item Type</th>
                    <th style="">Description</th>
                    <th style="width: 100px">Revision</th>
                    <th style="width: 100px">Units</th>
                    <th style="width: 150px">Created By</th>
                    <th style="width: 150px">Created On</th>
                    <th style="width: 150px">Modified By</th>
                    <th style="width: 150px">Modified On</th>
                    <th style="width: 150px; text-align: center">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="itemsVm.loading == true">
                    <td colspan="15">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading items...
                        </span>
                    </td>
                </tr>

                <tr ng-if="itemsVm.loading == false && itemsVm.items.content.length == 0">
                    <td colspan="15">No items</td>
                </tr>

                <tr ng-repeat="item in itemsVm.items.content">
                    <td>
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                   ng-model="item.selected" ng-click="itemsVm.toggleSelection(item)">
                            <label for="item{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>

                    <td style="width: 150px">
                        <a href="" ng-click="itemsVm.showItem(item)">{{item.itemNumber}}</a>
                    </td>
                    <td style="width: 150px">{{item.itemType.name}}</td>
                    <td id="td">{{item.description}}</td>
                    <td style="width: 100px">{{item.revision}}</td>
                    <td style="width: 100px">{{item.units}}</td>
                    <td style="width: 150px">{{item.createdByObject.firstName}}</td>
                    <td style="width: 150px">{{item.createdDate}}</td>
                    <td style="width: 150px">{{item.modifiedByObject.firstName}}</td>
                    <td style="width: 150px">{{item.modifiedDate}}</td>
                    <td style="width: 150px; text-align: center">
                        <button title="Delete this item" class="btn btn-xs btn-danger"
                                ng-click="itemsVm.deleteItem(item)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="itemsVm.items.totalElements ==0">
                            {{(itemsVm.pageable.page*itemsVm.pageable.size)}}
                        </span>
                        <span ng-if="itemsVm.items.totalElements > 0">
                            {{(itemsVm.pageable.page*itemsVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="itemsVm.items.last ==false">{{((itemsVm.pageable.page+1)*itemsVm.pageable.size)}}</span>
                        <span ng-if="itemsVm.items.last == true">{{itemsVm.items.totalElements}}</span>


                        <span>of</span>
                        {{itemsVm.items.totalElements}}</h5>
                </div>


                <div class="text-right">
                    <span style="margin-right: 5px;margin-left: 10px;">Page</span>
                    {{itemsVm.items.totalElements != 0 ? itemsVm.items.number+1:0}}
                    <span>of</span>{{itemsVm.items.totalPages}}
                    <a href="" ng-click="itemsVm.previousPage()"
                       ng-class="{'disabled': itemsVm.items.first}"><i class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="itemsVm.nextPage()"
                       ng-class="{'disabled': itemsVm.items.last}"><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>

<style scoped>
    #td {
        display: run-in;
        word-wrap: break-word;
        width: 300px;
        white-space: normal;
        text-align: left;
    }

    .description {
        display: run-in;
        word-wrap: break-word;
        width: 300px;
        white-space: normal !important;
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
        right: 0 !important;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button ng-if="hasPermission('permission.items.new')" class="btn btn-sm btn-success"
                    ng-click="itemsVm.showNewItem()">New Item
            </button>
        </div>
        <free-text-search on-clear="itemsVm.resetPage" search-term="itemsVm.searchText"
                          on-search="itemsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="padding: 10px;overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Nomenclature</th>
                    <th>Item Code</th>
                    <th>Type</th>
                    <th class="description">Description</th>
                    <th>Units</th>
                    <th>Revision</th>
                    <th>Material</th>
                    <th>Drawing Number</th>
                    <th style="width: 70px;">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="itemsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Items...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="freeTextQuery == null && itemsVm.loading == false && itemsVm.items.content.length == 0">
                    <td colspan="25">No Items</td>
                </tr>

                <tr ng-if="freeTextQuery != null && itemsVm.loading == false && itemsVm.items.content.length == 0">
                    <td colspan="25">No Search result found</td>
                </tr>

                <tr ng-repeat="item in itemsVm.items.content">
                    <td>
                        <a href="" ng-click="itemsVm.showItem(item)" title="Click to show details">
                            <span ng-bind-html="item.itemName | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td><span ng-bind-html="item.itemCode | highlightText: freeTextQuery"></span></td>
                    <td><span ng-bind-html="item.itemType.name | highlightText: freeTextQuery"></span></td>
                    <td class="description"><span
                            ng-bind-html="item.description | highlightText: freeTextQuery"></span></td>
                    <td>{{item.itemType.units}}</td>
                    <td>{{item.latestRevisionObject.revision}}</td>
                    <td>{{item.material}}</td>
                    <td>{{item.latestRevisionObject.drawingNumber}}</td>
                    <td style="width: 70px">
                        <div class="btn-group">
                            <button title="Delete Item" ng-disabled="item.bomItems.length > 0 || item.createdAsBomItem"
                                    class="btn btn-xs btn-danger"
                                    ng-click="itemsVm.deleteItem(item)">
                                <i class="fa fa-trash"></i>
                            </button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%--<h5>Displaying {{itemsVm.items.numberOfElements}} of {{itemsVm.items.totalElements}}</h5>--%>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="itemsVm.items.totalElements == 0">
                            {{(itemsVm.pageable.page*itemsVm.pageable.size)}}
                        </span>
                        <span ng-if="itemsVm.items.totalElements > 0">
                            {{(itemsVm.pageable.page*itemsVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="itemsVm.items.last == false">{{((itemsVm.pageable.page+1)*itemsVm.pageable.size)}}</span>
                        <span ng-if="itemsVm.items.last == true">{{itemsVm.items.totalElements}}</span>


                        <span> of </span>{{itemsVm.items.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span class="mr10">Page {{itemsVm.items.totalElements != 0 ? itemsVm.items.number+1:0}} of {{itemsVm.items.totalPages}}</span>
                    <a href="" ng-click="itemsVm.previousPage()"
                       ng-class="{'disabled': itemsVm.items.first}"><i class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="itemsVm.nextPage()"
                       ng-class="{'disabled': itemsVm.items.last}"><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>
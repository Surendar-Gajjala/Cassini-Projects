<style>

    .multiselect-parent .dropdown-toggle {
        width: 270px;
    }

    .multiselect-parent .dropdown-menu {
        width: 270px;
        height: 300px !important;
    }

</style>
<div class="view-container" fitcontent>


    <style scoped>
        .active-item {
            background-image: -webkit-linear-gradient(top, #337ab7 0, #2b669a 100%);
            background-image: -o-linear-gradient(top, #337ab7 0, #2b669a 100%);
            background-image: -webkit-gradient(linear, left top, left bottom, from(#337ab7), to(#2b669a));
            background-image: linear-gradient(to bottom, #337ab7 0, #2b669a 100%);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff337ab7', endColorstr='#ff2b669a', GradientType=0);
            background-repeat: repeat-x;
            border-color: #2b669a;
            color: #fff !important;
        }

        .fixed-left:after {
            content: '';
            /*width: 0;*/
            height: 100%;
            position: absolute;
            border: 1px solid #eeeeee;
            top: 0;
            background: #EEE;
            left: 310px; /* Same as left component width */
            width: 5px;
        }

        .search-form {
            height: 30px;
            border-radius: 15px;
            border: 1px solid lightgrey;
        }
    </style>


    <div class="view-toolbar">
        <div class="col-md-5 col-sm-8 btn-group" style="margin-bottom: 0px;">
            <button class="btn btn-sm btn-success min-width"
                    ng-click="storageVm.onSave()">Save
            </button>

            <button class="btn btn-sm btn-primary" ng-click="showValidateStorageView()" title="">
                Validate Storage
            </button>
        </div>

        <h4 class="col-md-5 col-sm-8" style="margin-bottom: 0px;">{{storeType}}</h4>
    </div>

    <div class="view-content no-padding" ng-show="storageVm.isTree" style="overflow-y: auto;padding: 10px;">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="newWarehouse" ng-click="storageVm.newWarehouse()">New WareHouse</a>
                </li>
                <li><a tabindex="-1" href="" id="newStockRoom" ng-click="storageVm.newStockRoom()">New StockRoom</a>
                </li>
                <%--<li><a tabindex="-1" href="" id="newOnHoldStorage" ng-click="storageVm.newOnHoldStorage()">
                    New On Hold Store</a>
                </li>
                <li><a tabindex="-1" href="" id="newReturnStorage" ng-click="storageVm.newReturnStorage()">
                    New Return Store</a>
                </li>--%>
            </ul>
        </div>

        <div id="whcontextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="whStockRoom" ng-click="storageVm.newStockRoom()">New StockRoom</a></li>
                <li><a tabindex="-1" href="" id="whArea" ng-click="storageVm.newArea()">New Area</a></li>
                <li><a tabindex="-1" href="" id="whRack" ng-click="storageVm.newRack()">New Rack</a></li>
                <%--<li><a tabindex="-1" href="" id="whShelf" ng-click="storageVm.newShelf()">New Shelf</a></li>--%>
                <%--<li><a tabindex="-1" href="" id="whBin" ng-click="storageVm.newBin()">New Bin</a></li>--%>
                <li><a tabindex="-1" href="" id="delWarehouse" ng-click="storageVm.deleteStorageType()">{{storageVm.deleteWarehouseTitle}}</a>
                </li>
            </ul>
        </div>

        <div id="strcontextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="strArea" ng-click="storageVm.newArea()">New Area</a></li>
                <li><a tabindex="-1" href="" id="strRack" ng-click="storageVm.newRack()">New Rack</a></li>
                <%--<li><a tabindex="-1" href="" id="strShelf" ng-click="storageVm.newShelf()">New Shelf</a></li>--%>
                <li><a tabindex="-1" href="" id="strBin" ng-click="storageVm.newBin()">New Bin</a></li>
                <li><a tabindex="-1" href="" id="delStockroom" ng-click="storageVm.deleteStorageType()">Delete
                    StockRoom</a></li>
            </ul>
        </div>

        <div id="areacontextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="subArea" ng-click="storageVm.newArea()">New Sub Area</a></li>
                <li><a tabindex="-1" href="" id="arRack" ng-click="storageVm.newRack()">New Rack</a></li>
                <%--<li><a tabindex="-1" href="" id="arShelf" ng-click="storageVm.newShelf()">New Shelf</a></li>--%>
                <li><a tabindex="-1" href="" id="arBin" ng-click="storageVm.newBin()">New Bin</a></li>
                <li><a tabindex="-1" href="" id="delArea" ng-click="storageVm.deleteStorageType()">Delete Area</a></li>
            </ul>
        </div>

        <div id="rackcontextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="rcShelf" ng-click="storageVm.newShelf()">New Shelf</a></li>
                <%--<li><a tabindex="-1" href="" id="rcBin" ng-click="storageVm.newBin()">New Bin</a></li>--%>
                <li><a tabindex="-1" href="" id="delRack" ng-click="storageVm.deleteStorageType()">Delete Rack</a></li>
            </ul>
        </div>


        <div id="shelfcontextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="shBin" ng-click="storageVm.newBin()">New Bin</a></li>
                <li><a tabindex="-1" href="" id="delShelf" ng-click="storageVm.deleteStorageType()">Delete Shelf</a>
                </li>
            </ul>
        </div>

        <div id="bincontextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="delBin" ng-click="storageVm.deleteStorageType()">Delete Bin</a></li>
            </ul>
        </div>

        <div class="split-pane fixed-left">
            <div class="split-pane-component" style="width: 310px; padding: 5px;">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <div>
                        <input type="search" class="form-control input-sm search-form" ng-model="storageVm.searchValue"
                               ng-change="storageVm.searchTree()" placeholder="Search"/>
                    </div>
                    <div class="pull-right" style="margin-right: 30px;">
                        <i class="fa fa-plus-square" title="Expand"
                           ng-if="(storageVm.lastSelectedStorage != null && storageVm.lastSelectedStorage.children != undefined
                                  && storageVm.lastSelectedStorage.children.length > 0 && storageVm.lastSelectedStorage.state == 'closed')
                                   || ((storageVm.lastSelectedStorage.type == 'RETURN' || storageVm.lastSelectedStorage.type == 'ONHOLD')
                                        && storageVm.lastSelectedStorage.state == 'closed')"
                           ng-click="storageVm.expandAll()"
                           style="font-size: 20px;cursor: pointer;color: green"></i>
                        <i class="fa fa-minus-square" title="Collapse"
                           ng-if="(storageVm.lastSelectedStorage != null && storageVm.lastSelectedStorage.children != undefined
                                  && storageVm.lastSelectedStorage.children.length > 0 && storageVm.lastSelectedStorage.state == 'open')
                                   || ((storageVm.lastSelectedStorage.type == 'RETURN' || storageVm.lastSelectedStorage.type == 'ONHOLD')
                                        && storageVm.lastSelectedStorage.state == 'open')"
                           ng-click="storageVm.collapseAll()"
                           style="font-size: 20px;cursor: pointer;color: cadetblue;"></i>
                    </div>
                    <ul id="storageTree" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <%--<div class="split-pane-divider"></div>--%>

            <div id="storageDetailsView" class="split-pane-component split-right-pane noselect" style="left: 300px;">
                <div ng-include="'app/desktop/modules/storage/details/storageDetailsView.jsp'"
                     ng-controller="StorageDetailsController as storageDetailsVm"></div>
            </div>
        </div>
    </div>
</div>
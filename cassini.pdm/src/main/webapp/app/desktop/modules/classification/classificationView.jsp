<div class="view-container" fitcontent>
    <style>
        .search-form {
            border-radius: 30px;
        }
    </style>

    <div class="view-content no-padding">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" id="addType" href="" ng-click="classVm.addItemType()">Add Type</a></li>
                <li><a tabindex="-1" id="deleteType" href="" ng-click="classVm.deleteItemType()">Delete Type</a></li>
            </ul>
        </div>

        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane">
                <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">Classification</span>

                <div style="height: 40px;">
                    <input type="search" class="form-control input-sm search-form" placeholder="Search"
                           ng-model="classVm.searchValue" ng-change="vaultVm.searchTree()">
                </div>
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <ul id="classificationTree" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane noselect">
                <div ng-include="'app/desktop/modules/classification/details/itemTypeDetailsView.jsp'"
                     ng-controller="ItemTypeDetailsController as itemTypeVm"></div>
            </div>
        </div>
    </div>
</div>

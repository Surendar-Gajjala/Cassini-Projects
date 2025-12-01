<div class="view-container" fitcontent>

    <style scoped>
        .search-form {
            height: 30px;
            border-radius: 15px;
            border: 1px solid lightgrey;
        }

        .classification-pane .tree-node {
            height: auto !important;
        }

        .classification-pane .tree-node .tree-title {
            height: auto !important;
            font-size: 14px;
            width: 180px !important;
            text-align: left;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>

    <div class="view-content  no-padding" style="overflow-y: auto;padding: 10px;">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" id="addType" href="" ng-click="classVm.addType()">
                    <span>Add Type</span></a></li>
                <li><a tabindex="-1" id="deleteType" href="" ng-click="classVm.deleteType()">
                    <span>Delete Type</span></a>
                </li>
                <li><a tabindex="-1" id="importTypes" href="" ng-click="classVm.importClassification()">
                    <span>Import Classification</span></a>
                </li>
            </ul>
        </div>

        <input type="file" style="display:none"
               id="fileUpload" name='file' custom-on-change="uploadFile"/>

        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <div>
                        <input type="search" class="form-control input-sm search-form" placeholder="Search"
                               ng-model="classVm.searchValue" ng-change="classVm.searchTree()">
                    </div>
                    <ul id="classificationTree" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane noselect" style="left:300px;overflow: hidden">
                <div ng-include="'app/desktop/modules/classification/details/itemTypeDetailsView.jsp'"
                     ng-controller="ItemTypeDetailsController as itemTypeVm"></div>
            </div>
        </div>
    </div>
</div>

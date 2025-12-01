<div class="view-container" fitcontent>
    <div class="view-content no-padding">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" ng-click="classVm.addFolder()">Add Folder</a></li>
                <li><a tabindex="-1" href="" ng-click="classVm.deleteItemFolder()">Delete Folder</a></li>
            </ul>
        </div>

        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <ul id="classificationTree" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane noselect">
                <%--<div ng-include="'app/desktop/modules/classification/details/itemTypeDetailsView.jsp'"
                     ng-controller="ItemTypeDetailsController as itemTypeVm"></div>--%>
            </div>
        </div>
    </div>
</div>

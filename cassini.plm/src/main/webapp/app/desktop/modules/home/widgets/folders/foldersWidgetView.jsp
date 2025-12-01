<div style="height: 100%">
    <style scoped>

        #foldersTree {
            padding-top: 10px;
        }

        .folder-item {
            background: transparent url("app/assets/images/gear_wheel.png") no-repeat !important;
            height: 16px;
        }

        .folder-change {
            background: transparent url("app/assets/images/admin_edit.gif") no-repeat !important;
            height: 16px;
        }

        .folder-mfr {
            background: transparent url("app/assets/images/mfr.png") no-repeat !important;
            height: 16px;
        }

        .items-loading {
            background: transparent url("app/assets/bower_components/cassini-platform/images/loaders/loader2.gif") no-repeat !important;
            height: 16px;
        }

        #foldersTree .tree-file + span {
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }

        .folders-panel .context-menu {
            z-index: 9999 !important;
        }

        .modal {
            z-index: 9999 !important;
        }

        .app-side-panel {
            z-index: 9998 !important;
        }
    </style>
    <div class="home-widget-panel folders-panel" style="height: 100%">

        <div id="foldersContextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999" ng-show="hasPermission('folder', 'create') || hasPermission('folder', 'delete')">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li id="addFolderMenuItem"><a tabindex="-1" href=""  ng-click="foldersVm.addFolder()" translate
                        ng-show="hasPermission('folder', 'create')">ADD_FOLDER</a>
                </li>
                <li id="deleteFolderMenuItem"><a tabindex="-1" href="" ng-click="foldersVm.deleteFolder()" translate
                        ng-show="hasPermission('folder', 'delete')">
                    DELETE_FOLDER</a></li>
                <li id="removeItemMenuItem"><a tabindex="-1" href="" ng-click="foldersVm.removeItemFromFolder()"
                                               translate>REMOVE_ITEM</a></li>

            </ul>
        </div>
        <%-- <div class="home-widget-panel-header">
            <span class="panel-header-title">Folders</span>
        </div>--%>
        <div class="home-widget-panel-body">
            <div id="classificationContainer" class="tree-pane" data-toggle="context" data-target="#context-menu">
                <ul id="foldersTree" class="easyui-tree">
                </ul>
            </div>

        </div>
    </div>
</div>

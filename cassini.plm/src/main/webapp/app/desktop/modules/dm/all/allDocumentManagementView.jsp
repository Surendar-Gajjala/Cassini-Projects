<div>
    <style scoped>
        .view-content {
            position: relative;
        }

        .view-content .responsive-table {
            padding: 10px;
            position: absolute;
            bottom: 0px;
            top: 0;
            overflow: auto;
        }

        .view-content .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        .select-item td {
            background-color: #ddd !important;
            /*color: white !important;*/
        }

        .split-pane-component .responsive-table {
            padding: 0;
            position: absolute;
            bottom: 5px;
            top: 0;
            overflow: auto;
        }

        .split-pane-component .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
            /*background-color: #fff;*/
        }

        .split-pane-component .table-footer {
            padding: 0 10px 0 10px;
            position: absolute;
            bottom: 0px !important;
            height: 40px;
            border-top: 1px solid #D3D7DB;
            display: table;
        }

        .split-pane-component .table-footer > div {
            display: table-row;
            line-height: 30px;
        }

        .split-pane-component .table-footer > div h5 {
            margin: 0;
        }

        .split-pane-component .table-footer > div > div {
            display: table-cell;
            vertical-align: middle;
        }

        .split-pane-component .table-footer > div > div > i {
            font-size: 16px;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        #freeTextSearchDirective {
            top: 7px !important;
        }

        .table-striped > tbody > tr:nth-child(2n) > td.actions-col {
            /*background-color: #fff;*/
        }

        .table-striped > tbody > tr:nth-child(2n):hover > td.sticky-col {
            background-color: #d6e1e0;
        }

        #freeTextSearchDirective ::-webkit-input-placeholder {
            /**/
            font-weight: bold;
        }

        #freeTextSearchDirective :-moz-placeholder {
            /**/
            font-weight: bold;
        }

        #classificationContainer .search-input i.fa-search {
            position: absolute;
            margin-top: 13px;
            margin-left: 10px;
            color: grey;
            opacity: 0.5;
            font-size: 12px;
        }

        #classificationContainer .search-input .search-form {
            padding-left: 25px;
            padding-right: 25px;
        }

        .search-form {
            border-radius: 3px;
            background-color: #eaeaea;
            border: 1px solid #fff;
        }

        .search-form:focus {
            box-shadow: none;
            border: 1px solid #c5cfd5;
        }

        #classificationContainer .search-input .search-form {
            padding-left: 25px;
            padding-right: 25px;
        }

        i.clear-search {
            margin-left: -24px;
            color: #aab4b7;
            cursor: pointer;
            z-index: 4 !important;
            position: absolute;
            margin-top: 12px;
        }

        .bom-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .bom-model .configurationEditor-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .dm-header {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        .dm-content {
            padding: 10px;
            overflow: auto;
            min-width: 100%;
            width: auto;
            height: calc(100% - 100px);
        }

        .dm-bottom {
            padding: 5px;
            border-top: 1px solid lightgrey;
            height: 50px;
        }

        .config-close {
            position: absolute;
            right: 35px;
            top: 25px;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .config-close:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .config-close:before, .config-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .config-close:before {
            transform: rotate(45deg) !important;
        }

        .config-close:after {
            transform: rotate(-45deg) !important;
        }

        .configuration-header {
            font-weight: bold;
            font-size: 22px;
            display: inline-block;
            margin-top: 7px;
        }

        .dm-content table th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
        }
        .config-close-btn {
            position: absolute;
            right: 50px;
            top: 7px;
            width: 32px;
            height: 32px;
            opacity: 0.3;
        }

        .config-close-btn:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .config-close-btn:before, .config-close-btn:after {
            position: absolute;
            top: 9px;
            left: 15px;
            content: ' ';
            height: 15px;
            width: 2px;
            background-color: #333;
        }

        .config-close-btn:before {
            transform: rotate(45deg) !important;
        }

        .config-close-btn:after {
            transform: rotate(-45deg) !important;
        }
        #configuration-error {
            display: none;
            padding: 11px !important;
            margin-bottom: 0 !important;
            width: 100%;
            height: 50px;
            float: left;
            position: relative;
        }
        #documentFolderTree{
            position: absolute;
            left: 0px;
            right: 0px;
            top: 60px;
            bottom: 0;
            overflow-y: auto;
        }

    </style>
    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">Document Management</span>

            <div class="btn-group">
                <button class="btn btn-default"
                        ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                        ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                    <i class="fa fa-copy" style=""></i>
                </button>
                <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="fa fa-copy" style=""></span><span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" style="left: auto;right: -100px;">
                        <li ng-click="clearAndCopyObjectFilesToClipBoard()">
                            <a href="" translate="">CLEAR_AND_ADD_FILES</a></li>
                        <li ng-click="copyObjectFilesToClipBoard()">
                            <a href=""><span translate>ADD_TO_EXISTING_FILES</span>
                                ({{clipBoardObjectFiles.length}})</a>
                        </li>
                    </ul>
                </div>
                <button ng-if="allDocumentManagementVm.allDocumentsCount > 0"
                        title="{{downloadTitle}}"
                        class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                    <i class="fa fa-download" aria-hidden="true" style=""></i>
                </button>
            </div>
            <free-text-search search-term="freeTextQuerys" on-clear="allDocumentManagementVm.resetPage"
                              on-search="allDocumentManagementVm.freeTextSearch"></free-text-search>
        </div>
        <div class="view-content no-padding">
            <div id="foldersContextMenu" class="context-menu dropdown clearfix"
                 style="position: fixed;display:none; z-index: 9999">
                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                    <li id="addFolderMenuItem">
                        <a tabindex="-1" href="" ng-click="allDocumentManagementVm.addFolder()" translate>ADD_FOLDER</a>
                    </li>
                    <li id="deleteFolderMenuItem">
                        <a tabindex="-1" href="" ng-click="allDocumentManagementVm.deleteFolder()" translate>
                            DELETE_FOLDER</a>
                    </li>
                    <li id="addPermission">
                        <a tabindex="-1" href="" ng-click="allDocumentManagementVm.showDMPermissions()" translate>
                            PERMISSIONS</a>
                    </li>

                    <li id="shareFolder">
                        <a tabindex="-1" href="" ng-click="allDocumentManagementVm.shareFolder()" translate>
                            SHARE_FOLDER</a>
                    </li>
                </ul>
            </div>
            <div class="split-pane fixed-left">
                <div class="split-pane-component split-left-pane"
                     style="padding: 0;min-width: 250px;max-width: 250px;overflow: auto;">
                    <div id="classificationContainer" class="classification-pane" data-toggle="context"
                         data-target="#context-menu">
                        <div class="search-input" style="height: 30px;margin: 10px 10px 20px 10px;width: 230px;">
                            <i class="fa fa-search"></i>
                            <input type="search" class="form-control input-sm search-form"
                                   placeholder={{searchTitle}}
                                   ng-model="allDocumentManagementVm.searchValue"
                                   ng-change="allDocumentManagementVm.searchTree()">
                            <i class="las la-times-circle clear-search"
                               ng-show="allDocumentManagementVm.searchValue.length > 0"
                               ng-click="allDocumentManagementVm.searchValue = '';allDocumentManagementVm.searchTree()"></i>
                        </div>
                        <ul id="documentFolderTree" class="easyui-tree classification-tree">
                        </ul>
                    </div>
                </div>
                <div class="split-pane-divider" style="left: 250px;z-index: 1;">
                </div>
                <div class="split-pane-component split-right-pane noselect"
                     style="left:250px;padding: 0;overflow-y: hidden;">
                    <object-files-view ng-if="!allDocumentManagementVm.loading" object-id="selectedFolderId"
                                       object-type="DOCUMENT" has-permission="true">
                    </object-files-view>

                </div>
            </div>
        </div>
    </div>

    <div id="dm-permissions" class="bom-model modal" ng-show="allDocumentManagementVm.dmPermissionView">
        <div class="configurationEditor-content">
            <div id="configuration-error" class="alert {{notificationBackground}} animated">
                <span style="margin-right: 10px;"><i class="fa {{notificationClass}}"></i></span>
                 <a href="" class="config-close-btn" ng-click="closeErrorNotification()"></a>
              {{message}}
             </div>
            <div class="dm-header">
                <span class="configuration-header" translate>PERMISSIONS_FOR_FOLDER</span>&nbsp;<span class="configuration-header">/{{allDocumentManagementVm.folderName}}</span>
                <a href="" ng-click="allDocumentManagementVm.hideDMPermissions()" class="config-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="dm-content" style="padding: 5px;overflow: auto;">
                <table class="table table-striped highlight-row">
                    <thead>
                        <tr>
                            <th></th>
                            <th style="text-align: center;z-index: auto !important;" scope="col"
                                ng-repeat="privilege in allDocumentManagementVm.dmPrivileges">
                                <span>{{privilege}}</span>
                                <%--<div style="display: inline-block;">
                                    <input type="checkbox" ng-click="allDocumentManagementVm.toggleDMPrivileges(privilege)"/>
                                </div>--%>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr ng-repeat="dmPermissions in allDocumentManagementVm.dmPermissionsTableList">
                            <td scope="row">
                                <span>{{dmPermissions.role.name}}</span>
                            </td>
                            <td style="text-align: center;" ng-repeat="privilege in dmPermissions.privilege">
                                <input type="checkbox"
                                       ng-model="privilege.checked"
                                       ng-click="allDocumentManagementVm.toggleDMPermission(privilege, dmPermissions.role)"/>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="dm-bottom">
                <div style="margin-top: 10px;margin-left: 10px;">
                    <input type="checkbox" ng-model="allDocumentManagementVm.isSubFolder"/>
                    <span translate>APPLY_FOR_SUB_FOLDERS</span>
                </div>
                <button class="btn btn-sm btn-success ng-scope pull-right" style="margin-top: -28px;"
                        ng-click="allDocumentManagementVm.saveDMPermissions()" translate>SAVE</button>
            </div>
        </div>
    </div>
</div>
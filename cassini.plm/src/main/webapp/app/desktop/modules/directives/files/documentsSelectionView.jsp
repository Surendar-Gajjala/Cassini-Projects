<div style="height: 100%;">
    <style scoped>
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

        #freeTextSearchInput {
            padding-left: 30px;
        }

        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -5px;
        }
    </style>
    <div class="split-pane fixed-left">
        <div class="split-pane-component split-left-pane"
             style="padding: 0;min-width: 230px;max-width: 230px;overflow: auto;">
            <div id="classificationContainer" class="classification-pane" data-toggle="context"
                 data-target="#context-menu">
                <div class="search-input" style="height: 30px;margin: 10px auto;width: 200px;">
                    <i class="fa fa-search"></i>
                    <input type="search" class="form-control input-sm search-form"
                           placeholder={{searchTitle}}
                           ng-model="documentsSelectionVm.searchValue"
                           ng-change="documentsSelectionVm.searchTree()">
                    <i class="las la-times-circle clear-search"
                       ng-show="documentsSelectionVm.searchValue.length > 0"
                       ng-click="documentsSelectionVm.searchValue = '';documentsSelectionVm.searchTree()"></i>
                </div>
                <ul id="documentFolderTree" class="easyui-tree classification-tree">
                </ul>
            </div>
        </div>
        <div class="split-pane-divider" style="left: 230px;">
        </div>
        <div class="split-pane-component split-right-pane noselect"
             style="left:230px;padding: 0;overflow-y: hidden;">
            <div style="height: 40px">
                <free-text-search search-term="freeTextQuerys" on-clear="documentsSelectionVm.resetPage"
                                  on-search="documentsSelectionVm.freeTextSearch"></free-text-search>
            </div>
            <div class="responsive-table" style="height: calc(100% - 40px);overflow-y: auto;">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th style="text-align: center;z-index: 11;"
                            class="select-input-box sticky-col sticky-actions-col">
                            <input name="document" type="checkbox" ng-model="documentsSelectionVm.selectAllCheck"
                                   ng-click="documentsSelectionVm.selectAll(check)" ng-checked="check">
                        </th>
                        <th class="col-width-250 document-name sticky-col sticky-actions-col" style="z-index: 11;"
                            translate>TAB_FILES_FILE_NAME
                        </th>
                        <th class="col-width-100" translate>TAB_FILES_FILE_SIZE</th>
                        <th class="description-column" translate>DESCRIPTION</th>
                        <th style="text-align: center" translate>TAB_FILES_VERSION</th>
                        <th style="text-align: center" translate>LIFECYCLE</th>
                        <th translate>MODIFIED_DATE</th>
                        <th translate>MODIFIED_BY</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="documentsSelectionVm.loading == true">
                        <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DOCUMENTS</span>
                        </span>
                        </td>
                    </tr>
                    <tr ng-if="documentsSelectionVm.documents.length == 0 && documentsSelectionVm.loading == false">
                        <td colspan="15" translate>NO_DOCUMENTS</td>
                    </tr>
                    <tr ng-if="documentsSelectionVm.documents.length > 0 && documentsSelectionVm.loading == false"
                        ng-repeat="document in documentsSelectionVm.documents">
                        <td style="vertical-align: middle;text-align: center;z-index: 12;"
                            class="select-input-box sticky-col sticky-actions-col">
                            <input name="document" type="checkbox" ng-model="document.selected"
                                   ng-click="documentsSelectionVm.selectCheck(document)">
                        </td>

                        <td class="col-width-250 document-name sticky-col sticky-actions-col" style="z-index: 12;">
                            <span class="imageTooltip level{{document.level}}" ng-show="document.fileType == 'FILE'">
                                    <span ng-if="hasDownload">
                                <a href="" ng-click="downloadFile(document)"
                                   title="{{document.name}} - {{downloadFileTitle}}">
                                    <span ng-bind-html="document.name | highlightText: documentsSelectionVm.filters.searchQuery"></span>
                                </a>
                                </span>

                                <span ng-if="!hasDownload">
                                    <span ng-bind-html="document.name | highlightText: documentsSelectionVm.filters.searchQuery"></span>
                                </span>
                            </span>
                        </td>
                        <td>
                            <span ng-if="document.fileType == 'FILE'">{{document.size.toFileSize()}}</span>
                        </td>
                        <td class="description-column">
                            <span ng-if="document.fileType == 'FILE'">{{document.description}}</span>
                        </td>
                        <td style="text-align: center">
                            <span ng-if="document.fileType == 'FILE'">{{document.revision}}.{{document.version}}</span>
                        </td>
                        <td style="text-align: center">
                            <item-status item="document"></item-status>
                        </td>
                        <td>{{document.modifiedDate}}</td>
                        <td>{{document.modifiedByObject.fullName}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
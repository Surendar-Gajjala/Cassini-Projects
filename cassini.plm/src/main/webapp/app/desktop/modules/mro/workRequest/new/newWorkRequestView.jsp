<div style="position: relative;">
    <style scoped>
        .files-preview {
            display: flex;
            flex-wrap: wrap;
            align-items: flex-start;
        }

        .files-preview .dz-preview {
            flex: 1 0 0;
            white-space: nowrap;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 3px;
            margin: 10px;
        }

        .files-preview .dz-preview .progress {
            height: 5px;
            margin-bottom: 0;
        }

        .workrequests-file-dropzone {
            min-height: 72px;
            border: 1px dashed #b6b6b6;
            border-radius: 5px;
            margin: 2px 0px 20px 1px;
            cursor: pointer;
        }

        .workrequests-file-dropzone i.la-close {
            margin-top: 5px;
            padding: 5px;
            font-size: 14px;
            position: absolute;
            right: 28px;
        }

        .workrequests-file-dropzone i.la-close:hover {
            background-color: #ddd;
            border-radius: 50%;
            font-weight: 600;
        }

        .workrequests-file-dropzone .drop-files-label {
            font-style: italic;
            text-align: center;
            line-height: 72px
        }

    </style>
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORK_REQUEST_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span id="Select" translate>SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">

                                            <manufacturing-type-tree
                                                    on-select-type="newWorkRequestVm.onSelectType"
                                                    object-type="WORKREQUESTTYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newWorkRequestVm.type.name" readonly>

                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newWorkRequestVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newWorkRequestVm.workRequest.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ASSET</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newWorkRequestVm.workRequest.asset" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newWorkRequestVm.selectAssetTitle}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="asset.id as asset in newWorkRequestVm.assets | filter: $select.search">
                                    <div>{{asset.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_NAME' | translate}}"
                                   name="title" ng-model="newWorkRequestVm.workRequest.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newWorkRequestVm.workRequest.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REQUESTOR</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newWorkRequestVm.workRequest.requestor" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newWorkRequestVm.persons | filter: $select.search">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NOTES</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_NOTES_TITLE' | translate}}"
                                      ng-model="newWorkRequestVm.workRequest.notes"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PRIORITY</span>:
                        </label>

                        <div class="col-sm-7">
                            <div class="switch-toggle switch-candy">
                                <input id="priorityL" name="priority" type="radio" value="LOW" checked
                                       ng-model="newWorkRequestVm.workRequest.priority">
                                <label for="priorityL">LOW</label>

                                <input id="priorityM" name="priority" type="radio" value="MEDIUM"
                                       ng-model="newWorkRequestVm.workRequest.priority">
                                <label for="priorityM">MEDIUM</label>

                                <input id="priorityH" name="priority" type="radio" value="HIGH"
                                       ng-model="newWorkRequestVm.workRequest.priority">
                                <label for="priorityH">HIGH</label>

                                <input id="priorityC" name="priority" type="radio" value="CRITICAL"
                                       ng-model="newWorkRequestVm.workRequest.priority">
                                <label for="priorityC">CRITICAL</label>
                                <a></a>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ATTACHMENTS</span>
                            :</label>

                        <div class="col-sm-7">
                            <div id="workReqFiles" class="workrequests-file-dropzone"
                                 ng-click="newWorkRequestVm.selectWrFiles()"
                                 ng-if="newWorkRequestVm.showFilesDropZone">
                                <%--<i class="la la-close" title="Close" ng-click="newWorkRequestVm.loadDropZoneFiles()"></i>--%>

                                <div class="drop-files-label">{{"DROP_FILES_OR_CLICK" | translate}}</div>

                                <div id="fileUploadPreviews" class="files-preview">
                                    <div class="dz-preview dz-file-preview" id="fileUploadTemplate">
                                        <div class="dz-details">
                                            <div class="dz-filename"><span data-dz-name></span></div>
                                            <div class="dz-size" data-dz-size></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newWorkRequestVm.attributes.length > 0"
                                     attributes="newWorkRequestVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>

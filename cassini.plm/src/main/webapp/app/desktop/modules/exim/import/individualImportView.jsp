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

        .import-file-dropzone {
            min-height: 72px;
            border: 1px dashed #b6b6b6;
            border-radius: 5px;
            margin: 0px 4px 5px 71px;
            cursor: pointer;
            width: 82%;
        }

        .import-file-dropzone i.la-close {
            margin-top: 5px;
            padding: 5px;
            font-size: 14px;
            position: absolute;
            right: 46px;
        }

        .import-file-dropzone i.la-close:hover {
            background-color: #ddd;
            border-radius: 50%;
            font-weight: 600;
        }

        .import-file-dropzone .drop-files-label {
            font-style: italic;
            text-align: center;
            line-height: 72px
        }

        .import-loading {
            text-align: center !important;
        }

        .import-container {
            margin: 8px 0 0 -29px !important;
        }

        .import-container .files-preview .dz-preview .progress {
            height: 5px;
            margin-bottom: 0;
        }

    </style>
    <div style="padding: 10px">
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <div id="import-container" class="import-container">
            <form class="form-horizontal">
                <div class="row form-group">
                    <label class="col-sm-4 control-label">
                        <span translate>Import Object Type</span>
                        <span class="asterisk"></span> : </label>

                    <div class="col-sm-8">
                        <ui-select ng-model="individualImportVm.objectType" theme="bootstrap"
                                   style="width:100%" on-select="individualImportVm.selectObject($item)">
                            <ui-select-match placeholder="Select import object type">
                                {{$select.selected.label}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="obj in individualImportVm.objectTypes | filter: $select.search.label">
                                <div ng-bind-html="obj.label"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>


                <div class="row form-group" style="margin-top: 10px !important;" ng-if="individualImportVm.subTypeFlag ==true">
                    <label class="col-sm-4 control-label">
                        <span translate>Sub Type</span>
                        <span class="asterisk"></span> : </label>

                    <div class="col-sm-8">
                        <ui-select ng-model="individualImportVm.subType" theme="bootstrap"
                                   style="width:100%" on-select="individualImportVm.selectObjectSubTypes($item)">
                            <ui-select-match placeholder="Select import object subtype">
                                {{$select.selected.label}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="obj in individualImportVm.subTypes | filter: $select.search.label">
                                <div ng-bind-html="obj.label"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <br>
                <div id="import-file" class="import-file-dropzone"
                     ng-click="selectFiles()"
                     ng-if="individualImportVm.showFilesDropZone && individualImportVm.enableTemplateLink">
                    <%-- <i class="la la-close" title="Close" ng-click="individualImportVm.closeDropZone()"></i>--%>

                    <div class="drop-files-label">{{"DROP_FILES_OR_CLICK" | translate}}</div>

                    <div id="fileUploadPreviews" class="files-preview">
                        <div class="dz-preview dz-file-preview" id="fileUploadTemplate" style="display: none">
                            <div class="dz-details">
                                <div class="dz-filename"><span data-dz-name></span></div>
                                <div class="dz-size" data-dz-size></div>
                                <div class="dz-progress"><span class="dz-upload" data-dz-uploadprogress></span>
                                </div>
                                <div class="progress progress-striped active" style="display: none;">
                                    <div class="progress-bar"
                                         role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                         aria-valuemax="100" style="width: 100%">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="import-loading" ng-if="individualImportVm.enableTemplateLink">
                    <a ng-click="individualImportVm.downloadObjectTemplates()">
                        <i class="fa fa-download" style="font-size: 15px"></i>
                        <span class="hidden-xs hidden-sm">Download Template</span>
                    </a>
                </div>
            </form>


        </div>
        <div class="row" style="margin: 0;">
            <div ng-if="individualImportVm.headers.length > 0">
                <div style="padding-top: 20px" class="col-md-12">
                    <div class="col-md-5">
                        <label class="control-label"><span style="font-weight: bold;font-size: 17px" translate>APPLICATION_COLUMN</span></label>
                    </div>
                    <div class="col-md-6">
                        <label class="control-label"><span style="font-weight: bold;font-size: 17px" translate>IMPORTED_FILE_COLUMN</span></label>
                    </div>
                    </br></br>
                </div>
                <div style="padding-top: 20px" class="col-md-12" ng-repeat="header in individualImportVm.headerObjs">
                    <div class="col-md-5">
                        <label ng-if="header.mapHeader == ''" class="control-label"><span style="font-weight: bold">{{header.header}}</span></label>
                        <label ng-if="header.mapHeader != ''"
                               class="control-label"><span>{{header.header}}</span></label>
                        <span> : </span></label>
                    </div>
                    <div class="col-md-6">
                        <ui-select ng-model="header.mapHeader"
                                   theme="bootstrap"
                                   style="width:95%">
                            <ui-select-match placeholder="Select Mapping Header">{{$select.selected}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="header in individualImportVm.headers1 track by $index">
                                <div ng-bind="header"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                    </br></br>
                </div>
            </div>
        </div>
    </div>
</div>
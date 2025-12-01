<div style="padding: 20px;">
    <style scoped>
        .selected-item-details {

        }

        .selected-item-details img.thumbnail{
            width: 100%;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        .selected-item-details .section-heading {
            font-size: 18px;
            font-weight: 400;
            padding: 10px 0;
            border-bottom: 1px solid #ddd;
            margin-bottom: 10px;
        }

        .selected-item-details .whereused-section {
            min-height: 200px;
        }

        .selected-item-details .bom-section {
            min-height: 200px;
        }

        .selected-item-details .thumbnails-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, max-content));
            grid-gap: 20px;
            justify-content: flex-start;
            padding: 20px;
        }

        .selected-item-details .thumbnails-container .thumbnail-item {
            flex: 1;
            max-width: 300px;
            min-width: 300px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            display: flex;
            flex-flow: column;
            height: 260px;
            background-image: linear-gradient(to top, #f3e7e9 0%, #e3eeff 99%, #e3eeff 100%);
        }

        .selected-item-details .thumbnails-container .thumbnail-item .thumbnail-item-body {
            position: relative;
            height: 95px;
        }

        .selected-item-details .thumbnails-container .thumbnail-item .thumbnail-item-image {
            text-align: center;
        }

        .selected-item-details .thumbnails-container .thumbnail-item .thumbnail-item-image img {
            border-radius: 3px;
        }

        .selected-item-details .thumbnails-container .thumbnail-item .thumbnail-item-body .file-size {
            padding: 5px;
            background-color: #1ABC9C;
            color: #fff;
            border-radius: 3px;
        }

        .selected-item-details .thumbnails-container .thumbnail-item .thumbnail-item-body .file-versions {
            padding: 3px 10px;
            background-color: #ff5252;
            color: #fff;
            border-radius: 3px;
            min-width: 50px;
        }

        .selected-item-details .thumbnails-container .thumbnail-item .thumbnail-item-body h5 {
            white-space: -moz-pre-wrap !important;  /* Mozilla, since 1999 */
            white-space: -webkit-pre-wrap;          /* Chrome & Safari */
            white-space: -pre-wrap;                 /* Opera 4-6 */
            white-space: -o-pre-wrap;               /* Opera 7 */
            word-wrap: break-word;                  /* Internet Explorer 5.5+ */
            word-break: break-all;
            white-space: normal;
            font-size: 16px !important;
            font-weight: 400 !important;
        }

        .selected-item-details .thumbnails-container .thumbnail-item .thumbnail-item-body .file-current-version {
            padding: 3px 10px;
            background-color: #3B3E3E;
            color: #fff;
            border-radius: 3px;
            min-width: 50px;
        }

        .flex-container {
            display: flex;
        }

        .flex-item {
            flex: 1
        }


        .mt-10 {
            margin-top: 10px;
        }

        .mb-10 {
            margin-bottom: 10px;
        }

        .mt-20 {
            margin-top: 20px;
        }

        .ml-5 {
            margin-left: 5px;
        }

        .mr-5 {
            margin-left: 5px;
        }

        .ml-10 {
            margin-left: 10px;
        }

        .mr-10 {
            margin-left: 10px;
        }

        .no-whereused h4 {
            opacity: 0.5;
            font-weight: 400 !important;
        }

    </style>
    <div class="selected-item-details">
        <div class="row">
            <div class="info-section">
                <div class="section-heading">Information</div>

                <div class="col-sm-8">
                    <div class="item-details">
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Name</span> :
                            </div>
                            <div class="value col-xs-8 col-sm-9">
                                <span>{{basicVm.revisionedObject.name}}</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Number</span> :
                            </div>
                            <div class="value col-xs-8 col-sm-9">
                                <a href="" ui-sref="app.items.details({itemId: basicVm.revisionedObject.master.plmItem.latestRevision})"
                                   ng-click="$event.stopPropagation()">
                                    {{basicVm.revisionedObject.master.plmItem.itemNumber}}
                                </a>
                            </div>
                        </div>
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Version</span> :
                            </div>
                            <div class="value col-xs-8 col-sm-9">
                                <span>{{basicVm.revisionedObject.revision}}.{{basicVm.revisionedObject.version}}</span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Versions</span> :
                            </div>
                            <div class="value col-xs-8 col-sm-9">
                                <span>{{basicVm.fileVersion.file.versions}}</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Created By</span> :
                            </div>
                            <div class="value col-xs-8 col-sm-9">
                                <span>{{basicVm.revisionedObject.createdByObject.fullName}}</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Created Date</span> :
                            </div>
                            <div class="value col-xs-8 col-sm-9">
                                <span>{{basicVm.revisionedObject.createdDate}}</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Modified By</span> :
                            </div>
                            <div class="value col-xs-8 col-sm-9">
                                <span>{{basicVm.revisionedObject.modifiedByObject.fullName}}</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Modified Date</span> :
                            </div>
                            <div class="value col-xs-8 col-sm-9">
                                <span>{{basicVm.revisionedObject.modifiedDate}}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4">
                    <img class="thumbnail" ng-src="api/pdm/core/assemblies/{{basicVm.revisionedObject.id}}/thumbnail" alt=""
                         ng-if="basicVm.selectedItem != null && (basicVm.selectedItem['@type'] === 'AssemblyDTO' || basicVm.selectedItem.objectType === 'PDM_ASSEMBLY')"
                         onerror="this.src='app/assets/images/cassini-logo-greyscale-text.png';">
                    <img class="thumbnail" ng-src="api/pdm/core/parts/{{basicVm.selectedItem.part.id}}/thumbnail" alt=""
                         ng-if="basicVm.selectedItem != null && basicVm.selectedItem['@type'] === 'PartDTO'"
                         onerror="this.src='app/assets/images/cassini-logo-greyscale-text.png';">
                </div>
            </div>
        </div>
        <div class="whereused-section">
            <div class="section-heading">Where Used</div>
            <div ng-if="basicVm.whereUsed.length == 0" class="no-whereused">
                <h4>No items</h4>
            </div>
            <div class="thumbnails-container">
                <div class="thumbnail-item" ng-repeat="whereUsed in basicVm.whereUsed">
                    <div class="thumbnail-item-image file-thumbnail">
                        <img ng-src="{{basicVm.getThumbnailUrl(whereUsed.fileVersion)}}" alt="" width="200"
                             onerror="this.src='app/assets/images/cassini-logo-greyscale-text.png';">
                    </div>
                    <div class="thumbnail-item-body">
                        <h5 class="text-center">{{whereUsed.fileVersion.name}}</h5>
                        <div class="flex-container" style="position: absolute;bottom: 0px;left: 0;right: 0;font-size: 12px">
                            <div class="flex-item ml-10">
                                <span>File Size</span> <br> <span class="file-size">{{basicVm.fileSizeToString(whereUsed.fileVersion.size)}}</span>
                            </div>
                            <div class="flex-item text-center">
                                <span>Version</span> <br> <span class="file-current-version">{{whereUsed.fileVersion.version}}</span>
                            </div>
                            <div class="flex-item text-right mr-10">
                                <span>Versions</span> <br> <span class="file-versions">{{whereUsed.fileVersion.file.versions}}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="bom-section" ng-if="basicVm.selectedItem['@type'] === 'AssemblyDTO' || basicVm.selectedItem.objectType === 'PDM_ASSEMBLY'">
            <div class="section-heading">Children</div>
            <div class="thumbnails-container">
                <div class="thumbnail-item" ng-repeat="child in basicVm.children">
                    <div class="thumbnail-item-image file-thumbnail">
                        <img ng-src="{{basicVm.getThumbnailUrl(child.fileVersion)}}" alt="" width="200"
                             onerror="this.src='app/assets/images/cassini-logo-greyscale-text.png';">
                    </div>
                    <div class="thumbnail-item-body">
                        <h5 class="text-center">{{child.fileVersion.name}}</h5>
                        <div class="flex-container" style="position: absolute;bottom: 0px;left: 0;right: 0;font-size: 12px">
                            <div class="flex-item ml-10">
                                <span>File Size</span> <br> <span class="file-size">{{basicVm.fileSizeToString(child.fileVersion.size)}}</span>
                            </div>
                            <div class="flex-item text-center">
                                <span>Version</span> <br> <span class="file-current-version">{{child.fileVersion.version}}</span>
                            </div>
                            <div class="flex-item text-right mr-10">
                                <span>Versions</span> <br> <span class="file-versions">{{child.fileVersion.file.versions}}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
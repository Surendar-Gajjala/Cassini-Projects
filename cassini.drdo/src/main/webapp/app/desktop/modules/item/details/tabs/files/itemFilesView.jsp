<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style scoped>

    table.highlight-row tr.dropzone-row:hover, table.highlight-row tr.dropzone-row:hover td {
        background-color: inherit !important;
        background-image: none !important;
        color: rgb(99, 110, 123) !important;
    }

</style>
<div ng-show="itemFilesVm.showDropzone == false" id="itemFiles">
    <%--<div id="filesTab" style="font-style: italic; text-align: center;margin-bottom: 20px;">
        Drag and drop files on to the table or <a href="" ng-click="itemFilesVm.selectFiles()">Click here to add
        files</a>
    </div>--%>
    <div class="responsive-table" style="padding: 10px;" id="itemFilesTableContainer">
        <div style="font-style: italic; text-align: center;">
            Drag and drop files on to the table or <a href="" ng-click="itemFilesVm.selectFiles()">Click here to add
            files</a>
        </div>
        <table id="itemFilesTable" class="table table-striped highlight-row">
            <thead>
            <tr>
                <th>Name</th>
                <th>Size</th>
                <th style="text-align: center">Version</th>
                <th>Created Date</th>
                <th>Created By</th>
                <th>Modified Date</th>
                <th>Modified By</th>
                <th style="text-align: center">Locked</th>
                <th>Locked By</th>
                <th style="text-align: center">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-show="itemFilesVm.showFileDropzone == true" class="dropzone-row">
                <td colspan="14">
                    <div style="height: 300px; border: slategray dashed 2px; overflow-y: auto">
                        <div class="table table-striped table-bordered" class="files previews" id="previews">
                            <div id="template" class="file-row">
                                <!-- This is used as the file preview template -->
                                <div style="width:10px">
                                    <span class="preview"><img data-dz-thumbnail/></span>
                                </div>
                                <div>
                                    <p class="name" data-dz-name></p>
                                    <strong class="error text-danger" data-dz-errormessage></strong>
                                </div>
                                <div>
                                    <p class="size" data-dz-size></p>

                                    <div class="progress progress-striped active" role="progressbar"
                                         aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"
                                         style="margin-bottom: 0">
                                        <div class="progress-bar progress-bar-success" style="width:0%;"
                                             data-dz-uploadprogress></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-if="itemFilesVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span>Loading Files...</span>
                </td>
            </tr>
            <tr ng-if="itemFilesVm.showFileDropzone == false && itemFilesVm.loading == false && itemFilesVm.files.length == 0">
                <td colspan="10">No Files.</td>
            </tr>
            <tr ng-if="itemFilesVm.showFileDropzone == false && itemFilesVm.files.length > 0"
                ng-repeat="file in itemFilesVm.files">
                <td>
                    <a href="" style="cursor: pointer;" title="{{itemFilesVm.downloadFileTitle}}"
                       ng-click="itemFilesVm.downloadFile(file)">
                        <span ng-bind-html="file.name | highlightText: freeTextQuerys"></span>
                    </a>
                </td>
                <td>{{file.size.toFileSize()}}</td>
                <td style="text-align: center">{{file.version}}</td>
                <td>{{file.createdDate}}</td>
                <td>{{file.createdByObject.firstName}}</td>
                <td>{{file.modifiedDate}}</td>
                <td>{{file.modifiedByObject.firstName}}</td>
                <td style="text-align: center">
                    <input type="checkbox" ng-model="file.locked"
                           ng-change="itemFilesVm.lockFile(file, file.locked)">
                </td>
                <td>{{file.lockedByObject.firstName}}</td>
                <td style="text-align: center">
                    <div class="btn-group" style="margin-bottom: 0px;">
                        <button title="Show file history" class="btn btn-xs btn-warning"
                                ng-click="itemFilesVm.showFileHistory(file)">
                            <i class="fa fa-copy"></i>
                        </button>
                        <%--<button title="File Download History"
                                class="btn btn-xs btn-info"
                                ng-click="itemFilesVm.showFileDownloadHistory(file)">
                            <i class="fa fa-history"></i>
                        </button>--%>
                        <button title="delete file"
                                class="btn btn-xs btn-danger"
                                ng-disabled="file.locked && file.lockedBy.id != loginPersonDetails.person.id"
                                ng-click="itemFilesVm.deleteFile(file)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

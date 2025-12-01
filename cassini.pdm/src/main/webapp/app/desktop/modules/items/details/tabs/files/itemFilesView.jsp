<style scoped>

</style>
<div ng-show="itemFilesVm.showDropzone == true">
    <%@include file="dropzoneComponent.jsp" %>
</div>
<div ng-show="itemFilesVm.showDropzone == false">
    <div class="responsive-table">
        <table id="itemFilesTable" class="table table-striped">
            <thead>
            <tr>
                <th>File Name</th>
                <th>File Size</th>
                <th style="text-align: center">Version</th>
                <th>Date Created</th>
                <th>Created By</th>
                <th>Last Modified</th>
                <th>Modified By</th>
                <th style="text-align: center">Is Locked</th>
                <th>Locked By</th>
                <th style="text-align: center">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="itemFilesVm.loading == true">
                <%-- <td colspan="10"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">Loading files...</td>--%>
            </tr>
            <tr ng-if="itemFilesVm.loading == false && itemFilesVm.files.length == 0">
                <td colspan="10">No files</td>
            </tr>
            <tr ng-if="itemFilesVm.files.length > 0"
                ng-repeat="file in itemFilesVm.files">
                <td>
                    {{file.name}}
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
                    <comments-button object-type="FILE" object-id="file.id"></comments-button>
                    <button title="Show file history" class="btn btn-xs btn-warning"
                            ng-click="itemFilesVm.showFileHistory(file.id)">
                        <i class="fa fa-copy"></i>
                    </button>
                    <button title="Download file"
                            class="btn btn-xs btn-primary"
                            ng-click="itemFilesVm.downloadFile(file)">
                        <i class="fa fa-download"></i>
                    </button>
                    <button title="delete file"
                            class="btn btn-xs btn-danger"
                            ng-click="itemFilesVm.deleteFile(file)">
                        <i class="fa fa-trash"></i>
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

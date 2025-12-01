<div ng-show="runCaseFilesVm.showDropzone == true">
    <%@include file="dropzoneComponent.jsp" %>
</div>
<div ng-show="runCaseFilesVm.showDropzone == false" id="caseFiles">
    <div class='responsive-table' style="padding: 10px">
        <div style="font-style: italic; text-align: center;margin-bottom: 20px;">
            Drag and drop files on to the table or <a href="" ng-click="runCaseFilesVm.selectFile()">(Click
            here to add files)</a>
        </div>
        <table id="itemFilesTable" class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th>File Name</th>
                <th>File Size</th>
                <th style="text-align: center">Version</th>
                <th>Created By</th>
                <th>Created Date</th>
                <th>Modified Date</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>

            <tr ng-if="runCaseFiles.length == 0">
                <td colspan="10"><span>No files</span></td>
            </tr>
            <tr ng-if="runCaseFiles.length > 0"
                ng-repeat="file in runCaseFiles">
                <td><a href="" ng-click="runCaseFilesVm.downloadFile(file)">{{file.name}}</a></td>
                <td>{{runDetailsVm.fileSizeToString(file.size)}}</td>
                <td style="text-align: center">{{file.version}}</td>
                <td>{{file.createdByObject.fullName}}</td>
                <td>{{file.createdDate}}</td>
                <td>{{file.modifiedDate}}</td>
                <td>
                    <button title="Delete this file" class="btn btn-xs btn-danger"
                            ng-click="runCaseFilesVm.deleteFile(file)">
                        <i class="fa fa-trash"></i>
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
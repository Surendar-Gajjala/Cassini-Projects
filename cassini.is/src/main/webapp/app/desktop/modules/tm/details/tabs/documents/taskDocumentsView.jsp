<div class="row">
    <div class="col-sm-12">
        <div class="responsive-table">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="width: 200px;text-align: center">File Name</th>
                    <th style="width: 200px;text-align: center">File Size</th>
                    <th style="width: 200px;text-align: center">Version</th>
                    <th style="width: 200px;text-align: center">Modified Date</th>
                    <th class="actions-col">Actions</th>

                </tr>
                </thead>
                <tbody>
                <tr ng-if="taskDocVm.taskDocuments.length == 0">
                    <td colspan="6">No Documents are available to view</td>
                </tr>
                <tr ng-repeat="document in taskDocVm.taskDocuments">
                    <td style="width: 200px;text-align: center">
                        <a href="" ng-click="taskDocVm.downloadDocument(document)">{{document.refItemObject.name}}</a>

                    </td>
                    <td style="width: 200px;text-align: center">
                        {{taskDocVm.fileSizeToString(document.refItemObject.size)}}
                    </td>
                    <td style="width: 200px;text-align: center">{{document.refItemObject.version}}</td>
                    <td style="width: 200px;text-align: center">{{document.refItemObject.modifiedDate}}</td>
                    <td class="actions-col">
                        <button class="btn btn-xs btn-danger"
                                ng-disabled="selectedProject.locked == true || hasPermission('permission.tasks.deleteItem') == false"
                                title="Delete" ng-click="taskDocVm.deleteTaskDocument(document)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

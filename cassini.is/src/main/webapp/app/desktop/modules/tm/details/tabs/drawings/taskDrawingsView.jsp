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
                <tr ng-if="taskDrawingVm.taskDrawings.length == 0">
                    <td colspan="6">No Drawings are available to view</td>
                </tr>
                <tr ng-repeat="drawing in taskDrawingVm.taskDrawings">
                    <td style="width: 200px;cursor: pointer;text-align: center">
                        <a href="" ng-click="taskDrawingVm.downloadDocument(drawing)">{{drawing.refItemObject.name}}</a>

                    </td>
                    <td style="width: 200px;text-align: center">
                        {{taskDrawingVm.fileSizeToString(drawing.refItemObject.size)}}
                    </td>
                    <td style="width: 200px;text-align: center">{{drawing.refItemObject.version}}</td>
                    <td style="width: 200px;text-align: center">{{drawing.refItemObject.modifiedDate}}</td>
                    <td class="actions-col">
                        <button class="btn btn-xs btn-danger"
                                ng-disabled="selectedProject.locked == true || hasPermission('permission.tasks.deleteItem') == false"
                                title="Delete" ng-click="taskDrawingVm.deleteTaskDrawing(drawing)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<style>
    .left_div {
        width: 200px;
        border: 1px solid #ddd;
        float: left;
        padding: 1%;
        position: absolute;
        top: 5px;
        bottom: 5px;
        left: 5px;
    }

    .right_div {
        border: 1px solid #ddd;
        float: left;
        padding: 1%;
        position: absolute;
        left: 210px;
        bottom: 5px;
        top: 5px;
        right: 5px;
    }

    td {
        padding: 5px !important;
    }

</style>
<div class="modal-body">
    <div class="main" style="padding: 1px; max-height: 500px;min-height: 500px;">
        <div class="left_div">
            <div>
                <ul id="docFoldersTree" class="easyui-tree"></ul>
            </div>
        </div>
        <div class="right_div">
            <div class="col-md-12" style="padding:0px;max-height: 300px;overflow: auto;">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th style="vertical-align: middle;padding-left: 20px;">
                            <div class="ckbox ckbox-default" style="display: inline-block; margin-left: 7px">
                                <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                       ng-model="drawingVm.selectedAll"
                                       ng-click="drawingVm.checkAll()">
                                <label for="item{{$index}}" class="item-selection-checkbox"></label>
                            </div>
                        </th>
                        <th style="vertical-align: middle;text-align: center">File Name</th>
                        <th style="vertical-align: middle;text-align: center">File Size</th>
                        <th style="vertical-align: middle;text-align: center">Version</th>
                        <th style="vertical-align: middle;text-align: center">Locked By</th>

                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="drawingVm.drawings.length == 0">
                        <td colspan="11">No Drawings are available to view</td>
                    </tr>
                    <tr ng-repeat="drawing in drawingVm.drawings">
                        <th style="width: 80px; text-align: center; text-align: center">
                            <div class="ckbox ckbox-default" style="display: inline-block;">
                                <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                       ng-click="drawingVm.select(drawing)"
                                       ng-model="drawing.selected">
                                <label for="item{{$index}}" class="item-selection-checkbox"></label>
                            </div>
                        </th>
                        <td style="vertical-align: middle;text-align: center">
                            {{drawing.name}}
                        </td>
                        <td style="vertical-align: middle;text-align: center">
                            {{drawing.size}}
                        </td>
                        <td style="vertical-align: middle;text-align: center">
                            {{drawing.version}}
                        </td>
                        <td style="vertical-align: middle;text-align: center"><span ng-show='document.locked'>
                            {{drawing.lockedByObject.fullName}}
                            </span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


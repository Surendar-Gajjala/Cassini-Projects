<style scoped>
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
<div class="modal-body" style="padding: 1px; max-height: 500px;min-height: 500px;">
    <div class="main">
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
                        <th style="vertical-align: middle;text-align: center;">
                            <%--<input type="checkbox" ng-model="documentVm.selectedAll"
                                   ng-click="documentVm.checkAll()"
                                   style="margin-left: 1px; margin-top: 10px;margin-bottom: 10px" width="80px">--%>
                                <div class="ckbox ckbox-default" style="display: inline-block;">
                                    <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                           ng-model="documentVm.selectedAll"
                                           ng-click="documentVm.checkAll()">
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
                    <tr ng-if="documentVm.documents.length == 0">
                        <td colspan="11">No Documents are available to view</td>
                    </tr>
                    <tr ng-repeat="document in documentVm.documents">
                        <th style="width: 80px; text-align: center">
                            <div class="ckbox ckbox-default" style="display: inline-block;">
                                <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                       ng-click="documentVm.select(document)"
                                       ng-model="document.selected">
                                <label for="item{{$index}}" class="item-selection-checkbox"></label>
                            </div>
                        </th>
                        <td style="vertical-align: middle;text-align: center">
                            {{document.name}}
                        </td>
                        <td style="vertical-align: middle;text-align: center">
                            {{document.size}}
                        </td>
                        <td style="vertical-align: middle; text-align: center">
                            {{document.version}}
                        </td>
                        <td style="vertical-align: middle;text-align: center"><span ng-show='document.locked'>
                            {{document.lockedByObject.firstName}}
                            </span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<div style="padding: 20px; height: auto;">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="width: 80px; text-align: center"></th>
                    <th>Widget Name</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="widget in selectWidgetVm.widgets | filter:{permission:true}">
                    <th style="width: 80px; text-align: center">
                        <input name="widget" type="checkbox" ng-model="widget.checked"
                               ng-click="selectWidgetVm.selectCheck(widget)">
                    </th>
                    <td style="vertical-align: middle;">
                        {{widget.name}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>

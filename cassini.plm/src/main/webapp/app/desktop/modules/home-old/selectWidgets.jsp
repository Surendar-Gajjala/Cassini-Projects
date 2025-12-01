<div style="padding: 20px; height: auto;">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px; text-align: center"></th>
                    <th translate>WIDGET_NAME</th>
                </tr>
                </thead>
                <tbody>
                <tr id="{{$index}}" ng-repeat="widget in selectWidgetVm.widgets  | orderBy : 'priority'" dragwidget
                    widgets="selectWidgetVm.widgets" update-widget-seq="selectWidgetVm.updateWidgetSeq">
                    <td style="width: 30px; text-align: center">
                        <input name="widget" type="checkbox" ng-model="widget.checked"
                               ng-click="selectWidgetVm.selectCheck(widget)">
                    </td>
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

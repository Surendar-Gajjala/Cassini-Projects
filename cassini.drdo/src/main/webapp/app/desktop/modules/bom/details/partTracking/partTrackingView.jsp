<div style="padding: 20px; height: auto;">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="width: 80px; text-align: center"></th>
                    <th>Part Tracking Name</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="partTracking in selectPartTrackVm.partTrackings">
                    <th style="width: 80px; text-align: center">
                        <input class="form-control" name="widget" type="radio"
                               ng-model="selectPartTrackVm.selectedPartTracking"
                               ng-value="partTracking">
                    </th>
                    <td style="vertical-align: middle;">
                        {{partTracking.name}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>
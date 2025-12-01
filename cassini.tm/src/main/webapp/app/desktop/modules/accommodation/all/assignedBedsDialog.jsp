<div class="view-container">
    <div class="view-toolbar">
        <h3 style="margin-top: 5px;">Assigned Beds</h3>
    </div>

    <div class="modal-body" style="padding: 20px; max-height: 500px;">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width: 200px;">Bed</th>
                <th style="width: 200px;">Person</th>
            </tr>

            </thead>
            <tbody>
            <tr ng-repeat="bed in assignedBeds">
                <td>{{bed.name}}</td>
                <td>{{bed.assignedToObject.firstName}}, {{bed.assignedToObject.lastName}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default" ng-click="onOk()">Ok</button>
        </div>
    </div>
</div>



















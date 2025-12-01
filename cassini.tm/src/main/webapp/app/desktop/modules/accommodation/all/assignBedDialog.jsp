<div class="view-container">
    <div class="view-toolbar">
        <h3 style="margin-top: 5px;">Assign Bed</h3>
    </div>

    <div class="modal-body" style="padding: 20px; max-height: 500px;">
        <form class="form-horizontal">
            <div>
                <span style="margin-left: 150px" class="text-danger text-center">{{}}</span>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label">Bed Name:</label>

                <div class="col-sm-7">
                    <ui-select class="required-field" ng-model="selectedBed" theme="bootstrap">
                        <ui-select-match placeholder="Select Bed">{{$select.selected}}</ui-select-match>
                        <ui-select-choices repeat="bed in beds | filter: $select.search">
                            <div ng-bind="bed | highlight: $select.name.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label">Person Name:</label>

                <div class="col-sm-7">
                    <ui-select class="required-field" ng-model="selectedPerson" theme="bootstrap">
                        <ui-select-match placeholder="Select Person">{{$select.selected.fullName}}</ui-select-match>
                        <ui-select-choices repeat=" person in persons | filter: $select.search">
                            <div ng-bind="person.fullName| highlight: $select.name.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default" ng-click="onCancel()">Cancel</button>
            <button type="button" class="btn btn-sm btn-success" ng-click="onOk()">Assign</button>
        </div>
    </div>
</div>



















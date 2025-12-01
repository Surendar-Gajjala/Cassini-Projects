<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
    <h3 style="margin-top:0px; margin-bottom:0px;">New Process</h3>
</div>
<div class="modal-body">
    <div class="row">
        <div class="col-sm-8 col-sm-offset-2">
            <form class="form" name="newProcess" novalidate angular-validator>
                <div class="mb15">
                    <div class="form-group">
                        <label class="control-label">Name</label>
                        <input type="text" name="jobtitle" ng-model="process.name" class="form-control"
                               required-message="constants.REQUIRED" validate-on="dirty" required>
                    </div>
                </div>
                <div class="mb15">
                    <div class="form-group">
                        <label class="control-label">Description</label>
                        <input type="text" name="jobtitle" ng-model="process.description" class="form-control"
                               required-message="constants.REQUIRED" validate-on="dirty" required>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal-footer" style="background-color: #F9F9F9;text-align: left;">
    <div class="row">
        <div class="col-md-12" style="text-align: right">
            <button type="button" class="btn btn-default" style="min-width: 80px"
                    ng-click="cancel()">Cancel
            </button>
            <button class="btn btn-primary" style="min-width: 80px"
                    ng-click="create(newProcess)">Create
            </button>
        </div>
    </div>
</div>
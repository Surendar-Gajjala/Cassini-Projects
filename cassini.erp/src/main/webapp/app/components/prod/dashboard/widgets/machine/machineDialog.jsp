<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
    <h3 style="margin-top:0px; margin-bottom:0px;">New Machine</h3>
</div>
<div class="modal-body">
    <div class="row">
        <div class="col-sm-8 col-sm-offset-2">
            <form class="form" name="newMachine" novalidate angular-validator>
                <div class="mb15">
                    <div class="form-group">
                        <label class="control-label">Name</label>
                        <input type="text" name="jobtitle" ng-model="machine.name" class="form-control"
                               required-message="constants.REQUIRED" validate-on="dirty" required>
                    </div>
                </div>
                <div class="mb15">
                    <div class="form-group">
                        <label class="control-label">Description</label>
                        <input type="text" name="jobtitle" ng-model="machine.description" class="form-control"
                               required-message="constants.REQUIRED" validate-on="dirty" required>
                    </div>
                </div>
                <div class="mb15">
                    <div class="form-group">
                        <label class="control-label">Workcenter</label>
                        <ui-select ng-model="machine.workcenter" theme="bootstrap" style="width: 100%;"
                                   title="Choose a Workcenter">
                            <ui-select-match placeholder="Choose a Workcenter">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices repeat="workcenter in workcenterList | filter: $select.search">
                                <div ng-bind-html="workcenter.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                        <%--    <input type="text" name="workcenterdummy" ng-model="machine.workcenter.id" class="hide form-control"
                                   required-message="constants.REQUIRED"
                                   validator = "workcenterValidator(machine.workcenter)"
                                   validate-on="dirty"
                                   required>--%>
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
                    ng-click="create(newMachine)">Create
            </button>
        </div>
    </div>
</div>
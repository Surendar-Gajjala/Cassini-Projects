<style>
    .view-content .table-footer > div {
        display: table-row;
        line-height: 31px;
    }

    .ui-select-bootstrap > .ui-select-match > .btn {
        text-align: left !important;
        line-height: 16px;
    }

</style>
<div class="view-container">
    <div class="view-toolbar">

    </div>

    <div class="view-content">
        <div class="row">
            <div class="col-md-6 col-md-offset-3">
                <form>
                    <div class="form-group form-group-sm">
                        <label for="Title">Title</label>
                        <input type="text" class="form-control" id="Title" ng-model="newIssueVm.issue.title"
                               placeholder="Title">
                    </div>
                    <div class="form-group form-group-sm">
                        <label for="Type">Type</label>
                        <input type="text" class="form-control" id="Type" ng-model="newIssueVm.issue.type"
                               placeholder="Type">
                    </div>
                    <div class="form-group form-group-sm">
                        <label for="Description">Details</label>
                        <input type="text" class="form-control" id="Description" ng-model="newIssueVm.issue.description"
                               placeholder="Description">
                    </div>
                    <div class="form-group form-group-sm">
                        <label for="Priority">Priority</label>
                        <input type="text" class="form-control" ng-model="newIssueVm.issue.priority" id="Priority"
                               placeholder="Priority">
                    </div>
                    <div class="form-group form-group-sm">
                        <label for="AssignTo">AssignTo</label>
                        <input type="text" class="form-control" ng-model="newIssueVm.issue.assignedTo" id="AssignTo"
                               placeholder="AssignTo">
                    </div>

                    <div class="form-group form-group-sm">
                        <button type="button" class="btn btn-sm btn-default"
                                ng-click="newIssueVm.cancel()">Cancel
                        </button>
                        <button type="button" class="btn btn-sm btn-success"
                                ng-click="newIssueVm.create()">Create
                        </button>
                    </div>

                </form>
            </div>
        </div>

    </div>
</div>

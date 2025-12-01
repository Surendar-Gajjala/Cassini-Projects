<div class="modal-header">
    <h4>Create Articles <i class="fa fa-plus-circle pull-right" ng-click="newArticleVm.addInstance()"
                           title="Add Instance" style="font-size:23px;color: green;cursor: pointer;"></i></h4>
</div>
<div class="modal-body" style="padding: 20px; max-height: 500px;">
    <div style="display: inline-flex" ng-repeat="instance in instances track by $index">
        <p style="margin-top:7px;">{{$index+1}}.</p>
        <input id="count" class="form-control" style="margin-left: 20px;margin-bottom: 10px;width: 200px" maxlength="2"
               ng-enter="newArticleVm.addInstance()"
               ng-model="instance.name" type="text">
    </div>
    <p ng-show="newArticleVm.errorMessage != null" style="color: darkred;font-weight: 600;font-size: 14px;">
        {{newArticleVm.errorMessage}}</p>
</div>
<div class="modal-footer">
    <div class="row">
        <div class="btn-group" role="group" aria-label="Basic example">
            <button style="margin-right: 10px" type="submit" ng-click="newArticleVm.cancel()"
                    class="btn btn-danger">Cancel
            </button>
            <button type="submit" ng-click="newArticleVm.generate()" class="btn btn-success">OK
            </button>
        </div>
    </div>
</div>
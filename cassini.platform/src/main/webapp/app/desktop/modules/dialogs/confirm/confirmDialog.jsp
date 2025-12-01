<div class="modal-header">
    <h4 style="margin: 0">{{confirmVm.title}}</h4>
</div>
<div class="modal-body">
    <br>
    <div style="font-size: 18px;">{{confirmVm.message}}</div>
    <br>
</div>
<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm" ng-class="confirmVm.cancelButtonClass"
                    ng-click="confirmVm.onCancel()">{{confirmVm.cancelButtonText}}</button>
            <button type="button" class="btn btn-sm" ng-class="confirmVm.okButtonClass"
                    ng-click="confirmVm.onOk()">{{confirmVm.okButtonText}}</button>
        </div>
    </div>
</div>
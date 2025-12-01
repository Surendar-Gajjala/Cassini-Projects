<div class="view-container">
    <div class="view-toolbar">
        <button class="btn btn-sm btn-success" ng-click="docDetailsVm.back()">Back</button>

    </div>

    <div class="view-content no-padding">
        <div ng-if="docDetailsVm.loading == true" style="padding: 30px">
            <br/>
                <span style="font-size: 15px;">
                    <img src="app/assets/images/loaders/loader6.gif" class="mr5">Loading document details...
                </span>
            <br/>
        </div>
        <div class="row row-eq-height" style="margin: 0;" ng-if="docDetailsVm.loading == false">
            <div class="col-sm-12">
                <h3>{{docDetailsVm.document.name}}</h3>
            </div>

        </div>
    </div>
</div>

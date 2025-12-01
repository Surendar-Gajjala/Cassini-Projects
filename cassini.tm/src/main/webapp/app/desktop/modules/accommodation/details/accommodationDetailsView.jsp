<div class="view-container">
    <div class="view-toolbar">
        <button class="btn btn-sm btn-success min-width" ng-click="accommodationDetailsVm.back();">Back</button>
        <button class="btn btn-sm btn-info min-width" ng-click="accommodationDetailsVm.updateAccommodation();">Save
        </button>
    </div>

    <div class="view-content">
        <div class="item-details" style="padding: 30px">
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Name :</span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a href="#" editable-text="accommodationDetailsVm.accommodation.name">{{accommodationDetailsVm.accommodation.name}}</a>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Description : </span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a href="#" editable-textarea="accommodationDetailsVm.accommodation.description" e-rows="4"
                       e-cols="60">{{accommodationDetailsVm.accommodation.description}}</a>
                </div>
            </div>
        </div>
    </div>
</div>
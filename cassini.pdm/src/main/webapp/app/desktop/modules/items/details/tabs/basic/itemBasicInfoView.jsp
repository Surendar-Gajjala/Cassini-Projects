<div ng-if="itemBasicVm.loading == true" style="padding: 30px;">

    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading item details...
    </span>
    <br/>
</div>
<div ng-if="itemBasicVm.loading == false">

    <div class="row row-eq-height">
        <div class="col-sm-12">
            <div class="item-details" style="padding: 30px">
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Item Type: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.itemType.name}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Item Number: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.itemNumber}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Description: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="" e-style="width:250px" onaftersave="itemBasicVm.updateItem()"
                           editable-textarea="itemBasicVm.item.description">{{itemBasicVm.item.description}}</a>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Revision: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.revision}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Units: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.units}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Created On: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.createdDate}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Created By: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.createdByPerson.firstName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Modified On: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.modifiedDate}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Modified By: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.modifiedByPerson.firstName}}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
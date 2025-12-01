<div style="overflow-y: auto; overflow-x: hidden; padding: 10px 20px;">
    <div class="row" style="margin: 0;">
        <div>
            <form class="form-horizontal">
                <p><span style="font-size: 18px;font-weight: 600;">UPN </span> :
                    {{verifyPartVm.inwardItemInstance.item.upnNumber}}</p>

                <p><span style="font-size: 18px;font-weight: 600;">Location </span> :
                    {{verifyPartVm.inwardItemInstance.item.storagePath}}</p>

                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span>UPN <span class="asterisk">*</span> </span>: </label>

                    <div class="col-sm-7">
                        <input class="form-control" placeholder="Scan UPN Barcode"
                               ng-model="verifyPartVm.upnNumber"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span>Storage <span class="asterisk">*</span> </span>: </label>

                    <div class="col-sm-7">
                        <input class="form-control" ng-model="verifyPartVm.storageName"
                               placeholder="Scan Storage Barcode"/>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
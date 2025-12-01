<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
    <h3 style="margin-top:0px; margin-bottom:0px;">New Product Category</h3>
</div>
<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Name <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="type" placeholder="Enter Store Name"
                                   ng-model="store.storeName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Description : </label>

                        <div class="col-sm-7">
                        <textarea name="description" rows="5" class="form-control" style="resize: none"
                                  placeholder="Enter Description"
                                  ng-model="store.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Location <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="type" placeholder="Enter Location"
                                   ng-model="store.locationName">
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newStoreVm.storeRequiredAttributes"></attributes-view>
                    <br>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newStoreVm.storeAttributes"></attributes-view>
                </form>
                <br>
                <br>
            </div>
        </div>
    </div>
</div>
<div class="modal-footer" style="background-color: #F9F9F9;text-align: left;">
    <div class="row">
        <div class="col-md-8" style="text-align: right">
            <button type="button" class="btn btn-default" style="min-width: 80px"
                    ng-click="cancel()">Cancel</button>
            <button class="btn btn-primary" style="min-width: 80px"
                    ng-click="ok()" >Create</button>
        </div>
    </div>
</div>


<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>

                <form class="form-horizontal" ng-if="newStoreVm.storeType == 'Stores'">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Name <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="type" placeholder="Enter Store Name"
                                   ng-model="newStoreVm.topStore.storeName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Description : </label>

                        <div class="col-sm-7">
                        <textarea name="description" rows="5" class="form-control" style="resize: none"
                                  placeholder="Enter Description"
                                  ng-model="newStoreVm.topStore.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Location <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="type" placeholder="Enter Location"
                                   ng-model="newStoreVm.topStore.locationName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Import Store Items: </label>

                        <div class="col-sm-7">
                            <input class="form-control" id="file"
                                   type="file"/>
                        </div>
                    </div>

                    <div class="col-sm-7 col-sm-offset-4" ng-click="newStoreVm.downloadFileFormat()">
                        <a style="cursor: pointer">Click here to download import file format</a>
                    </div>

                    <div>
                        <form class="form-horizontal">
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="newStoreVm.requiredAttributes"></attributes-view>
                            <br>
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="newStoreVm.attributes"></attributes-view>
                            <br>
                            <br>
                        </form>
                    </div>

                </form>
                <br>
                <br>
            </div>
        </div>
    </div>
</div>

